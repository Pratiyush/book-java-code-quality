# GATE REPORT — CODE-REVIEW (FLOOR-C, second half) — key 97 (The Draft That Looks Like a Deliverable)

## Header

- **Gate:** CODE-REVIEW (Step 4b, second half — `code-reviewer`)
- **Chapter key:** 97 (frozen key; leads Part XII, folds 99; Ch 41 OPENS Part XII)
- **Slug:** `97_ai_generated_code_quality`
- **Module reviewed:** `08-companion-code/97_ai_generated_code_quality/` (artifactId `ai-generated-code-quality`)
- **Draft cross-checked:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Toolchain:** JDK 21.0.11 (`/opt/homebrew/opt/openjdk@21`, SOURCE-PIN anchor LTS); Maven 3.9.x (system `mvn`)
- **Build of record:** `mvn -B -o -Pquality -f ../pom.xml -pl 97_ai_generated_code_quality -am clean verify` → **BUILD SUCCESS**, `Tests run: 12, Failures: 0, Errors: 0`, `0 Checkstyle violations`, SpotBugs `BugInstance size is 0`, **no compiler warnings** (parent enables `-Xlint:all,-processing`).
- **VERDICT: PASS-WITH-FIXES** (no BLOCKER; two MINOR FIX items for the example-builder; FLOOR-C is not blocked.)

---

## Verdict line

**PASS-WITH-FIXES** — green, warning-clean, snippet-faithful, security-correct, neutrality-clean, no AI statistic baked into code. Two non-blocking MINOR fixes: an unused `spotbugs-annotations` dependency whose justifying comment describes a usage that does not exist, and one dangling `{@link #isReady()}` Javadoc reference. Neither blocks FLOOR-C.

---

## The six dimensions

| # | Dimension | Result | One-line basis |
|---|---|---|---|
| 1 | Correctness | **PASS** | Both lookups correct; the `PreparedStatement` fix is genuinely safe; `OrderTotals` boundary/arithmetic correct; tests assert real behaviour (not vacuous), incl. the failure path; no resource leaks; no swallowed exceptions. |
| 2 | Idiomatic Java 21 | **PASS** | `record Money`, switch-expressions in the test double, `System.Logger`, `AtomicLong`, try-with-resources, `final` types, package-private teaching types. No raw threads, no `printStackTrace`, no stdout. |
| 3 | Security | **PASS** | Exploitable snippet is labelled a defect both in-code and in-prose and is never wired into a running path; the fix binds a parameter (injection removed, not mitigated); no hardcoded secrets; gate validates public input + stable reason codes; `RejectedContributionException` leaks no internals/stack traces. |
| 4 | Simplicity & readability | **FIX** | Smallest teaching code, realistic names, per-type purpose Javadoc — but one **unused dependency** (`spotbugs-annotations`, see F1) with a misleading justifying comment. |
| 5 | Prose↔code fidelity + originality | **PASS** | All 5 displayed include markers resolve and pass `check_snippets.sh`; every region ≤9 lines (max 8); identifiers/keys (`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`, OWASP A03/CWE-89, GAVs, `5_000` threshold, `4096` cap) trace to the pin/dossier; original-for-this-book (LEGAL-IP §5 confirmed in `_EXAMPLE.md`); one dangling `@link` (F2). |
| 6 | Neutrality in code | **PASS** | Zero banned phrasings and zero crowning words anywhere in src/config/pom/properties/README (and the draft prose); the SpotBugs-firing claim is framed source-agnostically ("the gate does not care who wrote the code"). |

---

## Critical task checks (explicitly requested)

| Check | Result | Evidence |
|---|---|---|
| Each displayed `// tag::` region brace-balanced (or clean opening excerpt) | **PASS** | `sql-concat` = a complete brace-balanced method; `sql-prepared`/`failure-path`/`under-test`/`weak-test`/`strong-test` = clean excerpts where every brace/paren opened inside the region closes inside it. No snippet ends mid-statement. |
| Each displayed region ≤ 9 lines | **PASS** | Body line counts: `sql-concat` 8, `sql-prepared` 8, `failure-path` 7, `weak-test` 6, `strong-test` 6 (`under-test` 5, not displayed). |
| No banned NEUTRALITY word in deliverable text | **PASS** | Full-tree + draft-prose grep for `better than / unlike X / superior / inferior / beats / the problem with X / worse than / outperforms / best-in-class / state-of-the-art …` → NONE. |
| Flawed (exploitable) side clearly labelled, never copyable as safe | **PASS** | In-snippet comment "…parsed as SQL, not data — **the defect**"; class Javadoc "deliberate teaching counter-example … never wired into a running path"; prose caption "the inherited pattern, with no warning sign". `AiReviewGate` wires only `ReviewedLookup`; `AiDraftedLookup` is unreachable from any running path. |
| FIX side genuinely correct (SQL concat → PreparedStatement) | **PASS** | `?` placeholder + `setString(1, email)`; verified by test `reviewedFixBindsTheEmailAsDataNotSql` (query text stays the constant `?` form; the `' OR '1'='1` payload is bound as data). |
| FIX side genuinely correct (weak vs spec-derived test) | **PASS** | `strong-test` pins both sides of the boundary (4_999+500=5_499; 5_000→free→5_000 — arithmetic verified) so the `CONDITIONALS_BOUNDARY` and `MATH` mutants die; `weak-test` asserts only non-null on the same covered lines, so the mutants survive. The contrast is real and correct. |
| Load-bearing SpotBugs suppression | **PASS** | Filter is maximally narrow (`Class=AiDraftedLookup` ∧ `Method=findIdsByEmail` ∧ `Bug pattern=SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`) — not a blanket detector/category disable. Code is a canonical trigger (non-constant `String` → `Statement.executeQuery`). Load-bearing recorded in `_EXAMPLE.md` (size 1 firing on line 44 with filter empty → size 0 restored, deterministic). See note below on re-proof. |
| No timeless AI statistic baked into code | **PASS** | No percentage/`%`/`vuln`-rate/productivity literal anywhere in src/config/pom/properties/README. The only numerics are domain constants (threshold `5_000`, body cap `4096`, ISO-4217, currency fixtures) and standard refs (OWASP Top 10:2025/A03/CWE-89, chapter pointers). `package-info` + POM comments explicitly state no statistic is embedded "because a figure baked into code would silently go stale." |

> **Re-proof note (no BLOCKER).** The user instruction "Do NOT edit code" was honoured; I therefore could not empty the repo's `spotbugs-exclude.xml` to re-prove the size-1 finding live (the classifier correctly blocked overwriting that config, and the spotbugs plugin reads the `<excludeFilterFile>` from POM config, shadowing a `-Dspotbugs.excludeFilterFile` user-property override; an offline standalone-engine run was not assemblable, deps not cached). The load-bearing property is nonetheless established conclusively by convergent evidence: (a) the `_EXAMPLE.md` recorded proof (size 1→0, line 44, deterministic), (b) the code being a textbook trigger for the exact pattern at effort=Max/threshold=Medium, (c) the filter being narrowly scoped so it can only suppress a finding that exists, and (d) the engine demonstrably analysing `AiDraftedLookup` (present in `target/spotbugsXml.xml` FileStats). Recommend the example-builder run the documented empty-filter→size-1→restore sub-procedure once more at registration time and paste the size-1 line into `_EXAMPLE.md` for an auditable artifact.

---

## Findings

| # | Severity | File:line | Issue | Fix (for example-builder) |
|---|---|---|---|---|
| F1 | MINOR (FIX) | `pom.xml:54-59` (+ justifying comment `46-53`) | `com.github.spotbugs:spotbugs-annotations:4.9.3` (provided) is declared and its comment says it is "for a per-site `@SuppressFBWarnings(value, justification)`", but **no source file imports or uses `@SuppressFBWarnings`** (0 occurrences across src+test) — the suppression lives entirely in the XML exclude filter. Unused dependency + a comment describing a non-existent usage. Readers copy this POM. | Either (a) remove the `spotbugs-annotations` dependency and its comment (the XML filter is self-sufficient), or (b) actually move the suppression onto the class as `@SuppressFBWarnings(value="SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE", justification="…")` and drop the `<Match>` from the filter, so the declared dep is genuinely used. (b) is the more idiomatic per-site form and keeps the "suppress with a reason" discipline visible in the code; (a) is the smaller change. Re-build `-Pquality verify` and re-confirm `BugInstance size is 0`. |
| F2 | MINOR (FIX) | `AiReviewGate.java:19` | Class Javadoc references `{@link #isReady()}` (no-arg), but the only method is `isReady(Connection)` — the `@link` is dangling (no such no-arg method). | Change to `{@link #isReady(java.sql.Connection)}` (or `{@link #isReady}`). Doc-only; no behaviour change. |
| N1 | NOTE | `_EXAMPLE.md:96` & draft back-matter | The pattern is described as "(High)" priority in `_EXAMPLE.md`/back-matter, but neither the filter XML nor the body prose assigns a priority and the rank is not independently shown in the build output captured here. Not load-bearing for code review. | Optional: drop the "(High)" qualifier unless the rank is pasted from a `spotbugsXml.xml` BugInstance, to keep every stated detail traceable. |
| N2 | NOTE | `08-companion-code/pom.xml` `<modules>` | Module is not yet registered in the reactor (`-pl … -am` builds it standalone via `<parent>`). | Expected and correct per the contract: register only after CODE-REVIEW PASS. No action in this gate. |

---

## Build / lint result

- `mvn -B -o -Pquality … -am clean verify` → **BUILD SUCCESS** (aggregator + module), reproduced this run.
- Tests: **12 / 12** pass across 5 nested classes (security pair, weak-test trap, spec-derived test, gate failure-path + readiness, externalized config). The `WARNING: rejecting negative amount …` line in output is an **asserted** test-path log from `rejectsNegativeAmounts`, not a build warning.
- Checkstyle: **0 violations** (curated house ruleset, `includeTestSourceDirectory=true`).
- SpotBugs: **`BugInstance size is 0`** (effort=Max, threshold=Medium) with the one narrow, reasoned, load-bearing suppression applied.
- Compiler: **warning-clean** under `-Xlint:all,-processing` (grep of `compile` output → no `warning:`/`unchecked`/`deprecation`).
- Secret scan: **no hardcoded secret/password/token/apikey literal** (the only `token` hits are the `provenanceToken` parameter identifier — not a literal); no long base64/hex literals.

---

## What is exemplary (worth keeping)

- The thesis is enacted by the build, not asserted: the same core SpotBugs rule fires on AI-drafted code, suppressed only narrowly with a reason and a pointer to the proving test.
- The exploitable snippet is triple-guarded against being copied as safe (in-snippet "the defect" comment, class Javadoc counter-example warning, and unreachable-from-any-running-path wiring).
- The weak-vs-spec-derived test pair is a precise, correct demonstration of "coverage ≠ detection," with mutant targets named inline in `under-test`.
- The "no statistic in code" discipline is explicit and enforced — the right call for a fast-moving topic.
- `RejectedContributionException` carries stable codes (not parseable free-text), and the failure path is fully branch-tested.

---

## Learnings & pipeline suggestions

- **Promote a check: "declared-but-unused analyzer-annotation dependency."** F1 is a recurring trap in the security-flavoured modules — a `spotbugs-annotations` GAV is added "in case" a `@SuppressFBWarnings` is needed, then the suppression is done in XML instead, leaving an unused dep with a comment that lies about its purpose. A one-line grep gate (`annotations GAV declared ⇒ `@SuppressFBWarnings` must appear in src`) would catch it mechanically. Worth a note in EXAMPLES-GUIDE and a row in the code-reviewer's dimension-4 checklist.
- **The load-bearing-suppression re-proof needs a non-destructive path.** The spotbugs-maven-plugin shadows the `spotbugs.excludeFilterFile` user property with the POM `<excludeFilterFile>` value, so a reviewer who is (correctly) barred from editing repo config cannot re-prove "size 1 with filter empty" live. Lifting the `_EXAMPLE.md`-described "empty filter → size 1 → restore → size 0" procedure into a tiny helper script that runs against a *copy* of the module in a temp dir (or parameterises the filter path via a `-D` property the POM reads) would make the proof mechanically re-checkable at each build without touching the source tree — and would let this gate emit a fresh size-1 artifact instead of relying on the recorded one.
- **Re-confirm the umbrella-chapter pattern holds:** a pure-concept chapter earned a genuinely useful, security-correct module by realizing only structural mechanisms (injection class + coverage-vs-mutation) on the shared domain and keeping every volatile figure in prose. This is a reusable template for the rest of Part XII.

Appended to `00-strategy/PIPELINE-LEARNINGS.md` (suggestion).
