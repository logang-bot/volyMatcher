package com.restrusher.volymatcher.ui.navigation

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
@Serializable data class BodyScanRoute(val playerId: String)

// ── Bottom-nav tab registry ───────────────────────────────────────────────────
data class BottomNavTab(
    val routeClass: KClass<*>,
    val routeInstance: Any,
    val label: String,
    val isPrimary: Boolean = false,
)

val bottomNavTabs = listOf(
    BottomNavTab(HomeRoute::class, HomeRoute, "Home"),
    BottomNavTab(MatchesRoute::class, MatchesRoute, "Matches"),
    BottomNavTab(AutoBalanceRoute::class, AutoBalanceRoute, "Balance", isPrimary = true),
    BottomNavTab(PlayersRoute::class, PlayersRoute, "Players"),
    BottomNavTab(StatsRoute::class, StatsRoute, "Stats"),
)
