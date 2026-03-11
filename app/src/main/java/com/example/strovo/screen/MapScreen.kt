package com.example.strovo.screen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.strovo.component.mapScreenComponents.MapYearComponent
import com.example.strovo.component.progressScreenComponents.YearSelectionComponent
import com.example.strovo.presentation.progress.ProgressViewModel

@Composable
fun ActivitiesMapScreen(holdProgressViewModel: ProgressViewModel, context: Context) {
//    val selectedYearActivities = holdProgressViewModel.selectedYearActivities.collectAsState()
//    val isLoading = holdProgressViewModel.isLoading.collectAsState()
//    val selectedYear = holdProgressViewModel.selectedYear.collectAsState()
//
//    LaunchedEffect(selectedYear.value) {
//        Log.d("MapDebug", "Selected year changed")
//        getYearActivities(selectedYear.value, holdProgressViewModel)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//
//    ) {
//        YearSelectionComponent(
//            incrementYear = {
//                if(!isLoading.value){
//                    holdProgressViewModel.incrementYear()
//                }
//            },
//            decrementYear = {
//                if(!isLoading.value) {
//                    holdProgressViewModel.decrementYear()
//                }
//            },
//            selectedYear = selectedYear.value
//        )
//        Box {
//            selectedYearActivities.value?.let{ activities ->
//                MapYearComponent(holdProgressViewModel, context)
//            }
//            Box(
//                modifier = Modifier
//                    .padding(8.dp)
//                    .background(
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
//                        shape = RoundedCornerShape(8.dp)
//                    ),
//            ) {
//                Text(
//                    modifier = Modifier.padding(start = 4.dp, end = 20.dp, top = 2.dp, bottom = 2.dp),
//                    text =
//                        if (!isLoading.value) "Vos traces de l'année"
//                        else "Chargement des traces...",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 20.sp,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//    }

}