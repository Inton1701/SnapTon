package com.inton1701.snapton.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.inton1701.snapton.core.model.Brand

object SnapTonPalette {
    const val Navigation = Brand.Primary
    const val Selected = Brand.Active
    const val ScanAction = Brand.Active
    const val Heading = Brand.DeepNavy
    const val Background = 0xFFF6F8FC
    const val Surface = 0xFFFFFFFF
    const val SurfaceSoft = 0xFFEDF3FC
    const val Outline = 0xFFD7E0ED
    const val Muted = 0xFF687A94
    const val Success = 0xFF17864B
}

val PrimaryBlue = Color(SnapTonPalette.Navigation)
val ActiveBlue = Color(SnapTonPalette.Selected)
val DeepNavy = Color(SnapTonPalette.Heading)
val AppBackground = Color(SnapTonPalette.Background)
val SoftSurface = Color(SnapTonPalette.SurfaceSoft)
val Hairline = Color(SnapTonPalette.Outline)
val MutedText = Color(SnapTonPalette.Muted)
val OfflineGreen = Color(SnapTonPalette.Success)
