package com.example.strovo.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.strovo.presentation.dashboard.DashboardScreen
import com.example.strovo.presentation.dashboard.DashboardViewModel
import com.example.strovo.presentation.progress.ProgressScreen
import com.example.strovo.presentation.progress.ProgressViewModel

@Composable
fun MainPagerScreen(navController: NavController, dashboardViewModel: DashboardViewModel, progressViewModel: ProgressViewModel, pagerState: PagerState) {

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> DashboardScreen(navController, dashboardViewModel)
            1 -> ProgressScreen(navController, progressViewModel)
        }
    }
}