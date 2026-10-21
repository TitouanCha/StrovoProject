package com.example.strovo.presentation.hallOfFame

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import com.example.strovo.data.local.competiton.CompDao
import com.example.strovo.data.repository.HallOfFameRepositoryImpl
import com.example.strovo.domain.repository.HallOfFameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HallOfFameViewModel(val hallOfFameRepository: HallOfFameRepository) : ViewModel() {

    private val _hallOfFameUiState = MutableStateFlow<HallOfFameUiState>(HallOfFameUiState.Loading)
    var hallOfFameUiState: StateFlow<HallOfFameUiState> = _hallOfFameUiState.asStateFlow()

    fun loadComp() {
        _hallOfFameUiState.value = HallOfFameUiState.Loading
        viewModelScope.launch {
            val result = hallOfFameRepository.getAllComp()
            result.onSuccess { compData ->
                _hallOfFameUiState.value = HallOfFameUiState.Success(compData)
            }.onFailure { exception ->
                _hallOfFameUiState.value =
                    HallOfFameUiState.Error(exception.message ?: "Unknown error")
            }
        }
    }
}