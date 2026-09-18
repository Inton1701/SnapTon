package com.inton1701.snapton.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inton1701.snapton.app.AppDestination
import com.inton1701.snapton.core.ui.icon.SnapTonIcons
import com.inton1701.snapton.core.ui.theme.ActiveBlue
import com.inton1701.snapton.core.ui.theme.Hairline
import com.inton1701.snapton.core.ui.theme.PrimaryBlue

@Composable
fun SnapTonBottomBar(
    selected: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp,
            tonalElevation = 0.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Hairline.copy(alpha = 0.65f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BottomItem(AppDestination.Home, selected, onDestinationSelected, Modifier.weight(1f))
                BottomItem(AppDestination.Library, selected, onDestinationSelected, Modifier.weight(1f))
                Spacer(Modifier.width(78.dp))
                BottomItem(AppDestination.Tools, selected, onDestinationSelected, Modifier.weight(1f))
                BottomItem(AppDestination.Settings, selected, onDestinationSelected, Modifier.weight(1f))
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-1).dp),
        ) {
            FloatingActionButton(
                onClick = { onDestinationSelected(AppDestination.Scan) },
                modifier = Modifier.size(62.dp),
                containerColor = ActiveBlue,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp,
                ),
            ) {
                Icon(
                    imageVector = SnapTonIcons.forDestination(AppDestination.Scan),
                    contentDescription = "Open camera scanner",
                    modifier = Modifier.size(29.dp),
                )
            }
            Text(
                text = AppDestination.Scan.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected == AppDestination.Scan) ActiveBlue else PrimaryBlue,
                fontWeight = if (selected == AppDestination.Scan) FontWeight.Bold else FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun RowScope.BottomItem(
    destination: AppDestination,
    selected: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSelected = destination == selected
    NavigationBarItem(
        selected = isSelected,
        onClick = { onDestinationSelected(destination) },
        icon = {
            Icon(
                imageVector = SnapTonIcons.forDestination(destination),
                contentDescription = destination.label,
                modifier = Modifier.size(23.dp),
            )
        },
        label = { Text(destination.label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = ActiveBlue,
            selectedTextColor = ActiveBlue,
            unselectedIconColor = PrimaryBlue,
            unselectedTextColor = PrimaryBlue,
            indicatorColor = Color.Transparent,
        ),
        modifier = modifier,
    )
}
