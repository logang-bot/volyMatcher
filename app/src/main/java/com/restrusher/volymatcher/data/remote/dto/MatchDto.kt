package com.restrusher.volymatcher.data.remote.dto

import com.restrusher.volymatcher.data.local.entity.MatchEntity

data class MatchDto(
    val id: String,
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

fun MatchDto.toEntity() = MatchEntity(
    id = id, sport = sport, title = title, format = format,
    date = date, venue = venue, status = status,
    score = score, teamA = teamA, teamB = teamB, winner = winner,
    teamCount = teamCount, balance = balance,
)
