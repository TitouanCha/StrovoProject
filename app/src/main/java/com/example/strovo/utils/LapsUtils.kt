package com.example.strovo.utils


import com.example.strovo.data.Lap
import org.maplibre.android.geometry.LatLng
import org.maplibre.geojson.Point


fun getPointsForLaps(laps: List<Lap>, trackPoints: List<Pair<Double, Double>>): List<Point> {
    val result = mutableListOf<Point>()
    val trackPointsLatLngs = trackPoints.map { LatLng(it.first, it.second) }
    laps.forEach { lap ->
        val lapPoints = trackPoints.subList(
            lap.start_index.coerceIn(0, trackPoints.size),
            (lap.end_index + 1).coerceIn(0, trackPoints.size)
        )

        if (lapPoints.isEmpty()) return@forEach

        val lapDistance = calculateLapDistance(trackPointsLatLngs)
        val interval = if (lapDistance < 1000) {
            lapDistance / 2
        } else {
            1000.0
        }

        result.addAll(getPointsWithInterval(trackPointsLatLngs, interval))
    }

    return result
}

fun calculateLapDistance(points: List<LatLng>): Double {
    var totalDistance = 0.0
    for (i in 0 until points.size - 1) {
        totalDistance += points[i].distanceTo(points[i + 1])
    }
    return totalDistance
}

fun getPointsWithInterval(points: List<LatLng>, interval: Double): List<Point> {
    val result = mutableListOf<Point>()
    var accumulatedDistance = 0.0

    result.add(Point.fromLngLat(points.first().longitude, points.first().latitude))

    for (i in 0 until points.size - 1) {
        val current = points[i]
        val next = points[i + 1]
        val segmentDistance = current.distanceTo(next)

        if (accumulatedDistance + segmentDistance >= interval) {
            result.add(Point.fromLngLat(next.longitude, next.latitude))
            accumulatedDistance = 0.0
        } else {
            accumulatedDistance += segmentDistance
        }
    }

    return result
}

fun LatLng.distanceTo(other: LatLng): Double {
    val earthRadius = 6371000.0
    val dLat = Math.toRadians(other.latitude - this.latitude)
    val dLng = Math.toRadians(other.longitude - this.longitude)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return earthRadius * c
}