# SCORECARD (INDEPENDENT) — Ch 25 "Principles, Measures, and Where the Lines Fall" (key 53 + folds 54 + 57)

> **Independent (different-model) re-score** of the printed Chapter 25 against `00-strategy/SCORING.md`.
> Harsh-skeptic pass. Artifact: `03-drafts/53_solid_coupling_cohesion_packages/53_solid_coupling_cohesion_packages_v1.md`.
> Gate reports read whole: `_EXAMPLE.md` (build green), `_CODEREVIEW.md` (CODE-REVIEW PASS), `_SCORE.md`
> (prior self-score 43/50). Pin 2026-06-20. Scorer: chapter-scorer (independent). Date: 2026-06-28.
> Lift-pass #: 2 (one in-bounds prose pass + one re-evaluation after confirming the figure-render lever
> was already closed). FLOOR-C COMPILE/CODE-REVIEW resolved since the self-score (was PENDING).

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 53 (folds 54, 57) — `01-index/FINAL_INDEX.md` Ch 25, OPENS Part VI
- **Slug:** `53_solid_coupling_cohesion_packages`
- **Title:** Principles, Measures, and Where the Lines Fall (SOLID, coupling, cohesion & package structure)
- **Part / arc position:** Part VI — Architecture & Design Governance (opener)
- **Artifact scored:** `03-drafts/53_solid_coupling_cohesion_packages/53_solid_coupling_cohesion_packages_v1.md`
- **Verified against SOURCE-PIN:** pinned 2026-06-20 (re-check date 2026-06-28 — pins unchanged)
- **Scorer:** chapter-scorer agent (independent re-score)
- **Date:** 2026-06-28
- **Lift-pass #:** 2

---

## The five clusters (final, after lift)

| # | Cluster | Score (1–10) | Note (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The dual-failure hook (over-engineered vs under-structured) frames the whole; the three-altitudes synthesis (principle = *why* / measure = *whether* / place = *where*) genuinely unifies three folded dossiers; four CONCEPT callouts (two-schools, main-sequence, module-ladder, three-altitudes deep dive) + two load-bearing figures anchor the mechanism. Not a 10: a never-met-the-topic reader cannot reconstruct the *metrics* from here — by design they are routed to Ch 2. |
| 2 | **ACCURACY** | **9** | Body prose carries **zero** `UNVERIFIED`/`verify-at-pin` markers (all 8 `⚠` are confined to the back-matter traceability rows, which is the rubric-correct handling of genuinely-unpinnable secondary canon). Load-bearing atoms trace to pinned/runnable sources: `I = Ce/(Ca+Ce)` confirmed against the built `DependencyDirection.java`; `slices().should().beFreeOfCycles()` = real ArchUnit 1.4.2 API cited as illustration (enforcement routed to Ch 26); JPMS = JEP 261 (OpenJDK JEP index, primary). 6/6 snippets verified with recorded paths (`_CODEREVIEW.md`). Named canon (Martin/North/Liskov-Wing/Constantine-Yourdon) used as *attributed concepts*, gaps flagged to `09-flags/` — not asserted verbatim. **fig53_1.png confirmed re-rendered** (reads "Chapter 5", no "Ch 15" — the flag's "raster stale" note is itself stale). No drift artifact remains. |
| 3 | **UTILITY** | **9** | Concrete decision frames a team can act on: SOLID-as-direction-not-target; DIP-inverts-a-wrong-direction-dependency; cycles as the one hard structural gate; by-feature vs by-layer keyed to context; the module-strength ladder ("pick the lowest rung that holds"); gate-only-the-thin-hard-subset. The companion module makes each contrast runnable. |
| 4 | **DEPTH** | **8** | The three-altitudes unification (goal → proxy → heuristic → substrate), the judgment-vs-enforceable split, and the "both hook failures treated a heuristic as a target" diagnosis are senior-architect material. Held at 8 (not 9): the chapter deliberately routes metric *definitions* to key 04 (Ch 2) and *enforcement* to Ch 26, so several threads (main-sequence distance, LCOM's competing definitions, what `slices()` computes) are named rather than developed here. Correct scope decision — and **correctly not padded** to chase the number. |
| 5 | **READABILITY** | **9** | Em-dash density brought to **7.8/1000** (from 13.1 — the chapter was flagged em-dash-heavy ~54; now 33 in prose), under the house <8/1000 norm. Sentences that stacked two–three dashes now break cleanly into colons/commas/sentences; the locked voice holds; precision without density-for-its-own-sake. Strong dual-failure hook, four callouts, two tables, clean judgment→enforcement hand-off to Ch 26. |

**Cluster subtotal: 44 / 50** (no cluster below 6).

---

## The three content-floors (PASS / FAIL)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | Banned-phrase sweep over prose = 0 (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperform`). SOLID-vs-CUPID/"simple over SOLID" framed as two attributed schools, "crown neither" stated; by-layer vs by-feature as a trade-off table, "Neither is crowned" in prose; each SOLID principle gets intent **and** over-application trap; both hook failure modes condemned equally; "best judged qualitatively" / "no winner" confirmed non-crowning. No section title carries a superlative. Figure fig53_1 footer reinforces "neither crowned." |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Dedicated §Limitations with an explicit when-NOT-to-use for every element: dogmatic-SOLID-over-engineers + YAGNI; SOLID-ness-cannot-be-gated; SRP-subjective; metrics-are-proxies; some-coupling-necessary + LCOM-contested; metrics-need-structure-first; no-universal-structure + over-modularization; structure≠design + restructuring-invasive. Per-principle traps inline; the deep-dive "two failures" resolution; §When to use. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ **PASS** | **SOURCE-TRACE:** zero invented atoms; load-bearing facts trace to pin or built module; canon gaps + unpinned tools (ckjm/JDepend) flagged to `09-flags/53_solid_canon_verbatims_and_tool_defaults_verify_at_pin.md`; body prose marker-free. **COMPILE:** `_EXAMPLE.md` — `mvn -B -Pquality verify` on JDK 21.0.11 → BUILD SUCCESS, 13 tests / 0 failures, 0 Checkstyle, 0 SpotBugs. **CODE-REVIEW:** `_CODEREVIEW.md` — PASS across all six dimensions, BLOCKER 0 · FAIL 0 · FIX 0 · NIT 0; 6/6 snippets statement-complete, ≤9 lines, banned-word-free. |

---

## Verdict

- [x] **SHIP** — clears the bar (44/50, no cluster below 6); all THREE floors PASS; ready for the Step-12 human approval gate.

**One-line rationale:** A clean, neutral, well-figured Part-VI opener that unifies three dossiers; after one in-bounds prose pass (em-dash 13.1→7.8/1000, cross-refs numbered + verified) and confirmation that the figure-render drift was already closed, it reaches 44/50 with all floors green — no padding, no inflation.

---

## Flagged weakest cluster (post-lift)

- **Weakest cluster:** DEPTH — score 8.
- **Why it is the weakest:** The chapter deliberately defers metric definitions (Ch 2) and enforcement (Ch 26), so a few threads are named rather than developed here. This is the correct anti-triplication scope decision.
- **Single highest-leverage move to lift it:** None that is in-bounds — adding the deferred definitions/enforcement would be padding *and* scope creep (it would duplicate Ch 2 and Ch 26). DEPTH is honestly capped at 8 by design; the bar is met without touching it.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIM | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 43 / 50 | PASS | PASS | PASS / ⚠ PENDING / — | LIFT | initial self-score; COMPILE pending (toolchain ready) |
| 1 (indep) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS / PASS / PASS | LIFT | **READABILITY 7→9:** em-dash density 13.1→7.8/1000 (≈22 mid-sentence dashes → colons/commas/sentence breaks across hook, overview, SOLID/coupling/package sections, deep-dive, limitations, figure captions). **Cross-refs:** "the metrics chapter"→"Chapter 2", "the next chapter"/"next chapter"→"Chapter 26" (verified vs FINAL_INDEX: Ch 2 folds key 04; Ch 26 = enforcement). No new facts; 6/6 includes intact; 0 banned phrases. ACCURACY held at 8 pending figure-drift check. |
| 2 (indep) | 2026-06-28 | **44 / 50** | PASS | PASS | PASS / PASS / PASS | **SHIP** | **ACCURACY 8→9 (fair re-score, not inflation):** read `fig53_1.png` directly — it was already re-rendered from the corrected HTML (reads "Chapter 5", zero "Ch 15"); the one drift artifact I had held a point for does not exist. Body prose marker-free; snippets verified with paths; canon gaps correctly flagged (rubric-correct, not a deduction). No drift remains → top band earned. |

---

## In-bounds edits applied (Pass 1) — prose only, no code touched

All edits are dash-removal using already-verified material (no new facts, no scope change, no floor risk):

| # | Location | Change |
|---|---|---|
| 1 | Hook | "fail the same goal — *safe, cheap change* —" → "fail the same goal, *safe, cheap change*," |
| 2 | Overview (chapter map) | "is the *where* — the lines…" → "is the *where*: the lines…"; split a dashed run into two sentences |
| 3 | Overview ("does NOT cover") | named the routed chapters: "the metrics chapter **(Chapter 2)**", "the very next chapter **(Chapter 26)**" (also removed 2 dashes) |
| 4 | SOLID intro | "useful *vocabulary and set of heuristics* — not a law…" → comma |
| 5 | OCP / LSP bullets | dashed clauses → colon / sentence break |
| 6 | Package §, table lead-in | "bad structure — everything in one `com.acme.service` package —" → parenthetical |
| 7 | Package §, "Neither is crowned" | em-dash → sentence break |
| 8 | Module-strength-ladder CONCEPT | "(names only — discipline)" → "(names only, by discipline)" |
| 9 | Cycles paragraph | "(next chapter)" → "(Chapter 26)" |
| 10 | Deep-dive | "safe, cheap change — the maintainability…" → colon; "Principle, measure, place — why…" → colon; "**The metrics chapter (Chapter 2)**" / "**The next chapter (Chapter 26)**" numbered |
| 11 | "One idea worth holding" + by-feature lead-in | dashed asides → sentence break / comma |
| 12 | Limitations (proxies) | "Do not optimize the number — read the code." → semicolon |
| 13 | Figure captions 25.1 / 25.2 | triple-dash captions rewritten with periods/colons |

Confirmed post-edit: 6 `<!-- include: -->` directives intact; 0 banned phrases; em-dash 7.8/1000; no double-spaces; 14 headings intact. **No companion-code file was edited, so no rebuild / `check_snippets` was required** (per task gate; the green build + CODE-REVIEW from `_EXAMPLE.md`/`_CODEREVIEW.md` stand unchanged).

---

## Residual (tracked elsewhere — not a ship blocker)

- **Named-canon attributions** (Martin SOLID defs + SDP/SAP/main-sequence/REP/CCP/CRP; North CUPID; Liskov-Wing formal LSP; Constantine-Yourdon origin) and **ckjm/JDepend** are not SOURCE-PIN §7/tool rows. Honestly flagged in `09-flags/53_solid_canon_verbatims_and_tool_defaults_verify_at_pin.md`, used as attributed concepts (no verbatim asserted). Closing them is a SOURCE-PIN runbook action (pin new rows) or an out-of-band verbatim check — out of scope for a prose lift, correctly deferred. This is the *expected* state for non-pinned secondary sources and does not reduce ACCURACY below 9.
- **Flag self-staleness:** the flag still says fig53_1.png "shows Ch 15 until re-rendered"; the PNG read shows it is already re-rendered ("Chapter 5"). Recommend the figure agent strike that line from the flag on next pass.

---

## Learnings & pipeline suggestions

1. **Em-dash density is a cheap, high-leverage READABILITY lever and should be a scripted pre-pass.** This chapter sat at 13.1/1000 (≈1.6× the house <8 norm) purely from mid-sentence dashes; ~22 mechanical conversions to colon/comma/period moved READABILITY 7→9 with zero content change. A `check_em_dash.sh` (count `—` per 1000 prose words, excluding the HTML front-matter comment, list-label dashes, and back-matter rows) would catch this at draft time instead of at scoring. Propose adding it alongside the neutrality pre-pass.
2. **Cross-refs should be numbered, not named, and validated against FINAL_INDEX.** "the metrics chapter" / "the next chapter" are unverifiable phrasings; numbering them (Chapter 2 / Chapter 26) both removed dashes *and* made them greppable against the index. Suggest a CLARITY-gate check: every "the … chapter" reference resolves to a FINAL_INDEX row.
3. **A scorer must read the figure raster, not trust the flag.** I nearly held ACCURACY at 8 for a "Ch 15" drift the flag described; the rendered PNG had already been fixed. Flags can lag the artifact — verify the live asset before docking a cluster.
4. **Honest canon-gap flagging is rubric-*correct*, not an accuracy penalty.** A topic built on non-pinnable secondary canon (Martin/North/Liskov-Wing) earns a 9 when it attributes-as-concept and flags the gap; it would only fail FLOOR C if it asserted those as verbatim pinned fact. Scorers should not reflexively dock ACCURACY for the presence of `⚠` markers when they are confined to back-matter and correctly routed.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
