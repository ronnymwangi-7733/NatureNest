package com.ronny.naturenest.ui.screens.components

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
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.ronny.naturenest.models.CommunityPost
import com.ronny.naturenest.models.HealthTip
import com.ronny.naturenest.models.Reminder
import com.ronny.naturenest.models.ReminderType
import com.ronny.naturenest.models.TipCategory
import com.ronny.naturenest.ui.theme.*

// ─── Bottom Navigation Bar ───────────────────────────────────────────────────

data class BottomNavItem(
    val label: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
    val route: String
)

@Composable
fun NatureNestBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home",
            { Icon(Icons.Filled.Home, null, tint = Blush) },
            { Icon(Icons.Outlined.Home, null, tint = TextHint) },
            "home"
        ),
        BottomNavItem("Tracker",
            { Icon(Icons.Filled.FavoriteBorder, null, tint = Blush) },
            { Icon(Icons.Outlined.FavoriteBorder, null, tint = TextHint) },
            "tracker"
        ),
        BottomNavItem("Tips",
            { Icon(Icons.Filled.Lightbulb, null, tint = Blush) },
            { Icon(Icons.Outlined.Lightbulb, null, tint = TextHint) },
            "health_tips"
        ),
        BottomNavItem("Community",
            { Icon(Icons.Filled.People, null, tint = Blush) },
            { Icon(Icons.Outlined.People, null, tint = TextHint) },
            "community"
        ),
        BottomNavItem("Profile",
            { Icon(Icons.Filled.Person, null, tint = Blush) },
            { Icon(Icons.Outlined.Person, null, tint = TextHint) },
            "profile"
        )
    )

    NavigationBar(
        containerColor = SurfaceWhite,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { if (selected) item.selectedIcon() else item.unselectedIcon() },
                label = {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) Blush else TextHint,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = BlushLight,
                    selectedIconColor = Blush,
                    unselectedIconColor = TextHint
                )
            )
        }
    }
}

// ─── Section Header ───────────────────────────────────────────────────────────

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String = "See all",
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        if (onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = Blush
                )
            }
        }
    }
}

// ─── Health Tip Card ─────────────────────────────────────────────────────────

@Composable
fun HealthTipCard(
    tip: HealthTip,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColors = mapOf(
        TipCategory.NUTRITION to Pair(SageLight, SageDark),
        TipCategory.EXERCISE to Pair(PeachLight, PeachDark),
        TipCategory.MENTAL_HEALTH to Pair(LavenderLight, LavenderSoft),
        TipCategory.BABY_CARE to Pair(BlushLight, BlushDark),
        TipCategory.POSTPARTUM to Pair(PeachLight, Blush),
        TipCategory.PRENATAL to Pair(BlushLight, BlushDark)
    )
    val (bgColor, accentColor) = categoryColors[tip.category] ?: Pair(BlushLight, BlushDark)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(tip.category.emoji, fontSize = 24.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = bgColor,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        tip.category.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    tip.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        tip.readTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint
                    )
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextHint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─── Reminder Card ────────────────────────────────────────────────────────────

@Composable
fun ReminderCard(
    reminder: Reminder,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typeColors = mapOf(
        ReminderType.CLINIC_VISIT to Pair(SageLight, SageDark),
        ReminderType.MEDICATION to Pair(BlushLight, BlushDark),
        ReminderType.NUTRITION to Pair(PeachLight, PeachDark),
        ReminderType.EXERCISE to Pair(LavenderLight, LavenderSoft),
        ReminderType.SUPPLEMENT to Pair(SageLight, SageDark)
    )
    val (bgColor, accentColor) = typeColors[reminder.type] ?: Pair(BlushLight, BlushDark)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isCompleted) Color(0xFFF5F5F5) else SurfaceWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (reminder.isCompleted) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (reminder.isCompleted) Color(0xFFEEEEEE) else bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    reminder.type.emoji,
                    fontSize = 20.sp,
                    modifier = Modifier.alpha(if (reminder.isCompleted) 0.5f else 1f)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (reminder.isCompleted) TextHint else TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (reminder.isCompleted)
                        androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Schedule, null, tint = if (reminder.isCompleted) TextHint else accentColor, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${reminder.time} · ${reminder.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (reminder.isCompleted) TextHint else accentColor
                    )
                }
            }
            Checkbox(
                checked = reminder.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Sage,
                    uncheckedColor = TextHint
                )
            )
        }
    }
}

// ─── Community Post Card ──────────────────────────────────────────────────────

@Composable
fun CommunityPostCard(
    post: CommunityPost,
    onLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarColors = listOf(BlushLight, SageLight, PeachLight, LavenderLight, CreamDark)
    val avatarTextColors = listOf(BlushDark, SageDark, PeachDark, LavenderSoft, WarmBrown)
    val colorIndex = post.id % avatarColors.size

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(avatarColors[colorIndex]),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        post.authorInitials,
                        style = MaterialTheme.typography.titleSmall,
                        color = avatarTextColors[colorIndex],
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        post.authorName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${post.authorWeek} · ${post.timeAgo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextHint
                    )
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    color = BlushLight,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        post.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = BlushDark
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = CreamDark, thickness = 1.dp)
            Spacer(Modifier.height(8.dp))
            Row {
                TextButton(
                    onClick = onLike,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        null,
                        tint = if (post.isLiked) Blush else TextHint,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${post.likes}",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (post.isLiked) Blush else TextHint
                    )
                }
                Spacer(Modifier.width(4.dp))
                TextButton(
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Outlined.ChatBubbleOutline, null, tint = TextHint, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${post.comments}", style = MaterialTheme.typography.labelMedium, color = TextHint)
                }
            }
        }
    }
}

// ─── Week Progress Card ───────────────────────────────────────────────────────

@Composable
fun WeekProgressCard(
    currentWeek: Int,
    totalWeeks: Int = 40,
    modifier: Modifier = Modifier
) {
    val progress = currentWeek.toFloat() / totalWeeks.toFloat()
    val trimester = when {
        currentWeek <= 13 -> "1st Trimester"
        currentWeek <= 27 -> "2nd Trimester"
        else -> "3rd Trimester"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Blush, BlushDark),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            "Week $currentWeek",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            trimester,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                    Text("🤰", fontSize = 48.sp)
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "${totalWeeks - currentWeek} weeks to go",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

// ─── Quick Action Button ──────────────────────────────────────────────────────

@Composable
fun QuickActionChip(
    emoji: String,
    label: String,
    onClick: () -> Unit,
    backgroundColor: Color = BlushLight,
    textColor: Color = BlushDark
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─── Top App Bar ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NatureNestTopBar(
    title: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = if (showBack) {{
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = TextPrimary)
            }
        }} else {{}},
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceCream
        )
    )
}