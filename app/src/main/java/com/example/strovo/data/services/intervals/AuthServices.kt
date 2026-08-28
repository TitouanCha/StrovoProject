package com.example.strovo.data.services.intervals

import com.example.strovo.domain.model.IntervalsUserInfos
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IntervalsAuthServices {

    @GET("athlete/{athlete_id}")
    suspend fun getUserInfos(
        @Path("athlete_id") athleteId: String,
        @Header("Authorization") authorization: String
    ): IntervalsUserInfos
}