package com.ronny.naturenest.ui.screens.baby

import androidx.compose.animation.core.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.theme.*

// ─── Data models ───────────────────────────────────────────────────────────────

data class GrowthEntry(val ageMonths: Int, val weightKg: Float, val heightCm: Float)

data class ImmunisationItem(
    val vaccine: String,
    val ageLabel: String,
    val diseases: String,
    val isDue: Boolean,
    val isDone: Boolean
)

// ─── Static data ───────────────────────────────────────────────────────────────

private val defaultGrowthData = listOf(
    GrowthEntry(0, 3.2f, 50f),
    GrowthEntry(1, 4.5f, 54f),
    GrowthEntry(2, 5.6f, 57f),
    GrowthEntry(4, 6.7f, 62f),
    GrowthEntry(6, 7.9f, 67f),
    GrowthEntry(9, 9.2f, 72f),
    GrowthEntry(12, 10.1f, 76f)
)

private val immunisationSchedule = listOf(
    ImmunisationItem("BCG", "At Birth", "Tuberculosis", isDue = false, isDone = true),
    ImmunisationItem("OPV 0", "At Birth", "Polio", isDue = false, isDone = true),
    ImmunisationItem("Hepatitis B (1st)", "At Birth", "Hepatitis B", isDue = false, isDone = true),
    ImmunisationItem("DPT-HepB-Hib (1st)", "6 Weeks", "Diphtheria, Pertussis, Tetanus, Hep B, Hib", isDue = false, isDone = true),
    ImmunisationItem("OPV 1", "6 Weeks", "Polio", isDue = false, isDone = true),
    ImmunisationItem("PCV (1st)", "6 Weeks", "Pneumococcal disease", isDue = false, isDone = true),
    ImmunisationItem("Rotavirus (1st)", "6 Weeks", "Rotavirus diarrhoea", isDue = false, isDone = true),
    ImmunisationItem("DPT-HepB-Hib (2nd)", "10 Weeks", "Diphtheria, Pertussis, Tetanus, Hep B, Hib", isDue = false, isDone = true),
    ImmunisationItem("OPV 2", "10 Weeks", "Polio", isDue = false, isDone = true),
    ImmunisationItem("PCV (2nd)", "10 Weeks", "Pneumococcal disease", isDue = false, isDone = true),
    ImmunisationItem("Rotavirus (2nd)", "10 Weeks", "Rotavirus diarrhoea", isDue = false, isDone = true),
    ImmunisationItem("DPT-HepB-Hib (3rd)", "14 Weeks", "Diphtheria, Pertussis, Tetanus, Hep B, Hib", isDue = true, isDone = false),
    ImmunisationItem("OPV 3", "14 Weeks", "Polio", isDue = true, isDone = false),
    ImmunisationItem("PCV (3rd)", "14 Weeks", "Pneumococcal disease", isDue = true, isDone = false),
    ImmunisationItem("Measles-Rubella (1st)", "9 Months", "Measles, Rubella", isDue = false, isDone = false),
    ImmunisationItem("Yellow Fever", "9 Months", "Yellow Fever", isDue = false, isDone = false),
    ImmunisationItem("Vitamin A (1st)", "6 Months", "Vitamin A deficiency", isDue = false, isDone = false),
    ImmunisationItem("Measles-Rubella (2nd)", "18 Months", "Measles, Rubella (booster)", isDue = false, isDone = false),
    ImmunisationItem("DPT (Booster)", "18 Months", "Diphtheria, Pertussis, Tetanus (booster)", isDue = false, isDone = false),
    ImmunisationItem("OPV 4 (Booster)", "18 Months", "Polio (booster)", isDue = false, isDone = false)
)

// ─── BabyProfileScreen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyProfileScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Overview", "Growth", "Immunisation")

    // Editable baby info state
    var babyName by remember { mutableStateOf("Baby Amara") }
    var babyAge by remember { mutableStateOf("3 months") }
    var babyWeight by remember { mutableStateOf("6.2 kg") }
    var babyHeight by remember { mutableStateOf("61 cm") }
    var babyBloodGroup by remember { mutableStateOf("A+") }
    var showEditDialog by remember { mutableStateOf(false) }

    // Growth data state — user can add entries
    val growthData = remember { mutableStateListOf(*defaultGrowthData.toTypedArray()) }
    var showAddGrowthDialog by remember { mutableStateOf(false) }

    // Immunisation done state
    val immunisationState = remember {
        mutableStateMapOf<String, Boolean>().apply {
            immunisationSchedule.forEach { put(it.vaccine, it.isDone) }
        }
    }

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "Baby Profile"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Baby header card
            BabyHeaderCard(
                name = babyName,
                age = babyAge,
                weight = babyWeight,
                height = babyHeight,
                bloodGroup = babyBloodGroup,
                onEditClick = { showEditDialog = true }
            )

            // Tab row
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

            // Tab content
            when (selectedTab) {
                0 -> BabyOverviewTab(
                    name = babyName,
                    age = babyAge,
                    immunisationState = immunisationState
                )

                1 -> BabyGrowthTab(
                    growthData = growthData,
                    onAddEntry = { showAddGrowthDialog = true }
                )

                2 -> BabyImmunisationTab(
                    immunisationState = immunisationState,
                    onToggle = { vaccine -> immunisationState[vaccine] = !(immunisationState[vaccine] ?: false) }
                )
            }
        }
    }

    // ── Edit baby info dialog ──
    if (showEditDialog) {
        var draftName by remember { mutableStateOf(babyName) }
        var draftAge by remember { mutableStateOf(babyAge) }
        var draftWeight by remember { mutableStateOf(babyWeight) }
        var draftHeight by remember { mutableStateOf(babyHeight) }
        var draftBlood by remember { mutableStateOf(babyBloodGroup) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Edit Baby Profile", style = MaterialTheme.typography.titleMedium, color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    BabyTextField("Name", draftName) { draftName = it }
                    BabyTextField("Age", draftAge) { draftAge = it }
                    BabyTextField("Weight", draftWeight) { draftWeight = it }
                    BabyTextField("Height", draftHeight) { draftHeight = it }
                    BabyTextField("Blood Group", draftBlood) { draftBlood = it }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    babyName = draftName; babyAge = draftAge
                    babyWeight = draftWeight; babyHeight = draftHeight
                    babyBloodGroup = draftBlood
                    showEditDialog = false
                }) { Text("Save", color = Blush) }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancel", color = TextHint) }
            }
        )
    }

    // ── Add growth entry dialog ──
    if (showAddGrowthDialog) {
        var draftMonths by remember { mutableStateOf("") }
        var draftWeight by remember { mutableStateOf("") }
        var draftHeight by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddGrowthDialog = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Add Growth Entry", style = MaterialTheme.typography.titleMedium, color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    BabyTextField("Age (months)", draftMonths) { draftMonths = it }
                    BabyTextField("Weight (kg)", draftWeight) { draftWeight = it }
                    BabyTextField("Height (cm)", draftHeight) { draftHeight = it }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val months = draftMonths.toIntOrNull()
                    val kg = draftWeight.toFloatOrNull()
                    val cm = draftHeight.toFloatOrNull()
                    if (months != null && kg != null && cm != null) {
                        growthData.add(GrowthEntry(months, kg, cm))
                        growthData.sortBy { it.ageMonths }
                    }
                    showAddGrowthDialog = false
                }) { Text("Add", color = Blush) }
            },
            dismissButton = {
                TextButton(onClick = { showAddGrowthDialog = false }) { Text("Cancel", color = TextHint) }
            }
        )
    }
}

// ─── Baby header ───────────────────────────────────────────────────────────────

@Composable
private fun BabyHeaderCard(
    name: String,
    age: String,
    weight: String,
    height: String,
    bloodGroup: String,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(colors = listOf(BlushLight, SurfaceCream)))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(colors = listOf(Blush, BlushDark)))
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("👶", fontSize = 36.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(name, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(age, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BabyStatChip("⚖️", weight, "Weight")
                BabyStatChip("📏", height, "Height")
                BabyStatChip("🩸", bloodGroup, "Blood")
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onEditClick,
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
}

@Composable
private fun BabyStatChip(emoji: String, value: String, label: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 16.sp)
            Text(value, style = MaterialTheme.typography.labelLarge, color = Blush, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextHint)
        }
    }
}

// ─── Overview tab ──────────────────────────────────────────────────────────────

@Composable
private fun BabyOverviewTab(
    name: String,
    age: String,
    immunisationState: Map<String, Boolean>
) {
    val doneCount = immunisationState.values.count { it }
    val totalCount = immunisationState.size
    val progress = if (totalCount > 0) doneCount.toFloat() / totalCount else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Next vaccine due
        val nextDue = immunisationSchedule.firstOrNull { it.isDue && immunisationState[it.vaccine] == false }
        if (nextDue != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LavenderLight),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💉", fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Next Vaccine Due",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextHint
                        )
                        Text(
                            nextDue.vaccine,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            nextDue.ageLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Immunisation progress
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Immunisation Progress", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Text("$doneCount / $totalCount", style = MaterialTheme.typography.labelLarge, color = Blush)
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Blush,
                    trackColor = BlushLight
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${(progress * 100).toInt()}% complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextHint
                )
            }
        }

        // Quick milestones
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Developmental Milestones", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
                listOf(
                    "😊" to "Social smiling",
                    "👁️" to "Tracking objects with eyes",
                    "👂" to "Responding to sounds",
                    "🤲" to "Grasping objects",
                    "🗣️" to "Cooing & babbling"
                ).forEach { (emoji, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(emoji, fontSize = 20.sp)
                        Spacer(Modifier.width(12.dp))
                        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.CheckCircle, null, tint = Blush, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

// ─── Growth tab ────────────────────────────────────────────────────────────────

@Composable
private fun BabyGrowthTab(
    growthData: List<GrowthEntry>,
    onAddEntry: () -> Unit
) {
    var activeChart by remember { mutableIntStateOf(0) } // 0 = weight, 1 = height

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Chart selector
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Weight (kg)", "Height (cm)").forEachIndexed { index, label ->
                FilterChip(
                    selected = activeChart == index,
                    onClick = { activeChart = index },
                    label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Blush,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceWhite,
                        labelColor = TextHint
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            OutlinedButton(
                onClick = onAddEntry,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Blush),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Blush, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Add", color = Blush, style = MaterialTheme.typography.labelMedium)
            }
        }

        // Growth chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    if (activeChart == 0) "Weight Over Time" else "Height Over Time",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (activeChart == 0) "kg" else "cm",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextHint
                )
                Spacer(Modifier.height(12.dp))
                GrowthLineChart(
                    data = growthData,
                    valueSelector = if (activeChart == 0) { e -> e.weightKg } else { e -> e.heightCm },
                    lineColor = Blush,
                    fillColor = BlushLight
                )
            }
        }

        // Data table
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Growth Records", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))

                // Header
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Age", style = MaterialTheme.typography.labelMedium, color = TextHint, modifier = Modifier.weight(1f))
                    Text("Weight", style = MaterialTheme.typography.labelMedium, color = TextHint, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("Height", style = MaterialTheme.typography.labelMedium, color = TextHint, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                }
                Divider(modifier = Modifier.padding(vertical = 6.dp), color = BlushLight)

                growthData.forEach { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            "${entry.ageMonths}m",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "${entry.weightKg} kg",
                            style = MaterialTheme.typography.bodySmall,
                            color = Blush,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "${entry.heightCm} cm",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

// ─── Line chart canvas ─────────────────────────────────────────────────────────

@Composable
private fun GrowthLineChart(
    data: List<GrowthEntry>,
    valueSelector: (GrowthEntry) -> Float,
    lineColor: Color,
    fillColor: Color
) {
    if (data.size < 2) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Add at least 2 entries to see the chart", style = MaterialTheme.typography.bodySmall, color = TextHint)
        }
        return
    }

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, animationSpec = tween(1000, easing = EaseInOutCubic))
    }
    val progress by animProgress.asState()

    val values = data.map(valueSelector)
    val minVal = values.min()
    val maxVal = values.max()
    val range = (maxVal - minVal).coerceAtLeast(0.1f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val w = size.width
        val h = size.height
        val padL = 8f; val padR = 8f; val padT = 12f; val padB = 24f
        val chartW = w - padL - padR
        val chartH = h - padT - padB

        val points = data.mapIndexed { i, entry ->
            val x = padL + (i.toFloat() / (data.size - 1)) * chartW
            val y = padT + chartH - ((valueSelector(entry) - minVal) / range) * chartH
            Offset(x, y)
        }

        // Animated point count
        val animatedIndex = (progress * (points.size - 1)).toInt().coerceIn(0, points.size - 2)
        val visiblePoints = points.subList(0, animatedIndex + 2).let {
            if (it.size > points.size) points else it
        }

        // Fill path
        if (visiblePoints.size >= 2) {
            val fillPath = Path().apply {
                moveTo(visiblePoints.first().x, padT + chartH)
                visiblePoints.forEach { lineTo(it.x, it.y) }
                lineTo(visiblePoints.last().x, padT + chartH)
                close()
            }
            drawPath(fillPath, Brush.verticalGradient(
                colors = listOf(fillColor.copy(alpha = 0.4f), fillColor.copy(alpha = 0f)),
                startY = padT, endY = padT + chartH
            ))

            // Line
            val linePath = Path().apply {
                moveTo(visiblePoints.first().x, visiblePoints.first().y)
                visiblePoints.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(linePath, lineColor, style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }

        // Dots + age labels
        visiblePoints.forEachIndexed { i, pt ->
            drawCircle(Color.White, radius = 5f, center = pt)
            drawCircle(lineColor, radius = 4f, center = pt)
        }
    }

    // X-axis labels
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        data.forEach { entry ->
            Text("${entry.ageMonths}m", style = MaterialTheme.typography.labelSmall, color = TextHint)
        }
    }
}

// ─── Immunisation tab ──────────────────────────────────────────────────────────

@Composable
private fun BabyImmunisationTab(
    immunisationState: Map<String, Boolean>,
    onToggle: (String) -> Unit
) {
    // Group by age
    val grouped = immunisationSchedule.groupBy { it.ageLabel }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "🗓️ Immunisation Schedule",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Tap any vaccine to mark it as administered. Keep this up to date with your healthcare provider.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        grouped.forEach { (ageLabel, vaccines) ->
            // Age group header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Blush)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    ageLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = Blush,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    vaccines.forEachIndexed { index, item ->
                        val isDone = immunisationState[item.vaccine] ?: false
                        val isDue = item.isDue && !isDone

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggle(item.vaccine) }
                                .background(
                                    when {
                                        isDue -> Color(0xFFFFF3E0)
                                        isDone -> Color(0xFFF1F8E9)
                                        else -> Color.Transparent
                                    }
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Status icon
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isDone -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                                            isDue -> Color(0xFFFF9800).copy(alpha = 0.15f)
                                            else -> BlushLight.copy(alpha = 0.5f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    when {
                                        isDone -> Icons.Default.CheckCircle
                                        isDue -> Icons.Default.Schedule
                                        else -> Icons.Outlined.RadioButtonUnchecked
                                    },
                                    null,
                                    tint = when {
                                        isDone -> Color(0xFF4CAF50)
                                        isDue -> Color(0xFFFF9800)
                                        else -> TextHint
                                    },
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        item.vaccine,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (isDue) {
                                        Spacer(Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFFF9800)
                                        ) {
                                            Text(
                                                "DUE",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    item.diseases,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextHint
                                )
                            }
                        }

                        if (index < vaccines.lastIndex) {
                            Divider(color = BlushLight.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "⚕️ Always consult your paediatrician before administering vaccines.",
            style = MaterialTheme.typography.bodySmall,
            color = TextHint,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ─── Reusable text field ────────────────────────────────────────────────────────

@Composable
private fun BabyTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blush,
            unfocusedBorderColor = BlushLight,
            focusedLabelColor = Blush
        ),
        singleLine = true
    )
}

// ─── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun BabyProfileScreenPreview() {
    BabyProfileScreen(navController = rememberNavController())
}