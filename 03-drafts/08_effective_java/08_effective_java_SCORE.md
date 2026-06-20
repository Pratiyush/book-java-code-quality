# SCORECARD — Ch 5 "Effective Java & modern Java for quality" (key 08 + 13)

> Opens Part II. Main-loop; gates = manual passes. Canon-dating shape. Draft: `08_effective_java_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0; no crowning; Alternatives (other canons / "just read JEPs") = complementary, not ranked. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (book predates feature train; "served ≠ obsolete"; feature reach bounded; preview≠stable) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | principles → EJ 3e; each "changed terrain" → its JEP/JLS; JEP numbers carried verify-at-pin; structured concurrency/Valhalla flagged AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (handwritten-vs-record, sealed+switch) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | canon-dating method + the rule→feature→verdict table; the records "serve not retire" nuance is crisp. |
| ACCURACY | 8 | EJ + JEPs traced; −2 for JEP numbers carried verify-at-pin (confirmed in Ch 13). |
| UTILITY | 8 | tells a senior reader exactly which 2018 idioms to update and which to keep; the records decision rule. |
| DEPTH | 8 | merges the canon + modern features with the folklore correction and the standing book-as-secondary discipline. |
| READABILITY | 8 | strong hook (40-line class → one-line record), table-led, sparing callouts. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. Models the canon-dating shape reused by Ch 12 (patterns), 25 (SOLID), 39 (Fowler/Feathers).
