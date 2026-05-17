# Theme

## Files

| File | Responsibility |
|---|---|
| `Color.kt` | Raw color constants (palette) |
| `Type.kt` | Font families + M3 `Typography` object |
| `Theme.kt` | `LightColorScheme`, `DarkColorScheme`, `VolyMatcherTheme` composable |

---

## Color palette

### Design token → Compose role mapping

| Design name | Hex (light) | `MaterialTheme.colorScheme` role | Notes |
|---|---|---|---|
| `cream` | `#F4F1EA` | `background` / `surfaceVariant` | Page background |
| `paper` | `#FAF7F0` | `surface` | Card surface |
| `ink` | `#1A1613` | `onBackground` / `onSurface` / `inverseSurface` | Primary text, hero card bg |
| `inkSoft` | `#5A534C` | `onSurfaceVariant` | Secondary text |
| `inkFaint` | `#A8A096` | `outline` | Placeholder / subtle text |
| `line` | `#E4DDD0` | `outlineVariant` | Borders, dividers |
| `accent` (sport orange) | `#FF5C2B` | `primary` | CTAs, selected state |
| `accentDeep` | `#E04511` | `primaryContainer` | Pressed accent |
| `lime` | `#C7E84A` | `secondary` | Live / active badge |
| `sky` | `#9FC6E8` | `secondaryContainer` | Cool accent |
| `clay` | `#C86A4A` | `tertiary` | Warm accent |
| — | `#1A1613` | `inverseSurface` | Hero card background (light) |
| — | `#FAF7F0` | `inverseOnSurface` | Hero card text (light) |

### Dark mode overrides

Dark mode keeps `primary`, `secondary`, and `tertiary` identical. The background shifts to a warm near-black palette:

| Role | Dark value |
|---|---|
| `background` | `#0F0D0B` |
| `surface` | `#1C1815` |
| `onBackground` / `onSurface` | `#F4F1EA` |
| `onSurfaceVariant` | `#A8A096` |
| `outline` | `#5A534C` |
| `outlineVariant` | `#2A2521` |
| `inverseSurface` | `#000000` |
| `inverseOnSurface` | `#F4F1EA` |

---

## Typography

Three font families are wired into the M3 `Typography` object. They currently use system defaults as placeholders — swap the `FontFamily` constants in `Type.kt` to activate the design typefaces.

| Constant | Intended typeface | Usage in typography scale |
|---|---|---|
| `DisplayFontFamily` | **Archivo Black** | `display*`, `headline*`, `titleLarge` |
| `SansFontFamily` | **Inter** | `titleMedium`, `titleSmall`, `body*` |
| `MonoFontFamily` | **JetBrains Mono** | `label*` (stats, chips, readouts) |

### Size reference

| Style | Size | Line height | Family |
|---|---|---|---|
| `displayLarge` | 48sp | 52sp | Display |
| `displayMedium` | 36sp | 40sp | Display |
| `displaySmall` | 28sp | 32sp | Display |
| `headlineLarge` | 26sp | 30sp | Display |
| `headlineMedium` | 22sp | 26sp | Display |
| `headlineSmall` | 18sp | 22sp | Display |
| `titleLarge` | 15sp | 20sp | Display |
| `titleMedium` | 14sp | 20sp | Sans |
| `titleSmall` | 13sp | 18sp | Sans |
| `bodyLarge` | 14sp | 20sp | Sans |
| `bodyMedium` | 13sp | 18sp | Sans |
| `bodySmall` | 12sp | 16sp | Sans |
| `labelLarge` | 12sp | 16sp | Mono |
| `labelMedium` | 10sp | 14sp | Mono |
| `labelSmall` | 9sp | 12sp | Mono |

### How to import real fonts

1. Add font files to `app/src/main/res/font/`.
2. Declare `FontFamily` objects in `Type.kt` using `Font(R.font.archivo_black)` etc.
3. Replace the three `FontFamily.Default` / `FontFamily.Monospace` placeholders.
4. No other file changes needed — all screens resolve through `MaterialTheme.typography.*`.
