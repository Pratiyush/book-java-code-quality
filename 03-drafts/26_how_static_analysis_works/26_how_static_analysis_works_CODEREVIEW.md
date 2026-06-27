# CODE-REVIEW — Chapter 26 · how-static-analysis-works (FLOOR C, second half)

- **Module:** `08-companion-code/26_how_static_analysis_works/`
- **Draft:** `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`
- **Reviewer:** code-reviewer agent (senior-PR review of code readers copy)
- **Date:** 2026-06-27
- **Build env:** JDK 21.0.11 (Homebrew openjdk@21), Maven, `-Pquality` profile.
- **Build result:** `mvn -B -Pquality clean verify` = **BUILD SUCCESS** — 8 tests pass, 0 Checkstyle violations, 0 SpotBugs findings (BugInstance size 0, Error size 0). Compile is **warning-clean** under the parent's `-Xlint:all,-processing` (no compiler warnings emitted).

## Verdict

**PASS-WITH-FIXES** — no BLOCKER on snippet integrity; the module is exemplary teaching code that builds green and warning-clean. **One neutrality finding (banned word "beats") and one prose↔code fidelity error (wrong SpotBugs pattern ID in Javadoc) must be fixed before FLOOR C can be marked PASS** — under the gate rule a neutrality/invention finding blocks FLOOR C. Both are one-line edits; re-review after the example-builder applies them.

> **FLOOR-C disposition:** HOLD → PASS once F1 (neutrality) and F2 (fidelity) are fixed and the module re-built green. No security, no originality/attribution, no correctness, no test-quality blocker. The displayed-snippet integrity gate is **clean** (all six tag regions brace-balanced, ≤9 lines, complete statements, no duplicate/mid-statement end-tags).

## Six dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 & code quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity (incl. originality/attribution) | **FIX** (F2 — wrong pattern ID in one Javadoc line) |
| 6 | Neutrality in code | **FAIL** (F1 — banned word "beats" in a shipped/displayed string) |

### 1. Correctness — PASS
Logic is right on every path. `ResourceReader.readFirstLine` closes via try-with-resources on normal/early/exception return; `ResourceLeakDemo` is the deliberate unclosed-stream counter-example (correctly the leak the chapter names). `SuppressionDemo` constructor defensively copies (`counts.clone()`); `CatalogQuery` is immutable (`Map.copyOf`). No resource leaks outside the intentional teaching target, no swallowed exceptions outside the intentional `ast-smell` target (and that one logs in its safe twin). Tests are **non-vacuous** — each asserts a concrete behavioral claim from the prose (the type-misuse query is `false` while the typed query is `true`; tainted and sanitized lookups return identical rows so "the difference is the flow, not the result"). **Failure path is genuinely exercised**: missing resource → `null`; SQL-injection payload `"tools' OR '1'='1"` → empty (proves the sanitizer holds); unknown category → empty; out-of-bounds day → `ArrayIndexOutOfBoundsException` via `assertThatThrownBy`.

### 2. Idiomatic Java 21 & code quality — PASS
All types `final` with private constructors for the static-utility demos; `System.Logger` (JDK platform logger, no stdout); `StandardCharsets.UTF_8` explicit; `Map.of`/`Map.copyOf`/`List.of` factories; `getOrDefault` over null-checks. SpotBugs annotations are `provided`-scoped so runtime stays JDK-only. The pom's "two-pin" handling (overriding the checkstyle-plugin's bundled 9.3 engine to the pinned 10.26.1 via a plugin-level dependency) is correct and well-commented.

### 3. Security — PASS
No hardcoded secrets/passwords/tokens/keys (literal-assignment scan clean). The `taint-flow` snippet shows string-concatenated SQL **as a deliberate teaching counter-example**; its sink `CatalogQuery.runRaw` is an in-memory stub that never executes SQL, so there is **no live injection sink** in the deliverable — and this is honestly disclosed in both the module comment and README ("an in-module sink SpotBugs does not model as JDBC injection"). The `taint-fixed` form and the injection-payload test demonstrate the parameterized barrier. Input on the public seam degrades to empty results, never leaking internals or stack traces.

### 4. Simplicity & readability — PASS
Smallest code that teaches each rung; no dead code, no unused deps (the one extra GAV, spotbugs-annotations, is load-bearing for the `justified-suppression` snippet). Realistic storefront-domain names (`catalogueHas`, `lookupTainted`, `dailyCounts`) — no `foo`/`bar`/`tmp`/placeholder packages. Every public type carries a one-line purpose Javadoc; the SMELL/MISUSE/LEAK/TAINT/SANITIZED inline markers make each counter-example legible cold.

### 5. Prose↔code fidelity — FIX (F2)
Snippet tags, rule/pattern IDs, GAV coordinates, and version numbers all trace to SOURCE-PIN / the draft back-matter and the module's own `_EXAMPLE.md`. The "eight tools the chapter names" claim in `package-info.java:37` is accurate (PMD, Error Prone, Checkstyle, SpotBugs, CodeQL, Semgrep, Checker Framework, SonarQube = 8). **Originality/attribution: PASS** — all shapes are bespoke storefront-domain code, not copied/reskinned upstream quickstarts; no copyright/SPDX/"generated"/sample markers. **The one defect (F2):** `SuppressionDemo.java:12` Javadoc says SpotBugs reports `{@code EI_EXPOSE_REP2}`, but `snapshot()` is a getter returning a mutable internal field — that is `EI_EXPOSE_REP`, which is exactly what the annotation 26 lines below suppresses (`value = "EI_EXPOSE_REP"`), what the exclude-filter comment, README, package-info, draft back-matter, and `_EXAMPLE.md` ("confirmed to fire when the annotation is removed") all name, and what the green build empirically proves (the annotation suppressing `EI_EXPOSE_REP` is what keeps the gate clean). `EI_EXPOSE_REP2` is the **store** form (constructor/setter), which the cloning constructor here would not trigger. The wrong ID is an invented/incorrect pattern ID in a published deliverable — a fidelity defect that blocks FLOOR C. (It sits in the Javadoc body, NOT in the displayed snippet, which shows the correct ID — so it is a Major FIX, not a BLOCKER.)

### 6. Neutrality in code — FAIL (F1)
`SuppressionDemo.java:40` contains the word **"beats"** ("Recording the reason beats disabling the detector.") — on the NEUTRALITY blocklist ("beats / outperforms (as a verdict...)"). The gate rule is binary and explicit: banned phrasings are an automatic FAIL "anywhere, code included," and the enforcement checklist box reads "No blocklist phrase appears anywhere." This one is **borderline** (it compares two of the book's own prescribed practices, not a comparator product, so it does not crown a rival tool — the harm the rule chiefly targets is absent), and it is flagged here for the human/example-builder's awareness. But the rule as written admits no judgment exception, and the phrase ships **inside the displayed `tag::justified-suppression` region (line 40)** — it is in the snippet readers paste. Treat as FAIL on dimension 6; fix to a non-verdict phrasing.

## Findings by severity

| ID | Severity | File:line | Issue | Fix |
|----|----------|-----------|-------|-----|
| F1 | **Major (neutrality — blocks FLOOR C)** | `src/main/java/org/acme/staticanalysis/SuppressionDemo.java:40` | Banned word "beats" in the `@SuppressFBWarnings` justification string — and it ships inside the displayed `justified-suppression` snippet. | Replace with a non-verdict phrasing, e.g. `...sharing the reference is safe here. The reason is recorded next to the code instead of disabling the detector.` (keep ≤9 lines; the region is currently 7). |
| F2 | **Major (fidelity / invented ID — blocks FLOOR C)** | `src/main/java/org/acme/staticanalysis/SuppressionDemo.java:12` | Javadoc states SpotBugs reports `EI_EXPOSE_REP2`; the actual pattern for a getter returning an internal array is `EI_EXPOSE_REP` (matches the annotation, the filter, package-info, README, and the green build). | Change `{@code EI_EXPOSE_REP2}` → `{@code EI_EXPOSE_REP}` in the class Javadoc. |
| N1 | Nit (optional) | `SuppressionDemo.java` Javadoc / `snapshot()` | `snapshot()` deliberately returns the live internal array to host the suppression demo; the name "snapshot" implies an isolated copy. Defensible as written (the doc says callers treat it read-only and the suppression explains the choice), so no change required — noted only for awareness. | None required. |

## Snippet-integrity gate (the critical check) — CLEAN

All six displayed `// tag::` regions verified by extraction: each is brace-balanced, ≤9 body lines, a complete statement region (a full method body — no mid-statement end-tag), tag/end-tag names match, and there are **no duplicate tag names** across the tree.

| Tag | File | Body lines | Braces | Status |
|-----|------|-----------|--------|--------|
| `ast-smell` | AstSmellDemo.java | 8 | 3/3 balanced | OK |
| `type-misuse` | TypeMisuseDemo.java | 5 | 1/1 balanced | OK |
| `dataflow-leak` | ResourceLeakDemo.java | 8 | 2/2 balanced | OK |
| `taint-flow` | TaintFlowDemo.java | 6 | 1/1 balanced | OK |
| `taint-fixed` | TaintFlowDemo.java | 6 | 1/1 balanced | OK |
| `justified-suppression` | SuppressionDemo.java | 7 | 1/1 balanced | OK (but carries F1's banned word) |

**No BLOCKER.** (Contrast the two snippet bugs found elsewhere this run — none present here.)

## Build & lint result

- `mvn -B -Pquality -f pom.xml clean verify` → **BUILD SUCCESS** (3.2 s).
- Tests: 8 run, 0 failures, 0 errors, 0 skipped.
- Checkstyle: **0 violations** (engine 10.26.1 via plugin 3.6.0).
- SpotBugs: **BugInstance size 0, Error size 0** (engine 4.9.3, effort Max / threshold Medium); the two genuine findings (`OS_OPEN_STREAM`, `GC_UNRELATED_TYPES`) carry narrow class+method+pattern reasoned suppressions in the filter file; `EI_EXPOSE_REP` carries the in-code `@SuppressFBWarnings`.
- Compiler: **warning-clean** under `-Xlint:all,-processing` (no warnings).
- Secret scan: clean. Neutrality scan: one hit (F1). Originality scan: clean.

## Exemplary notes
- The module genuinely **dogfoods its own thesis**: it shows the false-negative half (AST smell + taint flow sit below the gate's chosen point and need no suppression) and the false-positive-control half (two reasoned filter suppressions + one in-code annotation) as *running code*, not assertion.
- The pom's two-pin override comment (build-plugin engine vs analyzer engine are separate versions) is a reusable teaching artifact in its own right.
- Pairing every counter-example with its resolved twin (`parseQuantitySafely`, `containsQuantity`, `ResourceReader`, `lookupSafe`) and asserting both in one test method is a clean way to prove "the technique goes quiet once the shape is gone."

## Learnings & pipeline suggestions
- **Add a greppable banned-phrase pre-pass over `08-companion-code/**` (not just drafts).** F1 ("beats") slipped through EXAMPLE-BUILD into a shipped code string and a displayed snippet; the NEUTRALITY blocklist is currently scanned only at the AUDIT gate over the draft prose. A `check_neutrality.sh` that also scans companion-code source/comments/justification strings would catch this class before code-review.
- **Cross-check pattern IDs named in Javadoc against the annotation/filter that suppresses them.** F2 is a self-inconsistency the build cannot catch (the wrong ID was in a comment, not the suppression). A lint that flags any SpotBugs pattern ID mentioned in prose/Javadoc but never matched by an annotation or `<Bug pattern>` in the same module would surface `EI_EXPOSE_REP2`-vs-`EI_EXPOSE_REP` drift automatically.
- **Consider a NEUTRALITY carve-out note for self-comparisons of the book's own prescribed practices.** "beats" between two book-advocated techniques (not a comparator product) is arguably outside the rule's intent; if the law means to keep the blocklist strictly binary regardless, the rule text could say so explicitly to remove reviewer ambiguity.

---
**ORCHESTRATOR FIX — 2026-06-27 — F1 + F2 RESOLVED.** F1 (NEUTRALITY): the banned
word "beats" inside the displayed `justified-suppression` region (SuppressionDemo.java:40)
was reworded to "Record the reason rather than disable the detector." — no banned phrasing,
same meaning. F2 (fidelity): the Javadoc bug-ID `EI_EXPOSE_REP2` (line 12) corrected to
`EI_EXPOSE_REP` to match the annotation, filter, README, and green build. Rebuilt green
(15 tests reactor, 0 Checkstyle, 0 SpotBugs); displayed region "beats" count now 0;
check_snippets 6/6. **Verdict → PASS** (both FLOOR-blocking majors resolved).
