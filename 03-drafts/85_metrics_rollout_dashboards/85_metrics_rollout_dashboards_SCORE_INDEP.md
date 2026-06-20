# INDEPENDENT SCORECARD — Ch 38 — model: Claude Sonnet 4.6 — 2026-06-20

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 85 (folds 87, 88)
- **Slug:** `85_metrics_rollout_dashboards`
- **Title:** Knowing Whether It Works
- **Part / arc position:** Part X — Process, People & Metrics (closer)
- **Artifact scored:** `03-drafts/85_metrics_rollout_dashboards/85_metrics_rollout_dashboards_v1.md`
- **Verified against:** `02-research/85_metrics_dora_space/85_metrics_dora_space_RESEARCH.md` (the ONLY allowed source basis); SOURCE-PIN 2026-06-20
- **Scorer:** chapter-scorer (Claude Sonnet 4.6 — independent gate, different model from author)
- **Date:** 2026-06-20
- **Lift-pass #:** 0 (initial scoring)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | The LOC-leaderboard hook (Goodhart made flesh) frames the problem immediately and the three-part structure (which-metrics / how-to-roll-out / how-to-present) is clean and ordered. Four CONCEPT callouts anchor the mechanism. The "why before how" structure gate holds: stakes arrive before mechanics. One structural drag: the chapter carries both a `## Hook` section *and* a full `## Overview` syllabus block before arriving at `## How it works` — the syllabus lists what will be covered twice (the subtitle and the overview bullets) and the "If you hold one idea" paragraph at line 38 pre-summarizes the synthesis before it is earned. This creates a throat-clearing corridor between the hook and the mechanism. The deep-dive (§78-82) is the chapter's clearest passage. |
| 2 | **ACCURACY** | 8 | DORA four keys (deployment frequency, lead time for changes, change-failure rate, failed-deployment recovery time) match the research dossier. SPACE five dimensions match Forsgren et al. dossier verbatim. The throughput-stability-correlate finding is accurately stated and properly attributed. Goodhart's law correctly attributed. Baseline/ratchet mechanics tied to specific tools (SpotBugs, Checkstyle, Sonar new-code) traceable to earlier chapters and the dossier. No invented rule IDs, config keys, API signatures, or benchmark figures appear — all specific band figures are deliberately ⚠-flagged. One precision concern: the draft uses "failed-deployment recovery time" (line 47) where DORA documentation uses "time to restore service" / "mean time to restore service" — this is a paraphrase of the standard term, and the ⚠ @pin flag covers it, but it is worth confirming at the pinned edition. SOURCE-PIN §7 canon gap (DORA/State-of-DevOps + SPACE not yet pinned rows) is acknowledged in the dossier and flagged in the draft. |
| 3 | **UTILITY** | 8 | Directly actionable: the DORA four-key pair (throughput + stability, never split), the "never Activity alone" SPACE rule, the baseline-then-ratchet-then-warn-then-block rollout sequence, the new-code-lens/audience-fit dashboard framing, the vanity-do-not-use list, the never-a-leaderboard rule. The "When to use what" section (lines 105-113) is a clean decision table. The chapter answers the three real questions a team lead actually faces: what to measure, how to introduce gates on a legacy codebase, and how to present progress. One gap: the "how to push back on leadership asking for LOC/velocity" is mentioned (line 51, line 89) but the counter-argument toolkit is thin — the chapter says "arm the reader to push back" but the arming is largely the claim itself rather than specific counter-framing a lead can use in a meeting. |
| 4 | **DEPTH** | 8 | Three dossiers synthesized coherently. The Goodhart-recurs-everywhere analysis (coverage→assertion-free tests at line 80, deploy-freq→split deploys, velocity→inflated estimates) with the counter-metric/trend/system-not-individual defenses shows genuine understanding, not surface listing. The "measurement-is-servant-of-judgment-not-replacement" synthesis at line 82 is the capstone observation: DORA does not detect architecture problems; a green dashboard is not quality. That is genuinely senior-level material and is the chapter's deepest insight. Mild deduction: the baseline and ratchet concepts are correctly described but never concretized below the concept level (what does a SpotBugs baseline XML file look like? what does a Sonar new-code gate actually gate on?). This is appropriate for the chapter's scope (tools are addressed in chs 16/17/19), so it is not a gap — but it means the depth lives at the process level, not the implementation level, which is honest. |
| 5 | **READABILITY** | 6 | The hook is gripping and the deep-dive prose is strong. However, the voice rule violations are systematic and material, and a sharp reader will feel them: (a) **21 contractions in narration prose** across 13 lines — "didn't", "That's", "doesn't" (×4), "haven't", "you've" (×2), "It's" (×4), "can't" (×4), "Don't", "they're" — none inside a quoted log/error string (the sole exemption). The VOICE guide is explicit: "no contractions in narration." This is the book's single most-checked authenticity gate. (b) **Second-person "you" in narration** at line 38 ("If you hold one idea"), line 53 ("you can split deploys"), line 78 ("the metric you gate on"), line 82 ("DORA tells you") — the locked person is third person; "you" in narration is banned except in imperatives. (c) **Em-dash density 25.2 per 1,000 words** against a target of ~8/1,000; the guide names the em-dash appositive "the prose's most over-used cadence and a clear AI tell." A strip-and-rewrite of the narration contractions, the second-person slips, and the em-dash overuse would bring this to 8. The content is strong; the delivery breaks the locked voice. |

**Cluster subtotal:** 38 / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| **NEUTRALITY** | PASS | Banned-phrase scan: zero hits (grep over body prose for "better than", "unlike X", "the problem with X", "superior", "beats", "kills", "destroys", "blows away", "no reason to use"). DORA vs SPACE is framed as complementary, not ranked (line 97: "DORA vs SPACE — delivery-outcome metrics versus a multi-dimensional productivity frame; complementary… not either/or"). Outcome vs vanity metrics is a discipline call (a quality principle, not a product comparison). No section title carries a comparative superlative. The DORA/SPACE/Goodhart choices are framed as "questions, not verdicts" — no winner crowned. PASS. |
| **HONEST-LIMITATIONS** | PASS | Every mechanism gets its hardest objection and explicit when-NOT-to-use. DORA: gameable (split deploys), incomplete (no signal for architecture/debt/mentoring), observational not causal (line 88). SPACE: needs honest self-report (dossier §4; echoed in draft). Baseline: without a paydown plan it becomes "formalized ignoring" (line 64; §90). Big-bang rollout: explicit FAIL case (line 91). New-code focus: leaves cold legacy untouched (line 92). Dashboard: weaponizes as leaderboard (line 72); dashboard nobody acts on is theater (line 74); green dashboard is not quality (line 74, 93). Vanity metrics: persist because easy to measure — the reader must push back (line 89). The "Limitations & when NOT to reach for it" section (lines 84-93) is thorough and does not hedge-stack. PASS. |
| **SOURCE-TRACE** | PASS | Zero invented rule IDs, config keys, tool flags, API signatures, GAV coordinates, or quoted claims in the prose body. DORA four keys traced to `dora.dev`/State of DevOps (dossier §8 source 1). SPACE traced to Forsgren et al. ACM Queue 2021 (dossier §8 source 2). Goodhart's law: attributed by name, core principle matches standard formulation. Baseline/ratchet mechanics (SpotBugs baseline/exclude, Checkstyle suppressions, Sonar new-code) cross-referenced to chapters that hold those details — not re-derived. All specific DORA performance bands and SPACE dimensions ⚠-flagged at pin with "year matters" caveat (line 49, back-matter §121). SOURCE-PIN §7 canon gap acknowledged (DORA/SPACE not yet pinned rows). No unverified claims asserted as fact. COMPILE = PENDING per task instruction (process/spec artifacts; companion module spec'd). CODE-REVIEW = N/A per task instruction. SOURCE-TRACE PASS. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (≥35/50, no cluster below 6); all floors PASS
- [x] **LIFT-LOOP** — close; apply the line-level fixes below and re-score
- [ ] **CUT**

**One-line rationale:** Three floors PASS and the aggregate clears 35 (38/50, no cluster below 6), but READABILITY scores 6 due to 21 narration contractions, 4+ second-person "you" slips, and em-dash density at 3× target — a voice-conformance pass is required before this chapter reads as the locked voice.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 6
- **Why it is the weakest:** The chapter contains 21 contractions in narration prose (none are inside quoted error/log strings, the only exemption), repeated second-person "you" in narrative passages (banned by the locked person rule), and an em-dash density of ~25/1,000 words against an ~8/1,000 target. The VOICE guide identifies these as the "clearest AI tells" and the authenticity gate requires a sharp reader be unable to detect AI authorship. The content is strong — the voice delivery fails the rule.
- **Single highest-leverage move:** Strip narration contractions to their full-form equivalents (21 contractions → "did not", "that is", "does not", "have not", "it is", "cannot", "do not", "they are", "you have") and simultaneously convert second-person "you" in narration to third-person constructions; then cut em-dashes by two-thirds (replace most with commas, periods, or colons), targeting ~8/1,000 density.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | Line 23 (hook para) | "didn't", "That's" — narration contractions | → "did not"; "That is" |
| 2 | READABILITY | Line 38 (Overview) | "If you hold one idea" — second-person "you" in narration | → "The one idea worth holding:" or "Hold this one idea:" (imperative is permitted) |
| 3 | READABILITY | Line 53 (CONCEPT callout) | "you can split deploys" — second-person "you" in narration | → "a team can split deploys" |
| 4 | READABILITY | Lines 59, 64, 68, 80, 82, 88-91, 113, 117 | 19 remaining narration contractions spread across these lines | Strip every contraction: doesn't → does not; haven't → have not; you've → the debt has been; It's → It is; can't → cannot; Don't → Do not; they're → they are; etc. |
| 5 | READABILITY | Lines 78, 82 | "the metric you gate on" (L78); "DORA tells you" (L82) — second-person in narration | → "the metric the gate targets"; "DORA indicates" |
| 6 | READABILITY | Lines 23, 25, 36, 44, 51, 57, 62, 68, 70, 74, 78, 80, 82 | Em-dash density 25/1,000 in prose — target ~8/1,000; guide calls it "the most over-used cadence and a clear AI tell" | Convert 2/3 of em-dash appositives to commas, colons, or periods; keep only where the appositive is genuinely parenthetical and emphatic |
| 7 | CLARITY | Lines 25-38 (Overview block) | Hook → Overview syllabus announces structure twice; "If you hold one idea" pre-summarizes the synthesis before it is earned — throat-clearing between hook and mechanism | Trim the Overview to 3–4 tight lines; move "If you hold one idea" to the chapter close (where the synthesis is earned) or cut it; let the hook drive directly to §"How it works" |
| 8 | UTILITY | Lines 51, 89 | "arm the reader to push back on LOC/velocity demands" is stated but the counter-argument is not supplied — the dossier mentions vanity persists because easy; the chapter could add one concrete reframe a lead can use | Add 1–2 sentences naming the specific counter-move: "when leadership asks for LOC velocity, offer deployment frequency and change-failure rate paired — the DORA throughput-stability pair resists the single-axis gaming LOC invites." |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 38 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | initial independent score |

---

## Comparison with author scorecard

The author scorecard (in `85_metrics_rollout_dashboards_SCORE.md`) scored this 44/50 (READABILITY 8, CLARITY 9). This independent score is 38/50 (READABILITY 6, CLARITY 8). The gap is driven by a different judgment on voice conformance: the author scorecard did not flag the 21 narration contractions, the second-person slips, or the em-dash density as READABILITY deductions. This independent scorer finds these material — the VOICE guide is explicit on all three as "clearest AI tells" and the authenticity gate is a hard requirement. On the substance (ACCURACY, UTILITY, DEPTH), the judgments are essentially aligned at 8/8/8 vs 9/9/9; the independent scorer rates slightly lower on the grounds that DORA's specific performance bands are still unverified at pin (a genuine open item the dossier itself flags). The floor verdicts agree: A/B/C-source all PASS, COMPILE PENDING.

---

## Learnings & pipeline suggestions

1. **Contraction-check should be a scripted pre-gate.** The 21 narration contractions are a deterministic, greppable failure. A `check_voice.sh` script running `grep -n "didn't\|don't\|can't\|won't\|it's\|that's\|you've\|they're\|you're\|isn't\|wasn't\|weren't\|haven't"` over the body prose (lines after the HTML comment block) would catch this before it reaches the scorecard. Add this to the AUDIT gate pre-pass.

2. **Em-dash density is a scripted check.** The target is ~8/1,000. The current pass is 25/1,000. The python snippet used in this scorecard (count `—` in body prose, divide by word count) is the test. Add it to `check_voice.sh` or as a standalone `check_emdash.sh`.

3. **Second-person "you" in narration is greppable.** `\byou\b` in prose lines (not bullet lists, not quoted strings) catches this. The imperative exception is allowed for instruction steps; the narration exception is not.

4. **Independent scoring by a different model is necessary.** The author scorecard (Opus) scored READABILITY at 8; this independent score (Sonnet 4.6) scored it at 6, driven by explicit rule checks rather than gestalt reading. The two-model gate is working as designed — same rubric, different calibration. The pattern to log: Opus tends to smooth over voice rule violations when content is strong; the independent gate should specifically re-run the VOICE guide checklist.

5. **"If you hold one idea" device.** This device appears in multiple chapters. It is not prohibited but it consistently creates a throat-clearing corridor in the Overview and pre-summarizes the synthesis before it is earned. Consider adding a PIPELINE-LEARNINGS note that this device should appear only at chapter close (after the synthesis is earned) or be cut — not in the Overview.

6. **DORA/SPACE are §7 canon gaps.** The fact that the State-of-DevOps and SPACE ACM Queue paper are not yet pinned rows is a real quality risk: band figures cannot be verified at pin. The pipeline should add these as explicit pin targets in SOURCE-PIN.md before any chapter citing DORA bands is approved.
