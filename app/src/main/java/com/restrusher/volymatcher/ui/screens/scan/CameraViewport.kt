package com.restrusher.volymatcher.ui.screens.scan

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.restrusher.volymatcher.R
import com.restrusher.volymatcher.domain.model.Player

@Composable
fun CameraViewport(
    hasCameraPermission: Boolean,
    accentColor: Color,
    heroFg: Color,
    player: Player?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    if (hasCameraPermission) {
        DisposableEffect(lifecycleOwner) {
            var cameraProvider: ProcessCameraProvider? = null
            val future = ProcessCameraProvider.getInstance(context)
            future.addListener({
                cameraProvider = future.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                runCatching {
                    cameraProvider?.unbindAll()
                    cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                    )
                }
            }, ContextCompat.getMainExecutor(context))
            onDispose { cameraProvider?.unbindAll() }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, accentColor, RoundedCornerShape(18.dp)),
    ) {
        if (hasCameraPermission) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color(0xFF2A2420), Color(0xFF1A1613)))),
            )
        }

        ScanGridOverlay(accentColor = accentColor)
        BodySilhouetteOverlay(accentColor = accentColor)
        ScanLineOverlay(accentColor = accentColor)

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

        if (player != null) {
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
    }
}

@Composable
private fun ScanGridOverlay(accentColor: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val gridColor = accentColor.copy(alpha = 0.25f)
        val hStep = size.height / 20f
        val vStep = size.width / 15f
        repeat(20) { i -> drawLine(gridColor, Offset(0f, i * hStep), Offset(size.width, i * hStep), 0.5f) }
        repeat(15) { i -> drawLine(gridColor, Offset(i * vStep, 0f), Offset(i * vStep, size.height), 0.5f) }
    }
}

@Composable
private fun BodySilhouetteOverlay(accentColor: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
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
}

@Composable
private fun ScanLineOverlay(accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .offset(y = 30.dp)
            .background(Brush.horizontalGradient(listOf(Color.Transparent, accentColor, Color.Transparent))),
    )
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
