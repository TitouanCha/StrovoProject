package com.example.strovo.domain.model

import com.example.strovo.data.model.FetchActivity
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm
import java.time.LocalDate
import kotlin.math.max

data class ActivityDetailModel (
    val id: String,
    val name: String,
    val distance: Double,
    val time: String,
    val avgSpeed: String,
    val avgSpeedDouble: Double,
    val maxSpeed: String,
    val maxSpeedDouble: Double,
    val elevationGain: Double,
    val date: LocalDate,
    val type: String,
){
    companion object {
        fun fromApi(apiModel: FetchActivity): ActivityDetailModel {
            val distanceKm = (apiModel.distance / 1000.0 * 100.0).toInt() / 100.0
            val time = secondsToHms(apiModel.movingTime)
            val avgPace = speedToPaceMinPerKm(apiModel.averageSpeed)
            val maxPace = speedToPaceMinPerKm(apiModel.averageSpeed)
            return ActivityDetailModel(
                id = apiModel.id.toString(),
                name = apiModel.name,
                distance = distanceKm,
                time = time,
                avgSpeed = avgPace,
                avgSpeedDouble = apiModel.averageSpeed,
                maxSpeed = maxPace,
                maxSpeedDouble = apiModel.maxSpeed,
                elevationGain = apiModel.totalElevationGain,
                date = LocalDate.parse(apiModel.startDateLocal.substring(0, 10)),
                type = apiModel.type
            )
        }
    }
}