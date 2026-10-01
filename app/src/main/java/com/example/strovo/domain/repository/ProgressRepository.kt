package com.example.strovo.domain.repository

import com.example.strovo.domain.model.ActivityTrackPoint
import com.example.strovo.model.strava.YearActivitiesModel

interface ProgressRepository {
    suspend fun getYearActivities(
        year: Int
    ): Result<YearActivitiesModel>
    suspend fun getActivitiesTrackPoint(
        activityIds: List<String>
    ): Result<List<ActivityTrackPoint>>
}