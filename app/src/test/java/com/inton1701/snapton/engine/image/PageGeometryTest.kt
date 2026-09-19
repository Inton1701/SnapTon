package com.inton1701.snapton.engine.image

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class PageGeometryTest {
    @Test
    fun unorderedCornersBecomeTopLeftClockwise() {
        val result = PageCorners.fromUnordered(
            listOf(Point(0.8f, 0.8f), Point(0.2f, 0.2f), Point(0.8f, 0.2f), Point(0.2f, 0.8f)),
        )
        assertEquals(Point(0.2f, 0.2f), result.topLeft)
        assertEquals(Point(0.8f, 0.2f), result.topRight)
        assertTrue(result.area() > 0.3f)
    }

    @Test
    fun invalidCornerCountAndTinyPageAreRejected() {
        assertThrows(IllegalArgumentException::class.java) {
            PageCorners.fromUnordered(listOf(Point(0.1f, 0.1f), Point(0.2f, 0.1f)))
        }
        assertThrows(IllegalArgumentException::class.java) {
            PageCorners(Point(0.1f, 0.1f), Point(0.2f, 0.1f), Point(0.2f, 0.2f), Point(0.1f, 0.2f))
        }
    }
}
