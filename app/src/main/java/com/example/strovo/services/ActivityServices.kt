package com.example.strovo.services

import com.example.strovo.data.ActivityDetailModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.OverallStats
import com.example.strovo.screen.Screen
import org.intellij.lang.annotations.Pattern
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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

    @GET("/api/v3/athletes/{athlete_id}/stats")
    suspend fun getAthleteStats(
        @Header("Authorization") authorization: String,
        @Path("athlete_id") athleteId: String
    ): OverallStats

    @GET("https://www.strava.com/api/v3/activities/{activity_id}")
    suspend fun getActivityDetails(
        @Header("Authorization") authorization: String,
        @Path("activity_id") activityId: String
    ): ActivityDetailModel

}