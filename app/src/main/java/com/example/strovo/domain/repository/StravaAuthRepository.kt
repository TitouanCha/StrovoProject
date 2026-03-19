package com.example.strovo.domain.repository

import com.example.strovo.data.model.GetStravaTokenModel
import com.example.strovo.data.model.RefreshStravaTokenModel

interface StravaAuthRepository {
    suspend fun getAccessToken(code: String): Result<GetStravaTokenModel>
    suspend fun refreshAccessToken(): Result<RefreshStravaTokenModel>
}