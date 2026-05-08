package com.ronny.naturenest.ui.screens.privacy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

// ─── Firebase tip fetcher ──────────────────────────────────────────────────────
//
// Firestore structure expected:
//   Collection: "tips"
//   Document fields:
//     - stage:      String  e.g. "Pregnant" | "New Mom (0-12 months)" | "Trying to Conceive" | "Experienced Mom"
//     - preference: String  e.g. "nutrition" | "mental_health" | "exercise"  (optional)
//     - text:       String  the tip content shown to the user
//
// The user profile is stored at: users/{uid}/profile/details
//   Fields: stage (String), preferences (List<String>)

private fun fetchPersonalisedTips(
    db: FirebaseFirestore,
    auth: FirebaseAuth,
    onResult: (List<String>) -> Unit
) {
    val uid = auth.currentUser?.uid ?: return

    db.collection("users").document(uid)
        .collection("profile").document("details")
        .get()
        .addOnSuccessListener { profileDoc ->
            val stage = profileDoc.getString("stage") ?: return@addOnSuccessListener
            val preferences = (profileDoc.get("preferences") as? List<*>)
                ?.filterIsInstance<String>() ?: emptyList()

            db.collection("tips")
                .whereEqualTo("stage", stage)
                .get()
                .addOnSuccessListener { snapshot ->
                    val tips = snapshot.documents
                        .filter { doc ->
                            val tipPref = doc.getString("preference")
                            // include tip if it has no preference filter OR matches user's preferences
                            tipPref == null || tipPref in preferences
                        }
                        .mapNotNull { it.getString("text") }
                        .shuffled()
                        .take(5)
                    onResult(tips)
                }
        }
}

// ─── Biometric helper ──────────────────────────────────────────────────────────

private fun launchBiometricPrompt(
    context: Context,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val activity = context as? FragmentActivity ?: run {
        onFailure("Biometric authentication requires a FragmentActivity context.")
        return
    }
    val executor = ContextCompat.getMainExecutor(context)

    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            onSuccess()
        }
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            onFailure(errString.toString())
        }
        override fun onAuthenticationFailed() {
            onFailure("Authentication failed. Please try again.")
        }
    }

    val prompt = BiometricPrompt(activity, executor, callback)

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("NatureNest Biometric Login")
        .setSubtitle("Use your fingerprint or face to unlock")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    prompt.authenticate(promptInfo)
}

private fun isBiometricAvailable(context: Context): Boolean =
    BiometricManager.from(context)
        .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) ==
            BiometricManager.BIOMETRIC_SUCCESS

// ─── Open Google Maps for nearby clinics ──────────────────────────────────────

private fun openNearbyHealthcarePlaces(context: Context, lat: Double, lng: Double) {
    val query = Uri.encode("hospitals clinics near me")
    val mapsUri = Uri.parse("geo:$lat,$lng?q=$query")
    val mapsIntent = Intent(Intent.ACTION_VIEW, mapsUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    if (mapsIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapsIntent)
    } else {
        // Fallback: open in browser
        val browserUri = Uri.parse(
            "https://www.google.com/maps/search/hospitals+clinics/@$lat,$lng,14z"
        )
        context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
    }
}

// ─── PrivacySafetyScreen ───────────────────────────────────────────────────────

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrivacySafetyScreen(navController: NavController) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val firebaseAuth = remember {
        if (isPreview) null else try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
    }
    val firestore = remember {
        if (isPreview) null else try { FirebaseFirestore.getInstance() } catch (e: Exception) { null }
    }

    // ── Toggle states ──
    var analyticsEnabled by remember { mutableStateOf(true) }
    var personalisedTipsEnabled by remember { mutableStateOf(true) }
    var communityVisibility by remember { mutableStateOf(true) }
    var locationEnabled by remember { mutableStateOf(false) }
    var crashReportsEnabled by remember { mutableStateOf(true) }
    var marketingEnabled by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var twoFactorEnabled by remember { mutableStateOf(false) }

    // ── Dialog / feedback state ──
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBiometricFeedbackDialog by remember { mutableStateOf(false) }
    var biometricFeedback by remember { mutableStateOf("") }
    var showLocationRationaleDialog by remember { mutableStateOf(false) }
    var showTipsDialog by remember { mutableStateOf(false) }
    var personalisedTips by remember { mutableStateOf<List<String>>(emptyList()) }
    var tipsLoading by remember { mutableStateOf(false) }

    // ── Location permission (Accompanist) ──
    val locationPermission = if (isPreview) {
        remember {
            object : PermissionState {
                override val permission: String = Manifest.permission.ACCESS_FINE_LOCATION
                override val status: PermissionStatus = PermissionStatus.Denied(shouldShowRationale = false)
                override fun launchPermissionRequest() {}
            }
        }
    } else {
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Helper: get location and open Maps
    fun fetchLocationAndOpenMaps() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val lat = location?.latitude ?: -1.286389  // Nairobi fallback
                val lng = location?.longitude ?: 36.817223
                openNearbyHealthcarePlaces(context, lat, lng)
            }
        } catch (_: SecurityException) {
            locationEnabled = false
        }
    }

    // ── Location toggle handler ──
    fun handleLocationToggle(enabled: Boolean) {
        locationEnabled = enabled
        if (!enabled) return
        when {
            locationPermission.status.isGranted -> fetchLocationAndOpenMaps()
            locationPermission.status.shouldShowRationale -> showLocationRationaleDialog = true
            else -> locationPermission.launchPermissionRequest()
        }
    }

    // Re-act when permission is granted after the request
    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted && locationEnabled) {
            fetchLocationAndOpenMaps()
        }
    }

    // ── Personalised tips toggle handler ──
    fun handlePersonalisedTipsToggle(enabled: Boolean) {
        personalisedTipsEnabled = enabled
        if (!enabled) return
        tipsLoading = true
        showTipsDialog = true

        if (firestore != null && firebaseAuth != null) {
            fetchPersonalisedTips(firestore, firebaseAuth) { tips ->
                tipsLoading = false
                personalisedTips = tips
            }
        } else {
            // Provide mock tips for Preview or if Firebase is unavailable
            tipsLoading = false
            personalisedTips = listOf(
                "Eat plenty of leafy greens for folic acid 🌿",
                "Stay hydrated by drinking 8-10 glasses of water daily 💧",
                "Take short walks to improve circulation and energy 🚶‍♀️"
            )
        }
    }

    // ── Biometric toggle handler ──
    fun handleBiometricToggle(enable: Boolean) {
        if (!enable) { biometricEnabled = false; return }
        if (!isBiometricAvailable(context)) {
            biometricFeedback = "No biometric hardware found or no fingerprint/face enrolled. " +
                    "Please set one up in your device Settings first."
            showBiometricFeedbackDialog = true
            return
        }
        launchBiometricPrompt(
            context = context,
            onSuccess = {
                biometricEnabled = true
                biometricFeedback = "Biometric login enabled successfully! ✅"
                showBiometricFeedbackDialog = true
            },
            onFailure = { error ->
                biometricEnabled = false
                biometricFeedback = "Could not enable biometric login: $error"
                showBiometricFeedbackDialog = true
            }
        )
    }

    // ── Persist all toggle settings to Firestore ──
    LaunchedEffect(
        analyticsEnabled, personalisedTipsEnabled, communityVisibility,
        locationEnabled, crashReportsEnabled, marketingEnabled,
        biometricEnabled, twoFactorEnabled
    ) {
        if (isPreview) return@LaunchedEffect
        val uid = firebaseAuth?.currentUser?.uid ?: return@LaunchedEffect
        firestore?.collection("users")?.document(uid)
            ?.collection("settings")?.document("privacy")
            ?.set(mapOf(
                "analyticsEnabled" to analyticsEnabled,
                "personalisedTipsEnabled" to personalisedTipsEnabled,
                "communityVisibility" to communityVisibility,
                "locationEnabled" to locationEnabled,
                "crashReportsEnabled" to crashReportsEnabled,
                "marketingEnabled" to marketingEnabled,
                "biometricEnabled" to biometricEnabled,
                "twoFactorEnabled" to twoFactorEnabled
            ))
    }

    // ── Load saved settings from Firestore on first open ──
    LaunchedEffect(Unit) {
        if (isPreview) return@LaunchedEffect
        val uid = firebaseAuth?.currentUser?.uid ?: return@LaunchedEffect
        firestore?.collection("users")?.document(uid)
            ?.collection("settings")?.document("privacy")
            ?.get()
            ?.addOnSuccessListener { doc ->
                if (doc.exists()) {
                    analyticsEnabled = doc.getBoolean("analyticsEnabled") ?: true
                    personalisedTipsEnabled = doc.getBoolean("personalisedTipsEnabled") ?: true
                    communityVisibility = doc.getBoolean("communityVisibility") ?: true
                    locationEnabled = doc.getBoolean("locationEnabled") ?: false
                    crashReportsEnabled = doc.getBoolean("crashReportsEnabled") ?: true
                    marketingEnabled = doc.getBoolean("marketingEnabled") ?: false
                    biometricEnabled = doc.getBoolean("biometricEnabled") ?: false
                    twoFactorEnabled = doc.getBoolean("twoFactorEnabled") ?: false
                }
            }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // UI — pixel-identical to the original
    // ══════════════════════════════════════════════════════════════════════════

    Scaffold(
        containerColor = SurfaceCream,
        topBar = { NatureNestTopBar(title = "Privacy & Safety") }
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
                    onCheckedChange = { handlePersonalisedTipsToggle(it) }
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
                    onCheckedChange = { handleLocationToggle(it) }
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
                    onCheckedChange = { handleBiometricToggle(it) }
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

            // ── Encryption badge ──
            Card(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
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
                            modifier = Modifier.fillMaxWidth().clickable { }.padding(16.dp),
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
                            modifier = Modifier.fillMaxWidth().clickable { showDeleteDialog = true }.padding(16.dp),
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

    // ── Delete dialog — unchanged ──
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            icon = { Icon(Icons.Default.Warning, null, tint = ErrorRed, modifier = Modifier.size(32.dp)) },
            title = {
                Text("Delete Account?", style = MaterialTheme.typography.titleMedium, color = TextPrimary, textAlign = TextAlign.Center)
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

    // ── Biometric feedback dialog ──
    if (showBiometricFeedbackDialog) {
        AlertDialog(
            onDismissRequest = { showBiometricFeedbackDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            icon = {
                Icon(
                    if (biometricEnabled) Icons.Default.Fingerprint else Icons.Default.Warning,
                    null,
                    tint = if (biometricEnabled) Color(0xFF4CAF50) else ErrorRed,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    if (biometricEnabled) "Biometric Enabled" else "Biometric Unavailable",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(biometricFeedback, style = MaterialTheme.typography.bodySmall, color = TextSecondary, textAlign = TextAlign.Center)
            },
            confirmButton = {
                TextButton(onClick = { showBiometricFeedbackDialog = false }) { Text("OK", color = Blush) }
            }
        )
    }

    // ── Location rationale dialog ──
    if (showLocationRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showLocationRationaleDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            icon = { Icon(Icons.Outlined.LocationOn, null, tint = Blush, modifier = Modifier.size(32.dp)) },
            title = {
                Text("Location Permission Needed", style = MaterialTheme.typography.titleMedium, color = TextPrimary, textAlign = TextAlign.Center)
            },
            text = {
                Text(
                    "NatureNest needs your location to find nearby healthcare providers and clinics. Your location is never stored or shared with third parties.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { showLocationRationaleDialog = false; locationPermission.launchPermissionRequest() },
                    colors = ButtonDefaults.buttonColors(containerColor = Blush),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Allow Location") }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { locationEnabled = false; showLocationRationaleDialog = false },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BlushLight)
                ) { Text("Not Now", color = TextHint) }
            }
        )
    }

    // ── Personalised tips dialog ──
    if (showTipsDialog) {
        AlertDialog(
            onDismissRequest = { showTipsDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            icon = { Text("✨", fontSize = 32.sp) },
            title = {
                Text("Your Personalised Tips", style = MaterialTheme.typography.titleMedium, color = TextPrimary, textAlign = TextAlign.Center)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (tipsLoading) {
                        CircularProgressIndicator(
                            color = Blush,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else if (personalisedTips.isEmpty()) {
                        Text(
                            "No tips found for your profile yet. Make sure your pregnancy stage and preferences are set up in your profile.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        personalisedTips.forEach { tip ->
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                                Text("🌿", fontSize = 14.sp)
                                Spacer(Modifier.width(8.dp))
                                Text(tip, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTipsDialog = false }) { Text("Got it", color = Blush) }
            }
        )
    }
}

// ─── Reusable section wrapper — unchanged ─────────────────────────────────────

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

// ─── Toggle row — unchanged ────────────────────────────────────────────────────

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

// ─── Nav row — unchanged ──────────────────────────────────────────────────────

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