package com.example.strovo.model

import com.example.strovo.data.model.GetStravaActivitiesModel
import com.example.strovo.data.model.GetStravaActivitiesModelItem

data class YearStravaActivitiesModel(
    var year: Int,
    var allActivities:  MutableList<GetStravaActivitiesModelItem>
)