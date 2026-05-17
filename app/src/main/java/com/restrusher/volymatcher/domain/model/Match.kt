package com.restrusher.volymatcher.domain.model

data class Match(
    val id: String,
    val sport: String,
    val title: String,
    val format: MatchFormat,
    val date: String,
    val venue: String,
    val status: String,
    val score: String? = null,
    val teamA: String? = null,
    val teamB: String? = null,
    val winner: String? = null,
    val teamCount: Int? = null,
    val balance: Int,
)

enum class MatchFormat { Official, BattleRoyale }
