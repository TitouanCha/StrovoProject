package com.example.strovo.domain.model

import com.example.strovo.data.model.GetActivityLap
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm

data class ActivityLapModel (
    val lapNumber: Int,
    val distance: Int,
    val time: String,
    val timeDouble: Int,
    val avgSpeed: String,
    val avgSpeedDouble: Double,
    val maxSpeed: String,
    val maxSpeedDouble: Double,
    val elevationGain: Double,
    val maxheartRate: Int,
    val minheartRate: Int
){
    companion object{
        fun fromApi(apiData: GetActivityLap, index: Int): ActivityLapModel {
            val distanceKm = apiData.distance.toInt()

            val avgSpeedDouble = speedToPaceMinPerKm(apiData.averageSpeed)
            val maxSpeedDouble = speedToPaceMinPerKm(apiData.maxSpeed)

            return ActivityLapModel(
                lapNumber = index,
                distance = distanceKm,
                time = secondsToHms(apiData.elapsedTime),
                timeDouble = apiData.elapsedTime,
                avgSpeed = avgSpeedDouble,
                avgSpeedDouble = apiData.maxSpeed,
                maxSpeed = maxSpeedDouble,
                maxSpeedDouble = apiData.maxSpeed,
                elevationGain = apiData.totalElevationGain,
                maxheartRate = apiData.maxHeartrate,
                minheartRate = apiData.minHeartrate
            )
        }
    }
}