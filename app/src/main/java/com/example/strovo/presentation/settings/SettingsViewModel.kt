package com.example.strovo.presentation.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.model.Discipline
import com.example.strovo.data.repository.SettingsRepositoryImpl
import com.example.strovo.domain.model.SettingsDataModel
import com.example.strovo.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application): AndroidViewModel(application) {
    private val settingsRepository: SettingsRepository = SettingsRepositoryImpl(application)

    private val _settingsUiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    fun loadSettings() {
        _settingsUiState.value = SettingsUiState.Loading
        viewModelScope.launch {
            var userDiscipline = listOf<Discipline>()
            settingsRepository.getSelectedDiscipline().onSuccess{ discipline ->
                userDiscipline = discipline
            }.onFailure {
                _settingsUiState.value = SettingsUiState.Error("Impossible de recuperer la liste des disciplines")
            }

            _settingsUiState.value = SettingsUiState.Success(
                SettingsDataModel(
                    userName = settingsRepository.getUserStravaName(),
                    selectedDisciplines = userDiscipline,
                    allDisciplines = settingsRepository.getDisciplineList()
                )
            )
        }
    }

    fun addDisciplines(disciplines: List<Discipline>) {
        viewModelScope.launch {
            settingsRepository.addDisciplines(disciplines).onSuccess { disciplines ->
                val currentState = _settingsUiState.value
                if (currentState is SettingsUiState.Success) {
                    _settingsUiState.value = SettingsUiState.Success(
                        currentState.settingsData.copy(
                            selectedDisciplines = disciplines
                        )
                    )
                }
            }
        }
    }
}