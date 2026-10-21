package com.example.strovo.presentation.hallOfFame

import com.example.strovo.domain.model.CompModel

sealed class HallOfFameUiState {
    object Loading : HallOfFameUiState()
    data class Success(val compData: List<CompModel>) : HallOfFameUiState()
    data class Error(val message: String) : HallOfFameUiState()
}