package com.example.strovo.data.model

import com.google.gson.annotations.SerializedName

data class FetchHealthData(
    @SerializedName("id")
    val date: String,
    val atl: Double,
    val atlLoad: Double,
    val avgSleepingHR: Any,
    val ctl: Double,
    val ctlLoad: Double,
    val fatigue: Any,
    val kcalConsumed: Any,
    val rampRate: Double,
    val restingHR: Int,
    val sleepQuality: Any,
    val sleepScore: Any,
    val sleepSecs: Int,
    val soreness: Any,
    val spO2: Double,
    val steps: Any,
    val stress: Any,
    val vo2max: Double
)