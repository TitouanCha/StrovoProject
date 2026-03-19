package com.example.strovo.domain.model

import com.example.strovo.data.model.Discipline

data class SettingsDataModel (
    val userName: String,
    val selectedDisciplines: List<Discipline>,
    val allDisciplines: List<Discipline>
)