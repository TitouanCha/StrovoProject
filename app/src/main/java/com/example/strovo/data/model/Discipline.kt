package com.example.strovo.data.model

enum class Discipline {
    RUNNING,        // course
    CYCLING,        // vélo
    WALKING,        // marche
    HIKING,         // randonnée
    CLIMBING,       // escalade
    SWIMMING,       // natation
    SURFING,        // surf
    WORKOUT,        // fitness général
    OTHER           // autre
}

fun String.toDiscipline(): Discipline? = when (this) {
    "Run" -> Discipline.RUNNING
    "Ride" -> Discipline.CYCLING
    "Walk" -> Discipline.WALKING
    "Hike" -> Discipline.HIKING
    "Swim" -> Discipline.SWIMMING
    "Workout" -> Discipline.WORKOUT
    "RockClimbing" -> Discipline.CLIMBING
    "Surfing" -> Discipline.SURFING
    else -> Discipline.OTHER
}