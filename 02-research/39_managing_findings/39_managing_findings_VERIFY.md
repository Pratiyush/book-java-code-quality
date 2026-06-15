# GATE REPORT — SOURCE-VERIFY (key 39)

- **Gate:** SOURCE-VERIFY (step 2, pre-pin)
- **Artifact:** `02-research/39_managing_findings/39_managing_findings_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** PASS_WITH_FLAGS — 0 blockers
- **Scripts:** `check_source_pin.sh` RUN (FAIL by construction — pin absent/unhealable, `{URL}`, `multi-authority`
  tag, `/pin-source` never run); `verify_sources.sh` RUN (cannot trace, no clone); `lint_citations.sh` RUN
  (26 violations = the known bare-domain-URL + `☐`/`live-line`-status false positives, same class as keys
  11/12/19/20/22/23/25). No script claimed to have run that did not.

## Pre-pin caveat (per key-12 learning)
PASS_WITH_FLAGS here means "flagged the right things," NOT "atoms byte-verified." SOURCE-PIN is multi-authority
with every Part-IV tool/plugin row `TO-PIN`; no pinned text is fetchable. Identity (annotation names, filter
element names, CLI flags, config keys) is confirmable from each tool's live docs; exact versions/defaults/
renamed-status are NOT — they are correctly marked `⚠ verify at pin` and filed. Atom re-trace must run after
`/pin-source`. No web access this pass: doubtful-but-undisprovable items → "recommend web re-check at draft."

## Checked claims / issues

| # | Claim / check | Location | Result |
|---|---|---|---|
| 1 | Pin present & on-pin | scripts | FAIL by construction (pre-pin); expected, non-blocking |
| 2 | Banned-phrasing blocklist | whole file | CLEAN (none) |
| 3 | No crowning; cross-tool verdict routed to key 37 | §1 header, §4 last ¶, line 329 | CLEAN — verdict explicitly deferred to key 37 |
| 4 | Folklore-as-fact (FindBugs-as-current, 10x/100x, coverage-as-quality) | whole file | CLEAN — every "FindBugs" hit is the real `FindBugsFilter` element / `edu.umd.cs.findbugs.annotations` package; §5 line 350 explicitly states "FindBugs is dead → SpotBugs; never cite findbugs-maven-plugin as current" (guard working) |
| 5 | Off-pin / moving-target / newer-than-pin citations | §8, §2 | NONE asserted as current; all version-sensitive atoms `⚠ verify at pin` |
| 6 | HONEST-LIMITATIONS floor (each lever: hardest objection + when-NOT-to-use) | §4 | MET — 4 levers, each with both; plus a shared-limit "honest centre" |
| 7 | Sonar "Won't Fix"→"Accepted" rename | §2.2/§2.7/§5/§7 | CORRECTLY marked `⚠ verify at pin`; flag `09-flags/39_sonar_wontfix_accepted_rename_unverified.md` present & accurate |
| 8 | Tool versions / GAVs / defaults `failOnViolation`, `violationSeverity`, `baselineFiles`, `--suppress-marker` | §1/§2.7/§6/§7 | CORRECTLY `⚠ verify at pin`; flag `09-flags/39_tool_versions_and_suppression_defaults_unverified.md` present & accurate |
| 9 | `EI_EXPOSE_REP` example pattern code | §6 TRY-IT | CORRECTLY `⚠ verify at pin` against pinned bugDescriptions.html |
| 10 | Synthesized/comparative claims supported | §3/§4 | Each tool's surface cited to its own doc; "different approaches to the same need" framing is balanced, each side named + cited (NEUTRALITY §"cited-source requirement") |
| 11 | SpotBugs `@SuppressFBWarnings` class-retention behavioral claim | §2.2 line 137-141 | Plausible identity claim; cannot byte-verify offline → recommend web re-check at draft (not failed) |

## Findings (minor / draft-fix — none blocking)

| F | Finding | Location | Fix |
|---|---|---|---|
| F1 | **Quote-drift (recurring, keys 19/20/25):** SpotBugs baseline quote in TWO spellings — "bugs found in the baseline files won't be reported" (§2.4 L192) vs "bugs in the baseline won't be reported" (§3 L264). Only one can be verbatim. | L192, L264 | Make byte-identical to the mojo doc at draft, or demote one to paraphrase (no quotes). |
| F2 | **Quote-drift:** Sonar new-code gate quoted as "The default Quality gates applies conditions only to new code issues" (L210) but trimmed to "conditions only to new code" elsewhere (L268-269, scan-log L506). | L210, L268, L506 | Reserve double-quotes for the one verbatim span; mark the trimmed forms as paraphrase. Verify the odd "gates applies" grammar verbatim at draft. |
| F3 | **Unverifiable verbatim quotes (no web this pass):** Sonar "ignores both accepted issues and false positive issues" (L154); Checkstyle "only works in conjunction with a SuppressWarningsHolder" (L123); Checkstyle "To allow users to use suppressions configured in the same config…" (L127). | L123, L127, L154 | Recommend web re-check at draft (byte-for-byte). Not failed — undisprovable offline. |
| F4 | **lint_citations 26 violations** = known bare-domain (no `http://` prefix) + `☐`/`live-line`-status false positives. | §8 tables | No action (known linter limitation); confirm URLs resolve at `/pin-source`. |

## Blockers
None.

## VERIFY gate-specific checks
- [x] Every specific atom traces to that tool's own doc OR is marked `⚠ verify at pin`/`⚠ UNVERIFIED`.
- [x] No folklore-as-fact (FindBugs-as-current explicitly guarded).
- [x] No off-pin / SNAPSHOT / newer-than-pin citation asserted as current.
- [x] NEUTRALITY: no crowning, no banned phrasing; multi-tool comparison balanced + each side cited; cross-cutting verdict routed to key 37.
- [x] HONEST-LIMITATIONS floor met per lever (hardest objection + when-NOT-to-use).
- [x] Both required flag files present and consistent with the dossier.
- [ ] Atom bytes verified — DEFERRED to `/pin-source` (pre-pin).

## Learnings & pipeline suggestions
- Quote-drift recurs again (F1/F2) — reinforces the proposed `lint_citations.sh` same-quote-spelling check
  (keys 19/20/25). A repeated double-quoted external span must be byte-identical or demoted to paraphrase.
- The "four-lever scope ladder" shape makes NEUTRALITY structural (each tool = a surface for the same four
  levers); SOURCE-VERIFY confirms the floor falls out per lever cleanly — good reuse signal for keys 76/80/87.
- Pre-pin SOURCE-VERIFY remains a flag-discipline audit, not atom verification (key-12 caveat) — re-run after
  `/pin-source` for byte verification of versions/defaults/rename and the three F3 verbatim quotes.
