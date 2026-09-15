package com.example.strovo.domain.model

import com.example.strovo.data.model.GetActivityMetaData
import org.maplibre.geojson.Point

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
    val stepLength: List<Int>,
    val trackPoints: List<Pair<Double, Double>>,
    val kmPoint: List<Point>
) {
    companion object {

        private fun findByType(list: List<GetActivityMetaData>, type: String): List<Any?> {
            return list.firstOrNull { it.type == type }?.data.orEmpty()
        }

        private fun findEntryByType(list: List<GetActivityMetaData>, type: String): GetActivityMetaData? {
            return list.firstOrNull { it.type == type }
        }

        private fun toDoubleList(data: List<Any?>): List<Double> =
            data.mapNotNull { (it as? Number)?.toDouble() }

        private fun toIntList(data: List<Any?>): List<Int> =
            data.mapNotNull { (it as? Number)?.toInt() }

        private fun toNullableDoubleList(data: List<Any?>): List<Double?> =
            data.map { (it as? Number)?.toDouble() }

        private fun toAlignedLatLngPairs(list: List<GetActivityMetaData>): List<Pair<Double, Double>?> {
            val entry = findEntryByType(list, "latlng") ?: return emptyList()
            val latitudes = toNullableDoubleList(entry.data)
            val longitudes = toNullableDoubleList(entry.data2.orEmpty())
            return latitudes.indices.map { index ->
                val lat = latitudes.getOrNull(index)
                val lng = longitudes.getOrNull(index)
                if (lat != null && lng != null) Pair(lat, lng) else null
            }
        }

        private fun computeKmPoints(
            alignedTrackPoints: List<Pair<Double, Double>?>,
            distanceList: List<Double>,
            altitudeList: List<Double>
        ): List<Point> {
            val points = mutableListOf<Point>()
            var nextKmThreshold = 1000.0
            for (index in distanceList.indices) {
                if (distanceList[index] >= nextKmThreshold) {
                    val latLng = alignedTrackPoints.getOrNull(index)
                    val altitudeAtIndex = altitudeList.getOrNull(index) ?: 0.0
                    if (latLng != null) {
                        points.add(
                            Point.fromLngLat(latLng.second, latLng.first, altitudeAtIndex)
                        )
                    }
                    nextKmThreshold += 1000.0
                }
            }
            return points
        }

        fun fromApi(id: String, apiModel: List<GetActivityMetaData>): ActivityMetaData {

            val timeList = toDoubleList(findByType(apiModel, "time"))
            val velocityList = toDoubleList(findByType(apiModel, "velocity_smooth"))
            val heartrateList = toIntList(findByType(apiModel, "heartrate"))
            val distanceList = toDoubleList(findByType(apiModel, "distance"))
            val altitudeList = toDoubleList(findByType(apiModel, "altitude"))
            val stepLengthList = toIntList(findByType(apiModel, "step_length"))

            val alignedTrackPoints = toAlignedLatLngPairs(apiModel)
            val trackPoints = alignedTrackPoints.filterNotNull()

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
                latLng = trackPoints.flatMap { listOf(it.first, it.second) },
                stepLength = stepLengthList,
                trackPoints = trackPoints,
                kmPoint = computeKmPoints(alignedTrackPoints, distanceList, altitudeList)
            )
        }
    }
}
