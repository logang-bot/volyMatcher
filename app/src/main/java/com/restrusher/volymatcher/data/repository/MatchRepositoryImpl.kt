package com.restrusher.volymatcher.data.repository

import com.restrusher.volymatcher.data.local.dao.MatchDao
import com.restrusher.volymatcher.data.local.mapper.toDomain
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.repository.MatchRepository

class MatchRepositoryImpl(private val dao: MatchDao) : MatchRepository {
    override suspend fun getAll(): List<Match> = dao.getAll().map { it.toDomain() }
    override suspend fun getById(id: String): Match? = dao.getById(id)?.toDomain()
}
