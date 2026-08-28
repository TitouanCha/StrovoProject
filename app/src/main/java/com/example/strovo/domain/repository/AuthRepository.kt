package com.example.strovo.domain.repository

import com.example.strovo.data.model.GetStravaTokenModel
import com.example.strovo.domain.model.IntervalsUserInfos
import com.example.strovo.data.model.RefreshStravaTokenModel

interface AuthRepository {
    suspend fun saveUserInfo(athleteId: String, accessToken: String): Result<IntervalsUserInfos>
    suspend fun getAccessToken(code: String): Result<GetStravaTokenModel>
    suspend fun refreshAccessToken(): Result<RefreshStravaTokenModel>
}