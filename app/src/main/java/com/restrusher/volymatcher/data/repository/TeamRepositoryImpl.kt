package com.restrusher.volymatcher.data.repository

import com.restrusher.volymatcher.data.local.dao.PlayerDao
import com.restrusher.volymatcher.data.local.dao.TeamDao
import com.restrusher.volymatcher.data.local.mapper.toDomain
import com.restrusher.volymatcher.domain.model.Team
import com.restrusher.volymatcher.domain.repository.TeamRepository

class TeamRepositoryImpl(
    private val teamDao: TeamDao,
    private val playerDao: PlayerDao,
) : TeamRepository {

    override suspend fun getAll(): List<Team> {
        val allPlayers = playerDao.getAll().associate { it.id to it.toDomain() }
        return teamDao.getAll().map { entity ->
            val players = entity.playerIds.split(",")
                .filter { it.isNotEmpty() }
                .mapNotNull { allPlayers[it] }
            entity.toDomain(players)
        }
    }

    override suspend fun getByName(name: String): Team? {
        val entity = teamDao.getByName(name) ?: return null
        val allPlayers = playerDao.getAll().associate { it.id to it.toDomain() }
        val players = entity.playerIds.split(",")
            .filter { it.isNotEmpty() }
            .mapNotNull { allPlayers[it] }
        return entity.toDomain(players)
    }
}
