package com.example.strovo.data.model

class GetStravaActivitiesModel : ArrayList<GetStravaActivitiesModelItem>()

data class YearStravaActivitiesModel(
    var year: Int,
    var allActivities: GetStravaActivitiesModel
)