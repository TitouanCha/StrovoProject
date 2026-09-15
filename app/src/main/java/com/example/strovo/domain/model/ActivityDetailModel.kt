package com.example.strovo.domain.model

import com.example.strovo.data.model.FetchActivity
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm
import java.time.LocalDate

data class ActivityDetailModel (
    val id: String,
    val name: String,
    val distance: Double,
    val time: String,
    val timeDouble: Int,
    val avgSpeed: String,
    val avgSpeedDouble: Double,
    val maxSpeed: String,
    val maxSpeedDouble: Double,
    val elevationGain: Int,
    val date: LocalDate,
    val type: String,
    val laps: List<ActivityLapModel>
){
    companion object {
        fun fromApi(apiData: FetchActivity): ActivityDetailModel {
            val distanceKm = (apiData.distance / 1000.0 * 100.0).toInt() / 100.0
            val time = secondsToHms(apiData.icuRecordingTime)
            val avgPace = speedToPaceMinPerKm(apiData.averageSpeed)
            val maxPace = speedToPaceMinPerKm(apiData.averageSpeed)
            var laps = emptyList<ActivityLapModel>()
            if(apiData.icuIntervals.isNotEmpty()){
                laps = apiData.icuIntervals.map { lap ->
                    ActivityLapModel.fromApi(lap, apiData.icuIntervals.indexOf(lap) + 1)
                }
            }
            return ActivityDetailModel(
                id = apiData.id.toString(),
                name = apiData.name,
                distance = distanceKm,
                time = time,
                timeDouble = apiData.movingTime,
                avgSpeed = avgPace,
                avgSpeedDouble = apiData.averageSpeed,
                maxSpeed = maxPace,
                maxSpeedDouble = apiData.maxSpeed,
                elevationGain = apiData.totalElevationGain.toInt(),
                date = LocalDate.parse(apiData.startDateLocal.substring(0, 10)),
                type = apiData.type,
                laps = laps
            )
        }
    }
}