package com.example.strovo.component.activityDetailsComponents

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.strovo.utils.mapUtils.mapGeoSource
import com.example.strovo.utils.mapUtils.mapPointSources
import com.example.strovo.utils.mapUtils.mapPointStyle
import com.example.strovo.utils.mapUtils.mapTraceLayer
import com.example.strovo.utils.mapUtils.mapZoomPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.geojson.Point

@Composable
fun MapComponent(
    context: Context,
    trackPoints: List<Pair<Double, Double>>,
    lapPoints: List<Point>,
    selectedLapIndex: Int?
) {
    val isDisposing = remember { mutableStateOf(false) }
    val mapView = remember { MapView(context) }
    val mapRef = remember { mutableStateOf<MapLibreMap?>(null) }

    LaunchedEffect(selectedLapIndex, isDisposing.value) {
        if (isDisposing.value) return@LaunchedEffect

        mapRef.value?.let { map ->
            map.style?.let { style ->
                style.getLayer("selected-lap-layer")?.let { style.removeLayer(it) }
                style.getSource("selected-lap-source")?.let { style.removeSource(it) }

                if (selectedLapIndex != null && selectedLapIndex >= 0 && selectedLapIndex < lapPoints.size) {
                    val lapPoint = lapPoints[selectedLapIndex]
                    val lapLatLng = LatLng(lapPoint.latitude(), lapPoint.longitude())

                    style.addSource(mapPointSources(listOf(lapPoint), "selected-lap"))
                    style.addLayer(mapPointStyle("selected-lap", 10f, 2f, 0xFFFFFF00.toInt(), 0xFFFF9800.toInt()))

                    map.easeCamera(
                        CameraUpdateFactory.newLatLngZoom(lapLatLng, 13.0),
                        500
                    )
                } else {
                    val points = trackPoints.map { LatLng(it.first, it.second) }
                    map.easeCamera(
                        CameraUpdateFactory.newLatLngBounds(mapZoomPosition(points), 100, 100, 100, 100),
                        500
                    )
                }
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = {
            mapView.apply {
                getMapAsync { map ->
                    mapRef.value = map
                    map.setStyle("https://tiles.openfreemap.org/styles/bright") {
                        if (trackPoints.isNotEmpty()) {
                            val points = trackPoints.map { LatLng(it.first, it.second) }
                            val start = Point.fromLngLat(points.first().longitude, points.first().latitude)
                            val finish = Point.fromLngLat(points.last().longitude, points.last().latitude)

                            map.style?.addSource(mapGeoSource(points))
                            map.style?.addLayer(mapTraceLayer(0xFF199ED2.toInt(), 5f))

                            map.style?.addSource(mapPointSources(lapPoints, "lap"))
                            map.style?.addLayer(mapPointStyle("lap", 4f, 1f, 0xFFFF9800.toInt(), 0xFFFFFFFF.toInt()))

                            map.style?.addSource(mapPointSources(listOf(start), "start"))
                            map.style?.addLayer(mapPointStyle("start", 8f, 2f, 0xFF00FF00.toInt(), 0xFF199ED2.toInt()))

                            map.style?.addSource(mapPointSources(listOf(finish), "finish"))
                            map.style?.addLayer(mapPointStyle("finish", 8f, 2f, 0xFFFF0000.toInt(), 0xFF199ED2.toInt()))

                            map.easeCamera(
                                CameraUpdateFactory.newLatLngBounds(mapZoomPosition(points), 100, 100, 100, 100),
                                500
                            )
                        }
                    }
                }
            }
        },
        onRelease = { view ->
            isDisposing.value = true
            view.onPause()
            view.onStop()
            view.onDestroy()
        }
    )
}