package com.example.strovo.presentation.component.monthlyActivitiesScreenComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.presentation.component.MonthAverageStatsDisplay
import com.example.strovo.model.strava.AverageMonthStatsModel

@Composable
fun AverageStatsDisplayCard(averageStats: AverageMonthStatsModel?, lastYearAverageStats: AverageMonthStatsModel?){
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 0.dp)
            .fillMaxWidth()
    ) {
        val stats = averageStats
        val oldStats = lastYearAverageStats
        if (stats != null) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .fillMaxWidth()
            ){
                val distanceDif = if (oldStats != null) {
                    (stats.distance - oldStats.distance).toInt()
                } else 0
                val weeklyDif = if (oldStats != null) {
                    (stats.weekly_average - oldStats.weekly_average).toInt()
                } else 0
                val activitiesDif = if (oldStats != null) {
                    stats.activities - oldStats.activities
                } else 0
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.running_svg),
                        contentDescription = "Trending Up",
                        modifier = Modifier.size(45.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Column{
                        Text(
                            "Distance parcourue",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                "%.1f".format(stats.distance),
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                " km",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Row(modifier = Modifier.padding(vertical = 8.dp)){
                    MonthAverageStatsDisplay("Moyenne hebdomadaire", "%.1f km".format(stats.weekly_average), weeklyDif)
                    Spacer(modifier = Modifier.weight(0.1f))
                    MonthAverageStatsDisplay("Nombres d'activités", "${stats.activities}", activitiesDif)
                }
            }
        } else {
            Text(
                text = "Aucune donnée disponible pour ce mois.",
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}