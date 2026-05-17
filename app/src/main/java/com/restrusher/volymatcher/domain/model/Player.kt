package com.restrusher.volymatcher.domain.model

data class Player(
    val id: String,
    val name: String,
    val nick: String,
    val colorLong: Long,
    val initials: String,
    val height: Int,
    val weight: Int,
    val reach: Int,
    val jump: Int,
    val speed: Int,
    val skill: Int,
    val hand: String,
    val role: String,
)
