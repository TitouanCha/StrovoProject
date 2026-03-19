package com.example.strovo.data.services

import com.example.strovo.data.model.GetOverallStatsModel
import com.example.strovo.data.model.GetStravaUserInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StravaAthleteServices {

    @GET("/api/v3/athletes/{athlete_id}/stats")
    suspend fun getAthleteStats(
        @Header("Authorization") authorization: String,
        @Path("athlete_id") athleteId: String
    ): GetOverallStatsModel

    @GET("api/v3/athlete")
    suspend fun getAthleteInfo(
        @Header("Authorization") authorization: String
    ): GetStravaUserInfo
}