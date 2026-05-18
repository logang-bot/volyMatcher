package com.restrusher.volymatcher.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.ui.components.Avatar
import com.restrusher.volymatcher.ui.components.Chip
import com.restrusher.volymatcher.ui.components.SectionHeader
import com.restrusher.volymatcher.ui.components.TeamCrest
import com.restrusher.volymatcher.ui.theme.VolyMatcherTheme

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    onPlayerClick: (String) -> Unit = {},
    viewModel: StatsViewModel = viewModel(factory = StatsViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    StatsContent(
        uiState = uiState,
        modifier = modifier,
        onPlayerClick = onPlayerClick,
        onScope = { viewModel.setScope(it) },
        onTab = { viewModel.setTab(it) },
    )
}

@Composable
private fun StatsContent(
    uiState: StatsUiState,
    modifier: Modifier = Modifier,
    onPlayerClick: (String) -> Unit = {},
    onScope: (String) -> Unit = {},
    onTab: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 120.dp),
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                Text(stringResource(R.string.stats_title), style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onBackground)
                Text(uiState.activeScope, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        item {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.scopes.forEach { scope ->
                    Chip(label = scope, active = uiState.activeScope == scope, onClick = { onScope(scope) })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.stats_top_squad),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (uiState.leaders.size >= 3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            PodiumEntry(rank = 2, player = uiState.leaders[1], podiumHeight = 82.dp, isGold = false)
                            Spacer(modifier = Modifier.width(10.dp))
                            PodiumEntry(rank = 1, player = uiState.leaders[0], podiumHeight = 110.dp, isGold = true)
                            Spacer(modifier = Modifier.width(10.dp))
                            PodiumEntry(rank = 3, player = uiState.leaders[2], podiumHeight = 66.dp, isGold = false)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                uiState.tabs.forEach { tab ->
                    val isActive = uiState.activeTab == tab
                    Box(
                        modifier = Modifier
                            .clickable { onTab(tab) }
                            .padding(vertical = 8.dp)
                            .padding(end = 18.dp),
                    ) {
                        Column {
                            Text(
                                text = tab,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                ),
                                color = if (isActive) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            if (isActive) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .background(MaterialTheme.colorScheme.onBackground),
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant),
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(uiState.leaders, key = { it.id }) { player ->
            val rank = uiState.leaders.indexOf(player) + 1
            LeaderRow(
                player = player,
                rank = rank,
                modifier = Modifier.padding(horizontal = 20.dp),
                onClick = { onPlayerClick(player.id) },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }

        item { SectionHeader(title = stringResource(R.string.stats_section_teams)) }
        item { Spacer(modifier = Modifier.height(12.dp)) }

        items(uiState.teams, key = { it.name }) { team ->
            Surface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = (uiState.teams.indexOf(team) + 1).toString().padStart(2, '0'),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.width(16.dp),
                    )
                    TeamCrest(letter = team.letter, shape = team.shape, color = Color(team.colorLong), size = 32.dp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(team.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
                        Text("${team.wins}W · ${team.losses}L", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = "${(team.winPercentage * 100).toInt()}%",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PodiumEntry(
    rank: Int,
    player: Player,
    podiumHeight: Dp,
    isGold: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(78.dp),
    ) {
        Avatar(player = player, size = 42.dp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = player.nick,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Text(
            text = "${player.skill} OVR",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(podiumHeight)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(
                    if (isGold) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.08f)
                )
                .border(
                    1.dp,
                    if (isGold) Color.Transparent else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.1f),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                ),
            contentAlignment = Alignment.TopCenter,
        ) {
            Text(
                text = rank.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = if (isGold) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun LeaderRow(
    player: Player,
    rank: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = rank.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = if (rank <= 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.width(20.dp),
                textAlign = TextAlign.End,
            )
            Avatar(player = player, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(player.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
                Text(player.role.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = player.skill.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.End,
            )
        }
    }
}

@Preview(showBackground = true, name = "Stats — Light")
@Composable
private fun StatsLightPreview() {
    VolyMatcherTheme(darkTheme = false) {
        StatsContent(
            uiState = StatsUiState(
                leaders = SampleDataSource.players.sortedByDescending { it.skill }.take(6),
                teams = SampleDataSource.teams().sortedByDescending { it.winPercentage },
            ),
        )
    }
}

@Preview(showBackground = true, name = "Stats — Empty")
@Composable
private fun StatsEmptyPreview() {
    VolyMatcherTheme(darkTheme = false) {
        StatsContent(uiState = StatsUiState())
    }
}
