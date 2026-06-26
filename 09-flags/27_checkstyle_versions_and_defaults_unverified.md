# FLAG — key 27 (Checkstyle): versions, plugin GAVs & default properties UNVERIFIED until `/pin-source`

**Raised by:** researcher (key 27 dossier) · **Date:** 2026-06-15 · **Severity:** version/default atoms (`⚠ verify at pin`)

## What is verified (live-line, from Checkstyle's own docs)
- Module/check **identity + category** (the 14 categories; `ConstantName`, `LineLength`, `MethodName`,
  `UnusedImports`, naming family incl. `RecordComponentName`/`PatternVariableName`, filters, etc.).
- The config **structure**: `Checker` (root) → `TreeWalker` (AST `FileSetCheck`) → `Check` submodules;
  `severity` = `error` (default) / `warning` / `info` / `ignore`; bundled configs `google_checks.xml` /
  `sun_checks.xml` / `openjdk_checks.xml` / `doc_comments_checks.xml`.
- The single-file **limitations** (verbatim, `writingchecks.html`): one file only; cannot determine an
  expression's type or full inheritance hierarchy.
- Two headline **defaults** captured verbatim from the per-check pages: `LineLength max=80` /
  `ignorePattern=^(package|import) .*`; `ConstantName format=^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`.

## What is NOT verified at a pin (must re-trace after `/pin-source`)
1. **Checkstyle version.** Live site shows latest **13.6.0 (2026-06-15)**; SOURCE-PIN §2 Checkstyle row is
   `TO-PIN`. Do not assert a version number until pinned.
2. **Most check default property values / thresholds** — naming regexes for `MemberName`/`MethodName`/
   `LocalVariableName`/`ParameterName`/`TypeName`/`PackageName`; `CyclomaticComplexity`/`NPathComplexity`
   numeric defaults; `MagicNumber`/`MissingJavadocMethod`/Block/Whitespace defaults. Defaults move across
   versions — fetch the specific check page at the pinned version; never print a number as "the" limit (key 19).
3. **`maven-checkstyle-plugin` GAV + bundled-engine version.** Plugin live **3.6.0 (2024-10-22)**, **bundles
   Checkstyle 9.3 by default** (verbatim, plugin docs). Confirm both at pin AND the override coordinate
   `com.puppycrawl.tools:checkstyle` (artifactId `checkstyle`) used to swap in the pinned engine. Two pins:
   engine ≠ plugin.
4. **Gradle `CheckstyleExtension` properties** — `toolVersion`/tasks verified; `config`/`configFile`/
   `maxWarnings`/`maxErrors`/`ignoreFailures` not surfaced by the live fetch → confirm at pin.
5. **Bundled-config XML contents** — module membership of `google_checks.xml`/`sun_checks.xml` and the Google
   100-col/+2/+4 numbers verified from the style-guide page; re-confirm the config XML at the pinned version.

## Resolution
Re-trace all of the above after `SOURCE-PIN.md` §2 Checkstyle row + §4 Maven/Gradle rows are pinned; convert
`⚠ verify at pin` → verified or correct. Until then, every Checkstyle version/default/GAV in the dossier is
identity-verified only.

## Update — 2026-06-26 (EXAMPLE-BUILD, key 27)
- **Pin status changed:** `SOURCE-PIN.md` §2 now pins Checkstyle at **13.6.0** (`✅ pinned 2026-06-20`).
- **`ConstantName` default regex CONFIRMED LIVE.** The Chapter 27 companion build
  (`08-companion-code/27_checkstyle/`, engine `com.puppycrawl.tools:checkstyle:10.26.1`) emitted, on a
  strip-and-rebuild, `Name 'centsFormat' must match pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'. [ConstantName]`
  — the dossier's regex, verified by the running engine, not just identity. Item 2's `ConstantName` default
  is now confirmed at the build engine.
- **Engine-vs-pin delta to re-pin (NEW, non-blocking).** SOURCE-PIN pins **13.6.0**, but the companion-code
  aggregator's house engine is **10.26.1** across all 22 built peer modules (the two-pin override target).
  The Chapter 27 module follows that consistent house engine. When the aggregator re-pins its engine to the
  13.x line (re-pin runbook step 4), rebuild `27_checkstyle` (and peers) against it — the module's checks
  (`ConstantName`, `LineLength`, naming family incl. `RecordComponentName`/`PatternVariableName`, import +
  block + coding checks, `SuppressWarningsFilter`/`SuppressWarningsHolder`) all exist across the 10.x/13.x
  lines, so no rule-rename re-trace is expected; confirm at re-pin.
- Items 1, 3, 4, 5 (plugin GAV/bundled-engine, Gradle extension props, bundled-config XML contents) remain
  `⚠ verify at pin` — not exercised by this Maven module.
