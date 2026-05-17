package com.restrusher.volymatcher.domain.usecase

import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.repository.PlayerRepository

class GetPlayersUseCase(private val repository: PlayerRepository) {
    operator fun invoke(): List<Player> = repository.getAll()
}
