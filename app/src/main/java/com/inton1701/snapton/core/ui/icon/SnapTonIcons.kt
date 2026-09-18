package com.inton1701.snapton.core.ui.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FolderCopy
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.inton1701.snapton.app.AppDestination

object SnapTonIcons {
    fun forDestination(destination: AppDestination): ImageVector = when (destination) {
        AppDestination.Home -> Icons.Outlined.Home
        AppDestination.Library -> Icons.Outlined.FolderCopy
        AppDestination.Scan -> Icons.Outlined.CameraAlt
        AppDestination.Tools -> Icons.Outlined.GridView
        AppDestination.Settings -> Icons.Outlined.Settings
    }
}
