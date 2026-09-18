package com.example.strovo.component.dashboardScreenComponents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.component.DataOverallStatsDisplay
import com.example.strovo.domain.model.HealthDataModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.text.format
import kotlin.text.toInt
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun ColumnScope.AthleteStatsComponent(athleteHealth: HealthDataModel, modifier: Modifier) {
    val heartRateVariabilityModelProducer = remember { CartesianChartModelProducer() }
    val stepsModelProducer = remember { CartesianChartModelProducer() }
    val sleepTimeModelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(athleteHealth) {
        val heartRateVariability = athleteHealth.heartRateVariability
        heartRateVariabilityModelProducer.runTransaction {
            lineSeries { series(heartRateVariability) }
        }
        val steps = athleteHealth.steps
        stepsModelProducer.runTransaction {
            columnSeries { series(steps) }
        }
        val sleepTime = athleteHealth.sleepTime
        sleepTimeModelProducer.runTransaction {
            columnSeries { series(sleepTime) }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .padding(8.dp),
                        painter = painterResource(R.drawable.progress_svgrepo_com),
                        contentDescription = "Stats Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Stats des dernières semaines",
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Row() {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        DataOverallStatsDisplay(
                            "Forme physique",
                            "%.2f".format(athleteHealth.form),
                        )
                        DataOverallStatsDisplay(
                            "Score sommeil",
                            "${"%.0f".format(athleteHealth.sleepScore)}/100"
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        DataOverallStatsDisplay(
                            "Bpm au repos",
                            "${athleteHealth.restingHeartRate} bpm"
                        )
                        DataOverallStatsDisplay(
                            "Vo2 Max",
                            "%.1f".format(athleteHealth.vo2Max)
                        )
                    }
                }
                DataChartComponent(
                    data = athleteHealth.steps,
                    modelProducer = stepsModelProducer,
                    title = "Nombres de pas"
                )
                DataChartComponent(
                    data = athleteHealth.sleepTime,
                    modelProducer = sleepTimeModelProducer,
                    title = "Temps de sommeil"
                )
                DataChartComponent(
                    data = athleteHealth.heartRateVariability,
                    modelProducer = heartRateVariabilityModelProducer,
                    title = "VFC"
                )
            }
        }
    }
}