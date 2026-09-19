package com.inton1701.snapton.feature.camera

enum class CameraPermissionState {
    Granted,
    NeedsRequest,
    Denied,
}

fun cameraPermissionState(granted: Boolean, requestedBefore: Boolean): CameraPermissionState = when {
    granted -> CameraPermissionState.Granted
    requestedBefore -> CameraPermissionState.Denied
    else -> CameraPermissionState.NeedsRequest
}
