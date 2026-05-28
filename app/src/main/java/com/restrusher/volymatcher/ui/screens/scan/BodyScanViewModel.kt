package com.restrusher.volymatcher.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.restrusher.volymatcher.data.di.RepositoryLocator
import com.restrusher.volymatcher.domain.model.Player
import com.restrusher.volymatcher.domain.usecase.GetPlayerByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

data class BodyScanUiState(
    val player: Player? = null,
    val measurement: ScanMeasurement = ScanMeasurement(),
)

private const val SCAN_FRAME_TARGET = 300
private const val MIN_LIKELIHOOD = 0.5f
private const val AVG_SHOULDER_CM = 45f
private const val BASELINE_FRAMES = 30

private val TRACKED_LANDMARKS = listOf(
    PoseLandmark.NOSE,
    PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER,
    PoseLandmark.LEFT_ELBOW, PoseLandmark.RIGHT_ELBOW,
    PoseLandmark.LEFT_WRIST, PoseLandmark.RIGHT_WRIST,
    PoseLandmark.LEFT_HIP, PoseLandmark.RIGHT_HIP,
    PoseLandmark.LEFT_KNEE, PoseLandmark.RIGHT_KNEE,
    PoseLandmark.LEFT_ANKLE, PoseLandmark.RIGHT_ANKLE,
    PoseLandmark.LEFT_HEEL, PoseLandmark.RIGHT_HEEL,
    PoseLandmark.LEFT_FOOT_INDEX, PoseLandmark.RIGHT_FOOT_INDEX,
)

class BodyScanViewModel(
    private val playerId: String?,
    private val getPlayerByIdUseCase: GetPlayerByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BodyScanUiState())
    val uiState: StateFlow<BodyScanUiState> = _uiState.asStateFlow()

    private val hipYBaseline = mutableListOf<Float>()
    private var minHipY = Float.MAX_VALUE

    init {
        if (!playerId.isNullOrBlank()) {
            viewModelScope.launch {
                _uiState.update { it.copy(player = getPlayerByIdUseCase(playerId)) }
            }
        }
    }

    fun onPoseResult(pose: Pose, imageWidth: Int, imageHeight: Int, rotationDegrees: Int) {
        val landmarks = extractLandmarks(pose, imageWidth, imageHeight, rotationDegrees)
        if (landmarks.isEmpty()) {
            _uiState.update { it.copy(measurement = it.measurement.copy(isPersonDetected = false, landmarks = emptyMap())) }
            return
        }

        val leftShoulder = landmarks[PoseLandmark.LEFT_SHOULDER]
        val rightShoulder = landmarks[PoseLandmark.RIGHT_SHOULDER]
        val shoulderWidthNorm = if (leftShoulder != null && rightShoulder != null)
            abs(rightShoulder.x - leftShoulder.x) else null

        val heightCm = computeHeight(landmarks, shoulderWidthNorm)
        val reachCm = computeReach(landmarks, shoulderWidthNorm)
        val jumpCm = computeJump(landmarks, shoulderWidthNorm, _uiState.value.measurement.jumpCm)

        _uiState.update { state ->
            state.copy(
                measurement = state.measurement.copy(
                    heightCm = heightCm ?: state.measurement.heightCm,
                    reachCm = reachCm ?: state.measurement.reachCm,
                    jumpCm = jumpCm,
                    isPersonDetected = true,
                    framesAnalyzed = (state.measurement.framesAnalyzed + 1).coerceAtMost(SCAN_FRAME_TARGET),
                    landmarks = landmarks,
                )
            )
        }
    }

    private fun computeHeight(
        landmarks: Map<Int, NormalizedLandmark>,
        shoulderWidthNorm: Float?,
    ): Int? {
        if (shoulderWidthNorm == null || shoulderWidthNorm < 0.05f) return null
        val nose = landmarks[PoseLandmark.NOSE] ?: return null
        val footY = listOfNotNull(
            landmarks[PoseLandmark.LEFT_HEEL]?.y,
            landmarks[PoseLandmark.RIGHT_HEEL]?.y,
            landmarks[PoseLandmark.LEFT_FOOT_INDEX]?.y,
            landmarks[PoseLandmark.RIGHT_FOOT_INDEX]?.y,
        ).maxOrNull() ?: return null

        val pxPerCm = shoulderWidthNorm / AVG_SHOULDER_CM
        val headTopY = nose.y - shoulderWidthNorm * 0.35f
        return ((footY - headTopY) / pxPerCm).roundToInt().coerceIn(140, 230)
    }

    private fun computeReach(
        landmarks: Map<Int, NormalizedLandmark>,
        shoulderWidthNorm: Float?,
    ): Int? {
        if (shoulderWidthNorm == null || shoulderWidthNorm < 0.05f) return null
        val leftWrist = landmarks[PoseLandmark.LEFT_WRIST] ?: return null
        val rightWrist = landmarks[PoseLandmark.RIGHT_WRIST] ?: return null
        val pxPerCm = shoulderWidthNorm / AVG_SHOULDER_CM
        return (abs(rightWrist.x - leftWrist.x) / pxPerCm).roundToInt().coerceIn(140, 240)
    }

    private fun computeJump(
        landmarks: Map<Int, NormalizedLandmark>,
        shoulderWidthNorm: Float?,
        prevJumpCm: Int?,
    ): Int? {
        val leftHip = landmarks[PoseLandmark.LEFT_HIP]
        val rightHip = landmarks[PoseLandmark.RIGHT_HIP]
        if (leftHip == null || rightHip == null || shoulderWidthNorm == null || shoulderWidthNorm < 0.05f) return prevJumpCm

        val hipY = (leftHip.y + rightHip.y) / 2f
        if (hipYBaseline.size < BASELINE_FRAMES) {
            hipYBaseline.add(hipY)
            return prevJumpCm
        }

        val baseline = hipYBaseline.average().toFloat()
        if (hipY < minHipY) minHipY = hipY
        val displacementNorm = (baseline - minHipY).coerceAtLeast(0f)
        val pxPerCm = shoulderWidthNorm / AVG_SHOULDER_CM
        val jumpCm = (displacementNorm / pxPerCm).roundToInt().coerceIn(0, 100)
        return if (jumpCm > 0) jumpCm else prevJumpCm
    }

    private fun extractLandmarks(
        pose: Pose,
        imageWidth: Int,
        imageHeight: Int,
        rotationDegrees: Int,
    ): Map<Int, NormalizedLandmark> = buildMap {
        for (type in TRACKED_LANDMARKS) {
            val lm = pose.getPoseLandmark(type) ?: continue
            if (lm.inFrameLikelihood < MIN_LIKELIHOOD) continue
            val (nx, ny) = normalizePoint(lm.position.x, lm.position.y, imageWidth, imageHeight, rotationDegrees)
            put(type, NormalizedLandmark(nx, ny, lm.inFrameLikelihood))
        }
    }

    private fun normalizePoint(
        x: Float, y: Float,
        imageWidth: Int, imageHeight: Int,
        rotationDegrees: Int,
    ): Pair<Float, Float> = when (rotationDegrees) {
        90  -> Pair(1f - y / imageHeight, x / imageWidth)
        180 -> Pair(1f - x / imageWidth, 1f - y / imageHeight)
        270 -> Pair(y / imageHeight, 1f - x / imageWidth)
        else -> Pair(x / imageWidth, y / imageHeight)
    }

    companion object {
        fun factory(playerId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BodyScanViewModel(
                    playerId = playerId,
                    getPlayerByIdUseCase = GetPlayerByIdUseCase(RepositoryLocator.playerRepository),
                )
            }
        }
    }
}
