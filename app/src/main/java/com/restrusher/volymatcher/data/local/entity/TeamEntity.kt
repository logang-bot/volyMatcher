package com.restrusher.volymatcher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val name: String,
    val colorLong: Long,
    val shape: String,
    val letter: String,
    val playerIds: String,
    val wins: Int,
    val losses: Int,
    val overallRating: Int,
    val sport: String,
    val createdAt: String,
    val setDiff: Int,
    val recentResults: String,
)
