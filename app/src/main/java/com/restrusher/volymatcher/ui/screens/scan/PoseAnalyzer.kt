package com.restrusher.volymatcher.ui.screens.scan

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector

class PoseAnalyzer(
    private val detector: PoseDetector,
    private val onPoseDetected: (Pose, Int, Int, Int) -> Unit,
) : ImageAnalysis.Analyzer {

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run { imageProxy.close(); return }
        val rotation = imageProxy.imageInfo.rotationDegrees
        val width = imageProxy.width
        val height = imageProxy.height
        val inputImage = InputImage.fromMediaImage(mediaImage, rotation)
        detector.process(inputImage)
            .addOnSuccessListener { pose -> onPoseDetected(pose, width, height, rotation) }
            .addOnCompleteListener { imageProxy.close() }
    }
}
