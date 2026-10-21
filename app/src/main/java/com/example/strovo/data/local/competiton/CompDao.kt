package com.example.strovo.data.local.competiton

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CompDao {

    @Query("SELECT * FROM comp")
    suspend fun getAll(): List<CompEntity>

    @Query("SELECT * FROM comp WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CompEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CompEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<CompEntity>)

    @Query("DELETE FROM comp WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM comp")
    suspend fun getCount(): Int

    @Query("UPDATE comp SET activityId = :activityId WHERE id = :compId")
    suspend fun linkActivityToComp(compId: String, activityId: String)
}