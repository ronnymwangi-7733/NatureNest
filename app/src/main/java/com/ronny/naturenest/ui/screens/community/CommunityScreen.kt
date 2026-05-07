package com.ronny.naturenest.ui.screens.community

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.models.NatureNestRepository
import com.ronny.naturenest.navigation.ROUT_COMMUNITY
import com.ronny.naturenest.ui.screens.components.CommunityPostCard
import com.ronny.naturenest.ui.screens.components.NatureNestBottomBar
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

@Composable
fun CommunityScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUT_COMMUNITY

    var posts by remember { mutableStateOf(NatureNestRepository.getCommunityPosts()) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Recent", "Trending 🔥", "My Posts")

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "Community",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = Blush,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Edit, "New Post")
            }
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
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Welcome banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(colors = listOf(LavenderLight, SurfaceCream))
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Column {
                        Text(
                            "A safe space for mamas 💕",
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Share your journey, ask questions, support each other",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(Modifier.height(12.dp))

                        // Community stats
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            StatChip("12.4K", "Moms", "👩")
                            StatChip("847", "Posts today", "📝")
                            StatChip("4.8⭐", "Rating", "")
                        }
                    }
                }
            }

            // Tab row
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SurfaceCream,
                    contentColor = Blush,
                    indicator = { tabPositions ->
                        if (selectedTab < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = Blush
                            )
                        }
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
                                    color = if (selectedTab == index) Blush else TextSecondary
                                )
                            }
                        )
                    }
                }
            }

            // Community guidelines card
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SageLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🛡️", fontSize = 20.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "This is a moderated, judgment-free community. Be kind, supportive, and always consult your doctor for medical advice.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Posts
            items(posts, key = { it.id }) { post ->
                var isLiked by remember { mutableStateOf(post.isLiked) }
                CommunityPostCard(
                    post = post.copy(
                        isLiked = isLiked,
                        likes = if (isLiked && !post.isLiked) post.likes + 1 else post.likes
                    ),
                    onLike = { isLiked = !isLiked },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun StatChip(value: String, label: String, emoji: String) {
    Surface(
        color = SurfaceWhite,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "$emoji $value",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
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

@Preview(showBackground = true)
@Composable
fun CommunityScreenPreview() {
    CommunityScreen(navController = rememberNavController())
}


