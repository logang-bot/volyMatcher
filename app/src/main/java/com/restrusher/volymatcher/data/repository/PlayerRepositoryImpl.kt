package com.restrusher.volymatcher.data.repository

import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.repository.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {
    override fun getAll(): List<Player> = SampleDataSource.players
    override fun getById(id: String): Player? = SampleDataSource.players.firstOrNull { it.id == id }
}
