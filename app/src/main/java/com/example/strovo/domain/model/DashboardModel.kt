package com.example.strovo.domain.model

import com.example.strovo.data.model.GetOverallStatsModel
import com.example.strovo.data.model.GetStravaActivitiesModelItem

data class DashboardModel(
    val monthActivity: List<GetStravaActivitiesModelItem>,
    val overallStats: GetOverallStatsModel
)
