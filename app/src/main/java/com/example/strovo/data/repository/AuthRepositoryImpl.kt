package com.example.strovo.data.repository

import android.content.Context
import com.example.strovo.domain.repository.AuthRepository
import com.example.strovo.data.model.GetStravaTokenModel
import com.example.strovo.data.services.strava.StravaRetrofitInstance
import com.example.strovo.BuildConfig
import com.example.strovo.domain.model.IntervalsUserInfos
import com.example.strovo.data.model.RefreshStravaTokenModel
import com.example.strovo.data.services.intervals.IntervalsRetrofitClient
import com.example.strovo.data.services.intervals.basicAuthHeader
import com.example.strovo.data.utils.TokenManager
import kotlin.toString

class AuthRepositoryImpl(context: Context): AuthRepository {
    private val tokenManager = TokenManager(context)

    override suspend fun saveUserInfo(athleteId: String, accessToken: String): Result<IntervalsUserInfos> {
        return try {
            val authString = basicAuthHeader(accessToken)
            val response: IntervalsUserInfos = IntervalsRetrofitClient.authApi.getUserInfos(
                athleteId = athleteId,
                authorization = authString
            )
            val userInfos = IntervalsUserInfos(
                athleteId = athleteId,
                apiKey = accessToken,
                firstName = response.firstName,
                lastName = response.lastName
            )
            Result.success(userInfos)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getAccessToken(code: String): Result<GetStravaTokenModel>{
        return try {
            val response: GetStravaTokenModel = StravaRetrofitInstance.authApi.getAccessToken(
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
            val response: RefreshStravaTokenModel = StravaRetrofitInstance.authApi.refreshToken(
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