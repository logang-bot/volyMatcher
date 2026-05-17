package com.restrusher.volymatcher.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restrusher.volymatcher.domain.model.Player
import kotlin.math.hypot

@Composable
fun Avatar(
    player: Player,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    showRing: Boolean = false,
) {
    val playerColor = Color(player.colorLong)
    val darkerColor = playerColor.copy(
        red = (playerColor.red * 0.88f).coerceIn(0f, 1f),
        green = (playerColor.green * 0.88f).coerceIn(0f, 1f),
        blue = (playerColor.blue * 0.88f).coerceIn(0f, 1f),
    )
    val textColor = if (playerColor.luminance() > 0.35f)
        MaterialTheme.colorScheme.onBackground
    else
        MaterialTheme.colorScheme.inverseOnSurface

    val ringBorderColor = MaterialTheme.colorScheme.surface
    val ringAccentColor = MaterialTheme.colorScheme.onBackground

    val ringModifier = if (showRing) {
        Modifier
            .border(2.dp, ringAccentColor, CircleShape)
            .border(4.dp, ringBorderColor, CircleShape)
    } else Modifier

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(ringModifier)
            .drawBehind {
                val diagonal = hypot(this.size.width, this.size.height)
                val stripeWidth = 3.dp.toPx()
                val period = 6.dp.toPx()

                drawRect(color = darkerColor)

                var offset = -diagonal
                while (offset < diagonal * 2) {
                    drawLine(
                        color = playerColor,
                        start = Offset(offset, 0f),
                        end = Offset(offset + this.size.height, this.size.height),
                        strokeWidth = stripeWidth,
                    )
                    offset += period
                }
            },
    ) {
        Text(
            text = player.initials,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.38f).sp,
            ),
            color = textColor,
        )
    }
}
