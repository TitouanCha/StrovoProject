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
    val kmPoint: List<Point>,
    val velocityPerKm: List<Double>,
    val heartratePerKm: List<Double>,
    val altitudePerKm: List<Double>,
    val distanceMarkers: List<Int>,
    val velocityPerMin: List<Double>,
    val heartratePerMin: List<Double>,
    val altitudePerMin: List<Double>,
    val timeMarkers: List<Int>
) {
    companion object {

        private const val SEGMENT_METERS = 100.0
        private const val SEGMENT_SECONDS = 60.0

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

        private fun computePerKmValues(
            distanceList: List<Double>,
            valueList: List<Double>
        ): List<Double> {
            if (distanceList.isEmpty() || valueList.isEmpty()) return emptyList()

            val maxDistance = distanceList.last()
            val totalSegments = (maxDistance / SEGMENT_METERS).toInt() + 1
            val result = mutableListOf<Double>()

            var currentSegment = 0
            val buffer = mutableListOf<Double>()

            for (index in distanceList.indices) {
                val distance = distanceList[index]
                val value = valueList.getOrNull(index) ?: continue
                val segmentIndex = (distance / SEGMENT_METERS).toInt()
                if (segmentIndex > currentSegment) {
                    result.add(if (buffer.isNotEmpty()) buffer.average() else 0.0)
                    buffer.clear()
                    currentSegment = segmentIndex
                }

                buffer.add(value)
            }

            if (buffer.isNotEmpty()) {
                result.add(buffer.average())
            }

            while (result.size < totalSegments) {
                result.add(0.0)
            }

            return result
        }

        private fun computeDistanceMarkers(size: Int): List<Int> {
            return List(size) { index -> ((index + 1) * SEGMENT_METERS).toInt() }
        }

        private fun computePerTimeValues(
            timeList: List<Double>,
            valueList: List<Double>
        ): List<Double> {
            if (timeList.isEmpty() || valueList.isEmpty()) return emptyList()

            val maxTime = timeList.last()
            val totalSegments = (maxTime / SEGMENT_SECONDS).toInt() + 1
            val result = mutableListOf<Double>()

            var currentSegment = 0
            val buffer = mutableListOf<Double>()

            for (index in timeList.indices) {
                val time = timeList[index]
                val value = valueList.getOrNull(index) ?: continue
                val segmentIndex = (time / SEGMENT_SECONDS).toInt()
                if (segmentIndex > currentSegment) {
                    result.add(if (buffer.isNotEmpty()) buffer.average() else 0.0)
                    buffer.clear()
                    currentSegment = segmentIndex
                }

                buffer.add(value)
            }

            if (buffer.isNotEmpty()) {
                result.add(buffer.average())
            }

            while (result.size < totalSegments) {
                result.add(0.0)
            }

            return result
        }

        private fun computeTimeMarkers(size: Int): List<Int> {
            return List(size) { index -> ((index + 1) * SEGMENT_SECONDS).toInt() }
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

            val velocityPerKm = computePerKmValues(distanceList, velocityList)
            val heartratePerKm = computePerKmValues(distanceList, heartrateList.map { it.toDouble() })
            val altitudePerKm = computePerKmValues(distanceList, altitudeList)
            val distanceMarkers = computeDistanceMarkers(velocityPerKm.size)

            val velocityPer30Sec = computePerTimeValues(timeList, velocityList)
            val heartratePer30Sec = computePerTimeValues(timeList, heartrateList.map { it.toDouble() })
            val altitudePer30Sec = computePerTimeValues(timeList, altitudeList)
            val timeMarkers = computeTimeMarkers(velocityPer30Sec.size)

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
                kmPoint = computeKmPoints(alignedTrackPoints, distanceList, altitudeList),
                velocityPerKm = velocityPerKm,
                heartratePerKm = heartratePerKm,
                altitudePerKm = altitudePerKm,
                distanceMarkers = distanceMarkers,
                velocityPerMin = velocityPer30Sec,
                heartratePerMin = heartratePer30Sec,
                altitudePerMin = altitudePer30Sec,
                timeMarkers = timeMarkers
            )
        }
    }
}
