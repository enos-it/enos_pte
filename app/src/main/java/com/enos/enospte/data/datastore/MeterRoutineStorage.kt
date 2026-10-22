package com.enos.enospte.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private val Context.meterRoutineDataStore by preferencesDataStore(name = "meter_routine")

data class MeterRoutineDefinition(
    val id: String,
    val name: String,
    val meterIds: List<String>
)

data class MeterRoutineConfig(
    val routines: List<MeterRoutineDefinition> = emptyList(),
    val activeRoutineId: String? = null
)

class MeterRoutineStorage(private val context: Context) {

    private fun legacyKey(userId: String) = stringPreferencesKey("routine_$userId")
    private fun configKey(userId: String) = stringPreferencesKey("routine_config_$userId")

    private fun enc(value: String): String =
        URLEncoder.encode(value, StandardCharsets.UTF_8.toString())

    private fun dec(value: String): String =
        URLDecoder.decode(value, StandardCharsets.UTF_8.toString())

    /**
     * Format:
     * activeRoutineId||routine1##routine2##...
     *
     * routine format:
     * id|name|meter1,meter2,meter3
     *
     * id, name in meter ids are URL-encoded
     */
    private fun encodeConfig(config: MeterRoutineConfig): String {
        val active = config.activeRoutineId?.let { enc(it) } ?: ""

        val routinesPart = config.routines.joinToString("##") { routine ->
            val ids = routine.meterIds.joinToString(",") { enc(it) }
            listOf(
                enc(routine.id),
                enc(routine.name),
                ids
            ).joinToString("|")
        }

        return "$active||$routinesPart"
    }

    private fun parseConfig(raw: String?): MeterRoutineConfig? {
        if (raw.isNullOrBlank()) return null

        return runCatching {
            val parts = raw.split("||", limit = 2)
            val activeRaw = parts.getOrNull(0).orEmpty()
            val routinesRaw = parts.getOrNull(1).orEmpty()

            val routines = if (routinesRaw.isBlank()) {
                emptyList()
            } else {
                routinesRaw.split("##")
                    .mapNotNull { entry ->
                        if (entry.isBlank()) return@mapNotNull null

                        val fields = entry.split("|", limit = 3)
                        val id = fields.getOrNull(0)?.takeIf { it.isNotBlank() }?.let { dec(it) }
                        val name = fields.getOrNull(1)?.takeIf { it.isNotBlank() }?.let { dec(it) }
                        val meterIdsRaw = fields.getOrNull(2).orEmpty()

                        if (id == null || name == null) return@mapNotNull null

                        val meterIds = if (meterIdsRaw.isBlank()) {
                            emptyList()
                        } else {
                            meterIdsRaw.split(",")
                                .mapNotNull { token ->
                                    token.takeIf { it.isNotBlank() }?.let { dec(it) }
                                }
                        }

                        MeterRoutineDefinition(
                            id = id,
                            name = name,
                            meterIds = meterIds
                        )
                    }
            }

            MeterRoutineConfig(
                routines = routines,
                activeRoutineId = activeRaw.takeIf { it.isNotBlank() }?.let { dec(it) }
            )
        }.getOrNull()
    }

    private fun legacyToConfig(raw: String?): MeterRoutineConfig? {
        if (raw.isNullOrBlank()) return null

        val ids = raw.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        if (ids.isEmpty()) return null

        return MeterRoutineConfig(
            routines = listOf(
                MeterRoutineDefinition(
                    id = "default",
                    name = "Moja rutina",
                    meterIds = ids
                )
            ),
            activeRoutineId = "default"
        )
    }

    private fun normalize(config: MeterRoutineConfig): MeterRoutineConfig {
        val cleanedRoutines = config.routines
            .map { routine ->
                MeterRoutineDefinition(
                    id = routine.id.trim(),
                    name = routine.name.trim().ifBlank { "Rutina" },
                    meterIds = routine.meterIds
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .distinct()
                )
            }
            .filter { it.id.isNotBlank() }
            .distinctBy { it.id }

        val activeId = when {
            cleanedRoutines.isEmpty() -> null

            // null pomeni namensko izbran način "Vsi števci"
            config.activeRoutineId == null -> null

            cleanedRoutines.any { it.id == config.activeRoutineId } ->
                config.activeRoutineId

            // Če je shranjen ID neveljaven, izberemo prvo obstoječo rutino.
            else -> cleanedRoutines.first().id
        }

        return MeterRoutineConfig(
            routines = cleanedRoutines,
            activeRoutineId = activeId
        )
    }

    fun observeConfig(userId: String): Flow<MeterRoutineConfig> {
        val legacy = legacyKey(userId)
        val config = configKey(userId)

        return context.meterRoutineDataStore.data.map { prefs ->
            val parsedConfig = parseConfig(prefs[config])
            val migratedLegacy = legacyToConfig(prefs[legacy])
            normalize(parsedConfig ?: migratedLegacy ?: MeterRoutineConfig())
        }
    }

    fun observeActiveRoutine(userId: String): Flow<MeterRoutineDefinition?> =
        observeConfig(userId).map { config ->
            config.routines.firstOrNull { routine -> routine.id == config.activeRoutineId }
        }

    private suspend fun saveConfig(userId: String, config: MeterRoutineConfig) {
        val normalized = normalize(config)
        val configK = configKey(userId)
        val legacyK = legacyKey(userId)

        context.meterRoutineDataStore.edit { prefs ->
            prefs[configK] = encodeConfig(normalized)
            prefs.remove(legacyK)
        }
    }

    suspend fun saveRoutine(userId: String, routine: MeterRoutineDefinition) {
        val current = observeConfig(userId).first()
        val routines = current.routines.toMutableList()
        val index = routines.indexOfFirst { it.id == routine.id }

        if (index >= 0) {
            routines[index] = routine
        } else {
            routines.add(routine)
        }

        val next = current.copy(
            routines = routines,
            activeRoutineId = current.activeRoutineId ?: routine.id
        )

        saveConfig(userId, next)
    }

    suspend fun deleteRoutine(userId: String, routineId: String) {
        val current = observeConfig(userId).first()
        val remaining = current.routines.filterNot { it.id == routineId }

        val nextActive = when {
            remaining.isEmpty() -> null
            current.activeRoutineId == routineId -> remaining.first().id
            else -> current.activeRoutineId
        }

        saveConfig(
            userId = userId,
            config = current.copy(
                routines = remaining,
                activeRoutineId = nextActive
            )
        )
    }

    suspend fun setActiveRoutine(userId: String, routineId: String?) {
        val current = observeConfig(userId).first()

        val nextActive = when {
            routineId == null -> null
            current.routines.any { it.id == routineId } -> routineId
            else -> current.activeRoutineId
        }

        saveConfig(
            userId = userId,
            config = current.copy(activeRoutineId = nextActive)
        )
    }

}