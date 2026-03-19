package com.example.strovo.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.strovo.R
import com.example.strovo.component.CustomBottomSheet
import com.example.strovo.component.HeaderComponent
import com.example.strovo.component.OnErrorComponent
import com.example.strovo.component.dashboardScreenComponents.AthleteStatsComponent
import com.example.strovo.component.dashboardScreenComponents.BottomSheetComponent
import com.example.strovo.component.dashboardScreenComponents.CalendarDisplay
import com.example.strovo.component.dashboardScreenComponents.LastActivityCard
import com.example.strovo.data.model.GetStravaActivitiesModelItem
import com.example.strovo.data.utils.PointerInputUtils
import com.example.strovo.component.Screen
import com.example.strovo.data.model.toDiscipline
import com.example.strovo.data.utils.DisciplineManager
import java.time.Instant
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, dashBoardViewModel: DashboardViewModel) {
    val pointerUtils = PointerInputUtils()

    val dashboardUiState = dashBoardViewModel.dashboardUiState.collectAsState().value
    //val selectedDiscipline = DisciplineManager(navController.context).getSelectedDisciplines()

    var refreshScrollState = remember { mutableStateOf(false) }
    var sheetState = remember { mutableStateOf(false) }
    var selectedActivities = remember { mutableStateOf<List<GetStravaActivitiesModelItem>?>(null) }

    val todayDate = Instant.now()
    val beforeDate = todayDate.epochSecond.toString()
    val afterDate = todayDate.minus(30, ChronoUnit.DAYS).epochSecond.toString()

    LaunchedEffect(Unit) {
//        val tokenManager = TokenManager(navController.context)
//        tokenManager.saveTokens("123", tokenManager.getRefreshToken()?:"", tokenManager.getAthleteId()?:"")

        if(dashboardUiState is DashboardUiState.Success) return@LaunchedEffect
        dashBoardViewModel.getDashBoardData(beforeDate, afterDate)
    }


    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .run {
                    with(pointerUtils) {
                        verticalDragToRefresh(
                            refreshScrollState = refreshScrollState
                        ) {
                            dashBoardViewModel.getDashBoardData(beforeDate, afterDate)
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ){
                HeaderComponent(
                    "Dashboard", R.drawable.baseline_settings_24,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    navController.navigate(Screen.Settings.route)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(9f),
                contentAlignment = Alignment.TopCenter
            ) {
                when (dashboardUiState) {
                    is DashboardUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier)
                    }
                    is DashboardUiState.Error -> {
                        OnErrorComponent(
                            errorMessage = dashboardUiState.message,
                            onRetry = {
                                dashBoardViewModel.refreshToken(beforeDate, afterDate)
                            }
                        )
                    }
                    is DashboardUiState.Success -> {
                        val activitiesData = dashboardUiState.dashboardData.monthActivity
                        val overallStat = dashboardUiState.dashboardData.overallStats
                        val lastActivity = dashboardUiState.dashboardData.lastActivity
                        val selectedDiscipline = dashboardUiState.dashboardData.selectedDiscipline
                        if (activitiesData.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LastActivityCard(
                                    lastActivity,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .weight(1.5f),
                                ) {
                                    navController.navigate(
                                        Screen.ActivityDetails.createRoute(
                                            activitiesData[0].id.toString()
                                        )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .weight(2.5f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column {
                                        val days = listOf("L", "Ma", "Me", "J", "V", "S", "D", "Km")
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            days.forEach { day ->
                                                Text(
                                                    text = day,
                                                    modifier = Modifier.weight(1f),
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                        CalendarDisplay(3, activitiesData, selectedDiscipline) { activity ->
                                            selectedActivities.value = activity
                                            sheetState.value = true
                                        }
                                        CalendarDisplay(2, activitiesData, selectedDiscipline) { activity ->
                                            selectedActivities.value = activity
                                            sheetState.value = true
                                        }
                                        CalendarDisplay(1, activitiesData, selectedDiscipline) { activity ->
                                            selectedActivities.value = activity
                                            sheetState.value = true
                                        }
                                        CalendarDisplay(0, activitiesData, selectedDiscipline) { activity ->
                                            selectedActivities.value = activity
                                            sheetState.value = true
                                        }
                                    }
                                }

                                AthleteStatsComponent(
                                    overallStat,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                        .weight(1.5f),
                                )
                            }
                        }
                    }
                }
                if (refreshScrollState.value) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(50)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(8.dp)

                        )
                    }
                }
            }
        }
        if (sheetState.value) {
            CustomBottomSheet(
                onDismiss = { sheetState.value = false },
                isVisible = sheetState.value,
            ) {
                BottomSheetComponent(selectedActivities.value) { index ->
                    sheetState.value = false
                    navController.navigate(
                        Screen.ActivityDetails.createRoute(
                            selectedActivities.value?.get(index)?.id.toString()
                        )
                    )
                }
            }
        }
    }
}


