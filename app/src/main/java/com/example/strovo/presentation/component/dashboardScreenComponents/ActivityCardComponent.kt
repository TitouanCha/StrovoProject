package com.example.strovo.presentation.component.dashboardScreenComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.presentation.component.DataActivityDisplay
import com.example.strovo.data.model.strava.GetStravaActivitiesModelItem
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm
import com.example.strovo.data.utils.stravaDateToLocal
import com.example.strovo.domain.model.ActivityDetailModel
import java.time.format.DateTimeFormatter


@Composable
fun ColumnScope.LastActivityCard(
    activity: ActivityDetailModel?,
    modifier: Modifier,
    onclick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable {
                    onclick()
                }
        ) {
            when (activity) {
                null -> {
                    Text(
                        text = "Aucune activité ne correspond à la discipline sélectionnée ce mois-ci",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            //.padding(8.dp)
                    ) {
                        Row(

                        ) {
                            Text(
                                text = "Dernieres Activités",
                                modifier = Modifier,
                                fontSize = 22.sp,
                                style = MaterialTheme.typography.titleMedium
                                //lineHeight = 18.sp
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.open_outline_svgrepo_com),
                                contentDescription = "Arrow Right Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 8.dp, top = 4.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        OutlinedCard(){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter =
                                        if (activity.name.contains("Finisher", ignoreCase = true))
                                            painterResource(id = R.drawable.trophy_cup_silhouette_svgrepo_com)
                                        else
                                            painterResource(id = R.drawable.running_svg),
                                    contentDescription = "Activities Icon",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(end = 8.dp),
                                    tint =
                                        if (activity.name.contains("Finisher", ignoreCase = true))
                                            MaterialTheme.colorScheme.tertiary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Column {
                                    Text(
                                        text = "Course du ${
                                            activity.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        }",
                                        modifier = Modifier,
                                        fontSize = 16.sp,
                                        lineHeight = 16.sp
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp, top = 8.dp),
                                    ) {
                                        DataActivityDisplay(
                                            "Distance",
                                            "${"%.1f".format(activity.distance)}km"
                                        )
                                        DataActivityDisplay(
                                            "Durée",
                                            activity.time
                                        )
                                        DataActivityDisplay(
                                            "Allure",
                                            activity.avgSpeed
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
}