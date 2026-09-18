package com.inton1701.snapton.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppDestinationTest {
    @Test
    fun bottomNavigationMatchesApprovedOrder() {
        assertEquals(
            listOf("Home", "Library", "Scan", "Tools", "Settings"),
            AppDestination.entries.map { it.label },
        )
    }

    @Test
    fun scanIsTheOnlyPrimaryDestination() {
        assertTrue(AppDestination.Scan.isPrimary)
        assertEquals(1, AppDestination.entries.count { it.isPrimary })
    }
}
