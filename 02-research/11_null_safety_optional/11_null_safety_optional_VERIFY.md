# GATE REPORT — SOURCE-VERIFY (key 11, Null-safety & Optional discipline)

- **Gate:** VERIFY (Step-2 source-verify)
- **Artifact:** `02-research/11_null_safety_optional/11_null_safety_optional_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS**

## Pin / scripts status (honest header)
- `check_source_pin.sh` / `ensure_source_pin.sh --heal`: **pin ABSENT and un-healable** — SOURCE-PIN.md
  is still entirely `TO-PIN` / `{URL}` (the `/pin-source` step has never run; this is a recorded
  OPEN ITEM in PIPELINE-LEARNINGS). The ephemeral multi-authority clone therefore does not exist.
  **Consequence:** byte-level verbatim verification against pinned source text was NOT possible this run.
  All facts were checked against canonical doc URLs + reviewer knowledge and against the dossier's own
  `⚠ verify-at-pin` discipline. This is consistent with the whole-book pre-pin research posture (keys
  01–19 ran the same way); it is NOT a hard-fail of the dossier, but byte-verification is DEFERRED to pin.
- `verify_sources.sh`: could not run (aborts — pinned clone absent). Reported, not faked.
- `lint_citations.sh`: ran — **7 structural violations** (minor, see below).
- `check_neutrality.sh`: ran — **blocklist CLEAN**; advisory FLAGs only (1 filler word, em-dash density).
- `check_snippets.sh`: N/A (research dossier, no include markers; fenced blocks all ≤9 lines per lint).

## Checked claims / issues

| # | Claim / atom | Trace | Result |
|---|---|---|---|
| 1 | JEP 358 → JDK 14 target, ON by default since JDK 15, flag `-XX:+ShowCodeDetailsInExceptionMessages` | JEP 358 (403 to fetch) | OK + correctly flagged `⚠ UNVERIFIED` (`09-flags/11_jep358_text_unverified.md`); verbatim example message NOT block-quoted |
| 2 | JSpecify 1.0.0, GAV `org.jspecify:jspecify:1.0.0`, pkg `org.jspecify.annotations`, 4 annotations, `@NullMarked`=non-null default, type-use | jspecify.dev / Maven Central | OK (matches release); verify-at-pin marked |
| 3 | NullAway 1.15× / Eradicate 2.8× / Checker 5.1× build overhead | arXiv:1907.02127 (2019) | OK — correctly cited as a DATED 2019 study figure, not a current benchmark |
| 4 | EJ 3e Item 54 ("Return empty collections/arrays, not nulls") + Item 55 ("Return optionals judiciously"), no-container-in-Optional, perf caveat | EJ 3e | OK (titles/guidance correct); verbatim quotes + page flagged `⚠ verify at draft` |
| 5 | `Objects` method JDK levels — requireNonNull(7), Supplier msg(8), requireNonNullElse/ElseGet(9) | Objects Javadoc | OK |
| 6 | `Optional` API-Note quote ("clear need to represent 'no result'…") + value-based-class quote | Optional Javadoc SE 21 | OK to recollection; byte-verbatim DEFERRED (no clone) — fine as quoted, but confirm at pin before block-quoting in draft |
| 7 | Sonar `java:S3655` / `java:S2789` / `java:S2259` meanings | rules.sonarsource.com | OK — real rules, meanings correct (note: key-18 "rule-ID-from-memory" trap checked; these survive). Defaults/severity correctly deferred `⚠ verify at pin` |
| 8 | Error Prone `NullableOptional` / `OptionalNotPresent` / `ReturnMissingNullable`; SpotBugs `NP_NULL_ON_SOME_PATH` / `RCN_*` | errorprone.info / spotbugs detectors | OK — real patterns, meanings correct |
| 9 | Spring 7 / Boot 4 JSpecify (Nov 2025), IntelliJ 2025.3, Valhalla null-restricted types | spring.io / jspecify blog | OK — correctly marked `⚠ AHEAD-OF-PIN`, framed as adoption signal only (`09-flags/11_nullsafety_ahead_of_pin.md`) |
| 10 | JSR-305 dormant; not current | — | OK — handled correctly (demoted, not asserted current) |
| 11 | Neutrality (4 levers, annotation systems named) | NEUTRALITY.md | PASS — no crowning, no blocklist phrase; each lever + each checker gets case + objection; choice deferred to key 32 |
| 12 | HONEST-LIMITATIONS floor (§4) | floor | PASS — every lever has a hardest-objection + when-NOT-to-use |
| 13 | Folklore cross-check (PIPELINE folklore list) | folklore list | PASS — Valhalla "reified/null-restricted soon" correctly marked AHEAD-OF-PIN, not asserted; no 1:10:100, no coverage-as-quality, no MI-as-score stated as fact |

## Flags (advisory / minor — do NOT block)
- **F1 (lint, minor).** 7 `lint_citations.sh` structural violations: 5 source rows use a non-standard
  verified-marker ("partial", "corroboration", "☐") the linter does not recognize as a date-verified
  marker; rows 7 and 12 carry `github.com/... · arxiv...` and `print` instead of a single plain `http`
  URL. Cosmetic / linter-shape; fix at draft (add date-verified column, give NullAway/arXiv plain URLs,
  mark the EJ "print" row explicitly). No fact is wrong.
- **F2 (status honesty).** Several §2.7 / §8 atoms read "verified @pin" / "☑" although NO pinned clone
  exists (book is pre-`/pin-source`). The caveats ARE present in §7/§8 and the header, so this is an
  over-confident marker, not a false fact. At draft, downgrade bare "verified @pin" to "verify at pin"
  until `/pin-source` runs and `verify_sources.sh` can pass.
- **F3 (recommend web re-check at draft, cannot disprove offline).** Two values I could not byte-verify
  without the clone but have no reason to doubt: JEP 358 verbatim example-message wording; EJ 3e Item 55
  exact quotations + page numbers. Already queued in §7 — keep them out of block-quotes until confirmed.
- **F4 (advisory, AUDIT lane).** Em-dash density 19/1000 (over ~8/1000 ceiling) + 1 filler word — style,
  for the CLARITY/AUDIT gates, not a VERIFY blocker.

## Blockers
- **NONE** that are the dossier's fault. The pin being un-healable is a project-level OPEN ITEM
  (`/pin-source` not yet run), already tracked; it blocks byte-verification book-wide, not this chapter.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority/primary OR is marked `⚠ UNVERIFIED` / `⚠ verify at pin`.
- [x] No folklore stated as fact (cross-checked the folklore list).
- [x] No off-pin / moving-target fact asserted as settled (Spring 7 / Boot 4 / IntelliJ / Valhalla all AHEAD-OF-PIN).
- [x] Neutrality: no winner crowned, no banned phrasing, each option case + limit + own-source cite.
- [x] Synthesized / causal / comparative claims supported (the 1.15×/5.1× sound-vs-fast trade-off is cited to the same 2019 paper; "NPE is a design defect" framed as thesis, not as a sourced empirical claim — acceptable).
- [x] HONEST-LIMITATIONS floor met (each lever: hardest objection + when-NOT-to-use).
- [ ] Byte-verbatim verification against pinned source — DEFERRED (no clone; pin un-healable this run).

## Learnings & pipeline suggestions
- The VERIFY gate cannot do its byte-level job until `/pin-source` runs; until then it can only confirm
  flagging-discipline + neutrality + folklore + claim-support. Recommend the standing OPEN ITEM make
  explicit that pre-pin `_VERIFY.md` reports are "discipline-PASS, byte-verification DEFERRED."
- The "verified @pin / ☑" marker is being used pre-pin across dossiers (F2 recurs); recommend reserving
  ☑ strictly for post-pin byte-checks and using "verify at pin" everywhere until then. (Append to PIPELINE-LEARNINGS.)
