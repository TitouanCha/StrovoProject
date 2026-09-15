package com.example.strovo.data.repository

import android.content.Context
import com.example.strovo.data.model.GetActivityMetaData
import com.example.strovo.data.services.intervals.IntervalsRetrofitClient
import com.example.strovo.data.services.intervals.basicAuthHeader
import com.example.strovo.data.utils.TokenManager
import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.domain.repository.ActivityDetailRepository
import com.example.strovo.domain.model.ActivityMetaData

class ActivityDetailRepositoryImpl(context: Context): ActivityDetailRepository {

    private val tokenManager = TokenManager(context)

    override suspend fun getActivityMetaData(activityId: String): Result<ActivityMetaData> {
        return try{
            val connectionString = basicAuthHeader(tokenManager.getAccessToken().toString())
            val activityDetailResponse: List<GetActivityMetaData> = IntervalsRetrofitClient.activityApi.getActivityMetaData(
                authorization = connectionString,
                activityId = activityId
            )
            val activityMetaData = ActivityMetaData.fromApi(activityId, activityDetailResponse)
            Result.success(activityMetaData)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getActivityDetail(activityId: String): Result<ActivityDetailModel> {
        return try {
            val connectionString = basicAuthHeader(tokenManager.getAccessToken().toString())
            val activityDetailResponse = IntervalsRetrofitClient.activityApi.getActivityDetail(
                authorization = connectionString,
                activityId = activityId
            )
            val activityDetailModel = ActivityDetailModel.fromApi(activityDetailResponse)
            Result.success(activityDetailModel)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}