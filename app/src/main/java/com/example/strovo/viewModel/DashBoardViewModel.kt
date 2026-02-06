package com.example.strovo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.OverallStats
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : StravaViewModel(application) {
    private val _overallStats = MutableStateFlow<OverallStats?>(null)
    val overallStats: StateFlow<OverallStats?> = _overallStats.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _monthActivities = MutableStateFlow<GetStravaActivitiesModel?>(null)
    val monthActivities: StateFlow<GetStravaActivitiesModel?> = _monthActivities.asStateFlow()

    fun getMonthActivities(before: String, after: String){
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

            }catch (e: Exception){
                Log.e("StravaViewModel", "Error getting activities: ${e.message}")

                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}