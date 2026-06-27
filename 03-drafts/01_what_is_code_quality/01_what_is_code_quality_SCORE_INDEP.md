# INDEPENDENT SCORECARD — Ch 1 "Quality Is a Word No Team Can Manage" (key 01 + 02 + 59)

> **Independent (different-model) re-score** per `SCORING.md` §"The ship bar" (auto-approval requires an
> independent score, not a main-loop self-score). Deliberately harsh / skeptical posture: ≥44/50 awarded
> only if a senior Java engineer would find the chapter excellent **and** error-free. This is a SCORE-ONLY
> pass — no draft edits, no lift loop, no other files touched. This pass **re-scores after a polish pass**
> that targeted the two structural levers the prior independent re-score (42/50, LIFT-LOOP) had named as
> the only thing standing between the chapter and the 88% bar: figure front-loading and prose em-dash density.

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score, post-polish)
- **Dossier key:** 01 (owner) + folds 02, 59 — per `01-index/FINAL_INDEX.md` Ch 1
- **Slug:** `01_what_is_code_quality`
- **Title (as drafted):** "Quality Is a Word No Team Can Manage"
- **Part / arc position:** Part I — Foundations, Chapter 1
- **Artifact scored:** `03-drafts/01_what_is_code_quality/01_what_is_code_quality_v1.md`
- **Verified against:** `00-strategy/SOURCE-PIN.md` — pin re-checked at this scoring 2026-06-28
- **Gate inputs read:** `_EXAMPLE.md` (EXAMPLE-BUILD = N/A, FLOOR-C compile clause inapplicable), prior independent `_SCORE_INDEP.md` (pre-polish = 42/50, LIFT-LOOP), flags `01_named_canon_verbatims_and_cisq_stat_verify_at_pin.md` + `01_iso25010_2023_subtree_unverified.md`, and `01-index/FINAL_INDEX.md` (LOCKED — to re-resolve every internal cross-reference). (No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` / `_CODEREVIEW.md` exist for this chapter — gates were run as documented manual passes; no companion module ⇒ no CODE-REVIEW.)
- **Scorer:** chapter-scorer agent (independent re-score)
- **Date:** 2026-06-28
- **Lift-pass #:** post-polish re-score (the polish itself was run by the drafter in-bounds; this is a SCORE-ONLY verification of the result — pass 2 of ≤3 in the bounded lift loop)

---

## Re-score focus — were the two named gaps genuinely resolved?

The prior independent pass (42/50, LIFT-LOOP) was defect- and floor-clean. It sat 2 below the 88% bar on
exactly two structural levers, both named in that pass's fix-list (#1 figure front-loading; #3 prose
em-dash density). Each was re-verified against disk this pass — and **measured**, not taken on the polish
note's word.

| Prior gap (capping a cluster) | Prior state | State now (verified/measured this pass) | Resolved? |
|---|---|---|---|
| **Figure front-loading** — capped CLARITY 8 and UTILITY 9 ("the §How-it-works paragraph still front-loads all three figures before any has been earned… costing a first-time reader one re-read"). | §How it works ¶ opened by announcing all three figures up front, before any section had earned its picture. | Draft L39 now reads **"Each section carries the one figure that earns it, introduced where it lands."** — no figure named up front. Each figure is now introduced **in prose immediately before it appears**: Fig 1.1 at L43 ("Figure 1.1 lays out the whole model…") → image L45; Fig 1.2 at L79 ("Figure 1.2 plots it: the cost of adding a feature against the age of the codebase, traced twice…") → image L89; Fig 1.3 at L116 ("Figure 1.3 sets the two axes against each other and gives each of the four cells a voice…") → image L123. The triple-announce is gone; every figure is earned and named where it lands. | **YES — front-load removed; all three figures introduced in prose before they appear.** |
| **Prose em-dash density** — capped READABILITY 8 ("em-dash density still slightly over target… a handful of appositive em-dashes could become periods/commas"). | A handful of appositive em-dashes in running body prose pushed density over the soft ~8/1k target. | **Measured this pass.** Body (HTML comments stripped) = 3,675 words, 21 em-dashes ⇒ 5.71/1,000 raw — already under the ~8/1k soft target. Decomposed: **15 of 21 are figure-caption / title / epigraph titling punctuation** (the house "Figure N.N — Title — subtitle" caption format carries two em-dashes each ×3 figures, plus the H1 subtitle and the Fowler epigraph), which the READABILITY anchor does not score as prose density. **Prose-flow appositive em-dashes in running body text are effectively eliminated:** the only `—` left in running prose are L120 (inside a *quoted* dialogue cell — sanctioned) and L230 (the ISO standard's own subtitle inside a source citation). The polish note's stated "~3.8/1000" understates the raw count but **overstates nothing about the fix**: the prose-flow appositive dashes the prior pass flagged are gone. | **YES — running-prose appositive em-dashes converted out; residual is caption/title titling, not prose.** |

**Note on the polish note's stated figure.** The note claimed em-dash density "cut to ~3.8/1000." The
measured raw body figure is **5.71/1000** (the note appears to have excluded caption titling from its own
denominator-or-numerator differently). This is recorded for honesty — but it does **not** change the
verdict on the gap: the *prose-flow appositive* dashes the prior pass actually penalized are resolved, and
even the raw 5.71 is under the soft target. No score is awarded on the note's number; the score is awarded
on the measured running-prose state.

---

## Content-floors (checked FIRST — fatal if FAIL)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Scripted banned-phrase sweep of the body = **0 hits** (`better than` / `unlike ` / `the problem with` / `superior` / `beats` / `outperforms` / `worse than` / `inferior` / `best-in-class`). No winner crowned. The two "Alternatives" (practitioner attribute-lists; quality-in-use/25019) are framed as complementary, not ranked: "neither is 'the' answer," "complementary, not competing." Comparative claims (DORA throughput↔stability) are sourced. ISO/Fowler/Cunningham/DORA are treated as shared foundations, not rivals. No comparative superlative in any heading. (Unchanged by the polish — neutrality was already clean.) |
| **B — HONEST-LIMITATIONS** | **PASS** | Negative-cost claim explicitly scoped to the long run with the throwaway-code exception stated twice (IMPORTANT callout L104; §When to use L197). §Limitations gives 5 honest edges (25010 is vocabulary-not-metric; debt number is a gameable heuristic; quality ≠ product success; model-general-vs-Java-specific). SQALE WARNING (L137): "a model output, not ground truth … never a target to hit." Debt-register NOTE (L173): "with no paydown plan is not management." Every feature carries a when-NOT / a cost. (Unchanged by the polish.) |
| **C — SOURCE-TRACE** | **PASS** | Zero invented rule IDs / config keys / tool flags / API signatures / GAV coordinates / version numbers / benchmark figures. Every load-bearing atom traces to a `SOURCE-PIN.md` row (ISO/IEC 25010:2023; Fowler; *Clean Code* 2008; Cunningham named-secondary; SonarQube 2026.1 LTA; CISQ — non-pin, **dated + attributed + hedged**; DORA 2025 + *Accelerate* 2018). The named-canon verbatims, the CISQ statistic, the ISO-2023 finer sub-tree, and the SonarQube SQALE defaults are correctly held `⚠ verify-at-pin` / `⚠ UNVERIFIED` and flagged to `09-flags/` (two flag files) — not asserted as machine-verified. Internal "Ch NN" cross-references re-resolved against the LOCKED `FINAL_INDEX.md` this pass: SQALE→Ch 17 (key 35, matches the chapter's own flag); readability→Ch 2+6; deep-dive routing table row-by-row; debt-management→Ch 34/38/39 — all match. Nothing fabricated; nothing drifted off the pin. (Unchanged by the polish — it touched figure-intro wording and prose dashes, not facts.) |
| **C — COMPILE / CODE-REVIEW** | **N/A (not FAIL)** | Pure-concept chapter; body displays **0** Java fences and **0** `<!-- include: -->` markers (the sole fenced block, L81-87, is the ASCII source-sketch for the rendered Fig 1.2). `_EXAMPLE.md` adjudicates EXAMPLE-BUILD = **N/A** — no module in scope, so the FLOOR-C compile/code-review clause is inapplicable, not failed. The trailing RUNNABLE-EXAMPLE-SPEC is a withdrawn proposal, retained for provenance only; building it would invent an undisplayed listing. |

**Floor result: A PASS · B PASS · C(source) PASS · C(compile/review) N/A — no fatal floor failure. Scoring proceeds.**

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | **The one named cap is resolved.** The prior 8 was held back *solely and explicitly* by the §How-it-works figure front-load ("still front-loads all three figures before any has been earned… costing a first-time reader one re-read"). Draft L39 is now a single earned-where-it-lands sentence, and each figure is introduced in prose immediately before it appears (L43→45, L79→89, L116→123). The mechanism spine was already clean and ordered — decompose ("quality" → ISO characteristics) → internal/external split → negative-cost (4 numbered steps L97-100) → debt = principal + interest → put a number on it (SQALE) → speed↔stability — with three load-bearing figures + two tables + an ASCII sketch carrying the model (no grey-text wall) and why-before-how held throughout. With the only cited structural friction removed, a first-time reader can reconstruct the model from the chapter alone. Earns **9**, not 10: a 10 is reserved for a chapter with zero re-read risk anywhere; one or two beats still lean lightly on a forward-pointer (fix-list residual), so 9 is the honest mark. |
| 2 | **ACCURACY** | **9** | Source-trace to the pin is strong (see FLOOR C) and edition discipline is exemplary — the ISO table is explicitly the **2011** model, 2023 top-level changes are stated as fact, and the finer 2023 sub-tree + SQALE defaults + named verbatims are correctly hedged and flagged. The internal cross-reference defect cleared two passes ago is re-confirmed against disk this pass (every "Ch NN" pointer resolves correctly in the LOCKED `FINAL_INDEX.md`). The polish did not touch a single fact, citation, or flag — it reordered figure-intro prose and converted appositive dashes — so the accuracy posture is unchanged and intact. Held back from 10 only by the residual verify-at-pin / UNVERIFIED atoms that are *correctly* flagged but not yet machine-confirmed at the pin (named-canon verbatims, CISQ stat, ISO-2023 sub-tree, SQALE defaults) — honest and well-handled, but a 10 wants zero open atoms. No invented fact; floor intact. (Not inflated to 10 — the open flags are real.) |
| 3 | **UTILITY** | **10** | **The one named cap is resolved.** The prior 9 was held back from 10 by exactly "the same figure-front-loading re-read that touches CLARITY" — and that re-read is now gone. The chapter's core utility move is a *map into the rest of the book* ("each row's tooling is a later chapter"); with every forward-reference resolving correctly (verified) and the figure friction removed, that map is trustworthy and friction-free on first read. The page hands the intended reader (a lead / working dev) the exact vocabulary + the negative-cost claim + the decisive timeframe argument to win a real planning conversation ("over what horizon does this code live?"), the concrete Maintainability→Java table (named smells, named tools), the three debt-management rules (make visible / prioritize by churn / gate new debt), and a sources-pointing "Trace it back." This is the cluster where the chapter is strongest, and its sole remaining cap has been removed. Earns **10** — it becomes the page a reader keeps open. |
| 4 | **DEPTH** | **8** | Unchanged — and correctly so: the polish targeted clarity/readability, not substance, so there is no basis to move depth in either direction. Comfortably sustains a full opening chapter without padding — three dossiers (definition + economics + debt-management) merged into mechanism + evidence-for + honest limitations + alternatives + when-to-use, all sourced. Two lenses (standards + economics) plus the debt quadrant give genuine, slightly contested substance. SQALE mechanics are deliberately deferred to Ch 17 (by design, the right call for Ch 1) rather than developed here — which caps depth-on-the-page at solid rather than exceptional. **8** is the honest structural ceiling; not inflated. |
| 5 | **READABILITY** | **9** | **The named cap is resolved.** The prior 8 was held back from 9 *only* by "em-dash density still slightly over target… a handful of appositive em-dashes could become periods/commas" (plus minor self-narration). Measured this pass: prose-flow appositive em-dashes in running body text are **effectively eliminated** — of 21 body em-dashes, 15 are figure-caption/title/epigraph titling punctuation (not prose density), and the only `—` left in running prose are inside a quoted dialogue cell (L120, sanctioned) and an ISO subtitle in a citation (L230). Raw body density 5.71/1k is already under the soft ~8/1k target. The locked voice was already clean (third-person invisible narrator; the two `don't` hits are inside quoted dialogue — sanctioned; plain-language-first glossing of cruft / technical debt / principal-interest / analysability; callouts used sparingly). With the primary named defect resolved, earns **9**, not 10: a 10 is "effortless at full precision," and a few mildly self-narrating beats remain (fix-list residual #2), so 9 is honest. |

**Cluster subtotal: 45 / 50** — no cluster below 6 (lowest = DEPTH 8).

---

## Verdict

- [x] **SHIP** (auto-approve into `04-approved/`)
- [ ] **LIFT-LOOP**
- [ ] **CUT**

**Ship-bar test (`SCORING.md`):** auto-approval needs **all floors PASS AND ≥44/50 (88%) with no cluster < 6**, on an independent score. Floors A/B/C-source **PASS** (C compile/review N/A — pure-concept chapter, correctly not a FAIL). Aggregate **45/50 ≥ 44/50** AND lowest cluster = **8 ≥ 6**. → **Both conditions met on an independent score. SHIP.**

**The polish cleared the last 2 points — honestly, not by inflation.** The prior pass named exactly two
structural levers as the only thing between the chapter and the bar, and both were genuinely resolved
against disk this pass: the figure front-load is gone (each figure introduced in prose where it lands),
lifting **CLARITY 8→9** and **UTILITY 9→10**; and the running-prose appositive em-dashes are converted out,
lifting **READABILITY 8→9**. Aggregate **42→45 (+3)**. ACCURACY held at 9 (open verify-at-pin atoms keep it
off 10) and DEPTH held at 8 (SQALE mechanics deferred by design) — neither was touched by the polish and
neither was moved here, because moving them would be inflation, not measurement. No floor risk was
introduced: the polish reordered figure-intro wording and changed punctuation; it touched no fact, no
citation, no flag, and no neutrality posture.

**One-line rationale:** Floor-clean, source-disciplined, well-voiced opening chapter whose two named
structural caps (figure front-loading; prose em-dash density) are genuinely resolved — CLARITY and
READABILITY to 9, UTILITY to 10 — clearing the 88% bar at 45/50 on an independent score.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self, prior) | 2026-06-20 | 40 / 50 | PASS | PASS | PASS / PENDING-RUNTIME→N/A / N/A | (self — cannot approve) | initial main-loop self-score |
| 0 (indep, pre-lift) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS / N/A / N/A | LIFT-LOOP | independent harsh re-score; −1 vs self, driven by locked-index cross-reference defects (SQALE→Ch 38 vs Ch 17; readability refs to Ch 3/17) |
| 1 (indep, post-lift) | 2026-06-28 | 42 / 50 | PASS | PASS | PASS / N/A / N/A | LIFT-LOOP | cross-reference lift: SQALE repointed to Ch 17 (matches `FINAL_INDEX` + flag key 35); readability refs → Ch 2+6; deep-dive routing table verified row-by-row; title reconciled. ACCURACY 7→9, UTILITY 8→9. Aggregate 39→42 (+3); 2 below the bar on structural CLARITY/READABILITY caps, no remaining defect. |
| 2 (indep, post-polish) | 2026-06-28 | **45 / 50** | PASS | PASS | PASS / N/A / N/A | **SHIP** | re-score after the drafter's in-bounds polish: figure front-load removed (each figure introduced in prose where it lands — verified L39/43/79/116); running-prose appositive em-dashes converted out (measured: prose-flow appositives ≈ eliminated, residual is caption/title titling; raw body 5.71/1k, under the soft target). **CLARITY 8→9, UTILITY 9→10, READABILITY 8→9** — the two named structural caps resolved. ACCURACY held 9 (open verify-at-pin atoms), DEPTH held 8 (SQALE deferred by design). Aggregate **42→45 (+3)**; **clears the 88% bar → SHIP**. No fact/citation/flag/neutrality change ⇒ floors intact. |

---

## Learnings & pipeline suggestions

- **A polish note's self-reported metric must be re-measured, not trusted.** The note claimed em-dash density "~3.8/1000"; the measured raw body figure is 5.71/1000. The discrepancy did **not** change the verdict (the *prose-flow appositive* dashes that were actually penalized are resolved, and even 5.71 is under the soft target), but scoring on the note's number rather than the disk would have been sloppy. **Suggestion:** the READABILITY check should measure em-dash density on **running prose only** — figure captions, the H1 subtitle, and epigraphs use a fixed house titling format ("Figure N.N — Title — subtitle") that mechanically contributes ~2 dashes per caption and is not prose density. A naive whole-file dash count over-counts a figure-rich chapter by ~15 here; promote a "strip captions/titles/fences/comments before density" rule into the CLARITY/READABILITY gate so the soft flag fires on prose, not on house caption punctuation.
- **"Introduce the figure in prose before it appears" is a high-leverage, cheap clarity lever.** Removing one up-front triple-announce and letting each figure be named where it lands moved two clusters (CLARITY 8→9, UTILITY 9→10) with zero new material and zero floor risk — the single most efficient pass in this chapter's history. **Suggestion:** add "every figure is named in prose in the section that earns it, not announced up front" as an explicit CLARITY checklist item in the gate, since it recurs across image-heavy chapters and is bar-deciding.
- **The bounded lift loop terminated correctly at pass 2 of ≤3.** 39 → 42 → 45: each pass resolved a *named* defect/cap and re-scored honestly; no pass lowered the bar or inflated a cluster to reach it (ACCURACY and DEPTH were explicitly held at their honest ceilings both times). This is the loop working as designed — the bar separated "defect-clean" (42) from "excellent" (45), and a single legitimate clarity pass closed the gap. Keep the discipline of moving *only* the cluster whose named cap a pass actually addressed.
- **Floor discipline on a concept chapter held steady across all three passes.** EXAMPLE-BUILD = N/A is correctly *not* a FLOOR-C FAIL (no displayed code ⇒ no module), the banned-phrase sweep stayed at 0, and the named-canon verbatims + non-pin CISQ stat remain correctly dated/attributed/flagged. A polish pass that touches only wording and punctuation cannot move a content floor — confirmed here; keep this steady state.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement rule.)
