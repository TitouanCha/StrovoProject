package com.example.strovo.domain.model

import com.example.strovo.model.StravaActivityDetailModel
import org.maplibre.geojson.Point

data class ActivityDetailModel (
    var activityDetail: StravaActivityDetailModel,
    var trackPoints: List<Pair<Double, Double>>,
    var kmPoints: List<Point>,
    var lapPoints: List<Point>
)
