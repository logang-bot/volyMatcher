package com.restrusher.volymatcher.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.restrusher.volymatcher.data.local.entity.TeamEntity

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams")
    suspend fun getAll(): List<TeamEntity>

    @Query("SELECT * FROM teams WHERE name = :name")
    suspend fun getByName(name: String): TeamEntity?

    @Upsert
    suspend fun upsertAll(teams: List<TeamEntity>)
}
