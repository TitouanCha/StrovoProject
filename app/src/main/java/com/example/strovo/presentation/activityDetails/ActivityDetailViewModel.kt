package com.example.strovo.presentation.activityDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.repository.ActivityDetailRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityDetailViewModel(application : Application): AndroidViewModel(application) {
    private val activityDetailRepository = ActivityDetailRepositoryImpl(application)

    private val _activityDetailUiState = MutableStateFlow<ActivityDetailUiState>(ActivityDetailUiState.Loading)
    val activityDetailUiState: StateFlow<ActivityDetailUiState> = _activityDetailUiState.asStateFlow()

    fun getActivityDetails(activityId: String){
        viewModelScope.launch {
            val metaDataResponse = activityDetailRepository.getActivityMetaData(activityId)
            val metaData = metaDataResponse.getOrElse {
                _activityDetailUiState.value = ActivityDetailUiState.Error(it.message ?: "")
                return@launch
            }

            val activityDetailResponse = activityDetailRepository.getActivityDetail(activityId)
            val activityDetail = activityDetailResponse.getOrElse {
                _activityDetailUiState.value = ActivityDetailUiState.Error(it.message ?: "")
                return@launch
            }

            _activityDetailUiState.value = ActivityDetailUiState.Success(
                activityMetaData = metaData,
                activityDetail = activityDetail
            )
        }
    }
}