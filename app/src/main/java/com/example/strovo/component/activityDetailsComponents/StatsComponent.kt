package com.example.strovo.component.activityDetailsComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import com.example.strovo.data.ActivityDetailModel
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm
import com.example.strovo.utils.stravaDateToLocal

@Composable
fun ActivityStats(
    activity: ActivityDetailModel
) {
    var dataFontSize = 22
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.running_man_icon),
                    contentDescription = "Activity Date Icon",
                    modifier = Modifier.size(25.dp).padding(end = 4.dp),
                    tint = MaterialTheme.colorScheme.primary

                )
                Text(
                    modifier = Modifier,
                    text = "Activitées du ${
                        stravaDateToLocal(
                            activity.start_date_local
                        )
                    }",
                    fontSize = 18.sp
                )
            }
            Text(
                text = activity.name,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                DataActivityDisplay("Temps", secondsToHms(activity.moving_time), dataFontSize)
                DataActivityDisplay("Distance", "%.2f km".format(activity.distance / 1000), dataFontSize)
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                thickness = 2.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                DataActivityDisplay("Vitesse", speedToPaceMinPerKm(activity.average_speed), dataFontSize)
                DataActivityDisplay("Vitesse max", speedToPaceMinPerKm(activity.max_speed), dataFontSize)
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                thickness = 2.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                DataActivityDisplay("Dénivelé", "${activity.total_elevation_gain} d+", dataFontSize)
            }
        }
    }
}