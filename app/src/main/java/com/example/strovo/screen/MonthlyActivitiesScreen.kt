package com.example.strovo.screen



import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strovo.R
import com.example.strovo.data.AverageMonthStatsModel
import com.example.strovo.data.AverageStatsModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.utils.DataFormattingUtils
import com.example.strovo.viewmodel.StravaViewModel
import com.patrykandpatrick.vico.compose.common.fill
import kotlin.text.toInt

fun getMonthAverageStats(activities: GetStravaActivitiesModel?): AverageMonthStatsModel? {
    if(activities == null){
        return null
    }
    val distance = activities.sumOf { it.distance }
    val weeklyAverage = distance / 7
    return AverageMonthStatsModel(
        activities = activities.size,
        distance = distance / 1000,
        weekly_average = weeklyAverage / 1000
    )
}

@Composable
fun MonthAverageStatsDisplay(title: String, data: String, dif: Int){
    Column(
        modifier = Modifier
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            lineHeight = 12.sp,
        )
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = data,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(3f)
                    .padding(4.dp)
            )
            Row( modifier = Modifier.weight(1f) ){
                if(dif != 0) {
                    Icon(
                        painter =
                            if (dif < 0) painterResource(id = R.drawable.baseline_trending_down_24)
                            else painterResource(id = R.drawable.baseline_trending_up_24),
                        contentDescription = "Trending Up",
                        modifier = Modifier.size(30.dp),
                        tint =
                            if (dif < 0) Color.Red
                            else Color.Green
                    )
                    Text(
                        text = if (dif > 0) "+$dif" else "$dif",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically),
                        color =
                            if (dif < 0) Color.Red
                            else Color.Green
                    )
                }
            }
        }
    }
}

@Composable
fun MonthlyActivitiesScreen(navController: NavController, viewModel: StravaViewModel = viewModel(), monthIndex: Int) {
    val pagerState = rememberPagerState(
        initialPage = monthIndex,
        pageCount = { 12 }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        MonthlyActivitiesContent(
            navController = navController,
            viewModel = viewModel,
            monthIndex = page
        )
    }
}

@Composable
private fun MonthlyActivitiesContent(navController: NavController, viewModel: StravaViewModel, monthIndex: Int){
    val monthName = listOf(
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    )[monthIndex]
    val dataFormatting = DataFormattingUtils()

    val allMonthlyActivities = viewModel.monthlyDistances.collectAsState()
    val selectedYear = viewModel.selectedYear.collectAsState()
    var monthlyActivities = remember { mutableStateOf<MonthlyDistanceItem?>(null) }
    var lastYearMonthlyActivities = remember { mutableStateOf<MonthlyDistanceItem?>(null) }
    val averageStats = remember { mutableStateOf<AverageMonthStatsModel?>(null) }
    val lastYearAverageStats = remember { mutableStateOf<AverageMonthStatsModel?>(null) }

    LaunchedEffect(allMonthlyActivities.value, monthIndex, selectedYear.value) {
        monthlyActivities.value = allMonthlyActivities.value.selectedYear?.get(monthIndex)
        lastYearMonthlyActivities.value = allMonthlyActivities.value.lastYear?.get(monthIndex)
        averageStats.value = getMonthAverageStats(monthlyActivities.value?.activities)
        lastYearAverageStats.value = getMonthAverageStats(lastYearMonthlyActivities.value?.activities)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Activités de $monthName ${selectedYear.value}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

        }
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            val stats = averageStats.value
            val oldStats = lastYearAverageStats.value
            if (stats != null) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                        .fillMaxWidth()
                ){
                    val distanceDif = if (oldStats != null) {
                        (stats.distance - oldStats.distance).toInt()
                    } else 0
                    val weeklyDif = if (oldStats != null) {
                        (stats.weekly_average - oldStats.weekly_average).toInt()
                    } else 0
                    val activitiesDif = if (oldStats != null) {
                        stats.activities - oldStats.activities
                    } else 0
                    MonthAverageStatsDisplay("Distance totale", "%.2f km".format(stats.distance), distanceDif)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    MonthAverageStatsDisplay("Moyenne hebdomadaire", "%.2f km".format(stats.weekly_average), weeklyDif)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    MonthAverageStatsDisplay("Nombres d'activités", "${stats.activities}", activitiesDif)
                }
            } else {
                Text(
                    text = "Aucune donnée disponible pour ce mois.",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Text(
            text = "Liste des activitées : ",
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 18.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val activities = monthlyActivities.value?.activities
            activities?.forEach { activity ->
                item {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            var fontSize = 20.sp
                            if(activity.name.length > 30){
                                fontSize = 16.sp
                            } else if(activity.name.length > 20){
                                fontSize = 20.sp
                            }
                            Text(activity.name,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                                fontSize = fontSize,
                                lineHeight = fontSize,
                                maxLines = 2,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            ) {
                                DataActivityDisplay(
                                    "Distance",
                                    "${"%.1f".format(activity.distance / 1000)}km"
                                )
                                DataActivityDisplay(
                                    "Durée",
                                    dataFormatting.secondsToHms(activity.moving_time)
                                )
                                DataActivityDisplay(
                                    "Allure",
                                    dataFormatting.speedToPaceMinPerKm(activity.average_speed)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}