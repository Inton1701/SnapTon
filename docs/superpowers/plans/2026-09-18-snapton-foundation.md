# SnapTon Foundation and App Shell Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a runnable native Android SnapTon application with approved branding, a polished dark-blue Compose design system, the two-row Home tool grid, local recent documents, and complete bottom navigation.

**Architecture:** A single Android app module uses feature-oriented Kotlin packages and Jetpack Compose. Pure Kotlin catalog and navigation models are kept independent from Android so their ordering, labels, and icon semantics can be unit-tested; Compose screens render those models through reusable core UI components.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Android Gradle Plugin, JUnit 4, AndroidX lifecycle, immutable screen state.

**Spec:** `docs/superpowers/specs/2026-09-18-snapton-android-design.md`

## Global Constraints

- Use native Kotlin and Jetpack Compose.
- Minimum Android 8.0 (API 26); compile and target Android API 36 in the current build environment.
- Use the approved name `SnapTon` and artwork `branding/snapton-app-icon-final.png`.
- Use primary `#164B9B`, active `#0A326F`, and deep navy `#0C1D39`.
- Use vector line icons; do not use emoji.
- Home quick tools are exactly two centered rows of four, with icon and centered label only.
- Bottom navigation order is Home, Library, raised Scan, Tools, Settings.
- Installed features must work without an account or network.
- Minimum touch target is 48 dp and every non-text control has a content description.
- Commit directly to `main` as `Inton1701` and push each commit to GitHub immediately.

---

## File Structure

- `settings.gradle.kts`: repository and module declarations.
- `build.gradle.kts`: root Android and Kotlin plugin aliases.
- `gradle/libs.versions.toml`: one source of dependency and plugin versions.
- `gradle.properties`: AndroidX, Kotlin, and Gradle memory settings.
- `app/build.gradle.kts`: application configuration, Compose, and tests.
- `app/src/main/AndroidManifest.xml`: SnapTon application and launcher activity.
- `app/src/main/java/com/inton1701/snapton/MainActivity.kt`: Android entry point only.
- `app/src/main/java/com/inton1701/snapton/app/SnapTonApp.kt`: root Compose navigation state and scaffold.
- `app/src/main/java/com/inton1701/snapton/app/AppDestination.kt`: typed bottom destinations and selection rules.
- `app/src/main/java/com/inton1701/snapton/core/model/Brand.kt`: stable brand values available to local unit tests.
- `app/src/main/java/com/inton1701/snapton/core/ui/theme/*`: colors, type, shapes, and Material theme.
- `app/src/main/java/com/inton1701/snapton/core/ui/icon/SnapTonIcons.kt`: semantic icon mapping using Material vector icons.
- `app/src/main/java/com/inton1701/snapton/core/ui/component/*`: search field, section heading, tool tile, document row, and bottom bar.
- `app/src/main/java/com/inton1701/snapton/feature/home/*`: Home models, fixed tool catalog, sample local document state, and Home UI.
- `app/src/main/java/com/inton1701/snapton/feature/library/LibraryScreen.kt`: library browsing empty/recent state.
- `app/src/main/java/com/inton1701/snapton/feature/tools/ToolsScreen.kt`: complete grouped offline-tool catalog.
- `app/src/main/java/com/inton1701/snapton/feature/settings/SettingsScreen.kt`: offline/privacy/accessibility settings UI.
- `app/src/test/java/com/inton1701/snapton/*`: pure unit tests for brand, catalog, and navigation invariants.
- `app/src/androidTest/java/com/inton1701/snapton/HomeScreenTest.kt`: Compose semantics check for the approved Home surface.

### Task 1: Buildable Android project and brand contract

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle/libs.versions.toml`
- Create: `gradle.properties`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/inton1701/snapton/core/model/Brand.kt`
- Create: `app/src/test/java/com/inton1701/snapton/core/model/BrandTest.kt`
- Create: `app/src/main/res/values/strings.xml`
- Create: `app/src/main/res/drawable-nodpi/snapton_brand_source.png`
- Create: `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
- Create: `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`

**Interfaces:**
- Produces: `object Brand { const val AppName: String; val Primary: Long; val Active: Long; val DeepNavy: Long }`.
- Produces: Android application id and Kotlin namespace `com.inton1701.snapton`.

- [ ] **Step 1: Write the failing brand test**

```kotlin
class BrandTest {
    @Test fun approved_identity_is_stable() {
        assertEquals("SnapTon", Brand.AppName)
        assertEquals(0xFF164B9B, Brand.Primary)
        assertEquals(0xFF0A326F, Brand.Active)
        assertEquals(0xFF0C1D39, Brand.DeepNavy)
    }
}
```

- [ ] **Step 2: Add the Gradle files and run the focused test to prove it fails**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.BrandTest"`

Expected: compilation fails because `Brand` does not exist.

- [ ] **Step 3: Add the minimum brand model and Android manifest**

```kotlin
object Brand {
    const val AppName = "SnapTon"
    const val Primary = 0xFF164B9B
    const val Active = 0xFF0A326F
    const val DeepNavy = 0xFF0C1D39
}
```

Copy the approved PNG byte-for-byte to `snapton_brand_source.png`; reference it from adaptive foreground/background resources without inserting text or another mark.

- [ ] **Step 4: Generate the Gradle wrapper and run the focused test**

Run: cached Gradle 8.13 `gradle.bat wrapper --gradle-version 8.13`, then `./gradlew.bat :app:testDebugUnitTest --tests "*.BrandTest"`.

Expected: `BUILD SUCCESSFUL` and one passing test.

- [ ] **Step 5: Commit and push**

```bash
git add settings.gradle.kts build.gradle.kts gradle gradle.properties app branding
git commit -m "Build SnapTon Android foundation"
git push origin main
```

### Task 2: Dark-blue Compose design system

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/theme/Color.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/theme/Type.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/theme/Shape.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/theme/SnapTonTheme.kt`
- Create: `app/src/test/java/com/inton1701/snapton/core/ui/theme/SnapTonPaletteTest.kt`

**Interfaces:**
- Consumes: `Brand.Primary`, `Brand.Active`, and `Brand.DeepNavy`.
- Produces: `SnapTonTheme(darkTheme: Boolean, content: @Composable () -> Unit)` and `SnapTonPalette` semantic colors.

- [ ] **Step 1: Write the palette contract test**

```kotlin
@Test fun navigation_and_scan_colors_use_approved_blues() {
    assertEquals(Brand.Primary, SnapTonPalette.navigation)
    assertEquals(Brand.Active, SnapTonPalette.selected)
    assertEquals(Brand.Active, SnapTonPalette.scanAction)
}
```

- [ ] **Step 2: Run the focused test and verify failure**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.SnapTonPaletteTest"`

Expected: compilation fails because `SnapTonPalette` does not exist.

- [ ] **Step 3: Implement the palette and theme**

Use `Color(0xFF164B9B)` for unselected navigation icons, `Color(0xFF0A326F)` for selected controls and the raised Scan action, `Color(0xFF0C1D39)` for headings, off-white `Color(0xFFF7F9FC)` for app background, and 14–18 dp rounded surfaces. Use system typography with clear weight hierarchy and no decorative font dependency.

- [ ] **Step 4: Run unit tests**

Run: `./gradlew.bat :app:testDebugUnitTest`

Expected: all tests pass.

- [ ] **Step 5: Commit and push**

```bash
git add app/src/main/java/com/inton1701/snapton/core/ui app/src/test/java/com/inton1701/snapton/core/ui
git commit -m "Add SnapTon visual system"
git push origin main
```

### Task 3: Typed app destinations and bottom navigation

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/app/AppDestination.kt`
- Create: `app/src/main/java/com/inton1701/snapton/app/SnapTonApp.kt`
- Create: `app/src/main/java/com/inton1701/snapton/MainActivity.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/icon/SnapTonIcons.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/component/SnapTonBottomBar.kt`
- Create: `app/src/test/java/com/inton1701/snapton/app/AppDestinationTest.kt`

**Interfaces:**
- Produces: `enum class AppDestination(val label: String, val isPrimary: Boolean)` with `Home`, `Library`, `Scan`, `Tools`, `Settings`.
- Produces: `SnapTonApp(initialDestination: AppDestination = AppDestination.Home)`.
- Produces: `SnapTonIcons.forDestination(destination: AppDestination): ImageVector`.

- [ ] **Step 1: Write destination ordering tests**

```kotlin
@Test fun bottom_navigation_matches_approved_order() {
    assertEquals(
        listOf("Home", "Library", "Scan", "Tools", "Settings"),
        AppDestination.entries.map { it.label },
    )
    assertTrue(AppDestination.Scan.isPrimary)
    assertEquals(1, AppDestination.entries.count { it.isPrimary })
}
```

- [ ] **Step 2: Run the test and verify failure**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.AppDestinationTest"`

Expected: compilation fails because `AppDestination` does not exist.

- [ ] **Step 3: Implement destinations, semantic icons, and scaffold**

Use distinct icons: house for Home, document library for Library, camera for Scan, grid for Tools, and gear for Settings. Render Scan as a 64 dp raised circle centered over the navigation bar. Selecting Scan opens the camera destination surface; selecting another item swaps body content while preserving bottom selection.

- [ ] **Step 4: Run unit tests and assemble the app**

Run: `./gradlew.bat :app:testDebugUnitTest :app:assembleDebug`

Expected: all tests pass and `app-debug.apk` is produced.

- [ ] **Step 5: Commit and push**

```bash
git add app/src/main
git add app/src/test/java/com/inton1701/snapton/app
git commit -m "Add SnapTon app navigation"
git push origin main
```

### Task 4: Approved Home tool catalog and recent documents

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/feature/home/HomeModels.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/home/HomeCatalog.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/home/HomeScreen.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/component/SnapTonSearchField.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/component/QuickToolTile.kt`
- Create: `app/src/main/java/com/inton1701/snapton/core/ui/component/RecentDocumentRow.kt`
- Create: `app/src/test/java/com/inton1701/snapton/feature/home/HomeCatalogTest.kt`
- Create: `app/src/androidTest/java/com/inton1701/snapton/HomeScreenTest.kt`

**Interfaces:**
- Produces: `data class QuickTool(val id: ToolId, val label: String, val icon: ToolIcon)`.
- Produces: `enum class ToolId { PDF_TOOLS, SCAN_ID, IMAGE_TO_TEXT, IMAGE_TO_PDF, ID_PHOTO, IMPORT_FILES, TRANSLATE, ALL_TOOLS }`.
- Produces: `HomeCatalog.quickTools: List<QuickTool>` and `HomeScreen(onToolSelected: (ToolId) -> Unit)`.

- [ ] **Step 1: Write the exact catalog test**

```kotlin
@Test fun quick_tools_have_two_complete_rows_in_approved_order() {
    assertEquals(8, HomeCatalog.quickTools.size)
    assertEquals(
        listOf("PDF tools", "Scan ID", "Image to text", "Image to PDF",
            "ID photo", "Import files", "Translate", "All tools"),
        HomeCatalog.quickTools.map { it.label },
    )
    assertEquals(2, HomeCatalog.quickTools.chunked(4).size)
    assertEquals(4, HomeCatalog.quickTools.chunked(4).last().size)
}
```

- [ ] **Step 2: Run the focused test and verify failure**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.HomeCatalogTest"`

Expected: compilation fails because `HomeCatalog` does not exist.

- [ ] **Step 3: Implement models, catalog, and UI**

Each 80–96 dp tile contains one correct line icon centered above one centered label and no description. Use a two-row `LazyVerticalGrid` fixed to four columns on phones, with subtle white surfaces and blue icons. Below it, render local recent-document rows with page count and modified time; above it, render a search field and a compact SnapTon heading.

- [ ] **Step 4: Add the Compose semantics assertion**

```kotlin
@Test fun home_shows_all_approved_tools_without_descriptions() {
    composeRule.setContent { SnapTonTheme { HomeScreen(onToolSelected = {}) } }
    HomeCatalog.quickTools.forEach { composeRule.onNodeWithText(it.label).assertIsDisplayed() }
    composeRule.onNodeWithText("Convert and edit").assertDoesNotExist()
}
```

- [ ] **Step 5: Run unit tests and compile instrumented tests**

Run: `./gradlew.bat :app:testDebugUnitTest :app:compileDebugAndroidTestKotlin`

Expected: unit tests pass and Android test sources compile.

- [ ] **Step 6: Commit and push**

```bash
git add app/src/main/java/com/inton1701/snapton/feature/home app/src/main/java/com/inton1701/snapton/core/ui/component app/src/test app/src/androidTest
git commit -m "Build SnapTon home experience"
git push origin main
```

### Task 5: Library, Tools, Settings, and scan entry surfaces

**Files:**
- Create: `app/src/main/java/com/inton1701/snapton/feature/library/LibraryScreen.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/tools/ToolCatalog.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/tools/ToolsScreen.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/settings/SettingsScreen.kt`
- Create: `app/src/main/java/com/inton1701/snapton/feature/camera/CameraEntryScreen.kt`
- Create: `app/src/test/java/com/inton1701/snapton/feature/tools/ToolCatalogTest.kt`

**Interfaces:**
- Produces: `ToolCatalog.groups: List<ToolGroup>` grouped as Scan and recognize, Convert documents, Language and math.
- Produces: four composables wired into `SnapTonApp`: `LibraryScreen`, `ToolsScreen`, `SettingsScreen`, and `CameraEntryScreen`.

- [ ] **Step 1: Write the complete tool coverage test**

```kotlin
@Test fun required_offline_tools_are_exposed() {
    val labels = ToolCatalog.groups.flatMap { it.tools }.map { it.label }.toSet()
    assertTrue(labels.containsAll(setOf(
        "Image to text", "Image to Word", "Image to PDF", "ID scan", "ID photo",
        "QR scanner", "Math solver", "Word to PDF", "PDF to Word",
        "PowerPoint to Word", "Word to PowerPoint", "Translate",
    )))
}
```

- [ ] **Step 2: Run the focused test and verify failure**

Run: `./gradlew.bat :app:testDebugUnitTest --tests "*.ToolCatalogTest"`

Expected: compilation fails because `ToolCatalog` does not exist.

- [ ] **Step 3: Implement the four destination surfaces**

The Tools screen renders grouped tool cards with correct icons and short names. Library renders search, sort, and an honest local empty state. Settings renders offline status, model packs, privacy, reduced motion, and local-storage choices. Camera entry uses a dark full-screen camera treatment with top controls, mode rail, and correctly drawn camera shutter while the CameraX pipeline is delivered by the scanning plan.

- [ ] **Step 4: Run full verification**

Run: `./gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug`

Expected: tests and lint pass, and the debug APK is produced.

- [ ] **Step 5: Commit and push**

```bash
git add app/src/main app/src/test
git commit -m "Complete SnapTon app shell"
git push origin main
```

### Task 6: Foundation documentation and device-ready handoff

**Files:**
- Create: `README.md`
- Create: `docs/architecture/foundation.md`
- Modify: `.gitignore`

**Interfaces:**
- Consumes: all foundation build commands and package names.
- Produces: reproducible setup, test, APK location, offline guarantee, and the next scanning milestone boundary.

- [ ] **Step 1: Document exact build and install commands**

Document `./gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug`, APK path `app/build/outputs/apk/debug/app-debug.apk`, and `adb install -r` usage. State that this milestone contains the branded shell and camera experience surface, while CameraX capture, auto-crop, filters, OCR, and converters are delivered by the subsequent plans.

- [ ] **Step 2: Run the documented command from a clean Gradle invocation**

Run: `./gradlew.bat --no-daemon :app:testDebugUnitTest :app:lintDebug :app:assembleDebug`

Expected: `BUILD SUCCESSFUL`.

- [ ] **Step 3: Inspect repository state**

Run: `git status --short` and `git log -1 --oneline`.

Expected: only the intended README and architecture documentation are uncommitted before this task's commit.

- [ ] **Step 4: Commit and push**

```bash
git add README.md docs/architecture/foundation.md .gitignore
git commit -m "Document SnapTon Android foundation"
git push origin main
```

## Follow-on Plans

The approved product spec is intentionally split after this independently runnable milestone:

1. `snapton-scan-pipeline`: CameraX capture, edge stability, automatic batch crop, manual corner editing, and animated geometry.
2. `snapton-editor-filters`: real OpenCV filters, adjustment recipes, live thumbnails, compare gesture, and golden fixtures.
3. `snapton-ocr-library`: OCR preprocessing, confidence, spelling safeguards, reversible corrections, Room, encrypted assets, and search.
4. `snapton-offline-tools`: PDF/image converters, translation packs, QR/barcodes, ID workflows, math solver, and Office adapters.

Each follow-on plan begins only after this app shell builds and its tests pass, preserving a usable Android artifact at every milestone.
