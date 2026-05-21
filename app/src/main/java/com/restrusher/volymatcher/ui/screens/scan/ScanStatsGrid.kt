package com.restrusher.volymatcher.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.restrusher.volymatcher.R
import com.restrusher.volymatcher.domain.model.Player

@Composable
fun ScanStatsGrid(
    player: Player?,
    accentColor: Color,
    heroFg: Color,
    modifier: Modifier = Modifier,
) {
    val scanning = stringResource(R.string.scan_scanning_value)
    val stats = listOf(
        Triple(stringResource(R.string.stat_height),   player?.height?.let { "$it cm" } ?: scanning, player != null),
        Triple(stringResource(R.string.stat_weight),   player?.weight?.let { "$it kg" } ?: scanning, player != null),
        Triple(stringResource(R.string.stat_wingspan), player?.reach?.let { "$it cm" }  ?: scanning, player != null),
        Triple(stringResource(R.string.stat_vert_jump), "…", false),
        Triple(stringResource(R.string.stat_body_comp), scanning, false),
        Triple(stringResource(R.string.stat_hand),     player?.hand ?: scanning, player != null),
    )

    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        Text(
            text = stringResource(R.string.scan_stats_label),
            style = MaterialTheme.typography.labelSmall,
            color = heroFg.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.height(10.dp))

        stats.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { (label, value, done) ->
                    ScanStatCard(
                        label = label,
                        value = value,
                        done = done,
                        accentColor = accentColor,
                        heroFg = heroFg,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ScanStatCard(
    label: String,
    value: String,
    done: Boolean,
    accentColor: Color,
    heroFg: Color,
    modifier: Modifier = Modifier,
) {
    val secondary = MaterialTheme.colorScheme.secondary
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (done) secondary.copy(alpha = 0.08f) else heroFg.copy(alpha = 0.05f))
            .border(
                1.dp,
                if (done) secondary.copy(alpha = 0.25f) else heroFg.copy(alpha = 0.1f),
                RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = heroFg.copy(alpha = 0.5f))
                if (done) {
                    Text("✓", style = MaterialTheme.typography.labelSmall, color = secondary)
                } else {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(accentColor.copy(alpha = 0.6f)))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = if (done) heroFg else heroFg.copy(alpha = 0.4f),
            )
        }
    }
}
