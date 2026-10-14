package com.example.strovo.presentation.component.activityDetailsComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.strovo.presentation.component.DataActivityDisplay
import com.example.strovo.model.strava.StravaActivityDetailModel
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm
import com.example.strovo.data.utils.stravaDateToLocal
import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.domain.model.ActivityMetaData
import java.time.format.DateTimeFormatter

@Composable
fun ActivityStats(
    activity: ActivityDetailModel,
    metaData: ActivityMetaData
) {
    var dataFontSize = 22
    var titleFontSize = 22.sp
    if(activity.name.length > 30){
        titleFontSize = 18.sp
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.running_svg),
                contentDescription = "Activity Date Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Activitées du ${
                        activity.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    }",
                    fontSize = 18.sp
                )

                Text(
                    text = activity.name,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold
                )
            }
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
                DataActivityDisplay(
                    "Temps", activity.time, dataFontSize, modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp),
                    ).padding(12.dp)
                )
                Spacer(modifier = Modifier.weight(0.1f))
                DataActivityDisplay(
                    "Distance", "%.2f km".format(activity.distance), dataFontSize, modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp),
                    ).padding(12.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                DataActivityDisplay(
                    "Vitesse",
                    if(activity.type == "Run" || activity.type == "TrailRun") activity.avgSpeed
                    else "%.1f km/h".format(activity.avgSpeedDouble * 3.6),
                    dataFontSize,
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp),
                    ).padding(12.dp)
                )
                Spacer(modifier = Modifier.weight(0.1f))
                DataActivityDisplay(
                    "Vitesse max",
                    if(activity.type == "Run" || activity.type == "TrailRun") activity.maxSpeed
                    else "%.1f km/h".format(activity.maxSpeedDouble * 3.6),
                    dataFontSize,
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp),
                    ).padding(12.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                DataActivityDisplay("Dénivelé", "${activity.elevationGain} d+", dataFontSize,
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp),
                    ).padding(12.dp)
                )
                Spacer(modifier = Modifier.weight(0.1f))
                DataActivityDisplay("Bpm moyen", "${metaData.heartrate} bpm", dataFontSize,
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp),
                    ).padding(12.dp)
                )
            }
        }
    }
}