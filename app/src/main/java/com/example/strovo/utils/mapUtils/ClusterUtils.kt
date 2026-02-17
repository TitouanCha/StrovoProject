package com.example.strovo.utils.mapUtils


import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonOptions
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.android.style.expressions.Expression.get

fun mapClusterPoint(source: String, allStartPoint: List<Point>): GeoJsonSource {
    return GeoJsonSource(
        source,
        FeatureCollection.fromFeatures(
            allStartPoint.mapIndexed { index, point ->
                Feature.fromGeometry(point)
            }
        ),
        GeoJsonOptions()
            .withCluster(true)
            .withClusterRadius(50)
            .withClusterMaxZoom(10)
    )
}

fun mapClusterStyle(source: String, clusterSource: String, radius: Float, color: Int, strokeColor: Int): CircleLayer {
    return CircleLayer(source, clusterSource).withProperties(
        PropertyFactory.circleRadius(radius),
        PropertyFactory.circleColor(color),
        PropertyFactory.circleStrokeWidth(1f),
        PropertyFactory.circleStrokeColor(strokeColor)
    ).withFilter(
        Expression.has("point_count")
    )
}

fun mapClusterSymbol(source: String, clusterSource: String, color: Int): SymbolLayer {
    return SymbolLayer("$source-count", clusterSource).withProperties(
        PropertyFactory.textField(get("point_count")),
        PropertyFactory.textSize(12f),
        PropertyFactory.textColor(color),
    ).withFilter(
        Expression.has("point_count")
    )
}

fun mapUnClusterStyle(source: String, clusterSource: String, radius: Float, color: Int, strokeColor: Int): CircleLayer {
    return CircleLayer("$source-point", clusterSource).withProperties(
        PropertyFactory.circleRadius(radius),
        PropertyFactory.circleColor(color),
        PropertyFactory.circleStrokeWidth(1f),
        PropertyFactory.circleStrokeColor(strokeColor)
    ).withFilter(
        Expression.not(Expression.has("point_count"))
    )
}