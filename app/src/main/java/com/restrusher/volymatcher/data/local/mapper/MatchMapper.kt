package com.restrusher.volymatcher.data.local.mapper

import com.restrusher.volymatcher.data.local.entity.MatchEntity
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.MatchFormat

fun MatchEntity.toDomain() = Match(
    id = id, sport = sport, title = title,
    format = runCatching { MatchFormat.valueOf(format) }.getOrDefault(MatchFormat.Official),
    date = date, venue = venue, status = status,
    score = score, teamA = teamA, teamB = teamB, winner = winner,
    teamCount = teamCount, balance = balance,
)

fun Match.toEntity() = MatchEntity(
    id = id, sport = sport, title = title, format = format.name,
    date = date, venue = venue, status = status,
    score = score, teamA = teamA, teamB = teamB, winner = winner,
    teamCount = teamCount, balance = balance,
)
