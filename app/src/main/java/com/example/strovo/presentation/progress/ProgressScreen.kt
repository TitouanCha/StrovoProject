package com.example.strovo.presentation.progress

import com.example.strovo.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.component.HeaderComponent
import com.example.strovo.component.progressScreenComponents.MonthlyAverageStatsComponents
import com.example.strovo.component.progressScreenComponents.MonthlyDistanceListComponent
import com.example.strovo.component.progressScreenComponents.YearSelectionComponent
import com.example.strovo.domain.model.ProgressModel
import com.example.strovo.util.PointerInputUtils
import com.example.strovo.screen.Screen
import com.example.strovo.viewModel.StravaViewModel
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
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import java.time.LocalDate


@Composable
fun ProgressScreen(navController: NavController, progressViewModel: ProgressViewModel) {
    val months = listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
        "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc")

    val pointerUtils = PointerInputUtils()
    var refreshScrollState = remember { mutableStateOf(false) }
    val activitiesModelProducer = remember { CartesianChartModelProducer() }

    val progressUiState = progressViewModel.progressUiState.collectAsState().value
    val selectedYear = progressViewModel.selectedYear.collectAsState().value

    LaunchedEffect(Unit) {
        progressViewModel.loadProgressData(LocalDate.now().year)
    }
    LaunchedEffect(progressUiState) {
        if (progressUiState is ProgressUiState.Success) {
            val currentYearData = progressUiState.progressData.selectedYearDistances
            val lastYearData = progressUiState.progressData.lastYearDistances
            activitiesModelProducer.runTransaction {
                columnSeries { series(currentYearData.map { it.distance }) }
                lineSeries { series(lastYearData.map { it.distance }) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ){
            HeaderComponent(
                "Progrès", R.drawable.map_location_pin_svgrepo_com,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal =  16.dp),
            ) {
                navController.navigate(Screen.Map.route)
            }
        }
        if (refreshScrollState.value) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        Column(
            modifier = Modifier
                .weight(9f)
                .run {
                    with(pointerUtils) {
                        verticalDragToRefresh(
                            refreshScrollState = refreshScrollState,
                        ) {
                            progressViewModel.loadProgressData(selectedYear)
                        }
                    }
                },
        ) {
            when (progressUiState) {
                is ProgressUiState.Loading -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    ) {
                        YearSelectionComponent(
                            incrementYear = {
                                progressViewModel.incrementYear()
                                progressViewModel.loadProgressData(selectedYear + 1)
                            },
                            decrementYear = {
                                progressViewModel.decrementYear()
                                progressViewModel.loadProgressData(selectedYear - 1)
                            },
                            selectedYear = selectedYear
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is ProgressUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Erreur lors du chargement des activités",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Verrifier votre connexion a Strava.",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                is ProgressUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    ) {
                        YearSelectionComponent(
                            incrementYear = {
                                progressViewModel.incrementYear()
                                progressViewModel.loadProgressData(selectedYear + 1)
                            },
                            decrementYear = {
                                progressViewModel.decrementYear()
                                progressViewModel.loadProgressData(selectedYear - 1)
                            },
                            selectedYear = selectedYear
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
                                    )
                                ),
                                startAxis = VerticalAxis.rememberStart(
                                ),
                                bottomAxis = HorizontalAxis.rememberBottom(
                                    valueFormatter = { x, chartValues, _ ->
                                        months.getOrNull(chartValues.toInt()) ?: ""
                                    }
                                ),
                            ),
                            modelProducer = activitiesModelProducer,
                            zoomState = rememberVicoZoomState(
                                initialZoom = Zoom.Content,
                                zoomEnabled = true
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 16.dp, bottom = 12.dp)
                        )
                        MonthlyAverageStatsComponents(progressUiState.progressData.averageStats)
                    }
                    MonthlyDistanceListComponent(
                        progressUiState.progressData.selectedYearDistances,
                        onMonthClick = { monthIndex ->
                            navController.navigate(Screen.MonthlyActivities.createRoute(monthIndex))
                        }
                    )
                }
            }
        }
    }
}