package com.example.strovo.presentation.progress

import com.example.strovo.domain.model.ProgressModel

sealed class ProgressUiState {
    object Loading : ProgressUiState()
    data class Success(val progressData: ProgressModel) : ProgressUiState()
    data class Error(val message: String) : ProgressUiState()
}