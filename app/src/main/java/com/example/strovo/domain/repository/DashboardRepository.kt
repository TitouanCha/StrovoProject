package com.example.strovo.domain.repository

import com.example.strovo.data.model.strava.GetOverallStatsModel
import com.example.strovo.data.model.strava.GetStravaActivitiesModel
import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.model.strava.Activity

interface DashboardRepository {
    suspend fun getMonthData(before: String, after: String): Result<List<ActivityDetailModel>>
    suspend fun getOverallStats(): Result<GetOverallStatsModel>
}