package com.example.strovo.screen

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.R
import com.example.strovo.component.activityDetailsComponents.ActivityData
import com.example.strovo.component.activityDetailsComponents.MapComponent
import com.example.strovo.viewModel.ActivityDetailViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun ActivityDetails(activityId: String, context: Context) {
    val viewModel: ActivityDetailViewModel = viewModel()

    val activityDetails = viewModel.activityDetails.collectAsState()
    val trackPoints = viewModel.trackPoints.collectAsState()
    val lapPoints = viewModel.lapPoints.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    val dataHeight = remember { Animatable(0.5f) }
    val mapHeight = remember { Animatable(0.5f) }
    val scope = rememberCoroutineScope()

    val selectedLap = remember { mutableStateOf<Int?>(null) }

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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(mapHeight.value)
                        .align(Alignment.TopCenter)
                ){
                    MapComponent(
                        context,
                        trackPoints.value,
                        lapPoints.value,
                        selectedLap.value
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable( onClick = {
                                scope.launch {
                                    coroutineScope {
                                        if(mapHeight.value == 1f){
                                            launch { mapHeight.animateTo(0.5f, tween(500)) }
                                            launch { dataHeight.animateTo(0.5f, tween(500))}
                                        } else {
                                            launch { mapHeight.animateTo(1f, tween(500)) }
                                            launch { dataHeight.animateTo(0.1f, tween(500)) }
                                        }
                                    }
                                }
                            })
                ){
                    Icon(
                        painter = painterResource(
                            id = if (mapHeight.value == 0.5f) {
                                R.drawable.baseline_fullscreen_24
                            } else {
                                R.drawable.baseline_fullscreen_exit_24
                            }),
                        contentDescription = "Fullscreen Icon",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(dataHeight.value)
                        .align(Alignment.BottomCenter)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    scope.launch {
                                        when (dataHeight.value) {
                                            0.9f -> dataHeight.animateTo(0.5f, tween(500))
                                            0.5f -> dataHeight.animateTo(0.9f, tween(500))
                                            else -> {
                                                coroutineScope {
                                                    launch{ dataHeight.animateTo(0.5f, tween(500))}
                                                    launch{mapHeight.animateTo(0.5f, tween(500))}
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            var totalDrag = 0f
                            detectVerticalDragGestures(
                                onDragStart = { totalDrag = 0f },
                                onVerticalDrag = { _, dragAmount ->
                                    totalDrag += dragAmount
                                    scope.launch {
                                        when {
                                            totalDrag > 200 -> dataHeight.animateTo(0.5f, tween(500))
                                            totalDrag < -200f ->
                                                if(mapHeight.value == 0.5f) {
                                                    dataHeight.animateTo(0.9f, tween(500))
                                                }else{
                                                    coroutineScope {
                                                        launch{ dataHeight.animateTo(0.5f, tween(500)) }
                                                        launch{ mapHeight.animateTo(0.5f, tween(500)) }
                                                    }
                                                }
                                        }
                                    }
                                }
                            )
                        }
                ) {
                    ActivityData(details){ index ->
                        if(selectedLap.value == index){
                            selectedLap.value = null
                        } else {
                            selectedLap.value = index
                        }
                    }
                }
            }
        }
        else -> {
            Text(text = "No details available.")
        }
    }
}



