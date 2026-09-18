package com.inton1701.snapton.feature.home

object HomeCatalog {
    val quickTools = listOf(
        QuickTool(ToolId.PdfTools, "PDF tools", ToolIcon.Pdf),
        QuickTool(ToolId.ScanId, "Scan ID", ToolIcon.IdCard),
        QuickTool(ToolId.ImageToText, "Image to text", ToolIcon.ExtractText),
        QuickTool(ToolId.ImageToPdf, "Image to PDF", ToolIcon.ImagePdf),
        QuickTool(ToolId.IdPhoto, "ID photo", ToolIcon.Portrait),
        QuickTool(ToolId.ImportFiles, "Import files", ToolIcon.Import),
        QuickTool(ToolId.Translate, "Translate", ToolIcon.Translate),
        QuickTool(ToolId.AllTools, "All tools", ToolIcon.Grid),
    )
}
