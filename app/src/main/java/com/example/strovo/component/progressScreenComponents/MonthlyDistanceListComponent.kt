package com.example.strovo.component.progressScreenComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.strovo.model.MonthlyDistanceModel
import java.util.Calendar

@Composable
fun MonthlyDistanceListComponent(modifier: Modifier,monthlyDistancesList: MutableList<MonthlyDistanceModel>, selectedYear: Int, onMonthClick: (Int) -> Unit) {
    LazyColumn(
        modifier = modifier
    ){
        val monthName = listOf(
            "Janvier", "Fevriver", "Mars", "Avril", "Mai", "Juin",
            "Julillet", "Août", "Septpembre", "Octobre", "Novembre", "Decembre"
        )
        var actualMonthNumber = 12
        if(selectedYear == Calendar.getInstance().get(Calendar.YEAR)){
            actualMonthNumber = Calendar.getInstance().get(Calendar.MONTH)+1
        }
        for(i in 0..< actualMonthNumber step 2){
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                        .fillMaxWidth()
                ) {
                    DistanceCardDisplayComponent(
                        title = monthName[i],
                        distance = "${monthlyDistancesList[i].distance}",
                        activityNumber = "${monthlyDistancesList[i].activities.size}",
                        index = i,
                        onClick = { onMonthClick(i) }
                    )
                    if((actualMonthNumber%2 != 0 && i != actualMonthNumber-1) || actualMonthNumber == 12 ) {
                        DistanceCardDisplayComponent(
                            title = monthName[i + 1],
                            activityNumber = "${monthlyDistancesList[i + 1].activities.size}",
                            distance = "${monthlyDistancesList[i + 1].distance}",
                            index = i + 1,
                            onClick = { onMonthClick(i + 1) }
                        )
                    }
                }
            }
        }
    }

}