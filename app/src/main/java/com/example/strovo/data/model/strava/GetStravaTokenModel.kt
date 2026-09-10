package com.example.strovo.data.model.strava

import com.example.strovo.model.strava.Athlete

data class GetStravaTokenModel(
    val access_token: String,
    val expires_at: Int,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String,
    val athlete: Athlete
)