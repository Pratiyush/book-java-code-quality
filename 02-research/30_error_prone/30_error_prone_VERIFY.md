# GATE REPORT — SOURCE-VERIFY (key 30, Error Prone + Refaster)

- **Gate:** SOURCE-VERIFY (pipeline step 2)
- **Artifact:** `02-research/30_error_prone/30_error_prone_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** (0 blockers)
- **Pin posture:** PRE-PIN. `check_source_pin.sh` FAILs by construction — SOURCE-PIN is multi-authority,
  every tool/JDK/spec row `TO-PIN`, clone URL is `{URL}` placeholder, `/pin-source` never run, ephemeral
  clone absent at `~/.claude/jobs/4a0aee36/tmp`. `verify_sources.sh` cannot trace (no clone).
  **This PASS means "flagged the right things," NOT "atoms byte-verified."** Atom re-trace is DEFERRED to
  after `/pin-source` (same caveat as keys 11/12/13/15/16/19/20/22/23/25).

## Scripts run vs manual
| Script | Result |
|---|---|
| `check_source_pin.sh` | FAIL by construction (multi-authority, `{URL}`, all `TO-PIN`, clone absent/unhealable) |
| `verify_sources.sh` | Cannot trace (no clone) — manual flag-discipline audit performed instead |
| `check_neutrality.sh` | FAIL on 1 token (`unlike`, line 566) + em-dash density 18/1000 — see findings F1/F5 |
| `ensure_source_pin.sh` | Not run — pin unhealable (placeholder URL); cannot bring on-pin |

## Checked claims / findings
| # | Location | Claim / atom | Finding | Fix |
|---|---|---|---|---|
| F1 | §9 Tooling note, line 566 | "...readable (no 403, **unlike** the openjdk JEP pages)" | NEUTRALITY blocklist literal `unlike` token. NOT a tool-quality crowning (it contrasts WebFetch fetchability of two doc sites, a process note) — same false-positive class logged for keys 20/23. But the literal banned token appears and the script FAILs. | Reword: "...readable (no 403; the openjdk JEP pages return 403)." Minor draft-fix; not a blocker. |
| F2 | §1, §2.x, §3, §4 (~38×) | Flag names / check identities / JDK-floor table annotated "**(verified)**" / "verified verbatim" | Pre-pin overclaim: "verified" reads as pin-verified, but no pin exists; the dossier's OWN §8 table correctly demotes all rows to "⚠ live-line, verify at pin." Same `(verified)/(confirmed)-without-pin` trap as keys 07/10/11/13/15/19/25. No invented fact — atoms are real on the live docs and re-flagged in §7. | In draft, replace body "(verified)" with "live-line" to match the §8 caveat. Reserve ☑/"verified" for post-`/pin-source`. Non-blocking. |
| F3 | §1, §2, §4, §6, §8 | Sibling-tool position claims — SpotBugs reads `.class` post-compile; Checkstyle/PMD parse source AST/text | Cross-tool factual claims. Each is neutrally framed ("different approaches"), the layering verdict is routed to key 37 (9×), and citation is **deferred** to each tool's own pinned source. Correct at pre-pin, but no inline cite at the assertion site yet. | At draft: attach each sibling-position claim to that tool's pinned source (keys 27/28/29) per NEUTRALITY §"cited-source requirement." Verdict stays in key 37. Non-blocking at research stage. |
| F4 | §1 line 94 / §7 | "origin a 2012 ICSE/research context" | Correctly marked `⚠ UNVERIFIED`, "color only, cite the repo not folklore." Clean — no folklore-as-fact. | None. Keep `⚠ UNVERIFIED`; never assert "since 2012" without the source. |
| F5 | whole file | Em-dash density 18/1000 (108 in 5806 words), over ~8/1000 ceiling | Clarity/style flag, not a source-verify atom. | Carry to CLARITY/draft gate; not a VERIFY blocker. |
| F6 | §2.7 / §2.6 | ON_BY_DEFAULT ERROR/WARNING/Experimental category lists; verbatim check descriptions (`DeadException` "Exception created but not thrown", etc.) | Identity + category verified live-line; exact default-on membership + per-check severity + byte-identical quotes are version-sensitive → correctly `⚠ verify at pin` (key-19 quote-drift guard applied in §7). | Re-trace §2.7 lists as ONE unit at `/pin-source`; re-confirm quotes byte-identical. Already flagged. |
| F7 | §2.5 / §7 | Refaster `com.google.errorprone.refaster.annotation.*` package path | Self-flagged "package recalled, not byte-verified → ⚠ verify at pin." Correct discipline. | Confirm package path at pin. Already flagged. |
| F8 | §1 / §5 | JDK-floor history 2.10.0→8 / 2.31.0→11 / 2.42.0→17 / current→21+; "must be run on JDK 21 or newer" | Cited to install doc; version-sensitive numbers unverifiable pre-pin. Tagged "(verified, install doc)" — same F2 overclaim. Doubtful-but-undisprovable specific version-floor figures → recommend web re-check at draft (no web access here). | Demote "(verified)" to "live-line"; re-confirm the floor table at `/pin-source`/draft web re-check. Non-blocking. |

## VERIFY gate-specific checks
- [x] Every specific atom (flags, check IDs, GAV, versions, quotes) traces to the tool's own docs OR is `⚠ verify at pin` / `⚠ UNVERIFIED`. (Live-line, deferred to pin.)
- [x] No folklore-as-fact (no FindBugs-as-current; "2012 origin" marked UNVERIFIED).
- [x] No off-pin / SNAPSHOT / newer-than-pin citation; no version number asserted (all `TO-PIN`).
- [~] NEUTRALITY — no tool crowned; layering verdict routed to key 37; ONE literal `unlike` token (F1, reword); sibling claims cite-deferred (F3).
- [x] Synthesized/comparative claims supported (in-`javac` type-attributed position is the load-bearing claim; sibling positions framed as approaches, verdict deferred).
- [x] HONEST-LIMITATIONS floor met: §4 gives hardest objections (every-compile cost, JDK coupling, build-failing-defaults adoption tax, FP/suppression, Refaster syntactic-not-semantic) AND when-NOT-to-use (pre-21 JDK, decoupled analysis, style/formatting).
- [x] Flag file present + accurate: `09-flags/30_error_prone_versions_and_defaults_unverified.md`.

## Blockers
None.

## Required fixes (carry to draft; none block research bank)
1. F1 — reword the line-566 `unlike` token (neutrality blocklist literal).
2. F2/F8 — demote body "(verified)" to "live-line" to match the §8 caveat; reserve ☑ for post-pin.
3. F3 — attach each sibling-tool position claim to that tool's pinned source at draft (verdict stays key 37).
4. Re-trace §2.6/§2.7 atoms (versions, GAV, default set, severities, verbatim quotes, Refaster package) as one unit after `/pin-source`.

## Learnings & pipeline suggestions
- The `check_neutrality.sh` `unlike`-token FAIL is a **recurring false positive** when "unlike" describes a
  fetch-tool / doc-site contrast rather than a tool-quality crowning (keys 20/23/30). Propose the scripted
  pre-pass whitelist a process/tooling context OR research dossiers avoid the literal token in §9 notes.
- "(verified)" in dossier BODY prose keeps recurring as a pre-pin overclaim even when §8 is disciplined
  (keys 07/10/11/13/15/19/25/30). Propose a lint: body "verified/confirmed" requires a pinned identifier;
  pre-pin use "live-line."
