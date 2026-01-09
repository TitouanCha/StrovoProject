package com.example.strovo.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DataFormattingUtils {
    fun secondsToHms(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        if(h>0){ return "%02dh%02d:%02d".format(h, m, s) }
        return  "%02d:%02d".format(m, s)
    }

    fun speedToPaceMinPerKm(speed: Double): String {
        val totalSecondsPerKm = 1000 / speed
        val minutes = (totalSecondsPerKm / 60).toInt()
        val seconds = (totalSecondsPerKm % 60).toInt()
        return "%d:%02d/km".format(minutes, seconds)
    }

    fun stravaDateToLocal(date: String): String {
        val instant = Instant.parse(date)
        val localDateTime = instant.atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)
        val formattedDate = localDateTime.format(formatter)
        return formattedDate
    }
}