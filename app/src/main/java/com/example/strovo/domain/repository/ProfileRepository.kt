package com.example.strovo.domain.repository

import com.example.strovo.data.model.Discipline
import com.example.strovo.domain.model.AthletePB

interface ProfileRepository {
    suspend fun getUserStravaName(): String
    suspend fun addDisciplines(disciplines: List<Discipline>): Result<List<Discipline>>
    suspend fun getSelectedDiscipline(): Result<List<Discipline>>
    suspend fun getDisciplineList(): List<Discipline>
    suspend fun logOut(): Result<Void>
    suspend fun getAthletePB(): Result<AthletePB>
}