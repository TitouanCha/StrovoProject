package com.example.strovo.data.local.competiton

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import androidx.room.TypeConverter
import com.example.strovo.domain.model.ActivityDetailModel
import com.google.gson.Gson
import java.util.Date

@TypeConverters(DateConverter::class, ActivityDetailConverter::class)
@Entity(tableName = "comp")
data class CompEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val date: Long,
    val location: String,
    val type: String,
    val activityId: String
)

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    @JvmStatic
    fun toDate(time: Long?): Date? = time?.let { Date(it) }
}

object ActivityDetailConverter {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun fromActivity(activity: ActivityDetailModel?): String =
        activity?.let { gson.toJson(it) } ?: ""

    @TypeConverter
    @JvmStatic
    fun toActivity(json: String?): ActivityDetailModel? =
        if (json.isNullOrEmpty()) null else gson.fromJson(json, ActivityDetailModel::class.java)
}