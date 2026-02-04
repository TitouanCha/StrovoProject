package com.example.strovo.screen

import android.R.attr.theme
import android.adservices.common.AdData
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.MainActivity
import com.example.strovo.component.DashboardScreenComponents.AthleteStatsComponent
import com.example.strovo.component.DashboardScreenComponents.BottomSheetComponent
import com.example.strovo.component.DashboardScreenComponents.CalendarDisplay
import com.example.strovo.component.DashboardScreenComponents.LastActivityCard
import com.example.strovo.component.DataActivityDisplay
import com.example.strovo.component.DataOverallStatsDisplay
import com.example.strovo.data.AllRunTotals
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.OverallStats
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.utils.PointerInputUtils
import com.example.strovo.utils.TokenManager
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm
import com.example.strovo.utils.stravaDateToLocal
import com.example.strovo.viewmodel.StravaViewModel
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.flow.internal.SendingCollector
import java.nio.file.WatchEvent
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import kotlin.compareTo
import kotlin.isInitialized
import kotlin.math.log
import kotlin.rem
import kotlin.text.toLong

fun getMonthActivities(viewModel: StravaViewModel, context: Context) {
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
fun DashboardScreen(navController: NavController, viewModel: StravaViewModel = viewModel()) {
    val context = LocalContext.current
    val isInitialized = viewModel.isInitialized.collectAsState()
    val pointerUtils = PointerInputUtils()

    val activities = viewModel.monthActivities.collectAsState()
    val overallAthleteStat = viewModel.overallStats.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    var refreshScrollState = remember { mutableStateOf(false) }

    var sheetState = rememberModalBottomSheetState()
    var showSheet = remember { mutableStateOf(false) }
    var selectedActivities = remember { mutableStateOf<getStravaActivitiesModelItem?>(null) }

    LaunchedEffect(isInitialized.value) {
        if (isInitialized.value && activities.value == null) {
            getMonthActivities(viewModel, context)
        }
    }

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
                        getMonthActivities(viewModel, context)
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(showSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showSheet.value = false },
                sheetState = sheetState
            ) {
                BottomSheetComponent(selectedActivities.value) {
                    showSheet.value = false
                        navController.navigate(
                            Screen.ActivityDetails.createRoute(
                                selectedActivities.value?.id.toString()
                            )
                        )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Box( modifier = Modifier.weight(1f)){}
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
            isLoading.value -> CircularProgressIndicator( modifier = Modifier.weight(25f))
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
                        .weight(10f),
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
                        CalendarDisplay(3, activitiesData){ activity ->
                            selectedActivities.value = activity
                            showSheet.value = true
                        }
                        CalendarDisplay(2, activitiesData){ activity ->
                            selectedActivities.value = activity
                            showSheet.value = true
                        }
                        CalendarDisplay(1, activitiesData){ activity ->
                            selectedActivities.value = activity
                            showSheet.value = true}
                        CalendarDisplay(0, activitiesData){ activity ->
                            selectedActivities.value = activity
                            showSheet.value = true}
                    }
                }
                AthleteStatsComponent(overallAthleteStat.value)
            }
        }
    }
}


