package com.example.strovo.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import android.util.Log
import android.content.Context
import com.example.strovo.screen.getMonthActivities

class PointerInputUtils {

    fun Modifier.verticalDragToRefresh(
        refreshScrollState: MutableState<Boolean>,
        triggerDistance: Float,
        isInitialized: Boolean,
        onRefresh: () -> Unit
    ): Modifier = this.pointerInput(Unit) {
        var totalDrag = 150f
        detectVerticalDragGestures(
            onDragEnd = {
                refreshScrollState.value = false
                if (isInitialized && totalDrag >= triggerDistance) {
                    onRefresh()
                }
                totalDrag = 0f
            },
            onVerticalDrag = { _, dragAmount ->
                totalDrag += dragAmount
                refreshScrollState.value = totalDrag >= triggerDistance
            },
            onDragCancel = {
                refreshScrollState.value = false
                totalDrag = 0f
            }
        )
    }


}