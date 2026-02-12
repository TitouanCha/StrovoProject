package com.example.strovo.screen




import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.strovo.component.monthlyActivitiesScreenComponent.MonthlyActivitiesContent
import com.example.strovo.data.AverageMonthStatsModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.viewModel.ProgressViewModel

fun getMonthAverageStats(activities: GetStravaActivitiesModel?): AverageMonthStatsModel? {
    if(activities == null){
        return null
    }
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
    val pagerState = rememberPagerState(
        initialPage = monthIndex,
        pageCount = { 12 }
    )

    val allMonthlyActivities = viewModel.monthlyDistances.collectAsState()
    val selectedYear = viewModel.selectedYear.collectAsState()
    var monthlyActivities = remember { mutableStateOf<List<MonthlyDistanceItem?>>(emptyList()) }
    var lastYearMonthlyActivities = remember { mutableStateOf<List<MonthlyDistanceItem?>>(emptyList()) }
    val averageStats = remember { mutableStateOf<List<AverageMonthStatsModel?>>(emptyList()) }
    val lastYearAverageStats = remember { mutableStateOf<List<AverageMonthStatsModel?>>(emptyList()) }

    LaunchedEffect(allMonthlyActivities.value, selectedYear.value) {
        val tempMonthlyActivities = mutableListOf<MonthlyDistanceItem?>()
        val tempLastYearActivities = mutableListOf<MonthlyDistanceItem?>()
        val tempAverageStats = mutableListOf<AverageMonthStatsModel?>()
        val tempLastYearAverageStats = mutableListOf<AverageMonthStatsModel?>()

        for (i in 0..11) {
            val currentMonth = allMonthlyActivities.value.selectedYear?.get(i)
            val lastYearMonth = allMonthlyActivities.value.lastYear?.get(i)

            tempMonthlyActivities.add(currentMonth)
            tempLastYearActivities.add(lastYearMonth)
            tempAverageStats.add(getMonthAverageStats(currentMonth?.activities))
            tempLastYearAverageStats.add(getMonthAverageStats(lastYearMonth?.activities))
        }

        monthlyActivities.value = tempMonthlyActivities
        lastYearMonthlyActivities.value = tempLastYearActivities
        averageStats.value = tempAverageStats
        lastYearAverageStats.value = tempLastYearAverageStats
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        MonthlyActivitiesContent(
            navController = navController,
            month = monthName[page],
            selectedYear = selectedYear.value,
            activities = monthlyActivities.value.getOrNull(page),
            averageStats = averageStats.value.getOrNull(page),
            lastYearAverageStats = lastYearAverageStats.value.getOrNull(page)
        )
    }
}
