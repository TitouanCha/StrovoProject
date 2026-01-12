package com.example.strovo.screen

import android.adservices.common.AdData
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.strovo.MainActivity
import com.example.strovo.data.AllRunTotals
import com.example.strovo.data.GetStravaActivitiesModel
import com.example.strovo.data.OverallStats
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.utils.DataFormattingUtils
import com.example.strovo.utils.TokenManager
import com.example.strovo.viewmodel.StravaViewModel
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
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
            fontSize = 10.sp
        )
        Text(
            text = data,
            modifier = Modifier,
            fontSize = 20.sp
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

@Composable
fun DashboardScreen(navController: NavController, viewModel: StravaViewModel = viewModel()) {
    val context = LocalContext.current
    val isInitialized = viewModel.isInitialized.collectAsState()
    val dataFormatting = DataFormattingUtils()

    val activities = viewModel.activities.collectAsState()
    val overallAthleteStat = viewModel.overallStats.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    val todayDate = Instant.now()

    LaunchedEffect(isInitialized.value) {
        if (isInitialized.value) {
            val beforeDate = todayDate.epochSecond.toString()
            val afterDate = todayDate.minus(30, ChronoUnit.DAYS).epochSecond.toString()
            viewModel.getActivities(
                context = context,
                perPage = 30,
                page = 1,
                before = beforeDate,
                after = afterDate,
                isStats = true
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text("Profil")
            IconButton(onClick = {
                navController.navigate(Screen.Profile.route)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_account_icon),
                    contentDescription = "Profile Icon",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        when {
            isLoading.value -> CircularProgressIndicator()
            errorMessage.value != null -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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
                var filteredActivity = activities.value?.filter {  it.type == "Run" }
                filteredActivity?.firstOrNull()?.let { activity ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.running_man_icon),
                                    contentDescription = "Activities Icon",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 8.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                                        } a Yerres",
                                        modifier = Modifier,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp
                                    )
                                }

                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            ) {
                                DataActivityDisplay("Distance", "${"%.1f".format(activity.distance / 1000)}km")
                                DataActivityDisplay("Durée", dataFormatting.secondsToHms(activity.moving_time))
                                DataActivityDisplay("Allure", dataFormatting.speedToPaceMinPerKm(activity.average_speed))
                            }
                        }
                    }
                }
                var activitiesData = activities.value ?: emptyList()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        val days = listOf("L", "Ma", "Me", "J", "V", "S", "D")
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
                        CalendarDisplay(3, activitiesData)
                        CalendarDisplay(2, activitiesData)
                        CalendarDisplay(1, activitiesData)
                        CalendarDisplay(0, activitiesData)
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(16.dp)
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
                            DataOverallStatsDisplay( "Distance", "${"%.0f".format(stats.distance / 1000)} km")
                            DataOverallStatsDisplay("Temps", dataFormatting.secondsToHms(stats.moving_time))
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun CalendarDisplay(week: Long, data: List<getStravaActivitiesModelItem>) {
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
    WeekCalendar(
        state = state,
        dayContent = { day ->
            val activitiesForDay = activitiesByDate[day.date].orEmpty()
            val activityCount = activitiesForDay.size
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .size(40.dp)
                    .background(
                        if (activityCount > 0)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                when{
                    activityCount > 0 -> {
                        val iconRes = when (activitiesForDay[0].type) {
                            "Run" -> R.drawable.running_svgrepo_com
                            "RockClimbing" -> R.drawable.climb_person_people_climber_svgrepo_com
                            "Ride" -> R.drawable.biking_svgrepo_com
                            "Hike" -> R.drawable.man_in_hike_svgrepo_com
                            else -> R.drawable.man_doing_exercises_svgrepo_com
                        }
                        Icon(
                            painter = painterResource( id = iconRes),
                            contentDescription = "Activity Icon",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
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
