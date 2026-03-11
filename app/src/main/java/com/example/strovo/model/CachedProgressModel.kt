package com.example.strovo.model

data class CachedProgressModel (
    var selectedYear: MutableList<YearStravaActivitiesModel> = mutableListOf(),
    var lastYear: MutableList<YearStravaActivitiesModel> = mutableListOf()
)