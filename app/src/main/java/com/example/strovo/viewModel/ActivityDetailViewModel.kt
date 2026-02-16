package com.example.strovo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.strovo.data.ActivityDetailModel
import com.example.strovo.services.RetrofitInstance
import com.example.strovo.utils.decodePolyline
import com.example.strovo.utils.getPointsForLaps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.maplibre.geojson.Point

class ActivityDetailViewModel(application : Application): StravaViewModel(application) {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _activityDetails = MutableStateFlow<ActivityDetailModel?>(null)
    val activityDetails: StateFlow<ActivityDetailModel?> = _activityDetails.asStateFlow()

    private val _trackPoints = MutableStateFlow<List<Pair<Double, Double>>>(emptyList())
    val trackPoints: StateFlow<List<Pair<Double, Double>>> = _trackPoints.asStateFlow()

    private val _lapPoints = MutableStateFlow<List<Point>>(emptyList())
    val lapPoints: StateFlow<List<Point>> = _lapPoints.asStateFlow()

    fun getActivityDetails(activityId: String){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null;
            try {
                val activityDetailResponse: ActivityDetailModel = RetrofitInstance.activityApi.getActivityDetails(
                    authorization = "Bearer ${tokenManager.getAccessToken()}",
                    activityId = activityId
                )
                _activityDetails.value = activityDetailResponse
                _trackPoints.value = decodePolyline(activityDetailResponse.map.polyline)
                _lapPoints.value = getPointsForLaps(activityDetailResponse.laps, _trackPoints.value)

            }catch (e: Exception){
                Log.e("StravaViewModel", "Error getting activity details: ${e.message}")

                _errorMessage.value = e.message.toString()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}