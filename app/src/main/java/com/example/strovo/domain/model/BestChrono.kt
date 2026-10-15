package com.example.strovo.domain.model

import androidx.compose.foundation.pager.PagerSnapDistance
import com.example.strovo.data.model.FetchActivity
import com.example.strovo.data.utils.secondsToHms
import com.example.strovo.data.utils.speedToPaceMinPerKm
import com.example.strovo.model.strava.Activity
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.text.toLong
import kotlin.toString

data class BestChrono(
    val activityId: String,
    val chrono: String,
    val speed: String,
    val distance: Long,
    val date: String,
){
    companion object{
        fun fromActivityList(
            activities: List<FetchActivity>,
            distance: Long
        ): BestChrono? {
            val toleranceBelow = 100L
            val toleranceAbove = 500L

            return activities
                .filter {
                    it.distance.toLong() in (distance - toleranceBelow)..(distance + toleranceAbove)
                }
                .minByOrNull { it.movingTime }
                ?.let { activity ->
                    val chrono = secondsToHms(activity.movingTime)
                    val date = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                        .format(Date.from(Instant.parse(activity.startDate)))
                    val speed = speedToPaceMinPerKm(activity.averageSpeed)

                    BestChrono(
                        activityId = activity.id.toString(),
                        chrono = chrono,
                        distance = activity.distance.toLong(),
                        speed = speed,
                        date = date,
                    )
                }
        }
        fun fetchLongestDistance(activities: List<FetchActivity>): BestChrono? {
            return activities.maxByOrNull { it.distance }?.let { activity ->
                val chrono = secondsToHms(activity.movingTime)
                val date = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format( Date.from(Instant.parse(activity.startDate)))
                val speed = speedToPaceMinPerKm(activity.averageSpeed)
                BestChrono(
                    activityId = activity.id.toString(),
                    chrono = chrono,
                    distance = activity.distance.toLong(),
                    speed = speed,
                    date = date
                )
            }
        }
    }
}
