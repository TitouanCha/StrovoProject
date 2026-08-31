package com.example.strovo.data.repository

import android.content.Context
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.data.utils.mapUtils.decodePolyline
import com.example.strovo.domain.repository.ActivityDetailRepository
import com.example.strovo.model.strava.StravaActivityDetailModel
import com.example.strovo.data.services.strava.StravaRetrofitInstance
import com.example.strovo.data.utils.getPointsForKm
import com.example.strovo.data.utils.getPointsForLaps
import kotlinx.serialization.internal.throwMissingFieldException

class ActivityDetailRepositoryImpl(context: Context): ActivityDetailRepository {
    private val tokenManager = TokenManager(context)
    override suspend fun getActivityDetail(activityId: String): Result<com.example.strovo.domain.model.strava.StravaActivityDetailModel> {
        TODO("Not yet implemented")
    }

//    override suspend fun getActivityDetail(activityId: String): Result<StravaActivityDetailModel> {
//        return try {
//            val activityDetailResponse: com.example.strovo.model.strava.StravaActivityDetailModel = StravaRetrofitInstance.activityApi.getActivityDetails(
//                authorization = "Bearer ${tokenManager.getAccessToken()}",
//                activityId = activityId
//            )
//            Result.success(StravaActivityDetailModel(
//                activityDetail = activityDetailResponse,
//                trackPoints = decodePolyline(activityDetailResponse.map.polyline),
//                kmPoints = getPointsForKm(
//                    (activityDetailResponse.distance/1000).toInt(),
//                    decodePolyline(activityDetailResponse.map.polyline)
//                ),
//                lapPoints = getPointsForLaps(
//                    activityDetailResponse.laps,
//                    decodePolyline(activityDetailResponse.map.polyline)
//                )
//            ))
//        }catch (e: Exception){
//            Result.failure(e)
//        }
//    }

}