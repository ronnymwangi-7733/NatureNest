package com.ronny.naturenest.ui.screens.article



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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.models.NatureNestRepository
import com.ronny.naturenest.ui.theme.*

@Composable
fun ArticleDetailScreen(navController: NavController, tipId: Int) {
    val tip = remember { NatureNestRepository.getHealthTips().find { it.id == tipId } }
        ?: return

    var isBookmarked by remember { mutableStateOf(false) }

    val articleContent = mapOf(
        1 to """Iron is one of the most critical nutrients during pregnancy. Your body needs iron to make hemoglobin, the protein in red blood cells that carries oxygen from your lungs to the rest of your body — and to your growing baby.

During pregnancy, your blood volume increases by nearly 50%, which means you need significantly more iron to keep up with demand. The recommended daily intake for pregnant women is 27mg per day, nearly double the amount for non-pregnant women.

**Best Food Sources of Iron**

There are two types of iron in food: heme iron (from animal products) and non-heme iron (from plant sources). Heme iron is more easily absorbed by your body.

• Red meat, poultry, and fish (heme iron)
• Fortified cereals and bread (non-heme iron)  
• Beans, lentils, and chickpeas
• Tofu and tempeh
• Dark leafy greens like spinach and kale
• Pumpkin seeds and dried apricots

**Tips for Better Absorption**

Pair iron-rich foods with vitamin C to significantly boost absorption. A squeeze of lemon on lentils or a glass of orange juice with breakfast can make a big difference. Avoid consuming iron-rich foods with coffee, tea, or calcium-rich foods, as these can inhibit absorption.

If your doctor recommends an iron supplement, take it on an empty stomach for best absorption, though this may cause stomach upset in some women. If so, take it with a small amount of food.""",
        2 to """Prenatal yoga is one of the safest and most beneficial forms of exercise during pregnancy. It helps strengthen your body, calm your mind, and prepare you for labor — all while being gentle enough for every trimester.

Before starting any exercise program during pregnancy, consult with your healthcare provider. Once you have the go-ahead, these poses are generally considered safe for most pregnant women.

**Safe Poses for All Trimesters**

Cat-Cow Stretch: This gentle spinal movement relieves back pain and improves posture. Start on all fours, alternate between arching and rounding your back with each breath.

Warrior II: Strengthens legs and opens the hips. Stand with feet wide, turn one foot out, and bend the knee over the ankle while extending arms.

Modified Child's Pose: Great for hip opening and relaxation. Place a pillow between your thighs and chest for comfort as your belly grows.

**Poses to Avoid**

After the first trimester, avoid lying flat on your back for extended periods, as this can compress the vena cava. Avoid deep twists, hot yoga, and any pose that feels uncomfortable.

Always listen to your body and stop if you feel pain, dizziness, or shortness of breath."""
    )

    val content = articleContent[tipId] ?: tip.description

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
            .verticalScroll(rememberScrollState())
    ) {
        // Article header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BlushLight, SurfaceCream)
                    )
                )
                .padding(top = 48.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Column {
                // Top bar actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SurfaceWhite)
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back", tint = TextPrimary, modifier = Modifier.size(18.dp))
                    }
                    Row {
                        IconButton(
                            onClick = { isBookmarked = !isBookmarked },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SurfaceWhite)
                        ) {
                            Icon(
                                if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                "Bookmark",
                                tint = if (isBookmarked) Blush else TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SurfaceWhite)
                        ) {
                            Icon(Icons.Outlined.Share, "Share", tint = TextPrimary, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Category badge
                Surface(
                    color = BlushLight,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "${tip.category.emoji} ${tip.category.label}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = BlushDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    tip.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp
                )

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Author avatar
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SageLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👩‍⚕️", fontSize = 16.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "NatureNest Health Team",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Medically reviewed · ${tip.readTime}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextHint
                        )
                    }
                }
            }
        }

        // Article body
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            content.split("\n\n").forEach { paragraph ->
                if (paragraph.startsWith("**") && paragraph.endsWith("**")) {
                    Text(
                        paragraph.removeSurrounding("**"),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else if (paragraph.startsWith("•")) {
                    paragraph.split("\n").forEach { bullet ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("•", color = Blush, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(end = 8.dp))
                            Text(
                                bullet.removePrefix("• "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                lineHeight = 22.sp
                            )
                        }
                    }
                } else {
                    Text(
                        paragraph,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        lineHeight = 26.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Medical disclaimer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CreamDark),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(modifier = Modifier.padding(14.dp)) {
                    Text("⚠️", fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "This content is for informational purposes only and is not a substitute for professional medical advice. Always consult your healthcare provider for personalized guidance.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleDetailScreenPreview() {
    ArticleDetailScreen(navController = rememberNavController(), tipId = 1)
}