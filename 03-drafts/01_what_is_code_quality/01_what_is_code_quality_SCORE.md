# SCORECARD — Ch 1 "What is code quality & what poor quality costs" (key 01 + 02 + 59)

> Pilot chapter, drafted main-loop (cheaper mode). Gates run as **documented manual passes** (no scorer
> subagent — spend-cap aware; the skill permits manual). Draft: `01_what_is_code_quality_v1.md`.
> Pinned @ SOURCE-PIN 2026-06-20.

## Content-floors (checked first; fatal if FAIL)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ PASS | banned-phrasing sweep = 0; no winner crowned; the two "Alternatives" are framed as complementary, not ranked. |
| **B — HONEST-LIMITATIONS** | ✅ PASS | §Limitations (5 items) + §When to use (throwaway-code exception); negative-cost claim explicitly scoped to long-run. |
| **C — SOURCE-TRACE** | ✅ PASS | every load-bearing fact cited to a pinned authority (ISO 25010:2023, Fowler, Clean Code, Cunningham, SQALE/Sonar, CISQ 2022, DORA 2025). Edition-sensitive facts (25010 2023 vs 2011) handled with the standards-edition discipline. |
| **C — COMPILE / CODE-REVIEW** | ⚠ **PENDING-RUNTIME** | concept chapter; companion module spec'd but **not built** (no JDK installed). FLOOR-C compile clause deferred per repro-proofer toolchain-gating. Lifts when JDK 21 is installed + `./mvnw -B verify` runs green. |

## Five clusters (1–10)

| Cluster | Score | One-line justification |
|---|---|---|
| CLARITY | 8 | mechanism (decompose → internal/external → negative-cost → debt) ordered; each step earns the next; ASCII sketch + tables carry the model. |
| ACCURACY | 8 | fully traced to pinned sources; −2 because edition sub-tree (25010:2023) + exact SQALE thresholds are marked verify-at-pin (Ch 38), not yet machine-confirmed. |
| UTILITY | 8 | gives a lead the vocabulary + the negative-cost + the timeframe argument to actually use in a planning conversation; concrete Java maintainability table. |
| DEPTH | 8 | merges three dossiers (def + economics + debt-management) with real substance; honest exceptions; could go deeper on SQALE mechanics (deferred to Ch 38 by design). |
| READABILITY | 8 | concrete hook, plain-language-first, second person, sparing callouts, visual rhythm; no jargon wall. |

**Aggregate: 40 / 50**, no cluster < 6.

## Ship verdict

- **Ship bar (SCORING.md):** ≥35/50, no cluster <6, ALL floors PASS. → Clusters ✅ (40/50), Floors A/B/C-source ✅.
- **BLOCKED on FLOOR C COMPILE = PENDING-RUNTIME** until a JDK is installed and the companion module builds green. The chapter is **draft-complete and gate-clean except the compile floor**; it cannot pass the Step-12 human gate until FLOOR C lifts.
- No bounded-lift-loop pass needed (clusters cleared on the first draft).

## Manual gate notes (Steps 5–8)
- **VERIFY (5):** facts trace to pinned rows; banned-phrasing sweep clean. (Formal `_VERIFY.md` for the folded dossiers 01/02/59 folds here — done at draft.)
- **CLARITY (6):** mechanism followable; one core concept ("name it / price it"); ≤3 heading levels.
- **AUDIT (7):** neutrality + voice + authenticity — reads as human-authored senior prose; no AI-tells in a spot check. (Independence note: a true RED-TEAM/ORIGINALITY pass should run on a different model than the drafter — deferred, recommended before the human gate.)
- **SCORE (8):** above.

## Learnings & pipeline suggestions
- Concept-chapter FLOOR C is cleanly toolchain-gated (PENDING-RUNTIME) — the pipeline handles a JDK-less environment without blocking prose. Confirms the "concept chapter example optional" learning.
- Merging 3 dossiers into one chapter worked well at ~3.5k words; the merge map in FINAL_INDEX held.
- Before the human gate (Step 12), run the two independence gates (ORIGINALITY 5b, RED-TEAM 8b) on a different model/persona than the drafter, per the kernel rule.
