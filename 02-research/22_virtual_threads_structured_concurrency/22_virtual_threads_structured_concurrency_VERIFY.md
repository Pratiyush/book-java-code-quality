# GATE REPORT — SOURCE-VERIFY (key 22)

- **Gate:** VERIFY (step-2 SOURCE-VERIFY, pre-pin)
- **Artifact:** `02-research/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** — 0 blockers.

## Pre-pin caveat (HARD — same as keys 11/12/13/15/16/19/20/23/25)
`check_source_pin.sh` FAILs **by construction**: SOURCE-PIN is multi-authority, every row `TO-PIN`,
repo URL TBD, `/pin-source` never run; the ephemeral clone is ABSENT and unhealable. Therefore atom
**byte-verification is DEFERRED to `/pin-source`**. This PASS means "flagged the right things," NOT
"atoms machine-verified." Language/runtime facts here were curl-verified against the **live**
openjdk JEP pages (version-stable primaries); tool rule defaults are identity-only + `⚠ verify at pin`.

## Scripts (run vs manual)
| Script | Result |
|---|---|
| `check_source_pin.sh` | FAIL by construction (pin ABSENT/unhealable, multi-authority TO-PIN) |
| `ensure_source_pin.sh` | cannot heal (repo URL TBD) — out of scope until `/pin-source` |
| `verify_sources.sh` | cannot trace tool facts (no clone) — manual flag-discipline audit instead |
| `lint_citations.sh` | 21 violations — ALL known bare-domain / status-glyph / print-canon false positives |
| `check_neutrality.sh` | blocklist **clean**; em-dash density 19/1000 advisory (AUDIT lane, not VERIFY) |
| `check_snippets.sh` | n/a (research dossier, not a draft with include markers) |

## Checked claims
| # | Claim | Check | Result |
|---|---|---|---|
| 1 | VT GA@21 (JEP 444 Closed/Delivered/Release 21) | matches verified list (PL line 284) | OK — may state as fact |
| 2 | SC preview through 25 (JEP 453→462→480→499→505 Fifth Preview; 525 Sixth@26) | matches verified list | OK — `⚠ AHEAD-OF-PIN` everywhere |
| 3 | Scoped Values GA@25 not 21 (JEP 506; preview 446@21) | matches verified list; preview chain 429/446/464/481/487 consistent | OK — Java-25 delta, not anchor |
| 4 | `synchronized` pinning fixed@24 (JEP 491, Release 24, verbatim) | matches verified list; advice correctly JDK-dated | OK — version-delta carried into prose |
| 5 | SC API shape change 21 ctors → 25 `open(Joiner…)` | JEP 505 verbatim "static factory methods rather than public constructors" | OK — flagged, no companion dep |
| 6 | JMM unchanged on VT (JLS ch.17) | §17 sub-section numbers correctly marked `⚠ verify at pin` (Durable principle #1) | OK |
| 7 | Folklore "VT make concurrency safe / ignore JMM" | stated-and-CORRECTED (§1/§2.6/§4), never asserted | OK — matches folklore list |
| 8 | Tool rule IDs (EP / SpotBugs MT / Sonar S6906/S6881) | identity-only, every default `⚠ verify at pin`; both flag files match | OK |
| 9 | `@GuardedBy` provenance (JCIP 2006 vs EP package) | package disambiguation deferred to EP pin (matches key-25 4-package trap) | OK |
| 10 | No benchmark figure asserted | §4 explicitly declines (never-invent-figures) | OK |
| 11 | Neutrality (competing in-Java approaches, Bucket-i) | crowns none; "different approaches" framing; no banned phrase | OK |
| 12 | HONEST-LIMITATIONS floor | each lever (VT pinning / don't-pool / TL-blow-up / SC-preview / scoped-values) has hardest objection + when-NOT-to-use | OK |

## Findings (non-blocking, draft cleanup)
| # | Loc | Finding | Fix |
|---|---|---|---|
| F1 | §1 line 32 + §8 row 5 | text names JEP **499** (4th preview) but URL list shows only `462, /480` — 499 URL missing | add `/499` to the cited URL list |
| F2 | §8 header + ☑ marks | "verified by direct `curl` fetch **@the pin**" while NO pin exists — recurring pre-pin overclaim (keys 07/10/11/13/15/25) | reword to "live-line, verify at pin"; reserve ☑ for post-`/pin-source` |
| F3 | lint_citations | 21 structural violations | all known false positives (bare-domain URLs, status-glyph rows, print canon) — no action |
| F4 | §-wide | em-dash density 19/1000 over ceiling | advisory; AUDIT-gate lane, not a VERIFY fact defect |

## Blockers
None.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned-authority/primary OR is marked `⚠ UNVERIFIED`/`⚠ AHEAD-OF-PIN`/`⚠ verify at pin`
- [x] No folklore-as-fact (VT-safety entry stated-and-corrected)
- [x] No off-pin/moving-target citations stated stable; SC/scoped-values/Valhalla preview features marked AHEAD-OF-PIN
- [x] Neutrality: no crowning, no banned phrasing, competing in-Java approaches balanced
- [x] Synthesized/causal/comparative claims supported by the cited JEP (readability/debuggability framing = JEP 444 verbatim; pinning-defeats-scaling = JEP 444 verbatim)
- [x] HONEST-LIMITATIONS floor met (hardest objection + when-NOT-to-use per option)
- [x] Both required flag files present and consistent with the dossier

## Learnings & pipeline suggestions
- F2 ("@the pin" overclaim) recurs across keys 07/10/11/13/15/25/22 — reinforces the standing proposal to
  reserve ☑/"@the pin" for **post-`/pin-source`** state; pre-pin should read "live-line, verify at pin."
- F1 (partial JEP URL list when a chain of JEP numbers is named) is a sibling of the key-13/20
  "no JEP-number without its source row" guard — every JEP number named should carry its own URL.
- Re-run atom byte-verification after `/pin-source` (tool rule defaults, JLS §17 sub-section numbers,
  exact system-property names `jdk.virtualThreadScheduler.parallelism` / `jdk.tracePinnedThreads`).
