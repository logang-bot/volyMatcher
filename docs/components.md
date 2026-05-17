# UI Components

All components live in `ui/components/`. They accept only `MaterialTheme` tokens — no hardcoded colors or sizes.

---

## Avatar

**File:** `Avatar.kt`

Circular player avatar with a diagonal-stripe pattern drawn via `drawBehind`. The stripe color is derived from `Player.colorLong`; text color is computed from luminance so it stays legible on both light and dark player colors.

```kotlin
Avatar(player = player, size = 40.dp, showRing = false)
```

| Parameter | Default | Description |
|---|---|---|
| `player` | — | Provides `colorLong` and `initials` |
| `size` | `40.dp` | Diameter of the circle |
| `showRing` | `false` | Draws a two-layer ring (ink + surface) for hero avatars |

**Color logic:** base fill is `playerColor` darkened by 12 %; diagonal stripes are `playerColor` at full opacity. Text uses `onBackground` when luminance > 0.35, otherwise `inverseOnSurface`.

---

## TeamCrest

**File:** `TeamCrest.kt`

Canvas-drawn team badge in one of four shapes, with the team letter centered inside.

```kotlin
TeamCrest(letter = 'S', shape = CrestShape.Shield, color = accentColor, size = 36.dp)
```

| Shape | Geometry |
|---|---|
| `Shield` | Pointed-bottom shield with shoulder curves |
| `Disc` | Full circle |
| `Diamond` | Four-point polygon |
| `Blob` | Cubic bézier organic shape |

All paths are defined in a 44×44 viewport and scaled to `size` at draw time with `canvas.scale()`. The letter is drawn with `rememberTextMeasurer()` + `drawText()`.

---

## BalanceMeter

**File:** `BalanceMeter.kt`

Circular arc progress indicator for team balance scores.

```kotlin
BalanceMeter(value = 97, size = 72.dp, strokeWidth = 6.dp, showLabel = true)
```

| Value range | Arc color |
|---|---|
| ≥ 90 | `secondary` (lime) |
| 75–89 | `primary` (accent orange) |
| < 75 | `tertiary` (clay) |

The arc sweeps from the bottom-left (225°) through 270° × (value / 100). A percentage label is drawn in the center using `displaySmall` style.

---

## SportGlyph

**File:** `SportGlyph.kt`

Vector icons for sports, built as `ImageVector` via `ImageVector.Builder` (no drawable resources).

```kotlin
SportGlyph(sport = "volleyball", size = 18.dp)
```

Recognized `sport` strings: `"volleyball"`, `"basketball"`, `"soccer"`. Any other value renders a generic circle glyph.

---

## Common components (`CommonComponents.kt`)

### Chip

Filter / scope selector pill.

```kotlin
Chip(label = "This month", active = true, onClick = { ... })
```

Active state: `onBackground` fill, `background` text. Inactive state: transparent fill, `outlineVariant` border.

---

### Pill

Non-interactive badge / tag. Text is auto-uppercased.

```kotlin
Pill(text = "Live", tone = PillTone.Accent)
```

| `PillTone` | Background | Text |
|---|---|---|
| `Ink` | `onBackground` | `background` |
| `Accent` | `primary` | White |
| `Outline` | Transparent | `onBackground` (+ `outlineVariant` border) |
| `Lime` | `secondary` | `onSecondary` |
| `Ghost` | `onBackground` at 6 % alpha | `onBackground` |

---

### PrimaryButton / InkButton

Full-width CTA buttons with `RoundedCornerShape(14.dp)`.

```kotlin
PrimaryButton(text = "Create match", onClick = { ... })  // orange
InkButton(text = "Lock it in", onClick = { ... })        // ink / dark
```

---

### StatBar

Mono-labelled progress bar for player attribute display.

```kotlin
StatBar(label = "SKILL", value = 82, max = 100, barColor = MaterialTheme.colorScheme.primary)
```

Layout: `[label 52dp] [bar weight=1] [value 32dp]`. Bar uses `outlineVariant` as track.

---

### SectionHeader

Row with a `titleLarge` title and an optional `primary`-colored action link.

```kotlin
SectionHeader(title = "Players", action = "See all", onActionClick = { ... })
```

---

### MatchMiniCard

Compact match summary card for the home dashboard. Shows sport glyph, match title/format/date, and either score or winner.

```kotlin
MatchMiniCard(match = match)
```

---

### WeightSlider

Static visual slider (display only — no interaction) for the auto-balance weight configuration.

```kotlin
WeightSlider(label = "Skill rating", value = 40)
```

Renders label + percentage on top, filled progress bar below. Fill uses `primary`, track uses `outlineVariant`.
