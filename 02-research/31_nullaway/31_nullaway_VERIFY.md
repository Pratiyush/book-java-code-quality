# GATE REPORT — SOURCE-VERIFY · key 31 (NullAway)

- **Gate:** VERIFY (step 2, pre-pin)
- **Artifact:** `02-research/31_nullaway/31_nullaway_RESEARCH.md`
- **Date:** 2026-06-15
- **Scripts:** `check_source_pin.sh` = FAIL by construction (multi-authority pin, all rows `TO-PIN`,
  `{URL}` placeholder, clone absent/unhealable). `verify_sources.sh` = cannot trace (no clone).
  `lint_citations.sh` = 13 violations, all the known bare-domain / `☐`-status false positives.
  Neutrality grep (manual; `check_neutrality.sh` pre-pass not gating) run by hand.
- **Verdict:** **PASS_WITH_FLAGS** · **Blockers: 0**

> **PRE-PIN CAVEAT (keys 11/12/19/20/22/23/25):** a pre-pin PASS_WITH_FLAGS means "the right things are
> flagged," NOT "atoms byte-verified." All identity-vs-version atoms must be re-traced after `/pin-source`.

## Checked claims / issues

| # | Claim / atom | Location | Finding | Required fix |
|---|---|---|---|---|
| 1 | Repo description "eliminate NPEs … low build-time overhead"; "type-based, local checks" | §1 | Quoted, attributed to repo; identity-marked, version `⚠ verify at pin` | Re-quote byte-exact at pinned tag |
| 2 | Three modular assumptions; "callees perform no mutation (unsound)"; "optimistic default" | §2.1/2.3/4 | Quoted from *How NullAway Works*; the neutrality + HONEST-LIMITATIONS anchor | Re-confirm verbatim at pin |
| 3 | Flag family `-Xep:NullAway:ERROR`, `-XepOpt:NullAway:*` (18 flags, §2.6 table) | §2.2/2.6 | Identity from wiki Configuration; since-versions (0.12.0/0.12.3/0.12.8) `⚠ verify at pin` | Re-trace flag identity + since-version at pin |
| 4 | Error-message stems (§2.5) | §2.5 | Live-wiki stems; flagged "re-confirm verbatim" | Quote, don't paraphrase, at pin |
| 5 | GAV `com.uber.nullaway:nullaway`; `org.jspecify:jspecify:1.0.0`; MIT | §0/2.6/8 | Identity OK; version `0.13.6` correctly `⚠ AHEAD-OF-PIN` (5 Jun 2026, past cutoff) | Confirm pinned version |
| 6 | "less than 10%" (repo); "1.15X … (2.8-5.1X)" (FSE'19) | §3/§7 | Comparative figures cited to NullAway's OWN paper (NEUTRALITY-compliant) | Re-quote byte-exact at pinned paper |
| 7 | Per-tool split Eradicate 2.8× / CFNullness 5.1× | §3/§4/Fig 31.2 | Split confirmed via WebSearch (scan-log #8), NOT paper text directly | Confirm the per-tool attribution against the paper at draft |
| 8 | "never due to NullAway's unsound assumptions for checked code"; 64/17/17% NPE split | §2.1/§3 | Quoted from abstract; the empirical neutrality anchor | Re-confirm verbatim |
| 9 | Min **JDK 17** / **Error Prone ≥ 2.36.0** | §0/2.2/4/5 | Documented, `⚠ verify at pin`. NOTE potential cross-source tension: key-30 learning records EP's own floor "must be run on JDK 21 or newer" — NullAway-min-17 vs EP-min-21 may conflict | Re-check NullAway JDK floor vs its required EP's JDK floor at pin |
| 10 | `@Nullable` matched by SIMPLE NAME; `org.jspecify` recommended | §3/§2.6/Learnings | Correctly states package not load-bearing for recognition but IS for recommendation | — |
| 11 | §8 source table uses `☑` on rows 1-9 | §8 | Pre-pin overclaim trap (keys 07/10/11/13/15/20/22/25): header says "reserve ☑/@the-pin for post-`/pin-source`" yet table marks ☑. MITIGATED — marks are scoped ("☑ identity", "☑ design verbatim"), not "@the pin", and version atoms carry `⚠ verify at pin` alongside | Demote ☑ to "live-line, verify at pin" at draft, or keep the explicit identity-vs-version split |
| 12 | Gradle setup snippet | §2.2 | 9 lines, within ≤9 ceiling (self-annotated) | — |

## Gate-specific checks
- [x] Every specific atom (GAV, flags, annotations, versions, figures, quotes) traces to NullAway's OWN repo/wiki or the FSE'19 paper, OR is marked `⚠ verify at pin` / `⚠ AHEAD-OF-PIN` / `⚠ UNVERIFIED`.
- [x] No folklore-as-fact; no FindBugs-as-current; debunked-number guard clean.
- [x] No off-pin/moving-target citation asserted as fact — `0.13.6` flagged `⚠ AHEAD-OF-PIN`; no `-SNAPSHOT`/main.
- [x] NEUTRALITY: no banned phrasing (the one "unlike" = "unlike a rule ID", a concept not a named rival); no crowning (every "best/crown/winner" token is a NON-crowning statement); comparative figures balanced and cited to NullAway's own paper; cross-cutting "which to pick" verdict routed to key 37 (5×), annotation ecosystem to key 32.
- [x] Synthesized/comparative claims supported: the proxy-trade-off spine, the 1.15×/2.8×/5.1× positioning, and "optimism rarely bites checked code" all trace to the paper/wiki that make the claim (not stitched from parts).
- [x] HONEST-LIMITATIONS floor met: hardest objection (intentional unsoundness → false negatives, not a proof) + full when-NOT-to-use (soundness-required, unannotated codebase, runtime-strategy, heavy generics).
- [x] Required flag files present: `09-flags/31_nullaway_version_and_minimums_unverified.md`, `09-flags/31_nullaway_overhead_figures_unverified.md` — both match dossier claims.

## Blockers
None.

## Findings to carry to draft (non-blocking)
1. (F7) Per-tool 2.8×/5.1× split came from WebSearch, not the paper text — byte-confirm the per-tool attribution at the pinned paper before drawing Fig 31.2.
2. (F9) Reconcile NullAway-min-JDK-17 against its required Error Prone's own JDK floor (key-30: "JDK 21 or newer") at pin — possible effective floor of 21.
3. (F11) §8 ☑ contradicts its own "reserve ☑ for post-pin" header — demote to "live-line, verify at pin" (recurring across keys).

## Learnings & pipeline suggestions
- **Comparative-figure-in-own-paper is a clean NEUTRALITY path, but verify the SPLIT not just the range.** The
  paper's quoted range "(2.8-5.1X)" is one citeable atom; decomposing it into per-rival points (Eradicate 2.8×,
  CFNullness 5.1×) is a SECOND atom that must trace to the paper's own per-tool table — confirmed here only via
  WebSearch. Propose a rule: when a positioning diagram splits a quoted range into per-rival points, each point
  needs its own byte-cite from the source.
- **Cross-tool requirement-floor consistency (new check):** a tool whose host has its own JDK floor can state a
  LOWER min than its host requires (NullAway 17 vs EP 21 per key 30). Cross-check a chapter's tool-min against
  its named host's tool-min recorded in a sibling key before asserting the lower number.
- Reconfirms: pre-pin `☑`-in-§8 overclaim trap, bare-domain lint false-positives, and the version-ahead-of-pin
  flag pattern for fast-releasing `0.x` tools.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
