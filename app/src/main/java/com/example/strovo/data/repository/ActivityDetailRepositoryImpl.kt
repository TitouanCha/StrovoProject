package com.example.strovo.data.repository

import android.content.Context
import android.util.Log
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.data.utils.getPointsForLaps
import com.example.strovo.data.utils.mapUtils.decodePolyline
import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.domain.repository.ActivityDetailRepository
import com.example.strovo.model.StravaActivityDetailModel
import com.example.strovo.presentation.activityDetails.ActivityDetailUiState
import com.example.strovo.services.RetrofitInstance

class ActivityDetailRepositoryImpl(context: Context): ActivityDetailRepository {
    private val tokenManager = TokenManager(context)

    override suspend fun getActivityDetail(activityId: String): Result<ActivityDetailModel> {
        return try {
            val activityDetailResponse: StravaActivityDetailModel = RetrofitInstance.activityApi.getActivityDetails(
                authorization = "Bearer ${tokenManager.getAccessToken()}",
                activityId = activityId
            )
            Result.success(ActivityDetailModel(
                activityDetail = activityDetailResponse,
                trackPoints = decodePolyline(activityDetailResponse.map.polyline),
                lapPoints = getPointsForLaps(activityDetailResponse.laps, decodePolyline(activityDetailResponse.map.polyline)
            )))
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}