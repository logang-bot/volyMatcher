package com.restrusher.volymatcher.data.repository

import com.restrusher.volymatcher.data.local.dao.PlayerDao
import com.restrusher.volymatcher.data.local.mapper.toDomain
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.repository.PlayerRepository

class PlayerRepositoryImpl(private val dao: PlayerDao) : PlayerRepository {
    override suspend fun getAll(): List<Player> = dao.getAll().map { it.toDomain() }
    override suspend fun getById(id: String): Player? = dao.getById(id)?.toDomain()
}
