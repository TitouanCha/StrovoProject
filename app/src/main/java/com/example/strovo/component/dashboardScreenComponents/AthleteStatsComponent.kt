package com.example.strovo.component.dashboardScreenComponents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.component.DataOverallStatsDisplay
import com.example.strovo.data.model.strava.GetOverallStatsModel
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.domain.model.HealthDataModel
import com.example.strovo.model.strava.Athlete

@Composable
fun ColumnScope.AthleteStatsComponent(athleteHealth: HealthDataModel, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp)
                            .padding(8.dp),
                        painter = painterResource(R.drawable.progress_svgrepo_com),
                        contentDescription = "Stats Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Stats des dernières semaines",
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                Row() {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        DataOverallStatsDisplay(
                            "Forme physique",
                            "%.2f".format(athleteHealth.form),
                        )
                        DataOverallStatsDisplay(
                            "Score sommeil",
                            "${athleteHealth.sleepScore}/100"
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        DataOverallStatsDisplay(
                            "Bpm au repos",
                            "${athleteHealth.restingHeartRate} bpm"
                        )
                        DataOverallStatsDisplay(
                            "Vo2 Max",
                            "${athleteHealth.vo2Max}"
                        )
                    }
                }
            }
        }
    }
}