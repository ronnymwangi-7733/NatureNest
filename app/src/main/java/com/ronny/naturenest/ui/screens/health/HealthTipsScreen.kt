package com.ronny.naturenest.ui.screens.health

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.models.NatureNestRepository
import com.ronny.naturenest.models.TipCategory
import com.ronny.naturenest.navigation.ROUT_ARTICLE
import com.ronny.naturenest.navigation.ROUT_HEALTH
import com.ronny.naturenest.ui.screens.components.HealthTipCard
import com.ronny.naturenest.ui.screens.components.NatureNestBottomBar
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

@Composable
fun HealthTipsScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUT_HEALTH
    val allTips = remember { NatureNestRepository.getHealthTips() }
    var selectedCategory by remember { mutableStateOf<TipCategory?>(null) }

    val filteredTips = if (selectedCategory == null) allTips
    else allTips.filter { it.category == selectedCategory }

    Scaffold(
        containerColor = SurfaceCream,
        topBar = { NatureNestTopBar(title = "Health Tips") },
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
            // Category filter row
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        "Browse by Category",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        // "All" chip
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                label = { Text("All ✨") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Blush,
                                    selectedLabelColor = androidx.compose.ui.graphics.Color.White,
                                    containerColor = SurfaceWhite,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedCategory == null,
                                    borderColor = CreamDark,
                                    selectedBorderColor = Blush
                                )
                            )
                        }
                        items(TipCategory.values()) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = {
                                    selectedCategory = if (selectedCategory == category) null else category
                                },
                                label = { Text("${category.emoji} ${category.label}") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Blush,
                                    selectedLabelColor = androidx.compose.ui.graphics.Color.White,
                                    containerColor = SurfaceWhite,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedCategory == category,
                                    borderColor = CreamDark,
                                    selectedBorderColor = Blush
                                )
                            )
                        }
                    }
                }
            }

            // Article count
            item {
                Text(
                    "${filteredTips.size} articles",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextHint,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            // Tip cards
            items(filteredTips, key = { it.id }) { tip ->
                HealthTipCard(
                    tip = tip,
                    onClick = { navController.navigate(ROUT_ARTICLE) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }

            // Expert advice banner
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SageLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "🩺 Ask an Expert",
                                style = MaterialTheme.typography.titleMedium,
                                color = SageDark,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Get personalized answers from certified midwives and OB-GYNs",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Sage),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    "Coming Soon",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = androidx.compose.ui.graphics.Color.White
                                )
                            }
                        }
                        Text("👩‍⚕️", fontSize = 52.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthTipsScreenPreview() {
    HealthTipsScreen(navController = rememberNavController())
}