package com.example.strovo.util

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

class PointerInputUtils {

    fun Modifier.verticalDragToRefresh(
        refreshScrollState: MutableState<Boolean>,
        onRefresh: () -> Unit
    ): Modifier = this.pointerInput(Unit) {
        var totalDrag = 0f
        var triggerDistance = 300f
        detectVerticalDragGestures(
            onDragEnd = {
                refreshScrollState.value = false
                if (totalDrag >= triggerDistance) {
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