# GATE REPORT — SOURCE-VERIFY (key 09, Designing clear APIs & method contracts)

- **Gate:** SOURCE-VERIFY (pipeline step 2)
- **Artifact:** `02-research/09_api_method_contracts/09_api_method_contracts_RESEARCH.md`
- **Date:** 2026-06-15
- **Pin state:** PRE-PIN. SOURCE-PIN is multi-authority; every tool/JDK/spec row is `TO-PIN`.
  `check_source_pin.sh` → FAIL (ephemeral clone absent; expected). No pinned source text is
  fetchable, so this gate is a **flag-discipline audit, not atom re-verification**
  (per PIPELINE-LEARNINGS key-12 lesson). Atom re-trace must happen after `/pin-source`.
- **Scripts run:** `check_source_pin.sh` (FAIL-absent, expected), `lint_citations.sh`
  (6 structural nits — see below), banned-phrase grep (clean), moving-target grep.
  `verify_sources.sh` not run against a clone (no pin); `check_snippets.sh` n/a (dossier, no include markers).

## Overall verdict: PASS_WITH_FLAGS

The dossier is disciplined: 31 `⚠`/AHEAD-OF-PIN/verify-at-pin markers, no folklore-as-fact,
no banned phrasings, no winner crowned, HONEST-LIMITATIONS floor met (each option carries a
hardest-objection + when-NOT-to-use). The flags it raises are filed (`09-flags/09_s2201_scope_limit_unverified.md`,
`09-flags/09_jep467_markdown_doccomments_ahead_of_pin.md`). Issues found are minor/hygiene and
the standing pre-pin atom-trace debt — none is an invention or a neutrality breach.

## Checked claims / issues

| # | Claim / check | Finding | Verdict |
|---|---|---|---|
| 1 | `Objects` signatures + `since` (1.7/1.8/9/16), throws | Marked verified-vs-Javadoc; matches known JDK history; cannot re-confirm pre-pin | OK (re-trace at pin) |
| 2 | EJ 3e item numbering (Ch 8 = 49–56; 15–17, 50–51) | Matches known 3e structure; item titles via O'Reilly ToC; verbatim quotes flagged for draft | OK |
| 3 | "≤4 parameters" (Item 51), Item 16 accessor-not-field, Item 49 check-params | Correct against EJ 3e | OK |
| 4 | JEP numbers (395/16, 409/17, 441/21, 440/21, 467/23) | Match the key-13 verified list in PIPELINE-LEARNINGS | OK |
| 5 | JEP 467 Markdown doc comments = JDK 23 | Correctly marked AHEAD-OF-PIN, not shown as available at 21; flagged | OK |
| 6 | IntelliJ 2025.3 "JSpecify preferred", JSpecify 1.0 | Correctly marked AHEAD-OF-PIN / direction-of-travel, not anchor baseline | OK |
| 7 | Tool rule IDs (Error Prone / SpotBugs / PMD / Sonar) | Cited as stable IDs + named tool; severities/defaults deferred `⚠ verify at pin` (correct granularity) | OK (re-trace at pin) |
| 8 | Sonar `java:S2201` scoped type list | Marked verify-at-pin; flag file matches; honest limitation surfaced in §4 | OK |
| 9 | Sonar `java:S1226` reassign-before-read framing | Matches updated RSPEC-1226; attributed | OK |
| 10 | Folklore cross-check (records-replace-immutability, etc.) | None stated as fact; records framed as "transparent immutable carriers" w/ cross-ref | CLEAN |
| 11 | Neutrality (no crowning, no banned phrases) | grep clean; §4 note explicitly crowns no tool, defers layering to key 37 | CLEAN |
| 12 | HONEST-LIMITATIONS floor | §4 gives each lever a hardest-objection + when-NOT-to-use | MET |
| 13 | Moving-target citations | Source #4 + S2201 flag use `github.com/.../blob/master/...` (master path) | NIT (fix) |
| 14 | `lint_citations.sh` per-row date-verified | 6 rows carry `☐`/`corroboration` not a date | NIT (hygiene) |

## Required fixes (minor — do at draft, none block research bank)

1. **Moving-target URL.** Replace the two `/blob/master/` GitHub links (Accessible source #4
   `CheckReturnValue.md`; the S2201 flag's `IgnoredReturnValueCheck.java`) with a tagged/pinned
   ref once `/pin-source` fixes the error-prone and sonar-java rows. SOURCE-PIN moving-target
   policy forbids `master`/`main` as the cited identifier. (Low risk — both are corroboration/flag tier.)
2. **Citation hygiene (lint).** The 6 lint VIOLATIONs are rows correctly marked `☐ confirm at pin`
   / `corroboration`; add a date-verified stamp (or the linter's accepted marker) at draft so the
   row shape passes — no fabricated date.
3. **Standing pre-pin debt (NOT a defect of this dossier):** after `/pin-source`, re-trace the
   atom set this gate could not confirm against text — `Objects` `since`/signatures, EJ verbatim
   quotes, JLS §§ (8.4.1 / 15.12.2 / 6.6), the S2201 scoped list, all tool severities/GAVs.

## Blockers: 0

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority OR is marked `⚠ UNVERIFIED` / `verify at pin` / `AHEAD-OF-PIN`.
- [x] No folklore stated as fact (cross-checked the Folklore list).
- [x] No off-pin / moving-target fact asserted as settled (one corroboration-tier `master` URL noted to fix).
- [x] Neutrality: no winner crowned, no banned phrasing, cross-tool facts cited to the named tool.
- [x] Synthesized / comparative claims supported (machine-checkability framing traces to each tool's own rule).
- [x] HONEST-LIMITATIONS floor met.
- [x] Flags filed match the dossier's `⚠` markers.

## Learnings & pipeline suggestions
- Pre-pin SOURCE-VERIFY for tool-naming dossiers is reliably a flag-discipline audit; key 09
  confirms the key-09/key-12 pattern (cite stable rule ID + named tool, defer severity/GAV).
- Recurring nit across keys: corroboration-tier and flag-file GitHub links use `/blob/master/`.
  Recommend a standing rule — even corroboration links use a tagged ref, never `master`/`main`.
