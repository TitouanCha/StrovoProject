package com.example.strovo.model

import com.example.strovo.data.model.GetStravaActivitiesModel
import com.example.strovo.data.model.GetStravaActivitiesModelItem

data class MonthlyDistanceModel (
    var distance: Int,
    var activities: ArrayList<GetStravaActivitiesModelItem>,
)