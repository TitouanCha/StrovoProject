package com.example.strovo.domain.repository

import com.example.strovo.model.GetStravaTokenModel
import com.example.strovo.model.RefreshStravaTokenModel

interface StravaAuthRepository {
    suspend fun getAccessToken(code: String): Result<GetStravaTokenModel>
    suspend fun refreshAccessToken(refreshToken: String): Result<RefreshStravaTokenModel>
}