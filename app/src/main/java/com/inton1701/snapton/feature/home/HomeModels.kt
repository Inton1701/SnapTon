package com.inton1701.snapton.feature.home

enum class ToolId {
    PdfTools,
    ScanId,
    ImageToText,
    ImageToPdf,
    IdPhoto,
    ImportFiles,
    Translate,
    AllTools,
}

enum class ToolIcon {
    Pdf,
    IdCard,
    ExtractText,
    ImagePdf,
    Portrait,
    Import,
    Translate,
    Grid,
}

data class QuickTool(
    val id: ToolId,
    val label: String,
    val icon: ToolIcon,
)

data class RecentDocument(
    val id: String,
    val title: String,
    val pageCount: Int,
    val modifiedLabel: String,
)
