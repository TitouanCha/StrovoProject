package com.example.strovo.data

//class ChartDistanceModel: ArrayList<ChartDistanceItem>()

data class ChartDistanceModel (
    var distance: Int,
    var activities: GetStravaActivitiesModel?,
)