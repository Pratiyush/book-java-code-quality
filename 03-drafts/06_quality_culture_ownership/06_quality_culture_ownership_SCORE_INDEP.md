# INDEPENDENT SCORECARD — Ch 4 "Whose Job Is Quality?" (key 06, folds 90)

> **Independent (different-model) re-score** per the ship bar in `00-strategy/SCORING.md` (auto-approval
> is ≥44/50, no cluster <6, floors A/B/C-source PASS, scored by an independent gate). Deliberately
> **harsh, skeptical**: ≥44/50 is awarded only if a senior Java engineer would find the chapter
> **excellent and error-free**. This file is the independent record; it does not replace the main-loop
> self `_SCORE.md`.
>
> **This is a RE-SCORE after the two ACCURACY-capping residuals were CLEARED since the 40/50.** Per the
> task brief and **verified directly against the artifact + the pin + the re-rendered PNG** (not taken on
> report): (1) `fig06_1.png` was **re-rendered** with the corrected **"Westrum, 2004"** body citation —
> the stale 1988 label is gone; (2) **Vogels** (*ACM Queue*, 2006) and **Deming** (*Out of the Crisis*)
> are now **pinned SOURCE-PIN §7 rows** — every named source is pin-traced and the named-source flag is
> RESOLVED. All five clusters are re-scored against the current artifact; the floors are re-confirmed.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score — post figure-re-render + Vogels/Deming pin-add + flag-clear)
- **Dossier key:** 06 (folds 90 — bus/truck factor) · printed **Chapter 4**, closes Part I
- **Slug:** `06_quality_culture_ownership`
- **Artifact scored:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Companion module:** **none** — culture/process chapter; EXAMPLE-BUILD = **N/A**. FLOOR C COMPILE +
  CODE-REVIEW clauses **do not attach**; FLOOR C reduces to **SOURCE-TRACE only** for this chapter.
- **Verified directly this pass (the two named clears, checked against the artifacts, not the brief):**
  - **`fig06_1.png` opened and read directly** (mtime 12:35:35 — the newest file in the figure dir, newer
    than the prior scorecard's read). The printed title reads **"Figure 4.1"** and the body footer now
    reads **"Westrum typology (Ron Westrum, 2004) as described in *Accelerate* (Forsgren/Humble/Kim,
    2018)."** The superseded **1988** label the prior pass flagged is **GONE**. `fig06_1.html` line 256
    confirms the source: `(Ron Westrum, 2004)`. The figure body now matches the corrected draft caption
    (line 40) and the §5 pin row — **the figure-body drift is cleared.**
  - **SOURCE-PIN.md re-inspected** — `grep` for Vogels / "you build it" / Deming / "out of the crisis"
    now returns **two matches**: §7 line 145 (**Vogels**, *ACM Queue* "A Conversation with Werner Vogels"
    2006 + AWS News Blog 2006-05-16 corroboration, ✅ web-verified 2026-06-28) and §7 line 142 (**Deming**,
    *Out of the Crisis*, MIT Press 1982/1986, ✅ web-verified 2026-06-28). The prior pass's `grep`=0 finding
    is now **stale** — both are pin-traced. Combined with the already-present §5 (DORA + 2019 psych-safety +
    Westrum 2004) and §7 (Smith 2001 + Boy Scout 2010) rows, **every named source in the chapter is now on
    the pin.**
  - **`09-flags/06_culture_named_source_verbatims_verify_at_pin.md`** header read directly: **`[RESOLVED
    2026-06-28]` — all atoms cleared**, the §7 Vogels + Deming rows explicitly noted as "now also done,"
    and the figure label residual noted as fixed. Nothing left pending at the next `/pin-source`.
- **Gate reports on disk:** `_VERIFY.md` (web-verify PASS, 2026-06-28), `_EXAMPLE.md` (PASS / N/A), the
  three figure sidecars (`fig06_1.sources.md` now records the HTML label corrected to 2004 + the PNG
  re-render), `09-flags/...` (RESOLVED). No standalone `_CLARITY.md` / `_AUDIT.md` — those criteria judged
  from the artifact + VERIFY + the scripted sweeps below.
- **Scorer:** chapter-scorer agent (independent, harsh)
- **Date:** 2026-06-28
- **Lift/verify pass #:** post-clear re-score (the 6th independent scoring event on this artifact)

---

## What changed since the 40/50, and what I verified directly

The prior independent pass (40/50) held **ACCURACY at 8** on **two independent** residuals: (a) `fig06_1.png`
was re-rendered but **still baked the superseded "Westrum 1988"** body label, and (b) two named-source rows
(**Vogels, Deming**) were web-verified-at-use but **not on the pin** (`grep`=0). The brief reports both are now
cleared; I checked each against the artifact, the pin, and the re-rendered PNG rather than taking them on report.

- **Figure-body drift — CLEARED (verified by reading the PNG directly).** `fig06_1.png` was re-rendered
  (mtime 12:35:35) and its body footer now reads **"Ron Westrum, 2004."** The 1988 edition label is gone. The
  HTML source (line 256) carries the corrected `2004`, the sidecar records the label change + the re-render, and
  the manuscript-facing rendered artifact now matches the body caption and the §5 pin. The drift that capped
  ACCURACY below 9 on its first half is **resolved**.
- **Vogels / Deming pin completeness — CLEARED (verified by grep of the pin).** SOURCE-PIN §7 now carries both
  rows (line 145 Vogels; line 142 Deming), both web-verified 2026-06-28. They have moved from
  web-verified-at-use to **pin-traced**. The central-evidence rows (DORA + 2019 psych-safety + Westrum 2004 §5;
  Smith 2001 + Boy Scout 2010 §7) were already in place. **Every named source is now pin-traced.**
- **Named-source flag — RESOLVED (verified by reading the flag header).** All atoms cleared: 1–5 pinned, 6 + 8
  (Broken Windows / Deming) genuine attributed paraphrases with no quoted span, 7 (Vogels) web-verified and
  cited dated-at-use **and** now pinned. No copyrighted-book verbatim remains in the body.

**Net effect.** The two drifts that held ACCURACY at 8 are **both cleared**: the figure body no longer bakes
1988, and Vogels/Deming are on the pin. Per the prior scorecard's own lift map ("a corrected figure re-render
… **and** add the Vogels/Deming rows … together worth at most ACCURACY 8→9"), this lifts **ACCURACY 8 → 9**.
The other four clusters are unchanged genuine 8s, design-capped by the chapter's deliberate Part-I deferral, so
the aggregate moves **40 → 41**.

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | Verdict | Evidence (scripted sweep + direct read this pass) |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Full-draft blocklist sweep (`better than` / `unlike <Name>` / `the problem with` / `superior` / `beats` / `outperforms` / `blows away` / `leaves … in the dust` / `obvious choice over` / `no reason to use` / `destroys` / `killer of`) = **0 comparative hits**. The one `kills` hit (line 66, "Diffuse responsibility **kills** quality") is **not** a rival-denigrating verdict — no alternative is named or ranked; it is a rhetorical claim about diffusion, not "X kills Y." Ownership models held as a costed trade-off, none crowned ("the knob that lowers the bus-factor risk is the knob that raises the drift risk"; "Strong ownership keeps its place for the small, deep, specialist area"). QA-as-separate-function vs everyone's-job = "points on a spectrum, not a contest." No comparative superlative in any heading. The Vogels/Deming/Broken-Windows attributions crown nothing. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated `## Limitations` (culture slow; correlation-not-guarantee; heuristics soft/contested; ownership trade-offs real; bus-factor a crude proxy; culture-not-a-substitute-for-tooling and vice-versa). `## When to use` carries explicit when-NOT ("ease off forcing process where it does not fit … never weaponize a culture metric"). The `IMPORTANT` callout states the hardest limitation up front ("A generative culture cannot be installed. No `mvn` goal builds trust."). Every bus-factor raiser names its cost; the section closes "None of these is free, which is the point." |
| **C — SOURCE-TRACE** *(COMPILE + CODE-REVIEW dropped — module N/A)* | **PASS** | **Zero invented hard atoms, and now zero pin-completeness gaps.** Pin inspected directly: DORA generative-culture + 2019 psych-safety + Westrum 2004 are pinned §5 rows; Smith 2001 + Boy Scout 2010 + **Vogels 2006** + **Deming** are pinned §7 rows (all web-verified 2026-06-28). No rule IDs / config keys / tool flags / API signatures / GAV / version / benchmark figures; **no numeric DORA/defect-cost band asserted** (key-85 guard held). Broken Windows + Deming are genuine attributed paraphrases (no quoted span); Vogels is a web-verified short coinage cited dated-at-use **and** pinned. The named-source **flag is RESOLVED**. `fig06_1.png` now bakes the corrected 2004 Westrum label (verified by direct read). The trace record (sidecar + pin + body caption + rendered figure) is fully consistent and nothing is invented — floor PASSES with the prior pin/render-completeness gaps now closed. |

**All three floors PASS.** No floor failure. The aggregate is scored on its merits below.

---

## The five clusters (score 1–10 — independent, harsh)

| # | Cluster | Score | Δ | Justification (specific) |
|---|---|---|---|---|
| 1 | **CLARITY** | **8** | = | The spine is clean and the bus-factor section visibly pays off the ownership table ("the ownership table turns back on itself") and resolves the tension through the gates the chapter keeps returning to. A solid 8. Held off 9–10 because the chapter discharges much of its *how* through **forward references to ~13 distinct chapters** (33/34/35/37/38/39 + 1/2/3/6/19 + Parts IV–IX) — confirmed by a `grep` of the draft — so several mechanisms stay promissory rather than reconstructable from this chapter alone (the 9–10 bar: "a reader who never met the topic can reconstruct how it works from the chapter alone"). |
| 2 | **ACCURACY** | **9** | **+1** | **Both ACCURACY-capping residuals are now CLEARED — verified directly against the artifacts.** (a) `fig06_1.png` was re-rendered and its body footer now reads **"Ron Westrum, 2004"** (read directly; the 1988 label is gone; HTML line 256 confirms `2004`) — the manuscript-facing figure now matches the body caption and the §5 pin. (b) **Vogels** (§7 line 145) and **Deming** (§7 line 142) are now **pinned rows** (`grep` returns both; the prior `grep`=0 is stale) — every named source is pin-traced: DORA + 2019 psych-safety + Westrum 2004 (§5), Smith 2001 + Boy Scout 2010 + Vogels 2006 + Deming (§7). No numeric performance band asserted (key-85 guard); no rule IDs / flags / GAV / versions to drift. The named-source **flag is RESOLVED**. This meets the 9-bar — **"fully traced, zero drift"** (the snippet-path clause is N/A: no companion module). **Held at 9, not 10:** the chapter's core claims rest on **secondary canon** (named books/articles — Westrum, Smith, Fowler, Deming, Vogels, Martin) rather than primary specs, and two of those (Deming, Broken Windows) are honest attributed *paraphrases* whose underlying wording cannot be verbatim-checked against a web-public primary — correct, floor-passing, and the right call, but the residual reason a scrupulous scorer reserves the absolute 10. |
| 3 | **UTILITY** | **8** | = | The trade-off framing hands a tech lead a sharp decision tool: the ownership "knob," the gate as the thing that makes knowledge-spreading affordable, and the "raising the bus factor is an investment weighed against throughput" close. `## When to use` gives real decision frames (strong for small/deep/specialist; collective for flow + gates; weak as the pragmatic middle). Held to **8**, not 9: by design the chapter routes every *enforceable* mechanic elsewhere (CODEOWNERS → Ch 37, gate policy → Ch 33, ratcheting → Ch 38), so the reader leaves with a strong decision frame, not a runnable artifact — below the 9–10 "page kept open while working" bar. |
| 4 | **DEPTH** | **8** | = | The carried-forward in-bounds lift (applied in an earlier pass) is banked: §"Knowledge is a quality asset" **argues the tension the chapter owns** (the ownership knob that lowers the bus-factor risk is the same knob that raises the drift risk; the gates resolve it; each raiser spends something real). That is the 7–8 band's "full mechanism + for + against + alternatives + when-to-use," with the trade-off *argued* rather than listed, genuinely merging the two dossiers (culture key 06 + bus-factor key 90). Held **off 9–10**: the chapter's deepest contested mechanics (clean-as-you-code, ratcheting, gate tuning) are **by design named and deferred** to later chapters, so the substance is rich-and-argued at the principle level, not the "rich, contested, foundational deep-dive" the 9–10 anchor demands. A clean, earned 8. |
| 5 | **READABILITY** | **8** | = | Banned-filler sweep (`easily`/`just`/`simply`/`obviously`/`of course`/`basically`) = **0 condescension hits**; the three `easy` hits are all the sanctioned paved-path idiom ("make the right thing the **easy** thing"), characterizing the path not the reader. Title refrain "Whose job is quality?" recurs **3×** (hook, deep-dive synthesis, back-matter) — controlled. **Body-prose em-dash density measured this pass = 7.82 / 1,000** (excluding the front-matter comment, image lines, and table rows) — now **comfortably under** the ~8/1,000 house target, though the appositive cadence is still visibly present in the bus-factor section. No narration contractions; no first person (the `grep` "I/we" hits are all "Part I"/"Parts IV", false positives); the locked voice holds, with deliberate short-sentence cadence ("A team cannot turn it one way for free." / "None of these is free, which is the point.") breaking monotone. Held to **8**, not 9: clean and well-paced rather than effortless-at-full-precision — the still-present appositive-dash habit and the 3× `easy` idiom are what separate a genuine 8 from the 9–10 "effortless at full precision" bar. |

**Cluster subtotal: 41 / 50** — no single cluster below 6.

---

## Verdict

- **Aggregate: 41 / 50** (up from 40/50 — **ACCURACY 8 → 9**, the two capping residuals cleared). Floors
  **A / B / C-source all PASS**. No cluster below 6.
- **Ship bar (≥44 / 50, no cluster <6, floors PASS):** **NOT CLEARED.** 41 is **3 points short** of the 44
  auto-approval bar. The floors do not block — this is a **cluster-quality miss**.

**Phase-3 chapter scorecard:** **LIFT** (still below bar) → **the remaining gap is structural, not
in-bounds-liftable** → escalate to the **human gate**.

**Why the aggregate moved exactly +1 (the honest read).** The two updates were real and correct, and I verified
both directly rather than on report: the re-rendered PNG now bakes "Westrum, 2004" (1988 gone), and Vogels +
Deming are now pinned §7 rows (`grep` confirms; the prior `grep`=0 is stale). Together they cleared **both**
residuals that held ACCURACY at 8, so ACCURACY moves to a genuine 9 — "fully traced, zero drift." That is the
**only** point the artifact-completeness work could buy, and it is now banked. ACCURACY is held off **10** only
by its reliance on secondary canon + two honest paraphrases (a deliberate, floor-passing design choice), which
is not a defect to lift.

### The exact remaining gap (named precisely) — and whether ANY of it is in-bounds-liftable

The gap to 44 is now **+3**, and it lives entirely in **four genuine 8s (CLARITY / UTILITY / DEPTH /
READABILITY)** — **none of it is in-bounds-liftable.** This is the structural ceiling of a pure Part-I
culture/process closer:

1. **ACCURACY (9) — maxed for this chapter type; the +1 it offered is spent.** Both capping residuals are
   cleared and every named source is pin-traced. The only thing between 9 and 10 is the chapter's reliance on
   secondary canon and two honest attributed paraphrases — a correct, floor-passing design choice, **not** a
   liftable defect. No further ACCURACY point is available without changing the chapter's source base.
2. **CLARITY / UTILITY / DEPTH / READABILITY (each 8 → would need 9) — NOT reachable in-bounds.** Each is a
   genuine 8 held off 9 by the chapter's **deliberate design**: it discharges its *how* through forward
   references to ~13 chapters, routes **every enforceable mechanic** to a later chapter (CODEOWNERS → 37, gate
   policy → 33, clean-as-you-code/ratcheting → 34/38), and reads clean-not-effortless. Lifting any of these to 9
   would require the chapter to **carry more of its own enforceable *how*** — i.e. import the very mechanics this
   Part-I closer hands to Chs 33/34/35/37/38 — which is **scope creep and a floor risk**, explicitly out of
   bounds for the lift loop. The READABILITY 8→9 is a polish ceiling (effortless-at-full-precision), not a
   defect a single in-bounds pass crosses.

**Is any of the gap in-bounds-liftable? No.** The one in-principle-reachable point (ACCURACY 8→9) has now been
**realized** by the out-of-pass artifact-completeness work (figure re-render + pin-adds), not by an in-bounds
prose pass — and it is the *only* point that work could yield. The remaining **+3** is **structural
design-deferral**: a pure Part-I culture/process chapter that, by design, routes its mechanics to later
chapters cannot self-contain enough mechanism to push CLARITY/UTILITY/DEPTH/READABILITY to 9 without importing
those chapters' content. That is a **structural question for the human gate** (is the honest ceiling of a
deferring Part-I culture chapter ~41 against an 88% bar that rewards self-contained mechanism?), **not** a
drafter lift.

**Honest budget read.** The bounded lift loop has spent all three in-bounds passes (37 → 38 → 39 → 40); this is
a re-score at **41** after the out-of-pass figure-re-render + pin-adds banked the last reachable ACCURACY point.
Per `SCORING.md`'s bounded lift loop, **the remaining +3 is not reachable in-bounds without scope creep**, so
this is a **flag/escalate candidate for the human gate**. A senior engineer would now find this chapter
**sound, neutral, well-written, fully pin-traced (figure included), and genuinely argued at the bus-factor
section** — but would not call it *excellent and error-free at the 44 bar* while the chapter (correctly) defers
its deepest mechanics to later chapters. **LIFT → escalate. Do not lower the bar, do not pad, do not import
later chapters' mechanics to inflate the score.**

---

## Flagged weakest cluster (for the human gate / next decision)

- **Weakest clusters:** a **9 / 8 / 8 / 8 / 8 profile** — ACCURACY now leads at 9; the four held at 8 are
  **CLARITY, UTILITY, DEPTH, READABILITY**, each design-capped, no single sub-8 cluster.
- **No in-bounds prose point remains.** The one carried in-bounds move (DEPTH 7→8) was applied in an earlier
  pass; the figure re-render + Vogels/Deming pin-adds this round were out-of-pass mechanical work that banked the
  last reachable ACCURACY point (8→9). Every further point requires an 8→9 lift the chapter's intentional
  deferral of mechanics forbids in-bounds.
- **The structural question to escalate:** *can a pure Part-I culture/process chapter that by design defers its
  enforceable mechanics to later chapters clear an 88% bar that rewards self-contained mechanism — or is its
  honest, floor-clean, fully-pin-traced ceiling ~41?* This chapter is now at that ceiling with every
  artifact-completeness gap closed; the decision is editorial, not a further lift.

---

## Line-level fixes (status after this pass)

| # | Cluster / floor | Location | Status |
|---|---|---|---|
| 1 | SOURCE-TRACE / legal (RESOLVED) | Draft lines 56 / 78 / 124 (Deming / Vogels / Broken Windows) | **RESOLVED.** Deming + Broken Windows are genuine attributed paraphrases (no quoted span); Vogels web-verified (ACM Queue 2006 + AWS blog) cited dated-at-use **and now pinned §7**. Flag RESOLVED. Floor C PASS. |
| 2 | ACCURACY (figure-designer) — **CLEARED** | `05-figures/06_quality_culture_ownership/fig06_1.{html,png}` | **CLEARED — verified directly.** PNG re-rendered (mtime 12:35:35); body footer now reads **"Ron Westrum, 2004"** (1988 gone); HTML line 256 = `(Ron Westrum, 2004)`; sidecar records the label change + re-render. Figure now matches the body caption + §5 pin. Contributed ACCURACY 8→9. |
| 3 | ACCURACY (pin completeness) — **CLEARED** | SOURCE-PIN.md §7 (Vogels; Deming) | **CLEARED — verified directly (`grep` returns both).** §7 line 145 = Vogels (ACM Queue 2006 + AWS blog, web-verified 2026-06-28); §7 line 142 = Deming (*Out of the Crisis*, web-verified 2026-06-28). Every named source now pin-traced. Contributed ACCURACY 8→9. |
| 4 | (Watch — not a fix) READABILITY | em-dash cadence (bus-factor §) + heading "Make the right thing the easy thing" | **No hard change required.** Body-prose em-dash density = 7.82/1,000 (now comfortably under the ~8 target; appositive cadence still present in the bus-factor paragraphs). The `easy` idiom recurs 3× — sanctioned paved-path phrasing, not condescension. Noted edge risks that hold READABILITY at 8; neither breaches a floor or the soft target. |

---

## Lift / verify-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE (COMPILE/CR = N/A) | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | **37 / 50** | PASS | PASS | PASS (flag-escape; module N/A) | LIFT | independent baseline; harsh re-score of main-loop self 40/50 |
| 1 (indep) | 2026-06-28 | **38 / 50** | PASS | PASS | PASS (flag-escape; module N/A) | LIFT | DORA attribution reframed to pinned; Smith 2001 flagged in-body → **ACCURACY 6→7**. |
| 2 (indep, post-VERIFY) | 2026-06-28 | **39 / 50** | PASS | PASS | PASS (5 verbatims pin-traced; atoms 6–8 flag-escape) | LIFT | Web-verify pass: epigraph reframed to a verbatim DORA statement; DORA/2019 + Westrum 2004 + Smith 2001 + Boy Scout web-verified + §5/§7 rows → **ACCURACY 7→8**. READABILITY residuals cleared. DEPTH 7. |
| 3 (indep, post-DEPTH-lift) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (no new facts; atoms 6–8 flag-escape) | LIFT | DEPTH lift applied: §"Knowledge is a quality asset" rewritten list → argued tension → **DEPTH 7→8**. **3 lift passes spent.** |
| 4 (indep, post-verbatim-resolution) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (verbatims resolved) | LIFT → human gate | Broken Windows + Deming → attributed paraphrases; Vogels web-verified. Removed the legal sub-cap; ACCURACY stayed 8 (stale figure + Vogels/Deming not pinned). |
| 5 (indep, post-figure-re-render + partial pin-add) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (central evidence pinned; Vogels/Deming off-pin; figure body still 1988) | LIFT → human gate | Central-evidence §5/§7 rows pinned; figure number fixed but **body still baked 1988**; Vogels/Deming **off the pin** → ACCURACY held at 8. |
| 6 (indep, post-clear) | 2026-06-28 | **41 / 50** | PASS | PASS | PASS (every named source pinned; figure body = 2004; flag RESOLVED) | **LIFT → human gate (structural)** | **Verified directly:** `fig06_1.png` re-rendered — body now reads **"Westrum, 2004"** (1988 gone); **Vogels + Deming now pinned §7** (`grep` returns both); named-source flag **RESOLVED**. Both ACCURACY-capping residuals cleared → **ACCURACY 8→9**, aggregate **40→41**. Remaining +3 is **structural design-deferral**, not in-bounds-liftable → escalate. |

> Independent: 37 → 38 → 39 → 40 → 40 → 40 → **41/50**. The artifact-completeness work has now banked the last
> reachable point (ACCURACY 8→9). All scores agree the chapter is **below the 44 bar**; the remaining gap is
> structural (a deferring Part-I culture chapter against an 88% self-contained-mechanism bar), not a lift.

---

## Learnings & pipeline suggestions

- **A figure re-render only clears an ACCURACY residual when the BAKED BODY changes — verify by reading the
  PNG, not the mtime.** Pass 5 saw a fresh mtime and a fixed *number* but the body still baked 1988; pass 6
  confirmed by **reading the rendered PNG directly** that the body footer now reads "Westrum, 2004." Pipeline
  suggestion: a figure-residual is "cleared" only when the *specific baked atom* is re-read in the rendered
  image — file freshness and a fixed adjacent atom are not proof. (Carried from pass 5, now closed.)
- **A `/pin-source` follow-up was the last point between traced-at-use and pin-traced — scheduling it before the
  human gate was correct.** Vogels + Deming sat web-verified-but-unpinned across two passes, holding ACCURACY a
  point short on a clerical gap. They are now §7 rows (verified by grep). Pipeline suggestion stands: when a
  pin-row addition is the only thing between an otherwise-clean chapter and a cluster point, do it before the
  gate and **confirm the row by grep, not by report** — which is how this pass confirmed the prior `grep`=0 had
  gone stale.
- **The structural ceiling is now empirically located: a pure Part-I culture/process closer tops out at ~41
  (9/8/8/8/8) with EVERY artifact-completeness gap closed.** With both ACCURACY residuals cleared, the chapter
  reaches 41 and stops — the remaining +3 lives in four clusters each design-capped by routing enforceable
  mechanics to later chapters (correct for a Part-I closer). This is no longer a "finish the pin work" question;
  it is the **declared-carve-out / human-gate-path** question for the small class of pure-culture/process
  chapters whose 88% is unreachable without scope creep. Recommend the human gate decide this class with a
  standing rule so the loop does not re-spend budget chasing 8→9 lifts the chapter's design forbids.
- (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
