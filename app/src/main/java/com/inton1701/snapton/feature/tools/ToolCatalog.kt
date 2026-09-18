package com.inton1701.snapton.feature.tools

enum class ToolGlyph {
    Text,
    Word,
    Pdf,
    IdCard,
    Portrait,
    QrCode,
    Slides,
    Images,
    Merge,
    Split,
    Compress,
    Reorder,
    Rotate,
    Protect,
    Translate,
    Math,
}

data class OfflineTool(
    val id: String,
    val label: String,
    val glyph: ToolGlyph,
)

data class ToolGroup(
    val title: String,
    val tools: List<OfflineTool>,
)

object ToolCatalog {
    val groups = listOf(
        ToolGroup(
            title = "Scan and recognize",
            tools = listOf(
                OfflineTool("image_text", "Image to text", ToolGlyph.Text),
                OfflineTool("image_word", "Image to Word", ToolGlyph.Word),
                OfflineTool("image_pdf", "Image to PDF", ToolGlyph.Pdf),
                OfflineTool("id_scan", "ID scan", ToolGlyph.IdCard),
                OfflineTool("id_photo", "ID photo", ToolGlyph.Portrait),
                OfflineTool("qr", "QR scanner", ToolGlyph.QrCode),
            ),
        ),
        ToolGroup(
            title = "Convert documents",
            tools = listOf(
                OfflineTool("word_pdf", "Word to PDF", ToolGlyph.Pdf),
                OfflineTool("pdf_word", "PDF to Word", ToolGlyph.Word),
                OfflineTool("ppt_word", "PowerPoint to Word", ToolGlyph.Slides),
                OfflineTool("word_ppt", "Word to PowerPoint", ToolGlyph.Slides),
                OfflineTool("images_pdf", "Images to PDF", ToolGlyph.Images),
                OfflineTool("images_word", "Images to Word", ToolGlyph.Images),
                OfflineTool("merge_pdf", "Merge PDF", ToolGlyph.Merge),
                OfflineTool("split_pdf", "Split PDF", ToolGlyph.Split),
                OfflineTool("compress_pdf", "Compress PDF", ToolGlyph.Compress),
                OfflineTool("reorder_pdf", "Reorder pages", ToolGlyph.Reorder),
                OfflineTool("rotate_pdf", "Rotate pages", ToolGlyph.Rotate),
                OfflineTool("protect_pdf", "Protect PDF", ToolGlyph.Protect),
            ),
        ),
        ToolGroup(
            title = "Language and math",
            tools = listOf(
                OfflineTool("translate", "Translate", ToolGlyph.Translate),
                OfflineTool("math", "Math solver", ToolGlyph.Math),
            ),
        ),
    )
}
