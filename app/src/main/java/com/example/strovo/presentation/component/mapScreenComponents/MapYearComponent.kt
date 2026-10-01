package com.example.strovo.presentation.component.mapScreenComponents

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.strovo.data.utils.mapUtils.mapGeoSource
import com.example.strovo.data.utils.mapUtils.mapPointSources
import com.example.strovo.data.utils.mapUtils.mapPointStyle
import com.example.strovo.data.utils.mapUtils.mapTraceLayer
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.maps.MapView
import org.maplibre.geojson.Point


fun loadActivityOnMap(style: Style, trackPoints: List<List<Pair<Double, Double>>>) {
    style.layers.filter { it.id.startsWith("activity-") }
        .forEach { style.removeLayer(it.id) }
    style.sources.filter { it.id.startsWith("activity-") }
        .forEach { style.removeSource(it.id) }

    val allStartPoint = trackPoints.map {
        val latLng = it.map { p -> LatLng(p.first, p.second) }
        Point.fromLngLat(latLng.first().longitude, latLng.first().latitude)
    }
    style.addSource(mapPointSources(allStartPoint, "activity-points"))
    style.addLayer(
        mapPointStyle("activity-points", 4f, 1f, 0xFFFF9800.toInt(), 0xFF1976D2.toInt())
            .apply { maxZoom = 9.5f }
    )

    trackPoints.forEachIndexed { index, points ->
        val latLngPoints = points.map { LatLng(it.first, it.second) }
        style.addSource(mapGeoSource(latLngPoints, "activity-$index"))
        style.addLayer(
            mapTraceLayer(0xFF199ED2.toInt(), 2f, "activity-$index").apply { minZoom = 9f }
        )
    }
}

@Composable
fun MapYearComponent(trackPoints: List<List<Pair<Double, Double>>>, context: Context) {
    val isDisposing = remember { mutableStateOf(false) }
    val mapView = remember { MapView(context) }
    val mapRef = remember { mutableStateOf<MapLibreMap?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current


    LaunchedEffect(trackPoints, isDisposing.value) {
        if (isDisposing.value) return@LaunchedEffect

        mapRef.value?.getStyle { style ->
            if (!style.isFullyLoaded) return@getStyle
            if (trackPoints.isNotEmpty()) {
                loadActivityOnMap(style, trackPoints)
            } else {
                style.layers.filter { it.id.startsWith("activity-") }
                    .forEach { style.removeLayer(it.id) }
                style.sources.filter { it.id.startsWith("activity-") }
                    .forEach { style.removeSource(it.id) }
            }
        }
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            mapView.apply {
                onCreate(null)
                onStart()
                onResume()
                getMapAsync { map ->
                    mapRef.value = map
                    map.uiSettings.isRotateGesturesEnabled = false
                    map.uiSettings.isTiltGesturesEnabled = false
                    map.setStyle("https://tiles.openfreemap.org/styles/bright") { style ->
                        map.easeCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                LatLngBounds.from(50.0, 7.0, 43.0, -4.0),
                                100, 100, 100, 100
                            ),
                            500
                        )
                        // Ne plus appeler loadActivityOnMap ici,
                        // le LaunchedEffect s'en charge une fois le style prêt.
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