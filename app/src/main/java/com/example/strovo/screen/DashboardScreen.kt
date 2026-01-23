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
import com.example.strovo.data.AllRunTotals
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.OverallStats
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.utils.DataFormattingUtils
import com.example.strovo.utils.PointerInputUtils
import com.example.strovo.utils.TokenManager
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

@Composable
fun RowScope.DataActivityDisplay(title: String, data: String) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = title,
            modifier = Modifier,
            fontSize = 10.sp,
            lineHeight = 12.sp
        )
        Text(
            text = data,
            modifier = Modifier,
            fontSize = 20.sp,
            lineHeight = 22.sp,
        )
    }
}

@Composable
fun DataOverallStatsDisplay(title: String, data: String){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = title
        )
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = ":"
        )
        Text(
            modifier = Modifier.weight(2f),
            text = data
        )
    }
}

fun getMonthActivities(viewModel: StravaViewModel, context: Context) {
    val todayDate = Instant.now()
    val beforeDate = todayDate.epochSecond.toString()
    val afterDate = todayDate.minus(30, ChronoUnit.DAYS).epochSecond.toString()
    viewModel.getMonthActivities(
        before = beforeDate,
        after = afterDate
    )
}

@Composable
fun DashboardScreen(navController: NavController, viewModel: StravaViewModel = viewModel()) {
    val context = LocalContext.current
    val isInitialized = viewModel.isInitialized.collectAsState()
    val dataFormatting = DataFormattingUtils()
    val pointerUtils = PointerInputUtils()

    val activities = viewModel.monthActivities.collectAsState()
    val overallAthleteStat = viewModel.overallStats.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    var refreshScrollState = remember { mutableStateOf(false) }

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
                filteredActivity?.firstOrNull()?.let { activity ->
                    if (refreshScrollState.value) {
                        CircularProgressIndicator()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .weight(7f),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                navController.navigate(Screen.ActivityDetails.createRoute(activity.id.toString()))
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter =
                                            if(activity.name.contains("Finisher", ignoreCase = true))
                                                painterResource(id = R.drawable.trophy_cup_silhouette_svgrepo_com)
                                            else
                                                painterResource(id = R.drawable.running_man_icon),
                                        contentDescription = "Activities Icon",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(end = 8.dp),
                                        tint =
                                            if(activity.name.contains("Finisher", ignoreCase = true))
                                                MaterialTheme.colorScheme.tertiary
                                            else
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Text(
                                            text = "Dernieres Activités",
                                            modifier = Modifier,
                                            fontSize = 16.sp,
                                            lineHeight = 18.sp
                                        )
                                        Text(
                                            text = "Course du ${
                                                dataFormatting.stravaDateToLocal(
                                                    activity.start_date_local
                                                )
                                            }",
                                            modifier = Modifier,
                                            fontSize = 10.sp,
                                            lineHeight = 12.sp
                                        )
                                    }

                                }
                                var fontSize = 20.sp
                                if(activity.name.length > 30){
                                    fontSize = 16.sp
                                } else if(activity.name.length > 20){
                                    fontSize = 20.sp
                                }
                                Text(activity.name,
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                                    fontSize = fontSize,
                                    lineHeight = fontSize,
                                    maxLines = 2,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                                ) {
                                    DataActivityDisplay(
                                        "Distance",
                                        "${"%.1f".format(activity.distance / 1000)}km"
                                    )
                                    DataActivityDisplay(
                                        "Durée",
                                        dataFormatting.secondsToHms(activity.moving_time)
                                    )
                                    DataActivityDisplay(
                                        "Allure",
                                        dataFormatting.speedToPaceMinPerKm(activity.average_speed)
                                    )
                                }
                            }
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
                            CalendarDisplay(3, activitiesData, navController)
                            CalendarDisplay(2, activitiesData, navController)
                            CalendarDisplay(1, activitiesData, navController)
                            CalendarDisplay(0, activitiesData, navController)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .weight(7f),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            overallAthleteStat.value?.all_run_totals?.let { stats ->
                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .padding(8.dp),
                                            painter = painterResource(R.drawable.progress_svgrepo_com),
                                            contentDescription = "Stats Icon",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            modifier = Modifier.padding(8.dp),
                                            text = "Stats générales",
                                            fontSize = 18.sp,
                                            lineHeight = 20.sp
                                        )
                                    }
                                    DataOverallStatsDisplay("Activités", stats.count.toString())
                                    DataOverallStatsDisplay(
                                        "Distance",
                                        "${"%.0f".format(stats.distance / 1000)} km"
                                    )
                                    DataOverallStatsDisplay(
                                        "Temps",
                                        dataFormatting.secondsToHms(stats.moving_time)
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDisplay(week: Long, data: List<getStravaActivitiesModelItem>, navController: NavController) {
    val dataFormatting = DataFormattingUtils()
    var sheetState = rememberModalBottomSheetState()
    var showSheet = remember { mutableStateOf(false) }
    var selectedActivities = remember { mutableStateOf<getStravaActivitiesModelItem?>(null) }


    val today = remember { LocalDate.now().minusDays(week * 7L) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val startOfWeek = remember(today) {
        today.minusDays(
            ((today.dayOfWeek.value - firstDayOfWeek.value + 7) % 7).toLong()
        )
    }
    val endOfWeek = remember(startOfWeek) {
        startOfWeek.plusDays(6)
    }
    val weekActivities = data.filter { activity ->
        val date = OffsetDateTime
            .parse(activity.start_date_local)
            .toLocalDate()
        date >= startOfWeek && date <= endOfWeek
    }
    val activitiesByDate = remember(weekActivities) {
        weekActivities.groupBy { activity ->
            OffsetDateTime.parse(activity.start_date_local).toLocalDate()
        }
    }
    val state = rememberWeekCalendarState(
        startDate = startOfWeek,
        endDate = startOfWeek,
        firstDayOfWeek = firstDayOfWeek
    )

    if(showSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showSheet.value = false },
            sheetState = sheetState
        ) {
            selectedActivities.value?.let { activity ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    Text(
                        "Activité du ${DataFormattingUtils().stravaDateToLocal(activity.start_date_local)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    Text(
                        "Type d'activitées : ${activity.type}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(
                        activity.name,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
                    )
                    if (activity.type == "Run") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
                        ) {
                            DataActivityDisplay(
                                "Distance",
                                "${"%.1f".format(activity.distance / 1000)}km"
                            )
                            DataActivityDisplay(
                                "Durée",
                                dataFormatting.secondsToHms(activity.moving_time)
                            )
                            DataActivityDisplay(
                                "Allure",
                                dataFormatting.speedToPaceMinPerKm(activity.average_speed)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Button(
                                onClick = {
                                    showSheet.value = false
                                    navController.navigate(Screen.ActivityDetails.createRoute(activity.id.toString()))
                                }
                            ) {
                                Text(
                                    "Detail de l'activité",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }


                }
            }
        }
    }
    Row{
        Box(
            modifier = Modifier.weight(7f)
        ) {
            WeekCalendar(
                state = state,
                dayContent = { day ->
                    val activitiesForDay = activitiesByDate[day.date].orEmpty().reversed()
                    val activityCount = activitiesForDay.size
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(35.dp)
                            .background(
                                if (activityCount == 1)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (activitiesForDay.isNotEmpty() &&
                                   activitiesForDay[0].name.contains("Finisher", ignoreCase = true))
                                      MaterialTheme.colorScheme.tertiary
                                else
                                   Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                if (activityCount > 0) {
                                    selectedActivities.value = activitiesForDay[0]
                                    showSheet.value = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            activityCount > 0 -> {
                                val iconRes = when (activitiesForDay[0].type) {
                                    "Run" -> R.drawable.running_svgrepo_com
                                    "RockClimbing" -> R.drawable.climb_person_people_climber_svgrepo_com
                                    "Ride" -> R.drawable.biking_svgrepo_com
                                    "Hike" -> R.drawable.man_in_hike_svgrepo_com
                                    else -> R.drawable.man_doing_exercises_svgrepo_com
                                }
                                Icon(
                                    painter =
                                        if(activitiesForDay[0].name.contains("Finisher", ignoreCase = true))
                                            painterResource(id = R.drawable.trophy_cup_silhouette_svgrepo_com)
                                        else
                                            painterResource(id = iconRes),
                                    contentDescription = "Activity Icon",
                                    modifier = Modifier.size(20.dp),
                                    tint =
                                        if(activitiesForDay[0].name.contains("Finisher", ignoreCase = true))
                                            MaterialTheme.colorScheme.tertiary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            else -> {
                                Text(
                                    text = day.date.dayOfMonth.toString(),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
                .size(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = weekActivities.filter { it.type == "Run" }.sumOf { it.distance }
                    .let { "%.0f".format(it / 1000) },
                textAlign = TextAlign.Center
            )
        }
    }
}
