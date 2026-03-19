package com.example.strovo.data.utils


import com.example.strovo.model.Lap
import org.maplibre.android.geometry.LatLng
import org.maplibre.geojson.Point


fun getPointsForKm(distance: Int, trackPoints: List<Pair<Double, Double>>): List<Point> {
    val result = mutableListOf<Point>()
    val trackPointsLatLng = trackPoints.map { LatLng(it.first, it.second) }

    var startIndex = 0
    for(i in 0..<distance.toInt()){
        val (newPoint, newIndex) = getPointWithIntervalFromIndex(startIndex, trackPointsLatLng, 1000.0)
        if(newPoint != null){
            result.add(newPoint)
            startIndex = newIndex
        } else {
            break
        }
    }
    return result
}

fun getPointsForLaps(laps: List<Lap>, trackPoints: List<Pair<Double, Double>>): List<Point> {
    val result = mutableListOf<Point>()
    val trackPointsLatLng = trackPoints.map { LatLng(it.first, it.second) }

    var startIndex = 0
    laps.forEach { lap ->
        val (newPoint, newIndex) = getPointWithIntervalFromIndex(startIndex, trackPointsLatLng, lap.distance)
        if (newPoint != null) {
            result.add(newPoint)
            startIndex = newIndex
        } else {
            return@forEach
        }
    }
    return result
}

fun getPointWithIntervalFromIndex(startIndex: Int, trace: List<LatLng>, interval: Double): Pair<Point?, Int> {
    var accumulatedDistance = 0.0

    for (i in startIndex until trace.size - 1) {
        val current = trace[i]
        val next = trace[i + 1]
        val segmentDistance = current.distanceTo(next)

        if (accumulatedDistance + segmentDistance >= interval) {
            val ratio = (interval - accumulatedDistance) / segmentDistance
            val lat = current.latitude + ratio * (next.latitude - current.latitude)
            val lng = current.longitude + ratio * (next.longitude - current.longitude)
            return Pair(Point.fromLngLat(lng, lat), i)
        }

        accumulatedDistance += segmentDistance
    }

    return Pair(null, trace.size - 1)
}