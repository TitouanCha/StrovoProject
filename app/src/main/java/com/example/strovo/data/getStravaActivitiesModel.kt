package com.example.strovo.data

class GetStravaActivitiesModel : ArrayList<getStravaActivitiesModelItem>()

data class YearStravaActivitiesModel(
    var year: Int,
    var allActivities: GetStravaActivitiesModel
)