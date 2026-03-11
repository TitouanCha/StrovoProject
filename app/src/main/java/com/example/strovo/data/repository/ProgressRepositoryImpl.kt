package com.example.strovo.data.repository

import android.content.Context
import android.util.Log
import com.example.strovo.domain.repository.ProgressRepository
import com.example.strovo.model.YearStravaActivitiesModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.util.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.ZoneOffset

class ProgressRepositoryImpl(context: Context): ProgressRepository {
    private val tokenManager = TokenManager(context)

    override suspend fun getYearActivities(year: Int): Result<YearStravaActivitiesModel> {
        var before = LocalDateTime.of(year, 12, 31, 23, 59, 59).toEpochSecond(ZoneOffset.UTC)
        var after = LocalDateTime.of(year, 1, 1, 0, 0, 0).toEpochSecond(ZoneOffset.UTC)
        val token = tokenManager.getAccessToken()
        return try {
            coroutineScope {
                val page1 = async {
                    RetrofitInstance.activityApi.getActivities(
                        authorization = "Bearer $token",
                        perPage = 200,
                        page = 1,
                        before = before.toString(),
                        after = after.toString()
                    )
                }
                val page2 = async {
                    RetrofitInstance.activityApi.getActivities(
                        authorization = "Bearer $token",
                        perPage = 200,
                        page = 2,
                        before = before.toString(),
                        after = after.toString()
                    )
                }
                val allActivities = page1.await().toMutableList()
                allActivities.addAll(page2.await())
                Result.success(YearStravaActivitiesModel(
                    year = year,
                    allActivities = allActivities.filter { it.type == "Run" }.toMutableList()
                ))
            }
        } catch (e: Exception) {
             Result.failure(e)
        }
    }
}