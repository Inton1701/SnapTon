package com.inton1701.snapton.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inton1701.snapton.core.ui.component.SnapTonBottomBar
import com.inton1701.snapton.core.ui.theme.SnapTonTheme
import com.inton1701.snapton.feature.home.HomeScreen
import com.inton1701.snapton.feature.home.ToolId

@Composable
fun SnapTonApp(initialDestination: AppDestination = AppDestination.Home) {
    var destination by rememberSaveable { mutableStateOf(initialDestination) }

    SnapTonTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                SnapTonBottomBar(
                    selected = destination,
                    onDestinationSelected = { destination = it },
                )
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
                else -> DestinationIntro(destination = destination, padding = padding)
            }
        }
    }
}

@Composable
private fun DestinationIntro(destination: AppDestination, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = destination.label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = when (destination) {
                AppDestination.Home -> "Your scans and tools, all on this device."
                AppDestination.Library -> "Find every document stored on this device."
                AppDestination.Scan -> "Point the camera at a document to begin."
                AppDestination.Tools -> "Convert, recognize, translate, and solve offline."
                AppDestination.Settings -> "Control storage, models, privacy, and motion."
            },
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
