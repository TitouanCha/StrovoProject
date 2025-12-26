package com.example.strovo.data

data class RefreshStravaTokenModel(
    val access_token: String,
    val expires_at: Int,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)