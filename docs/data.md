# Data Layer

## Files

| File | Contents |
|---|---|
| `data/Models.kt` | Data classes and enums — no Compose imports |
| `data/SampleData.kt` | `samplePlayers` list, `sampleMatches` list, `sampleTeams()` function |

---

## Models

### Player

```kotlin
data class Player(
    val id: String,          // unique identifier used for navigation args
    val name: String,        // full name, e.g. "Carlos Mendez"
    val nick: String,        // short display name, e.g. "CMEN"
    val colorLong: Long,     // avatar background color as 0xFFRRGGBBL
    val initials: String,    // 2-char initials drawn inside avatar
    val height: Int,         // cm
    val weight: Int,         // kg
    val reach: Int,          // wingspan in cm
    val jump: Int,           // vertical jump in cm
    val speed: Int,          // 0–100
    val skill: Int,          // overall rating 0–100
    val hand: String,        // "Right" | "Left"
    val role: String,        // position label, e.g. "Setter", "Libero"
)
```

**Color convention:** `colorLong` is stored as a `Long` (not `Color`) so the data layer has zero Compose dependency. Convert at the UI boundary:
```kotlin
val color = Color(player.colorLong)
```

---

### Match

```kotlin
data class Match(
    val id: String,
    val sport: String,        // "volleyball" | "basketball" | "soccer" | …
    val title: String,        // display name, e.g. "Friday Night 6v6"
    val format: MatchFormat,
    val date: String,         // human-readable, e.g. "Apr 19"
    val venue: String,
    val status: String,       // "Live" | "Upcoming" | "Finished"
    val score: String?,       // e.g. "25–21" — present when finished
    val teamA: String?,
    val teamB: String?,
    val winner: String?,      // team name of the winner
    val teamCount: Int?,      // number of teams (Battle Royale)
    val balance: Int,         // balance score 0–100
)

enum class MatchFormat { Official, BattleRoyale }
```

---

### Team

```kotlin
data class Team(
    val name: String,
    val colorLong: Long,       // same color convention as Player
    val shape: CrestShape,
    val letter: Char,          // single letter shown in TeamCrest
    val players: List<Player>,
    val wins: Int,
    val losses: Int,
    val overallRating: Int,
) {
    val winPercentage: Float   // computed: wins / (wins + losses)
}

enum class CrestShape { Shield, Disc, Diamond, Blob }
```

---

## Sample data

`samplePlayers` — 12 players covering all roles (Setter, Outside, Libero, Middle, Opposite, Universal). Each has a distinct avatar color.

`sampleMatches` — 5 matches across formats (Official + BattleRoyale) and statuses (Live, Upcoming, Finished).

`sampleTeams(players: List<Player>)` — function that builds 4 teams by selecting from the player list. Returns teams pre-sorted by the callers where needed (e.g. `sortedByDescending { it.winPercentage }`).

---

## Conventions

- **No IDs on Team** — teams are identified by name in the current prototype. If navigation to `TeamDetailRoute(teamId)` is needed, `teamId` is matched against `team.name`.
- **Reach vs wingspan** — `Player.reach` is labelled "REACH" in UI but stores wingspan in cm.
- **`balance` on Match** — integer 0–100 used by `BalanceMeter` to show how evenly the teams were split.
