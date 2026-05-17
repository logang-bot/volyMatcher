package com.restrusher.volymatcher.data.source

import com.restrusher.volymatcher.domain.model.CrestShape
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.MatchFormat
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.model.Team

object SampleDataSource {

    val players: List<Player> = listOf(
        Player("p1",  "Leo Varga",     "Leo",   0xFFFF5C2BL, "LV", 188, 82, 243, 61, 82, 78, "R", "Outside"),
        Player("p2",  "Maya Chen",     "Maya",  0xFFC7E84AL, "MC", 174, 63, 221, 58, 88, 84, "R", "Setter"),
        Player("p3",  "Diego Ruiz",    "Diego", 0xFF9FC6E8L, "DR", 192, 88, 251, 65, 74, 71, "L", "Middle"),
        Player("p4",  "Priya Shah",    "Pri",   0xFFC86A4AL, "PS", 168, 59, 215, 54, 91, 80, "R", "Libero"),
        Player("p5",  "Tomás Brandt",  "Tommy", 0xFF1A1613L, "TB", 183, 79, 236, 56, 79, 75, "R", "Opposite"),
        Player("p6",  "Nia Okafor",    "Nia",   0xFFFF8A5BL, "NO", 179, 70, 228, 62, 86, 82, "R", "Outside"),
        Player("p7",  "Kai Johansson", "Kai",   0xFF5A8A4AL, "KJ", 196, 91, 254, 67, 71, 69, "R", "Middle"),
        Player("p8",  "Elena Rossi",   "Ellie", 0xFFB84A8AL, "ER", 171, 61, 219, 55, 84, 77, "R", "Setter"),
        Player("p9",  "Jamal Park",    "Jamal", 0xFF4A6A8AL, "JP", 185, 80, 239, 59, 81, 73, "R", "Outside"),
        Player("p10", "Sara Khalil",   "Sar",   0xFFE0C24AL, "SK", 176, 66, 224, 57, 87, 79, "L", "Opposite"),
        Player("p11", "Luca Moretti",  "Luc",   0xFF7A4AB8L, "LM", 190, 85, 246, 63, 76, 72, "R", "Middle"),
        Player("p12", "Aisha Bello",   "Aish",  0xFF4AB89AL, "AB", 173, 62, 220, 56, 89, 81, "R", "Libero"),
    )

    val matches: List<Match> = listOf(
        Match("m1", "Volleyball", "Tuesday Pickup", MatchFormat.Official, "Tue, Apr 14",
            "Parque Norte · Court 3", "Final", score = "2 — 1",
            teamA = "Orange Crush", teamB = "Cream Wolves", balance = 97),
        Match("m2", "Soccer", "Barrio 5-a-side", MatchFormat.BattleRoyale, "Sat, Apr 11",
            "Liberty Field", "Final", winner = "The Pigeons", teamCount = 4, balance = 92),
        Match("m3", "Basketball", "3v3 King of Court", MatchFormat.BattleRoyale, "Fri, Apr 10",
            "Fulton Playground", "Final", winner = "Short Kings", teamCount = 3, balance = 88),
        Match("m4", "Volleyball", "Office Rematch", MatchFormat.Official, "Wed, Apr 8",
            "Gym · Bay 2", "Final", score = "2 — 0",
            teamA = "Spike Club", teamB = "Net Losses", balance = 94),
        Match("m5", "Volleyball", "Friday Night Jam", MatchFormat.BattleRoyale, "Fri, Apr 4",
            "Parque Norte · Court 1", "Final", winner = "Orange Crush", teamCount = 3, balance = 91),
    )

    fun teams(players: List<Player> = this.players): List<Team> = listOf(
        Team("Orange Crush", 0xFFFF5C2BL, CrestShape.Shield,  'O', players.slice(0..5),  8, 2, 79),
        Team("Cream Wolves", 0xFF1A1613L, CrestShape.Disc,    'W', players.slice(6..11), 6, 3, 76),
        Team("Spike Club",   0xFFC7E84AL, CrestShape.Diamond, 'S',
            listOf(players[1], players[3], players[5], players[7]), 4, 1, 81),
        Team("Net Losses",   0xFF9FC6E8L, CrestShape.Blob,    'N',
            listOf(players[2], players[6], players[10]), 2, 5, 71),
    )
}
