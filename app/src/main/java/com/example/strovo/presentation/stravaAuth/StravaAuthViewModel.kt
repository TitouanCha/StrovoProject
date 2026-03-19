package com.example.strovo.presentation.stravaAuth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.strovo.data.repository.StravaAuthRepositoryImpl
import com.example.strovo.data.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.FirstLaunchManager
import kotlinx.coroutines.launch

class StravaAuthViewModel(application: Application): AndroidViewModel(application) {
    private val tokenManager = TokenManager(application)
    private val disciplineManager = DisciplineManager(application)
    private val firstLaunchManager = FirstLaunchManager(application)
    private val stravaRepository = StravaAuthRepositoryImpl(application)

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
                firstLaunchManager.setFirstLaunch(false)
            }.onFailure { exception ->
                _stravaUiState.value = StravaAuthUiState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun refreshStravaToken() {
        val refreshToken = tokenManager.getRefreshToken()
        if(refreshToken == null){
            _stravaUiState.value = StravaAuthUiState.Initial
            return
        }
        viewModelScope.launch {
            _stravaUiState.value = StravaAuthUiState.Loading
            stravaRepository.refreshAccessToken().onSuccess { tokenResponse ->
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

    fun saveUserDisciplines(disciplines: List<Discipline>) {
        viewModelScope.launch {
            disciplineManager.saveSelectedDisciplines(disciplines)
        }
    }

    fun resetUiState(){
        _stravaUiState.value = StravaAuthUiState.Initial
    }

}