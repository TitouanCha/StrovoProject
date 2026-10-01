package com.example.strovo.domain.model

import com.example.strovo.data.model.GetActivityMetaData

data class ActivityTrackPoint(
    val activityId: String,
    val trackPoint: List<Pair<Double, Double>>
){
    companion object {

        private fun toNullableDoubleList(data: List<Any?>): List<Double?> =
            data.map { (it as? Number)?.toDouble() }

        fun fromApi(activityMetaData: List<GetActivityMetaData>, activityId: String): ActivityTrackPoint {
            val entry = activityMetaData.firstOrNull { it.type == "latlng" }

            val latitudes = entry?.data.orEmpty().let { toNullableDoubleList(it) }
            val longitudes = entry?.data2.orEmpty().let { toNullableDoubleList(it) }

            val trackPoint = latitudes.indices.mapNotNull { index ->
                val lat = latitudes.getOrNull(index)
                val lng = longitudes.getOrNull(index)
                if (lat != null && lng != null) Pair(lat, lng) else null
            }

            return ActivityTrackPoint(
                activityId = activityId,
                trackPoint = trackPoint
            )
        }
    }
}