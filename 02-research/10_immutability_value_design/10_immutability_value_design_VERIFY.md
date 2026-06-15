# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Key:** 10 — Immutability & value-based design (records, immutable collections, defensive copies)
- **Artifact:** `02-research/10_immutability_value_design/10_immutability_value_design_RESEARCH.md`
- **Gate:** VERIFY · **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS**

## Scripts run vs manual
| Step | Tool | Result |
|---|---|---|
| Pin confirm | `check_source_pin.sh` / `ensure_source_pin.sh` | **Pin ABSENT and cannot be healed** — repo URL is TBD; the global `/pin-source` step (SOURCE-PIN OPEN ITEM) has not run. NOT a defect of this dossier; tool/JDK facts therefore traced from primary JEP/JLS/JDK knowledge + the dossier's own `⚠ verify at pin` discipline, not the (missing) local clone. |
| Citation lint | `lint_citations.sh` | 8 structural nits (see below) — non-blocking |
| Source verify | `verify_sources.sh` | Could not run (pin absent) — manual trace performed |
| Neutrality | `check_neutrality.sh` | blocklist **clean**; em-dash density + 3 filler words = advisory (AUDIT-gate lane) |
| Snippets | n/a (research dossier, no `include:` markers) | n/a |

## Checked claims / issues
| # | Claim | Source | Status |
|---|---|---|---|
| 1 | Records = JEP 395, final/GA Java 16, baseline at 21/25 | JEP 395 (primary) | ✔ correct |
| 2 | Generated members (private final fields, canonical ctor, accessors no `get`, equals/hashCode/toString); implicitly final; extends `java.lang.Record`; no extra instance fields | JEP 395 / dev.java | ✔ correct |
| 3 | Compact ctor validates/normalizes params, can't assign fields directly; defensive copy belongs there | dev.java / JEP 395 | ✔ correct |
| 4 | Record does NOT deep-copy/freeze mutable components (shallow immutability) | dev.java (quoted) | ✔ correct; defuses key-08 folklore "records make immutability obsolete" |
| 5 | `List/Set/Map.of` = Java 9; UOE on mutate; null-hostile; IAE on dup; randomized iteration order | Oracle JDK 9 core-libs | ✔ correct |
| 6 | `List/Set/Map.copyOf` = **Java 10** | Oracle/API | ✔ correct, AND correctly marked `⚠ verify at pin` |
| 7 | `Collections.unmodifiable*` = view, not copy; leaks if backing source retained | JDK API / Oracle | ✔ correct |
| 8 | "An immutable collection of objects is not the same as a collection of immutable objects" (verbatim) | Oracle JDK 9 core-libs | ✔ quote attributed; verbatim re-confirm at pin advisable |
| 9 | Effective Java Item 17 five rules + "separate object for each distinct value" cost | EJ 3e Item 17 (print) | ✔ accurate summary; verbatim/pages correctly deferred to draft |
| 10 | Effective Java Item 50 (copy-before-validate, accessor copies, clone caveat, Date→Instant) | EJ 3e Item 50 (print) | ✔ accurate summary |
| 11 | JEP 390 value-based contract: 8 wrappers + java.time + Optional; wrapper-ctor deprecation; javac sync/`==` warnings; Java 16 | JEP 390 | ✔ correct |
| 12 | `@jdk.internal.ValueBased` annotation | JEP 390 / JDK | ✔ correct internal marker (kept in source-units, not over-asserted reader-facing) |
| 13 | **JEP 401 value classes = PREVIEW at JDK 25** | JEP 401 | ✔ correctly labelled `⚠ AHEAD-OF-PIN`, flagged to `09-flags/10_value_classes_jep401_preview.md`; not presented as GA |
| 14 | Sonar `java:S2384`, PMD (`ImmutableField`/`ArrayIsStoredDirectly`/`MethodReturnsInternalArray`), Error Prone `@Immutable`/`ImmutableChecker`/`MixedMutabilityReturnType` | each tool's own doc | ✔ rule identities plausible & cited to own source; exact title/category/CWE/defaults correctly `⚠ verify at pin` (S2384 page 403'd, flagged) |
| 15 | Guava `ImmutableList`/`ImmutableMap` null-hostile, `copyOf` defensive copy, GAV `com.google.guava:guava` | guava.dev | ✔ in body; ver `⚠ verify at pin` |
| 16 | Neutrality (no crowning; each tool cited to own source) | NEUTRALITY.md | ✔ clean — blocklist clean, "three instruments, crown none" framing |
| 17 | HONEST-LIMITATIONS floor (per-instrument gap + when-NOT-to-use) | floor | ✔ met — §4 + "When NOT to reach for it" |

## Issues / flags (none blocking)
1. **Pin unhealable (environmental, pre-existing).** Local authority clone absent and repo URL TBD; tool-doc facts could not be machine-traced. Dossier mitigates correctly by marking every tool fact `⚠ verify at pin` and flagging the 403s. **Action: resolve the global `/pin-source` OPEN ITEM before DRAFT/source-verify of tool-specific facts.**
2. **Guava "95%/5% study" figure (scan log only).** Row 10 of §9 records a weakly-evidenced famous Guava-wiki null figure. It does NOT appear in the dossier body (provenance note only). **Action: do NOT let this migrate into the draft as fact** — if Guava null behaviour is asserted, cite guava.dev verbatim, not the folklore percentage.
3. **Citation-lint structural nits (8, non-blocking).** Book-canon print rows (EJ Item 17/50) and the SEI CERT / Guava rows lack plain-text URLs — expected for print canon / path-only sources; S2384 row lacks a date-verified marker (it is the 403'd row, already `⚠`). Cosmetic; tidy at draft.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority/primary OR is marked `⚠ UNVERIFIED`/`⚠ verify at pin`
- [x] No folklore stated as fact (key-08 "records obsolete immutability" overclaim is explicitly defused; no 1:10:100, no MI-as-score)
- [x] No off-pin / moving-target citation asserted as settled (JEP 401 preview correctly `⚠ AHEAD-OF-PIN`; no JDK-feature-ahead-of-25 asserted)
- [x] Neutrality: no winner crowned, no banned phrasing, tools each cited to own source
- [x] Synthesized/causal/comparative claims supported (three-instruments-each-leaves-a-gap chain traces to dev.java + Oracle core-libs + EJ; thread-safety dividend attributed to JCiP/cluster key 21, not over-derived)
- [x] HONEST-LIMITATIONS floor met (each instrument's gap + a "When NOT to reach for it")

## Required fixes (for DRAFT, none block this dossier)
1. Before drafting tool-specific facts, run `/pin-source` and re-confirm S2384 title/category/CWE, PMD categories, Error Prone severity, Guava version + null wording, and `copyOf` Java-10 version against the pinned local fetch dirs.
2. Confirm verbatim wording + page numbers for EJ Item 17 (five rules; "separate object for each distinct value") and Item 50 ("copy before validating") before any block quote.
3. Keep JEP 401 as a clearly-labelled preview/horizon sidebar only; never in the compiled companion module.
4. Do not promote the Guava "95%/5%" figure from the scan log into the body.

## Learnings & pipeline suggestions
- **Recurring blocker across keys:** every Standard-band tool chapter (07/12/13/14/16/18/19 and now 10) stalls source-verify on the same root cause — `SOURCE-PIN` rows are `TO-PIN` and the local authority clone is unhealable (repo URL TBD). The `⚠ verify at pin` discipline is working as designed, but a real source-trace of tool facts is impossible until `/pin-source` runs. Recommend prioritising that OPEN ITEM before the next research batch so VERIFY can be machine-backed, not manual.
- **Reusable check:** scan dossier §9 (scan log) for weakly-evidenced "famous numbers" (here the Guava 95%/5%) that sit in provenance but must be barred from the body — add to the folklore-guard pass.
