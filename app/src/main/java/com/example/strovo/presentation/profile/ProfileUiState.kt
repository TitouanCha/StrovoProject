package com.example.strovo.presentation.profile

import com.example.strovo.domain.model.SettingsDataModel

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val settingsData: SettingsDataModel) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}