package com.restrusher.volymatcher.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private fun volleyballIcon(): ImageVector = ImageVector.Builder(
    name = "Volleyball",
    defaultWidth = 16.dp,
    defaultHeight = 16.dp,
    viewportWidth = 16f,
    viewportHeight = 16f,
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.4f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        fill = null,
    ) {
        moveTo(8f, 1.5f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 13f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -13f)
        close()
    }
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.4f,
        strokeLineCap = StrokeCap.Round,
        fill = null,
    ) {
        moveTo(1.8f, 8f)
        cubicTo(4.8f, 7f, 8.8f, 7f, 14.2f, 9f)
    }
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.4f,
        strokeLineCap = StrokeCap.Round,
        fill = null,
    ) {
        moveTo(4f, 3f)
        cubicTo(6f, 6f, 7f, 11f, 6.5f, 15f)
    }
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.4f,
        strokeLineCap = StrokeCap.Round,
        fill = null,
    ) {
        moveTo(12f, 3f)
        cubicTo(10f, 6f, 9f, 11f, 9.5f, 15f)
    }
}.build()

private fun basketballIcon(): ImageVector = ImageVector.Builder(
    name = "Basketball",
    defaultWidth = 16.dp,
    defaultHeight = 16.dp,
    viewportWidth = 16f,
    viewportHeight = 16f,
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.4f,
        strokeLineCap = StrokeCap.Round,
        fill = null,
    ) {
        moveTo(8f, 1.5f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 13f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -13f)
        close()
        moveTo(1.5f, 8f)
        horizontalLineTo(14.5f)
        moveTo(8f, 1.5f)
        verticalLineTo(14.5f)
        moveTo(3f, 3.5f)
        cubicTo(5f, 5f, 5f, 11f, 3f, 12.5f)
        moveTo(13f, 3.5f)
        cubicTo(11f, 5f, 11f, 11f, 13f, 12.5f)
    }
}.build()

private fun soccerIcon(): ImageVector = ImageVector.Builder(
    name = "Soccer",
    defaultWidth = 16.dp,
    defaultHeight = 16.dp,
    viewportWidth = 16f,
    viewportHeight = 16f,
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.4f,
        strokeLineCap = StrokeCap.Round,
        fill = null,
    ) {
        moveTo(8f, 1.5f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 13f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -13f)
        close()
    }
    path(
        strokeLineWidth = 1.4f,
        fill = SolidColor(Color.Black),
        fillAlpha = 0.15f,
    ) {
        moveTo(8f, 4f)
        lineTo(11f, 6f)
        lineTo(10f, 9.5f)
        horizontalLineTo(6f)
        lineTo(5f, 6f)
        close()
    }
}.build()

private fun genericSportIcon(): ImageVector = ImageVector.Builder(
    name = "Sport",
    defaultWidth = 16.dp,
    defaultHeight = 16.dp,
    viewportWidth = 16f,
    viewportHeight = 16f,
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.4f,
        fill = null,
    ) {
        moveTo(8f, 1.5f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 13f)
        arcToRelative(6.5f, 6.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, -13f)
        close()
    }
}.build()

@Composable
fun SportGlyph(
    sport: String,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val icon = when {
        sport.contains("volley", ignoreCase = true) -> volleyballIcon()
        sport.contains("basket", ignoreCase = true) -> basketballIcon()
        sport.contains("soccer", ignoreCase = true) ||
            sport.contains("football", ignoreCase = true) -> soccerIcon()
        else -> genericSportIcon()
    }
    Icon(
        imageVector = icon,
        contentDescription = sport,
        tint = tint,
        modifier = modifier.size(size),
    )
}
