package com.example.strovo.component.DashboardScreenComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.strovo.R
import com.example.strovo.component.DataActivityDisplay
import com.example.strovo.data.getStravaActivitiesModelItem
import com.example.strovo.screen.Screen
import com.example.strovo.utils.secondsToHms
import com.example.strovo.utils.speedToPaceMinPerKm
import com.example.strovo.utils.stravaDateToLocal
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlin.collections.get
import kotlin.collections.orEmpty


@Composable
fun CalendarDisplay(week: Long, data: List<getStravaActivitiesModelItem>, onclick: (getStravaActivitiesModelItem) -> Unit) {
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
                                if (activitiesForDay.isNotEmpty()) {
                                    onclick(activitiesForDay[0])
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