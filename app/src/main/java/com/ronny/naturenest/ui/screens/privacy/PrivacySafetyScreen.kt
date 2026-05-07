package com.ronny.naturenest.ui.screens.privacy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

// ─── PrivacySafetyScreen ───────────────────────────────────────────────────────

@Composable
fun PrivacySafetyScreen(navController: NavController) {
    // Privacy toggles state
    var analyticsEnabled by remember { mutableStateOf(true) }
    var personalisedTipsEnabled by remember { mutableStateOf(true) }
    var communityVisibility by remember { mutableStateOf(true) }
    var locationEnabled by remember { mutableStateOf(false) }
    var crashReportsEnabled by remember { mutableStateOf(true) }
    var marketingEnabled by remember { mutableStateOf(false) }

    // Security toggles
    var biometricEnabled by remember { mutableStateOf(false) }
    var twoFactorEnabled by remember { mutableStateOf(false) }

    // Delete confirmation
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "Privacy & Safety"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(colors = listOf(BlushLight, SurfaceCream)))
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(colors = listOf(Blush, BlushDark))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Lock, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "Your data, your control",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Manage how NatureNest uses and protects your information.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Data & Privacy ──
            PrivacySection(title = "Data & Privacy") {
                PrivacyToggleItem(
                    icon = Icons.Outlined.BarChart,
                    title = "Usage Analytics",
                    subtitle = "Help us improve the app by sharing anonymous usage data",
                    checked = analyticsEnabled,
                    onCheckedChange = { analyticsEnabled = it }
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyToggleItem(
                    icon = Icons.Outlined.AutoAwesome,
                    title = "Personalised Tips",
                    subtitle = "Receive tips tailored to your pregnancy stage and preferences",
                    checked = personalisedTipsEnabled,
                    onCheckedChange = { personalisedTipsEnabled = it }
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyToggleItem(
                    icon = Icons.Outlined.Group,
                    title = "Community Profile Visibility",
                    subtitle = "Let other members see your profile in the community",
                    checked = communityVisibility,
                    onCheckedChange = { communityVisibility = it }
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyToggleItem(
                    icon = Icons.Outlined.LocationOn,
                    title = "Location Access",
                    subtitle = "Used to find nearby healthcare providers and clinics",
                    checked = locationEnabled,
                    onCheckedChange = { locationEnabled = it }
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyToggleItem(
                    icon = Icons.Outlined.BugReport,
                    title = "Crash Reports",
                    subtitle = "Automatically send crash logs to help fix bugs",
                    checked = crashReportsEnabled,
                    onCheckedChange = { crashReportsEnabled = it }
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyToggleItem(
                    icon = Icons.Outlined.Campaign,
                    title = "Marketing Communications",
                    subtitle = "Receive updates about new features and promotions",
                    checked = marketingEnabled,
                    onCheckedChange = { marketingEnabled = it }
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Account Security ──
            PrivacySection(title = "Account Security") {
                PrivacyToggleItem(
                    icon = Icons.Outlined.Fingerprint,
                    title = "Biometric Login",
                    subtitle = "Use fingerprint or face unlock to access the app",
                    checked = biometricEnabled,
                    onCheckedChange = { biometricEnabled = it }
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyToggleItem(
                    icon = Icons.Outlined.Security,
                    title = "Two-Factor Authentication",
                    subtitle = "Add an extra layer of security to your account",
                    checked = twoFactorEnabled,
                    onCheckedChange = { twoFactorEnabled = it }
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyNavItem(
                    icon = Icons.Outlined.Password,
                    title = "Change Password",
                    subtitle = "Update your account password"
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyNavItem(
                    icon = Icons.Outlined.Devices,
                    title = "Active Sessions",
                    subtitle = "View and manage devices logged into your account"
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Data Management ──
            PrivacySection(title = "Your Data") {
                PrivacyNavItem(
                    icon = Icons.Outlined.Download,
                    title = "Download My Data",
                    subtitle = "Export a copy of all your NatureNest data"
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyNavItem(
                    icon = Icons.Outlined.PrivacyTip,
                    title = "Privacy Policy",
                    subtitle = "Read our full privacy policy"
                )
                Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                PrivacyNavItem(
                    icon = Icons.Outlined.Gavel,
                    title = "Terms of Service",
                    subtitle = "Review our terms and conditions"
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Data encryption badge ──
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f))
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "End-to-End Encrypted",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "All your health data is encrypted in transit and at rest. We never sell your personal information.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Danger zone ──
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    "DANGER ZONE",
                    style = MaterialTheme.typography.labelSmall,
                    color = ErrorRed,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(1.dp),
                    border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.2f))
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.DeleteSweep, null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Clear All Data", style = MaterialTheme.typography.bodyMedium, color = ErrorRed, fontWeight = FontWeight.Medium)
                                Text("Wipe all health records, reminders and notes", style = MaterialTheme.typography.bodySmall, color = TextHint)
                            }
                            Icon(Icons.Default.ChevronRight, null, tint = ErrorRed.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
                        }
                        Divider(color = ErrorRed.copy(alpha = 0.1f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDeleteDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.PersonOff, null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Delete Account", style = MaterialTheme.typography.bodyMedium, color = ErrorRed, fontWeight = FontWeight.Medium)
                                Text("Permanently delete your account and all data", style = MaterialTheme.typography.bodySmall, color = TextHint)
                            }
                            Icon(Icons.Default.ChevronRight, null, tint = ErrorRed.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // ── Delete confirmation dialog ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            icon = { Icon(Icons.Default.Warning, null, tint = ErrorRed, modifier = Modifier.size(32.dp)) },
            title = {
                Text(
                    "Delete Account?",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "This will permanently delete your account, all health records, baby profiles, and reminders. This action cannot be undone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Delete Permanently") }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BlushLight)
                ) { Text("Cancel", color = TextHint) }
            }
        )
    }
}

// ─── Reusable section wrapper ──────────────────────────────────────────────────

@Composable
private fun PrivacySection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            color = TextHint,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(content = content)
        }
    }
}

// ─── Toggle row ────────────────────────────────────────────────────────────────

@Composable
private fun PrivacyToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Blush, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextHint)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Blush,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BlushLight
            )
        )
    }
}

// ─── Nav row (no toggle) ───────────────────────────────────────────────────────

@Composable
private fun PrivacyNavItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Blush, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextHint)
        }
        Icon(Icons.Default.ChevronRight, null, tint = TextHint, modifier = Modifier.size(18.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacySafetyScreenPreview() {
    PrivacySafetyScreen(navController = rememberNavController())
}