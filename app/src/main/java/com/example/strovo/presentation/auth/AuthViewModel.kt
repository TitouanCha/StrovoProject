package com.example.strovo.presentation.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.strovo.data.repository.AuthRepositoryImpl
import com.example.strovo.data.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.utils.DisciplineManager
import com.example.strovo.data.utils.FirstLaunchManager
import com.example.strovo.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    val tokenManager: TokenManager,
    val disciplineManager: DisciplineManager,
    val firstLaunchManager: FirstLaunchManager,
    val authRepository: AuthRepository
): ViewModel() {

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    fun saveUserInfo(athleteId: String, apiKey: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading

            authRepository.saveUserInfo(athleteId, apiKey).onSuccess { response ->
                tokenManager.saveIntervalsAthleteInfo(
                    athleteId = response.athleteId,
                    apiKey = response.apiKey
                )
                _authUiState.value = AuthUiState.Success(response.apiKey)
                firstLaunchManager.setFirstLaunch(false)
            }.onFailure { exception ->
                _authUiState.value = AuthUiState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun getUserInfo() {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            authRepository.isUserInfoSaved().onSuccess { response ->
                if(response){
                    _authUiState.value = AuthUiState.Success(tokenManager.getAccessToken().toString())
                }else{
                    _authUiState.value = AuthUiState.Error("Impossible de récupérer les informations de l'utilisateur.\n Veuillez vous reconnecter.")
                }
            }.onFailure { exception ->
                _authUiState.value = AuthUiState.Error("Impossible de récupérer les informations de l'utilisateur.\n Veuillez vous reconnecter.")
            }
            //_authUiState.value = AuthUiState.Initial
        }
    }

    fun refreshStravaToken() {
        val refreshToken = tokenManager.getRefreshToken()
        if(refreshToken == null){
            _authUiState.value = AuthUiState.Initial
            return
        }
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            authRepository.refreshAccessToken().onSuccess { tokenResponse ->
                tokenManager.saveTokens(
                    accessToken = tokenResponse.access_token,
                    refreshToken = tokenResponse.refresh_token,
                    athleteId = tokenManager.getAthleteId() ?: ""
                )
                _authUiState.value = AuthUiState.Success(tokenResponse.access_token)
            }.onFailure { exception ->
                _authUiState.value = AuthUiState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun saveUserDisciplines(disciplines: List<Discipline>) {
        viewModelScope.launch {
            disciplineManager.saveSelectedDisciplines(disciplines)
        }
    }

    fun resetUiState(){
        _authUiState.value = AuthUiState.Initial
    }

}