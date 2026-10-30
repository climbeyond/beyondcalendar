# BeyondCalendar

Open-source Kotlin Multiplatform calendar library (Compose Multiplatform UI) for Android and iOS.
Shows one month at a time with a year/month header, swipes between months within a configured
range, and draws accents (e.g. dots) on given days. Public repo: `climbeyond/beyondcalendar`.

## Related repositories

- `../climbeyond-app`: the Climbeyond app, main consumer of this library (has its own
  CLAUDE.md). Check how it uses the library before changing public API.

## Project structure

- Single Gradle module `:shared`, renamed to `beyondcalendar` in `settings.gradle.kts` (task paths
  are `:beyondcalendar:...`).
- `shared/src/commonMain/kotlin/app/climbeyond/beyondcalendar/`
  - `BeyondCalendar.kt`: main entry point; `Listener` callbacks, accents, `View()` composable
  - `Settings.kt`: calendar configuration
  - `ui/`: `CalendarView.kt`, `Accent.kt`, `Fonts.kt`
- `shared/src/commonMain/composeResources/`: drawable, fonts and strings. Consumers must also add
  these resources to their own app (see README "Setup & Gradle").
- `shared/src/androidMain/`, `shared/src/iosMain/`: platform `actual`s.

## Stack and build

- Kotlin `2.4.10`, Compose Multiplatform, `kotlinx-datetime`, `kotlinx.serialization`. Versions
  are in `gradle/libs.versions.toml`.
- Targets: Android library, `iosArm64`, `iosSimulatorArm64` (static `BeyondCalendar` XCFramework).
- JVM toolchain 21.
- Library version lives in root `build.gradle.kts` (`versionMajor`/`versionMinor`/`versionPatch`
  in `extra`); `versionName` is `major.minor.patch`.
- Published to GitHub Packages (`maven.pkg.github.com/climbeyond/beyondcalendar`) by the manually
  triggered `.github/workflows/release.yml`. `GITHUB_USER` / `GITHUB_TOKEN` are passed as Gradle
  properties; never print or commit credentials.

## Library design rules

- **Public API is used by released apps.** Prefer additive changes; when a breaking change is
  needed, say so explicitly so it can go in a minor/major version and the README example can be
  updated.
- **No storage/database dependencies in the library.** Features that need persistence expose an
  interface (e.g. `NotificationStore`) and the host app implements it with Room or any other
  database. Don't add Room, KSP, SQLite or similar to `:shared`, not even as an optional module.
- Keep the README example in sync with the public API.

## Commit messages

Format: `<keyword>: short description` (imperative, lowercase, no period). Add a body below a
blank line only if it needs more explanation.

Core keywords:
- `feat`: new functionality in the public API or UI
- `fix`: bug fix
- `refactor`: code change with no behavior change
- `chore`: deps, build config, tooling, CI
- `docs`: CLAUDE.md, README, comments

Optional, only when it clarifies:
- `ui`: visual/layout-only tweaks (no logic change)
- `deps`: dependency version bumps (subset of `chore`, use if worth calling out separately)

Add a platform scope in parens when a change isn't shared: `fix(ios): ...`, `feat(android): ...`.

Example: `feat: add event reminder notifications`
