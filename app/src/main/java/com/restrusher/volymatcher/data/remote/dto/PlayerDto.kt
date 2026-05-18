package com.restrusher.volymatcher.data.remote.dto

import com.restrusher.volymatcher.data.local.entity.PlayerEntity

data class PlayerDto(
    val id: String,
    val name: String,
    val nick: String,
    val color: Long,
    val initials: String,
    val heightCm: Int,
    val weightKg: Int,
    val reach: Int,
    val jump: Int,
    val speed: Int,
    val skill: Int,
    val handedness: String,
    val role: String,
)

fun PlayerDto.toEntity() = PlayerEntity(
    id = id, name = name, nick = nick, colorLong = color, initials = initials,
    height = heightCm, weight = weightKg, reach = reach, jump = jump, speed = speed,
    skill = skill, hand = handedness, role = role,
)
