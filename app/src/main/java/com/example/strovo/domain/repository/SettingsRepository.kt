package com.example.strovo.domain.repository

import android.R
import com.example.strovo.data.model.Discipline

interface SettingsRepository {
    suspend fun getUserStravaName(): String
    suspend fun addDisciplines(disciplines: List<Discipline>): Result<List<Discipline>>
    suspend fun getSelectedDiscipline(): Result<List<Discipline>>
    suspend fun getDisciplineList(): List<Discipline>
    suspend fun stravaLogOut()
}