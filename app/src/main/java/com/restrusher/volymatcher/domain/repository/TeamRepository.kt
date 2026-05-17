package com.restrusher.volymatcher.domain.repository

import com.restrusher.volymatcher.domain.model.Team

interface TeamRepository {
    fun getAll(): List<Team>
    fun getByName(name: String): Team?
}
