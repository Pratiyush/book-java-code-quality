# GATE REPORT — SOURCE-VERIFY (key 35, Sonar quality platform & rule engine)

- **Gate:** SOURCE-VERIFY (pipeline step 2) · **Artifact:** `02-research/35_sonarqube/35_sonarqube_RESEARCH.md`
- **Date:** 2026-06-15 · **Verdict:** **PASS_WITH_FLAGS** · **Blockers:** 0
- **Pin state:** ABSENT + UNHEALABLE by construction — SOURCE-PIN is multi-authority, every Sonar row
  `TO-PIN`, repo `{URL}` placeholder, `/pin-source` never run. So this is a **flag-discipline audit**, NOT
  atom byte-verification (same caveat as keys 11/12/13/19/20/22/23/25/30). Atom re-trace MUST happen after
  `/pin-source`. A pre-pin PASS_WITH_FLAGS means "flagged the right things," not "atoms verified."

## Scripts run (vs manual)
| Script | Result |
|---|---|
| `check_source_pin.sh` | FAIL by construction (clone absent, multi-authority, no single tag) |
| `verify_sources.sh` | FAIL by construction (no clone to trace against) |
| `lint_citations.sh` | 16 "violations" — ALL the known false-positive class (elided `…` doc URLs + `☑/☐` status markers, not date stamps). Not real. |
| `check_neutrality.sh` | **PASS** — blocklist clean. Advisory FLAGs only: 4 filler words, em-dash 18/1000 (→ CLARITY/AUDIT, not VERIFY). |
| `check_snippets.sh` | n/a — dossier (step 2), no `<!-- include -->` markers yet. |

## Checked claims / atoms
| # | Claim / atom | Location | Status |
|---|---|---|---|
| 1 | Oct-2024 product rename (Server/Cloud/for IDE/Community Build; formerly SonarQube/SonarCloud/SonarLint/Community Edition) | §1, §5, src 8 | OK — cited to Sonar's own press release; legacy names ALWAYS tagged "formerly"; no folklore |
| 2 | `java:` rule-key prefix; `java:S2077` documented example | §1, §2.1, §2.7 | OK — identity citeable; example is the doc's own; other keys (S106/S1192/S1118/S2259) "exist, defaults `⚠ verify at pin`" — correct identity/default split |
| 3 | `sonar.java.binaries`/`.libraries`/`.test.*` verbatim ("required", "analysis will fail") | §1, §2.1, §2.7 | OK — quoted as Java-analysis doc; demote body "(verified verbatim)" → "live-line" (F1) |
| 4 | MQR severities (Blocker/High/Medium/Low/Info) vs Standard (Blocker/Critical/Major/Minor/Info) | §2.2 | OK — both stated, correctly distinguished by mode |
| 5 | 14 Clean Code attributes verbatim (FORMATTED…RESPECTFUL); 4 categories | §2.2, §7 | OK — list verbatim; attribute→category mapping `⚠ verify at pin` (clean-code page 404'd) + flagged |
| 6 | Issue types "de-emphasized/reorganized, NOT removed" (Standard Experience retains) | §2.2, §5 | OK — explicitly guards the "deprecated≠removed" trap; flag filed |
| 7 | "Sonar way" profile/gate built-in/default/read-only; Clean as You Code ("issues > 0" fails; hotspots 100%) | §2.3/2.4, §3 | OK — quoted to quality-gates/profiles docs |
| 8 | SQALE/debt: tech-debt=sum remediation min; `sqale_debt_ratio` (÷30 min/line × LOC); rating grid A=0-0.05…E=0.51-1 | §2.5, §2.7 | OK — quoted; grids/defaults `⚠ verify at pin`; folklore guard (key-04) applied (coarse trend, not precise) |
| 9 | Taint/deeper SAST = "Developer Edition 9.9 LTS or higher … no additional cost"; not in Community Build | §2.1, §3, §4 | OK structurally; specific edition+version quote = doubtful-but-undisprovable (no web) → **recommend web re-check at draft**; matrix `⚠ verify at pin` + flagged |
| 10 | Analyzer "requires Java 17 to run … as of version 7.31" | §2.1 | Version-paired runtime atom; marked `⚠ verify at pin` → **recommend web re-check at draft**, not failed |
| 11 | Symbolic-execution / data-flow engine, path-sensitive, cross-procedural | §2.1, §3 | OK — quoted to Java-analysis doc |
| 12 | Security hotspots ("no severity until reviewed"; ">80% … resolved as reviewed") | §2.2 | OK — verbatim quote; teaching point |
| 13 | Scanners: `org.sonarsource.scanner.maven:sonar-maven-plugin` (`sonar:sonar`), `org.sonarqube` Gradle, CLI | §1, §2.7, §3 | OK — names cited; GAV/versions `⚠ verify at pin` |
| 14 | Cross-tool overlap/redundancy (Checkstyle/PMD/SpotBugs/Error Prone) | §1, §2.1, §4 | OK — framed as different *roles/approaches*, NO crowning, every cross-tool fact cite-deferred to that tool's source, verdict routed to **key 37** (11×) |
| 15 | "600+ Java rules" / "6,000+ rules" / "20+ languages" | §1, §3, §7, src A2 | OK — explicitly "corroboration only, not asserted", `⚠ verify at pin` |
| 16 | AI CodeFix / IDE 2024+ features | §5 | OK — "direction only, never anchor fact", `⚠ verify at pin` |

## Floor checks
- [x] **No folklore-as-fact** — no FindBugs-as-current; SQALE/debt-as-precise-score explicitly guarded (key-04); "600+ rules" demoted to corroboration.
- [x] **No off-pin/moving-target citation** — no SNAPSHOT/main; no version asserted (all `TO-PIN`); LTA-line + analyzer version deferred to pin.
- [x] **No legacy product name asserted as current** — SonarLint/SonarCloud only ever "formerly".
- [x] **NEUTRALITY** — blocklist clean; no "best tool" crowned; ⚠ comparison balanced, each side cite-deferred to its own source; cross-cutting verdict routed to key 37 (not asserted here).
- [x] **Synthesized/comparative claims supported** — "platform = rule engine + layer above" framing; the "distinctive value vs bare analyzer" claim is about the persistence/trend/gate *layer* (cited), not a quality crowning.
- [x] **HONEST-LIMITATIONS floor met** — §4 carries Sonar's hardest objections (paid-edition SAST gating; needs compiled bytecode+classpath; FP/triage burden + hotspot review; metric/rating opacity; server ops cost) AND an explicit "When NOT to reach for Sonar" + shared-limits-of-static-analysis centre.
- [x] **Required flag files present & accurate** — `09-flags/35_sonar_versions_and_defaults_unverified.md`, `09-flags/35_clean_code_taxonomy_and_issuetype_status_unverified.md`.

## Findings (all non-blocking, draft-fix)
- **F1 (recurring overclaim):** 46 body "(verified)" / "(verified verbatim)" tokens read as pin-verified though NO pin exists; §8 correctly demotes all rows to "☑ live-line, verify at pin." Demote body wording to "live-line, verify at pin." (Same class as keys 07/10/11/13/15/19/25/30.)
- **F2 (no-web, recommend re-check):** the "Developer Edition 9.9 LTS or higher" edition+version quote and the analyzer "requires Java 17 … as of version 7.31" runtime atom are doubtful-but-undisprovable here — web re-check at draft (both already `⚠ verify at pin` + flagged). Not failed.
- **F3 (lint false positives):** 16 `lint_citations.sh` "violations" = elided-URL + status-marker class; no action.

## Verdict
**PASS_WITH_FLAGS — 0 blockers.** No invented or unflagged atom; no folklore-as-fact; no off-pin/legacy-name
assertion; neutrality blocklist clean with the cross-cutting verdict correctly routed to key 37;
HONEST-LIMITATIONS floor met. All version-sensitive atoms (severities, profile membership, SQALE grids,
GAVs, edition gating, counts) marked `⚠ verify at pin` and filed. Atom byte-verification DEFERRED to
`/pin-source`; re-run this gate after pinning.

## Learnings & pipeline suggestions
- The `(verified)/(confirmed)-without-pin` body overclaim recurs AGAIN (key 35). Strong candidate to promote a
  `lint_citations.sh` rule: body "verified/confirmed" requires a pinned identifier; pre-pin → "live-line."
- The "deprecated ≠ removed" precision (issue types reorganized, not removed) and the "product-rename atom"
  (verify product names against the vendor's rename announcement) are durable, reusable traps — already logged
  in the dossier's own Learnings; reinforce in SOURCE-PIN never-invent emphasis (extend "no ID from memory" to
  product names).
