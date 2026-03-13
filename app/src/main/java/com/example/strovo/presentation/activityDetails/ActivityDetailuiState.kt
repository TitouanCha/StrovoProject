package com.example.strovo.presentation.activityDetails

import com.example.strovo.domain.model.ActivityDetailModel

sealed class ActivityDetailUiState {
    object Loading: ActivityDetailUiState()
    data class Success(val activityDetail: ActivityDetailModel): ActivityDetailUiState()
    data class Error(val message: String): ActivityDetailUiState()
}
