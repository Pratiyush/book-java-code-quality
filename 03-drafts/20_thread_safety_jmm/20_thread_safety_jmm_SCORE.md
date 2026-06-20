# SCORECARD — Ch 13 "Thread-safety, the JMM & safe publication" (key 20 + 21 + 23)

> Part III OPENER (Part III = Ch 13-14), three merged dossiers (JMM/thread-safety + safe publication + j.u.c).
> A-tier flagship correctness chapter. Main-loop; gates = manual passes. happens-before-spine +
> four-publication-idioms + version-bound-advice + library-vs-hand-rolled shapes. Draft:
> `20_thread_safety_jmm_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); j.u.c-vs-hand-rolled framed as two ways of using the same JDK (both Java, neither crowned, each its when-NOT); four publication idioms presented as edges not a ranking; confinement/immutability/locks/scoped-values as different shapes of one goal, crown none; tool detectors cited each to own source. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (JMM formal model hard; volatile≠atomicity; final-field 2 traps + this-escape; DCL fragile + S2168/S3077 tension; CHM per-op-not-per-txn; COW/StampedLock edges; executor leak; immutability copy cost; static detectors incomplete + JCStress proves-presence-not-absence; j.u.c not free) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | happens-before/data-race/correctly-synchronized + final-field freeze + long/double verbatim from JLS SE 21 ch.17; h-b edge list from j.u.c summary; JEP Release fields; j.u.c Since fields; tool rule IDs each to own tool; SE 25 §§/Sonar titles/SpotBugs verbatim/EP severities/JCIP carried verify-at-pin; structured-concurrency/scoped-values/JEP491 flagged AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (racy counter + 3 fixes; @GuardedBy ERROR build-fail; JCStress FORBIDDEN→ACCEPTABLE) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the "bug that passes every test" hook + happens-before-as-definition-of-correct spine + the edge table + the four-idioms table make the hardest topic in the book genuinely followable; two CONCEPT callouts (whole-job, visibility≠atomicity) anchor it. |
| ACCURACY | 9 | core JMM facts verbatim from JLS SE 21; h-b edges from j.u.c summary; version-bound pinning (JEP 444→491) dated precisely; −1 for SE 25 §§, Sonar/SpotBugs/EP defaults, JCIP verbatim carried verify-at-pin (atoms, not load-bearing claims). |
| UTILITY | 8 | gives a senior reader the reasoning method (which edge?), the four publication idioms, the j.u.c-first stance, the detection floor (@GuardedBy + JCStress), and version-bound VT advice; per-shape When-to-use directly actionable. |
| DEPTH | 9 | merges JMM theory + safe publication + j.u.c into one "establish the edges" arc; the-race-you-cant-test-for framing + this-escape + DCL ladder + version-bound advice are genuine senior-staff material, not a tutorial. |
| READABILITY | 8 | strong concrete hook, table-led (edges, idioms), the version-bound pinning worked as the dating-discipline exemplar, bounded inline snippets; appropriately the longest chapter so far (4053w) without a grey wall. |

**Aggregate 43/50**, none < 6 (highest so far — fitting for the flagship correctness chapter). Floors A/B/C-source ✅;
FLOOR-C COMPILE = PENDING-RUNTIME. **OPENS PART III.** New reusable shapes: happens-before-edge-per-idiom,
version-bound-recommendation (advice dated to JDK level), library-vs-hand-rolled-same-JDK neutrality. Hands off
to Ch 14 (virtual threads / structured concurrency / concurrency testing). Static-detector depth → Part IV.
