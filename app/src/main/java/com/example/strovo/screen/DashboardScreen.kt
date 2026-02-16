package com.example.strovo.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.strovo.R
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.strovo.component.CustomBottomSheet
import com.example.strovo.component.dashboardScreenComponents.AthleteStatsComponent
import com.example.strovo.component.dashboardScreenComponents.BottomSheetComponent
import com.example.strovo.component.dashboardScreenComponents.CalendarDisplay
import com.example.strovo.component.dashboardScreenComponents.LastActivityCard
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.utils.PointerInputUtils
import com.example.strovo.viewModel.DashboardViewModel
import com.example.strovo.viewModel.StravaViewModel
import java.time.Instant
import java.time.temporal.ChronoUnit

fun getMonthActivities(viewModel: DashboardViewModel, context: Context) {
    val todayDate = Instant.now()
    val beforeDate = todayDate.epochSecond.toString()
    val afterDate = todayDate.minus(30, ChronoUnit.DAYS).epochSecond.toString()
    viewModel.getMonthActivities(
        before = beforeDate,
        after = afterDate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, stravaViewModel: StravaViewModel, dashBoardViewModel: DashboardViewModel) {

    val context = LocalContext.current
    val isInitialized = stravaViewModel.isInitialized.collectAsState()
    val pointerUtils = PointerInputUtils()

    val activities = dashBoardViewModel.monthActivities.collectAsState()
    val overallAthleteStat = dashBoardViewModel.overallStats.collectAsState()
    val isLoading = dashBoardViewModel.isLoading.collectAsState()
    val errorMessage = dashBoardViewModel.errorMessage.collectAsState()

    var refreshScrollState = remember { mutableStateOf(false) }

    var sheetState = remember { mutableStateOf(false) }
    var selectedActivities = remember { mutableStateOf<List<getStravaActivitiesModelItem>?>(null) }

    LaunchedEffect(isInitialized.value) {
        if (isInitialized.value && activities.value == null) {
            getMonthActivities(dashBoardViewModel, context)
        }
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
                            refreshScrollState = refreshScrollState,
                            isInitialized = isInitialized.value
                        ) {
                            getMonthActivities(dashBoardViewModel, context)
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .weight(2f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(modifier = Modifier.weight(1f)) {}
                Text(
                    modifier = Modifier
                        .weight(4f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    text = "Dashboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = "Settings Icon",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            when {
                isLoading.value -> CircularProgressIndicator(modifier = Modifier.weight(25f))
                errorMessage.value != null && activities.value == null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .weight(8f)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Erreur lors du chargement des activités",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Verrifier votre connexion a Strava.",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                activities.value != null -> {
                    var filteredActivity = activities.value?.filter { it.type == "Run" }
                    if (refreshScrollState.value) {
                        CircularProgressIndicator()
                    }
                    filteredActivity?.firstOrNull()?.let { activity ->
                        LastActivityCard(activity) {
                            navController.navigate(Screen.ActivityDetails.createRoute(activity.id.toString()))
                        }
                    }
                    var activitiesData = activities.value ?: emptyList()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .weight(8f),
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
                            CalendarDisplay(3, activitiesData) { activity ->
                                selectedActivities.value = activity
                                sheetState.value = true
                            }
                            CalendarDisplay(2, activitiesData) { activity ->
                                selectedActivities.value = activity
                                sheetState.value = true
                            }
                            CalendarDisplay(1, activitiesData) { activity ->
                                selectedActivities.value = activity
                                sheetState.value = true
                            }
                            CalendarDisplay(0, activitiesData) { activity ->
                                selectedActivities.value = activity
                                sheetState.value = true
                            }
                        }
                    }
                    AthleteStatsComponent(overallAthleteStat.value)
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


