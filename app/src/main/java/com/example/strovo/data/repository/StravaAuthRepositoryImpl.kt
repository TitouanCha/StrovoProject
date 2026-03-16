package com.example.strovo.data.repository

import android.content.Context
import com.example.strovo.domain.repository.StravaAuthRepository
import com.example.strovo.data.model.GetStravaTokenModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.BuildConfig
import com.example.strovo.data.model.RefreshStravaTokenModel
import com.example.strovo.data.utils.TokenManager

class StravaAuthRepositoryImpl(context: Context): StravaAuthRepository {
    private val tokenManager = TokenManager(context)
    override suspend fun getAccessToken(code: String): Result<GetStravaTokenModel>{
        return try {
            val response: GetStravaTokenModel = RetrofitInstance.authApi.getAccessToken(
                clientId = BuildConfig.STRAVA_CLIENT_ID,
                clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                code = code
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun refreshAccessToken(): Result<RefreshStravaTokenModel> {
        return try {
            val response: RefreshStravaTokenModel = RetrofitInstance.authApi.refreshToken(
                clientId = BuildConfig.STRAVA_CLIENT_ID,
                clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                refreshToken = tokenManager.getRefreshToken() ?: throw Exception("Refresh token not found")
            )
            Result.success(response)
        } catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }
}