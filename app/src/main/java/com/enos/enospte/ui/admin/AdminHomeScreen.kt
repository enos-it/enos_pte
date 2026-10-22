package com.enos.enospte.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    enteredBy: String,
    onUsers: () -> Unit,
    onHistory: () -> Unit,
    onChangePin: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administracija") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Prijavljen uporabnik",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = enteredBy,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }

            Text(
                text = "Administrativne nastavitve",
                style = MaterialTheme.typography.titleMedium
            )

            AdminHomeItem(
                icon = Icons.Default.People,
                title = "Uporabniki",
                subtitle = "Dodajanje, brisanje in upravljanje uporabnikov.",
                onClick = onUsers
            )

            AdminHomeItem(
                icon = Icons.Default.History,
                title = "Zgodovina",
                subtitle = "Pregled vpisov in administrativnih dogodkov.",
                onClick = onHistory
            )

            AdminHomeItem(
                icon = Icons.Default.Key,
                title = "Spremeni PIN",
                subtitle = "Spremeni ali ponastavi uporabniški PIN.",
                onClick = onChangePin
            )

            AdminHomeItem(
                icon = Icons.Default.Logout,
                title = "Odjava",
                subtitle = "Odjavi se iz administratorskega računa.",
                onClick = onLogout
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "ENOS PTE • ADMIN",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AdminHomeItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            },
            headlineContent = { Text(title) },
            supportingContent = { Text(subtitle) },
            trailingContent = {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        )
    }
}
