package com.restrusher.volymatcher.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.restrusher.volymatcher.data.local.entity.MatchEntity

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches")
    suspend fun getAll(): List<MatchEntity>

    @Query("SELECT * FROM matches WHERE id = :id")
    suspend fun getById(id: String): MatchEntity?

    @Upsert
    suspend fun upsertAll(matches: List<MatchEntity>)
}
