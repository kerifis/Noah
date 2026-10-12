package com.talktv.aac.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.talktv.aac.ui.category.CategorySubGridScreen
import com.talktv.aac.ui.home.HomeScreen
import com.talktv.aac.ui.home.HomeViewModel
import com.talktv.aac.ui.settings.SettingsScreen
import com.talktv.aac.ui.settings.SettingsViewModel

@Composable
fun TalkTvNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToCategory = { categoryId ->
                    navController.navigate(Screen.Category.createRoute(categoryId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.Category.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val homeUiState by homeViewModel.uiState.collectAsState()

            CategorySubGridScreen(
                categoryId = categoryId,
                initialPhrase = homeUiState.currentPhrase,
                onBackToHome = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
