package com.inton1701.snapton.feature.tools

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ToolCatalogTest {
    @Test
    fun requiredOfflineToolsAreExposed() {
        val labels = ToolCatalog.groups.flatMap { it.tools }.map { it.label }.toSet()

        assertTrue(
            labels.containsAll(
                setOf(
                    "Image to text",
                    "Image to Word",
                    "Image to PDF",
                    "ID scan",
                    "ID photo",
                    "QR scanner",
                    "Math solver",
                    "Word to PDF",
                    "PDF to Word",
                    "PowerPoint to Word",
                    "Word to PowerPoint",
                    "Translate",
                ),
            ),
        )
    }

    @Test
    fun toolsUseThreeTaskBasedGroups() {
        assertEquals(
            listOf("Scan and recognize", "Convert documents", "Language and math"),
            ToolCatalog.groups.map { it.title },
        )
    }
}
