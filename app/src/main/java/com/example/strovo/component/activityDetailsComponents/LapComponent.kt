package com.example.strovo.component.activityDetailsComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.model.strava.Lap
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm
import com.example.strovo.domain.model.ActivityLapModel

@Composable
fun ActivityLap(
    activityLaps: List<ActivityLapModel>,
    onClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        activityLaps.forEach { lap ->
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onClick(lap.lapNumber)
                        }
                ) {
                    Row() {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 2.dp
                        )
                        Text(
                            text = "Lap ${lap.lapNumber}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 2.dp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding()
                    ){
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "${lap.distance} m",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = lap.avgSpeed,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = lap.time,
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        thickness = 2.dp
                    )
                }
            }
        }
    }
}