# FLAG — key 26: tool versions / API paths / defaults UNVERIFIED (TO-PIN)

**Chapter:** 26 — How static analysis works (AST, data-flow, taint, false-positive problem)
**Status:** ⚠ verify at pin (technique/API identity + verbatim quotes captured from each tool's LIVE docs)

## What is verified (live-line, from each tool's own docs)
- **Technique vocabulary + verbatim quotes** captured from the live docs of: PMD (root AST node /
  SymbolFacade / "DFA (data flow analysis) visitor … building control flow graphs and data flow nodes"),
  Error Prone ("augment the compiler's type analysis"; "hooks into your standard build"), CodeQL (data-flow
  graph "models the way data flows through the program at runtime"; taint "extends data flow analysis …"),
  Semgrep ("AST … translated into an analysis-friendly intermediate language (IL)"; "No path sensitivity /
  No pointer or shape analysis / No soundness guarantees"; intraprocedural), Checker Framework ("values
  soundness over limiting false positives"; "unsound in a few places …"), SpotBugs (`OpcodeStackDetector` /
  `BytecodeScanningDetector`; filter file; `@SuppressFBWarnings(value, justification)`), SonarQube
  ("False positive" / "Won't fix" resolutions; Administer Issues permission).

## What is NOT verified (must re-trace at /pin-source)
- Exact **latest-stable versions** + **GAV coordinates**: `spotbugs-maven-plugin`, `spotbugs-annotations`,
  `maven-pmd-plugin`/PMD, `error_prone_core`, Checker Framework, Semgrep CLI, CodeQL CLI — all `TO-PIN`
  (`SOURCE-PIN.md` §2).
- **SpotBugs API package path** for `OpcodeStackDetector` / `BytecodeScanningDetector` at the pinned version.
- **SonarQube resolution label** — "Won't fix" may be relabelled "Accept" across server versions; confirm.
- **Semgrep OSS vs Pro boundary** — intraprocedural-only is OSS; Semgrep Pro adds interprocedural/cross-file
  flow. Do not conflate; confirm tier behavior at pin.
- **Default-ruleset membership / severities** of any illustrating rule — owned by keys 27–35.

## Action
Re-trace every quote byte-identical and every version/coordinate after `/pin-source`. Until then these are
`⚠ verify at pin`, never "(confirmed)".
