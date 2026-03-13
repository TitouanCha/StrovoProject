package com.example.strovo.presentation.map

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.component.mapScreenComponents.MapYearComponent
import com.example.strovo.component.progressScreenComponents.YearSelectionComponent
import com.example.strovo.presentation.progress.ProgressUiState
import com.example.strovo.presentation.progress.ProgressViewModel

@Composable
fun ActivitiesMapScreen(progressViewModel: ProgressViewModel) {
    val context = LocalContext.current
    val progressUiState = progressViewModel.progressUiState.collectAsState().value
    val selectedYear = progressViewModel.selectedYear.collectAsState().value

    val tracksPoint = remember { mutableStateOf<List<List<Pair<Double, Double>>>>(listOf()) }

    LaunchedEffect(selectedYear) {
        progressViewModel.loadProgressData(selectedYear)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        YearSelectionComponent(
            incrementYear = {
                progressViewModel.incrementYear()
            },
            decrementYear = {
                progressViewModel.decrementYear()
            },
            selectedYear = selectedYear
        )
        Box {
            MapYearComponent(tracksPoint.value, context)
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                when(progressUiState){
                    is ProgressUiState.Loading -> {
                        Text(
                            modifier = Modifier.padding(start = 4.dp, end = 20.dp, top = 2.dp, bottom = 2.dp),
                            text = "Chargement des traces de l'année...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is ProgressUiState.Error -> {
                        Text(
                            modifier = Modifier.padding(start = 4.dp, end = 20.dp, top = 2.dp, bottom = 2.dp),
                            text = "Erreur lors du chargement des traces",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is ProgressUiState.Success -> {
                        tracksPoint.value = progressUiState.progressData.activitiesTrackPoints
                        Text(
                            modifier = Modifier.padding(start = 4.dp, end = 20.dp, top = 2.dp, bottom = 2.dp),
                            text = "Vos traces de l'année",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}