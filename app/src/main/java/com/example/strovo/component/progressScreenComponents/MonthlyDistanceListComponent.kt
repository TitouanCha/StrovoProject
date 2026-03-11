package com.example.strovo.component.progressScreenComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.strovo.model.MonthlyDistanceModel

@Composable
fun MonthlyDistanceListComponent(monthlyDistancesList: MutableList<MonthlyDistanceModel>, onMonthClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ){
        val monthName = listOf(
            "Jan", "Fev", "Mar", "Avr", "Mai", "Juin",
            "Jul", "Août", "Sept", "Oct", "Nov", "Dec"
        )
        for(i in 0..11 step 2){
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                        .fillMaxWidth()
                ) {
                    DistanceCardDisplayComponent(
                        title = monthName[i],
                        data = "${monthlyDistancesList[i].distance}",
                        index = i,
                        onClick = { onMonthClick(i) }
                    )
                    DistanceCardDisplayComponent(
                        title = monthName[i + 1],
                        data = "${monthlyDistancesList[i+1].distance}",
                        index = i + 1,
                        onClick = { onMonthClick(i + 1) }
                    )
                }
            }
        }
    }

}