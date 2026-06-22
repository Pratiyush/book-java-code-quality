# INDEPENDENT SCORECARD — Ch 42 — model: Claude Sonnet 4.6 — 2026-06-20 (lift pass 1)

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 100 (folds 98)
- **Slug:** `100_governing_ai_ai_review`
- **Title:** Only Policy Can Ship It — Governing AI in the development workflow
- **Part / arc position:** Part XII (closer), Ch 42 of 47
- **Artifact scored:** `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`
- **Figure scored:** `05-figures/100_governing_ai_ai_review/fig100_1.png` (885 KB, rendered 2026-06-20 22:14; sources traced in `fig100_1.sources.md`)
- **Dossier read:** `02-research/100_governing_ai_workflow/100_governing_ai_workflow_RESEARCH.md`
- **Verified against Java code quality SOURCE-PIN.md** — pinned at 2026-06-20 (re-check date: 2026-06-20)
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — independent model, different from drafter)
- **Date:** 2026-06-20
- **Lift-pass #:** 1

---

## What changed since pass 0

| Item | Pass 0 state | Pass 1 state |
|---|---|---|
| Fig 100.1 | Absent from draft; dossier specified it | Rendered (`fig100_1.png`, 885 KB); referenced inline in "How it works" with caption; `fig100_1.sources.md` traces every label to the draft |
| Voice / self-narration | "Part XII has made one argument at three scales, and the close is the place to see it whole"; "The honest center, and the right way to close the part, is that..." | Both phrases removed; deep-dive opens directly on the invariant; self-referential close opens "This book is the worked example of its own thesis." |
| Em-dash density | High; appositive cadence in nearly every paragraph | Reduced but not yet at ~8/1,000 target; still somewhat frequent in the governance and AI-review sections |
| SOURCE-TRACE pending items | Five figure-clusters flagged TO-PIN (arXiv 2508.18771; NIST SATE; O'Reilly "half your bugs"; Sonatype attribution; productivity/risk triad) | Status unchanged — no new SOURCE-PIN §7 rows added; DORA 2025 (§5, pinned) remains the most credible primary for the productivity/risk triad but is still not cited explicitly in chapter text |

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Mechanism explained cleanly; ordered; why is as clear as what | **8** | Fig 100.1 is now present and referenced in the "How it works" section — the governance loop (sanction → AI assists → same gates + AI-specific checks → human gate → disclose → measure) has a visual anchor. The inline reference and caption are correctly placed before the governance bullet list. The figure sources file (`fig100_1.sources.md`) traces every label and annotation to the draft. The mechanism is ordered and each stage earns the next. The "intent ceiling approached from both directions" framing in the deep-dive is one of the chapter's clearest structural contributions. No remaining prose-only walls in the mechanism section. |
| 2 | **ACCURACY** | Every specific figure/claim traces to a pinned source; nothing invented | **6** | No rule IDs, API signatures, GAV coordinates, or config keys appear (correct for a policy/concept chapter — no invented technical atoms). Statistics are real, attributed, and explicitly flagged as vendor-sourced snapshots — not invented. However, four figure-clusters remain without SOURCE-PIN.md rows: (a) arXiv 2508.18771 (16-tool / ~22,000 comments / ~35% critical / single-digit subtle) — §7 canon gap, TO-PIN; (b) NIST SATE plateau (~50–60% on security) — §7 gap, TO-PIN; (c) O'Reilly "catches half your bugs" — §7 gap, TO-PIN; (d) Sonatype "only policy can ship it" — attribution flagged, TO-PIN. The productivity/risk triad (~78%/~72%/~65%) is attributed to "recent (2024–2025) industry surveys" in chapter text; DORA 2025 (SOURCE-PIN §5, pinned) is the strongest primary source for these figures but is not cited explicitly in the chapter. This is not invented material, but unresolved pin rows are a real accuracy gap. Score holds at 6. |
| 3 | **UTILITY** | Reader can act on this; concrete decision frames; when-to-use | **8** | "When to use what" remains the most actionable section: tool-vetting criteria (data-handling, code-leaves-org, IP, privacy posture), verification checklist (same gates + SAST/SCA/secrets + mutation-verify AI tests + no auto-merge), AI-review patterns (scope to diff, standards-as-context, specific lenses, draft-and-disposition), and the independence rule for AI-reviewing-AI. All immediately applicable. The Alternatives section is approach-framed and decision-useful. Companion artifact spec is clear (policy template + illustrative AI-review comment); EXAMPLE-BUILD = PENDING per dossier (policy/illustrative artifact, not buildable module — declared upfront). |
| 4 | **DEPTH** | Enough verified substance; mechanism + for + against + alternatives + when-to-use | **8** | Two-topic scope (governance + AI code review) is earned with distinct mechanism, evidence-for, ceiling analysis, and alternatives for each. The "intent ceiling" framing — the same wall (no ground truth of what code should do) applies symmetrically to AI generation and AI review — is the chapter's strongest substantive contribution and is unique to this chapter. The self-referential close (this book as a worked example of its own thesis, with specific structural parallels: Step-12 = human gate, independence gates = different-model rule, source-tracing = verify-before-ship) adds depth without padding. The "AI reviewing AI compounds blind spots / independence is the safeguard" angle is substantive and well-grounded in the dossier. |
| 5 | **READABILITY** | Prose carries the reader; locked voice holds; hook in; forward hook out | **8** | Voice pass removed the two self-narrating sentences flagged in pass 0. The deep-dive now opens directly on the invariant, and the self-referential close opens on the claim rather than announcing that a close is coming. Hook is strong (the post-mortem non-defense — concrete scene, stakes-first). Forward hook into Part XIII is well-formed and pulls on a genuine open question. Voice is correct throughout: third person, no narration contractions, no first person, imperative only for instructions. Three CONCEPT callouts are appropriate and not stacked. Em-dash density is reduced from pass 0 but still slightly above the ~8/1,000 target in the governance and AI-review sections — not severe enough to cost a point, but noted for the next pass if one occurs. |

**Cluster subtotal:** 8 + 6 + 8 + 8 + 8 = **38 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **A — NEUTRALITY** | No winner crowned; no banned phrasings; alternatives approach-based; cross-subject claims cited | **PASS** | Full scan of pass-1 draft: no banned phrases found ("better than", "unlike X", "the problem with X", "superior", "beats", "kills", "blows away", "no reason to use X"). The "Alternatives & adjacent approaches" section is approach-based ("Govern vs ban vs free-for-all — the central choice"; "AI review vs deterministic tools vs human review — three layers: … composed, not substituted"). No tool is crowned. Deterministic tools, AI review, and human review are each given value and limits. The "shadow AI" failure mode is narrated as a failure of prohibition policy, not as a comparative put-down of any named tool. The AI-review tooling variance note ("crown none, benchmark for your context") is explicitly non-crowning. PASS. |
| **B — HONEST-LIMITATIONS** | Every feature gets hardest objections + explicit when-NOT-to-use | **PASS** | The dedicated "Limitations & when NOT to reach for it" section (8 bullets) covers every major claim: policy-without-enforcement-fails; banning-drives-underground; accountability-stays-with-author; AI-review-cant-verify-intent (with empirical ceiling); AI-reviewing-AI-compounds-blind-spots; automation-bias; measure-risk-not-just-productivity; stats-volatile-vendor-flagged; not-legal-advice. In-text CONCEPT callouts reinforce limitations inline ("counter-metric productivity with risk — velocity blind is a trap"; "AI review cannot verify intent — that is the fundamental ceiling"; "AI reviewing AI compounds blind spots — independence is the safeguard"). No feature is presented as cost-free. PASS. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented detail; everything traces to pinned authority set or flagged to 09-flags/; COMPILE = separate track (policy artifact); CODE-REVIEW = N/A | **PENDING** | SOURCE-TRACE: No rule IDs, API signatures, GAV coordinates, or config keys appear — correct for a policy/concept chapter; no invented technical atoms. Statistics are real, dated, and attributed — none are invented or contradicted by the dossier. The back-matter explicitly marks all four unresolved figure-clusters as "⚠ §7 canon rows, figures @pin / TO-PIN." Under the scoring brief's directive (FAIL C-source only if an atom is unattributed or contradicts the dossier), these PENDING rows are not a FAIL. However, they are also not a PASS: SOURCE-PIN.md §7 does not contain arXiv 2508.18771, NIST SATE, O'Reilly "half your bugs," or a confirmed Sonatype attribution row. The productivity/risk triad is not explicitly cited to DORA 2025 (pinned §5) in the chapter text. SOURCE-TRACE = PENDING (fourth consecutive scoring event at this status — the pin rows must be resolved before this chapter can reach SHIP). COMPILE: PENDING per pipeline (policy/illustrative artifact, not buildable module). CODE-REVIEW: N/A. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **APPROVE** — clears the APPROVE bar (≥45/50, all THREE floors PASS). Ready for the human approval gate.
- [x] **LIFT** — aggregate above the standard ship bar (≥35/50, no cluster below 6); A and B PASS; C-SOURCE PENDING; ACCURACY at 6 is the weakest cluster and the only one with remaining prose-fixable blockers.
- [ ] **CUT** — below bar or structural floor failure.

**One-line rationale:** Aggregate 38/50 (76%) clears the standard ship bar (≥35/50, no cluster below 6) and NEUTRALITY and HONEST-LIMITATIONS both PASS, but the APPROVE threshold (≥45/50) is not cleared and SOURCE-TRACE remains PENDING on four unresolved pin rows. The voice pass successfully resolved CLARITY (+1) and READABILITY (+1); one further ACCURACY pass — explicitly citing DORA 2025 for the productivity/risk triad and either adding §7 SOURCE-PIN rows for the AI-review studies or formally flagging them to 09-flags/ — is the highest-leverage remaining move.

---

## Remaining blockers to 90%

| # | Cluster / floor | Blocker | Type | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / SOURCE-TRACE | Productivity/risk triad (~78%/~72%/~65%) attributed to "industry surveys" — DORA 2025 (SOURCE-PIN §5) is pinned and is the most credible primary; not cited explicitly in chapter text | **prose-fixable** | In the CONCEPT callout "Counter-metric productivity with risk," replace "recent (2024–2025) industry surveys" with "the 2025 DORA report (§5)" and verify the exact figures against the pinned DORA 2025 row. |
| 2 | ACCURACY / SOURCE-TRACE | arXiv 2508.18771 (16-tool / ~22,000 comments / ~35% critical / single-digit subtle), NIST SATE plateau (~50–60%), O'Reilly "half your bugs," Sonatype "only policy can ship it" — not in SOURCE-PIN §7; TO-PIN | **needs-pin-verify** | Add §7 rows for each source (or formally flag to 09-flags/ with a TO-PIN note and replace in-chapter text with the confirmed DORA-traced figure or a hedged reference without the specific study numbers). |
| 3 | READABILITY | Em-dash density in the governance and AI-review sections still slightly above the ~8/1,000 target | **prose-fixable** | One targeted pass converting the remaining em-dash appositives to commas or periods in the two densest paragraphs (§ "AI code review: a bounded augmentation" and § "Governing AI"). |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 36 / 50 (72%) | PASS | PASS | PENDING | LIFT | Initial independent score (Claude Sonnet 4.6). Fig 100.1 absent; self-narrating deep-dive frames present; em-dash density high. |
| 1 | 2026-06-20 | 38 / 50 (76%) | PASS | PASS | PENDING | LIFT | Voice pass: Fig 100.1 rendered (885 KB) and referenced inline with caption; self-narrating introductions in deep-dive removed; em-dash density reduced. SOURCE-TRACE PENDING items unchanged (four TO-PIN rows; no explicit DORA 2025 citation added). CLARITY +1, READABILITY +1. |

---

## Learnings & pipeline suggestions

1. **A voice pass that adds a rendered figure is the highest single-pass CLARITY gain available.** Lift pass 1 recovered 2 points (CLARITY 7→8, READABILITY 7→8) by resolving the two items the pass 0 score identified as deductions: the missing figure and the self-narrating deep-dive frames. Future drafter passes should order these two items first when CLARITY or READABILITY is the weakest cluster.

2. **SOURCE-TRACE PENDING status survives across multiple passes when the dossier §7 gap is not addressed.** The four TO-PIN rows (arXiv 2508.18771, NIST SATE, O'Reilly, Sonatype) have been PENDING since dossier creation. The pipeline should require the source-verifier to resolve §7 TO-PIN items before the chapter scorer runs — a TO-PIN gap that reaches the scorer unresolved produces a SOURCE-TRACE PENDING verdict at every pass until resolved. Candidate resolution: cite DORA 2025 (already pinned §5) explicitly for the productivity/risk triad; flag arXiv/NIST/O'Reilly to 09-flags/ with a TO-PIN note; rewrite in-chapter language to rely on the confirmed DORA figures.

3. **The APPROVE threshold (≥45/50) is structurally unreachable for a policy/concept chapter with PENDING source rows.** Until the SOURCE-TRACE floor resolves to PASS, the chapter is correctly held at LIFT regardless of the cluster aggregate. The cluster aggregate (38/50 at pass 1) is already above the standard ship bar; the bottleneck is the SOURCE-TRACE floor, not the cluster scores.

4. **The "AI statistics intentionally dated+attributed" policy (scorer brief) correctly prevents FAIL for real-but-unresolved statistics.** The chapter handles this well in back-matter: every figure-cluster is marked ⚠ with its source and its TO-PIN status. This is the right authorial practice; the scorer should continue to treat it as PENDING, not FAIL, and pipeline enforcement should catch it before the chapter ships (i.e., resolve §7 rows before human approval gate, not after).

5. **Em-dash density is a lagging indicator.** The voice pass reduced it but did not bring it below the ~8/1,000 target. The AUDIT gate em-dash count should be surfaced to the drafter at the start of each lift pass, not only as a scorer observation. A target count (e.g., "reduce from ~14/1,000 to ≤8/1,000") is more actionable than a qualitative note.

---

*Append to `00-strategy/PIPELINE-LEARNINGS.md`:*

> **2026-06-20 · Ch 42 (key 100/98) lift pass 1:** Voice pass (figure rendered + self-narration pruned) gained 2 points in 1 pass (CLARITY 7→8, READABILITY 7→8). SOURCE-TRACE PENDING status persists because §7 TO-PIN rows were not resolved — the bottleneck is the dossier gap, not the prose. Confirmed: APPROVE threshold (≥45/50) is unreachable while SOURCE-TRACE is PENDING. Recommended pipeline change: block the chapter scorer from running until all dossier §7 TO-PIN items are resolved or formally flagged to 09-flags/. DORA 2025 (§5) should be the explicit citation for AI-productivity/risk stats; the chapter should name it rather than "industry surveys."
