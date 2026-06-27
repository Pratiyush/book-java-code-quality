# GATE REPORT — CODE-REVIEW (key 84)

## Header

- **Gate:** CODE-REVIEW (Step 4b — FLOOR-C second half; senior-PR review of published-deliverable code)
- **Chapter key:** 84 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 86 + 89)
- **Slug:** `84_code_review_standards_documentation`
- **Draft under review:** `03-drafts/84_code_review_standards_documentation/84_code_review_standards_documentation_v1.md`
- **Module path:** `08-companion-code/84_code_review_standards_documentation/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Build re-run:** `mvn -B -Pquality clean verify` at `JAVA_HOME=/opt/homebrew/opt/openjdk@21` (JDK 21.0.11) — re-verified green by this gate
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is correct, idiomatic Java 21, secure, minimal, well-tested (real failure-path coverage), and
original-for-this-book; the build is green and **warning-clean** (0 `[WARNING]` lines), and all five
displayed tag regions are element/brace-balanced, ≤9 lines, and free of banned phrasings. One required fix
holds the verdict short of a clean PASS: a literal NEUTRALITY-blocklist phrase ("better than") in a
**committed deliverable file** (`docs/CODE_REVIEW_GUIDELINES.md:5`). It is **not** inside a displayed
tag region (so it is **not a displayed-region BLOCKER**), but NEUTRALITY.md §banned-phrasings makes the
construction an automatic floor failure "anywhere, code included," and this file ships to readers. The fix
is a one-line reword and is **required before FLOOR C is cleared**, not optional. No security, no
invention, no displayed-region defect.

---

## Six review dimensions

| # | Dimension | Verdict |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java code quality & Java 21+ | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity + originality/attribution (LEGAL-IP §5) | **PASS** |
| 6 | Neutrality in code | **FIX** |

### 1. Correctness — PASS
- `RefundPolicy` guards construction (`linePriceCents < 0` → `IllegalArgumentException`) and the public
  method (`daysSincePurchase < 0` → throw); the boundary `> REFUND_WINDOW_DAYS` is exclusive, so a return
  *on* day 30 still refunds (asserted at `RefundPolicyTest:26`). The window-closed path returns a defined
  `0L`, not a thrown signal for an ordinary outcome — a genuine, documented failure path.
- `ReviewThroughputHealth.report` rejects a negative median and partitions HEALTHY / WATCH / DEGRADED on
  `> CEILING/2` and `> CEILING`; integer division `EFFECTIVE_REVIEW_CEILING_LINES / 2` = 150 is intended
  and the WATCH band (151–300) is exercised at value 200.
- Tests are non-vacuous and assert behaviour: the failure path is real (`pastTheWindow...ZeroRatherThanAThrow`,
  `theDocumentedPreconditionsAreEnforced`, `aNegativeMedianIsRejected`), not a smoke test. No resource use,
  no `catch` blocks anywhere (nothing swallowed), no leaks. `equals`/`hashCode` are consistent.

### 2. Idiomatic Java code quality & Java 21+ — PASS
- Pattern-match `instanceof` in `equals` (`other instanceof RefundPolicy policy && ...`) — Java 21 idiom.
- `final` value-ish class with private field; utility class `ReviewThroughputHealth` correctly has a
  `private` no-arg constructor and `static` API; nested `enum Status` with per-constant Javadoc.
- `long`/`int` chosen deliberately (cents as `long`, day/line counts as `int`); underscore literals
  (`1_299L`) in tests; `UpperEll` honoured (`0L`, `1_299L`). No raw threads, no blocking, no `System.out`
  logging, no ad-hoc concurrency — consistent with the JDK-only ADR.

### 3. Security — PASS
- Grep for `password|secret|token|apikey|private-key|credential`-style literals across the tree (excluding
  `target/`): **no hits**. No hardcoded secrets.
- Public entry points validate input and fail fast; no injection sink (no SQL/exec/reflection/file I/O);
  error messages echo only the offending numeric value, never internals or a stack trace. `CODEOWNERS`
  handles are explicitly illustrative (`@acme/...`), not real accounts.

### 4. Simplicity & readability — PASS
- Smallest code that teaches the point; no dead code, no unused deps (`spotbugs-annotations` is `provided`,
  used by the toolchain; JUnit/AssertJ are `test`). No gratuitous abstraction.
- Realistic names throughout (`org.acme.review`, `RefundPolicy`, `refundCents`, `ReviewThroughputHealth`) —
  no `Foo`/`Bar`/`tmp`/placeholder; no `TODO`/`FIXME`. Every public type carries a one-line purpose Javadoc
  (read-cold friendly). `package-info.java` orients the reader to the two halves.

### 5. Prose↔code fidelity + originality — PASS
- All **five** `<!-- include: -->` directives in the draft resolve to exactly five paired `tag::/end::`
  regions; nothing dangling, nothing unreferenced. Each displayed region (counts below) is ≤9 lines.
- Tool atoms in code trace to the pin or its recorded delta: Checkstyle check names + per-check property
  keys (`MissingJavadocType`/`scope`, `MissingJavadocMethod`/`scope`, `JavadocMethod`/`accessModifiers`+
  `validateThrows`, `SummaryJavadoc`) match the cached engine (checkstyle 10.26.1); GAVs
  (`maven-checkstyle-plugin:3.6.0`, `spotbugs-maven-plugin:4.9.3.0`, `spotbugs-annotations:4.9.3`) match the
  built peer set. Named-canon figures (Cohen/SmartBear ~100-300 LOC / 30-60 min; Google eng-practices "code
  health"; Nygard ADR) are **attributed in-place** to their source everywhere they appear in the module and
  are SOURCE-PIN §7 gaps flagged at `09-flags/84_code_review_canon_figures_and_engine_delta.md` (flag file
  present) — attributed + flagged, never asserted as the book's own fact, so no invention.
- **Originality (LEGAL-IP §5):** every file is original-for-this-book, modelled on this book's own peer
  modules (27/39), not an upstream quickstart/sample. The ADR uses only the generic Nygard heading
  convention (attributed); the PR template / CODEOWNERS carry no upstream boilerplate. No unattributed
  verbatim lift. (Build output skin CSS under `target/site/` carries Apache-License strings — generated,
  gitignored, never committed; not a source file.)

### 6. Neutrality in code — FIX
- `docs/CODE_REVIEW_GUIDELINES.md:5` contains the literal NEUTRALITY blocklist phrase **"better than"**
  ("a checklist a team will actually keep is *better than* an exhaustive one it ignores"). Semantically it
  compares two hypothetical checklists, not a tool/practice/comparator — so it is benign in intent and
  **does not crown any rival**. But NEUTRALITY.md (§"banned phrasings", line 35) lists "better than" in the
  blocklist and (line 45) makes any occurrence an **automatic FAIL of the neutrality floor**; the auditor's
  scan is a literal banned-phrase pass, and this is a **committed deliverable file readers receive**. It is
  **not** inside any displayed `tag::` region (the displayed `review-checklist-item` region is lines 36-42),
  so it is **not a displayed-region BLOCKER** — but it must be reworded before FLOOR C clears.
- No other comment, identifier, log string, or config value crowns or disparages a comparator. "Best Kept
  Secrets of Peer Code Review" (book title) and "crown no doctrine" (the anti-crowning idiom) are not
  violations.

---

## Findings

Severity scale: BLOCKER · MAJOR · MINOR · NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Literal NEUTRALITY blocklist phrase "better than" in a committed deliverable doc (not in a displayed region; does not crown a rival, but trips the automatic banned-phrase FAIL) | **MAJOR** | `08-companion-code/84_code_review_standards_documentation/docs/CODE_REVIEW_GUIDELINES.md:5` | Reword without any blocklist token (avoid "better than"/"beats"/"superior"). Suggested: "...deliberately short: a checklist a team will actually keep, rather than an exhaustive one it ignores." |
| 2 | Named-canon figures (Cohen/SmartBear ~100-300 LOC, Google eng-practices "code health", Nygard ADR) are SOURCE-PIN §7 gaps; two appear in or near displayed regions | NOTE | `pull_request_template.md:21-22,44`; `ReviewThroughputHealth.java:14`; `CODE_REVIEW_GUIDELINES.md:15-16`; `README.md:82-83`; `docs/adr/0001-...:16` | None for CODE-REVIEW: each is attributed to its source in-place and flagged at `09-flags/84_code_review_canon_figures_and_engine_delta.md`. Pin-status is the VERIFY gate's to clear at re-pin. |
| 3 | Checkstyle house engine 10.26.1 trails SOURCE-PIN §2 (13.6.0) | NOTE | `pom.xml` `quality` profile (lines 86-92) | None — recorded two-pin delta shared by all built peers; all Javadoc checks exist on both lines. Re-build at re-pin (runbook step 4). |
| 4 | `javadoc-contract` displayed region ends on the method signature line, leaving an open `{` brace (body excluded by `end::` at line 47) | NOTE | `RefundPolicy.java:38-47` | None — this is a deliberate, clean **opening excerpt**: the full Javadoc `/** ... */` is element-balanced and the signature anchors `@param`/`@return`/`@throws`; it is not a broken mid-statement. Required this way so the `tag::` pair does not sit *between* the Javadoc and its method (which would break the `MissingJavadocMethod` association). |

---

## Blockers

**None.** No security finding, no invention, no neutrality phrase inside a displayed tag region, no
displayed-region balance/length defect. Finding #1 is MAJOR (required-before-FLOOR-C) but not a BLOCKER
under this gate's displayed-region blocker rule.

---

## Build / lint result (re-verified by this gate)

- **Command:** `mvn -B -Pquality clean verify` with `JAVA_HOME=/opt/homebrew/opt/openjdk@21/...` (JDK 21.0.11).
- **Result:** `BUILD SUCCESS`. `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`
  (`TheRefundContract` 3 + `TheHealthSurface` 2). `You have 0 Checkstyle violations.`
  `BugInstance size is 0` / `No errors/warnings found`.
- **Warning-clean:** `[WARNING]` line count over the full build = **0**.
- **Failure-path test present:** YES — `pastTheWindowTheRefundIsZeroRatherThanAThrow` (defined zero),
  `theDocumentedPreconditionsAreEnforced` and `aNegativeMedianIsRejected` (fail-fast guards). The real
  failure paths are exercised, not a vacuous smoke test.
- **Hardcoded-secret grep:** no hits (password/secret/token/apikey/key/credential), `target/` excluded.

### Displayed tag-region audit (all ≤9 lines, all balanced)

| Tag | File | Body lines | Balance |
|---|---|---|---|
| `review-checklist-item` | `docs/CODE_REVIEW_GUIDELINES.md` | 5 | Self-contained checklist; clean |
| `pr-checklist-item` | `.github/pull_request_template.md` | 5 | HTML comment + 4 items; clean |
| `codeowners-rule` | `.github/CODEOWNERS` | 4 | 2 comments + 2 path/owner rules; clean |
| `javadoc-contract` | `src/main/java/org/acme/review/RefundPolicy.java` | 8 | Javadoc fully balanced; clean **opening excerpt** to the signature (dangling `{` is intentional, see finding #4) |
| `javadoc-presence-rule` | `config/checkstyle/checkstyle.xml` | 7 | 3 complete `<module>...</module>` elements; element-balanced |

---

## Chapter-specific artifacts the chapter teaches (reviewed)

- **Checkstyle Javadoc-presence rule** (`config/checkstyle/checkstyle.xml`) — `MissingJavadocType` +
  `MissingJavadocMethod` (`scope=public`) + `SummaryJavadoc` enforce PRESENCE; `JavadocMethod`
  (`accessModifiers=public`, `validateThrows=true`) + `AtclauseOrder` + `NonEmptyAtclauseDescription`
  enforce well-formedness. Scoped to the public API only, `includeTestSourceDirectory` left false — matches
  the prose ("presence, not quality"). Load-bearing per the EXAMPLE gate's strip-and-rebuild.
- **PR template** (`.github/pull_request_template.md`) — splits author "mechanical is automated" confirmations
  from a reviewer "substantive only" checklist; faithfully encodes the bot/human division the chapter argues.
- **CODEOWNERS** (`.github/CODEOWNERS`) — last-match-wins ordering documented; contract surface + ADR log
  routed to stricter owners; illustrative handles. Correct GitHub semantics.
- **Review checklist** (`docs/CODE_REVIEW_GUIDELINES.md`) — docs-as-code, Google-ordered, with an explicit
  "honest limits" section. (Carries finding #1.)
- **Exemplar Javadoc class** (`RefundPolicy`) — Javadoc-as-contract (what / `@param` / `@return` /
  `@throws` / nullability), no "what" narration; the chapter's documentation standard made concrete.
- **ADR** (`docs/adr/0001-...`) — Nygard form (Status/Context/Decision/Consequences), attributed, bespoke
  content. **Observability surface** (`ReviewThroughputHealth`) — read-only, gates nothing, as claimed.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity / prose-code-fidelity PASS;
  neutrality-in-code FIX (one reword required). Build green + warning-clean; failure-path test present;
  every displayed region balanced, ≤9 lines, banned-phrase-free.
- **Re-review required after fix #1**, then this gate flips to PASS and the module may be registered into
  `08-companion-code/pom.xml` `<modules>`.

---

## Learnings & pipeline suggestions

- **Banned-phrase scanning must cover the companion module, not only the draft prose.** The EXAMPLE gate's
  source-trace and the AUDIT gate's neutrality scan both run against the chapter text; a benign-sounding
  "better than" survived inside a deliverable `docs/` file because no scan walks `08-companion-code/**`.
  Suggest the (not-yet-built) `check_neutrality.sh` pre-pass include the chapter's module tree
  (`*.md`/`*.java`/`*.xml`/`CODEOWNERS`, excluding `target/`), and that the EXAMPLES-GUIDE add a line:
  "the blocklist applies to every committed companion file, comments and docs included, even where the
  phrase crowns no rival — it is a lexical ban."
- **A clean opening excerpt is the right tag shape for Javadoc-as-contract.** Ending the region on the
  method signature line (dangling `{`) keeps the `tag::` pair from splitting the Javadoc from its method
  (which would break `MissingJavadocMethod`) while still showing the full contract. Worth recording in the
  snippet-machinery notes as the sanctioned pattern for "show a Javadoc + its signature, not the body."
- **Attribution-in-place is what lets unpinned named figures live in a deliverable.** Every Cohen/SmartBear
  and Google/Nygard figure here is sourced in the same sentence and flagged; that is the discipline that
  keeps a §7 pin gap a NOTE rather than an invention. Reusable pattern for any process-centric module.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 84 gate-run PASS-WITH-FIXES
```

---
**ORCHESTRATOR FIX — 2026-06-27 — MAJOR (FLOOR-A) RESOLVED.** docs/CODE_REVIEW_GUIDELINES.md
carried the banned NEUTRALITY phrase "better than" ("a checklist a team will actually keep is
better than an exhaustive one it ignores"). Reworded to "...a team will actually keep, rather
than an exhaustive one it ignores." — no blocklist token, same meaning. Rebuilt green.
**Verdict → PASS.**
