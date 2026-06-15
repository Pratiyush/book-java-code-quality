# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** VERIFY (source-verify)
- **Key:** 07 — Naming, structure & formatting — the readability fundamentals
- **Artifact:** 02-research/07_naming_structure_formatting/07_naming_structure_formatting_RESEARCH.md
- **Date:** 2026-06-15 · **Reviewer:** source-verifier (adversarial cold read)
- **Verdict:** **PASS_WITH_FLAGS**

## Scripts run vs manual
| Script | Result |
|---|---|
| check_source_pin.sh | FAIL — clone ABSENT (multi-authority pin not fetched; script assumes single repo/tag). Documented infra gap (PIPELINE-LEARNINGS OPEN ITEM, SOURCE-PIN all rows `TO-PIN`). NOT a dossier defect. |
| verify_sources.sh | Did not run — aborts because pinned clone absent (same gap). Fact-tracing performed MANUALLY against dossier's own `verified` / `⚠ verify at pin` markers. |
| lint_citations.sh | PASS — snippet length ok, two-tier sources present, plain-text URLs, no DOI residue. |
| check_snippets.sh | n/a — research dossier (no `<!-- include: -->` markers; applies at draft). |
| Neutrality banned-phrase scan (manual grep) | PASS — zero blocklist phrases. |

## Checked claims / issues
| # | Claim / atom | Location | Status | Note |
|---|---|---|---|---|
| 1 | Tool default regexes (Checkstyle MemberName/PackageName/RecordComponentName/PatternVariableName/AbbrevLen/LineLength; PMD ShortVariable/LongVariable; Sonar S100/101/115/116/117) | §2.4, §7 | OK — correctly `⚠ verify at pin` | All tool rows `TO-PIN`; flag 07_naming_defaults_unverified.md filed. Honest. |
| 2 | Checkstyle ConstantName `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, MethodName `^[a-z][a-zA-Z0-9]*$`; PMD classPattern/constantPattern/finalFieldPattern/testClassPattern | §2.4 | "verified" — plausible, recommend web re-check at draft | Matches known current docs; re-confirm at pinned version (defaults can move across majors). |
| 3 | Sonar rule IDs S100/S101/S115/S116/S117 map to method/class/constant/field/local | §2.4 | OK | IDs match conventions; defaults correctly deferred. No memory-guessed IDs (cf. key 18 trap). |
| 4 | google-java-format non-configurable, 2-space default / `--aosp` 4-space; palantir 120-char | §2.3, §2.4 | "verified" — accurate | Quoted design statement; re-confirm verbatim + GAV at pin. |
| 5 | Camel-case algorithm `XMLHTTPRequest → XmlHttpRequest` | §2.1 | "verified" — correct | Google §5.3 algorithm yields this. |
| 6 | Google Java Style §3 file order / §3.4.2 member order / §4 indent+2 / col 100 / K&R | §2.2-2.3 | "verified"; §-numbers `⚠ verify at pin` (moving web page) | Correct discipline — guide is a live page, snapshot queued. |
| 7 | JLS §6.1 naming-conventions wording + section number | §2.5, §7 | OK — correctly `⚠`, not asserted | Honors Durable principle #1 (spec-edition needs own text). |
| 8 | Unnamed variables `_` / JEP 456 / finalization JDK | §5, §7 | OK — correctly hedged `⚠`, not asserted as settled | Avoids AHEAD-OF-PIN/wrong-JDK trap. (Re-check: JEP number + JDK at draft.) |
| 9 | EJ Item 68 quotes "straightforward and largely unambiguous" / "more complex and looser" | §2.1, §3, §4 | "verified" scheme; verbatim+page `⚠` at draft | Canon quote; confirm character-for-character before block-quoting. |
| 10 | *Clean Code* read:write "well over 10 to 1" | §1 | OK — attributed to the book (canon, secondary), not asserted | Not the debunked 1:10:100 folklore; framed as the book's claim. |
| 11 | **Spotless ratchetFrom "~100x" faster** | §3, §9 | **FLAG** — labelled "verified"; a quantitative/comparative atom unconfirmed | Benchmark figure = never-invent atom (SOURCE-PIN). README (TO-PIN) unfetched; "~100x" is a characterization, not a confirmed documented figure. |
| 12 | PMD `GenericsNaming` deprecated | §5 | "verified" — recommend re-check at pin | Plausible; confirm at pinned PMD version. |
| 13 | Folklore cross-check (1:10:100, MI-as-score, coverage-as-quality, records-replace-immutability, Valhalla) | whole | PASS — none stated as fact | No folklore present. |
| 14 | Neutrality — no crowning, formatters "crown none", style values framed as cited choices | §2.3, §4, §7 | PASS | Banned-phrase scan clean; style-value landmine handled correctly. |
| 15 | HONEST-LIMITATIONS floor (hardest-objection + when-NOT per option) | §4 | PASS | Each layer/option has its limit + when-NOT-to-use. |

## Blockers
None. (No invented fact, no unflagged off-pin claim, no neutrality breach.)

## Required fixes (before draft)
1. **Finding 11 (minor):** Downgrade the Spotless "~100x" from "verified" to `⚠ verify at pin`, OR replace with the exact Spotless README wording quoted verbatim at the pinned version. A performance/comparative figure is a never-invent atom; do not carry it as "verified" while the Spotless row is `TO-PIN`.
2. **At /pin-source (already queued in flag 07_naming_defaults_unverified.md):** resolve all `⚠ verify at pin` default regexes/thresholds (Checkstyle/PMD/Sonar), the JLS §6.1 wording+number, the JEP 456 number+JDK, and EJ Item 68 verbatim quotes before the chapter drafts.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority/primary OR is marked `⚠ verify at pin` / `⚠ UNVERIFIED` (one exception → Finding 11, minor fix).
- [x] No folklore stated as fact (Folklore list cross-checked).
- [x] No off-pin / moving-target citations (no SNAPSHOT/main; JEP 456 + JLS edition correctly hedged).
- [x] Neutrality — no winner crowned, no banned phrasings, style values cited not asserted.
- [x] Synthesized/causal/comparative claims supported (except the "~100x" figure → Finding 11).
- [x] HONEST-LIMITATIONS floor met (hardest-objection + when-NOT per option).
- [x] Cross-tool claims each carry the named tool's own source (§2.4 per-row sources; cross-tool note states each from own source).

## Learnings & pipeline suggestions
1. **"verified" must not cover quantitative/comparative atoms while the source row is `TO-PIN`.** The "~100x" case shows a qualitative "feature exists" verification can silently extend to a number. Propose: at SOURCE-VERIFY, treat any figure/benchmark/percentage labelled "verified" against an unpinned (`TO-PIN`) source as a finding by default.
2. **Multi-authority pin breaks `check_source_pin.sh` / `verify_sources.sh`** (single-repo assumption) — already an OPEN ITEM; until adapted, source-pin scripts cannot gate this book and verification is manual. Reiterate as blocking infra for runnable `/stage-report`.
