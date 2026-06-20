# INDEPENDENT SCORECARD — Ch 39 — model: Claude Sonnet 4.6 — 2026-06-20

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 91 (owner, folds 92 + 93 + 95)
- **Slug:** `91_refactoring_legacy_modernization`
- **Title:** Changing Code Without Breaking It
- **Part / arc position:** Part XI — Refactoring & Legacy (Ch 39, opener)
- **Artifact scored:** `03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
- **Verified against:** SOURCE-PIN.md pinned 2026-06-20
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — independent gate)
- **Date:** 2026-06-20
- **Lift-pass #:** 0 (initial)

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | 8 | The four-section ladder (method → under-test → system → platform) unified by "one invariant" is genuinely clarifying. CONCEPT callouts land well; terms glossed before use. Held from 9: no figure to carry the refactoring loop or seam-creation flow (both are described mechanically in prose but would benefit from a visual); the deep-dive opening sentence (§"Deep dive", line 78) is self-narrating structure-announcement ("The four sections are not four topics; they are *one discipline at four scales*, and seeing that is what turns..."). |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set at the pins in SOURCE-PIN.md. No invented rule IDs, config keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, or quoted claims. | 7 | Fowler refactoring names (Extract Method, Rename, Move, Replace Conditional with Polymorphism, Introduce Parameter Object) match SOURCE-PIN §7 Fowler 2e 2018. Feathers seam types (object, interface, link) match WELC 2004. LTS sequence 8→11→17→21→25 is traceable from OpenJDK (SOURCE-PIN §1). JPMS "since 17" correct (JEP 396/403). OpenRewrite recipe names (`UpgradeToJava17/21/25`, "composite so 25 includes 21") appear in back matter with ⚠ flag and in the body at line 74 — flagged correctly for pin-level verification but not yet confirmed at pin. Strangler fig 2004 date (Fowler bliki) flagged ⚠ @pin in back matter. No invented atoms found. Score would rise to 8 once SOURCE-VERIFY confirms recipe names and bliki wording. |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | 7 | The "When to use what" section (lines 106–114) is concrete and decision-keyed to real situations. The chapter answers the practitioner questions a Java engineer actually faces (untested legacy, system too big to refactor, version migration). Held at 7 because EXAMPLE-BUILD is PENDING — no verified code snippets exist yet; the seam-creation loop and the refactoring-commit sequence are described in prose but not demonstrated with executable code. With a green companion module the utility score would likely be 9. |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | 8 | Covers four authoritative bodies of knowledge (Fowler refactoring discipline, Feathers legacy/seams, Fowler strangler fig, Java version migration) and synthesizes them under one unifying invariant — that is substantive depth, not aggregation. The limitations section (lines 84–94) enumerates nine concrete when-NOT-to-use points, all sourced from the dossier. Mild imbalance: the strangler fig section is thinner than the other three (one substantive paragraph + one CONCEPT callout vs multiple paragraphs for refactoring and legacy); the migration section's "composite so 25 includes 21" claim (line 74) and seam-creation loop (lines 58–60) each earn their depth. |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice per VOICE-GUIDE.md? Hook in, forward hook out? | 7 | Voice is largely locked (third person, no narration contractions, no first person found). The hook (two-year cancelled rewrite) is vivid and stakes-bearing. The forward hook at line 118 pulls to the next chapter well. Three concerns that prevent a higher score: (1) Em-dash density is substantially above target (~8/1,000 words) — a rough count finds 22–25 em-dashes in ~2,200 words of body prose; the deep-dive paragraph (lines 78–82) alone contains 7. (2) Self-narrating phrases: "the load-bearing honesty" (line 60), "the load-bearing precondition" (line 82), "the load-bearing point" — VOICE-GUIDE explicitly bans "the load-bearing point is" as self-narration (§ "Excise self-narration"). (3) Deep-dive opening sentence is throat-clearing structure-announcement. |

**Cluster subtotal:** 37 / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | No winner crowned; banned phrasings absent; comparative claims sourced. | PASS | No banned constructions found ("better than", "unlike X", "the problem with X", "superior", "beats" — none present). The big-bang rewrite is framed as a scale/risk argument about an approach, not a comparison to a named competing product; its limitations are treated as real (line 98: "the big-bang rewrite is the option this chapter argues against at scale, not a forbidden one"). Alternatives section (lines 96–103) is approach-based and non-crowning. Each of the four techniques in the chapter gets both its strongest case and its hardest limitation. |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a when-NOT-to-use. | PASS | All four techniques carry inline limitations: refactoring-without-tests (line 49), mixing hats (lines 47, 87), characterization-pins-bugs (lines 60, 89), strangler stall + dual-running cost + shared-state difficulty (lines 68, 90), migration-recipe-gaps + dep-first + big-jump-risk + migration≠modernization (lines 74, 91–93). Dedicated §"Limitations & when NOT to reach for it" (lines 84–94) covers nine distinct when-NOT-to-use points. No technique is presented as cost-free. |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented detail; companion builds green; passes CODE-REVIEW gate. | PASS (SOURCE-TRACE) / PENDING (COMPILE) / N/A (CODE-REVIEW) | Source-trace: no invented rule IDs, API signatures, config keys, or version numbers found. All named-canon sources (Fowler Refactoring 2e, Feathers WELC, Fowler StranglerFig bliki) correctly annotated ⚠ @pin in back matter (lines 122–127). OpenRewrite recipe names flagged ⚠ @pin. LTS list and JPMS-since-17 traceable from OpenJDK SOURCE-PIN §1. COMPILE: PENDING (EXAMPLE-BUILD not yet run; noted in draft header and back matter). CODE-REVIEW: N/A (per scoring instructions). SOURCE-TRACE alone: PASS. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (≥35/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate.
- [x] **LIFT-LOOP** — close; apply the line-level fixes below and re-score (increment lift-pass #).
- [ ] **CUT** — below bar or a structural floor failure; return to drafting or re-scope.

**One-line rationale:** 37/50 clears the numeric bar (≥35, no cluster below 6), all floors PASS on source-trace, but READABILITY is pulled down by demonstrably high em-dash density and self-narrating phrases ("the load-bearing X") that VOICE-GUIDE explicitly bans; one targeted prose pass to excise these would push READABILITY to 8 and potentially lift ACCURACY once the ⚠-flagged recipe names receive pin-confirm.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 7
- **Why it is the weakest:** Em-dash density is estimated at 22–25 per ~2,200 words (~10–11 per 1,000), roughly 30% above the ~8/1,000-word target. Compounding this, VOICE-GUIDE §"Excise self-narration" explicitly bans "the load-bearing point is" and related "load-bearing X" constructions as AI tells; the draft uses "load-bearing" as a prose-describing adjective four times. The deep-dive opening is a structure-announcement paragraph that narrates what the four sections do rather than making the point directly.
- **Single highest-leverage move to lift it:** Run one em-dash audit pass on the body text — convert most em-dash appositives to periods or commas (especially in the deep-dive section, lines 78–82), and simultaneously replace all "the load-bearing X" constructions with the actual claim.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · paragraph · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | §"Deep dive", lines 78–82 (first two paragraphs) | 7 em-dashes in ~200 words; opening sentence is structure-announcement self-narration ("The four sections are not four topics; they are *one discipline at four scales*, and seeing that is...") | Open instead with the substantive claim directly: "One invariant governs all four scales: preserve behavior, verify with tests, move in reversible steps." Convert em-dash appositives to periods or commas throughout. |
| 2 | READABILITY | Lines 60, 82 — "the load-bearing honesty" / "the load-bearing precondition" | "load-bearing" as a self-narrating adjective is explicitly on the VOICE-GUIDE banned-self-narration list | State the point: "Characterization tests pin current behavior including bugs." / "The test net is the precondition that makes safe change possible." |
| 3 | READABILITY | §"How it works — Refactoring", lines 45–49 | Long sentence at line 49 runs 72 words, three nested clauses and two em-dashes | Break at "Fowler's 'two hats'" into a standalone sentence. |
| 4 | ACCURACY | §"Java version migration", line 74; back matter line 125 | OpenRewrite recipe names `UpgradeToJava17/21/25` and "composite so 25 includes 21" — correctly flagged ⚠ but unconfirmed at pin | Confirm against OpenRewrite 8.81.0 docs (SOURCE-PIN §6); if confirmed, remove the ⚠ flag; if names differ, correct. |
| 5 | CLARITY | §"Strangler fig" section — thinner than the other three | One substantive paragraph + one CONCEPT callout vs. multiple paragraphs for refactoring and legacy; the data/consistency challenge ("database migration, dual-writes") is named but not explained | Add one sentence on how teams typically isolate shared state (event outbox or dual-write adapter before the DB is migrated), citing the Fowler bliki at pin. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | initial independent score |

---

## Learnings & pipeline suggestions

1. **Em-dash density is an objective, measurable readability signal that should be auto-checked before scoring.** The VOICE-GUIDE targets ~8/1,000 words and warns the em-dash appositive is "the most over-used cadence and a clear AI tell." A simple grep/count pass (`grep -o ' — ' draft.md | wc -l` divided by word count) could surface a flag in the pre-SCORE pipeline step before the scorer spends time on prose that will need a mandatory pass. Propose: add an em-dash density pre-check (threshold 10/1,000 words = auto-flag) to the AUDIT gate checklist.

2. **"Load-bearing" as a self-narrating adjective is a recurring AI tell.** VOICE-GUIDE §"Excise self-narration" lists "the load-bearing point is" but the pattern generalizes to "the load-bearing X" (honesty, precondition, fact, insight). Propose: add "load-bearing" to the banned-self-narration token list in VOICE-GUIDE so the AUDIT gate explicitly checks it.

3. **EXAMPLE-BUILD PENDING reduces UTILITY score structurally.** A chapter on refactoring discipline — where the whole point is demonstrating small-step behavior-preserving transformations — needs code to be fully evaluated on utility. The scoring rubric's UTILITY cluster explicitly calls out "examples are runnable" as the top-tier criterion. Pipeline suggestion: when EXAMPLE-BUILD is PENDING at score time, cap UTILITY at 7 (not 8+) and note it in the lift-pass log so the re-score after EXAMPLE-BUILD can update it.

4. **The "one invariant at four scales" synthesis is this chapter's genuine contribution.** The deep-dive section (lines 76–82) does real conceptual work by showing how refactoring, legacy-characterization, strangler-fig, and version-migration are all the same discipline at different scales. This synthesis would be strengthened by a single figure (the four-scale ladder with the invariant at the top), which the figure-designer agent should produce before SHIP.
