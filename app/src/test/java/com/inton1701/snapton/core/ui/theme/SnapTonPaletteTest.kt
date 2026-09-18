package com.inton1701.snapton.core.ui.theme

import com.inton1701.snapton.core.model.Brand
import org.junit.Assert.assertEquals
import org.junit.Test

class SnapTonPaletteTest {
    @Test
    fun navigationAndScanColorsUseApprovedBlues() {
        assertEquals(Brand.Primary, SnapTonPalette.Navigation)
        assertEquals(Brand.Active, SnapTonPalette.Selected)
        assertEquals(Brand.Active, SnapTonPalette.ScanAction)
        assertEquals(Brand.DeepNavy, SnapTonPalette.Heading)
    }
}
