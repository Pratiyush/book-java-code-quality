# GATE REPORT — SOURCE-VERIFY (key 21, Immutability & safe publication)

**Gate:** VERIFY (step 2, pre-draft research dossier)
**Artifact:** `02-research/21_safe_publication/21_safe_publication_RESEARCH.md`
**Date:** 2026-06-15
**Verdict:** **PASS_WITH_FLAGS** (0 blockers)

> **Pre-pin caveat (keys 12/16 doctrine):** SOURCE-PIN is multi-authority with every tool/JDK/spec row
> `TO-PIN` and repo URL `{URL}`; `/pin-source` has never run. A PASS_WITH_FLAGS here means
> "the dossier flagged the right things," NOT "atoms byte-verified." Atom re-trace must happen after
> `/pin-source`.

## Scripts run vs manual
| Script | Result |
|---|---|
| `check_source_pin.sh` | **FAIL — clone ABSENT** at the ephemeral job tmp; pin is `multi-authority / n/a-multi-authority`. Expected (pre-pin). |
| `ensure_source_pin.sh --heal` | **FAIL** — `fatal: repository '{URL}' does not exist`. Heal out of scope at VERIFY (it is `/pin-source`'s job). |
| `verify_sources.sh` | **FAIL by construction** — aborts (clone absent). No machine byte-trace possible; verification is MANUAL. |
| `check_neutrality.sh` | **PASS** — blocklist clean. Advisory only: 1 filler word; em-dash density 16/1000 (over the ~8 ceiling) → AUDIT/clarity cleanup, not a fact defect. |
| `lint_citations.sh` | 16 "violations" — ALL the known bare-domain false-positive (regex wants `http://`; every row DOES carry a URL) + `⚠`/`☐` status cells lacking a date token. Documented noise (keys 13/16/17). Not fact defects. |
| `check_snippets.sh` | N/A — no draft exists yet (`03-drafts/21_safe_publication/` absent). |

## Checked claims / facts
| Claim / atom | Where | Status |
|---|---|---|
| JEP 444 Virtual Threads = GA at **21**, does not change the JMM | §2.7, §5, §8 | OK — consistent w/ keys 20/23/24/25 verified list |
| JEP 506 Scoped Values = **GA/final at 25** (post-21 forward note only) | §0,§2.8,§5,§8, flag | OK — correctly post-anchor, not anchor-baseline |
| JEP 505 Structured Concurrency = **Fifth Preview at 25** → `⚠ AHEAD-OF-PIN` | §0,§2.8,§5,§7,§8, flag | OK — marked AHEAD-OF-PIN everywhere; never asserted stable |
| JEP 395 records = Java 16; records safely publishable by §17.5 when properly constructed | §2.2,§2.8 | OK — version correct; framed as nuance (no "records replace immutability") |
| JLS §17.5.1 "freeze action … when c exits" (verbatim) | §2.2,§3,§8 | Quote plausible & matches normative text; **clone absent → byte-verify at pin** |
| j.u.c "Memory Consistency Properties" happens-before list (verbatim, ellipsis-trimmed) | §2.1,§8 | Plausible/accurate; ellipsis-trimmed quotes acceptable but **byte-verify at pin** |
| JMM from JSR-133 / Java 5 (2004) | §1,§2.8,§5 | OK |
| Sonar `java:S2168`, `java:S3077` (IDs only; titles/defaults deferred) | §2.5,§2.7,§7,§8 | OK — `⚠ verify at pin`; survives "rule-ID-from-memory" trap (flag filed) |
| SpotBugs MT codes (IS2_INCONSISTENT_SYNC, DC_DOUBLECHECK/DC_PARTIALLY_CONSTRUCTED, LI_LAZY_INIT_STATIC, DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE, VO_VOLATILE_INCREMENT) | §2.5,§3,§4,§7,§8 | OK — "codes present", descriptions `⚠ verify at pin` (flag filed) |
| Error Prone `GuardedBy`/`Immutable` = severity ERROR; annotation FQNs | §2.6,§3,§4,§7,§8 | OK — pages cited; FQNs `⚠ verify at pin` (flag filed) |
| Folklore: "constructor finished ⇒ fully visible" = FALSE | §2.3,§7,§Learnings | OK — explicitly DEFUSED, not asserted as fact (matches folklore list addition) |

## Findings (all minor; none blocking)
| # | Location | Finding | Fix |
|---|---|---|---|
| F1 | §8 rows, §2.x ✅/☑ markers | `☑ (verbatim)` / `✅` used pre-pin with clone absent — reserve ☑ for post-`/pin-source` byte-checks (recurs keys 07/10/11/15). | At draft, downgrade verbatim-quote ☑ to "verify at pin" until the clone exists; re-trace the §17.5.1 freeze + j.u.c list byte-for-byte then. |
| F2 | §3 | JEP 506 quote *"enable a method to share immutable data … easier to reason about than thread-local variables"* mixes verbatim spans with paraphrase-inside-quotes. | Quote the JEP 506 Summary verbatim (or paraphrase outside quotes) at draft; byte-verify at pin. |
| F3 | §4 / §2.7 | `VO_VOLATILE_INCREMENT` is cited in §4 (Evidence-against) and queued in §7, but absent from the §2.8 reference-units table. | Add it to §2.8 so every named tool code sits in the single re-trace unit (key-15 matrix-coverage rule). |
| F4 | §6, §7 | `DEMO-CATALOG.md` row `21_safe_publication` not yet present; intended-deps marked `☐ verify at pin`. | Backfill the catalog row before example-build (flag already noted to catalog owner). |
| F5 | header / lint | Citation rows trip `lint_citations.sh` bare-domain false-positive. | Known script noise; optionally adopt `https://` house style. No content change required. |

## Gate-specific checks
- [x] Every specific atom (APIs, rule IDs, JEP releases, versions, quotes) traces to a primary/authority OR carries `⚠ verify at pin` / `⚠ AHEAD-OF-PIN` / `⚠ UNVERIFIED`.
- [x] No folklore-as-fact — the "constructor finished ⇒ visible" misconception is defused, not asserted; no 1:10:100 / coverage-as-quality / reified-generics / "records replace immutability."
- [x] No off-pin / moving-target citation asserted as stable; preview features (structured concurrency) marked AHEAD-OF-PIN; scoped values marked GA-at-25-post-anchor.
- [x] Neutrality: blocklist clean; tools framed as enforcement support, each cited to its own source; no crowning; §4 "competing approach" framed neutrally (approach-based, crown none).
- [x] Synthesized/causal/comparative claims supported (visibility-not-ordering thesis; final-field freeze ⇒ safe publication; DCL-broken-without-volatile; VT don't change the JMM) — each tied to JLS §17 / j.u.c summary / JEP.
- [x] HONEST-LIMITATIONS floor met — §4 gives each lever its hardest objection + explicit when-NOT-to-use (immutability/final/volatile/locks/DCL/checkers).
- [x] Both required flags filed and accurate (`21_structured_concurrency_ahead_of_pin.md`, `21_tool_rule_defaults_unverified.md`).

## Blockers
None.

## Required fixes (carry to draft)
F1 (reserve ☑ for post-pin; byte-verify the two verbatim quotes), F2 (JEP 506 verbatim), F3 (add VO_VOLATILE_INCREMENT to §2.8). F4/F5 are housekeeping.

## Learnings & pipeline suggestions
- Pre-pin SOURCE-VERIFY pattern holds again (keys 07–25): scripts FAIL by construction on the multi-authority
  pin; the gate audits flag/`⚠` discipline, folklore, neutrality, AHEAD-OF-PIN labelling — not atoms. Re-run
  after `/pin-source`.
- Reinforces the "reserve ☑ for post-pin byte-checks" candidate rule (keys 07/10/11/15) — key 21 again marks
  verbatim quotes ☑ with the clone absent.
- Matrix-coverage rule (key 15) reused: every tool code named in prose should also appear in the §2.8
  reference-units table (caught VO_VOLATILE_INCREMENT).
