# INDEPENDENT SCORECARD — Ch 4 "Whose Job Is Quality?" (key 06, folds 90)

> **Independent (different-model) re-score** per the ship bar in `00-strategy/SCORING.md` (auto-approval
> is ≥44/50, no cluster <6, floors A/B/C-source PASS, scored by an independent gate). Deliberately
> **harsh, skeptical**: ≥44/50 is awarded only if a senior Java engineer would find the chapter
> **excellent and error-free**. This file is the independent record; it does not replace the main-loop
> self `_SCORE.md`.
>
> **This is a RE-SCORE after three named updates since the 40/50.** Per the task brief and **verified
> directly against the artifact + the pin + the rendered PNG** (not taken on report):
> (1) the book verbatims are paraphrased and Vogels is web-verified; (2) DORA / Westrum-2004 / Smith are
> pinned on SOURCE-PIN §5/§7; (3) `fig06_1.png` was **re-rendered** to the printed "Figure 4.1" form (the
> stale Fig-number is fixed). All five clusters are re-scored against the current artifact; the floors are
> re-confirmed; the pin and the rendered figure were inspected directly.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score — post figure-re-render + pin-add + verbatim-resolution)
- **Dossier key:** 06 (folds 90 — bus/truck factor) · printed **Chapter 4**, closes Part I
- **Slug:** `06_quality_culture_ownership`
- **Artifact scored:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Companion module:** **none** — culture/process chapter; EXAMPLE-BUILD = **N/A**. FLOOR C COMPILE +
  CODE-REVIEW clauses **do not attach**; FLOOR C reduces to **SOURCE-TRACE only** for this chapter.
- **Verified directly this pass:**
  - **SOURCE-PIN.md** inspected — §5 now carries the **DORA generative-culture + 2019 psychological-safety**
    row and the **Westrum 2004** row (both ✅ pinned 2026-06-28); §7 carries the **Smith 2001** and **Boy Scout
    2010** named-article rows (both ✅ web-verified 2026-06-28). `grep` for Vogels / "you build it" / Deming /
    "out of the crisis" on SOURCE-PIN.md returns **zero matches** — both remain **off the pin**.
  - **`fig06_1.png`** opened and read directly (re-rendered 2026-06-28, newer than the sidecar). The printed
    title now reads **"Figure 4.1"** (stale Fig-number fixed — confirmed). The DORA callout asserts no numeric
    band. **The figure-body footer still reads "Westrum typology (Ron Westrum, 1988) as described in
    *Accelerate* …"** — the superseded **1988** label the brief warned about is baked into the rendered,
    manuscript-facing PNG, diverging from the corrected body caption (2004) and the §5 pin row.
- **Gate reports on disk:** `_VERIFY.md` (web-verify PASS, 2026-06-28), `_EXAMPLE.md` (PASS / N/A), the three
  figure sidecars (`fig06_1.sources.md` itself flags the on-figure Westrum line should read 2004),
  `09-flags/06_culture_named_source_verbatims_verify_at_pin.md`. No standalone `_CLARITY.md` / `_AUDIT.md` —
  those criteria judged from the artifact + VERIFY + the scripted sweeps below.
- **Scorer:** chapter-scorer agent (independent, harsh)
- **Date:** 2026-06-28
- **Lift/verify pass #:** post-figure-re-render re-score (the 5th independent scoring event on this artifact)

---

## What changed since the 40/50, and what I verified directly

The prior independent pass (40/50) held **ACCURACY at 8** on **two independent** residuals: (a) `fig06_1.png`
baked a superseded Westrum edition label, and (b) the named-source rows were web-verified-**at-use** but not all
**pin-traced**. The brief reports three updates; I checked each against the artifact, the pin, and the rendered
PNG rather than taking them on report.

- **Pin completeness for the central evidence — GENUINELY IMPROVED.** The §5/§7 rows for **DORA + 2019
  psych-safety, Westrum 2004, Smith 2001, Boy Scout 2010** are now present on SOURCE-PIN.md, web-verified
  2026-06-28. The central evidence has moved from web-verified-at-use to **pin-traced**. This closes the
  *central-evidence* half of sub-cap (b).
- **Figure re-render — NUMBER fixed, BODY LABEL still stale (the brief's warned residual is real).** The PNG
  was re-rendered (file mtime 12:24, after the sidecar at 10:50). The printed **number is corrected to "Figure
  4.1."** But the re-render did **not** fix the figure *body*: the footer still bakes **"Ron Westrum, 1988."**
  The corrected draft body (line 40) and the §5 pin both carry **2004** as the citation DORA/*Accelerate* use,
  with 1988 as the conference *origin*. So a manuscript-facing rendered artifact still drifts from the body
  caption and the pin. `fig06_1.sources.md` (lines 69–73) names this exact residual: "set the on-figure
  Westrum citation to the 2004 BMJ paper."
- **Verbatim resolution — confirmed, floor-protective, not aggregate-moving.** Broken Windows (line 124) and
  Deming (line 56) are genuine attributed paraphrases ("in this book's words" / "as this book reads him", no
  quoted span). Vogels (line 78) is web-verified (ACM Queue 2006 + AWS News Blog corroboration) cited
  dated-at-use. The flag is RESOLVED. This retired a legal/quoted-span risk — real and necessary — but Vogels
  and Deming are still **not on the pin** (grep-confirmed zero matches), so they stay web-verified/attributed-
  **at-use**, not pin-traced.

**Net effect.** The central-evidence pin rows are now in place (good), but the two things that actually capped
ACCURACY below 9 are **not both cleared**: the figure body still bakes 1988 (drift, unchanged in substance —
re-rendered but not corrected), and two named-source rows (Vogels/Deming) remain off the pin. ACCURACY stays
**8**, and with the other four clusters each a design-capped 8, the aggregate holds at **40/50**.

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | Verdict | Evidence (scripted sweep + direct read this pass) |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Full-draft blocklist sweep (`better than` / `unlike <Name>` / `the problem with` / `superior` / `beats` / `outperforms` / `blows away` / `obvious choice over` / `no reason to use` / `destroys` / `killer of`) = **0 comparative hits**. The one `kills` hit (line 66, "Diffuse responsibility **kills** quality") is **not** a rival-denigrating verdict — no alternative is named or ranked; it is a rhetorical claim about diffusion, not "X kills Y." Ownership models held as a costed trade-off, none crowned ("the knob that lowers the bus-factor risk is the knob that raises the drift risk"; "Strong ownership keeps its place for the small, deep, specialist area"). QA-as-separate-function vs everyone's-job = "points on a spectrum, not a contest." No comparative superlative in any heading. The resolved Vogels/Deming/Broken-Windows paraphrases attribute and crown nothing. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated `## Limitations` (culture slow; correlation-not-guarantee; heuristics soft/contested; ownership trade-offs real; bus-factor a crude proxy; culture-not-a-substitute-for-tooling and vice-versa). `## When to use` carries explicit when-NOT ("ease off forcing process where it does not fit … never weaponize a culture metric"). The `IMPORTANT` callout states the hardest limitation up front ("A generative culture cannot be installed. No `mvn` goal builds trust."). Every bus-factor raiser names its cost; the section closes "None of these is free, which is the point." |
| **C — SOURCE-TRACE** *(COMPILE + CODE-REVIEW dropped — module N/A)* | **PASS** | **Zero invented hard atoms.** Pin inspected directly: DORA generative-culture + 2019 psych-safety + Westrum 2004 are pinned §5 rows; Smith 2001 + Boy Scout 2010 are pinned §7 named-article rows (all web-verified 2026-06-28). No rule IDs / config keys / tool flags / API signatures / GAV / version / benchmark figures; **no numeric DORA/defect-cost band asserted** (key-85 guard held). The three previously-flagged named sources are floor-passing by the flag-escape route: Broken Windows + Deming are genuine attributed paraphrases (no quoted span to trace); Vogels is a web-verified short coinage cited dated-at-use. The flag is RESOLVED. *Trace-completeness note (caps ACCURACY, does NOT FAIL the floor):* **Vogels and Deming are confirmed absent from SOURCE-PIN.md** (grep = 0 matches), and `fig06_1.png` still bakes the superseded 1988 Westrum label — the trace **record** (sidecar + pin + body caption) is correct and nothing is invented, so the floor PASSES; these are pin/render-completeness gaps, scored against ACCURACY below. |

**All three floors PASS.** No floor failure. The aggregate is scored on its merits below.

---

## The five clusters (score 1–10 — independent, harsh)

| # | Cluster | Score | Δ | Justification (specific) |
|---|---|---|---|---|
| 1 | **CLARITY** | **8** | = | The spine is clean and the bus-factor section visibly pays off the ownership table ("the ownership table turns back on itself") and resolves the tension through the gates the chapter keeps returning to. A solid 8. Held off 9–10 because the chapter discharges much of its *how* through **forward references to ~13 distinct chapters** (33/34/35/37/38/39 + 1/2/3/6/19 + Parts IV–IX) — confirmed by a `grep` of the draft — so several mechanisms stay promissory rather than reconstructable from this chapter alone (the 9–10 bar: "a reader who never met the topic can reconstruct how it works from the chapter alone"). |
| 2 | **ACCURACY** | **8** | = | **The central-evidence pin rows are now in place; one of the two capping residuals is reduced, but neither is fully clear.** The core evidence is pin-traced and verbatim-confirmed (epigraph + DORA generative-culture + 2019 psych-safety, Westrum 2004, Smith 2001, Boy Scout 2010 — all on §5/§7, inspected directly). No numeric performance band asserted. Broken Windows + Deming are clean attributed paraphrases; Vogels is web-verified — the legal/quoted-span cap is gone. **Held at 8, not 9, for the exact remaining gap (verified directly, two concrete drifts):** (a) **`fig06_1.png` was re-rendered but still bakes "Ron Westrum, 1988"** in its body footer — a manuscript-facing rendered artifact that drifts from the corrected body caption (2004) and the §5 pin row; the re-render fixed the *number* (now "Figure 4.1") but not the *edition label* the sidecar itself flags; and (b) **Vogels and Deming are confirmed off the pin** (`grep` = 0 matches) — web-verified/attributed at use (floor-passing) but **not pin-traced**. The 9-bar is "fully traced, zero drift." A drifted rendered PNG plus two not-yet-pinned named-source rows is exactly the drift that holds a genuine 8 below a genuine 9. |
| 3 | **UTILITY** | **8** | = | The trade-off framing hands a tech lead a sharp decision tool: the ownership "knob," the gate as the thing that makes knowledge-spreading affordable, and the "raising the bus factor is an investment weighed against throughput" close. `## When to use` gives real decision frames (strong for small/deep/specialist; collective for flow + gates; weak as the pragmatic middle). Held to **8**, not 9: by design the chapter routes every *enforceable* mechanic elsewhere (CODEOWNERS → Ch 37, gate policy → Ch 33, ratcheting → Ch 38), so the reader leaves with a strong decision frame, not a runnable artifact — below the 9–10 "page kept open while working" bar. |
| 4 | **DEPTH** | **8** | = | The carried-forward in-bounds lift (applied in an earlier pass) is correctly banked: §"Knowledge is a quality asset" **argues the tension the chapter owns** (the ownership knob that lowers the bus-factor risk is the same knob that raises the drift risk; the gates resolve it; each raiser spends something real). That is the 7–8 band's "full mechanism + for + against + alternatives + when-to-use," now with the trade-off *argued* rather than listed, genuinely merging the two dossiers (culture key 06 + bus-factor key 90). Held **off 9–10**: the chapter's deepest contested mechanics (clean-as-you-code, ratcheting, gate tuning) are **by design named and deferred** to later chapters, so the substance is rich-and-argued at the principle level, not the "rich, contested, foundational deep-dive" the 9–10 anchor demands. A clean, earned 8. |
| 5 | **READABILITY** | **8** | = | Banned-filler sweep (`easily`/`just`/`simply`/`obviously`/`of course`/`basically`) = **0 condescension hits**; the three `easy` hits are all the sanctioned paved-path idiom ("make the right thing the **easy** thing"), characterizing the path not the reader's difficulty (noted edge risk, leans on the word 3×). Title refrain "Whose job is quality?" recurs **3×** (hook, deep-dive synthesis, back-matter) — controlled. **Body-prose em-dash density measured this pass = 7.90 / 1,000** (excluding the front-matter metadata comment and table rows) — **just under** the ~8/1,000 house target, but not comfortably; the appositive cadence is visibly heavy in the bus-factor section (≥5 appositive dashes in two paragraphs), and the appositive em-dash is the most-cited AI tell in VOICE-GUIDE. No narration contractions; no first person (the `grep` "I/we" hits are all "Part I"/"Parts IV", false positives); the locked voice holds, with deliberate short-sentence cadence ("A team cannot turn it one way for free." / "None of these is free, which is the point.") breaking monotone. Held to **8**, not 9: clean and well-paced rather than effortless-at-full-precision — the at-target-not-under em-dash load and the 3× `easy` idiom are exactly what separates a genuine 8 from the 9–10 "effortless at full precision" bar. |

**Cluster subtotal: 40 / 50** — no single cluster below 6 (even all-8 profile).

---

## Verdict

- **Aggregate: 40 / 50** (unchanged from the prior 40/50). Floors **A / B / C-source all PASS**. No cluster
  below 6.
- **Ship bar (≥44 / 50, no cluster <6, floors PASS):** **NOT CLEARED.** 40 is **4 points short** of the 44
  auto-approval bar. The floors do not block — this is a **cluster-quality miss**.

**Phase-3 chapter scorecard:** **LIFT** (still below bar) → escalating to the **human gate** (lift loop spent;
the remaining points are not reachable in-bounds this pass).

**Why the three updates did not move the aggregate (the honest read).** The pin-adds and the verbatim
resolution were real and correct: the central evidence is now pin-traced, and a legal/quoted-span risk is
retired. The figure re-render fixed the printed **number** (now "Figure 4.1"). But ACCURACY was held at 8 by
two independent drifts, and this pass cleared **neither completely**: the figure was re-rendered but **still
bakes the 1988 edition label** in its body, and Vogels/Deming are **still off the pin** (both directly
verified). One of the two sub-residuals (central-evidence pinning) genuinely improved, but the two *capping*
items — figure-body drift and two unpinned named rows — both persist, so ACCURACY stays 8 and the aggregate
holds at 40.

### The exact remaining gap (named precisely)

This is a **by-design-deferring Part-I culture closer** that tops out at an **even all-8 (40/50)**. The gap to
44 has exactly two parts:

1. **ACCURACY 8 → 9 (+1) — the only in-principle-reachable point, blocked by two concrete residuals, one of
   which is now out-of-bounds for a prose pass.** (a) Re-render `fig06_1.png` from `fig06_1.html` via
   `render.mjs` with the on-figure Westrum line set to the **2004** BMJ paper (the body still bakes 1988 after
   this pass's re-render) — a **figure-designer task**, not an in-bounds drafter prose pass; **and** (b) add the
   **Vogels** (ACM Queue / AWS-blog) row and, if it is to be cited as canon, the **Deming** *Out of the Crisis*
   row to **SOURCE-PIN.md §7** — a **`/pin-source` follow-up**, not a prose pass. Worth **at most +1** →
   aggregate 41.
2. **The remaining +3 to 44 (CLARITY / UTILITY / DEPTH / READABILITY 8 → 9) — NOT reachable in-bounds.** Each
   is a genuine 8 held off 9 by the chapter's deliberate design: it discharges its *how* through forward
   references to ~13 chapters, routes every enforceable mechanic to a later chapter, and reads
   clean-not-effortless (em-dash load at target, not under; the `easy` idiom 3×). Lifting any to 9 would
   require the chapter to carry more of its own enforceable *how* — **importing the very mechanics this Part-I
   closer hands to Chs 33/34/35/37/38 (scope creep)** — or a prose polish that does not, by itself, cross a
   genuine 8 into a genuine 9. This is a **structural-design question for the human gate**, not a drafter lift.

**Honest budget read.** The bounded lift loop has spent all three passes (37 → 38 → 39 → 40), and this is a
re-score at 40 after out-of-pass work (figure re-render + pin-adds + verbatim resolution). Even taking the
out-of-pass ACCURACY point, the chapter reaches **~41 — still 3 short of 44**. Per `SCORING.md`'s bounded lift
loop, the remaining gap is **not reachable in-bounds without scope creep or out-of-pass work**, so this is a
**cut/flag candidate for the human gate**. A senior engineer would find this chapter **sound, neutral,
well-written, pin-traced on its central evidence, and genuinely argued at the bus-factor section** — but would
not call it *excellent and error-free at the 44 bar* while one rendered figure still bakes a superseded
edition, two named-source rows sit web-verified-but-not-pinned, and the chapter (correctly) defers its deepest
mechanics. **LIFT → escalate. Do not lower the bar, do not pad, do not import later chapters' mechanics to
inflate the score.**

---

## Flagged weakest cluster (for the human gate / next decision)

- **Weakest clusters:** an **even all-8 profile** — no single sub-8 cluster. The cluster with in-principle
  headroom is **ACCURACY (8)**, blocked by two concrete residuals both verified directly this pass.
- **No confidently-in-bounds prose point remains.** The one carried in-bounds move (DEPTH 7→8) was applied in
  an earlier pass; the figure re-render + pin-adds this round were out-of-pass mechanical/legal work, not an
  aggregate lift. Every further point requires out-of-pass work (a corrected figure re-render; the
  `/pin-source` rows) or an 8→9 lift the chapter's intentional deferral of mechanics forbids in-bounds.
- **Single highest-leverage move (out of this pass's scope):** re-render `fig06_1.png` with the on-figure
  Westrum line set to **2004** via `render.mjs` (figure-designer) **and** add the Vogels (and, if cited as
  canon, Deming) rows to SOURCE-PIN §7 at the next `/pin-source` — together worth at most ACCURACY 8→9
  (aggregate → 41). The remaining +3 to 44 is the structural question to escalate: *can a pure Part-I
  culture/process chapter that by design defers its mechanics clear an 88% bar that rewards self-contained
  mechanism, or is its honest ceiling ~40–41?*

---

## Line-level fixes (status after this pass)

| # | Cluster / floor | Location | Status |
|---|---|---|---|
| 1 | SOURCE-TRACE / legal (RESOLVED) | Draft lines 56 / 78 / 124 (Deming / Vogels / Broken Windows) | **RESOLVED.** Deming + Broken Windows are genuine attributed paraphrases (no quoted span); Vogels is web-verified (ACM Queue 2006 + AWS blog) cited dated-at-use. Flag RESOLVED. Floor C PASS. |
| 2 | ACCURACY (figure-designer) | `05-figures/06_quality_culture_ownership/fig06_1.{html,png}` | **STILL OPEN — verified directly.** The PNG was re-rendered (number now "Figure 4.1"), but the body footer still bakes **"Ron Westrum, 1988."** Re-render with the on-figure Westrum line set to the **2004** BMJ paper to match the corrected body caption + the §5 pin row → enables ACCURACY 8→9. Out of scope for an in-bounds prose pass. |
| 3 | ACCURACY (pin completeness) | SOURCE-PIN.md §7 (Vogels; Deming) | **STILL OPEN — verified directly (`grep` = 0 matches).** Central-evidence rows are now pinned, but **Vogels and Deming are not on the pin.** Web-verified/attributed at use (floor-passing) but not pin-traced. Add both (a `/pin-source` follow-up) to convert traced-at-use → pin-traced, alongside fix #2, for ACCURACY 8→9. |
| 4 | (Watch — not a fix) READABILITY | em-dash cadence (bus-factor §, lines 84–96) + heading "Make the right thing the easy thing" | **No hard change required.** Body-prose em-dash density = 7.90/1,000 (under the ~8 target but not comfortably; appositive cadence heavy in the bus-factor paragraphs). The `easy` idiom recurs 3× — sanctioned paved-path phrasing, not condescension. Noted edge risks that hold READABILITY at 8; neither breaches a floor or the soft target. |

---

## Lift / verify-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE (COMPILE/CR = N/A) | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | **37 / 50** | PASS | PASS | PASS (flag-escape; module N/A) | LIFT | independent baseline; harsh re-score of main-loop self 40/50 |
| 1 (indep) | 2026-06-28 | **38 / 50** | PASS | PASS | PASS (flag-escape; module N/A) | LIFT | DORA attribution reframed to pinned; Smith 2001 flagged in-body → **ACCURACY 6→7**. |
| 2 (indep, post-VERIFY) | 2026-06-28 | **39 / 50** | PASS | PASS | PASS (5 verbatims pin-traced; atoms 6–8 flag-escape) | LIFT | Web-verify pass: epigraph reframed to a verbatim DORA statement; DORA/2019 + Westrum 2004 + Smith 2001 + Boy Scout web-verified + §5/§7 rows → **ACCURACY 7→8**. READABILITY residuals cleared. DEPTH 7. |
| 3 (indep, post-DEPTH-lift) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (no new facts; atoms 6–8 flag-escape) | LIFT | DEPTH lift applied: §"Knowledge is a quality asset" rewritten list → argued tension → **DEPTH 7→8**. **3 lift passes spent.** |
| 4 (indep, post-verbatim-resolution) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (verbatims resolved) | LIFT → human gate | Broken Windows + Deming → attributed paraphrases; Vogels web-verified. Removed the legal sub-cap; ACCURACY stays 8 (stale figure + Vogels/Deming not pinned). |
| 5 (indep, post-figure-re-render + pin-add) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (central evidence now pinned §5/§7; Vogels/Deming off-pin; figure re-rendered but body label stale) | **LIFT → human gate** | **Verified directly:** central-evidence §5/§7 rows now on the pin (good); `fig06_1.png` re-rendered — **number fixed to "Figure 4.1" but body still bakes "Westrum 1988"**; Vogels/Deming confirmed **off the pin** (`grep`=0). The two ACCURACY *capping* residuals both persist → **ACCURACY holds at 8**, aggregate **holds at 40**. Lift loop exhausted in-bounds → escalate. |

> Self `_SCORE.md` returned **40/50** (8/8/8/8/8). Independent: 37 → 38 → 39 → 40 → 40 → **40/50**. Independent
> and self agree exactly at **40/50**; all scores agree the chapter is **below the 44 bar** and none approves it.

---

## Learnings & pipeline suggestions

- **A re-render that fixes the figure NUMBER is not the same as fixing the figure BODY.** `fig06_1.png` was
  re-rendered this round and the printed number is now correct ("Figure 4.1"), which can read as "the figure
  residual is resolved" — but the body footer still bakes the superseded "Westrum 1988" label the sidecar
  itself flags. Pipeline suggestion: when a figure is re-rendered to fix one baked atom, **re-check every baked
  atom the sidecar lists as a residual** in the same pass; a partial re-render leaves a manuscript-facing drift
  that still caps ACCURACY.
- **Web-verified-at-use ≠ pin-traced — and this is now verifiable by grep.** The ACCURACY 9-bar is "fully
  traced, zero drift." A direct `grep` of SOURCE-PIN.md confirms Vogels and Deming have **no row**; both are
  honest flag-escape/attributed-paraphrase (floor-passing) but not pinned, which correctly holds ACCURACY at 8.
  Pipeline suggestion: when a `/pin-source` follow-up is the only thing between traced-at-use and pin-traced for
  an otherwise-clean chapter, **schedule it before the human gate** so the chapter is not held a point short by
  a clerical pin-row addition — and verify the row exists by grep, not by report.
- **Enumerate every sub-cap, and re-verify each independently each pass.** ACCURACY here was gated by *two*
  residuals (stale figure + unpinned rows) plus a now-cleared third (central-evidence pinning). Clearing one (or
  partially clearing one, as the figure re-render did) does not move the band. A scorecard that caps a cluster
  should list every sub-cap with a check method (grep the pin; open the PNG), so a later pass does not assume one
  fix lifts the cluster.
- **A pure Part-I culture/process chapter that by design defers its mechanics has a structural ceiling at ~40
  (even all-8), not 44.** Three of five clusters (CLARITY/UTILITY/READABILITY) and the upper half of DEPTH are
  design-capped by routing every enforceable mechanic to a later chapter (correct for a Part-I closer). The ship
  bar may need a **declared carve-out or a human-gate path** for the small number of pure-culture/process
  chapters whose 88% is unreachable without scope creep — otherwise the loop spends budget chasing 8→9 lifts the
  chapter's design forbids. Flag this class early so the budget is not wasted.
- (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
