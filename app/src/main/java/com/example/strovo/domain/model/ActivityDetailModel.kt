package com.example.strovo.domain.model

import com.example.strovo.model.strava.StravaActivityDetailModel
import org.maplibre.geojson.Point

data class ActivityDetailModel (
    var activityDetail: StravaActivityDetailModel,
    var trackPoints: List<Pair<Double, Double>>,
    var kmPoints: List<Point>,
    var lapPoints: List<Point>
)
