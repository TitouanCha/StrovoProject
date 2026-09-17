package com.example.strovo.data.services.intervals

import com.example.strovo.data.model.FetchHealthData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface HealthServices {

    @GET("athlete/{athlete_id}/wellness")
    suspend fun getHealthData(
        @Path("athlete_id") athleteId: String,
        @Query("oldest") oldest: String,
        @Query("newest") newest: String,
        @Header("Authorization") authorization: String
    ): List<FetchHealthData>

}