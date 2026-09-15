package com.example.strovo.data.model

import androidx.compose.foundation.lazy.layout.IntervalList
import com.google.gson.annotations.SerializedName

data class FetchActivity(
    var id: String = "",
    @SerializedName("start_date_local")
    var startDateLocal: String = "",
    var type: String = "",
    var name: String = "",
    @SerializedName("icu_recording_time")
    var icuRecordingTime: Int = 0,
    @SerializedName("moving_time")
    var movingTime: Int = 0,
    @SerializedName("start_date")
    var startDate: String = "",
    var distance: Double = 0.0,
    @SerializedName("total_elevation_gain")
    var totalElevationGain: Double = 0.0,
    @SerializedName("total_elevation_loss")
    var totalElevationLoss: Double = 0.0,
    @SerializedName("max_speed")
    var maxSpeed: Double = 0.0,
    @SerializedName("average_speed")
    var averageSpeed: Double = 0.0,
    @SerializedName("max_heartrate")
    var maxHeartrate: Int = 0,
    @SerializedName("average_heartrate")
    var averageHeartrate: Double = 0.0,
    @SerializedName("average_cadence")
    var averageCadence: Double = 0.0,
    @SerializedName("calories")
    var calories: Int = 0,
    @SerializedName("min_altitude")
    var minAltitude: Double = 0.0,
    @SerializedName("max_altitude")
    var maxAltitude: Double = 0.0,
    @SerializedName("icu_intervals")
    var icuIntervals: List<GetActivityLap> = emptyList()
)