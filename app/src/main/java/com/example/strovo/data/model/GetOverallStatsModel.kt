package com.example.strovo.data.model

import com.example.strovo.model.strava.AllRunTotals
import com.example.strovo.model.strava.RecentRunTotals

data class GetOverallStatsModel(
    val all_run_totals: AllRunTotals,
    val recent_run_totals: RecentRunTotals
)