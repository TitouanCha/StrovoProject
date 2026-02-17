package com.example.strovo.utils.mapUtils

import com.google.maps.android.PolyUtil
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonOptions
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point

fun decodePolyline(encoded: String): List<Pair<Double, Double>> {
    return PolyUtil.decode(encoded).map { latLng ->
        Pair(latLng.latitude, latLng.longitude)
    }
}

fun mapGeoSource(points: List<LatLng>, track: String? = "track"): GeoJsonSource {
    val lineCoordinates = points.map {
        Point.fromLngLat(it.longitude, it.latitude)
    }
    val lineString = LineString.fromLngLats(lineCoordinates)
    return GeoJsonSource("$track-source", lineString)
}

fun mapTraceLayer(color: Int, width: Float, track: String? = "track"): LineLayer {
    return LineLayer("$track-layer", "$track-source").withProperties(
        PropertyFactory.lineColor(color),
        PropertyFactory.lineWidth(width)
    )
}

fun mapPointSources(points: List<Point>, source: String): GeoJsonSource {
    return GeoJsonSource(
        "$source-source",
        FeatureCollection.fromFeatures(
            points.map { Feature.fromGeometry(it) }
        )
    )
}

fun mapPointStyle(source: String, radius: Float, width: Float, color: Int, strokeColor: Int): CircleLayer {
     return CircleLayer("$source-layer", "$source-source").withProperties(
        PropertyFactory.circleRadius(radius),
        PropertyFactory.circleColor(color),
        PropertyFactory.circleStrokeWidth(width),
        PropertyFactory.circleStrokeColor(strokeColor)
    )
}

fun mapZoomPosition(points:  List<LatLng>):  LatLngBounds{
    val boundsBuilder = LatLngBounds.Builder()
    points.forEach { boundsBuilder.include(it) }
    return boundsBuilder.build()
}

