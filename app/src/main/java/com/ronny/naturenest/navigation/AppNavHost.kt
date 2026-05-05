package com.ronny.naturenest.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ronny.naturenest.ui.screens.about.AboutScreen
import com.ronny.naturenest.ui.screens.article.ArticleDetailScreen
import com.ronny.naturenest.ui.screens.auth.LoginScreen
import com.ronny.naturenest.ui.screens.auth.RegisterScreen
import com.ronny.naturenest.ui.screens.community.CommunityScreen
import com.ronny.naturenest.ui.screens.health.HealthTipsScreen
import com.ronny.naturenest.ui.screens.home.HomeScreen
import com.ronny.naturenest.ui.screens.home.ProfileScreen
import com.ronny.naturenest.ui.screens.nutrition.NutritionScreen
import com.ronny.naturenest.ui.screens.onboarding.OnBoardingScreen
import com.ronny.naturenest.ui.screens.reminder.RemindersScreen
import com.ronny.naturenest.ui.screens.splash.SplashScreen
import com.ronny.naturenest.ui.screens.tracker.TrackerScreen


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_HOME
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUT_ONBOARDING) {
            OnBoardingScreen(navController)
        }
        composable(ROUT_LOGIN) {
            LoginScreen(navController)
        }
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUT_TRACKER) {
            TrackerScreen(navController = navController)
        }
        composable(ROUT_HEALTH) {
            HealthTipsScreen(navController = navController)
        }
        composable(ROUT_REMINDER) {
            RemindersScreen(navController = navController)
        }
        composable(ROUT_COMMUNITY) {
            CommunityScreen(navController = navController)
        }
        composable(ROUT_NUTRITION) {
            NutritionScreen(navController = navController)
        }
        composable(ROUT_PROFILE) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = ROUT_ARTICLE,
            arguments = listOf(navArgument("tipId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tipId = backStackEntry.arguments?.getInt("tipId") ?: 1
            ArticleDetailScreen(navController = navController, tipId = tipId)
        }









    }
}