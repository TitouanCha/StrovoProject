package com.example.strovo.presentation.activityDetails

import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.domain.model.ActivityMetaData

sealed class ActivityDetailUiState {
    object Loading: ActivityDetailUiState()
    data class Success(val activityMetaData: ActivityMetaData, val activityDetail: ActivityDetailModel): ActivityDetailUiState()
    data class Error(val message: String): ActivityDetailUiState()
}
