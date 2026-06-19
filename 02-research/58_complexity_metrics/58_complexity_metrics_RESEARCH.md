# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Builds on the cyclomatic-vs-cognitive distinction banked in
> key 03 and the measurement discipline in key 04. This chapter owns *complexity as a controlled, gated
> property*. Thresholds `⚠ verify at pin`.

---

## Topic
- **Key:** 58 — `01-index/CANDIDATE_POOL.md` · **Title:** Complexity metrics — cyclomatic & cognitive complexity, measuring & controlling
- **Part:** VI · **Tier:** B
- **Primary authorities:** McCabe (cyclomatic, 1976); Campbell/SonarSource (Cognitive Complexity, key 03); NPath (Nejmeh); tools — SonarQube `java:S3776`, Checkstyle (`CyclomaticComplexity`, `NPathComplexity`, `JavaNCSS`), PMD.

## 1. Core definition & purpose
Complexity is the most directly *gate-able* readability/maintainability proxy: unlike "good design," a method's complexity is a number you can threshold in CI. This chapter consolidates the metrics (defined in keys 03/04), then focuses on the senior question — **how to control complexity without gaming the metric**: thresholds, where to set them, refactoring triggers, and the limits of the number.

## 2. Mechanism (the spine)
- **Cyclomatic** (McCabe): independent execution paths ≈ minimum tests; great for testability, weak for understandability (key 03).
- **Cognitive Complexity** (Campbell/SonarSource): understandability, penalizes nesting; SonarQube `java:S3776` (common default threshold **15** — `⚠ verify at pin`).
- **NPath**: acyclic execution paths (combinatorial); Checkstyle `NPathComplexity`.
- **Aggregate**: weighted methods per class (WMC, key 04); file/class size (`JavaNCSS`, `FileLength`).
- **Controlling it:** set per-method thresholds as a *fitness function* (key 56) in Checkstyle/PMD/Sonar; treat a breach as a *refactor trigger* (extract method, replace nested conditionals with guard clauses / polymorphism / pattern matching — key 13), not a number to suppress. Ratchet on new code (key 80) rather than boiling the ocean on legacy.

## 3. Evidence FOR
- **Objective, gate-able, tool-supported** across Checkstyle/PMD/Sonar; the most actionable of the structural metrics.
- **Cognitive complexity is empirically validated** for understandability (key 03; arXiv 2007.12520).
- Correlates with defect-proneness in the literature (key 04).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Gameable & proxy** (key 04/Goodhart) — splitting a method to dodge a threshold can *raise* coupling; low complexity with bad names is still unreadable.
- **Thresholds are conventions, not laws** — 15 is a starting point, not a truth; the right value is team/context-dependent.
- **Whole-repo absolute complexity mixes legacy with new** — gate *new* code (key 80); a legacy hotspot list is more useful than one global number.
- **Cyclomatic ≠ cognitive** — gating the wrong one (cyclomatic for "readability") misleads (key 03).

## 5. Current status
McCabe/NPath long-established; Cognitive Complexity the current understandability direction; all tool-supported. Thresholds verify-at-pin per tool.

## 6. Worked example / figure spec
- **Companion add-on:** a deeply-nested method with its cyclomatic vs cognitive scores, then refactored below threshold; Checkstyle/Sonar rule config shown. Reuses key 03/27/35 material.
- **Figure:** Fig 58.1 — same method, complexity before/after refactor (both metrics) + the refactor applied. Trace to SonarSource/McCabe.

## 7. Gap-filling (verification queue)
- ⚠ `java:S3776` default threshold, Checkstyle complexity defaults, NPath formula — verify at pin.
- ⚠ McCabe formula (E−N+2P) — confirm if stated.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | McCabe (1976); Campbell/SonarSource Cognitive Complexity | IEEE; sonarsource.com (key 03) | ☑ concept; ⚠ thresholds |
| 2 | Checkstyle/PMD/Sonar complexity rules | respective docs | ☑ rule IDs; ⚠ defaults |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis of keys 03/04) | cyclomatic vs cognitive; java:S3776; NPath; gate-on-new-code |

---
## Learnings & pipeline suggestions
- Reuse the **metric-card** shape. Keep cyclomatic-vs-cognitive *definition* in key 03; *control/gating* here. **Cross-ref:** 03, 04, 27, 35, 56, 80.
