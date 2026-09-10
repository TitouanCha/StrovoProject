package com.example.strovo.domain.repository

import com.example.strovo.domain.model.strava.StravaActivityDetailModel

interface ActivityDetailRepository {
    suspend fun getActivityDetail(activityId: String): Result<StravaActivityDetailModel>
}