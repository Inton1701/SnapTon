package com.inton1701.snapton.feature.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.camera.core.ImageCapture
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.inton1701.snapton.core.ui.theme.ActiveBlue

@Composable
fun CameraEntryScreen(onClose: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionPreferences = remember(context) {
        context.getSharedPreferences("camera_permission", 0)
    }
    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var requestedBefore by remember {
        mutableStateOf(permissionPreferences.getBoolean("requested_before", false))
    }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val permissionState = cameraPermissionState(permissionGranted, requestedBefore)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        requestedBefore = true
        permissionPreferences.edit().putBoolean("requested_before", true).apply()
        permissionGranted = granted
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA,
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(permissionState) {
        if (permissionState == CameraPermissionState.NeedsRequest) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val modes = listOf("Document", "ID card", "Book", "QR")
    var selectedMode by remember { mutableStateOf("Document") }
    val transition = rememberInfiniteTransition(label = "scan-line")
    val scanPosition by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.88f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "scan-position",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF07101F), Color(0xFF162741), Color(0xFF050A12)),
                ),
            ),
    ) {
        if (permissionState == CameraPermissionState.Granted) {
            CameraPreview(
                onImageCaptureReady = { imageCapture = it },
                onCameraReady = {},
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            CameraPermissionMessage(
                permissionState = permissionState,
                onRequestPermission = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onOpenSettings = {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null),
                        ),
                    )
                },
                modifier = Modifier.align(Alignment.Center),
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 26.dp, vertical = 150.dp),
        ) {
            drawRoundRect(
                color = Color.White.copy(alpha = 0.78f),
                style = Stroke(width = 2.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
            )
            val y = size.height * scanPosition
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, Color(0xFF53A0FF), Color.Transparent),
                ),
                start = Offset(12.dp.toPx(), y),
                end = Offset(size.width - 12.dp.toPx(), y),
                strokeWidth = 2.dp.toPx(),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CameraTopButton(Icons.Outlined.Close, "Close scanner", onClose)
            Spacer(Modifier.weight(1f))
            Surface(color = Color.Black.copy(alpha = 0.42f), shape = RoundedCornerShape(16.dp)) {
                Text(
                    text = "AUTO",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            CameraTopButton(Icons.Outlined.FlashOff, "Flash off", {})
            CameraTopButton(Icons.Outlined.MoreVert, "Camera options", {})
        }

        Text(
            text = "Keep the whole page inside the frame",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 78.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.76f))
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(top = 14.dp, bottom = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                modes.forEach { mode ->
                    Text(
                        text = mode,
                        color = if (mode == selectedMode) Color.White else Color.White.copy(alpha = 0.58f),
                        fontWeight = if (mode == selectedMode) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier
                            .clickable { selectedMode = mode }
                            .border(
                                width = if (mode == selectedMode) 1.dp else 0.dp,
                                color = if (mode == selectedMode) ActiveBlue else Color.Transparent,
                                shape = RoundedCornerShape(16.dp),
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.PhotoLibrary, contentDescription = "Import image", tint = Color.White)
                }
                Surface(
                    onClick = {},
                    enabled = imageCapture != null,
                    modifier = Modifier
                        .size(78.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(7.dp),
                    shape = CircleShape,
                    color = ActiveBlue,
                ) {}
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    Text("1", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CameraPermissionMessage(
    permissionState: CameraPermissionState,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = if (permissionState == CameraPermissionState.Denied) {
                "Camera access is off"
            } else {
                "Camera access is needed to scan"
            },
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = if (permissionState == CameraPermissionState.Denied) {
                "Allow camera access in Settings to scan documents."
            } else {
                "SnapTon uses the camera only while the scanner is open."
            },
            color = Color.White.copy(alpha = 0.74f),
            style = MaterialTheme.typography.bodyMedium,
        )
        Surface(
            onClick = if (permissionState == CameraPermissionState.Denied) {
                onOpenSettings
            } else {
                onRequestPermission
            },
            color = ActiveBlue,
            shape = RoundedCornerShape(18.dp),
        ) {
            Text(
                text = if (permissionState == CameraPermissionState.Denied) {
                    "Open Settings"
                } else {
                    "Allow camera"
                },
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun CameraTopButton(icon: androidx.compose.ui.graphics.vector.ImageVector, description: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, contentDescription = description, tint = Color.White)
    }
}
