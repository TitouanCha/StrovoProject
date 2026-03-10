package com.example.strovo.presentation.stravaAuth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.strovo.data.repository.StravaAuthRepositoryImpl
import com.example.strovo.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StravaAuthViewModel(application: Application): AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)
    private val stravaRepository = StravaAuthRepositoryImpl()

    private val _stravaUiState = MutableStateFlow<StravaAuthUiState>(StravaAuthUiState.Initial)
    val stravaUiState: StateFlow<StravaAuthUiState> = _stravaUiState.asStateFlow()

    fun getStravaToken(code: String) {
        viewModelScope.launch {
            _stravaUiState.value = StravaAuthUiState.Loading
            stravaRepository.getAccessToken(code).onSuccess { tokenResponse ->
                tokenManager.saveTokens(
                    accessToken = tokenResponse.access_token,
                    refreshToken = tokenResponse.refresh_token,
                    athleteId = tokenResponse.athlete.id.toString()
                )
                _stravaUiState.value = StravaAuthUiState.Success(tokenResponse.access_token)
            }.onFailure { exception ->
                _stravaUiState.value = StravaAuthUiState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun refreshStravaToken(refreshToken: String) {
        viewModelScope.launch {
            stravaRepository.refreshAccessToken(refreshToken).onSuccess { tokenResponse ->
                tokenManager.saveTokens(
                    accessToken = tokenResponse.access_token,
                    refreshToken = tokenResponse.refresh_token,
                    athleteId = tokenManager.getAthleteId() ?: ""
                )
                _stravaUiState.value = StravaAuthUiState.Success(tokenResponse.access_token)
            }.onFailure { exception ->
                _stravaUiState.value = StravaAuthUiState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun resetUiState(){
        _stravaUiState.value = StravaAuthUiState.Initial
    }

}