package com.example.strovo.model.strava

import com.example.strovo.data.model.strava.GetStravaActivitiesModelItem

data class MonthlyDistanceModel (
    var distance: Int,
    var activities: ArrayList<GetStravaActivitiesModelItem>,
)