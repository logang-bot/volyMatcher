package com.restrusher.volymatcher.data.remote.dto

import com.restrusher.volymatcher.data.local.entity.TeamEntity

data class TeamDto(
    val name: String,
    val color: Long,
    val shape: String,
    val letter: String,
    val playerIds: List<String>,
    val wins: Int,
    val losses: Int,
    val overallRating: Int,
    val sport: String,
    val createdAt: String,
    val setDiff: Int,
    val recentResults: List<String>,
)

fun TeamDto.toEntity() = TeamEntity(
    name = name, colorLong = color, shape = shape, letter = letter,
    playerIds = playerIds.joinToString(","),
    wins = wins, losses = losses, overallRating = overallRating,
    sport = sport, createdAt = createdAt, setDiff = setDiff,
    recentResults = recentResults.joinToString(","),
)
