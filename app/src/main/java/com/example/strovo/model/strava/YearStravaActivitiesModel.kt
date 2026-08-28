package com.example.strovo.model.strava

import com.example.strovo.data.model.GetStravaActivitiesModelItem

data class YearStravaActivitiesModel(
    var year: Int,
    var allActivities:  MutableList<GetStravaActivitiesModelItem>
)