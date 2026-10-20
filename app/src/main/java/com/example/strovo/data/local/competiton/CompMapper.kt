package com.example.strovo.data.local.competiton

import com.example.strovo.domain.model.ActivityDetailModel
import com.example.strovo.domain.model.CompModel
import com.google.gson.Gson
import java.util.Date

object CompMapper {
    private val gson = Gson()

    fun toEntity(domain: CompModel): CompEntity = CompEntity(
        id = domain.id,
        name = domain.name,
        description = domain.description,
        date = domain.date.time,
        location = domain.location,
        type = domain.type,
        activityJson = gson.toJson(domain.activityId)
    )

    fun toDomain(entity: CompEntity): CompModel = CompModel(
        id = entity.id,
        name = entity.name,
        description = entity.description,
        date = Date(entity.date),
        location = entity.location,
        type = entity.type,
        activityId = gson.fromJson(entity.activityJson, ActivityDetailModel::class.java)
    )
}