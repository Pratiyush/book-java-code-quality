# GATE REPORT — EXAMPLE-BUILD — key 97 (The Draft That Looks Like a Deliverable)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 97 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`; leads Part XII, folds 99; Ch 41)
- **Slug:** `97_ai_generated_code_quality`
- **Draft under review:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Module built:** `08-companion-code/97_ai_generated_code_quality/` (artifactId `ai-generated-code-quality`)
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×6), `check_snippets.sh`; build via Maven `verify` (`-Pquality`)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand with the pinned toolchain)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

This chapter has a concrete RUNNABLE EXAMPLE SPEC (its foot-of-draft spec: an AI-drafted string-concat
SQL vuln that SAST flags + review fixes; an AI test-from-code whose mutants survive vs a spec-derived
test that kills them), so it is a BUILD, not an N/A. The module builds green deterministically at the
pinned toolchain (JDK 21.0.11), warning-clean, 0 Checkstyle, 0 unsuppressed SpotBugs, 12 tests passing.
All five displayed snippet tags resolve to bounded (≤9-line) tag regions inside the compiled files and
every prose include marker passes `check_snippets.sh`. The one deliberate counter-example that fires at
the gate (the AI-drafted SQL concatenation) carries a narrow, load-bearing, reasoned suppression
verified against the build (size 1 → 0). Every fact in the module traces to the dossier + SOURCE-PIN; no
atom is invented, and — per the chapter's own discipline — no AI statistic is embedded in code. Both
FLOOR-C preconditions hold and are logged below.

---

## FLOOR C guard — preconditions (both required for PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime ≥ Java 21 anchor | `java -version` → `openjdk version "21.0.11" 2026-04-21` | ✅ meets the SOURCE-PIN anchor (Java 21 LTS) |
| (b) `./mvnw -B verify` GREEN | `mvn -B -Pquality clean verify` → `BUILD SUCCESS`; `Tests run: 12, Failures: 0, Errors: 0`; `0 Checkstyle violations`; `BugInstance size is 0` | ✅ green, warning-clean |

> Toolchain note: the companion tree commits a Maven wrapper at its root; this run used a pinned local
> Maven 3.9.16 + JDK 21.0.11 (the `[MANUAL — tooling pending]` state). The exact command of record is
> `mvn -B -Pquality clean verify` run from `08-companion-code/97_ai_generated_code_quality/`. The module
> is a child of the one aggregator and is built standalone (against its `<parent>`) because it is not yet
> registered in the parent `<modules>` list (see "Module registration" below).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `failure-path` tag initially resolved to 10 lines (> 9-line ceiling) | MINOR | `AiReviewGate.java` (tag markers) | Moved the `// tag::` start past the body-too-large check so the displayed region leads with the AI-specific provenance check + the dispatch to the reviewed fix; now 7 lines. Re-verified by `extract_snippet.sh`. Fixed. |
| 2 | `-pl 97_… -am` against the parent fails ("not in the reactor") | NOTE | `08-companion-code/pom.xml` `<modules>` | Expected and correct: per the contract a module joins the parent module list ONLY after green build + CODE-REVIEW PASS. Built standalone via the child pom for now. No action. |
| 3 | arXiv AI-code studies + "slopsquatting" + CodeScene guardrails are NOT pinned rows in SOURCE-PIN §7 | NOTE | Dossier `⚠ §7 canon` + draft back-matter | Module deliberately embeds **no** AI statistic or study claim; it demonstrates only structural mechanisms that trace to already-pinned authorities (OWASP Top 10:2025 A03/CWE-89; SpotBugs; the mutation-testing mechanism). The prose-side study/figure verification is a SOURCE-VERIFY concern, not a build atom. See "Flags raised". |

---

## Blockers

None. (Finding #1 fixed during the run; #2 and #3 are NOTEs requiring no build change.)

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | How this module meets it |
|---|---|---|
| 1 | **Pinned platform (one inherited property)** | Child sets `<parent>` `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; carries no `<groupId>`/`<version>` literal and no own BOM. Java pin (`maven.compiler.release=21`) and the JUnit/AssertJ pins are inherited from the aggregator. The one version literal is the pinned `spotbugs-annotations:4.9.3` GAV (`provided`, compile-path only) — the same single-GAV pattern as the Ch 30 module, not a version-set. |
| 2 | **Externalized config profiles** | `src/main/resources/application.properties` externalizes the request-body cap and the provenance requirement under `%dev` / `%prod`, read by `AiReviewProfile`, selected with `-Dai.profile=prod` (default `dev`). Test `ExternalizedConfig#prodRequiresProvenanceWhileDevDoesNot` asserts the two postures differ. No hard-coded posture; no secret in any file. |
| 3 | **≥1 integration test of the chapter's mechanism** | `AiGeneratedCodeQualityTest` — 12 tests across 5 nested classes: the AI draft through the gate (injection demo), the tests-from-code trap (weak test), the spec-derived test (kills mutants), the gate failure path + readiness, externalized config. Runs under `verify`. **Test-harness setup:** none beyond the parent's surefire pin — the suite is plain JUnit 5 + AssertJ (inherited from the aggregator's `junit-bom`); confirmed by the clean 12/12 run with no spurious log-manager errors. |
| 4 | **Observability / health surface** | `AiReviewGate.isReady(Connection)` (readiness probe over the wired reviewed path) + `rejectedContributionCount()` (rejected-request metric); `OrderTotals.freeShippingGranted()` (a second illustrative counter). Driven by `readinessProbeReportsReadyWhenTheReviewedPathRuns` and the counter tests. |
| 5 | **Explicit failure path** | `AiReviewGate.acceptLookup` — a **real error response**: oversized / malformed / (prod) provenance-less contributions are turned away with `RejectedContributionException` carrying a stable code (`body-too-large`, `malformed-body`, `provenance-missing`). Every branch is driven by a test. This is the HONEST-LIMITATIONS floor in code: an AI draft is untrusted until verified, and what cannot be verified is refused, not merged. |

> Requirement 4 is genuinely in-scope here (the gate has a real readiness/metric seam), so no §1.2
> scoped-out reason is invoked.

---

## Snippet tags (tag-include anti-drift, EXAMPLES-GUIDE §5)

Five displayed regions, all confirmed ≤9 lines by `extract_snippet.sh` and all resolving via
`check_snippets.sh` (5 markers; 5 pass, 0 fail):

| Tag | File | Lines | Shows |
|---|---|---|---|
| `sql-concat` | `AiDraftedLookup.java` | 8 | the AI draft folding the email into the SQL text (inherited vuln) |
| `sql-prepared` | `ReviewedLookup.java` | 8 | the reviewed fix: bound `PreparedStatement` parameter |
| `weak-test` | `AiGeneratedCodeQualityTest.java` | 6 | the tests-from-code trap: full coverage, asserts only non-null |
| `strong-test` | `AiGeneratedCodeQualityTest.java` | 6 | the spec-derived test that pins both sides of the boundary |
| `failure-path` | `AiReviewGate.java` | 7 | the gate's reject path (provenance + malformed → reviewed fix) |

A sixth tagged region, `under-test` (`OrderTotals.java`, 5 lines — the boundary+arithmetic core), backs
the trap demo and has a home in the module but is not displayed in the prose.

**The thesis in the build.** The AI-drafted query is the one shape core SpotBugs raises at this
threshold: `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (High), verified load-bearing by emptying the
exclude filter (BugInstance size 1, firing on `AiDraftedLookup.findIdsByEmail` line 44) and restoring it
(size 0). The same rule fires whether a human or a model wrote the line — the gate is source-agnostic,
which is the chapter's central claim, demonstrated by the build rather than asserted.

---

## Captured screenshots (Step 4c)

**No captures planned.** This chapter's figure plan is one designed conceptual diagram, `fig97_1`
(already authored as HTML → PNG at `05-figures/97_ai_generated_code_quality/`), not a subject-native UI
capture. The module is a JDK-only library (no dev console, API explorer, or live health UI to
screenshot). Per Step 4c, designed diagrams are authored separately and are not the example-builder's
job; with zero planned captures, this step is recorded as complete with no PNGs or sidecars produced.

---

## LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK (file-by-file)

Every file under `08-companion-code/97_ai_generated_code_quality/` is original work written for this
book, confirmed file-by-file. None is a lightly-edited upstream sample, quickstart skeleton, or `_ref/`
listing, and none carries upstream `NOTICE`/header boilerplate. The SQL-injection demonstration and the
`java.sql` test double are structurally parallel to the Chapter 30 module (`69_secure_coding_owasp`),
which is itself this book's original work on the shared storefront domain; the parallel is a deliberate
in-book motif (the "AI draft vs human draft, same gate" framing), not a copy of any external source. No
region is taken substantially verbatim from a specific upstream file, so no per-file attribution is
required.

---

## Source-trace (every load-bearing atom → pin)

| Atom in module | Traces to |
|---|---|
| SQL injection class (string-concat → `PreparedStatement`) | OWASP Top 10:2025 A03 / CWE-89 (SOURCE-PIN; dossier §2) — same authority as the Ch 30 module |
| `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (SpotBugs pattern) | SpotBugs (SOURCE-PIN §3, SpotBugs 4.10.2 line; module builds against locally cached 4.9.3 engine, as the Ch 30 module does) — verified firing against the build |
| Checkstyle engine `10.26.1`; `spotbugs-maven-plugin 4.9.3.0`; `spotbugs-annotations 4.9.3` | SOURCE-PIN §3 + the established companion-code two-pin pattern (Ch 30 module) |
| Java 21 baseline; JUnit `6.0.3`; AssertJ `3.27.7` | aggregator `08-companion-code/pom.xml` (SOURCE-PIN §3), inherited |
| Mutation-testing mechanism (surviving mutant on a covered line; `CONDITIONALS_BOUNDARY`, `MATH`) | the Ch 23 mutation-testing mechanism, mirrored from the `48_coverage_mutation_effectiveness` module's worked pattern |
| "suppress with a documented reason, never disable a detector" | Ch 16 discipline (the curated-ruleset / reasoned-suppression rule the companion code dogfoods) |
| AI statistics / arXiv studies / slopsquatting / CodeScene guardrails | **deliberately NOT in code** — prose-only, dated + attributed; figures date, mechanisms do not (the chapter's own discipline) |

---

## Floor-C verdict

**PASS.** Zero invented `rule IDs / config keys / tool flags / API signatures / GAV coordinates / version
numbers / benchmark figures / quoted claims`; the module builds green via `-Pquality verify` at the
pinned toolchain, warning-clean; both FLOOR-C preconditions hold. (The CODE-REVIEW gate — Step 4b's
second half, the `code-reviewer` agent — and the module's registration in the parent `<modules>` list
remain downstream of this report.)

---

## Module registration (deferred, per contract)

`97_ai_generated_code_quality` is **NOT** yet added to `08-companion-code/pom.xml` `<modules>`
(`grep -c` → 0). Per EXAMPLES-GUIDE §2 and the parent-pom contract, a module joins the reactor ONLY after
green build AND CODE-REVIEW PASS. This report records the green build; registration is left for after the
CODE-REVIEW gate so a red or unreviewed module never enters the build. (Per the task scope, the parent
`pom.xml` was not edited.)

---

## Learnings & pipeline suggestions

- **A "pure-concept umbrella" chapter can still earn a runnable module without inventing facts**, by
  realizing only the *structural* mechanisms (string-concat injection; full-coverage-yet-mutants-survive)
  on the shared domain and keeping every volatile figure in prose. The chapter's own "every AI statistic
  is a dated snapshot, never a constant" discipline maps cleanly onto a code rule: *embed no statistic in
  a companion module* — a number baked into code silently goes stale and would become an un-dated,
  un-attributed claim the moment it ships. Worth promoting a one-line note to EXAMPLES-GUIDE §8.2.
- **The tests-from-code trap reuses the Ch 23 weak-vs-strong-test pattern almost exactly** (a correct
  implementation, an assertion-light test that leaves boundary/MATH mutants alive, and a spec-derived
  test that kills them). This is a strong sign the mutation-testing module is a reusable scaffold for any
  chapter whose thesis is "coverage is not detection" — the AI-era framing is new, the mechanism is not.
- **The load-bearing-suppression proof (empty the filter → confirm size 1 on the named pattern → restore
  → size 0) is now a repeatable sub-procedure** across the security-flavored modules (Ch 19, Ch 30, and
  now Ch 41). Consider lifting it into a tiny helper script so "the suppression is reasoned, not
  decorative" is mechanically re-checkable at each build rather than by hand.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 97 gate-run PASS
```
