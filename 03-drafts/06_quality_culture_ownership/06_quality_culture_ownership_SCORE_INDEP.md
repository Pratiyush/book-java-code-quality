# INDEPENDENT SCORECARD — Ch 4 "Whose Job Is Quality?" (key 06, folds 90)

> **Independent (different-model) re-score** per the ship bar in `00-strategy/SCORING.md` (auto-approval
> is ≥44/50, no cluster <6, floors A/B/C-source PASS, scored by an independent gate). Deliberately
> **harsh, skeptical** review: ≥44/50 is awarded only if a senior Java engineer would find the chapter
> excellent **and** error-free. This file is the independent record; it does not replace the main-loop
> self `_SCORE.md`.
>
> **This is a RE-SCORE after the in-bounds DEPTH lift** named by the prior independent pass (39/50, this
> file's predecessor) was **applied to the artifact**. The §"Knowledge is a quality asset" bus-factor
> section has been converted from a definition-plus-list into an **argued tension** using only material
> the chapter already owned (no new facts, no padding). All five clusters are re-scored against the lifted
> artifact; the floors are re-confirmed.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score — post in-bounds DEPTH lift)
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
- **Lift/verify pass #:** lift pass applied since the 39/50 post-VERIFY re-score (the in-bounds DEPTH move)

---

## What changed since the 39/50 re-score (the lift was applied this pass)

The 39/50 scorecard named ONE confidently-in-bounds point left: *"DEPTH 7 → 8 — in §'Knowledge is a
quality asset' surface the one already-owned tension (collective ownership raises the bus factor* but *needs
the automated gates to hold quality — the chapter owns both halves) so the section argues rather than lists."*
**That lift has now been applied to the artifact** (it had been carried un-applied across passes 0–2).

What the drafter changed in `§"Knowledge is a quality asset"` (in-bounds — synthesis of already-owned material,
**zero new facts**):

- Added a paragraph that turns the ownership table back on the bus-factor section and **argues the tension
  directly**: strong ownership buys depth and accountability and pays in a factor of one; collective/weak
  ownership raises the bus factor but, per the same table, drifts without strong shared standards — *"the knob
  that lowers the bus-factor risk is the knob that raises the drift risk."* Both halves were already stated
  elsewhere in the chapter (ownership table lines, "collective ownership only works if the automated standards
  keep everyone honest"); the lift *connects* them into one argued trade-off.
- Added a resolution paragraph: the automated gates (Parts IV–IX) are **what resolve the tension** — they hold
  the standard collective ownership would otherwise leave to discipline, so a team can buy down the bus factor
  without paying the drift the open model would cost; strong ownership keeps its place for the small, deep,
  specialist area. (Synthesis of the chapter's own recurring gate-as-enabler thesis.)
- Reframed the list of raisers so **each carries its real cost** (review costs reviewer time; collective
  ownership is affordable only with the gates; pairing trades throughput; docs cost upkeep and go stale;
  rotation costs near-term familiarity) and closed with *"None of these is free, which is the point …"* — a
  standing investment weighed against throughput, not a switch.

The list of concrete raisers is **kept** (it is useful applied content) but is now framed inside the argument
rather than standing as the whole of the section. This is exactly the carried-forward in-bounds move; it imports
no deferred mechanics and creates no floor risk.

**Residuals carried, unchanged this pass (honest):**

- The copyrighted-book verbatims — *Broken Windows* (*The Pragmatic Programmer*), Vogels "you build it, you
  run it" (ACM Queue 2006), Deming "build quality in" — have **no web-public primary** and **stay flagged**
  (flag atoms 6–8). Each is attributed, asserts no quoted span (Vogels softened to "a practice popularized at
  Amazon," no year; Deming an attributed paraphrase; Broken Windows with the contested-theory caveat).
  Flagged-not-invented satisfies the floor. **Left flagged per the task constraint.**
- One cosmetic figure residual: the baked `fig06_1.png` Westrum on-figure line; the body caption and sidecar
  are correct and the "2019 State of DevOps" baked text is the *verified* pinned psych-safety edition. **PNG
  deliberately NOT re-rendered this pass** (per the task constraint); recorded as a residual.

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | Full-draft blocklist sweep (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `destroys` / `blows away` / `killer` / `no reason to use` / `obvious choice over`) = **0 hits**, re-run on the lifted draft including the new paragraphs. The lift's new argument **strengthens** neutrality: the ownership models are now explicitly held as a *trade-off where each knob costs something* ("the knob that lowers the bus-factor risk is the knob that raises the drift risk"; "Strong ownership keeps its place for the small, deep, specialist area"), none crowned. QA-as-separate-function vs everyone's-job remains "points on a spectrum, not a contest." No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Dedicated `## Limitations` (culture slow; correlation-not-guarantee; heuristics soft/contested; ownership trade-offs real; bus-factor a crude proxy; culture-not-a-substitute-for-tooling and vice-versa). `## When to use` carries explicit when-NOT ("ease off forcing process where it does not fit … never weaponize a culture metric"). The `IMPORTANT` callout states the hardest limitation up front ("A generative culture cannot be installed. No `mvn` goal builds trust."). The lift **adds** limitation content: every bus-factor raiser now names its cost, and the section closes "None of these is free." Strengthened, not weakened. |
| **C — SOURCE-TRACE** *(COMPILE + CODE-REVIEW dropped — module N/A)* | ✅ **PASS** | **Zero invented hard atoms** and **the lift introduced none** — it is pure synthesis of claims already in the chapter (the ownership table's strengths/costs; the recurring "gates hold the line" thesis; the bus-factor definition). No rule IDs / config keys / tool flags / API signatures / GAV / version / benchmark figures; **no numeric DORA/defect-cost band asserted**. The five load-bearing named verbatims remain web-verified + pinned §5/§7; copyrighted-book verbatims (atoms 6–8) carry no quoted span and stay flagged. Flagged-not-invented satisfies the floor. PASS. *(The single stale rendered figure caps ACCURACY below but does not FAIL the floor — the trace record is correct.)* |

**All three floors PASS.** No floor failure. The aggregate is scored on its merits below.

---

## The five clusters (score 1–10 — independent, harsh)

| # | Cluster | Score | Δ | Justification (specific) |
|---|---|---|---|---|
| 1 | **CLARITY** | **8** | = | The lift **sharpens** the spine: the bus-factor section now visibly pays off the ownership table ("the ownership table turns back on itself"), tying two sections the prior draft left loosely linked, and resolves the tension through the gates the chapter keeps returning to — the reader now sees *why* the chapter circles back to automation. A real clarity gain, but it does not cross into 9–10: the chapter still discharges much of its *how* through **forward references to ~10 unwritten chapters** (33/34/35/37/38/39, plus 1/2/3/6/19/40), so several mechanisms remain promissory rather than reconstructable from this chapter alone (the 9–10 bar). Held at a now-more-solid **8**. |
| 2 | **ACCURACY** | **8** | = | **Unchanged — the lift added no facts.** The central evidence stays pin-traced and verbatim-confirmed against web-public primaries (epigraph + DORA generative-culture + 2019 psychological-safety findings, Westrum 2004, Smith 2001, Boy Scout) with matching SOURCE-PIN §5/§7 rows. No numeric performance band asserted. Held to **8, not 9** for the two carried, non-fatal residuals: **(a)** `fig06_1.png` still bakes the old Westrum on-figure line until a deferred re-render — a manuscript-facing pixel that lags the corrected sidecar (so not "every artifact verified," the 9-bar); and **(b)** three copyrighted-book heuristics (Broken Windows, Vogels, Deming) remain attributed-and-flagged with no web-public primary. Both are explicitly **out of this pass's scope per the task constraints** (do-not-re-render; leave verbatims flagged). |
| 3 | **UTILITY** | **8** | = | The trade-off framing hands a tech lead a **sharper decision tool** than the prior list did: the ownership "knob," and the gate as the thing that makes the knowledge-spreading option affordable, plus the explicit "raising the bus factor is an investment weighed against throughput" close. `## When to use` still gives real decision frames (strong for small/deep/specialist; collective for flow + gates; weak as the pragmatic middle; raise the bus factor where a factor of one would hurt most). Held to **8**, not 9: the chapter by design routes every *enforceable* mechanic elsewhere (CODEOWNERS → Ch 37, gate policy → Ch 33, ratcheting → Ch 38), so the reader leaves with a strong decision frame, not a runnable artifact — appropriate for a culture chapter, but below the 9–10 "page kept open while working" bar. |
| 4 | **DEPTH** | **8** | **▲ +1** | **The carried-forward in-bounds lift is now applied and earns the band.** The bus-factor section was the single sub-8 gap across three passes: a definition + a list of raisers. It now **argues the tension the chapter owns** — the ownership knob that lowers the bus-factor risk is the same knob that raises the drift risk; the automated gates resolve it; each raiser spends something real to buy resilience down; raising the bus factor is a weighed investment, not a switch. That is the 7–8 band's "full mechanism + for + against + alternatives + when-to-use, all sourced," now with the contested *trade-off argued* rather than stated — the chapter genuinely merges two dossiers (culture key 06 + bus-factor key 90) and the merged material no longer reads thin against the rest. A clean, earned **8**. Held **off 9–10**: the chapter's deepest, most contested mechanics (clean-as-you-code, ratcheting, gate tuning) are by design **named and deferred** to later chapters, so the substance is rich-and-argued at the principle level rather than the "rich, contested, foundational deep-dive" the 9–10 anchor demands. Broad, sound, and now genuinely argued — a strong 8 for a by-design-deferring Part-I closer. |
| 5 | **READABILITY** | **8** | = | Re-measured on the lifted draft: prose em-dash density **5.95/1,000** (14 em-dashes / 2,353 prose words) — still comfortably under the ~8 target; the lift's new prose did not push it over. Title refrain "Whose job is quality?" still recurs **3×** (hook, deep-dive synthesis, back-matter). Banned-filler sweep (`easily`/`just`/`simply`/`obviously`/`of course`) = **0**; zero narration contractions (apostrophes are possessives; `// NOSONAR` / `--no-verify` are quoted tokens); no first person. The added argument keeps the locked voice and the deliberate short-sentence cadence the guide asks for ("A team cannot turn it one way for free." / "None of these is free, which is the point."), breaking monotone. Held to **8**, not 9: the "make the right thing the **easy** thing" idiom remains a **noted edge risk, not a hit** (it characterizes the paved path, not the reader's difficulty — left unchanged, correctly); and the prose, while clean and now better-paced, is steadily-good rather than effortless-at-full-precision (the 9–10 bar). |

**Cluster subtotal: 40 / 50** — no single cluster below 6 (lowest is now CLARITY/ACCURACY/UTILITY/READABILITY at 8; DEPTH lifted to 8).

---

## Verdict

- **Aggregate: 40 / 50** (up from 39/50; DEPTH 7→8 on the now-applied in-bounds bus-factor tension). Floors
  **A / B / C-source all PASS**. No cluster below 6.
- **Ship bar (≥44 / 50, no cluster <6, floors PASS):** **NOT CLEARED.** 40 is **4 points short** of the 44
  auto-approval bar. The floors do not block — this is a **cluster-quality miss**.

**Phase-3 chapter scorecard:** ☑ **LIFT** (still below bar). The applied lift banked exactly the +1 the prior
pass said was the one confidently-in-bounds point left (DEPTH 7→8). The chapter is now an even, all-8 profile
with every floor passing — sound, neutral, pin-traced, and now genuinely argued where it used to list — but
**4 points short of the 44 auto-approval bar**, and the remaining gap is **not honestly reachable in-bounds**.

### Is 42/50 reachable in-bounds? No — and here is exactly what each remaining point would cost.

The aggregate is **40**. Reaching **42** needs **+2** beyond the all-8 floor; reaching the actual ship bar
(**44**) needs **+4**. Every remaining point requires either out-of-scope work or a 8→9 lift this
by-design-deferring chapter cannot earn without breaking an in-bounds rule:

1. **ACCURACY 8 → 9 (+1) — partly OUTSIDE in-bounds reach, and explicitly out of this pass's scope.**
   Requires (a) re-rendering `fig06_1.png` year-free from HTML via `render.mjs` so the baked figure matches
   the corrected sidecar — **excluded by the do-not-re-render constraint** — **and** (b) clearing the three
   copyrighted-book attributions (Broken Windows / Vogels / Deming), which needs the named editions in `_ref/`,
   not a web source — **excluded by the leave-them-flagged constraint** (and partly outside the drafter's
   reach). The "zero drift, every artifact verified" 9-bar is unreachable while one rendered figure lags and
   three secondary verbatims stay flagged.
2. **DEPTH 8 → 9 (+1) — not reachable in-bounds.** The 9–10 anchor is "rich, contested, foundational deep-dive."
   This chapter's deepest contested material (clean-as-you-code mechanics, ratcheting, gate tuning) is
   **intentionally deferred** to Chs 33/34/35/38. Pulling it forward to deepen this chapter is **scope creep**
   (it imports the very mechanics this Part-I closer hands to later chapters) — an in-bounds violation, not a
   lift. DEPTH is now a correct, earned **8** and cannot honestly go higher without that import.
3. **CLARITY / UTILITY / READABILITY 8 → 9 (+1 each) — not reachable in-bounds.** Each is a genuine 8 held off
   9 by the chapter's **deliberate design**: it discharges its *how* through forward references, routes every
   enforceable mechanic to a later chapter, and reads clean-not-effortless. Lifting any to 9 would require the
   chapter to carry more of its own *how* — again importing deferred mechanics (scope creep) — or a prose
   polish that does not, by itself, cross a genuine 8 into a genuine 9.

**Honest budget read.** DEPTH 7→8 was the last confidently-in-bounds point, and it has been spent (→40). **42
is not honestly reachable in-bounds**, and **44 (the real bar) is +4 away** through lifts that are either
explicitly out of scope this pass (the figure re-render, the copyrighted-edition diffs) or that this
by-design-deferring culture chapter cannot earn without scope creep or floor risk. A senior engineer would find
this chapter **sound, neutral, well-written, pin-traced, and now genuinely argued at the bus-factor section** —
but would not call it *excellent and error-free* at the 44 bar while one rendered figure bakes the old edition,
three secondary verbatims remain flagged, and the chapter (correctly) defers its deepest mechanics. **LIFT, not
SHIP.**

### Exactly what is needed to clear the bar (named, since 42/44 is not in-bounds reachable)

- **The two out-of-scope, mostly-mechanical points (ACCURACY 8→9):** re-render `fig06_1.png` year-free via
  `render.mjs` (figure-designer), **and** run the `_ref/` close-paraphrase check against the named editions to
  clear flag atoms 6–8 (Broken Windows / Vogels / Deming). Both were ring-fenced out of this pass by the task
  constraints; together they are worth **at most +1** (ACCURACY → 9 → aggregate 41).
- **The remaining +3 to 44 is a structural-design question for the human gate, not a drafter lift.** It can only
  come from CLARITY / UTILITY / DEPTH / READABILITY 8→9, each of which is capped by this chapter's intentional
  deferral of its mechanics. Reaching 44 would mean **changing the chapter's design** — having this Part-I
  closer carry more of its own enforceable *how* instead of routing it to Chs 33/34/35/37/38 — which is an
  editorial scope decision, not an in-bounds prose pass.

**Recommendation:** the lift loop has now spent its one confidently-in-bounds point. Per `SCORING.md`'s bounded
lift loop, the remaining gap to 44 is **not reachable in-bounds without scope creep or floor risk**, so this is
a **cut/flag candidate for the human gate** — escalate the structural-ceiling decision (does a pure Part-I
culture chapter clear an 88% bar that rewards self-contained mechanism, or is this chapter's ceiling ~40–41 by
design?) rather than continuing to loop. **Do not lower the bar, do not pad, do not import the later chapters'
mechanics to inflate the score.**

**One-line rationale:** The carried-forward DEPTH bus-factor tension is now applied — the section argues
(collective ownership raises the bus factor *but* drifts without the gates, which resolve the tension) instead
of listing — so DEPTH earns 7→8 and the aggregate reaches **40/50**, but 42 is **not honestly reachable
in-bounds**: the remaining +4 to the 44 bar runs through an ACCURACY 8→9 that is explicitly out of this pass's
scope (the figure re-render + copyrighted-edition diffs) and three further 8→9 lifts this by-design-deferring
culture chapter cannot earn without scope creep, leaving it 4 short.

---

## Flagged weakest cluster (for the human gate / next decision)

- **Weakest clusters:** now an **even all-8 profile** (no single sub-8 cluster remains). The former sole gap,
  DEPTH, is closed.
- **No confidently-in-bounds point remains.** The one carried in-bounds move (DEPTH 7→8) has been applied. Every
  further point requires out-of-scope work (figure re-render; `_ref/` edition diffs) or a 8→9 lift the chapter's
  intentional deferral of mechanics forbids in-bounds.
- **Beyond this pass (recorded so the budget stays honest):**
  - **ACCURACY 8→9 (out of scope this pass):** re-render `fig06_1.png` year-free (figure-designer / `render.mjs`)
    so the baked figure matches the corrected sidecar; and confirm the three copyrighted-book verbatims against
    their named editions (`_ref/` corpus, close-paraphrase check only) to clear flag atoms 6–8. Needs the
    editions/figure tooling, not a web source.
  - **CLARITY / UTILITY / DEPTH / READABILITY 8→9:** each is capped by the chapter's deliberate deferral of its
    enforceable mechanics; lifting any to 9 without scope creep is the structural question to escalate.

> **Budget honesty.** This pass spent the lift loop's last confidently-in-bounds point (DEPTH 7→8 → 40). The
> further +4 to 44 is **not reachable in-bounds** without importing deferred mechanics (scope creep) or work
> ring-fenced out of this pass (the figure re-render + copyrighted-edition diffs). Per the bounded lift loop,
> the chapter is now a **cut/flag candidate for the human gate**: escalate whether a by-design-deferring Part-I
> culture chapter can clear the 88% bar, rather than continuing to loop. Do not lower the bar, do not pad, do
> not import later chapters' mechanics to inflate DEPTH.

---

## Line-level fixes (status after the lift)

| # | Cluster / floor | Location | Status |
|---|---|---|---|
| 1 | **DEPTH** (in-bounds) | §"Knowledge is a quality asset" | **APPLIED this pass.** Converted from definition-plus-list into an argued tension (the ownership knob that lowers bus-factor risk raises drift risk; the gates resolve it; each raiser carries a real cost) using only already-owned material. No new facts. DEPTH 7→8. |
| 2 | ACCURACY (figure-designer) | `05-figures/06_quality_culture_ownership/fig06_1.{html,png}` | **Carried — out of scope this pass** (do-not-re-render constraint). Baked Westrum on-figure line lags the corrected sidecar; body caption + sidecar correct; "2019" baked text is the verified pinned edition. Re-render year-free at the next deliberate pass to enable ACCURACY 8→9. |
| 3 | ACCURACY / SOURCE-TRACE (legal/`_ref` reach) | Draft lines (Broken Windows / Vogels / Deming) | **Carried — left flagged per task constraint.** Atoms 6–8 attributed-and-flagged, no web-public primary; each asserts no quoted span. Confirm against named editions in `_ref/` (close-paraphrase check only) to clear, alongside fix #2, for ACCURACY 8→9. |
| 4 | (Watch — not a fix) READABILITY | Heading "Make the right thing the easy thing" | **No change required** (left unchanged). Reads as the paved-path noun phrase characterizing the path, not the reader's difficulty. Noted edge risk only, consistent with prior passes. |

---

## Lift / verify-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE (COMPILE/CR = N/A) | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | **37 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (flag-escape; module N/A) | LIFT | independent baseline; harsh re-score of main-loop self 40/50 |
| 1 (indep) | 2026-06-28 | **38 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (flag-escape; module N/A) | LIFT | DORA attribution reframed to pinned in `fig06_1.sources.md`; Smith 2001 flagged in-body. Two off-pin attributions resolved → **ACCURACY 6→7**. DEPTH lift not applied. |
| 2 (indep, post-VERIFY) | 2026-06-28 | **39 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (5 verbatims pin-traced; atoms 6–8 flag-escape; module N/A) | LIFT | Web-verify pass: epigraph reframed to a verbatim DORA statement; DORA/2019 + Westrum 2004 + Smith 2001 + Boy Scout web-verified + SOURCE-PIN §5/§7 rows → **ACCURACY 7→8**. READABILITY residuals cleared (em-dash 5.52/1k, refrain 3×). DEPTH tension **not yet applied** → DEPTH 7. |
| 3 (indep, post-DEPTH-lift) | 2026-06-28 | **40 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (no new facts; atoms 6–8 flag-escape; module N/A) | **LIFT** | **DEPTH lift applied:** §"Knowledge is a quality asset" rewritten from a list into an argued tension (ownership knob lowers bus-factor risk / raises drift risk; gates resolve it; each raiser carries a cost) from already-owned material, no new facts → **DEPTH 7→8**. CLARITY/UTILITY sharpened but held at 8; READABILITY re-measured **5.95/1k**, refrain 3×, held 8. Floors A/B/C-source re-confirmed PASS (lift strengthens A & B). **3 lift passes now spent.** |

> Self `_SCORE.md` returned **40/50** (8/8/8/8/8). Independent baseline 37 → 38 → 39 → **40/50**. All scores
> agree the chapter is **below the 44 bar**; none approves it. Independent and self have **converged exactly to
> 40/50** — the disagreement (the ACCURACY band, then the DEPTH band) is resolved.

---

## Learnings & pipeline suggestions

- **The carried-forward in-bounds DEPTH lift was real, worth exactly +1, and is now spent.** It survived three
  passes as "available, un-applied," which the prior scorecard correctly read as a signal not an oversight.
  Applying it converted the bus-factor section from a list into an argued trade-off and moved DEPTH 7→8 — and
  no further. The lift loop has now used its **third and last pass** (`SCORING.md`'s ≤3-pass cap); the chapter
  is at 40/50 and the bounded loop is exhausted in-bounds. The honest next move is the **human gate**, not a
  fourth pass.
- **A pure Part-I culture/process chapter that by design defers its mechanics has a structural ceiling at ~40
  (even all-8), not 44.** This chapter is now an even all-8 with every floor passing — the best an in-bounds
  pass can produce — yet sits 4 short of the 88% auto-approval bar, because **three of five clusters
  (CLARITY/UTILITY/READABILITY) and the upper half of DEPTH are design-capped** by routing every enforceable
  mechanic to a later chapter (correct for a Part-I closer). Pipeline suggestion: the ship bar may need a
  **declared carve-out or a human-gate path** for the small number of pure-culture/process chapters whose 88%
  is unreachable without scope creep — otherwise the loop spends budget chasing 8→9 lifts the chapter's design
  forbids. Flag this class early so the budget is not wasted.
- **In-bounds synthesis can lift DEPTH without touching ACCURACY or the floors — and can even strengthen the
  floors.** This lift added zero facts (pure synthesis of owned material) yet *strengthened* NEUTRALITY (the
  ownership models are now explicitly held as costed trade-offs, none crowned) and HONEST-LIMITATIONS (each
  raiser now names its cost; "None of these is free"). A clean template for a no-new-facts DEPTH lift:
  *connect two halves the chapter already states into one argued tension, then resolve it with the chapter's own
  recurring thesis.*
- **Two named, out-of-pass points remain for ACCURACY 8→9** (re-render `fig06_1.png` year-free; clear
  copyrighted-book atoms 6–8 via `_ref/` edition diffs) — both ring-fenced out of this pass by constraint, worth
  at most +1 together. Even taken, the chapter reaches ~41, still short of 44 — confirming the structural
  ceiling above.
- (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
