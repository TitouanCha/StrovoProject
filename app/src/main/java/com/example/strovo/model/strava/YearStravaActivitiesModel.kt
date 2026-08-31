package com.example.strovo.model.strava

import com.example.strovo.data.model.strava.GetStravaActivitiesModelItem

data class YearStravaActivitiesModel(
    var year: Int,
    var allActivities:  MutableList<GetStravaActivitiesModelItem>
)