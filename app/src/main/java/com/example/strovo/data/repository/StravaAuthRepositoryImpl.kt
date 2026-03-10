package com.example.strovo.data.repository

import android.content.Context
import android.widget.Toast
import androidx.navigation.internal.NavContext
import com.example.strovo.domain.repository.StravaAuthRepository
import com.example.strovo.model.GetStravaTokenModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.BuildConfig
import com.example.strovo.model.RefreshStravaTokenModel
import com.example.strovo.util.TokenManager

class StravaAuthRepositoryImpl(): StravaAuthRepository {
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

    override suspend fun refreshAccessToken(refreshToken: String): Result<RefreshStravaTokenModel> {
        return try {
            val response: RefreshStravaTokenModel = RetrofitInstance.authApi.refreshToken(
                clientId = BuildConfig.STRAVA_CLIENT_ID,
                clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                refreshToken = refreshToken
            )
            Result.success(response)
        } catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }
}