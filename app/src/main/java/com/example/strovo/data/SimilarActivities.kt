package com.example.strovo.data

data class SimilarActivities(
    val average_speed: Double,
    val effort_count: Int,
    val frequency_milestone: Any,
    val max_average_speed: Double,
    val mid_average_speed: Double,
    val min_average_speed: Double,
    val pr_rank: Any,
    val resource_state: Int,
    val trend: Trend
)