package com.example.strovo.model

//class ChartDistanceModel: ArrayList<ChartDistanceItem>()

data class MonthlyDistanceModel (
    var selectedYear: MutableList<MonthlyDistanceItem>?,
    var lastYear: MutableList<MonthlyDistanceItem>?,
)

data class MonthlyDistanceItem (
    var distance: Int,
    var activities: GetStravaActivitiesModel?,
)