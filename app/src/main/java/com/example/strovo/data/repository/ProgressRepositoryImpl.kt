package com.example.strovo.data.repository

import android.content.Context
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.model.FetchActivity
import com.example.strovo.data.model.toDiscipline
import com.example.strovo.data.services.intervals.IntervalsRetrofitClient
import com.example.strovo.data.services.intervals.basicAuthHeader
import com.example.strovo.domain.repository.ProgressRepository
import com.example.strovo.model.strava.YearActivitiesModel
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.model.ActivityDetailModel
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProgressRepositoryImpl(context: Context): ProgressRepository {
    private val tokenManager = TokenManager(context)
    private val disciplineManager = DisciplineManager(context)

    override suspend fun getYearActivities(year: Int): Result<YearActivitiesModel> {
        val oldest: String = LocalDateTime.of(year, 1, 1, 0, 0, 0)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        val newest: String = LocalDateTime.of(year, 12, 31, 23, 59, 59)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        val token = tokenManager.getAccessToken()
        val athleteId = tokenManager.getAthleteId()

        val disciplines: List<Discipline> = disciplineManager.getSelectedDisciplines()
        return try {
            coroutineScope {
                val result: List<FetchActivity> =
                    IntervalsRetrofitClient.activityApi.getActivities(
                        authorization = basicAuthHeader(token.toString()),
                        athleteId = athleteId.toString(),
                        oldest = oldest,
                        newest = newest
                    )
                val allActivities = result.map{ activity -> ActivityDetailModel.fromApi(activity)}
                Result.success(YearActivitiesModel(
                    year = year,
                    allActivities = allActivities.filter {
                        val activityDiscipline = it.type.toDiscipline()
                        activityDiscipline != null && disciplines.contains(activityDiscipline)
                    }.toMutableList()
                ))
            }
        } catch (e: Exception) {
             Result.failure(e)
        }
    }
}