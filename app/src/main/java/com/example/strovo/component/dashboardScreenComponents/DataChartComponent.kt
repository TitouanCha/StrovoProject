package com.example.strovo.component.dashboardScreenComponents

import androidx.appcompat.widget.DialogTitle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.strovo.domain.model.HealthDataModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget
import java.time.format.DateTimeFormatter

@Composable
fun DataChartComponent(
    data: List<Any>,
    modelProducer: CartesianChartModelProducer,
    title: String
) {
    val today = java.time.LocalDate.now()
    val last7Days = (5 downTo 0).map { daysAgo ->
        today.minusDays(daysAgo.toLong())
            .format(DateTimeFormatter.ofPattern("dd/MM"))
    }
    val minY = remember(data) {
        data.minOfOrNull { (it as? Number)?.toDouble() ?: Double.MAX_VALUE } ?: 0.0
    }
    val maxY = remember(data) {
        data.maxOfOrNull { (it as? Number)?.toDouble() ?: Double.MIN_VALUE } ?: 0.0
    }
    Column(
        horizontalAlignment = Alignment.Start,
    ){
        Spacer(modifier = Modifier.size(16.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 2.dp
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(title,
            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
        )
        CartesianChartHost(
            rememberCartesianChart(
                rememberColumnCartesianLayer(),
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.Line(
                            fill = LineCartesianLayer.LineFill.single(
                                fill(MaterialTheme.colorScheme.secondary)
                            )
                        )
                    ),
                    rangeProvider = remember(minY, maxY) {
                        CartesianLayerRangeProvider.fixed(
                            minY = minY,
                            maxY = maxY
                        )
                    }
                ),
                startAxis = VerticalAxis.rememberStart(
                    valueFormatter = { _, value, _ ->
                        value.toInt().toString()
                    },
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = { _, x, _ ->
                        last7Days.getOrNull(x.toInt()) ?: " "
                    }
                )
            ),
            modelProducer = modelProducer,
            zoomState = rememberVicoZoomState(
                initialZoom = Zoom.Content,
                zoomEnabled = false
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp, bottom = 12.dp)
                .height(150.dp)
        )
    }
}