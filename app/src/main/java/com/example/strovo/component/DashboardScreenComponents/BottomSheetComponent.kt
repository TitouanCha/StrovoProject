package com.example.strovo.component.DashboardScreenComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.component.DataActivityDisplay
import com.example.strovo.data.Activity
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.screen.Screen
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm
import com.example.strovo.utils.stravaDateToLocal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetComponent(selectedActivities: getStravaActivitiesModelItem?, onclick: () -> Unit) {

    selectedActivities?.let { activity ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(
                "Activité du ${stravaDateToLocal(activity.start_date_local)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Text(
                "Type d'activitées : ${activity.type}",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                activity.name,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
            )
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