package com.example.strovo.data.services.intervals

import com.example.strovo.data.model.FetchActivity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActivityServices {

    @GET("athlete/{athlete_id}/activities")
    suspend fun getActivities(
        @Path("athlete_id") athleteId: String,
        @Query("oldest") oldest: String,
        @Query("newest") newest: String,
        @retrofit2.http.Header("Authorization") authorization: String
    ): List<FetchActivity>
}