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
import androidx.compose.ui.res.stringResource
import com.restrusher.volymatcher.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.domain.model.Player
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.restrusher.volymatcher.ui.components.Avatar
import com.restrusher.volymatcher.ui.components.MatchMiniCard
import com.restrusher.volymatcher.ui.components.Pill
import com.restrusher.volymatcher.ui.components.PillTone
import com.restrusher.volymatcher.ui.components.PrimaryButton
import com.restrusher.volymatcher.ui.components.SectionHeader
import com.restrusher.volymatcher.ui.theme.VolyMatcherTheme

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
    HomeContent(
        uiState = uiState,
        modifier = modifier,
        onNavigateToMatches = onNavigateToMatches,
        onNavigateToTeams = onNavigateToTeams,
        onNavigateToBalance = onNavigateToBalance,
        onNavigateToBodyScan = onNavigateToBodyScan,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onNavigateToMatches: () -> Unit = {},
    onNavigateToTeams: () -> Unit = {},
    onNavigateToBalance: () -> Unit = {},
    onNavigateToBodyScan: () -> Unit = {},
) {
    val nextMatch = uiState.nextMatch
    val players = uiState.squadPlayers
    val recentMatches = uiState.recentMatches
    val today = LocalDate.now()
    val dateLabel = today.format(DateTimeFormatter.ofPattern("EEE · d MMM")).uppercase()

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
                        text = dateLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.home_greeting),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = stringResource(R.string.home_greeting_sub),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        Pill(text = stringResource(R.string.home_up_next), tone = PillTone.Ghost)
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
                            if (players.isNotEmpty()) {
                                Text(
                                    text = stringResource(R.string.home_squad_player_count, players.size),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                PrimaryButton(text = stringResource(R.string.home_action_balance_teams), onClick = onNavigateToBalance)
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
                                    text = stringResource(R.string.home_action_roster),
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
                title = stringResource(R.string.home_section_recent_matches),
                action = stringResource(R.string.action_see_all),
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
            SectionHeader(title = stringResource(R.string.home_section_your_squad, players.size), action = stringResource(R.string.action_manage))
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
            QuickActionCard("✨", stringResource(R.string.home_quick_auto_balance), stringResource(R.string.home_quick_auto_balance_sub), cardBg, borderColor, onBalance)
            QuickActionCard("➕", stringResource(R.string.home_quick_new_match), stringResource(R.string.home_quick_new_match_sub), cardBg, borderColor, onNewMatch)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickActionCard("📡", stringResource(R.string.home_quick_scan_player), stringResource(R.string.home_quick_scan_player_sub), accentBg, borderColor, onScan)
            QuickActionCard("👥", stringResource(R.string.home_quick_invite_friends), stringResource(R.string.home_quick_invite_friends_sub), cardBg, borderColor, {})
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

@Preview(showBackground = true, name = "Home — Light")
@Composable
private fun HomeScreenLightPreview() {
    VolyMatcherTheme(darkTheme = false) {
        HomeContent(
            uiState = HomeUiState(
                nextMatch = SampleDataSource.matches.first(),
                recentMatches = SampleDataSource.matches.drop(1).take(2),
                squadPlayers = SampleDataSource.players,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Home — Dark")
@Composable
private fun HomeScreenDarkPreview() {
    VolyMatcherTheme(darkTheme = true) {
        HomeContent(
            uiState = HomeUiState(
                nextMatch = SampleDataSource.matches.first(),
                recentMatches = SampleDataSource.matches.drop(1).take(2),
                squadPlayers = SampleDataSource.players,
            ),
        )
    }
}
