# SOURCE-VERIFY GATE REPORT — key 26 (How static analysis works)

**Gate:** SOURCE-VERIFY (step 2) · **Date:** 2026-06-15 · **Artifact:** `02-research/26_how_static_analysis_works/26_how_static_analysis_works_RESEARCH.md`
**Verdict:** **PASS_WITH_FLAGS** · **Blockers:** 0

## Scripts run vs manual
| Check | How | Result |
|---|---|---|
| `check_source_pin.sh` | ran | FAIL **by construction** — multi-authority pin, `{URL}` placeholder, all §2 rows `TO-PIN`, ephemeral clone absent. Not healable (`/pin-source` not yet run). Same as keys 12/19/20/22/23/25. |
| `verify_sources.sh` | ran | Cannot trace (clone absent) — atom byte-verification DEFERRED to `/pin-source`. |
| `lint_citations.sh` | ran | 24 violations = the **known** bare-domain / `☐`-status / orientation-row false positives (no plain URL + no date marker on every §8 row). Not real defects; same class as keys 20/22/23/25. |
| Neutrality blocklist scan | grep + cold read | CLEAN (see below). |
| Flag-discipline / content audit | manual cold+adversarial read | See findings. |

> **Pre-pin caveat (per PIPELINE-LEARNINGS, keys 12/22/23/25):** a pre-pin PASS_WITH_FLAGS means "the right
> things are flagged," NOT "atoms are byte-verified." Verbatim quotes, SpotBugs API package paths, GAVs,
> versions, default-ruleset membership and the undecidability primary citation MUST be re-traced after `/pin-source`.

## Checked claims / issues
| # | Claim / atom checked | Location | Status |
|---|---|---|---|
| 1 | FindBugs-as-current folklore | §1, §2.3, §2.7, §5, §7 | OK — explicitly "FindBugs is dead → SpotBugs"; never cited as current. Folklore guard met. |
| 2 | Neutrality — no crowning, no banned phrase | whole | OK — 5 "wins/crowned" hits are all the routing rule itself ("which tool wins → routed to key 37", "none is crowned"). No winner asserted. |
| 3 | Cross-tool "which to choose" verdict | §0, §2.5, §4, §7, §8 | OK — routed to **key 37** everywhere; not asserted here. |
| 4 | Verbatim doc quotes (PMD AST/DFA, Error Prone, CodeQL data-flow + taint, Semgrep IL + "No soundness guarantees", Checker FW soundness, SonarQube FP/Won't fix) | §2.x, §3, §4, §8 | Flagged `⚠ live-line, verify at pin` (§7 + `26_tool_versions_...md`). Plausible; byte re-check deferred. |
| 5 | SpotBugs `OpcodeStackDetector` package path `edu.umd.cs.findbugs.bcel.OpcodeStackDetector` | §8 row 3 | **DOUBTFUL atom** — the `.bcel.` segment is suspect (class historically sits under `edu.umd.cs.findbugs`). Already marked `⚠ verify at pin` (§7, flag). No web here → **recommend web re-check at draft**, not a fail. |
| 6 | Semgrep OSS intraprocedural-only vs Pro interprocedural | §2.4, §4, §7 | OK — OSS/Pro boundary correctly flagged `⚠ verify at pin`; not conflated. |
| 7 | Undecidability / Rice's theorem / sound∧complete impossible | §1, §2.6, §4 | OK — marked `⚠ UNVERIFIED`, dedicated flag `26_undecidability_..._md`; theorem NOT cited to a tool; Checker FW used only as design-choice illustration. |
| 8 | Version / GAV / "supports Java 25" assertions | scan | OK — no concrete version/GAV printed; §5 explicitly forbids asserting "supports Java 25" without the tool's docs. |
| 9 | Rule-IDs from memory | scan | OK — none; technique chapter, only API class names + verbatim quotes, all flagged. |
| 10 | HONEST-LIMITATIONS floor (each technique: hardest objection + when-NOT-to-use) | §4 | MET — AST/pattern, intraproc DFA, interproc/taint, sound checkers, structural FP problem; crowns none. |
| 11 | Both required flag files present + accurate | `09-flags/` | OK — `26_tool_versions_and_defaults_unverified.md`, `26_undecidability_primary_citation_unverified.md`; both consistent with dossier. |

## Required fixes (carry to draft — none blocking)
1. **F1 (web re-check):** Confirm SpotBugs `OpcodeStackDetector` / `BytecodeScanningDetector` exact package path at pinned version (suspect `.bcel.` segment in §8 row 3).
2. **F2 (atom):** Re-trace all verbatim quotes byte-identical at `/pin-source`; never upgrade `☐`/`verify at pin` to "(confirmed)" without a pin (recurring keys 19/22/25 rule).
3. **F3 (atom):** Fix a **primary** PL/compilers text for Rice's theorem before the undecidability claim ships as fact; keep `⚠ UNVERIFIED` until then.
4. **F4 (atom):** Confirm SonarQube resolution label ("Won't fix" may be "Accept") + "Administer Issues" permission at pinned server version.
5. **F5 (atom):** Confirm Semgrep OSS-vs-Pro interprocedural boundary at pin; do not present interprocedural taint as an OSS capability.

## Blockers
None.

## Learnings & pipeline suggestions
- **Framing/"how-it-works" chapter passes the same pre-pin shape as the per-tool keys** — technique/API identity + verbatim quotes captured live, every version/default/path flagged, theorem held `UNVERIFIED` for a primary. The "illustrate-here, verdict-route-to-37" discipline made neutrality structural and the blocklist clean despite the chapter naming 8 tools.
- **New trap class:** a *passing API package path* (`edu.umd.cs.findbugs.bcel.…`) is a never-invent atom just like a rule-ID — easy to mis-segment from memory. Extends the keys-14/18/20 "no atom from memory" guard to fully-qualified API class paths (sibling of the key-25 4-package `@GuardedBy` rule). Recommend appending to PIPELINE-LEARNINGS.
