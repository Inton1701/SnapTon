package com.inton1701.snapton.feature.camera

import androidx.camera.core.ImageCapture
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScanSessionControllerTest {
    @Test
    fun successfulCapturesAppendInOrder() {
        val writer = FakeCaptureWriter(listOf(Result.success("page-1.jpg"), Result.success("page-2.jpg")))
        val controller = ScanSessionController(writer)
        controller.capture(null)
        controller.capture(null)

        assertEquals(listOf("page-1.jpg", "page-2.jpg"), controller.state.value.pages.map { it.sourcePath })
        assertFalse(controller.state.value.captureInProgress)
        assertEquals(null, controller.state.value.error)
    }

    @Test
    fun failedCapturePreservesPriorPages() {
        val writer = FakeCaptureWriter(listOf(Result.success("page-1.jpg"), Result.failure(IllegalStateException("disk full"))))
        val controller = ScanSessionController(writer)
        controller.capture(null)
        controller.capture(null)

        assertEquals(listOf("page-1.jpg"), controller.state.value.pages.map { it.sourcePath })
        assertTrue(controller.state.value.error!!.contains("disk full"))
        assertFalse(controller.state.value.captureInProgress)
    }

    private class FakeCaptureWriter(private val results: List<Result<String>>) : CaptureWriter {
        private var index = 0

        override fun capture(imageCapture: ImageCapture?, callback: (Result<String>) -> Unit) {
            callback(results[index++])
        }
    }
}
