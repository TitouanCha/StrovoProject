package com.example.strovo.component.DashboardScreenComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.component.DataActivityDisplay
import com.example.strovo.data.Activity
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.screen.Screen
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm
import com.example.strovo.utils.stravaDateToLocal
import com.patrykandpatrick.vico.core.cartesian.marker.ColumnCartesianLayerMarkerTarget.Column


@Composable
fun ColumnScope.LastActivityCard(activity: getStravaActivitiesModelItem, onclick: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .weight(7f),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onclick()
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter =
                            if(activity.name.contains("Finisher", ignoreCase = true))
                                painterResource(id = R.drawable.trophy_cup_silhouette_svgrepo_com)
                            else
                                painterResource(id = R.drawable.running_man_icon),
                        contentDescription = "Activities Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp),
                        tint =
                            if(activity.name.contains("Finisher", ignoreCase = true))
                                MaterialTheme.colorScheme.tertiary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Column(
                        modifier = Modifier
                    ) {
                        Text(
                            text = "Dernieres Activités",
                            modifier = Modifier,
                            fontSize = 16.sp,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = "Course du ${
                                stravaDateToLocal(
                                    activity.start_date_local
                                )
                            }",
                            modifier = Modifier,
                            fontSize = 10.sp,
                            lineHeight = 12.sp
                        )
                    }

                }
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
                        secondsToHms(activity.moving_time)
                    )
                    DataActivityDisplay(
                        "Allure",
                        speedToPaceMinPerKm(activity.average_speed)
                    )
                }
            }
        }
    }
}