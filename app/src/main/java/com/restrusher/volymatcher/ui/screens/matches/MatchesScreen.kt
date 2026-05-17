package com.restrusher.volymatcher.ui.screens.matches

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
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.MatchFormat
import com.restrusher.volymatcher.ui.components.Chip
import com.restrusher.volymatcher.ui.components.Pill
import com.restrusher.volymatcher.ui.components.PillTone
import com.restrusher.volymatcher.ui.components.SportGlyph

@Composable
fun MatchesScreen(
    modifier: Modifier = Modifier,
    onMatchClick: (String) -> Unit = {},
    onCreateMatch: () -> Unit = {},
    viewModel: MatchesViewModel = viewModel(factory = MatchesViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 120.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Matches", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onBackground)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) { Text("🔍", style = MaterialTheme.typography.bodyMedium) }
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable(onClick = onCreateMatch),
                        contentAlignment = Alignment.Center,
                    ) { Text("＋", style = MaterialTheme.typography.titleSmall, color = Color.White) }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.filters.forEach { label ->
                    Chip(label = label, active = uiState.activeFilter == label, onClick = { viewModel.setFilter(label) })
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Surface(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp)).fillMaxWidth(),
                ) {
                    MiniStat("24", "matches", modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.width(1.dp).height(56.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    MiniStat("17", "wins", isAccent = true, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.width(1.dp).height(56.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    MiniStat("92%", "avg balance", modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text("April", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${uiState.filteredMatches.size} GAMES", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(uiState.filteredMatches, key = { it.id }) { match ->
            MatchListCard(match = match, modifier = Modifier.padding(horizontal = 20.dp), onClick = { onMatchClick(match.id) })
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun MiniStat(value: String, label: String, modifier: Modifier = Modifier, isAccent: Boolean = false) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = if (isAccent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        )
        Text(text = label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun MatchListCard(match: Match, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val isBR = match.format == MatchFormat.BattleRoyale
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 0.dp) {
        Column(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .padding(16.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SportGlyph(sport = match.sport, size = 14.dp)
                Text(
                    text = "${match.date.uppercase()} · ${match.sport.uppercase()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                if (isBR) Pill(text = "BR · ${match.teamCount}", tone = PillTone.Accent)
                else Pill(text = "Official", tone = PillTone.Outline)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = match.title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(12.dp))
            if (isBR) {
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.background).padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(modifier = Modifier.size(26.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondary), contentAlignment = Alignment.Center) {
                        Text("🏆", style = MaterialTheme.typography.labelMedium)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("WINNER", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(match.winner ?: "", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
                    }
                    Text("${match.teamCount} teams", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(match.teamA ?: "", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                    Text(match.score ?: "", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
                    Text(match.teamB ?: "", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(match.venue, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("BAL ${match.balance}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
