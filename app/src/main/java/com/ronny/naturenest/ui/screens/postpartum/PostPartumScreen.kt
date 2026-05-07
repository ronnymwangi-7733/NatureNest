package com.ronny.naturenest.ui.screens.postpartum

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

// ─── Data models ───────────────────────────────────────────────────────────────

data class MoodEntry(val emoji: String, val label: String, val color: Color)

data class RecoveryTip(val emoji: String, val title: String, val description: String)

data class SupportResource(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val tag: String,
    val tagColor: Color
)

// ─── Static data ───────────────────────────────────────────────────────────────

private val moodOptions = listOf(
    MoodEntry("😊", "Great", Color(0xFF4CAF50)),
    MoodEntry("🙂", "Good", Color(0xFF8BC34A)),
    MoodEntry("😐", "Okay", Color(0xFFFFC107)),
    MoodEntry("😔", "Low", Color(0xFFFF9800)),
    MoodEntry("😢", "Difficult", Color(0xFFF44336))
)

private val recoveryTips = listOf(
    RecoveryTip(
        "💤", "Rest When Baby Rests",
        "Sleep deprivation intensifies emotional vulnerability. Prioritise sleep over chores whenever possible."
    ),
    RecoveryTip(
        "🥗", "Nourish Your Body",
        "Eat iron-rich foods, stay hydrated, and consider a postnatal vitamin. Your body is still healing."
    ),
    RecoveryTip(
        "🚶‍♀️", "Gentle Movement",
        "Short walks outdoors can significantly lift mood. Start slowly — even 10 minutes helps."
    ),
    RecoveryTip(
        "🗣️", "Talk About It",
        "Share your feelings with a trusted person. Baby blues are common in the first 2 weeks; seek help if it persists."
    ),
    RecoveryTip(
        "🧘‍♀️", "Breathe & Ground",
        "Try box breathing: inhale 4 counts, hold 4, exhale 4, hold 4. Repeat when overwhelmed."
    ),
    RecoveryTip(
        "🤱", "Accept Help",
        "Accepting support from family and friends is a sign of strength, not weakness. Let others show up for you."
    )
)

private val supportResources = listOf(
    SupportResource(
        "📞", "Postpartum Support International",
        "Helpline & online support groups for new mothers worldwide",
        "Helpline", Color(0xFF7B61FF)
    ),
    SupportResource(
        "🧠", "Understanding Baby Blues",
        "What to expect in the first 2 weeks vs signs of postpartum depression",
        "Education", Color(0xFF2196F3)
    ),
    SupportResource(
        "💬", "Online Peer Community",
        "Connect with other mothers in the NatureNest community",
        "Community", Color(0xFF4CAF50)
    ),
    SupportResource(
        "👩‍⚕️", "Find a Therapist",
        "Locate maternal mental health professionals near you",
        "Therapy", Color(0xFFE91E63)
    ),
    SupportResource(
        "📖", "Recovery Journal",
        "Guided prompts for processing emotions after birth",
        "Journaling", Color(0xFFFF9800)
    ),
    SupportResource(
        "🎧", "Guided Meditations",
        "Audio sessions designed specifically for postpartum recovery",
        "Wellness", Color(0xFF00BCD4)
    )
)

private val warningSignsList = listOf(
    "Persistent sadness or hopelessness beyond 2 weeks",
    "Difficulty bonding with your baby",
    "Thoughts of harming yourself or your baby",
    "Severe anxiety or panic attacks",
    "Hallucinations or feeling detached from reality",
    "Inability to sleep even when baby sleeps"
)

// ─── PostpartumScreen ──────────────────────────────────────────────────────────

@Composable
fun PostpartumScreen(navController: NavController) {
    var selectedMood by remember { mutableStateOf<MoodEntry?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Recovery", "Resources", "Warning Signs")

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "Postpartum Support"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Hero banner ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(colors = listOf(LavenderLight, SurfaceCream))
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("💆", fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "You're doing amazingly well",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Postpartum recovery is a journey. Be gentle with yourself — your feelings are valid.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))

                    // ── Daily mood check-in ──
                    Text(
                        "How are you feeling today?",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        moodOptions.forEach { mood ->
                            val isSelected = selectedMood == mood
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) mood.color.copy(alpha = 0.15f)
                                        else Color.Transparent
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) mood.color else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedMood = mood }
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                Text(mood.emoji, fontSize = 26.sp)
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    mood.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) mood.color else TextHint,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    if (selectedMood != null) {
                        Spacer(Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = selectedMood!!.color.copy(alpha = 0.1f)
                        ) {
                            Text(
                                when (selectedMood!!.label) {
                                    "Great" -> "Wonderful! Keep embracing these moments 🌸"
                                    "Good" -> "That's great to hear. Keep going! 💪"
                                    "Okay" -> "It's okay to just be okay. One day at a time 🌿"
                                    "Low" -> "Low days are valid. Reach out if you need support 🤗"
                                    "Difficult" -> "Please know you're not alone. Consider talking to someone 💕"
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = selectedMood!!.color,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // ── Tabs ──
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceWhite,
                contentColor = Blush,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Blush
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selectedTab == index) Blush else TextHint
                            )
                        }
                    )
                }
            }

            // ── Tab content ──
            when (selectedTab) {
                0 -> RecoveryTab()
                1 -> ResourcesTab()
                2 -> WarningSignsTab()
            }
        }
    }
}

// ─── Recovery tab ──────────────────────────────────────────────────────────────

@Composable
private fun RecoveryTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Recovery Tips",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Small, consistent acts of self-care make a big difference during your postpartum recovery.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        recoveryTips.forEach { tip ->
            RecoveryTipCard(tip)
        }

        Spacer(Modifier.height(8.dp))

        // Physical recovery section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏥", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Physical Recovery Checklist",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(10.dp))
                listOf(
                    "6-week postpartum check-up scheduled",
                    "Pelvic floor exercises started",
                    "Wound/stitches healing well",
                    "Breastfeeding support if needed",
                    "Contraception discussed with doctor",
                    "Thyroid check if feeling off"
                ).forEach { item ->
                    var checked by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { checked = !checked }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (checked) Icons.Default.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                            null,
                            tint = if (checked) Blush else TextHint,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            item,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (checked) TextHint else TextPrimary,
                            textDecoration = if (checked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun RecoveryTipCard(tip: RecoveryTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(BlushLight),
                contentAlignment = Alignment.Center
            ) {
                Text(tip.emoji, fontSize = 22.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    tip.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    tip.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

// ─── Resources tab ─────────────────────────────────────────────────────────────

@Composable
private fun ResourcesTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Support Resources",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "You don't have to navigate this alone. These resources are here for you.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        supportResources.forEach { resource ->
            SupportResourceCard(resource)
        }

        Spacer(Modifier.height(8.dp))

        // Emergency banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            elevation = CardDefaults.cardElevation(0.dp),
            border = BorderStroke(1.dp, Color(0xFFFF9800))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🆘", fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "In Crisis?",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "If you feel unsafe or are having thoughts of harming yourself or your baby, please call emergency services or go to your nearest hospital immediately.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SupportResourceCard(resource: SupportResource) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* navigate or open link */ },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(resource.tagColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(resource.emoji, fontSize = 24.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    resource.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    resource.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Spacer(Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = resource.tagColor.copy(alpha = 0.12f)
            ) {
                Text(
                    resource.tag,
                    style = MaterialTheme.typography.labelSmall,
                    color = resource.tagColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// ─── Warning signs tab ─────────────────────────────────────────────────────────

@Composable
private fun WarningSignsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Difference between baby blues and PPD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Baby Blues vs. Postpartum Depression",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))

                // Baby blues
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BabyBluesColumn(
                        title = "Baby Blues",
                        color = Color(0xFF4CAF50),
                        emoji = "🌤️",
                        points = listOf(
                            "Starts day 3–5",
                            "Lasts up to 2 weeks",
                            "Mood swings & tearfulness",
                            "Resolves on its own"
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    BabyBluesColumn(
                        title = "PPD",
                        color = Color(0xFFF44336),
                        emoji = "⛈️",
                        points = listOf(
                            "Starts anytime in first year",
                            "Lasts weeks or months",
                            "Severe & persistent",
                            "Needs professional help"
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Warning signs list
        Text(
            "⚠️ Seek Help If You Experience:",
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                warningSignsList.forEachIndexed { index, sign ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF44336).copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                null,
                                tint = Color(0xFFF44336),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            sign,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )
                    }
                    if (index < warningSignsList.lastIndex) {
                        Divider(color = BlushLight.copy(alpha = 0.5f))
                    }
                }
            }
        }

        // Postpartum psychosis note
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)),
            elevation = CardDefaults.cardElevation(0.dp),
            border = BorderStroke(1.dp, Color(0xFFE91E63).copy(alpha = 0.3f))
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                Text("🚨", fontSize = 22.sp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "Postpartum Psychosis",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFE91E63),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "A rare but serious condition. Symptoms include hallucinations, confusion, rapid mood swings, and paranoia. This is a medical emergency — call emergency services immediately.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }
        }

        // Reassurance footer
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("💕", fontSize = 28.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Seeking help is the bravest thing you can do for yourself and your baby. You are not alone.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun BabyBluesColumn(
    title: String,
    color: Color,
    emoji: String,
    points: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.07f))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 16.sp)
            Spacer(Modifier.width(6.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        points.forEach { point ->
            Row(
                modifier = Modifier.padding(vertical = 3.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(6.dp))
                Text(point, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
            }
        }
    }
}

// ─── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun PostpartumScreenPreview() {
    PostpartumScreen(navController = rememberNavController())
}