package com.example.strovo.presentation.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.repository.DashboardRepositoryImpl
import com.example.strovo.domain.model.DashboardModel
import com.example.strovo.data.model.GetOverallStatsModel
import com.example.strovo.data.model.GetStravaActivitiesModel
import com.example.strovo.data.model.toDiscipline
import com.example.strovo.data.repository.StravaAuthRepositoryImpl
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.presentation.progress.ProgressUiState
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)
    private val dashboardRepository = DashboardRepositoryImpl(application)
    private val authRepository = StravaAuthRepositoryImpl(application)

    private val _dashboardUiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val dashboardUiState: StateFlow<DashboardUiState> = _dashboardUiState.asStateFlow()

    val disciplineManager = DisciplineManager(application)

    fun getDashBoardData(before: String, after: String){
        _dashboardUiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            val selectedDiscipline = disciplineManager.getSelectedDisciplines()
            val monthData = dashboardRepository.getMonthData(before, after).getOrElse {
                _dashboardUiState.value = DashboardUiState.Error(it.message ?: "Unknown error")
                return@launch
            }

            val overallStats = dashboardRepository.getOverallStats().getOrElse {
                _dashboardUiState.value = DashboardUiState.Error(it.message ?: "Unknown error")
                return@launch
            }
            val lastActivity = monthData.firstOrNull{
                var activityDiscipline = it.type.toDiscipline()
                activityDiscipline != null && selectedDiscipline.contains(activityDiscipline)
            }

            _dashboardUiState.value = DashboardUiState.Success(
                DashboardModel(
                    lastActivity = lastActivity,
                    monthActivity =  monthData,
                    overallStats = overallStats
                )
            )
        }
    }

    fun refreshToken(before: String, after: String) {
        viewModelScope.launch {
            authRepository.refreshAccessToken().onSuccess { tokenResponse ->
                tokenManager.saveTokens(
                    accessToken = tokenResponse.access_token,
                    refreshToken = tokenResponse.refresh_token,
                    athleteId = tokenManager.getAthleteId() ?: ""
                )
                getDashBoardData(before, after)
            }.onFailure {
                _dashboardUiState.value =
                    DashboardUiState.Error("Failed to refresh token: ${it.message}")
            }
        }
    }
}