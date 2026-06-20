# SCORECARD — Ch 8 "Immutability, records & value semantics" (key 10 + 15)

> Part II craft chapter, two merged dossiers (immutability discipline + the four JDK contracts).
> Main-loop; gates = manual passes. Three-instruments-and-the-gap + contract-card shapes. Draft:
> `10_immutability_value_design_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); analyzers framed as checkers of the same contracts not rivals (layering → Ch 17); Guava/EP/JDK-factories/hand-written framed as layering choices, none crowned; the equals+inheritance tension explicitly left unresolved (no rule crowned). |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (records shallow; unmodifiable=view; object-churn cost; records constrained; equals+inheritance unsolved; compareTo-consistency recommended-not-required; static-check FP/FN; Objects.hash allocates; when NOT to be immutable) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | equals/hashCode/compareTo contracts verbatim from JDK 21 Javadoc; JEP 395/390 traced; rule IDs each to own tool; copyOf version, tool defaults, EJ verbatims, BigDecimal figures, JEP numbers carried verify-at-pin; JEP 401 flagged AHEAD-OF-PIN/preview. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (leaky→sealed Order; BrokenPrice HashMap-loses-key; strict-profile gate) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | three-instruments-and-the-gap table + the "using the feature ≠ getting the guarantee" thesis + the HashMap-loses-key walkthrough make a subtle topic concrete. |
| ACCURACY | 8 | contracts verbatim @JDK 21; JEPs + rule IDs traced; −2 for copyOf version, tool defaults/IDs, EJ verbatims, BigDecimal specifics, JEP numbers carried verify-at-pin. |
| UTILITY | 8 | gives a senior reader the five rules, the defensive-copy seam, the violation→rule map, and per-surface When-to-use; directly actionable for value-type design. |
| DEPTH | 8 | merges immutability discipline with the four contracts into one "describe the value once, derive the behavior" arc; honest on the unsolved equals+inheritance tension. |
| READABILITY | 8 | runnable hook (the growing Order), table-led violation map, the HashMap deep-dive carries the mechanism; sparing CONCEPT/AHEAD-OF-PIN callouts; no grey wall. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. Reuses the
**three-instruments-and-the-gap** shape (candidate for Ch 9 null-safety, Ch on try-with-resources) and the
**contract-card** shape (Ch 7). Hands off to Ch 9 (null-safety) and Ch 14 (thread-safety payoff).
