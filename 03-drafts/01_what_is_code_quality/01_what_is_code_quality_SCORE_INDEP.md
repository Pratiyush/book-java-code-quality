# INDEPENDENT SCORECARD — Ch 1 "Quality Is a Word No Team Can Manage" (key 01 + 02 + 59)

> **Independent (different-model) re-score** per `SCORING.md` §"The ship bar" (auto-approval requires an
> independent score, not a main-loop self-score). Deliberately harsh / skeptical posture: ≥44/50 awarded
> only if a senior Java engineer would find the chapter excellent **and** error-free. This is a SCORE-ONLY
> pass — no draft edits, no lift loop, no other files touched. This pass **re-scores after the lift** that
> targeted the cross-reference defects surfaced in the prior independent pass (39/50, LIFT-LOOP).

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score, post-lift)
- **Dossier key:** 01 (owner) + folds 02, 59 — per `01-index/FINAL_INDEX.md` Ch 1
- **Slug:** `01_what_is_code_quality`
- **Title (as drafted):** "Quality Is a Word No Team Can Manage"
- **Part / arc position:** Part I — Foundations, Chapter 1
- **Artifact scored:** `03-drafts/01_what_is_code_quality/01_what_is_code_quality_v1.md`
- **Verified against:** `00-strategy/SOURCE-PIN.md` — pin re-checked at this scoring 2026-06-28
- **Gate inputs read:** `_EXAMPLE.md` (EXAMPLE-BUILD = N/A, FLOOR-C compile clause inapplicable), prior self-`_SCORE.md`, prior independent `_SCORE_INDEP.md` (this file, pre-lift = 39/50), flags `01_named_canon_verbatims_and_cisq_stat_verify_at_pin.md` + `01_iso25010_2023_subtree_unverified.md`, and `01-index/FINAL_INDEX.md` (LOCKED — to resolve every internal cross-reference). (No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` / `_CODEREVIEW.md` exist for this chapter — gates were run as documented manual passes; no companion module ⇒ no CODE-REVIEW.)
- **Scorer:** chapter-scorer agent (independent re-score)
- **Date:** 2026-06-28
- **Lift-pass #:** post-lift re-score (the lift itself was run by the drafter in-bounds; this is a SCORE-ONLY verification of the result)

---

## Re-score focus — were the prior defects resolved?

The prior independent pass (39/50, LIFT-LOOP) was driven by two accuracy defects. Both were re-verified
against disk this pass:

| Prior defect | Prior state | State now (verified this pass) | Resolved? |
|---|---|---|---|
| **SQALE model cross-ref** | Asserted "owned in **Chapter 38**" ×3 (L134, L169-debt-view, L225) — but SonarQube/SQALE is **key 35 → Ch 17** in the locked index, contradicting the chapter's own flag. | Draft L134 + L225 now read "the model is owned in **Chapter 17**." `FINAL_INDEX.md` Ch 17 = "SonarQube, IDE inspections & the layered stack" (key 35) — **match**. The flag's stated owner is key 35 — **match** (the flag's L56 "Chapter 38" string is the flag recording the *old* draft text for provenance; its L57-58 explicitly state owner = key 35). | **YES — reconciled to the locked index AND the flag.** |
| **Title drift** | Draft H1 "…No Team Can Manage" but gate reports/flags still "…You Can't Manage". | Draft H1 (L10), `_EXAMPLE.md` (L21), this `_SCORE_INDEP.md` header, and the flag file all now read "…**No Team Can Manage**." (The superseded Jun-20 self-`_SCORE.md` carries no live title string; it is not a live gate input.) | **YES — reconciled across all live artifacts.** |

**Full sweep of every remaining internal cross-reference vs the LOCKED `FINAL_INDEX.md` (this pass):**
all now resolve correctly. L108 readability list = "Chapters 2 and 6" (Ch 2 readability/measuring ✓;
Ch 6 naming/formatting ✓ — the prior loose Ch 3/Ch 17 refs are gone). Deep-dive table (L157-161):
Analysability→"Ch 2, 6, 16" (Ch 16 = Checkstyle/PMD/SpotBugs/Error Prone ✓; Cognitive Complexity = Ch 2 ✓
— prior wrong "Ch 3" removed); Modifiability→Ch 25 (SOLID/coupling ✓); Testability→Ch 21, 23, 39 (unit
testing ✓ / coverage ✓ / refactoring-legacy-seams = Feathers WELC ✓); Modularity→Ch 25, 26 (enforcing
architecture/ArchUnit/JPMS ✓); Reusability→Ch 7 (APIs/SemVer ✓). Debt-management (L169-171):
debt view/register→Ch 38 (metrics/dashboards/Sonar dashboards ✓); churn/hotspots→Ch 39 (refactoring ✓);
clean-as-you-code→Ch 34 (the literal listed anchor for key 80 ✓) + Ch 38. **Zero mis-pointed
cross-references remain.** The accuracy defect that capped the prior pass is gone; the UTILITY penalty
the prior pass tied to the same mis-pointing is gone with it.

---

## Content-floors (checked FIRST — fatal if FAIL)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Greppable banned-phrase sweep of the body = 0 hits (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms`). No winner crowned. The two "Alternatives" (practitioner attribute-lists; quality-in-use/25019) are framed as complementary, not ranked: "neither is 'the' answer," "complementary, not competing." Comparative claims (DORA throughput↔stability) are sourced. ISO/Fowler/Cunningham/DORA are treated as shared foundations, not rivals. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Negative-cost claim explicitly scoped to the long run with the throwaway-code exception stated twice (IMPORTANT callout L104; §When to use L197). §Limitations gives 5 honest edges (25010 is vocabulary-not-metric; debt number is a gameable heuristic; quality ≠ product success; model-general-vs-Java-specific). SQALE WARNING (L137): "a model output, not ground truth … never a target to hit." Debt-register NOTE (L173): "with no paydown plan is not management." Every feature carries a when-NOT / a cost. |
| **C — SOURCE-TRACE** | **PASS** | Zero invented rule IDs / config keys / tool flags / API signatures / GAV coordinates / version numbers / benchmark figures. Every load-bearing atom traces to a `SOURCE-PIN.md` row (ISO/IEC 25010:2023; Fowler; *Clean Code* 2008; Cunningham named-secondary; SonarQube 2026.1 LTA; CISQ — non-pin, **dated + attributed + hedged**; DORA 2025 + *Accelerate* 2018). The named-canon verbatims, the CISQ statistic, the ISO-2023 finer sub-tree, and the SonarQube SQALE defaults are correctly held `⚠ verify-at-pin` / `⚠ UNVERIFIED` and flagged to `09-flags/` (two flag files) — not asserted as machine-verified. Nothing fabricated; nothing drifted off the pin. The repointed "owned in Chapter 17" is an internal cross-reference, not an external fact — it does not touch the source-trace floor, but it is now also correct against the index. |
| **C — COMPILE / CODE-REVIEW** | **N/A (not FAIL)** | Pure-concept chapter; body displays **0** Java fences and **0** `<!-- include: -->` markers (the sole fenced block, L81-87, is the ASCII source-sketch for the rendered Fig 1.2). `_EXAMPLE.md` adjudicates EXAMPLE-BUILD = **N/A** — no module in scope, so the FLOOR-C compile/code-review clause is inapplicable, not failed. The trailing RUNNABLE-EXAMPLE-SPEC is a withdrawn proposal, retained for provenance only; building it would invent an undisplayed listing. |

**Floor result: A PASS · B PASS · C(source) PASS · C(compile/review) N/A — no fatal floor failure. Scoring proceeds.**

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | Mechanism spine is clean and each step earns the next: decompose ("quality" → ISO characteristics) → the internal/external split → the negative-cost argument (4 numbered steps, L97-100) → debt = principal + interest → put a number on it (SQALE) → speed↔stability. Three load-bearing figures (`fig01_1/2/3.png` + HTML + sources sidecars present) plus two tables and an ASCII sketch carry the model — no grey-text wall. Why-before-how holds. Held back from 9: the §How-it-works paragraph (L39) still front-loads all three figures before any has been earned, and a couple of beats lean on a forward/back-pointer rather than re-stating the link inline, costing a first-time reader one re-read. (Unchanged by the lift — the lift targeted accuracy, not the figure front-loading.) |
| 2 | **ACCURACY** | **9** | Source-trace to the pin is strong (see FLOOR C) and edition discipline is exemplary — the ISO table is explicitly the **2011** model, 2023 top-level changes are stated as fact, and the finer 2023 sub-tree + SQALE defaults + named verbatims are correctly hedged and flagged. **The cross-reference defect that capped the prior pass at 7 is resolved:** every internal "Ch NN" pointer now resolves to the correct chapter in the LOCKED `FINAL_INDEX.md` (SQALE→Ch 17 = key 35, matching the chapter's own flag; readability→Ch 2+6; the deep-dive routing table verified row-by-row; debt-management→Ch 34/38/39, all matched). Verified this pass against disk. Held back from a 10 only by the residual verify-at-pin / UNVERIFIED atoms that are *correctly* flagged but not yet machine-confirmed at the pin (named-canon verbatims, CISQ stat, ISO-2023 sub-tree, SQALE defaults) — honest and well-handled, but a 10 wants zero open atoms. No invented fact; floor intact; the "wrong-pointer" defect is gone. |
| 3 | **UTILITY** | **9** | High for the intended reader (a lead/working dev): it hands over the exact vocabulary + the negative-cost + the timeframe argument to win a real planning conversation ("over what horizon does this code live?"), the concrete Maintainability→Java table (named smells, named tools), and the three debt-management rules (make visible / prioritize by churn / gate new debt). The chapter's core utility move — a *map into the rest of the book* ("each row's tooling is a later chapter") — was the exact feature degraded by the prior mis-pointing; with every forward-reference now resolving correctly, that map is trustworthy and the page earns its keep-open status. "Trace it back" points to sources. Held back from 10 by the same figure-front-loading re-read that touches CLARITY, a minor cap on first-read usability. |
| 4 | **DEPTH** | **8** | Comfortably sustains a full opening chapter without padding — three dossiers (definition + economics + debt-management) merged into mechanism + evidence-for + honest limitations + alternatives + when-to-use, all sourced. Two lenses (standards + economics) plus the debt quadrant give genuine, slightly contested substance (Clean Code cited-and-critiquable; negative-cost claim scoped). SQALE mechanics are deliberately deferred to Ch 17 (by design) rather than developed here — the right call for Ch 1, but it caps depth-on-the-page at solid rather than exceptional. (Unchanged by the lift.) |
| 5 | **READABILITY** | **8** | Locked voice holds: third-person invisible narrator, no first person, no narration contractions (the two `don't` hits are inside quoted dialogue — sanctioned), plain-language-first glossing (cruft, technical debt, principal/interest, analysability all defined before use), callout taxonomy used sparingly (CONCEPT/NOTE/IMPORTANT/WARNING). Strong concrete hook (the "improve code quality" PR). Held back from 9: em-dash density still slightly over the ~8/1,000-words target (soft-flag, not a fail) — a handful of appositive em-dashes could become periods/commas; and a few sentences self-narrate lightly. (Unchanged by the lift — soft target, never a blocker.) |

**Cluster subtotal: 42 / 50** — no cluster below 6.

---

## Verdict

- [ ] **SHIP**
- [x] **LIFT-LOOP** (cluster quality, not a floor — the aggregate is the gate; 42 < 44)
- [ ] **CUT**

**Ship-bar test (`SCORING.md`):** auto-approval needs **all floors PASS AND ≥44/50 (88%) with no cluster < 6**, on an independent score. Floors A/B/C-source **PASS** (C compile/review N/A). Aggregate **42/50 ≥ no-cluster-below-6 (lowest = 8)** but **42 < 44/50**. → **Does not auto-approve; still LIFT-LOOP — but now within 2 points of the bar, up from 5 (39→42).**

**The lift worked.** The prior pass's two named defects are genuinely resolved against disk (not papered over): ACCURACY 7→9 and UTILITY 8→9, both driven by the now-correct internal cross-references. The aggregate moved 39→42 (+3). The remaining 2-point gap is **not** a defect — it is the deliberately steep 88% bar meeting a chapter whose CLARITY/DEPTH/READABILITY sit at a solid-but-not-exceptional 8 for honest, structural reasons (figure front-loading; SQALE mechanics deferred by design; em-dash density). There is no further accuracy or floor problem to fix.

**One-line rationale:** Floor-clean, source-disciplined, well-voiced opening chapter whose internal map now resolves correctly end-to-end — the lift cleared the cross-reference defects; the chapter sits 2 below the 88% bar on structural clarity/depth ceilings, not on error.

---

## Flagged weakest cluster (for any further lift pass)

- **Weakest cluster:** CLARITY — score **8** (tied with DEPTH and READABILITY at 8; CLARITY is the highest-leverage of the three because the single in-bounds move below lifts UTILITY with it).
- **Why it is the weakest:** Not error — structure. The §How-it-works paragraph (L39) introduces all three figures before any has been earned, and a few beats defer their logical link to a pointer instead of stating it inline; both cost a first-time reader a re-read. This is the one remaining lever that, moved, plausibly carries CLARITY (and UTILITY, which shares the re-read penalty) to 9 and clears the bar.
- **Single highest-leverage in-bounds move (NOT run here — SCORE-ONLY):** Split the L39 "three pictures" paragraph so each figure is named only in the section that earns it (the figures are already placed there; this only removes the up-front triple-announce), and re-state inline the one-sentence logical link the negative-cost and quality-vs-speed beats currently defer to a pointer. No new facts, no scope creep, no floor risk — pure reordering/inlining of material already on the page. Estimated effect: CLARITY 8→9, UTILITY 8→9 (≈ +2 → 44/50, at the bar). DEPTH/READABILITY are at honest structural ceilings and are not worth a pass.

---

## Line-level fixes (the lift list — for an in-bounds drafter pass, not run here)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | CLARITY / UTILITY | §How it works ¶ L39 ("Three pictures carry this chapter…") | All three figures announced before any is earned; mild front-load that costs a first-read re-orientation. | Drop the up-front triple-announce; name each figure only where it lands (Fig 1.1 at the ISO section, 1.2 at the economics curve, 1.3 at the quadrant — all already placed there). |
| 2 | CLARITY | §Why internal quality has negative cost (4 steps, L97-100) + §quality-vs-speed | The link back to the cruft mechanism is carried by a pointer rather than re-stated inline, costing one re-read. | Re-state the one-sentence cruft→cost link inline at the §quality-vs-speed beat (material already on the page; no new fact). |
| 3 | READABILITY | whole body (em-dash density slightly over ~8/1k); a few appositive dashes + light self-narration | Soft-flag over the em-dash target. | Convert ~6 appositive em-dashes to periods/commas/parentheses; trim residual "The point is not… It is that…" self-narration. Soft target — not a blocker. |

> **Note on prior fix-list items #1–#3 (SQALE→Ch 38; readability refs to Ch 3/17; title drift):** all **resolved** this pass and removed from the live list. SQALE now → Ch 17 (matches index + flag); readability list → Ch 2+6 and the deep-dive table verified row-by-row; title reconciled to "No Team Can Manage" across all live artifacts.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self, prior) | 2026-06-20 | 40 / 50 | PASS | PASS | PASS / PENDING-RUNTIME→N/A / N/A | (self — cannot approve) | initial main-loop self-score |
| 0 (indep, pre-lift) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS / N/A / N/A | LIFT-LOOP | independent harsh re-score; −1 vs self, driven by locked-index cross-reference defects (SQALE→Ch 38 vs Ch 17; readability refs to Ch 3/17) |
| 1 (indep, post-lift) | 2026-06-28 | **42 / 50** | PASS | PASS | PASS / N/A / N/A | **LIFT-LOOP** | re-score after the drafter's in-bounds lift: SQALE repointed to **Ch 17** (matches `FINAL_INDEX` + the chapter's own flag/key 35); readability refs tightened to Ch 2+6; deep-dive routing table verified row-by-row; title reconciled across live artifacts. **ACCURACY 7→9, UTILITY 8→9** (cross-ref defects cleared). Aggregate **39→42 (+3)**; now 2 below the 88% bar on structural CLARITY/DEPTH ceilings, no remaining defect. |

---

## Learnings & pipeline suggestions

- **The cross-reference lift is verifiable and it worked.** The prior pass's `check_xrefs` suggestion paid off immediately: every "Ch NN" pointer was resolved against `01-index/FINAL_INDEX.md` this pass and all now match. **Suggestion (re-affirm):** promote a scripted `check_xrefs` (grep "Ch NN"/"Chapter NN" → resolve each against `FINAL_INDEX.md`'s topic; flag a topic mismatch) into the VERIFY/CLARITY gate so this class of defect is caught *before* an independent score, not after.
- **A chapter contradicting its own flag is a high-value, now-closed signal.** The flag `01_named_canon…verify_at_pin.md` records owner = key 35; the draft now agrees (Ch 17). The flag's body still quotes the *old* "Chapter 38" draft text for provenance (L56) — correct as a record, but a naive scripted diff could false-positive on it. **Suggestion:** when the reconcile gate diffs a flag's stated-owner against the draft, key on the flag's explicit "owning chapter is …" line (L57-58 here), not on every chapter-number string in the flag body.
- **The 88% bar correctly separates "defect-clean" from "exceptional."** Post-lift this chapter is defect- and floor-clean yet still 2 below the bar — because CLARITY/DEPTH/READABILITY sit at an honest structural 8, not because anything is wrong. The bar is doing its job: it is not a defect gate, it is an excellence gate. **Suggestion:** keep the bar; the remaining lever (figure front-loading / inlined links, fix-list #1-2) is a legitimate clarity lift, not bar-shaving — a single further in-bounds pass is justified and is the right next action under the bounded lift loop (this is pass 1 of ≤3).
- **Floor discipline on a concept chapter held up again.** EXAMPLE-BUILD = N/A is correctly *not* a FLOOR-C FAIL (no displayed code ⇒ no module), and the named-canon verbatims + non-pin CISQ stat remain correctly dated/attributed/flagged. Keep this steady state.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement rule.)
