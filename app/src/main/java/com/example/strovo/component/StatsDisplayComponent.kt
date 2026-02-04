package com.example.strovo.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


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