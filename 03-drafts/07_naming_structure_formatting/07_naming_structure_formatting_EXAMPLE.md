# GATE REPORT — EXAMPLE (Step 4b / EXAMPLE-BUILD)

## Header

- **Gate:** EXAMPLE (EXAMPLE-BUILD — companion module + snippet binding)
- **Chapter key:** 07 (owner; folds 17 + 34) — FINAL_INDEX Ch 6
- **Slug:** `07_naming_structure_formatting`
- **Draft under review:** `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
- **Module path:** `08-companion-code/07_naming_structure_formatting/`
- **Run date:** 2026-06-26
- **Reviewer:** example-builder
- **Scripts run:** `extract_snippet.sh` (×5), `check_snippets.sh` (draft), banned-phrase grep
- **Build state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand)
- **Verdict:** **PASS-WITH-FIXES** (Floor C source-trace + compile + neutrality PASS; one deferred
  SOURCE-PIN fix flagged — does not block the green build)

---

## Verdict rationale

The module is self-contained (its own `config/` + its own `quality` profile, mirroring the reference
module `09_api_method_contracts/`), builds **green standalone** under `-Pquality` (BUILD SUCCESS, 6
tests pass, 0 Checkstyle violations, 0 SpotBugs findings, warning-clean), and all five declared snippet
tags resolve to bounded (≤9-line) tag regions inside the buildable files. The one required fix is a
SOURCE-PIN coordinate correction (Spotless Maven-plugin version), flagged and worked around without
inventing a version; it does not affect the green build.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime meets minimum (Java 21+).** `java -version` →
  `openjdk version "21.0.11" 2026-04-21` (Homebrew). Maven `3.9.16`, runtime JDK `21.0.11`. **PASS.**
- **(b) Build GREEN.** Exact command (run from repo root, standalone — root reactor pom NOT edited):
  ```
  export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
  export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"
  mvn -B -Pquality -f 08-companion-code/07_naming_structure_formatting/pom.xml clean verify
  ```
  Result line: **`[INFO] BUILD SUCCESS`** — `Tests run: 6, Failures: 0, Errors: 0, Skipped: 0`;
  `You have 0 Checkstyle violations.`; `BugInstance size is 0 … No errors/warnings found`.
  No `[WARNING]`/`[ERROR]` lines from Maven/compiler (`-Xlint:all,-processing` inherited from the
  aggregator emitted nothing). **PASS.**

Both preconditions hold → **Floor-C verdict: PASS.**

---

## Module registration (parent reactor)

- The module is a child of the ONE aggregator (`<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`,
  **no own `<groupId>`/`<version>` literal, no own BOM**), exactly like the key-01/09 reference modules.
- **NOT added to `08-companion-code/pom.xml` `<modules>`** (instruction: do not edit the root reactor pom).
  Registration is deferred to after CODE-REVIEW per the build-discipline rule; the root reactor pom is
  unmodified. Built standalone via `-f .../07_naming_structure_formatting/pom.xml`.

---

## Snippet tags (one artifact: prose ↔ code)

`check_snippets.sh 03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
→ **5 marker(s); 5 pass, 0 fail** (exit 0).

| Tag | Backing file | Resolved lines (≤9) | Inserted in draft at |
|---|---|---|---|
| `naming-bad` | `src/main/java/org/acme/storefront/readability/ReadabilityNotes.java` | 5 | Deep dive, right after the cramped "before" `orderthing` block (draft line ~163) |
| `naming-good` | `src/main/java/org/acme/storefront/readability/OrderLine.java` | 7 | Deep dive, right after the renamed "after" `OutstandingInvoices` block (draft line ~181) |
| `checkstyle-naming` | `config/checkstyle/checkstyle.xml` | 7 | Layer 1 — Naming, after the per-tool regex table / "do not present one as the canonical default" (draft line ~90) |
| `spotless-config` | `config/spotless/spotless-reference.xml` | 9 | Layer 3 — Formatting, after the engine/orchestrator/baseline bullet list (draft line ~118) |
| `editorconfig-baseline` | `.editorconfig` | 9 | Layer 3 — Formatting, immediately after `spotless-config` (draft line ~122) |

- Tag style: AsciiDoc `// tag::name[]` … `// end::name[]` (the kernel marker `extract_snippet.sh`
  expects), in `//` for Java and `<!-- -->` for XML and `#` for `.editorconfig`. Each region is the
  exact text the chapter displays; prose and code are one artifact.
- `naming-bad` lives inside a `/* */` block comment in a real, compiling class **on purpose**: were
  those mis-cased identifiers real declarations, the live Checkstyle naming gate would reject them — which
  is the chapter's lesson, so the before-state is shown, not compiled. The build stays green.

---

## Enterprise-grade checklist

- **Module of the ONE parent project (not standalone):** yes — `<parent>` set, no version literal, no own
  BOM; first-module shape mirrored.
- **Pinned dependency set via one inherited property:** yes — runtime (`maven.compiler.release` 21),
  JUnit (junit-bom `6.0.3`), AssertJ (`3.27.7`), compiler/surefire plugin versions all inherited from the
  aggregator; the module adds zero version literals of its own. Checkstyle engine pinned via the plugin-level
  "two-pin" override (`com.puppycrawl.tools:checkstyle:10.26.1`), SpotBugs `4.9.3.0` — both matching the
  reference module 09.
- **Externalized config profiles:** yes — `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml`,
  a module-root `.editorconfig`, and `config/spotless/spotless-reference.xml`; analysis is opt-in via the
  `-Pquality` profile so the default build stays fast (the `%dev`/`%prod` analogue for a Maven module).
- **At least one integration test exercising the chapter's mechanism:** yes — `OrderLineTest` (6 tests, JUnit
  5 + AssertJ) proves the rename/reformat is semantics-preserving (`lineTotal` computes the same value the
  cramped before-state would) and exercises the failure path. **Test-harness setup:** the JUnit Platform
  provider is auto-detected by the aggregator-pinned surefire `3.5.6` over the junit-bom; no extra system
  properties are needed for this module. Confirmed green (`Tests run: 6 … 0 failures`).
- **Observability / health surface:** the build report itself is the surface (the Checkstyle/SpotBugs
  pass/fail is the signal, per the dossier §6), plus the failure-path rejection is logged through
  `java.lang.System.Logger` (`WARNING: rejected order line for sku …`) so a turned-away call is observable,
  not silent.
- **Explicit failure path:** yes — `ReadabilityNotes.validatedLine` fails fast at construction: blank SKU /
  out-of-range quantity → `IllegalArgumentException`, null unit price → `NullPointerException`, each logged
  before it propagates. Demonstrated green in the test run (two `WARNING` log lines observed).

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** The Chapter 6 figure plan fixed at draft time is exactly one **designed** diagram
(Fig 07.1 — the typography/meaning axis), already authored as HTML→PNG with its sources sidecar at
`05-figures/07_naming_structure_formatting/fig07_1.{html,png,sources.md}` (authored separately — not the
example-builder's job, and not image-generated). The dossier listed a captured tool surface only as a
*candidate*, not a fixed plan; per the CAPTURE discipline ("capture only what the figure plan fixed at draft
time; do not invent new figures"), no screenshot capture is in scope for this module. Step 4c skipped.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. None is a copied or
lightly-edited upstream sample, quickstart/getting-started skeleton, or `NOTICE`/header boilerplate.
- `Money.java` shares the book's house value-type shape with module 09's `Money` (intra-book consistency,
  itself original-for-this-book), but is independently written (distinct method `times(int)`, distinct
  Javadoc) — not an upstream copy.
- The `naming-bad` before-state (`orderthing` / `maxRetries` / `calc` / `data`) is the chapter's own
  pedagogical example, already present in the draft prose — original to this book.
- `config/checkstyle/checkstyle.xml` and `config/spotbugs/spotbugs-exclude.xml` are the book's curated house
  ruleset (same as the reference modules), not an upstream ruleset import. No substantially-verbatim region
  from any pinned source is present, so no per-file attribution is required.

---

## Source-trace (every never-invent atom → pin)

| Atom in the module | Traces to (SOURCE-PIN / dossier) |
|---|---|
| CONSTANT_CASE = `static final` **and** deeply immutable (not merely `static final`) | Google Java Style §5.2.4 (dossier §2.1, verified) |
| Checkstyle naming modules `TypeName`/`MethodName`/`ConstantName`/`MemberName`/`ParameterName`/`LocalVariableName` | Checkstyle 13.6.0 naming index (SOURCE-PIN §2; dossier §2.4) |
| `LineLength` max 120 = this team's cited choice, not a universal truth | dossier §4 (style-value neutrality); Google uses 100, palantir 120 — each from its own source |
| google-java-format "no configurability … deliberate design decision" | google-java-format README (SOURCE-PIN §2, gjf 1.35.0; dossier §2.3, verified) |
| google-java-format version `1.35.0` (min JDK 21) | SOURCE-PIN §2 (resolves on Maven Central, HTTP 200) |
| Spotless `ratchetFrom` "only format files which have changed since origin/main" | Spotless plugin README (dossier §2.4; SOURCE-PIN §2 — but see flag on the Maven-plugin version) |
| EditorConfig keys `root`/`indent_style`/`indent_size`/`end_of_line`/`charset`/`trim_trailing_whitespace`/`insert_final_newline` | EditorConfig spec (dossier §2; `max_line_length` deliberately omitted — not in core spec) |
| Checkstyle engine `10.26.1`, SpotBugs `4.9.3.0`, JUnit `6.0.3`, AssertJ `3.27.7` | inherited from aggregator `08-companion-code/pom.xml` (mirrors reference module 09) |

No invented rule ID, config key, tool flag, API signature, GAV, version, benchmark figure, or quoted claim.
The one atom that could not be pinned (the Spotless **Maven-plugin** version literal) is **not** asserted —
it is a `${...}` placeholder and is flagged (below). No benchmark/percentage figure appears in the module
(the dossier's "~100x" Spotless figure was deliberately excluded, per VERIFY Finding 11).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | SOURCE-PIN "Spotless 8.7.0" does not resolve as a Maven-plugin coordinate (Maven plugin is the 3.x line; lib is 4.x; 8.x is the Gradle-plugin/project line). 8.7.0 → HTTP 404 for both `spotless-maven-plugin` and `spotless-lib`. | MAJOR (deferred) | `config/spotless/spotless-reference.xml` (`spotless-config` tag); SOURCE-PIN §2 Spotless row | Worked around without inventing a version: the formatter half is shown as a **reference** fragment with a `${spotless.maven.plugin.version}` placeholder (gjf pinned to the resolvable `1.35.0`), not wired live. Resolve the Maven-vs-Gradle version split at `/pin-source` (treat like the Checkstyle build-plugin-vs-engine "two-pin"). Flagged → `09-flags/34_spotless_maven_plugin_version_unresolved.md`. **Does not block the green build.** |
| 2 | `naming-bad` shown inside a block comment (not a compiling bad-named class) so the live Checkstyle naming gate stays green. | NOTE | `ReadabilityNotes.java` | Intentional and faithful: a real mis-cased declaration would (correctly) fail the gate — which is the lesson. The chapter prose already presents the same before-state as an illustrative fragment. No action. |
| 3 | `.editorconfig` snippet renders with an empty language fence (extract_snippet has no extension→lang mapping for `.editorconfig`). | NOTE | `extract_snippet.sh` lang switch | Cosmetic only; the region resolves and is ≤9 lines. Optional kernel improvement: map `.editorconfig` → `ini`/`editorconfig`. No action for this chapter. |

---

## Blockers

**None.** The build is green, all snippets resolve, Floor-C preconditions both hold, neutrality is clean,
and the one MAJOR finding is a deferred SOURCE-PIN correction that is flagged and worked around without any
invented atom.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via the standalone `verify` command at the pin; every
  displayed snippet resolves to a real bounded (≤9-line) tag region in a buildable file; FLOOR C source-trace
  clean (one unpinnable atom flagged, not asserted).
- [x] Self-contained exactly like `09_api_method_contracts/` (own `config/`, own `quality` profile,
  `<parent>` set, no own version literal/BOM).
- [x] Pinned dependency set (inherited), externalized config, ≥1 integration test (+ harness setup),
  observability surface, explicit failure path — all present.
- [x] Neutrality in code/comments/README — banned-phrase scan clean; style values stated as cited choices,
  never as the correct value; components shipped as part of Java code quality treated as such, no rival framing.
- [x] LEGAL-IP §5 original-for-this-book — confirmed file-by-file; no upstream sample copied.
- [x] Root reactor pom (`08-companion-code/pom.xml`) NOT edited; module NOT yet registered (deferred to
  post-CODE-REVIEW); no `git`/remote/`gh`/push; local only.

---

## Learnings & pipeline suggestions

1. **A SOURCE-PIN version can be true for one build tool and non-existent for another.** "Spotless 8.7.0"
   is the Gradle-plugin/project line; the **Maven** plugin is a separate 3.x line and `spotless-lib` a
   separate 4.x line. The same hazard the book already names for Checkstyle (build-plugin vs engine — the
   "two-pin" lesson) applies to Spotless across Maven vs Gradle. **Propose:** SOURCE-PIN should record the
   *Maven-plugin* coordinate/version explicitly for any tool whose Maven-plugin version differs from its
   project/Gradle line, so an example-builder is not forced to choose between a 404 and an invented version.
2. **A "bad example" snippet can stay build-green by living in a comment** when the module's own gate would
   (correctly) reject the bad code. This is a reusable pattern for every "before/after readability" or
   "anti-pattern" chapter (keys 07/17/86/89): the gate's refusal *is* the lesson, so the anti-example is
   shown-not-compiled, and the after-example is the live code. Propose adding to `EXAMPLES-GUIDE` as the
   sanctioned way to display a deliberately-non-conforming snippet without a red build or a suppression.
3. **`extract_snippet.sh` has no `.editorconfig`→language mapping** — minor kernel polish (map to `ini`).
   Recorded as Finding 3; not chapter-blocking.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 07 gate-run PASS-WITH-FIXES
```
