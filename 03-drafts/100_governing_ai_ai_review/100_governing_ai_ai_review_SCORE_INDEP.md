# INDEPENDENT SCORECARD — Ch 42 — model: Claude Sonnet 4.6 — 2026-06-20

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 100 (folds 98)
- **Slug:** `100_governing_ai_ai_review`
- **Title:** Only Policy Can Ship It — Governing AI in the development workflow
- **Part / arc position:** Part XII (closer), Ch 42 of 47
- **Artifact scored:** `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`
- **Dossier read:** `02-research/100_governing_ai_workflow/100_governing_ai_workflow_RESEARCH.md`
- **Verified against Java code quality the pins in SOURCE-PIN.md** — pinned at 2026-06-20 (re-check date: 2026-06-20)
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — independent model, different from drafter)
- **Date:** 2026-06-20
- **Lift-pass #:** 0

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Mechanism explained cleanly; ordered; why is as clear as what | **7** | The governance spine (sanction → verify → human gate → disclose → measure) is clearly ordered and each section earns the next. The "intent ceiling" is well-constructed from both the generation and review directions. Deduction: the dossier specifies Fig 100.1 (AI governance loop) but no figure is present or referenced in the draft — the "How it works" section is a wall of prose without a visual anchor, which SCORING.md explicitly flags as a deficiency. |
| 2 | **ACCURACY** | Every specific figure/claim traces to a pinned source; nothing invented | **6** | No rule IDs, API flags, or config keys are invented (correct for a policy/concept chapter). Statistics are real and attributed, not fabricated — but five of the key figures are not yet in SOURCE-PIN.md pinned rows: (a) the "16-tool / ~22,000 comments / ~35% critical / single-digit subtle" figures trace to arXiv 2508.18771 (§7 canon gap, TO-PIN); (b) NIST SATE "~50-60% on security" plateau (§7 gap, TO-PIN); (c) O'Reilly "catches half your bugs" (§7 gap, TO-PIN); (d) Sonatype "only policy can ship it" quote (dossier §7 flags for attribution confirmation); (e) the productivity/risk triad (~78%/~72%/~65%) is attributed to industry surveys, plausibly traceable to DORA 2025 (pinned §5), but the chapter does not cite DORA explicitly. Draft back-matter marks these "⚠ §7 canon rows, figures @pin / TO-PIN" — transparent but unresolved at this scoring event. |
| 3 | **UTILITY** | Reader can act on this; concrete decision frames; when-to-use | **8** | "When to use what" is the most actionable section in the chapter: tool vetting criteria (data-handling, code-leaves-org, IP, privacy scorecard), verification policy checklist (same gates + SAST/SCA/secrets + mutation-verify AI tests + no auto-merge), AI-review patterns (scope to diff, standards-as-context, specific lenses, draft-and-disposition), and the independence-for-AI-reviewing-AI rule are all immediately applicable. Limitation: example-build is PENDING (policy/illustrative artifact, not buildable module) — dossier notes this upfront and the draft repeats it; the companion spec is clear and well-formed. |
| 4 | **DEPTH** | Enough verified substance; mechanism + for + against + alternatives + when-to-use | **8** | The chapter earns its two-topic scope (governance + AI code review) with distinct mechanism, evidence, ceiling analysis, and alternatives for each. The "intent ceiling as the invariant" deep-dive is the chapter's strongest substantive contribution — framing the same wall (no ground truth of what the code should do) as applying symmetrically to AI generation and AI review. The self-referential close (this book as worked example of its own governance thesis) adds depth without padding. The "AI reviewing AI compounds blind spots / independence" angle is substantive and well-grounded. |
| 5 | **READABILITY** | Prose carries the reader; locked voice holds; hook in; forward hook out | **7** | Hook is strong (the post-mortem "the AI did it" non-defense — concrete scene, stakes-first). Forward hook is well-formed and pulls into Part XIII. Voice is correct: third person, no narration contractions, no first person throughout. Three CONCEPT callouts are appropriate and not overused. Deductions: (1) em-dash density is high — the chapter uses em-dash appositives in nearly every paragraph (e.g., "AI code review — using AI to help review the flood of AI code", "inference is not verification — that is a ceiling no model capability removes", "the human gate — not in the sense that humans must read every line"), well above the ~8/1,000 target; (2) self-narration appears in the deep-dive: "Part XII has made one argument at three scales, and the close is the place to see it whole" and "strip away the productivity numbers and the tooling and the governance frameworks, and the durable principle is small and old" — the narrator announces the structure rather than stating the point; (3) "The honest center, and the right way to close the part, is that..." is a self-narrating sentence that should be cut to its content. |

**Cluster subtotal:** 36 / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | No winner crowned; no banned phrasings; alternatives approach-based; cross-subject claims cited | **PASS** | Full scan: no banned phrases found ("better than", "unlike X", "the problem with X", "superior", "beats", "kills", "blows away", "no reason to use X"). The "Alternatives & adjacent approaches" section is correctly approach-based ("Govern vs ban vs free-for-all — the central choice"; "AI review vs deterministic tools vs human review — three layers: … composed, not substituted"). No tool is crowned. Comparative mentions are scoped: deterministic tools, human review, and AI review are each given their value and their limits. The "shadow AI" failure mode is narrated neutrally as a failure of prohibition policy, not as a comparative put-down of any named tool. PASS. |
| **HONEST-LIMITATIONS** | Every feature gets hardest objections + explicit when-NOT-to-use | **PASS** | The dedicated "Limitations & when NOT to reach for it" section (7 bullets) covers every major claim with an honest ceiling: policy-without-enforcement-fails; banning-drives-underground; accountability-doesn't-transfer; AI-review-cant-verify-intent (with the empirical ceiling figure); AI-reviewing-AI-compounds-blind-spots; automation-bias; measure-risk-not-just-productivity; stats-volatile-vendor-flagged; not-legal-advice. The in-text CONCEPT callouts reinforce limitations ("counter-metric productivity with risk", "AI review can't verify intent — that's the fundamental ceiling"). No feature is presented as cost-free. PASS. |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented detail; everything traces to the pinned authority set or is flagged to 09-flags/; COMPILE = PENDING (separate track); CODE-REVIEW = N/A | **PENDING** | SOURCE-TRACE: No rule IDs, API signatures, GAV coordinates, or config keys appear (correct for a policy chapter — no invented technical atoms). Statistics are real and attributed, not invented. However, five figure-clusters are not yet in SOURCE-PIN.md pinned rows and are marked TO-PIN in back-matter: (1) arXiv 2508.18771 (16-tool / 22,000 comments / ~35%/single-digit figures); (2) NIST SATE plateau (~50-60%); (3) O'Reilly "half your bugs"; (4) Sonatype "only policy can ship it" attribution confirmation; (5) productivity/risk triad (~78%/~72%/~65%) — DORA 2025 is pinned (§5 of SOURCE-PIN.md) and is the most plausible primary source, but the chapter does not cite DORA explicitly for these numbers. The draft back-matter discloses all of these as TO-PIN, which is honest but leaves them unresolved. SOURCE-TRACE is PENDING (not FAIL because no figures are invented — they are real, attributed, and openly flagged — but not PASS because the pin rows do not yet exist for the AI-review study and NIST/O'Reilly figures). COMPILE: PENDING per instruction (policy/illustrative artifact, not buildable module). CODE-REVIEW: N/A per instruction. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (≥35/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate.
- [x] **LIFT-LOOP** — close; apply the line-level fixes below and re-score (increment lift-pass #).
- [ ] **CUT** — below bar or a structural floor failure; return to drafting or re-scope.

**One-line rationale:** Cluster aggregate 36/50 clears the bar and no cluster is below 6; NEUTRALITY and HONEST-LIMITATIONS both PASS; SOURCE-TRACE is PENDING on five unresolved figure-sets that must be pinned or flagged to 09-flags/ before the chapter can reach SHIP status. LIFT is the correct verdict: resolve the SOURCE-TRACE pending items (pin the arXiv / NIST / O'Reilly rows or formally flag to 09-flags/ and replace with confirmed DORA-traced figures), add the missing Fig 100.1, and trim the em-dash density and self-narration tells.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score 6
- **Why it is the weakest:** Five key figure-clusters used in the chapter (the 16-tool AI-review study from arXiv 2508.18771; NIST SATE plateau; O'Reilly "half your bugs"; Sonatype "only policy can ship it"; and the productivity/risk triad) do not yet have corresponding rows in SOURCE-PIN.md. The chapter's back-matter transparently marks these TO-PIN, but a scored chapter requires them to be either pinned or replaced with confirmed DORA 2025-traced figures.
- **Single highest-leverage move to lift it:** Add arXiv 2508.18771, NIST SATE (or its primary source), and the O'Reilly source to SOURCE-PIN.md §7 (or confirm the DORA 2025 report as the primary for the productivity/risk triad and cite it explicitly in the chapter). Alternatively, replace unresolvable figures with the confirmed DORA 2025 figures and flag the arXiv/NIST/O'Reilly figures to 09-flags/ with a TO-PIN note.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / SOURCE-TRACE | Back-matter §sources + in-text stats (§ "Governing AI" CONCEPT callout; § "AI code review" CONCEPT callout) | arXiv 2508.18771 (16-tool study), NIST SATE, O'Reilly "half your bugs", and Sonatype quote are marked TO-PIN but are not in SOURCE-PIN.md pinned rows; DORA 2025 is pinned but not cited explicitly for the productivity/risk triad | Add these sources to SOURCE-PIN.md §7 (with edition/URL) OR replace with confirmed DORA 2025-attributed figures + flag the remainder to 09-flags/. Update in-text citations to name the pinned source explicitly (e.g. "per the 2025 DORA report"). |
| 2 | CLARITY | § "How it works" (entire section) | Dossier spec calls for Fig 100.1 — AI governance loop (sanction → AI assists → same gates + AI-specific checks → human gate → disclose → measure); no figure is present or referenced in the draft — the mechanism section is all-prose. | Add the figure reference (as designed HTML→PNG per GUIDELINES §8) and add a prose pointer to it before the governance bullet list; even a placeholder "[Fig 100.1 — AI governance loop — pending render]" improves the skeleton clarity for the next pass. |
| 3 | READABILITY | § "Deep dive" ¶1 ("Part XII has made one argument at three scales, and the close is the place to see it whole.") | Self-narrating sentence; the narrator announces the argument's structure rather than stating the argument. | Cut the meta-frame. Open directly on the invariant: "The human gate holds at every scale of the Part XII argument — mechanism, individual, organization — for one reason: …" |
| 4 | READABILITY | § "Deep dive" ¶3 ("The honest center, and the right way to close the part, is that this book is the worked example of its own thesis.") | Self-narration ("the right way to close the part") | Cut the introduction; start with the claim: "This book is the worked example of its own thesis." |
| 5 | READABILITY | § "How it works" + § "AI code review" | Em-dash density is high across both sections; the appositive cadence repeats in nearly every paragraph. | Convert the majority of em-dash appositives to periods, commas, or parentheses. Leave at most two or three per section where the appositive adds genuine snap. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 36 / 50 | PASS | PASS | PENDING | LIFT-LOOP | initial independent score (Claude Sonnet 4.6) |

---

## Learnings & pipeline suggestions

1. **Policy/concept chapters with external-study statistics need their own SOURCE-PIN §7 sub-section before drafting.** The dossier correctly flagged all five figure-clusters as TO-PIN, but the chapter reached the scoring gate without those rows being added. The pipeline should require the source-verifier to resolve every §7 TO-PIN item before the drafter is handed the dossier — or at minimum before the chapter-scorer runs. Add a gate-check: if any dossier §7 item is still TO-PIN, SOURCE-TRACE cannot reach PASS.

2. **Figures specified in the dossier's worked-example/figure spec must appear (or be referenced as pending render) in the draft.** Fig 100.1 was specified in the dossier and is missing from v1. The FIGURE gate (Step 9) is downstream, but clarity suffers when the mechanism section has no visual anchor. The drafter should add a bracketed placeholder for any figure specified in the dossier, so the scorer can judge the intended structure.

3. **The self-referential "this book practices what it preaches" close is a genuine authorial strength** — it is not a flourish; it is the demonstration. The issue is only the self-narrating framing that announces it rather than stating it. Preserve the substance; cut the meta-commentary.

4. **Em-dash density in AI-governance/concept chapters trends high** because every nuance wants an appositive. Flag this at AUDIT gate with a count (target ~8/1,000 words); the AUDIT gate should surface the count so the drafter can prune before the chapter reaches the scorer.

5. **DORA 2025 is the most credible and already-pinned source for AI-productivity/risk stats.** The productivity/risk triad should be explicitly attributed to the DORA 2025 report (SOURCE-PIN §5) in the chapter text, not left as a generic "industry surveys" reference. This would immediately resolve the most significant ACCURACY uncertainty.

---

*Append to `00-strategy/PIPELINE-LEARNINGS.md`:*

> **2026-06-20 · Ch 42 (key 100/98) independent score:** Policy/concept chapters with external study figures (AI-review tooling studies, NIST SATE) require SOURCE-PIN §7 rows to be resolved BEFORE scoring — a TO-PIN dossier flag that reaches the scorer unresolved produces a SOURCE-TRACE PENDING verdict even when no figures are invented. Add a pre-scoring gate: if any §7 item is TO-PIN, block the score run and resolve first. DORA 2025 (already pinned §5) should be the primary citation for AI-productivity/risk stats in these chapters. Figures specified in the dossier's worked-example spec must appear in the draft (or as a placeholder) before CLARITY can score above 7.
