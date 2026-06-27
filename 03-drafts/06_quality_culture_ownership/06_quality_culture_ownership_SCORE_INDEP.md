# INDEPENDENT SCORECARD — Ch 4 "Whose Job Is Quality?" (key 06, folds 90)

> **Independent (different-model) re-score** per the ship bar in `00-strategy/SCORING.md` (auto-approval is
> ≥44/50, no cluster <6, floors A/B/C-source PASS, scored by an independent gate). Deliberately **harsh,
> skeptical** review: ≥44/50 is awarded only if a senior Java engineer would find the chapter excellent
> **and** error-free. This file is the independent record; it does not replace the main-loop self
> `_SCORE.md` (which returned 40/50). **SCORE ONLY — no draft edits, no lift loop performed here.**

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 06 (folds 90 — bus/truck factor) · printed **Chapter 4**, closes Part I
- **Slug:** `06_quality_culture_ownership`
- **Title:** Whose Job Is Quality? (Culture, ownership, shift-left, knowledge distribution)
- **Artifact scored:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Companion module:** **none** — culture/process chapter; EXAMPLE-BUILD = **N/A** (per `_EXAMPLE.md`,
  verdict PASS / module N/A). FLOOR C COMPILE + CODE-REVIEW clauses **do not attach**; FLOOR C reduces to
  **SOURCE-TRACE only** for this chapter.
- **Verified against** the pinned authority set (`00-strategy/SOURCE-PIN.md`) — pin date 2026-06-27 (corrected pin).
- **Gate reports on disk:** `_EXAMPLE.md` (PASS / N/A) + `09-flags/06_culture_named_source_verbatims_verify_at_pin.md`
  (atoms 1–8 `⚠ verify-at-pin`). **No standalone `_VERIFY.md`, `_CLARITY.md`, or `_AUDIT.md` exists** — VERIFY
  findings are recorded inside the flag file; CLARITY and AUDIT were not independently recorded on disk. Floors
  below are judged from the artifact + the flag.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (independent baseline — no lift performed)

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | Scripted blocklist sweep (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / hype) = **0 hits**. Ownership models (strong/weak/collective, §"Ownership", lines 68–78) each carry strength **and** cost — no model crowned. The QA-as-separate-function vs quality-as-everyone's-job contrast (§Alternatives, line 133) is framed as "points on a spectrum, not a contest." Boy Scout Rule + Broken Windows attributed, nothing crowned. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Dedicated `## Limitations` (lines 122–129): culture is slow; correlation-not-guarantee; heuristics soft/contested; ownership trade-offs real; bus-factor a crude proxy; culture-not-a-substitute-for-tooling and vice-versa. `## When to use` (lines 135–140) carries explicit when-NOT — "ease off forcing process where it does not fit … never weaponize a culture metric." The `IMPORTANT` callout (line 52) states the hardest limitation up front: "A generative culture cannot be installed. No `mvn` goal builds trust." Every idea presented carries its when-NOT-to-use. |
| **C — SOURCE-TRACE** *(COMPILE + CODE-REVIEW dropped — module N/A)* | ✅ **PASS (by the flag-escape clause)** | **Zero invented hard atoms:** no rule IDs, no config/ruleset keys, no tool flags, no API signatures, no GAV coordinates, and — notably — **no numeric statistic** is asserted (DORA findings stated qualitatively: "associated with", "predictive of"; the shift-left figure uses *relative* cost ordering only, never the contested 100×/Boehm multiplier). Tool versions referenced only via the figures trace correctly to the pin (Checkstyle 13.6.0, PMD 7.25.0, SpotBugs 4.10.2, JaCoCo 0.8.15, SonarQube 2026.1 LTA, ArchUnit 1.4.2, Spotless 3.6.0, NullAway 0.13.4). The **named-source verbatims/attributions that cannot be clone-verified are all flagged** to `09-flags/06_culture_named_source_verbatims_verify_at_pin.md` (atoms 1–8), satisfying the rubric's "traces to the pin **OR is flagged to `09-flags/`**" condition. **PASS — but see the two ACCURACY caveats below; they do not FAIL the floor because they are flagged, but they cap the ACCURACY cluster.** |

**All three floors PASS.** No floor failure. The aggregate is scored on its merits below.

---

## The five clusters (score 1–10 — independent, harsh)

| # | Cluster | Score | Justification (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The spine is clean and each step earns the next: culture-is-causal → shift-left (*when*) → ownership (*who*) → bus factor (knowledge as asset) → make-the-right-thing-easy (the lever) → the layered synthesis. Three load-bearing figures (Westrum types, shift-left cost curve, ownership models) carry structure prose handles poorly; no grey wall. The why-before-how gate passes (the two-teams hook frames the problem before any mechanism). Held to 8, not 9: the chapter leans on **forward references to ~10 unwritten chapters** (33/34/35/37/38/39, plus 1/2/3/6/19/40) to discharge its mechanism — a reader of Part I in isolation meets "clean as you code (Chapter 34)", "local↔CI parity (Chapter 35)", "ratcheting (Chapter 38)" as promissory notes, so the *how* of several claims is deferred rather than reconstructable from this chapter alone (the 9–10 bar). |
| 2 | **ACCURACY** | **6** | The discipline on hard atoms is strong (no invented keys/flags/versions; no fabricated number; the shift-left curve correctly refuses the contested cost multiplier). But this is the cluster a harsh reviewer must hold down, for two concrete, locatable reasons. **(a)** The chapter's load-bearing claims are *named-source verbatims that are attributed-but-not-clone-verified* — the epigraph "A generative culture is a psychologically safe culture" (line 14), the DORA generative-culture + psychological-safety findings (line 50), Westrum's 1988 typology descriptors (lines 46–48), Smith's 2001 "shift-left" coinage (line 56), the Boy Scout wording (line 117). The flag itself records all eight as `⚠ verify-at-pin` because there is **no fetched DORA clone and the named books are not redistributed**, so none can be diffed character-for-character. They are honestly flagged (which is why FLOOR C passes), but a chapter whose central evidence is entirely "confirm-at-pin" cannot score in the 7–8 "every specific fact carries a citation to the **pinned** source" band. **(b)** Two real off-pin attributions exist in supporting artifacts: **Fig 06.1's caption sources a "2019 State of DevOps report"** for the psychological-safety finding (`fig06_1.sources.md` lines 32–33), but `SOURCE-PIN.md` §5 pins **only the 2025 DORA report + *Accelerate* 2018** — the 2019 edition is **not a pinned row**; and the **Smith 2001 / Dr. Dobb's citation has no SOURCE-PIN row at all** (`fig06_2.sources.md` line 27: "SOURCE-PIN §5: n/a"), a canon *gap*, not merely a deferred verify. Both are flagged, so neither FAILs the floor — but a printed figure carrying an unpinned edition is exactly the kind of drift this cluster penalizes. **6, not 7.** |
| 3 | **UTILITY** | **8** | Squarely answers the question a tech lead actually has — "whose job is quality?" — with concrete, applied levers: choose an ownership model by context (small/deep → strong; flow → collective + gates; weak as the pragmatic middle); raise the bus factor deliberately (review, docs, pairing, rotation); introduce gates as enablers (fast, low-false-positive, applied to new code) not turnstiles. The `## When to use` section gives real decision frames. Held to 8: a lead can *act on the framing* but the chapter deliberately routes every enforceable mechanic elsewhere (CODEOWNERS → Ch 37, gate policy → Ch 33, ratcheting → Ch 38), so the reader leaves with orientation and a decision frame, not a runnable artifact or a step they execute today — appropriate for a culture chapter, but it caps applied utility below the 9–10 "page kept open while working" bar. |
| 4 | **DEPTH** | **7** | Genuinely merges two dossiers (culture key 06 + bus-factor key 90) and earns a full chapter: mechanism (Westrum → DORA causality, Deming → Smith lineage, three ownership models), evidence-for, honest limitations, an approach-based Alternatives section, and a when-to-use. It is honest about the soft/contested edges (Broken Windows disputed; correlation-not-cause; bus-factor a proxy). Held to 7, not 8: the substance is real but **stays resolutely at the principle level** — most of the contested *depth* (e.g. the actual mechanics of clean-as-you-code, ratcheting, gate tuning) is named and deferred, and the bus-factor treatment is a definition + a list of raisers rather than a worked tension. The chapter is broad-and-sound rather than deep-and-contested; that is a defensible design for a Part-I closer, but on the rubric it is solid-7 substance, not 8+. |
| 5 | **READABILITY** | **8** | The locked voice holds: third person, invisible narrator, no first person, no narration contractions (the apostrophes are possessives; `// NOSONAR` / `--no-verify` are quoted tokens). Concrete stakes-first hook (two teams, identical stack, opposite outcomes) lands the chapter's one idea before any definition. Callouts used sparingly (IMPORTANT / NOTE / two "Trace it back" beats). Zero self-narration / meta-scaffolding tells. Two minor, honest deductions keep it at 8 not 9: **em-dash density is 9.0 per 1,000** (target ~8 — over the soft target, flag-not-fail), and the title refrain **"Whose job is quality?" recurs four times** (hook, deep-dive, when-to-use synthesis, back-matter), which begins to milk the device the voice guide says to "state once." The "make the right thing the **easy** thing" idiom (lines 92/154) sits on the edge of the banned-difficulty-word rule; it reads as the paved-path noun phrase rather than filler characterizing reader difficulty, so it is a noted risk, not a hit. |

**Cluster subtotal: 37 / 50** — no single cluster below 6 (lowest is ACCURACY at 6).

---

## Verdict

- **Aggregate: 37 / 50.** Floors **A / B / C-source all PASS**. No cluster below 6.
- **Ship bar (≥44 / 50, no cluster <6, floors PASS):** **NOT CLEARED.** 37 is **7 points short** of the 44 auto-approval bar. The floors do not block — this is a **cluster-quality miss**, so the bounded lift loop is the correct next move (a floor failure would instead force a prose/scope fix).

**Phase-3 chapter scorecard:** ☑ **LIFT-LOOP** — close on floors and voice, short on the bar; the gap is concentrated in ACCURACY (6) and DEPTH (7). Apply the in-bounds fixes below and re-score. *(Per the harsh independent bar this is a clear LIFT, not a SHIP: a senior engineer would find the chapter sound and well-written but would not call its evidence base "error-free" while its central claims are entirely confirm-at-pin and a rendered figure cites an unpinned report edition.)*

**One-line rationale:** Floors clean and the writing is solid, but the evidence is almost entirely attributed-and-flagged rather than pin-traced, with two concrete off-pin attributions in the figures, so ACCURACY caps the aggregate well below the 44 bar.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score **6**.
- **Why it is the weakest:** every load-bearing claim is a named-source verbatim that the flag records as `⚠ verify-at-pin` (no DORA clone; books not redistributed), and two supporting-artifact attributions are actually **off-pin**: Fig 06.1 cites a "2019 State of DevOps report" that is not a pinned row, and the Smith 2001 Dr. Dobb's citation has **no SOURCE-PIN row at all** (a canon gap). The chapter is honest (all flagged → FLOOR C passes), but "honestly flagged as unverified" is not "traced to the pin," and that is the difference between a 6 and an 8.
- **Single highest-leverage move to lift it:** resolve the named-source pin gaps at `/pin-source` — **add a SOURCE-PIN row for DORA / State of DevOps (the exact edition that carries the psychological-safety finding) and a named-article row for Smith, "Shift-Left Testing," Dr. Dobb's (2001)** — then (a) re-confirm the epigraph + Westrum + Boy Scout verbatims against the pinned editions and clear atoms 1–8, and (b) **correct Fig 06.1's caption** to cite the *pinned* DORA edition rather than the unpinned 2019 report. That single pin-and-reconcile pass converts the chapter's evidence from "attributed-and-flagged" to "pin-traced," which is the only thing standing between ACCURACY at 6 and ACCURACY at 8 — and is the largest single move toward the 44 bar.

---

## Line-level fixes (the lift list — for the drafter, in-bounds)

| # | Cluster / floor | Location (section · ¶ · element) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / SOURCE-TRACE | `05-figures/.../fig06_1.sources.md` lines 32–33 + the figure caption | Caption sources a **"2019 State of DevOps report"** for the psychological-safety finding; that edition is **not** a pinned SOURCE-PIN §5 row (pin carries 2025 DORA + *Accelerate* 2018). | At `/pin-source`, add the exact DORA/State-of-DevOps edition that carries the finding as a §5 row, then re-caption Fig 06.1 to the **pinned** edition. No new claim — only align the citation to the pin. |
| 2 | ACCURACY / SOURCE-TRACE | Draft line 56 + `fig06_2.sources.md` line 27 | **Smith 2001 / Dr. Dobb's** "shift-left" citation has **no SOURCE-PIN row** ("§5: n/a") — a canon gap, flagged but unpinned. | Add a named-article SOURCE-PIN row for Smith, "Shift-Left Testing," *Dr. Dobb's* (2001); confirm date/venue; clear flag atom 4. |
| 3 | ACCURACY | Draft line 14 (epigraph), line 50 (DORA finding), lines 46–48 (Westrum), line 117 (Boy Scout) | Load-bearing verbatims are `⚠ verify-at-pin` (atoms 1,2,3,5) — attributed but not clone-verified. | Once rows 1–2 above are pinned, diff each verbatim character-for-character against the pinned edition and append a VERIFIED line to both the flag and a chapter `_VERIFY.md`. |
| 4 | DEPTH | §"Knowledge is a quality asset" (lines 80–90) | Bus-factor treatment is a definition + a list of raisers; the merged key-90 material reads thin against the rest of the chapter. | In-bounds: surface one concrete already-verified tension (e.g. how collective ownership raises the bus factor but needs the gates to hold quality — the chapter already owns both halves) so the section argues rather than lists. No new facts. |
| 5 | READABILITY | Draft lines 20, 104, 140, 154 | Title refrain "Whose job is quality?" repeats 4×; em-dash density 9.0/1k (target ~8). | Keep the refrain at the hook and the synthesis; drop one of the middle repeats. Convert ~3 appositive em-dashes to periods/commas to pull density under 8. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE (COMPILE/CR = N/A) | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | **37 / 50** | ✅ PASS | ✅ PASS | ✅ PASS (flag-escape; module N/A) | **LIFT-LOOP** | independent baseline; harsh re-score of main-loop self 40/50 |

> Note: the main-loop self `_SCORE.md` returned **40/50** (8/8/8/8/8). This independent harsh re-score returns
> **37/50**, lowering ACCURACY (8→6) for the all-flagged evidence base + two off-pin figure attributions, and
> DEPTH (8→7) for principle-level-only substance. Both scores agree the chapter is **below the 44 bar** and
> needs a lift; neither approves it.

---

## Learnings & pipeline suggestions

- **"Attributed-and-flagged" must not score as "pin-traced" at ACCURACY.** FLOOR C correctly passes a chapter
  whose untraceable atoms are all flagged to `09-flags/` (the escape clause is doing its job). But the
  ACCURACY *cluster* is a different instrument: a chapter whose central evidence is entirely `⚠ verify-at-pin`
  cannot reach the 7–8 "every specific fact carries a citation to the **pinned** source" band. The two
  instruments pulling in different directions (floor PASS, cluster 6) is the correct, designed outcome — worth
  a one-line note in `SCORING.md` so a future scorer does not "round ACCURACY up to match the floor."
- **A culture/process chapter front-loads its accuracy risk into named-source verbatims, not code.** With no
  module and no numbers, the entire SOURCE-TRACE surface is quotations and attributions from the named-book
  canon + web-hosted DORA — exactly the atoms the multi-authority pin cannot diff against a clone. The pipeline
  lesson: process chapters should trigger the `/pin-source` named-article additions (DORA editions, Smith 2001,
  Boy Scout source) **before** the figures are rendered, so a caption never bakes in an unpinned edition (as
  Fig 06.1's "2019 State of DevOps" did here).
- **Figures inherit the chapter's pin obligations.** A rendered PNG caption is as load-bearing as body prose; an
  off-pin edition in `figNN.sources.md` is a real source-trace defect even when the body prose is careful to
  name no year. Suggest the figure-designer gate cross-check every caption citation against an existing
  SOURCE-PIN row, failing on "n/a" the way the body verify would.
- (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
