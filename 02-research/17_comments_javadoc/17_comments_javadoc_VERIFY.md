# GATE REPORT — SOURCE-VERIFY — key 17 (Comments, Javadoc & self-documenting code)

- **Gate:** VERIFY (Step 2 source-verify)
- **Artifact:** `02-research/17_comments_javadoc/17_comments_javadoc_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** PASS_WITH_FLAGS

## Scripts run vs manual
| Check | How | Result |
|---|---|---|
| `check_source_pin.sh` / `ensure_source_pin.sh --heal` | script | Pinned clone ABSENT; heal failed — SOURCE-PIN is all `TO-PIN`, repo URL is `{URL}`, `/pin-source` has never run. Live machine-tracing of facts not possible this session (known/expected; dossier marks version+threshold facts `⚠ verify at pin`). |
| `verify_sources.sh` | script | Could not run (pinned clone absent — by design). |
| `lint_citations.sh` | script | 7 structural violations — all benign (see findings F4). |
| `check_neutrality.sh` | script | Blocklist CLEAN. Advisory FLAGs: em-dash density 18/1000, 1 filler word (AUDIT lane, not VERIFY). |
| `check_snippets.sh` | script | 0 include markers (companion module not yet built — correct for a research dossier). |
| Adversarial fact/folklore/neutrality read | manual | See findings. |

## Checked claims
| Claim | Status |
|---|---|
| `{@snippet}` = JEP 413, GA JDK 18 | OK (≤ anchor; corroborated by PIPELINE-LEARNINGS key 17/13) |
| `///` Markdown = JEP 467, JDK 23 | OK + correctly `⚠ AHEAD-OF-PIN`, consistent in all 7 occurrences; flag filed |
| `{@return}` inline JDK 16 / `{@summary}` JDK 10 / `{@index}` JDK 9 / `{@systemProperty}` JDK 12 / `@spec` JDK 20 | Match Java history; ≤ anchor; sourced to spec per-tag notes — but NOT in §7 re-confirm queue (F2) |
| `@apiNote`/`@implSpec`/`@implNote` JDK 8 via JEP 8068562 | OK — correctly flagged JEP # is a DRAFT; dossier defers to the JDK 21 spec where tags appear |
| DocLint default-on since JDK 8; `-Xdoclint` categories | OK (accessibility/html/missing/reference/syntax) |
| `<doclint>all,-missing` middle path, maven-javadoc-plugin ≥ 3.0.0 | OK; exact plugin version `TO-PIN` |
| Effective Java Item 56 = doc-comments-for-exposed-API | OK; verbatim quotes marked `⚠ verify at draft` |
| CommonMark 0.31.2 (JEP 467) | OK |
| Tool rule names (Checkstyle `MissingJavadocMethod`/`JavadocMethod`/`SummaryJavadoc`; PMD `CommentRequired`/`CommentSize`; Sonar `java:S125`) | Names accepted; defaults/thresholds/version `⚠ verify at pin` + flag filed — correct granularity for a `TO-PIN` tool row |
| Folklore (10x/100x, MI-as-score, coverage=quality, reified generics) | None present — clean |
| Neutrality (two schools, no crown, qntm as named critique) | Compliant — each school cited to its own source; HONEST-LIMITATIONS floor met (each school has hardest-objection + when-NOT-to-use) |
| Synthesized/comparative claims (snippet=anti-drift; over-enforcement→vacuous comments; S125 false positives) | Each traces to a cited source (JEP 413 / Item 56 + DocLint behavior / S125 documented FP); no unsupported inference crowned |

## Findings (each with a fix)
| # | Location | Finding | Fix |
|---|---|---|---|
| F1 | environment | Pinned source clone absent and SOURCE-PIN is entirely `TO-PIN` (no repo URL) — no fact could be machine-traced this session. | Not a dossier defect. Run `/pin-source`, then re-run VERIFY before AUDIT/draft. All version+threshold facts already carry `⚠ verify at pin`. |
| F2 | §2.3 inline-tag table; §5 | Inline-tag "since" levels (JDK 9/10/12/16) and `@spec` (JDK 20) are stated bare (no per-item `⚠`) and are NOT in the §7 re-confirm queue (which lists only `{@snippet}` and `///`). Values match Java history but were unconfirmable this session. | Add these "since" levels to the §7 verify-at-draft queue; re-confirm against the JDK 21 doc-comment spec's per-tag notes once the pin clone exists. Recommend web/spec re-check at draft. |
| F3 | §2.2; §7; Sources #6 | `@apiNote`/`@implSpec`/`@implNote` rest on JEP **draft** 8068562. | Already handled — dossier flags the draft # and defers to the JDK 21 spec. No action beyond confirming the tags in the spec at pin. |
| F4 | §8 Sources rows 3,6,7,8,9,13 | `lint_citations.sh`: 7 structural violations — `⚠`-status rows lack a date-verified token; print book-canon rows (EJ/Clean Code/APoSD) have no URL. | Benign: print books legitimately have no URL; `⚠` rows are intentional. Optional: add a "verified 2026-06-15" stamp to status cells to satisfy the linter. Not blocking. |
| F5 | SOURCE-PIN §7 | *A Philosophy of Software Design* (Ousterhout) is cited as School B but is NOT a pinned canon row. | Already logged as a standing OPEN ITEM in PIPELINE-LEARNINGS + dossier §7/§9. Add the canon row at `/pin-source`. Not blocking this dossier. |

## Blockers
None. (F1 is an environment/pin-step prerequisite, not an invented/unflagged fact.)

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority OR is marked `⚠`/AHEAD-OF-PIN (29 `⚠` markers).
- [x] No folklore stated as fact (cross-checked the Folklore list).
- [x] No off-pin / SNAPSHOT / newer-than-pin citations; the one past-anchor feature (JEP 467/JDK 23) is `⚠ AHEAD-OF-PIN` throughout.
- [x] Neutrality: no winner crowned, no banned phrasing, contested topic balanced with each side cited to its own source.
- [x] Synthesized/causal/comparative claims trace to a source that makes the claim.
- [x] HONEST-LIMITATIONS floor met (each school: hardest objection + when-NOT-to-use).
- [ ] Live machine-trace against pinned clone — NOT possible this session (pin step pending); deferred to post-`/pin-source` re-verify.

## Learnings & pipeline suggestions
- VERIFY cannot machine-trace anything until `/pin-source` runs; for `TO-PIN`-era dossiers the gate's real job is auditing flag/`⚠` discipline, folklore, neutrality, and AHEAD-OF-PIN labelling — all of which this dossier passes.
- Reusable VERIFY rule candidate: for language-feature chapters, every per-tag/per-feature "since JDK N" level (not just the headline feature) belongs in the §7 re-confirm queue, since these are exactly the never-invent atoms (version numbers) and are easy to recall-from-memory wrong. (F2.)
- `lint_citations.sh` flags print book-canon rows and `⚠`-status rows as violations; consider teaching it to accept "print" sources and `⚠`/AHEAD-OF-PIN status cells so the signal isn't noisy.
