package com.restrusher.volymatcher.domain.repository

import com.restrusher.volymatcher.domain.model.Player

interface PlayerRepository {
    suspend fun getAll(): List<Player>
    suspend fun getById(id: String): Player?
}
