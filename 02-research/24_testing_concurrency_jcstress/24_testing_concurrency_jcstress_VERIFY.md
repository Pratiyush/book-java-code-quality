# GATE REPORT — SOURCE-VERIFY (key 24)

- **Gate:** SOURCE-VERIFY (pipeline step 2)
- **Artifact:** `02-research/24_testing_concurrency_jcstress/24_testing_concurrency_jcstress_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** (0 blockers)

## Scripts: run vs manual
- `check_source_pin.sh` — RAN; **FAIL by construction** (multi-authority pin, ephemeral clone ABSENT, repo URL TBD; `/pin-source` not yet run). Heal is out of scope for the VERIFY gate.
- `verify_sources.sh` — RAN; cannot byte-trace atoms (no pinned clone). Atom verification DEFERRED to post-`/pin-source`.
- `lint_citations.sh` — RAN; only the known false-positives (bare-domain URLs, author-year `(Goetz et al., 2006)`, print-canon/`⚠`-status rows lacking date token). No real citation defect.
- `check_neutrality.sh` — RAN; **blocklist clean**. Advisory FLAGs only (1 filler word; em-dash density 18/1000 over the ~8/1000 ceiling) — AUDIT-gate prose concerns, not fact defects.
- Manual adversarial read of all 9 sections + 3 flag files performed.

> **Pre-pin caveat (recurs from keys 10/11/12/13/15/16/17/19):** a PASS_WITH_FLAGS here means "flagged the right things," NOT "atoms byte-verified." The ☑ marks in §2.7/§8 were taken against JCStress `master` + live JEP/spec/tool pages, not a pin. Re-trace every atom after `/pin-source`.

## Checked claims / findings

| # | Location | Claim | Verdict | Fix |
|---|---|---|---|---|
| 1 | §1, §5, §8 | JEP 444 Virtual Threads FINAL@21; JEP 505 Structured Concurrency fifth-preview@25; JEP 506 Scoped Values FINAL@25; preview chain 453/21→462/22→480/23→499/24→505/25→525 | OK — correct; 505 marked `⚠ AHEAD-OF-PIN` throughout; 506 GA correctly allowed | none (re-confirm `Release` field at pin) |
| 2 | §2.1, §2.7, §8 | JLS ch.17 §17.4/§17.4.5/§17.5/§17.6 sections; `hb(x,y)` happens-before verbatim | OK — sections correct; quote attributed to SE 21 page | re-confirm verbatim + SE 25 §-numbers when block-quoting at draft |
| 3 | §2.2, §2.7 | JCStress `Expect` (ACCEPTABLE/ACCEPTABLE_INTERESTING/FORBIDDEN/UNKNOWN), `Mode` (Continuous/Termination), `@Actor`/`@State`/`@Outcome`/`@Signal`/`@Arbiter` Javadoc quotes | OK as marked — verbatim against `master`; carries `⚠ verify at pin` | re-trace against the pinned JCStress tag (master = moving target) |
| 4 | §1, §2.7, §6, §8 | GAV `org.openjdk.jcstress:jcstress-core` + archetype; release **0.16** latest | OK — never asserted as pinned; `⚠ verify at pin`; flag filed | add SOURCE-PIN §3 JCStress row at `/pin-source` |
| 5 | §2.2, §4, §5 | "experimental" / "probabilistic" self-labels; "test failure does not immediately mean implementation bug" | OK — quoted from README, drives HONEST-LIMITATIONS floor | re-confirm verbatim at pinned tag |
| 6 | §2.7, §3, §8 | Error Prone `GuardedBy` severity ERROR + summary; SpotBugs `MT_CORRECTNESS` category | OK — `GuardedBy` verbatim; `MT_CORRECTNESS` identity-only with pattern IDs `⚠ verify at pin` (owned by key 25) | verify enablement/IDs at pin |
| 7 | §4, §7, §8 | Lincheck named as a different *approach* (model-checking/linearizability vs sampling); "crown neither" | OK — neutral framing, no banned phrasing, flagged UNPINNED | any factual Lincheck claim at draft needs a pinned cite or cut |
| 8 | §5 | "Scoped values are the structured-concurrency-era replacement for `ThreadLocal` inheritance across forked tasks" | OK (synthesized) — matches JEP 506 motivation | carry JEP 506 cite at draft |
| 9 | §6 | Dependency table marks Java-21 platform pin and archetype-exists as ☑ while SOURCE-PIN rows are TO-PIN | MINOR — reserve ☑ for post-pin byte-checks (recurs keys 07/10/11/15) | downgrade ☑→"verify at pin" |
| 10 | §1.3 header "The problem it solves." | Greps near blocklist phrase "the problem with" | OK — section label about the subject, not "the problem with <rival>"; blocklist scan clean | none |

## Folklore guard
- No 1:10:100 / 100×, no MI-as-score, no coverage-as-quality, no "reified generics/Valhalla soon." **Clean.**
- Positively reinforces the folklore discipline: "a green run ≠ proof" is stated as the harness's own self-label, not asserted as a famous number.

## Neutrality
- Blocklist clean (script + manual). No crowning. JCStress treated as the subject's own OpenJDK harness; jqwik/latches/`Thread.sleep`/Lincheck each get strongest case + hardest limitation. `Thread.sleep` framed as a *technique* anti-pattern (stated-and-corrected), not a rival claim. PASS.

## AHEAD-OF-PIN / preview discipline
- JEP 505 Structured Concurrency / `StructuredTaskScope` marked `⚠ AHEAD-OF-PIN` in §1, §4, §5, §6, §7 and kept out of the compiled module (no `--enable-preview`). JEP 506 Scoped Values correctly allowed as Java-25 GA. PASS.

## HONEST-LIMITATIONS floor
- MET. Every option carries a hardest-objection + explicit when-NOT-to-use: JCStress (probabilistic/experimental/hardware-dependent; not a single-run merge gate), latch/barrier (one interleaving; regression-lock only), `Thread.sleep` (retire), jqwik (needs a sequential spec), virtual threads (JEP 444/491 change the terrain), JCiP (2006, canon-dated).

## Required flags (all present + accurate)
- `09-flags/24_jcstress_not_pinned.md` ✓
- `09-flags/24_structured_concurrency_ahead_of_pin.md` ✓
- `09-flags/24_lincheck_unpinned.md` ✓

## Blockers
- **None.**

## Required fixes (non-blocking, carry to draft)
1. Downgrade pre-pin ☑ marks in §2.7/§6/§8 to "verify at pin" (reserve ☑ for post-`/pin-source` byte-checks).
2. Re-trace all JCStress Javadoc/README quotes against the pinned release tag (master ≠ pin) once the SOURCE-PIN §3 JCStress row is added.
3. Re-confirm JLS §-numbers + happens-before quote against the SE 25 edition when block-quoting.
4. At draft, any Lincheck factual claim needs a pinned Lincheck cite or must be cut.
5. (AUDIT-gate, advisory) trim em-dash density / filler word flagged by `check_neutrality.sh`.

## Learnings & pipeline suggestions
- The "prove-a-bug-exists" shape makes the honesty floor unusually clean: the harness's own "experimental"/"probabilistic" self-labels carry the when-NOT-to-use, so the dossier never has to invent a caveat. Reusable for keys 46/49.
- Recurring pre-pin pattern holds: scripts can only FAIL on the multi-authority pin; VERIFY is a manual flag-discipline audit until `/pin-source` + the multi-authority script adaptation land (standing OPEN ITEM).
- ☑-overuse pre-pin recurs again (now keys 07/10/11/15/24) — promote the "reserve ☑ for post-pin" rule into GATE-REPORT-TEMPLATE.
