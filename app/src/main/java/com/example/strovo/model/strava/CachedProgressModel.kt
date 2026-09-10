package com.example.strovo.model.strava

data class CachedProgressModel (
    var selectedYear: MutableList<YearActivitiesModel> = mutableListOf(),
    var lastYear: MutableList<YearActivitiesModel> = mutableListOf()
)