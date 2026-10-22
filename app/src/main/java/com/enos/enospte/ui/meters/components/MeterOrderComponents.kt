package com.enos.enospte.ui.meters.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enos.enospte.data.model.Meter

@Composable
fun SiteSelector(
    sites: List<String>,
    selectedSite: String,
    onSiteSelected: (String) -> Unit
) {
    if (sites.isEmpty()) return

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        sites.forEachIndexed { index, site ->
            SegmentedButton(
                selected = selectedSite == site,
                onClick = { onSiteSelected(site) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = sites.size
                )
            ) {
                Text(site)
            }
        }
    }
}

@Composable
fun RoutineOrderRow(
    meter: Meter,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meter.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = meterCategoryPath(meter),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    enabled = canMoveUp,
                    onClick = onMoveUp
                ) {
                    Text("↑")
                }

                OutlinedButton(
                    enabled = canMoveDown,
                    onClick = onMoveDown
                ) {
                    Text("↓")
                }
            }
        }
    }
}

@Composable
fun MeterSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text("Išči števec") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Počisti iskanje"
                    )
                }
            }
        }
    )
}

@Composable
fun ExpandableGroupRow(
    name: String,
    selectedCount: Int,
    totalCount: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    onSelectAll: () -> Unit,
    onRemoveAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExpandableHeader(
                name = name,
                selectedCount = selectedCount,
                totalCount = totalCount,
                expanded = expanded,
                onToggle = onToggle,
                titleStyle = HeaderStyle.GROUP
            )

            BulkSelectionActions(
                selectedCount = selectedCount,
                totalCount = totalCount,
                onSelectAll = onSelectAll,
                onRemoveAll = onRemoveAll,
                bottomPadding = 4
            )
        }
    }
}

@Composable
fun ExpandableSubgroupRow(
    name: String,
    selectedCount: Int,
    totalCount: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    onSelectAll: () -> Unit,
    onRemoveAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExpandableHeader(
                name = name,
                selectedCount = selectedCount,
                totalCount = totalCount,
                expanded = expanded,
                onToggle = onToggle,
                titleStyle = HeaderStyle.SUBGROUP
            )

            BulkSelectionActions(
                selectedCount = selectedCount,
                totalCount = totalCount,
                onSelectAll = onSelectAll,
                onRemoveAll = onRemoveAll,
                bottomPadding = 2
            )
        }
    }
}

@Composable
fun MeterSelectionRow(
    meter: Meter,
    checked: Boolean,
    indentLevel: Int,
    onCheckedChange: (Boolean) -> Unit
) {
    val startPadding = when (indentLevel) {
        2 -> 32.dp
        1 -> 16.dp
        else -> 0.dp
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startPadding),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCheckedChange(!checked)
                }
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meter.name,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (meter.unit.isNotBlank()) {
                    Text(
                        text = meter.unit,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

private enum class HeaderStyle {
    GROUP,
    SUBGROUP
}

@Composable
private fun ExpandableHeader(
    name: String,
    selectedCount: Int,
    totalCount: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    titleStyle: HeaderStyle
) {
    val isGroup = titleStyle == HeaderStyle.GROUP

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(
                start = 14.dp,
                end = 8.dp,
                top = if (isGroup) 10.dp else 8.dp,
                bottom = if (isGroup) 2.dp else 1.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = if (isGroup) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.titleSmall
                }
            )

            Text(
                text = selectedCountText(
                    selectedCount = selectedCount,
                    totalCount = totalCount
                ),
                style = if (isGroup) {
                    MaterialTheme.typography.labelMedium
                } else {
                    MaterialTheme.typography.labelSmall
                },
                color = if (isGroup) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }

        Icon(
            imageVector = if (expanded) {
                Icons.Default.ExpandLess
            } else {
                Icons.Default.ExpandMore
            },
            contentDescription = if (expanded) {
                "Zapri skupino"
            } else {
                "Odpri skupino"
            }
        )
    }
}

@Composable
private fun BulkSelectionActions(
    selectedCount: Int,
    totalCount: Int,
    onSelectAll: () -> Unit,
    onRemoveAll: () -> Unit,
    bottomPadding: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 6.dp,
                end = 6.dp,
                bottom = bottomPadding.dp
            ),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            enabled = selectedCount < totalCount,
            onClick = onSelectAll
        ) {
            Text("Izberi vse")
        }

        TextButton(
            enabled = selectedCount > 0,
            onClick = onRemoveAll
        ) {
            Text("Odstrani vse")
        }
    }
}

private fun meterCategoryPath(
    meter: Meter
): String {
    return if (meter.subgroup.isNotBlank()) {
        "${meter.site} • ${meter.group} • ${meter.subgroup}"
    } else {
        "${meter.site} • ${meter.group}"
    }
}

private fun selectedCountText(
    selectedCount: Int,
    totalCount: Int
): String {
    return if (selectedCount == 0) {
        "$totalCount števcev"
    } else {
        "$selectedCount od $totalCount izbranih"
    }
}
