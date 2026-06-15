# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** VERIFY (SOURCE-VERIFY)
- **Key:** 14 — Generics & type-safety (variance, bounded types, avoiding raw/unchecked)
- **Artifact:** `02-research/14_generics_type_safety/14_generics_type_safety_RESEARCH.md`
- **Date:** 2026-06-15
- **Reviewer:** source-verifier (adversarial, cold read; no web access this run)
- **Verdict:** **PASS_WITH_FLAGS**

## Scripts: run vs manual
| Script | Status | Result |
|---|---|---|
| `check_source_pin.sh` | RAN | **FAIL** — clone absent / off-pin (`{URL}`, whole authority set still `TO-PIN`). Pre-pin state, see PIPELINE-LEARNINGS OPEN ITEMS. |
| `ensure_source_pin.sh --heal` | RAN | Cannot heal — pin identifier is `{URL}` (nothing to fetch). |
| `check_neutrality.sh` | RAN | **PASS** (blocklist clean; advisory FLAGs: em-dash density, 1 filler word — style, AUDIT's lane). |
| `verify_sources.sh` | RAN | FAIL to run — depends on the absent pinned clone. |
| `lint_citations.sh` | RAN | 19 structural format violations + 1 author-year — house-style/format only (see findings). |

> Because the multi-authority pin is unfetchable (repo URL TBD; all rows `TO-PIN`), facts were judged against
> the dossier's own honest `⚠ verify at pin` markings and reviewer knowledge, not against a live pinned clone.
> Tool-specific defaults/severities are correctly deferred, not asserted. Recommend re-run after `/pin-source`.

## Checked claims / issues
| # | Claim / atom | Location | Trace / judgement | Result |
|---|---|---|---|---|
| 1 | Generics GA Java SE 5.0 (2004); JSR-14 | §1, §3 | Correct; primary | PASS |
| 2 | Diamond + `@SafeVarargs` = Java SE 7 (Project Coin) | §2.6/2.7 | Correct | PASS |
| 3 | JEP 213 "Milling Project Coin" (Java 9): anon-diamond + private-instance `@SafeVarargs` | §2.6/2.7, tbl | Correct title/version/scope | PASS |
| 4 | `var` = JEP 286 (Java 10); `var x = new ArrayList<>()` infers `ArrayList<Object>` | §2.7 | Correct | PASS |
| 5 | JLS §4.6 erasure→leftmost bound; §4.7 reifiable list; §4.8 raw def + §4.8-1 Cell | §2.1–2.3 | Content correct; marked "✅ verbatim" but verified at research-time web fetch, NOT a pinned clone (none exists) | FLAG |
| 6 | Arrays covariant+reified vs generics invariant+erased; `ArrayStoreException` | §2.6 | Correct (EJ Item 28) | PASS |
| 7 | PECS = EJ Item 31; Items 26–33 = Ch.5; "no wildcard return types" caveat | §2.4, §4 | Correct | PASS |
| 8 | JEP 440 record patterns / JEP 441 switch patterns (Java 21) | §5, §7 | Correct; dossier marks ☐ confirm at pin | PASS |
| 9 | Project Valhalla / reified generics NOT in 21/25 → `⚠ AHEAD-OF-PIN` | §5, §7 | Correctly flagged; matches Folklore-list addition | PASS |
| 10 | Sonar `java:S3740`/`java:S1452` (rule existence/titles) | §2 tbl, §8 | RSPEC page ECONNREFUSED; corroborated via sonar-java source + community; flagged `09-flags/14_sonar_rule_pages_unverified.md`; defaults NOT asserted | PASS (flagged) |
| 11 | PMD `UseDiamondOperator`/`LooseCoupling` FPs "per PMD issues" | §4 | Tool's own tracker (OK source) but no issue numbers; `⚠ verify at pin` | FLAG |
| 12 | "Checkstyle/PMD match source patterns (raw types, diamond, loose coupling)" | §4 | **Inaccurate** — those named rules are PMD's; Checkstyle does not ship them. Wrongly lumped | FLAG |
| 13 | "Sonar `java:S1452` is community-contested ... — Sonar community threads" | §4 | Comparative/limitation claim leans on non-pinned community threads (not a pinned authority) | FLAG |
| 14 | Error Prone `TypeParameterUnusedInFormals`, Checker Framework soundness quote | §3, §4 | Pattern/quote plausibly correct; `⚠ verify at pin` | PASS (flagged) |
| 15 | Neutrality — tool framing | §4 | No crown, no banned phrases (script clean), per-tool sourcing | PASS |
| 16 | HONEST-LIMITATIONS floor | §4 | Each option has hardest-objection + when-NOT-to-use | PASS |
| 17 | Citation house-style | §8 | 19 structural lint violations + author-year "(Bloch, 2018)" — format, no invented fact | FLAG |

## Findings & required fixes (none blocking; address at draft)
1. **§4 — "Checkstyle/PMD" misattribution (fix at draft).** The named generics rules (raw types, diamond, loose coupling) are PMD's, not Checkstyle's. Either drop Checkstyle from that clause or cite a real Checkstyle rule. Recommend web re-check at draft.
2. **§4 — S1452 "community-contested" (qualify/cite at draft).** Trace the limitation to Sonar's own pinned rule page/docs, or mark the contestation as corroboration-only (community threads are not a pinned authority).
3. **§4 — PMD FP claim (add issue refs at pin).** Add the PMD issue numbers when the PMD row is pinned; today correctly `⚠ verify at pin`.
4. **§2 / §8 — soften "✅ verbatim" JLS marks.** Content is correct, but no pinned clone exists; re-confirm §4.6/4.7/4.8 and the §4.8-1 example against the JLS SE 21 edition text at pin and keep §8 row-2 ☐ markers honest.
5. **§8 — citation format (mechanical).** Resolve `lint_citations.sh` structural violations (per-row URL/date-verified shape; author-year → house style) at draft assembly.
6. **Infrastructure (not chapter):** `/pin-source` must run — multi-authority pin is `{URL}`; re-run verify_sources/check_source_pin after pinning. Tracked in PIPELINE-LEARNINGS OPEN ITEMS.

## Blockers
- **None.** No invented fact, no unflagged off-pin claim, no neutrality breach. Every uncertain atom is honestly marked `⚠ verify at pin` / `⚠ UNVERIFIED` / `⚠ AHEAD-OF-PIN`, and both required flags (`09-flags/14_valhalla_ahead_of_pin.md`, `09-flags/14_sonar_rule_pages_unverified.md`) exist and are accurate.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a (research-time) primary/authority OR is marked `⚠`.
- [x] No folklore stated as fact (Valhalla correctly flagged AHEAD-OF-PIN).
- [x] No off-pin / moving-target citation asserted as settled (tool defaults deferred; no version > 25).
- [x] Neutrality: no winner crowned, no banned phrasing; tool claims per-tool-sourced.
- [~] Synthesized/comparative claims supported — mostly yes; finding #1 (Checkstyle) and #13 (S1452 source) need draft fix.
- [x] HONEST-LIMITATIONS floor met (hardest-objection + when-NOT per option).

## Learnings & pipeline suggestions
- **Pin infrastructure is the real gate today.** Until `/pin-source` resolves `{URL}`, VERIFY cannot trace against a clone; reviewers fall back to research-time fetches + reviewer knowledge. Re-run this gate after pinning.
- **"Checkstyle/PMD" lumping is a recurring trap** for survey chapters — attribute each named rule to the exact tool that ships it. Candidate AUDIT/VERIFY check: every named rule ID maps to exactly one tool.
- **Comparative limitation claims need a pinned source, not community threads** — promote a reminder that tool-limitation claims trace to the tool's own pinned docs/tracker, with community as corroboration only.
