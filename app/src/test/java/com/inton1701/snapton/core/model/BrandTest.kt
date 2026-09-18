package com.inton1701.snapton.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class BrandTest {
    @Test
    fun approvedIdentityIsStable() {
        assertEquals("SnapTon", Brand.AppName)
        assertEquals(0xFF164B9B, Brand.Primary)
        assertEquals(0xFF0A326F, Brand.Active)
        assertEquals(0xFF0C1D39, Brand.DeepNavy)
    }
}
