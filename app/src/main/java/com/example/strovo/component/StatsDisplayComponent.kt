package com.example.strovo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R


@Composable
fun RowScope.DataActivityDisplay(title: String, data: String, fontSize: Int = 20) {
    val fontSizeTitle = (fontSize-10)
    Column(
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = title,
            modifier = Modifier,
            fontSize = fontSizeTitle.sp,
            lineHeight = (fontSizeTitle + 2).sp
        )
        Text(
            text = data,
            modifier = Modifier,
            fontSize = fontSize.sp,
            lineHeight = (fontSize + 2).sp,
        )
    }
}

@Composable
fun DataOverallStatsDisplay(title: String, data: String){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = title
        )
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = ":"
        )
        Text(
            modifier = Modifier.weight(2f),
            text = data
        )
    }
}

@Composable
fun MonthAverageStatsDisplay(title: String, data: String, dif: Int){
    Column(
        modifier = Modifier
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            lineHeight = 12.sp,
        )
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = data,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(3f)
                    .padding(4.dp)
            )
            Row( modifier = Modifier.weight(1f) ){
                if(dif != 0) {
                    Icon(
                        painter =
                            if (dif < 0) painterResource(id = R.drawable.baseline_trending_down_24)
                            else painterResource(id = R.drawable.baseline_trending_up_24),
                        contentDescription = "Trending Up",
                        modifier = Modifier.size(30.dp),
                        tint =
                            if (dif < 0) Color.Red
                            else Color.Green
                    )
                    Text(
                        text = if (dif > 0) "+$dif" else "$dif",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically),
                        color =
                            if (dif < 0) Color.Red
                            else Color.Green
                    )
                }
            }
        }
    }
}