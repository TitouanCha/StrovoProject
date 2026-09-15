package com.example.strovo.data.model

import com.google.gson.annotations.SerializedName

data class GetActivityLap(
    @SerializedName("average_cadence")
    val averageCadence: Double,
    @SerializedName("average_heartrate")
    val averageHeartrate: Int,
    @SerializedName("average_speed")
    val averageSpeed: Double,
    @SerializedName("average_step_length")
    val averageStepLength: Double,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("elapsed_time")
    val elapsedTime: Int,
    @SerializedName("end_index")
    val endIndex: Int,
    @SerializedName("end_time")
    val endTime: Int,
    @SerializedName("gap")
    val gap: Double,
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("intensity")
    val intensity: Int,
    @SerializedName("max_altitude")
    val maxAltitude: Double,
    @SerializedName("max_cadence")
    val maxCadence: Int,
    @SerializedName("max_heartrate")
    val maxHeartrate: Int,
    @SerializedName("max_speed")
    val maxSpeed: Double,
    @SerializedName("min_altitude")
    val minAltitude: Double,
    @SerializedName("min_cadence")
    val minCadence: Int,
    @SerializedName("min_heartrate")
    val minHeartrate: Int,
    @SerializedName("min_speed")
    val minSpeed: Double,
    @SerializedName("moving_time")
    val movingTime: Int,
    @SerializedName("start_index")
    val startIndex: Int,
    @SerializedName("start_time")
    val startTime: Int,
    @SerializedName("total_elevation_gain")
    val totalElevationGain: Double,
    @SerializedName("type")
    val type: String,
    @SerializedName("zone")
    val zone: Int
)