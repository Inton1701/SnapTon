package com.inton1701.snapton.core.ui.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.FolderCopy
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Portrait
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.ui.graphics.vector.ImageVector
import com.inton1701.snapton.app.AppDestination
import com.inton1701.snapton.feature.home.ToolIcon

object SnapTonIcons {
    fun forDestination(destination: AppDestination): ImageVector = when (destination) {
        AppDestination.Home -> Icons.Outlined.Home
        AppDestination.Library -> Icons.Outlined.FolderCopy
        AppDestination.Scan -> Icons.Outlined.CameraAlt
        AppDestination.Tools -> Icons.Outlined.GridView
        AppDestination.Settings -> Icons.Outlined.Settings
    }

    fun forTool(icon: ToolIcon): ImageVector = when (icon) {
        ToolIcon.Pdf -> Icons.Outlined.PictureAsPdf
        ToolIcon.IdCard -> Icons.Outlined.Badge
        ToolIcon.ExtractText -> Icons.Outlined.DocumentScanner
        ToolIcon.ImagePdf -> Icons.Outlined.Image
        ToolIcon.Portrait -> Icons.Outlined.Portrait
        ToolIcon.Import -> Icons.Outlined.FileUpload
        ToolIcon.Translate -> Icons.Outlined.Translate
        ToolIcon.Grid -> Icons.Outlined.GridView
    }
}
