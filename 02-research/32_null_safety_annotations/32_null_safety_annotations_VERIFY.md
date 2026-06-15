# GATE REPORT — SOURCE-VERIFY (step 2)

- **Key / dossier:** 32 — The null-safety annotation landscape (JSpecify / Checker Framework / JSR-305 legacy)
- **Artifact:** `02-research/32_null_safety_annotations/32_null_safety_annotations_RESEARCH.md`
- **Gate:** VERIFY (pre-pin flag-discipline audit)
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** — 0 blockers
- **Pre-pin caveat (per PIPELINE-LEARNINGS, keys 11/12/20/22/23/25):** SOURCE-PIN is multi-authority with every
  tool/spec row `TO-PIN` and the clone `{URL}`/tag unhealable. A PASS_WITH_FLAGS here means **the right things
  are flagged**, NOT that version/GAV/conformance atoms are byte-verified. Atom re-trace is DEFERRED to `/pin-source`.

## Scripts (run vs manual)
| Script | Result |
|---|---|
| `check_source_pin.sh` | **FAIL by construction** — pin ABSENT/unhealable (multi-authority, `{URL}`, tag `n/a-multi-authority`, all rows `TO-PIN`). `/pin-source` never run. Expected. |
| `verify_sources.sh` | **FAIL by construction** — "pinned clone absent; run check_source_pin.sh first." Cannot trace atoms with no clone. |
| `lint_citations.sh` | 17 violations — **all known false positives**: every §8 source row carries a plain bare-domain URL (script wants a markdown-link/date-marker shape) + 3 `☐`-status rows. No real citation defect. |
| `check_snippets.sh` | N/A at step 2 (no draft / include markers yet). |
| banned-phrasing / folklore grep | **CLEAN** (manual). |

## Checked claims / issues
| # | Claim / atom | Location | Finding | Fix |
|---|---|---|---|---|
| C1 | Annotation **identity + semantics** (JSpecify 4 annotations type-use; Checker FW qualifiers + sub-checkers; JSR-305 declaration set) | §1/§2/§2.7 | Verified from each project's own docs (live-line), matches the key-32 PIPELINE-LEARNINGS atom set. | none — re-trace bytes @pin |
| C2 | 4-spelling `@Nullable` disambiguation (jspecify / checkerframework.qual / javax.annotation / springframework.lang) | §1, §2.4, Learnings | Each `@Nullable` is package-qualified; never cited generically. Matches key-25 `@GuardedBy` discipline. | none |
| C3 | All version/GAV atoms (`jspecify 1.0.0`, `jsr305:3.0.2`, EP `≥2.14.0`, NullAway `0.12.11`, Kotlin `1.8.20+`, pre-JDK-22 bug) | §1–§7, §2.7 | Carry `⚠ verify at pin` at point of use OR in the §7 queue + flag file. **Kotlin 1.8.20+ stated flat in §3 (270) and §5 (378)** — covered by §7 (482) + flag, but not marked inline. | Minor: mark `1.8.20+` inline at first FOR/status use (or demote to "per JSpecify conformance, verify at pin"). |
| C4 | AHEAD-OF-PIN items (Spring 7 / Boot 4 Nov 2025 + Spring annotation deprecation; IntelliJ 2025.3; Kotlin interop; Valhalla null-restricted types) | §5, §7, header | Every item `⚠ AHEAD-OF-PIN`, dated, cited as direction only; flag `32_nullsafety_adoption_ahead_of_pin.md` present + matches. | none |
| C5 | FindBugs references | §1 (×6), §2.4, §5, §9 | **Correctly historical** — "FindBugs-shipped jar," "FindBugs lineage," "born from the FindBugs annotations." NEVER presented as a current tool (no FindBugs-as-current folklore). | none |
| C6 | Valhalla "null-restricted types" | §5, flag | Marked exploratory / not in 21 or 25 / `⚠ AHEAD-OF-PIN`, never imminent — matches the key-11/14 Valhalla folklore guard. | none |
| C7 | Neutrality — no crowning, every cross-family fact cited to that family's source, verdict routed to key 37 | header, §2.6, §4, §8 | **CLEAN.** No banned phrasing (grep clean). "no winner crowned" / key-37 routing stated 8×. The two "recommend" tokens are *attributed* (JSpecify's own guidance; Spring's own recommendation), not the book crowning. | none |
| C8 | HONEST-LIMITATIONS floor — each family hardest objection + when-NOT-to-use | §4 | **MET** for all three families + a shared honest centre ("annotations alone do nothing"; reflection/deser bypass; migration cost). | none |
| C9 | Synthesized/comparative claims (type-use = precision difference; "annotations stable ≠ checkers complete"; same-problem-different-approach) | §2.1, §4 | Each traces to a source that makes the claim (JLS TYPE_USE / JSR 308; JSpecify conformance page for the gaps). Comparison framed Bucket-ii-neutral. | none — confirm JLS § numbers @draft |
| F1 | Quote-drift: "**still** under development" (210/303/527) vs "under development" (253/555) | §2.5, §2.7, §9 | Documented key-19/25 quote-drift class — the NullAway wiki phrase must be byte-identical or demoted. | Pick one verbatim spelling everywhere or paraphrase. |
| F2 | Elided/stitched quote: JSpecify goal "common set of annotation types **…** to improve…" (line 80) | §1 | Ellipsis quote needs a verbatim re-check marker (key-20 trap); not web-recheckable now. | Mark for verbatim re-check @draft; recommend web re-check (no web here, NOT failed). |
| F3 | JLS `TYPE_USE`/`TYPE_PARAMETER` § numbers (§9.7.4 / §4.11 named in Topic) | Topic, §7, §8 | Correctly deferred — §7 says assert exact § only from JLS SE 21 text @draft (Durable principle #1). The Topic-block §9.7.4/§4.11 should not be asserted until confirmed. | Keep `⚠ verify at pin` on the § numbers until JLS-text-confirmed. |

## Blockers
None.

## VERIFY gate-specific checks
- [x] Every specific fact traces to that family's own source OR is marked `⚠ verify at pin` / `⚠ AHEAD-OF-PIN` / `⚠ UNVERIFIED`.
- [x] No folklore-as-fact (FindBugs historical-only; Valhalla AHEAD-OF-PIN; no MI/coverage/100× claims present).
- [x] No off-pin / SNAPSHOT / newer-than-pin citation asserted as baseline (AHEAD-OF-PIN items dated + flagged).
- [x] NEUTRALITY — no crowning, no banned phrasing, comparisons balanced + each side cited, cross-stack verdict routed to key 37.
- [x] Synthesized/comparative claims supported by a source that makes the claim.
- [x] HONEST-LIMITATIONS floor met (each family + shared centre).
- [x] Both required flag files present and consistent with the dossier.
- [ ] Atom byte-verification — DEFERRED to `/pin-source` (pin absent; expected pre-pin).

## Required fixes (all minor, none blocking)
1. Normalize the NullAway "(still) under development" quote to one verbatim spelling (F1).
2. Add a verbatim-re-check marker to the elided JSpecify goal quote (F2); web re-check at draft.
3. Mark Kotlin `1.8.20+` inline `⚠ verify at pin` at first use, or demote (C3).
4. Hold `⚠ verify at pin` on JLS §9.7.4 / §4.11 until confirmed from JLS SE 21 text (F3).

## Learnings & pipeline suggestions
- Pre-pin SOURCE-VERIFY for a `⚠` comparison key with 3 annotation families is clean when: each `@Nullable`
  is package-qualified, every version atom is flagged, AHEAD-OF-PIN adoption is dated, and the cross-stack
  verdict is routed (key 37) rather than asserted. Key 32 did all four.
- Recurring minor classes again: (a) same-quote spelling drift ("still under development"); (b) elided-quote
  re-check marker. Reinforces the proposed `lint_citations.sh` quote-drift check + an elided/`…` re-check rule.
- New small class: a **conformance version atom** (Kotlin 1.8.20+) stated flat in prose but only flagged in the
  §7 queue — same shape as the key-23 "rule-ID in prose not in matrix" trap, applied to a *consumer version*.
  Extend the rule: every version atom named in prose must also carry `⚠ verify at pin` at first use, not only
  in the §7 queue.
