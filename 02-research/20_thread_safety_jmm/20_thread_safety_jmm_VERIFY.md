# GATE REPORT — SOURCE-VERIFY (Step 2)

- **Gate:** VERIFY (SOURCE-VERIFY)
- **Key / artifact:** 20 — Thread-safety as a quality dimension — the Java Memory Model in practice
  · `02-research/20_thread_safety_jmm/20_thread_safety_jmm_RESEARCH.md`
- **Date:** 2026-06-15
- **Scripts:** run, but **NOT machine-authoritative** (pre-pin / multi-authority) —
  - `check_source_pin.sh` → **FAIL by construction**: pin ABSENT + unhealable (`{URL}`,
    `multi-authority` tag, `/pin-source` never run). Heal is out of scope for VERIFY (it is `/pin-source`'s job).
  - `verify_sources.sh` → cannot trace tool/spec atoms (no clone). No machine atom verification possible.
  - `lint_citations.sh` → 20 "violations", **all the known bare-domain / `⚠`-status false positives**
    (regex wants `http://` prefix + a date token; book house style is bare domains + `☑`/`⚠`/`☐` status).
    Not dossier defects (cf. keys 12/13/16/17 learnings).
  - `check_snippets.sh` → N/A at step 2 (no draft / include markers yet).
- **Mode:** MANUAL flag-discipline + folklore + neutrality + AHEAD-OF-PIN + HONEST-LIMITATIONS audit.
  A pre-pin PASS_WITH_FLAGS means "flagged the right things," NOT "atoms byte-verified." Atom re-trace
  is mandatory after `/pin-source`.

## VERDICT: **PASS_WITH_FLAGS**  (0 blockers)

The dossier invents no fact, asserts no folklore, crowns no tool, JDK/JEP `Release` fields match the
book's verified list (keys 13/24), version-sensitive tool atoms are correctly identity-only with
`⚠ verify at pin`, preview features are `⚠ AHEAD-OF-PIN`, and the HONEST-LIMITATIONS floor is met
per lever. Both required flag files are filed and match the dossier. Findings are draft-cleanup /
verify-at-draft, not fact defects.

## Checked claims / atoms

| # | Claim / atom | Where | Status |
|---|---|---|---|
| 1 | JLS §17.4.1/.4.5/.5/.6/.7 section numbers (SE 21) for shared mem / hb / data race / final / tearing | §1,§2,§2.7 | ✓ correct for SE 21; SE 25 re-confirm correctly queued §7 |
| 2 | hb / data-race / correctly-synchronized verbatim quotes | §1,§2 | ✓ consistent w/ JLS §17.4.5; verbatim re-check at draft (no web here) |
| 3 | "...appear to be **[sequentially consistent]**" bracketed insert | §1 (l.75) | FLAG F1 — editorial bracket; make verbatim or mark paraphrase at draft |
| 4 | §17.5 final-field guarantee + this-escape usage model (verbatim) | §2.3 | ✓ matches §17.5; long quote — re-check verbatim at draft |
| 5 | §17.7 long/double non-atomic + volatile-atomic carve-out (verbatim) | §2.4 | ✓ correct |
| 6 | JEP 444 VT=Rel 21; pinning caveat quote (ellipsis-stitched) | §2.7,§4,§8 | ✓ release correct; FLAG F2 — verify each stitched fragment verbatim at draft |
| 7 | JEP 491 pinning removal = Rel 24 (LTS-flip recommendation) | §2.7,§4,§5 | ✓ correct; advice correctly JDK-bound (not timeless) — the key-20 LTS-flip trap |
| 8 | JEP 453 SC preview@21 / JEP 505 SC Fifth Preview@25 = AHEAD-OF-PIN | §5,§7,flag | ✓ correctly never asserted stable; flag filed |
| 9 | JEP 446 SV preview@21 / JEP 506 SV final@25 (GA, not preview) | §5,flag | ✓ status correction logged; not lumped with SC |
| 10 | SpotBugs MT_CORRECTNESS category label + codes (VO/DC/IS2/AT/etc.) | §2.5,§2.7,§3 | ✓ identity-only, `⚠ verify at pin` for rank/enablement; flag filed |
| 11 | Error Prone `GuardedBy` = ERROR + summary + 3 annotations | §2.5,§3 | ✓ identity verified live; severity `verify at pin` (severities move) |
| 12 | Sonar `java:S2168` (DCL) / `java:S3077` (volatile ref) | §2.7,§4,§7 | ✓ existence corroborated; titles/severity `⚠ verify at pin`; survives key-18 rule-ID-from-memory trap |
| 13 | JCStress "experimental harness" project description (verbatim) | §2.6,§3 | ✓ matches; correctly handed to key 24; experimental label kept |
| 14 | JCStress outcome labels "ACCEPTABLE / FORBIDDEN / INTERESTING" | §2.6,§6 | FLAG F3 — literal `Expect` enum is `ACCEPTABLE_INTERESTING` (not bare `INTERESTING`); verify vs `Expect.java` at draft (key 24 owns) |
| 15 | "JEP 188 GC fences post-date the book" | §1 (l.82) | FLAG F4 — doubtful: JEP 188 is "JMM Update" (process); fences shipped w/ VarHandle JEP 193 (Java 9). Recommend web re-check at draft; not failed (no web here) |
| 16 | VarHandle = Java 9, JEP 193; JSR-133 = Java 5, 2004 | §1,§5 | ✓ correct |
| 17 | JLS §8.3.1.4 (volatile) / §14.19 (synchronized stmt) / §17.1 cross-refs | §2.7,l.38 | ✓ correct for SE 21 |
| 18 | Folklore guard (1:10:100, 95% null, MI-as-score, coverage-as-quality, Valhalla-reified) | whole | ✓ NONE leak into body or §9 scan log |
| 19 | Neutrality: blocklist + crowning | whole | ✓ clean — see notes |
| 20 | HONEST-LIMITATIONS floor (hardest objection + when-NOT per lever/tool) | §4 | ✓ met for JMM-causality / volatile / synchronized / DCL / static-analyzers / JCStress / 64-bit tearing / competing-approaches (crowns none) |

## Findings (all minor — fix at draft, none blocking)

- **F1 (quote fidelity):** §1 "appear to be **[sequentially consistent]**" — bracketed editorial insertion
  inside a quoted span. *Fix:* restore JLS verbatim wording or convert to attributed paraphrase at draft.
- **F2 (stitched quote):** §4/§2.7 JEP 444 pinning quote stitches 3 fragments with ellipses. *Fix:* confirm
  each fragment is verbatim + in-context against `openjdk.org/jeps/444` at draft (no web access at this gate).
- **F3 (enum precision):** JCStress "INTERESTING" should be `ACCEPTABLE_INTERESTING` (literal `Expect`
  constant). *Fix:* align to `Expect.java` Javadoc at draft; key 24 owns JCStress depth.
- **F4 (doubtful color atom):** "JEP 188 GC fences" — JEP number likely wrong (JEP 188 = "Java Memory Model
  Update"; the fence APIs shipped with VarHandle, JEP 193). *Fix:* recommend web re-check at draft; drop the
  JEP number or correct it. Not failed here (undisprovable without web).
- **F5 (script noise, informational):** the 20 `lint_citations.sh` violations are the documented bare-domain /
  `⚠`-status false positives — no action on the dossier; standing OPEN ITEM to tune the script.
- **F6 (neutrality, informational):** two literal "unlike" tokens (l.57 "unlike most quality dimensions";
  l.450 "unlike `openjdk.org/jeps`") — neither crowns a code-quality rival (self-description + fetch-tool
  contrast). Clean, but flag at research time so the AUDIT greppable pre-pass doesn't bounce them (key-16 note).

## Blockers
- **None.**

## VERIFY gate-specific checks
- [x] Every specific fact traces to a pinned authority/primary source OR is marked `⚠ UNVERIFIED`/`⚠ verify at pin`/`⚠ AHEAD-OF-PIN`.
- [x] No folklore stated as fact (body + §9 scan log clean).
- [x] No off-pin / moving-target citation; no `-SNAPSHOT`/`main`.
- [x] Preview features (structured concurrency JEP 505, scoped values pre-25, JEP 453) are AHEAD-OF-PIN, never stated stable; JEP 506 SV correctly GA@25 and not lumped with SC.
- [x] Neutrality: no blocklist phrase crowns a rival; no tool crowned; tool claims cite each tool's own source.
- [x] Synthesized/causal/comparative claims supported (invisible-failure ← §17.4.5 data-race def; LTS-flip advice JDK-bound; "different tools for different sharing shapes" crowns none).
- [x] HONEST-LIMITATIONS floor met (hardest objection + when-NOT-to-use per lever/tool).
- [x] Required flag files present + match dossier (`20_jcstress_structured_concurrency_ahead_of_pin.md`, `20_tool_rule_defaults_unverified.md`).
- [ ] Machine atom byte-verification — **DEFERRED to post-`/pin-source`** (pin absent/unhealable; this is the standing multi-authority pre-pin state, not a dossier defect).

## Learnings & pipeline suggestions
- **Stitched/bracketed quoted spans on the JLS/JEP** (F1/F2) recur on every spec-heavy dossier — propose a
  draft-gate sub-check: any quoted span containing `…`/`[ ]` must carry a verbatim re-check marker before it
  ships. Sibling of the key-19 quote-drift lint.
- **Color-atom JEP-number trap (F4):** secondary "JEP NNN does X" color claims (here JEP 188) are
  never-invent atoms too — extend the "no rule-ID/JEP-number from memory" guard (keys 14/18) to passing
  color mentions, not just the headline feature.
- **JCStress enum precision (F3):** when a chapter *introduces* a tool another key *owns* (here key 24), its
  named constants still must match the owner's verbatim source — propose a cross-key "introduced-here /
  owned-elsewhere" atom-consistency note.
- Reconfirms the standing pre-pin pattern (keys 07/10/11/12/13/15/16/17/19): pre-pin SOURCE-VERIFY audits
  flag-discipline, not atoms; `lint_citations.sh` bare-domain false positives + `check_source_pin.sh`
  FAIL-by-construction remain OPEN ITEMS.

> **Re-run this gate after `/pin-source`** to byte-verify every JLS §, JEP `Release` field, and tool atom.
