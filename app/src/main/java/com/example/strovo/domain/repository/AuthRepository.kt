package com.example.strovo.domain.repository

import com.example.strovo.data.model.strava.GetStravaTokenModel
import com.example.strovo.domain.model.IntervalsUserInfos
import com.example.strovo.data.model.strava.RefreshStravaTokenModel

interface AuthRepository {
    suspend fun isUserInfoSaved(): Result<Boolean>
    suspend fun saveUserInfo(athleteId: String, accessToken: String): Result<IntervalsUserInfos>
    suspend fun getAccessToken(code: String): Result<GetStravaTokenModel>
    suspend fun refreshAccessToken(): Result<RefreshStravaTokenModel>
}