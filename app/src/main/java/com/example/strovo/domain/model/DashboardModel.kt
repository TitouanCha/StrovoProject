package com.example.strovo.domain.model

import com.example.strovo.model.GetStravaActivitiesModel
import com.example.strovo.model.GetOverallStatsModel
import com.example.strovo.model.getStravaActivitiesModelItem

data class DashboardModel(
    val monthActivity: List<getStravaActivitiesModelItem>,
    val overallStats: GetOverallStatsModel
)
