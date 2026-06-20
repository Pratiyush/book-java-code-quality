# INDEPENDENT SCORECARD — Ch 47 — model: Claude Sonnet 4.6 — 2026-06-20

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 110 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `110_maturity_model_roadmap`
- **Title:** Where to Start, and How to Keep Going
- **Part / arc position:** Part XIV — Capstone & Synthesis (Chapter 47, final chapter)
- **Artifact scored:** `03-drafts/110_maturity_model_roadmap/110_maturity_model_roadmap_v1.md`
- **Verified against:** `02-research/110_maturity_model_roadmap/110_maturity_model_roadmap_RESEARCH.md` + `00-strategy/SCORING.md`
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — independent track, not the authoring model)
- **Date:** 2026-06-20
- **Lift-pass #:** 0 (initial score)

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | Verdict | Evidence / offending text + fix |
|---|---|---|---|
| **A — NEUTRALITY** | No winner crowned; no banned phrasings ("better than", "superior", "beats", "unlike X", "the problem with X"); comparative claims cited. | **PASS** | Synthesis chapter. No tool is crowned superior over another. The "staged roadmap vs rigid maturity ladder" framing in the Alternatives section presents both with trade-offs, not a verdict. DORA's move from maturity levels to capabilities is stated as a framing fact (key 85), not a superiority claim. No banned phrase found anywhere in the draft. All tool references delegate to prior chapters by key. |
| **B — HONEST-LIMITATIONS** | Every feature/claim has its hardest objections and an explicit when-NOT-to-use. | **PASS** | The "Limitations & when NOT to reach for it" section (six bullet points) is substantive and explicit: maturity-level-as-vanity-metric (Goodhart trap), roadmap-is-a-default-not-your-plan, tools-without-culture-fail, more-maturity-not-more-value-past-a-point, everything-is-dated, no-done. Each roadmap stage's when-NOT (the pain-first override) is embedded in the CONCEPT callout ("Do not ignore your own most expensive problem because it's listed in Stage 3"). "Never:" bullet in "When to use what" is explicit. No claim is presented as cost-free. |
| **C — SOURCE-TRACE** | Zero invented rule IDs, config keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, or quoted claims. Everything traces to the dossier/pin. | **PASS** | This chapter contains no tool flags, GAV coordinates, version numbers, or benchmark figures — it is a pure synthesis chapter. All 45 tool/practice references map to prior chapter keys (key 34, 62, 71/82, 42/75, 27/28/30, 48/80, 76/81, 84, 87, 29, 65/66, 33/55, 35, 47, 79, 70/73, 56, 88/85, 105, 106-108, 38, 100, 59/96, 06/90, 109) consistent with the dossier. The one new primary atom — DORA's "capabilities over maturity levels" framing — is verified in the dossier at "☑ frame" (key 85, dora.dev), with a "TO-PIN" note in the back matter. This is a pending deep-verification item, not an invented detail; the frame is correctly attributed to DORA. No invented claims detected. |
| **C — COMPILE** | Companion module builds green via `./mvnw -B verify`. | **PENDING** | No per-chapter buildable module exists or is required. Draft comment and dossier both mark EXAMPLE-BUILD = N/A (a roadmap artifact, not a buildable module). Tracked separately; does not fail the chapter. |
| **C — CODE-REVIEW** | Module passes the CODE-REVIEW gate. | **N/A** | No buildable module; EXAMPLE-BUILD = N/A. |

---

## The five clusters (score 1–10)

| # | Cluster | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | The five-stage roadmap mechanism is explained in clear order with CONCEPT callouts carrying the major ideas. The why-before-how structure is followed (vanity-level-4 hook → the mechanism). The "context-driven not linear" concept is explicitly unpacked. Mild gap: the dossier specifies Fig 110.1 (a stage × practice × chapter visual), but no figure appears in the draft and none is referenced as in-progress. The CONCEPT callouts serve as a text substitute, which holds clarity but leaves a visual rhythm gap noted in GUIDELINES §8. No clarity-breaking jumps or undefined terms. |
| 2 | **ACCURACY** | 9 | Synthesis chapter with no invented atoms. Every tool/practice maps to a dossier key; back-matter section lists them explicitly. No version numbers, GAV coordinates, tool flags, or benchmark figures to invent. The one new primary atom (DORA capabilities-over-levels) is attributed to key 85 and dora.dev, consistent with the dossier's "☑ frame" verification. Minor: the dossier marks this with "TO-PIN" in the back matter (SOURCE-PIN §7 canon: "DORA capabilities/continuous-improvement TO-PIN"), indicating full pin-level verification is deferred — not an invented detail, but a pending verification step. Scored 9 rather than 10 for this open verification item. |
| 3 | **UTILITY** | 8 | Highly actionable. The "When to use what" section gives concrete entry points for different team contexts (from zero, to make the gate real, when the basics hold, when you need to govern, to sustain). The "start where your pain is" guidance includes specific examples (team with production incidents → Stage 3; security mandate → security gate). Stage-by-stage chapter cross-references make the whole book navigable. The one-page roadmap artifact spec (companion artifact) is well-defined for a reader to adapt. No runnable example required (EXAMPLE-BUILD = N/A). |
| 4 | **DEPTH** | 7 | Deliberately a synthesis chapter — no new primary material. Depth is measured by quality of synthesis. The Goodhart/DORA framing adds a coherent explanatory layer (why the roadmap is a guide not a ladder, why DORA dropped rigid levels). The sociotechnical conclusion (culture is the deciding factor) ties the book's through-line together. The "Alternatives & adjacent approaches" section is thin (five short bullets, no extended treatment), which is appropriate for a closing chapter but slightly under-serves readers who need the contrast laid out more fully. The "Deep dive" section sustains the synthesis well. Depth appropriate for a closing synthesis chapter; not penalized for not introducing new material, per SCORING.md (depth = verified substance, not word count). |
| 5 | **READABILITY** | 7 | The hook (Level-4 vanity team) is concrete, stakes-first, and effective — one of the stronger hooks in the book. The closing section is genuinely moving as a book-closer. However, the draft violates the locked-person rule (VOICE-GUIDE: third person, invisible narrator, no "you" in narration) in multiple places. Specific violations: "You've reached the end. You have the *case* for quality..." (Hook, line 22 of draft); "You start where *your* pain is" and "you remember that the hardest parts are people" and "you treat the whole thing" (How it works, CONCEPT callout, line 59); "So start where your pain is... Measure whether it improved an outcome you care about. Then do the next thing" (Closing section — these are imperative sentences, which are permitted, but "your" in "an outcome you care about" is narration-level second person); the closing paragraph uses imperative mode throughout ("Start where your pain is. Measure. Improve continuously."). Imperative is permitted for instructions, but "You've reached the end" and "You have the case" are narration, not instruction. The em-dash density appears at the high end (~15–18 in ~2,000 words against a ~16/2,000 target — borderline but within tolerance). No banned filler detected. No self-narration beyond mild instances. Prose rhythm is strong otherwise. |

**Cluster subtotal:** 8 + 9 + 8 + 7 + 7 = **39 / 50**

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar (39/50 ≥ 35/50; no cluster below 6; all THREE floors PASS); ready for the human approval gate.
- [ ] **LIFT-LOOP** — not required; bar is cleared.
- [ ] **CUT**

**One-line rationale:** All floors PASS and the aggregate 39/50 clears the ship bar with no cluster below 7; the draft functions as an effective book-closer with a strong hook, clear staged mechanism, and explicit limitations — the second-person voice violations and the pending Fig 110.1 are the two specific items to address before the human approval gate, but neither is fatal.

---

## Aggregate

**Aggregate 39/50**

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 7 (tied with DEPTH, but READABILITY has fixable prose violations)
- **Why it is the weakest:** Multiple narration-level second-person uses violate the locked voice ("You've reached the end", "You have the case for quality", "You start where your pain is" in the CONCEPT callout). The VOICE-GUIDE bans first person and second person in narration; imperative is permitted for instructions only.
- **Single highest-leverage move to lift it:** Recast narration-level "you" passages to the invisible third-person narrator. Hook line 22: "The end of the book is the beginning of the reader's work. The *case* for quality lives in Part I..." Closing: "Start where the pain is. Measure whether it moved an outcome that matters. Then do the next thing." (These are already imperatives and survive as instructions; the narration bridges need recasting.) The CONCEPT callout at line 59 needs "You start where your pain is" recast to "The team starts where its pain is" or restructured as an imperative instruction.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · paragraph · text) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | Hook, para 2: "You've reached the end. You have the *case* for quality (Part I), the *techniques* across a dozen parts, and — last chapter — a concrete *reference stack* (Chapter 46)." | Narration-level second person — banned per VOICE-GUIDE ("no you in narration"). | Recast: "The book has made the *case* for quality (Part I), covered the *techniques* across a dozen parts, and — last chapter — built a concrete *reference stack* (Chapter 46)." |
| 2 | READABILITY | How it works § CONCEPT callout, line ~59: "You start where *your* pain is, not at Stage 0 dogmatically; you climb for results, not levels; you remember that the hardest parts are people, not plugins; and you treat the whole thing as a continuous practice with no 'done.'" | Narration-level second person mid-CONCEPT callout. | Recast as imperative (permitted) or third person: "Start where the pain actually is, not at Stage 0 dogmatically. Climb for results, not levels. The hardest parts are people, not plugins; treat the whole thing as a continuous practice with no 'done.'" |
| 3 | READABILITY | Closing section, para 1: "You've reached the end of the book, so it ends not with a hand-off but with a send-off" — the section opens "This is the last chapter, so it ends not with a hand-off" (line ~100) — acceptable, but then "to carry with you" uses "you". | Borderline narration-second-person ("carry with you"). | Recast: "...to carry into the work ahead." |
| 4 | ACCURACY / SOURCE-TRACE | Back matter, routing line: "SOURCE-PIN §7 canon: DORA capabilities/continuous-improvement TO-PIN." | DORA capabilities-over-levels framing verified only at frame level ("☑ frame"); full pin-level verification deferred. | Confirm against dora.dev (key 85) at pin before human-approval gate; update the dossier verify report when done. Not a blocking issue — the attribution is correct and the dossier flags it. |
| 5 | CLARITY | No figure present despite dossier specifying Fig 110.1 (stage × practice × chapter roadmap visual). | The visual roadmap is the most load-bearing deliverable of the chapter; its absence leaves a gap in the figure plan. | Produce Fig 110.1 per the spec in the dossier (HTML → PNG, stages × practices × chapter keys, marked "start where your pain is"). This is a pipeline step (figure design), not a prose fix; flag for the /figure 110 command. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 39 / 50 | PASS | PASS | PASS / PENDING / N/A | SHIP | initial score (independent scorer: Claude Sonnet 4.6) |

---

## Learnings & pipeline suggestions

1. **Synthesis chapters need special voice vigilance.** A closing chapter that speaks *to* the reader (second-person send-off) is a natural authorial instinct, but the locked voice bans narration-level "you". The hook and the closing are the highest-risk sections for this drift. Pipeline suggestion: add a dedicated "narration-person" check to the AUDIT gate checklist — grep for "You've", "You have", "You start", "you treat", "you remember" as voice violations, not just the existing banned filler/hype terms.

2. **Figure plan compliance should be a CLARITY gate item.** The dossier specifies Fig 110.1; the draft contains no figure and no reference to one being produced. A figure specified in the dossier but absent from the draft should surface as a CLARITY gap at the gate stage, not only at the figure-design step. Pipeline suggestion: the CLARITY reviewer should cross-check the dossier's "Figure:" spec against the draft.

3. **TO-PIN verification items should be closed before the chapter-scorer runs.** The DORA capabilities-over-levels atom carries a "TO-PIN" marker in the back matter, meaning the source-verifier left it open. The scorer can only note it; the verifier should complete it. Pipeline suggestion: the SOURCE-TRACE floor should require all "TO-PIN" markers resolved before scoring begins, or the scorer should treat them as PENDING items with a hard resolve-before-approve-gate rule.

4. **The EXAMPLE-BUILD = N/A path is clean.** The synthesis/roadmap chapter with no buildable module handled the COMPILE floor correctly (PENDING, not FAIL). This pattern is confirmed working — no pipeline change needed, but record it as a precedent for future non-code chapters.

→ Also append to `00-strategy/PIPELINE-LEARNINGS.md`.
