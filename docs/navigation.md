# Navigation

## Overview

Navigation uses Jetpack Navigation Compose with **type-safe serializable routes**. All routes are defined as `@Serializable` objects or data classes in `Screen.kt`. The entire graph is wired in a single composable: `VolyNavGraph.kt`.

Dependencies required: `navigation-compose` 2.8+ and `kotlin.plugin.serialization`.

---

## Route Definitions (`Screen.kt`)

| Route | Type | Parameters |
|---|---|---|
| `HomeRoute` | `data object` | — |
| `MatchesRoute` | `data object` | — |
| `PlayersRoute` | `data object` | — |
| `AutoBalanceRoute` | `data object` | — |
| `StatsRoute` | `data object` | — |
| `CreateMatchRoute` | `data object` | — |
| `TeamsRoute` | `data object` | — |
| `MatchDetailRoute` | `data class` | `matchId: String` |
| `TeamDetailRoute` | `data class` | `teamId: String` |
| `PlayerProfileRoute` | `data class` | `playerId: String` |
| `BodyScanRoute` | `data class` | `playerId: String?` — null when scanning a new player |

---

## Navigation Flow

```
HomeRoute  (bottom-nav root)
    ├─► MatchesRoute  (bottom-nav root)
    │       ├─► MatchDetailRoute(matchId)
    │       └─► CreateMatchRoute
    ├─► PlayersRoute  (bottom-nav root)
    │       ├─► BodyScanRoute(null)          ← new-player scan
    │       └─► PlayerProfileRoute(playerId)
    │               └─► BodyScanRoute(playerId)
    ├─► AutoBalanceRoute  (bottom-nav root — primary tab)
    ├─► StatsRoute  (bottom-nav root)
    │       └─► PlayerProfileRoute(playerId)
    ├─► TeamsRoute  (reached from HomeRoute quick-action)
    │       └─► TeamDetailRoute(teamId)
    │               └─► PlayerProfileRoute(playerId)
    │                       └─► BodyScanRoute(playerId)
    └─► BodyScanRoute(null)                  ← new-player scan from Home quick-action
```

---

## Scaffold & Bottom Bar

`VolyNavGraph` wraps the entire graph in a `Scaffold` with `contentWindowInsets = WindowInsets(0)` (zero out Scaffold's default inset consumption). The custom floating `VolyBottomBar` handles its own navigation-bar inset via `WindowInsets.navigationBars.asPaddingValues()`.

The bottom bar is only rendered for the five root destinations. Detection uses `NavDestination.hierarchy` so the bar stays visible when the back stack contains a root as an ancestor:

```kotlin
val isBottomNavScreen = bottomNavTabs.any { isRouteActive(it.routeClass) }
if (!isBottomNavScreen) return
```

#### Visual design

The bar is a rounded-pill `Row` (`RoundedCornerShape(22.dp)`) with a subtle `shadow(4.dp)` and `outlineVariant` border. A `Box` wraps the pill and the primary FAB together:

- The pill has `padding(top = fabHalfSize)` so its top edge aligns with the FAB's center.
- The primary FAB (`52.dp` circle) is positioned at `Alignment.TopCenter` of the outer Box, making it protrude above the bar.
- The FAB's orange glow is rendered via `drawBehind` with four concentric semi-transparent circles — no `BlurMaskFilter` required, works on all API levels.
- Regular tabs use `Icons.Rounded` (Home, GridView, Group, BarChart); the primary FAB uses `Icons.Rounded.Add`.

---

## Bottom-Nav Tabs (`Screen.kt`)

| Tab | Route | Primary |
|---|---|---|
| Home | `HomeRoute` | — |
| Matches | `MatchesRoute` | — |
| Balance | `AutoBalanceRoute` | ✓ (elevated circle button) |
| Players | `PlayersRoute` | — |
| Stats | `StatsRoute` | — |

Tab navigation uses `saveState / restoreState` so each root's scroll position and VM state survive tab switches. `popUpTo` uses the type-safe route object (not the numeric `.id`) to avoid ID resolution issues with serializable routes:

```kotlin
navController.navigate(tab.routeInstance) {
    popUpTo(HomeRoute) { saveState = true }
    launchSingleTop = true
    restoreState = true
}
```

---

## Reading Route Arguments

Arguments are decoded with `NavBackStackEntry.toRoute<T>()`:

```kotlin
composable<PlayerProfileRoute> { backStack ->
    val route = backStack.toRoute<PlayerProfileRoute>()
    PlayerProfileScreen(playerId = route.playerId, ...)
}
```

---

## Back Stack

- All detail screens call `navController.popBackStack()` via their `onBack` lambda.
- No inclusive pop is currently needed; all flows are simple push/pop.
