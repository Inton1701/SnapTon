package com.inton1701.snapton.engine.image

import org.junit.Assert.assertEquals
import org.junit.Test

class StabilityGateTest {
    private val corners = PageCorners(Point(0.2f, 0.2f), Point(0.8f, 0.2f), Point(0.8f, 0.8f), Point(0.2f, 0.8f))
    private val quality = FrameQuality(0.8f, 0.5f, 0.1f, 0.9f)

    @Test
    fun blurNeverBecomesReady() {
        val gate = StabilityGate()
        repeat(8) { timestamp ->
            assertEquals(StabilityDecision.Waiting, gate.observe(corners, quality.copy(sharpness = 0.1f), timestamp * 100L))
        }
    }

    @Test
    fun fiveStableFramesBecomeReadyOnceThenCooldown() {
        val gate = StabilityGate()
        repeat(4) { index -> assertEquals(StabilityDecision.Waiting, gate.observe(corners, quality, index * 100L)) }
        assertEquals(StabilityDecision.Ready, gate.observe(corners, quality, 400L))
        assertEquals(StabilityDecision.Cooldown, gate.observe(corners, quality, 500L))
        assertEquals(StabilityDecision.Waiting, gate.observe(corners, quality, 1_700L))
    }
}
