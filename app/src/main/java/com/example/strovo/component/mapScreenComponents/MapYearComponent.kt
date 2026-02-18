package com.example.strovo.component.mapScreenComponents

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.utils.mapUtils.decodePolyline
import com.example.strovo.utils.mapUtils.mapGeoSource
import com.example.strovo.utils.mapUtils.mapPointSources
import com.example.strovo.utils.mapUtils.mapPointStyle
import com.example.strovo.utils.mapUtils.mapTraceLayer
import com.example.strovo.viewModel.ProgressViewModel
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.maps.MapView
import org.maplibre.geojson.Point
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver


fun loadActivityOnMap(mapRef: MapLibreMap? ,trackPoints: List<List<Pair<Double, Double>>>): Style? {
    val style: Style? = mapRef?.style
    val allStartPoint = trackPoints.map{
        val latLng = it.map { LatLng(it.first, it.second) }
        Point.fromLngLat(latLng.first().longitude, latLng.first().latitude)
    }
    style?.addSource(mapPointSources(allStartPoint, "activity-points"))
    style?.addLayer(mapPointStyle("activity-points", 4f, 1f, 0xFFFF9800.toInt(), 0xFF1976D2.toInt()).apply{
        maxZoom = 9.5f
    })

    trackPoints.forEachIndexed { index, points ->
        val latLngPoints = points.map { LatLng(it.first, it.second) }
        style?.addSource(mapGeoSource(latLngPoints, "activity-$index"))
        style?.addLayer(mapTraceLayer(0xFF199ED2.toInt(), 2f, "activity-$index").apply {
            minZoom = 9f
        })
    }
    return style
}

@Composable
fun MapYearComponent(viewModel: ProgressViewModel, context: Context) {
    val isDisposing = remember { mutableStateOf(false) }
    val trackPoints = viewModel.trackPoints.collectAsState()

    val mapView = remember { MapView(context) }
    val mapRef = remember { mutableStateOf<MapLibreMap?>(null) }


    LaunchedEffect(trackPoints.value, isDisposing.value) {
        if (isDisposing.value) return@LaunchedEffect

        Log.d("MapDebug", "Track points updated")
        mapRef.value?.let{ map ->
            map.style?.let{ style ->
                style.layers.filter { it.id.startsWith("activity-") }
                    .forEach { style.removeLayer(it.id) }
                style.sources.filter { it.id.startsWith("activity-") }
                    .forEach { style.removeSource(it.id) }

                if(trackPoints.value.isNotEmpty()){
                    loadActivityOnMap(mapRef.value, trackPoints.value)
                }
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            mapView.apply{
                getMapAsync { map ->
                    mapRef.value = map
                    map.setStyle("https://tiles.openfreemap.org/styles/bright"){ style ->
                        if(trackPoints.value.isNotEmpty()){
                            loadActivityOnMap(mapRef.value, trackPoints.value)
                        }
                        map.easeCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                LatLngBounds.from(50.0, 7.0, 43.0, -4.0),
                                100, 100, 100, 100
                            ),
                            500
                        )
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