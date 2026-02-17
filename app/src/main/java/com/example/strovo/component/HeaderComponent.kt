package com.example.strovo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.screen.Screen

@Composable
fun HeaderComponent(title: String, icon: Int, onclick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(modifier = Modifier.weight(1f)) {}
        Text(
            modifier = Modifier
                .weight(4f)
                .padding(8.dp),
            textAlign = TextAlign.Center,
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            onClick = {
                onclick()
            }) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Settings Icon",
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}