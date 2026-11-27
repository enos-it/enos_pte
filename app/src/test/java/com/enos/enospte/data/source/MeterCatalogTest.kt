package com.enos.enospte.data.source

import com.enos.enospte.data.model.Site
import com.enos.enospte.data.model.Group
import com.enos.enospte.data.model.MeterName
import com.enos.enospte.data.model.MeterUnit
import com.enos.enospte.data.model.SubGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MeterCatalogTest {
    @Test
    fun getMeterFindsEveryMeterByItsGeneratedId() {
        MeterCatalog.getMeters().forEach { meter ->
            assertEquals(meter, MeterCatalog.getMeter(meter.id))
        }
    }

    @Test
    fun getMeterReturnsNullForUnknownId() {
        assertNull(MeterCatalog.getMeter("unknown"))
        assertNull(MeterCatalog.getMeter(""))
        assertNull(MeterCatalog.getMeter("bela_bosch_zagoni"))
    }

    @Test
    fun getMetersGeneratesIdsFromEachMetersFields() {
        val meters = MeterCatalog.getMeters()

        assertTrue(meters.any { it.site == Site.BELA })
        assertTrue(meters.any { it.site == Site.JESENICE })
        assertTrue(meters.any { it.subgroup == SubGroup.EMPTY })
        assertTrue(meters.any { it.subgroup != SubGroup.EMPTY })
        meters.forEach { meter ->
            val expected = listOf(meter.site.name, meter.group.name, meter.subgroup.label, meter.name.label)
                .joinToString("_") { it.lowercase() }
            assertEquals(expected, meter.id)
        }
        assertEquals(meters.size, meters.map { it.id }.distinct().size)
        assertEquals(meters.map { it.id }, MeterCatalog.getMeters().map { it.id })
    }

    @Test
    fun getMetersPreservesSpacesUnicodeAndEmptySubgroupsInIds() {
        val meters = MeterCatalog.getMeters()

        assertTrue(meters.any { it.id == "bela_kotli_bosch_števec zagonov" })
        assertTrue(meters.any { it.id == "bela_vodarna__tlak pred" })
        assertTrue(meters.any { it.id == "jesenice_kotli_vvk 1_plinski števec" })
    }

    @Test
    fun metersWithoutSubgroupsUseEmpty() {
        val meters = MeterCatalog.getMeters()
        val ungroupedMeters = meters.filter { it.subgroup == SubGroup.EMPTY }

        assertEquals(24, ungroupedMeters.size)
        assertTrue(ungroupedMeters.any { it.site == Site.BELA && it.group == Group.SPTE })
        assertTrue(ungroupedMeters.any { it.site == Site.BELA && it.group == Group.VODARNA })
        assertTrue(ungroupedMeters.any { it.site == Site.JESENICE && it.group == Group.VODARNA })
    }

    @Test
    fun catalogUsesEverySubgroupWithDistinctIdsAndLabels() {
        val subgroups = SubGroup.values().toList()

        assertEquals(subgroups.toSet(), MeterCatalog.getMeters().map { it.subgroup }.toSet())
        assertEquals(subgroups.size, subgroups.map { it.id }.distinct().size)
        assertEquals(subgroups.size, subgroups.map { it.label }.distinct().size)
        assertEquals("SPTE 7", SubGroup.SPTE7.label)
        assertEquals("VVK 2", SubGroup.VVK2.label)
    }

    @Test
    fun catalogUsesEveryMeterNameWithDistinctIdsAndLabels() {
        val names = MeterName.values().toList()

        assertEquals(92, names.size)
        assertEquals(names.toSet(), MeterCatalog.getMeters().map { it.name }.toSet())
        assertEquals((1..names.size).toList(), names.map { it.id })
        assertEquals(names.size, names.map { it.label }.distinct().size)
        assertTrue(names.any { it.label == "Toplotni Števec" })
        assertTrue(names.any { it.label == "Toplotni števec" })
        assertTrue(names.any { it.label == "T3-SPTE 7-Temp. mešanice" })
    }

    @Test
    fun catalogUsesEveryUnitWithDistinctIdsAndLabels() {
        val units = MeterUnit.values().toList()
        val meters = MeterCatalog.getMeters()

        assertEquals(units.toSet(), meters.map { it.unit }.toSet())
        assertEquals((0 until units.size).toList(), units.map { it.id })
        assertEquals(units.size, units.map { it.label }.distinct().size)
        assertEquals("", MeterUnit.EMPTY.label)
        assertEquals(5, meters.count { it.unit == MeterUnit.EMPTY })
        assertEquals("kWh", MeterUnit.KILOWATT_HOUR.label)
        assertEquals("m³/h", MeterUnit.CUBIC_METER_PER_HOUR.label)
        assertEquals("°C", MeterUnit.CELSIUS.label)
    }
}
