package com.example.strovo.model

import com.example.strovo.data.model.GetStravaActivitiesModel

data class MonthlyDistanceModel (
    var distance: Int,
    var activities: GetStravaActivitiesModel?,
)