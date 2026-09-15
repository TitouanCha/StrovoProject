package com.example.strovo.data.services.intervals

import com.example.strovo.data.model.FetchActivity
import com.example.strovo.data.model.GetActivityMetaData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ActivityServices {

    @GET("athlete/{athlete_id}/activities")
    suspend fun getActivities(
        @Path("athlete_id") athleteId: String,
        @Query("oldest") oldest: String,
        @Query("newest") newest: String,
        @Header("Authorization") authorization: String
    ): List<FetchActivity>

    @GET("activity/{activity_id}?intervals=true")
    suspend fun getActivityDetail(
        @Path("activity_id") activityId: String,
        @Header("Authorization") authorization: String
    ): FetchActivity

    @GET("activity/{activity_id}/streams.json")
    suspend fun getActivityMetaData(
        @Path("activity_id") activityId: String,
        @Header("Authorization") authorization: String
    ): List<GetActivityMetaData>
}