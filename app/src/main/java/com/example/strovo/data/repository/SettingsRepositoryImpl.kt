package com.example.strovo.data.repository

import android.content.Context
import androidx.datastore.dataStoreFile
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.services.RetrofitInstance
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.repository.SettingsRepository
import com.example.strovo.presentation.settings.SettingsUiState

class SettingsRepositoryImpl(context: Context): SettingsRepository {
    private val disciplineManager = DisciplineManager(context)
    private val tokenManager = TokenManager(context)

    override suspend fun getUserStravaName(): String {
        try{
            val userInfoResponse = RetrofitInstance.athleteApi.getAthleteInfo(
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