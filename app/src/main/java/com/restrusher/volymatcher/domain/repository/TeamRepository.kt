package com.restrusher.volymatcher.domain.repository

import com.restrusher.volymatcher.domain.model.Team

interface TeamRepository {
    suspend fun getAll(): List<Team>
    suspend fun getByName(name: String): Team?
}
