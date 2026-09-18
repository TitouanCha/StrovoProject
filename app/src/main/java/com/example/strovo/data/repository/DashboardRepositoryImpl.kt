package com.example.strovo.data.repository


import android.content.Context
import com.example.strovo.data.model.FetchActivity
import com.example.strovo.data.model.FetchHealthData
import com.example.strovo.domain.repository.DashboardRepository
import com.example.strovo.data.model.strava.GetOverallStatsModel
import com.example.strovo.data.model.strava.GetStravaActivitiesModel
import com.example.strovo.data.services.intervals.IntervalsRetrofitClient
import com.example.strovo.data.services.intervals.basicAuthHeader
import com.example.strovo.data.services.strava.StravaRetrofitInstance
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.domain.model.HealthDataModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardRepositoryImpl(context: Context): DashboardRepository {
    val tokenManager: TokenManager = TokenManager(context)

    override suspend fun getMonthData(before: String, after: String): Result<List<ActivityDetailModel>> {
        return try {
            val connectionString = basicAuthHeader(tokenManager.getAccessToken().toString())
            val activityResponse: List<FetchActivity> = IntervalsRetrofitClient.activityApi.getActivities(
                athleteId = "${tokenManager.getAthleteId()}",
                oldest = after,
                newest = before,
                authorization = connectionString
            )
            val activityDetailList: List<ActivityDetailModel> = activityResponse.map { activity -> ActivityDetailModel.fromApi(activity) }
            Result.success(activityDetailList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOverallStats(): Result<GetOverallStatsModel> {
        return try {
            val statsResponse: GetOverallStatsModel = StravaRetrofitInstance.athleteApi.getAthleteStats(
                authorization = "Bearer ${tokenManager.getAccessToken()}",
                athleteId = "${tokenManager.getAthleteId()}"
            )
            Result.success(statsResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHealthStats(): Result<HealthDataModel> {
        return try {
            val now = LocalDateTime.now()
            val beforeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val afterDate = now.minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            val connectionString = basicAuthHeader(tokenManager.getAccessToken().toString())
            val healthStatsResponse: List<FetchHealthData> = IntervalsRetrofitClient.healthApi.getHealthData(
                athleteId = "${tokenManager.getAthleteId()}",
                oldest = afterDate,
                newest = beforeDate,
                authorization = connectionString,
            )
            val healthData = HealthDataModel.fromApi(healthStatsResponse)
            Result.success(healthData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}