package com.example.strovo.presentation

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
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