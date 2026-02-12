package com.example.strovo.component.ProgressScreenComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.DistanceCardDisplayComponent( title: String, data: String, index: Int, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
        onClick = {
            onClick(index)
        }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.weight(2f),
                text = title,
                fontSize = 20.sp,
                lineHeight = 20.sp,
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ){
                Text(
                    modifier = Modifier,
                    text = data,
                    fontSize = 15.sp,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.End,
                )
                Text(
                    modifier = Modifier,
                    text = "km",
                    fontSize = 8.sp,
                    lineHeight = 8.sp,
                    //textAlign = TextAlign.End,
                )
            }

        }
    }
}
