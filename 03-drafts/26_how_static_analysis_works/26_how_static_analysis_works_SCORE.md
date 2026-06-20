# SCORECARD — Ch 15 "How static analysis works" (key 26)

> Part IV OPENER (frames the part), single-key foundational/technique chapter. Main-loop; gates = manual
> passes. technique-ladder + soundness-quadrant + illustrate-here-verdict-there shapes. Draft:
> `26_how_static_analysis_works_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); every tool (PMD/Error Prone/SpotBugs/CodeQL/Semgrep/Checker FW) used ONLY to illustrate a technique, cited to its own doc; "which tool to choose" verdict explicitly routed to Ch 17; the soundness/completeness framing presents the trade-off as a chosen point, no tool's choice crowned. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (AST shape-not-behavior; intraprocedural method-boundary; global-flow precision/time/aliasing; sound-checker tax; FP problem structural/undecidable; noisy-gate trust erosion; more-tools≠more-quality) + §When to use + the whole Deep dive IS the limitation (undecidability). |
| C — SOURCE-TRACE | ✅ PASS | each technique verbatim from its tool's docs (PMD AST/DFA, Error Prone javac, SpotBugs bytecode, CodeQL data-flow+taint, Semgrep IL/no-soundness, Checker FW soundness-choice); FP controls cited; undecidability flagged ⚠ UNVERIFIED (needs primary text @draft); FindBugs→SpotBugs carried; verbatim quotes + tool versions verify-at-pin. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (one class, four planted defects one-per-technique + justified suppression) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the wrong-in-both-directions hook + the four-move ladder (each rung with cost/blind-spot) + the soundness/completeness CONCEPT make an abstract framing topic concrete; the ladder table is the whole part's map. |
| ACCURACY | 8 | techniques verbatim from each tool's docs; FP/FN/sound/complete defined precisely; −2 for undecidability primary-text (⚠ UNVERIFIED until cited), verbatim quotes + tool versions/API paths + SonarQube resolution labels carried verify-at-pin. |
| UTILITY | 8 | gives a reader the mental model to read ANY tool's findings (which technique produced it, what its blind spot is) + the FP-handling discipline (justified suppression, baselines); calibrates trust — the framing the rest of Part IV needs. |
| DEPTH | 8 | derives the whole part from two ideas (the ladder + undecidability); the intraprocedural-vs-interprocedural cost axis and the soundness-is-a-chosen-point framing (Checker FW verbatim) are genuine, not a tool tour. |
| READABILITY | 8 | concrete hook, the ladder table + the four-quadrant CONCEPT, tools-as-illustrations woven in, no grey wall; appropriately a framing-chapter length (3703w). |

**Aggregate 41/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. **OPENS PART IV.**
New reusable shapes: technique-ladder + soundness-quadrant (each later per-tool chapter = one+ rung) +
illustrate-here-route-verdict-there (framing-chapter neutrality). Hands off to Ch 16 (the four bug-finders);
verdict → Ch 17; FP-policy → Ch 19; custom rules → Ch 18.
