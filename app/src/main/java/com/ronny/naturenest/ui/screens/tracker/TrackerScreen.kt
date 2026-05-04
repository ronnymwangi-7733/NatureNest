package com.ronny.naturenest.ui.screens.tracker

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.models.NatureNestRepository
import com.ronny.naturenest.navigation.ROUT_TRACKER
import com.ronny.naturenest.ui.screens.components.NatureNestBottomBar
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

@Composable
fun TrackerScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUT_TRACKER

    var selectedWeek by remember { mutableIntStateOf(20) }
    val weekInfo = remember(selectedWeek) { NatureNestRepository.getPregnancyWeekInfo(selectedWeek) }
    val totalWeeks = 40

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "Pregnancy Tracker",
                showBack = false
            )
        },
        bottomBar = {
            NatureNestBottomBar(
                currentRoute = currentRoute,
                onNavigate = { navController.navigate(it) { launchSingleTop = true } }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // Week selector header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(colors = listOf(BlushLight, SurfaceCream))
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            "Week $selectedWeek of $totalWeeks",
                            style = MaterialTheme.typography.displaySmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Tap a week to explore your baby's growth",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(Modifier.height(16.dp))

                        // Week scroll selector
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items((1..40).toList()) { week ->
                                val isSelected = week == selectedWeek
                                val trimesterColor = when {
                                    week <= 13 -> Blush
                                    week <= 27 -> Sage
                                    else -> Peach
                                }
                                Surface(
                                    onClick = { selectedWeek = week },
                                    shape = CircleShape,
                                    color = if (isSelected) trimesterColor else if (week < selectedWeek) trimesterColor.copy(0.25f) else CreamDark,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            "$week",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (isSelected) Color.White else if (week < selectedWeek) trimesterColor else TextHint,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Trimester legend
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            listOf(
                                Triple("1st (1-13)", Blush, selectedWeek <= 13),
                                Triple("2nd (14-27)", Sage, selectedWeek in 14..27),
                                Triple("3rd (28-40)", Peach, selectedWeek > 27)
                            ).forEach { (label, color, isActive) ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isActive) TextPrimary else TextHint,
                                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Baby size card
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(colors = listOf(Blush, BlushDark))
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Your Baby This Week",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White.copy(0.85f)
                                )
                                Text(
                                    weekInfo.emoji,
                                    fontSize = 48.sp
                                )
                                Text(
                                    "Size of a ${weekInfo.babySize}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Column {
                                        Text("Weight", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.7f))
                                        Text(weekInfo.babyWeight, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                                    }
                                    Column {
                                        Text("Length", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.7f))
                                        Text(weekInfo.babyLength, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Baby Development
            item {
                InfoSection(
                    title = "👶 Baby's Development",
                    content = weekInfo.babyDevelopment,
                    backgroundColor = BlushLight,
                    textColor = BlushDark
                )
            }

            // Mom's Changes
            item {
                InfoSection(
                    title = "🌸 Your Body This Week",
                    content = weekInfo.momChanges,
                    backgroundColor = SageLight,
                    textColor = SageDark
                )
            }

            // Tips
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        "💡 Tips for Week $selectedWeek",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    weekInfo.tips.forEachIndexed { index, tip ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(PeachLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${index + 1}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PeachDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(
                                tip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                modifier = Modifier.weight(1f),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            // Milestones checklist placeholder
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LavenderLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📋", fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Prenatal Checklist", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Track your prenatal appointments & tests", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = TextHint)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: String,
    backgroundColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackerScreenPreview() {
    TrackerScreen(navController = rememberNavController())
}