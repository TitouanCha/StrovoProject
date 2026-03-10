package com.example.strovo.domain.model

import com.example.strovo.data.model.GetStravaActivitiesModel

//class ChartDistanceModel: ArrayList<ChartDistanceItem>()

data class MonthlyDistanceModel (
    var selectedYear: MutableList<MonthlyDistanceItem>?,
    var lastYear: MutableList<MonthlyDistanceItem>?,
)

data class MonthlyDistanceItem (
    var distance: Int,
    var activities: GetStravaActivitiesModel?,
)