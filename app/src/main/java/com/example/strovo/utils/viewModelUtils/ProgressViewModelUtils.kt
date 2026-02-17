package com.example.strovo.utils.viewModelUtils

import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.data.YearStravaActivitiesModel
import com.example.strovo.viewModel.ProgressViewModel
import java.time.LocalDate
import java.time.ZoneId


fun getYearActivities(selectedYear: Int, viewModel: ProgressViewModel){
    val firstDayOfYear = LocalDate.of(selectedYear, 1, 1)
    val lastDayOfYear = LocalDate.of(selectedYear, 12, 31)
    val afterDate = firstDayOfYear
        .atStartOfDay(ZoneId.systemDefault())
        .toEpochSecond().toString()
    val beforeDate = lastDayOfYear
        .atStartOfDay(ZoneId.systemDefault())
        .toEpochSecond().toString()
    viewModel.getYearActivities(
        after = afterDate,
        before = beforeDate,
    )
}

fun parseMonthlyActivitiesDistance(activities: YearStravaActivitiesModel?): MutableList<MonthlyDistanceItem>? {
    if(activities != null){
        val parsedMonthlyDistances = MutableList(12) { MonthlyDistanceItem(0, null) }
        for (i in 1..12) {
            val monthActivities = activities.allActivities.filter { activity ->
                val activityDate = LocalDate.parse(activity.start_date_local.substring(0, 10))
                activityDate.year == activities.year && activityDate.monthValue == i
            }
            parsedMonthlyDistances[i-1].distance = (monthActivities.sumOf { it.distance } / 1000).toInt()
            parsedMonthlyDistances[i-1].activities = GetStravaActivitiesModel().apply {
                addAll(monthActivities)
            }
        }
        return parsedMonthlyDistances
    }
    return null
}