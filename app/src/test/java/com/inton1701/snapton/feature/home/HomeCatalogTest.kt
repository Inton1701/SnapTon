package com.inton1701.snapton.feature.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class HomeCatalogTest {
    @Test
    fun quickToolsHaveTwoCompleteRowsInApprovedOrder() {
        assertEquals(8, HomeCatalog.quickTools.size)
        assertEquals(
            listOf(
                "PDF tools",
                "Scan ID",
                "Image to text",
                "Image to PDF",
                "ID photo",
                "Import files",
                "Translate",
                "All tools",
            ),
            HomeCatalog.quickTools.map { it.label },
        )
        assertEquals(listOf(4, 4), HomeCatalog.quickTools.chunked(4).map { it.size })
    }

    @Test
    fun translateUsesItsOwnSemanticIcon() {
        val translate = HomeCatalog.quickTools.single { it.id == ToolId.Translate }
        val pdf = HomeCatalog.quickTools.single { it.id == ToolId.PdfTools }

        assertEquals(ToolIcon.Translate, translate.icon)
        assertNotEquals(pdf.icon, translate.icon)
    }
}
