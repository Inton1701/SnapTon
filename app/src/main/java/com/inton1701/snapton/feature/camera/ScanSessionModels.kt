package com.inton1701.snapton.feature.camera

import java.util.UUID
import com.inton1701.snapton.engine.image.PageCorners

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
