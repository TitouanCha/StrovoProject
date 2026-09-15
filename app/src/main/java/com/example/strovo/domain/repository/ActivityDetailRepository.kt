package com.example.strovo.domain.repository

import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.domain.model.ActivityMetaData

interface ActivityDetailRepository {
    suspend fun getActivityMetaData(activityId: String): Result<ActivityMetaData>
    suspend fun getActivityDetail(activityId: String): Result<ActivityDetailModel>
}