package com.example.strovo.screen

import com.example.strovo.R
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.component.ProgressScreenComponents.MonthlyAverageStatsComponents
import com.example.strovo.utils.PointerInputUtils
import com.example.strovo.viewModel.ProgressViewModel
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
import java.time.ZoneId


fun getYearActivities(selectedYear: Int, viewModel: ProgressViewModel, context: Context){
    val firstDayOfYear = LocalDate.of(selectedYear, 1, 1)
    val lastDayOfYear = LocalDate.of(selectedYear, 12, 31)
    val afterDate = firstDayOfYear
        .atStartOfDay(ZoneId.systemDefault())
        .toEpochSecond().toString()
    val beforeDate = lastDayOfYear
        .atStartOfDay(ZoneId.systemDefault())
        .toEpochSecond().toString()
    viewModel.getYearActivities(
        after = afterDate,
        before = beforeDate,
    )
}

@Composable
fun ProgressScreen(navController: NavController, stravaViewModel: StravaViewModel, progressViewModel: ProgressViewModel) {

    val pointerUtils = PointerInputUtils()
    val activitiesModelProducer = remember { CartesianChartModelProducer() }
    val context = LocalContext.current
    val isInitialized = stravaViewModel.isInitialized.collectAsState()

    val activities = progressViewModel.yearActivities.collectAsState()

    val isLoading = progressViewModel.isLoading.collectAsState()
    val errorMessage = progressViewModel.errorMessage.collectAsState()

    val monthlyDistances = progressViewModel.monthlyDistances.collectAsState()
    val averageStats = progressViewModel.averageStats.collectAsState()


    val selectedYear = progressViewModel.selectedYear.collectAsState()
    var refreshScrollState = remember { mutableStateOf(false) }


    LaunchedEffect(selectedYear.value) {
        if(isInitialized.value){
            if(activities.value?.year != selectedYear.value){
                getYearActivities(selectedYear.value, progressViewModel, context)
            }
        }
    }
    LaunchedEffect(monthlyDistances.value) {
        activitiesModelProducer.runTransaction {
            val currentYearData = monthlyDistances.value.selectedYear
            val lastYearData = monthlyDistances.value.lastYear

            if (currentYearData != null) {
                columnSeries { series(currentYearData.map { it.distance }) }
            }
            if (lastYearData != null) {
                lineSeries { series(lastYearData.map { it.distance }) }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (refreshScrollState.value) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .run {
                    with(pointerUtils) {
                        verticalDragToRefresh(
                            refreshScrollState = refreshScrollState,
                            isInitialized = isInitialized.value
                        ) {
                            progressViewModel.setYear(LocalDate.now().year)
                        }
                    }
                },
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if(!isLoading.value) {
                            progressViewModel.decrementYear()
                        }
                    },
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                        contentDescription = "Previous Year",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "Donnée de ${selectedYear.value}",
                    modifier = Modifier
                        .weight(3f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                if(selectedYear.value != LocalDate.now().year){
                    IconButton(
                        onClick = {
                            if(!isLoading.value){
                                progressViewModel.incrementYear()
                            }
                        },
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                            contentDescription = "Previous Year",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }else {
                    Box(modifier = Modifier.weight(1f)) {}
                }
            }
            when{
                isLoading.value -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage.value != null || activities.value == null -> {
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
                activities.value != null -> {
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
                                    val months = listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
                                        "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc")
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
                            .padding(start = 8.dp, end = 16.dp, bottom = 16.dp)
                    )
                    MonthlyAverageStatsComponents(averageStats.value)
                }
            }
        }
        when{
            isLoading.value -> {
            }
            monthlyDistances.value.selectedYear != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ){
                    var currentYearData = monthlyDistances.value.selectedYear
                    currentYearData?.forEachIndexed { index, monthData ->
                        val monthName = listOf(
                            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
                        )[index]
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                onClick = {navController.navigate(Screen.MonthlyActivities.createRoute(index))}
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ){
                                    Text(
                                        modifier = Modifier.weight(2f),
                                        text = monthName,
                                    )
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "${monthData.distance} km",
                                        textAlign = TextAlign.End,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}