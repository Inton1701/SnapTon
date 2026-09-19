package com.inton1701.snapton.feature.camera

sealed interface CameraPermissionState {
    data object Granted : CameraPermissionState
    data object NeedsRequest : CameraPermissionState
    data object Denied : CameraPermissionState
}

fun cameraPermissionState(granted: Boolean, requestedBefore: Boolean): CameraPermissionState = when {
    granted -> CameraPermissionState.Granted
    requestedBefore -> CameraPermissionState.Denied
    else -> CameraPermissionState.NeedsRequest
}
