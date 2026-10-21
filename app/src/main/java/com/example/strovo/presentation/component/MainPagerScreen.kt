package com.example.strovo.presentation.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.strovo.presentation.dashboard.DashboardScreen
import com.example.strovo.presentation.dashboard.DashboardViewModel
import com.example.strovo.presentation.hallOfFame.HallOfFameScreen
import com.example.strovo.presentation.hallOfFame.HallOfFameViewModel
import com.example.strovo.presentation.progress.ProgressScreen
import com.example.strovo.presentation.progress.ProgressViewModel

@Composable
fun MainPagerScreen(navController: NavController, hallOfFameViewModel: HallOfFameViewModel ,dashboardViewModel: DashboardViewModel, progressViewModel: ProgressViewModel, pagerState: PagerState) {

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> HallOfFameScreen(navController, hallOfFameViewModel)
            1 -> DashboardScreen(navController, dashboardViewModel)
            2 -> ProgressScreen(navController, progressViewModel)
        }
    }
}