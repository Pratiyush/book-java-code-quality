# GATE REPORT — CODE-REVIEW — Chapter 100 (Only Policy Can Ship It)

## Header

- **Gate:** CODE-REVIEW (FLOOR C, second half — the senior-PR judgment a passing build cannot make)
- **Chapter key:** 100 (folds 98)
- **Slug:** `100_governing_ai_ai_review`
- **Module:** `08-companion-code/100_governing_ai_ai_review/`
- **Draft under review:** `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` (agent)
- **Scripts / commands run:** `./mvnw -B -Pquality verify` (JAVA_HOME=openjdk@21.0.11); banned-word grep; hardcoded-secret grep; baked-stat grep; tag-region line-count + brace-balance check; tag↔include cross-check.
- **Verdict:** **PASS**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21 that a reader can paste with confidence. It builds green
(`BUILD SUCCESS`, 16 tests, 0 Checkstyle, SpotBugs "No errors/warnings found") and is warning-clean. The
two displayed tag regions are each **exactly 9 lines, brace-balanced** (three complete `if` blocks apiece —
no mid-statement cut), and free of banned NEUTRALITY words. The subject-specific landmine — a timeless AI
productivity/risk statistic baked into the code as fact — is **not present**: every figure (78/72/65/35/50-60)
is kept out of the code by deliberate design (ADR-0001), and the counter-metric reasons over caller-supplied
numbers only. Test fixtures use synthetic measurements (60.0, 0.12) that are not the chapter's cited figures.
No security, neutrality, invention, or correctness finding blocks FLOOR C. The handful of findings below are
MINOR/NOTE polish, none required before approval.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21+ & code quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity (+ originality/attribution) | **PASS** |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `AiUsageGate.evaluate` applies preconditions in order and the FIRST unmet one blocks (sanctioned-tool →
  required-checks → disclosure → accountable-human → no-auto-merge), exactly as the Javadoc and prose state.
  Non-AI changes short-circuit to `Permit` before any AI-specific control — correct and tested.
- `AiReviewOutcome.classify` covers all four boolean combinations: (T,T)=REAL_CATCH, (T,F)=FALSE_POSITIVE,
  (F,T)=MISSED_BUG, (F,F)=the trailing "no comment, no defect" FALSE_POSITIVE. No fall-through gap.
- `AiAdoptionCounterMetric.verdict` returns LOW_ADOPTION < 10%, else RISK_RISING when failure rate exceeds
  baseline, else HEALTHY. Range validation in the compact constructor makes an impossible measurement
  unrepresentable. Velocity-alone can never earn HEALTHY — the chapter's whole point, enforced in code.
- Resource safety: the only I/O (`AiGovernancePolicy.load`) uses try-with-resources on the `InputStream`;
  `IOException` is wrapped in `UncheckedIOException` (not swallowed); a missing profile throws
  `IllegalArgumentException`. No leak, no swallowed exception.
- Tests are non-vacuous: each asserts the decision *type* AND the actionable reason substring (e.g. "shadow
  AI", "not a defense", "keep the human gate", "MUTATION_VERIFIED_TESTS"), and `blockedCount()` transitions.
- **Failure path exercised for real:** `blocksUnsanctionedTool`, `blocksWhenRequiredCheckMissing`,
  `blocksUndisclosedAiUse`, `blocksWhenNoAccountableHuman`, `blocksAutoMergeOnAiReview` each drive a genuine
  block branch; `FailFastGuards` covers null change + null `ChangeContext` component;
  `unknownProfileIsRejected` covers the load failure path; `rejectsImpossibleMeasurement` covers range
  validation. Both halves of the failure path (named `Block` verdict AND thrown exceptions) are tested.

### 2. Idiomatic Java 21+ & code quality — PASS
- Java 21 idioms used where they read better: `sealed interface GateDecision permits ...` with `record`
  permittees (the binary verdict), `record` value types (`AiGovernancePolicy`, `ChangeContext`,
  `AiAdoptionCounterMetric`) with compact constructors doing validation + defensive copy, `EnumSet` for the
  check set, `Set.copyOf` / `Collections.unmodifiableSet` for immutability (Effective Java Item 17, cited).
- No anti-patterns: no raw threads, no blocking, no `System.out` (a logger is not even needed — pure logic);
  metrics use `AtomicLong`. Lifecycle/scope is N/A (JDK-only, no framework container) and the module says so.
- Representation exposure is handled structurally (defensive copies behind unmodifiable views), which the
  module's own SpotBugs gate would otherwise flag — a nice piece of dogfooding, and SpotBugs is in fact clean.

### 3. Security — PASS
- Hardcoded-secret grep (password/secret/token/apikey/key literals + AKIA/ghp_/sk-/BEGIN patterns): **zero
  hits** across `.java/.properties/.xml/.yml`. The `sanctioned-tools` values are generic placeholders
  (`vetted-assistant-a/-b`), explicitly not credentials.
- Input validation present on every constructor boundary (null checks, range checks, profile existence).
  `AiCheck.valueOf` in `parseChecks` will throw `IllegalArgumentException` on a malformed config token — a
  fail-fast on bad operator input, appropriate here (config is trusted-but-validated; see NOTE 3).
- No injection sink, no reflection on user input, no error response leaking internals (the `Block` reason is
  an intentional, controlled, actionable string — not a stack trace).

### 4. Simplicity & readability — PASS
- Smallest code that teaches the point: 7 source types, each one concept. No dead code, no unused deps
  (JDK-only runtime; JUnit/AssertJ test-scope only, versions inherited from the aggregator — no literals).
- Names are realistic and intent-revealing (`AiUsageGate`, `ChangeContext`, `MISSED_BUG`, `isIntentCeiling`,
  `forbidAutoMergeOnAiReview`). No `Foo`/`Bar`/`tmp`. Package `org.acme.aigovernance` is the book's house
  example package, consistent with peer modules.
- Every public type carries a one-line (here, fuller) purpose Javadoc; readers meeting this cold get the why.

### 5. Prose↔code fidelity (+ originality/attribution) — PASS
- `<!-- include: ...#only-policy-can-ship-it -->` (draft L56) and `...#ai-review-outcomes` (draft L74) both
  resolve to real tag regions in the compiled files. Each region is **exactly 9 lines** (verified by marker
  line-count) and **brace-balanced** (three full `if{...}` blocks; no statement cut mid-expression).
- Prose L54 claims the displayed gate region blocks on "disclosure, an accountable human, and the
  no-auto-merge line" — the `only-policy-can-ship-it` region shows precisely those three controls, in order.
- Prose L72 claims "exactly three buckets ... a real catch, a false positive, and ... a missed bug" — the
  `ai-review-outcomes` region shows REAL_CATCH → FALSE_POSITIVE → MISSED_BUG, in order. Faithful.
- **Timeless-stat check (CRITICAL) — CLEAN.** No AI productivity/risk figure (78%/72%/65%/35%/50-60%/"22,000"/
  "16 tools") appears as a constant in any `.java`, `.properties`, `.xml`, or `.yml`, nor in `docs/*.md`. The
  code comments that mention "productivity"/"risk" do so qualitatively ("large majorities", "a substantial
  fraction") and explicitly state every figure is a dated/attributed/vendor-sourced snapshot kept OUT of code
  (`AiAdoptionCounterMetric` Javadoc; `package-info`; ADR-0001 decision #2). Test fixture numbers (60.0,
  0.12, 0.20, 140.0, 1.4) are synthetic measurements to drive branches — not the chapter's cited statistics.
- The two *taggable-but-not-displayed* regions (`gate-decision`, `ai-policy`, `ai-gate-step`) are also ≤9
  lines and balanced, so a later draft can surface them without rework.
- **Attribution:** "only policy can ship it" is consistently attributed to Sonatype in code comments and
  docs (not asserted as the book's own coinage). The SaaS GitHub Actions in `ci/` are dated-at-use (2026-06)
  per SOURCE-PIN §5, not presented as timeless.
- **Originality (LEGAL-IP §5):** the domain (an AI-usage governance gate + outcome taxonomy + counter-metric)
  is original-for-this-book, not a reskinned upstream quickstart. No unattributed verbatim lift observed.

### 6. Neutrality in code — PASS
- Banned-phrasing grep (`better than`, `unlike X`, `superior`, `inferior`, `beats`, `the problem with`) +
  crowning-verb grep (`best`, `leading`, `winner`, `world-class`, `state of the art`, `gold standard`,
  `recommend a tool`): **zero hits** anywhere in the deliverable tree (code, comments, config, docs).
- No AI tool/assistant/reviewer is named or crowned; `sanctioned-tools` is generic placeholders, and
  ADR-0001 records the deliberate "crown none" decision. The neutrality posture is itself dogfooded.

---

## Findings

Severity scale: BLOCKER / MAJOR / MINOR / NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Javadoc typo: "a vocabulary tests and a dashboard can use" — stray "tests" (should be "a vocabulary tests and dashboards can use" → "a vocabulary that tests and a dashboard can use"). | MINOR | `AiReviewOutcome.java:14` | Reword to "a vocabulary a test and a dashboard can use" (or similar). Cosmetic; not in a displayed region. |
| 2 | The compliant-change happy path is asserted only as `Permit`; the *permit reason* string ("policy satisfied — a human still reviews intent") is never asserted, unlike every block path which asserts its reason. | MINOR | `AiUsageGateTest.java:44-49` | Optionally add `assertThat(((GateDecision.Permit) decision).reason()).contains("human still reviews intent")` to pin the permit message too. |
| 3 | `AiGovernancePolicy.parseChecks` lets a malformed config token throw `IllegalArgumentException` from `AiCheck.valueOf` with the raw enum message, which is fine for trusted config but is not unit-tested and the message is less actionable than the rest of the module's named reasons. | NOTE | `AiGovernancePolicy.java:116-124` | Acceptable as-is (config is owner-controlled). If desired, wrap with a message naming the offending token + the legal set, and add a malformed-profile test. |
| 4 | `AiUsageGate.evaluate` order means a non-AI change is permitted before disclosure/human checks — correct and documented, but a reader skimming only the displayed 9-line region (which starts at disclosure) does not see the two earlier gates (tool, checks). | NOTE | `AiUsageGate.java:57-66` (above the tag) | None required — prose L54 scopes its claim to the displayed controls. Mentioned only so the tag boundary is understood. |
| 5 | README/prose cite "16 tests"; build confirms 16. No drift. | NOTE | `README.md:53`, draft L5/L132 | None — recorded as a positive verification. |

---

## Blockers

**None.** No BLOCKER-severity finding. No banned NEUTRALITY word, no hardcoded secret, no baked-in timeless
statistic, no broken/mid-statement displayed snippet, no invented fact. FLOOR C is not blocked.

---

## Build / lint result

```
JAVA_HOME = /opt/homebrew/opt/openjdk@21/.../Home   (openjdk 21.0.11 — SOURCE-PIN anchor LTS)
command   = ./mvnw -B -Pquality -f pom.xml verify
result    = BUILD SUCCESS  (exit 0)
tests     = Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
checkstyle= You have 0 Checkstyle violations.   (config/checkstyle/checkstyle.xml, engine 10.26.1)
spotbugs  = No errors/warnings found            (effort=Max, threshold=Medium)
warnings  = warning-clean (the only "WARNING" token in the log is inside the string "No errors/warnings found")
```

Displayed tag regions — line count & balance:
- `AiUsageGate.java#only-policy-can-ship-it` → 9 lines, brace-balanced (3 complete `if{...}` blocks).
- `AiReviewOutcome.java#ai-review-outcomes` → 9 lines, brace-balanced (3 complete `if{...}` blocks).

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity / prose-code-fidelity /
  neutrality-in-code all PASS.
- [x] Build green via `./mvnw -B -Pquality verify`; warning-clean.
- [x] At least one integration test per public behavior **including the failure path** (block branches +
  thrown-exception guards both covered).
- [x] Hardcoded-secret grep: zero hits.
- [x] Banned-NEUTRALITY-word grep across the whole deliverable tree: zero hits.
- [x] Timeless-AI-statistic check: zero baked-in figures in code/config/comments/docs (caller-supplied only).
- [x] Every displayed `// tag::` region ≤9 lines, brace-balanced, traces to the prose claim.

---

## Learnings & pipeline suggestions

- This module is a model for the "governance/policy/config-centric" chapter shape: when the topic is a
  *decision*, making the decision a small sealed-verdict + record + externalized-properties gate keeps the
  displayed snippet honest and the failure path real. Worth citing in EXAMPLES-GUIDE as a pattern.
- The ADR (`docs/adr/0001-...`) that pre-commits "crown no tool, keep statistics out of code" is an effective
  anti-drift device for AI-subject modules — it makes the two highest-risk findings (endorsement + timeless
  stat) structurally hard to re-introduce. Suggest recommending an ADR for any future AI-era module.
- Suggest adding a cheap CI/lint assertion that every displayed tag region is brace-balanced AND ≤9 lines, so
  the snippet-ceiling + balance check this gate did by hand becomes mechanical (extend `check_snippets.sh`).
- MINOR typo at `AiReviewOutcome.java:14` is the only code-text nit; batch it with finding #2 if the
  example-builder does a polish pass, but neither blocks approval.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 100 gate-run PASS
```
