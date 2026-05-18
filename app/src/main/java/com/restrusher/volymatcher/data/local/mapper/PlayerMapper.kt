package com.restrusher.volymatcher.data.local.mapper

import com.restrusher.volymatcher.data.local.entity.PlayerEntity
import com.restrusher.volymatcher.domain.model.Player

fun PlayerEntity.toDomain() = Player(
    id = id, name = name, nick = nick, colorLong = colorLong, initials = initials,
    height = height, weight = weight, reach = reach, jump = jump, speed = speed,
    skill = skill, hand = hand, role = role,
)

fun Player.toEntity() = PlayerEntity(
    id = id, name = name, nick = nick, colorLong = colorLong, initials = initials,
    height = height, weight = weight, reach = reach, jump = jump, speed = speed,
    skill = skill, hand = hand, role = role,
)
