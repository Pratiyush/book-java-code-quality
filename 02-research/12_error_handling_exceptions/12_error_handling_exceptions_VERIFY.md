# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** SOURCE-VERIFY
- **Key:** 12 — Error handling & exceptions done right
- **Artifact:** `02-research/12_error_handling_exceptions/12_error_handling_exceptions_RESEARCH.md`
- **Date:** 2026-06-15
- **Pin status:** SOURCE-PIN.md is multi-authority and every tool/JDK/spec row is **TO-PIN** (unpinned).
  `check_source_pin.sh` → FAIL (no git checkout; clone path absent). No pinned source text is fetchable at
  this stage, so verification is **internal-consistency + folklore + neutrality + flag-discipline** only;
  exact rule IDs/defaults/JEP-levels are confirmable only after `/pin-source`. The dossier is written to
  match this state (every version-sensitive atom carries `⚠ verify at pin` and is filed to `09-flags/`).
- **Scripts:** `check_source_pin.sh` run (FAIL — pre-pin, expected). `verify_sources.sh` / `lint_citations.sh`
  not run against pinned text (nothing pinned to verify against). Manual cold read performed.

## Checked claims / issues

| # | Claim / atom | Location | Status |
|---|---|---|---|
| 1 | JLS SE 21 §11 hierarchy, §11.2 catch-or-specify, §14.20/.3 constructs | §1, §2.1 | OK — language facts, edition stated; recommend spec re-check at pin |
| 2 | EJ 3e Items 69–77 titles + Item 70 directives | §1, §2.2 | Flagged correctly — "verbatim sense", §7 notes confirm wording/page before block-quoting |
| 3 | JEP 358 default-on **since JDK 15** | §2.1, §5, §7 | Flagged ⚠ UNVERIFIED (§7 + flag #1) — correct, not asserted as settled |
| 4 | Sonar `java:S112/S1166/S2221/S1181/S108` (vs legacy `squid:`) | §2.5, §8 | Flagged ⚠ verify-at-pin (§7 + flag #2) — IDs not asserted as fact |
| 5 | PMD 6→7 rule moves (`AvoidCatchingGenericException`, `typesThatShouldNotBeCaught`) | §2.5, §7 | Flagged ⚠ verify-at-pin (§7 + flag #3) |
| 6 | Checkstyle / SpotBugs default lists & pattern IDs | §2.5, §7 | Flagged ⚠ verify-at-pin (§7 + flags #4/#5) |
| 7 | JEP numbers 441/409/395/440/453 | §2.4, §5, §8 | Flagged ⚠ confirm-at-pin (§7 + flag #6) |
| 8 | `StructuredTaskScope` = **preview** at 21, preview through 25 | §2.4, §4, §5, §7, §8 | Correct — marked `⚠ AHEAD-OF-PIN`/preview consistently; not presented as stable |
| 9 | Sealed/records/pattern-switch presented as modelling **approach**, not winner over exceptions | §2.4, §3, §4 | Neutral — explicit "not as a winner over exceptions" |
| 10 | Folklore scan (10×/100×, MI-as-score, coverage-as-quality, reified generics, records-replace-immutability) | whole file | None present — clean |
| 11 | Banned phrasings (better than / unlike X / superior / beats / the problem with X / wins / best) | whole file | None present — clean |
| 12 | HONEST-LIMITATIONS floor (each option: hardest objection + when-NOT-to-use) | §4 | Met — checked-exc, tool false-positives, sealed/Result, JEP 358, finally |
| 13 | PMD false-positive issue numbers #422/#844/#5318 | §4 | Specific & citable but unverifiable here — recommend web re-check at draft |
| 14 | Flag file `09-flags/12_jep358_default_level_and_rule_ids.md` | §7, learnings | Exists and matches dossier flags — correct |

## Verdict

**PASS_WITH_FLAGS.**

No invented-and-unflagged fact, no folklore-as-fact, no off-pin/moving-target citation asserted as
settled, no neutrality breach. Every version-sensitive atom is correctly marked `⚠ verify at pin` /
`⚠ AHEAD-OF-PIN` and filed to `09-flags/`. The flags exist because the authority set is unpinned, not
because of dossier error. The dossier is the model of correct pre-pin discipline.

## Required fixes (none blocking; resolve at draft / pin)

1. Run `/pin-source` to pin tools/JDK/specs, then re-trace and confirm: Sonar IDs (S112 vs squid:S00112,
   S1181, S108), PMD 6→7 moves, Checkstyle/SpotBugs defaults & pattern IDs, JEP numbers, and JEP 358
   default-on-since-15. (Carried in flag #12.)
2. Before block-quoting EJ Item 70/71 beyond fair-use sense, confirm exact wording + page from the 3e text.
3. Web re-check the PMD false-positive issue numbers (#422/#844/#5318) at draft.
4. Keep `StructuredTaskScope` marked preview/AHEAD-OF-PIN through to the draft — do not let a drafter
   present it as stable.

## Learnings & pipeline suggestions

- **Pre-pin SOURCE-VERIFY is mostly a flag-discipline audit.** With every authority TO-PIN, this gate
  cannot confirm rule-ID/default atoms against source text; its real job at this stage is to confirm that
  each such atom is *marked* (not asserted) and *filed*. The dossier passing means it flagged the right
  things, not that the IDs are correct. Worth stating in the gate template so PASS_WITH_FLAGS at pre-pin
  is not mistaken for full atom verification.
- **Item-to-rule crosswalk** (EJ Item N → enforcing rule, each cited to its own pinned source) is a clean,
  reusable shape and was applied neutrally here — endorse for keys 08/10/14/15/16/18.
