package com.example.strovo.model

class GetStravaActivitiesModel : ArrayList<getStravaActivitiesModelItem>()

data class YearStravaActivitiesModel(
    var year: Int,
    var allActivities: GetStravaActivitiesModel
)