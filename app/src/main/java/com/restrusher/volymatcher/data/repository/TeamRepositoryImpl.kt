package com.restrusher.volymatcher.data.repository

import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.domain.model.Team
import com.restrusher.volymatcher.domain.repository.TeamRepository

class TeamRepositoryImpl : TeamRepository {
    override fun getAll(): List<Team> = SampleDataSource.teams()
    override fun getByName(name: String): Team? = SampleDataSource.teams().firstOrNull { it.name == name }
}
