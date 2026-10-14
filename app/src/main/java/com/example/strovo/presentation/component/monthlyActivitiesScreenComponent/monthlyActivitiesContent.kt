package com.example.strovo.presentation.component.monthlyActivitiesScreenComponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.presentation.component.DataActivityDisplay
import com.example.strovo.model.strava.AverageMonthStatsModel
import com.example.strovo.model.strava.MonthlyDistanceModel
import java.time.format.DateTimeFormatter

@Composable
fun MonthlyActivitiesContent(
    onActivityClick: (activityId: String) -> Unit,
    activities: MonthlyDistanceModel?,
    averageStats: AverageMonthStatsModel?,
    lastYearAverageStats: AverageMonthStatsModel?
){

    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        AverageStatsDisplayCard(averageStats, lastYearAverageStats)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            thickness = 2.dp
        )
        Text(
            text = "Liste des activitées",
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            fontSize = 22.sp,
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
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        onClick = {
                            onActivityClick(activity.id)
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            var fontSize = 18.sp
                            if(activity.name.length > 25){
                                fontSize = 14.sp
                            } else if(activity.name.length > 20){
                                fontSize = 14.sp
                            }
                            Row(
                                modifier = Modifier.padding(end = 16.dp, bottom = 4.dp, start = 8.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    activity.name,
                                    modifier = Modifier,
                                    fontSize = fontSize,
                                    lineHeight = fontSize,
                                    maxLines = 2,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = activity.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    modifier = Modifier,
                                    fontSize = 14.sp,
                                    lineHeight = 12.sp
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
                            ) {
                                DataActivityDisplay(
                                    "Distance",
                                    "${"%.1f".format(activity.distance)}km",
                                    20
                                )
                                DataActivityDisplay(
                                    "Durée",
                                    activity.time,
                                    20
                                )
                                DataActivityDisplay(
                                    "Allure",
                                    activity.avgSpeed,
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
