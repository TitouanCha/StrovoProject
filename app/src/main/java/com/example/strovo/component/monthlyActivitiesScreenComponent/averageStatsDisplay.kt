package com.example.strovo.component.monthlyActivitiesScreenComponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.component.MonthAverageStatsDisplay
import com.example.strovo.data.AverageMonthStatsModel

@Composable
fun AverageStatsDisplayCard(averageStats: AverageMonthStatsModel?, lastYearAverageStats: AverageMonthStatsModel?){
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        val stats = averageStats
        val oldStats = lastYearAverageStats
        if (stats != null) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp)
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
                MonthAverageStatsDisplay("Distance totale", "%.2f km".format(stats.distance), distanceDif)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                MonthAverageStatsDisplay("Moyenne hebdomadaire", "%.2f km".format(stats.weekly_average), weeklyDif)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                MonthAverageStatsDisplay("Nombres d'activités", "${stats.activities}", activitiesDif)
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