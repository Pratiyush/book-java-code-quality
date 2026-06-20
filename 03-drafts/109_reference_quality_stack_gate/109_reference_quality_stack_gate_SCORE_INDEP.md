# INDEPENDENT SCORECARD — Ch 46 — model: Claude Sonnet 4.6 — 2026-06-20

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 109 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `109_reference_quality_stack_gate`
- **Title:** So What Do I Actually Set Up? (A reference quality stack & gate design)
- **Part / arc position:** Part XIV — Capstone & Synthesis, Chapter 46 (opener)
- **Artifact scored:** `03-drafts/109_reference_quality_stack_gate/109_reference_quality_stack_gate_v1.md`
- **Verified against:** SOURCE-PIN.md — pinned at 2026-06-20
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — INDEPENDENT, separate model from drafter)
- **Date:** 2026-06-20
- **Lift-pass #:** 0 (initial score)
- **Note:** Self-score (SCORE.md by drafter) was 46/50. This independent scorecard disagrees materially.

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | **7** | The per-pick "what it catches · cost · alternative · when-to-swap" format is clean and the gate design CONCEPT callout is well-ordered. However: (a) Fig 109.1 specified in the dossier (layered stack mapped onto gate stages) is absent from the draft — this is the one chapter where a visual synthesis is mandatory, and its absence leaves the "layered and de-duplicated" claim carried entirely by a prose list; (b) the "Overview / What this chapter covers" block (lines 28–38) is partly redundant with the hook and partly a nested bullet list — the structure announcement before "How it works" slows entry; (c) the three-part opening (pull-quote preamble + Hook section + Overview) means the same orienting idea ("45 chapters neutral, now one recommendation") is stated three times before the reader sees any stack content. The mechanism is clear once reached; the entry to it is not. |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set at the pins. No invented rule IDs, GAV coordinates, version numbers. | **7** | No raw GAV coordinates, version strings, or tool flags are asserted without a "verify-at-pin" caveat — this is correct practice for an aggregating capstone. All claims route to chapter cross-references (Ch 3, Ch 16, Ch 18, Ch 27, Ch 29, Ch 33, Ch 35, etc.) and dossier keys (37, 62, 67, 27/28, 30/29, 31/32, etc.) that trace to pinned tool dossiers. **One live ambiguity:** "JUnit (6) + AssertJ + Mockito" (stack entry, line 51; also in the dossier at line 27) — the parenthetical "(6)" reads as a version number. JUnit 6 is not a released version as of the pin date (current is JUnit 5/Jupiter; JUnit 6 was in early proposal stages). If "(6)" is a count of listed items rather than a version, the notation is ambiguous and will mislead readers. This needs explicit resolution — either "JUnit 5 (Jupiter)" with the correct version or removal of the parenthetical. This is not a fatal invented-fact violation if the "(6)" is parenthetical count, but it is an accuracy risk that must be resolved before ship. Score held at 7 rather than 8 because this ambiguity is load-bearing for a reader configuring a real build. |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | **8** | The "When to use what" section is directly actionable and maps concrete scenarios (legacy codebase, small team, budget, swap a pick) to specific guidance. The four-stage gate breakdown is copyable. The incremental-adoption framing ("format first, then fast linters, then heavier analysis") is the chapter's most practical sentence. Penalized from potential 9: (a) the capstone module build is PARTIAL — the EXAMPLE-BUILD = PARTIAL note is honest, but a reader who clones the reference project today gets Stage 1-3, not the full stack; the chapter's primary utility claim rests on that runnable project; (b) no actual code snippets or config fragments appear in the draft — the chapter describes what the pom.xml will show but shows none of it. For a chapter whose core value is "here is the concrete stack, runnable," the absence of even one snippet from the partial build weakens the "open while working" utility. |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | **7** | The deep-dive section (lines 69–75) on how the recommendation stays honest is the chapter's intellectual peak — it resolves a genuine tension (neutrality vs. helpfulness) with a real argument rather than hand-waving, and "forty-five chapters of neutrality earn the one recommendation" is a substantive framing. The structural-lessons synthesis (layering, de-duplication, feedback-latency ladder, new-code focus, incremental adoption) correctly treats this as a capstone of principles, not a tool list. However, as a synthesis chapter, depth is inherently bounded — each tool and each gate mechanism has its own chapter; this one connects them. The "tools don't make quality" closing argument is present but brief (three sentences in the deep-dive, four bullets in Limitations). A reader who skipped Parts I and XIV would find this chapter's depth thin; a reader who read all 45 prior chapters finds it a satisfying synthesis. Score reflects the correct scope, not a deficiency in the synthesis itself. |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice? Hook in, forward hook out? | **6** | The hook question ("so what do I actually set up?") is genuinely gripping and the forward hand-off to Ch 47 is clean. The per-pick format (catches · cost · alternative) is easy to scan. However, four specific readability problems lower this score materially: (1) **Triple opening**: the pull-quote (lines 18–19), the Hook section (lines 22–25), and the Overview (lines 27–38) all make the same orienting point — the draft opens three times before content begins. (2) **Self-narration density**: "This chapter is the one place in the book that recommends" (line 24), "the honest center, and the reason this chapter cannot be the last word even though it's the book's one recommendation" (line 75), "This is why the very next chapter is not more tooling but a maturity model" (line 75), "understanding how it stays honest while breaking the no-crowning rule is the key to using it well" (line 69) — the narrator repeatedly describes the chapter's own argument rather than making it. VOICE-GUIDE.md explicitly bans this: "Do not narrate the chapter's own argument." (3) **"Carve-out" repetition**: the phrase "carve-out" appears 10+ times across a ~4,500-word draft ("the carve-out's honesty," "the carve-out's whole point," "the carve-out's core honesty," "this is a deliberate exception," etc.). The concept earns one clear statement and then should drive the chapter implicitly, not be re-announced every section. (4) **Second-person intrusion**: "It hands you a single, coherent... setup" (line 24) and "here is a coherent composition that works... here is why... here is when you'd choose differently" (line 71) use "you/your" and "here is" in the narrator voice, which the locked voice bans ("no first person," imperative only for instructions). Score 6: above-floor but needs a prose pass specifically targeting self-narration and the triple-open. |

**Cluster subtotal: 35 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | No rival crowned superior. Banned phrasings absent. Every pick names alternative + trade-off. Capstone carve-out: recommends one setup, framed "one defensible setup," never "the best." | **PASS** | Full banned-phrase sweep: zero hits for "better than," "unlike X," "the problem with X," "superior," "beats," "outperforms," "kills," "destroys," "blows away," "the obvious choice over," "no reason to use X." Every tool entry uses the "what it catches · cost/limit · alternative · when-to-swap" format. Alternatives named explicitly for all nine tool layers. The chapter frames the recommendation as "one defensible setup, not the setup" multiple times. The "Alternatives & adjacent approaches" section is approach-based, not a leaderboard. Capstone carve-out (NEUTRALITY.md line 20, "key 109") is invoked correctly — the chapter recommends without crowning. No section title carries a comparative superlative. Note: the two occurrences of "best" in the draft ("the word *best* never appeared as a verdict" lines 23, 23) are meta-references describing the book's own neutrality policy, not verdicts — correctly not banned-phrase violations. |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a "when NOT to reach for this." | **PASS** | Seven explicit when-NOT items in "Limitations & when NOT to reach for it" (lines 79–85): this-is-a-stack-not-the-stack; adopt-incrementally; stack-is-code-to-own; tools-dont-make-quality; versions-move; all-OSS-not-effort-free; green-capstone-necessary-not-sufficient. Each tool entry carries a Cost/limit. The deep-dive section closes with the "scaffolding not quality itself" argument (line 75), the book's hardest limitation of this chapter's entire premise. This floor is thoroughly satisfied. |
| **SOURCE-TRACE** | Zero invented rule IDs, GAV coordinates, version numbers, benchmark figures. Everything traces to pinned source or is flagged to 09-flags/. | **PASS** | No raw version strings or GAV coordinates asserted without "verify-at-pin" caveat. All tool references route to chapter cross-references (Ch 3, 6, 9, 16, 17, 18, 25, 27, 28, 29, 30, 31, 33, 34, 35, 37, 38, 40) and dossier keys that trace to pinned tool dossiers from Parts IV–IX. The back-matter explicitly flags "EVERY version/GAV/CI-config ⚠ verify @pin." No benchmark figures, no quoted claims from unpinned sources. **Live risk (not a FAIL, but must resolve before ship):** the "JUnit (6)" notation is ambiguous — if "(6)" is a version, it is wrong; if it is a count, the notation must be made unambiguous. Recommended fix: change to "JUnit 5 (Jupiter) + AssertJ + Mockito + Testcontainers" to make the version explicit and correct. |
| **SOURCE-TRACE / COMPILE** | Companion module builds green via ./mvnw -B verify at the pins. | **PENDING** | EXAMPLE-BUILD = PARTIAL per the draft's own honest disclosure (lines 6, 116): storefront-checkout Stage 1-3 green (Checkstyle 0 + SpotBugs 0); full-stack capstone assembly pending. Per task brief, COMPILE is a separate track — treated as PENDING, not FAIL, for this scorecard. Chapter cannot move to human-approve until the full capstone build completes and is verified green. |
| **SOURCE-TRACE / CODE-REVIEW** | Module passes CODE-REVIEW gate. | **N/A** | Per task brief. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (≥35/50, no cluster below 6); all THREE floors PASS; ready for human approval.
- [x] **LIFT** — 35/50 exactly meets the numeric floor, but READABILITY at 6 is fragile (real prose issues), the JUnit notation must be resolved, and the missing figure (Fig 109.1) leaves the synthesis mechanism under-illustrated. Apply targeted fixes below, re-score.
- [ ] **CUT** — below bar or structural floor failure.

**One-line rationale:** The chapter meets the 35/50 bar exactly with one cluster at its floor (READABILITY 6) due to self-narration, a triple opening, and carve-out repetition; the JUnit (6) notation is an accuracy risk; the promised Fig 109.1 is absent; a single targeted prose pass would bring this to 38–39 and make the ship confident.

**Delta from self-score:** Self-score was 46/50. Independent score is 35/50 (delta -11). The self-score gave CLARITY 9, READABILITY 9, UTILITY 10 — all over-generous. CLARITY was penalized for the absent figure and triple-open; READABILITY for documented VOICE-GUIDE violations (self-narration, banned person); UTILITY for the partial build and absent snippets.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score **6**
- **Why it is the weakest:** Three compounding problems: (1) the chapter opens three times on the same orienting idea (pull-quote, Hook, Overview) before reaching content; (2) the narrator repeatedly describes the chapter's own argument ("This chapter is the one place," "the honest center and the reason this chapter cannot be," "this is why the very next chapter") — VOICE-GUIDE.md explicitly bans this narration pattern; (3) "carve-out" appears 10+ times, turning a useful framing into a verbal tic that deadens by the third section.
- **Single highest-leverage move to lift it:** Cut the triple-open to one clean hook paragraph (absorb the pull-quote into the Hook, delete the overlap in Overview), then run a self-narration sweep: anywhere the narrator announces what the prose is doing instead of doing it, cut the announcement and keep only the content. That single pass recovers READABILITY to 7–8 without touching any other cluster.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · para · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | Lines 18–38 (pull-quote + Hook + Overview) | Triple opening: three consecutive passages make the same orienting point ("45 chapters neutral, now one recommendation") before the reader sees any stack content. | Collapse the pull-quote (lines 18–19) into the Hook opening sentence, delete the overlap from the Overview block; the Overview should open with "What this chapter covers" bullet list only, preceded by one sentence max of context. Target: one opening statement, then content. |
| 2 | READABILITY | Lines 24, 69, 71, 75 (Hook, Deep Dive) | Self-narration: "This chapter is the one place in the book that recommends," "understanding how it stays honest while breaking the no-crowning rule is the key to using it well," "the honest center, and the reason this chapter cannot be the last word" — narrator announces the chapter's argument instead of making it. VOICE-GUIDE.md: "Do not narrate the chapter's own argument." | Replace each announcement with its content: instead of "This chapter is the one place that recommends — it hands you..." write "Here is one defensible, worked, end-to-end quality stack..." Drop the meta-framing; make the recommendation directly. |
| 3 | READABILITY | Lines 24, 33, 71, 79, 89, 93, 95, 113 | "carve-out" appears 10+ times in ~4,500 words, including "the carve-out's honesty," "the carve-out's whole point," "the carve-out's core honesty," "per the NEUTRALITY capstone carve-out." The concept is stated, then re-announced in every subsequent section. | State the carve-out concept once (Hook or first paragraph of Deep Dive). Every subsequent use: replace with the underlying idea it points to ("this recommendation" / "the one place the book recommends" / implicit — just make the recommendation and move on). |
| 4 | ACCURACY | Line 51 (stack entry) and line 27 of dossier | "JUnit (6) + AssertJ + Mockito + Testcontainers" — "(6)" reads as a version number; JUnit 6 is not released as of the pin date. | Change to "JUnit 5 (Jupiter) + AssertJ + Mockito + Testcontainers" or simply "JUnit + AssertJ + Mockito + Testcontainers" with a "verify at pin for current version" note. Resolve in the dossier as well. |
| 5 | CLARITY | No figure rendered in the draft; dossier spec at line 50 calls for "Fig 109.1 — the reference stack as a layered diagram mapped onto the gate stages." | The one chapter where a visual synthesis is mandatory (nine tools mapping to four gate stages) contains no figure. The prose list of tools does not substitute for the diagram. | Realize Fig 109.1 per the dossier spec before ship: the layered stack (build → format → style → bug → null → arch → test → security → platform) mapped onto the four gate stages (pre-commit → PR → nightly → merge), authored as HTML, rendered to PNG per the GUIDELINES rule. Reference it in the "How it works" section before the gate CONCEPT callout. |
| 6 | READABILITY | Line 24 | "It hands you a single, coherent, worked, end-to-end quality stack" — "hands you" is second-person adjacent in a locked third-person narrator voice. | Rewrite: "The chapter delivers one coherent, worked, end-to-end quality stack..." or begin with "One coherent, worked, end-to-end quality stack..." |
| 7 | UTILITY | Lines 65–67 (capstone module section) | The capstone module section describes what the reference project will show (pom.xml, gate config, ArchUnit rules) but shows none of it — no snippet. For a chapter whose utility claim is "clone it Monday," one short display (e.g., the aggregator pom.xml structure or the gate config skeleton) would ground the claim concretely. | When the full capstone build completes (EXAMPLE-BUILD PENDING → green), add one snippet (≤9 lines) from the realized project — e.g., the pinned-plugin block from the parent pom, as a tag-region. This is blocked on the build completing; note it as a post-build addition. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 35 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT | Initial independent score. Self-score was 46/50; delta -11. Weakest: READABILITY (6) — self-narration + triple-open + carve-out repetition. JUnit (6) ambiguity. Fig 109.1 absent. |

---

## Learnings & pipeline suggestions

1. **Self-narration is the capstone's primary tell.** A synthesis chapter that must explain its own uniqueness ("this is the one chapter that recommends") risks the narrator talking about the prose instead of writing it. The AUDIT gate should add "This chapter is the one" and "the reason this chapter" to its self-narration scan pattern.

2. **The triple-open anti-pattern.** A pull-quote followed by a Hook section followed by an Overview section, all covering the same ground, is a structural problem the CLARITY gate should catch explicitly. The CHAPTER-TEMPLATE should note: a pull-quote and a hook section are alternatives, not complements — pick one entry point.

3. **Version-notation ambiguity in parentheticals.** "JUnit (6)" looks like a version. Dossier shorthand ("[tool] (N items follow)" or "(N)" as a count) must be disambiguated before it enters a draft — the dossier template should require explicit "count:" or "version:" qualifier when a number appears beside a tool name.

4. **The absent figure is an accuracy risk, not just a clarity gap.** A chapter that promises a figure (dossier §6 explicitly specs Fig 109.1) but ships without it has a broken cross-reference in the dossier-to-draft handoff. The VERIFY gate should check: "Does every figure listed in the dossier appear in the draft, or is its absence explicitly noted as pending?"

5. **Self-scores at the 46/50 range should trigger independent review automatically.** The 11-point delta between self-score and independent score confirms the value of the independent scorer. The pipeline should flag any self-score ≥43/50 for mandatory independent review before human-approve.

6. **Capstone carve-out prose discipline.** The carve-out gives permission to recommend; it should not become a verbal crutch. Naming the carve-out repeatedly across every section is structurally identical to the banned "unlike X" pattern — it keeps orienting the reader to the meta-framework rather than the content. Propose adding to GUIDELINES: "The capstone carve-out is invoked once, not used as a recurring phrase throughout the chapter."

*Append to `00-strategy/PIPELINE-LEARNINGS.md`.*
