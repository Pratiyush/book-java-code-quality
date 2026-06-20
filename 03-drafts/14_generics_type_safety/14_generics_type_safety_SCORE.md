# SCORECARD — Ch 11 "Generics & type-safety" (key 14)

> Part II (Ch 5-12), single-key chapter. Main-loop; gates = manual passes. PECS-variance-ladder +
> erasure-timeline + earned-assertion (@SafeVarargs) + item-to-rule shapes. Draft:
> `14_generics_type_safety_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); the analyzers (Checkstyle/PMD/Error Prone/Checker FW/Sonar) framed as points on a cost/coverage curve, none crowned (layering → Ch 17); Sonar S1452 stated as contested default not law; raw-types-legal-in-2-places guards against over-claiming a lint. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (erasure permanent; wildcard readability cost + no-wildcard-returns; don't over-genericize / TypeParameterUnusedInFormals; @SafeVarargs must be earned; raw types legal in 2 places; analyzers disagree by design; legacy boundary conversions) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | erasure/reifiable/raw verbatim from JLS SE 21 §4.6/4.7/4.8; EJ Items 26-33 cited; arrays-vs-generics + heap pollution + @SafeVarargs traced; tool rule IDs each to own tool; JLS §§/EJ verbatims/JEP numbers/tool defaults carried verify-at-pin; Valhalla reified generics flagged AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (type-safe Stack<E> PECS pushAll/popAll; justified @SuppressWarnings; unsafe-vs-safe varargs; -Xlint clean) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | the ClassCastException-far-from-cause hook + erasure-as-root-fact framing + PECS table + the earned-assertion deep dive make a notoriously slippery topic followable; reifiable/non-reifiable CONCEPT anchors the restrictions. |
| ACCURACY | 8 | JLS §4.6/4.7/4.8 verbatim, EJ items, arrays/varargs/heap-pollution traced; −2 for EJ verbatims, JLS §§ re-confirm, JEP 440/441 numbers, tool defaults (+ Sonar RSPEC ECONNREFUSED) carried verify-at-pin. |
| UTILITY | 8 | gives a senior reader the unchecked-warning discipline, PECS with the no-return-wildcard rule, the @SafeVarargs earned-assertion test, and -Xlint config; per-surface When-to-use directly actionable. |
| DEPTH | 8 | derives every sharp edge from erasure as one root fact; the arrays-vs-generics opposite-semantics deep dive + heap-pollution/@SafeVarargs is genuine senior-level material, not a tutorial rehash. |
| READABILITY | 8 | runnable hook, variance table, two sparing CONCEPT callouts + one AHEAD-OF-PIN, bounded snippets (≤9 lines), the "let the compiler carry it" thesis threaded throughout; no grey wall. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. Mid Part II
(Part II = Ch 5-12; Ch 12 closes it). Reuses item-to-rule crosswalk (Ch 7/8/10) + earned-assertion shape;
new PECS-variance-ladder + erasure-timeline shapes (candidate figures). Hands off to Ch 12 (code smells/patterns).
