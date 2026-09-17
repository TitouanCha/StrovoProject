package com.example.strovo.presentation.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.model.strava.GetOverallStatsModel
import com.example.strovo.data.repository.DashboardRepositoryImpl
import com.example.strovo.domain.model.DashboardModel
import com.example.strovo.data.model.toDiscipline
import com.example.strovo.data.repository.AuthRepositoryImpl
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)
    private val disciplineManager = DisciplineManager(application)
    private val dashboardRepository = DashboardRepositoryImpl(application)
    private val authRepository = AuthRepositoryImpl(application)

    private val _dashboardUiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val dashboardUiState: StateFlow<DashboardUiState> = _dashboardUiState.asStateFlow()


    fun getDashBoardData(){
        val now = LocalDateTime.now()
        val beforeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        val afterDate = now.minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))

        _dashboardUiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            val selectedDiscipline = disciplineManager.getSelectedDisciplines()
            val monthData = dashboardRepository.getMonthData(beforeDate, afterDate).getOrElse {
                _dashboardUiState.value = DashboardUiState.Error(it.message ?: "Unknown error")
                return@launch
            }


            val lastActivity = monthData.firstOrNull{
                var activityDiscipline = it.type.toDiscipline()
                activityDiscipline != null && selectedDiscipline.contains(activityDiscipline)
            }
            val healthData = dashboardRepository.getHealthStats().getOrElse {
                _dashboardUiState.value = DashboardUiState.Error(it.message ?: "Unknown error")
                return@launch
            }
            _dashboardUiState.value = DashboardUiState.Success(
                DashboardModel(
                    lastActivity = lastActivity,
                    monthActivity = monthData,
                    selectedDiscipline = selectedDiscipline,
                    healthData = healthData,
                    overallStats = null
                )
            )
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            authRepository.refreshAccessToken().onSuccess { tokenResponse ->
                tokenManager.saveTokens(
                    accessToken = tokenResponse.access_token,
                    refreshToken = tokenResponse.refresh_token,
                    athleteId = tokenManager.getAthleteId() ?: ""
                )
                getDashBoardData()
            }.onFailure {
                _dashboardUiState.value =
                    DashboardUiState.Error("Failed to refresh token: ${it.message}")
            }
        }
    }
}