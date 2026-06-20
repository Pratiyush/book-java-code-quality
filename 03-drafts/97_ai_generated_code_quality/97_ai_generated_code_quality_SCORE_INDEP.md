# INDEPENDENT SCORECARD — Ch 41 — model: Claude Sonnet 4.6 — 2026-06-20

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 97 (folds 99)
- **Slug:** `97_ai_generated_code_quality`
- **Title:** The Draft That Looks Like a Deliverable — Quality of AI-generated Java: characteristic risks, refactoring/test-gen guardrails
- **Part / arc position:** Part XII — AI-Era Code Quality (Ch 41, OPENS Part XII; umbrella)
- **Artifact scored:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Verified against Java code quality the pins in SOURCE-PIN.md** — pinned at 2026-06-20 (re-check date: 2026-06-20)
- **Scorer:** chapter-scorer agent (independent — Claude Sonnet 4.6, not the drafter model)
- **Date:** 2026-06-20
- **Lift-pass #:** 0

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | **8** | The two core mechanisms — vulnerability inheritance and confident wrongness — are introduced in plain language before any technical detail. The CONCEPT callouts give each its own clear definition. The chapter's two-halves structure (risks → guardrails) is announced upfront and held throughout. The stance logic flows cleanly: mechanism → characteristic risks → stance → refactoring guardrail → test-generation guardrail → why the gate is source-agnostic. Minor deduction: the "Deep dive" section is dense (lines 65–69) with multiple stacked-appositive sentences that briefly lose the reader in qualified clauses before landing the point. |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set at the pins in SOURCE-PIN.md. No invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Snippets/examples verified. | **7** | Positive: the chapter correctly treats the percentage statistics ("~40% with critical gaps," "XSS missed in the large majority of cases") as unverified snapshots requiring dated attribution — it explicitly says they "must be verified against the specific dated study and cited as a snapshot, never as a constant" (line 47). arXiv papers 2502.01853 and 2409.19182 are named (present in SOURCE-PIN §7 as acknowledged pending rows). CodeScene's three guardrails are attributed but flagged "⚠ wording @pin." No invented rule IDs, config keys, GAV coordinates, or version numbers appear. Deduction: the statistics are still cited in the prose body as "often-cited rates in the range of 'around 40% with critical gaps'" — presenting them as approximate truth while flagging verification, rather than cutting them entirely or moving them to back matter until pinned. The dossier's own note (line 9) marks these ⚠ VERIFY AT PIN and the SOURCE-PIN §7 rows are acknowledged-gaps. The prose handles this transparently but the chapter stops short of stating "figures not yet pin-verified" inline for the reader; the back matter carries the honest flag. |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | **8** | The "When to use what" section (lines 93–101) gives concrete, differentiated guidance: drafting/scaffolding (AI + verify), mechanical large-scale refactoring (OpenRewrite/Refaster, not AI), judgment-heavy one-offs (AI with test net and review), test generation (spec-derived or AI-suggested + human-assert + mutation testing), security (SAST/SCA/secrets as matter-of-course). The "Limitations & when NOT to reach for it" (lines 72–81) is a bulleted, scannable checklist. The "Alternatives & adjacent approaches" section (lines 83–91) maps each choice to its tradeoff. Deduction: the companion module is PENDING, so no runnable code anchor exists in this draft. The chapter is concept-heavy and practical in framing but gives no inline code illustration of the SQL injection pattern or the hollow test anti-pattern — two examples mentioned in the hook and companion spec but absent in prose. |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | **8** | Two distinct mechanisms (vulnerability inheritance + confident wrongness) are explained at mechanism level, not just named. The refactoring and test-generation sections both go past the general warning to name the specific failure mode (behavior-change without a net; tests-from-code defeating double-bookkeeping). The "Deep dive" section (lines 63–69) advances the argument — the gate is source-agnostic, the bottleneck shifts from writing to verifying, confident wrongness defeats the human's instinct-based defense — rather than restating the first half. The statistics-are-volatile point is elevated to a durable structural argument (the discipline holds when the percentage doesn't). The self-aware framing (this book is AI-written) is a genuine depth move. Deduction: the "double-bookkeeping" concept is load-bearing but "double-bookkeeping" itself is a coined term attributed to no specific source in the dossier; the dossier flags attribution as needed ("⚠ wording @pin"). |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice per VOICE-GUIDE.md? Hook in, forward hook out? | **6** | The hook paragraph (line 22) is strong — concrete scenario, three specific defects named, the "plausible by construction" thesis delivered with force. The forward hand-off (lines 104–106) is a pulling thread, not a syllabus. Significant deductions on voice discipline: (a) Contractions in narration prose: "it's" (lines 51, 61, 105), "can't" (line 74), "isn't" (line 74), "don't" (lines 79, 81) — the VOICE-GUIDE bans contractions in narration; (b) Em-dash density: ~103 em-dash uses in ~4,695 words = approximately 22 per 1,000 words, against a target of ~8 per 1,000 — nearly 3× the ceiling; every other clause is bracketed by em-dashes; (c) Self-narration tells: line 67 "this is why 'AI proposes, the deterministic stack disposes' is not a slogan but the load-bearing principle" announces the principle's importance rather than stating the principle; (d) The "Deep dive" section contains back-to-back paragraphs each beginning with "What is genuinely new…" / "The honest center…" / "There is no version of…" — uniform mid-length declarative cadence that goes flat before the section ends. The voice holds better in the earlier sections than in the deep-dive. |

**Cluster subtotal:** 37 / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | Neutral comparative survey: each option gets its strongest case and its hardest limitation; no crowning; banned phrasings absent ("better than," "unlike X," "the problem with X," "superior," "beats"). No rival crowned superior. Any cross-subject claim has a cited source. | **FAIL** | **Offending text (line 57, narration prose):** "An LLM suggesting a structural change or modernization is *not behavior-preserving by construction* — **unlike an IDE refactoring or an OpenRewrite recipe** (Chapters 39, 40), whose transformations are correct by their type-aware mechanics." The phrase "unlike an IDE refactoring or an OpenRewrite recipe" is the exact banned construction "unlike X" from NEUTRALITY.md's blocklist. This is not in a designated comparison/migration chapter; it appears in running narration. The comparison itself is factually defensible and necessary to the chapter's purpose — but the phrasing uses the banned construction. **Fix (minimal — in-bounds):** Rewrite to neutral pattern: "AI refactoring is not behavior-preserving by construction; IDE refactoring and OpenRewrite recipes are correct by their type-aware mechanics (Chapters 39, 40), and that distinction is the source of the guardrail." No content change, no floor risk in the rewrite, no new facts needed. |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a "when NOT to reach for this." Environment/compatibility caveats stated where relevant. | **PASS** | The chapter dedicates a full section to "Limitations & when NOT to reach for it" (lines 72–81) covering over-trust, vulnerability inheritance, hallucinated dependencies, the tests-from-code anti-pattern, behavior-non-preserving refactoring, coverage theater, volume-outpaces-review, statistics-volatility, and the familiarity gap. Limitations are also woven into each mechanism section and the CONCEPT callouts. Both the AI-assisted-refactoring and AI-test-generation uses name their hardest failure mode explicitly. The chapter correctly avoids selling AI tools without also naming when NOT to use them (it explicitly addresses "Refuse AI vs gate AI" in the Alternatives section, rejecting refusal while naming the gating requirement). |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims — everything traces to the pins or is flagged to `09-flags/`. COMPILE = PENDING (separate track). CODE-REVIEW = N/A per scoring instructions. | **PASS (source-trace); PENDING (compile); N/A (code-review)** | **Source-trace:** No invented rule IDs, config keys, tool flags, API signatures, GAV coordinates, or version numbers appear anywhere in the prose body. The percentage statistics ("~40%," "86% XSS miss") are explicitly flagged in the prose itself as needing verification against dated studies, and the back matter repeats "ALL stats ⚠ VERIFY @pin + DATED + ATTRIBUTED." arXiv papers 2502.01853 + 2409.19182 are named as sources and are acknowledged in SOURCE-PIN §7 as existing but having unverified figures. CodeScene's three guardrails are cited and flagged "⚠ wording @pin." The chapter does not assert any specific numeric figure as a confirmed fact. "Slopsquatting" traces to arXiv 2409.19182 (dossier line 48). "Double-bookkeeping" is flagged for attribution at pin (dossier line 9). No cross-subject claim about IDE/OpenRewrite behavior asserts a figure — it describes mechanical properties referenced from within-book chapters. **COMPILE:** PENDING — companion module not yet built; EXAMPLE-BUILD flagged as pending in both the draft header (line 5) and companion spec (line 113). Toolchain confirmed ready (JDK 21.0.11+25.0.3). Not scored against chapter on FLOOR C per scoring instructions. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (≥35/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate.
- [x] **LIFT-LOOP** — FLOOR A FAIL on "unlike X" banned phrasing (line 57). One-word fix needed before cluster re-score. After the floor fix is applied, aggregate 37/50 clears the ≥35 bar with no cluster below 6 — chapter is SHIP-eligible pending FLOOR A repair and READABILITY lift.
- [ ] **CUT** — below bar or a structural floor failure; return to drafting or re-scope.

**One-line rationale:** FLOOR A fails on a single banned phrase ("unlike an IDE refactoring or an OpenRewrite recipe," line 57) that is a one-sentence rewrite; once cleared, the aggregate (37/50) clears the ship bar — but READABILITY at 6 is the weakest cluster and benefits from a targeted contraction-and-em-dash pass before final approval.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 6
- **Why it is the weakest:** Three overlapping voice violations: (1) contractions in narration ("it's," "can't," "isn't," "don't") in at least six locations in running prose — the VOICE-GUIDE bans all narration-level contractions; (2) em-dash density at ~22 per 1,000 words against the ~8 target, producing a clause-bracketing cadence that reads as AI-generated fluency; (3) the Deep Dive section accumulates self-narrating phrases that announce the point rather than state it ("is not a slogan but the load-bearing principle").
- **Single highest-leverage move to lift it:** One pass replacing narration contractions with their expansions ("it is," "cannot," "is not," "do not") and converting the majority of em-dash appositives to periods, commas, or parentheses — no content change, no new facts, no floor risk.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | **FLOOR A — NEUTRALITY** | §"AI-assisted refactoring and test generation: the guardrails" · line 57, first sentence | "unlike an IDE refactoring or an OpenRewrite recipe" — banned phrase "unlike X" per NEUTRALITY.md blocklist | Rewrite: "AI refactoring is not behavior-preserving by construction; IDE refactoring and OpenRewrite recipes are correct by their type-aware mechanics (Chapters 39, 40), and that distinction is the source of the guardrail." Zero new facts; one in-bounds rewrite. |
| 2 | **READABILITY** | Lines 51, 61, 74, 79, 81, 105 — narration prose | Contractions in narration: "it's" (×3), "can't," "isn't," "don't" (×2) | Expand each: "it is," "cannot," "is not," "do not." No content change. |
| 3 | **READABILITY** | §"Deep dive" lines 65–69, and throughout — em-dash density | ~22 em-dashes per 1,000 words vs ~8 target; every other clause in the Deep Dive section bracketed with em-dashes | Convert approximately two-thirds of em-dash appositives to periods or commas; keep em-dashes only where the appositive is the point, not the scaffold. |
| 4 | **READABILITY** | §"Deep dive" line 67, paragraph 2, last sentence | "this is why 'AI proposes, the deterministic stack disposes' is not a slogan but the load-bearing principle" — self-narrating ("is not just a slogan but") | Cut the defence: "Against confident wrongness, the only reliable defense is the mechanical stack. AI proposes; the deterministic stack disposes." |
| 5 | **ACCURACY** | Line 47 — statistics paragraph | The percentage figures ("around 40% with critical gaps," "XSS missed in the large majority of cases") are framed as "often-cited rates" without explicit inline dating — the back matter carries the flag but the prose body does not name the study or year inline | Either: (a) cut the figures from prose and retain only the back-matter flag, or (b) once pin-verified, insert the dated inline attribution: "(arXiv 2502.01853, as of [year])" immediately after each figure. Current state is acceptable but leaves an accuracy gap visible to a sharp reader. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | FAIL (line 57: "unlike X") | PASS | PASS (src-trace); PENDING (compile); N/A (code-review) | LIFT-LOOP | initial score — independent model (Claude Sonnet 4.6) |

---

## Learnings & pipeline suggestions

1. **"Unlike X" is the single most common NEUTRALITY slip in comparison chapters.** The banned phrase survives review because it is used to *make a factually correct and necessary contrast* rather than to crown a winner — it feels harmless. The fix costs nothing (rewrite to "A does X; B does Y"), so the lesson is to run the banned-phrase scan as the first action on every draft, before any quality scoring. Recommend promoting a pre-gate bash scan of banned phrases as Step 4a in PIPELINE.md before the auditor's full pass.

2. **Statistics handled as snapshots (not constants) — a model to follow.** This chapter's treatment of the "~40% / 86% XSS" figures is exemplary: they appear in prose with an explicit "must be verified against the specific dated study" instruction, repeated in back matter. No other chapter in the book handles a contested, fast-moving empirical field this cleanly. Recommend capturing this pattern in GUIDELINES.md as the standard for any chapter in a rapidly-evolving domain.

3. **Em-dash density as an AI-tell check should be mechanized.** The ~22/1,000 rate is a repeating pattern across multiple chapters. The VOICE-GUIDE already specifies ~8/1,000 as a soft target with AUDIT-gate flagging, but no script yet enforces it. Recommend adding an `em_dash_density.sh` lint to `.claude/scripts/` that fails at >15/1,000 and warns at >10/1,000; this would catch the slip automatically before the auditor pass.

4. **Contraction elimination is a mechanical fix that should be auto-checked.** Multiple narration contractions passed through the drafter uncaught. A simple grep for `'s `, `n't `, `'ve `, `'ll `, `'d ` in non-callout, non-quoted prose lines would catch all instances. Recommend adding this to the lint suite alongside the banned-phrase scan.

5. **Self-narration ("is not just a slogan but the load-bearing point") recurs.** The VOICE-GUIDE explicitly flags "the load-bearing point is," "is not just," and similar meta-scaffolding. These appear to survive because they function as emphasis beats — the drafter uses them to signal importance. The fix is always the same (state the point; do not announce it). Recommend adding these exact patterns to the auditor's banned-phrase scan.
