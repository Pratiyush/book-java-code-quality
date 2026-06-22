# INDEPENDENT SCORECARD — Ch 39 — model: Claude Sonnet 4.6 — 2026-06-20 (lift pass 1)

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 91 (owner, folds 92 + 93 + 95)
- **Slug:** `91_refactoring_legacy_modernization`
- **Title:** Changing Code Without Breaking It
- **Part / arc position:** Part XI — Refactoring & Legacy (Ch 39, opener)
- **Artifact scored:** `03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
- **Figure artifact:** `05-figures/91_refactoring_legacy_modernization/fig91_1.png` (source-traced via `fig91_1.sources.md`)
- **Verified against:** SOURCE-PIN.md pinned 2026-06-20
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — independent gate, lift pass 1)
- **Date:** 2026-06-20
- **Lift-pass #:** 1 (voice pass; fig91_1.png now referenced)

---

## What changed since pass 0

| Item | Pass-0 finding | Pass-1 status |
|---|---|---|
| Figure (fig91_1.png) | Not present; CLARITY held at 8 for no visual | Added; referenced at line 43; source-traced in fig91_1.sources.md |
| "load-bearing" self-narration | 4 instances in prose | 0 instances — cleared |
| Deep-dive opening | Structure-announcement sentence | Now opens with direct substantive claim: "One invariant governs all four scales:" |
| Em-dash density | Estimated 10–11/1,000 words | Measured 19.7/1,000 across full prose body (target ~8/1,000) — not improved |
| Figure prose intro | N/A | Figure dropped cold after "## How it works" heading; no introductory prose sentence before the image tag |
| Banned word "easy" | Not individually flagged | Present in CONCEPT callout: "make the change easy" |
| Banned word "just" | Not individually flagged | Present twice: "often just a `record`" (L54); "just editing" in Limitations (L91) |

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Mechanism explained cleanly; ordered; terms defined; figure present and load-bearing | **8** | The four-scale ladder unified by one invariant is structurally clear. CONCEPT callouts land well. Fig 91.1 is now present and correctly source-traced, showing the safe-change loop and four-scale breakdown — this directly addresses the pass-0 gap. Score held at 8 (not 9) because the figure is dropped cold: it appears immediately after "## How it works" with no introductory prose sentence naming what it shows. VOICE-GUIDE requires prose before the figure; as placed, a reader turning to this page sees the diagram before any framing. The strangler-fig section also remains thinner than the other three sections — one paragraph plus one CONCEPT callout vs multiple paragraphs for refactoring and legacy. |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set; no invented atoms | **7** | Fowler refactoring names (Extract Method, Rename, Move, Replace Conditional with Polymorphism, Introduce Parameter Object) match SOURCE-PIN §7. Feathers seam types (object, interface, link) match WELC 2004. LTS sequence 8→11→17→21→25 traceable from OpenJDK SOURCE-PIN §1. JPMS since-17 correct (JEP 396/403). OpenRewrite recipe names (`UpgradeToJava17/21/25`; "composite so 25 includes 21") and the Fowler StranglerFig bliki 2004 date remain flagged ⚠ @pin in back matter — correctly flagged, but unconfirmed. The figure sources.md explicitly pins OpenRewrite 8.81.0 (SOURCE-PIN §6) and JDK 21.0.11/25.0.3 (SOURCE-PIN §1) for all diagram labels. No invented atoms found in draft or figure. Score would reach 8 once SOURCE-VERIFY confirms the ⚠-flagged recipe names and bliki wording at pin. |
| 3 | **UTILITY** | Reader can act on this; decision frames; "When to use what" concrete | **7** | The "When to use what" section (lines 111–119) is concrete and decision-keyed. The chapter answers real Java-engineer questions about untested legacy, systems too large to refactor, and version migration. The Alternatives section (lines 102–109) is approach-based and non-trivial. EXAMPLE-BUILD remains PENDING — no verified, compiled code snippet exists; the four-step refactoring loop and seam-creation are described in prose but not demonstrated with runnable code. Cap at 7 while EXAMPLE-BUILD is PENDING (per pass-0 pipeline suggestion: a chapter on refactoring discipline without a compiled refactoring demonstration cannot reach 8+ on utility). |
| 4 | **DEPTH** | Verified substance: mechanism, evidence-for, honest limitations, alternatives, when-to-use | **8** | Four authoritative bodies of knowledge (Fowler refactoring discipline, Feathers legacy/seams, Fowler strangler fig, Java version migration) synthesized under one unifying invariant — genuine conceptual depth. Nine distinct when-NOT-to-use points, all sourced. The deep-dive section (lines 82–88) does real synthetic work connecting the four scales to a single discipline. Score held at 8 (not 9) because the strangler-fig section is structurally thinner: one substantive paragraph plus one CONCEPT callout; the data/consistency challenge ("database migration, dual-writes") is named but the mechanism is not shown. Depth is otherwise rich. |
| 5 | **READABILITY** | Prose carries reader; locked voice; no jargon wall; hook in; forward hook out | **7** | The "load-bearing" self-narration phrases are gone and the deep-dive opens with the direct substantive claim — both targeted fixes from pass 0 successfully made. The hook (two-year cancelled rewrite) is vivid and stakes-bearing; the forward hook pulls cleanly to the next chapter. Three residual blockers prevent a higher score: (1) Em-dash density measured at 19.7 per 1,000 words across the full prose body — more than double the ~8/1,000 target. The Refactoring section runs 28/1,000; the Migration CONCEPT callout 29/1,000; "Alternatives" 25/1,000; "When to use" 34/1,000; the hand-off 33/1,000. The deep-dive is the sole section near target (8/1,000). A voice pass was declared but the em-dash problem was not materially reduced. (2) Figure dropped cold — no introductory prose before the image tag at line 43 (VOICE-GUIDE: "Refer to it before it appears, naming what it shows"). (3) Banned word "easy" in CONCEPT callout (line 52: "make the change easy") and "just" as filler in line 54 ("is often just a `record`"). Note: "refactoring without tests is just editing" (line 91) is a deliberate Fowler-aligned signal phrase in a Limitations bullet and is contextually defensible — not counted as a failure. |

**Cluster subtotal: 37 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text |
|---|---|---|---|
| **A — NEUTRALITY** | No winner crowned; banned phrasings absent; comparative claims sourced | **PASS** | Banned constructions scanned: "better than", "unlike X", "the problem with X", "superior", "beats", "kills", "destroys", "blows away", "no reason to use" — none present. The big-bang rewrite is framed as a scale/risk argument about an approach ("the big-bang rewrite is the option this chapter argues against at scale, not a forbidden one" — line 103), not a crowning verdict against a named product. Alternatives section is approach-based. All four techniques receive both strongest case and hardest limitation. No cross-subject claim without framing. |
| **B — HONEST-LIMITATIONS** | Every feature gets its hardest objections AND explicit when-NOT-to-use | **PASS** | All four techniques carry inline limitations: refactoring-without-tests (lines 54, 91); mixing-hats (lines 52, 92); characterization-pins-bugs (lines 65, 94); strangler stall + dual-running cost + shared-state (lines 73, 95); migration-recipe-gaps + dep-first + big-jump-risk + migration-not-modernization (lines 79, 96–98). Dedicated §"Limitations & when NOT to reach for it" (lines 90–99) covers nine distinct when-NOT-to-use points. No technique is presented as cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented detail; companion builds green; passes CODE-REVIEW | **PASS (SOURCE-TRACE) / PENDING (COMPILE) / N/A (CODE-REVIEW)** | Source-trace: no invented rule IDs, API signatures, config keys, or version numbers found in draft or figure. Named-canon sources (Fowler Refactoring 2e, Feathers WELC, Fowler StranglerFig bliki) correctly annotated ⚠ @pin in back matter (lines 127–131). OpenRewrite recipe names flagged ⚠ @pin. LTS list and JPMS-since-17 traceable from OpenJDK SOURCE-PIN §1. Fig 91.1 source-trace sidecar (`fig91_1.sources.md`) traces every label and atom to pinned sources or the chapter draft — no invented labels. COMPILE: PENDING (EXAMPLE-BUILD not run; noted in draft header, back matter, and companion spec). CODE-REVIEW: N/A per scoring instructions. SOURCE-TRACE alone: PASS. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (≥35/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate.
- [x] **LIFT-LOOP** — bar is met numerically (37/50, no cluster below 6); all floors PASS on source-trace; apply targeted prose fixes and re-score (pass 2).
- [ ] **CUT** — below bar or a structural floor failure; return to drafting or re-scope.

**One-line rationale:** 37/50 clears the numeric ship bar and all floors PASS, but the voice pass claimed for lift-1 did not materially reduce em-dash density (19.7/1,000 vs ~8/1,000 target) and introduced a new figure-placement issue (no introductory prose before the image). READABILITY is still at 7; a genuine em-dash audit pass plus a one-sentence figure intro would move READABILITY to 8 and the aggregate to 39, which is the highest the chapter can reach while EXAMPLE-BUILD is PENDING (UTILITY capped at 7).

---

## Remaining blockers to 90%

| # | Cluster | Blocker | Label |
|---|---|---|---|
| 1 | READABILITY | Em-dash density at 19.7/1,000 — materially above the 8/1,000 target. Every section except the deep-dive is well above target. Convert most em-dash appositives in Refactoring, Migration, Alternatives, "When to use", and hand-off sections to periods, commas, or parentheses. | prose-fixable |
| 2 | READABILITY | Figure dropped cold at line 43 — no introductory prose sentence before the image. Add one sentence naming what the figure shows before the `![...]` tag. | prose-fixable |
| 3 | READABILITY | Banned word "easy" at line 52 ("make the change easy") and "just" as filler at line 54 ("is often just a `record`"). Rewrite: "preparatory refactoring (refactor before a change so the change is straightforward)" and "Replace Constructor with Factory often becomes a `record`". | prose-fixable |
| 4 | ACCURACY | OpenRewrite recipe names `UpgradeToJava17/21/25` and "composite so 25 includes 21" — flagged ⚠ @pin, unconfirmed at SOURCE-PIN §6 (OpenRewrite 8.81.0). | needs-pin-verify |
| 5 | UTILITY | EXAMPLE-BUILD PENDING — no verified, compiled companion module; utility capped at 7. | needs-example-build (separate gate; not a prose-fixable blocker) |

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 7 (unchanged from pass 0)
- **Why it is still the weakest:** The pass-0 score identified em-dash density and "load-bearing" self-narration as the two primary blockers. The "load-bearing" self-narration was cleared. The em-dash density was not reduced — it is now precisely measured at 19.7/1,000 across the prose body, worse than the pass-0 estimate of 10–11/1,000 (the pass-0 scorer measured only ~2,200 words of body; the full prose body is ~3,191 words and the higher density is real). Two new minor items were identified: the figure dropped cold, and two banned-word hits ("easy", "just" as filler).
- **Single highest-leverage move for pass 2:** Perform a genuine em-dash audit — go line by line through the Refactoring, Migration CONCEPT, Alternatives, "When to use", and hand-off sections and convert every em-dash appositive that can become a period or comma. Target: ≤30 em-dashes in the full prose body (~9/1,000). Simultaneously add one introductory sentence before the figure and fix the two banned-word hits.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | Initial independent score |
| 1 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | Voice pass: "load-bearing" self-narration cleared (0 instances); deep-dive opening rewritten to direct claim; fig91_1.png added and source-traced. Em-dash density not reduced (measured 19.7/1,000). Figure dropped cold (no prose intro). Two banned-word hits ("easy", "just") newly identified. Score unchanged at 37/50. |

---

## Learnings & pipeline suggestions

1. **A declared "voice pass" must include a measurable em-dash audit, not just prose rewrites.** The pass-0 score flagged em-dash density as the primary READABILITY blocker and identified the highest-density section. The lift pass addressed the self-narration phrases (correctly) but did not materially reduce em-dash count. A voice-pass checklist should include a mandatory em-dash count before and after: run `grep -o ' — ' draft.md | wc -l` divided by prose word count and confirm it has moved toward the 8/1,000 target. Propose adding this as a gated sub-step in the drafter's voice-pass SOP.

2. **Figure placement requires a prose-introduction sentence to be VOICE-GUIDE compliant.** When a figure is added in a lift pass, the figure-designer or drafter must simultaneously add the introductory prose sentence ("Figure 91.1 shows...") immediately before the `![...]` tag. The figure sidecar (`fig91_1.sources.md`) is fully source-traced, but source-trace does not substitute for prose placement. Propose adding "figure introduction prose present" as a checkbox in the figure-designer gate report.

3. **"Easy" and "just" as filler survive draft reviews because they occur inside CONCEPT callouts.** The banned-word check should not skip callout blocks (lines beginning with `> **CONCEPT**`). Propose extending the auditor's banned-term scan to include callout blocks explicitly.

4. **EXAMPLE-BUILD PENDING creates a structural UTILITY ceiling.** At pass 1, with EXAMPLE-BUILD still pending, UTILITY is capped at 7 regardless of prose quality. The chapter is on refactoring discipline — executable code demonstrating the refactoring loop is the primary utility artifact. The EXAMPLE-BUILD gate should be a parallel track to the voice-pass track, not a later sequential step, so that UTILITY can be scored fully at re-score time.

5. **The chapter's core synthesis (one invariant at four scales) is intellectually sound and the figure now carries it visually.** The fig91_1.sources.md source-trace is thorough and correctly attributes every label. This is a model for how a figure's source-trace sidecar should be produced.
