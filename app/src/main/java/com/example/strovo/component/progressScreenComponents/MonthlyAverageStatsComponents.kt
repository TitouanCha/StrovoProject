package com.example.strovo.component.progressScreenComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.strovo.component.DataActivityDisplay
import com.example.strovo.data.AverageStatsModel

@Composable
fun MonthlyAverageStatsComponents(stats: AverageStatsModel?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            DataActivityDisplay("Distance Total", stats?.distance ?: "")
            DataActivityDisplay("Activités totale", stats?.activities ?: "")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            DataActivityDisplay("Moyenne hebdomadaire", stats?.weekly_average ?: "")
            DataActivityDisplay("Moyenne Mensuelle", stats?.monthly_average ?: "")
        }
    }
}