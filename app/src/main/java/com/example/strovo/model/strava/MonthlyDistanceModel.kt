package com.example.strovo.model.strava

import com.example.strovo.data.model.strava.GetStravaActivitiesModelItem
import com.example.strovo.domain.model.ActivityDetailModel

data class MonthlyDistanceModel (
    var distance: Int,
    var activities: ArrayList<ActivityDetailModel>,
)