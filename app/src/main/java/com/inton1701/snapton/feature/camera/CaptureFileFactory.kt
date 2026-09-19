package com.inton1701.snapton.feature.camera

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

interface CaptureWriter {
    fun capture(imageCapture: ImageCapture?, callback: (Result<String>) -> Unit)
}

class CaptureFileFactory(
    private val context: Context,
    private val sessionId: String = UUID.randomUUID().toString(),
) : CaptureWriter {
    override fun capture(imageCapture: ImageCapture?, callback: (Result<String>) -> Unit) {
        if (imageCapture == null) {
            callback(Result.failure(IllegalArgumentException("Camera capture is unavailable")))
            return
        }
        val outputDirectory = File(context.filesDir, "scans/$sessionId/original")
        if (!outputDirectory.mkdirs() && !outputDirectory.isDirectory) {
            callback(Result.failure(IllegalStateException("Unable to prepare scan storage")))
            return
        }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(Date())
        val outputFile = File(outputDirectory, "${timestamp}_${UUID.randomUUID()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    callback(Result.success(outputFile.absolutePath))
                }

                override fun onError(exception: ImageCaptureException) {
                    outputFile.delete()
                    callback(Result.failure(exception))
                }
            },
        )
    }
}
