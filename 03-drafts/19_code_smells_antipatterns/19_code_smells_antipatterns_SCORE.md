# SCORECARD — Ch 12 "Code smells, design patterns & anti-patterns" (key 19 + 61)

> Part II CLOSER (Part II = Ch 5-12), two merged dossiers (smell catalogue + design/anti-patterns ⚠ contested).
> Main-loop; gates = manual passes. Smell-card + canon-dating + two-schools + item-to-rule shapes. Draft:
> `19_code_smells_antipatterns_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); patterns-as-vocabulary vs simplicity-first given as two-schools, resolved to a shared rule (pattern-for-a-problem-it-lacks = anti-pattern), neither crowned; analyzers framed as pipeline-point fits, none crowned (→ Ch 17); anemic-vs-rich + inheritance-vs-composition left as trade-offs; canon-dating keeps "principle stands, idiom dated" (GoF not dismissed). |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (smell=hint not verdict; thresholds disagree/vanity-trap; undetectable smells review-only; refactor needs test net; over-fix re-smells; patternitis; labels weaponizable; contested designs; tools disagree by design) + §When to use + the deep-dive same-catalogue-opposite-verdicts beat. |
| C — SOURCE-TRACE | ✅ PASS | smell names→Fowler 2e; refactorings→Fowler catalogue; idiom anti-patterns→EJ items; patterns→GoF; anti-pattern term→Brown et al.; simplicity school→Ousterhout; rule keys each to own tool; thresholds/EJ items/JEP numbers/Fowler-2e-list/Sonar-RSPEC carried verify-at-pin. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (smelly vs refactored OrderService; EI_EXPOSE_REP smell-that-bites test; OpenRewrite recipe) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | the "names for what's wrong" hook + the smell-card CONCEPT + the grouped catalogue table + canon-dating table make a breadth chapter navigable; the deep-dive two-schools resolution is crisp. |
| ACCURACY | 8 | Fowler/EJ/GoF/Ousterhout traced; rule keys cited per tool; −2 for thresholds (move per version), EJ item numbers, Fowler-2e complete list, GoF/AntiPatterns verbatim, JEP numbers carried verify-at-pin. |
| UTILITY | 8 | gives a reviewer the smell→refactor→rule triple, the tool-found-vs-review-found split, the GoF-served-by-modern-Java map, and the patternitis judgment rule; directly usable in review + CI. |
| DEPTH | 8 | merges the code-level smell catalogue (19) with the design-level contested patterns debate (61) into one "vocabulary for what's wrong" arc; honest that the hardest smells are undetectable; the smell-that-bites-at-runtime vs clearer-whole contrast is senior-level. |
| READABILITY | 8 | three-smell hook, two catalogue tables + canon-dating table, smell-card CONCEPT, the contested deep dive carries the neutrality load; reads Part II back as vocabulary; no grey wall. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. **CLOSES PART II
(Ch 5-12).** Reuses smell-card (new) + canon-dating (Ch 5) + two-schools (Ch 2/6/9) + item-to-rule (Ch 7/8/10/11)
shapes. Hands off to Part III (Ch 13, concurrency). Detection-tool depth → Ch 16/17; automated apply → Ch 39.
