# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** VERIFY (source-verify)
- **Key:** 18 — Defensive coding & input validation (Jakarta Validation, guard clauses)
- **Artifact:** `02-research/18_defensive_validation/18_defensive_validation_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS**

## Scripts run vs manual
| Script | Status | Result |
|---|---|---|
| `check_source_pin.sh` | RAN | FAIL — pinned clone ABSENT (ephemeral; book-wide TO-PIN state, `/pin-source` not yet run). Expected per PIPELINE-LEARNINGS open item. |
| `ensure_source_pin.sh` | RAN | Cannot heal — no repo URL exists yet (every SOURCE-PIN tool row is `TO-PIN`; multi-authority pin not established). |
| `verify_sources.sh` | RAN | Aborts: "pinned clone absent." Trace-against-clone impossible until `/pin-source`. Fact-trace done MANUALLY (cold read + cross-check vs PIPELINE-LEARNINGS + known JDK/JLS/JEP facts). |
| `lint_citations.sh` | RAN | 8 structural URL-format violations (bare `docs.oracle.com/...` lack an `http://` prefix; one row missing a verified marker). Cosmetic citation-format issues, fix at draft. |
| `check_snippets.sh` | n/a | Not a draft; no `<!-- include -->` markers. Fenced blocks all <=9 lines (lint confirms). |

> The pin cannot be brought on-pin because the book has not run `/pin-source` (all rows TO-PIN). This is the documented book-wide state, not a key-18 defect. The dossier correctly anticipates this: every tool/version atom is marked `⚠ verify at pin`, and untraceable items are flagged. Verification here is the manual cold/adversarial read the task specifies.

## Checked claims / issues
| # | Claim | Trace / judgment | Status |
|---|---|---|---|
| 1 | Jakarta Validation **3.1**, renamed from "Jakarta Bean Validation", **Final 2024-03-28**, record support clarified | Corroborated by PIPELINE-LEARNINGS key-18 entry (independent); marked `⚠ verify at pin` | OK (flagged) |
| 2 | Hibernate Validator **9.1.0.Final** = RI of **3.1.1**, requires **Java 17+**; GAV `org.hibernate.validator:hibernate-validator:9.1.0.Final` | Matches PIPELINE-LEARNINGS key-18 verbatim; marked `⚠ verify exact patch at pin` | OK (flagged) |
| 3 | `Objects.requireNonNull` (Java 7+); `checkIndex`/`checkFromIndexSize`/`checkFromToIndex` (Java 9+); returns arg, throws NPE/IOOBE | Standard, accurate JDK API facts | OK |
| 4 | `assert` statement = **JLS §14.10**; disabled unless `-ea`; throws `AssertionError` | Accurate (JLS SE 21 §14.10) | OK |
| 5 | Records final at **Java 16, JEP 395**; compact-constructor home for invariant guards | Accurate; consistent with key-13 verified JEP list | OK |
| 6 | Executable/method validation "spec since 1.1"; cross-parameter; `ExecutableValidator` API | Accurate (JSR 349 / BV 1.1 introduced method + cross-param validation) | OK |
| 7 | OWASP: input validation "should **not** be used as the *primary* method of preventing XSS, SQL Injection"; allowlist > denylist; server-side/trusted | Genuine OWASP Cheat Sheet position, load-bearing for neutrality with key 72 | OK |
| 8 | Sonar `java:S5128` = Bean Validation configuration rule | Correctly marked `⚠ verify at pin`; flag filed `09-flags/18_sonar_s5128_title_unverified.md`; mis-recalled IDs (S4790/S5876/S5527) explicitly **discarded** not asserted | OK (flagged — model discipline) |
| 9 | Error Prone `ParameterMissingNullable`/`FieldMissingNullable`/`ReturnMissingNullable` | Plausible pattern names; severities marked `⚠ verify at pin` | OK (recommend web re-check at draft) |
| 10 | Jakarta EL GAV (`org.glassfish.expressly:expressly` vs `org.glassfish:jakarta.el`) | Explicitly listed unresolved in §7; marked `⚠ verify at pin` | OK (flagged) |
| 11 | "Bad input is the single largest source of both bugs and vulnerabilities" (line 46) | Unsourced rhetorical generalization (no fake statistic — not folklore) | MINOR — soften/qualify at draft |
| 12 | Quoted Item-49 wording ("check these restrictions at the beginning of the method body…") | Marked `⚠ verbatim at draft` (§7) — quote not yet verified char-for-char | OK (flagged; MUST verify before block-quoting) |
| 13 | Banned-phrasing scan | None present (`grep` clean) | PASS |
| 14 | Folklore scan (1:10:100, MI-as-score, coverage-as-quality, records-obsolete-immutability, Valhalla) | None stated as fact | PASS |
| 15 | Off-pin / moving-target (SNAPSHOT/main/newer-than-pin/JDK>25 as settled) | None. One correct `⚠ AHEAD-OF-PIN` guard at line 163 (no 25-preview asserted) | PASS |
| 16 | Neutrality — no winner crowned, two complementary mechanisms | Met; "neither subsumes the other"; per-tool strongest-case + hardest-limitation | PASS |
| 17 | HONEST-LIMITATIONS floor (hardest-objection + when-NOT per option) | Met for BOTH guard clauses and Jakarta Validation (lines 147-153) | PASS |

## Blockers
None. (0 blockers.)

## Required fixes (before/at draft — non-blocking)
1. **Verify Item-49 quote verbatim** against *Effective Java* 3e print before any block-quote (currently `⚠ verbatim at draft`). A quote-as-fact ships only when char-for-char confirmed.
2. **Resolve at `/pin-source`** (the dossier already queues these; do not assert until cleared): Jakarta 3.1 GAV/version, Hibernate Validator patch, Jakarta EL GAV, `java:S5128` exact title/type, Error Prone pattern names + severities.
3. **Citation format** (lint): give Source rows full `http(s)://` URLs and a verified marker on row 9; cosmetic, fix at draft.
4. **Soften line 46** ("single largest source of bugs and vulnerabilities") to a qualified/attributed statement or cut the superlative — no figure backs it.

## VERIFY gate-specific checks
- [x] Every specific atom traces to a pinned authority OR is marked `⚠ verify at pin` / `⚠ UNVERIFIED` (book-wide TO-PIN respected).
- [x] No folklore stated as fact.
- [x] No off-pin / moving-target / JDK>25-as-settled citation.
- [x] Neutrality: no crowning, no banned phrasing, balanced two-mechanism survey.
- [x] Synthesized/causal/comparative claims supported (complementary-mechanisms framing is descriptive, not a winner claim; OWASP "not primary defense" cited).
- [x] HONEST-LIMITATIONS floor met per option.
- [ ] Pinned-clone trace — NOT possible (no pin yet); deferred to `/pin-source`, atoms flagged accordingly.

## Learnings & pipeline suggestions
- This dossier is a model of the **"never assert a rule ID from memory"** discipline already promoted from key 18 — it discards mis-recalled Sonar IDs rather than guessing. Keep as the reference example when promoting the "tool rule IDs" note into SOURCE-PIN's never-invent atom list.
- The whole book remains un-pinnable (`verify_sources.sh`/`check_source_pin.sh` hard-fail with no repo URL). Until `/pin-source` runs, SOURCE-VERIFY can only do manual cold-reads + flagging; that is the current ceiling. Strongly recommend prioritizing `/pin-source` so trace-against-clone becomes real for the Part II batch.
