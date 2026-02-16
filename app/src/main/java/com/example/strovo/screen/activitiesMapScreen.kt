package com.example.strovo.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.strovo.component.activityDetailsComponents.MapComponent
import com.example.strovo.viewModel.DashboardViewModel
import com.example.strovo.viewModel.ProgressViewModel
import com.example.strovo.viewModel.StravaViewModel
import org.maplibre.android.maps.MapView

@Composable
fun ActivitiesMapScreen(stravaViewModel: StravaViewModel, progressViewModel: ProgressViewModel) {
    val context = LocalContext.current
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            MapView(context).apply{
                getMapAsync { map ->
                    map.setStyle("https://tiles.openfreemap.org/styles/bright"){}
                }
            }
        }
    )
}