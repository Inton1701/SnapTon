# SnapTon Android Foundation

## Package boundary

The application id and namespace are `com.inton1701.snapton`. The single `app` module is organized by user-facing feature so later scanning engines can be added without turning the Compose app shell into a monolith.

- `app`: root destination state and activity.
- `core/model`: stable product identity and shared models.
- `core/ui`: theme, icons, and reusable controls.
- `feature/home`: the fixed eight-item quick-tool catalog and Home UI.
- `feature/library`: local-document browsing surface.
- `feature/camera`: full-screen camera experience and scan controls.
- `feature/tools`: grouped offline-tool catalog and UI.
- `feature/settings`: privacy, storage, model, and motion choices.

## Navigation

`SnapTonApp` owns one saveable `AppDestination`. Home, Library, Tools, and Settings share `SnapTonBottomBar`; Scan intentionally removes that bar so the capture area uses the full display. The central camera control is visually raised but remains part of the same deterministic destination model.

System navigation insets are included beneath the visible bottom controls. `BottomBarLayout` keeps this calculation unit-testable, preventing gesture or three-button navigation from covering labels.

## Visual identity

The app renders the approved brand asset without modifying its pixels. Android adaptive-icon XML supplies the system mask and themed monochrome form. Compose colors derive from the approved values:

- Primary blue: `#164B9B`
- Active blue: `#0A326F`
- Deep navy: `#0C1D39`

The interface uses quiet off-white backgrounds, white tool surfaces, centered line icons, sentence-case labels, and no emoji.

## Build stability

The local Windows environment can be memory-constrained while Android Studio, Docker, and WSL are active. Gradle therefore uses a 1 GB heap, at most two workers, and an in-process Kotlin compiler. Verification commands additionally use `--max-workers=1` to keep APK and lint builds reliable without stopping unrelated applications.

## Next boundary

The next milestone replaces the camera surface background with CameraX `PreviewView`, adds permission handling, capture persistence, throttled quality analysis, page-corner geometry, automatic batch capture, and manual crop editing. The existing `CameraEntryScreen` remains the visual contract for that engine work.
