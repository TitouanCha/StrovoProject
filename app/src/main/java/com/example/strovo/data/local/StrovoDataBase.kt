package com.example.strovo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.strovo.data.local.competiton.CompDao
import com.example.strovo.data.local.competiton.CompEntity

@Database(
    entities = [
        CompEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StrovoDataBase : RoomDatabase() {
    abstract fun compDao(): CompDao
}
