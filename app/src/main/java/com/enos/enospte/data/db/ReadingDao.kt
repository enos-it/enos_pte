package com.enos.enospte.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {

    @Query("SELECT * FROM readings ORDER BY timestampMs DESC")
    fun observeAll(): Flow<List<ReadingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reading: ReadingEntity)

    @Query("DELETE FROM readings WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM readings WHERE enteredBy = :userId")
    suspend fun deleteByUserId(userId: String)

    @Query("DELETE FROM readings")
    suspend fun deleteAll()
}