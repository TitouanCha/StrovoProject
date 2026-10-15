package com.example.strovo.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.repository.ProfileRepositoryImpl
import com.example.strovo.domain.model.AthletePB
import com.example.strovo.domain.model.SettingsDataModel
import com.example.strovo.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl(application)

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    fun loadProfile() {
        _profileUiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            var userDiscipline = listOf<Discipline>()
            profileRepository.getSelectedDiscipline().onSuccess{ discipline ->
                userDiscipline = discipline
            }.onFailure {
                _profileUiState.value = ProfileUiState.Error("Impossible de recuperer la liste des disciplines")
                return@launch
            }

            var athletePb: AthletePB? = null
            profileRepository.getAthletePB().onSuccess { pbs ->
                athletePb = pbs
            }.onFailure {
                _profileUiState.value = ProfileUiState.Error("Impossible de récupérer les records personnels")
                return@launch
            }

            _profileUiState.value = ProfileUiState.Success(
                SettingsDataModel(
                    userName = profileRepository.getUserStravaName(),
                    selectedDisciplines = userDiscipline,
                    allDisciplines = profileRepository.getDisciplineList(),
                    userPb = athletePb
                )
            )
        }
    }


    fun addDisciplines(disciplines: List<Discipline>) {
        viewModelScope.launch {
            profileRepository.addDisciplines(disciplines).onSuccess { disciplines ->
                val currentState = _profileUiState.value
                if (currentState is ProfileUiState.Success) {
                    _profileUiState.value = ProfileUiState.Success(
                        currentState.settingsData.copy(
                            selectedDisciplines = disciplines
                        )
                    )
                }
            }
        }
    }

    fun logOut(){
    }
}