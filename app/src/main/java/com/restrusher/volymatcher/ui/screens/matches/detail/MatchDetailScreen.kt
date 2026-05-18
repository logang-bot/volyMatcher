package com.restrusher.volymatcher.ui.screens.matches.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.restrusher.volymatcher.domain.model.Match
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.ui.components.Avatar
import com.restrusher.volymatcher.ui.components.Pill
import com.restrusher.volymatcher.ui.components.PillTone
import com.restrusher.volymatcher.ui.components.SectionHeader
import com.restrusher.volymatcher.ui.theme.VolyMatcherTheme

@Composable
fun MatchDetailScreen(
    matchId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: MatchDetailViewModel = viewModel(factory = MatchDetailViewModel.factory(matchId)),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MatchDetailContent(
        uiState = uiState,
        modifier = modifier,
        onBack = onBack,
    )
}

@Composable
private fun MatchDetailContent(
    uiState: MatchDetailUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    if (uiState.match == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.match_detail_not_found), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    val match = uiState.match
    val isLive = match.status.equals("Live", ignoreCase = true)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 40.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .padding(20.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                        if (isLive) {
                            Pill(text = stringResource(R.string.match_detail_live_pill), tone = PillTone.Accent)
                        } else {
                            Pill(text = match.status.uppercase(), tone = PillTone.Ghost)
                        }
                        Text(
                            text = match.sport.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = match.teamA ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                            )
                        }
                        Text(
                            text = if (match.score != null) match.score else stringResource(R.string.match_detail_vs),
                            style = if (match.score != null) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = if (match.score != null) 1f else 0.5f),
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End,
                        ) {
                            Text(
                                text = match.teamB ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                            )
                        }
                    }

                    if (match.winner != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text("🏆", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = match.winner,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            }
        }

        item {
            SectionHeader(title = stringResource(R.string.match_detail_on_court), action = stringResource(R.string.match_detail_sub))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CourtSide(
                    teamName = match.teamA ?: "",
                    players = uiState.teamAPlayers,
                    accentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                CourtSide(
                    teamName = match.teamB ?: "",
                    players = uiState.teamBPlayers,
                    accentColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

    }
}

@Preview(showBackground = true, name = "Match Detail — Light")
@Composable
private fun MatchDetailLightPreview() {
    VolyMatcherTheme(darkTheme = false) {
        MatchDetailContent(
            uiState = MatchDetailUiState(
                match = SampleDataSource.matches.first(),
                teamAPlayers = SampleDataSource.players.take(3),
                teamBPlayers = SampleDataSource.players.drop(6).take(3),
            ),
        )
    }
}

@Preview(showBackground = true, name = "Match Detail — Empty")
@Composable
private fun MatchDetailEmptyPreview() {
    VolyMatcherTheme(darkTheme = false) {
        MatchDetailContent(uiState = MatchDetailUiState())
    }
}

@Composable
private fun CourtSide(
    teamName: String,
    players: List<Player>,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                .padding(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                )
                Text(
                    text = teamName,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            players.forEach { player ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 6.dp),
                ) {
                    Avatar(player = player, size = 22.dp)
                    Text(
                        text = player.nick,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = player.role.take(3).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

