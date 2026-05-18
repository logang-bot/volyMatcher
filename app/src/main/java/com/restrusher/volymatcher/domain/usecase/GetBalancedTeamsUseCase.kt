package com.restrusher.volymatcher.domain.usecase

import com.restrusher.volymatcher.domain.model.BalancedTeams
import com.restrusher.volymatcher.domain.repository.PlayerRepository
import kotlin.math.abs

class GetBalancedTeamsUseCase(private val repository: PlayerRepository) {
    suspend operator fun invoke(): BalancedTeams {
        val sorted = repository.getAll().sortedByDescending { it.skill }
        // Snake draft: even-index picks go to A, odd to B — produces balanced skill totals
        val teamA = sorted.filterIndexed { i, _ -> i % 2 == 0 }
        val teamB = sorted.filterIndexed { i, _ -> i % 2 == 1 }
        val sumA = teamA.sumOf { it.skill }
        val sumB = teamB.sumOf { it.skill }
        val total = sumA + sumB
        val score = if (total > 0) (100 - abs(sumA - sumB) * 100 / total).coerceIn(0, 100) else 100
        return BalancedTeams(teamA, teamB, score)
    }
}
