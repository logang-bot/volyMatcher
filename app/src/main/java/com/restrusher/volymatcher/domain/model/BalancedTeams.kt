package com.restrusher.volymatcher.domain.model

data class BalancedTeams(
    val teamA: List<Player>,
    val teamB: List<Player>,
    val balanceScore: Int,
) {
    val ovrA: Int get() = if (teamA.isEmpty()) 0 else teamA.sumOf { it.skill } / teamA.size
    val ovrB: Int get() = if (teamB.isEmpty()) 0 else teamB.sumOf { it.skill } / teamB.size

    val reachDelta: Int get() {
        val avgA = if (teamA.isEmpty()) 0 else teamA.sumOf { it.reach } / teamA.size
        val avgB = if (teamB.isEmpty()) 0 else teamB.sumOf { it.reach } / teamB.size
        return avgA - avgB
    }

    val jumpDelta: Int get() {
        val avgA = if (teamA.isEmpty()) 0 else teamA.sumOf { it.jump } / teamA.size
        val avgB = if (teamB.isEmpty()) 0 else teamB.sumOf { it.jump } / teamB.size
        return avgA - avgB
    }

    /** Snake-draft order: pick 1 goes to A, 2 to B, 3 to B, 4 to A, … */
    val draftOrder: List<Pair<String, Player>> by lazy {
        val allSorted = (teamA + teamB).sortedByDescending { it.skill }
        allSorted.mapIndexed { index, player ->
            val team = when (index % 4) {
                0, 3 -> "A"
                else -> "B"
            }
            team to player
        }
    }
}
