# INDEPENDENT SCORECARD — Ch 2 "The Number That Lies" (key 03 + folds 04, 58)

> **Independent re-score** (Step 8b) — different-model, deliberately HARSH skeptical reviewer.
> Bar applied: **≥44/50 (88%) with no single cluster below 6, floors A/B/C-source PASS** → SHIP; else LIFT.
> Ship only if a senior Java engineer finds it excellent **and** error-free.
> Artifact: `03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`
> Companion: `08-companion-code/03_readability_maintainability/` · Pinned @ SOURCE-PIN 2026-06-20
> Reviewer model: claude-opus-4-8 · Date: 2026-06-28 · Lift-pass #: **3** (final — the UTILITY decision aid landed)

---

## Why this re-score exists

Pass 2 (43/50) left the chapter **defect-free** — floors clean, the M1 false cyclomatic-equality claim
closed and re-verified — but **one point under** the harsh 88% bar, with CLARITY/ACCURACY/DEPTH at 9 and
**UTILITY/READABILITY held at 8**. Pass 2 named the single highest-leverage, lowest-floor-risk move that
could honestly carry the 44th point:

> **UTILITY → 9:** add a compact *decision aid* the reader keeps open — a "which metric answers which
> question, and what to pair it with" recap distilled from material already in the chapter
> (cyclomatic→tests, cognitive→readability, coverage+mutation, throughput+stability), framed as a
> question-not-verdict checklist. **No new facts** — it re-tables what L106–122 already establish into a
> reach-for-it form.

This pass applied exactly that move, verified it is a pure re-form of already-verified material (no new
fact, no scope, no floor risk), and re-scored all five clusters. UTILITY lifts **8→9** on a genuine move
toward its 9 anchor; READABILITY holds at a secure 8 (its remaining distance is the subject's irreducible
density, not a fixable drag). **Aggregate 43→44 — clears the bar.**

---

## The lift move applied this pass (in-bounds, verified)

- **A reach-for-it decision table added at the end of §When to use (after L171).** Five rows, each a
  question the reader actually has → the metric that answers it → the counter-metric that keeps the answer
  honest, framed explicitly as "a question … never as a verdict to rank a person":
  - *How many tests does this method need?* → cyclomatic complexity → pair with the cognitive score.
  - *How hard is this to read?* → cognitive complexity (`java:S3776`) → pair with readable naming.
  - *Do the tests detect bugs, not just touch lines?* → mutation score → pair with line coverage.
  - *Is delivery healthy?* → throughput → pair with stability (change-failure rate).
  - *Is the code base growing in value?* → nothing; LOC is vanity here → pair with outcomes (defects-escaped,
    lead time); no counter-metric rescues LOC.
- **Every atom is already in the chapter and already source-traced** — this is a re-form, not new content:
  cyclomatic→tests/testability (L65, L122, body Key-takeaways L182); cognitive→readability + `java:S3776`
  (L67, L71, L122, L182); mutation vs coverage as counter-metric (L95, L97, L108); throughput↔stability,
  velocity↔change-failure rate (L108); LOC-as-vanity with no rescue (L90, L95). The "questions, not
  verdicts / never to rank a person" framing is the chapter's own (L110, L170). **No new source, no new
  number, no scope change.**

## Verification performed this pass (not taken on trust)

- **Snippet tag-regions untouched.** The three `<!-- include: … -->` lines still resolve at L128
  (`#smell-nested`), L132 (`#smell-fragmented`), L136 (`#refactor-balanced`) — the edit sits in §When to
  use, well clear of the code beat. `check_snippets.sh` state from `_EXAMPLE.md` unchanged: 3/3 resolving,
  ≤9 lines (8 / 7 / 9).
- **Banned-phrase sweep over the full draft → 0 hits** (`better than` / `unlike X` / `superior` / `beats`
  / `outperforms` / `the problem with X` / `blows away` / `killer of` / `obvious choice over` / `no reason
  to use`). The new table crowns nothing — it maps each metric to the question it answers, never ranks the
  metrics against each other, and the LOC row states LOC's own limitation (honest-limitations), not a
  rival's.
- **Narration first-person sweep → 0 hits** (`I think` / `we can see` / `we add` / `let us`). Voice/person
  holds: third person, the one direct address is the sanctioned imperative ("Read each row as …").
- **No contraction introduced in narration.** The table prose uses "does not", "is not".
- **Em-dash density still under target.** Whole-file glyph count 23→**24** (one new appositive — "nothing —
  lines of code is vanity here"); roughly half the whole-file glyphs live in the `<!-- … -->`
  front-matter/footer metadata, so prose density remains well under the ~8/1000 soft target.
- **No new fact / no padding.** The table replaces nothing real and re-states verified material in a
  denser, scannable form; it is a keep-open artifact, not filler. It does not touch any deferred-threshold
  atom — the concrete S3776/Checkstyle/PMD defaults stay deferred to their owning chapters (16/17), exactly
  as before.
- **Floor-C build/code-review state re-read from the gate reports (unchanged).** `_EXAMPLE.md`:
  `BUILD SUCCESS`, Tests run 43 / 0 failures / 0 errors, 0 Checkstyle violations, 0 SpotBugs findings,
  JDK 21.0.11 / Maven 3.9.16 at the pin. `_CODEREVIEW.md`: `PASS-WITH-FIXES` (B1 resolved; the M1 it had
  deferred is closed in code and re-verified across Passes 1–2). The prose edit does not touch the module.

---

## The five clusters (1–10)

| # | Cluster | Score | Δ vs Pass 2 | Note (specific, located) |
|---|---|---|---|---|
| 1 | **CLARITY** | **9** | hold | The table→antidotes bridge (L99) already secured 9 at Pass 2. The new decision table sits in §When to use and reinforces the cyclomatic-vs-cognitive spine in scannable form without altering the explanation. A reader who never met the topic can still reconstruct the mechanism from the chapter alone. Held — no basis to inflate to 10. |
| 2 | **ACCURACY** | **9** | hold | The edit is a pure re-form of already-traced atoms — `java:S3776`, cyclomatic/cognitive, mutation vs coverage, throughput↔change-failure-rate, LOC-as-vanity, DORA — and introduces **no new claim**. M1 re-verified still correct across passes. Held off 10 by the same honest reservation as before: three secondary atoms (S3776's *default* threshold, the records/pattern-switch JEP since-versions, Checkstyle/PMD defaults) are deferred to their owning chapters rather than nailed here — Floor-C clean, but a couple of numbers stated-then-deferred. |
| 3 | **UTILITY** | **9** | **8→9** | **Lifted — the one named UTILITY ceiling is filled with a genuine move toward the 9 anchor.** Pass 2 held this at 8 because the guidance, while strong, was principle-level and scattered across running prose — a lead had to reconstruct "which metric → which question → which counter-metric" from L106–122. The new question→metric→counter-metric table consolidates exactly that into one scannable, reach-for-it artifact, framed as questions-not-verdicts and carrying the no-rank-a-person caveat. The 9 anchor is *"becomes the page a reader keeps open while working"* — converting the chapter's own decision content from prose-scattered to keep-open table is a positive move toward that anchor, not the mere removal of a flaw and not inflation: zero new facts, every atom already verified. The deferred *concrete thresholds* remain deferred by design (that is an honest scope choice, not a UTILITY deficiency now that the decision logic the chapter *does* own is in reach-for-it form). Earned. |
| 4 | **DEPTH** | **9** | hold | The L149 contested-zone deepening (when each school's prescription wins, from already-cited Clean Code / APoSD) secured 9 at Pass 2. Unchanged this pass — full mechanism + metrics taxonomy + Goodhart discipline + contested zone + honest limitations + alternatives + when-to-use, all sourced; comfortably sustains a deep-dive. Held. |
| 5 | **READABILITY** | **8** | hold | **Honestly not movable to 9.** The two Pass-1 soft drags (em-dash density, stacked bullets) were fixed at Pass 2 and that work banks a *secure* 8. The 9 anchor is *"effortless to read at full precision,"* and the chapter still carries genuinely dense stretches **by subject necessity** — the metrics-landscape table, the three-form code demo, and now a second (small, scannable) decision table. The new table aids rhythm and scannability at the chapter's end, but a measurement chapter's irreducible density does not become "effortless." Awarding 9 here would be the inflation the bar exists to prevent — **held at 8.** |

**Cluster subtotal: 44 / 50** (none below 6).

---

## The three content-floors (PASS / FAIL — independent of score)

| Floor | PASS/FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep over the full draft AND module `src/`+README → **0 hits** (re-swept post-edit). Clean Code vs A Philosophy of Software Design presented as two reputable schools, neither crowned (L138–149); the L149 deepening frames the contexts symmetrically. The new §When-to-use table maps each metric to the question it answers and **crowns no metric** — it ranks none against another; the LOC row states LOC's own limitation, not a rival's. "Alternatives" (now L165+) is trade-off/approach-based, not a leaderboard. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §Limitations (5 items: subjectivity, proxy-not-causation, conflicting prescriptions, aggregate-index false confidence, harm of measuring individuals) + §When to use with explicit "ease off" / when-NOT conditions. Every metric family in the landscape table carries its trap column; the new decision table reinforces it — coverage marked necessary-not-sufficient, LOC marked vanity with no counter-metric rescue. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** no invented atom — `java:S3776`, McCabe 1976, the SonarSource white paper, the CK suite, Goodhart/Strathern, DORA/SPACE all trace to pinned authorities; the new table re-forms only these; deferred thresholds/JEPs honestly marked verify-at-pin, none asserted as settled. **Compile:** green at the pin (`_EXAMPLE.md`: BUILD SUCCESS, 43/0/0, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11) — the prose edit does not touch the module. **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES — the one MAJOR (M1) is closed in code and re-verified across passes. No open accuracy defect in the copy-verbatim deliverable. |

Floors gate the aggregate; they are not averaged in. **All three PASS.**

---

## Verdict

**Phase-3 chapter scorecard: SHIP (auto-approve).**

- Floors A/B/C-source: **PASS** (all three).
- Aggregate: **44/50**, no cluster below 6.
- **Clears the 44/50 (88%) auto-approval bar.**

> **DISPOSITION: SHIP.** The single in-bounds lift move Pass 2 had already identified — a reach-for-it
> decision table distilled from material already in the chapter (L106–122), framed as questions-not-verdicts
> — landed cleanly and carried **UTILITY 8→9** on a genuine move toward its 9 anchor ("the page a reader
> keeps open while working"). The move is a pure re-form of already-verified atoms: **no new fact, no scope
> change, no floor risk** — banned-phrase sweep still 0, person holds, snippet tag-regions untouched, build
> still green. READABILITY honestly holds at a secure 8 (its remaining distance is the subject's irreducible
> density, not a fixable drag), and inflating it to 9 was declined. The aggregate moves **43→44** and the
> chapter is defect-free with all floors clean. Per SCORING.md it clears the 88% bar and **auto-approves**.

---

## Flagged weakest cluster (post-ship — informational)

- **Weakest cluster:** **READABILITY at 8** (CLARITY/ACCURACY/UTILITY/DEPTH at 9). This is **not** a drag to
  lift — it is the subject's irreducible density (a measurement chapter has tables and a code demo). Chasing
  the 9 anchor ("effortless at full precision") would risk thinning real content for rhythm, which is the
  floor-adjacent "padding" line the lift loop forbids. No further lift is warranted: the chapter ships at 44.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| — | — | — | **None open.** All Pass-1/2 flags resolved; the Pass-2 UTILITY decision-aid recommendation is **done** (the §When-to-use table) and carried UTILITY 8→9. | — |

> **Resolved this pass (do not re-flag):** the UTILITY decision aid — the §When-to-use question→metric→
> counter-metric table — is **done and verified** (pure re-form, floors re-swept clean), and carried
> UTILITY 8→9.
> **Resolved at Pass 2 (do not re-flag):** the CLARITY table→antidotes bridge (L99) and the DEPTH
> contested-zone deepening (L149) — carried CLARITY and DEPTH 8→9. The READABILITY em-dash/bullet drags —
> fixed; that work banks READABILITY's secure 8.
> **Resolved at Pass 1 (do not re-flag):** the M1 false cyclomatic-equality claim — re-verified still
> correct across all passes.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 open) | LIFT-LOOP | Independent harsh baseline. Confirmed build green & B1 fix by hand; confirmed M1 (cyclomatic-equality) genuinely false in companion Javadoc. |
| 1 (indep) | 2026-06-28 | 41 / 50 | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 closed in code) | LIFT-LOOP | Re-read the M1 fix: nested Javadoc + README now correctly state cyclomatic is *higher* (≈13 vs ≈5); Fig 2.1's abstract equal case no longer contradicts the module. **ACCURACY 7→9**, unblocking UTILITY's reservation. 39→41. |
| 2 (indep) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 closed) | LIFT-LOOP | Verified the polish in the source: em-dash prose density →~4/1000 (under target); deep-dive bullet runs reduced; **CLARITY bridge (L99)** closes the table→antidotes seam → **CLARITY 8→9**; **contested-zone deepening (L149)** adds *when each school wins* from already-cited sources → **DEPTH 8→9**. UTILITY/READABILITY held at 8. 41→43 — 1 under the bar. |
| 3 (indep) | 2026-06-28 | **44 / 50** | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 closed) | **SHIP** | Applied the in-bounds UTILITY move Pass 2 named: a reach-for-it question→metric→counter-metric **decision table** in §When to use, distilled from L106–122 (no new fact, framed questions-not-verdicts). Re-swept clean: 0 banned-phrase, 0 narration first-person, 3/3 snippet tag-regions intact, build still green. **UTILITY 8→9** on a genuine move toward its anchor; READABILITY held at a secure 8 (density irreducible). **43→44 — clears the 88% bar. Auto-approves.** |

> Note: this is the **3rd and final** allowed pass of the bounded loop (passes 0–3 used the loop's
> ≤3-pass budget). The chapter cleared the bar **on** the last pass with the single honest in-bounds move
> the prior pass had pre-identified — not by lowering the bar and not by inflating a cluster. READABILITY
> was deliberately **left at 8** rather than nudged to 9, because its remaining distance to the anchor is the
> subject's irreducible density; the 44th point was earned at UTILITY, where a real reach-for-it artifact
> was added on already-verified material.

---

## Learnings & pipeline suggestions

- **A pre-identified in-bounds lever from a prior pass is the cheapest honest 8→9.** Pass 2 ended one point
  short but did the harder work of *naming* the single earnable move (a decision-aid table) and proving its
  atoms were already verified. Pass 3 then only had to *execute and re-verify* it. Promotable to the
  lift-loop playbook: when a pass ends short, spend its closing analysis pinning the exact next move and
  pre-clearing its source-trace, so the following pass is execution-plus-verification, not re-diagnosis.
  This is what kept the final pass from drifting into inflation — the move was specified before it was made.
- **Consolidating scattered-but-verified decision content into one scannable artifact is a real UTILITY
  lift, distinct from padding.** The chapter already *had* its decision logic — spread across four prose
  beats. Re-forming it into a keep-open table added zero facts yet moved UTILITY toward its "page a reader
  keeps open" anchor. The discriminator from padding: the table *replaces nothing real, invents nothing, and
  shortens the reader's path to a decision*. Worth a SCORING.md note: a re-form that lowers a reader's
  reach-for-it cost is a legitimate positive move toward the UTILITY 9 anchor, even though it carries no new
  material — the test is whether it makes the chapter *more usable*, not *longer*.
- **Hold the floor cluster honestly even when it is the last one below the line.** READABILITY was the only
  cluster under 9 and lifting it would have hit the bar too — but its distance to 9 is irreducible subject
  density, not a fixable drag, so it was held at 8 and the 44th point was earned elsewhere. The bar is
  protected precisely by refusing the *convenient* point and taking the *earnable* one. A useful reinforcement
  that "which cluster do I lift?" is answered by "which has a genuine move toward its anchor," never by
  "which is cheapest to nudge over the line."

---

## RETURN LINE

**Ch2 44/50 (C9/A9/U9/D9/R8) floors A-PASS/B-PASS/C-PASS -> SHIP**
