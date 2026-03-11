package com.example.strovo.presentation.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.repository.DashboardRepositoryImpl
import com.example.strovo.domain.model.DashboardModel
import com.example.strovo.data.model.GetOverallStatsModel
import com.example.strovo.data.model.GetStravaActivitiesModel
import com.example.strovo.viewModel.StravaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val dashboardRepository = DashboardRepositoryImpl(application)

    private val _dashboardUiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val dashboardUiState: StateFlow<DashboardUiState> = _dashboardUiState.asStateFlow()

    lateinit var monthData: GetStravaActivitiesModel
    lateinit var overallStats: GetOverallStatsModel

    fun getDashBoardData(before: String, after: String){
        _dashboardUiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            dashboardRepository.getMonthData(before, after).onSuccess { dataResponse ->
                monthData = dataResponse
            }.onFailure {
                _dashboardUiState.value = DashboardUiState.Error(it.message ?: "Unknown error")
            }

            dashboardRepository.getOverallStats().onSuccess { dataResponse ->
                overallStats = dataResponse
            }.onFailure {
                _dashboardUiState.value = DashboardUiState.Error(it.message ?: "Unknown error")
            }
            _dashboardUiState.value = DashboardUiState.Success( DashboardModel(monthData, overallStats) )
        }
    }
}