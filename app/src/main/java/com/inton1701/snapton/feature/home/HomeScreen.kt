package com.inton1701.snapton.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.inton1701.snapton.core.ui.component.QuickToolTile
import com.inton1701.snapton.core.ui.component.RecentDocumentRow
import com.inton1701.snapton.core.ui.component.SnapTonSearchField
import com.inton1701.snapton.core.ui.theme.ActiveBlue
import com.inton1701.snapton.core.ui.theme.OfflineGreen

@Composable
fun HomeScreen(
    recentDocuments: List<RecentDocument> = emptyList(),
    onToolSelected: (ToolId) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val visibleDocuments = recentDocuments.filter {
        query.isBlank() || it.title.contains(query, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 20.dp,
            top = 18.dp,
            end = 20.dp,
            bottom = 22.dp,
        ),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "SnapTon",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Surface(
                    color = Color.Transparent,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Box(
                            modifier = Modifier.size(7.dp),
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = OfflineGreen,
                                shape = MaterialTheme.shapes.extraLarge,
                            ) {}
                        }
                        Text(
                            text = "Offline",
                            style = MaterialTheme.typography.labelLarge,
                            color = OfflineGreen,
                        )
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            SnapTonSearchField(query = query, onQueryChange = { query = it })
            Spacer(Modifier.height(18.dp))
            SectionHeading(
                title = "Quick tools",
                action = "See all",
                onAction = { onToolSelected(ToolId.AllTools) },
            )
            Spacer(Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(184.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false,
            ) {
                items(HomeCatalog.quickTools, key = { it.id }) { tool ->
                    QuickToolTile(tool = tool, onClick = { onToolSelected(tool.id) })
                }
            }
            Spacer(Modifier.height(18.dp))
            SectionHeading(title = "Recent documents")
        }

        if (visibleDocuments.isEmpty()) {
            item { EmptyDocuments(onScan = { onToolSelected(ToolId.ImageToPdf) }) }
        } else {
            items(visibleDocuments.size, key = { visibleDocuments[it].id }) { index ->
                RecentDocumentRow(
                    document = visibleDocuments[index],
                    onClick = {},
                    onMoreClick = {},
                )
            }
        }
    }
}

@Composable
private fun SectionHeading(
    title: String,
    action: String? = null,
    onAction: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (action != null) {
            TextButton(onClick = onAction) {
                Text(action, color = ActiveBlue)
            }
        }
    }
}

@Composable
private fun EmptyDocuments(onScan: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "No documents yet",
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Scan a page to start your private local library.",
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextButton(onClick = onScan, modifier = Modifier.padding(top = 4.dp)) {
                Text("Scan document")
            }
        }
    }
}
