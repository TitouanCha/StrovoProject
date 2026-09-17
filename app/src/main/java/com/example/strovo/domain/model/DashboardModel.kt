package com.example.strovo.domain.model

import com.example.strovo.data.model.Discipline
import com.example.strovo.data.model.strava.GetOverallStatsModel
import com.example.strovo.data.model.strava.GetStravaActivitiesModelItem

data class DashboardModel(
    val selectedDiscipline: List<Discipline>,
    val lastActivity: ActivityDetailModel?,
    val monthActivity: List<ActivityDetailModel>,
    val healthData: HealthDataModel,
    val overallStats: GetOverallStatsModel?
)
