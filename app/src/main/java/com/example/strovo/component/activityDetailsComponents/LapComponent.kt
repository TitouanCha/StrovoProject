package com.example.strovo.component.activityDetailsComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.data.Lap
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm

@Composable
fun ActivityLap(
    activityLaps: List<Lap>,
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
                            onClick(lap.lap_index)
                        }
                ) {
                    Row() {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 2.dp
                        )
                        Text(
                            text = "Lap ${lap.lap_index}",
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
                            text = speedToPaceMinPerKm(lap.average_speed),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = secondsToHms(lap.elapsed_time),
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