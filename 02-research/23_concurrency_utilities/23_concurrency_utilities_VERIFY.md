# GATE REPORT — SOURCE-VERIFY (key 23)

- **Gate:** VERIFY (step 2, pre-pin)
- **Artifact:** `02-research/23_concurrency_utilities/23_concurrency_utilities_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** (0 blockers, 3 minor findings)

## Scripts: run vs manual
- `check_source_pin.sh` — RAN → **FAIL by construction**: multi-authority pin, all SOURCE-PIN rows `TO-PIN`,
  repo URL `{URL}`, ephemeral clone ABSENT and unhealable from the VERIFY gate (heal is `/pin-source`'s job).
- `verify_sources.sh` — RAN → aborts ("pinned clone absent"); no machine byte-trace of atoms possible pre-pin.
- `lint_citations.sh` — RAN → 25 "violations", all the **known bare-domain / `☐`-status / print-canon false
  positives** (keys 13/16/17 documented; regex wants `http://` prefix + date token). Not dossier defects.
- Neutrality blocklist greppable scan — RAN → **CLEAN**. Folklore greppable scan — RAN → **CLEAN**.
- `check_snippets.sh` — N/A (research dossier, no `<!-- include -->` markers; snippet tags are a draft-step artifact).

> **Pre-pin caveat (keys 11/12/13/15/16/19 standing):** a PASS_WITH_FLAGS here means "flagged the right
> things / no invented-or-unflagged fact," NOT "atoms byte-verified." Atom re-trace must run after `/pin-source`.

## Checked claims / issues

| # | Claim / atom | Where | Finding | Status |
|---|---|---|---|---|
| 1 | j.u.c family **Since 1.5**; `CompletableFuture`/`LongAdder`/`StampedLock` **Since 1.8** | §1, §2, §2.9, §9 | Matches PIPELINE-LEARNINGS key-23 verified `Since` atoms; "library `Since:` = never-invent atom" discipline applied; marked verified-by-fetch | OK |
| 2 | JEP 444 Virtual Threads **GA/final @21** | §2.6, §5, §2.9 | Matches key-24/20/21 verified `Release` field; correctly stated as fact at anchor | OK |
| 3 | JEP 453→505 Structured Concurrency **preview 21→fifth-preview 25** | §2.6, §5, §2.9, §8 | Matches keys 20/24/25; `StructuredTaskScope` never stated stable; kept out of compiled module; `⚠ AHEAD-OF-PIN` throughout | OK |
| 4 | JEP 506 Scoped Values **final @25** (incubator 429@20) | §5, §2.9, §8 | Matches keys 20/24/25; `⚠ AHEAD-OF-PIN @21`, horizon-note only | OK |
| 5 | JEP 491 no-pinning **@24**; "prefer ReentrantLock in VT code" = 21-era reality narrowed at 24 | §2.6, §4, §5 | Matches key-20/24 "recommendation-flips-across-LTS" trap; version boundary carried, never timeless | OK |
| 6 | happens-before edges (lock/volatile/start/join) quoted verbatim from j.u.c package-summary; tied to JLS ch.17 | §2.1, §3, §9 | Verbatim-quote claim consistent with key-20/21/23 "package-summary is the fetchable JMM spine"; package-summary quotes asserted, JLS **§ numbers** correctly deferred (§7, src #8 `☐`) | OK (re-verify quote bytes + JLS §§ at pin) |
| 7 | `LongAdder` throughput note quoted verbatim | §2.3, §3, §4 | Single consistent spelling across §2.3/§3/§4 (no key-19 quote-drift); a numeric/comparative-flavored quote → re-confirm byte-exact at pin | OK (verify at pin) |
| 8 | Error Prone `GuardedBy`=ERROR (verbatim), `DoubleCheckedLocking`/`ImmutableEnumChecker`=WARNING; `FutureReturnValueIgnored` severity uncaptured | §2.7, §3, §8, flag | ID+severity discipline correct; `FutureReturnValueIgnored` severity correctly `⚠ verify at pin`; annotation FQN `com.google.errorprone.annotations.concurrent.GuardedBy` named (key-25 @GuardedBy-package trap respected) | OK |
| 9 | SpotBugs MT_CORRECTNESS family (8 codes) verified present; long-form titles/category deferred | §2.7, §3, §7, §8, flag | Identity-only, defaults `⚠ verify at pin` (keys 16/19 discipline) | OK |
| 10 | Sonar `java:S2142`, `java:S3077`, `java:S3078`, `java:S2445` | §1, §2.7, §3, §4, §7, §8 | `S2142`/`S3077` corroborated exist; `S2445`/`S3078` flagged **recalled-from-memory, verify at pin** (key-18 trap respected); all `⚠`-marked | OK |
| 11 | Sonar `java:S2222` ("lock-release family") | §2.2 prose only (line 105) | **FINDING (minor):** a from-memory Sonar ID that appears ONLY in §2.2 prose; it is `⚠ verify at pin`-marked at use, but is absent from the §1 atom list, the §2.7 reference table, and is not named in the §7 verify queue (§7 alludes to "the lock-release rule, recalled but not pinned" without giving the ID). Add `S2222` to §2.7 + §7 explicitly or downgrade to "Sonar lock-release rule (ID verify at pin)". | FIX |
| 12 | `Executors.newVirtualThreadPerTaskExecutor()` present in JDK 21 | §2.6, §5, §2.9, §9 | Consistent with JEP 444 GA@21; verified-by-fetch claim | OK (verify at pin) |
| 13 | `Thread.stop/suspend/resume` deprecation/removal state | §5 | Correctly NOT asserted — carries `⚠ confirm exact JDK 21/25 status at pin` (§5 + §7) | OK |
| 14 | Neutrality: "j.u.c vs raw synchronized/wait/notify" framed as same-JDK Bucket-i contrast, crowns neither | §1 Cmp, §4, §5 | Matches key-23 lesson-3 (same-JDK contrast ≠ rival comparison); each side gets honest when-NOT-to-use; no blocklist phrase | OK |
| 15 | HONEST-LIMITATIONS floor (hardest objection + when-NOT-to-use per option) | §4 | MET — ReentrantLock, StampedLock, CopyOnWriteArrayList, ConcurrentHashMap, atomics/LongAdder, Executors, CompletableFuture, AND raw synchronized each get a hardest-objection + explicit when-NOT-to-use | OK |
| 16 | Both required flag files present + match dossier | `09-flags/23_*` | `23_concurrency_tool_rule_defaults_unverified.md` + `23_structured_concurrency_scoped_values_ahead_of_pin.md` present, accurate, cross-ref `08_*` | OK |

## Blockers
- **None.**

## Required fixes (carry to draft / non-blocking)
1. **(F1)** Add Sonar `java:S2222` to the §2.7 reference table and name it explicitly in the §7 verify queue
   (currently only an unnamed "lock-release rule" allusion), or downgrade the §2.2 mention to an unkeyed
   "Sonar lock-release rule — ID verify at pin." (Finding #11 — from-memory ID, key-18 trap.)
2. **(F2)** At draft, re-confirm BYTE-EXACT the two block-quotes (j.u.c happens-before text; `LongAdder`
   throughput note) and pin the JLS SE 21 ch.17 **section numbers** (§17.4.5 happens-before / §17.5 final-field)
   before any JLS-§ is stated as fact (already queued §7).
3. **(F3)** Post-`/pin-source`: re-trace ALL tool atoms (Error Prone severities, SpotBugs titles+MT_CORRECTNESS
   grouping, Sonar IDs/titles/defaults), Java-21 `Since` fetches, and the VT-executor factory. Reserve `☑`/"verified"
   for post-pin byte-checks (recurring keys 07/10/11/15 note).

## VERIFY gate-specific checks
- [x] Pin confirmed-or-explained — ABSENT/unhealable (multi-authority, all `TO-PIN`); pre-pin manual procedure declared.
- [x] No invented fact / no unflagged atom — every version-sensitive atom is `⚠`-marked or in a flag file.
- [x] No folklore-as-fact (no 1:10:100 / coverage-as-quality / Valhalla-imminent; JMM "constructor-done = visible" not asserted).
- [x] Preview/incubator (Structured Concurrency, Scoped Values) NOT stated stable — `⚠ AHEAD-OF-PIN` throughout.
- [x] Neutrality: blocklist clean; same-JDK contrast crowns neither; no rival-cite gate misapplied.
- [x] Synthesized/causal/comparative claims supported (correctness-by-reuse, LongAdder trade-off, VT pooling shift) — each tied to a primary or flagged.
- [x] HONEST-LIMITATIONS floor met (every option, incl. raw synchronized).
- [x] Both required flag files present and accurate.

## Learnings & pipeline suggestions
- **From-memory rule IDs in PROSE escape the re-trace matrix.** `java:S2222` was `⚠`-marked at point of use but
  never made it into the §2.7 table or a *named* §7 queue entry — the same §2.7-matrix-coverage gap flagged at
  key 15. Reinforce the key-15 rule: **every rule code named ANYWHERE in a dossier (incl. §2.x prose) must also
  appear by ID in the §2.7 reference table AND the §7 verify queue**, so the matrix stays the single re-trace unit.
- **Pre-pin pattern holds (keys 11/12/13/15/16/19):** scripts can only FAIL on the multi-authority TO-PIN pin;
  SOURCE-VERIFY is a MANUAL flag-discipline audit. State "manual," never claim a script ran clean. The
  `lint_citations.sh` bare-domain / `☐`-status / print-canon false-positives recur — standing fix proposal open.
- This dossier is a clean instance of the key-23 durable shapes (library `Since:` atom; package-summary JMM spine;
  same-JDK contrast neutrality; recommendation-flips-across-LTS) it itself proposed — no new trap surfaced.
