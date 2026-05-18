package com.restrusher.volymatcher.data.local.mapper

import com.restrusher.volymatcher.data.local.entity.TeamEntity
import com.restrusher.volymatcher.domain.model.CrestShape
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.model.Team

fun TeamEntity.toDomain(players: List<Player>) = Team(
    name = name,
    colorLong = colorLong,
    shape = runCatching { CrestShape.valueOf(shape) }.getOrDefault(CrestShape.Shield),
    letter = letter.firstOrNull() ?: name.firstOrNull() ?: '?',
    players = players,
    wins = wins,
    losses = losses,
    overallRating = overallRating,
    sport = sport,
    createdAt = createdAt,
    setDiff = setDiff,
    recentResults = recentResults.split(",").filter { it.isNotEmpty() },
)

fun Team.toEntity() = TeamEntity(
    name = name,
    colorLong = colorLong,
    shape = shape.name,
    letter = letter.toString(),
    playerIds = players.joinToString(",") { it.id },
    wins = wins,
    losses = losses,
    overallRating = overallRating,
    sport = sport,
    createdAt = createdAt,
    setDiff = setDiff,
    recentResults = recentResults.joinToString(","),
)
