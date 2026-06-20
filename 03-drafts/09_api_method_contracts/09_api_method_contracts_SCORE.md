# SCORECARD — Ch 7 "Designing clear APIs, contracts & compatibility" (key 09 + 60)

> Part II craft chapter, two merged dossiers (in-the-small contracts + cross-version evolution).
> Main-loop; gates = manual passes. Two-halves-of-a-contract + contract-card shapes; comparison-sensitive
> (revapi/japicmp), no crown. Draft: `09_api_method_contracts_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 ("worse than none" reworded → "actively misleads"); analyzers framed as enforcers of the same rules not rivals (layering deferred to Ch 17); revapi vs japicmp each its own case+limit, neither crowned; annotation packages framed neutrally. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (runtime checks not free/complete; Optional costs; defensive-copy shallow-clone trap; S2201 scoped; annotation packages not one standard; doc drift; compat tools = signature-not-behaviour breaks + setup cost) + §When to use (incl. when NOT to gate compat). |
| C — SOURCE-TRACE | ✅ PASS | Objects signatures+since verified @JDK 21; EJ Items 15–17/49–56 cited; analyzer rule IDs each to own tool; JLS §§/ch.13 + JEP numbers + EJ verbatims + S2201 list + JSpecify version carried verify-at-pin; JEP 467 flagged AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (MoneyTransfer contracts + EP/SpotBugs/PMD gate; v1→v2 binary-break + japicmp) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | two-halves table + "move promises leftward" thesis + the enforcement matrix make an abstract topic concrete; the hook's Optional move seeds the whole chapter. |
| ACCURACY | 8 | JDK API verified; EJ items + rule IDs traced; −2 for EJ verbatims, JLS §§, JEP numbers, S2201 scoped list, JSpecify version, tool severities carried verify-at-pin. |
| UTILITY | 8 | gives a senior reader the design canon + the machine-check map + the semver/binary-compat gate recipe; per-surface When-to-use is directly actionable. |
| DEPTH | 8 | merges code-level contract craft with cross-version evolution into one "contract over time" arc; honest on tool scope limits and the source-vs-binary trap. |
| READABILITY | 8 | concrete NPE hook, table-led, sparing CONCEPT/AHEAD-OF-PIN callouts, the matrix carries the enforcement story; no grey wall. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. Establishes the
**contract-card** + **two-halves (type-carried vs doc/runtime-carried)** shapes reused across Part II
(Ch 8 equals/hashCode, Ch 9 null-safety, Ch 10 validation). Compatibility half hands off to Ch 26 (fitness functions).
