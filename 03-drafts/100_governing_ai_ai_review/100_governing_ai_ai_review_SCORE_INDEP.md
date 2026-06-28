# INDEPENDENT SCORECARD — Ch 42 (`100_governing_ai_ai_review`) — harsh-skeptic re-score (post in-bounds lift)

> Independent (different-model) score of the **current** printed draft
> `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`, **after one bounded in-bounds lift
> pass** applied this run. Target bar: 44/50 (auto-approval) with no cluster < 6 and floors A/B/C PASS; this
> run's lift target was the **84% / 42-of-50** ship line. This score **supersedes** the prior SCORE_INDEP
> (41/50). Stance: harsh skeptic.
>
> **What changed since the prior (41/50) score, and why it moves CLARITY + READABILITY:** the prior
> independent score capped *both* CLARITY (8) and READABILITY (8) on one shared cause — "the dense L87
> paragraph at full precision" — and judged it un-liftable ("tightening it trades substance for
> sentence-count"). That judgement was wrong on inspection: L87 carried a **removable density defect**, not
> irreducible substance. It opened its trade-off with a banned `There is`-style meta-scaffold ("There is a
> trade-off a senior team has to weigh here, and it is uncomfortable:" — narrator announcing the point, which
> VOICE-GUIDE §"State facts as facts" / §"Excise self-narration" / the `there is/there are`-opener filler ban
> all forbid), said the same trade-off across **two** sentences ("AI makes everything around … did not get
> cheaper" + "The cheaper the generation and review become … per reviewer-hour"), and chained three
> appositive em-dashes. One in-bounds tightening pass removed the scaffold, collapsed the duplicated sentence,
> and pruned the appositives **with zero loss of verified substance** — every fact (the intent ceiling from
> both directions, the wall/intent-outside-the-code, author-has-intent vs reviewer-reconstructs, the
> structurally-human job, the trade-off, the bottleneck, the sharpening) survives. That removed the single
> shared sub-9 cause, lifting CLARITY 8→9 and READABILITY 8→9.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 100 (folds 98) — FINAL_INDEX Ch 42 (CLOSES Part XII)
- **Slug:** `100_governing_ai_ai_review`
- **Title:** Only Policy Can Ship It — Governing AI in the development workflow
- **Part / arc position:** Part XII (closer), Ch 42 of 47
- **Artifact scored:** `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md` (post-lift)
- **Gate reports read:** `_EXAMPLE.md` (BUILT GREEN, 2026-06-27) · `_CODEREVIEW.md` (CODE-REVIEW PASS, 6/6,
  2026-06-27). No separate `_VERIFY/_CLARITY/_AUDIT` exist — source-trace + neutrality + prose↔code fidelity
  are folded into `_EXAMPLE.md` / `_CODEREVIEW.md`.
- **Companion module:** `08-companion-code/100_governing_ai_ai_review/` — `mvn -B -Pquality verify` GREEN,
  16 tests, 0 Checkstyle / 0 SpotBugs (JDK 21.0.11). Both displayed tag regions unchanged by the lift (the
  edit touched §"Deep dive" prose only — neither `#only-policy-can-ship-it` nor `#ai-review-outcomes` is in
  that section).
- **Verified against SOURCE-PIN.md** — pinned 2026-06-20; §8 AI-era rows web-verified + pinned 2026-06-28.
- **Scorer:** chapter-scorer (independent model — different from drafter)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 of ≤3 (one bounded in-bounds pass applied this run — see lift-loop log).

---

## Floors checked first (gate the aggregate — not averaged)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent banned-phrase scan (`better than`, `unlike X`, `the problem with`, `superior`, `beats`, `outperforms`, `blows away`, `no reason to use`, `obvious choice over`, `destroys`, `killer of`, `leaves … in the dust`): **0 hits** in the draft body (re-run post-edit). "Alternatives & adjacent approaches" stays approach-based ("Govern vs ban vs free-for-all"; "AI review vs deterministic tools vs human review — composed, not substituted"). No tool crowned; the tooling note is "crown none, benchmark for your context." No section title carries a superlative. CODE-REVIEW dim 6 (neutrality-in-code) independently PASS. The lift edit introduced no comparative phrasing. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" (9 bullets) + 3 inline CONCEPT callouts cover every claim with its hardest objection + when-NOT: policy-without-enforcement-fails (shadow AI); banning-drives-underground; accountability-stays-with-author / no auto-merge; AI-review-cant-verify-intent (empirical ~half ceiling); AI-reviewing-AI-compounds-blind-spots; automation-bias; measure-risk-not-just-productivity (Goodhart); stats-volatile/vendor-flagged; not-legal-advice. No feature sold cost-free; the chapter's own thesis (governance) carries its failure mode (shadow AI). Untouched by the lift. |
| **C — SOURCE-TRACE + COMPILE + CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms in prose or module; the lift added no fact. Every load-bearing AI-era fact traces to a **pinned SOURCE-PIN row**: the AI-review trio to **§8** (arXiv:2508.18771v2 "16 … review actions … 22,000 review comments … 178 repositories" / "effectiveness varies widely" — verbatim; Stellman/O'Reilly 2026-04-30 "around half" + "50–60% detection rates" + implementation-vs-design split + "what the developer intended it to do" — verbatim; Sonatype/Fox 2025-11-04 thesis title — verbatim) and the DORA frame to **§5** ("2025 DORA report, *State of AI-assisted Software Development*, ~5,000 respondents"). The unsourced ~35%/single-digit figures are **cut** (HTML-header note only, never prose). Residual `verify-at-pin` items (productivity/risk 78/72/65; privacy-scorecard 2509.20388; EU-AI-Act specifics) are **not asserted as hard figures** — hedged to ranges ("around three-quarters", "around two-thirds"), frame pinned to DORA §5, all flagged in `09-flags/100_ai_governance_stats_sources_verify_at_pin.md`. **Compile:** `mvn -B -Pquality verify` GREEN per `_EXAMPLE.md` + `_CODEREVIEW.md`. **Code-review:** PASS, 6/6 dims. **Snippets (unchanged by the lift):** `AiUsageGate.java#only-policy-can-ship-it` = 9 lines, brace-balanced; `AiReviewOutcome.java#ai-review-outcomes` = 9 lines, brace-balanced; both still match their prose claims (L54 controls-in-order; L72 outcomes-in-order). Both `<!-- include: -->` markers verified present (lines 57, 75). |

**All three floors PASS.** No floor gates the aggregate.

---

## The five clusters

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | **Lifted 8→9.** Fig 42.1 anchors the governance loop (sanction → assist → same gates + AI-specific → human gate → disclose → measure); the "intent ceiling approached from both directions" (generation infers from a prompt, review infers from the code, both at one wall) is a clean structural device; and the deep-dive's core paragraph — the *sole* reason the prior score held this at 8 — is now scaffold-free and linearly ordered, each step earning the next. A reader who never met the topic can reconstruct the governance mechanism and *why* the human gate is irreducible from the chapter alone. Meets the 9 anchor. |
| 2 | **ACCURACY** | **9** | Unchanged from the prior 9 (a prose-tightening edit cannot move traceability). Every load-bearing fact traces to a pinned row — AI-review trio to **§8** (verbatim), DORA frame to **§5** — zero invented atoms, zero drift; the unsourced 35% figure cut, the NIST-SATE mis-attribution removed. Snippets verified with recorded paths (9 lines, brace-balanced). The only un-pinned statistics (productivity/risk triad) are correctly **not asserted** — hedged + flagged, which is right handling, not a traceability gap. Meets the "fully traced, snippets verified, zero drift" 9 anchor. |
| 3 | **UTILITY** | **8** | Unchanged. "When to use what" + "Alternatives" give concrete decision frames: tool-vetting (does code leave the org? / IP / privacy), the verification checklist (same gates + SAST/SCA/secrets + mutation-verify + no auto-merge), AI-review patterns (scope/standards/lenses/draft-disposition), the independence rule, the productivity-with-risk counter-metric. Module BUILT GREEN and runnable. Honestly 8, not 9: the guidance is policy-level and a reader still translates it to their own toolchain — and lifting it to 9 has no in-bounds path (a decision-table recap would be padding; a richer concrete frame would need the un-pinned figures asserted, an out-of-bounds SOURCE-VERIFY action). |
| 4 | **DEPTH** | **8** | Unchanged. Two earned topics (governance + AI review), each with mechanism + evidence-for + ceiling + limits + alternatives; the symmetric intent-ceiling analysis and the self-as-worked-example close add verified substance, not padding. Honestly 8, not 9: the AI-review half rests on two pinned studies rather than the contested, multi-source richness a 9 ("rich, contested, foundational") wants — DEPTH-9 needs ≥3 pinned primaries, a SOURCE-VERIFY action, not an in-bounds prose lift. |
| 5 | **READABILITY** | **9** | **Lifted 8→9.** The shared sub-9 cause is fixed: a banned `There is`-style meta-scaffold removed ("There is a trade-off … to weigh here, and it is uncomfortable:" → "The trade-off a senior team weighs here is uncomfortable."), one duplicated trade-off sentence collapsed, three appositive em-dashes pruned. Voice holds (third person; the only "we" is inside the sanctioned quoted rhetorical question "is this what we actually want?"); em-dash density well under the 8/1000 bar; strong concrete hook (the post-mortem non-defense); clean forward hook into Part XIII. The prose now reads effortlessly at full precision. Meets the 9 anchor. |

**Cluster subtotal: 43 / 50.** No cluster below 6.

---

## Ship-bar verdict

**LIFT** against the book's 44/50 auto-approval bar — but the chapter now **clears the 84% / 42-of-50 ship
line (43/50)**, the target this run was asked to reach, with a point of margin. Floors A/B/C all PASS.

- **The lift was real and in-bounds.** One bounded pass removed a genuine density defect in the deep-dive's
  core paragraph (a banned `There is`-opener meta-scaffold + a duplicated trade-off sentence + three
  appositive em-dashes), with **no new fact, no padding (the paragraph is net shorter), no scope creep, and
  no floor risk**. The prior score's claim that this paragraph could only be "tightened by trading substance
  for sentence-count" was incorrect — the cut material was scaffolding and repetition, not substance. CLARITY
  and READABILITY both legitimately reach 9.
- **The residual gap to 44 is honestly un-closeable in-bounds.** ACCURACY is at its 9 ceiling. UTILITY (8) is
  policy-level by topic; DEPTH (8) rests on two pinned studies. Lifting either to 9 would require padding
  (forbidden), scope creep (forbidden), or new material — specifically a *richer pinned AI-review evidence
  base* (≥3 primaries, and/or pinning the productivity/risk vendor body and privacy-scorecard 2509.20388),
  which is a SOURCE-VERIFY/pin action, **not** a bounded-loop prose lift. Per the loop rule I do not
  manufacture a 9, and I stop the loop rather than force a second/third pass that no honest in-bounds move
  supports.

**One-line rationale:** 43/50, floors all PASS; CLARITY + READABILITY lifted 8→9 by one in-bounds tightening
pass that removed a real density defect (banned `There is` meta-scaffold + duplicated trade-off sentence +
appositive em-dashes) from the deep-dive core with zero substance lost; clears the 84%/42 ship line;
residual gap to the 44 auto-approval bar is UTILITY/DEPTH at a solid 8, liftable only by an out-of-bounds
SOURCE-VERIFY action.

---

## Bounded lift loop — log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (prior run) | 2026-06-28 | 41 / 50 | PASS | PASS | PASS | LIFT | Re-score after the SOURCE-PIN §8 update lifted ACCURACY 8→9 (40→41). Named CLARITY+READABILITY 8 on one shared cause ("dense L87 paragraph") and judged it un-liftable. |
| **1 (this run)** | 2026-06-28 | **43 / 50** | PASS | PASS | PASS | **LIFT (≥42 ship line cleared)** | **One in-bounds tightening pass on the deep-dive core (§"Deep dive", the intent-ceiling paragraph):** removed the banned `There is`-style meta-scaffold opener of the trade-off ("There is a trade-off a senior team has to weigh here, and it is uncomfortable:" → "The trade-off a senior team weighs here is uncomfortable."), collapsed the duplicated trade-off into one sentence ("AI makes everything *around* the intent-check cheap and fast, so more code arrives per reviewer-hour while the entire residual risk concentrates onto the one step that did not get cheaper."), and pruned three appositive em-dashes. **Zero facts added or removed; net shorter; floors unchanged; snippets/tag-regions/includes untouched (the edit is in §"Deep dive" only).** **CLARITY 8→9, READABILITY 8→9** (the prior score's single shared sub-9 cause is gone). ACCURACY/UTILITY/DEPTH unchanged. Banned-phrase scan re-run: 0 hits; include markers verified present (lines 57, 75). |

**Loop stopped after pass 1 (within the ≤3 budget):** the only remaining sub-9 clusters are UTILITY and
DEPTH, and neither has an in-bounds path to 9 — a UTILITY decision-aid recap would be padding, and DEPTH-9
needs ≥3 pinned primaries (a SOURCE-VERIFY action). No floor risk introduced. Not lowering the bar. The 84% /
42 target is met at 43/50; the 44 auto-approval bar is not reachable in-bounds and is left for the
SOURCE-VERIFY action below.

---

## Weakest cluster (flagged)

- **Weakest cluster:** two-way tie at **8** (UTILITY, DEPTH); CLARITY/ACCURACY/READABILITY at 9.
- **Highest-leverage genuine lift (OUT of in-bounds scope — for the human gate / SOURCE-VERIFY):** the cleanest
  real path to 44 is **DEPTH** — a third pinned AI-review primary (and/or pinning the privacy-scorecard arXiv
  2509.20388 and the productivity/risk vendor body, flag item 1/6) would let the AI-review half present the
  *contested, multi-source* picture a DEPTH 9 wants, and would also let the productivity figures be asserted
  as pinned facts rather than hedged ranges (a second small UTILITY/ACCURACY lever). That is a SOURCE-VERIFY/
  pin action (add §8 rows), not an in-bounds prose lift, so it is out of the bounded loop. Absent it, **43/50
  is the honest in-bounds ceiling** for the current pinned source set — which clears the 42 ship line but not
  the 44 auto-approval bar.

---

## What this run verified (evidence trail)

- **The lift edit, scoped:** confined to §"Deep dive: the human gate is the invariant" (the intent-ceiling
  paragraph). No change to the hook, Overview, "How it works", the CONCEPT callouts, the two snippet
  regions, "Limitations", "Alternatives", "When to use what", the hand-off, or the back matter.
- **Includes / tag regions:** both `<!-- include: -->` markers present and unchanged (lines 57, 75 — the
  `AiUsageGate#only-policy-can-ship-it` and `AiReviewOutcome#ai-review-outcomes` regions); the displayed
  snippets are outside the edited section and remain 9 lines / brace-balanced.
- **Banned NEUTRALITY phrases:** 0 hits (case-insensitive scan over the full post-edit draft body).
- **First-person narration:** none — the single "we" in the edited paragraph is inside the sanctioned quoted
  rhetorical question "is this what we actually want?" (VOICE-GUIDE permits first person inside a quotation);
  the elsewhere-sanctioned "I copied it from Stack Overflow" quote is unchanged; narration holds third person.
- **Em-dash density:** still well under the 8/1000 bar (the lift *removed* three appositive em-dashes; total
  body em-dash count did not rise; word count fell, so density is ≤ the prior 4.51/1000).
- **AI-review trio vs SOURCE-PIN §8:** unchanged and still verbatim-matched (arXiv:2508.18771v2; Stellman/
  O'Reilly 2026-04-30; Sonatype/Fox 2025-11-04). DORA frame = §5 verbatim. The lift touched none of these.
- **35% figure / NIST-SATE:** still cut / absent from prose (header note only); 50–60% sourced to Stellman.
- **FLOOR C compile/code-review:** GREEN + PASS (6/6) per `_EXAMPLE.md` / `_CODEREVIEW.md`; module bakes in
  zero statistics (caller-supplied numbers only). The prose lift does not touch the module.

---

## Learnings & pipeline suggestions

1. **"Dense paragraph" is not automatically "irreducible substance" — inspect the density before declaring a
   cluster un-liftable.** The prior independent score capped CLARITY and READABILITY at 8 on one paragraph
   and ruled it un-liftable ("trades substance for sentence-count"). On inspection the density was a banned
   `There is`-style meta-scaffold + a duplicated sentence + appositive em-dashes — all VOICE-GUIDE-bannable,
   all removable with zero fact loss. Removing them lifted *two* clusters by one in-bounds pass. **Recommend:**
   before a scorer records "no in-bounds path" for a CLARITY/READABILITY cap rooted in a single dense
   passage, it must first run that passage against the VOICE-GUIDE line-edit checklist (self-narration,
   `there is/there are` openers, one-idea-per-sentence, em-dash appositives); a defect there is an in-bounds
   lift, not irreducible substance. Add this as a step to the chapter-scorer's lift-loop procedure.

2. **A shared sub-9 cause across two clusters is the highest-leverage in-bounds lift available.** When CLARITY
   and READABILITY are both held at 8 by the *same* passage, one tightening pass can move both — a 2-point
   aggregate gain from a single bounded edit. **Recommend:** when scoring, explicitly note when two clusters
   share a single named cause, and target that cause first in the lift loop.

3. **The in-bounds ceiling and the auto-approval bar can legitimately differ.** This chapter's honest in-bounds
   ceiling is 43/50 (clears the 84%/42 ship line) while the 44 auto-approval bar needs an out-of-bounds
   SOURCE-VERIFY action (a richer pinned AI-review evidence base). Recording both — "43 in-bounds; 44 needs a
   pin action, not a prose lift" — keeps the next step honest. **Recommend:** keep flagging the bar-clearing
   move as a SOURCE-VERIFY action when the residual gap is a DEPTH/UTILITY cap that only new pinned primaries
   can close.

*(Append to `00-strategy/PIPELINE-LEARNINGS.md`:)*

> **2026-06-28 · Ch 42 (key 100/98) independent re-score + lift pass 1:** Post-lift draft = **43/50**, floors
> A/B/C PASS, verdict LIFT vs the 44 auto-approval bar but **clears the 84%/42 ship line**. CLARITY +
> READABILITY lifted **8→9** by one bounded in-bounds pass on the §"Deep dive" intent-ceiling paragraph:
> removed a banned `There is`-style meta-scaffold opener of the trade-off, collapsed a duplicated trade-off
> sentence, pruned three appositive em-dashes — zero facts changed, net shorter, floors/snippets/includes
> untouched. The prior 41/50 score had named that paragraph as the shared sub-9 cause but wrongly judged it
> un-liftable ("trades substance for sentence-count"); the cut material was scaffolding + repetition, not
> substance. ACCURACY stays 9 (§8/§5 pins), UTILITY/DEPTH stay 8 (policy-level / two studies). Residual gap
> to 44 is a DEPTH cap closable only by a richer pinned AI-review base (≥3 primaries; pin productivity/risk
> vendor body + privacy scorecard 2509.20388) — a SOURCE-VERIFY action, not a prose lift. **Learning: a
> CLARITY/READABILITY cap rooted in one dense passage must be run against the VOICE-GUIDE line-edit checklist
> before a scorer declares "no in-bounds path" — a self-narration / `there is`-opener / em-dash-appositive
> defect is an in-bounds lift, and a shared cause across two clusters is a 2-point gain from one pass.**
