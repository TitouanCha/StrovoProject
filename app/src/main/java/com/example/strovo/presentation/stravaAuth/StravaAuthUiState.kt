package com.example.strovo.presentation.stravaAuth

sealed class StravaAuthUiState{
    object Initial : StravaAuthUiState()
    object Loading : StravaAuthUiState()
    data class Success(val accessToken: String) : StravaAuthUiState()
    data class Error(val message: String) : StravaAuthUiState()
}