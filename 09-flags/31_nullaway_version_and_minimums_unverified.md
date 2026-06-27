# FLAG — key 31 (NullAway): version, minimums, and flag since-versions unverified at pin

**Status:** ⚠ verify at pin (+ ⚠ AHEAD-OF-PIN for 0.13.x-only behavior)

**Context.** `SOURCE-PIN.md` §2 pins NullAway as `TO-PIN` (no exact version). Research observed:
- NullAway latest release **0.13.6** (released **5 Jun 2026**) — newer than any pinned line and past the
  assistant knowledge cutoff. GAV `com.uber.nullaway:nullaway`; annotations `com.uber.nullaway.annotations.*`.
- Documented minimums: **JDK 17+**, **Error Prone ≥ 2.36.0** (version-sensitive).
- Flag since-versions: `OnlyNullMarked` (v0.12.3+), `SuppressionNameAliases` (v0.12.8+),
  `LegacyAnnotationLocations` / type-use annotation-location change (since 0.12.0).

**Required action at `/pin-source`.**
1. Pin the exact NullAway version (latest stable as of pin date) + Error Prone version in `SOURCE-PIN.md` §2.
2. Re-confirm byte-exact: min JDK / min Error Prone; each flag's since-version; the default severity of the
   NullAway check.
3. Treat any behavior that exists only in **0.13.x** (newer than the pinned line) as `⚠ AHEAD-OF-PIN` until
   the pin catches up; do not present 0.13.x-only detail as anchor fact.

**Identity verified now** (from NullAway repo/wiki, live-line): flag names, annotation names, error-message
stems, GAV coordinate. Only the *version numbers / minimums / since-versions / default severity* are deferred.

## Update — 2026-06-27 (web-verify pass, Maven Central + official NullAway README/GitHub)
- **Pinned version 0.13.4: RESOLVED (resolves on Central).** `com.uber.nullaway:nullaway` publishes 0.13.4
  (and 0.13.5/0.13.6/0.13.7) on Maven Central (`maven-metadata.xml`, fetched 2026-06-27). SOURCE-PIN §2 pins
  **0.13.4** — a valid published version. **Current latest = 0.13.7** (GitHub release tag `v0.13.7`, published
  2026-06-16, per `api.github.com/repos/uber/NullAway/releases/latest`); recorded as the live latest, NOT a
  re-pin (the pin governs; re-pin only via the runbook).
- **Minimums (JDK 17 + Error Prone 2.36.0): RESOLVED verbatim.** NullAway README states, verbatim:
  "NullAway requires that you build your code with JDK 17 or higher and Error Prone, version 2.36.0 or higher."
  (`github.com/uber/NullAway`). `error_prone_annotations` 2.36.0 also resolves on Central. Draft 11 line 191
  now cites this README confirmation; the minimums atom is no longer pin-deferred.
- **Still deferred:** flag since-versions (`OnlyNullMarked` v0.12.3+, etc.) and the default severity of the
  NullAway check were not re-confirmed verbatim this pass — they remain `⚠ verify-at-pin`. The FSE'19 overhead
  figures/quote stay flagged separately (`31_nullaway_overhead_figures_unverified.md`) — the paper is outside
  the pinned authority set (left flagged, by rule).
