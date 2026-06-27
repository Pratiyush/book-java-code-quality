# FLAG — key 32: GAV versions + tool-conformance matrix UNVERIFIED until /pin-source

**Severity:** material (atom verification deferred)
**Status:** annotation **identity + semantics** verified from each project's own docs; **versions,
coordinates, and conformance state** are `⚠ verify at pin`.

## Versions / GAV coordinates (all TO-PIN in SOURCE-PIN.md §2)
- `org.jspecify:jspecify` — **1.0.0** observed on jspecify.dev; exact 1.0.0 release date not captured.
- `org.checkerframework:checker` + `org.checkerframework:checker-qual` — version TO-PIN.
- `com.google.code.findbugs:jsr305:3.0.2` — de-facto JSR-305 jar (observed); confirm at pin.
- `com.uber.nullaway:nullaway` + `com.google.errorprone:error_prone_core` — versions TO-PIN (depth = key 31/30).

## Tool-conformance matrix (live-line from jspecify.dev/docs/whether — moves fast)
- **NullAway** — "supports JSpecify annotations but does not yet analyze generics"; JSpecify mode
  (`-XepOpt:NullAway:JSpecifyMode=true`) "still under development … may report false positive warnings";
  requires Error Prone ≥ 2.14.0; 0.12.11 adds a javac-compatibility guard.
- **IntelliJ IDEA** — JSpecify support "with some issues, largely around generics."
- **Checker Framework** — "understands only `@Nullable` and `@NonNull`, lacking `@NullMarked`/`@NullUnmarked`."
- **EISOP** — "good conformance except for its interpretation of unspecified-nullness code."
- **Kotlin compiler** — full support from 1.8.20+.

## Other version-sensitive atoms
- Pre-**JDK 22** bug: type-use annotations "were not properly read from class files" (affects annotation
  processors e.g. Dagger) — confirm JDK version/bug ID at draft.
- `javax.annotation` Java 9+ **split package** with platform module `java.annotation` — confirm exact platform
  module name from the JDK module descriptor at draft.

**Action:** re-trace every version/GAV/conformance cell at `/pin-source`; the §2.7 reference table + the
Fig 32.2 conformance matrix are the single re-trace unit. Reserve ☑ for post-pin.

---

**UPDATE 2026-06-27 (corrected pin + Ch 11 green build).** GAV/version atoms now RESOLVED against
SOURCE-PIN.md §2 and empirically confirmed by `08-companion-code/11_null_safety_optional/`
(`mvn -Pquality verify` green on JDK 21, bytecode 65):
- `org.jspecify:jspecify` = **1.0.0** — pinned (§2) and module-resolved (`provided` scope, compiles green).
- `org.checkerframework` = **Checker Framework 4.2.0** — now pinned (§2).
- `com.uber.nullaway:nullaway` = **0.13.4** (requires JDK 17 + Error Prone 2.36.0+) — now pinned (§2);
  this is the pin, NOT ahead-of-pin (an earlier Ch-11 draft note mislabelled 0.13.x as AHEAD-OF-PIN).
- `com.google.code.findbugs:jsr305:3.0.2` remains a de-facto coordinate (JSR-305 is a migrate-from
  family, not a pinned authority row).
STILL OPEN: the tool-**conformance** matrix (NullAway/IntelliJ/Checker-FW/EISOP/Kotlin JSpecify-mode &
generics state) is a fast-moving live-line, not module-exercised here — keep flagged.
