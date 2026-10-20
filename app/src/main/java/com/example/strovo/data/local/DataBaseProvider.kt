package com.example.strovo.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: StrovoDataBase? = null

    fun getDatabase(context: Context): StrovoDataBase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                StrovoDataBase::class.java,
                "strovo_db"
            )
                //.fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}