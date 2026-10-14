package com.example.strovo.presentation.component.activityDetailsComponents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.strovo.presentation.component.DataChartComponent
import com.example.strovo.domain.model.ActivityMetaData
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries

@SuppressLint("DefaultLocale")
@Composable
fun ActivityGraphs(
    activityMetaData: ActivityMetaData
) {
    val heartRateVariabilityModelProducer = remember { CartesianChartModelProducer() }
    val altitudeModelProducer = remember { CartesianChartModelProducer() }
    val altitudeTempsModelProducer = remember { CartesianChartModelProducer() }
    val speedModelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        val heartRateVariability = activityMetaData.heartratePerMin
        heartRateVariabilityModelProducer.runTransaction {
            lineSeries { series(heartRateVariability) }
        }
        val altitude = activityMetaData.altitudePerKm
        altitudeModelProducer.runTransaction {
            lineSeries { series(altitude) }
        }
        val speed = activityMetaData.velocityPerKm
        speedModelProducer.runTransaction {
            lineSeries { series(speed) }
        }
        val altitudeTemps = activityMetaData.altitudePerKm
        altitudeTempsModelProducer.runTransaction {
            lineSeries { series(altitudeTemps) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        DataChartComponent(
            data = activityMetaData.velocityPerKm,
            modelProducer = speedModelProducer,
            title = "Vitesse",
            bottomDataLabel = activityMetaData.distanceMarkers.map { "${String.format("%.1f", it / 1000.0)} km" },
            isZoomEnabled = true,
            isPace = true
        )
        DataChartComponent(
            data = activityMetaData.heartratePerMin,
            modelProducer = heartRateVariabilityModelProducer,
            title = "Frequence cardiaque",
            bottomDataLabel = activityMetaData.heartrateList.mapIndexed { index, _ -> "${index}m" },
            isZoomEnabled = true,
        )
        DataChartComponent(
            data = activityMetaData.altitudePerKm,
            modelProducer = altitudeModelProducer,
            title = "Altitude/Distance",
            bottomDataLabel = activityMetaData.distanceMarkers.map { "${it / 1000.0} km" },
            isZoomEnabled = true,
        )
    }
}