# GATE REPORT — SOURCE-VERIFY (key 40, annotation processors & the Lombok debate)

- **Gate:** SOURCE-VERIFY (pipeline step 2, pre-pin)
- **Artifact:** `02-research/40_annotation_processors_lombok/40_annotation_processors_lombok_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** — 0 blockers
- **Pre-pin caveat (keys 11–36 pattern):** SOURCE-PIN is multi-authority with every row `TO-PIN`,
  `{URL}` placeholder, `/pin-source` never run. `check_source_pin.sh` and `verify_sources.sh` FAIL
  **by construction** (no clone). A PASS_WITH_FLAGS here means **"the right atoms are flagged,"** NOT
  "atoms byte-verified." Atom re-trace MUST run after `/pin-source`.

## Scripts run vs manual
| Script | Result |
|---|---|
| `check_source_pin.sh` | FAIL by construction — pin ABSENT/unhealable (multi-authority, `{URL}`, all `TO-PIN`) |
| `verify_sources.sh` | FAIL by construction — "pinned clone absent; run check_source_pin first" |
| `lint_citations.sh` | 19 structural "violations" — ALL the known false-positive class (URLs live inside table cells; `☐`-status rows lack a date-verified marker). DOI/author-year residue: clean. |
| `check_neutrality.sh` | PASS — blocklist clean. Advisory FLAGs: 1 filler word; em-dash 18/1000 (→ CLARITY/draft, not VERIFY). |
| banned-phrase grep | clean (no better-than / unlike / superior / beats / outperforms / problem-with / obvious-choice) |
| `check_snippets.sh` | N/A — dossier (step 2), no `<!-- include -->` markers yet. |

## Checked claims
| # | Claim | Location | Result |
|---|---|---|---|
| 1 | JEP 395 records = final @ JDK 16 | §1/§2.6/§3/§5/§8 | ✅ matches verified JEP list (`records=16`); text `☐` (403) deferred |
| 2 | JEP 440 record patterns = final @ JDK 21 | §5 | ✅ matches verified list (`record-patterns=21(440)`) |
| 3 | "records make immutability obsolete" over-claim | §4 record objection | ✅ folklore stated-and-corrected per PIPELINE-LEARNINGS, not asserted |
| 4 | JSR 269 = Java 6; `apt` superseded, "do not cite as current" | §1/§5 | ✅ identity; no FindBugs/`apt`-as-current folklore |
| 5 | SE 21 verbatim roles (Filer "creation of new files"; RoundEnvironment "query … about a round") | §1/§2.1/§2.6 | ✅ consistent spelling, marked verbatim/live-line; byte-recheck at pin |
| 6 | Lombok internals (AnnotationProcessorHider / ShadowClassLoader `.SCL.lombok` / LombokProcessor / HandlerLibrary / @HandlerPriority / forced-round dummy-file + Filer patch) | §2.3/§2.6 | ✅ identity from execution-path doc, tool-version `⚠ at pin` |
| 7 | JDK-16 `jdk.compiler` non-export of `com.sun.tools.javac.processing`; `--add-opens` set | §2.4/§4/§8#9 | ⚠ verify-at-pin (Lombok issues #2681/#3719 framing) — correctly flagged |
| 8 | `annotationProcessorPaths` mandatory @ JDK 23 | §2.4/§5/§2.6 | ⚠ verify-at-pin (framing) — correctly flagged, not asserted as quoted spec |
| 9 | javac `-proc:none/-proc:only/-proc:full` default change @ JDK 21 | §2.6/§5 | ⚠ verify-at-pin — "do not assert JDK number from memory" honored |
| 10 | JaCoCo ≥ 0.8.1 / Lombok ≥ 1.16.20 + `@lombok.Generated` exclusion | §2.4/§3/§7 | ⚠ verify-at-pin (version thresholds), identity OK |
| 11 | Lombok/AutoValue/Immutables/MapStruct GAVs + versions | throughout | ⚠ UNVERIFIED — not SOURCE-PIN rows; flagged (material) |
| 12 | NEUTRALITY: records-vs-Lombok | §4 Obj 3 / Learnings | ✅ "different approaches, no crowning"; analyzer verdict routed to key 37 |
| 13 | HONEST-LIMITATIONS floor | §4 | ✅ every approach: hardest objection + when-NOT-to-use; shared-limits block |

## Findings (all minor / draft-fix — none blocking)
| ID | Finding | Location | Fix |
|---|---|---|---|
| F1 | Quote-drift: "generate source from source … an entirely new class … with its own name" (§2.2) vs "… with its own name" (§3) — two spellings of one quoted span (key-19/25 trap) | L165 vs L278 | Make one byte-exact at pin or demote to paraphrase |
| F2 | Paraphrase-inside-quotes risk: "never loads Lombok's processor at all" attributed as verified framing | §2.4 L202 | Confirm verbatim from Lombok setup page or demote to paraphrase at draft |
| F3 | `lint_citations` 19 violations = known false-positive class (table-cell URLs / `☐` rows) | §8 | Cosmetic; revisit when script handles table rows |
| F4 | em-dash density 18/1000 over ceiling | whole | → CLARITY/draft lane, not VERIFY |
| F5 | DEMO-CATALOG + SOURCE-PIN rows for Lombok/AutoValue/Immutables/MapStruct missing | §7 | Owned by /pin-source + catalog owner; both flags filed |

## Blockers
None.

## VERIFY gate-specific checks
- [x] Every specific atom traces to source OR is marked `⚠ verify at pin` / `⚠ UNVERIFIED` / `⚠ AHEAD-OF-PIN`
- [x] No folklore-as-fact (records-obsolete guard invoked; no FindBugs/apt-as-current)
- [x] No off-pin / SNAPSHOT / version-newer-than-pin asserted (all versions `TO-PIN`/hedged)
- [x] NEUTRALITY: no crowning, blocklist clean, comparisons balanced + each side cited, cross-cutting verdict routed to key 37
- [x] Synthesized/comparative claims supported (the contract-relation axis; each tool's strongest case + limit)
- [x] HONEST-LIMITATIONS floor met (each approach: hardest objection + when-NOT-to-use)
- [x] Both required flag files present & accurate (`40_lombok_and_codegen_tools_not_pinned.md`, `40_jdk_internal_api_and_processing_defaults_unverified.md`)

## Learnings & pipeline suggestions
- The "generate source from source …" span is a new instance of the recurring quote-drift trap (keys 19/25/20).
  Reinforces the proposed `lint_citations.sh` same-quote-spelling check: any span repeated with `verbatim`/
  `verified framing` markers must be byte-identical across §-occurrences or demoted to paraphrase.
- Pre-pin SOURCE-VERIFY remains a flag-discipline audit, not atom verification (key-12 lesson). Dossier 40
  did it correctly: identity verified from live docs, every version/GAV/JDK-boundary `⚠ verify at pin` + filed.
- "Verified framing" in quote marks (F2 "never loads … at all") is the sibling of the (verified)-without-pin
  overclaim — a quoted span needs a verbatim source, not framing. Propose: any double-quoted span tagged
  "framing" must be either un-quoted (paraphrase) or upgraded to a verbatim source at draft.
