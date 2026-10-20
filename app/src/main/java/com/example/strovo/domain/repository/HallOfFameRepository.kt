package com.example.strovo.domain.repository

import com.example.strovo.domain.model.CompModel

interface HallOfFameRepository {
    suspend fun getAllComp(): Result<List<CompModel>>
    suspend fun updateComp(id: String, data: CompModel): Result<CompModel>
    suspend fun addComp(data: CompModel): Result<CompModel>
    suspend fun deleteComp(id: String): Result<CompModel>
    suspend fun compDetails(id: String): Result<CompModel>
    suspend fun linkActivityToComp(compId: String, activityId: String): Result<CompModel>
}