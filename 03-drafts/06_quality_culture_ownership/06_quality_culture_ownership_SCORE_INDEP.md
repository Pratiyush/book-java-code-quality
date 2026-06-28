# INDEPENDENT SCORECARD — Ch 4 "Whose Job Is Quality?" (key 06, folds 90)

> **Independent (different-model) re-score** per the ship bar in `00-strategy/SCORING.md` (auto-approval
> is ≥44/50, no cluster <6, floors A/B/C-source PASS, scored by an independent gate). Deliberately
> **harsh, skeptical** review: ≥44/50 is awarded only if a senior Java engineer would find the chapter
> **excellent and error-free**. This file is the independent record; it does not replace the main-loop
> self `_SCORE.md`.
>
> **This is a RE-SCORE after the named-source verbatim residuals were resolved.** Per the task brief and
> confirmed against the artifact + the pin + the flag: Broken Windows (*The Pragmatic Programmer*, Hunt &
> Thomas) and Deming (*Out of the Crisis*) are now **faithful attributed paraphrases** (genuine rewrites,
> no quoted span); Vogels "you build it, you run it" is **web-verified** (ACM Queue 2006 + AWS News Blog
> corroboration) and cited dated-at-use; Westrum 2004 + the Boy Scout Rule were already web-verified and
> pinned. DEPTH had been lifted 7→8 in the prior pass. All five clusters are re-scored against the current
> artifact; the floors are re-confirmed; the pin was inspected directly.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score — post verbatim-resolution)
- **Dossier key:** 06 (folds 90 — bus/truck factor) · printed **Chapter 4**, closes Part I
- **Slug:** `06_quality_culture_ownership`
- **Title:** Whose Job Is Quality? (Culture, ownership, shift-left, knowledge distribution)
- **Artifact scored:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Companion module:** **none** — culture/process chapter; EXAMPLE-BUILD = **N/A**. FLOOR C COMPILE +
  CODE-REVIEW clauses **do not attach**; FLOOR C reduces to **SOURCE-TRACE only** for this chapter.
- **Verified against** the pinned authority set (`00-strategy/SOURCE-PIN.md`), inspected directly this pass:
  §5 carries DORA generative-culture + 2019 psychological-safety + Westrum 2004 rows (web-verified
  2026-06-28); §7 carries Smith 2001 + Boy Scout 2010 named-article rows (web-verified 2026-06-28).
- **Gate reports on disk:** `06_quality_culture_ownership_VERIFY.md` (web-verify PASS, 2026-06-28),
  `_EXAMPLE.md` (PASS / N/A), `09-flags/06_culture_named_source_verbatims_verify_at_pin.md`
  (**RESOLVED 2026-06-28** — all atoms cleared), the three figure sidecars. No standalone `_CLARITY.md` /
  `_AUDIT.md` on disk — those criteria are judged from the artifact + the VERIFY pass + the sidecars.
- **Scorer:** chapter-scorer agent (independent, harsh)
- **Date:** 2026-06-28
- **Lift/verify pass #:** post-verbatim-resolution re-score (the 4th independent scoring event on this artifact)

---

## What changed since the 40/50 re-score, and what I verified directly

The prior independent pass (40/50) held **ACCURACY at 8** for **two** named caps: (a) three copyrighted-book
verbatims — Broken Windows, Vogels, Deming — were "attributed-and-flagged with no web-public primary," and
(b) `fig06_1.png` bakes a stale Westrum edition label. The task brief says cap (a) is now resolved. I checked
this against the artifact, the flag, and the pin rather than taking it on report:

- **Broken Windows (draft line 124)** — rewritten as a genuine attributed paraphrase: *"the idea, **in this
  book's words**, is that a single defect left visibly unrepaired quietly resets the standard …"* No quotation
  marks, no quoted span, attributed to "Hunt and Thomas, *The Pragmatic Programmer*," with the contested-theory
  caveat retained (lines 126, 132). **Confirmed: a true rewrite, not a verbatim.**
- **Deming (draft line 56)** — rewritten as a genuine attributed paraphrase: *"whose work on management
  (*Out of the Crisis*) argued, **as this book reads him**, that an organization should stop relying on
  end-of-line inspection …"* No quoted span. **Confirmed: a true rewrite, not a verbatim.**
- **Vogels (draft line 78)** — *"the model Amazon CTO Werner Vogels summed up as *you build it, you run it*
  (ACM Queue, 'A Conversation with Werner Vogels,' 2006) …"* with the documented quality rationale, cited
  dated-at-use; VERIFY corroborates against the AWS News Blog (Jeff Barr, 2006-05-16). **Confirmed:
  web-verified short coinage (6 words), cited dated-at-use.** The flag is marked **RESOLVED**.

**Net effect of the resolution:** real and necessary — it removed a legal/IP-and-floor risk and retired one of
the two sub-caps that held ACCURACY at 8. But it was **not aggregate-moving**, because ACCURACY was already
gated by a *second, independent* residual that this pass did not touch (the stale baked figure), and because
two of the now-cleared rows are web-verified-**at-use** but **not yet on SOURCE-PIN.md** (see ACCURACY below
and FLOOR C). The chapter remains an even all-8 → **40/50**.

**Residuals carried, unchanged this pass (honest):**

- **`fig06_1.png` still bakes the superseded Westrum edition label** — stated in the task brief as a deferred
  cosmetic. The body caption + sidecar are corrected; the rendered PNG (a manuscript-facing artifact) lags
  them. Not re-rendered this pass.
- **Vogels and Deming are not yet on the pin.** The flag's own words: the Vogels row is a "**SOURCE-PIN §7
  named-article candidate row** (add at next `/pin-source`)" and the open follow-up is "add the Vogels ACM
  Queue / AWS-blog row to SOURCE-PIN §7." Deming (*Out of the Crisis*) is **not present anywhere in
  SOURCE-PIN.md** (it stands as an attributed secondary-canon paraphrase, not a pinned row). Both are honest
  flag-escape / attributed-paraphrase — floor-passing — but neither is yet *pin-traced*, which is what the
  ACCURACY 9-bar requires.

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Full-draft blocklist sweep (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `obvious choice` / `no reason to use`) = **0 hits** (scripted re-run this pass). Ownership models held as a costed trade-off, none crowned ("the knob that lowers the bus-factor risk is the knob that raises the drift risk"; "Strong ownership keeps its place for the small, deep, specialist area"). QA-as-separate-function vs everyone's-job is "points on a spectrum, not a contest." No comparative superlative in any heading. The resolved Vogels/Deming/Broken-Windows paraphrases attribute and crown nothing. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated `## Limitations` (culture slow; correlation-not-guarantee; heuristics soft/contested; ownership trade-offs real; bus-factor a crude proxy; culture-not-a-substitute-for-tooling and vice-versa). `## When to use` carries explicit when-NOT ("ease off forcing process where it does not fit … never weaponize a culture metric"). The `IMPORTANT` callout states the hardest limitation up front ("A generative culture cannot be installed. No `mvn` goal builds trust."). Every bus-factor raiser names its cost; the section closes "None of these is free." |
| **C — SOURCE-TRACE** *(COMPILE + CODE-REVIEW dropped — module N/A)* | **PASS** | **Zero invented hard atoms.** Pin inspected directly: DORA generative-culture + 2019 psych-safety + Westrum 2004 are pinned SOURCE-PIN §5 rows (web-verified 2026-06-28); Smith 2001 + Boy Scout 2010 are pinned §7 named-article rows. No rule IDs / config keys / tool flags / API signatures / GAV / version / benchmark figures; **no numeric DORA/defect-cost band asserted**. The three remaining named sources are floor-passing by the flag-escape route: **Broken Windows** and **Deming** are now genuine attributed paraphrases (no quoted span to trace); **Vogels** is a web-verified short coinage cited dated-at-use. The flag is **RESOLVED**. *Trace-completeness note (caps ACCURACY, does NOT FAIL the floor):* the Vogels row is still a §7 "candidate" not yet added, Deming is not on the pin at all, and `fig06_1.png` still bakes the superseded edition label — the trace **record** is correct and nothing is invented, so the floor PASSES; these are pin-completeness gaps, scored against ACCURACY below. |

**All three floors PASS.** No floor failure. The aggregate is scored on its merits below.

---

## The five clusters (score 1–10 — independent, harsh)

| # | Cluster | Score | Δ | Justification (specific) |
|---|---|---|---|---|
| 1 | **CLARITY** | **8** | = | The spine is clean and the bus-factor section now visibly pays off the ownership table ("the ownership table turns back on itself") and resolves the tension through the gates the chapter keeps returning to. A solid 8. Held off 9–10 because the chapter discharges much of its *how* through **forward references to ~10 unwritten chapters** (33/34/35/37/38/39 + 1/2/3/6/19/40), so several mechanisms stay promissory rather than reconstructable from this chapter alone (the 9–10 bar). |
| 2 | **ACCURACY** | **8** | = | **The verbatim resolution removed one sub-cap but not the cluster cap.** The central evidence is pin-traced and verbatim-confirmed (epigraph + DORA generative-culture + 2019 psych-safety, Westrum 2004, Smith 2001, Boy Scout 2010 — all on SOURCE-PIN §5/§7, web-verified). No numeric performance band asserted. Broken Windows + Deming are now clean attributed paraphrases and Vogels is web-verified — the **legal/quoted-span cap is gone**. Held at **8, not 9** for two concrete, still-present pin-completeness residuals (the exact remaining gap): **(a)** `fig06_1.png` still bakes the **superseded Westrum edition label** — a manuscript-facing rendered artifact that drifts from its corrected sidecar; and **(b)** the **Vogels** (§7 "candidate, add at next `/pin-source`") and **Deming** (not on the pin at all) named sources are web-verified/attributed **at use** but **not yet pin-traced**. The 9-bar is "fully traced, zero drift" — a drifted PNG plus two not-yet-pinned rows is exactly the drift that holds an 8 below a 9. Both are explicitly out of this pass's scope (do-not-re-render; the pin row is a `/pin-source` follow-up). |
| 3 | **UTILITY** | **8** | = | The trade-off framing hands a tech lead a sharp decision tool: the ownership "knob," the gate as the thing that makes knowledge-spreading affordable, and the "raising the bus factor is an investment weighed against throughput" close. `## When to use` gives real decision frames (strong for small/deep/specialist; collective for flow + gates; weak as the pragmatic middle). Held to **8**, not 9: by design the chapter routes every *enforceable* mechanic elsewhere (CODEOWNERS → Ch 37, gate policy → Ch 33, ratcheting → Ch 38), so the reader leaves with a strong decision frame, not a runnable artifact — below the 9–10 "page kept open while working" bar. |
| 4 | **DEPTH** | **8** | = | The carried-forward in-bounds lift (applied in the prior pass) is correctly banked: §"Knowledge is a quality asset" **argues the tension the chapter owns** (the ownership knob that lowers the bus-factor risk is the same knob that raises the drift risk; the gates resolve it; each raiser spends something real). That is the 7–8 band's "full mechanism + for + against + alternatives + when-to-use," now with the trade-off *argued* rather than listed, and it genuinely merges the two dossiers (culture key 06 + bus-factor key 90). Held **off 9–10**: the chapter's deepest contested mechanics (clean-as-you-code, ratcheting, gate tuning) are **by design named and deferred** to later chapters, so the substance is rich-and-argued at the principle level, not the "rich, contested, foundational deep-dive" the 9–10 anchor demands. A clean, earned 8. |
| 5 | **READABILITY** | **8** | = | Banned-filler sweep (`easily`/`just`/`simply`/`obviously`/`of course`) = **0** (scripted). Title refrain "Whose job is quality?" recurs **3×** (hook, deep-dive synthesis, back-matter) — controlled. Prose em-dash density is within the house target on a prose-only measure (~6/1,000; the coarse whole-file count is inflated by markdown table pipes). No narration contractions; no first person; the locked voice holds, with deliberate short-sentence cadence ("A team cannot turn it one way for free." / "None of these is free, which is the point.") breaking monotone. The "make the right thing the **easy** thing" idiom is a noted edge risk, not a hit (it characterizes the paved path, not the reader's difficulty). Held to **8**, not 9: clean and well-paced rather than effortless-at-full-precision (the 9–10 bar). |

**Cluster subtotal: 40 / 50** — no single cluster below 6 (even all-8 profile).

---

## Verdict

- **Aggregate: 40 / 50** (unchanged from the prior 40/50). Floors **A / B / C-source all PASS**. No cluster
  below 6.
- **Ship bar (≥44 / 50, no cluster <6, floors PASS):** **NOT CLEARED.** 40 is **4 points short** of the 44
  auto-approval bar. The floors do not block — this is a **cluster-quality miss**.

**Phase-3 chapter scorecard:** **LIFT** (still below bar) → escalating to the **human gate** (lift loop spent).

**Why the verbatim resolution did not move the aggregate (the honest read).** Resolving Broken Windows /
Deming / Vogels was real, necessary, and floor-protective — it retired a legal/quoted-span risk and one of the
two sub-caps the prior pass placed on ACCURACY. But ACCURACY was held at 8 by **two independent** residuals,
and this pass cleared only one of them. The other — `fig06_1.png` still baking the superseded edition label,
plus the Vogels/Deming rows being web-verified/attributed **at use** but **not yet pin-traced** — still
separates a genuine 8 from a genuine 9. So ACCURACY stays 8, and with the other four clusters each a
design-capped 8, the aggregate holds at 40.

### The exact remaining gap (named precisely)

This is a **by-design-deferring Part-I culture closer** that tops out at an **even all-8 (40/50)**. The gap to
44 has exactly two parts:

1. **ACCURACY 8 → 9 (+1) — the only in-principle-reachable point, blocked by two concrete out-of-scope
   residuals.** (a) Re-render `fig06_1.png` from HTML via `render.mjs` so the baked Westrum edition label
   matches the corrected sidecar — **excluded this pass by the do-not-re-render constraint** — **and** (b) add
   the **Vogels** ACM Queue / AWS-blog row and (if it is to be cited as canon) the **Deming** *Out of the
   Crisis* row to **SOURCE-PIN.md §7**, converting them from web-verified-at-use to pin-traced — a
   **`/pin-source` follow-up**, not an in-bounds prose pass. Worth **at most +1** → aggregate 41.
2. **The remaining +3 to 44 (CLARITY / UTILITY / DEPTH / READABILITY 8 → 9) — NOT reachable in-bounds.** Each
   is a genuine 8 held off 9 by the chapter's deliberate design: it discharges its *how* through forward
   references, routes every enforceable mechanic to a later chapter, and reads clean-not-effortless. Lifting
   any to 9 would require the chapter to carry more of its own enforceable *how* — **importing the very
   mechanics this Part-I closer hands to Chs 33/34/35/37/38 (scope creep)** — or a prose polish that does not,
   by itself, cross a genuine 8 into a genuine 9. This is a **structural-design question for the human gate**,
   not a drafter lift.

**Honest budget read.** The lift loop has spent all three passes (37 → 38 → 39 → 40, then this re-score at 40).
Even taking the out-of-scope ACCURACY point, the chapter reaches ~41 — still **3 short of 44**. Per
`SCORING.md`'s bounded lift loop, the remaining gap is **not reachable in-bounds without scope creep or
out-of-pass work**, so this is a **cut/flag candidate for the human gate**. A senior engineer would find this
chapter **sound, neutral, well-written, pin-traced on its central evidence, and now genuinely argued at the
bus-factor section** — but would not call it *excellent and error-free at the 44 bar* while one rendered figure
bakes a superseded edition, two named-source rows sit web-verified-but-not-yet-pinned, and the chapter
(correctly) defers its deepest mechanics. **LIFT → escalate. Do not lower the bar, do not pad, do not import
later chapters' mechanics to inflate the score.**

---

## Flagged weakest cluster (for the human gate / next decision)

- **Weakest clusters:** an **even all-8 profile** — no single sub-8 cluster. The cluster with in-principle
  headroom is **ACCURACY (8)**, blocked by two concrete out-of-pass residuals (below).
- **No confidently-in-bounds prose point remains.** The one carried in-bounds move (DEPTH 7→8) was applied in
  the prior pass; the verbatim resolution this pass was a floor/legal fix, not an aggregate lift. Every further
  point requires out-of-pass work (figure re-render; the `/pin-source` rows) or a 8→9 lift the chapter's
  intentional deferral of mechanics forbids in-bounds.
- **Single highest-leverage move (out of this pass's scope):** re-render `fig06_1.png` year/edition-free via
  `render.mjs` (figure-designer) **and** add the Vogels (and, if cited as canon, Deming) rows to SOURCE-PIN §7
  at the next `/pin-source` — together worth at most ACCURACY 8→9 (aggregate → 41). The remaining +3 to 44 is
  the structural question to escalate: *can a pure Part-I culture/process chapter that by design defers its
  mechanics clear an 88% bar that rewards self-contained mechanism, or is its honest ceiling ~40–41?*

---

## Line-level fixes (status after the verbatim resolution)

| # | Cluster / floor | Location | Status |
|---|---|---|---|
| 1 | SOURCE-TRACE / legal (RESOLVED) | Draft lines 56 / 78 / 124 (Deming / Vogels / Broken Windows) | **RESOLVED this pass.** Deming + Broken Windows are genuine attributed paraphrases (no quoted span); Vogels is web-verified (ACM Queue 2006 + AWS blog) cited dated-at-use. Flag marked RESOLVED. Floor C PASS. |
| 2 | ACCURACY (figure-designer) | `05-figures/06_quality_culture_ownership/fig06_1.{html,png}` | **Carried — out of scope this pass** (do-not-re-render). Baked Westrum edition label lags the corrected sidecar; body caption + sidecar correct. Re-render edition-free to enable ACCURACY 8→9. |
| 3 | ACCURACY (pin completeness) | SOURCE-PIN.md §7 (Vogels; Deming) | **Carried — `/pin-source` follow-up.** Vogels is a §7 "candidate row, add at next `/pin-source`"; Deming is not on the pin. Web-verified/attributed at use (floor-passing) but not yet pin-traced. Add both to convert traced-at-use → pin-traced, alongside fix #2, for ACCURACY 8→9. |
| 4 | (Watch — not a fix) READABILITY | Heading "Make the right thing the easy thing" | **No change required.** Paved-path noun phrase characterizing the path, not the reader's difficulty. Noted edge risk only. |

---

## Lift / verify-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE (COMPILE/CR = N/A) | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | **37 / 50** | PASS | PASS | PASS (flag-escape; module N/A) | LIFT | independent baseline; harsh re-score of main-loop self 40/50 |
| 1 (indep) | 2026-06-28 | **38 / 50** | PASS | PASS | PASS (flag-escape; module N/A) | LIFT | DORA attribution reframed to pinned; Smith 2001 flagged in-body → **ACCURACY 6→7**. DEPTH lift not applied. |
| 2 (indep, post-VERIFY) | 2026-06-28 | **39 / 50** | PASS | PASS | PASS (5 verbatims pin-traced; atoms 6–8 flag-escape; module N/A) | LIFT | Web-verify pass: epigraph reframed to a verbatim DORA statement; DORA/2019 + Westrum 2004 + Smith 2001 + Boy Scout web-verified + SOURCE-PIN §5/§7 rows → **ACCURACY 7→8**. READABILITY residuals cleared. DEPTH tension not yet applied → DEPTH 7. |
| 3 (indep, post-DEPTH-lift) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (no new facts; atoms 6–8 flag-escape; module N/A) | LIFT | DEPTH lift applied: §"Knowledge is a quality asset" rewritten from a list into an argued tension → **DEPTH 7→8**. **3 lift passes spent.** |
| 4 (indep, post-verbatim-resolution) | 2026-06-28 | **40 / 50** | PASS | PASS | PASS (verbatims resolved — paraphrase/web-verify; module N/A) | **LIFT → human gate** | Broken Windows + Deming → genuine attributed paraphrases; Vogels web-verified + cited dated-at-use; flag RESOLVED. **Removed the legal/quoted-span sub-cap on ACCURACY but ACCURACY stays 8** (residual: stale baked figure + Vogels/Deming not yet pin-traced). Aggregate **holds at 40**. Lift loop exhausted in-bounds → escalate. |

> Self `_SCORE.md` returned **40/50** (8/8/8/8/8). Independent: 37 → 38 → 39 → 40 → **40/50**. Independent and
> self agree exactly at **40/50**; all scores agree the chapter is **below the 44 bar** and none approves it.

---

## Learnings & pipeline suggestions

- **Resolving a quoted-span / legal residual is not the same as lifting ACCURACY** when the cluster is gated by
  more than one residual. The verbatim resolution (Broken Windows/Deming paraphrased, Vogels web-verified) was
  necessary and floor-protective, but ACCURACY was held at 8 by **two** independent residuals — the stale baked
  figure and the not-yet-pinned named-source rows — so clearing only the legal one left the cluster (and the
  aggregate) flat. Pipeline suggestion: when a scorecard caps a cluster, **enumerate every sub-cap** so a later
  pass does not assume that clearing one will move the band.
- **Web-verified-at-use ≠ pin-traced.** The ACCURACY 9-bar is "fully traced, zero drift." Vogels is a §7
  *candidate* row ("add at next `/pin-source`") and Deming is not on the pin at all; both are honest
  flag-escape/attributed-paraphrase (floor-passing) but not yet pinned, which correctly holds ACCURACY at 8.
  Pipeline suggestion: when a `/pin-source` follow-up is the only thing between traced-at-use and pin-traced,
  schedule it before the human gate so an otherwise-clean chapter is not held a point short by a clerical
  pin-row addition.
- **A rendered figure that lags its corrected sidecar is an ACCURACY drift, not merely cosmetic.** `fig06_1.png`
  baking a superseded edition label is a manuscript-facing artifact that diverges from the corrected sidecar;
  under the harsh 9-bar that is real drift. It does not FAIL FLOOR C (the trace record is correct), but it caps
  ACCURACY below 9. Pipeline suggestion: treat "rendered artifact lags sidecar" as a tracked ACCURACY item, not
  a deferrable cosmetic, when the chapter is otherwise at the bar.
- **A pure Part-I culture/process chapter that by design defers its mechanics has a structural ceiling at ~40
  (even all-8), not 44.** Three of five clusters (CLARITY/UTILITY/READABILITY) and the upper half of DEPTH are
  design-capped by routing every enforceable mechanic to a later chapter (correct for a Part-I closer). The ship
  bar may need a **declared carve-out or a human-gate path** for the small number of pure-culture/process
  chapters whose 88% is unreachable without scope creep — otherwise the loop spends budget chasing 8→9 lifts the
  chapter's design forbids. Flag this class early so the budget is not wasted.
- (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
