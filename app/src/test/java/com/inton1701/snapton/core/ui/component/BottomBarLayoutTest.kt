package com.inton1701.snapton.core.ui.component

import org.junit.Assert.assertEquals
import org.junit.Test

class BottomBarLayoutTest {
    @Test
    fun navigationInsetIsAddedBelowVisibleControls() {
        assertEquals(116, BottomBarLayout.totalHeightDp(navigationInsetDp = 24))
        assertEquals(96, BottomBarLayout.surfaceHeightDp(navigationInsetDp = 24))
    }

    @Test
    fun negativeInsetsAreIgnored() {
        assertEquals(92, BottomBarLayout.totalHeightDp(navigationInsetDp = -1))
    }
}
