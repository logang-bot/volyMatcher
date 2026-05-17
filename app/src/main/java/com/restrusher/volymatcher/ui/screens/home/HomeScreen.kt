package com.restrusher.volymatcher.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.ui.components.Avatar
import com.restrusher.volymatcher.ui.components.MatchMiniCard
import com.restrusher.volymatcher.ui.components.Pill
import com.restrusher.volymatcher.ui.components.PillTone
import com.restrusher.volymatcher.ui.components.PrimaryButton
import com.restrusher.volymatcher.ui.components.SectionHeader

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToMatches: () -> Unit = {},
    onNavigateToTeams: () -> Unit = {},
    onNavigateToBalance: () -> Unit = {},
    onNavigateToBodyScan: () -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val nextMatch = uiState.nextMatch
    val players = uiState.squadPlayers
    val recentMatches = uiState.recentMatches

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 120.dp),
    ) {
        // Greeting header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        text = "TUE · 14 APR",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hey, Alex.",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "Let's get a game on.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "AR",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.background,
                    )
                }
            }
        }

        // Hero card — next match
        if (nextMatch != null) {
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.inverseSurface)
                        .padding(20.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .offset(x = 60.dp, y = (-24).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.TopEnd),
                    )
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Pill(text = "Up next · Tonight 7:30", tone = PillTone.Ghost)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = nextMatch.title,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                        Text(
                            text = nextMatch.venue,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            StackedAvatars(players = players.take(5))
                            Text(
                                text = "+7 going",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f),
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                PrimaryButton(text = "Balance teams", onClick = onNavigateToBalance)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.3f),
                                        RoundedCornerShape(10.dp),
                                    )
                                    .clickable(onClick = onNavigateToTeams)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "Roster",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item {
            QuickActionsGrid(
                onBalance = onNavigateToBalance,
                onScan = onNavigateToBodyScan,
                onNewMatch = onNavigateToMatches,
            )
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }

        item {
            SectionHeader(
                title = "Recent matches",
                action = "See all",
                onActionClick = onNavigateToMatches,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(recentMatches) { match ->
            MatchMiniCard(
                match = match,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            SectionHeader(title = "Your squad · ${players.size} players", action = "Manage")
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(players.take(7)) { player ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(64.dp),
                    ) {
                        Avatar(player = player, size = 52.dp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = player.nick,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = "${player.skill} ovr",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StackedAvatars(players: List<Player>) {
    Box {
        players.forEachIndexed { index, player ->
            Box(modifier = Modifier.padding(start = (index * 18).dp)) {
                Avatar(player = player, size = 28.dp, showRing = true)
            }
        }
    }
}

@Composable
private fun QuickActionsGrid(
    onBalance: () -> Unit,
    onScan: () -> Unit,
    onNewMatch: () -> Unit,
) {
    val cardBg = MaterialTheme.colorScheme.surface
    val accentBg = MaterialTheme.colorScheme.secondary
    val borderColor = MaterialTheme.colorScheme.outlineVariant

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickActionCard("✨", "Auto-balance", "pick players, we'll split", cardBg, borderColor, onBalance)
            QuickActionCard("➕", "New match", "official or battle royale", cardBg, borderColor, onNewMatch)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickActionCard("📡", "Scan a player", "measure in 30s", accentBg, borderColor, onScan)
            QuickActionCard("👥", "Invite friends", "share your squad", cardBg, borderColor, {})
        }
    }
}

@Composable
private fun QuickActionCard(
    emoji: String,
    label: String,
    sub: String,
    bg: Color,
    borderColor: Color,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = bg,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                .clickable(onClick = onClick)
                .padding(14.dp),
        ) {
            Text(text = emoji, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = label, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            Text(text = sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
