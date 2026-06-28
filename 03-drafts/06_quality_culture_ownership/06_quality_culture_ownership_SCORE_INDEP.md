# INDEPENDENT SCORECARD — Ch 4 "Whose Job Is Quality?" (key 06, folds 90)

> **Independent (different-model) re-score** per the ship bar in `00-strategy/SCORING.md` (auto-approval
> is ≥44/50, no cluster <6, floors A/B/C-source PASS, scored by an independent gate). Deliberately
> **harsh, skeptical** review: ≥44/50 is awarded only if a senior Java engineer would find the chapter
> excellent **and** error-free. This file is the independent record; it does not replace the main-loop
> self `_SCORE.md`. **SCORE ONLY — no draft edits, no lift loop performed here.**
>
> **This is a RE-SCORE after a web-verify VERIFY pass** (`06_quality_culture_ownership_VERIFY.md`,
> 2026-06-28). The prior independent baselines returned **37/50** then **38/50** (ACCURACY 6→7), naming
> the load-bearing named-source verbatims as "attributed-but-not-clone-verified" — the structural ceiling
> on ACCURACY. This pass **independently web-verified those verbatims against web-public primaries**
> (reproduced below), confirms the SOURCE-PIN §5/§7 rows now exist, re-confirms the floors, and re-scores
> all five clusters against the lifted artifact.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score — post web-verify)
- **Dossier key:** 06 (folds 90 — bus/truck factor) · printed **Chapter 4**, closes Part I
- **Slug:** `06_quality_culture_ownership`
- **Title:** Whose Job Is Quality? (Culture, ownership, shift-left, knowledge distribution)
- **Artifact scored:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Companion module:** **none** — culture/process chapter; EXAMPLE-BUILD = **N/A**. FLOOR C COMPILE +
  CODE-REVIEW clauses **do not attach**; FLOOR C reduces to **SOURCE-TRACE only** for this chapter.
- **Verified against** the pinned authority set (`00-strategy/SOURCE-PIN.md`) — §5 DORA/Westrum + §7
  Smith/Boy-Scout rows pinned 2026-06-28; runtime/tool rows pinned 2026-06-20.
- **Gate reports on disk:** `06_quality_culture_ownership_VERIFY.md` (web-verify PASS, 2026-06-28),
  `_EXAMPLE.md` (PASS / N/A), `09-flags/06_culture_named_source_verbatims_verify_at_pin.md` (atoms 1–5
  CLEARED; 6–8 still flagged), the three figure sidecars. No standalone `_CLARITY.md` / `_AUDIT.md` on
  disk — those criteria are judged from the artifact + the VERIFY pass + the sidecars.
- **Scorer:** chapter-scorer agent (independent, harsh)
- **Date:** 2026-06-28
- **Lift/verify pass #:** post-VERIFY re-score (the VERIFY pass is the in-bounds lift since 38/50)

---

## What changed since the 38/50 baseline (independently confirmed)

The 38/50 scorecard set ONE explicit gate between ACCURACY 7 and 8: *"the load-bearing verbatims are
`⚠ verify-at-pin` … nothing is diffed character-for-character … 'honestly flagged' is floor-passing but
is not the 8+ 'fully traced, zero drift' band."* The VERIFY pass closed exactly that gate, and this
scorer **reproduced the web-verification independently** (not taken on the VERIFY pass's word):

| Atom | Independently re-fetched value | Source | Verdict |
|---|---|---|---|
| Epigraph / DORA generative-culture | Verbatim on the capability page: *"a high-trust, generative culture predicts software delivery and organizational performance."* The draft epigraph (line 14) matches. | dora.dev/capabilities/generative-organizational-culture/ | **VERIFIED** |
| DORA psychological-safety finding | Same page, verbatim: *"In the 2019 State of DevOps Report further analysis shows that a culture of psychological [safety]…"* — confirms the draft's exact attribution to the **2019** report (line 50). | dora.dev/…/generative-organizational-culture/ | **VERIFIED** |
| Westrum typology | *"A typology of organisational cultures"* — confirmed BMJ/Qual Saf Health Care **2004** (PubMed 15576687). The draft's earlier bare "1988" is corrected to 2004 (lines 40, 158, 166, 174). | pubmed.ncbi.nlm.nih.gov/15576687/ | **VERIFIED; date fixed** |
| Boy Scout Rule | Verbatim in *97 Things Every Programmer Should Know*: *"Always leave the campground cleaner than you found it."* — the draft attributes the code-heuristic form and the casing matches (line 117). | oreilly.com/library/view/97-things-every/9780596809515/ch08.html | **VERIFIED** |
| Smith "Shift-Left Testing" | Wayback snapshot of *Dr. Dobb's*: opening def. verbatim *"Shift-left testing is how I refer to a better way of integrating the quality assurance (QA) and development parts of a software project."*; Larry Smith / 2001 confirmed. | web.archive.org/…/drdobbs.com/shift-left-testing/184404768 | **VERIFIED** |

`SOURCE-PIN.md` now carries the matching rows: §5 DORA capability + 2019 report, §5 Westrum 2004, §7
Smith 2001, §7 Boy Scout 2010 — all dated `✅ pinned 2026-06-28`. The evidence base for this chapter's
central claims has moved from **"attributed-and-flagged"** to **"pin-traced against a web-public
primary"** — the precise condition the prior scorecard named as the only thing between ACCURACY 7 and 8.

**Residual (carried, honest):** the copyrighted-book verbatims — *Broken Windows* (*The Pragmatic
Programmer*), Vogels "you build it, you run it" (ACM Queue 2006), Deming "build quality in" — have **no
web-public primary** and **stay flagged** (flag atoms 6–8). The draft asserts no quoted span for any of
them (Broken Windows is attributed with the contested-theory caveat; Vogels softened to "a practice
popularized at Amazon," no year; Deming is an attributed paraphrase). Flagged-not-invented satisfies the
floor. One cosmetic figure residual also remains (the baked `fig06_1.png` still shows "2019 State of
DevOps" — sidecar and body caption are correct; the PNG re-render is deferred by the do-not-re-render
constraint).

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | Full-draft blocklist sweep (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `destroys` / `killer` / `no reason to use` / `obvious choice over`) = **0 hits** (re-run this pass). Ownership models (strong/weak/collective, table lines 68–72) each carry strength **and** cost — none crowned. The QA-as-separate-function vs quality-as-everyone's-job contrast (§Alternatives, line 133) is "points on a spectrum, not a contest." Boy Scout + Broken Windows attributed, nothing crowned. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Dedicated `## Limitations` (lines 122–129): culture slow; correlation-not-guarantee; heuristics soft/contested; ownership trade-offs real; bus-factor a crude proxy; culture-not-a-substitute-for-tooling and vice-versa. `## When to use` (lines 135–140) carries explicit when-NOT — "ease off forcing process where it does not fit … never weaponize a culture metric." The `IMPORTANT` callout (line 52) states the hardest limitation up front: "A generative culture cannot be installed. No `mvn` goal builds trust." Every idea presented carries a when-NOT. |
| **C — SOURCE-TRACE** *(COMPILE + CODE-REVIEW dropped — module N/A)* | ✅ **PASS** | **Zero invented hard atoms:** no rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates; **no numeric DORA/defect-cost band asserted** (findings qualitative — "predicts"/"predictive of"; the shift-left figure uses *relative* cost ordering only — the contested 100×/Boehm multiplier is never asserted). The five load-bearing named verbatims are now **web-verified + pinned §5/§7** (table above). The copyrighted-book verbatims (atoms 6–8) carry **no quoted span** and are **flagged**, not invented. **Flagged-not-invented satisfies the floor.** PASS. *(The single stale rendered figure caps ACCURACY below but does not FAIL the floor — the trace record is correct.)* |

**All three floors PASS.** No floor failure. The aggregate is scored on its merits below.

---

## The five clusters (score 1–10 — independent, harsh)

| # | Cluster | Score | Δ | Justification (specific) |
|---|---|---|---|---|
| 1 | **CLARITY** | **8** | = | Untouched by the citation/verify work (it is mechanism-side; the lift was evidence-side). The spine is clean and each step earns the next: culture-is-causal → shift-left (*when*) → ownership (*who*) → bus factor (knowledge as asset) → make-the-right-thing-easy (the lever) → the layered synthesis. Three load-bearing figures (Westrum types, shift-left cost curve, ownership models) carry structure prose handles poorly; no grey wall. Why-before-how passes (the two-teams hook frames the problem before any mechanism). Held to 8, not 9: the chapter discharges much of its *how* through **forward references to ~10 unwritten chapters** (33/34/35/37/38/39, plus 1/2/3/6/19/40) — a Part-I reader meets "clean as you code (Ch 34)", "local↔CI parity (Ch 35)", "ratcheting (Ch 38)" as promissory notes, so several mechanisms are deferred rather than reconstructable from this chapter alone (the 9–10 bar). |
| 2 | **ACCURACY** | **8** | **▲ +1** | **The web-verify pass earns the band, and this scorer reproduced the verification independently.** The prior cap (7) was set on one explicit condition: the load-bearing verbatims were `⚠ verify-at-pin`, never diffed against a primary. That condition is now gone — the epigraph, the DORA generative-culture + 2019 psychological-safety findings, the Westrum 2004 attribution (date corrected from a bare 1988), the Smith 2001 definition, and the Boy Scout wording are all **verbatim-confirmed against web-public primaries** (dora.dev / PubMed / O'Reilly / Wayback-Dr.Dobb's) and carry matching **SOURCE-PIN §5/§7 rows dated 2026-06-28**. The evidence base sits squarely in the 7–8 band's defining sentence — *"every specific fact carries a citation to the pinned source."* No numeric performance band is asserted (the standing DORA-bands guard holds). Held to **8, not 9**, for two honest, *non-fatal* residuals: **(a)** the rendered `fig06_1.png` still visually bakes "2019 State of DevOps" until a deferred re-render — a real manuscript-facing pixel that lags the corrected sidecar/body caption, so this is not "zero drift, every artifact verified" (the 9–10 bar); and **(b)** three copyrighted-book heuristics (Broken Windows, Vogels, Deming) remain attributed-and-flagged with no web-public primary — honestly handled and floor-passing, but not pin-confirmed, so the chapter is not *fully* traced end-to-end. A clean, earned **8**: the central evidence is now pin-traced and verbatim-confirmed; one rendered figure and three secondary-canon attributions keep it off 9. |
| 3 | **UTILITY** | **8** | = | Unchanged. Squarely answers the question a tech lead actually has — "whose job is quality?" — with concrete, applied levers: choose an ownership model by context (small/deep → strong; flow → collective + gates; weak as the pragmatic middle); raise the bus factor deliberately (review, docs, pairing, rotation); introduce gates as enablers (fast, low-false-positive, applied to new code) not turnstiles. `## When to use` gives real decision frames. Held to 8: a lead can *act on the framing*, but the chapter deliberately routes every enforceable mechanic elsewhere (CODEOWNERS → Ch 37, gate policy → Ch 33, ratcheting → Ch 38), so the reader leaves with orientation and a decision frame, not a runnable artifact — appropriate for a culture chapter, but it caps applied utility below the 9–10 "page kept open while working" bar. |
| 4 | **DEPTH** | **7** | = | **Unchanged — the in-bounds DEPTH lift was still NOT applied.** §"Knowledge is a quality asset" (lines 80–90) remains a definition + a list of raisers rather than the worked tension the prior lift-list named (collective ownership raises the bus factor *but* needs the gates to hold quality — the chapter owns both halves). The substance otherwise genuinely merges two dossiers (culture key 06 + bus-factor key 90) and earns a full chapter: mechanism (Westrum → DORA, Deming → Smith lineage, three ownership models), evidence-for, honest limitations, an approach-based Alternatives section, a when-to-use; honest about soft/contested edges. Held to 7: the substance **stays at the principle level** — most contested depth (clean-as-you-code mechanics, ratcheting, gate tuning) is named and deferred, and the bus-factor treatment is still a list, not an argued tension. Broad-and-sound rather than deep-and-contested — a defensible Part-I-closer design, but solid-7 substance, not 8+. **This is the single remaining gap to the 44 bar.** |
| 5 | **READABILITY** | **8** | = | Held at 8, now with the prior pass's two deductions **resolved** and re-measured this pass. Em-dash density is **5.52/1,000** (13 em-dashes / 2,353 prose words) — comfortably under the ~8 target (the prior 38/50 read of ~9.0 no longer holds; the appositive surgery landed). The title refrain "Whose job is quality?" now recurs **3×** (hook, deep-dive synthesis, back-matter), down from ~4 — at the edge of "state once" but no longer milked. The locked voice holds: third person, invisible narrator, no first person, no narration contractions (apostrophes are possessives; `// NOSONAR` / `--no-verify` are quoted tokens); zero banned filler (`easily`/`just`/`simply`/`obviously` sweep = 0). Concrete stakes-first hook (two teams, identical stack, opposite outcomes). Callouts sparing. Held to 8, not 9: the "make the right thing the **easy** thing" idiom (heading + line 154) sits on the edge of the difficulty-word rule — it reads as the paved-path noun phrase ("the easy thing"/"made easy by the tools") characterizing the *path*, not the reader's difficulty, so it is a **noted edge risk, not a hit** — and the prose, while clean and paced, is steadily-good rather than effortless-at-full-precision (the 9–10 bar). |

**Cluster subtotal: 39 / 50** — no single cluster below 6 (lowest is DEPTH at 7).

---

## Verdict

- **Aggregate: 39 / 50** (up from 38/50; ACCURACY 7→8 on the independently-reproduced web-verification of
  the five load-bearing verbatims + matching SOURCE-PIN rows). Floors **A / B / C-source all PASS**. No
  cluster below 6.
- **Ship bar (≥44 / 50, no cluster <6, floors PASS):** **NOT CLEARED.** 39 is **5 points short** of the 44
  auto-approval bar. The floors do not block — this is a **cluster-quality miss**, so the bounded lift loop
  is the correct next move.

**Phase-3 chapter scorecard:** ☑ **LIFT** (still below bar). The web-verify pass banked the highest-leverage
fix it set out to (verbatims pin-traced → ACCURACY +1, the one band the prior scorecard said was available),
and READABILITY's two soft residuals are confirmed resolved (em-dash 5.52/1k, refrain 3×). The gap to 44 is
now **concentrated and nameable**.

**The exact remaining gap (44 − 39 = 5 points), in order of leverage:**

1. **DEPTH 7 → 8 (+1) — the single in-bounds, no-new-facts move still on the table.** Apply the long-carried
   lift-list item: in §"Knowledge is a quality asset" surface the one already-owned tension (collective
   ownership raises the bus factor *but* needs the automated gates to hold quality — the chapter owns both
   halves) so the section **argues rather than lists**. This has been carried across three passes and is the
   one point fully inside the drafter's reach.
2. **ACCURACY 8 → 9 (+1) — requires re-rendering `fig06_1.png`** year-free from HTML via `render.mjs` so the
   baked caption matches the corrected sidecar, **and** clearing the three copyrighted-book attributions
   (Broken Windows / Vogels / Deming) — which needs the named editions, not a web source, so it is partly
   outside in-bounds reach. The "zero drift, every artifact verified" 9-bar is not reachable while one
   rendered figure lags and three secondary verbatims stay flagged.
3. **+3 more must come from CLARITY / UTILITY / READABILITY 8 → 9** — each is a genuine 8 held off 9 by the
   chapter's deliberate design (forward-references discharge the *how*; every mechanic is routed to a later
   chapter; the prose is clean-not-effortless). Lifting any of these to 9 is **hard for a Part-I culture
   chapter that by design defers its mechanics** — it would require the chapter to carry more of its own
   *how* without scope creep or a floor risk.

**Honest budget read:** DEPTH 7→8 is reachable in-bounds (+1 → 40). The remaining +4 to 44 is **not
plausibly reachable inside the lift budget** without either (a) violating in-bounds rules (importing the
deferred mechanics this chapter intentionally hands to Chs 33/34/35/37/38) or (b) work outside the drafter's
reach (the figure re-render + the copyrighted-edition diffs). A senior engineer would find this chapter
**sound, neutral, well-written, and now pin-traced** — but would not call it *excellent and error-free* at
the 44 bar while DEPTH stays a list, one rendered figure bakes the old edition, and three secondary
verbatims remain flagged. **LIFT, not SHIP.**

**One-line rationale:** The five load-bearing named verbatims are now web-verified and pinned (epigraph,
DORA/2019, Westrum 2004, Smith 2001, Boy Scout), so ACCURACY earns 7→8 and the aggregate reaches 39/50 with
em-dash/refrain residuals also cleared — but the carried-forward DEPTH bus-factor tension is still un-applied
and the path from 39 to 44 runs mostly through 8→9 lifts that this by-design-deferring culture chapter cannot
reach in-bounds, leaving it 5 short of the bar.

---

## Flagged weakest cluster (for the next lift pass)

- **Weakest cluster:** **DEPTH (7)** — now the sole sub-8 cluster and the one fully-in-bounds point left.
- **Single highest-leverage in-bounds move (DEPTH 7→8):** in §"Knowledge is a quality asset" (lines 80–90)
  convert the definition-plus-list into an argued tension using material the chapter already owns: collective
  ownership raises the bus factor *but* requires the automated gates (Parts IV–IX) to hold quality from
  drifting — the chapter already states both halves elsewhere, so this is synthesis, **no new facts**.
- **Beyond DEPTH (outside or partly-outside in-bounds reach, recorded so the budget stays honest):**
  - **ACCURACY 8→9:** re-render `fig06_1.png` year-free (figure-designer / `render.mjs`) so the baked caption
    matches the corrected sidecar; and confirm the three copyrighted-book verbatims against their named
    editions (`_ref/` corpus, close-paraphrase check only) to clear flag atoms 6–8 — needs the editions, not
    a web source.
  - **CLARITY / UTILITY / READABILITY 8→9:** each is held off 9 by deliberate design (deferred mechanics,
    routed mechanics, clean-not-effortless prose); lifting any to 9 without scope creep or floor risk is the
    hard part and may not be achievable for a Part-I culture chapter.

> **Budget honesty.** This was the post-verify re-score. DEPTH 7→8 (+1 → 40) is the only confidently
> in-bounds point remaining; the further +4 to 44 runs through 8→9 lifts that this by-design-deferring
> chapter struggles to reach in-bounds. If, after the DEPTH lift, the aggregate cannot clear 44 within the
> remaining budget without importing deferred mechanics (scope creep) or work outside the drafter's reach,
> the chapter is a **cut/flag candidate for the human gate** — do not lower the bar, do not pad, do not
> import the later chapters' mechanics to inflate DEPTH.

---

## Line-level fixes (the lift list — in-bounds unless noted)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | **DEPTH** (in-bounds) | §"Knowledge is a quality asset" (lines 80–90) | **Still pending across 3 passes** — bus-factor treatment is a definition + a list of raisers; the merged key-90 material reads thin against the rest of the chapter. | Surface one concrete already-owned tension (collective ownership raises the bus factor *but* needs the gates to hold quality) so the section argues rather than lists. **No new facts.** This is the one fully-in-bounds point left. |
| 2 | ACCURACY (figure-designer) | `05-figures/06_quality_culture_ownership/fig06_1.{html,png}` | The **baked `fig06_1.png` still displays "2019 State of DevOps"** (sidecar + body caption already corrected). | Edit `fig06_1.html` to the year-free pinned attribution; re-render the PNG via `render.mjs` so the caption pixels match the corrected sidecar. Clears the sidecar PNG residual. |
| 3 | ACCURACY / SOURCE-TRACE (legal/`_ref` reach) | Draft lines 118, 180 (Broken Windows); 78 (Vogels); 56, 179 (Deming) | Three copyrighted-book heuristics remain **attributed-and-flagged** (atoms 6–8); no web-public primary. | Confirm wording against the named editions in `_ref/` (close-paraphrase check only — never quote into the manuscript); clear atoms 6–8 or keep the attributed-no-quote form. Needs the editions, not a web source. |
| 4 | (Watch — not a fix) READABILITY | Heading "Make the right thing the easy thing" + line 154 | "the easy thing" sits on the edge of the difficulty-word rule. | **No change required** — it reads as the paved-path noun phrase characterizing the path, not the reader's difficulty. Recorded as a noted edge risk only, consistent with prior passes. |

---

## Lift / verify-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE (COMPILE/CR = N/A) | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | **37 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (flag-escape; module N/A) | LIFT | independent baseline; harsh re-score of main-loop self 40/50 |
| 1 (indep) | 2026-06-28 | **38 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (flag-escape; module N/A) | LIFT | DORA attribution reframed to pinned in `fig06_1.sources.md`; Smith 2001 flagged in-body. Two concrete off-pin attributions resolved → **ACCURACY 6→7**. DEPTH lift not applied. |
| 2 (indep, post-VERIFY) | 2026-06-28 | **39 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (5 verbatims now pin-traced; atoms 6–8 flag-escape; module N/A) | **LIFT** | Web-verify pass: epigraph reframed to a verbatim DORA capability statement; DORA/2019 + Westrum 2004 + Smith 2001 + Boy Scout **web-verified against public primaries** (reproduced independently this pass) + SOURCE-PIN §5/§7 rows added → **ACCURACY 7→8**. READABILITY residuals confirmed cleared (em-dash **5.52/1k**, refrain **3×**) — held at 8. DEPTH bus-factor tension **still not applied** → DEPTH held 7. |

> Self `_SCORE.md` returned **40/50** (8/8/8/8/8). Independent baseline 37 → 38 → **39/50**. All scores
> agree the chapter is **below the 44 bar** and needs further lift; none approves it. The independent and
> self scores have now converged to within 1 point — the disagreement was entirely the ACCURACY band that
> the web-verify pass closed.

---

## Learnings & pipeline suggestions

- **A web-verify pass against public primaries is the lift that moves ACCURACY 7→8 — exactly one band, and
  only when the verbatims were the cap.** The 38/50 scorecard named the precise gate ("verbatims are
  `⚠ verify-at-pin`, not diffed character-for-character"); closing it with reproduced web-verification was
  worth +1 and no more. The next band (8→9) is a *different* lift again — "zero drift, every artifact
  verified" — gated by the stale rendered figure and the un-sourced copyrighted verbatims. Three distinct
  ACCURACY lifts now sit on the record (fix concrete off-pin drift → 6-7; web-verify the verbatims → 7-8;
  re-render + clear every secondary → 8-9); a scorer should award each only for its own work.
- **A culture/process chapter that by design defers its mechanics has a structural READABILITY/CLARITY/
  UTILITY ceiling around 8.** This chapter routes every enforceable mechanic to a later chapter (correct for
  a Part-I closer) — which is *why* CLARITY, UTILITY, and READABILITY each sit at a genuine 8 and not 9. The
  pipeline lesson: for the small number of pure-culture/process chapters, the 44 bar may only be reachable by
  lifting DEPTH and ACCURACY to 8–9, because the other three are design-capped. The scorer should flag this
  early so the lift budget is not spent chasing a 9 that the chapter's design forbids.
- **A carried-forward in-bounds lift item that survives three passes is a signal, not an oversight.** DEPTH's
  bus-factor-tension fix has been available and un-applied across passes 0–2. It is now the *only* confidently
  in-bounds point to the bar. Naming it as "carried forward, pass 3 is the budget" keeps the loop honest and
  tells the drafter the chapter is one applied fix from 40 and then a hard 8→9 climb from the bar.
- (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
