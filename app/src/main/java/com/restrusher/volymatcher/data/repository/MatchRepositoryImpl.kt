package com.restrusher.volymatcher.data.repository

import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.repository.MatchRepository

class MatchRepositoryImpl : MatchRepository {
    override fun getAll(): List<Match> = SampleDataSource.matches
    override fun getById(id: String): Match? = SampleDataSource.matches.firstOrNull { it.id == id }
}
