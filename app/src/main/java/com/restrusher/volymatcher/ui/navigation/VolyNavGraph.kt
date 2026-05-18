package com.restrusher.volymatcher.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.restrusher.volymatcher.ui.screens.balance.AutoBalanceScreen
import com.restrusher.volymatcher.ui.screens.scan.BodyScanScreen
import com.restrusher.volymatcher.ui.screens.matches.create.CreateMatchScreen
import com.restrusher.volymatcher.ui.screens.home.HomeScreen
import com.restrusher.volymatcher.ui.screens.matches.detail.MatchDetailScreen
import com.restrusher.volymatcher.ui.screens.matches.MatchesScreen
import com.restrusher.volymatcher.ui.screens.players.profile.PlayerProfileScreen
import com.restrusher.volymatcher.ui.screens.players.PlayersScreen
import com.restrusher.volymatcher.ui.screens.stats.StatsScreen
import com.restrusher.volymatcher.ui.screens.teams.detail.TeamDetailScreen
import com.restrusher.volymatcher.ui.screens.teams.TeamsScreen
import kotlin.reflect.KClass

@Composable
fun VolyNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = { VolyBottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    onNavigateToMatches = { navController.navigate(MatchesRoute) },
                    onNavigateToTeams = { navController.navigate(TeamsRoute) },
                    onNavigateToBalance = { navController.navigate(AutoBalanceRoute) },
                    onNavigateToBodyScan = { navController.navigate(BodyScanRoute("p1")) },
                )
            }
            composable<MatchesRoute> {
                MatchesScreen(
                    onMatchClick = { id -> navController.navigate(MatchDetailRoute(id)) },
                    onCreateMatch = { navController.navigate(CreateMatchRoute) },
                )
            }
            composable<MatchDetailRoute> { backStack ->
                val route = backStack.toRoute<MatchDetailRoute>()
                MatchDetailScreen(
                    matchId = route.matchId,
                    onBack = { navController.popBackStack() },
                )
            }
            composable<CreateMatchRoute> {
                CreateMatchScreen(onBack = { navController.popBackStack() })
            }
            composable<TeamsRoute> {
                TeamsScreen(onTeamClick = { navController.navigate(TeamDetailRoute(it)) })
            }
            composable<TeamDetailRoute> { backStack ->
                val route = backStack.toRoute<TeamDetailRoute>()
                TeamDetailScreen(
                    teamId = route.teamId,
                    onBack = { navController.popBackStack() },
                    onPlayerClick = { navController.navigate(PlayerProfileRoute(it)) },
                )
            }
            composable<PlayersRoute> {
                PlayersScreen(
                    onPlayerClick = { id -> navController.navigate(PlayerProfileRoute(id)) },
                    onScan = { navController.navigate(BodyScanRoute("p2")) },
                )
            }
            composable<PlayerProfileRoute> { backStack ->
                val route = backStack.toRoute<PlayerProfileRoute>()
                PlayerProfileScreen(
                    playerId = route.playerId,
                    onBack = { navController.popBackStack() },
                    onScan = { navController.navigate(BodyScanRoute(route.playerId)) },
                )
            }
            composable<BodyScanRoute> { backStack ->
                val route = backStack.toRoute<BodyScanRoute>()
                BodyScanScreen(
                    playerId = route.playerId,
                    onBack = { navController.popBackStack() },
                )
            }
            composable<AutoBalanceRoute> {
                AutoBalanceScreen(onBack = { navController.popBackStack() })
            }
            composable<StatsRoute> {
                StatsScreen(
                    onPlayerClick = { id -> navController.navigate(PlayerProfileRoute(id)) },
                )
            }
        }
    }
}

@Composable
private fun VolyBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    @Suppress("UNCHECKED_CAST")
    fun isRouteActive(routeClass: KClass<*>) =
        currentDestination?.hierarchy?.any { it.hasRoute(routeClass as KClass<Any>) } == true

    val isBottomNavScreen = bottomNavTabs.any { isRouteActive(it.routeClass) }
    if (!isBottomNavScreen) return

    val navBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val bg = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outlineVariant
    val accentColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, bg),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY,
                )
            )
            .padding(navBarPadding)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(surfaceColor)
                .border(1.dp, borderColor, RoundedCornerShape(22.dp))
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            bottomNavTabs.forEach { tab ->
                val isSelected = isRouteActive(tab.routeClass)

                if (tab.isPrimary) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                ambientColor = accentColor.copy(alpha = 0.35f),
                                spotColor = accentColor.copy(alpha = 0.35f),
                            )
                            .background(accentColor)
                            .clickable {
                                navController.navigate(tab.routeInstance) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = stringResource(tab.label),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate(tab.routeInstance) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Icon(
                            imageVector = tabIcon(tab.routeClass),
                            contentDescription = stringResource(tab.label),
                            tint = if (isSelected) accentColor else inactiveColor,
                            modifier = Modifier.size(22.dp),
                        )
                        Text(
                            text = stringResource(tab.label),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            ),
                            color = if (isSelected) accentColor else inactiveColor,
                        )
                    }
                }
            }
        }
    }
}

private fun tabIcon(routeClass: KClass<*>): ImageVector = when (routeClass) {
    HomeRoute::class -> Icons.Rounded.Home
    MatchesRoute::class -> Icons.Rounded.Star
    PlayersRoute::class -> Icons.Rounded.Person
    StatsRoute::class -> Icons.Rounded.Star
    else -> Icons.Rounded.Home
}
