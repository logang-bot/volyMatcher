package com.restrusher.volymatcher.domain.repository

import com.restrusher.volymatcher.domain.model.Player

interface PlayerRepository {
    fun getAll(): List<Player>
    fun getById(id: String): Player?
}
