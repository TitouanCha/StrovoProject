package com.example.strovo.screen

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.strovo.component.activityDetailsComponents.ActivityData
import com.example.strovo.viewmodel.StravaViewModel
import kotlinx.coroutines.launch


@Composable
fun ActivityDetails(navController: NavController, viewModel: StravaViewModel, activityId: String) {
    val activityDetails = viewModel.activityDetails.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    var mapHeight = remember { Animatable(0.5f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(activityId) {
        viewModel.getActivityDetails(activityId)
    }

    when{
        isLoading.value -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        activityDetails.value != null -> {
            val details = activityDetails.value!!
            Text("Atcivity: ${details.name}")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit){
                        var totalDrag = 0f
                        detectVerticalDragGestures(
                            onDragStart = { totalDrag = 0f },
                            onVerticalDrag = { _, dragAmount ->
                                totalDrag += dragAmount
                                Log.v("DragAmount", "Total Drag: $totalDrag")
                                scope.launch {
                                    when {
                                        totalDrag > 400f -> mapHeight.animateTo(0.8f, tween(300))
                                        totalDrag < -400f -> mapHeight.animateTo(0.1f, tween(300))
                                        else -> mapHeight.animateTo(0.5f, tween(300))
                                    }
                                }
                            }
                        )
                    }
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(mapHeight.value)
                        .background(color = Color.White)
                ) {}
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f - mapHeight.value)
                        .pointerInput(Unit){
                            detectTapGestures(
                                onDoubleTap = {
                                    scope.launch {
                                        when (mapHeight.value) {
                                            0.8f -> mapHeight.animateTo(0.5f, tween(300))
                                            0.5f -> mapHeight.animateTo(0.1f, tween(300))
                                            0.1f -> mapHeight.animateTo(0.5f, tween(300))
                                        }
                                    }
                                }
                            )
                        }
                ) { ActivityData(details) }
            }
        }
        else -> {
            Text(text = "No details available.")
        }
    }
}



