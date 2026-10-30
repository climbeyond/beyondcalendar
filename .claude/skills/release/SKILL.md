---
name: release
description: Prepare a new library release - pick the next version (no skipped versions), bump it in build.gradle.kts, commit, tag, and (when asked) push and start the GitHub Packages publish workflow. Use when the user asks to release, bump or set a new library version.
argument-hint: "[patch | minor | major | <x.y.z>]"
---

The version has a single source: `versionMajor`, `versionMinor` and `versionPatch` in the `extra`
block of the root `build.gradle.kts`. `versionName` is `major.minor.patch` (used as the Maven
version and in the AAR/XCFramework names) and `versionCode` is
`androidMinSdk * 10000000 + major * 10000 + minor * 100 + patch`, so minor and patch must stay ≤ 99.

Releases are tagged `v<x.y.z>` (annotated) on the release commit. Older releases used tags without
the `v` prefix (up to `0.1.9`) and some were never tagged; leave them as they are.

## 1. Check the starting point

- On `main`, up to date with `origin/main`, with a clean working tree. Anything uncommitted: stop
  and ask the user.
- Read the current version from `build.gradle.kts`.
- The tag `v<new version>` must not exist yet (`git tag -l`).

## 2. Choose the version

Only the next version of each kind is allowed, so no version is skipped. For current `X.Y.Z`:
- patch → `X.Y.(Z+1)`
- minor → `X.(Y+1).0`
- major → `(X+1).0.0`

If the argument gives a bump kind or an allowed version, use it. Otherwise list the three options
with their versions as plain text and ask which one (this user prefers a chat question over the
AskUserQuestion tool). Refuse any other version, explaining which ones are allowed. Refuse a
minor or patch above 99 (versionCode formula) and ask the user how to proceed.

Show the commits since the previous release so the user can check what goes in:
`git log --oneline $(git log -1 --format=%h -G'versionPatch' -- build.gradle.kts)..HEAD`
(the last commit that changed the version). If any of them breaks the public API (see CLAUDE.md),
point it out and suggest a minor or major bump instead of a patch.

## 3. Bump the version

Set `versionMajor`/`versionMinor`/`versionPatch` in `build.gradle.kts`. If the README mentions a
version for consumers, update it too. Commit: `chore: release <x.y.z>`. No attribution lines.

Don't build locally: the Release workflow builds every target (iOS needs macOS) as part of
publishing.

## 4. Tag

`git tag -a v<x.y.z> -m "Release <x.y.z>"` on the release commit.

## 5. Push and publish (only when the user asks)

Show the new commits and the tag (`git log --oneline --decorate -3`). Don't push unless the user
asks. When they do:

1. `git push origin main v<x.y.z>`
2. Start the publish workflow on the tag: `gh workflow run release.yml --ref v<x.y.z>`
3. Give the user the run link (`gh run list --workflow=release.yml --limit 1`). If it fails, show
   the failing step's log (`gh run view <id> --log-failed`). A version that already exists in
   GitHub Packages can't be published again, so a failed publish after a successful upload needs a
   new patch version, not a re-run.

Remind the user to bump `beyondcalendar` in `../climbeyond-app/gradle/libs.versions.toml` when
they want the app to use the release.
