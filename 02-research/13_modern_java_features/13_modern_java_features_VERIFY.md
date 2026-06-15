# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** VERIFY (SOURCE-VERIFY)
- **Key / artifact:** 13 — Modern Java for quality (records, sealed types, pattern matching, switch expressions, text blocks, 21→25)
- **Artifact under review:** `02-research/13_modern_java_features/13_modern_java_features_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS**
- **Blockers:** 0

## Scripts run (vs manual)
| Script | Status | Result |
|---|---|---|
| `check_source_pin.sh` | RAN | FAIL — pinned clone ABSENT (ephemeral; repo URL still "TBD", all SOURCE-PIN rows `TO-PIN`). |
| `ensure_source_pin.sh --heal` | BLOCKED | Denied (mutates shared pin state; out of scope for a verify-only task). Cannot bring source on-pin. |
| `verify_sources.sh` | RAN | FAIL — "pinned clone absent"; no machine fact-tracing possible. |
| `lint_citations.sh` | RAN | 21 "violations" — all are bare-domain URLs lacking an `https://` scheme; the URLs ARE present. Cosmetic; not missing citations. |
| `check_neutrality.sh` | RAN | Blocklist CLEAN. Advisory: em-dash density 15/1000 (AUDIT-gate concern, not VERIFY). |
| `check_snippets.sh` | N/A | No `<!-- include -->` snippet markers in a research dossier. |

## Pin context (decisive)
The multi-authority pin has **never been fetched** — `/pin-source` is an open to-do (SOURCE-PIN OPEN ITEMS; repo URL = TBD; every authority row = `TO-PIN`). No fetched corpus exists to trace atoms against, so verification is by (a) the dossier's own honest UNVERIFIED/AHEAD-OF-PIN marking discipline and (b) adversarial sanity-check of load-bearing facts. This is a known book-wide state, not a fault introduced by this dossier. The dossier handles it correctly: every tool atom carries `⚠ verify at pin`, JLS § numbers deferred, and the two required flags are filed.

## Checked claims / issues
| # | Claim / atom | Location | Finding | Disposition |
|---|---|---|---|---|
| 1 | JEP Release fields: records=16, sealed=17, switch-expr=14, text-blocks=15, pattern-instanceof=16, pattern-switch=21, record-patterns=21 | §1, §2, §2.7, §3, §8 | Correct (matches JEP head tables). Preview chains (359/384; 360/397; 405/432; 406/420/427/433) also correct. | OK |
| 2 | JEP 512 Compact Source/Instance-main final at 25 | §5 | Correct. | OK |
| 3 | JEP 507 primitive type patterns = Third Preview at 25 | §5, §7, src row 9 | Correct; marked `⚠ AHEAD-OF-PIN`, flag filed (`13_jep507...`). | OK |
| 4 | Sonar rule IDs `java:S6206/S131/S1301/S6916` | §2.7, §3, §4, §7 | Existence corroborated (community); titles/defaults NOT pin-verified. Correctly marked `⚠ verify at pin`; flag filed (`13_sonar_rule_defaults...`). | FLAG (correctly handled) |
| 5 | Error Prone `PatternMatchingInstanceof`, `StatementSwitchToExpressionSwitch` (WARNING + summaries) | §2.7, §3 | Marked ☑ verbatim via WebFetch; cannot re-trace without pin (errorprone row is `TO-PIN`). Re-confirm wording/severity at pin. | FLAG (recommend web re-check at draft) |
| 6 | Synthesized spine: sealed + record + exhaustive switch → "forgot a case" becomes a compile error | §1.2, §2.4(5), §4 | Supported by JEP 441 / JLS exhaustiveness; correct language semantics. | OK |
| 7 | Causal "default trap": adding `default` to a sealed switch defeats exhaustiveness checking | §4, §6 | Correct semantics; traces to JEP 441/JLS. Sonar S131-FP friction attributed to community (corroboration, not spec) + flagged. | OK |
| 8 | Folklore guard | whole file | No folklore-as-fact. "Records make immutability obsolete" over-claim AVOIDED; records framed as transparent-immutable carriers with EJ as rationale not ruling. No Valhalla/reified-generics/100×/MI claims. | OK |
| 9 | Neutrality (language chapter; not `⚠`) | §intro, §4 | No crowning, no blocklist phrase. Lombok-vs-records uses neutral "different approaches / crown neither" pattern, deferred to keys 10/40. | OK |
| 10 | HONEST-LIMITATIONS floor | §4 | Met — records, sealed, pattern/exhaustive switch, text blocks each get hardest-objection + when-NOT-to-use; plus var-hides-types, migration cost. | OK |
| 11 | Lombok factual claims ("works on older JDKs", "annotation processor") | §4 | Rival-tool facts; per NEUTRALITY must carry a Lombok-pinned cite when stated in prose. Deferred to key 40 — acceptable for dossier, required at draft. | FLAG (cite at draft) |
| 12 | Nested record-pattern example `case Line(Point(var x1, var y1), Point p2))` | §2.4(2) | Unbalanced parenthesis (extra `)`); illustrative pseudocode, not a load-bearing atom. | MINOR — fix at draft |
| 13 | JLS SE 21/SE 25 § numbers (exhaustiveness, record patterns, minimal-indentation) | §2.4, §7, src row 10 | Not pinned; correctly deferred `⚠` per Durable principle #1 (JLS § discipline). | FLAG (cite § at draft) |

## Blockers
None. No invented atom is stated as fact; every unverifiable atom is marked `⚠ UNVERIFIED` / `⚠ verify at pin` / `⚠ AHEAD-OF-PIN`, and the two required flags are filed.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority OR is explicitly marked UNVERIFIED. (Pin un-fetched book-wide; dossier marks every tool atom and defers JLS §.)
- [x] No folklore stated as fact (cross-checked Folklore list).
- [x] No off-pin / moving-target citation presented as settled (JEP 507 preview marked AHEAD-OF-PIN; no SNAPSHOT/main; nothing past 25 asserted).
- [x] Neutrality: no winner crowned; no banned phrasing; Lombok contrast neutral.
- [x] Synthesized / causal / comparative claims supported (exhaustiveness spine, default-trap).
- [x] HONEST-LIMITATIONS floor met (hardest-objection + when-NOT-to-use per option).

## Required fixes (carry into draft — none block the dossier)
1. Resolve the two filed flags before any tool atom is stated as fact: pin-verify Sonar `S6206/S131/S1301/S6916` titles+defaults (RSPEC repo / in-product) and re-confirm the two Error Prone bug-pattern summaries+severity at the pinned tool versions.
2. Cite exact JLS SE 21 / SE 25 § numbers for exhaustiveness, record patterns, and minimal-indentation when block-quoting (Durable principle #1).
3. Any Lombok factual claim that survives into prose needs a Lombok-pinned citation (NEUTRALITY cross-subject rule) or stays deferred to key 40.
4. Fix the unbalanced parenthesis in the nested record-pattern example (§2.4).
5. Recommend web re-check at draft (no web access here): Error Prone summary wording/severity; Sonar rule titles; JLS § numbers.

## Learnings & pipeline suggestions
- `verify_sources.sh` is unrunnable book-wide until `/pin-source` fetches the multi-authority corpus; every Step-2 VERIFY currently rests on the dossier's own marking discipline + adversarial sanity-check. The book's verify-discipline is doing the job the un-fetched pin cannot — but this should be made explicit in the gate report shape so a future reviewer does not read "verify_sources FAIL" as a dossier defect.
- `lint_citations.sh` flags bare-domain URLs (no scheme) as "no URL." For a book whose house style uses `openjdk.org/jeps/NN`, this produces 21 false violations per dossier. Recommend the script accept scheme-less domains, or the house style adopt `https://`.
