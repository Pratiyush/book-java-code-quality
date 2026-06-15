# FLAG — key 30 (Error Prone + Refaster): versions, GAV & defaults UNVERIFIED until /pin-source

**Raised:** 2026-06-15 · **Dossier:** `02-research/30_error_prone/30_error_prone_RESEARCH.md`

## What is verified (live-line, from the tool's own docs)
- **Flag names / syntax** (verbatim, `errorprone.info/docs/{installation,flags,patching,refaster}`):
  `-Xplugin:ErrorProne`, `-XDcompilePolicy=simple`, `--should-stop=ifError=FLOW`,
  `-Xep:<Check>:<OFF|WARN|ERROR>`, `-XepAllErrorsAsWarnings`, `-XepDisableAllChecks`,
  `-XepDisableWarningsInGeneratedCode`, `-XepExcludedPaths:<regex>`, `-XepOpt:[NS:]Flag[=Val]`,
  `-XepPatchChecks:<list>`, `-XepPatchLocation:<path|IN_PLACE>`, Refaster
  `@BeforeTemplate`/`@AfterTemplate`, `-Xplugin:RefasterRuleCompiler --out *.refaster`,
  `-XepPatchChecks:refaster:*`.
- **Check identity + category descriptions** (`errorprone.info/bugpatterns`): names + ON_BY_DEFAULT
  ERROR / WARNING / Experimental *category* for ~24 checks (`ArrayEquals`, `CollectionIncompatibleType`,
  `DeadException`, `ReturnValueIgnored`, `MissingOverride`, `FallThrough`, `DefaultCharset`, …).
- **JDK floor** (install doc): "must be run on JDK 21 or newer"; history 2.10.0→8, 2.31.0→11, 2.42.0→17.
- **Gradle plugin id**: `net.ltgt.errorprone` (`tbroyer/gradle-errorprone-plugin`) — "an external contribution."

## What is UNVERIFIED (deferred to /pin-source)
- ⚠ Exact **version + GAV** of `error_prone_core`, `error_prone_annotations`, `error_prone_refaster`
  (`SOURCE-PIN.md` §2 row Error Prone = `TO-PIN`).
- ⚠ Exact **version** of the `net.ltgt.errorprone` Gradle plugin (independent community cadence) and the
  exact `options.errorprone` method set at that version.
- ⚠ Exact **default-on membership** and **per-check severity** (these move across Error Prone versions).
- ⚠ Byte-identical **check description quotes** and the Refaster `@AfterTemplate`/`@BeforeTemplate` rule
  sentence (quote-drift guard, key 19) + the `com.google.errorprone.refaster.annotation` package path.
- ⚠ Whether `-XDcompilePolicy=simple` / `--should-stop=ifError=FLOW` are unchanged for a Java 25 build.

## Action
At `/pin-source`: pin the Error Prone row, then re-trace the §2.6/§2.7 reference tables as one unit; replace
every `⚠ verify at pin` with the pinned version's verified value; reserve ☑ for that post-pin pass.
