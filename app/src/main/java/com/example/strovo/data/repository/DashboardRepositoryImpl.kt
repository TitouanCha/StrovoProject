package com.example.strovo.data.repository


import android.content.Context
import com.example.strovo.domain.repository.DashboardRepository
import com.example.strovo.data.model.GetOverallStatsModel
import com.example.strovo.data.model.GetStravaActivitiesModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.util.TokenManager

class DashboardRepositoryImpl(context: Context): DashboardRepository {
    val tokenManager: TokenManager = TokenManager(context)

    override suspend fun getMonthData(before: String, after: String): Result<GetStravaActivitiesModel> {
        return try {
            val activityResponse: GetStravaActivitiesModel = RetrofitInstance.activityApi.getActivities(
                authorization = "Bearer ${tokenManager.getAccessToken()}",
                perPage = null,
                page = null,
                before = before,
                after = after
            )
            Result.success(activityResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOverallStats(): Result<GetOverallStatsModel> {
        return try {
            val statsResponse: GetOverallStatsModel = RetrofitInstance.activityApi.getAthleteStats(
                authorization = "Bearer ${tokenManager.getAccessToken()}",
                athleteId = "${tokenManager.getAthleteId()}"
            )
            Result.success(statsResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}