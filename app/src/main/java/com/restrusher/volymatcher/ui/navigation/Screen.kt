package com.restrusher.volymatcher.ui.navigation

import androidx.annotation.StringRes
import com.restrusher.volymatcher.R
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

// ── Bottom-nav roots ─────────────────────────────────────────────────────────
@Serializable data object HomeRoute
@Serializable data object MatchesRoute
@Serializable data object PlayersRoute
@Serializable data object AutoBalanceRoute
@Serializable data object StatsRoute

// ── Detail / flow screens (no bottom bar) ────────────────────────────────────
@Serializable data object CreateMatchRoute
@Serializable data object TeamsRoute
@Serializable data class MatchDetailRoute(val matchId: String)
@Serializable data class TeamDetailRoute(val teamId: String)
@Serializable data class PlayerProfileRoute(val playerId: String)
@Serializable data class BodyScanRoute(val playerId: String?)

// ── Bottom-nav tab registry ───────────────────────────────────────────────────
data class BottomNavTab(
    val routeClass: KClass<*>,
    val routeInstance: Any,
    @StringRes val label: Int,
    val isPrimary: Boolean = false,
)

val bottomNavTabs = listOf(
    BottomNavTab(HomeRoute::class, HomeRoute, R.string.nav_home),
    BottomNavTab(MatchesRoute::class, MatchesRoute, R.string.nav_matches),
    BottomNavTab(AutoBalanceRoute::class, AutoBalanceRoute, R.string.nav_balance, isPrimary = true),
    BottomNavTab(PlayersRoute::class, PlayersRoute, R.string.nav_players),
    BottomNavTab(StatsRoute::class, StatsRoute, R.string.nav_stats),
)
