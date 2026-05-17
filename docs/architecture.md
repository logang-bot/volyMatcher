# Architecture

## Tech stack

| Layer | Library / version |
|---|---|
| Language | Kotlin 2.2.10 |
| Build | AGP 9.1.0, Gradle Kotlin DSL |
| UI | Jetpack Compose (BOM 2024.09.00) + Material 3 |
| Navigation | Navigation Compose 2.8.4 (type-safe routes) |
| Serialization | `kotlin.plugin.serialization` (same version as Kotlin) |
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
│   ├── source/
│   │   └── SampleDataSource.kt  — hard-coded players, matches, teams
│   ├── repository/
│   │   ├── PlayerRepositoryImpl.kt
│   │   ├── MatchRepositoryImpl.kt
│   │   └── TeamRepositoryImpl.kt
│   └── di/
│       └── RepositoryLocator.kt  — manual DI singleton (replaces Hilt)
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
│   │       └── BodyScanViewModel.kt
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

**Manual DI via `RepositoryLocator`.** `RepositoryLocator` is an `object` singleton that instantiates each `RepositoryImpl` once. ViewModel companion factories reference it. Replace with Hilt when ready.

**Hero-card pattern.** Sections with a dark background in either theme use `inverseSurface` / `inverseOnSurface` instead of a hardcoded dark color. In light mode these are `Ink` / `Paper`; in dark mode they are `DarkHeroBg` / `DarkOnSurface`.

**Screen structure.** Every screen is a `LazyColumn` with `fillMaxSize().background(MaterialTheme.colorScheme.background)` at the root and `contentPadding = PaddingValues(bottom = 120.dp)` (or 40dp for modal/detail screens) to clear the floating bottom bar.

---

## Edge-to-edge & insets

`MainActivity` calls `enableEdgeToEdge()` before `setContent`. The `Scaffold` in `VolyNavGraph` passes `contentWindowInsets = WindowInsets(0)` to zero out the scaffold's default inset consumption. The floating `VolyBottomBar` then re-applies `WindowInsets.navigationBars.asPaddingValues()` itself so the pill sits above the system navigation bar. The `NavHost` uses `.padding(bottom = innerPadding.calculateBottomPadding())` to clear the bottom bar height.
