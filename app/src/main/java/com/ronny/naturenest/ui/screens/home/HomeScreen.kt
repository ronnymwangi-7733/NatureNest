package com.ronny.naturenest.ui.screens.home

import com.ronny.naturenest.models.NatureNestRepository
import com.ronny.naturenest.navigation.ROUT_HOME
import kotlin.collections.take


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
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
import com.ronny.naturenest.navigation.ROUT_ARTICLE
import com.ronny.naturenest.navigation.ROUT_COMMUNITY
import com.ronny.naturenest.navigation.ROUT_HEALTH
import com.ronny.naturenest.navigation.ROUT_PROFILE
import com.ronny.naturenest.navigation.ROUT_REMINDER
import com.ronny.naturenest.navigation.ROUT_TRACKER
import com.ronny.naturenest.ui.screens.components.CommunityPostCard
import com.ronny.naturenest.ui.screens.components.HealthTipCard
import com.ronny.naturenest.ui.screens.components.NatureNestBottomBar
import com.ronny.naturenest.ui.screens.components.QuickActionChip
import com.ronny.naturenest.ui.screens.components.ReminderCard
import com.ronny.naturenest.ui.screens.components.SectionHeader
import com.ronny.naturenest.ui.screens.components.WeekProgressCard
import com.ronny.naturenest.ui.theme.*


@Composable
fun HomeScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUT_HOME

    val tips = remember { NatureNestRepository.getHealthTips().take(3) }
    val reminders = remember { NatureNestRepository.getReminders().take(3) }
    val posts = remember { NatureNestRepository.getCommunityPosts().take(2) }

    Scaffold(
        containerColor = SurfaceCream,
        bottomBar = {
            NatureNestBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route -> navController.navigate(route) { launchSingleTop = true } }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Top greeting header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(colors = listOf(BlushLight, SurfaceCream))
                        )
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Good morning, 🌸",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextSecondary
                                )
                                Text(
                                    "Amina!",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(BlushLight)
                                    .border(2.dp, Blush, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("AM", style = MaterialTheme.typography.titleMedium, color = BlushDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Pregnancy Week Card
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    WeekProgressCard(currentWeek = 20)
                }
            }

            // Quick Actions
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    SectionHeader(title = "Quick Actions")
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionChip("📅", "Tracker", { navController.navigate(ROUT_TRACKER) }, BlushLight, BlushDark)
                        QuickActionChip("💊", "Reminders", { navController.navigate(ROUT_REMINDER) }, SageLight, SageDark)
                        QuickActionChip("🥗", "Nutrition", { navController.navigate(ROUT_PROFILE) }, PeachLight, PeachDark)
                        QuickActionChip("💬", "Community", { navController.navigate(ROUT_COMMUNITY) }, LavenderLight, DeepBrown)
                    }
                }
            }

            // Today's reminders
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(
                        title = "Today's Reminders",
                        onAction = { navController.navigate(ROUT_REMINDER) }
                    )
                }
            }
            items(reminders) { reminder ->
                var isChecked by remember { mutableStateOf(reminder.isCompleted) }
                ReminderCard(
                    reminder = reminder.copy(isCompleted = isChecked),
                    onToggle = { isChecked = !isChecked },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }

            // Daily tip banner
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(colors = listOf(Sage, SageDark))
                            )
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("💡 Tip of the Day", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(0.8f))
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Staying hydrated during pregnancy helps maintain amniotic fluid and supports nutrient transport to your baby.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    lineHeight = 18.sp
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text("💧", fontSize = 36.sp)
                        }
                    }
                }
            }

            // Health Tips section
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader(
                        title = "For You 📖",
                        onAction = { navController.navigate(ROUT_HEALTH) }
                    )
                }
            }
            items(tips) { tip ->
                HealthTipCard(
                    tip = tip,
                    onClick = { navController.navigate(ROUT_ARTICLE) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }

            // Community section
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    SectionHeader(
                        title = "Mom Community 💕",
                        onAction = { navController.navigate(ROUT_COMMUNITY) }
                    )
                }
            }
            items(posts) { post ->
                var isLiked by remember { mutableStateOf(post.isLiked) }
                CommunityPostCard(
                    post = post.copy(isLiked = isLiked),
                    onLike = { isLiked = !isLiked },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}