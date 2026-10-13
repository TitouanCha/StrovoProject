package com.example.strovo.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.strovo.R
import com.example.strovo.presentation.component.CustomBottomSheet
import com.example.strovo.presentation.component.HeaderComponent
import com.example.strovo.presentation.component.OnErrorComponent
import com.example.strovo.presentation.component.dashboardScreenComponents.AthleteStatsComponent
import com.example.strovo.presentation.component.dashboardScreenComponents.BottomSheetComponent
import com.example.strovo.presentation.component.dashboardScreenComponents.CalendarDisplay
import com.example.strovo.presentation.component.dashboardScreenComponents.LastActivityCard
import com.example.strovo.data.model.strava.GetStravaActivitiesModelItem
import com.example.strovo.data.utils.PointerInputUtils
import com.example.strovo.presentation.component.Screen
import com.example.strovo.domain.model.ActivityDetailModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import java.time.Instant
import java.time.temporal.ChronoUnit


@Composable
fun DashboardScreen(navController: NavController, dashBoardViewModel: DashboardViewModel) {
    val pointerUtils = PointerInputUtils()
    val dashboardUiState = dashBoardViewModel.dashboardUiState.collectAsState().value
    val refreshScrollState = remember { mutableStateOf(false) }
    val sheetState = remember { mutableStateOf(false) }
    val selectedActivities = remember { mutableStateOf<List<ActivityDetailModel>?>(null) }


    LaunchedEffect(Unit) {
        //if (dashboardUiState is DashboardUiState.Success) return@LaunchedEffect
        dashBoardViewModel.getDashBoardData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .run {
                    with(pointerUtils) {
                        verticalDragToRefresh(
                            refreshScrollState = refreshScrollState
                        ) {
                            dashBoardViewModel.getDashBoardData()
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            HeaderComponent(
                "Dashboard", R.drawable.user_svg,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                navController.navigate(Screen.Settings.route)
            }
            // Refresh Indicator
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

            // Content
            when (dashboardUiState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }
                is DashboardUiState.Error -> {
                    OnErrorComponent(
                        errorMessage = dashboardUiState.message,
                        onRetry = {
                            dashBoardViewModel.refreshToken()
                        }
                    )
                }
                is DashboardUiState.Success -> {
                    val activitiesData = dashboardUiState.dashboardData.monthActivity
                    val healthData = dashboardUiState.dashboardData.healthData
                    val lastActivity = dashboardUiState.dashboardData.lastActivity
                    val selectedDiscipline = dashboardUiState.dashboardData.selectedDiscipline

                    if (activitiesData.isNotEmpty()) {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            // Calendar Display
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column {
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                    ){
                                        Icon(
                                            painter = painterResource(id = R.drawable.calendar_svg),
                                            contentDescription = "Calendar Icon",
                                            modifier = Modifier
                                                .size(28.dp)
                                                .padding(end = 8.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Activitées des derniers jours",
                                            fontSize = 22.sp,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
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
                                    CalendarDisplay(
                                        3,
                                        activitiesData,
                                        selectedDiscipline
                                    ) { activity ->
                                        selectedActivities.value = activity
                                        sheetState.value = true
                                    }
                                    CalendarDisplay(
                                        2,
                                        activitiesData,
                                        selectedDiscipline
                                    ) { activity ->
                                        selectedActivities.value = activity
                                        sheetState.value = true
                                    }
                                    CalendarDisplay(
                                        1,
                                        activitiesData,
                                        selectedDiscipline
                                    ) { activity ->
                                        selectedActivities.value = activity
                                        sheetState.value = true
                                    }
                                    CalendarDisplay(
                                        0,
                                        activitiesData,
                                        selectedDiscipline
                                    ) { activity ->
                                        selectedActivities.value = activity
                                        sheetState.value = true
                                    }
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                thickness = 2.dp
                            )
                            // Last Activity Card
                            LastActivityCard(
                                lastActivity,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            ) {
                                navController.navigate(
                                    Screen.ActivityDetails.createRoute(
                                        lastActivity?.id.toString()
                                    )
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 8.dp),
                                thickness = 2.dp
                            )

                            // Athlete Stats Component
                            AthleteStatsComponent(
                                healthData,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }
                    }
                }
            }

        }

        // Bottom Sheet
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
