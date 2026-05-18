package com.restrusher.volymatcher.ui.screens.scan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.restrusher.volymatcher.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.ui.theme.VolyMatcherTheme

@Composable
fun BodyScanScreen(
    playerId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: BodyScanViewModel = viewModel(factory = BodyScanViewModel.factory(playerId)),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BodyScanContent(uiState = uiState, modifier = modifier, onBack = onBack)
}

@Composable
private fun BodyScanContent(
    uiState: BodyScanUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val player = uiState.player ?: return
    val accentColor = MaterialTheme.colorScheme.primary
    val heroBg = MaterialTheme.colorScheme.inverseSurface
    val heroFg = MaterialTheme.colorScheme.inverseOnSurface

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(heroBg),
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
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(heroFg.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Rounded.ArrowBack, "Back", tint = heroFg, modifier = Modifier.size(16.dp))
                    }
                }
                Text(text = stringResource(R.string.scan_step), style = MaterialTheme.typography.labelMedium, color = heroFg.copy(alpha = 0.6f))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(heroFg.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("?", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = heroFg)
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(text = stringResource(R.string.scan_instruction, player.name), style = MaterialTheme.typography.headlineLarge, color = heroFg)
                Text(text = stringResource(R.string.scan_position), style = MaterialTheme.typography.labelMedium, color = heroFg.copy(alpha = 0.6f))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFF2A2420), Color(0xFF1A1613))))
                    .border(1.dp, accentColor, RoundedCornerShape(18.dp)),
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val gridColor = accentColor.copy(alpha = 0.25f)
                    val hStep = size.height / 20f
                    val vStep = size.width / 15f
                    repeat(20) { i -> drawLine(gridColor, Offset(0f, i * hStep), Offset(size.width, i * hStep), 0.5f) }
                    repeat(15) { i -> drawLine(gridColor, Offset(i * vStep, 0f), Offset(i * vStep, size.height), 0.5f) }
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path().apply {
                        val cx = size.width / 2f
                        val headR = size.width * 0.12f
                        addOval(androidx.compose.ui.geometry.Rect(cx - headR, size.height * 0.08f, cx + headR, size.height * 0.08f + headR * 2))
                        moveTo(cx - headR * 1.5f, size.height * 0.23f)
                        lineTo(cx - headR * 2.5f, size.height * 0.5f)
                        lineTo(cx - headR, size.height * 0.5f)
                        lineTo(cx - headR * 0.8f, size.height * 0.78f)
                        lineTo(cx - headR * 0.8f, size.height * 0.95f)
                        lineTo(cx + headR * 0.8f, size.height * 0.95f)
                        lineTo(cx + headR * 0.8f, size.height * 0.78f)
                        lineTo(cx + headR, size.height * 0.5f)
                        lineTo(cx + headR * 2.5f, size.height * 0.5f)
                        lineTo(cx + headR * 1.5f, size.height * 0.23f)
                        close()
                    }
                    drawPath(path, Brush.verticalGradient(listOf(accentColor.copy(0.15f), accentColor.copy(0.05f))))
                    drawPath(path, accentColor, style = Stroke(2f))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.Center)
                        .offset(y = 30.dp)
                        .background(Brush.horizontalGradient(listOf(Color.Transparent, accentColor, Color.Transparent))),
                )

                CornerBracket(Modifier.align(Alignment.TopStart).padding(10.dp), accentColor, isTop = true, isLeft = true)
                CornerBracket(Modifier.align(Alignment.TopEnd).padding(10.dp), accentColor, isTop = true, isLeft = false)
                CornerBracket(Modifier.align(Alignment.BottomStart).padding(10.dp), accentColor, isTop = false, isLeft = true)
                CornerBracket(Modifier.align(Alignment.BottomEnd).padding(10.dp), accentColor, isTop = false, isLeft = false)

                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1A1613).copy(alpha = 0.75f))
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                ) {
                    Text(stringResource(R.string.scan_capturing), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                    Text("FRAME 247/300", style = MaterialTheme.typography.labelSmall, color = heroFg.copy(alpha = 0.8f))
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1A1613).copy(alpha = 0.75f))
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text("H  ${player.height} cm", style = MaterialTheme.typography.labelSmall, color = accentColor)
                    Text("W  ${player.weight} kg", style = MaterialTheme.typography.labelSmall, color = accentColor)
                    Text("REACH  ${player.reach}", style = MaterialTheme.typography.labelSmall, color = accentColor)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(stringResource(R.string.scan_progress_label), style = MaterialTheme.typography.labelMedium, color = heroFg.copy(alpha = 0.6f))
                    Text("82%", style = MaterialTheme.typography.labelMedium, color = accentColor)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(heroFg.copy(alpha = 0.1f)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.82f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(accentColor),
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(text = stringResource(R.string.scan_stats_label), style = MaterialTheme.typography.labelSmall, color = heroFg.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(10.dp))

                val stats = listOf(
                    Triple(stringResource(R.string.stat_height), "${player.height} cm", true),
                    Triple(stringResource(R.string.stat_weight), "${player.weight} kg", true),
                    Triple(stringResource(R.string.stat_wingspan), "${player.reach} cm", true),
                    Triple(stringResource(R.string.stat_vert_jump), "…", false),
                    Triple(stringResource(R.string.stat_body_comp), stringResource(R.string.scan_scanning_value), false),
                    Triple(stringResource(R.string.stat_hand), player.hand, true),
                )

                stats.chunked(2).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { (label, value, done) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (done) MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f) else heroFg.copy(alpha = 0.05f))
                                    .border(
                                        1.dp,
                                        if (done) MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f) else heroFg.copy(alpha = 0.1f),
                                        RoundedCornerShape(10.dp),
                                    )
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                            ) {
                                Column {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(label, style = MaterialTheme.typography.labelSmall, color = heroFg.copy(alpha = 0.5f))
                                        if (done) {
                                            Text("✓", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
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
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentColor)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.scan_hold_steady),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun CornerBracket(modifier: Modifier = Modifier, color: Color, isTop: Boolean, isLeft: Boolean) {
    Box(modifier = modifier.size(18.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sw = 2.dp.toPx()
            val len = size.width
            if (isTop) drawLine(color, Offset(0f, 0f), Offset(len, 0f), sw)
            else drawLine(color, Offset(0f, size.height), Offset(len, size.height), sw)
            if (isLeft) drawLine(color, Offset(0f, if (isTop) 0f else size.height), Offset(0f, if (isTop) len else size.height - len), sw)
            else drawLine(color, Offset(size.width, if (isTop) 0f else size.height), Offset(size.width, if (isTop) len else size.height - len), sw)
        }
    }
}

@Preview(showBackground = true, name = "Body Scan — Light")
@Composable
private fun BodyScanLightPreview() {
    VolyMatcherTheme(darkTheme = false) {
        BodyScanContent(uiState = BodyScanUiState(player = SampleDataSource.players.first()))
    }
}
