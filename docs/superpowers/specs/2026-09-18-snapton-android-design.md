# SnapTon Android Design

## Product

SnapTon is a privacy-first Android document scanner and conversion toolkit. It runs locally after optional language packs are installed. Documents, images, recognized text, translations, and conversion outputs remain on the device unless the user explicitly shares them through Android.

The approved brand name is **SnapTon**. The approved launcher artwork is `branding/snapton-app-icon-final.png`. The interface uses a dark-blue family: primary `#164B9B`, active `#0A326F`, and deep navy `#0C1D39`. Icons use one consistent vector line family and never use emoji.

## Supported Android baseline

- Native Kotlin and Jetpack Compose.
- Minimum Android 8.0 (API 26).
- Target the latest stable Android SDK available to the build environment.
- Portrait-first phone interface with adaptive tablet layouts.
- No account and no network requirement for installed features.
- Optional OCR and translation packs may be downloaded once, then used offline.

## Information architecture

The bottom navigation contains Home, Library, a raised central Scan action, Tools, and Settings. All navigation icons use dark blue; the active destination and Scan action use the deeper active blue.

### Home

Home shows a search field, two centered rows of four quick tools, and recent documents. Quick tools contain only an icon and centered label:

1. PDF tools
2. Scan ID
3. Image to text
4. Image to PDF
5. ID photo
6. Import files
7. Translate
8. All tools

### Camera

The camera uses a full-screen native preview. Top controls contain close, automatic/manual capture, flash, and overflow. The lower mode rail includes Document, ID card, Book, QR, and additional modes available through All features. The shutter is large and reachable with one hand.

Document edges animate into place. A restrained scan line confirms stable detection. Automatic capture occurs only when the page is stable, sharp enough, and fully inside the frame. Manual capture always remains available.

### Batch capture and crop

Each captured page is automatically detected, perspective-corrected, and added to the current batch without forcing an editor interruption. The review screen shows all pages as thumbnails. Low-confidence edge detections are marked for review. Any page can be manually recropped, rotated, retaken, deleted, or reordered.

Manual crop uses four draggable corners, edge snapping, and a magnifier that follows the active finger. Crop transitions animate from detected geometry to corrected page geometry.

### Page editor and filters

Every filter produces a real image-processing result and a live thumbnail preview. Selecting a filter crossfades the full-resolution preview and animates processing progress. Initial filters:

- Original
- Auto enhance
- Clean document
- Color document
- Grayscale
- Black and white
- Whiteboard
- Photo
- Low light
- Remove shadows

Brightness, contrast, saturation, temperature, sharpness, and threshold are adjustable. A compare gesture temporarily reveals the original. Filters can apply to one page or the full batch.

### OCR and spelling correction

OCR runs on the corrected high-resolution page, not the camera preview. Preprocessing evaluates orientation, deskew, illumination, denoise, contrast, and adaptive threshold variants, then selects the result with the best recognition confidence.

Recognized text retains blocks, lines, paragraphs, tables where detectable, and page coordinates. The correction stage uses:

- OCR confidence and alternative glyph candidates.
- Language-specific frequency dictionaries.
- SymSpell-style edit-distance correction.
- Context scoring for neighboring words.
- Rules that protect names, IDs, account numbers, URLs, formulas, and mixed alphanumeric strings.

Corrections are visibly marked and reversible. Users can view original OCR text, corrected text, or a difference view. Low-confidence words remain highlighted instead of being silently changed.

### Library

Documents are stored locally with encrypted metadata, page ordering, thumbnails, OCR text, tags, and export history. Search works across titles and recognized text. Files can be renamed, duplicated, moved to folders, exported, shared, or deleted.

### Tools

Tools are grouped by task instead of file format.

#### Scan and recognize

- Image to text
- Image to Word
- Image to PDF
- ID scan with front/back layout
- ID photo crop and background presets
- QR and barcode scanner with local history
- Math solver from image or typed text

#### Convert documents

- Word to PDF
- PDF to Word
- PowerPoint to Word
- Word to PowerPoint
- Images to PDF
- Images to Word
- Merge, split, compress, reorder, rotate, and protect PDF files

Office conversions are best-effort and fully local. Standard paragraphs, headings, lists, tables, images, and common slide layouts are preserved. Macros, embedded executables, advanced SmartArt, and unsupported fonts are ignored safely and reported before export. PDF-to-Word prioritizes visual fidelity by combining page layout images with editable OCR text when semantic reconstruction is uncertain.

#### Language and math

- Translate typed text or recognized image text using installed on-device language packs.
- Solve arithmetic, fractions, percentages, unit conversions, linear equations, quadratic equations, and common printed expressions locally.
- Show the parsed expression and steps before the final answer so OCR mistakes can be corrected.

## Architecture

The project uses a single Android application with feature-oriented packages and dependency-injected engine interfaces.

- `app`: activity, navigation, theme, app shell.
- `feature/home`: quick tools and recent documents.
- `feature/camera`: CameraX preview, capture modes, stability analysis.
- `feature/crop`: edge overlay, perspective correction, manual geometry.
- `feature/editor`: filter pipeline and batch editing.
- `feature/ocr`: OCR, layout reconstruction, confidence, correction.
- `feature/library`: Room-backed local document library.
- `feature/tools`: tool catalog and conversion workflows.
- `engine/image`: OpenCV-backed edge detection and filters.
- `engine/text`: OCR, dictionaries, correction, translation packs.
- `engine/document`: PDF and Open XML import/export adapters.
- `engine/math`: tokenizer, parser, solver, and step formatter.
- `engine/code`: QR and barcode recognition.
- `core/model`: stable domain models.
- `core/storage`: encrypted file and metadata access.
- `core/ui`: reusable Compose components and vector icons.

Feature code depends on interfaces rather than concrete engines. Heavy processing runs in cancellable coroutines or WorkManager tasks. The UI observes immutable state flows.

## Data flow

1. CameraX supplies preview frames to a throttled edge and quality analyzer.
2. Capture writes the original image to an encrypted app-private working area.
3. The crop engine produces page corners, confidence, and perspective-corrected output.
4. The editor applies a non-destructive filter recipe and renders previews.
5. OCR and spelling correction run from the corrected full-resolution image.
6. Room stores document metadata and references encrypted page assets.
7. Export engines render requested output into a temporary file.
8. Android's share or document picker moves the result only after a user action.

## Privacy and permissions

- Camera permission is requested only when scanning begins.
- Android Photo Picker and Storage Access Framework avoid broad storage access.
- No analytics, advertising SDK, account, or background upload is included.
- Temporary files are removed after export or a failed/cancelled operation.
- Sensitive page images do not appear in logs or crash reports.
- Model-pack network access is explicit, shows download size, and can be disabled permanently.

## Error handling

- Camera quality guidance identifies blur, glare, missing corners, or low light in plain language.
- Failed processing preserves the original page and offers retry or manual editing.
- Converters inspect inputs first and list unsupported elements before starting.
- Long tasks show page-level progress, support cancellation, and resume safely after process recreation.
- Storage checks occur before capture and export.
- Corrupt or password-protected files produce specific recovery guidance.

## Accessibility and motion

- Minimum 48 dp touch targets and visible keyboard/accessibility focus.
- Content descriptions for all non-text controls.
- Text supports system font scaling.
- Color never carries status alone.
- Motion communicates edge locking, capture, crop correction, filter changes, and completion.
- Reduced-motion settings replace geometric transitions with short fades.

## Verification

- Unit tests cover crop geometry, filter parameters, OCR correction safeguards, conversion mapping, math parsing, and file lifecycle behavior.
- Instrumented tests cover navigation, permissions, batch capture review, manual crop, filter application, OCR correction review, and export flows.
- Golden image tests compare filters and perspective correction against fixed fixtures.
- OCR fixtures include blur, skew, shadows, mixed fonts, tables, IDs, URLs, and formulas.
- Conversion fixtures validate round trips for representative DOCX, PDF, PPTX, and image inputs.
- Manual device testing covers low-memory devices, rotation, process recreation, offline mode, and interrupted model downloads.

## Delivery sequence

1. App shell, SnapTon branding, Home, Library, Tools, and Settings.
2. Full-screen camera, automatic detection, batch capture, and manual crop.
3. Real filter pipeline and animated editor.
4. OCR, confidence display, and spelling correction.
5. PDF and image conversions.
6. Translation, QR, ID workflows, and math solver.
7. Word and PowerPoint conversion adapters.
8. Performance, accessibility, security, and release hardening.

