package com.example.strovo.presentation.settings

import com.example.strovo.domain.model.SettingsDataModel

sealed class SettingsUiState {
    object Loading : SettingsUiState()
    data class Success(val settingsData: SettingsDataModel) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}