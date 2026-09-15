package com.example.strovo.domain.model

import com.example.strovo.data.model.GetActivityMetaData

data class ActivityMetaData(
    val id: String,
    val time: Double,
    val avgSpeed: Double,
    val maxSpeed: Double,
    val velocity: List<Double>,
    val heartrate: Int,
    val heartrateList: List<Int>,
    val distance: Double,
    val distanceList: List<Double>,
    val altitude: Double,
    val altitudeList: List<Double>,
    val latLng: List<Double>,
    val stepLength: List<Int>
) {
    companion object {

        private fun findByType(list: List<GetActivityMetaData>, type: String): List<Any?> {
            return list.firstOrNull { it.type == type }?.data.orEmpty()
        }

        private fun toDoubleList(data: List<Any?>): List<Double> =
            data.mapNotNull { (it as? Number)?.toDouble() }

        private fun toIntList(data: List<Any?>): List<Int> =
            data.mapNotNull { (it as? Number)?.toInt() }

        fun fromApi(id: String, apiModel: List<GetActivityMetaData>): ActivityMetaData {

            val timeList = toDoubleList(findByType(apiModel, "time"))
            val velocityList = toDoubleList(findByType(apiModel, "velocity_smooth"))
            val heartrateList = toIntList(findByType(apiModel, "heartrate"))
            val distanceList = toDoubleList(findByType(apiModel, "distance"))
            val altitudeList = toDoubleList(findByType(apiModel, "altitude"))
            val latLngList = toDoubleList(findByType(apiModel, "latlng"))
            val stepLengthList = toIntList(findByType(apiModel, "step_length"))

            return ActivityMetaData(
                id = id,
                time = timeList.lastOrNull() ?: 0.0,
                avgSpeed = velocityList.takeIf { it.isNotEmpty() }?.average() ?: 0.0,
                maxSpeed = velocityList.maxOrNull() ?: 0.0,
                velocity = velocityList,
                heartrate = heartrateList.takeIf { it.isNotEmpty() }
                    ?.let { it.sum() / it.size } ?: 0,
                heartrateList = heartrateList,
                distance = distanceList.lastOrNull() ?: 0.0,
                distanceList = distanceList,
                altitude = altitudeList.maxOrNull() ?: 0.0,
                altitudeList = altitudeList,
                latLng = latLngList,
                stepLength = stepLengthList
            )
        }
    }
}

