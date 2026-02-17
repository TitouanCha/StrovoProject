package com.example.strovo.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.component.mapScreenComponents.MapYearComponent
import com.example.strovo.component.progressScreenComponents.YearSelectionComponent
import com.example.strovo.utils.viewModelUtils.getYearActivities
import com.example.strovo.viewModel.ProgressViewModel
import com.example.strovo.viewModel.StravaViewModel

@Composable
fun ActivitiesMapScreen(progressViewModel: ProgressViewModel, context: Context) {
    val selectedYearActivities = progressViewModel.selectedYearActivities.collectAsState()
    val isLoading = progressViewModel.isLoading.collectAsState()
    val selectedYear = progressViewModel.selectedYear.collectAsState()

    LaunchedEffect(selectedYear.value) {
        Log.d("MapDebug", "Selected year changed")
        getYearActivities(selectedYear.value, progressViewModel)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        YearSelectionComponent(
            incrementYear = {
                if(!isLoading.value){
                    progressViewModel.incrementYear()
                }
            },
            decrementYear = {
                if(!isLoading.value) {
                    progressViewModel.decrementYear()
                }
            },
            selectedYear = selectedYear.value
        )
        Box {
            selectedYearActivities.value?.let{ activities ->
                MapYearComponent(progressViewModel, context)
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                Text(
                    modifier = Modifier.padding(start = 4.dp, end = 20.dp, top = 2.dp, bottom = 2.dp),
                    text =
                        if (!isLoading.value) "Vos traces de l'année"
                        else "Chargement des traces...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}