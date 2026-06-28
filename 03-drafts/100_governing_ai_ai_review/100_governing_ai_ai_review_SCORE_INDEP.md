# INDEPENDENT SCORECARD — Ch 42 (`100_governing_ai_ai_review`) — harsh-skeptic re-score

> Independent (different-model) score of the **current** printed draft
> `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`. Target bar: **44/50** with no
> cluster < 6 and floors A/B/C PASS. This run **supersedes** the prior SCORE_INDEP. Stance: harsh skeptic.
>
> **What changed since the prior (40/50) score, and why it moves ACCURACY:** the prior independent score
> capped ACCURACY at 8 for a single, explicitly-named reason — "load-bearing AI-review figures live behind a
> `verify-at-pin` flag" (arXiv:2508.18771, "NIST SATE", O'Reilly), called SOURCE-PIN §7 canon gaps. All three
> are now **resolved**: arXiv:2508.18771, the O'Reilly/Stellman ceiling, and the Sonatype/Fox thesis are
> **verbatim-pinned SOURCE-PIN §8 rows** (web-verified, dated), the draft cites §8, the unsourced ~35% figure
> is **cut** from the prose, and the "NIST SATE" mis-attribution is gone (the 50–60% figure is now sourced to
> the pinned Stellman row). The prior score's sole named ACCURACY blocker is removed.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 100 (folds 98) — FINAL_INDEX Ch 42 (CLOSES Part XII)
- **Slug:** `100_governing_ai_ai_review`
- **Title:** Only Policy Can Ship It — Governing AI in the development workflow
- **Part / arc position:** Part XII (closer), Ch 42 of 47
- **Artifact scored:** `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (BUILT GREEN, 2026-06-27) · `_CODEREVIEW.md` (CODE-REVIEW PASS, 6/6,
  2026-06-27). No separate `_VERIFY/_CLARITY/_AUDIT` exist — source-trace + neutrality + prose↔code fidelity
  are folded into `_EXAMPLE.md` / `_CODEREVIEW.md`.
- **Companion module:** `08-companion-code/100_governing_ai_ai_review/` — `mvn -B -Pquality verify` GREEN,
  16 tests, 0 Checkstyle / 0 SpotBugs (JDK 21.0.11). Both displayed tag regions re-extracted this run.
- **Verified against SOURCE-PIN.md** — pinned 2026-06-20; §8 AI-era rows web-verified + pinned 2026-06-28;
  re-checked this run.
- **Scorer:** chapter-scorer (independent model — different from drafter)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (no in-bounds pass available this run — see lift-loop log)

---

## Floors checked first (gate the aggregate — not averaged)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent banned-phrase scan (`better than`, `unlike X`, `the problem with`, `superior`, `beats`, `outperforms`, `blows away`, `no reason to use`, `obvious choice over`, `destroys`, `killer of`, `leaves … in the dust`): **0 hits** in the draft body. "Alternatives & adjacent approaches" is approach-based ("Govern vs ban vs free-for-all"; "AI review vs deterministic tools vs human review — composed, not substituted"). No tool crowned; the AI-review tooling note is explicitly "crown none, benchmark for your context." No section title carries a superlative. CODE-REVIEW dim 6 (neutrality-in-code) independently PASS (0 hits in module). |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" (9 bullets) + 3 inline CONCEPT callouts cover every claim with its hardest objection + a when-NOT: policy-without-enforcement-fails (shadow AI); banning-drives-underground; accountability-stays-with-author / no auto-merge; AI-review-cant-verify-intent (empirical ceiling, ~half); AI-reviewing-AI-compounds-blind-spots; automation-bias; measure-risk-not-just-productivity (Goodhart); stats-volatile/vendor-flagged; not-legal-advice. No feature sold cost-free; the chapter's own thesis (governance) carries its failure mode (shadow AI) explicitly. |
| **C — SOURCE-TRACE + COMPILE + CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms in prose or module — no rule IDs / API sigs / GAV / config keys / version numbers fabricated. Every load-bearing AI-era fact now traces to a **pinned SOURCE-PIN row**: the AI-review trio to **§8** (arXiv:2508.18771v2 = "16 … review actions … more than 22,000 review comments in 178 repositories"; "effectiveness varies widely" — verbatim; Stellman/O'Reilly 2026-04-30 = "around half" + "50–60% detection rates" + implementation-vs-design split + "what the developer intended it to do" — all four verbatim; Sonatype/Fox 2025-11-04 = thesis title verbatim) and the DORA frame to **§5** ("2025 DORA report, *State of AI-assisted Software Development*, ~5,000 respondents"). The unsourced ~35%/single-digit figures are **cut** (appear only in the HTML header noting the cut, never in prose). Residual `verify-at-pin` items (productivity/risk 78/72/65; privacy-scorecard 2509.20388; EU-AI-Act specifics) are **not asserted as hard figures** — the prose hedges to ranges ("around three-quarters", "around two-thirds") and pins the *frame* to DORA §5; all are formally flagged in `09-flags/100_ai_governance_stats_sources_verify_at_pin.md`. That is the dated-snapshot path, not invention. **Compile:** `mvn -B -Pquality verify` GREEN per `_EXAMPLE.md` + `_CODEREVIEW.md`. **Code-review:** PASS, 6/6 dims, per `_CODEREVIEW.md`. **Snippets re-extracted this run:** `AiUsageGate.java#only-policy-can-ship-it` = **9 lines, 3/3 braces balanced**; `AiReviewOutcome.java#ai-review-outcomes` = **9 lines, 3/3 braces balanced**; both match their prose claims (L54 controls-in-order; L72 outcomes-in-order) exactly. |

**All three floors PASS.** No floor gates the aggregate.

---

## The five clusters

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | Fig 42.1 anchors the governance loop (sanction → assist → same gates + AI-specific → human gate → disclose → measure); each stage earns the next; the "intent ceiling approached from both directions" (generation infers from a prompt, review infers from the code, both bounded by the same wall) is a genuinely clean structural device. Off 9 only because the deep-dive's core paragraph (L87) is a dense full-precision stretch a first-time reader works through. |
| 2 | **ACCURACY** | **9** | Lifted 8→9 from the prior score: the named blocker is gone. Every load-bearing fact now traces to a pinned row — AI-review trio to **§8** (verbatim quotes confirmed), DORA frame to **§5** — with zero invented atoms and zero drift; the unsourced 35% figure is cut and the NIST-SATE mis-attribution removed. Snippets verified with recorded paths (9 lines, brace-balanced). The only un-pinned statistics (productivity/risk triad) are correctly **not asserted** — hedged to ranges + flagged — which is right handling, not a traceability gap. Meets the "fully traced, snippets verified, zero drift" 9 anchor. |
| 3 | **UTILITY** | **8** | "When to use what" + "Alternatives" give concrete decision frames: tool-vetting criteria (does code leave the org? / IP / privacy), the verification checklist (same gates + SAST/SCA/secrets + mutation-verify + no auto-merge), AI-review patterns (scope/standards/lenses/draft-disposition), the independence rule, the productivity-with-risk counter-metric. Companion module BUILT GREEN and runnable. Off 9 because the guidance, while concrete, is policy-level and a reader still translates it to their own toolchain. |
| 4 | **DEPTH** | **8** | Two earned topics (governance + AI review), each with mechanism + evidence-for + ceiling + limits + alternatives; the symmetric intent-ceiling analysis and the self-as-worked-example close add verified substance, not padding. Off 9 because the second half (AI review) leans on two pinned studies rather than the contested, multi-source richness a 9 ("rich, contested, foundational") wants. |
| 5 | **READABILITY** | **8** | Em-dash density **4.51/1000** (independently measured; well under the 8/1000 bar); voice holds (third person, no first-person narration — the one "I" is inside a sanctioned quoted string; CONCEPT callouts unstacked); strong concrete hook (the post-mortem non-defense); clean forward hook into Part XIII. Off 9 only for the one dense paragraph at L87. |

**Cluster subtotal: 41 / 50.** No cluster below 6.

---

## Ship-bar verdict

**LIFT.** Floors A/B/C all PASS, but the aggregate **41/50 < 44/50** (the auto-approval bar in SCORING.md
§"The ship bar"). The chapter is solid, ship-quality work, and is meaningfully closer to the bar than the
prior score — the §8 pinning lifted ACCURACY 8→9 and the aggregate 40→41. The residual 3-point gap cannot
be closed in-bounds:

- **The prior structural ceiling is gone.** ACCURACY is no longer capped — the AI-review trio and the DORA
  frame are now pinned (§8/§5), the unsourced 35% figure is cut, and the NIST-SATE mis-attribution is
  removed. ACCURACY legitimately reaches 9.
- **The remaining gap is the four clusters at 8, none reaching 9 in-bounds.** CLARITY and READABILITY share
  one sub-9 cause (the dense L87 paragraph at full precision — tightening it trades substance for
  sentence-count, not a real gain). UTILITY is policy-level by topic; DEPTH leans on two pinned studies, not
  a contested multi-source field. Lifting any of these to 9 would require padding (forbidden), scope creep
  (forbidden), or new unverified material (forbidden). Per the loop rule I do not manufacture a 9.

**One-line rationale:** 41/50, floors all PASS; ACCURACY lifted 8→9 because the §8 pins resolved the prior
score's sole named blocker (AI-review trio pinned, 35% cut, NIST-SATE mis-attribution removed); residual gap
to 44 is four clusters at a solid-but-not-exceptional 8 with no in-bounds path to 9.

---

## Bounded lift loop — log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (this run) | 2026-06-28 | **41 / 50** | PASS | PASS | PASS | **LIFT** | Independent re-score of the current draft after the SOURCE-PIN §8 update. Verified: AI-review trio (arXiv:2508.18771, Stellman/O'Reilly, Sonatype/Fox) now verbatim-pinned in §8 and cited in the draft; DORA frame pinned §5; the unsourced ~35% figure cut from prose (header-only note); NIST-SATE mis-attribution removed (50–60% now sourced to Stellman). Banned-phrase scan 0 hits; em-dash density 4.51/1000; both snippets 9 lines / brace-balanced. **ACCURACY 8→9** vs the prior 40/50 score (its sole named cap is resolved). No prose defect found this run. |

**No lift pass applied (within the ≤3 budget):** no in-bounds move raises the aggregate. ACCURACY is already
at its ceiling (9). The four 8-clusters cannot reach 9 without padding / scope creep / unverified material
(all forbidden), and the only sub-9 prose item (the dense L87 paragraph) is at full precision — cutting it
trades verified substance for cosmetic sentence-count, which is not a score gain. No floor risk introduced.
Not lowering the bar.

---

## Weakest cluster (flagged)

- **Weakest cluster:** four-way tie at **8** (CLARITY, UTILITY, DEPTH, READABILITY); ACCURACY is at 9.
- **Highest-leverage genuine lift (OUT of in-bounds scope — for the human gate):** the chapter is held at 41
  by the absence of any 9 outside ACCURACY, and the cleanest real lift is **DEPTH**: a third pinned AI-review
  source (or pinning the privacy-scorecard arXiv 2509.20388 and the productivity/risk vendor body, flag item
  1/6) would let the AI-review half present the *contested, multi-source* picture a DEPTH 9 wants, and would
  also let the productivity figures be asserted as pinned facts rather than hedged ranges (a second small
  ACCURACY/UTILITY lever). That is a SOURCE-VERIFY/pin action (add §8 rows), not an in-bounds prose lift, so
  it cannot be done in the bounded loop. Absent that, 41/50 is the honest, defensible ceiling for the current
  pinned source set.

---

## What this run verified (evidence trail)

- **Banned NEUTRALITY phrases:** 0 hits (case-insensitive grep over the full draft body).
- **Em-dash density (body, HTML header stripped):** 20 dashes / 4438 words = **4.51/1000 — PASS** (< 8/1000).
- **First-person narration:** none — the single "I" is inside the sanctioned quoted string "I copied it from
  Stack Overflow" (VOICE-GUIDE permits contractions/first-person inside a quotation); narration holds third
  person throughout.
- **AI-review trio vs SOURCE-PIN §8:** arXiv:2508.18771v2 ("16 … review actions … 22,000 review comments …
  178 repositories"; "effectiveness varies widely") — draft L71/L96 match the §8 row verbatim. Stellman/
  O'Reilly 2026-04-30 ("around half"; "50–60% detection rates"; implementation-vs-design 50/50; "what the
  developer intended it to do") — all four quotes match the §8 row. Sonatype/Fox 2025-11-04 thesis title —
  matches the §8 row. **All load-bearing AI-review facts pinned.**
- **35% figure:** **cut** — appears only in the HTML comment header (L9/L10/L130 noting the cut), never in
  prose. NIST-SATE mis-attribution: **absent** from the draft; 50–60% now sourced to Stellman (§8).
- **DORA statistic:** "2025 DORA report (*State of AI-assisted Software Development*, ~5,000 respondents)" =
  SOURCE-PIN §5 row verbatim — correctly pinned + dated. Productivity/risk percentages NOT asserted as hard
  figures (hedged to "around three-quarters"/"around two-thirds"); flagged item 1.
- **Displayed snippets:** both tag regions re-extracted from the module source = exactly 9 lines,
  brace-balanced (3 `if` blocks each); prose claims at L54 (disclosure → accountable human → no-auto-merge)
  and L72 (real catch → false positive → missed bug) match the code in order.
- **FLOOR C compile/code-review:** GREEN + PASS (6/6) per `_EXAMPLE.md` / `_CODEREVIEW.md`; module bakes in
  zero statistics (caller-supplied numbers only).

---

## Learnings & pipeline suggestions

1. **Pinning a flagged source materially moves the score, and the rubric rewards it correctly.** The prior
   independent score (40/50) named exactly one ACCURACY cap: the AI-review figures behind a `verify-at-pin`
   flag. The 2026-06-28 §8 pin (web-verified arXiv:2508.18771, Stellman/O'Reilly, Sonatype/Fox) closed that
   cap, lifting ACCURACY 8→9 and the aggregate 40→41 with **no prose change** — the lift came entirely from
   the source-pin step. **Recommend:** when a chapter is held at LIFT by a named "unpinnable statistic"
   cap, route the SOURCE-VERIFY/pin action as the next step rather than re-running the prose lift loop; the
   loop cannot move that cluster, only the pin can.

2. **Cutting an unsourceable figure is the right move and does not cost a cluster.** The ~35% / single-digit
   figures had no locatable primary; cutting them (softening to "around half" / "varies widely", both pinned)
   kept ACCURACY clean rather than carrying a hedged-but-asserted unpinned number. The chapter is *more*
   accurate for asserting less. **Recommend:** keep "cut, don't hedge, an unsourceable figure" as the
   standing rule for AI-era chapters (it is implicit in GUIDELINES rule 3 / the folklore rule — worth an
   explicit line in PIPELINE-LEARNINGS for the AI-statistics class).

3. **An AI-era chapter's realistic ceiling rises as its §8 rows fill, but the last few points need contested
   depth, not more pins.** Even fully pinned, this chapter sits at 41: the AI-review half rests on two
   studies, so DEPTH stays at 8 ("not yet contested/multi-source"). The path to 44 is a *richer* AI-review
   evidence base (more pinned primaries), which lifts DEPTH and lets the productivity figures be asserted —
   a SOURCE-VERIFY action, not a prose lift. **Recommend:** treat "≥3 pinned primaries on the load-bearing
   AI claim" as the practical DEPTH-9 pre-req for AI-era chapters, logged before the final approval score.

*(Append to `00-strategy/PIPELINE-LEARNINGS.md`:)*

> **2026-06-28 · Ch 42 (key 100/98) independent re-score:** Current draft = **41/50**, floors A/B/C PASS,
> verdict **LIFT** (bar 44). ACCURACY lifted **8→9** vs the prior 40/50 score because the SOURCE-PIN §8
> update web-verified + pinned the AI-review trio (arXiv:2508.18771 "16 tools/22k comments/178 repos/
> 'effectiveness varies widely'"; Stellman/O'Reilly "around half" + "50–60%"; Sonatype/Fox thesis), the
> draft now cites §8, the unsourced ~35% figure was cut from prose, and the NIST-SATE mis-attribution was
> removed (50–60% re-sourced to Stellman). No prose defect found; the lift came from the pin step alone.
> Residual gap to 44 is four clusters at a solid 8 (CLARITY/READABILITY share the one dense L87 paragraph;
> UTILITY is policy-level; DEPTH rests on two studies) with no in-bounds path to 9. The bar-clearing move is
> a *richer pinned AI-review evidence base* (≥3 primaries + pin the productivity/risk vendor body / privacy
> scorecard 2509.20388) — a SOURCE-VERIFY action, not a bounded-loop prose lift. Learning: a named
> "unpinnable-statistic" ACCURACY cap is closed by the pin step, not the lift loop — route it there.
