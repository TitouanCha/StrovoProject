package com.example.strovo.presentation.monthlyActivities

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.strovo.component.monthlyActivitiesScreenComponent.MonthlyActivitiesContent
import com.example.strovo.model.AverageMonthStatsModel
import com.example.strovo.data.model.GetStravaActivitiesModelItem
import com.example.strovo.model.MonthlyDistanceModel
import com.example.strovo.presentation.progress.ProgressUiState
import com.example.strovo.presentation.progress.ProgressViewModel
import java.util.Calendar

fun getMonthAverageStats(activities: ArrayList<GetStravaActivitiesModelItem>): AverageMonthStatsModel {
    val distance = activities.sumOf { it.distance }
    val weeklyAverage = distance / 4
    return AverageMonthStatsModel(
        activities = activities.size,
        distance = distance / 1000,
        weekly_average = weeklyAverage / 1000
    )
}

@Composable
fun MonthlyActivitiesScreen(navController: NavController, viewModel: ProgressViewModel, monthIndex: Int) {
    val monthName = listOf(
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    )


    val progressUiState = viewModel.progressUiState.collectAsState().value
    val selectedYear = viewModel.selectedYear.collectAsState().value

    var actualMonthNumber = 12
    if(selectedYear == Calendar.getInstance().get(Calendar.YEAR)){
        actualMonthNumber = Calendar.getInstance().get(Calendar.MONTH)+1
    }
    val pagerState = rememberPagerState(
        initialPage = monthIndex,
        pageCount = { actualMonthNumber }
    )

    var monthlyActivities = remember { mutableStateOf<List<MonthlyDistanceModel>>(emptyList()) }
    var lastYearMonthlyActivities = remember { mutableStateOf<List<MonthlyDistanceModel>>(emptyList()) }
    val averageStats = remember { mutableStateOf<List<AverageMonthStatsModel>>(emptyList()) }
    val lastYearAverageStats = remember { mutableStateOf<List<AverageMonthStatsModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        val tempMonthlyActivities = mutableListOf<MonthlyDistanceModel>()
        val tempLastYearActivities = mutableListOf<MonthlyDistanceModel>()
        val tempAverageStats = mutableListOf<AverageMonthStatsModel>()
        val tempLastYearAverageStats = mutableListOf<AverageMonthStatsModel>()

        if(progressUiState is ProgressUiState.Success) {
            val allActivitiesData = progressUiState.progressData
            for (i in 0..11) {
                val currentMonthData = allActivitiesData.selectedYearDistances[i]
                val lastYearMonthData = allActivitiesData.lastYearDistances[i]

                tempMonthlyActivities.add(currentMonthData)
                tempLastYearActivities.add(lastYearMonthData)
                tempAverageStats.add(getMonthAverageStats(currentMonthData.activities))
                tempLastYearAverageStats.add(getMonthAverageStats(lastYearMonthData.activities))
            }

            monthlyActivities.value = tempMonthlyActivities
            lastYearMonthlyActivities.value = tempLastYearActivities
            averageStats.value = tempAverageStats
            lastYearAverageStats.value = tempLastYearAverageStats
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        MonthlyActivitiesContent(
            navController = navController,
            month = monthName[page],
            selectedYear = selectedYear,
            activities = monthlyActivities.value.getOrNull(page),
            averageStats = averageStats.value.getOrNull(page),
            lastYearAverageStats = lastYearAverageStats.value.getOrNull(page)
        )
    }
}
