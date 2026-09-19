package com.inton1701.snapton.engine.image

data class FrameQuality(
    val sharpness: Float,
    val brightness: Float,
    val glare: Float,
    val cornersConfidence: Float,
) {
    val isAdequate: Boolean
        get() = sharpness >= 0.35f && brightness in 0.08f..0.95f && glare <= 0.65f && cornersConfidence >= 0.60f
}
