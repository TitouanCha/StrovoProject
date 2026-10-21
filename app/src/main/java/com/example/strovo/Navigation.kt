package com.example.strovo

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.strovo.presentation.activityDetails.ActivityDetails
import com.example.strovo.presentation.auth.AuthScreen
import com.example.strovo.presentation.component.BottomNavBar
import com.example.strovo.presentation.component.MainPagerScreen
import com.example.strovo.presentation.hallOfFame.AddCompScreen
import com.example.strovo.presentation.map.ActivitiesMapScreen
import com.example.strovo.presentation.monthlyActivities.MonthlyActivitiesScreen
import com.example.strovo.presentation.profile.ProfileScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    context: Context,
    viewModels: AppViewModel,
    currentRoute: String = Screen.Auth.route,
    navController: NavHostController
) {
    val pagerState = rememberPagerState (
        initialPage = 1,
        pageCount = {3}
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if(currentRoute != Screen.Auth.route) {
                BottomNavBar(
                    currentPage = if (currentRoute == Screen.MainPager.route) pagerState.currentPage else -1,
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
            startDestination = Screen.Auth.route,
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
            composable(Screen.Auth.route) {
                AuthScreen(viewModel = viewModels.authViewModel, navController = navController)
            }
            composable(Screen.Settings.route) {
                ProfileScreen(viewModel = viewModels.profileViewModel, navController = navController)
            }
            composable(Screen.MainPager.route) {
                MainPagerScreen(
                    navController = navController,
                    hallOfFameViewModel = viewModels.hallOfFameViewModel,
                    dashboardViewModel = viewModels.dashboardViewModel,
                    progressViewModel = viewModels.progressViewModel,
                    pagerState = pagerState,
                )
            }
            composable(Screen.Map.route) {
                ActivitiesMapScreen(viewModels.progressViewModel)
            }
            composable(Screen.AddCompetition.route) {
                AddCompScreen( viewModel = viewModels.hallOfFameViewModel, navController = navController)
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
                    viewModel = viewModels.progressViewModel
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
                    context = context,
                    navController = navController
                )
            }
        }
    }
}