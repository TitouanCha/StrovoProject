package com.example.strovo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strovo.presentation.dashboard.DashboardScreen
import com.example.strovo.presentation.progress.ProgressScreen
import com.example.strovo.screen.Screen
import com.example.strovo.ui.theme.StrovoTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.strovo.component.BottomNavBar
import com.example.strovo.screen.ActivitiesMapScreen
import com.example.strovo.screen.ActivityDetails
import com.example.strovo.presentation.monthlyActivities.MonthlyActivitiesScreen
import com.example.strovo.screen.SettingsScreen
import com.example.strovo.presentation.dashboard.DashboardViewModel
import com.example.strovo.presentation.progress.ProgressViewModel
import com.example.strovo.presentation.stravaAuth.StravaAuthScreen
import com.example.strovo.presentation.stravaAuth.StravaAuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StrovoTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val stravaAuthViewModel: StravaAuthViewModel = viewModel()
                val dashboardViewModel: DashboardViewModel = viewModel()
                val progressViewModel: ProgressViewModel = viewModel()



                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if(currentRoute != Screen.StravaAuth.route) {
                            BottomNavBar(navController)
                        }
                    }
                )
                { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.StravaAuth.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.StravaAuth.route) {
                            StravaAuthScreen(viewModel = stravaAuthViewModel, navController = navController)
                        }
                        composable(Screen.Dashboard.route) {
                            DashboardScreen(navController, dashboardViewModel)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController)
                        }
                        composable(Screen.Progress.route) {
                            ProgressScreen(navController, progressViewModel)
                        }
                        composable(Screen.Map.route) {
                            ActivitiesMapScreen(progressViewModel, context)
                        }
                        composable(
                            route = Screen.MonthlyActivities.route,
                            arguments = listOf(
                                navArgument("monthIndex") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val monthIndex = backStackEntry.arguments?.getInt("monthIndex") ?: 0
                            MonthlyActivitiesScreen(
                                navController = navController,
                                monthIndex = monthIndex,
                                viewModel = progressViewModel
                            )
                        }
                        composable(
                            route = Screen.ActivityDetails.route,
                            arguments = listOf(
                                navArgument("activityId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val activityId =
                                backStackEntry.arguments?.getString("activityId") ?: return@composable
                            ActivityDetails(
                                activityId = activityId,
                                context = context
                            )
                        }
                    }
                }
            }
        }
    }
}