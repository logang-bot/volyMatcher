package com.restrusher.volymatcher.domain.repository

import com.restrusher.volymatcher.domain.model.Match

interface MatchRepository {
    suspend fun getAll(): List<Match>
    suspend fun getById(id: String): Match?
}
