package com.example.strovo.model.strava

data class CachedProgressModel (
    var selectedYear: MutableList<YearStravaActivitiesModel> = mutableListOf(),
    var lastYear: MutableList<YearStravaActivitiesModel> = mutableListOf()
)