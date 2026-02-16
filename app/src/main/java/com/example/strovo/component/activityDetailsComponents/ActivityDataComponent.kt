package com.example.strovo.component.activityDetailsComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.data.ActivityDetailModel
import com.example.strovo.data.Lap

@Composable
fun ActivityData(activityDetail: ActivityDetailModel, lapOnClick: (Int) -> Unit) {
    val headerTitle = listOf<String>("Statistic", "Laps", "Graphs")
    val selectedHeaderIndex = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .width(40.dp)
                    .clip(RoundedCornerShape(5.dp)),
                thickness = 10.dp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            for (i in headerTitle.indices) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                        .clickable(onClick = {
                            selectedHeaderIndex.value = i
                        }),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = headerTitle[i],
                        fontSize = 16.sp,
                        modifier = Modifier
                    )
                    if (selectedHeaderIndex.value == i) {
                        HorizontalDivider(
                            modifier = Modifier.padding(end = 8.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        when (selectedHeaderIndex.value) {
            0 -> ActivityStats(activityDetail)
            1 -> ActivityLap(activityDetail.laps){ index ->
                lapOnClick(index)
            }
            2 -> ActivityGraphs(activityDetail)
        }
    }
}

