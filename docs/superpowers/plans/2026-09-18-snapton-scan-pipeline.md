# SnapTon Scan Pipeline Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the scanner mock surface with a live full-screen CameraX preview, reliable capture, automatic batch page detection, and an editable manual-crop review flow.

**Architecture:** CameraX is isolated behind `CameraController` and feeds immutable `ScanSessionState`. A pure geometry/quality layer evaluates normalized page corners independently of Android image classes; the Compose feature renders the preview and overlays, while capture files remain in app-private storage until explicitly exported.

**Tech Stack:** Kotlin, Jetpack Compose, CameraX 1.6.2, coroutines, Android app-private files, JUnit 4.

**Spec:** `docs/superpowers/specs/2026-09-18-snapton-android-design.md`

## Global Constraints

- Keep the camera preview full-screen and hide normal bottom navigation while scanning.
- Request camera permission only when Scan is opened.
- Use stable CameraX `1.6.2` artifacts from the official AndroidX release channel.
- Use the official OpenCV Android AAR `org.opencv:opencv:4.14.0` for page contours and perspective correction.
- Automatic capture requires four corners, adequate sharpness, and a stable frame; manual capture always remains available.
- Capture each original into app-private storage and never log image bytes or paths containing user data.
- Add pages to a batch without forcing an editor interruption.
- Mark low-confidence edge results for manual review.
- Manual crop has four draggable corners, bounded movement, edge snapping, and animated correction confirmation.
- Minimum touch target is 48 dp, icons contain content descriptions, and motion respects reduced-motion settings.
- Commit directly to `main` as `Inton1701` and push every commit immediately.

---

### Task 1: Camera permission and live CameraX preview

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`
- Modify: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/CameraPermissionState.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/CameraPreview.kt`
- Modify: `app/src/main/java/com/inton1701/snapton/feature/camera/CameraEntryScreen.kt`
- Test: `app/src/test/java/com/inton1701/snapton/feature/camera/CameraPermissionStateTest.kt`

**Interfaces:**
- Produces: `sealed interface CameraPermissionState { Granted; NeedsRequest; Denied }`.
- Produces: `CameraPreview(onImageCaptureReady: (ImageCapture) -> Unit, onCameraReady: (Camera) -> Unit)`.

- [ ] **Step 1: Write the permission decision test**

```kotlin
@Test fun deniedAfterRequestDoesNotLoopPermissionDialog() {
    assertEquals(Denied, cameraPermissionState(granted = false, requestedBefore = true))
}
```

- [ ] **Step 2: Run the focused test and verify it fails because the decision function is absent**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.CameraPermissionStateTest"`

- [ ] **Step 3: Implement the pure decision and CameraX preview**

Add CameraX core, camera2, lifecycle, and view `1.6.2`. Bind `Preview` and `ImageCapture` to the activity lifecycle, set `PreviewView.ScaleType.FILL_CENTER`, and unbind during disposal. Render the preview underneath existing controls only after permission is granted; otherwise show a concise permission action or settings guidance.

- [ ] **Step 4: Run unit tests and assemble**

Run: `./gradlew.bat --no-daemon --max-workers=1 :app:testDebugUnitTest :app:assembleDebug`

- [ ] **Step 5: Install on the connected device, grant permission, and verify a live preview**

Run: `adb install -r app/build/outputs/apk/debug/app-debug.apk`, open Scan, grant Camera, then inspect with `adb shell dumpsys activity top` and a device screenshot.

- [ ] **Step 6: Commit and push**

```bash
git add gradle app
git commit -m "Add live SnapTon camera preview"
git push origin main
```

### Task 2: Capture session and app-private batch files

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/ScanSessionModels.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/CaptureFileFactory.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/ScanSessionController.kt`
- Modify: `app/src/main/java/com/inton1701/snapton/feature/camera/CameraEntryScreen.kt`
- Test: `app/src/test/java/com/inton1701/snapton/feature/camera/ScanSessionControllerTest.kt`

**Interfaces:**
- Produces: `data class CapturedPage(val id: String, val sourcePath: String, val corners: PageCorners?, val needsReview: Boolean)`.
- Produces: `data class ScanSessionState(val pages: List<CapturedPage>, val captureInProgress: Boolean, val error: String?)`.
- Produces: `ScanSessionController.capture(imageCapture: ImageCapture)` and `StateFlow<ScanSessionState>`.

- [ ] **Step 1: Write a failing state test showing two successful captures append in order**

Use a fake capture writer returning `page-1.jpg` then `page-2.jpg`; assert page count and ordering.

- [ ] **Step 2: Run the test and verify the controller is missing**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.ScanSessionControllerTest"`

- [ ] **Step 3: Implement capture state and private file creation**

Write JPEGs beneath `filesDir/scans/<session-id>/original/`, use random UUID filenames, clear `captureInProgress` on both success and error, and preserve prior pages after a failed capture.

- [ ] **Step 4: Wire the shutter and batch counter**

Disable only the shutter while a capture is active, animate a short white flash on success, increment the visible page badge, and leave the live camera open for additional pages.

- [ ] **Step 5: Verify tests and APK, then commit and push**

Run: `./gradlew.bat --no-daemon --max-workers=1 :app:testDebugUnitTest :app:assembleDebug`.

Commit: `Capture pages into a SnapTon batch`, then push `main`.

### Task 3: Pure page geometry and stability gate

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/engine/image/PageGeometry.kt`
- Create: `app/src/main/java/com/inton1701/snapton/engine/image/FrameQuality.kt`
- Create: `app/src/main/java/com/inton1701/snapton/engine/image/StabilityGate.kt`
- Test: `app/src/test/java/com/inton1701/snapton/engine/image/PageGeometryTest.kt`
- Test: `app/src/test/java/com/inton1701/snapton/engine/image/StabilityGateTest.kt`

**Interfaces:**
- Produces: normalized `Point`, ordered `PageCorners(topLeft, topRight, bottomRight, bottomLeft)`, `FrameQuality(sharpness, brightness, glare, cornersConfidence)`, and `StabilityDecision`.
- Produces: `StabilityGate.observe(corners: PageCorners?, quality: FrameQuality, timestampMs: Long): StabilityDecision`.

- [ ] **Step 1: Write failing tests for corner ordering, bounds, and stability**

Assert that shuffled points are ordered clockwise, out-of-range coordinates are rejected, blur prevents auto-capture, and five sufficiently similar frames produce `Ready` exactly once.

- [ ] **Step 2: Run tests and verify missing geometry types**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.PageGeometryTest" --tests "*.StabilityGateTest"`

- [ ] **Step 3: Implement normalized geometry and the stability state machine**

Use normalized coordinates `[0,1]`, polygon area to reject tiny documents, maximum corner displacement for stability, and a cooldown after `Ready` so one page cannot auto-capture twice.

- [ ] **Step 4: Run all unit tests, commit, and push**

Commit: `Add scan geometry and stability gate`, then push `main`.

### Task 4: Frame analysis and animated edge overlay

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/DocumentFrameAnalyzer.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/PageEdgeOverlay.kt`
- Create: `app/src/main/java/com/inton1701/snapton/engine/image/OpenCvPageDetector.kt`
- Create: `app/src/main/java/com/inton1701/snapton/engine/image/QuadrilateralScorer.kt`
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`
- Modify: `app/src/main/java/com/inton1701/snapton/feature/camera/CameraPreview.kt`
- Modify: `app/src/main/java/com/inton1701/snapton/feature/camera/CameraEntryScreen.kt`
- Test: `app/src/test/java/com/inton1701/snapton/feature/camera/AnalyzerThrottleTest.kt`
- Test: `app/src/test/java/com/inton1701/snapton/engine/image/QuadrilateralScorerTest.kt`

**Interfaces:**
- Produces: `DocumentFrameAnalyzer(analyze: (FrameAnalysis) -> Unit)` with every proxy closed in `finally`.
- Produces: `PageEdgeOverlay(corners: PageCorners?, confidence: Float, reducedMotion: Boolean)`.
- Produces: `OpenCvPageDetector.detect(grayscale: Mat): DetectionResult` with ordered corners and a confidence score.

- [ ] **Step 1: Write a failing throttle test**

Feed timestamps at 0, 20, 80, and 140 ms with a 100 ms interval; assert only 0 and 140 are analyzed.

- [ ] **Step 2: Write quadrilateral scoring tests**

Score an in-bounds page-sized rectangle above a tiny or concave quadrilateral, and reject polygons whose ordered corner count is not four.

- [ ] **Step 3: Implement throttling, quality analysis, and real OpenCV page detection**

Calculate brightness and Laplacian sharpness from the Y plane. Downscale the grayscale frame, normalize illumination with CLAHE, blur, apply adaptive Canny edges and morphological close, then rank `approxPolyDP` four-point contours by area, convexity, angle quality, border clearance, and edge strength. Map the winning ordered quadrilateral back to normalized preview coordinates; return no corners below the documented confidence threshold.

- [ ] **Step 4: Replace the fixed frame with an animated corner overlay**

Interpolate displayed corners toward new detections, color the line blue only when stable, render plain-language blur/low-light guidance, and use a short fade instead when reduced motion is enabled.

- [ ] **Step 5: Run tests, lint, assemble, commit, and push**

Commit: `Analyze camera frames and animate page edges`, then push `main`.

### Task 5: Batch review and manual crop geometry

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/feature/crop/CropModels.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/crop/CropGeometry.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/crop/BatchReviewScreen.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/crop/ManualCropScreen.kt`
- Modify: `app/src/main/java/com/inton1701/snapton/app/SnapTonApp.kt`
- Test: `app/src/test/java/com/inton1701/snapton/feature/crop/CropGeometryTest.kt`

**Interfaces:**
- Produces: `moveCorner(corners, corner, target): PageCorners` clamped to image bounds without crossing adjacent edges.
- Produces: `BatchReviewScreen(pages, onEdit, onRetake, onDelete, onReorder, onDone)`.
- Produces: `ManualCropScreen(page, onApply, onCancel)`.

- [ ] **Step 1: Write failing crop-boundary tests**

Assert drag targets clamp to `[0,1]`, corner order remains valid, and the minimum crop area is enforced.

- [ ] **Step 2: Implement the pure crop geometry**

Return the previous valid polygon when a drag would cross an edge or shrink below minimum area; expose snap distance as a normalized constant.

- [ ] **Step 3: Build batch review and four-handle editor**

Show every page thumbnail, visibly mark `needsReview`, support selection/reordering, and open a full-page crop editor. Handles are at least 48 dp; a magnified circular loupe follows the active handle; applying animates corners to the corrected rectangle.

- [ ] **Step 4: Wire the camera batch badge to review without interrupting capture**

Tapping the badge opens review; the shutter continues adding pages otherwise. Closing review returns to the same scan session.

- [ ] **Step 5: Run full verification on the connected phone, commit, and push**

Run tests, lint, assemble, install, capture screenshots of camera, review, and crop. Commit: `Add batch review and manual crop`, then push `main`.

## Acceptance Check

- Opening Scan requests camera access once and then displays the live rear camera.
- The bottom navigation is absent on camera, review, and crop screens.
- Manual capture creates real JPEG files in app-private storage and increments the batch.
- Automatic capture cannot fire from absent or low-confidence geometry.
- Batch review never discards an original when detection or processing fails.
- Manual crop geometry remains a valid in-bounds quadrilateral under every drag tested.
- `testDebugUnitTest`, `lintDebug`, and `assembleDebug` pass before the final push.
