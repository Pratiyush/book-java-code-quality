# INDEPENDENT SCORECARD — Ch 46 — model: Claude Sonnet 4.6 — 2026-06-20 (lift pass 1)

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 109 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `109_reference_quality_stack_gate`
- **Title:** So What Do I Actually Set Up? (A reference quality stack & gate design)
- **Part / arc position:** Part XIV — Capstone & Synthesis, Chapter 46 (opener)
- **Artifact scored:** `03-drafts/109_reference_quality_stack_gate/109_reference_quality_stack_gate_v1.md`
- **Verified against:** SOURCE-PIN.md — pinned at 2026-06-20
- **Scorer:** chapter-scorer agent (Claude Sonnet 4.6 — INDEPENDENT, separate model from drafter)
- **Date:** 2026-06-20
- **Lift-pass #:** 1 (voice pass + two rendered figures Fig 109.1 and Fig 109.2 added)
- **Note:** Prior independent score (pass 0) was 35/50. This re-score follows a voice/figure lift pass.

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | **8** | **Lift from 7.** Fig 109.1 (the reference quality stack as a layered diagram, each row naming an alternative) and Fig 109.2 (the feedback-latency gate ladder, pre-commit → PR-fast → main/nightly → merge) are now present in the draft and correctly referenced in prose before they appear (lines 41–47). The visual synthesis that was missing at pass 0 is realized: the "nine tools mapped to four gate stages" claim is now carried by the figure, not only by a prose list. The per-pick "what it catches · cost · alternative · when-to-swap" structure in the text remains clean and the gate CONCEPT callout is well-ordered. The remaining clarity drag is the triple-open structure (pull-quote lines 18–19, Hook section lines 21–24, Overview lines 27–38) — all three make the same orienting point before the reader reaches any stack content — but this is a pacing issue, not a mechanism gap; the mechanism itself is now illustrated. Score 8: the mechanism is clear and the figures carry the load-bearing visual. |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set at the pins. No invented rule IDs, GAV coordinates, version numbers. | **8** | **Lift from 7.** The prior score's JUnit "(6)" ambiguity is resolved on two axes: (a) SOURCE-PIN.md §3 confirms JUnit 6 (6.1.0 GA 2026-05) IS the pinned version — not an invented or unreleased version; (b) the chapter prose at line 60 reads "JUnit + AssertJ + Mockito + Testcontainers" (no ambiguous parenthetical in the running text); (c) fig109_1.sources.md explicitly traces "JUnit 6 (6.1.0 GA 2026-05)" to SOURCE-PIN.md §3. The "(6)" that appears in the draft's HTML comment header (line 8) and back-matter (line 122) is dossier shorthand, not a version assertion in prose — it is not a FLOOR C violation. Both figure source-trace files (fig109_1.sources.md, fig109_2.sources.md) carry per-label pin-verification tables; every version number in the figures traces to a SOURCE-PIN.md row. No invented rule IDs, flags, GAV coordinates, or benchmark figures appear anywhere in the prose. All tool references route through chapter cross-references to pinned dossiers (Parts IV–IX). Score 8: fully traced with recorded source paths; no drift. |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | **8** | **Unchanged from pass 0.** The "When to use what" section is directly actionable; the four-stage gate breakdown is copyable; incremental adoption framing is the chapter's most practical sentence. The two load-bearing figures (Fig 109.1 — print-and-use stack checklist; Fig 109.2 — gate layout diagram) substantially increase utility as reference artifacts a developer would keep open. Held at 8 rather than 9 because: (a) the capstone build is still PARTIAL — the chapter's primary utility claim (clone it Monday) rests on a runnable project that is not yet fully assembled; (b) no runnable snippet from the actual companion module appears in the prose, so a reader cannot verify the composition in a working build context. These are build-state constraints, not prose issues, and are resolved only when COMPILE completes. |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | **7** | **Unchanged from pass 0.** The deep-dive on how the recommendation stays honest (lines 78–84) is the chapter's intellectual peak, and the structural-lessons synthesis (layering, de-duplication, feedback-latency ladder, new-code focus, incremental adoption) treats this correctly as a capstone of principles rather than a tool list. Depth is inherently scoped for a synthesis chapter — each tool has its own chapter; this one composes them. The "tools do not make quality" argument is brief but substantive. A reader who has read all 45 prior chapters finds the depth satisfying; a reader who has not would find it thin — but that is the correct scope, not a deficiency. Score 7 reflects appropriate synthesis depth. |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice? Hook in, forward hook out? | **7** | **Lift from 6.** The two figures break up the text structurally, providing visual rest in "How it works" and reducing the grey-text wall that existed at pass 0. The figure captions are clean and non-redundant. The hook question ("so what do I actually set up?") remains genuinely engaging and the forward hand-off to Ch 47 is clean. However, three VOICE-GUIDE.md violations identified at pass 0 persist unresolved: (1) **Triple opening**: the pull-quote (lines 18–19), Hook section (lines 21–24), and Overview (lines 27–38) all make the same orienting point — "forty-five chapters neutral, now one recommendation" — before the reader reaches any stack content; (2) **Self-narration**: "This is the one place in the book that recommends" (line 24), "This chapter is a deliberate exception to the book's central rule" (line 80) — the narrator announces the chapter's own argument rather than making it; VOICE-GUIDE.md explicitly bans this ("Do not narrate the chapter's own argument"); (3) **"Carve-out" repetition**: the term appears on lines 33, 88, 98 (twice), 102 — stated once clearly, then re-announced in every subsequent section rather than letting the underlying idea carry forward implicitly. Score 7: the figures lift this from 6, but the prose-level VOICE-GUIDE violations remain and cap the score. |

**Cluster subtotal: 38 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | No rival crowned superior. Banned phrasings absent. Every pick names alternative + trade-off. Capstone carve-out: recommends one setup, framed "one defensible setup," never "the best." | **PASS** | Full banned-phrase sweep of v1 draft: zero hits for "better than," "unlike X," "the problem with X," "superior," "beats," "outperforms," "kills," "destroys," "blows away," "the obvious choice over," "no reason to use X." Every stack entry uses the "what it catches · cost/limit · named alternative · when-to-swap" format. Alternatives are named for all nine tool layers. The chapter frames the recommendation as "one defensible setup, not the setup" in Hook (line 24), Overview (line 35), and Alternatives section (line 102). The "Alternatives & adjacent approaches" section is approach-based (not a leaderboard): each bullet contrasts approaches or trade-offs. The two occurrences of "best" (line 22: "universally best linter"; "best never appeared as a verdict") are explicit meta-statements about the book's neutrality policy — these are self-referential framings, not verdicts, and do not violate NEUTRALITY.md. Section titles carry no comparative superlatives. Capstone carve-out (NEUTRALITY.md line 20, key 109) correctly invoked: one defensible recommendation, every pick still named with alternative and trade-off, no crowning. Fig 109.1 and Fig 109.2 source-trace files confirm the figures apply the same neutrality discipline: each figure row names alternatives, uses no crowning language. FLOOR A: PASS. |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a "when NOT to reach for this." | **PASS** | Seven explicit when-NOT items in "Limitations & when NOT to reach for it" (lines 87–94): (1) this-is-A-stack-not-THE-stack; (2) adopt-incrementally; (3) stack-is-code-to-own; (4) tools-dont-make-quality; (5) versions-move; (6) all-OSS-not-effort-free; (7) green-capstone-necessary-not-sufficient. Every stack entry in "How it works" carries a named "Cost:" beat. The deep-dive section closes with the "scaffolding not quality itself" argument (lines 82–84) — the book's hardest limitation of this entire chapter's premise. FLOOR B: PASS. |
| **SOURCE-TRACE** | Zero invented rule IDs, GAV coordinates, version numbers, benchmark figures. Everything traces to pinned source or is flagged to 09-flags/. | **PASS** | No raw version strings or GAV coordinates asserted without "verify-at-pin" caveat in prose. The "(6)" appearing in the HTML comment header (line 8) and back-matter (line 122) is dossier-shorthand notation, not a version assertion in running prose — line 60 prose is clean ("JUnit + AssertJ + Mockito + Testcontainers"). SOURCE-PIN.md §3 confirms JUnit 6 (6.1.0 GA 2026-05) is the pinned version, resolving the prior ambiguity. Both figure source-trace files carry per-label pin tables — every version string (Maven 3.9.16, Gradle 9.6.0, Spotless 8.7.0, google-java-format 1.35.0, Checkstyle 13.6.0, PMD 7.25.0, SpotBugs 4.10.2, NullAway 0.13.4, JSpecify 1.0.0, Checker Framework 4.2.0, ArchUnit 1.4.2, JUnit 6.1.0, AssertJ 3.27.7, Mockito 5.23.0, Testcontainers 2.0.5, JaCoCo 0.8.16, PITest 1.25.3, OWASP Dependency-Check 12.2.2, CycloneDX 1.6, Semgrep 1.163.0, SonarQube 2026.1 LTA) traces to a SOURCE-PIN.md row. No benchmark figures, no quoted claims from unpinned sources, no invented rule IDs or config keys. FLOOR C (source-trace): PASS. |
| **SOURCE-TRACE / COMPILE** | Companion module builds green via ./mvnw -B verify at the pins. | **PENDING** | EXAMPLE-BUILD = PARTIAL per the draft's own honest disclosure (lines 6 and 125 of the chapter header and back-matter): storefront-checkout Stage 1-3 green (Checkstyle 0 + SpotBugs 0); full-stack capstone assembly pending. Per task brief, COMPILE = PENDING (do not fail). Chapter cannot move to human-approve until the full capstone build completes and is verified green on the pinned JDKs (Java 21 anchor, matrix 25). |
| **SOURCE-TRACE / CODE-REVIEW** | Module passes CODE-REVIEW gate. | **N/A** | Per task brief. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **APPROVE** — clears the bar (38/50; all clusters ≥ 7); all floors PASS (A, B, C-source-trace); COMPILE PENDING per task brief (not a fail condition for this scorecard). Ready for human approval once the capstone build completes and the COMPILE gate is confirmed green.
- [ ] **LIFT** — bar missed on cluster quality.
- [ ] **CUT** — below bar or structural floor failure.

**One-line rationale:** 38/50 with no cluster below 7; all three content floors PASS; the two load-bearing figures (Fig 109.1, Fig 109.2) are present, referenced, and pin-verified; the JUnit notation ambiguity is resolved (JUnit 6 is the pinned version); the remaining cap on READABILITY and DEPTH is accurate scope, not a deficiency, and does not threaten the bar. The single remaining hard stop is COMPILE (capstone build PENDING) which must complete before human-approve.

**Delta from pass 0:** +3 points (35 → 38). CLARITY +1 (figures present), ACCURACY +1 (JUnit pin confirmed, notation clean), READABILITY +1 (figures break grey-text wall). UTILITY and DEPTH unchanged; UTILITY already at 8 and held there; DEPTH correctly scoped at 7.

**Delta from self-score:** Self-score was 46/50. Independent score at lift pass 1 is 38/50 (delta -8). The self-score gave CLARITY 9, READABILITY 9, UTILITY 10 — all remain over-generous. READABILITY at 9 requires "effortless to read at full precision" with the locked voice held; the triple-open and self-narration violations that remain in this draft cannot reach 9.

---

## Remaining blockers to 90% (45/50)

The chapter is at 76% (38/50) and APPROVED at the current bar (≥35/50, no cluster below 6). To reach 90% (45/50) a second lift pass would need to target:

| # | Cluster | Blocker | Label |
|---|---|---|---|
| 1 | READABILITY (7 → 9) | Triple-open: collapse pull-quote + Hook + Overview to a single entry point. Cut the pull-quote (lines 18–19) into the Hook opening sentence; make Overview a tight bullet list only with no re-orienting preamble. | prose-fixable |
| 2 | READABILITY (7 → 9) | Self-narration: "This is the one place in the book that recommends" (line 24); "This chapter is a deliberate exception to the book's central rule" (line 80). Replace each announcement with the content it announces — drop "This chapter is X" and start with the recommendation directly. | prose-fixable |
| 3 | READABILITY (7 → 9) | "Carve-out" repetition: appears lines 33, 88, 98 (×2), 102. State the concept once in the hook paragraph, then drop it. Each subsequent use: replace with the underlying claim ("this recommendation" / "the one setup the book names" / implicit). | prose-fixable |
| 4 | UTILITY (8 → 9) | No snippet from the actual companion module appears in the prose. Once COMPILE completes, add one ≤9-line snippet (e.g., the pinned-plugin block from the parent pom as a tag-region) to ground the "clone it Monday" claim concretely. | needs-build-first |
| 5 | DEPTH (7 → 8) | The "tools do not make quality" argument (lines 82–84) is the chapter's deepest claim but receives only three sentences in the deep-dive and four bullets in Limitations. A half-paragraph expansion — citing the specific chapter (Ch 37 key 84) and naming the gap between a green gate and a quality codebase with one concrete example — would carry more weight without scope creep. | prose-fixable |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 35 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT | Initial independent score. Self-score was 46/50; delta -11. Weakest: READABILITY (6) — self-narration + triple-open + carve-out repetition. JUnit (6) ambiguity. Fig 109.1 absent. Fig 109.2 absent. |
| 1 | 2026-06-20 | 38 / 50 | PASS | PASS | PASS / PENDING / N/A | APPROVE | Voice pass + two figures rendered. Fig 109.1 (layered stack) and Fig 109.2 (gate ladder) added to draft with prose references. JUnit pin confirmed as JUnit 6.1.0 (SOURCE-PIN.md §3); notation clean in prose. CLARITY +1, ACCURACY +1, READABILITY +1. Triple-open, self-narration, and carve-out repetition persist — hold READABILITY at 7. No snippet yet (COMPILE pending). |

---

## Learnings & pipeline suggestions

1. **Figure lift is a real score lever.** Adding two well-sourced, pin-verified figures moved CLARITY from 7 to 8 and READABILITY from 6 to 7 — an aggregate gain of +2 from visuals alone. The FIGURE gate should run before the final score, not after: figures that are required (dossier §6 specifies them) should be realized as part of the draft pass, not as a separate lift.

2. **Pin ambiguity resolved the right way.** The JUnit "(6)" ambiguity that scored ACCURACY at 7 in pass 0 was resolved by reading SOURCE-PIN.md directly — JUnit 6 (6.1.0 GA 2026-05) is the pinned version. Independent scorers must read SOURCE-PIN.md before flagging parenthetical numerals as version errors; the notation is ambiguous but the pin is definitive.

3. **Self-narration is the persistent tell.** After a voice lift pass, the triple-open and self-narration ("This chapter is the one place," "This chapter is a deliberate exception") remain in the draft unchanged. These phrases are the clearest signal of AI-produced prose — a human author who knows the material states the recommendation directly and does not announce that the chapter is making a recommendation. The AUDIT gate should carry these exact patterns as scripted search targets.

4. **"Carve-out" as a verbal tic.** The word appears five times across the chapter; a human technical writer would use it once and then let the implication carry. This is a frequency threshold the AUDIT gate could enforce: any content-governing term appearing more than twice in a chapter shorter than 6,000 words should trigger a flag.

5. **Capstone APPROVE threshold with COMPILE PENDING is correct practice.** Approving a chapter at the prose/cluster level while holding the COMPILE gate as a separate hard stop is the right pipeline discipline — it does not lower the bar; it correctly separates prose quality from build state. The COMPILE gate remains fatal; it is simply on a separate track.

6. **The 90% blockers are all prose-fixable (except the snippet).** Three of the five remaining blockers (triple-open, self-narration, carve-out repetition) require no new facts, no scope changes, and no figure work. They are edits to existing prose. A second targeted pass could realistically add +4 to +5 points, reaching 42–43/50.

*Append to `00-strategy/PIPELINE-LEARNINGS.md`.*
