package com.restrusher.volymatcher.ui.screens.scan

data class NormalizedLandmark(
    val x: Float,
    val y: Float,
    val likelihood: Float,
)

data class ScanMeasurement(
    val heightCm: Int? = null,
    val reachCm: Int? = null,
    val jumpCm: Int? = null,
    val isPersonDetected: Boolean = false,
    val framesAnalyzed: Int = 0,
    val landmarks: Map<Int, NormalizedLandmark> = emptyMap(),
)
