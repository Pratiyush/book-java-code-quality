# GATE REPORT — SOURCE-VERIFY (key 36, IDE inspections as the first line)

- **Artifact:** `02-research/36_ide_inspections/36_ide_inspections_RESEARCH.md`
- **Gate:** SOURCE-VERIFY (pipeline step 2) · **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** · **Blockers:** 0

## Scripts (run vs manual)
- `check_source_pin.sh` — **RAN, FAIL by construction.** Pin is multi-authority, every row `TO-PIN`,
  repo URL is a `{URL}` placeholder, clone absent/ephemeral. Same pre-pin state as keys 11/12/19/20/22/23/25.
- `verify_sources.sh` — **RAN, FAIL by construction** ("pinned clone absent"); cannot atom-trace pre-pin.
- `lint_citations.sh` — **RAN.** 13 "no plain-text URL" violations = the known false-positive class
  (the dossier's own house style uses bare-domain URLs + ☐/☑ status cells, not `[text](url)`). Not real defects.
- `check_snippets.sh` — n/a (research dossier, no `<!-- include: -->` markers).

> **Pre-pin caveat (keys 12/19/22/23/25):** a SOURCE-VERIFY PASS_WITH_FLAGS at pre-pin means "the right
> version-sensitive atoms are *flagged*," NOT "atoms byte-verified." Atom re-trace MUST run after `/pin-source`.

## Checked claims / issues

| # | Claim / atom checked | Location | Finding | Fix |
|---|---|---|---|---|
| 1 | IntelliJ severity *set* (Error/Warning/Weak Warning/Server Problem/Grammar Error/Typo/Consideration/No-highlighting) | §2.2, §2.6, §8 | Identity verified from vendor doc; verbatim meanings carried; correctly `verify at pin` for default-severity/membership | none — re-trace bytes at pin |
| 2 | Highlighting levels None/Syntax/Essential/All Problems (default) | §1, §2.2 | Identity verified; "All Problems = default" marked IDE-version | none |
| 3 | `inspect.sh <project> <profile> <output>` flags `-changes/-d/-format`(xml/json/plain)/`-v0/1/2` + "won't work if another instance running" | §2.2, §2.6, §8 | CLI flag identity verified, constraint quoted verbatim, `verify at pin` on version | none |
| 4 | Actions on Save (Reformat/Optimize imports/Rearrange/Run code cleanup) | §1, §2.6 | Option-name identity verified | none |
| 5 | Eclipse Save Actions (Format/Organize imports/Additional actions→Clean Up) | §2.3, §2.6 | Verbatim option names; Clean-Up membership correctly `verify at pin` | none |
| 6 | Eclipse Compiler Errors/Warnings = Ignore/Warning/Error | §2.3, §7 | Stated from JDT/IBM mirror; §7 correctly queues re-confirm of exact dropdown + any "Info" level at pin | none |
| 7 | EditorConfig overrides project style (verbatim) | §2.4, §3 | Verbatim from IDEA help; identity verified | none |
| 8 | Qodana = same engine in CI, SARIF `qodana.sarif.json`, quality gates; EAP 2020 | §1, §5, §7 | Named as IDE→CI bridge only; GA date + config keys correctly `verify at pin`; "cite docs not blog for atoms" stated | none |
| 9 | SonarLint → "SonarQube for IDE" rename | §3, §5, §7 | Current name used, historical name noted, rename date `verify at pin` — folklore-rename discipline honored | none |
| 10 | No off-pin / SNAPSHOT / newer-than-pin citation | whole | None. No "since version X" asserted; all version atoms deferred. Moving-target policy honored | none |
| 11 | No folklore-as-fact (FindBugs-as-current, 1:10:100, MI-as-score, coverage-as-quality) | whole | grep clean — no FindBugs, no 10x/100x figure | none |
| 12 | NEUTRALITY blocklist (better than / unlike X / superior / beats / problem with X …) | whole | grep clean; all "crown/winner/wins" hits are explicit *no-crowning* statements | none |
| 13 | Each IDE strongest case AND hardest limitation (HONEST-LIMITATIONS floor) | §3 + §4 | Met: §4 gives IntelliJ, Eclipse, save-actions-both, and a shared-centre each a hardest objection + when-NOT-to-use | none |
| 14 | Cross-cutting layering verdict routed to key 37, not asserted here | §1, §2.5, §4, §7 | Honored consistently; no "which layer wins" verdict asserted | none |
| 15 | Cross-tool facts cited to the named vendor's own doc (NEUTRALITY cited-source rule) | §2.5, §8 | Each IDE row sourced to its own vendor help; no competitor-marketing cross-cites | none |
| 16 | Flag files exist + match dossier §7 | §7, 09-flags/ | Both `36_ide_authorities_not_pinned.md` + `36_ide_versions_and_defaults_unverified.md` present and consistent | none |

## Findings (non-blocking, draft-fix)

- **F1 (folklore-adjacent phrasing).** §3 "a fix offered at the keystroke costs **orders of magnitude** less
  attention than the same finding at PR time." Qualitative and tied to the shift-left rationale (key 06), NOT a
  cited numeric multiplier — but it sits next to the debunked 1:10:100 defect-cost curve (PIPELINE folklore list).
  *Fix at draft:* keep it qualitative ("cheaper/faster feedback"), or attribute the cost framing to key 06's
  cited shift-left treatment; never let it imply the debunked cost-multiplier curve.
- **F2 ("(verbatim ☑)"-without-pin, recurs keys 19/22/25).** §8 source rows carry "(verbatim ☑)" / "(identity ☑)"
  while the pin column correctly reads "☐ verify at pin" and the header says live-line. The ☑ means
  *identity confirmed from the live vendor page*, not *pin-verified*. Acceptable, but reserve ☑/"verified" for
  post-`/pin-source`; today read as "live-line, verify at pin."
- **F3 (Eclipse perf-issue color).** §4 + Accessible #1 cite JDT issues #1408/#3767 for organize-imports-on-save
  performance, correctly framed "reported, not asserted as a current defect at the pin." Good — keep the
  "reported" framing in the draft; do not promote to fact.
- **F4 (SOURCE-PIN gap, material but flagged).** IntelliJ IDEA / Eclipse JDT / Qodana are key 36's primaries
  but have no SOURCE-PIN §2 row (same class as key-24 JCStress). Flagged + proposed as an open item — not a
  blocker for the dossier, but must be pinned before draft atom re-trace.

## Blockers
- None.

## VERIFY gate-specific checks
- [x] Every specific atom either traces to the named vendor's own doc (identity) or is marked `⚠ verify at pin` / `UNVERIFIED`.
- [x] No fact invented or "fixed" by the verifier.
- [x] No folklore-as-fact; no FindBugs-as-current.
- [x] No off-pin / SNAPSHOT / newer-than-pin citation; moving-target policy honored.
- [x] NEUTRALITY: no blocklist phrase, no crowning, ⚠ comparison balanced + each side cited, layering verdict routed to key 37.
- [x] HONEST-LIMITATIONS floor met (each IDE + save-actions + shared-centre: hardest objection + when-NOT-to-use).
- [x] Synthesized/comparative claims supported (the §2.5 axis contrasts approaches, crowns none, cites each side).
- [x] Both required flag files present and consistent with §7.
- [ ] Atom byte-verification — DEFERRED to `/pin-source` (pin absent by construction).

## Learnings & pipeline suggestions
- Reconfirms the recurring **pre-pin reality**: for this multi-authority book a SOURCE-VERIFY PASS at step 2 is a
  *flag-discipline* pass, not atom verification. Continue to surface this in the report header (done).
- The **"(verbatim ☑)"-without-pin** pattern recurs again (keys 19/22/25/36). Worth promoting the standing rule:
  reserve ☑/"verified" for post-`/pin-source`; pre-pin use "live-line, verify at pin."
- The **F1 folklore-adjacent "orders of magnitude" phrasing** is a new minor trap for *author-time / shift-left*
  chapters (keys 05/06/82/36): the shift-left rationale invites the debunked cost-curve numeric. Note for the
  clarity/audit gates to watch for it in any left-shift chapter.
- SOURCE-PIN IDE-row gap (key 36) is the same recurring class as key-24 (JCStress) and the named-canon gaps —
  reinforces the standing open item to add an "IDEs / IDE-platform analyzers" sub-group to SOURCE-PIN §2.
