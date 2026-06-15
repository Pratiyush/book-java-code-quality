# GATE REPORT — SOURCE-VERIFY (key 08, Effective Java)

- **Gate:** VERIFY (Step-2 source-verify)
- **Artifact:** 02-research/08_effective_java/08_effective_java_RESEARCH.md
- **Date:** 2026-06-15
- **Verdict:** PASS_WITH_FLAGS

## Scripts run vs manual
| Script | Status | Note |
|---|---|---|
| check_source_pin.sh | RAN — FAIL (expected) | Multi-authority pin; all rows TO-PIN, ephemeral clone absent. Per SOURCE-PIN, TO-PIN-row facts are `⚠ UNVERIFIED` by policy; dossier verifies against JEP/JLS primaries + named book, which is correct for this model. |
| verify_sources.sh | RAN — FAIL (expected) | Fails only on absent ephemeral clone (same cause as above). No fact-level failure. |
| lint_citations.sh | RAN — 13 violations | ALL are the same scheme-less-URL false-positive (linter wants `https://`; rows use `openjdk.org/jeps/395`, ISBN, etc.). Every row carries a resolvable URL/ISBN. Snippet length / two-tier / hidden-link / style checks all OK. |
| check_snippets.sh | N/A | No `<!-- include: -->` markers in a research dossier (correct). |
| Manual adversarial fact-trace | DONE (no web access) | JEP numbers, JDK levels, Item numbers/titles cross-checked from knowledge. |

## Checked claims / issues
| # | Claim in dossier | Check | Result |
|---|---|---|---|
| 1 | JEP 395 records final JDK 16 | knowledge | OK |
| 2 | JEP 409 sealed classes final JDK 17 | knowledge | OK |
| 3 | JEP 441 pattern matching for switch final JDK 21 | knowledge | OK |
| 4 | JEP 440 record patterns final JDK 21 | knowledge | OK |
| 5 | JEP 421 deprecate finalization for removal JDK 18 | knowledge | OK |
| 6 | JEP 444 virtual threads final JDK 21 | knowledge | OK (dossier itself marks `⚠ verify number @pin`) |
| 7 | JEP 505 structured concurrency preview JDK 25 | knowledge | OK — correctly marked `⚠ AHEAD-OF-PIN`; flag file present |
| 8 | JEP 525 sixth preview ahead of pin | knowledge | Plausible; correctly framed as ahead-of-pin |
| 9 | JEP 286 var JDK 10; JEP 378 text blocks JDK 15 | knowledge | OK |
| 10 | JEP 290 deserialization filters JDK 9 | knowledge | OK |
| 11 | Stream::toList & Stream.mapMulti JDK 16 | knowledge | OK |
| 12 | EJ 3e ISBN 9780134685991, 2018, Addison-Wesley, Bloch | knowledge | OK (page-code 9780134686097 is an O'Reilly/eISBN variant — low-risk, flagged for draft) |
| 13 | Item numbers/titles (1–9, 10–14, 15–25, 23, 20, 38, 49/50/54/55, 69–77, 78–84, 85–90) | knowledge | OK — number+title accurate; dossier marks 26–90 finer in-item detail `⚠` for draft |
| 14 | Folklore "records make Item 17 obsolete" | Folklore list | HANDLED — dossier never asserts it; explicitly flags it as over-claim (line 230), matching folklore list |
| 15 | Banned-phrase scan | NEUTRALITY blocklist | CLEAN — no banned phrasing; no tool crowned; not a comparison key |
| 16 | HONEST-LIMITATIONS floor | §4 | MET — hardest objection (2018 snapshot) + when-NOT-to-use (throwaway code, hot paths, records-vs-Item-17) present |
| 17 | "most-cited Java idiom reference" | source | Unsourced superlative about the book (not a rival) — recommend qualifying ("widely cited") at draft |

## Required fixes (draft stage — none blocking)
1. **(lint)** Add `https://` scheme to all §8 source URLs so lint_citations.sh passes; ISBN rows should also carry the publisher URL. Mechanical; 13 rows.
2. **(soft)** Qualify "most-cited Java idiom reference" or attribute it; currently an unsourced superlative.
3. **(carry-forward, already flagged in §7)** Verify at draft: JEP 444 number, no EJ 4e at pin date, in-item verbatim quotes word-for-word + page, ISBN/eISBN variant.
4. **Recommend web re-check at draft** for: JEP 525 sixth-preview existence/number; exact O'Reilly page-code 9780134686097.

## Blockers
None.

## VERIFY gate checks
- [x] Every specific fact traces to a primary (JEP/JLS) / named book, OR is marked `⚠ UNVERIFIED` / `⚠ AHEAD-OF-PIN`.
- [x] No folklore stated as fact (records-obsolete over-claim explicitly guarded).
- [x] No off-pin / moving-target citation; preview feature (JEP 505) marked AHEAD-OF-PIN with flag file.
- [x] Neutrality: no winner crowned, no banned phrasing; book named and dated, not ranked.
- [x] Synthesized/causal/comparative claims supported (records carry Item 17 boilerplate; virtual threads change Item 80 cost model — both traced to JEPs, principle-vs-boilerplate distinction correct).
- [x] HONEST-LIMITATIONS floor met.

## Learnings & pipeline suggestions
- lint_citations.sh emits false-positive "no plain-text URL" on scheme-less URLs (`openjdk.org/...`) and ISBN-only rows. Either relax the regex to accept `domain.tld/path` and ISBN refs, or make the house style require `https://`. Recommend documenting the chosen rule in GUIDELINES §5 so dossiers and the linter agree.
- Multi-authority TO-PIN model means check_source_pin.sh / verify_sources.sh always hard-FAIL on the ephemeral-clone path until /pin-source runs. Until then these scripts cannot fact-verify; the verifier must trace to JEP/JLS primaries manually. Worth a script mode that skips the clone check for primary-source-only dossiers.
