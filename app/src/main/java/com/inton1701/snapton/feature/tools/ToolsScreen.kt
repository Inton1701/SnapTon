package com.inton1701.snapton.feature.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CallMerge
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.Portrait
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Reorder
import androidx.compose.material.icons.outlined.Slideshow
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ToolsScreen(
    contentPadding: PaddingValues,
    onToolSelected: (OfflineTool) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Text("Tools", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "Convert, recognize, translate, and solve on this device.",
                modifier = Modifier.padding(top = 5.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        ToolCatalog.groups.forEach { group ->
            item(key = group.title) {
                ToolGroupSection(group = group, onToolSelected = onToolSelected)
            }
        }
    }
}

@Composable
private fun ToolGroupSection(group: ToolGroup, onToolSelected: (OfflineTool) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text(group.title, style = MaterialTheme.typography.titleMedium)
        group.tools.chunked(2).forEach { rowTools ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                rowTools.forEach { tool ->
                    ToolCard(
                        tool = tool,
                        onClick = { onToolSelected(tool) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (rowTools.size == 1) androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ToolCard(tool: OfflineTool, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = iconFor(tool.glyph),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = tool.label,
                modifier = Modifier.padding(top = 9.dp),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun iconFor(glyph: ToolGlyph): ImageVector = when (glyph) {
    ToolGlyph.Text -> Icons.AutoMirrored.Outlined.TextSnippet
    ToolGlyph.Word -> Icons.Outlined.Description
    ToolGlyph.Pdf -> Icons.Outlined.PictureAsPdf
    ToolGlyph.IdCard -> Icons.Outlined.Badge
    ToolGlyph.Portrait -> Icons.Outlined.Portrait
    ToolGlyph.QrCode -> Icons.Outlined.QrCodeScanner
    ToolGlyph.Slides -> Icons.Outlined.Slideshow
    ToolGlyph.Images -> Icons.Outlined.Collections
    ToolGlyph.Merge -> Icons.AutoMirrored.Outlined.CallMerge
    ToolGlyph.Split -> Icons.Outlined.ContentCut
    ToolGlyph.Compress -> Icons.Outlined.Compress
    ToolGlyph.Reorder -> Icons.Outlined.Reorder
    ToolGlyph.Rotate -> Icons.AutoMirrored.Outlined.RotateRight
    ToolGlyph.Protect -> Icons.Outlined.Lock
    ToolGlyph.Translate -> Icons.Outlined.Translate
    ToolGlyph.Math -> Icons.Outlined.Calculate
}
