package com.example.strovo.domain.model

import com.example.strovo.model.strava.AverageStatsModel
import com.example.strovo.model.strava.MonthlyDistanceModel
import com.example.strovo.model.strava.YearActivitiesModel

data class ProgressModel(
    var selectedYear: MutableList<YearActivitiesModel>,
    var lastYear: MutableList<YearActivitiesModel>,
    var averageStats: AverageStatsModel,
    var selectedYearDistances: MutableList<MonthlyDistanceModel>,
    var lastYearDistances: MutableList<MonthlyDistanceModel>,
    var activitiesTrackPoints: List<List<Pair<Double, Double>>>?,
)