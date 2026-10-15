package com.example.strovo.domain.model

data class AthletePB(
    val distance5km: BestChrono?,
    val distance10km: BestChrono?,
    val distanceHalfMarathon: BestChrono?,
    val distanceMarathon: BestChrono?,
    val distanceLongest: BestChrono?,
)
