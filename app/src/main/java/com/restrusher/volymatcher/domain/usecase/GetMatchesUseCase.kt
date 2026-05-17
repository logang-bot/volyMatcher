package com.restrusher.volymatcher.domain.usecase

import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.repository.MatchRepository

class GetMatchesUseCase(private val repository: MatchRepository) {
    operator fun invoke(): List<Match> = repository.getAll()
}
