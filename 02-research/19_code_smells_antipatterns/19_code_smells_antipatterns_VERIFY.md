# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** VERIFY (source-verify)
- **Artifact:** `02-research/19_code_smells_antipatterns/19_code_smells_antipatterns_RESEARCH.md`
- **Key:** 19 — Code smells & Java anti-patterns
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS**
- **Blockers:** 1 (environmental: pin not established — see B1)

## Scripts: run vs manual
| Script | Status | Result |
|---|---|---|
| `check_source_pin.sh` / `ensure_source_pin.sh --heal` | RAN | FAIL — pinned clone ABSENT and unhealable (repo URL `<TBD>`, all rows `TO-PIN`; `/pin-source` has never run — confirmed OPEN ITEM in PIPELINE-LEARNINGS). |
| `verify_sources.sh` | RAN | FAIL — cannot trace: no pinned clone on disk. |
| `lint_citations.sh` | RAN | 11 structural violations (book-canon + spec rows missing URL/date-verified). |
| `check_neutrality.sh` | RAN | Blocklist CLEAN. Advisory FLAGs: 5 filler words; em-dash density 15/1000 (over ceiling). |
| `check_snippets.sh` | N/A | Dossier (step 2), no `include:` markers; deferred to draft. |
| Pinned-source fact tracing | MANUAL / NOT POSSIBLE | No pinned authority on disk; facts judged against in-repo cross-checks (key-13 verified JEP list, folklore list, EJ-3e public canon). No web access. |

## Checked claims
| # | Claim | Disposition |
|---|---|---|
| 1 | JEP↔version pairs: records 395/Java16, switch-patterns 441/Java21, sealed 409/Java17, text-blocks 378/Java15 | OK — match the in-repo key-13 VERIFIED list; all also marked `⚠ verify at pin`. No ahead-of-pin error. |
| 2 | EJ 3e item numbers (2 Builder, 8 finalizers, 10–11 equals/hashCode, 17 immutability, 26 raw types, 62 strings) | OK — consistent with established EJ 3e canon; correctly marked `⚠ verify at pin`. No invention. |
| 3 | PMD rule keys + defaults (GodClass, CyclomaticComplexity 10/80, NcssCount 60/1500, etc.) | FLAG — read from the **live** PMD Design page (off-pin); §2.5 annotates several "(confirmed)" which overstates given no pin. Threshold-as-convention framing present. Flagged in `09-flags/19_...`. |
| 4 | Sonar keys S3776=15, S107=7 / S138, S1192=3, S1448 | OK as flagged — S3776/S107 "confirmed", remainder `⚠ verify at pin`. Honors key-18 "never assert a rule ID from memory" discipline. |
| 5 | Rule names marked `*` (ExcessiveMethodLength, SwitchDensity, AvoidUsingHardCodedIP, S4276, etc.) | OK — explicitly marked "plausible but not confirmed verbatim → flag". Not asserted as fact. |
| 6 | Folklore guard ("every smell must be removed", "X lines = always too long", MI/coverage) | OK — invoked correctly, not stated as fact. |
| 7 | Neutrality ("crowns none", "different approaches", per-tool context map) | OK — blocklist clean; no winner crowned; balanced. |
| 8 | HONEST-LIMITATIONS floor (false-positive hint, when-NOT-to-gate, undetectable smells, refactor-not-free, over-refactor smell) | OK — met. |
| 9 | Synthesized claim: "Convergence across rivals is strong evidence the smells are real signals" | FLAG — reasonable but synthesized; soften to attributed observation at draft (no single pinned source makes this claim). |
| 10 | OpenRewrite "50+ issues" quote | FLAG — drifts across THREE spellings: "more than 50 types of issues" (§3), "more than 50 issues" (§7), "50+ issues" (§8/§9). Only one can be verbatim. |
| 11 | Fowler definition: smells "suggest (sometimes scream for) … refactoring" / "suggest" | FLAG — attributed verbatim to Fowler but unverifiable (print canon, no pinned text). Confirm against Refactoring 2e at draft or demote to paraphrase. |

## Blockers
- **B1 (environmental, not authorial):** No pinned authority set exists — `SOURCE-PIN.md` is entirely `TO-PIN` with repo URL `<TBD>`; `check_source_pin`/`ensure_source_pin --heal` both fail. No fact in this dossier has been traced against a pinned source by script. This does NOT fail the dossier (it correctly marks every tool fact `⚠ verify at pin` and filed `09-flags/19_...`), but it BLOCKS final fact-sign-off until `/pin-source` runs and a Step-2 re-trace is performed. This is a repo-wide standing OPEN ITEM, not specific to key 19.

## Required fixes (carry into draft)
1. **Quote consistency (must-fix before draft prints it):** pick ONE verbatim form of the OpenRewrite "50+ issues" line and confirm it against the pinned OpenRewrite docs, or demote to paraphrase ("over 50 fixes"). Remove the other two spellings.
2. **Fowler quote:** confirm the "suggest / scream for" wording against Refactoring 2e at the pin, or render as paraphrase. No verbatim quote may ship unverified.
3. **§2.5 "(confirmed)" annotations:** downgrade to `⚠ verify at pin` consistently — the source was the live PMD page, not a pin; the "(confirmed)" wording contradicts the dossier's own header caveat.
4. **Citation lint:** add date-verified markers to source rows; book-canon (print) rows may use "print, no URL" but should carry a verified/date stamp to clear the lint.
5. **Re-trace ALL tool facts** (rule keys, thresholds, OpenRewrite GAV/recipe id, EJ items, JEP numbers) after `/pin-source`, per B1.

## VERIFY gate-specific checks
- [x] Every specific fact traces to an authority OR is marked `⚠ UNVERIFIED`/`⚠ verify at pin` (65 unverified markers; flag filed).
- [x] No folklore stated as fact (cross-checked folklore list).
- [x] No off-pin / ahead-of-pin JEP/version claim (matches key-13 verified list).
- [x] Neutrality: no crowning, no banned phrasings, balanced.
- [~] Synthesized/causal/comparative claims supported — one ("strong evidence") needs softening (fix #1 reasoning).
- [x] HONEST-LIMITATIONS floor met (hardest-objection + when-NOT per option/class).
- [ ] Pinned-source trace COMPLETE — blocked by B1 (no pin on disk).

## Learnings & pipeline suggestions
- The whole pipeline is gated on B1: until `/pin-source` runs, EVERY chapter's SOURCE-VERIFY can only confirm internal consistency + flag discipline, never trace-to-pin. Recommend prioritizing `/pin-source` before more dossiers bank.
- **New reusable check:** quoted-span de-duplication — when the same external quote appears 3× in a dossier, lint for spelling drift (here OpenRewrite). Propose adding to `lint_citations.sh`. → append to PIPELINE-LEARNINGS.
- The "(confirmed)" annotation habit when the source was a live/unpinned page is a recurring trap (cf. keys 07/12/13/14 Sonar-page notes). Recommend a rule: the word "confirmed" in a dossier table requires a pinned identifier, else use "live-line, verify at pin."
