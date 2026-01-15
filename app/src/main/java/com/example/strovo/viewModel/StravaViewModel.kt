package com.example.strovo.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import com.example.strovo.BuildConfig
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.data.GetStravaTokenModel
import com.example.strovo.data.RefreshStravaTokenModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.OverallStats
import com.example.strovo.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.contracts.contract

class StravaViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)

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

                Toast.makeText(context, "Token Strava rafraîchi", Toast.LENGTH_SHORT).show()
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

    private val _monthActivities = MutableStateFlow<GetStravaActivitiesModel?>(null)
    val monthActivities: StateFlow<GetStravaActivitiesModel?> = _monthActivities.asStateFlow()
    private val _yearActivities = MutableStateFlow<GetStravaActivitiesModel?>(null)
    val yearActivities: StateFlow<GetStravaActivitiesModel?> = _yearActivities.asStateFlow()
    private val _overallStats = MutableStateFlow<OverallStats?>(null)
    val overallStats: StateFlow<OverallStats?> = _overallStats.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    suspend fun getActivities(page: Int? = null, perPage: Int? = null, before: String? = null, after: String? = null): GetStravaActivitiesModel?{
        try {
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
    fun getMonthActivities(before: String? = null, after: String? = null, context: Context){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null;
            try {
                _monthActivities.value = getActivities(1, 30, before, after)

                val statsResponse: OverallStats = RetrofitInstance.activityApi.getAthleteStats(
                    authorization = "Bearer ${tokenManager.getAccessToken()}",
                    athleteId = "${tokenManager.getAthleteId()}"
                )
                _overallStats.value = statsResponse
                Toast.makeText(context, "Activités récupérées", Toast.LENGTH_SHORT).show()

            }catch (e: Exception){
                Toast.makeText(context, "Erreur lors de la récupération des activités", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error getting activities: ${e.message}")

                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getYearActivities(before: String? = null, after: String? = null, context: Context){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null;
            try {
                val page1: GetStravaActivitiesModel? = getActivities(1, 200, before, after)
                val page2: GetStravaActivitiesModel? = getActivities(2, 200, before, after)
                val pages = GetStravaActivitiesModel().apply {
                    page1?.let { addAll(it) }
                    page2?.let { addAll(it) }
                }
                _yearActivities.value = GetStravaActivitiesModel().apply {
                    addAll(pages.filter { it.type == "Run" })
                }
                Toast.makeText(context, "Activités récupérées", Toast.LENGTH_SHORT).show()

            }catch (e: Exception){
                Toast.makeText(context, "Erreur lors de la récupération des activités", Toast.LENGTH_LONG).show()
                Log.e("StravaViewModel", "Error getting activities: ${e.message}")

                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

