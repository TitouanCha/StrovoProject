package com.example.strovo.domain.model.strava

import com.example.strovo.model.strava.StravaActivityDetailModel
import org.maplibre.geojson.Point

data class StravaActivityDetailModel (
    var activityDetail: StravaActivityDetailModel,
    var trackPoints: List<Pair<Double, Double>>,
    var kmPoints: List<Point>,
    var lapPoints: List<Point>
)