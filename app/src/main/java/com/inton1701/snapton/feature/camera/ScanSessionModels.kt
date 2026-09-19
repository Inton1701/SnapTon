package com.inton1701.snapton.feature.camera

import java.util.UUID

data class PagePoint(val x: Float, val y: Float)

data class PageCorners(
    val topLeft: PagePoint,
    val topRight: PagePoint,
    val bottomRight: PagePoint,
    val bottomLeft: PagePoint,
)

data class CapturedPage(
    val id: String = UUID.randomUUID().toString(),
    val sourcePath: String,
    val corners: PageCorners? = null,
    val needsReview: Boolean = true,
)

data class ScanSessionState(
    val pages: List<CapturedPage> = emptyList(),
    val captureInProgress: Boolean = false,
    val error: String? = null,
)
