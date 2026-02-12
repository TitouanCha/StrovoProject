package com.example.strovo.component.ProgressScreenComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.strovo.data.MonthlyDistanceModel
import com.example.strovo.screen.Screen
import kotlin.text.get

@Composable
fun MonthlyDistanceListComponent(monthlyDistances: MonthlyDistanceModel, onMonthClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ){
        val monthName = listOf(
            "Jan", "Fev", "Mar", "Avr", "Mai", "Juin",
            "Jul", "Août", "Sept", "Oct", "Nov", "Dec"
        )
        var currentYearData = monthlyDistances.selectedYear
        for(i in 0..11 step 2){
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                        .fillMaxWidth()
                ) {
                    DistanceCardDisplayComponent(
                        title = monthName[i],
                        data = "${currentYearData?.getOrNull(i)?.distance ?: 0}",
                        index = i,
                        onClick = { onMonthClick(i) }
                    )
                    DistanceCardDisplayComponent(
                        title = monthName[i + 1],
                        data = "${currentYearData?.getOrNull(i + 1)?.distance ?: 0}",
                        index = i + 1,
                        onClick = { onMonthClick(i + 1) }
                    )
                }
            }
        }
    }

}