package com.example.strovo.domain.repository

import com.example.strovo.model.strava.YearStravaActivitiesModel

interface ProgressRepository {
    suspend fun getYearActivities(
        year: Int
    ): Result<YearStravaActivitiesModel>
}