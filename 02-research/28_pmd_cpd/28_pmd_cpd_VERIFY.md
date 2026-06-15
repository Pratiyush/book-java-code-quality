# GATE REPORT — SOURCE-VERIFY (key 28: PMD & CPD)

- **Gate:** SOURCE-VERIFY (pipeline step 2)
- **Artifact:** `02-research/28_pmd_cpd/28_pmd_cpd_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** — 0 blockers, 1 required draft-time fix (banned phrase), recurring pre-pin caveats.

## Scripts run vs manual
| Script | Result | Note |
|---|---|---|
| `check_source_pin.sh` | FAIL (by construction) | Pin ABSENT + unhealable: multi-authority, `{URL}` placeholder, every PMD/Maven/Gradle row `TO-PIN`, `/pin-source` never run. Expected pre-pin state (keys 11/12/13/19/20/22/23/25/30). |
| `verify_sources.sh` | FAIL (clone absent) | Cannot trace atoms with no clone. Atom byte-verification DEFERRED to `/pin-source`. |
| `lint_citations.sh` | 17 violations — all known false positives | 2 = author-year on PRINT canon books (Fowler/Bloch 2018, no URL by nature); 15 = bare-domain table URLs + `☐`/`⚠`-status rows. Same class accepted at keys 19/20/23/25. |
| `check_neutrality.sh` | **FAIL — 1 real banned phrase** | Line 230 "the reason CPD **beats** `diff`". "beats" is on the blocklist (NEUTRALITY §30, "beats/outperforms as a verdict"). Em-dash density 17/1000 + 1 filler word → CLARITY's lane, not VERIFY. |
| `check_snippets.sh` | n/a | No draft / include-markers yet (step-2 dossier). |

> **Pre-pin caveat (keys 12/19/25 precedent):** a pre-pin PASS_WITH_FLAGS means "flagged the right things,"
> NOT "atoms verified." Rule IDs, priorities, default thresholds, GAV versions, the Maven `minimumTokens=100`,
> and PMD-7 CLI flag spellings must be byte-re-traced after `/pin-source`.

## Findings
| # | Location | Finding | Severity | Fix |
|---|---|---|---|---|
| F1 | §2.3 line 230 | "the reason CPD **beats** `diff` for clone-finding" — "beats" is a blocklist verb (NEUTRALITY §30). The parenthetical "stated as a capability, not a crowning" does not rescue a banned word. `diff` is a generic utility (Bucket-i), so the underlying claim is fine; only the verb is banned. | DRAFT-FIX (required before AUDIT) | Reword, e.g. "finds renamed/edited copies a plain `diff` cannot" (the §3 line 295 phrasing already does this correctly). |
| F2 | §4 lines 349-356 | Cross-tool characterizations (Checkstyle=style/source, SpotBugs=bytecode, Error Prone=`javac` plugin) are factual claims about rivals. The dossier asserts they "cite that rival's own pinned source" but carries no per-claim inline cite — it cite-defers. Same F3 class as key 30. | NON-BLOCKING (carry to draft) | At draft, attach each rival claim to that tool's own pinned source (Checkstyle/SpotBugs/Error Prone docs); route the layering VERDICT to key 37 (already done). |
| F3 | §8 header + §2.5 / Sources `☑` | "☑ = verified by direct fetch this session against the **live** PMD docs" — correctly demoted (not pin-confirmed), but `☑` reads as a tick; the recurring "(verified)/☑-without-pin" overclaim (keys 07/10/13/19/25/30). | NON-BLOCKING | Body annotations already say "live-line, verify at pin"; keep `☑` reserved for post-pin or relabel "live-line ✓". |
| F4 | §6 / §7 | `01-index/DEMO-CATALOG.md` absent — `28_pmd_cpd` demo row cannot be registered. Correctly flagged (`09-flags/28_demo_catalog_missing.md`). | NON-BLOCKING (filed) | Create catalog at draft/example-build (same gap keys 15/25). |

## VERIFY gate-specific checks
- [x] Every specific atom (rule IDs, category files, ruleset schema, CPD flags, suppression markers) traces to PMD's OWN doc page OR carries `⚠ verify at pin` / `⚠ UNVERIFIED`. Identity verified live; version-sensitive atoms flagged.
- [x] No folklore-as-fact. No FindBugs-as-current. Threshold-discipline applied (CPD `--minimum-tokens` "never print a number" per key-19; complexity limits = "convention not law").
- [x] No off-pin / moving-target citation asserted. All versions "observed" + `⚠ verify at pin`; both SNAPSHOT mentions are used as negative examples ("snapshots are never a pin source").
- [x] NEUTRALITY: no tool crowned ("no winner crowned" §2.4); cross-cutting verdict routed to key 37; **but** one banned verb present (F1) → neutrality FAIL until reworded.
- [x] Synthesized/comparative claims supported: two-spines/two-proxies frame is sound; CPD-vs-`diff` capability claim is verified (token-vs-text); §4 rival claims cite-deferred (F2).
- [x] HONEST-LIMITATIONS floor MET: PMD (syntactic proxy, reflection FPs, heuristic thresholds + when-NOT-to-use) and CPD (textual-only, minimum-tokens delicacy + when-NOT-to-use) each get hardest objection + when-NOT-to-use; build/compat + perf caveats present.
- [x] Flag files present & accurate: `28_pmd_versions_and_defaults_unverified.md`, `28_demo_catalog_missing.md` — both match §7.

## Blockers
None. (F1 is a one-word draft-fix; it must be cleared before the AUDIT gate but does not invalidate the dossier's substance.)

## Learnings & pipeline suggestions
- **Banned-phrase "beats" slipped past an author self-aware enough to annotate "not a crowning."** Confirms the
  rule: a blocklist word is banned even when the author flags it as harmless — the scripted neutrality pre-pass
  is the right gate of record. Sibling of the keys 20/23/30 `unlike`-token findings (there: false positives; here:
  a true positive). Propose adding "beats/outperforms" to a draft-time grep authors run before submission.
- **Recurring pre-pin shape holds for the whole Part-IV analyzer batch (27-37):** identity citeable now, versions/
  thresholds/defaults `verify at pin`, rival §4 claims cite-deferred, cross-cutting verdict → key 37. Key 28 follows it cleanly.
