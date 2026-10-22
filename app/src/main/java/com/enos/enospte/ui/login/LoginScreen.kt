package com.enos.enospte.ui.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.enos.enospte.R
import com.enos.enospte.data.datastore.UserPrefsStorage
import com.enos.enospte.domain.model.UserProfile
import com.enos.enospte.domain.model.UserRole
import com.enos.enospte.data.source.UserCatalog
import com.enos.enospte.viewmodel.UsersViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (UserProfile) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { UserPrefsStorage(context) }
    val usersVm: UsersViewModel = viewModel()
    val allUsers by usersVm.users.collectAsState()

    // ---- Entrance animation (replaces splash) ----
    val scope = rememberCoroutineScope()
    val bgAlpha = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }
    val contentOffsetY = remember { Animatable(16f) } // px-ish, used as translationY

    LaunchedEffect(Unit) {
        // background fades in first, content follows
        scope.launch {
            bgAlpha.animateTo(
                targetValue = 0.58f,
                animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
            )
        }
        scope.launch {
            contentAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 380, delayMillis = 120, easing = FastOutSlowInEasing)
            )
        }
        scope.launch {
            contentOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 420, delayMillis = 120, easing = FastOutSlowInEasing)
            )
        }
    }

    val admin = allUsers.firstOrNull { it.role == UserRole.ADMIN }
        ?: UserProfile(id = "ADMIN", displayName = "Admin", role = UserRole.ADMIN)

    val users = allUsers.filter { it.role == UserRole.USER }

    var selected by remember { mutableStateOf<UserProfile?>(null) }
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    var showPinSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ----- Admin easter egg: 5 taps in 2.5s, chip visible 3s -----
    var adminArmed by remember { mutableStateOf(false) }
    var tapCount by remember { mutableIntStateOf(0) }
    var firstTapMs by remember { mutableLongStateOf(0L) }
    val windowMs = 2_500L

    fun resetTap() {
        tapCount = 0
        firstTapMs = 0L
    }

    fun registerLogoTap() {
        val now = System.currentTimeMillis()
        if (firstTapMs == 0L || (now - firstTapMs) > windowMs) {
            firstTapMs = now
            tapCount = 1
            return
        }
        tapCount += 1
        if (tapCount >= 5) {
            adminArmed = true
            resetTap()
        }
    }

    LaunchedEffect(adminArmed) {
        if (adminArmed) {
            delay(3_000)
            adminArmed = false
        }
    }

    fun startAdminLogin() {
        pin = ""
        error = null
        selected = admin
        adminArmed = false
    }

    // DataStore override pin
    val storedPin by selected?.let { u ->
        prefs.observePin(u.id).collectAsState(initial = null)
    } ?: remember { mutableStateOf<String?>(null) }

    val effectivePin = storedPin ?: selected?.let { UserCatalog.defaultPin(it.id) }

    // open sheet when user selected
    LaunchedEffect(selected?.id) {
        pin = ""
        error = null
        showPinSheet = selected != null
    }

    fun tryLoginIfReady() {
        val user = selected ?: return
        val expected = effectivePin ?: return

        if (pin.length == 4) {
            if (pin == expected) {
                showPinSheet = false
                onLoginSuccess(user)
            } else {
                error = "Napačen PIN."
                pin = ""
            }
        }
    }

    fun appendDigit(d: Char) {
        if (pin.length >= 4) return
        pin += d
        error = null
        if (pin.length == 4) tryLoginIfReady()
    }

    fun backspace() {
        if (pin.isNotEmpty()) pin = pin.dropLast(1)
        error = null
    }

    fun clearPin() {
        pin = ""
        error = null
    }

    var menuExpanded by remember { mutableStateOf(false) }

    // “glass” dropdown field/menu
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f),
        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f),
        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
        focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f),
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )

    Box(Modifier.fillMaxSize()) {

        // background image (animated alpha)
        Image(
            painter = painterResource(R.drawable.enos_login_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 120.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter,
            alpha = bgAlpha.value
        )

        // overlay gradient (stays constant)
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.45f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(top = 60.dp)
                .graphicsLayer {
                    alpha = contentAlpha.value
                    translationY = contentOffsetY.value
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.enos_logo_icon),
                contentDescription = "ENOS",
                modifier = Modifier
                    .size(124.dp)
                    .clip(CircleShape)
                    .clickable { registerLogoTap() }
            )

            Text(
                text = "ENOS PTE",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            if (adminArmed) {
                Spacer(Modifier.height(10.dp))
                AssistChip(
                    onClick = { startAdminLogin() },
                    label = { Text("Admin prijava") }
                )
            }

            Spacer(Modifier.height(34.dp))

            ExposedDropdownMenuBox(
                expanded = menuExpanded,
                onExpandedChange = { menuExpanded = !menuExpanded }
            ) {
                val label = selected?.displayName?.takeIf { selected?.id != "ADMIN" }
                    ?: "Izberi uporabnika"

                OutlinedTextField(
                    value = label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Uporabnik") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(menuExpanded) },
                    colors = fieldColors,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .menuAnchor(
                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
                    shadowElevation = 14.dp
                ) {
                    users.forEachIndexed { idx, u ->
                        DropdownMenuItem(
                            text = { Text(u.displayName) },
                            onClick = {
                                selected = u
                                menuExpanded = false
                            }
                        )
                        if (idx != users.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                            )
                        }
                    }
                }
            }

            if (selected != null) {
                Spacer(Modifier.height(6.dp))
                TextButton(
                    onClick = {
                        selected = null
                        showPinSheet = false
                        pin = ""
                        error = null
                    },
                    modifier = Modifier.align(Alignment.End)
                ) { Text("Počisti izbiro") }
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "ENOS PTE • v0.1 (2026)",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    // PIN bottom sheet
    if (showPinSheet && selected != null) {
        val isAdmin = selected?.role == UserRole.ADMIN

        ModalBottomSheet(
            onDismissRequest = {
                showPinSheet = false
                pin = ""
                error = null
                selected = null
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isAdmin) "Admin PIN" else "Vnesi PIN",
                    style = MaterialTheme.typography.titleMedium
                )

                if (!isAdmin) {
                    Spacer(Modifier.height(6.dp))
                    AssistChip(
                        onClick = {},
                        enabled = false,
                        label = { Text(selected?.displayName ?: "") }
                    )
                }

                Spacer(Modifier.height(12.dp))

                val masked = "•".repeat(pin.length).padEnd(4, '—')
                Text(masked, style = MaterialTheme.typography.headlineLarge)

                error?.let {
                    Spacer(Modifier.height(6.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(12.dp))

                Keypad(
                    onDigit = { appendDigit(it) },
                    onBackspace = { backspace() },
                    onClear = { clearPin() }
                )

                Spacer(Modifier.height(10.dp))

                TextButton(onClick = {
                    showPinSheet = false
                    pin = ""
                    error = null
                    selected = null
                }) { Text("Zapri") }
            }
        }
    }
}

@Composable
private fun Keypad(
    onDigit: (Char) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {

        @Composable
        fun row(vararg keys: String) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                keys.forEach { k ->
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.0f),
                        onClick = {
                            when (k) {
                                "C" -> onClear()
                                "⌫" -> onBackspace()
                                else -> onDigit(k[0])
                            }
                        }
                    ) {
                        Text(k, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }

        row("1", "2", "3")
        row("4", "5", "6")
        row("7", "8", "9")
        row("C", "0", "⌫")
    }
}