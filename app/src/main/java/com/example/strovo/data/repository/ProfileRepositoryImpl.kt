package com.example.strovo.data.repository

import android.content.Context
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.services.intervals.IntervalsRetrofitClient
import com.example.strovo.data.services.intervals.basicAuthHeader
import com.example.strovo.data.services.strava.StravaRetrofitInstance
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.model.AthletePB
import com.example.strovo.domain.model.BestChrono
import com.example.strovo.domain.repository.ProfileRepository
import com.example.strovo.presentation.profile.ProfileUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileRepositoryImpl(context: Context): ProfileRepository {
    private val disciplineManager = DisciplineManager(context)
    private val tokenManager = TokenManager(context)

    override suspend fun getAthletePB(): Result<AthletePB> {
        val token = tokenManager.getAccessToken()
        val athleteId = tokenManager.getAthleteId()
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val newest = LocalDate.now().format(formatter)
        val oldest = LocalDate.now().minusYears(5).format(formatter)
        try{
            val activities = IntervalsRetrofitClient.activityApi.getActivities(
                authorization = basicAuthHeader(token.toString()),
                athleteId = athleteId.toString(),
                oldest = oldest,
                newest = newest
            ).filter { it.type == "Run" || it.type == "TrailRun" }
            val distance5km = BestChrono.fromActivityList(activities, 5000)
            val distance10km = BestChrono.fromActivityList(activities, 10000)
            val distanceHalfMarathon = BestChrono.fromActivityList(activities, 21097)
            val distanceMarathon = BestChrono.fromActivityList(activities, 42195)
            val distanceLongest = BestChrono.fetchLongestDistance(activities)
            return AthletePB(
                distance5km = distance5km,
                distance10km = distance10km,
                distanceHalfMarathon = distanceHalfMarathon,
                distanceMarathon = distanceMarathon,
                distanceLongest = distanceLongest
            ).let { athletePB ->
                Result.success(athletePB)
            }
        }catch (e: Error){
            return Result.failure(e)
        }
    }

    override suspend fun getUserStravaName(): String {
        try{
            val userInfoResponse = StravaRetrofitInstance.athleteApi.getAthleteInfo(
                authorization = "Bearer ${tokenManager.getAccessToken()}"
            )
            return "${userInfoResponse.firstname} ${userInfoResponse.lastname}"
        }catch(e: Exception){
            return ""
        }
    }

    override suspend fun addDisciplines(disciplines: List<Discipline>): Result<List<Discipline>> {
        return try {
            disciplineManager.saveSelectedDisciplines(disciplines)
            Result.success(disciplines)
        }catch (e: Error){
            Result.failure(e)
        }
    }

    override suspend fun getSelectedDiscipline(): Result<List<Discipline>> {
        return try {
            Result.success(disciplineManager.getSelectedDisciplines())
        }catch (e: Error){
            Result.failure(e)
        }
    }

    override suspend fun getDisciplineList(): List<Discipline> {
        return Discipline.entries
    }

    override suspend fun logOut(): Result<Void> {
        return TODO("Provide the return value")
    }
}