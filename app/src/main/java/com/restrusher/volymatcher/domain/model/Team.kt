package com.restrusher.volymatcher.domain.model

data class Team(
    val name: String,
    val colorLong: Long,
    val shape: CrestShape,
    val letter: Char,
    val players: List<Player>,
    val wins: Int,
    val losses: Int,
    val overallRating: Int,
) {
    val winPercentage: Float get() = wins.toFloat() / (wins + losses).coerceAtLeast(1)
}

enum class CrestShape { Shield, Disc, Diamond, Blob }
