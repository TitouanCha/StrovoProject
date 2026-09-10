package com.example.strovo.presentation.auth

sealed class AuthUiState{
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val accessToken: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}