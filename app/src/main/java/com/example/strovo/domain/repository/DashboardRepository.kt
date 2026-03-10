package com.example.strovo.domain.repository

import com.example.strovo.data.model.GetOverallStatsModel
import com.example.strovo.data.model.GetStravaActivitiesModel

interface DashboardRepository {
    suspend fun getMonthData(before: String, after: String): Result<GetStravaActivitiesModel>
    suspend fun getOverallStats(): Result<GetOverallStatsModel>
}