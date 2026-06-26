# GATE REPORT — EXAMPLE-BUILD — Chapter 100 (`100_governing_ai_ai_review`)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 100 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 98)
- **Slug:** `100_governing_ai_ai_review`
- **Draft under review:** `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`
- **Module path:** `08-companion-code/100_governing_ai_ai_review/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (per-region), `check_snippets.sh` (draft)
- **Build state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** — FLOOR C (SOURCE-TRACE + COMPILE + buildable) satisfied.

---

## Verdict rationale

The chapter is governance/policy/config-centric but has concrete, buildable artifacts (an AI-usage policy
as externalized config + a runnable guardrail gate that gates AI-assisted changes), so it was BUILT rather
than marked N/A. The module builds green under `mvn -B -Pquality verify` (warning-clean: 0 Checkstyle, 0
SpotBugs) on Java 21.0.11, all 16 tests pass, both wired snippet markers resolve to ≤9-line tag regions,
and every fact traces to the dossier + `SOURCE-PIN.md` or is deliberately kept out of the code (no
statistic baked in; no tool crowned). Both FLOOR-C guard preconditions hold and are logged below.

---

## FLOOR C guard — both preconditions logged (required for any PASS)

- **(a) Runtime meets the minimum (Java 21+):** `openjdk version "21.0.11" 2026-04-21` — meets the
  SOURCE-PIN anchor (JDK 21 LTS). Maven 3.9.16.
- **(b) Build GREEN:** `mvn -B -Pquality -f pom.xml clean verify` → **BUILD SUCCESS**.
  - `Tests run: 16, Failures: 0, Errors: 0, Skipped: 0`
  - `You have 0 Checkstyle violations.` (engine 10.26.1 via plugin 3.6.0)
  - `BugInstance size is 0` (SpotBugs plugin 4.9.3.0, effort=Max, threshold=Medium)

Both true → Floor-C verdict is eligible to be PASS (and is).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | SpotBugs `EI_EXPOSE_REP`: record accessor leaked a mutable `EnumSet` | MAJOR (fixed) | `AiGovernancePolicy.java` compact constructor | Wrapped the `EnumSet` in `Collections.unmodifiableSet` so the generated accessor returns an immutable view. Re-built green. |
| 2 | Banned-phrasing pattern `unlike X` in a Javadoc cross-reference | MINOR (fixed) | `GateDecision.java` class Javadoc | Reworded to "where the quality gate has a middle 'warn' outcome" — no comparative crowning. |
| 3 | Two tag regions initially exceeded the 9-line cap (16 and 12 lines) | MAJOR (fixed) | `AiUsageGate.java#only-policy-can-ship-it`, `AiReviewOutcome.java#ai-review-outcomes` | Narrowed each tag to the 9-line load-bearing core; both now resolve at exactly 9 lines. |
| 4 | Unpinned statistics in the chapter (productivity 78%/72%/risk 65%; AI-review 35%/50-60%/"half"); sources (Sonatype, O'Reilly, NIST SATE, arXiv 2508.18771 / 2509.20388) are not `SOURCE-PIN` rows | NOTE (flagged) | dossier `⚠ verify-at-pin`; SOURCE-PIN §7 canon gaps | Kept ALL statistics OUT of the module code/config (the code reasons over caller-supplied numbers); flag stands for the prose SOURCE-VERIFY pass — see Flags below. |

---

## Blockers

**None.** Findings 1-3 were fixed during the build; the final clean `-Pquality` build is green. Finding 4
is a prose-side pin gap that does not touch the module (no statistic is baked into the code).

---

## Enterprise-grade checklist

- **Child of the ONE aggregator, no version literal:** YES — `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no `<groupId>`/`<version>`/BOM of its own. JDK-only runtime (test libs inherited from the aggregator `dependencyManagement`).
- **Registered in `<modules>` LAST (not yet):** Correct — the module is **NOT** added to `08-companion-code/pom.xml` (per the task constraint and the green-build-+-CODE-REVIEW-first rule). Built standalone from the module dir. Registration is the orchestrator's job after the CODE-REVIEW gate.
- **Pinned dependency set via inherited parent:** YES — JUnit 6.0.3 BOM + AssertJ 3.27.7 from the aggregator; analyzer engines pinned in the `quality` profile (Checkstyle 10.26.1, SpotBugs 4.9.3.0), all present in the local `~/.m2` cache (offline build).
- **Externalized config profiles:** YES — `src/main/resources/aigov-dev.properties` / `aigov-prod.properties`, selected by `-Daigov.profile` (`AiGovernancePolicy.load`); dev is permissive, prod requires the full AI-specific check set. Test `devProfileIsLooser` proves they differ.
- **At least one integration test exercising the mechanism:** YES — `AiUsageGateTest` (16 tests) drives the gate through the externalized policy + a `ChangeContext`, asserting every governance precondition, the AI-review three-outcome taxonomy, and the counter-metric.
- **Test-harness setup:** JUnit Platform via surefire 3.5.6 (inherited); no system properties needed for the default profile (gate defaults to `dev`); the prod-profile test loads `prod` explicitly. No spurious logging — JDK-only, no logging framework on the path.
- **Observability / health surface:** YES — `AiUsageGate.blockedCount()` (gate block-rate metric) + `AiUsageGate.isReady()` (readiness probe over the wired policy) + `AiAdoptionCounterMetric.verdict()` (productivity-with-risk counter-metric).
- **Explicit failure path:** YES — `GateDecision.Block` is the defined not-permitted verdict (named reason, not a thrown control-flow signal for an ordinary outcome); fail-fast guards reject null change/components and out-of-range measurements. Both halves tested (`FailFastGuards`, `rejectsImpossibleMeasurement`).

---

## Snippet wiring (tag regions — one artifact with the prose)

Markers wired into the draft, both verified by `check_snippets.sh` (2 markers; 2 pass, 0 fail):

| Tag | File | Resolved lines (cap 9) | Draft location |
|---|---|---|---|
| `only-policy-can-ship-it` | `AiUsageGate.java` | **9** | §"Governing AI: sanction, verify, keep the human gate" (after the no-auto-merge line) |
| `ai-review-outcomes` | `AiReviewOutcome.java` | **9** | §"AI code review: a bounded augmentation" |

Additional taggable regions present in the module (not displayed in v1, available to the drafter):
`GateDecision.java#gate-decision` (8), `docs/AI_IN_THE_WORKFLOW_POLICY.md#ai-policy` (9),
`ci/ai-governance-gate.yml#ai-gate-step` (6). A "Snippet tags:" line was added to the draft header.

---

## Source traceability (every fact → its source)

- "AI can write code, but only policy can ship it" — Sonatype, attributed in the chapter and the code comments as the thesis (⚠ source not yet a SOURCE-PIN row; prose-side, flagged — not asserted as a bare fact in code).
- Governance shape (sanctioned tools as a security decision; same gates + AI-specific checks; accountable human / "it's your PR"; disclosure/provenance; no auto-merge on AI approval; policy+culture/shadow AI) — dossier key 100 (banked, verified-against-SOURCE-PIN 2026-06-20).
- AI-specific checks routed to their own chapters: SAST → Ch 30 (key 70); SCA → Ch 28 (key 65); secrets → Ch 31 (key 71); mutation-verify AI tests → Ch 23 (key 47); vulnerability inheritance → Ch 41 (key 97). All from the dossier routing block.
- AI review: augment-never-replace; the three outcomes (real catch / false positive / missed bug); intent ceiling (inference ≠ verification); AI-reviewing-AI compounds blind spots → independence — dossier key 98 (§B).
- Counter-metric productivity with risk; DORA change-failure rate → Ch 38 (key 85) — dossier; the SPECIFIC figures are NOT baked into code (see Flags).
- Runtime + tool pins: JDK 21 anchor; Checkstyle 10.26.1; SpotBugs 4.9.3.0; JUnit 6.0.3; AssertJ 3.27.7 — `SOURCE-PIN.md` §1/§2/§3.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file. Every file is original work written for this book under the original
`org.acme.aigovernance` package (9 source files) and original `docs/`, `ci/`, `config/` artifacts. Nothing
is a copied or lightly-edited upstream sample, quickstart skeleton, or `NOTICE`/header boilerplate. The
module follows the *shape* of the peer modules (keys 75 and 84) — a house pattern original to this book —
not any external source. No region is taken substantially verbatim from any source file, so no attribution
line is required. The chapter's statistics are dated/attributed snapshots in the PROSE only.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan (fixed at draft time) is ONE designed conceptual
diagram, `fig100_1` (already authored as HTML→PNG with its `fig100_1.sources.md` sidecar — authored
separately, not the example-builder's job). The module is JDK-only with no subject-native live UI surface
(no dev console, API explorer, or live health endpoint — the health surfaces are plain Java per the
JDK-only house ADR), so there is nothing to capture live. Consistent with the GUIDELINES image budget for
a chapter of this class. No new figure was invented.

---

## Floor-C verdict

**PASS.** Both guard preconditions logged (Java 21.0.11 ≥ 21; `-Pquality verify` GREEN, warning-clean).
Zero invented atoms in the module; every fact traces to the dossier or `SOURCE-PIN.md`, and the unpinned
statistics are kept out of the code and flagged for the prose pass. Snippet markers resolve ≤9 lines.
LEGAL-IP §5 original-for-this-book confirmed.

---

## Flags raised to `09-flags/`

- **`100`-stats-verify-at-pin (prose-side, NOT a build blocker):** the chapter's productivity/risk figures
  (~78% / ~72% / ~65%) and AI-review figures (~35% critical / 50-60% NIST SATE / O'Reilly "half your
  bugs"), and the source attributions (Sonatype "only policy can ship it"; arXiv 2508.18771 AI-review;
  arXiv 2509.20388 privacy scorecard; NIST SATE; O'Reilly) are `⚠ verify-at-pin` and are SOURCE-PIN §7
  canon GAPS (not pinned rows). The module deliberately bakes in NONE of them (the code reasons over
  caller-supplied numbers; the sanctioned-tool list is generic placeholders). Action: the SOURCE-VERIFY
  pass on the prose must either pin these sources (add §7 rows) or keep each figure dated+attributed and
  marked `⚠ UNVERIFIED`. This is the existing standing flag for keys 100/98; this report does not add a new
  module-level invention (there is none).

---

## Learnings & pipeline suggestions

- **A governance/policy chapter IS buildable when it has a decision to encode.** The "AI-usage gate" maps
  cleanly onto the peer-75 `QualityGate`/`GatePolicy`/`GateDecision` shape (externalized profile + a
  permit/block verdict) and the peer-84 policy-as-config + health-surface + ADR shape. The reusable pattern
  for any policy chapter: encode the policy's load-bearing decision as a unit-tested gate, externalize its
  thresholds as `%dev`/`%prod` properties, and carry the human-facing policy as docs-as-code with tag
  regions. Recommend noting this in EXAMPLES-GUIDE as the "policy chapter" recipe.
- **The standing AI-statistics rule has a clean code-side discipline:** keep every dated/vendor figure OUT
  of the code entirely and have the code reason over caller-supplied numbers. This makes the
  never-invent + dated-attributed rule structural rather than a comment a reviewer must police, and it kept
  this module's Floor-C source-trace trivially clean. Recommend promoting "policy/metric code bakes in no
  statistic" to a LEGAL-IP / SOURCE-PIN note for AI-era chapters.
- **SpotBugs `EI_EXPOSE_REP` on records with `EnumSet` components** is a recurring trap: `Set.copyOf`
  returns an immutable set, but `EnumSet.copyOf` does not — wrap it in `Collections.unmodifiableSet`. Worth
  a one-line note in the module-authoring checklist (the chapter dogfooded this by catching it on itself).
