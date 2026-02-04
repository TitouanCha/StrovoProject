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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.component.DataOverallStatsDisplay
import com.example.strovo.data.OverallStats
import com.example.strovo.utils.secondsToHms

@Composable
fun ColumnScope.AthleteStatsComponent(athleteStats: OverallStats?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .weight(7f),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            athleteStats?.all_run_totals?.let { stats ->
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(8.dp),
                            painter = painterResource(R.drawable.progress_svgrepo_com),
                            contentDescription = "Stats Icon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Stats générales",
                            fontSize = 18.sp,
                            lineHeight = 20.sp
                        )
                    }
                    DataOverallStatsDisplay("Activités", stats.count.toString())
                    DataOverallStatsDisplay(
                        "Distance",
                        "${"%.0f".format(stats.distance / 1000)} km"
                    )
                    DataOverallStatsDisplay(
                        "Temps",
                        secondsToHms(stats.moving_time)
                    )
                }
            }
        }
    }
}