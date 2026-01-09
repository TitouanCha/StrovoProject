package com.example.strovo.services

import com.example.strovo.data.GetStravaActivitiesModel
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StravaActivityServices {

    @GET("api/v3/athlete/activities")
    suspend fun getActivities(
        @Header("Authorization") authorization: String,
        @Query("per_page") perPage: Int?,
        @Query("page") page: Int?,
        @Query("before") before: String?,
        @Query("after") after: String?
    ): GetStravaActivitiesModel

}