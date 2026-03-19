package com.example.strovo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.strovo.ui.theme.StrovoTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.strovo.component.BottomNavBar
import com.example.strovo.component.MainPagerScreen
import com.example.strovo.component.Screen
import com.example.strovo.presentation.map.ActivitiesMapScreen
import com.example.strovo.presentation.activityDetails.ActivityDetails
import com.example.strovo.presentation.monthlyActivities.MonthlyActivitiesScreen
import com.example.strovo.presentation.settings.SettingsScreen
import com.example.strovo.presentation.dashboard.DashboardViewModel
import com.example.strovo.presentation.progress.ProgressViewModel
import com.example.strovo.presentation.settings.SettingsViewModel
import com.example.strovo.presentation.stravaAuth.StravaAuthScreen
import com.example.strovo.presentation.stravaAuth.StravaAuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StrovoTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = remember(navBackStackEntry) {
                    navBackStackEntry?.destination?.route
                }
                val pagerState = rememberPagerState (
                    initialPage = 0,
                    pageCount = {2}
                )
                val scope = rememberCoroutineScope()

                val stravaAuthViewModel: StravaAuthViewModel = viewModel()
                val dashboardViewModel: DashboardViewModel = viewModel()
                val progressViewModel: ProgressViewModel = viewModel()
                val settingsViewModel: SettingsViewModel = viewModel()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if(currentRoute != Screen.StravaAuth.route) {
                            BottomNavBar(
                                currentPage = pagerState.currentPage,
                                onPageSelected = { page ->
                                    scope.launch {
                                        pagerState.animateScrollToPage(page)
                                    }
                                    if(currentRoute != Screen.MainPager.route) {
                                        navController.navigate(Screen.MainPager.route) {
                                            popUpTo(Screen.MainPager.route) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }
                    }
                )
                { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.StravaAuth.route,
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                animationSpec = tween(300)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                                animationSpec = tween(300)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.End,
                                animationSpec = tween(300)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.End,
                                animationSpec = tween(300)
                            )
                        }
                    ) {
                        composable(Screen.StravaAuth.route) {
                            StravaAuthScreen(viewModel = stravaAuthViewModel, navController = navController)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController, settingsViewModel)
                        }
                        composable(Screen.MainPager.route) {
                            MainPagerScreen(
                                navController = navController,
                                dashboardViewModel = dashboardViewModel,
                                progressViewModel = progressViewModel,
                                pagerState = pagerState,
                            )
                        }
                        composable(Screen.Map.route) {
                            ActivitiesMapScreen(progressViewModel)
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