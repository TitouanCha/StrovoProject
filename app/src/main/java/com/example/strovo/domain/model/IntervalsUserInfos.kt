package com.example.strovo.domain.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class IntervalsUserInfos(
    @SerializedName("id")
    val athleteId: String = "",
    val apiKey: String = "",
    @SerializedName("firstname")
    val firstName: String = "",
    @SerializedName("lastname")
    val lastName: String = ""
)