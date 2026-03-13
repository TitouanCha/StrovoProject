package com.example.strovo.domain.repository

import com.example.strovo.domain.model.ActivityDetailModel

interface ActivityDetailRepository {
    suspend fun getActivityDetail(activityId: String): Result<ActivityDetailModel>
}