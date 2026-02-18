package com.example.strovo.component.monthlyActivitiesScreenComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.component.DataActivityDisplay
import com.example.strovo.component.MonthAverageStatsDisplay
import com.example.strovo.data.AverageMonthStatsModel
import com.example.strovo.data.MonthlyDistanceItem
import com.example.strovo.screen.getMonthAverageStats
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm
import com.example.strovo.utils.stravaDateToLocal
import com.example.strovo.viewModel.ProgressViewModel

@Composable
fun MonthlyActivitiesContent(
    navController: NavController,
    selectedYear: Int,
    month: String,
    activities: MonthlyDistanceItem?,
    averageStats: AverageMonthStatsModel?,
    lastYearAverageStats: AverageMonthStatsModel?
){

    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Activités de $month $selectedYear",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        AverageStatsDisplayCard(averageStats, lastYearAverageStats)
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
            val activities = activities?.activities
            activities?.forEach { activity ->
                item {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        onClick = {
                            navController.navigate("activity_details/${activity.id}")
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            var fontSize = 18.sp
                            if(activity.name.length > 25){
                                fontSize = 16.sp
                            } else if(activity.name.length > 20){
                                fontSize = 18.sp
                            }
                            Text(activity.name,
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 2.dp),
                                fontSize = fontSize,
                                lineHeight = fontSize,
                                maxLines = 2,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    stravaDateToLocal(
                                        activity.start_date_local
                                    ),
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
                                fontSize = 14.sp,
                                lineHeight = 12.sp
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
                            ) {
                                DataActivityDisplay(
                                    "Distance",
                                    "${"%.1f".format(activity.distance / 1000)}km",
                                    20
                                )
                                DataActivityDisplay(
                                    "Durée",
                                    secondsToHms(activity.moving_time),
                                    20
                                )
                                DataActivityDisplay(
                                    "Allure",
                                    speedToPaceMinPerKm(activity.average_speed),
                                    20
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
