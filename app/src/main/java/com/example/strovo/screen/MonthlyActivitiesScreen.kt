package com.example.strovo.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.strovo.data.AverageStatsModel
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.viewmodel.StravaViewModel

fun getAverageStats(activities: GetStravaActivitiesModel?): AverageStatsModel? {
    if(activities == null){
        return null
    }

    val distance = activities.sumOf { it.distance }
    val weeklyAverage = distance / 7
    return AverageStatsModel(
        activities = activities.size.toString(),
        distance = "%.2f km".format(distance / 1000),
        monthly_average = null,
        weekly_average = "%.2f km".format(weeklyAverage / 1000)
    )
}

@Composable
fun MonthlyActivitiesScreen(navController: NavController, viewModel: StravaViewModel = viewModel(), monthIndex: Int) {
    val monthName = listOf(
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    )[monthIndex]

    val activities = viewModel.yearActivities.collectAsState()
    val averageStats = getAverageStats(activities.value)

    Column {
        Card(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Activités de $monthName",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (averageStats != null) {
                        Column {
                            Text(
                                text = "Nombre d'activités : ${averageStats.activities}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(
                                text = "Distance totale : ${averageStats.distance}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(
                                text = "Moyenne hebdomadaire : ${averageStats.weekly_average}",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "Aucune donnée disponible pour ce mois.",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

        }
    }

}