package com.example.strovo.component.progressScreenComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.DistanceCardDisplayComponent( title: String, activityNumber: String, distance: String, index: Int, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
        onClick = {
            onClick(index)
        }
    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .fillMaxWidth()
                .height(70.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    //lineHeight = 20.sp,
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = distance,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 4.dp)
                    //lineHeight = 15.sp,
                )
                Text(
                    text = "km",
                    fontSize = 18.sp,
                    //lineHeight = 15.sp,
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = activityNumber,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 4.dp)
                    //lineHeight = 15.sp,
                )
                Text(
                    text = "Seances",
                    fontSize = 14.sp,
                    //lineHeight = 15.sp,
                )
            }
        }
    }
}
