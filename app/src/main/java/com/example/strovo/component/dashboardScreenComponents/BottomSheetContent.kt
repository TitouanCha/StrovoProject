package com.example.strovo.component.dashboardScreenComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.component.DataActivityDisplay
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm
import com.example.strovo.utils.stravaDateToLocal

@Composable
fun BottomSheetContent(selectedActivities: List<getStravaActivitiesModelItem>?, onclick: () -> Unit) {

    selectedActivities?.get(0)?.let { activity ->
        Column(
            verticalArrangement = Arrangement.Top
        ){
            var fontSize = 25.sp
            if(activity.name.length > 30){
                fontSize = 18.sp
            }
            Text(activity.name,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 4.dp),
                fontSize = fontSize,
                lineHeight = fontSize,
                maxLines = 2,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    "Activité du ${stravaDateToLocal(activity.start_date_local)}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding()
                )
                Text(
                    "Type d'activitées : ${activity.type}",
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
            ) {
                DataActivityDisplay(
                    "Durée",
                    secondsToHms(activity.moving_time)
                )
                if (activity.type == "Run") {
                    DataActivityDisplay(
                        "Distance",
                        "${"%.1f".format(activity.distance / 1000)}km"
                    )
                    DataActivityDisplay(
                        "Allure",
                        speedToPaceMinPerKm(activity.average_speed)
                    )
                }
            }
            //Spacer(modifier = Modifier.weight(1f))
            if(activity.type == "Run") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        onClick = {
                            onclick()
                        }
                    ) {
                        Text(
                            "Detail de l'activité",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}