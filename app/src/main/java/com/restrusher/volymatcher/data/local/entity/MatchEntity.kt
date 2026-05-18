package com.restrusher.volymatcher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: String,
    val sport: String,
    val title: String,
    val format: String,
    val date: String,
    val venue: String,
    val status: String,
    val score: String?,
    val teamA: String?,
    val teamB: String?,
    val winner: String?,
    val teamCount: Int?,
    val balance: Int,
)
