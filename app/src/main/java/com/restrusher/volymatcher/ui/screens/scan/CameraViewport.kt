package com.restrusher.volymatcher.ui.screens.scan

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.restrusher.volymatcher.R
import com.restrusher.volymatcher.domain.model.Player
import java.util.concurrent.Executors

private val SKELETON_CONNECTIONS = listOf(
    PoseLandmark.LEFT_SHOULDER  to PoseLandmark.RIGHT_SHOULDER,
    PoseLandmark.LEFT_SHOULDER  to PoseLandmark.LEFT_ELBOW,
    PoseLandmark.LEFT_ELBOW     to PoseLandmark.LEFT_WRIST,
    PoseLandmark.RIGHT_SHOULDER to PoseLandmark.RIGHT_ELBOW,
    PoseLandmark.RIGHT_ELBOW    to PoseLandmark.RIGHT_WRIST,
    PoseLandmark.LEFT_SHOULDER  to PoseLandmark.LEFT_HIP,
    PoseLandmark.RIGHT_SHOULDER to PoseLandmark.RIGHT_HIP,
    PoseLandmark.LEFT_HIP       to PoseLandmark.RIGHT_HIP,
    PoseLandmark.LEFT_HIP       to PoseLandmark.LEFT_KNEE,
    PoseLandmark.LEFT_KNEE      to PoseLandmark.LEFT_ANKLE,
    PoseLandmark.RIGHT_HIP      to PoseLandmark.RIGHT_KNEE,
    PoseLandmark.RIGHT_KNEE     to PoseLandmark.RIGHT_ANKLE,
)

@Composable
fun CameraViewport(
    hasCameraPermission: Boolean,
    accentColor: Color,
    heroFg: Color,
    player: Player?,
    measurement: ScanMeasurement,
    onPoseResult: (Pose, Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnPoseResult by rememberUpdatedState(onPoseResult)

    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = android.view.ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    if (hasCameraPermission) {
        DisposableEffect(lifecycleOwner) {
            val executor = Executors.newSingleThreadExecutor()
            val detector = PoseDetection.getClient(
                PoseDetectorOptions.Builder()
                    .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                    .build()
            )
            var cameraProvider: ProcessCameraProvider? = null
            ProcessCameraProvider.getInstance(context).also { future ->
                future.addListener({
                    cameraProvider = future.get()
                    val preview = Preview.Builder().build()
                        .also { it.setSurfaceProvider(previewView.surfaceProvider) }
                    val analysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { it.setAnalyzer(executor, PoseAnalyzer(detector) { pose, w, h, r -> currentOnPoseResult(pose, w, h, r) }) }
                    runCatching {
                        cameraProvider?.unbindAll()
                        cameraProvider?.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
                    }
                }, ContextCompat.getMainExecutor(context))
            }
            onDispose {
                cameraProvider?.unbindAll()
                executor.shutdown()
                detector.close()
            }
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

        if (measurement.isPersonDetected && measurement.landmarks.isNotEmpty()) {
            PoseSkeletonOverlay(landmarks = measurement.landmarks, accentColor = accentColor)
        } else {
            BodySilhouetteOverlay(accentColor = accentColor)
        }

        ScanLineOverlay(accentColor = accentColor)

        CornerBracket(Modifier.align(Alignment.TopStart).padding(10.dp), accentColor, isTop = true,  isLeft = true)
        CornerBracket(Modifier.align(Alignment.TopEnd).padding(10.dp),   accentColor, isTop = true,  isLeft = false)
        CornerBracket(Modifier.align(Alignment.BottomStart).padding(10.dp), accentColor, isTop = false, isLeft = true)
        CornerBracket(Modifier.align(Alignment.BottomEnd).padding(10.dp),   accentColor, isTop = false, isLeft = false)

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1A1613).copy(alpha = 0.75f))
                .padding(horizontal = 8.dp, vertical = 5.dp),
        ) {
            Text(stringResource(R.string.scan_capturing), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text("FRAME ${measurement.framesAnalyzed}/300", style = MaterialTheme.typography.labelSmall, color = heroFg.copy(alpha = 0.8f))
        }

        val displayHeight = measurement.heightCm ?: player?.height
        val displayReach = measurement.reachCm ?: player?.reach
        if (displayHeight != null || displayReach != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF1A1613).copy(alpha = 0.75f))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalAlignment = Alignment.End,
            ) {
                displayHeight?.let { Text("H  $it cm", style = MaterialTheme.typography.labelSmall, color = accentColor) }
                player?.weight?.let { Text("W  $it kg", style = MaterialTheme.typography.labelSmall, color = accentColor) }
                displayReach?.let { Text("REACH  $it", style = MaterialTheme.typography.labelSmall, color = accentColor) }
            }
        }
    }
}

@Composable
private fun PoseSkeletonOverlay(
    landmarks: Map<Int, NormalizedLandmark>,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        for ((from, to) in SKELETON_CONNECTIONS) {
            val a = landmarks[from] ?: continue
            val b = landmarks[to] ?: continue
            drawLine(
                color = accentColor.copy(alpha = 0.75f),
                start = Offset(a.x * size.width, a.y * size.height),
                end = Offset(b.x * size.width, b.y * size.height),
                strokeWidth = 2.dp.toPx(),
            )
        }
        for ((_, lm) in landmarks) {
            drawCircle(
                color = accentColor,
                radius = 4.dp.toPx(),
                center = Offset(lm.x * size.width, lm.y * size.height),
            )
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
    val transition = rememberInfiniteTransition(label = "scan_line")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scan_line_y",
    )
    Canvas(modifier = modifier.fillMaxSize()) {
        val y = size.height * progress
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, accentColor.copy(alpha = 0.85f), Color.Transparent),
                startX = 0f,
                endX = size.width,
            ),
            topLeft = Offset(0f, y - 1.dp.toPx()),
            size = Size(size.width, 2.dp.toPx()),
        )
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
