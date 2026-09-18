# SnapTon

SnapTon is a privacy-first Android document scanner and offline conversion toolkit. The native Kotlin app uses Jetpack Compose, keeps documents on the device, and does not require an account.

## Current Android build

The current milestone includes:

- Approved SnapTon name and launcher icon.
- Minimal dark-blue Home interface with eight correctly labeled quick tools in two rows.
- Home, Library, raised Scan, Tools, and Settings navigation.
- Full-screen scanner surface with camera-style controls, selectable modes, capture button, framing guide, and animated scan line.
- Complete offline tool catalog for recognition, PDF/image/Office conversion, translation, QR, ID, and math workflows.
- Local-only privacy and backup rules.

Live CameraX capture, automatic crop, filters, OCR, storage, and conversion engines are delivered in the next implementation milestones. The interface does not claim those engines are complete before they are wired.

## Build

Requirements:

- Android Studio with Android SDK 36 installed.
- JDK 17 or newer.

On Windows:

```powershell
.\gradlew.bat --no-daemon --max-workers=1 :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
```

The APK is created at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Install it on a connected Android device:

```powershell
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Offline guarantee

SnapTon has no account, analytics, advertising, or background-upload dependency. Optional OCR and translation model packs will require a one-time explicit download; after installation, supported scanning and processing stays offline.

## Design and plans

- Product design: `docs/superpowers/specs/2026-09-18-snapton-android-design.md`
- Foundation plan: `docs/superpowers/plans/2026-09-18-snapton-foundation.md`
- Foundation architecture: `docs/architecture/foundation.md`
