# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** VERIFY (SOURCE-VERIFY)
- **Key / artifact:** 16 — Resource & lifecycle management — `02-research/16_resource_management/16_resource_management_RESEARCH.md`
- **Date:** 2026-06-15
- **Reviewer:** source-verifier (adversarial cold read)
- **Pin state:** SOURCE-PIN.md is multi-authority; ALL tool rows are `TO-PIN`, no clonable repo URL pinned
  (`/pin-source` is an open standing to-do). The ephemeral clone is ABSENT and CANNOT be healed
  (`{URL}`). Script-based pin verification therefore could not run; verification was performed
  **manually** against the dossier's cited authorities + reviewer knowledge of the Java platform (no web access).

## Scripts: run vs manual
| Script | Status | Result |
|---|---|---|
| `check_source_pin.sh` | RAN | FAIL — clone absent (expected; multi-authority, no repo URL). |
| `ensure_source_pin.sh --heal` | RAN | FAIL — `{URL}`, nothing to clone. |
| `verify_sources.sh` | RAN | Aborted — needs the (non-existent) clone. Verification done MANUALLY. |
| `lint_citations.sh` | RAN | 20 structural "violations" — see Findings F1 (mostly false positives). |
| `check_snippets.sh` | N/A | Dossier (Step 2) carries no `<!-- include -->` markers. |

## Checked claims
| # | Claim | Source cited | Verdict |
|---|---|---|---|
| 1 | try-with-resources GA Java 7; JLS §14.20.3 (.3.1 basic / .3.2 extended) | JLS SE 21 | OK — section nos. correct |
| 2 | Reverse-order (LIFO) close; always-close; init-failed resource not closed | JLS §14.20.3 | OK |
| 3 | Suppressed exceptions: E1 propagates, E2 via `addSuppressed`/`getSuppressed` | JLS §14.20.3; Throwable javadoc | OK |
| 4 | `AutoCloseable.close()` `throws Exception`, NOT required idempotent | AutoCloseable SE 21 javadoc | OK — quote verbatim from javadoc |
| 5 | `Closeable.close()` `throws IOException`, idempotent; Closeable=Java 5, subtypes AutoCloseable in 7 | Closeable/AutoCloseable javadoc | OK |
| 6 | Effectively-final resources = Java 9 / JEP 213 (Milling Project Coin) | JEP 213 | OK |
| 7 | `Cleaner` (java.lang.ref) since Java 9; phantom-reachable; runs ≤ once; static State pattern | Cleaner SE 21 javadoc | OK |
| 8 | `finalize()` deprecated Java 9; deprecated-FOR-REMOVAL Java 18 / JEP 421; `--finalization=disabled`; still present at 21 & 25 | JEP 421; deprecated-list | OK |
| 9 | Effective Java 3e: Item 8 "Avoid finalizers and cleaners", Item 9 "Prefer try-with-resources…" | EJ 3e | OK (item nos./titles correct); Item 9 masking QUOTE flagged ⚠ verbatim/page §7 |
| 10 | `ExecutorService implements AutoCloseable` since Java 19 | SE 21 javadoc | OK — correctly marked `⚠ verify at pin` |
| 11 | SEI CERT ERR54-J | wiki.sei.cmu.edu | OK (corroboration) |
| 12 | Tool rule IDs: `java:S2095`, PMD `CloseResource`, SpotBugs `OBL_*`/`OS_OPEN_STREAM`/`ODR_*`, Error Prone `MustBeClosed`/`StreamResourceLeak`/`Finalize` | each tool's docs | IDENTITY OK; versions/thresholds/severities/default-profile `⚠ verify at pin` (flag filed) |
| 13 | Folklore: "GC closes your files" | — | OK — stated-AND-corrected as misconception (teaching), not asserted |
| 14 | AHEAD-OF-PIN: `StructuredTaskScope` preview → deferred to key 22 | — | OK — marked `⚠ AHEAD-OF-PIN`, not asserted as settled |

## Findings
| # | Severity | Location | Issue | Fix |
|---|---|---|---|---|
| F1 | Cosmetic / pipeline | §2.2 bullets; §8 Sources tables | `lint_citations.sh` reports 20 "violations" — mostly FALSE POSITIVES: it treats §2.2 mechanism bullets as "source rows," and flags Sources URLs because the house style uses bare-domain plain text (no `http://` scheme prefix) which the linter regex demands. Table rows DO carry plain-text URLs + ☑ markers. | No content change required. Either prefix URLs with scheme at draft, or tune linter for bare-domain + skip non-Sources bullets. Recorded as pipeline item. |
| F2 | Minor (neutrality phrasing) | Line 52 | Bold lead-in "**The problem with the older idiom**" contains the literal blocklist string "the problem with" (NEUTRALITY blocklist). It refers to the older Java `try`/`finally` idiom (subject's own evolution), NOT a rival tool, so it does not crown a winner — but the greppable AUDIT scan will flag the exact phrase. | Rewrite at draft, e.g. "The hazard of the older idiom" / "Why the older idiom was error-prone." |
| F3 | Minor (overclaim) | Lines 186–188 | "The **entire** `java.io`/`java.nio`/`java.sql`/`java.util.concurrent` surface implements AutoCloseable/Closeable" — over-broad; only the resource-bearing types do. | Qualify: "the resource-bearing types across `java.io`/`java.nio`/`java.sql`/`java.util.concurrent`." |
| F4 | Tracked (not a defect) | §2.5 table; §3; §7 | Tool versions / default thresholds / severities / default-profile membership unverified (SOURCE-PIN rows `TO-PIN`). | Correctly marked `⚠ verify at pin`; flagged `09-flags/16_s2095_tool_versions_unverified.md`. Resolve at `/pin-source`. Recommend web re-check at draft for: S2095 default "Sonar way" membership + exempt list, PMD CloseResource default props, SpotBugs bug-ranks, Error Prone default severities, ExecutorService Java-19 auto-close behavior, EJ Item 9 verbatim/page. |

## Blockers
- NONE. No invented fact, no unflagged off-pin fact, no folklore-as-fact, no neutrality crowning/denigration.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority/primary OR is marked `⚠ UNVERIFIED`/`⚠ verify at pin`/`⚠ AHEAD-OF-PIN`.
- [x] No folklore stated as fact (folklore list cross-checked; "GC closes files" correctly stated-and-corrected).
- [x] No off-pin / moving-target citations (no SNAPSHOT/main; preview `StructuredTaskScope` marked AHEAD-OF-PIN; Java-19 ExecutorService marked verify-at-pin).
- [x] Synthesized/causal/comparative claims supported (tool-consensus claim cites each tool's own source; crowns none).
- [x] HONEST-LIMITATIONS floor met (§4: each option has hardest objection + when-NOT-to-use; spectrum crowns none).
- [~] Neutrality: balanced & non-crowning, BUT one literal blocklist phrase (F2) must be rewritten before draft passes the AUDIT greppable scan.
- [x] Quotes attributed to javadoc are verbatim from the cited javadoc; EJ Item 9 quote flagged for print verbatim/page check.
- [x] Material unverified items flagged to `09-flags/`.

## Verdict: PASS_WITH_FLAGS
Language/spec/API/JEP/canon facts are accurate and traceable. Tool specifics are correctly scoped to
identity-only with versions/thresholds flagged. Three minor draft-time fixes (F2 blocklist phrase, F3
overclaim, F1 linter cosmetics) and the standing `⚠ verify at pin` queue (F4) — none rises to a rework
blocker.

## Learnings & pipeline suggestions
1. **Multi-authority pin breaks the clone-based scripts.** `check_source_pin.sh`/`verify_sources.sh`/
   `ensure_source_pin.sh` assume one repo+tag; for this book (rows all `TO-PIN`, `{URL}`) they
   can only ever FAIL. SOURCE-VERIFY for this book is a MANUAL procedure until `/pin-source` runs and the
   multi-authority adaptation (OPEN ITEM in PIPELINE-LEARNINGS) lands. Verifiers must say "manual," not
   "script ran clean."
2. **`lint_citations.sh` false-positives** on (a) bare-domain plain-text URLs and (b) §2.x mechanism
   bullets read as source rows. Tune the row-detector to the Sources section and accept bare domains, or
   adopt scheme-prefixed URLs as house style.
3. **Blocklist phrase guard extends to subject-evolution prose**, not just rival comparisons: "the problem
   with [the old idiom]" trips the literal scan even when no rival is crowned. Flag such constructions at
   research time so AUDIT doesn't bounce the draft.
