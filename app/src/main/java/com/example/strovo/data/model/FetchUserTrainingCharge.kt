package com.example.strovo.data.model

data class FetchUserTrainingCharge(
    val atl: Double,
    val atlLoad: Double,
    val ctl: Double,
    val ctlLoad: Double,
    val id: String,
    val rampRate: Double,
    val restingHR: Int,
    val sleepSecs: Int,
    val spO2: Double,
    val updated: String
)