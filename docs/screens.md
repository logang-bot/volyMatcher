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
| `onNavigateToBodyScan` | `BodyScanRoute(null)` — new-player scan |

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
| `onScan` | `BodyScanRoute(null)` — new-player scan |

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

**Route:** `BodyScanRoute(playerId: String?)`  
**Files:** `ui/screens/scan/` — `BodyScanScreen.kt`, `BodyScanViewModel.kt`, `CameraViewport.kt`, `CameraPermissionRequest.kt`, `ScanStatsGrid.kt`, `ScanMeasurement.kt`, `PoseAnalyzer.kt`

Live camera body-scan screen. Uses CameraX (`PreviewView` + `ImageAnalysis`) for the camera feed, runs ML Kit Pose Detection on each frame, and extracts physical measurements drawn as overlays. No bottom bar.

| Callback | Navigates to |
|---|---|
| `onBack` | `popBackStack()` |

#### Permission flow

`BodyScanScreen` checks `CAMERA` permission on composition and immediately launches the system dialog if not yet granted. The result is stored as `hasCameraPermission: Boolean` and threaded into `BodyScanContent`.

- **Permission granted →** `CameraViewport` is shown. `Preview` + `ImageAnalysis` are both bound to the lifecycle in a single `DisposableEffect`; they unbind and the `PoseDetector`/executor are closed on disposal.
- **Permission denied →** `CameraPermissionRequest` fills the viewport slot — explains the purpose of the camera and provides a "Grant camera access" button to re-trigger the dialog.

#### Pose detection pipeline

`PoseAnalyzer` implements `ImageAnalysis.Analyzer`. On each frame it calls `PoseDetector.process()` (ML Kit, `STREAM_MODE`) and forwards the result to `BodyScanViewModel.onPoseResult(pose, imageWidth, imageHeight, rotationDegrees)`.

The ViewModel normalizes all landmark positions to viewport [0, 1] space (accounting for camera rotation) and computes:

| Stat | Method |
|---|---|
| `height` | Head-top Y → lowest heel/foot Y, scaled by shoulder-width ruler |
| `reach` | Left wrist X → right wrist X when arms are outstretched |
| `jump` | Hip Y displacement above a 30-frame standing baseline |

**Scale calibration:** average adult shoulder width (45 cm) in normalized pixel space serves as the ruler. All measurements derive from `value_norm / (shoulderWidthNorm / 45)`, making them distance-independent.

`weight` and `hand` cannot be derived from pose — they are pre-filled from the existing `Player` if available, otherwise shown as "scanning" pending future user input.

#### Composable breakdown

| Composable | File | Role |
|---|---|---|
| `BodyScanScreen` | `BodyScanScreen.kt` | Entry point; manages permission state |
| `BodyScanContent` | `BodyScanScreen.kt` | Orchestrates the full-screen `LazyColumn` layout; wires `onPoseResult` and live progress |
| `CameraViewport` | `CameraViewport.kt` | Camera preview + `ImageAnalysis` binding + scan overlays (grid, animated scan line, corner brackets, live readouts) |
| `PoseSkeletonOverlay` | `CameraViewport.kt` | Canvas overlay drawing detected skeleton joints and bones; replaces static silhouette once a person is detected |
| `BodySilhouetteOverlay` | `CameraViewport.kt` | Static guide silhouette shown before a person is detected |
| `CameraPermissionRequest` | `CameraPermissionRequest.kt` | Rationale card shown when permission is denied |
| `ScanStatsGrid` | `ScanStatsGrid.kt` | 2-column grid; cards flip to confirmed (✓) as each measurement is captured live |
| `PoseAnalyzer` | `PoseAnalyzer.kt` | `ImageAnalysis.Analyzer` — feeds frames to ML Kit `PoseDetector` |
| `ScanMeasurement` | `ScanMeasurement.kt` | `NormalizedLandmark` + `ScanMeasurement` data classes held in `BodyScanUiState` |

#### `playerId` semantics

`playerId` is `String?`. When `null` (entry from Home or Players quick-action) the scan starts with no pre-loaded player — `weight` and `hand` cards display "scanning". When non-null (entry from `PlayerProfileScreen`) the ViewModel fetches the existing player and pre-fills those two cards; the camera-derived stats (`height`, `reach`, `jump`) are always captured fresh.
