package com.example.strovo.domain.model

data class AverageStatsModel(
    val activities: String,
    val distance: String,
    val monthly_average: String,
    val weekly_average: String
)

data class AverageMonthStatsModel(
    val activities: Int,
    val distance: Double,
    val weekly_average: Double
)