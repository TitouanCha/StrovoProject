package com.example.strovo.presentation.activityDetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.repository.ActivityDetailRepositoryImpl
import com.example.strovo.model.StravaActivityDetailModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.data.utils.getPointsForLaps
import com.example.strovo.data.utils.mapUtils.decodePolyline
import com.example.strovo.domain.repository.ActivityDetailRepository
import com.example.strovo.presentation.dashboard.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.maplibre.geojson.Point

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