package com.restrusher.volymatcher.domain.repository

import com.restrusher.volymatcher.domain.model.Match

interface MatchRepository {
    fun getAll(): List<Match>
    fun getById(id: String): Match?
}
