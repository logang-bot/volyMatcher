package com.restrusher.volymatcher.domain.usecase

import com.restrusher.volymatcher.domain.model.Team
import com.restrusher.volymatcher.domain.repository.TeamRepository

class GetTeamsUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(): List<Team> = repository.getAll()
}
