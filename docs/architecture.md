# Architecture

## Tech stack

| Layer | Library / version |
|---|---|
| Language | Kotlin 2.2.21 |
| Build | AGP 9.1.0, Gradle Kotlin DSL |
| UI | Jetpack Compose (BOM 2024.09.00) + Material 3 + Material Icons Extended |
| Navigation | Navigation Compose 2.8.4 (type-safe routes) |
| Serialization | `kotlin.plugin.serialization` (same version as Kotlin) |
| Local DB | Room 2.7.0 (entities, DAOs, `@Upsert`) |
| Camera | CameraX 1.4.0 (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`) |
| Annotation processing | KSP 2.2.21-2.0.5 (replaces KAPT for Room) |
| Min SDK | 24 · Target/Compile SDK 36 |

---

## Package layout

Clean Architecture with three layers: **domain** (pure Kotlin), **data** (implementations), **ui** (Compose).

```
com.restrusher.volymatcher
├── domain/
│   ├── model/
│   │   ├── Player.kt          — Player data class + color as Long
│   │   ├── Match.kt           — Match + MatchFormat enum
│   │   ├── Team.kt            — Team + CrestShape enum + winPercentage
│   │   └── BalancedTeams.kt   — ovrA/B + lazy draftOrder (snake draft)
│   ├── repository/
│   │   ├── PlayerRepository.kt
│   │   ├── MatchRepository.kt
│   │   └── TeamRepository.kt
│   └── usecase/
│       ├── GetPlayersUseCase.kt
│       ├── GetPlayerByIdUseCase.kt
│       ├── GetMatchesUseCase.kt
│       ├── GetMatchByIdUseCase.kt
│       ├── GetTeamsUseCase.kt
│       ├── GetTeamByNameUseCase.kt
│       └── GetBalancedTeamsUseCase.kt  — snake-draft algorithm
├── data/
│   ├── local/
│   │   ├── entity/
│   │   │   ├── PlayerEntity.kt    — Room entity (tableName = "players")
│   │   │   ├── MatchEntity.kt     — Room entity (tableName = "matches")
│   │   │   └── TeamEntity.kt      — Room entity (tableName = "teams")
│   │   ├── dao/
│   │   │   ├── PlayerDao.kt       — getAll, getById, getByIds, upsertAll
│   │   │   ├── MatchDao.kt        — getAll, getById, upsertAll
│   │   │   └── TeamDao.kt         — getAll, getByName, upsertAll
│   │   ├── database/
│   │   │   └── AppDatabase.kt     — RoomDatabase singleton (version 1)
│   │   └── mapper/
│   │       ├── PlayerMapper.kt    — PlayerEntity ↔ Player domain
│   │       ├── MatchMapper.kt     — MatchEntity ↔ Match domain
│   │       └── TeamMapper.kt      — TeamEntity ↔ Team domain
│   ├── remote/
│   │   └── dto/
│   │       ├── PlayerDto.kt       — network model + toEntity()
│   │       ├── MatchDto.kt        — network model + toEntity()
│   │       └── TeamDto.kt         — network model + toEntity()
│   ├── source/
│   │   └── SampleDataSource.kt   — preview data + first-launch DB seed
│   ├── repository/
│   │   ├── PlayerRepositoryImpl.kt
│   │   ├── MatchRepositoryImpl.kt
│   │   └── TeamRepositoryImpl.kt
│   └── di/
│       └── RepositoryLocator.kt  — manual DI singleton; call init(app) + seedIfEmpty()
├── ui/
│   ├── components/
│   │   ├── Avatar.kt
│   │   ├── BalanceMeter.kt
│   │   ├── CommonComponents.kt   — Chip, Pill, buttons, StatBar, etc.
│   │   ├── SportGlyph.kt
│   │   └── TeamCrest.kt
│   ├── navigation/
│   │   ├── Screen.kt             — @Serializable routes + BottomNavTab list
│   │   └── VolyNavGraph.kt       — NavHost + custom floating bottom bar
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── matches/
│   │   │   ├── MatchesScreen.kt
│   │   │   ├── MatchesViewModel.kt
│   │   │   ├── detail/
│   │   │   │   ├── MatchDetailScreen.kt
│   │   │   │   └── MatchDetailViewModel.kt
│   │   │   └── create/
│   │   │       ├── CreateMatchScreen.kt
│   │   │       └── CreateMatchViewModel.kt
│   │   ├── players/
│   │   │   ├── PlayersScreen.kt
│   │   │   ├── PlayersViewModel.kt
│   │   │   └── profile/
│   │   │       ├── PlayerProfileScreen.kt
│   │   │       └── PlayerProfileViewModel.kt
│   │   ├── teams/
│   │   │   ├── TeamsScreen.kt
│   │   │   ├── TeamsViewModel.kt
│   │   │   └── detail/
│   │   │       ├── TeamDetailScreen.kt
│   │   │       └── TeamDetailViewModel.kt
│   │   ├── balance/
│   │   │   ├── AutoBalanceScreen.kt
│   │   │   └── AutoBalanceViewModel.kt
│   │   ├── stats/
│   │   │   ├── StatsScreen.kt
│   │   │   └── StatsViewModel.kt
│   │   └── scan/
│   │       ├── BodyScanScreen.kt
│   │       ├── BodyScanViewModel.kt
│   │       ├── CameraViewport.kt        — live camera preview + scan overlays
│   │       ├── CameraPermissionRequest.kt — permission rationale + grant button
│   │       └── ScanStatsGrid.kt         — captured-stats card grid
│   └── theme/
│       ├── Color.kt        — palette constants
│       ├── Theme.kt        — light + dark MaterialTheme color schemes
│       └── Type.kt         — Typography + 3 font-family placeholders
└── MainActivity.kt
```

---

## Conventions

**No hardcoded colors or sizes.** All values are resolved through `MaterialTheme.colorScheme.*` and `MaterialTheme.typography.*`. This keeps every screen automatically correct for both light and dark mode without any conditional logic in screen code.

**Data layer is Compose-free.** Domain models and data sources import nothing from `androidx.compose`. Player and team colors are stored as `Long` (e.g. `0xFFFF5C2BL`) and converted to `Color(long)` at the UI boundary.

**ViewModel per feature.** Every screen has a co-located ViewModel (same package). ViewModels own `MutableStateFlow<UiState>` and expose an immutable `StateFlow`. Screens call `viewModel(factory = ...)` and observe via `collectAsStateWithLifecycle()`.

**Manual DI via `RepositoryLocator`.** `RepositoryLocator` is an `object` singleton that holds the Room `AppDatabase` and exposes lazy-initialized repositories. Call `RepositoryLocator.init(app)` from `Application.onCreate()` before accessing any repository. Replace with Hilt when ready.

**Application class — `VolyMatcherApp`.** Extends `Application` and registered in `AndroidManifest.xml`. Calls `RepositoryLocator.init(this)` on startup. DB seeding is not performed at runtime; `SampleDataSource` is used only in `@Preview` composables.

**Hero-card pattern.** Sections with a dark background in either theme use `inverseSurface` / `inverseOnSurface` instead of a hardcoded dark color. In light mode these are `Ink` / `Paper`; in dark mode they are `DarkHeroBg` / `DarkOnSurface`.

**Screen structure.** Every screen is a `LazyColumn` with `fillMaxSize().background(MaterialTheme.colorScheme.background)` at the root and `contentPadding = PaddingValues(bottom = 120.dp)` (or 40dp for modal/detail screens) to clear the floating bottom bar.

---

## Edge-to-edge & insets

`MainActivity` calls `enableEdgeToEdge()` before `setContent`. The `Scaffold` in `VolyNavGraph` passes `contentWindowInsets = WindowInsets(0)` to zero out the scaffold's default inset consumption. The floating `VolyBottomBar` then re-applies `WindowInsets.navigationBars.asPaddingValues()` itself so the pill sits above the system navigation bar. The `NavHost` uses `.padding(bottom = innerPadding.calculateBottomPadding())` to clear the bottom bar height.
