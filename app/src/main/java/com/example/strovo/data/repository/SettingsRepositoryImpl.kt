package com.example.strovo.data.repository

import android.content.Context
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.services.strava.StravaRetrofitInstance
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.repository.SettingsRepository

class SettingsRepositoryImpl(context: Context): SettingsRepository {
    private val disciplineManager = DisciplineManager(context)
    private val tokenManager = TokenManager(context)

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

    override suspend fun stravaLogOut() {
        //TODO("Not yet implemented")
    }
}