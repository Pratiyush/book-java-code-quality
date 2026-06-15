# GATE REPORT — SOURCE-VERIFY (key 25)

- **Gate:** VERIFY (step 2, pre-pin)
- **Artifact:** `02-research/25_static_concurrency_detection/25_static_concurrency_detection_RESEARCH.md`
- **Date:** 2026-06-15
- **Scripts:** `check_source_pin.sh` RAN (FAIL by construction — multi-authority pin, clone ABSENT,
  `{URL}`, all rows `TO-PIN`); `ensure_source_pin.sh --heal` OUT OF SCOPE for a verify-only task
  (healing is `/pin-source`'s job). `lint_citations.sh` RAN (16 structural violations — ALL known
  false-positives: bare-domain URLs + the print book-canon row, per keys 13/16/17). `verify_sources.sh`
  NOT run (cannot trace against an absent pin). `check_snippets.sh` N/A (no draft yet; §6 lists planned
  tags only). **Atom byte-verification is DEFERRED to post-`/pin-source`.** This gate audited
  flag-discipline, folklore, neutrality, AHEAD-OF-PIN labelling, internal consistency.

## Verdict: PASS_WITH_FLAGS (0 blockers)

A clean pre-pin flag-discipline pass. No invented or unflagged fact found; every version-sensitive atom
(GAV, severity, default-on membership, SpotBugs rank, IS2 threshold wording) carries `⚠ verify at pin`;
both required flag files exist and match the dossier; neutrality blocklist clean; no folklore-as-fact;
JEP 505 structured concurrency correctly `⚠ AHEAD-OF-PIN` throughout. PASS_WITH_FLAGS here means
"flagged the right things," NOT "atoms verified" — re-trace after `/pin-source`.

## Checked claims

| # | Claim / atom | Location | Status | Note |
|---|---|---|---|---|
| 1 | JLS SE 21 §17.4.5 data-race / correctly-synchronized verbatim defs | §1, §2.1, §2.6 | OK (flagged §7) | Cited to JLS SE 21 ch.17; re-confirm byte-identical + § numbers at draft (queued §7). |
| 2 | JLS §17.5 final-field publication; §17.7 long/double non-atomic | §2.1, §2.6 | OK (flagged §7) | Verbatim; queued for byte-check at draft. |
| 3 | JEP 444 Virtual Threads = final @21; no JMM change | §3, §5 | OK | Matches verified list keys 13/20/21/23/24. |
| 4 | JEP 506 Scoped Values = final @25 | §5 | OK | Correctly separated from JEP 505 (the easy-to-conflate trap). |
| 5 | JEP 505 Structured Concurrency = fifth preview @25 → AHEAD-OF-PIN | §intro, §5, §7, §8, §9 | OK | Marked everywhere; flag file filed; `StructuredTaskScope` never asserted stable. |
| 6 | Error Prone `GuardedBy` = ON_BY_DEFAULT ERROR; 3 annotation pkgs | §2.2, §2.6, §3 | IDENTITY OK | Severity/default-set verify-at-pin; FQNs named (good — key-25 4-package trap). |
| 7 | Error Prone `Immutable` ERROR / `ThreadSafe` experimental ERROR + WARNING checks | §2.2, §2.6 | IDENTITY OK | Per-check severities → `⚠ verify at pin`. |
| 8 | SpotBugs 20 MT_CORRECTNESS codes + verbatim short descriptions | §2.3, §2.6 | IDENTITY OK | Codes verified; rank/default-effort membership → verify-at-pin. |
| 9 | IS2_INCONSISTENT_SYNC "≤⅓ … writes weighed twice" + "various sources of inaccuracy" | §2.3, §4, §9 | OK w/ FINDING F1 | Quote-drift: two spellings in-body (see F1). |
| 10 | Checker FW Lock Checker soundness sentence + 6-annotation set | §2.4, §2.6, §3 | IDENTITY OK | Soundness sentence verbatim from live manual; re-confirm at pin (queued §7). |
| 11 | `@GuardedBy` = 4 FQN packages with distinct semantics | §1, §2.6, Learnings | OK | Correctly disambiguated; matches key-25 PIPELINE-LEARNINGS Lesson 2. |
| 12 | JCIP 2006 = doc-origin of `net.jcip.annotations.GuardedBy` (secondary, dated) | §1, §5, §8 | OK | Canon rule honored — cited for rationale only; primary (JLS) governs JMM facts. |
| 13 | GAV coordinates (error_prone_core, spotbugs-maven-plugin, jcstress-core, etc.) | §2.6, §6, §7 | DEFERRED | All `⚠ verify at pin` / `☐`; no version number asserted. Correct. |
| 14 | Neutrality — three tools as proxies, no crowning | §2.5, §4 | OK | Blocklist clean; comparison framed as approaches; each gets hardest objection + when-NOT. |
| 15 | HONEST-LIMITATIONS floor — per-tool + shared-undecidability centre | §4 | OK | Each tool has hardest-objection + when-NOT-to-use; shared limits (deadlock/liveness/runtime) stated. |

## Findings (all minor; none blocks)

- **F1 (quote-drift, key-19 trap).** IS2_INCONSISTENT_SYNC threshold quote appears in two spellings:
  §2.3 "no more than one third of all accesses, with writes being weighed twice as high as reads" vs
  §4 (line 295) "no more than one third of all accesses … unsynchronized, writes weighed twice." Only one
  can be byte-identical. **Fix:** at draft, quote one byte-identical form from the pinned SpotBugs version;
  demote the other to paraphrase. §7 already queues the byte-check — extend it to lint same-quote spelling.
- **F2 (pre-pin "☑ / verified @pin" overclaim, recurs keys 07/10/11/13/15).** §8 header reads "Primary /
  Official (verified by direct fetch @the pin)" and 10 `☑` marks sit in §8/§6 though NO pin exists (clone
  absent, all rows `TO-PIN`). These were **live-line** fetches, not pin-verified. **Fix:** reword to
  "live-line, verify at pin" and reserve `☑` for post-`/pin-source` byte-checks. Identity facts ARE
  separated from version/severity facts (which carry `⚠`), so this is wording, not a fact defect.
- **F3 (scan-log provenance, trivial).** §9 line 513 lists preview chain "425/453/480/446/487" — mixes the
  VT (425), SC (453/480/505) and Scoped-Values (446/487) chains in one note. Provenance only; the body and
  flag file state the correct SC chain (453→480→505). **Fix:** none required; tidy at draft if kept.
- **F4 (lint_citations false-positives).** 16 violations are all known script limitations (bare-domain
  URLs without `http://`; the `print` book-canon row with no URL/date). Not dossier defects. Same as
  keys 13/16/17 — script tune is an OPEN ITEM.

## Blockers
- None.

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority OR is marked `⚠ verify at pin` / `⚠ UNVERIFIED` / `⚠ AHEAD-OF-PIN`.
- [x] No folklore-as-fact (no 1:10:100, MI-as-score, coverage-as-quality, reified-generics, records-obsolete-immutability).
- [x] No off-pin / moving-target citations stated as stable; preview JDK features (JEP 505) marked AHEAD-OF-PIN.
- [x] Neutrality: no blocklist phrase; no crowning; comparison is approach-based; each tool cited to its own source.
- [x] Synthesized/causal/comparative claims supported (undecidability→proxy→FP/FN reasoning anchored in JLS §17.4.5; VT-no-JMM-change cited to JEP 444 + JLS ch.17).
- [x] HONEST-LIMITATIONS floor met (hardest-objection + when-NOT-to-use for each of the 3 tools, plus shared limits).
- [x] Both required flag files present and consistent with the dossier.
- [ ] Atom byte-verification — DEFERRED to post-`/pin-source` (pin absent; cannot machine-trace).

## Learnings & pipeline suggestions
- Pre-pin SOURCE-VERIFY on this multi-authority book remains a flag-discipline audit, not atom verification
  (consistent with keys 12/15/16/17). PASS_WITH_FLAGS = "flagged the right things."
- F1 reinforces the key-19 proposal: a `lint_citations.sh` same-quote-spelling-drift check would have caught
  the IS2 quote in two forms automatically.
- F2 reinforces the standing "reserve ☑ for post-pin byte-checks; pre-pin use 'live-line, verify at pin'"
  candidate rule (keys 07/10/11/13/15) — strong candidate to promote into GATE-REPORT-TEMPLATE.
- The key-25 4-package `@GuardedBy` discipline (Lesson 2 in PIPELINE-LEARNINGS) is honored throughout — a
  good model for keys 28–35.
