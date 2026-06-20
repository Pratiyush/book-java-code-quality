# INDEPENDENT SCORECARD — Ch 47 — model: Claude Sonnet 4.6 — 2026-06-20 (lift pass 1)

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 110 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `110_maturity_model_roadmap`
- **Title:** Where to Start, and How to Keep Going
- **Part / arc position:** Part XIV — Capstone & Synthesis (Chapter 47, final chapter)
- **Artifact scored:** `03-drafts/110_maturity_model_roadmap/110_maturity_model_roadmap_v1.md`
- **Verified against:** `02-research/110_maturity_model_roadmap/110_maturity_model_roadmap_RESEARCH.md` + `00-strategy/SCORING.md`
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — independent track, not the authoring model)
- **Date:** 2026-06-20
- **Lift-pass #:** 1 (voice pass by Opus drafter)

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | Verdict | Evidence / offending text + fix |
|---|---|---|---|
| **A — NEUTRALITY** | No winner crowned; no banned phrasings ("better than", "superior", "beats", "unlike X", "the problem with X"); comparative claims cited. | **PASS** | Synthesis chapter. No tool is crowned superior. The "staged roadmap vs rigid maturity ladder" Alternatives section presents both with trade-offs, no verdict. DORA's move from maturity levels to capabilities is stated as a framing fact (key 85), not a superiority claim. No banned phrase found in the draft. All tool references delegate to prior chapters by key. |
| **B — HONEST-LIMITATIONS** | Every feature/claim has its hardest objections and an explicit when-NOT-to-use. | **PASS** | The "Limitations & when NOT to reach for it" section carries six substantive bullet points: maturity-level-as-vanity-metric (Goodhart trap), roadmap-is-a-default-not-your-plan, tools-without-culture-fail, more-maturity-not-more-value-past-a-point, everything-is-dated, no-done. Each roadmap stage's pain-first override is embedded in the CONCEPT callout. "Never:" bullet in "When to use what" is explicit. No claim presented as cost-free. |
| **C — SOURCE-TRACE** | Zero invented rule IDs, config keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, or quoted claims. Everything traces to the dossier/pin. | **PASS** | Pure synthesis chapter: no tool flags, GAV coordinates, version numbers, or benchmark figures. All 45 tool/practice references map to prior chapter keys consistent with the dossier. The one new primary atom — DORA's "capabilities over maturity levels" framing — is attributed to key 85 and dora.dev, consistent with the dossier's "☑ frame" verification. The back matter retains a "TO-PIN" marker (SOURCE-PIN §7 canon: "DORA capabilities/continuous-improvement TO-PIN"), indicating full pin-level verification is still deferred. This is a pending deep-verification item, not an invented detail; the attribution is correct. C-SOURCE-TRACE: PASS with one open TO-PIN item that must close before human-approval gate. |
| **C — COMPILE** | Companion module builds green via `./mvnw -B verify`. | **PENDING** | No per-chapter buildable module exists or is required. Draft comment and dossier both mark EXAMPLE-BUILD = N/A (a roadmap artifact, not a buildable module). Tracked separately on the figure pipeline track; does not fail the chapter. |
| **C — CODE-REVIEW** | Module passes the CODE-REVIEW gate. | **N/A** | No buildable module; EXAMPLE-BUILD = N/A. |

---

## The five clusters (score 1–10)

| # | Cluster | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | Five-stage roadmap mechanism is explained in clear, ordered sequence with CONCEPT callouts carrying the major ideas. Why-before-how structure holds (vanity-Level-4 hook precedes the mechanism). "Context-driven not linear" concept is explicitly unpacked. The dossier specifies Fig 110.1 (stage × practice × chapter visual); no figure appears in the draft and none is referenced as in-progress. CONCEPT callouts serve as a text substitute, which holds clarity at this score but leaves a visual rhythm gap per GUIDELINES §8. No clarity-breaking jumps; no undefined terms. Score unchanged from pass 0: the pass introduced no figure and no structural change to clarity. |
| 2 | **ACCURACY** | 9 | Synthesis chapter with no invented atoms. Every tool/practice maps to a dossier key; back-matter section lists them explicitly. No version numbers, GAV coordinates, tool flags, or benchmark figures to invent. DORA capabilities-over-levels attributed to key 85 and dora.dev, consistent with dossier's "☑ frame" verification. One open TO-PIN item in back matter remains unresolved. Scored 9 (not 10) for this deferred verification item — not an invented detail, but not yet fully pinned. Score unchanged from pass 0: the TO-PIN was not closed in this pass. |
| 3 | **UTILITY** | 8 | Highly actionable. "When to use what" section gives concrete entry points for different team contexts. Pain-first guidance includes specific stage-jump examples. Stage-by-stage chapter cross-references make the whole book navigable. Companion artifact spec (one-page roadmap) is well-defined for reader adaptation. EXAMPLE-BUILD = N/A, correctly handled. No runnable example required. Score unchanged from pass 0. |
| 4 | **DEPTH** | 7 | Deliberately a synthesis chapter. Depth is the quality of synthesis, not new primary material. The Goodhart/DORA framing adds a coherent explanatory layer. The sociotechnical conclusion ties the book's through-line together. "Alternatives & adjacent approaches" section is appropriately light for a closing chapter. "Deep dive" sustains the synthesis well. Score unchanged from pass 0: the voice pass made no depth-related changes. |
| 5 | **READABILITY** | 7 | The voice pass removed the most egregious narration-level second-person violations identified in pass 0 — the "You've reached the end. You have the case for quality..." hook passage and the explicit "You start where your pain is... you remember... you treat..." CONCEPT callout — and these are confirmed absent in the current draft. This is a real improvement. However, multiple narration-level "you/your" instances remain, confirmed by full grep: Overview line — "**If you hold one idea**, hold this..." (direct narration-level "you"); CONCEPT callout — "it does not tell you to ignore your own most expensive problem" and "if a stage doesn't improve an outcome you care about" (narration "you"); Deep dive — "Goodhart's law turned on the very chapter meant to help you," "keep your eyes on the outcome," and "'how many practices have we adopted'" (narration "your/you" plus first-person "we"); Alternatives section — "a staged default you reorder by your own pain"; section heading "Closing: where the book ends and your work begins" (heading-level "your"); Closing paragraph — "trade-offs you weigh in your context," "a practice you keep," "Your work begins"; final "The book is complete" section — "Start where your pain is. Measure." (imperative is permitted but "your pain" keeps it at narration-person boundary). The em-dash count is 46 in approximately 4,315 total lines of text; removing the HTML comment preamble (~11 lines of dense metadata), the prose body is roughly 3,000 words, giving approximately 15 em-dashes per 1,000 words against a target of ~8/1,000. The AUDIT gate treats this as a soft flag. The pass trimmed the most problematic violations; the residual set is smaller but real. A score of 7 is held — not 8, because the voice rule is explicit and multiple instances remain; not 6, because the improvement is genuine and material (the hook and the CONCEPT callout, the two highest-weight passages, are now clean). |

**Cluster subtotal:** 8 + 9 + 8 + 7 + 7 = **39 / 50**

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — not yet; clears the numeric bar (39/50 >= 35/50; no cluster below 6; all THREE floors PASS) but the 90% threshold set for this independent scoring context (>=45/50) is not met, and the remaining voice violations and the missing figure prevent a clean APPROVE recommendation.
- [x] **LIFT** — the chapter clears the standard ship bar (35/50, no cluster below 6, all floors PASS); it does not clear the tighter 90% / 45-point APPROVE threshold. Remaining blockers (see below) require one additional targeted pass.
- [ ] **CUT**

**One-line rationale:** All floors PASS and aggregate 39/50 clears the standard ship bar with no cluster below 7; the voice pass removed the most egregious narration-level second-person violations but left a substantial residual set, and READABILITY holds at 7 rather than rising to 8; CLARITY is capped by the absent Fig 110.1; the DORA TO-PIN item remains open — three specific blockers prevent APPROVE at 90%.

---

## Remaining blockers to 90% (what must move to reach >=45/50)

The chapter currently scores 39/50 (78%). Reaching 45/50 (90%) requires +6 points across the five clusters, all currently capped by three specific issues:

| # | Blocker | Cluster(s) affected | Prose-fixable now? | Requires pipeline tooling? |
|---|---|---|---|---|
| 1 | Residual narration-level "you/your" violations: Overview "If you hold one idea, hold this"; CONCEPT callout "it does not tell you to ignore... you care about"; Deep dive "meant to help you," "your eyes," "we adopted"; Alternatives "you reorder by your own pain"; section heading "your work begins"; Closing "you weigh in your context," "a practice you keep," "Your work begins" | READABILITY (7 → 8) | YES — prose-fixable in one targeted rewrite pass; convert to imperative ("start where the pain is") or third person ("the team reorders by its own pain") throughout | No |
| 2 | Missing Fig 110.1 — dossier specifies the stage × practice × chapter roadmap visual; the draft references no figure and the CONCEPT callouts are the only visual substitute | CLARITY (8 → 9) | No (the CONCEPT text is clear; prose cannot replace a designed figure) | YES — requires the `/figure 110` pipeline step (HTML → render → PNG); not available until figure-render toolchain is unblocked |
| 3 | DORA "capabilities over maturity levels" TO-PIN item open in back matter; full pin-level verification not yet confirmed against dora.dev at key 85 | ACCURACY (9 → 10) | Partially — the source-verifier must confirm the framing against the pinned dora.dev source and update the dossier _VERIFY.md; this is a verification action, not a prose rewrite | No (no toolchain dependency; requires a source-verify pass against the pinned dora.dev authority) |

**Net-of-blockers projection:** Closing blockers 1 and 3 (both prose/verification fixable now) would move READABILITY to 8 and ACCURACY to 10, yielding 41/50 (82%). Closing blocker 2 (figure pipeline) would move CLARITY to 9, yielding 42/50 (84%). All three together yield 42/50 — still short of 45/50. The gap to 45/50 requires additional depth or utility refinement beyond these three blockers; however, at the standard ship bar (35/50) the chapter already ships. The 45/50 threshold is an independent-scorer APPROVE threshold set for this context, not the standard SCORING.md ship bar. The chapter is LIFT-eligible at the standard bar; the remaining blockers identify what is needed to reach the tighter 90% APPROVE threshold.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 39 / 50 | PASS | PASS | PASS / PENDING / N/A | SHIP (standard bar) | initial score (independent scorer: Claude Sonnet 4.6) |
| 1 | 2026-06-20 | 39 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT (tighter 90% threshold not met) | Voice pass: removed "You've reached the end / You have the case for quality" hook passage; removed "You start where your pain is / you remember / you treat" from CONCEPT callout. Residual narration-level "you/your" persists in ~9 additional locations. Em-dash density unchanged. No figure added. TO-PIN not closed. |

---

## Learnings & pipeline suggestions

1. **Partial voice passes leave a residual tail.** When a voice-fix pass targets the most obvious violations, subtler instances remain — possessives in section headings ("your work begins"), embedded narration-you in CONCEPT callouts ("a stage doesn't improve an outcome you care about"), and closing-paragraph possessives. The AUDIT gate grep should target not only "You" + "you" at sentence-start but also possessive "your" in prose lines and the first-person "we" which appeared once ("how many practices have we adopted"). Pipeline suggestion: the voice-scan grep list should include "your" in prose context (not just inside quoted text) and "we've / we adopted / we can" in narration.

2. **Section headings are voice surfaces too.** "Closing: where the book ends and your work begins" uses "your" in a heading, which the current heading-level AUDIT scan (aimed at neutrality/superlatives) does not catch. Pipeline suggestion: extend the heading scan to catch narration-person "your/you" in heading text, not only comparative superlatives.

3. **The 90% / 45-point threshold reveals a ceiling effect.** A synthesis chapter (no new primary material, no buildable module, one pending figure) has a natural ceiling in DEPTH (capped at 7–8 for a synthesis) and CLARITY (capped by missing figure). This means reaching 90% requires excellence in READABILITY, ACCURACY, and UTILITY — the prose-controllable clusters. The figure pipeline is the single highest-leverage unblocked item for CLARITY.

4. **Incremental voice improvement is real but insufficient at one pass.** The pass removed the two most prominent violations (hook and CONCEPT callout) but not the nine smaller ones. A targeted second voice pass with an explicit grep-and-recast checklist (covering possessives and embedded "you" in prose) would close READABILITY to 8 in one focused pass.

→ Also append to `00-strategy/PIPELINE-LEARNINGS.md`.
