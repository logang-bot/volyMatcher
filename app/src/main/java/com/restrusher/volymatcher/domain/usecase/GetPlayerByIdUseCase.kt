package com.restrusher.volymatcher.domain.usecase

import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.repository.PlayerRepository

class GetPlayerByIdUseCase(private val repository: PlayerRepository) {
    suspend operator fun invoke(id: String): Player? = repository.getById(id)
}
