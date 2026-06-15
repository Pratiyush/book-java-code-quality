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
