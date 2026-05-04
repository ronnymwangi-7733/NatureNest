package com.ronny.naturenest.ui.screens.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.navigation.ROUT_LOGIN
import com.ronny.naturenest.navigation.ROUT_PROFILE
import com.ronny.naturenest.ui.screens.components.NatureNestBottomBar
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUT_PROFILE

    Scaffold(
        containerColor = SurfaceCream,
        topBar = { NatureNestTopBar(title = "My Profile") },
        bottomBar = {
            NatureNestBottomBar(
                currentRoute = currentRoute,
                onNavigate = { navController.navigate(it) { launchSingleTop = true } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(colors = listOf(BlushLight, SurfaceCream))
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(colors = listOf(Blush, BlushDark))
                            )
                            .border(3.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "AM",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Amina Mwangi",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Expecting Mama · Week 20",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Blush),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Outlined.Edit, null, tint = Blush, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Edit Profile", color = Blush, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    Triple("20", "Weeks", "🤰"),
                    Triple("Feb 14", "Due Date", "💕"),
                    Triple("2nd", "Trimester", "🌸")
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
                            Text(
                                value,
                                style = MaterialTheme.typography.titleLarge,
                                color = Blush,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextHint
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Postpartum support section
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LavenderLight),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💆", fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Postpartum Support",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Resources for recovery, baby blues, and maternal wellbeing",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = TextHint)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Settings sections
            ProfileSection(title = "My Pregnancy") {
                ProfileItem(Icons.Outlined.CalendarToday, "Pregnancy Details", "Week 20 · Due Feb 14")
                ProfileItem(Icons.Outlined.ChildCare, "Baby Profile", "Set up baby's profile")
                ProfileItem(Icons.Outlined.Favorite, "Health Records", "Track vitals & notes")
            }

            Spacer(Modifier.height(8.dp))

            ProfileSection(title = "Notifications") {
                ProfileItem(Icons.Outlined.Notifications, "Reminder Settings", "6 active reminders")
                ProfileItem(Icons.Outlined.Campaign, "Community Alerts", "Mentions & replies")
            }

            Spacer(Modifier.height(8.dp))

            ProfileSection(title = "App") {
                ProfileItem(Icons.Outlined.Lock, "Privacy & Safety", "")
                ProfileItem(Icons.Outlined.HelpOutline, "Help & Support", "")
                ProfileItem(Icons.Outlined.Info, "About NatureNest", "Version 1.0.0")
            }

            Spacer(Modifier.height(8.dp))

            // Sign out
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(ROUT_LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Icon(Icons.Outlined.Logout, null, tint = ErrorRed, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Sign Out",
                        style = MaterialTheme.typography.labelLarge,
                        color = ErrorRed
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "NatureNest · Nurturing life, every step of the way 🌿",
                style = MaterialTheme.typography.bodySmall,
                color = TextHint,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
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

@Composable
fun ProfileItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Blush, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        if (value.isNotEmpty()) {
            Text(value, style = MaterialTheme.typography.bodySmall, color = TextHint)
            Spacer(Modifier.width(4.dp))
        }
        Icon(Icons.Default.ChevronRight, null, tint = TextHint, modifier = Modifier.size(18.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}