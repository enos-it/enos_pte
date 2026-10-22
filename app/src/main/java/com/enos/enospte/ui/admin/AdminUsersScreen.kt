package com.enos.enospte.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enos.enospte.domain.model.UserProfile
import com.enos.enospte.domain.model.UserRole
import com.enos.enospte.viewmodel.UsersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    currentUserId: String,
    currentUserName: String,
    onBack: () -> Unit,
    vm: UsersViewModel = viewModel()
) {
    val users by vm.users.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showAddDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<UserProfile?>(null) }
    var userForPin by remember { mutableStateOf<UserProfile?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Uporabniki") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Nazaj"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = { Text("Dodaj uporabnika") },
                elevation = FloatingActionButtonDefaults.elevation()
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "${users.size} uporabnikov",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(
                items = users.sortedWith(
                    compareByDescending<UserProfile> { it.role == UserRole.ADMIN }
                        .thenBy { it.displayName.lowercase() }
                ),
                key = { it.id }
            ) { user ->
                UserRow(
                    user = user,
                    canDelete = user.role != UserRole.ADMIN &&
                        !user.id.equals(currentUserId, ignoreCase = true),
                    onResetPin = { userForPin = user },
                    onDelete = { userToDelete = user }
                )
            }

            item {
                Text(
                    text = "Brisanje uporabnika ne izbriše njegove zgodovine vpisov.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 96.dp
                    )
                )
            }
        }
    }

    if (showAddDialog) {
        AddUserDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { id, name, pin, setError ->
                vm.addUser(
                    id = id,
                    displayName = name,
                    pin = pin,
                    actorId = currentUserId,
                    actorName = currentUserName
                ) { error ->
                    if (error == null) {
                        showAddDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Uporabnik je dodan.")
                        }
                    } else {
                        setError(error)
                    }
                }
            }
        )
    }

    userToDelete?.let { user ->
        AlertDialog(
            onDismissRequest = { userToDelete = null },
            title = { Text("Izbriši uporabnika") },
            text = {
                Text(
                    "Ali želiš izbrisati uporabnika ${user.displayName} (${user.id})? " +
                        "Njegova zgodovina vpisov bo ostala shranjena."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.deleteUser(
                            user = user,
                            actorId = currentUserId,
                            actorName = currentUserName
                        ) { error ->
                            if (error == null) {
                                userToDelete = null
                                scope.launch {
                                    snackbarHostState.showSnackbar("Uporabnik je izbrisan.")
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(error)
                                }
                            }
                        }
                    }
                ) {
                    Text("Izbriši")
                }
            },
            dismissButton = {
                TextButton(onClick = { userToDelete = null }) {
                    Text("Prekliči")
                }
            }
        )
    }

    userForPin?.let { user ->
        ResetPinDialog(
            user = user,
            onDismiss = { userForPin = null },
            onConfirm = { pin, setError ->
                vm.resetPin(
                    user = user,
                    newPin = pin,
                    actorId = currentUserId,
                    actorName = currentUserName
                ) { error ->
                    if (error == null) {
                        userForPin = null
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "PIN uporabnika ${user.displayName} je spremenjen."
                            )
                        }
                    } else {
                        setError(error)
                    }
                }
            }
        )
    }
}

@Composable
private fun UserRow(
    user: UserProfile,
    canDelete: Boolean,
    onResetPin: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (user.role == UserRole.ADMIN) {
                    Icons.Default.AdminPanelSettings
                } else {
                    Icons.Default.Person
                },
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (user.role == UserRole.ADMIN) {
                        "${user.id} • Administrator"
                    } else {
                        user.id
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onResetPin) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "Spremeni PIN"
                )
            }

            IconButton(
                enabled = canDelete,
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Izbriši uporabnika"
                )
            }
        }
    }
}

@Composable
private fun AddUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        id: String,
        name: String,
        pin: String,
        setError: (String?) -> Unit
    ) -> Unit
) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj uporabnika") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        error = null
                    },
                    label = { Text("Ime uporabnika") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = id,
                    onValueChange = {
                        id = it
                            .uppercase()
                            .filter { char ->
                                char.isLetterOrDigit() ||
                                    char == '_' ||
                                    char == '-'
                            }
                        error = null
                    },
                    label = { Text("ID uporabnika") },
                    supportingText = {
                        Text("Primer: MIHA ali MIHA_1")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        pin = it.filter(Char::isDigit).take(4)
                        error = null
                    },
                    label = { Text("Začetni PIN") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(id, name, pin) { error = it }
                }
            ) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Prekliči")
            }
        }
    )
}

@Composable
private fun ResetPinDialog(
    user: UserProfile,
    onDismiss: () -> Unit,
    onConfirm: (
        pin: String,
        setError: (String?) -> Unit
    ) -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Spremeni PIN") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("${user.displayName} (${user.id})")

                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        pin = it.filter(Char::isDigit).take(4)
                        error = null
                    },
                    label = { Text("Nov PIN") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(pin) { error = it }
                }
            ) {
                Text("Shrani")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Prekliči")
            }
        }
    )
}
