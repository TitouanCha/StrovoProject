package com.example.strovo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strovo.screen.DashboardScreen
import com.example.strovo.screen.ProgressScreen
import com.example.strovo.screen.Screen
import com.example.strovo.ui.theme.StrovoTheme
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.strovo.component.BottomNavBar
import com.example.strovo.screen.ActivitiesMapScreen
import com.example.strovo.screen.ActivityDetails
import com.example.strovo.screen.MonthlyActivitiesScreen
import com.example.strovo.screen.SettingsScreen
import com.example.strovo.utils.TokenManager
import com.example.strovo.viewModel.DashboardViewModel
import com.example.strovo.viewModel.ProgressViewModel
import com.example.strovo.viewModel.StravaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StrovoTheme {
                val navController = rememberNavController()
                val tokenManager = TokenManager(LocalContext.current)
                val stravaViewModel: StravaViewModel = viewModel()
                val dashboardViewModel: DashboardViewModel = viewModel()
                val progressViewModel: ProgressViewModel = viewModel()
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    if (tokenManager.hasTokens()) {
                        stravaViewModel.refreshAccessToken(
                            tokenManager.getRefreshToken()!!,
                            context
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Veuillez vous connecter à Strava via le profil",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(navController)
                    }
                )
                { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Dashboard.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Dashboard.route) {
                            DashboardScreen(navController, stravaViewModel, dashboardViewModel)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController)
                        }
                        composable(Screen.Progress.route) {
                            ProgressScreen(navController, stravaViewModel, progressViewModel)
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
                                backStackEntry.arguments?.getString("activityId") ?: ""
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