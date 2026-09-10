package com.example.strovo.model.strava

import com.example.strovo.domain.model.ActivityDetailModel

data class YearActivitiesModel(
    var year: Int,
    var allActivities: MutableList<ActivityDetailModel>
)