package com.restrusher.volymatcher.domain.usecase

import com.restrusher.volymatcher.domain.model.Team
import com.restrusher.volymatcher.domain.repository.TeamRepository

class GetTeamByNameUseCase(private val repository: TeamRepository) {
    suspend operator fun invoke(name: String): Team? = repository.getByName(name)
}
