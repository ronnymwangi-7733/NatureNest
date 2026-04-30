package com.ronny.naturenest.ui.screens.splash


import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.navigation.ROUT_ONBOARDING
import com.ronny.naturenest.navigation.ROUT_SPLASH
import com.ronny.naturenest.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        delay(2500)
        navController.navigate(ROUT_ONBOARDING) {
            popUpTo(ROUT_SPLASH) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BlushLight, Cream, SageLight)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "🌿",
            fontSize = 80.sp,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "NatureNest",
            style = MaterialTheme.typography.displayMedium,
            color = WarmBrown,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(alpha)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Nurturing life, every step of the way",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            modifier = Modifier.alpha(alpha)
        )
        Spacer(Modifier.height(60.dp))
        CircularProgressIndicator(
            color = Blush,
            strokeWidth = 2.dp,
            modifier = Modifier
                .size(24.dp)
                .alpha(alpha)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}