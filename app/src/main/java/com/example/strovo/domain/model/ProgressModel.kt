package com.example.strovo.domain.model

import com.example.strovo.model.Activity
import com.example.strovo.model.AverageStatsModel
import com.example.strovo.model.MonthlyDistanceModel
import com.example.strovo.model.YearStravaActivitiesModel

data class ProgressModel(
    var selectedYear: MutableList<YearStravaActivitiesModel>,
    var lastYear: MutableList<YearStravaActivitiesModel>,
    var averageStats: AverageStatsModel,
    var selectedYearDistances: MutableList<MonthlyDistanceModel>,
    var lastYearDistances: MutableList<MonthlyDistanceModel>,
    var activitiesTrackPoints: List<List<Pair<Double, Double>>>,
)