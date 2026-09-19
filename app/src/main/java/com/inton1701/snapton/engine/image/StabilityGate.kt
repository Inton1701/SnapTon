package com.inton1701.snapton.engine.image

enum class StabilityDecision { Waiting, Ready, Cooldown }

class StabilityGate(
    private val requiredFrames: Int = 5,
    private val maxCornerDisplacement: Float = 0.035f,
    private val cooldownMs: Long = 1_200L,
) {
    private var previousCorners: PageCorners? = null
    private var stableFrames = 0
    private var cooldownUntil = 0L

    fun observe(corners: PageCorners?, quality: FrameQuality, timestampMs: Long): StabilityDecision {
        if (timestampMs < cooldownUntil) return StabilityDecision.Cooldown
        if (corners == null || !quality.isAdequate) {
            previousCorners = null
            stableFrames = 0
            return StabilityDecision.Waiting
        }
        val previous = previousCorners
        stableFrames = if (previous != null && previous.maxDisplacement(corners) <= maxCornerDisplacement) {
            stableFrames + 1
        } else {
            1
        }
        previousCorners = corners
        if (stableFrames >= requiredFrames) {
            stableFrames = 0
            cooldownUntil = timestampMs + cooldownMs
            return StabilityDecision.Ready
        }
        return StabilityDecision.Waiting
    }
}
