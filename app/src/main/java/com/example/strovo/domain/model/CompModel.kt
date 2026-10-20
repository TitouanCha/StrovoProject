package com.example.strovo.domain.model

import java.util.Date

data class CompModel (
    val id: String,
    val name: String,
    val description: String,
    val date: Date,
    val location: String,
    val type: String,
    val activityId: String,
)