# GATE REPORT — CODE-REVIEW (key 39)

## Header

- **Gate:** CODE-REVIEW (Step 4b, second half — FLOOR-C judgment layer)
- **Chapter key:** 39 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `39_managing_findings`
- **Draft under review:** `03-drafts/39_managing_findings/39_managing_findings_v1.md`
- **Module path:** `08-companion-code/39_managing_findings/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` (senior-PR review of code readers copy)
- **Build env:** JDK 21.0.11 (Homebrew openjdk@21), Apache Maven 3.9.x, offline cache
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is correct, idiomatic Java 21, secure, simple, and well-tested; its load-bearing
suppression and baseline are genuinely narrow, justified, and both verified load-bearing by removing
each and observing the gate go red. All seven displayed `// tag::` regions are brace-balanced, ≤9
lines, complete (not mid-statement), and free of duplicate/orphan end-tags. **No BLOCKER, no
security finding, no neutrality finding, no invented atom.** The verdict is PASS-WITH-FIXES, not a
clean PASS, for **one MAJOR prose↔reality contradiction** (the module's own README + pom comment +
EXAMPLE report say it is "not yet listed in `<modules>`," but the parent pom already registers it)
plus two MINOR polish items on published code (an unguarded public factory and a mutable-list
return). None blocks FLOOR C; all are for the example-builder to reconcile, then re-review.

---

## The six dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 & code quality | **PASS** (2 MINOR polish) |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity (+ originality/attribution) | **FIX** (1 MAJOR doc contradiction) |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `FindingTriage.triage` is a total function: every `Finding` takes exactly one branch (false-positive →
  SUPPRESS, pre-existing → BASELINE, else FIX). Order is correct and matches the prose ladder.
- `FindingRatchet.newFindings` correctly grandfathers the frozen set by `ruleKey@location` key and
  returns only genuinely-new findings; `passes`/`baselineSize` are consistent with it. The test
  `aNewFindingFailsTheRatchetWhileLegacyIsGrandfathered` proves the same rule key at a *new* location
  is not grandfathered — the chapter's whole value proposition, asserted (not vacuous).
- `Finding` validates at construction and **refuses to represent an unjustified false positive** — the
  HONEST-LIMITATIONS floor in code; the failure-path test exercises the real failure path
  (`aFalsePositiveWithoutAReasonCannotBeRepresented`, blank `"  "` justification → IAE).
- No resource leaks; no swallowed exceptions; no empty catch. Defensive copies on the clean class
  (`Map.copyOf`, `Arrays.copyOf`, `.clone()`) are real and tested (`isNotSameAs` on repeated calls).
- **Tests are non-vacuous and assert behaviour** (15 tests, 6 `@Nested` groups). Re-run green this
  session: `Tests run: 15, Failures: 0, Errors: 0, Skipped: 0`.

### 2. Idiomatic Java 21 & code quality — PASS (2 MINOR)
- Records (`Finding`, `GateHealth`) with compact-constructor validation; sealed-intent enums
  (`Disposition`, `GateHealth.Status`); ternary; streams + `Collectors.toUnmodifiableSet`; `Optional`
  for the absent-SKU contract; `Locale.ROOT` on `String.format`. All read well and teach the point.
- `@SuppressFBWarnings` from `edu.umd.cs.findbugs.annotations`, `provided` scope (class retention) —
  the canonical, documented surface; matches peer Ch 27.
- No anti-patterns: no raw threads, no blocking, **no `System.out`/`err`** (grep clean), no ad-hoc
  logging. Build is **warning-clean** under the parent's inherited `-Xlint:all` (clean compile scan
  returned no compiler warnings).
- *MINOR (M2):* `FindingRatchet.newFindings` returns `Collectors.toList()` (mutable, no type
  guarantee) where the rest of the module favours immutability — see findings table.
- *MINOR (M3):* `GateHealth.report` factory does not null-guard its own params — see findings table.

### 3. Security — PASS
- Hardcoded-secret scan (password/secret/token/apikey/credential/PEM/AKIA over `src`, `config`,
  `pom.xml`): **no hits.** No secrets, no env-leak.
- No injection sink, no reflection, no deserialization, no file/network I/O. Construction-time
  validation rejects negative prices and blank identifiers with safe messages (echo only the
  offending value/SKU, no internals/stack-trace leak). This is a pure in-process policy/library
  slice — no public endpoint to validate beyond the constructor guards, which are present and tested.

### 4. Simplicity & readability — PASS
- Smallest code that teaches each lever; no dead code, no gratuitous abstraction, no unused deps
  (the one extra GAV, `spotbugs-annotations`, is load-bearing). No `Foo/Bar/tmp/placeholder` names
  (grep clean); realistic `org.acme.findings` domain (PricingService slice).
- Every public type carries a one-line+ purpose Javadoc that reads cold; `package-info.java` ties the
  three-kinds-of-code story together. README is accurate to the build (TRY-IT steps reproduce the
  red builds I observed).

### 5. Prose↔code fidelity & originality — FIX (1 MAJOR)
- All 7 draft `<!-- include: -->` directives resolve to a real tag region; every displayed atom traces
  to the pin: `@SuppressFBWarnings(value, justification)`, `FindBugsFilter`/`<Match>`/`<Bug pattern>`,
  `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` (confirmed live in the engine this session — both fire on removal),
  `SuppressWarningsFilter`+`SuppressWarningsHolder`, `SuppressionFilter`, engine versions
  (checkstyle 10.26.1, spotbugs 4.9.3.0). No invented detail. The `NP_…` on draft L73 is an explicit
  generic *illustration* of the annotation shape in prose, while the displayed snippet correctly shows
  `EI_EXPOSE_REP` — not an invention.
- The prose triage tree lists four *findings* (fix/suppress/accept/baseline) mapping false-positive
  AND accepted both to **lever 1**; the code's 3-value `Disposition` folds those two lever-1 cases into
  `SUPPRESS`, and both the `Disposition.SUPPRESS` Javadoc and the `FindingTriage` comment state "tool
  wrong **(or cost accepted)**." Internally consistent and documented — NOTE only (N1).
- **Originality (LEGAL-IP §5):** scan for `@author`/`Generated`/`Copyright`/license-header/quickstart
  markers returned NONE. All code is book-original in `org.acme.findings`; tool identifiers are short
  documented API names, not copied source files. No attribution required.
- *MAJOR (M1):* the module README (L57-59), the `pom.xml` registration comment, and the EXAMPLE report
  all assert the module is "intentionally **not yet** listed in `08-companion-code/pom.xml`'s
  `<modules>`," but the parent pom **already registers** `39_managing_findings` (line 38). The
  registration is the correct *end-state per policy once CODE-REVIEW passes*; the defect is that the
  module's own docs now misstate reality. Reconcile — see findings table.

### 6. Neutrality in code — PASS
- Banned-phrase scan (better than / unlike X / the problem with / superior / beats / outperforms /
  killer / destroys / blows away / obvious choice over …) across `src`, `config`, `pom.xml`,
  `README.md`: **no hits.** No comment, identifier, log/error string, or test name crowns or
  disparages any tool. Every analyzer is cited to its own surface; no tool is crowned. The README's
  LineLength note even flags the column limit as "this team's cited choice," not a universal truth.

---

## CRITICAL — displayed `// tag::` region validation

Each region was extracted between its markers and checked for brace/paren/bracket balance, body line
count (≤9), and completeness (not a mid-statement fragment). Tag inventory across the module: **7
`tag::` and 7 `end::`, each appearing exactly once — no duplicate or orphan end-tag.**

| Tag | File | Body lines | `{}` | `()` | `[]` | Complete statement/element? |
|---|---|---|---|---|---|---|
| `triage-decision` | `FindingTriage.java` | 9 | 3/3 | 6/6 | 0/0 | yes (`public static … triage(...) {` … `}`) |
| `reviewed-suppression` | `PriceFormatter.java` | 7 | 1/1 | 2/2 | 1/1 | yes (comment + `@SuppressFBWarnings` + method + `}`) |
| `checkstyle-suppression-filter` | `config/checkstyle/checkstyle.xml` | 1 | — | — | — | yes (single self-closed `<module .../>`) |
| `baseline-match` | `config/spotbugs/spotbugs-exclude.xml` | 1 | — | — | — | yes (single self-closed `<Bug pattern=.../>`) |
| `checkstyle-baseline` | `config/checkstyle/checkstyle.xml` | 4 | — | — | — | yes (`<module>` … `</module>`, balanced) |
| `ratchet` | `FindingRatchet.java` | 6 | 1/1 | 8/8 | 0/0 | yes (`public List<Finding> newFindings(...) {` … `}`) |
| `gate-health` | `GateHealth.java` | 7 | 1/1 | 7/7 | 0/0 | yes (`public static GateHealth report(...) {` … `}`) |

- **No BLOCKER.** Every region is brace-balanced, ≤9 lines, and a complete unit.
- The apparent `<>` "imbalance" in `ratchet` (2/3) and `gate-health` (3/4) is **not** a real defect: it
  is Java generics/relational syntax (`List<Finding>` plus a stray `>` from `silenced > budget` / the
  `!baseline.contains(...)` comment / the ternary), not XML/HTML. The structural delimiters
  (`{}`/`()`/`[]`) are all balanced and both files compile green.

---

## Findings

Severity: BLOCKER (blocks gate) · MAJOR (fix before approval) · MINOR (should fix) · NOTE (FYI).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| M1 | Docs say module is "not yet listed in `<modules>`" but parent pom already registers it | MAJOR | `08-companion-code/39_managing_findings/README.md:57-59`; `08-companion-code/39_managing_findings/pom.xml:24-28` comment region; cross-ref `08-companion-code/pom.xml:38` | Reconcile the docs with reality: now that CODE-REVIEW passes, registration is correct — update the README note and pom comment to say it **is** registered (joined the reactor on green-build + CODE-REVIEW pass), or, if registration was premature, remove L38 until this report lands. Pick one; do not leave the contradiction. |
| M2 | `newFindings` returns `Collectors.toList()` (mutable, no API guarantee) amid an immutability-first module | MINOR | `FindingRatchet.java:49` | Return `Collectors.toUnmodifiableList()` for a defensively-immutable view, consistent with `toUnmodifiableSet`/`List.copyOf`/`Map.copyOf` elsewhere. (Snippet stays ≤9 lines.) |
| M3 | Public static factory `report` dereferences `ratchet`/`suppressions`/`current` without null-guards, unlike every other public entry point | MINOR | `GateHealth.java:49-55` | Add `Objects.requireNonNull(ratchet/ suppressions/ current, "…")` at the top of `report` so a null arg fails with a named message, not a bare NPE — matches the module's defensive style and is the version readers copy. (Guards can sit above the `// tag::` line to keep the displayed body ≤9 lines, or fold one in.) |
| N1 | Prose triage tree has 4 finding-outcomes; `Disposition` has 3 (accept folded into SUPPRESS) | NOTE | `Disposition.java`; draft §"triage tree" | No action required — both the enum Javadoc and `FindingTriage` comment document the fold ("tool wrong **or cost accepted**"), and the prose maps both to lever 1. Recorded for transparency. |
| N2 | No `mvnw` wrapper at the companion-tree root (EXAMPLES-GUIDE §4 expects a committed build wrapper) | NOTE | `08-companion-code/` (tree-wide) | Out of scope for this chapter — a reactor-wide concern; the module itself builds green via system `mvn`. Flagged for the tree owner, not this module. |

---

## Blockers

**None.** No security, neutrality, or invented-fact finding; all tag regions valid. FLOOR C is not
blocked. M1 is a MAJOR fidelity fix required before approval but does not block the floor.

---

## Build / lint result (re-run this session, offline)

- `mvn -B -o -Pquality -f pom.xml clean verify` → **BUILD SUCCESS**;
  `Tests run: 15, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`;
  `BugInstance size is 0` / `No errors/warnings found`.
- **Warning-clean:** clean compile under inherited `-Xlint:all,-processing` produced no compiler
  warnings.
- **Hardcoded-secret grep:** no hits in `src`/`config`/`pom.xml`.

### Load-bearing controls (independently re-verified)

| Control | Action | Observed |
|---|---|---|
| Site `@SuppressFBWarnings` (false positive) | removed the annotation, re-ran | `BugInstance size is 1` → `EI_EXPOSE_REP` @ `PriceFormatter.java:39` (`denominationsCents()`) → **BUILD FAILURE**; restored → green |
| Baseline `<Match>` (legacy) | removed the `<Match>` block, re-ran | `BugInstance size is 2` → `EI_EXPOSE_REP` @ `LegacyPriceTable.java:39` + `EI_EXPOSE_REP2` @ `:29` → **BUILD FAILURE**; restored (byte-identical) → green |

Both reasoned suppressions are genuinely **narrow** (one pattern on one method; one bug-pattern pair
scoped to one class) and **justified** (a `justification=` field travelling with the code; a
reviewed, version-controlled `<Match>` with a reason-comment). Neither is decorative; the chapter's
thesis is an executable event. Files restored to their original state after each test.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic Java 21 / security / simplicity / prose↔code fidelity /
  neutrality-in-code reviewed; verdict PASS-WITH-FIXES.
- [x] Build green + warning-clean re-confirmed; ≥1 test per public behaviour incl. the real failure path.
- [x] Hardcoded-secret grep clean (no hits).
- [x] Every displayed `// tag::` region brace-balanced, ≤9 lines, complete; no duplicate/orphan end-tag.
- [x] Both load-bearing controls independently verified (remove → red; restore → green).
- [x] Originality (LEGAL-IP §5) confirmed — no `@author`/Generated/license boilerplate; original-for-book.
- [x] Neutrality across comments/identifiers/strings — no banned phrasings, no tool crowned.
- [ ] Prose↔reality fully consistent — **one MAJOR doc contradiction (M1) to reconcile**, then re-review.

---

## Learnings & pipeline suggestions

- **The module-registration claim and the parent pom can silently diverge.** M1 is a class of drift the
  EXAMPLE gate's own "deferred until CODE-REVIEW" NOTE invites: the README/pom-comment freeze a
  "not-yet-registered" claim that becomes false the moment registration lands. Suggest a one-line
  reconcile-script check (or a code-review checklist row) that diffs each module README's
  registration claim against `08-companion-code/pom.xml`'s `<modules>` list, so the doc and the reactor
  state cannot disagree.
- **Angle-bracket counting is a false-positive source for Java tag-region validation.** A naive `<`/`>`
  balance check flags every generic/relational region. Recommend the snippet checker balance only
  `{}`/`()`/`[]` for `.java` regions and reserve `<>` balancing for `.xml`/`.html` — recorded so the
  next reviewer does not chase a phantom imbalance.
- **"Verify load-bearing by removal" is cheap, decisive, and repeatable.** Reproducing the two red
  builds (exact `BugInstance size` + finding line) took seconds and is the strongest possible evidence
  a suppression/baseline is real. Endorse the EXAMPLE-gate suggestion to make a "controls verified
  load-bearing (with red-build evidence)" row standard for any suppression/baseline module — and note
  the code-reviewer should re-run it independently, not trust the builder's report.
- **The "type refuses to represent the bad state" move is a reusable HONEST-LIMITATIONS-in-code
  pattern.** `Finding`'s rejection of an unjustified suppression satisfies the failure-path floor while
  teaching the chapter's thesis. Good candidate to promote as an example pattern for policy/config
  chapters (keys 76/80/87).

---
**ORCHESTRATOR FIX — 2026-06-27 — M1 RESOLVED.** The README claim that the module is
"intentionally **not yet** listed in `<modules>`" contradicted reality (it is registered
at pom.xml:38). Reconciled the README to state it is registered (added after green build +
CODE-REVIEW, per the join-when-green policy). Doc-only; rebuilt green. M2 (toList →
toUnmodifiableList) and M3 (null-guards on GateHealth.report) remain MINOR for the lift pass.
