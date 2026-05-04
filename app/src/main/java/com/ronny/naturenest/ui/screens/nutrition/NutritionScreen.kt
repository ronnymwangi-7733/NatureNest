package com.ronny.naturenest.ui.screens.nutrition

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
import com.ronny.naturenest.models.NatureNestRepository
import com.ronny.naturenest.navigation.ROUT_NUTRITION
import com.ronny.naturenest.ui.screens.components.NatureNestBottomBar
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.screens.components.SectionHeader
import com.ronny.naturenest.ui.theme.*

@Composable
fun NutritionScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUT_NUTRITION

    val nutritionItems = remember { NatureNestRepository.getNutritionData() }

    val mealSuggestions = listOf(
        Triple("🥣", "Breakfast", "Oatmeal with berries + prenatal vitamin"),
        Triple("🥗", "Lunch", "Spinach salad, lentil soup, whole grain bread"),
        Triple("🍎", "Snack", "Apple slices with almond butter + water"),
        Triple("🍗", "Dinner", "Grilled chicken, sweet potato, steamed broccoli"),
        Triple("🥛", "Evening", "Warm milk with honey + calcium supplement")
    )

    Scaffold(
        containerColor = SurfaceCream,
        topBar = { NatureNestTopBar(title = "Nutrition & Wellness") },
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
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(colors = listOf(PeachLight, SurfaceCream))
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Column {
                        Text(
                            "Today's Nutrition",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Week 20 · Tailored for your trimester",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Daily intake trackers
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    SectionHeader(title = "Daily Goals 🎯")
                    Spacer(Modifier.height(12.dp))

                    nutritionItems.forEach { item ->
                        val progress = (item.current / item.target).coerceIn(0f, 1f)
                        val barColor = Color(item.color)

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    item.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "${item.amount.toInt()}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = barColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        " / ${item.target.toInt()} ${item.unit}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextHint
                                    )
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = barColor,
                                trackColor = CreamDark
                            )
                        }
                    }
                }
            }

            // Hydration reminder
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("💧 Hydration", style = MaterialTheme.typography.titleMedium, color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)
                            Text("6 of 8 glasses today", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                (1..8).forEach { i ->
                                    Text(if (i <= 6) "💧" else "○", fontSize = 16.sp)
                                }
                            }
                        }
                        TextButton(onClick = {}) {
                            Text("+1 Glass", color = Color(0xFF1565C0), style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            // Meal plan section
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    SectionHeader(title = "Today's Meal Plan 🍽️")
                }
            }
            items(mealSuggestions) { (emoji, mealTime, suggestion) ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(emoji, fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                mealTime,
                                style = MaterialTheme.typography.labelMedium,
                                color = Peach,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                suggestion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Key nutrients info
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SageLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "🌿 Key Nutrients for Week 20",
                            style = MaterialTheme.typography.titleMedium,
                            color = SageDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        listOf(
                            "🫀 Iron (27mg/day) — supports increased blood volume",
                            "🧠 DHA (200mg/day) — baby's brain development",
                            "🦴 Calcium (1000mg/day) — bone & teeth formation",
                            "🌟 Folic Acid (600mcg/day) — neural tube support"
                        ).forEach { note ->
                            Text(
                                note,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 3.dp),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionScreenPreview() {
    NutritionScreen(navController = rememberNavController())
}