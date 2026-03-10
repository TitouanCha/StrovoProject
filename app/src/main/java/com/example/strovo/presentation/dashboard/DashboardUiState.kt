package com.example.strovo.presentation.dashboard

import com.example.strovo.domain.model.DashboardModel

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val dashboardData: DashboardModel) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}