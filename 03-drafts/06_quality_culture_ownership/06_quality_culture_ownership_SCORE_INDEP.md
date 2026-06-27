# INDEPENDENT SCORECARD — Ch 4 "Whose Job Is Quality?" (key 06, folds 90)

> **Independent (different-model) re-score** per the ship bar in `00-strategy/SCORING.md` (auto-approval is
> ≥44/50, no cluster <6, floors A/B/C-source PASS, scored by an independent gate). Deliberately **harsh,
> skeptical** review: ≥44/50 is awarded only if a senior Java engineer would find the chapter excellent
> **and** error-free. This file is the independent record; it does not replace the main-loop self
> `_SCORE.md` (which returned 40/50). **SCORE ONLY — no draft edits, no lift loop performed here.**
>
> **This is a RE-SCORE after a targeted lift.** The prior independent baseline returned **37/50** (ACCURACY 6)
> and named two concrete off-pin attributions as the highest-leverage fix: Fig 06.1 citing an unpinned "2019
> State of DevOps report," and Smith 2001 carrying no SOURCE-PIN row / not flagged in-body. **Both are now
> resolved** (reframed-to-pinned and explicitly flagged, respectively). This pass re-confirms the floors and
> re-scores all five clusters against the lifted artifact.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score — post-lift)
- **Dossier key:** 06 (folds 90 — bus/truck factor) · printed **Chapter 4**, closes Part I
- **Slug:** `06_quality_culture_ownership`
- **Title:** Whose Job Is Quality? (Culture, ownership, shift-left, knowledge distribution)
- **Artifact scored:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Companion module:** **none** — culture/process chapter; EXAMPLE-BUILD = **N/A** (per `_EXAMPLE.md`,
  verdict PASS / module N/A). FLOOR C COMPILE + CODE-REVIEW clauses **do not attach**; FLOOR C reduces to
  **SOURCE-TRACE only** for this chapter.
- **Verified against** the pinned authority set (`00-strategy/SOURCE-PIN.md`) — pin date 2026-06-27 (corrected pin).
- **Gate reports on disk:** `_EXAMPLE.md` (PASS / N/A) + `09-flags/06_culture_named_source_verbatims_verify_at_pin.md`
  (atoms 1–8 `⚠ verify-at-pin`) + the three figure sidecars (`fig06_1/2/3.sources.md`). **No standalone
  `_VERIFY.md`, `_CLARITY.md`, or `_AUDIT.md` exists** — VERIFY findings are recorded inside the flag file and
  the figure sidecars; CLARITY and AUDIT were not independently recorded on disk. Floors below are judged from
  the artifact + the flag + the sidecars.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds lift applied since the independent baseline; this is the re-score)

---

## What the lift changed since the 37/50 baseline (confirmed on disk)

1. **DORA edition reframed to pinned — CONFIRMED (trace-record level).** `fig06_1.sources.md` (lines 51–65)
   now carries an explicit *"Attribution correction (psychological-safety finding)"*: the old **"2019 State of
   DevOps report"** attribution is removed from the authoritative sidecar and reframed to the **pinned DORA
   source** (dora.dev / 2025 DORA report + *Accelerate* 2018) with **no edition year asserted**; the specific
   State-of-DevOps edition that carries the psychological-safety result stays `⚠ verify-at-pin` (atom 2) until
   `/pin-source` adds it as a §5 row. The draft body captions (lines 42/44) already assert no year. **Residual
   (documented, deferred):** the *baked* `fig06_1.png` was **not** re-rendered (do-not-re-render constraint), so
   the on-figure caption pixels still display "2019 State of DevOps" until the next deliberate HTML-edit +
   `render.mjs` pass (sidecar lines 59–65). The trace record and the manuscript-facing body caption are correct;
   one rendered figure still lags the corrected trace.
2. **Smith-2001 flagged — CONFIRMED.** `fig06_2.sources.md` (line 27 + the trailing note, lines 41–46) records
   Smith / *Dr. Dobb's* 2001 as `⚠ verify-at-pin` with **no SOURCE-PIN row** (a §7 named-article canon gap),
   atom 4. The **draft body now carries the flag in-line** (line 175, Tier-1 Sources): *"attributed; ⚠
   verify-at-pin (no SOURCE-PIN row yet … see `09-flags/…` atom 4)."* Smith is no longer silently asserted.

Per the task and the rubric escape clause (FLOOR C: "traces to the pin **OR is flagged to `09-flags/`**"),
**flagged-not-invented is acceptable.** Both items are now reframed-to-pinned or explicitly flagged; neither is
invented. The *concrete off-pin drift* that pinned ACCURACY at 6 is gone.

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | Full-draft blocklist sweep (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / hype) = **0 hits**. Ownership models (strong/weak/collective, table lines 68–72) each carry strength **and** cost — no model crowned. The QA-as-separate-function vs quality-as-everyone's-job contrast (§Alternatives, line 133) is framed as "points on a spectrum, not a contest." Boy Scout Rule + Broken Windows attributed, nothing crowned. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Dedicated `## Limitations` (lines 122–129): culture is slow; correlation-not-guarantee; heuristics soft/contested; ownership trade-offs real; bus-factor a crude proxy; culture-not-a-substitute-for-tooling and vice-versa. `## When to use` (lines 135–140) carries explicit when-NOT — "ease off forcing process where it does not fit … never weaponize a culture metric." The `IMPORTANT` callout (line 52) states the hardest limitation up front: "A generative culture cannot be installed. No `mvn` goal builds trust." Every idea presented carries its when-NOT-to-use. |
| **C — SOURCE-TRACE** *(COMPILE + CODE-REVIEW dropped — module N/A)* | ✅ **PASS** | **Zero invented hard atoms:** no rule IDs, no config/ruleset keys, no tool flags, no API signatures, no GAV coordinates, and **no numeric statistic** asserted (DORA findings qualitative: "associated with", "predictive of"; the shift-left figure uses *relative* cost ordering only — never the contested 100×/Boehm multiplier). Tool versions trace correctly to the pin (Checkstyle 13.6.0, PMD 7.25.0, SpotBugs 4.10.2, JaCoCo 0.8.15, SonarQube 2026.1 LTA, ArchUnit 1.4.2, Spotless 3.6.0, NullAway 0.13.4, Error Prone 2.x). The two previously off-pin items are now **reframed-to-pinned (DORA edition, fig06_1 sidecar)** and **explicitly flagged (Smith 2001, fig06_2 sidecar + draft line 175)**; the named-source verbatims that cannot be clone-diffed are all flagged (atoms 1–8). **Flagged-not-invented satisfies the floor.** PASS. *(The named-verbatim "confirm-at-pin" status and the un-re-rendered `fig06_1.png` cap the ACCURACY cluster below, but do not FAIL the floor.)* |

**All three floors PASS.** No floor failure. The aggregate is scored on its merits below.

---

## The five clusters (score 1–10 — independent, harsh)

| # | Cluster | Score | Δ | Justification (specific) |
|---|---|---|---|---|
| 1 | **CLARITY** | **8** | = | Unchanged by the lift (citation-side fixes, not mechanism-side). The spine is clean and each step earns the next: culture-is-causal → shift-left (*when*) → ownership (*who*) → bus factor (knowledge as asset) → make-the-right-thing-easy (the lever) → the layered synthesis. Three load-bearing figures (Westrum types, shift-left cost curve, ownership models) carry structure prose handles poorly; no grey wall. The why-before-how gate passes (the two-teams hook frames the problem before any mechanism). Held to 8, not 9: the chapter leans on **forward references to ~10 unwritten chapters** (33/34/35/37/38/39, plus 1/2/3/6/19/40) to discharge its mechanism — a reader of Part I in isolation meets "clean as you code (Ch 34)", "local↔CI parity (Ch 35)", "ratcheting (Ch 38)" as promissory notes, so the *how* of several claims is deferred rather than reconstructable from this chapter alone (the 9–10 bar). |
| 2 | **ACCURACY** | **7** | **▲ +1** | **The lift moves this cluster, and the move is earned.** The 6 was pinned by **two concrete off-pin attributions** in supporting artifacts: (a) Fig 06.1 citing an unpinned **"2019 State of DevOps report"**, and (b) **Smith 2001** carrying **no SOURCE-PIN row** and **not flagged in-body**. Both are resolved: the DORA attribution is reframed to the **pinned** source (year-free) in the authoritative `fig06_1.sources.md`, and Smith is now `⚠ verify-at-pin` in both the figure sidecar and the **draft body** (line 175). The *real off-pin drift this cluster penalizes is gone* — that is the 6→7 move. Held to **7, not 8**, for two honest reasons that remain: **(a)** the chapter's load-bearing evidence is still **named-source verbatims that are attributed-but-not-clone-verified** — the epigraph (line 14), the DORA generative-culture + psychological-safety findings (line 50), Westrum's descriptors (lines 46–48), the Boy Scout wording (line 117) — all recorded `⚠ verify-at-pin` because there is no fetched DORA clone and the named books are not redistributed, so none is diffed character-for-character; a chapter whose central evidence is entirely "confirm-at-pin" cannot sit at the top of the 7–8 *"every specific fact carries a citation to the pinned source"* band. **(b)** The **rendered `fig06_1.png` still visually displays the unpinned "2019 State of DevOps"** caption until the next re-render — a real, if documented and deferred, manuscript-facing residual. A clean **7**: the concrete drift is fixed and the evidence is honestly traced-or-flagged (floor PASS), but it is not yet pin-verified-against-a-clone and one rendered pixel still lags the corrected trace — short of the 8+ "fully traced, zero drift" bar. |
| 3 | **UTILITY** | **8** | = | Unchanged. Squarely answers the question a tech lead actually has — "whose job is quality?" — with concrete, applied levers: choose an ownership model by context (small/deep → strong; flow → collective + gates; weak as the pragmatic middle); raise the bus factor deliberately (review, docs, pairing, rotation); introduce gates as enablers (fast, low-false-positive, applied to new code) not turnstiles. The `## When to use` section gives real decision frames. Held to 8: a lead can *act on the framing* but the chapter deliberately routes every enforceable mechanic elsewhere (CODEOWNERS → Ch 37, gate policy → Ch 33, ratcheting → Ch 38), so the reader leaves with orientation and a decision frame, not a runnable artifact or a step they execute today — appropriate for a culture chapter, but it caps applied utility below the 9–10 "page kept open while working" bar. |
| 4 | **DEPTH** | **7** | = | **Unchanged — the in-bounds DEPTH lift was NOT applied.** The prior lift-list item 4 asked the drafter to surface one already-verified bus-factor tension (collective ownership raises the bus factor but needs the gates to hold quality — the chapter owns both halves) so §"Knowledge is a quality asset" argues rather than lists; the section (lines 80–90) is still a definition + a list of raisers. The substance otherwise genuinely merges two dossiers (culture key 06 + bus-factor key 90) and earns a full chapter: mechanism (Westrum → DORA causality, Deming → Smith lineage, three ownership models), evidence-for, honest limitations, an approach-based Alternatives section, a when-to-use; honest about soft/contested edges (Broken Windows disputed; correlation-not-cause; bus-factor a proxy). Held to 7: the substance **stays resolutely at the principle level** — most contested depth (the mechanics of clean-as-you-code, ratcheting, gate tuning) is named and deferred, and the bus-factor treatment remains a definition + a list rather than a worked tension. Broad-and-sound rather than deep-and-contested; a defensible Part-I-closer design, but solid-7 substance, not 8+. |
| 5 | **READABILITY** | **8** | = | Unchanged. The locked voice holds: third person, invisible narrator, no first person, no narration contractions (the apostrophes are possessives; `// NOSONAR` / `--no-verify` are quoted tokens). Concrete stakes-first hook (two teams, identical stack, opposite outcomes) lands the chapter's one idea before any definition. Callouts used sparingly (IMPORTANT / NOTE / two "Trace it back" beats). Zero self-narration / meta-scaffolding tells. Two minor, honest deductions keep it at 8 not 9: **em-dash density ≈9.0 per 1,000** (target ~8 — over the soft target, flag-not-fail), and the title refrain **"Whose job is quality?" recurs ~4 times** (hook, deep-dive, when-to-use synthesis, back-matter), which begins to milk a device the voice guide says to "state once." The "make the right thing the **easy** thing" idiom (lines 92/154) sits on the edge of the banned-difficulty-word rule; it reads as the paved-path noun phrase rather than filler characterizing reader difficulty, so it is a noted risk, not a hit. |

**Cluster subtotal: 38 / 50** — no single cluster below 6 (lowest is ACCURACY and DEPTH, both at 7).

---

## Verdict

- **Aggregate: 38 / 50** (up from the 37/50 baseline; ACCURACY 6→7 on the two resolved off-pin attributions).
  Floors **A / B / C-source all PASS**. No cluster below 6.
- **Ship bar (≥44 / 50, no cluster <6, floors PASS):** **NOT CLEARED.** 38 is **6 points short** of the 44
  auto-approval bar. The floors do not block — this is a **cluster-quality miss**, so the bounded lift loop is
  the correct next move (a floor failure would instead force a prose/scope fix).

**Phase-3 chapter scorecard:** ☑ **LIFT-LOOP** (pass 1 of ≤3 complete; still below bar). The lift correctly
banked the highest-leverage fix (the two concrete off-pin attributions → ACCURACY +1), but the gap to 44 stays
concentrated in **ACCURACY (7)** — capped until the named verbatims are diffed against pinned editions *and*
`fig06_1.png` is re-rendered year-free — and **DEPTH (7)**, whose in-bounds lift (the bus-factor tension) was
**not yet applied**. *(Per the harsh independent bar this remains a clear LIFT, not a SHIP: a senior engineer
would find the chapter sound, neutral, and well-written, but would not call its evidence base "error-free"
while the central claims are still confirm-at-pin and a rendered figure still shows an unpinned report edition.)*

**One-line rationale:** Both flagged off-pin attributions are fixed (DORA reframed-to-pinned, Smith flagged), so
ACCURACY lifts 6→7 and the aggregate to 38/50 — but the evidence is still attributed-and-flagged rather than
clone-verified, one rendered figure still bakes the old edition, and the DEPTH lift was not applied, leaving the
chapter 6 short of the 44 bar.

---

## Flagged weakest cluster (for the next lift pass)

- **Weakest cluster (tied at 7):** **ACCURACY** and **DEPTH**. ACCURACY is the higher-leverage target — it is
  the cluster the bar most depends on and the one the remaining pin work directly unlocks.
- **Why ACCURACY is still capped at 7:** the concrete off-pin drift is fixed, but (1) every load-bearing
  verbatim (epigraph, Westrum, DORA findings, Boy Scout) is still `⚠ verify-at-pin` — no DORA clone, books not
  redistributed — so nothing is diffed character-for-character; and (2) the rendered `fig06_1.png` still shows
  "2019 State of DevOps" until the next deliberate re-render. "Honestly flagged" is floor-passing but is not the
  8+ "fully traced, zero drift" band.
- **Single highest-leverage move to lift ACCURACY 7→8:** at `/pin-source`, **add the two named rows the flag
  already names** — the exact DORA / State-of-DevOps edition that carries the psychological-safety finding (§5)
  and a named-article row for Smith, "Shift-Left Testing," *Dr. Dobb's* (2001) (§7) — then (a) diff the epigraph
  + Westrum + Boy Scout + DORA verbatims against the pinned editions and clear atoms 1–5, appending a VERIFIED
  line to both the flag and a chapter `_VERIFY.md`, and (b) **re-render `fig06_1.png` from HTML with the
  year-free pinned attribution** so the baked caption matches the corrected sidecar. That single pin-confirm-and-
  re-render pass converts the evidence from "attributed-and-flagged" to "pin-traced," which is the only thing
  between ACCURACY 7 and 8.
- **Second move (DEPTH 7→8), in-bounds, no new facts:** apply the still-pending lift-list item 4 — in
  §"Knowledge is a quality asset" surface the one already-owned tension (collective ownership raises the bus
  factor *but* needs the automated gates to hold quality) so the section argues rather than lists.

> Two clusters at 7 with both lift moves available and in-bounds means the 44 bar is reachable inside the
> remaining lift budget (≤3 passes total; this was pass 1): ACCURACY 7→8 (+1) and DEPTH 7→8 (+1) would reach
> 40, and a CLARITY/UTILITY/READABILITY 8→9 on the back of the now-pin-traced evidence is plausible — but the
> pin rows and the re-render are prerequisites, and they sit at `/pin-source`, partly outside the drafter's
> in-bounds reach. If the pin rows cannot be added, ACCURACY cannot clear 7 and the chapter is a cut/flag
> candidate after the budget; do not lower the bar.

---

## Line-level fixes (the lift list — for the drafter / `/pin-source`, in-bounds)

| # | Cluster / floor | Location (section · ¶ · element) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / SOURCE-TRACE | `/pin-source` + `05-figures/.../fig06_1.{html,png}` | DORA edition **reframed to pinned in the sidecar** (done), but (a) the exact edition carrying the psychological-safety finding is still `⚠ verify-at-pin` (atom 2), and (b) the **baked `fig06_1.png` still displays "2019 State of DevOps"**. | At `/pin-source` add the exact DORA/State-of-DevOps edition as a §5 row; then edit `fig06_1.html` to the **year-free pinned** attribution and re-render the PNG via `render.mjs` so the caption pixels match the corrected sidecar. Clears the sidecar PNG residual (lines 59–65). |
| 2 | ACCURACY / SOURCE-TRACE | `/pin-source` + draft line 175 + `fig06_2.sources.md` line 27 | **Smith 2001 / Dr. Dobb's** now **flagged in-body** (done — atom 4) but still has **no SOURCE-PIN row**. | Add a named-article SOURCE-PIN §7 row for Smith, "Shift-Left Testing," *Dr. Dobb's* (2001); confirm date/venue; clear flag atom 4; replace the in-body `⚠ verify-at-pin` note with the pinned cite. |
| 3 | ACCURACY | Draft line 14 (epigraph), line 50 (DORA), lines 46–48 (Westrum), line 117 (Boy Scout) | Load-bearing verbatims remain `⚠ verify-at-pin` (atoms 1,2,3,5) — attributed but not clone-verified. | Once rows 1–2 are pinned, diff each verbatim character-for-character against the pinned edition and append a VERIFIED line to both the flag and a chapter `_VERIFY.md`. |
| 4 | DEPTH | §"Knowledge is a quality asset" (lines 80–90) | **Still pending** — bus-factor treatment is a definition + a list of raisers; the merged key-90 material reads thin against the rest of the chapter. | In-bounds: surface one concrete already-verified tension (collective ownership raises the bus factor but needs the gates to hold quality — the chapter owns both halves) so the section argues rather than lists. No new facts. |
| 5 | READABILITY | Draft lines 20, 104, 140, 154 | Title refrain "Whose job is quality?" repeats ~4×; em-dash density ≈9.0/1k (target ~8). | Keep the refrain at the hook and the synthesis; drop one of the middle repeats. Convert ~3 appositive em-dashes to periods/commas to pull density under 8. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE (COMPILE/CR = N/A) | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | **37 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (flag-escape; module N/A) | LIFT-LOOP | independent baseline; harsh re-score of main-loop self 40/50 |
| 1 (indep re-score) | 2026-06-28 | **38 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (flag-escape; module N/A) | **LIFT-LOOP** | DORA attribution reframed to pinned (year-free) in `fig06_1.sources.md`; Smith 2001 flagged in-body (draft L175) + sidecar; the two concrete off-pin attributions resolved → **ACCURACY 6→7**. DEPTH lift (bus-factor tension) **not applied** → DEPTH held 7. CLARITY/UTILITY/READABILITY unchanged at 8. |

> The main-loop self `_SCORE.md` returned **40/50** (8/8/8/8/8). The independent baseline returned **37/50**;
> this independent re-score returns **38/50** after the targeted lift (ACCURACY 8→6→**7**). All three scores
> agree the chapter is **below the 44 bar** and needs further lift; none approves it.

---

## Learnings & pipeline suggestions

- **A targeted citation lift can move ACCURACY exactly one band — no further — when the residual is
  "attributed-and-flagged, not clone-verified."** Resolving the two *concrete off-pin* attributions (an
  unpinned edition; an un-rowed named article) was worth +1 (6→7): it removed real drift. It could not reach 8,
  because the remaining ceiling is the *structural* one — the load-bearing verbatims are `⚠ verify-at-pin`
  against a multi-authority pin with no clone. The lesson for a future scorer: "fixed the off-pin drift" is a
  one-band move; "diffed the verbatims against the pinned edition" is the next band. They are different lifts and
  should be scored as such — do not award both for the first.
- **A reframed *sidecar* attribution does not clear the cluster while the *baked PNG* still shows the old
  edition.** `fig06_1.sources.md` is correct and the body caption is year-free, but `fig06_1.png` still displays
  "2019 State of DevOps." A rendered figure is manuscript-facing; until it is re-rendered, ACCURACY carries the
  residual. The pipeline lesson (reinforcing the prior pass): the figure-designer gate should treat a caption
  citation change as a **re-render trigger**, not a sidecar-only edit, and should fail "sidecar corrected, PNG
  stale" as a drift state rather than a clean pass.
- **An un-applied in-bounds lift item should be reported as "carried forward," not silently dropped.** DEPTH's
  bus-factor-tension fix (lift-list item 4) was available, in-bounds, and not applied this pass, so DEPTH held at
  7 and the aggregate gained only the ACCURACY point. Naming the carried-forward item keeps the lift budget
  honest (this was pass 1 of ≤3) and tells the drafter exactly where the next point is.
- (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
