# Data Layer

## Three-model pattern

Each entity has three representations:

| Layer | Model | Package | Purpose |
|---|---|---|---|
| Network | `PlayerDto`, `MatchDto`, `TeamDto` | `data/remote/dto/` | JSON deserialization (future API) |
| Local DB | `PlayerEntity`, `MatchEntity`, `TeamEntity` | `data/local/entity/` | Room storage |
| Domain/UI | `Player`, `Match`, `Team` | `domain/model/` | Used by use cases and ViewModels |

Mapping direction: **DTO → Entity** (via `toEntity()` extension on the DTO), **Entity → Domain** (via `toDomain()` extension on the entity). Domain models never import Room or networking code.

---

## Domain models

### Player

```kotlin
data class Player(
    val id: String,          // unique identifier used for navigation args
    val name: String,        // full name, e.g. "Leo Varga"
    val nick: String,        // short display name, e.g. "Leo"
    val colorLong: Long,     // avatar background color as 0xFFRRGGBBL
    val initials: String,    // 2-char initials drawn inside avatar
    val height: Int,         // cm
    val weight: Int,         // kg
    val reach: Int,          // wingspan in cm
    val jump: Int,           // vertical jump in cm
    val speed: Int,          // 0–100
    val skill: Int,          // overall rating 0–100
    val hand: String,        // "R" | "L"
    val role: String,        // position label, e.g. "Setter", "Libero"
)
```

**Color convention:** `colorLong` is stored as a `Long` so the data layer has zero Compose dependency. Convert at the UI boundary: `val color = Color(player.colorLong)`.

---

### Match

```kotlin
data class Match(
    val id: String,
    val sport: String,        // "Volleyball" | "Basketball" | "Soccer" | …
    val title: String,        // display name, e.g. "Tuesday Pickup"
    val format: MatchFormat,
    val date: String,         // human-readable, e.g. "Tue, Apr 14"
    val venue: String,
    val status: String,       // "Live" | "Upcoming" | "Final"
    val score: String?,       // e.g. "2 — 1" — present for finished official matches
    val teamA: String?,
    val teamB: String?,
    val winner: String?,      // winner name — present for Battle Royale or finished official
    val teamCount: Int?,      // number of teams (Battle Royale only)
    val balance: Int,         // balance score 0–100
)

enum class MatchFormat { Official, BattleRoyale }
```

---

### Team

```kotlin
data class Team(
    val name: String,
    val colorLong: Long,
    val shape: CrestShape,
    val letter: Char,
    val players: List<Player>,
    val wins: Int,
    val losses: Int,
    val overallRating: Int,
    val sport: String = "",
    val createdAt: String = "",
    val setDiff: Int = 0,
    val recentResults: List<String> = emptyList(),  // e.g. ["W", "W", "L"]
) {
    val winPercentage: Float   // computed: wins / (wins + losses).coerceAtLeast(1)
}

enum class CrestShape { Shield, Disc, Diamond, Blob }
```

`Team.players` is stored in Room as comma-separated player IDs (`TeamEntity.playerIds`). `TeamRepositoryImpl` re-joins them via `PlayerDao`.

---

### BalancedTeams

```kotlin
data class BalancedTeams(
    val teamA: List<Player>,
    val teamB: List<Player>,
    val balanceScore: Int,
) {
    val ovrA: Int        // average skill of team A
    val ovrB: Int        // average skill of team B
    val reachDelta: Int  // avgReach(A) − avgReach(B)
    val jumpDelta: Int   // avgJump(A) − avgJump(B)
    val draftOrder: List<Pair<String, Player>>  // snake-draft sequence
}
```

---

## Room entities

`PlayerEntity`, `MatchEntity`, `TeamEntity` mirror the domain model fields with two differences:

- Enum fields (`role`, `hand`, `shape`, `format`) are stored as `String` and parsed safely in mappers via `runCatching { Enum.valueOf(...) }.getOrDefault(...)`.
- `TeamEntity.playerIds` is a comma-separated `String` of player IDs; `TeamEntity.recentResults` is a comma-separated `String` of result labels.

---

## SampleDataSource

`data/source/SampleDataSource.kt` is used exclusively for **`@Preview` data** — imported by `*Preview` composables so Android Studio can render screens without a running device.

`SampleDataSource` is **not** referenced by any `RepositoryImpl` and is **not** inserted into the Room database at runtime. The app operates entirely on data persisted via Room; screens that receive no data display empty states rather than fallback sample content.

#### Enabling first-launch seeding

`RepositoryLocator` exposes a `seedIfEmpty()` suspend function that inserts all sample players, matches, and teams if the `players` table is empty. To activate it on first install, call it from `VolyMatcherApp.onCreate()`:

```kotlin
// VolyMatcherApp.kt
override fun onCreate() {
    super.onCreate()
    RepositoryLocator.init(this)
    CoroutineScope(Dispatchers.IO).launch {
        RepositoryLocator.seedIfEmpty()
    }
}
```

Required import: `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.launch`.

`seedIfEmpty()` is idempotent — it checks row count before inserting, so calling it on every launch is safe.

---

## Conventions

- **No IDs on Team** — teams are identified by `name`. Navigation to `TeamDetailScreen` passes `team.name` as the argument.
- **`reach` vs wingspan** — `Player.reach` stores wingspan in cm; it is labelled "REACH" or "WINGSPAN" depending on context.
- **`balance` on Match** — integer 0–100 passed to `BalanceMeter` to show how evenly teams were split.
- **All repository methods are `suspend`** — use cases are also `suspend`. ViewModels call them inside `viewModelScope.launch { }`.
