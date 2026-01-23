package com.example.strovo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.strovo.viewmodel.StravaViewModel


@Composable
fun ActivityDetails(navController: NavController, viewModel: StravaViewModel, activityId: String) {
    val activityDetails = viewModel.activityDetails.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

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
            Column(
                modifier = Modifier.padding(16.dp)
            ){
                Text(text = "Activity Name: ${details.name}")
                Text(text = "Distance: ${details.distance} meters")
                Text(text = "Moving Time: ${details.moving_time} seconds")
                Text(text = "Elevation Gain: ${details.total_elevation_gain} meters")
            }
        }
        else -> {
            Text(text = "No details available.")
        }
    }
}