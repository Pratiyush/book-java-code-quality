# GATE REPORT — EXAMPLE-BUILD — Chapter 2 (`03_readability_maintainability`)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 03 (folds 04, 58) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `03_readability_maintainability`
- **Draft under review:** `03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`
- **Module path:** `08-companion-code/03_readability_maintainability/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh`, `check_snippets.sh`; build via `mvn -B -Pquality … verify`
- **Build state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** (FLOOR C)

---

## Verdict rationale

The chapter has concrete before/after readability code to show — the dossier's worked example ("the same
Java method written three ways: deeply nested, over-fragmented, balanced — each with its cognitive
complexity behaviour"). It is built as a runnable module: one discount rule in three forms behind a single
`DiscountRule` contract, proven behaviour-identical by a shared parameterized test. The module builds
green under `mvn -B -Pquality verify` on JDK 21.0.11 (warning-clean: `-Xlint:all`, 0 Checkstyle
violations, 0 SpotBugs findings). All three displayed snippets resolve to real tag regions ≤9 lines. No
invented atom — `java:S3776` and the cyclomatic/cognitive distinction trace to the pin. Module is NOT a
N/A case: the code is the chapter's central teaching artifact.

---

## Decision: BUILD (not N/A)

Per the task's foundational-chapter check: the draft is largely conceptual, but it carries concrete
before/after readability code (nesting → guard clauses; over-fragmentation as a cost), and both the draft
footer's RUNNABLE EXAMPLE SPEC and the dossier §6 specify the three-forms-of-one-method demo. That is
runnable before/after code, so a module is built rather than marked N/A.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime meets the minimum (Java 21+).** `openjdk version "21.0.11" 2026-04-21` — meets the
  SOURCE-PIN anchor (JDK 21 LTS). Maven `Apache Maven 3.9.16` (matches the pin row).
- **(b) Build GREEN.** Exact command (run from repo root against the module pom, single-module — the
  module is deliberately NOT yet in the aggregator `<modules>` list):

  ```
  mvn -B -Pquality -f 08-companion-code/03_readability_maintainability/pom.xml clean verify
  ```

  Result line: **`BUILD SUCCESS`** — `Tests run: 43, Failures: 0, Errors: 0, Skipped: 0`;
  `You have 0 Checkstyle violations.`; `BugInstance size is 0` (SpotBugs).

Both preconditions hold → FLOOR-C verdict is a real PASS, not assumed.

---

## Tag-includes and resolved line counts (≤9 ceiling)

| Tag | Backing file | Lines | check_snippets |
|---|---|---|---|
| `smell-nested` | `…/DiscountRulesNested.java` | 7 | PASS |
| `smell-fragmented` | `…/DiscountRulesFragmented.java` | 7 | PASS |
| `refactor-balanced` | `…/DiscountRules.java` | 9 | PASS |

`check_snippets.sh 03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md` →
`3 marker(s); 3 pass, 0 fail.`

Markers inserted in the Deep-dive "Controlling complexity without gaming it" section, after the existing
WARNING block, each with a one-line locked-voice lead-in (no prose deleted), closed by a
`Snippet tags: smell-nested, smell-fragmented, refactor-balanced` line. The displayed snippet and the
runnable file are one artifact.

---

## Enterprise-grade checklist

| # | Requirement | How met |
|---|---|---|
| 1 | **Pinned platform (one inherited property)** | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no `<groupId>`/`<version>` literal; no own BOM. `maven.compiler.release=21` and test-lib versions inherited from the aggregator. Analyzer engines (Checkstyle 10.26.1, SpotBugs plugin 4.9.3.0) pinned in the module's `quality` profile, matching peers 09/19. |
| 2 | **Externalized config profiles** | `src/main/resources/readability.properties` — `%dev`/`%prod` analysis-profile keys + pricing policy (currency, discount cap, no-discount floor) as config, not constants. |
| 3 | **≥1 integration test (harness configured)** | `DiscountRulesTest` (JUnit Jupiter + AssertJ, both inherited test-scope). 43 executed (parameterized over 36 cart combinations + targeted cases). Surefire auto-detects the JUnit Platform provider — no extra harness property needed (confirmed by the green run reporting the provider). |
| 4 | **Observability / health surface** | `PricingService.rejectedCount()` (running rejection counter) and `isReady()` (readiness probe over the configured policy) — same shape as peer 19's `rejectedCount()`/`isReady()`. |
| 5 | **Explicit failure path (test-driven)** | `PricingException` (typed, stable `cap-below-floor` reason code). `everyFormRejectsACapBelowTheFloorWithTheSameTypedError` drives it across all three forms; `serviceCountsRejectionsThroughTheFailurePath` proves the counter increments. |

Deliberate "before" smells (deep nesting, over-fragmentation) are metric/AST shapes, NOT bytecode bugs,
so the house SpotBugs gate does not flag them and **no suppression is needed** — the
`spotbugs-exclude.xml` is empty by design (the inverse of peer 19, which carries one reasoned suppression
for a real bytecode bug). This is the chapter's own "different tools see different smells" point, made
concrete and honest rather than hidden behind a suppression.

---

## Captured screenshots (Step 4c)

**No captures planned.** The figure plan (draft Step-9 block) calls for exactly two designed HTML→PNG
diagrams (Fig 03.1 cyclomatic-vs-cognitive; Fig 03.2 the contested map), both already authored under
`05-figures/03_readability_maintainability/` (fig03_1/2.{html,png,sources.md}) — these are designed
diagrams, authored separately, never produced here. The module is a pure JDK library (no dev console, API
explorer, or health-view UI surface), so there is no subject-native UI to capture. Step 4c is correctly
skipped.

---

## NEUTRALITY-in-code

Banned-phrase scan (`better than`, `unlike X`, `superior`, `beats`, `the problem with X`) across all
module files → **CLEAN** after one fix: two "better than" uses (comparing code *shapes*, not tools) were
rephrased to "read more clearly than" in `DiscountRules.java` Javadoc and `README.md`. The two contested
schools (tiny functions vs deep modules) are presented as a context-dependent trade-off in comments and
README, with neither form crowned. The draft insertion was scanned and is clean.

---

## LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK confirmation

Every file confirmed original for this book, file by file. The module realizes the shared storefront
pricing motif (DEMO-CATALOG) but copies **no** file from any upstream sample, quickstart, or `_ref/`
listing, and **no** file from peer module 19 — the value types (`Money`, `LoyaltyTier`), the rule trio,
the service, and the test were authored fresh for this chapter's demo (package `org.acme.readability`,
discount-rule domain, not module 19's order-placement domain). No `NOTICE`/header boilerplate copied. No
substantially-verbatim block from a pinned source. No attribution debt.

---

## Source-trace (every fact → pin path)

| Fact in module | Pin source (`SOURCE-PIN.md`) |
|---|---|
| `java:S3776` (Cognitive Complexity rule id) | SonarQube/Sonar rules row — Server 2026.1 LTA; SonarSource Cognitive Complexity white paper |
| Cognitive complexity "increments more for nesting" | SonarSource Cognitive Complexity white paper (dossier §2.2) |
| Cyclomatic complexity = independent execution paths; "how many tests" | McCabe, *A Complexity Measure* (1976) — book canon / dossier §2.2 |
| Records derive `equals`/`hashCode`/`toString`; value-type idiom | JLS SE 21 (records); *Effective Java* 3rd ed. (value types) |
| Guard clauses / Extract Function framing | *Refactoring* 2nd ed. (Fowler) — book canon |
| Deep-modules vs tiny-functions contested framing | *A Philosophy of Software Design* (Ousterhout) vs *Clean Code* (Martin) — book canon, presented neutrally |
| Java 21 baseline; `-Xlint:all` compiler floor | SOURCE-PIN runtime row (JDK 21.0.11) + aggregator pom |
| Checkstyle 10.26.1 / SpotBugs plugin 4.9.3.0 (engine pins) | SOURCE-PIN §2 rows; matched to peers 09/19 |

No atom required that the dossier + pin lack. No `UNVERIFIED` mark, no `09-flags/` gap raised.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Two "better than" uses (banned phrasing) comparing code shapes | MAJOR | `DiscountRules.java` L18; `README.md` L64 | Fixed → "read more clearly than" |
| 2 | `cap-negative` guard was unreachable (`Money` forbids negative minor units) — dead code | MAJOR | all three rule forms | Fixed → removed; failure path is the reachable `cap-below-floor` |
| 3 | `smell-nested` (34→) and `refactor-balanced` (15→) regions initially exceeded the 9-line ceiling | MAJOR | tag positions | Fixed → markers repositioned to bound a representative ≤9-line core (7 / 9 lines) |
| 4 | Module deliberately not yet in aggregator `<modules>` list | NOTE | `08-companion-code/pom.xml` | By design — registers only after CODE-REVIEW PASS (EXAMPLES-GUIDE §2) |

---

## Blockers

**None.** Build green on a conforming runtime; all snippets resolve within ceiling; FLOOR C source-trace
clean; LEGAL-IP §5 original confirmed.

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b) — module builds green via `mvn -B -Pquality verify` at the pin; every
  displayed snippet resolves to a real bounded tag region (≤9) in the compiled file; FLOOR C source-trace
  clean.
- [ ] **CODE-REVIEW** — pending (Step 4b second half, `code-reviewer` agent). Module stays OUT of the
  aggregator `<modules>` list until CODE-REVIEW PASS.

---

## Learnings & pipeline suggestions

- **A "conceptual" foundation chapter can still carry a strong runnable demo.** Chapter 2 reads as a
  measurement-discipline essay, yet its load-bearing idea (cognitive vs cyclomatic complexity) is most
  convincingly shown by *three behaviour-identical forms of one method* whose only difference is shape.
  The honesty floor is automatic: the test proves the cognitive score and the result are independent axes,
  which is exactly the chapter's "the number lies" thesis in code. Worth a note in
  `CHAPTER-TEMPLATE.md` that contested/measurement chapters often have a stronger demo than their prose
  suggests — check the dossier's worked-example spec before defaulting to N/A.
- **The empty-suppression case is as instructive as the suppression case.** Peer 19 suppresses one real
  bytecode bug; this module's deliberate smells are metric/AST shapes SpotBugs does not measure, so the
  exclude filter is empty *by design*. Documenting *why* an exclude file is empty (rather than deleting it)
  keeps the "suppress with a reason, never disable a detector" discipline legible and shows the detection
  boundary from both sides. Consider promoting "an empty suppression file with a documented reason is a
  valid, expected state" into EXAMPLES-GUIDE §8.2.
- **Tag-region sizing is easiest when the method is authored with a ≤9-line core in mind.** Two regions
  needed repositioning post-hoc. For deliberately-long "before" code, place the tag around the single
  representative slice (the deepest nest, the delegating head) from the start.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 03 gate-run PASS
```
