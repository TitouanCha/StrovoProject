package com.example.strovo.component.activityDetailsComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.strovo.data.ActivityDetailModel

@Composable
fun ActivityGraphs(
    activity: ActivityDetailModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Graphs Section - Coming Soon!")
    }
}