package com.example.strovo.presentation.component.progressScreenComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
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
import com.example.strovo.model.strava.AverageStatsModel
import com.example.strovo.model.strava.MonthlyDistanceModel
import java.util.Calendar

@Composable
fun MonthlyDistanceListComponent(
    modifier: Modifier,
    averageStats: AverageStatsModel,
    monthlyDistancesList: MutableList<MonthlyDistanceModel>,
    selectedYear: Int,
    onMonthClick: (Int) -> Unit
) {
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
        actualMonthNumber += 0
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.running_svg),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Données annuelles",
                    modifier = Modifier,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.titleMedium
                    //lineHeight = 18.sp
                )
            }
            MonthlyAverageStatsComponents(averageStats)
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                thickness = 2.dp
            )
            for (i in 0..<actualMonthNumber step 2) {
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
                    if (actualMonthNumber % 2 == 0 || i + 1 < actualMonthNumber) {
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