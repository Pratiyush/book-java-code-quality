# GATE REPORT — SOURCE-VERIFY (key 33, ArchUnit)

- **Gate:** SOURCE-VERIFY (pipeline step 2)
- **Artifact:** `02-research/33_archunit/33_archunit_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** (0 blockers)

> **Pre-pin caveat (keys 11/12/13/20/22/23/25 lesson).** `SOURCE-PIN.md` is multi-authority with the
> ArchUnit row `TO-PIN`; the clone is ABSENT and **unhealable** (`{URL}` placeholder, `/pin-source` never
> run). `check_source_pin.sh` FAILs by construction; `verify_sources.sh` cannot trace. This gate is a
> **flag-discipline audit**, NOT atom byte-verification. A PASS_WITH_FLAGS here means "flagged the right
> things," not "atoms verified." Atom re-trace MUST happen after `/pin-source`.

## Scripts run

| Script | Result |
|---|---|
| `check_source_pin.sh` | FAIL — pin ABSENT/unhealable (multi-authority, `{URL}`). Expected pre-pin. |
| `verify_sources.sh` | FAIL — cannot trace without clone. Expected pre-pin. |
| `lint_citations.sh` | 9 violations — ALL known bare-domain false positives (URLs present as plain text in the source column; script wants a different column shape). Two-tier structure OK; snippets ≤9 lines OK; no hidden links. |
| `check_neutrality.sh` | PASS — blocklist clean. Advisory FLAGs: em-dash density 20/1000, 1 filler word (AUDIT-gate concerns, not VERIFY). |
| `check_snippets.sh` | n/a — dossier (step 2), no `<!-- include -->` markers. |

## Checked claims / issues

| # | Claim / atom | Check | Result |
|---|---|---|---|
| 1 | GAV `com.tngtech.archunit:archunit` / `:archunit-junit5`, live-line **1.4.2** | Every mention framed "live-line, NOT the pin," `⚠ verify at pin`; never asserted as fact | PASS |
| 2 | API identity (`ArchRuleDefinition`, `ClassFileImporter`, `JavaClasses`, `Architectures.layered/onionArchitecture`, `SlicesRuleDefinition`, `@AnalyzeClasses`/`@ArchTest`, `FreezingArchRule`) | Identity-only, sourced to user guide; defaults/version marked `verify at pin` | PASS (byte-verify @pin) |
| 3 | `GeneralCodingRules` 5 constants by name | Marked "re-confirm full set at pin" (§7) — never invented | PASS (re-confirm @pin) |
| 4 | `archunit.properties` `cycles.maxNumberToDetect=100` / `maxNumberOfDependenciesPerEdge=20` | Defaults `⚠ verify at pin` everywhere they appear (§2.4, §2.7, §4, §7) | PASS (byte-verify @pin) |
| 5 | JDK / class-file compatibility window | Repo did not state a floor → `⚠ UNVERIFIED`, §7 + flag filed | PASS (honest gap) |
| 6 | Folklore (FindBugs-as-current, 10×/100×, MI-as-score, coverage-as-quality) | None present | PASS |
| 7 | Off-pin / moving-target cites (SNAPSHOT/main/newer-than-pin) | None; 1.4.2 explicitly NOT asserted as pin | PASS |
| 8 | NEUTRALITY — crowning / banned phrasings | Blocklist clean; "none is crowned" stated; jQAssistant/JDepend/JPMS framed as different approaches | PASS |
| 9 | Cross-tool claims carry own-source + verdict routing | jQAssistant (Neo4j/Cypher), JDepend (metrics), JPMS (Bucket i, JLS/JEP) each told to cite own source; layering verdict routed to key 37 (not asserted) | PASS |
| 10 | Synthesized claims ("bytecode-only → reflection/DI edges invisible"; "import is the expensive step") | Trace to the verified mechanism (model built from `JavaMethodCall`/`JavaFieldAccess`; caching note) — supported as stated | PASS |
| 11 | HONEST-LIMITATIONS floor | Hardest objection ("only catches what you encode") + bytecode blind spots + scoping surprises + when-NOT-to-use (JPMS/jQAssistant/JDepend) + process cost — floor met | PASS |
| 12 | CANDIDATE_POOL row 33 has no `⚠` glyph | Confirmed; dossier states this accurately AND applies full neutrality anyway | PASS |
| 13 | Filed flag `09-flags/33_archunit_version_and_jdk_unverified.md` | Present, accurate, matches §7 | PASS |

## Findings (non-blocking, draft fixes)

- **F1 (quote-drift — key-19/20 trap).** §1 carries TWO quoted self-descriptions ("A Java architecture
  test library, to specify and assert architecture rules in plain Java" / "a free, simple and extensible
  library for checking the architecture of your Java code"). Plus several `(verbatim …)` user-guide spans
  (import-options, two-dots, JUnit-support, freezing). NONE is byte-verifiable now (pin absent). Fix: at
  draft, byte-verify each against the pinned user guide/repo; one tagline wording must be canonical.
- **F2 (extra named tools).** "Spring Modulith / Deptective family" named once (§Topic) without a source row.
  If kept in the draft, each needs its own pinned cite or must be cut (NEUTRALITY cited-source rule); verdict
  stays in key 37. Currently scoped-out (routed), so acceptable in the dossier.
- **F3 (catalog gap).** No `33_archunit` row in `DEMO-CATALOG.md` — flagged in §7; backfill.
- **F4 (lint false positives).** 9 `lint_citations` violations are all bare-domain/column-shape false
  positives recurring since key 07; not real defects. Candidate for the lint fix already proposed.

## Blockers
None.

## Learnings & pipeline suggestions
- Reconfirms the pre-pin SOURCE-VERIFY posture (keys 11/12/13/20/22/23/25): pin unhealable → flag-discipline
  audit only; reserve true ☑/@pin for post-`/pin-source`. The dossier's §8 header already states this correctly.
- The quote-drift lint (proposed at key 19) would have caught F1 mechanically — the dual self-description
  spelling is exactly its target. Reinforce the proposal.
- Version-vs-identity granularity (API identity citeable now; GAV version + `cycles.*` defaults `verify at
  pin`) is applied cleanly — standard for Part-IV tool chapters.
