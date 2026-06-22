# INDEPENDENT SCORECARD — Ch 41 — model: Claude Sonnet 4.6 — 2026-06-20 (lift pass 1)

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 97 (folds 99)
- **Slug:** `97_ai_generated_code_quality`
- **Title:** The Draft That Looks Like a Deliverable — Quality of AI-generated Java: characteristic risks, refactoring/test-gen guardrails
- **Part / arc position:** Part XII — AI-Era Code Quality (Ch 41, OPENS Part XII; umbrella)
- **Artifact scored:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Verified against Java code quality the pins in SOURCE-PIN.md** — pinned at 2026-06-20 (re-check date: 2026-06-20)
- **Scorer:** chapter-scorer agent (independent — Claude Sonnet 4.6, not the drafter model)
- **Date:** 2026-06-20
- **Lift-pass #:** 1

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | **8** | The two core mechanisms — vulnerability inheritance and confident wrongness — are introduced in plain language before any technical detail. The CONCEPT callouts give each its own clear definition. The chapter's two-halves structure (risks / guardrails) is announced and held. The mechanism logic flows cleanly: mechanism → characteristic risks → stance → refactoring guardrail → test-generation guardrail → why the gate is source-agnostic. The em-dash conversion (0.7/1,000 vs. prior 22/1,000) has improved the cadence of the Deep Dive paragraphs. Minor residual: the Deep Dive section carries several long, multi-clause sentences that correctly build an argument but could be broken up further without content loss. Score holds at 8. |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set at the pins in SOURCE-PIN.md. No invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Snippets/examples verified. | **7** | Positive: percentage statistics ("~40% with critical gaps," "XSS missed in the large majority of cases") remain framed as snapshots that "must be verified against the specific dated study and cited as a snapshot, never as a constant." arXiv papers 2502.01853 and 2409.19182 are named and acknowledged in SOURCE-PIN §7 as existing-but-figures-unverified rows. CodeScene three guardrails are attributed and flagged "⚠ wording @pin." The fig97_1.sources.md confirms every figure element traces to a prose passage with no new atoms introduced. No invented rule IDs, config keys, GAV coordinates, or version numbers. Deduction: arXiv figure atoms and CodeScene wording remain open PENDING-pin items; the statistics appear in prose body as "as of recent (2024–2025) studies" without inline study-name — the back matter carries the honest flag ("⚠ §7 canon rows, figures @pin") but the prose body does not name the study inline. These are PENDING-pin items per task instructions, treated as such (not failing). Score unchanged at 7. |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | **8** | "When to use what" (lines 93–101) gives concrete, differentiated guidance across five distinct scenarios. "Limitations & when NOT to reach for it" (lines 72–81) is a scannable checklist of nine distinct when-not conditions. "Alternatives & adjacent approaches" (lines 83–91) maps each choice to its trade-off. The fig97_1.png now appears inline as a load-bearing diagram showing the full gate pipeline — it adds a decision-support visual the prior pass lacked. Deduction: companion module remains PENDING; no inline code illustration of the SQL injection pattern or hollow-test anti-pattern exists in prose. Those two scenarios are mentioned in the hook and companion spec but absent in runnable form. Score unchanged at 8. |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | **8** | Two distinct mechanisms explained at mechanism level, not just named. Refactoring and test-generation sections go past the general warning to name the specific failure mode (behavior-change without a net; tests-from-code defeating double-bookkeeping). The Deep Dive advances the argument rather than restating it: gate is source-agnostic, bottleneck shifts from writing to verifying, confident wrongness defeats the human's instinct-based defense, and the statistics-volatility point is elevated to a durable structural argument. Self-aware provenance framing is a genuine depth move unique to this chapter's position. Deduction: "double-bookkeeping" remains a coined term where the attribution is flagged "⚠ wording @pin" in the dossier; no separate section addresses the coverage-theater mechanism in depth (it appears as a limitation bullet and a single passage). Score unchanged at 8. |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice per VOICE-GUIDE.md? Hook in, forward hook out? | **8** | Lift-pass changes are verified and significant. (a) Contractions: zero narration contractions in prose body lines 14–110 (grep confirms clean). (b) Em-dash density: 0.7/1,000 words in prose body, against a ~8/1,000 target — the voice pass eliminated the appositive-clause cadence that was the primary AI-tell. (c) Self-narration: the "not a slogan but the load-bearing principle" phrase is absent (grep confirms). (d) Figure 97.1 now appears inline at line 41 with a descriptive alt-text caption and matching italic caption line, and the PNG exists in `05-figures/`. The hook paragraph remains strong. The forward hand-off is still a pulling thread, not a syllabus. Residual note: the statistics paragraph at line 52 uses "(2024–2025)" informally in prose; this is not a voice violation but a precision deduction. The Deep Dive's final paragraph (line 74) is ambitious in scope and carries more subordinate clauses than the earlier sections, but it reads rather than stalls. Score lifted from 6 to 8. |

**Cluster subtotal:** 39 / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | Neutral comparative survey: each option gets its strongest case and its hardest limitation; no crowning; banned phrasings absent ("better than," "unlike X," "the problem with X," "superior," "beats," "kills," "destroys," "blows away," "no reason to use X"). No rival crowned superior. Any cross-subject claim has a cited source. | **PASS** | The prior FAIL phrase ("unlike an IDE refactoring or an OpenRewrite recipe," previously at prose line 57) has been reworded. The current prose at line 62 reads: "An LLM suggesting a structural change or modernization is *not behavior-preserving by construction*. An IDE refactoring or an OpenRewrite recipe (Chapters 39, 40) is correct by its type-aware mechanics; an AI's suggestion, lacking that guarantee, can *silently change behavior*." The banned construction "unlike X" is absent. Grep scan of prose body lines 14–110 for all blocklist phrases returns clean (the word "kills" appears only in the back-matter companion-spec note about mutation testing, which is technical spec shorthand, not narration prose). No rival is crowned. No banned phrasing found in narration. PASS. |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a "when NOT to reach for this." Environment/compatibility caveats stated where relevant. | **PASS** | "Limitations & when NOT to reach for it" section (lines 72–81) covers nine distinct when-not conditions: over-trust, vulnerability inheritance, hallucinated dependencies, never-generate-tests-from-code, AI refactoring not behavior-preserving, coverage theater, volume outpaces review, every statistic dates fast, do not ship what the team does not understand. Limitations are also woven into each CONCEPT callout and the alternatives section. Both AI-assisted-refactoring and AI-test-generation uses name their hardest failure mode explicitly. PASS. |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims — everything traces to the pins or is flagged to `09-flags/`. COMPILE = PENDING (separate track). CODE-REVIEW = N/A per scoring instructions. | **PASS (source-trace); PENDING (compile); N/A (code-review)** | Source-trace: no invented rule IDs, config keys, tool flags, API signatures, GAV coordinates, or version numbers in prose body. Percentage statistics are explicitly flagged in the prose itself as pending per-study verification, and the back matter repeats "ALL stats ⚠ VERIFY @pin + DATED + ATTRIBUTED." arXiv 2502.01853 + 2409.19182 named and acknowledged-gap. CodeScene three guardrails cited and flagged "⚠ wording @pin." The fig97_1.sources.md confirms all figure atoms trace to prose passages; no new atoms introduced by the diagram. No cross-subject claim about IDE/OpenRewrite asserts a figure — describes mechanical properties referenced from within-book chapters. COMPILE: PENDING — companion module not yet built; toolchain confirmed ready (JDK 21.0.11+25.0.3). Not scored against chapter on FLOOR C per task instructions. PASS (source-trace). |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **APPROVE** — ≥45/50 AND all floors PASS. Not reached: 39/50 = 78%.
- [x] **LIFT** — all three floors PASS; aggregate 39/50 clears the standard ship bar (≥35/50, no cluster below 6) but does not reach the 90%/45-point threshold. Remaining blockers listed below.
- [ ] **CUT** — below bar or structural floor failure.

**One-line rationale:** All three floors now PASS (FLOOR A confirmed clean after "unlike X" rewrite); the voice pass lifted READABILITY from 6 to 8 and the aggregate from 37 to 39/50 (78%). The chapter clears the standard ship bar and is gate-eligible, but does not reach the 90% threshold for APPROVE. The gap is ACCURACY (pinned arXiv figures + CodeScene wording remain open) and the absent companion module / inline code illustration.

---

## Remaining blockers to 90%

| Blocker | Cluster / floor affected | Nature | Prose-fixable / needs-figure / needs-pin-verify |
|---|---|---|---|
| arXiv 2502.01853 + 2409.19182 figures not yet pinned — the statistics ("~40% gaps," "XSS miss rate") cannot be asserted with inline study-name and year until the SOURCE-PIN §7 rows are confirmed | ACCURACY | Pin-verify the arXiv papers and update inline attribution to name study + year; or cut the figures from prose body and retain only the back-matter flag until pinned | **needs-pin-verify** |
| CodeScene three-guardrails wording not yet pinned to CodeScene's own docs — "Code Quality, Code Familiarity, Code/Test Coverage" is attributed but "⚠ wording @pin" | ACCURACY | Pin the CodeScene guardrails wording to CodeScene's own pinned docs (a SOURCE-PIN §5 or §7 row); confirm the exact names | **needs-pin-verify** |
| "double-bookkeeping" term origin not pinned — coined framing used as the chapter's central concept for test-generation; attribution flagged in dossier | ACCURACY / DEPTH | Find and pin the coined-term attribution (if it traces to a specific source) or reframe as the chapter's own naming of the concept (and note it explicitly) | **needs-pin-verify** |
| Companion module PENDING — no inline code illustration of the SQL injection / hollow-test scenarios despite being named in the hook and companion spec | UTILITY | Example-build track (separate from prose); once the companion module builds green, a tag-region snippet for the SAST-caught injection and the mutation-killed hollow test should be integrated into the "How it works" or "Deep dive" section | **needs-figure / needs-companion-build** |
| Statistics paragraph (line 52) names "2024–2025" as a date range without the specific study name inline | ACCURACY | Once arXiv rows are pinned, insert: "(arXiv 2502.01853, [year of study])" immediately after each figure in prose; until then the inline date-range is the honest handling but leaves an attribution gap | **needs-pin-verify** |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | FAIL (line 57: "unlike X") | PASS | PASS (src-trace); PENDING (compile); N/A (code-review) | LIFT-LOOP | Initial score — independent model (Claude Sonnet 4.6) |
| 1 | 2026-06-20 | 39 / 50 | PASS (reworded to neutral pattern) | PASS | PASS (src-trace); PENDING (compile); N/A (code-review) | LIFT (standard bar cleared; 90% not reached) | FLOOR A fixed ("unlike X" → neutral; prose at line 62 confirmed clean); READABILITY lifted 6→8: zero narration contractions in prose body (grep clean), em-dash density 0.7/1,000 (was ~22/1,000), self-narration phrase removed, fig97_1.png now referenced inline with caption |

---

## Learnings & pipeline suggestions

1. **NEUTRALITY fix is one sentence and zero content change.** The "unlike X" phrase appeared in a factually correct, necessary contrast. The neutral rewrite ("An IDE refactoring or an OpenRewrite recipe (Chapters 39, 40) is correct by its type-aware mechanics") carries identical content with zero floor risk. This confirms: run the banned-phrase grep on every draft before the drafter submits, not at the scorer stage. Recommend promoting a banned-phrase pre-scan to Step 4a in PIPELINE.md.

2. **Em-dash density as an AI-tell is both high-signal and fully mechanical.** A drop from 22/1,000 to 0.7/1,000 in one voice pass, with no content loss, demonstrates how decisively a targeted em-dash conversion improves perceived authenticity. The VOICE-GUIDE already specifies the ~8/1,000 ceiling; the lesson is to automate the check. Recommend `em_dash_density.sh` in `.claude/scripts/` (fail >15/1,000, warn >10/1,000).

3. **Contraction elimination in narration is a zero-risk mechanical pass.** Zero narration contractions remained in the prose body after the lift pass. No content was changed. A pre-submission grep (`n't `, `it's`, `that's`) would catch these before review and remove them from the scorer's consideration entirely.

4. **Statistics handled as snapshots is the right model for fast-moving empirical fields.** The chapter's treatment of the AI-defect-rate figures ("must be verified against the specific dated study") is the correct pattern for any chapter in an area where model capability and study results change faster than a book's pin cycle. Recommend capturing this explicitly in GUIDELINES.md as the standard for any chapter with volatile empirical figures.

5. **The 90% threshold surfaces pin-completeness gaps the standard bar does not.** At 39/50 (78%), the chapter clears the standard ≥35/50 ship bar but not the ≥45/50 APPROVE threshold. The gap is almost entirely ACCURACY, driven by PENDING-pin items (arXiv figures, CodeScene wording, double-bookkeeping attribution). The standard bar would ship a chapter with acknowledged pin gaps; the 90% threshold correctly holds it until the underlying sources are confirmed. This distinction is worth preserving in the rubric.
