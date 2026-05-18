package com.restrusher.volymatcher.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.restrusher.volymatcher.data.local.entity.PlayerEntity

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players")
    suspend fun getAll(): List<PlayerEntity>

    @Query("SELECT * FROM players WHERE id = :id")
    suspend fun getById(id: String): PlayerEntity?

    @Query("SELECT * FROM players WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<PlayerEntity>

    @Upsert
    suspend fun upsertAll(players: List<PlayerEntity>)
}
