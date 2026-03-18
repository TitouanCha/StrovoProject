package com.example.strovo.data.model

enum class Discipline {
    RUNNING,        // course
    CYCLING,        // vélo
    WALKING,        // marche
    HIKING,         // randonnée
//    CLIMBING,       // escalade
//    SWIMMING,       // natation
//    WORKOUT,        // fitness général
}

fun String.toDiscipline(): Discipline? = when (this) {
    "Run" -> Discipline.RUNNING
    "Ride" -> Discipline.CYCLING
    "Walk" -> Discipline.WALKING
    "Hike" -> Discipline.HIKING
//    "Swim" -> Discipline.SWIMMING
//    "Workout" -> Discipline.WORKOUT
//    "RockClimbing" -> Discipline.CLIMBING
    else -> null
}