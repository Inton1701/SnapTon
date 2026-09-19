package com.inton1701.snapton.feature.camera

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CameraPermissionStateTest {
    @Test
    fun permissionStatesUseSealedInterfaceContract() {
        assertTrue(CameraPermissionState::class.java.isInterface)
    }

    @Test
    fun newSessionRequestsPermissionWhenNotGranted() {
        assertEquals(
            CameraPermissionState.NeedsRequest,
            cameraPermissionState(granted = false, requestedBefore = false),
        )
    }

    @Test
    fun denialAfterRequestDoesNotLoopDialog() {
        assertEquals(
            CameraPermissionState.Denied,
            cameraPermissionState(granted = false, requestedBefore = true),
        )
    }

    @Test
    fun grantedPermissionAlwaysShowsCamera() {
        assertEquals(
            CameraPermissionState.Granted,
            cameraPermissionState(granted = true, requestedBefore = true),
        )
    }
}
