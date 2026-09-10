package com.example.strovo.domain.repository

import com.example.strovo.model.strava.YearActivitiesModel

interface ProgressRepository {
    suspend fun getYearActivities(
        year: Int
    ): Result<YearActivitiesModel>
}