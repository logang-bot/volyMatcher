package com.restrusher.volymatcher.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restrusher.volymatcher.domain.model.CrestShape

@Composable
fun TeamCrest(
    letter: Char,
    shape: CrestShape,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
) {
    val textMeasurer = rememberTextMeasurer()
    val textColor = if (color.luminance() > 0.35f)
        MaterialTheme.colorScheme.onBackground
    else
        MaterialTheme.colorScheme.inverseOnSurface

    val letterStyle = MaterialTheme.typography.headlineSmall.copy(
        fontSize = (size.value * 0.42f).sp,
        textAlign = TextAlign.Center,
        color = textColor,
    )

    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                val scaleFactor = this.size.width / 44f
                scale(scaleFactor, scaleFactor, pivot = androidx.compose.ui.geometry.Offset.Zero) {
                    drawPath(buildShapePath(shape), color = color)
                }

                val textLayout = textMeasurer.measure(letter.toString(), letterStyle)
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = (this.size.width - textLayout.size.width) / 2f,
                        y = (this.size.height - textLayout.size.height) / 2f,
                    ),
                )
            },
    )
}

private fun buildShapePath(shape: CrestShape): Path = when (shape) {
    CrestShape.Shield -> Path().apply {
        // M22 4 l14 5 v13 c0,9 -6,14 -14,17 c-8,-3 -14,-8 -14,-17 V9 l14,-5 z
        moveTo(22f, 4f)
        lineTo(36f, 9f)
        lineTo(36f, 22f)
        cubicTo(36f, 31f, 30f, 36f, 22f, 39f)
        cubicTo(14f, 36f, 8f, 31f, 8f, 22f)
        lineTo(8f, 9f)
        close()
    }
    CrestShape.Disc -> Path().apply {
        addOval(androidx.compose.ui.geometry.Rect(4f, 4f, 40f, 40f))
    }
    CrestShape.Diamond -> Path().apply {
        moveTo(22f, 4f)
        lineTo(40f, 22f)
        lineTo(22f, 40f)
        lineTo(4f, 22f)
        close()
    }
    CrestShape.Blob -> Path().apply {
        // M22 4 c10,0 16,6 16,16 c-2,12 -8,18 -16,18 c-10,0 -16,-6 -16,-16 C4,10 12,4 22,4 z
        moveTo(22f, 4f)
        relativeCubicTo(10f, 0f, 16f, 6f, 16f, 16f)
        relativeCubicTo(-2f, 12f, -8f, 18f, -16f, 18f)
        relativeCubicTo(-10f, 0f, -16f, -6f, -16f, -16f)
        cubicTo(4f, 10f, 12f, 4f, 22f, 4f)
        close()
    }
}
