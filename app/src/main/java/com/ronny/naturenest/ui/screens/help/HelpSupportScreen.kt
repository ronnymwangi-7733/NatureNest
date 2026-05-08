package com.ronny.naturenest.ui.screens.help

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

data class FaqItem(val question: String, val answer: String)

data class ContactOption(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val tint: Color
)

// ─── Static data ───────────────────────────────────────────────────────────────

private val faqList = listOf(
    FaqItem(
        "How do I track my pregnancy week?",
        "Go to My Profile → Pregnancy Details and update your last menstrual period (LMP) date. NatureNest will automatically calculate your current week and due date."
    ),
    FaqItem(
        "Can I add more than one baby profile?",
        "Yes! Navigate to Baby Profile from your profile screen. Tap the '+' icon at the top right to add a new baby. You can switch between profiles at any time."
    ),
    FaqItem(
        "How do I set medication or appointment reminders?",
        "Go to Profile → Reminder Settings. Tap 'Add Reminder', choose the type (medication, appointment, feeding, etc.), set the time, and save."
    ),
    FaqItem(
        "Is my health data stored securely?",
        "Yes. All your personal and health data is encrypted in transit and at rest. We never sell your data to third parties. See Privacy & Safety for full details."
    ),
    FaqItem(
        "How do I update my immunisation records?",
        "Open Baby Profile → Immunisation tab. Tap any vaccine row to toggle it as administered. Changes are saved automatically."
    ),
    FaqItem(
        "What is the difference between Baby Blues and PPD?",
        "Baby Blues typically resolve within 2 weeks after birth. Postpartum Depression (PPD) is more severe and persistent. Visit the Postpartum Support section for a detailed comparison."
    ),
    FaqItem(
        "How do I delete my account?",
        "To delete your account and all associated data, go to Profile → Privacy & Safety → Delete Account. This action is irreversible."
    ),
    FaqItem(
        "Can I use NatureNest offline?",
        "Core features like viewing your pregnancy details, baby profile, and saved tips work offline. Community features and live content require an internet connection."
    )
)

private val contactOptions = listOf(
    ContactOption(Icons.Outlined.Email, "Email Support", "support@naturenest.app", Blush),
    ContactOption(Icons.Outlined.Chat, "Live Chat", "Available 8am – 8pm EAT", Color(0xFF4CAF50)),
    ContactOption(Icons.Outlined.Forum, "Community Forum", "Ask the NatureNest community", Color(0xFF7B61FF)),
    ContactOption(Icons.Outlined.Article, "Documentation", "Browse full help articles", Color(0xFF2196F3))
)

// ─── HelpSupportScreen ─────────────────────────────────────────────────────────

@Composable
fun HelpSupportScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("FAQs", "Contact Us", "Send Feedback")

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "Help & Support"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        Icon(Icons.Outlined.HelpOutline, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "How can we help?",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Browse FAQs, contact support, or send us feedback.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
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
                0 -> FaqTab()
                1 -> ContactTab()
                2 -> FeedbackTab()
            }
        }
    }
}

// ─── FAQ tab ───────────────────────────────────────────────────────────────────

@Composable
private fun FaqTab() {
    val expandedIndex = remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            "Frequently Asked Questions",
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        faqList.forEachIndexed { index, faq ->
            val isExpanded = expandedIndex.value == index
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedIndex.value = if (isExpanded) null else index
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpanded) BlushLight.copy(alpha = 0.3f) else SurfaceWhite
                ),
                elevation = CardDefaults.cardElevation(if (isExpanded) 0.dp else 1.dp),
                border = if (isExpanded) BorderStroke(1.dp, Blush.copy(alpha = 0.3f)) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isExpanded) Blush else BlushLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Q",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isExpanded) Color.White else Blush,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            faq.question,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            null,
                            tint = TextHint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (isExpanded) {
                        Spacer(Modifier.height(10.dp))
                        Divider(color = Blush.copy(alpha = 0.15f))
                        Spacer(Modifier.height(10.dp))
                        Text(
                            faq.answer,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

// ─── Contact tab ───────────────────────────────────────────────────────────────

@Composable
private fun ContactTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Get in Touch",
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Our team is here to help. Choose the channel that works best for you.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        contactOptions.forEach { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(option.tint.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(option.icon, null, tint = option.tint, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            option.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            option.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextHint
                        )
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = TextHint, modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Response time card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = LavenderLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("⏱️", fontSize = 24.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Typical Response Time",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Email: within 24 hours  •  Live chat: under 5 minutes",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

// ─── Feedback tab ──────────────────────────────────────────────────────────────

@Composable
private fun FeedbackTab() {
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var submitted by remember { mutableStateOf(false) }

    val categories = listOf("Bug Report", "Feature Request", "Content Issue", "Account Help", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (submitted) {
            // Success state
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("✅", fontSize = 40.sp)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Feedback Sent!",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Thank you for helping us improve NatureNest. We'll review your message and get back to you if needed.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            subject = ""; message = ""; selectedCategory = null; submitted = false
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Blush)
                    ) {
                        Text("Send Another", color = Blush)
                    }
                }
            }
        } else {
            Text(
                "Send Feedback",
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Your feedback helps us build a better experience for every mama.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            // Category chips
            Text("Category", style = MaterialTheme.typography.labelMedium, color = TextHint)
            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { i ->
                    val cat = categories[i]
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, style = MaterialTheme.typography.labelMedium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Blush,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceWhite,
                            labelColor = TextHint
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Subject
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject", style = MaterialTheme.typography.labelMedium) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blush,
                    unfocusedBorderColor = BlushLight,
                    focusedLabelColor = Blush
                ),
                singleLine = true
            )

            // Message
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message", style = MaterialTheme.typography.labelMedium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blush,
                    unfocusedBorderColor = BlushLight,
                    focusedLabelColor = Blush
                ),
                maxLines = 6
            )

            Button(
                onClick = { if (subject.isNotBlank() && message.isNotBlank()) submitted = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blush),
                enabled = subject.isNotBlank() && message.isNotBlank()
            ) {
                Icon(Icons.Default.Send, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Submit Feedback", style = MaterialTheme.typography.labelLarge)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HelpSupportScreenPreview() {
    HelpSupportScreen(navController = rememberNavController())
}