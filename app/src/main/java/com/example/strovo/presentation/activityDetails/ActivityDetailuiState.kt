package com.example.strovo.presentation.activityDetails

import com.example.strovo.domain.model.strava.StravaActivityDetailModel

sealed class ActivityDetailUiState {
    object Loading: ActivityDetailUiState()
    data class Success(val activityDetail: StravaActivityDetailModel): ActivityDetailUiState()
    data class Error(val message: String): ActivityDetailUiState()
}
