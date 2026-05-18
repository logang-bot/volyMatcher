package com.restrusher.volymatcher.ui.screens.players.profile

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
import androidx.compose.ui.res.stringResource
import com.restrusher.volymatcher.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.ui.components.Pill
import com.restrusher.volymatcher.ui.components.PillTone
import com.restrusher.volymatcher.ui.components.SectionHeader
import com.restrusher.volymatcher.ui.components.SportGlyph
import com.restrusher.volymatcher.ui.components.StatBar
import com.restrusher.volymatcher.ui.theme.VolyMatcherTheme

@Composable
fun PlayerProfileScreen(
    playerId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onScan: () -> Unit = {},
    viewModel: PlayerProfileViewModel = viewModel(factory = PlayerProfileViewModel.factory(playerId)),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlayerProfileContent(uiState = uiState, modifier = modifier, onBack = onBack, onScan = onScan)
}

@Composable
private fun PlayerProfileContent(
    uiState: PlayerProfileUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onScan: () -> Unit = {},
) {
    val player = uiState.player ?: return
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
                verticalAlignment = Alignment.CenterVertically,
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
                Text(
                    text = stringResource(R.string.profile_title),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = onScan, modifier = Modifier.size(36.dp)) {
                        Text("📡", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

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
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.05f),
                                    Color.Transparent,
                                )
                            )
                        ),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .width(90.dp)
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(player.colorLong).copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = player.initials,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.8f),
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Pill(text = stringResource(R.string.profile_scanned_pill), tone = PillTone.Lime)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = player.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                        Text(
                            text = "${player.role.uppercase()} · ${player.hand}-HAND",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            HeroStat(player.skill.toString(), "OVR")
                            HeroStat(player.height.toString(), "CM")
                            HeroStat(player.weight.toString(), "KG")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SectionHeader(title = stringResource(R.string.profile_section_body_scan), action = stringResource(R.string.profile_action_rescan), onActionClick = onScan)
            Spacer(modifier = Modifier.height(12.dp))
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
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary),
                        )
                        Text(
                            text = stringResource(R.string.profile_last_scanned),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    val bodyMetrics = listOf(
                        Triple(stringResource(R.string.stat_height), "${player.height}", stringResource(R.string.profile_unit_cm)),
                        Triple(stringResource(R.string.stat_weight), "${player.weight}", stringResource(R.string.profile_unit_kg)),
                        Triple(stringResource(R.string.stat_wingspan), "${player.reach}", stringResource(R.string.profile_unit_cm)),
                        Triple(stringResource(R.string.stat_vert_jump), "${player.jump}", stringResource(R.string.profile_unit_cm)),
                        Triple(stringResource(R.string.stat_body_comp), "19.8", stringResource(R.string.profile_unit_fat)),
                        Triple(stringResource(R.string.stat_speed_idx), "${player.speed}", stringResource(R.string.profile_unit_per100)),
                    )

                    bodyMetrics.chunked(2).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            row.forEach { (label, value, unit) ->
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        verticalAlignment = Alignment.Bottom,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    ) {
                                        Text(
                                            text = value,
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                        )
                                        Text(
                                            text = unit,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SectionHeader(title = stringResource(R.string.profile_section_performance))
            Spacer(modifier = Modifier.height(12.dp))
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
                    StatBar(stringResource(R.string.stat_jump), player.jump, 75, barColor = accentColor)
                    StatBar(stringResource(R.string.stat_speed), player.speed, 100, barColor = accentColor)
                    StatBar(stringResource(R.string.stat_reach), player.reach, 260)
                    StatBar(stringResource(R.string.stat_skill), player.skill, 100)
                    StatBar(stringResource(R.string.stat_stamina), 72, 100)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SectionHeader(title = stringResource(R.string.profile_section_last5))
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(uiState.recentMatches, key = { it.id }) { match ->
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    SportGlyph(sport = match.sport, size = 14.dp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(match.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onBackground)
                        Text(match.date.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(stringResource(R.string.profile_result_mvp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Text(stringResource(R.string.profile_result_win), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true, name = "Player Profile — Light")
@Composable
private fun PlayerProfileLightPreview() {
    VolyMatcherTheme(darkTheme = false) {
        PlayerProfileContent(
            uiState = PlayerProfileUiState(
                player = SampleDataSource.players.first(),
                recentMatches = SampleDataSource.matches.take(3),
            ),
        )
    }
}

@Composable
private fun HeroStat(value: String, label: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
        )
    }
}
