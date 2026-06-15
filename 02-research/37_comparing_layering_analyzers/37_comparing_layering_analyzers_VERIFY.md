# GATE REPORT — SOURCE-VERIFY (key 37: Comparing & layering the analyzers)

**Gate:** SOURCE-VERIFY (pipeline step 2) · **Artifact:** `02-research/37_comparing_layering_analyzers/37_comparing_layering_analyzers_RESEARCH.md`
**Date:** 2026-06-15 · **Verdict:** **PASS_WITH_FLAGS** · **Blockers:** 0

## Pin / scripts status
- `check_source_pin.sh` — **FAIL by construction** (SOURCE-PIN is multi-authority; every §2/§4 row is `TO-PIN`; clone path is an
  ephemeral job dir and the re-fetch URL is the `{URL}` placeholder; `ensure_source_pin.sh --heal` cannot resolve). Same
  unhealable pre-pin posture as keys 11/12/13/15/19/20/22/23/25.
- `verify_sources.sh` — cannot trace (pinned clone absent). DEFERRED to `/pin-source`.
- `lint_citations.sh` — 13 "violations", ALL the known false-positive class: bare domains without an `http://` prefix +
  `☐ verify-at-pin` status rows. Not real defects.
- `check_neutrality.sh` — **blocklist clean.** Advisory FLAGs only (em-dash density, 1 filler word) → AUDIT/clarity lane, not VERIFY.
- This is a **pre-pin flag-discipline audit**, NOT atom byte-verification. A PASS here means "the right things are flagged,"
  not "atoms confirmed." Atom re-trace MUST run after `/pin-source`.

## Checked claims
| # | Claim / atom | Location | Finding |
|---|---|---|---|
| 1 | Substrate map (Checkstyle/PMD=source, SpotBugs=bytecode, Error Prone=`javac`, Sonar=platform, ArchUnit=bytecode/test) | §2.1, §2.5, §3 | Correct; each routed to that tool's OWN docs. Identity verified, versions `⚠ verify at pin`. OK |
| 2 | Error Prone javac wiring: `-Xplugin:ErrorProne`, `-XDcompilePolicy=simple`, `annotationProcessorPaths`, `--should-stop=ifError=FLOW` | §2.1/2.5/4/6/7/8/9 | Internally consistent; matches errorprone.info/docs/installation (live-line). Marked verify-at-pin. OK |
| 3 | Maven plugin GAVs (`maven-checkstyle-plugin`, `maven-pmd-plugin`, `spotbugs-maven-plugin`, `spotless-maven-plugin`, `error_prone_core`) | §2.5, §6 | GAV identity correct; all versions `⚠ verify at pin` / `TO-PIN`. No invented version. OK |
| 4 | Quoted abstract "little to no agreement among the tools and a low degree of precision" | §1, §2.5, §3, §7, §8, §9 | Verbatim full form in scan-log §9; shorter `… low precision` elisions are honest ellipses of the same string, not divergent wordings. Verified abstract only (PDF undecoded). OK |
| 5 | "47 Java projects" figure | 6 occurrences | Consistent everywhere; traces to cited study. OK |
| 6 | FindBugs framing (folklore guard) | §0, §1, §2.5, §3, §5, §7, §8, §9 | EXEMPLARY — every mention carries superseded→SpotBugs + dating caveat; never presented as current. Flag file matches. OK |
| 7 | Sonar `java:S###` rule IDs | §1, §2.1, §2.5, §7 | Only the rule-ID *family pattern* used; explicitly "never cite a specific ID from memory"; `rules.sonarsource.com` offline noted. OK |
| 8 | Synthesized claim "running several is additive in coverage, not merely redundant" | §1, §3 | Supported by the cited low-agreement study (dated, caveated). Claim-as-stated backed. OK |
| 9 | Off-pin / SNAPSHOT / version-newer-than-pin citations | whole file | NONE found. OK |
| 10 | Neutrality — crowning / banned phrasing | whole file | Clean. "no winner" (§Learnings) is anti-crowning. Each tool gets strongest case (§3) + hardest objection (§4). OK |
| 11 | Cross-tool claims cited to named tool's own source | §2.1, §2.3, §3, §4 | Each substrate/mechanism claim routed to that tool's own docs; the arxiv study used only as corroboration for the layering rationale. OK |
| 12 | HONEST-LIMITATIONS floor | §4 | Met for the 5 in-scope deep tools (Checkstyle/PMD/SpotBugs/Error Prone/Sonar) + shared-stack limits. See F2. |

## Findings (non-blocking)
- **F1 — Empirical finding verified from ABSTRACT only (live page, not a pin).** The arxiv abstract is a live URL; the PDF did
  not decode, so per-tool numbers are correctly deferred. *Fix:* keep the qualitative finding only unless the full paper is
  fetched at draft; do not promote any per-tool number. Already flagged (`37_empirical_overlap_findbugs_dated.md`). Doubtful-but-
  undisprovable without web → recommend web/`pdftotext` re-check at draft, not a fail.
- **F2 — HONEST-LIMITATIONS coverage uneven for non-deep tools.** ArchUnit, Spotless/formatters, IDE inspections, NullAway appear
  in the matrix/ownership tables but receive no hardest-objection + when-NOT-to-use paragraph in §4 (only the 5 core deep tools do).
  Defensible under the documented synthesis-node scope (their limitations are owned by 33/34/36/31), but the draft should either
  add a one-line limitation per table-named tool or state explicitly that their limits are delegated. Draft-time check.
- **F3 — Quote ellipsis hygiene (key-19/20 quote-drift sibling).** The `… low precision` short form omits "and a low degree of"
  via `…`. Acceptable as ellipsis but the draft must render any quoted span byte-identical to the abstract; carry a verbatim
  re-check marker on the elided form.
- **F4 — Pre-pin `☑`/`✅` marks.** §2.5/§6/§8 use `☑`/`✅` "identity/mechanism" against live-line docs while no pin exists
  (recurring keys 07/10/11/13/25 overclaim trap). Read as "live-line identity confirmed," NOT "verified at pin." Reserve `☑`
  for post-`/pin-source`; the header caveat already says this.
- **F5 — lint_citations 13 hits + DEMO-CATALOG row missing** are known structural false-positives / a catalog-owner task
  (already noted in §7), not VERIFY defects.

## Blockers
- None.

## VERIFY gate-specific checks
- [x] Every specific fact traces to the named tool's own source OR is marked `⚠ verify at pin` / `TO-PIN`.
- [x] No folklore-as-fact (FindBugs correctly framed as dead/dated everywhere; no 1:10:100, MI-as-score, coverage-as-quality).
- [x] No off-pin / SNAPSHOT / newer-than-pin citation.
- [x] Neutrality: no crowning, no banned phrasing; ⚠ comparisons balanced, each side cited; cross-cutting verdict owned here, not asserted as a ranking.
- [x] Synthesized/comparative claims (additive coverage) backed by a source that makes the claim.
- [x] HONEST-LIMITATIONS floor met for the in-scope deep tools (F2 caveat for table-named-only tools).
- [x] Required flag files present and accurate (`37_tool_gavs_and_defaults_unverified.md`, `37_empirical_overlap_findbugs_dated.md`).
- [x] No invented atom anywhere.

## Learnings & pipeline suggestions
- Synthesis-node HONEST-LIMITATIONS scope: when a `⚠` synthesis chapter names tools in tables but delegates their depth, define
  whether each table-named tool needs a one-line limitation here or an explicit "limit delegated to key NN" note. Propose adding
  to GATE-REPORT/CHAPTER-TEMPLATE so F2 is decided once, not per chapter.
- Reconfirms the recurring pre-pin patterns: unhealable `{URL}` pin → flag-discipline-only audit; reserve `☑`/`@the-pin` for
  post-`/pin-source`; lint_citations bare-domain false-positive class persists (the `lint_citations.sh` URL-scheme check should
  accept bare domains or the dossiers should be told to prefix `https://`).
- Quote-ellipsis re-check marker (key-19/20 sibling) applies again (F3).
