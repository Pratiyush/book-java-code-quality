# INDEPENDENT SCORECARD — Ch 43 — model: Claude Sonnet 4.6 — 2026-06-20

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 101 (folds 102, 103, 104/51)
- **Slug:** `101_performance_profiling_memory_benchmarking`
- **Title:** Measure, Don't Guess — Performance as a quality attribute, profiling, allocation hygiene, and honest benchmarking with JMH
- **Part / arc position:** Part XIII — Performance & Observability (opener / umbrella, Ch 43)
- **Artifact scored:** `03-drafts/101_performance_profiling_memory_benchmarking/101_performance_profiling_memory_benchmarking_v1.md`
- **Source basis:** `02-research/101_performance_quality_attribute/101_performance_quality_attribute_RESEARCH.md` (the ONLY allowed source basis per scoring mandate)
- **Verified against SOURCE-PIN.md pin** (re-check date: 2026-06-20)
- **Scorer:** chapter-scorer agent — Claude Sonnet 4.6 (independent; different model from drafter)
- **Date:** 2026-06-20
- **Lift-pass #:** 0

---

## The five clusters (score 1–10)

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | **7** | The four-movement structure (requirement → profile → allocation → benchmark) is clean and the "instrument that lies" metaphor unifies the chapter well. CONCEPT callouts carry the mechanism correctly. "Why before how" gate is satisfied throughout. Weakness: the dossier (§6) specifies this as a "figure-led" concept chapter with "Fig 101.1 — the performance discipline loop" explicitly planned; no figure is present in the draft, leaving the loop abstract where a visual would have cemented it. The deep-dive section reads as a second introduction rather than a genuine escalation — the ground covered there largely recaps the concept sections. |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set. No invented rule IDs, flags, API signatures, version numbers, benchmark figures, quoted claims. | **8** | Every specific claim carries appropriate pin-hedging consistent with the dossier's own ⚠ markers. "~1% overhead (verify at the pin)" correctly mirrors the dossier's "~1% overhead ⚠ verify-at-pin." JEP 509 is marked "experimental" matching dossier. GC defaults (G1/ZGC/Parallel) hedged "verify defaults at the pin." @Setup(Level.Invocation) "almost always wrong" matches dossier verbatim. JMH version "1.37" (⚠verify-at-pin in dossier) is safely omitted rather than stated. Knuth premature-optimization is paraphrased, not falsely quoted verbatim — correct given the ⚠ in the dossier. No invented rule IDs, GAV coordinates, API signatures, or benchmark figures detected. Valhalla/value types correctly marked AHEAD-OF-PIN. Small residual risk: JMH sample numbers (08/09/10/38) cited in dossier do not appear in the draft — the dossier lists them as ⚠verify-at-pin items, so omission is safe. |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | **7** | "When to use what" section is concrete and decision-framed (when to profile, which profiler, when to reduce allocation, how to write honest JMH). Limitations section is specific and actionable. The chapter's most significant utility gap: EXAMPLE-BUILD is PENDING and the draft contains no code blocks at all — for a chapter specifically about JMH benchmarking (where the wrong-vs-right juxtaposition is the teaching instrument), the complete absence of even an illustrative snippet leaves the "naive benchmark lies" section without its sharpest tool. A reader wanting to apply the JMH guidance cannot see the @Benchmark / Blackhole / @State pattern. Guidance is sound but under-demonstrated. |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | **8** | Four research keys folded into one coherent umbrella with genuine depth on each. Benchmarking section covers DCE, constant folding, @State non-final, Blackhole.consume, forks, warmup, @Setup(Level.Invocation) pitfall (the "almost always wrong" warning), the IDE final-quickfix trap that re-enables constant folding — these are second-tier JMH pitfalls that signal real depth. Allocation section distinguishes churn from correctness leaks, covers escape analysis (a genuine subtlety), and names ThreadLocal risk with virtual threads. The epistemological framing in the deep-dive ("evidence requiring a defended methodology, never ground truth") is substantive. Connections to other chapters (Ch 2 readability trade-off, Ch 8 try-with-resources, Ch 38 vanity metrics) are meaningfully placed, not decorative. |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice per VOICE-GUIDE? Hook in, forward hook out? | **7** | Voice is mostly well-held: third person throughout, no narration-level contractions, active voice dominant, no filler phrases detected. Hook is strong (concrete scene — developer wastes a week on guessed hotspot, phantom 10x speedup from DCE). Forward hand-off is clean and specific. Two weaknesses flagged: (a) Self-narration leaks in the deep-dive — "naming the thing that makes it hard is what turns 'use a profiler and JMH' into a worldview"; "The honest center, which governs the whole part" — VOICE-GUIDE explicitly bans "narrating the chapter's own argument." (b) The "Hand-off to the next chapter" and "Next chapter teaser" sections are nearly identical in content (~120 words of redundant coverage of the same next-chapter preview), weakening the close. Em-dash density is moderately elevated (approximately 10-12 per 1,000 words in the deep-dive, above the ~8 target) but not flagrant. |

**Cluster subtotal: 37 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | Neutral comparative survey; no winner crowned; banned phrasings absent; cross-subject claims cited. | **PASS** | Blocklist scan: no "better than", "unlike X", "the problem with X", "superior", "beats", or any banned phrase found. Tool niches are assigned contextually (JFR for production/built-in, async-profiler for flame graphs, commercial for richer UIs) and the chapter explicitly states "the book crowns none." The JFR "default first reach" framing ("default first reach precisely because it's always available and cheap enough to leave on") is a contextual scope statement — the rationale is the presence on any JVM and sub-1% overhead — not a winner-crowning verdict, and async-profiler and commercial tools each receive their strongest case and a fair scope. Sampling vs instrumentation: both get honest trade-offs, no winner. JMH vs hand-rolled timer: framed as precision-to-need match, not JMH-wins. Object pooling: the "usually counterproductive" characterization is an honest-limitations claim about the technique itself, not a comparative denigration. No heading carries a winner-crowning superlative. Alternatives section is approach-based. |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a "when NOT to reach for this". | **PASS** | Dedicated "Limitations & when NOT to reach for it" section with 10 named limits, all specific and hard. In-line limits throughout: profiling shows WHERE not WHY/the-fix (§ profiling); sampling is statistical (§ profiling); instrumentation distorts (§ profiling); DCE/constant-folding/no-warmup (§ benchmarking); micro-not-macro called the "loudest caveat" (§ benchmarking); escape analysis undercuts naive allocation advice (§ allocation); object pooling usually counterproductive (§ allocation); GC tuning workload-specific (§ allocation); targets must be real, not arbitrary (§ requirement). Premature optimization anti-pattern is the governing constraint, not a footnote. No feature is presented as cost-free. |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented detail tracing to pinned source. COMPILE = PENDING (separate track — do not fail). CODE-REVIEW = N/A. | **PASS (source-trace) / PENDING (compile) / N/A (code-review)** | Source-trace: all specific technical claims (JFR overhead, JEP 509, GC defaults, JMH pitfalls, Valhalla status) are either hedged with pin-verification markers or match the dossier's own verified/⚠-marked content. No invented rule IDs, API signatures, GAV coordinates, version numbers, or benchmark figures found. Knuth paraphrase is safe (no false verbatim claim). JMH 1.37 omitted safely (⚠verify-at-pin in dossier). Valhalla/value types correctly held as AHEAD-OF-PIN with no assertion. COMPILE: PENDING — companion module spec is documented, toolchain ready, REPRO PENDING-RUNTIME per draft front-matter. This track is scored separately per scoring mandate and does not affect the source-trace verdict. CODE-REVIEW: N/A per scoring mandate. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **APPROVE** — Clears the ship bar (37/50, no cluster below 6); all floors PASS (FLOOR A NEUTRALITY PASS; FLOOR B HONEST-LIMITATIONS PASS; FLOOR C SOURCE-TRACE PASS, COMPILE PENDING on separate track, CODE-REVIEW N/A); ready for the human approval gate.

**One-line rationale:** Aggregate 37/50 with all floors passing; the chapter delivers genuine depth on a four-key umbrella, maintains disciplined neutrality, and provides comprehensive honest-limitations coverage — the weakest clusters (CLARITY at 7, UTILITY at 7) are held above the 6-floor by adequate mechanism coverage and a concrete decision section, though both would benefit from the planned figure and from even one illustrative code block.

---

## Flagged weakest cluster

- **Weakest clusters (tied):** CLARITY — score 7; UTILITY — score 7
- **Why CLARITY is flagged:** The planned Part XIII umbrella figure (Fig 101.1 — the performance discipline loop) is absent from the draft, leaving the four-movement loop abstract at the chapter's conceptual core. The deep-dive section re-narrates rather than deepens. Self-narration ("naming the thing that makes it hard is what turns…") violates the VOICE-GUIDE "do not narrate the chapter's own argument" rule.
- **Why UTILITY is flagged:** The complete absence of code blocks in a chapter about JMH benchmarking leaves the core actionable teaching — wrong benchmark vs right benchmark — without demonstration. The companion module spec is substantive but PENDING; even a minimal illustrative snippet (the @Benchmark / Blackhole / @State pattern) would raise utility materially.
- **Single highest-leverage move (CLARITY):** Add the Fig 101.1 performance discipline loop (set target → profile → benchmark the fix → gate against regression → "most code: don't optimize") as a designed diagram, even as a placeholder reference; and excise the self-narration sentences from the deep-dive.
- **Single highest-leverage move (UTILITY):** Insert one illustrative JMH "wrong vs right" snippet block (≤9 lines each, per VOICE-GUIDE snippet rules) that shows the DCE failure mode alongside the defended form, bridging the prose explanation to something a reader can copy.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | CLARITY | "Deep dive" section · ¶ 1, sentence 3 | "naming the thing that makes it hard is what turns 'use a profiler and JMH' into a worldview" — self-narration announcing the importance of the point rather than stating the point | Rewrite: state the point directly; remove the meta-frame |
| 2 | CLARITY | "Deep dive" section · ¶ 2, sentence 1 | "This is the same shape as the book's deepest recurring theme, now in its sharpest form" — narrating the chapter's own place in the book's arc | Remove or fold into a concrete observation; do not announce structural significance |
| 3 | CLARITY | Chapter has no figure despite dossier §6 specifying "figure-led; Fig 101.1 — the performance discipline loop" | Missing figure leaves the core mental model abstract | Add Fig 101.1 (HTML→PNG designed diagram): set-target → profile → benchmark-fix → gate → "most code: leave readable"; reference it from the "Performance as a measured requirement" section |
| 4 | UTILITY | "Honest benchmarking: JMH and the lies it defeats" section · the CONCEPT callout | The DCE / Blackhole / @State explanation is fully prose — no snippet | Add one illustrative ≤9-line snippet: a @Benchmark method discarding its result (wrong) alongside one returning/Blackholing from a non-final @State field (right); annotated inline |
| 5 | READABILITY | "Hand-off to the next chapter" section (¶1–2) AND "Next chapter teaser" section | Nearly identical content appears twice at the chapter's close, diluting both | Remove one; keep the more forward-pulling of the two ("Where this chapter found and fixed performance, the next keeps it from regressing") |
| 6 | READABILITY | "Deep dive" section · ¶ 3, sentence 4 | "The honest center, which governs the whole part, is that…" — announces a load-bearing point rather than stating it | Remove the announcement; begin with the substance: "The most important performance decision is usually not to optimize at all." |
| 7 | READABILITY | "Memory and allocation hygiene" section · last paragraph | "blind flag-tuning usually hurts, and the right collector choice with sensible defaults is usually the right call, where blind flag-tuning hurts — measure" — "blind flag-tuning hurts" appears twice in the same sentence | Edit to remove the repetition; one statement of the point is sufficient |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | APPROVE | initial independent score |

---

## Learnings & pipeline suggestions

1. **Figure-plan gaps reach scoring.** The dossier explicitly specified a "figure-led" chapter with Fig 101.1 specified in §6. When a chapter's own front-matter and dossier call for a planned figure and the figure is absent at scoring, the CLARITY cluster takes a meaningful hit. The figure-designer step should be tracked in CHAPTER-TRACKER before scoring, not after. Pipeline suggestion: add a "figure plan complete Y/N" check to the pre-scoring gate; flag any chapter where the dossier specifies a figure and the draft contains none.

2. **Redundant close sections.** Two nearly-identical hand-off sections ("Hand-off to the next chapter" and "Next chapter teaser") were found. The CHAPTER-TEMPLATE.md should state that exactly one forward-hand-off section is produced — the template appears to have generated both from slightly different template slots. This is a template discipline issue.

3. **JMH-specific chapters need at least one snippet.** A chapter whose mechanism is "JMH defeats naive benchmarks" and whose dossier specifies a worked wrong-vs-right example gains concrete utility from even a minimal, non-runnable illustrative snippet (the full companion module remains PENDING). The GUIDELINES "one runnable module per chapter" policy does not preclude illustrative inline snippets that are clearly labeled as illustrative pending the full build; in fact VOICE-GUIDE §snippet rules allow the display of minimal examples. For technique-heavy chapters where the EXAMPLE-BUILD is PENDING, the drafter should consider whether an illustrative (non-companion) snippet adds utility without floor risk.

4. **Self-narration in deep-dive sections.** The deep-dive section accumulated several VOICE-GUIDE violations of the "do not narrate the chapter's own argument" rule — a pattern seen in other chapters' deep-dives as well. The AUDIT gate's banned-term scan should be extended to catch the specific phrases: "is what turns X into Y", "now in its sharpest form", "which governs the whole [section/part]", "the load-bearing point is", "the honest center". These are the most common self-narration constructions in this book's drafts.

5. **APPROVE threshold well-supported at 37/50.** A four-key umbrella chapter covering profiling, allocation, and benchmarking at genuine depth with all floors passing and no cluster below 6 is a sound chapter. The 37/50 score reflects real but fixable weaknesses (absent figure, absent snippet, redundant close) rather than structural problems. The lift-list items above are all in-bounds revisions.
