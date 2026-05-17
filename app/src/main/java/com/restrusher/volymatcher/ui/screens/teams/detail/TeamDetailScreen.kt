package com.restrusher.volymatcher.ui.screens.teams.detail

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.ui.components.Avatar
import com.restrusher.volymatcher.ui.components.SectionHeader
import com.restrusher.volymatcher.ui.components.StatBar
import com.restrusher.volymatcher.ui.components.TeamCrest

@Composable
fun TeamDetailScreen(
    teamId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onPlayerClick: (String) -> Unit = {},
    viewModel: TeamDetailViewModel = viewModel(factory = TeamDetailViewModel.factory(teamId)),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val team = uiState.team ?: return
    val teamColor = Color(team.colorLong)
    val accentColor = MaterialTheme.colorScheme.primary

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 40.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Rounded.ArrowBack, "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("⋯", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TeamCrest(letter = team.letter, shape = team.shape, color = teamColor, size = 96.dp)
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "EST. JAN 2025 · VOLLEYBALL",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                        .fillMaxWidth(),
                ) {
                    BigStat("${team.wins}", "wins", modifier = Modifier.weight(1f), isAccent = true)
                    Box(modifier = Modifier.width(1.dp).height(70.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    BigStat("${team.losses}", "losses", modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.width(1.dp).height(70.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    BigStat("${team.overallRating}", "overall", modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.width(1.dp).height(70.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    BigStat("+21", "set diff", modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                Column(
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text("Team profile", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                        Text("AVG OF ${team.players.size} PLAYERS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    StatBar("HEIGHT", 181, 200, barColor = accentColor)
                    StatBar("REACH", 234, 260, barColor = accentColor)
                    StatBar("JUMP", 59, 75)
                    StatBar("SPEED", 82, 100)
                    StatBar("SKILL", 78, 100, barColor = accentColor)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SectionHeader(title = "Roster", action = "Edit")
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(team.players, key = { it.id }) { player ->
            RosterRow(
                player = player,
                rank = team.players.indexOf(player) + 1,
                modifier = Modifier.padding(horizontal = 20.dp),
                onClick = { onPlayerClick(player.id) },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            SectionHeader(title = "Last 5")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                listOf("W", "W", "L", "W", "W").forEach { result ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (result == "W") MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = result,
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (result == "W") Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BigStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    isAccent: Boolean = false,
) {
    Column(modifier = modifier.padding(horizontal = 12.dp, vertical = 14.dp)) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = if (isAccent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun RosterRow(
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
                text = rank.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.width(16.dp),
            )
            Avatar(player = player, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = player.name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
                Text(text = "${player.role.uppercase()} · ${player.height} CM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = player.skill.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
