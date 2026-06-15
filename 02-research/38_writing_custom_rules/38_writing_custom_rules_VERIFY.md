# GATE REPORT — SOURCE-VERIFY (key 38, Writing custom rules)

- **Gate:** VERIFY (step-2 SOURCE-VERIFY, pre-pin)
- **Artifact:** 02-research/38_writing_custom_rules/38_writing_custom_rules_RESEARCH.md
- **Date:** 2026-06-15
- **Verdict:** PASS_WITH_FLAGS · **Blockers:** 0

## Scripts run (vs manual)
| Script | Result | Note |
|---|---|---|
| check_source_pin.sh | FAIL (by construction) | Pin ABSENT/unhealable — multi-authority, `{URL}` placeholder, all §2/§3 rows `TO-PIN`, `/pin-source` never run. Ephemeral clone gone. Same state as keys 12/20/22/23/25/30. |
| verify_sources.sh | FAIL (by construction) | Cannot trace — no clone. Atom byte-verification DEFERRED to `/pin-source`. |
| lint_citations.sh | 22 violations — ALL known false-positive class | Bare-domain URLs (no `https://`) + "live-line, verify at pin" status (not a date marker). Same class as keys 19/20/23/25/30. Not blockers; clean up bare URLs / date column at draft. |
| check_neutrality.sh | FAIL — one `unlike` token (line 676) + em-dash density | See findings F1/F4. |
| (no web access) | — | Quoted spans cannot be byte-verified now; re-trace at `/pin-source`. |

> **Pre-pin caveat (keys 12/20/23/25/30):** a PASS_WITH_FLAGS here means "flagged the right things," NOT "atoms verified." API identity (classes/methods/annotations/config-element names) was verified by the dossier from each tool's live docs; byte-level re-trace must happen after `/pin-source`.

## Checked claims / findings
| # | Location | Claim checked | Disposition |
|---|---|---|---|
| C1 | §0/§7/§8 | All tool versions, GAVs, default severities, archetype `0.4.19`, ArchUnit `1.4.2`, maven-checkstyle-plugin `3.6.0` | OK — all marked "shown"/`⚠ verify at pin`, none asserted as the pin. |
| C2 | §5 | "FindBugs is dead — cite SpotBugs; `Detector`/`findbugs.xml` are lineage artifacts, not a live FindBugs dep" | OK — no folklore/FindBugs-as-current. Correctly framed. |
| C3 | §5 | JEP releases (395 GA16, 409 GA17, 441 GA21, 440 GA21; JEP 507 primitive patterns 3rd-preview@25 `⚠ AHEAD-OF-PIN`) | OK — match the PIPELINE key-13/22 verified list; preview correctly AHEAD-OF-PIN. |
| C4 | §2/§2.7 | Per-tool API identity each cited to that tool's OWN doc (Checkstyle/PMD/EP/SpotBugs/ArchUnit) | OK — no tool's docs used to describe another's model (NEUTRALITY cited-source rule). §2.7 matrix covers every named atom (key-15/23 coverage rule met). |
| C5 | §2.4/§2.2 | Verbatim quotes ("loaded dynamically … java.util.ServiceLoader"; token-method summaries; "match any subexpression of a compatible type"; base-detector descriptions; "does not support plugin dependencies") | UNVERIFIABLE now (no clone/web) — correctly marked "live-line, verify at pin." Re-trace byte-for-byte at `/pin-source`. Recommend web re-check at draft. |
| C6 | §4 | HONEST-LIMITATIONS floor — each of 5 models has hardest objection + when-NOT-to-use + shared-limits centre | OK — floor met; none crowned. |
| C7 | §2.1/§4 | "auto-fix is a trade-off, not a crowning"; "which tool should own a rule → key 37" | OK — cross-cutting verdict routed to key 37 (not asserted here); neutral approach-framing. |
| C8 | §7 | PMD 7.x AST/rulechain churn; XPath wrapper-class rename | OK — flagged `⚠ verify at pin`, filed `09-flags/38_pmd_api_churn_unverified.md`. |
| C9 | §7 | Both flag files present & accurate | OK — `09-flags/38_tool_versions_and_apis_unverified.md` + `09-flags/38_pmd_api_churn_unverified.md` exist and match the dossier. |

## Findings (draft-fix; non-blocking)
- **F1 (neutrality token).** Line 676: "(no 403, unlike the openjdk JEP pages)" trips the `unlike X` blocklist. Context is doc-site *fetchability* (a tooling note in Learnings), not a tool-quality crowning — same false-positive class as keys 20/23/30. **Fix:** reword anyway (e.g. "whereas the openjdk JEP pages 403"). The blocklist is literal; do not ship the token.
- **F2 (lint false positives).** 22 lint_citations violations are all bare-domain URLs + the "live-line, verify at pin" status column. **Fix at draft:** add `https://` and a verified-date once pinned.
- **F3 (version-as-color).** spotbugs-archetype `0.4.19`, ArchUnit `1.4.2`, plugin `3.6.0` are observed live-line values — keep them strictly `⚠ verify at pin`; never let them migrate into asserted prose without the pin (recurring "(verified)/(confirmed)-without-pin" trap, keys 19/25/30).
- **F4 (em-dash density 16/1000).** Over the ~8/1000 ceiling — CLARITY/draft lane, not a VERIFY blocker.

## VERIFY gate-specific checks
- [x] Every specific fact traces to that tool's OWN doc OR carries `⚠ verify at pin` / `⚠ UNVERIFIED`.
- [x] No folklore-as-fact (FindBugs correctly framed as dead lineage, not current).
- [x] No off-pin / SNAPSHOT / newer-than-pin citation asserted (all `TO-PIN`; observed versions flagged).
- [x] NEUTRALITY — no tool crowned; auto-fix framed as trade-off; cross-cutting verdict → key 37. (One literal `unlike` token to reword — F1.)
- [x] Synthesized/comparative claims supported (PMD-churn flagged; per-artifact strengths/limits tied to each artifact, cited per tool).
- [x] HONEST-LIMITATIONS floor met (each model + shared centre).
- [x] §2.7 matrix covers every named atom (single re-trace unit at `/pin-source`).
- [x] Required flag files present & accurate.

## Blockers
None.

## Learnings & pipeline suggestions
- **Recurring tooling-`unlike` false positive (keys 20/23/30/38).** A doc-site fetchability contrast keeps tripping check_neutrality's `unlike` token even when no tool is crowned. Reinforces the standing proposal to whitelist a tooling/doc-site `unlike` context in the scripted pre-pass — but authors should still reword to keep the blocklist literal-clean.
- **"Authoring-API identity vs version" granularity (new, from this key).** For a *custom-rule* chapter the never-invent atoms invert: API identity (base classes, override methods, annotations, config elements) is verifiable now from each tool's own docs, while versions/GAVs/severities/PMD AST-node renames defer to pin. Propose adding alongside the key-9/16 "rule-ID vs severity" split.
- **Pre-pin PASS_WITH_FLAGS = "flagged right," not "verified."** Same caveat as keys 12/20/23/25/30; atom byte-trace (all live-line quotes in C5) must run after `/pin-source`.
