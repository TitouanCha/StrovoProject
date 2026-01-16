package com.example.strovo.screen

import android.annotation.SuppressLint
import com.example.strovo.R
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strovo.data.AverageStatsModel
import com.example.strovo.data.ChartDistanceModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.utils.DataFormattingUtils
import com.example.strovo.utils.PointerInputUtils
import com.example.strovo.viewmodel.StravaViewModel
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
import kotlin.collections.addAll
import kotlin.collections.filter
import kotlin.text.toFloat
import kotlin.text.toInt


@Composable
fun RowScope.AverageStatsDisplay(title: String, data: String){
    Column(
        modifier = Modifier
            .weight(1f)
    ){
        Text(
            text = title,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
        Text(
            text = data,
            fontSize = 22.sp,
            lineHeight = 25.sp
        )
    }
}

fun getYearActivities(selectedYear: Int, viewModel: StravaViewModel, context: Context){
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
        context = context,
    )
}

fun getAverageStats(activities: GetStravaActivitiesModel?, selectedYear: Int): AverageStatsModel?{
    if(activities == null){
        return null
    }
    var distance = activities.sumOf { it.distance}
    var monthlyAverage: Double
    var weeklyAverage: Double
    if(selectedYear != LocalDate.now().year){
        monthlyAverage = distance / 12
        weeklyAverage = distance / 52
    }else{
        val currentMonth = LocalDate.now().monthValue
        monthlyAverage = distance / currentMonth
        weeklyAverage = distance / (LocalDate.now().dayOfYear / 7)
    }
    return AverageStatsModel(
        activities = activities.size.toString(),
        distance = "%.2f km".format(distance / 1000),
        monthly_average = "%.2f km".format(monthlyAverage / 1000),
        weekly_average = "%.2f km".format(weeklyAverage / 1000)
    )
}

fun parseActivitiesForChart(activities: GetStravaActivitiesModel?, selectedYear: Int): MutableList<ChartDistanceModel> {
    val monthlyDistances = MutableList(12) { ChartDistanceModel(0, null) } //mutableListOf<ChartDistanceModel>( ChartDistanceModel(0, null) )

    if (activities == null) {
        return monthlyDistances
    }
    for (i in 1..12) {
        val monthActivities = activities.filter { activity ->
            val activityDate = LocalDate.parse(activity.start_date_local.substring(0, 10))
            activityDate.year == selectedYear && activityDate.monthValue == i
        }
        monthlyDistances[i-1].distance = (monthActivities.sumOf { it.distance } / 1000).toInt()
        monthlyDistances[i-1].activities = GetStravaActivitiesModel().apply {
            addAll(monthActivities)
        }
    }
    Log.e("ProgressScreen", "Monthly Distances: $monthlyDistances")
    return monthlyDistances
}

@Composable
fun ProgressScreen(navController: NavController, viewModel: StravaViewModel = viewModel()) {
    val pointerUtils = PointerInputUtils()
    val activitiesModelProducer = remember { CartesianChartModelProducer() }
    val context = LocalContext.current
    val isInitialized = viewModel.isInitialized.collectAsState()

    val activities = viewModel.yearActivities.collectAsState()
    val lastYearActivities = viewModel.lastYearActivities.collectAsState()

    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    var averageStats = remember { mutableStateOf<AverageStatsModel?>(null) }
    var monthlyDistances = remember { mutableStateOf(mutableListOf<ChartDistanceModel>()) }

    var selectedYear = remember { mutableIntStateOf(LocalDate.now().year) }
    var refreshScrollState = remember { mutableStateOf(false) }

    LaunchedEffect(isInitialized.value) {
        if(isInitialized.value && activities.value == null){
            getYearActivities(selectedYear.intValue, viewModel, context)
        }
    }
    LaunchedEffect(lastYearActivities.value) {
        averageStats.value = getAverageStats(activities.value, selectedYear.intValue)
        monthlyDistances.value = parseActivitiesForChart(activities.value, selectedYear.intValue)
    }
    LaunchedEffect(monthlyDistances.value) {
        val lastYearParsedActivities = parseActivitiesForChart(lastYearActivities.value, selectedYear.intValue-1)
        Log.e("ProgressScreen", "Last Year Parsed Activities: $lastYearParsedActivities")
        activitiesModelProducer.runTransaction {
            columnSeries { series(monthlyDistances.value.map{it.distance})}
            lineSeries { series(lastYearParsedActivities.map{it.distance}) }

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
                            selectedYear.intValue = LocalDate.now().year
                            getYearActivities(LocalDate.now().year, viewModel, context)
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
                            selectedYear.intValue -= 1
                            getYearActivities(selectedYear.intValue, viewModel, context)
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
                    text = "Donnée de ${selectedYear.intValue}",
                    modifier = Modifier
                        .weight(3f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                if(selectedYear.intValue != LocalDate.now().year){
                    IconButton(
                        onClick = {
                            if(!isLoading.value){
                                selectedYear.intValue += 1
                                getYearActivities(selectedYear.intValue, viewModel, context)
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
                errorMessage.value != null && activities.value == null -> {
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            AverageStatsDisplay("Distance Total", averageStats.value?.distance ?: "")
                            AverageStatsDisplay("Activités totale", averageStats.value?.activities ?: "")
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            AverageStatsDisplay("Moyenne hebdomadaire", averageStats.value?.weekly_average ?: "")
                            AverageStatsDisplay("Moyenne Mensuelle", averageStats.value?.monthly_average ?: "")
                        }
                    }
                }
            }
        }
        when{
            isLoading.value -> {
            }
            monthlyDistances.value.isEmpty() -> {
                Text("Erreur lors du chargement des activités. Veuillez vérifier votre connexion à Strava.")
            }
            !monthlyDistances.value.isEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ){
                    monthlyDistances.value.forEachIndexed { index, monthData ->
                        val monthName = listOf(
                            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
                        )[index]
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
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