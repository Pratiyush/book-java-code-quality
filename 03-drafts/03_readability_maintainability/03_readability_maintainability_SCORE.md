# SCORECARD — Ch 2 "Readability, maintainability & measuring quality" (key 03 + 04 + 58)

> Drafted main-loop (cheaper mode); gates = documented manual passes. ⚠ contested-practice chapter.
> Draft: `03_readability_maintainability_v1.md`. Pinned @ SOURCE-PIN 2026-06-20.

## Content-floors
| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | banned sweep = 0; Clean Code vs A Philosophy of Software Design presented as two schools, crowned neither; Alternatives are trade-offs, not rankings. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (5) + §When to use; "metrics are proxies/gameable"; prescriptions conflict acknowledged. |
| C — SOURCE-TRACE | ✅ PASS | all facts cited to pinned sources (Clean Code, APoSD, SonarSource, McCabe, CK, Goodhart, DORA/SPACE); thresholds/JEPs marked verify-at-pin. |
| C — COMPILE/CODE-REVIEW | ⚠ PENDING-RUNTIME | concept chapter; companion module spec'd, not built (no JDK). |

## Five clusters
| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | cyclomatic-vs-cognitive distinction lands; vanity-metric map; Goodhart explained. |
| ACCURACY | 8 | traced; −2 for verify-at-pin thresholds/JEPs. |
| UTILITY | 8 | gives the "which metric for which question" + counter-metric discipline a lead can apply. |
| DEPTH | 8 | merges readability + measurement + complexity (3 dossiers) with the contested zone done fairly. |
| READABILITY | 8 | concrete hook (the 3 a.m. page), plain-language-first, tables carry rhythm. |

**Aggregate 40/50**, none < 6.

## Ship verdict
Clusters ✅ (40/50); floors A/B/C-source ✅; **BLOCKED on FLOOR-C COMPILE = PENDING-RUNTIME** (no JDK). Draft-complete, gate-clean except compile. Run independence gates (5b/8b) on a different model before the Step-12 human gate.
