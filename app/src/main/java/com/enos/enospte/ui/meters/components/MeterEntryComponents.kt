package com.enos.enospte.ui.meters.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.enos.enospte.data.datastore.MeterRoutineDefinition
import com.enos.enospte.data.model.Meter
import com.enos.enospte.data.model.Reading

@Composable
fun MeterEntryHeader(
    enteredBy: String,
    isAdmin: Boolean,
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit,
    onAdminHistory: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "VNOS",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Prijavljen: $enteredBy",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onOpenSettings) {
                Text("Nastavitve")
            }

            Button(onClick = onLogout) {
                Text("Odjava")
            }
        }
    }

    if (isAdmin) {
        AssistChip(
            onClick = onAdminHistory,
            label = {
                Text("Aktivnost")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeterViewSelector(
    sites: List<String>,
    selectedSite: String,
    activeRoutineName: String?,
    routines: List<MeterRoutineDefinition>,
    routineDoneToday: Boolean,
    routineExpanded: Boolean,
    onRoutineExpandedChange: (Boolean) -> Unit,
    onSiteSelected: (String) -> Unit,
    onAllMetersSelected: () -> Unit,
    onRoutineSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Prikaz:",
            style = MaterialTheme.typography.labelLarge
        )

        if (
            activeRoutineName != null &&
            routineDoneToday
        ) {
            Text(
                text = "✅ Današnja rutina \"$activeRoutineName\" opravljena",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (sites.isNotEmpty()) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                sites.forEachIndexed { index, site ->
                    SegmentedButton(
                        selected = selectedSite == site,
                        onClick = {
                            onSiteSelected(site)
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index,
                            sites.size
                        )
                    ) {
                        Text(site)
                    }
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = routineExpanded,
            onExpandedChange = onRoutineExpandedChange
        ) {
            OutlinedTextField(
                value = activeRoutineName ?: "Vsi števci",
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Prikaz števcev")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = routineExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = routineExpanded,
                onDismissRequest = {
                    onRoutineExpandedChange(false)
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text("Vsi števci")
                    },
                    onClick = onAllMetersSelected
                )

                if (routines.isNotEmpty()) {
                    HorizontalDivider()
                }

                routines.forEach { routine ->
                    DropdownMenuItem(
                        text = {
                            Text(routine.name)
                        },
                        onClick = {
                            onRoutineSelected(routine.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMetersCard(
    title: String,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeterInputSection(
    selectedMeter: Meter,
    meters: List<Meter>,
    meterExpanded: Boolean,
    valueText: String,
    valueError: String?,
    isSaving: Boolean,
    focusRequester: FocusRequester,
    onMeterExpandedChange: (Boolean) -> Unit,
    onMeterSelected: (Meter) -> Unit,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    isValueValid: Boolean
) {
    ExposedDropdownMenuBox(
        expanded = meterExpanded,
        onExpandedChange = onMeterExpandedChange
    ) {
        OutlinedTextField(
            value = buildString {
                append(selectedMeter.name)

                if (selectedMeter.unit.isNotBlank()) {
                    append(" (${selectedMeter.unit})")
                }
            },
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Števec")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = meterExpanded
                )
            },
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                )
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = meterExpanded,
            onDismissRequest = {
                onMeterExpandedChange(false)
            }
        ) {
            meters.forEach { meter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            if (meter.unit.isNotBlank()) {
                                "${meter.name} (${meter.unit})"
                            } else {
                                meter.name
                            }
                        )
                    },
                    onClick = {
                        onMeterSelected(meter)
                    }
                )
            }
        }
    }

    OutlinedTextField(
        value = valueText,
        onValueChange = onValueChange,
        label = {
            Text("Vrednost")
        },
        singleLine = true,
        isError = valueError != null,
        supportingText = {
            if (valueError != null) {
                Text(
                    text = valueError,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    if (selectedMeter.decimals == 0) {
                        "Celotno število"
                    } else {
                        "1 decimalka (npr. 1234.5)"
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSave()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )

    Button(
        onClick = onSave,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isSaving && isValueValid
    ) {
        Text(
            if (isSaving) {
                "Shranjujem…"
            } else {
                "Shrani"
            }
        )
    }
}

@Composable
fun ColumnScope.RecentReadingsSection(
    readings: List<Reading>,
    meterById: Map<String, Meter>,
    listState: LazyListState,
    formatTimestamp: (Long) -> String,
    formatValue: (Meter, Double) -> String
) {
    HorizontalDivider()

    Text(
        text = "Zadnji vnosi:",
        style = MaterialTheme.typography.titleMedium
    )

    if (readings.isEmpty()) {
        Text("Ni vnosov.")
        return
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = readings
                .sortedByDescending { it.timestampMs }
                .take(50),
            key = {
                it.id
            }
        ) { reading ->
            val meter = meterById[reading.meterId]
            val meterLabel =
                meter?.name ?: reading.meterId
            val unitLabel =
                meter?.unit.orEmpty()
            val valueLabel =
                meter?.let {
                    formatValue(it, reading.value)
                } ?: reading.value.toString()

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTimestamp(
                                reading.timestampMs
                            ),
                            style =
                                MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = reading.enteredBy,
                            style =
                                MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = meterLabel,
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = if (unitLabel.isNotBlank()) {
                            "$valueLabel $unitLabel"
                        } else {
                            valueLabel
                        },
                        style =
                            MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}
