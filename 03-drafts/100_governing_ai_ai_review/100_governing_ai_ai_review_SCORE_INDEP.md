# INDEPENDENT SCORECARD — Ch 42 (`100_governing_ai_ai_review`) — harsh-skeptic re-score + bounded lift

> Independent (different-model) score of the **current** printed draft
> `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md` (last revised 2026-06-27 23:38 —
> AFTER the prior independent score of 2026-06-20, which read an earlier draft state). Target bar: **44/50**
> with no cluster < 6 and floors A/B/C PASS. This run **supersedes** the 2026-06-20 SCORE_INDEP for the
> current draft. Stance: harsh skeptic.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 100 (folds 98) — FINAL_INDEX Ch 42 (CLOSES Part XII)
- **Slug:** `100_governing_ai_ai_review`
- **Title:** Only Policy Can Ship It — Governing AI in the development workflow
- **Part / arc position:** Part XII (closer), Ch 42 of 47
- **Artifact scored:** `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md` (rev 2026-06-27 23:38)
- **Gate reports read:** `_EXAMPLE.md` (BUILT GREEN, 2026-06-27) · `_CODEREVIEW.md` (CODE-REVIEW PASS, 2026-06-27).
  No separate `_VERIFY/_CLARITY/_AUDIT` exist for this chapter — source-trace + neutrality + prose↔code
  fidelity are folded into `_EXAMPLE.md` / `_CODEREVIEW.md`.
- **Companion module:** `08-companion-code/100_governing_ai_ai_review/` — `mvn -B -Pquality verify` GREEN,
  16 tests, 0 Checkstyle / 0 SpotBugs (JDK 21.0.11). Verified against the module source this run.
- **Verified against SOURCE-PIN.md** — pinned 2026-06-20; re-checked 2026-06-28.
- **Scorer:** chapter-scorer (independent model — different from drafter)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds pass applied this run)

---

## Floors checked first (gate the aggregate — not averaged)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan (`better than`, `unlike X`, `the problem with`, `superior`, `beats`, `outperforms`, `blows away`, `no reason to use`, `obvious choice over`, `destroys`, `killer of`): **0 hits** in the draft. "Alternatives & adjacent approaches" is approach-based ("Govern vs ban vs free-for-all"; "AI review vs deterministic tools vs human review — composed, not substituted"). No tool crowned; AI-review tooling note is explicitly "crown none, benchmark for your context." No section title carries a superlative. CODE-REVIEW dim 6 (neutrality-in-code) independently PASS (0 hits in module). |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" (9 bullets) + 3 inline CONCEPT callouts cover every claim with its hardest objection and a when-NOT: policy-without-enforcement-fails (shadow AI); banning-drives-underground; accountability-stays-with-author / no auto-merge; AI-review-cant-verify-intent (empirical ceiling); AI-reviewing-AI-compounds-blind-spots; automation-bias; measure-risk-not-just-productivity (Goodhart); stats-volatile-vendor-flagged; not-legal-advice. No feature sold cost-free. |
| **C — SOURCE-TRACE + COMPILE + CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms in prose or module — no rule IDs / API sigs / GAV / config keys fabricated (correct for a policy chapter). DORA frame is correctly pinned + dated ("2025 DORA report, *State of AI-assisted Software Development*, ~5,000 respondents" = SOURCE-PIN §5 verbatim). All unpinnable AI-era statistics (productivity/risk triad; arXiv 2508.18771 16-tool/~22k-comment/~35% figures; NIST SATE ~50–60%; O'Reilly "half your bugs"; Sonatype attribution) are formally flagged in `09-flags/100_ai_governance_stats_sources_verify_at_pin.md` (raised 2026-06-27) and kept dated+attributed+vendor-hedged in prose — the flagged-to-`09-flags/` path, not invention. **Compile:** `mvn -B -Pquality verify` GREEN per `_EXAMPLE.md`. **Code-review:** PASS per `_CODEREVIEW.md` (6/6 dims). Both displayed tag regions independently re-extracted this run: `AiUsageGate.java#only-policy-can-ship-it` = **9 lines, brace-balanced**; `AiReviewOutcome.java#ai-review-outcomes` = **9 lines, brace-balanced**; both match their prose claims exactly. |

**All three floors PASS.** No floor gates the aggregate.

---

## The five clusters (final — after lift pass 1)

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | Fig 100.1 anchors the governance loop (sanction → assist → same gates + AI-specific → human gate → disclose → measure); each stage earns the next; "intent ceiling approached from both directions" is a genuinely clean structural device. Off 9 only because the deep-dive's core paragraph (¶ at L87) is a dense, full-precision stretch a first-time reader works through. |
| 2 | **ACCURACY** | **8** | After lift pass 1: every numbered cross-ref now resolves correctly against FINAL_INDEX; DORA explicitly pinned+dated; no invented atoms; snippets verified with recorded paths; unpinnable stats hedged + flagged. Capped at 8 (not 9 = "fully traced, zero drift") because load-bearing AI-review figures live behind a `verify-at-pin` flag — real, attributed, but not pinnable in-bounds. |
| 3 | **UTILITY** | **8** | "When to use what" + "Alternatives" give concrete decision frames: tool-vetting criteria, the verification checklist (same gates + SAST/SCA/secrets + mutation-verify + no auto-merge), AI-review patterns (scope/standards/lenses/draft-disposition), the independence rule, the productivity-with-risk counter-metric. Companion module is now BUILT GREEN and runnable (a real lift from the prior PENDING state). |
| 4 | **DEPTH** | **8** | Two earned topics (governance + AI review), each with mechanism + evidence-for + ceiling + limits + alternatives; the symmetric intent-ceiling analysis and the self-as-worked-example close add verified substance, not padding. |
| 5 | **READABILITY** | **8** | Em-dash density **4.03/1000** (well under the 8/1000 bar — the prior score's standing concern is resolved); voice holds (third person, no first person, CONCEPT callouts unstacked); strong concrete hook; clean forward hook into Part XIII. Off 9 only for the one dense paragraph at L87. |

**Cluster subtotal: 40 / 50.** No cluster below 6.

---

## Ship-bar verdict

**LIFT.** Floors A/B/C all PASS, but the aggregate **40/50 < 44/50** (the auto-approval bar in SCORING.md
§"The ship bar"). The chapter is solid, ship-quality work; it is held off the bar by a **structural**
ceiling, not a fixable-in-bounds defect:

- The only concrete prose defect found — a wrong cross-ref ("Policy needs culture (Chapter 1)" → Chapter 4)
  — was **fixed in lift pass 1**, moving ACCURACY 7→8 and the aggregate 39→40.
- The remaining gap to 44 cannot be closed in-bounds: ACCURACY is capped at 8 by **unpinnable** AI-era
  statistics (arXiv 2508.18771, NIST SATE, O'Reilly — SOURCE-PIN §7 canon gaps, correctly flagged), and no
  cluster reaches the "exceptional" 9 anchor. Closing the gap would require inventing pins (forbidden) or
  padding DEPTH (forbidden). Per the brief and the loop rule, I do not manufacture a 9.

**One-line rationale:** 40/50, floors all PASS; one cross-ref defect fixed in pass 1 (ACC 7→8); the residual
gap to 44 is the structural unpinnable-statistics ceiling on ACCURACY, not an in-bounds fix.

---

## Bounded lift loop — log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (this run, pre-lift) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS | LIFT | Independent re-score of the current (2026-06-27) draft. Found one concrete cross-ref defect (L61 "Chapter 1" for culture; FINAL_INDEX places culture at Chapter 4). Em-dash density already at 4.03/1000 (resolved); banned-phrase scan clean; snippets 9/9 lines balanced; DORA correctly pinned. ACCURACY held at 7 for the cross-ref. |
| 1 | 2026-06-28 | **40 / 50** | PASS | PASS | PASS | **LIFT** | **In-bounds fix (ACCURACY):** L61 cross-ref corrected `Chapter 1`→`Chapter 4` (culture = key 06 = Ch 4 per FINAL_INDEX). Prose-only edit; no code touched → no rebuild / no `check_snippets` needed; FLOOR C unchanged. Re-verified all 11 prose cross-refs resolve against FINAL_INDEX. ACCURACY 7→8. Other four clusters unchanged. |

**Loop stopped after pass 1 (within the ≤3 budget):** the next-weakest movement is unavailable in-bounds —
remaining ACCURACY headroom needs pins that cannot be created without invention; CLARITY/READABILITY's only
sub-9 item (the dense L87 paragraph) is at full precision and tightening it would cut substance for cosmetic
sentence-count, which is not a real score gain. No floor risk introduced. Not lowering the bar.

---

## Weakest cluster (flagged)

- **Weakest cluster:** tie at **8** across all five; the binding constraint is **ACCURACY** — it is the one
  cluster with a *named, external* blocker on its path to 9.
- **Why:** ACCURACY cannot reach 9 ("fully traced, zero drift") while load-bearing AI-review statistics
  (arXiv 2508.18771; NIST SATE; O'Reilly) remain SOURCE-PIN §7 canon gaps, correctly carried as flagged,
  dated, attributed `verify-at-pin` atoms.
- **Single highest-leverage move (OUT of in-bounds scope — for the human gate / SOURCE-VERIFY step):** add
  SOURCE-PIN §7 rows for the AI-review study, NIST SATE, and the O'Reilly/Sonatype attributions (the
  deliberate, logged promote-to-fact path). That is the only move that lifts ACCURACY to 9 and the aggregate
  toward the bar — and it is a pin/verify action, not a prose lift, so it cannot be done in the bounded loop.

---

## What this run verified (evidence trail)

- **Em-dash density (prose body, header stripped):** 17 dashes / 4216 words = **4.03/1000 — PASS** (< 8/1000).
- **Banned NEUTRALITY phrases:** 0 hits (case-insensitive grep over the full draft).
- **Cross-refs vs FINAL_INDEX:** all 11 prose `Chapter NN` pointers resolve correctly after the L61 fix
  (Ch 4 culture · Ch 15–17 static analysis · Ch 19 false positives · Ch 23 coverage/mutation · Ch 28 SCA/SBOM ·
  Ch 30 secure coding · Ch 31 SAST+secrets · Ch 34 PR automation · Ch 37 code review · Ch 38 metrics/DORA ·
  "Parts IV–IX" spans the gate stack correctly). "Last/previous chapter" consistently = Ch 41; "Part XIII"
  hand-off correct.
- **Displayed snippets:** both tag regions re-extracted = exactly 9 lines, brace-balanced (3 `if` blocks
  each); prose claims at L54 / L72 match the code in order.
- **DORA statistic:** "2025 DORA report (*State of AI-assisted Software Development*, ~5,000 respondents)" =
  SOURCE-PIN §5 row verbatim — correctly pinned + dated. Chapter 38 pointer for the metrics chapter correct.
- **Stat-flag discipline:** `09-flags/100_ai_governance_stats_sources_verify_at_pin.md` exists (2026-06-27)
  and formally carries all 8 unpinnable atoms as `verify-at-pin`; module bakes in zero statistics.

---

## Learnings & pipeline suggestions

1. **Re-score the current draft, never trust a stale SCORE_INDEP.** The prior independent score (2026-06-20,
   38/50) read a draft state two of whose three blockers were already resolved by the 2026-06-27 revision
   (DORA now explicitly cited + dated; em-dash density already at 4.03/1000). A scorecard is only valid
   against the draft mtime it was run on; the reporting step should tie each SCORE_INDEP to the draft hash/mtime
   so a reviewer can see at a glance whether it is current. **Recommend:** the scorer records the draft mtime
   in the header (done here) and the status tooling flags a SCORE_INDEP older than its draft as STALE.

2. **Dossier-key shorthand leaking into prose as a chapter number is a recurring, greppable cross-ref bug.**
   The L61 defect was "Ch 1 (06)" (dossier-key shorthand) printed as "Chapter 1" when key 06 maps to
   **Chapter 4** in FINAL_INDEX. This class is mechanically detectable: every prose `Chapter NN` should be
   cross-checked against FINAL_INDEX's chapter→topic map, and any chapter whose topic does not match the
   sentence's subject is a likely key/number confusion. **Recommend:** add a `lint_crossrefs` check (prose
   `Chapter NN` vs FINAL_INDEX topic) to the CLARITY/VERIFY pre-pass so the drafter catches it before scoring.

3. **A correctly-flagged unpinnable statistic structurally caps ACCURACY at 8, and that is the right outcome.**
   AI-era chapters lean on real-but-vendor/preprint figures that are not pinnable without a deliberate §7
   re-pin. The chapter does everything right (dated, attributed, hedged, flagged, zero-baked-into-code), and
   the rubric still — correctly — withholds the 9 that requires "fully traced, zero drift." This means an
   AI-statistics-heavy chapter's realistic ceiling is ~40–43 until SOURCE-VERIFY promotes the §7 rows.
   **Recommend:** treat "§7 rows pinned" as a SOURCE-VERIFY pre-req gate before the *final* approval score on
   any AI-era chapter, so the chapter is not perpetually held at LIFT by a gap the prose cannot close.

*(Append to `00-strategy/PIPELINE-LEARNINGS.md`:)*

> **2026-06-28 · Ch 42 (key 100/98) independent re-score:** Current draft (rev 2026-06-27) = 40/50, floors
> A/B/C PASS, verdict LIFT. One in-bounds pass fixed a dossier-key→chapter-number cross-ref leak
> (L61 "Chapter 1"→"Chapter 4" for culture/key 06), ACC 7→8, aggregate 39→40. Em-dash density already
> resolved (4.03/1000) and DORA already correctly pinned+dated in the current draft — two of the prior
> score's three blockers were stale. Residual gap to 44 is structural: ACCURACY capped at 8 by unpinnable,
> correctly-flagged AI-era statistics (arXiv 2508.18771, NIST SATE, O'Reilly = §7 canon gaps). The
> bar-clearing move (add §7 pins) is a SOURCE-VERIFY/human-gate action, not an in-bounds prose lift.
> Recommend: (a) tie each SCORE_INDEP to its draft mtime + flag stale ones; (b) add a prose-`Chapter NN`
> vs FINAL_INDEX cross-ref lint; (c) require §7 AI-era rows pinned before the final approval score.
