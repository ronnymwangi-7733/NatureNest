package com.ronny.naturenest.ui.screens.about

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

// ─── Data models ───────────────────────────────────────────────────────────────

data class FeatureHighlight(val emoji: String, val title: String, val description: String)
data class TeamMember(val initials: String, val name: String, val role: String)
data class AboutLinkItem(val icon: ImageVector, val title: String, val subtitle: String)

// ─── Static data ───────────────────────────────────────────────────────────────

private val featureHighlights = listOf(
    FeatureHighlight("🤰", "Pregnancy Tracking", "Week-by-week insights, due date countdown, and trimester milestones."),
    FeatureHighlight("👶", "Baby Profiles", "Growth charts, immunisation schedules, and developmental milestones."),
    FeatureHighlight("💊", "Health Records", "Track vitals, medications, appointments, and lab results in one place."),
    FeatureHighlight("💆", "Postpartum Support", "Resources for maternal mental health, recovery tips, and mood tracking."),
    FeatureHighlight("🔔", "Smart Reminders", "Never miss a vaccination, appointment, or medication dose."),
    FeatureHighlight("🌿", "Community", "Connect with other expecting and new mothers on their journey.")
)

private val teamMembers = listOf(
    TeamMember("RO", "Ronny", "Founder & Lead Developer"),
    TeamMember("AM", "Amara", "UX Design & Research"),
    TeamMember("NJ", "Njeri", "Maternal Health Advisor"),
    TeamMember("KW", "Kamau", "Backend & Infrastructure")
)

private val legalLinks = listOf(
    AboutLinkItem(Icons.Outlined.PrivacyTip, "Privacy Policy", "Last updated January 2025"),
    AboutLinkItem(Icons.Outlined.Gavel, "Terms of Service", "Last updated January 2025"),
    AboutLinkItem(Icons.Outlined.Cookie, "Cookie Policy", "How we use cookies"),
    AboutLinkItem(Icons.Outlined.HealthAndSafety, "Medical Disclaimer", "NatureNest is not a medical service"),
    AboutLinkItem(Icons.Outlined.OpenInNew, "Open Source Licenses", "Third-party libraries used in this app")
)

// ─── AboutScreen ──────────────────────────────────────────────────────────────

@Composable
fun AboutScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("About", "Features", "Team")

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "About NatureNest"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── App identity hero ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(colors = listOf(BlushLight, SurfaceCream)))
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // App icon
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Brush.linearGradient(colors = listOf(Blush, BlushDark)))
                            .border(3.dp, Color.White, RoundedCornerShape(22.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🌿", fontSize = 40.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "NatureNest",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Nurturing life, every step of the way",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(12.dp))

                    // Version badges row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        VersionBadge("Version", "1.0.0")
                        VersionBadge("Build", "2025.01")
                        VersionBadge("Platform", "Android")
                    }
                }
            }

            // Tabs
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

            when (selectedTab) {
                0 -> AboutTab()
                1 -> FeaturesTab()
                2 -> TeamTab()
            }
        }
    }
}

// ─── About tab ─────────────────────────────────────────────────────────────────

@Composable
private fun AboutTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mission card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💚", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Our Mission",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "NatureNest was built to give every expectant and new mother in Africa — and beyond — a trusted companion through one of life's most transformative journeys. We believe informed, supported mothers raise healthier, happier families.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        // Stats row
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(
                Triple("10K+", "Mothers", "👩‍👧"),
                Triple("50K+", "Records", "📋"),
                Triple("4.8★", "Rating", "⭐")
            ).forEach { (value, label, emoji) ->
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(emoji, fontSize = 20.sp)
                        Text(value, style = MaterialTheme.typography.titleLarge, color = Blush, fontWeight = FontWeight.Bold)
                        Text(label, style = MaterialTheme.typography.labelSmall, color = TextHint)
                    }
                }
            }
        }

        // What makes us different
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "What Makes NatureNest Different",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(10.dp))
                listOf(
                    "🌍" to "Built for the African healthcare context",
                    "🔒" to "Your data never leaves your control",
                    "👩‍⚕️" to "Content reviewed by maternal health experts",
                    "📴" to "Core features work fully offline",
                    "💕" to "Designed with empathy, not just efficiency"
                ).forEach { (emoji, text) ->
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(emoji, fontSize = 18.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(text, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }
            }
        }

        // Legal links
        Text(
            "Legal",
            style = MaterialTheme.typography.labelLarge,
            color = TextHint,
            modifier = Modifier.padding(start = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column {
                legalLinks.forEachIndexed { index, link ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(link.icon, null, tint = Blush, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(link.title, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Medium)
                            Text(link.subtitle, style = MaterialTheme.typography.bodySmall, color = TextHint)
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = TextHint, modifier = Modifier.size(18.dp))
                    }
                    if (index < legalLinks.lastIndex) {
                        Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }

        // Footer
        Text(
            "Made with 💕 for every mama\nNatureNest © 2025 · All rights reserved",
            style = MaterialTheme.typography.bodySmall,
            color = TextHint,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
    }
}

// ─── Features tab ──────────────────────────────────────────────────────────────

@Composable
private fun FeaturesTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Everything in NatureNest",
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "A complete companion for your pregnancy and postpartum journey.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        featureHighlights.forEach { feature ->
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
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(BlushLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(feature.emoji, fontSize = 24.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            feature.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            feature.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Roadmap teaser
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🗺️", fontSize = 22.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("Coming Soon", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(8.dp))
                listOf(
                    "Contraction timer",
                    "Birth plan builder",
                    "Partner / support person mode",
                    "Clinic appointment booking"
                ).forEach { item ->
                    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Blush)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(item, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

// ─── Team tab ──────────────────────────────────────────────────────────────────

@Composable
private fun TeamTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Meet the Team",
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "NatureNest is built by a small team that genuinely cares about maternal wellbeing.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        teamMembers.forEach { member ->
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(colors = listOf(Blush, BlushDark)))
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            member.initials,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(member.name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text(member.role, style = MaterialTheme.typography.bodySmall, color = TextHint)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Contribute / contact card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = BlushLight.copy(alpha = 0.4f)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🤝", fontSize = 28.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Want to contribute?",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "We welcome maternal health experts, designers, and developers who share our vision.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Blush),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text("Get in Touch", color = Blush, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

// ─── Version badge ─────────────────────────────────────────────────────────────

@Composable
private fun VersionBadge(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SurfaceWhite,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.labelLarge, color = Blush, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextHint)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen(navController = rememberNavController())
}