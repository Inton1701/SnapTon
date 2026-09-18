package com.inton1701.snapton.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.inton1701.snapton.core.ui.component.SnapTonBottomBar
import com.inton1701.snapton.core.ui.theme.SnapTonTheme
import com.inton1701.snapton.feature.camera.CameraEntryScreen
import com.inton1701.snapton.feature.home.HomeScreen
import com.inton1701.snapton.feature.home.ToolId
import com.inton1701.snapton.feature.library.LibraryScreen
import com.inton1701.snapton.feature.settings.SettingsScreen
import com.inton1701.snapton.feature.tools.ToolsScreen

@Composable
fun SnapTonApp(initialDestination: AppDestination = AppDestination.Home) {
    var destination by rememberSaveable { mutableStateOf(initialDestination) }

    SnapTonTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (destination != AppDestination.Scan) {
                    SnapTonBottomBar(
                        selected = destination,
                        onDestinationSelected = { destination = it },
                    )
                }
            },
        ) { padding ->
            when (destination) {
                AppDestination.Home -> HomeScreen(
                    onToolSelected = { tool ->
                        destination = when (tool) {
                            ToolId.ScanId, ToolId.ImageToPdf -> AppDestination.Scan
                            ToolId.AllTools -> AppDestination.Tools
                            else -> AppDestination.Tools
                        }
                    },
                    modifier = Modifier.padding(padding),
                )
                AppDestination.Library -> LibraryScreen(contentPadding = padding)
                AppDestination.Scan -> CameraEntryScreen(onClose = { destination = AppDestination.Home })
                AppDestination.Tools -> ToolsScreen(contentPadding = padding, onToolSelected = {})
                AppDestination.Settings -> SettingsScreen(contentPadding = padding)
            }
        }
    }
}
