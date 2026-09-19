package com.inton1701.snapton.feature.camera

import androidx.camera.core.ImageCapture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ScanSessionController(
    private val writer: CaptureWriter,
) {
    private val _state = MutableStateFlow(ScanSessionState())
    val state: StateFlow<ScanSessionState> = _state.asStateFlow()

    fun capture(imageCapture: ImageCapture?) {
        if (_state.value.captureInProgress) return
        _state.value = _state.value.copy(captureInProgress = true, error = null)
        writer.capture(imageCapture) { result ->
            val current = _state.value
            _state.value = result.fold(
                onSuccess = { path ->
                    current.copy(
                        pages = current.pages + CapturedPage(
                            id = UUID.randomUUID().toString(),
                            sourcePath = path,
                        ),
                        captureInProgress = false,
                        error = null,
                    )
                },
                onFailure = { error ->
                    current.copy(captureInProgress = false, error = error.message ?: "Capture failed")
                },
            )
        }
    }
}
