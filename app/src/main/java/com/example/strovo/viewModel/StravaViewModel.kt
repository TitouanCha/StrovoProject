package com.example.strovo.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.BuildConfig
import com.example.strovo.data.ActivityDetailModel
import com.example.strovo.data.AverageStatsModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.data.GetStravaTokenModel
import com.example.strovo.data.RefreshStravaTokenModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.data.MonthlyDistanceModel
import com.example.strovo.data.OverallStats
import com.example.strovo.data.YearStravaActivitiesModel
import com.example.strovo.utils.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.collections.MutableList

open class StravaViewModel(application: Application) : AndroidViewModel(application) {
    val tokenManager = TokenManager(application)

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    fun refreshAccessToken(refreshToken: String, context: Context) {
        viewModelScope.launch {
            var newAccessToken = ""
            var newRefreshToken = ""
            try {
                val response: RefreshStravaTokenModel = RetrofitInstance.authApi.refreshToken(
                    clientId = BuildConfig.STRAVA_CLIENT_ID,
                    clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                    refreshToken = refreshToken
                )
                newAccessToken = response.access_token
                newRefreshToken = response.refresh_token
            } catch (e: Exception) {
                Toast.makeText(context, "Erreur lors du rafraîchissement du token", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error refreshing access token: ${e.message}")
                e.printStackTrace()
            }finally {
                tokenManager.saveTokens(newAccessToken, newRefreshToken, tokenManager.getAthleteId() ?: "")
                _isInitialized.value = true

            }
        }
    }

    fun getAccessToken(codeStrava: String, context: Context) {
        viewModelScope.launch {
            try {
                val response: GetStravaTokenModel = RetrofitInstance.authApi.getAccessToken(
                    clientId = BuildConfig.STRAVA_CLIENT_ID,
                    clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                    code = codeStrava
                )
                val accessToken = response.access_token
                val refreshToken = response.refresh_token
                val athleteId = response.athlete.id

                tokenManager.saveTokens(accessToken, refreshToken, athleteId)
                Toast.makeText(context, "Token Strava enregistré", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(context, "Erreur lors de l'enregistrement du token", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error getting access token: ${e.message}")

                e.printStackTrace()
            }finally {
                _isInitialized.value = true
            }
        }
    }

    suspend fun getActivities(page: Int? = null, perPage: Int? = null, before: String? = null, after: String? = null): GetStravaActivitiesModel?{
        try {
            Log.v("GetActivities", "Fetching activities")
            val activityResponse: GetStravaActivitiesModel = RetrofitInstance.activityApi.getActivities(
                authorization = "Bearer ${tokenManager.getAccessToken()}",
                perPage = perPage,
                page = page,
                before = before,
                after = after
            )
            return activityResponse
        }catch (e: Exception){
            return null
        }
    }

}

