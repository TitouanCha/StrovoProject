package com.example.strovo.data

data class SegmentEffort(
    val achievements: List<Achievement>,
    val activity: Activity,
    val athlete: AthleteX,
    val average_cadence: Double,
    val average_watts: Double,
    val device_watts: Boolean,
    val distance: Double,
    val elapsed_time: Int,
    val end_index: Int,
    val hidden: Boolean,
    val id: Long,
    val moving_time: Int,
    val name: String,
    val pr_rank: Int,
    val resource_state: Int,
    val segment: Segment,
    val start_date: String,
    val start_date_local: String,
    val start_index: Int,
    val visibility: String
)