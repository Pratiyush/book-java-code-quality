# INDEPENDENT SCORECARD — Ch 2 "The Number That Lies" (key 03 + folds 04, 58)

> **Independent re-score** (Step 8b) — different-model, deliberately HARSH skeptical reviewer.
> Bar applied: **≥44/50 (88%) with no single cluster below 6, floors A/B/C-source PASS** → SHIP; else LIFT.
> Ship only if a senior Java engineer finds it excellent **and** error-free.
> Artifact: `03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`
> Companion: `08-companion-code/03_readability_maintainability/` · Pinned @ SOURCE-PIN 2026-06-20
> Reviewer model: claude-opus-4-8 · Date: 2026-06-28 · Lift-pass #: **2** (re-score after the polish pass)

---

## Why this re-score exists

Pass 1 (41/50) gated the chapter to LIFT — not on a defect (the M1 false cyclomatic-equality claim was
already fixed and verified at Pass 1), but on cluster polish: four clusters parked at a solid 8. Pass 1
named three in-bounds lift moves on already-verified material:

1. **READABILITY** — bring the em-dash density under the soft target; convert stacked deep-dive bullet
   runs to varied prose.
2. **CLARITY** — add a one-sentence bridge between the metrics-landscape table and the Goodhart antidotes,
   so the reader is handed from "which number to track" to "how to keep it honest" rather than hitting
   both dense beats at once.
3. **DEPTH** — deepen the contested zone from named-positions level toward *when* each school's
   prescription actually wins, drawn only from the already-cited Clean Code / APoSD material.

This pass verifies the polish landed (in the source, not on trust) and re-scores all five clusters. The
polish was prose-rhythm and depth on already-verified material — **no new facts, no scope change, no floor
risk** — exactly the in-bounds envelope the lift loop allows.

---

## Verification performed this pass (not taken on trust)

- **CLARITY bridge (lift move #2) — confirmed present and well-formed.** Draft L99, immediately after the
  metrics-landscape table: *"Picking the right family from that table is only half the job. Even a
  well-chosen signal decays once a team starts steering by it, so the next section turns from which number
  to track to the discipline that keeps any number honest."* This is the exact hand-off Pass 1 asked for —
  it carries the reader from "what the families are" to "how to keep them honest." It is one sentence, adds
  no fact, and closes the dense table→antidotes seam Pass 1 named as CLARITY's only drag.
- **DEPTH deepening (lift move #3) — confirmed present, and it is genuinely the "when each school wins"
  material.** Draft L149: School A's small functions are placed in *"code with many short,
  independently-named responsibilities a reader scans and dips into, where each name is a signpost and the
  call graph is shallow"*; School B's deep modules in *"a substantial algorithm whose steps only make
  sense together, where pulling each step into its own method turns one followable body into a scavenger
  hunt across fragments"*; and comments are split the same way — *"a self-evident getter earns School A's
  silence, while a non-obvious why (a workaround for a known platform bug, a deliberate ordering
  constraint) is exactly the design intent School B argues the code cannot carry on its own."* This digs
  into the contexts where each prescription wins using only the already-cited positions. **No new source**;
  it elaborates Clean Code / APoSD, which were already pinned. The contested zone is now reasoned, not just
  catalogued.
- **READABILITY (lift move #1) — measured in the file, both halves done.**
  - *Em-dash density.* Counting the **prose body** (HTML-comment metadata + footer spec/figure-plan
    blocks excluded — they are not prose the reader sees): **12 em-dashes / 3037 words = 3.95 per 1000**,
    comfortably under the ~8/1000 soft target. (Of the 12, two sit inside attributed quotations — the
    Martin epigraph and the McCabe citation line — which are legitimate, not author voice.) The whole-file
    raw glyph count is 23, but ~half of those live in the `<!-- … -->` front-matter/footer metadata, which
    is not the prose under the soft target. Either denominator, the chief Pass-1 READABILITY drag is gone.
  - *Bullet de-density.* The Deep dive now carries **5 bullet lines** (down from the stacked runs Pass 1
    flagged), and the contested-zone deepening at L149 is **sustained prose**, not a bullet list. The
    over-reliance on stacked bullets is materially reduced.
- **M1 accuracy fix — re-verified, still correct (the polish did not touch facts).** `DiscountRulesNested`
  Javadoc L12–18 still reads that the nested form's *"cyclomatic complexity (McCabe's path count) is higher
  too,"* with the reason (branch ladder + repeated sale/coupon checks vs the balanced form reading the tier
  as data). README L15 matches (*"its cyclomatic count (McCabe's path count) runs higher than the balanced
  form's"*). Body prose L124–138 claims only **"same outcomes / same result,"** which the parameterized
  test proves. No residual false-equality language in any copy-paste artifact; the only remaining
  equal-cyclomatic language is in the **Fig 2.1** caption (a deliberate abstract figure, traced to the
  SonarSource white paper, that no longer contradicts the module).
- **Banned-phrase sweep** over the full draft → **0 hits**; over module `src/` + `README.md` → **0 hits**.
- **Build / code-review state re-read from the gate reports.** `_EXAMPLE.md`: `BUILD SUCCESS`, Tests run
  43 / 0 failures / 0 errors, 0 Checkstyle violations, SpotBugs BugInstance size 0, JDK 21.0.11 / Maven
  3.9.16 at the pin. `_CODEREVIEW.md`: verdict `FAIL → PASS-WITH-FIXES` (B1 resolved); the M1 it had left
  "for the lift pass" is closed in code (re-verified above).
- **Snippet regions:** `check_snippets.sh` 3/3 resolving, ≤9 lines (`smell-nested` 8, `smell-fragmented`
  7, `refactor-balanced` 9).

---

## The five clusters (1–10)

| # | Cluster | Score | Δ vs Pass 1 | Note (specific, located) |
|---|---|---|---|---|
| 1 | **CLARITY** | **9** | **8→9** | **Lifted — the one named CLARITY drag is fixed.** Pass 1 held this at 8 solely because *"the metrics table (L88–95) and the Goodhart antidotes arrive in quick succession — a dense stretch a first-time reader slows for."* The L99 bridge resolves exactly that: the reader is now explicitly handed from "which number to track" to "the discipline that keeps any number honest." The spine (cyclomatic vs cognitive) already landed cleanly — CONCEPT callout (L69) reduces it to two questions, Fig 2.1 carries it visually, the three-forms demo (L124–136) makes "same result, different shape" concrete, why-before-how holds. With the only located seam now bridged, a reader who never met the topic can reconstruct how it works from the chapter alone. Earned, not inflated. |
| 2 | **ACCURACY** | **9** | hold | Unchanged — the polish touched prose rhythm and depth, **not facts**, and introduced no new claim. The M1 fix is re-verified still correct (nested cyclomatic *higher*, hand-confirmed ≈13 vs ≈5; body prose claims only "same outcomes," which the test proves). Every load-bearing atom traces to the pin: `java:S3776`, McCabe 1976, the SonarSource white paper, the CK suite, Goodhart/Strathern, DORA/SPACE. Held off 10 by the same honest reservation: three secondary atoms (S3776's *default* threshold, the records/pattern-switch JEP since-versions, Checkstyle/PMD rule defaults) are marked "verify-at-pin" and deferred to their owning chapters (16/17, 5/13) rather than nailed here — an honest scoping choice (Floor C clean), but a couple of numbers stated-then-deferred. |
| 3 | **UTILITY** | **8** | hold | The polish was prose-rhythm and depth, **not new applied guidance**, so there is no honest basis to move this. The actionable spine is unchanged and strong: "which metric for which question" (L120), counter-metric pairing (L106), gate-new-code / trend-not-absolute, and "a number that cannot change a decision is vanity — stop collecting it" (L168). A lead can apply this Monday. Still 8 not 9 for the same structural reason as Pass 1: the guidance is principle-level; the concrete thresholds a reader would actually gate on are deferred to later chapters by design. The DEPTH deepening adds judgment value but does not turn this into the page a reader keeps open *while wiring up tooling*. Moving this would be inflation. |
| 4 | **DEPTH** | **9** | **8→9** | **Lifted — the one named DEPTH ceiling is filled.** Pass 1 held this at 8 because *"the contested zone is presented fairly but stays at the level of named positions; it does not dig into when each school's prescription actually wins."* The L149 deepening supplies exactly that: the contexts where tiny-functions vs deep-modules each reads better, and comments split the same way (self-evident getter vs non-obvious *why*), all from already-cited Clean Code / APoSD — **no new source**. The chapter already merged three dossiers (readability + measurement + complexity) without padding — full mechanism + metrics taxonomy + Goodhart discipline + contested zone (incl. the qntm.org critique) + honest limitations + alternatives + when-to-use. The one thing keeping it off 9 is now filled: rich, contested, foundational, comfortably sustains a deep-dive. Earned. |
| 5 | **READABILITY** | **8** | hold | **The two Pass-1 soft flags are fixed — and the fix earns a *secure* 8, not a 9.** Em-dash density is now ~4/1000 (well under target) and the deep-dive bullet runs are reduced (the contested zone is sustained prose). The voice/person holds (grep person-hits are inside quotations), the hook is concrete (3 a.m. page, covered-but-unasserted line), glossing is plain-language-first, tables carry rhythm. But the 9 anchor is *"effortless to read at full precision,"* and the chapter still carries genuinely dense stretches **by subject necessity** — the metrics-landscape table + Goodhart antidotes + the three-forms code demo are information-dense, and 5 bulleted lists remain in the deep dive where the densest teaching lives. Removing flagged drags *restores* a clean 8; it does not by itself make dense measurement material effortless. Awarding 9 here would be inflation — **held at 8.** |

**Cluster subtotal: 43 / 50** (none below 6).

---

## The three content-floors (PASS / FAIL — independent of score)

| Floor | PASS/FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep (`better than` / `unlike X` / `superior` / `beats` / `outperforms` / `the problem with X` / …) over the full draft AND module `src/`+README → **0 hits**. Clean Code vs A Philosophy of Software Design presented as two reputable schools, neither crowned (L138–149); the L149 deepening *frames the contexts symmetrically* (each school's prescription "wins" in its own terrain) — it strengthens neutrality rather than tilting it. The qntm.org critique cited as "a named position, not the field's verdict" (L147). "Alternatives" (L159–164) is trade-off/approach-based, not a leaderboard. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §Limitations (5 items, L151–157: subjectivity, proxy-not-causation, conflicting prescriptions, aggregate-index false confidence, harm of measuring individuals) + §When to use with explicit "ease off" / when-NOT conditions (L166–171). Every metric family in the table (L88–95) carries its trap column; coverage and LOC both named as vanity. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** no invented atom — `java:S3776`, McCabe 1976, the SonarSource white paper, CK suite, Goodhart/Strathern, DORA/SPACE all trace to pinned authorities; deferred thresholds/JEPs honestly marked verify-at-pin, none asserted as settled. **Compile:** green at the pin (`_EXAMPLE.md`: BUILD SUCCESS, 43/0/0, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11). **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES — the one MAJOR (M1) it deferred is **closed in code** and re-verified this pass. No open accuracy defect remains in the copy-verbatim deliverable. |

Floors gate the aggregate; they are not averaged in. **All three PASS.**

---

## Verdict

**Phase-3 chapter scorecard: LIFT (do not auto-approve).**

- Floors A/B/C-source: **PASS** (all three).
- Aggregate: **43/50**, no cluster below 6.
- **Below the 44/50 (88%) auto-approval bar by 1 point.**

> **DISPOSITION: LIFT.** The polish is real and earns two honest cluster lifts — **CLARITY 8→9** (the
> table→antidotes bridge closes the one named seam) and **DEPTH 8→9** (the contested zone now reasons
> about *when* each school wins, from already-cited material). That moves the aggregate **41→43**.
> UTILITY (8) and READABILITY (8) have **no honest basis to move**: UTILITY's ceiling is the deferred
> concrete thresholds (structural, unchanged by a prose pass), and READABILITY's fixes *restore* a secure
> 8 but do not reach "effortless at full precision" given the subject's irreducible density. The chapter
> is **defect-free** — floors clean, M1 closed — and sits exactly **1 point under** the harsh 88% bar.
> Per SCORING.md the chapter does not auto-approve at 43; it stays in the bounded lift loop. The honest
> call is **LIFT**, and manufacturing the 44th point by nudging UTILITY or READABILITY to 9 would be the
> inflation the bar exists to prevent.

---

## Flagged weakest cluster (for the next — and final — lift pass)

- **Weakest cluster:** tie at **8** between **UTILITY** and **READABILITY** (CLARITY, ACCURACY, DEPTH now
  at 9). The single highest-leverage, lowest-floor-risk target is **UTILITY**, because one in-bounds move
  could carry it 8→9 without touching a deferred fact.
- **Why UTILITY over READABILITY:** READABILITY's remaining distance to 9 is the subject's irreducible
  density (a measurement chapter *has* tables and a code demo); chasing "effortless" risks thinning real
  content for rhythm — low upside, mild floor-adjacent risk. UTILITY, by contrast, has a concrete
  in-bounds lever (below) that adds applied value on already-verified material.
- **Single highest-leverage move (UTILITY → 9):** add a short, concrete *decision aid* the reader keeps
  open — e.g. a 3–4 line "which metric answers which question, and what to pair it with" recap distilled
  from material already in the chapter (cyclomatic→tests, cognitive→readability, coverage+mutation,
  throughput+stability), framed as a question-not-verdict checklist. **No new facts** — it re-tables what
  L106–120 already establish into a reach-for-it form. If that move lands, re-test READABILITY too (a
  compact recap can also lift rhythm). One pass remains; this is the move most likely to clear the 44th
  point honestly.

---

## Line-level fixes (the lift list — in-bounds, no new unverified facts)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | UTILITY | §When to use / Deep dive recap | Guidance is strong but principle-level; no single reach-for-it decision aid that distils "which metric → which question → which counter-metric" | Add a compact 3–4 line decision aid distilled from L106–120 (cyclomatic→tests, cognitive→readability, coverage+mutation, throughput+stability) as a question-not-verdict checklist. No new facts — re-forms existing material into a keep-open artifact. Most likely path to 8→9. |
| 2 | READABILITY | Deep dive (5 remaining bullet lists) | A secure 8; remaining distance to 9 is subject-irreducible density | OPTIONAL / low-priority: only if the UTILITY recap can absorb a bullet run into prose. Do **not** thin real content for rhythm — that risks the floor-adjacent "padding" line. Most likely stays a clean 8. |

> **Resolved this pass (do not re-flag):** the CLARITY table→antidotes bridge (L99) and the DEPTH
> contested-zone deepening (L149) are **done and verified** — they carried CLARITY and DEPTH 8→9. The
> READABILITY em-dash/bullet drags from Pass 1 are **fixed** (density ~4/1000; bullets reduced) — that
> work is what secured READABILITY's 8; it is no longer an open drag, it is banked.
> **Resolved at Pass 1 (do not re-flag):** the M1 false cyclomatic-equality claim, re-verified still
> correct this pass.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 open) | LIFT-LOOP | Independent harsh baseline. Confirmed build green & B1 fix by hand; confirmed M1 (cyclomatic-equality) genuinely false in companion Javadoc. |
| 1 (indep) | 2026-06-28 | 41 / 50 | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 closed in code) | LIFT-LOOP | Re-read the M1 fix: nested Javadoc + README now correctly state cyclomatic is *higher* (≈13 vs ≈5); Fig 2.1's abstract equal case no longer contradicts the module. **ACCURACY 7→9**, unblocking UTILITY's reservation. 39→41. |
| 2 (indep) | 2026-06-28 | **43 / 50** | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 closed) | **LIFT-LOOP** | Verified the polish in the source: em-dash prose density 9.99→~4/1000 (under target); deep-dive bullet runs reduced; **CLARITY bridge (L99)** closes the table→antidotes seam → **CLARITY 8→9**; **contested-zone deepening (L149)** adds *when each school wins* from already-cited sources → **DEPTH 8→9**. UTILITY/READABILITY honestly held at 8 (no new applied guidance; density irreducible). **41→43 — 1 under the 44 bar.** |

> Note: **all three lift passes are now used** (0, 1, 2). Per SCORING.md the bounded loop is ≤3 passes.
> The chapter is at 43/50 — defect-free, floors clean — **1 point under** the harsh 88% bar. **The
> escalation rule now applies: a chapter still short after 3 passes goes to `09-flags/` for the human
> gate; the bar is not lowered.** The single remaining in-bounds candidate (the UTILITY decision aid,
> above) is the move to attempt *before* escalating, if the orchestrator elects one more in-bounds prose
> pass within the spirit of the loop; otherwise this is a human-gate decision: a 43/50 chapter with zero
> defects and all floors PASS, sitting one point under an 88% auto-approval bar, is precisely the
> editorial call SCORING.md reserves for the human (accept at 43 as a deliberate exception, or hold for
> the one earned point). **The scorer does not lower the bar and does not inflate to reach it.**

---

## Learnings & pipeline suggestions

- **Removing a flagged soft drag *restores* a cluster's score; it does not automatically raise it.** Pass
  1 named em-dash density and bullet-stacking as READABILITY drags. Fixing them was necessary and was done
  well — but the honest result is that READABILITY is a *secure* 8, not a 9, because the 9 anchor
  ("effortless at full precision") is blocked by the subject's irreducible density (tables + a code demo),
  not by the drags that were fixed. Worth a note in SCORING.md guidance: a lift pass that *clears a flag*
  banks the existing score; a lift to the next band needs a positive move toward that band's anchor, not
  just the absence of the old fault. This is the distinction that keeps the lift loop from inflating.
- **A bridge sentence and a "when each wins" deepening are high-leverage, low-risk 8→9 moves.** Two single
  in-bounds edits — one bridging sentence (L99) and one paragraph elaborating already-cited positions
  (L149) — each lifted a cluster a full band (CLARITY and DEPTH, 8→9) with zero new facts and zero floor
  risk. The pattern generalizes: when a cluster sits at 8 with a *single named* ceiling, the cheapest lift
  is usually a targeted micro-edit at that exact seam, not a rewrite. Worth promoting to the lift-loop
  playbook: "lift the named ceiling, not the chapter."
- **The bounded loop can legitimately *end* one point short of an 88% bar with a defect-free chapter.**
  After three passes this chapter is 43/50 — floors clean, M1 closed, every named ceiling either fixed
  (CLARITY, DEPTH, READABILITY drags) or honestly structural (UTILITY's deferred thresholds). The bar is
  doing its job: 88% is a high bar, and "defect-free + all named soft flags addressed" can still land at
  43. SCORING.md's escalation (≤3 passes → `09-flags/` for the human) is exactly the right valve here —
  the remaining gap is a single judgment point (is the deferred-thresholds structure of UTILITY worth one
  point, or is a compact decision-aid recap worth adding?), which is an editorial call, not a scoring
  failure. A useful reminder that the human gate exists precisely for the 43-vs-44 boundary on a clean
  chapter, and that the scorer's duty there is to escalate honestly rather than to find a 44th point.

---

## RETURN LINE

**Ch2 43/50 (C9/A9/U8/D9/R8) floors A-PASS/B-PASS/C-PASS -> LIFT**
