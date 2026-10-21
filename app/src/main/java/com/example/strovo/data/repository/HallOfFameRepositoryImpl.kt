package com.example.strovo.data.repository

import com.example.strovo.data.local.competiton.CompDao
import com.example.strovo.data.local.competiton.CompMapper
import com.example.strovo.domain.model.CompModel
import com.example.strovo.domain.repository.HallOfFameRepository

class HallOfFameRepositoryImpl(val local: CompDao): HallOfFameRepository{

    override suspend fun getAllComp(): Result<List<CompModel>> {
        try{
            val localDataComps = local.getAll()
            val comps = localDataComps.map{ CompMapper.toDomain(it) }
            return Result.success(comps)
        }catch (e: Exception){
            return Result.failure(e)
        }
    }

    override suspend fun updateComp(
        id: String,
        data: CompModel
    ): Result<CompModel> {
        try {
            val entity = CompMapper.toEntity(data)
            local.insert(entity)
            return Result.success(data)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun addComp(data: CompModel): Result<CompModel> {
        try {
            val entity = CompMapper.toEntity(data)
            local.insert(entity)
            return Result.success(data)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteComp(id: String): Result<CompModel> {
        try {
            val compToDelete = local.getById(id)
            if (compToDelete != null) {
                local.deleteById(id)
                return Result.success(CompMapper.toDomain(compToDelete))
            } else {
                return Result.failure(Exception("Competition with id $id not found"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun compDetails(id: String): Result<CompModel> {
        try {
            val compEntity = local.getById(id)
            return if (compEntity != null) {
                Result.success(CompMapper.toDomain(compEntity))
            } else {
                Result.failure(Exception("Competition with id $id not found"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun linkActivityToComp(
        compId: String,
        activityId: String
    ): Result<CompModel> {
        try {
            local.linkActivityToComp(compId, activityId)
            val updatedComp = local.getById(compId)
            return if (updatedComp != null) {
                Result.success(CompMapper.toDomain(updatedComp))
            } else {
                Result.failure(Exception("Competition with id $compId not found"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}