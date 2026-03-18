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
            activityDetailRepository.getActivityDetail(activityId).onSuccess { dataResponse ->
                _activityDetailUiState.value = ActivityDetailUiState.Success(dataResponse)
            }.onFailure {
                _activityDetailUiState.value = ActivityDetailUiState.Error(it.message ?: "")
            }
        }
    }
}