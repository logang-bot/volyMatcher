# Screens

11 screens total. 5 are bottom-nav roots (bottom bar visible); 6 are detail/flow screens (bottom bar hidden).

---

## Bottom-nav roots

### HomeScreen

**Route:** `HomeRoute`  
**File:** `ui/screens/home/HomeScreen.kt`

Dashboard with a hero match card, quick-action buttons, recent-matches list, and a top-players strip. Entry point to most flows.

| Callback | Navigates to |
|---|---|
| `onNavigateToMatches` | `MatchesRoute` |
| `onNavigateToTeams` | `TeamsRoute` |
| `onNavigateToBalance` | `AutoBalanceRoute` |
| `onNavigateToBodyScan` | `BodyScanRoute("p1")` |

---

### MatchesScreen

**Route:** `MatchesRoute`  
**File:** `ui/screens/matches/MatchesScreen.kt`

Scrollable list of all matches with sport glyph, status, score. Filter chips for Live / Upcoming / Finished.

| Callback | Navigates to |
|---|---|
| `onMatchClick(id)` | `MatchDetailRoute(matchId)` |
| `onCreateMatch` | `CreateMatchRoute` |

---

### PlayersScreen

**Route:** `PlayersRoute`  
**File:** `ui/screens/players/PlayersScreen.kt`

Player roster with avatar, name, role, and skill rating. Header shows squad totals. Includes a body-scan entry point.

| Callback | Navigates to |
|---|---|
| `onPlayerClick(id)` | `PlayerProfileRoute(playerId)` |
| `onScan` | `BodyScanRoute("p2")` |

---

### AutoBalanceScreen

**Route:** `AutoBalanceRoute`  
**File:** `ui/screens/balance/AutoBalanceScreen.kt`

Balanced team result view with a `BalanceMeter`, two team cards side-by-side, delta row, attribute weight sliders, and a draft-order list.

| Callback | Navigates to |
|---|---|
| `onBack` | `popBackStack()` |

---

### StatsScreen

**Route:** `StatsRoute`  
**File:** `ui/screens/stats/StatsScreen.kt`

Standings page with scope chips (This month / All time / This squad), a top-3 podium, a tabbed leaderboard, and a team standings table.

| Callback | Navigates to |
|---|---|
| `onPlayerClick(id)` | `PlayerProfileRoute(playerId)` |

---

## Detail / flow screens

### MatchDetailScreen

**Route:** `MatchDetailRoute(matchId: String)`  
**File:** `ui/screens/matches/detail/MatchDetailScreen.kt`

Full match detail: hero score card, team rosters, timeline / set scores. No bottom bar.

| Callback | Navigates to |
|---|---|
| `onBack` | `popBackStack()` |

---

### CreateMatchScreen

**Route:** `CreateMatchRoute`  
**File:** `ui/screens/matches/create/CreateMatchScreen.kt`

Multi-step match creation form (sport, format, venue, date, players). No bottom bar.

| Callback | Navigates to |
|---|---|
| `onBack` | `popBackStack()` |

---

### TeamsScreen

**Route:** `TeamsRoute`  
**File:** `ui/screens/teams/TeamsScreen.kt`

Grid of team crest cards with win/loss records. Reached from the Home quick-action. No bottom bar.

| Callback | Navigates to |
|---|---|
| `onTeamClick(teamId)` | `TeamDetailRoute(teamId)` |

---

### TeamDetailScreen

**Route:** `TeamDetailRoute(teamId: String)`  
**File:** `ui/screens/teams/detail/TeamDetailScreen.kt`

Team hero card, attribute comparison bars, player roster list. No bottom bar.

| Callback | Navigates to |
|---|---|
| `onBack` | `popBackStack()` |
| `onPlayerClick(id)` | `PlayerProfileRoute(playerId)` |

---

### PlayerProfileScreen

**Route:** `PlayerProfileRoute(playerId: String)`  
**File:** `ui/screens/players/profile/PlayerProfileScreen.kt`

Player hero card with avatar (ring variant), all attributes as `StatBar` rows, recent matches, and a body-scan CTA. No bottom bar.

| Callback | Navigates to |
|---|---|
| `onBack` | `popBackStack()` |
| `onScan` | `BodyScanRoute(playerId)` |

---

### BodyScanScreen

**Route:** `BodyScanRoute(playerId: String)`  
**File:** `ui/screens/scan/BodyScanScreen.kt`

Simulated camera body-scan UI with a dark hero background (`inverseSurface`). Includes a scan grid Canvas overlay, body silhouette path, scanning sweep line, live readout overlays, and a captured-stats grid. No bottom bar.

| Callback | Navigates to |
|---|---|
| `onBack` | `popBackStack()` |

**Note:** This screen uses `inverseSurface` / `inverseOnSurface` for its dark camera viewport, matching the hero-card pattern from the design.
