# SCORECARD (INDEPENDENT) — Chapter 36 (key 83) `release_quality`

> Independent (different-model) re-score against `00-strategy/SCORING.md`, harsh-skeptic stance.
> Companion to (does not overwrite) any main-loop self-score. This is the score of record for the
> auto-approval bar (≥44/50, no cluster <6, floors A/B/C-source PASS).

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 83 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `83_release_quality`
- **Title:** When Quality Meets Reality (printed Chapter 36 — Part IX closer)
- **Part / arc position:** Part IX — CI/CD & Quality Gates, Ch 36 of 33–36 (CLOSER)
- **Artifact scored:** `03-drafts/83_release_quality/83_release_quality_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28)
- **Scorer:** chapter-scorer agent (independent re-score)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds pass applied — see log)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Three-part loop (release gates → progressive delivery → feedback) named upfront and traced in order; Fig 36.1 carries the shift-left↔shift-right cycle; deploy/release decoupling gets its own CONCEPT callout and is the genuine load-bearing idea; the runnable gate is shown as a collect-all-failures loop returning a sealed `Blocked(failed-checks)`. Held off 10: §How-it-works and §Deep-dive restate the shift-left/shift-right framing several times — the spine is occasionally circled, not advanced. |
| 2 | **ACCURACY** | **9** | Every load-bearing atom traces: SemVer/`-SNAPSHOT` (key 60, semver.org + Maven, runnable+unit-tested green); DORA stability NAMES + speed/stability correlation (key 85 / SOURCE-PIN §5, **no numeric band asserted** — verified by scan); SBOM/SLSA v1.0/CycloneDX 1.6 named as concept cross-refs (§4); Keep a Changelog as a named public convention (illustrative file, fictional entries). Unpinned release-plugin versions + SaaS action digests correctly flagged to `09-flags/`, not invoked by the green build. Held off 10 because the *as-submitted* draft shipped two WRONG internal cross-ref numbers vs `FINAL_INDEX` — "semver contract of **Chapter 26**" (semver is Ch 7) and "the design flaw of **Chapter 30**" for the logic-flaw class (that catch is Ch 37). Traceable-but-wrong pointers, not invented facts; **corrected in-bounds this pass.** |
| 3 | **UTILITY** | **9** | A page a release engineer keeps open: runnable release-readiness gate returning an actionable sealed verdict (the exact failed-check list, not a bare red mark), externalized dev/prod policy, feature-flag type with kill-switch, a concrete "When to use what" decision list mapping each technique to its trigger, and the fix→test→new-gate discipline. Caps at 9: progressive-delivery tooling kept general (no canary-analysis tool wired — a correct neutrality/scope choice), so the reader still chooses infrastructure themselves. |
| 4 | **DEPTH** | **9** | Full mechanism + evidence + seven distinct honest limits + approach-based Alternatives + when-to-use, all sourced; the Deep-dive resolves the speed-vs-stability tension at the *shipping* layer (distinct from the merge-layer resolution earlier) and lands the "resilience, not absence of failure" ceiling — real Part-IX-closer synthesis. Not inflated past 9: a meaningful slice is cross-ref/framing (Parts VII/XIII/X) rather than new release-quality substance, and the @pin atoms (DORA bands, tooling) are correctly NOT deepened — so the verified-substance ceiling is 9. No padding. |
| 5 | **READABILITY** | **9** | Voice holds (third person, no narration contractions, no banned filler — scanned); em-dash density **7.73/1000** (under the ~8/1000 target); stakes-first hook (green pipeline, production still down); plain-language-first glosses on `-SNAPSHOT`, *semantic versioning*, canary/blue-green/flags; one load-bearing figure breaks the grey; forward-pulling hand-off to Part X. Off 10: a few 100+-word compound sentences in the Deep-dive run dense, and the shift-left/shift-right pairing repeats enough to lose a little snap. |

**Cluster subtotal:** **45 / 50**

---

## The three content-floors (PASS / FAIL)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **NEUTRALITY (A)** | **PASS** | Body banned-phrase scan = 0 (`better than`/`unlike X`/`superior`/`beats`/`the problem with X`/`outperforms`). Canary/blue-green/flags framed as complementary, trade-off-balanced ("complementary techniques (often combined), chosen by infrastructure"), none crowned. The "safe + frequent is not a trade-off" claim is sourced to DORA (key 85 / SOURCE-PIN §5), not an unsourced verdict. No winner crowned at sentence or heading layer. |
| **HONEST-LIMITATIONS (B)** | **PASS** | Dedicated "Limitations & when NOT to reach for it" with 7 limits each carrying an explicit when-NOT: needs infra+metrics (small internal app may skip canary); flags→debt (removal discipline); rollback not clean for stateful changes (expand-contract); feedback = theatre if not triaged; smoke tests verify artifact-not-design; continuous monitoring necessary post-release; and the load-bearing ceiling "a safe release process is not good code." Every feature carries its cost. |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW (C)** | **PASS** | Invented-detail check: NONE (unpinned plugin/action atoms flagged to `09-flags/83_release_versioning_plugin_versions_unpinned.md`, not invoked by the build). `mvn -B -Pquality verify` GREEN — 17 tests, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11 (per `_EXAMPLE.md`). CODE-REVIEW = **PASS-WITH-FIXES**: zero BLOCKERs, zero security/neutrality/invention findings; the 2 MINORs are polish in illustrative config (a `grep -F` escape; an immutability test) — no floor risk (per `_CODEREVIEW.md`). All 6 displayed snippets resolve (`check_snippets` 6/6 PASS, re-run this score). SemVer claims confirmed correct; DORA asserted with no band. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the auto-approval bar: **45/50 ≥ 44**, no cluster below 6, floors A/B/C-source PASS.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** Floors A/B/C PASS; 45/50 with no cluster <6 — a strong Part-IX closer that synthesizes shift-left↔shift-right honestly, with two reader-facing cross-ref errors corrected in-bounds.

---

## Flagged weakest cluster

- **Weakest cluster:** tie at **9** across all five; ACCURACY was the operative weakest *as submitted* (two wrong cross-ref numbers) — now resolved.
- **Why it was the weakest:** the as-submitted draft pointed "semver" at Ch 26 (it is Ch 7) and the logic-flaw class at Ch 30 (it is Ch 37's catch), both contradicting `FINAL_INDEX` — a real reader-facing accuracy defect, not an invented fact.
- **Single highest-leverage move applied:** corrected both cross-reference numbers against `FINAL_INDEX` (in-bounds: a wrong-but-traceable pointer fix, no new facts, no scope/floor change).

---

## Line-level fixes (the lift list — applied this pass)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY | §Release gates · `-SNAPSHOT` lead-in | "the semver contract of **Chapter 26**" — SemVer (key 60) is **Chapter 7** ("Designing clear APIs, contracts & compatibility"); Ch 26 is fitness functions/ArchUnit | Changed "Chapter 26" → "Chapter 7". (The separate, correct "fitness function (Chapter 26)" reference left intact.) |
| 2 | ACCURACY | §Hook · logic-bug sentence | "the design flaw of **Chapter 30**, the kind only human review or production finds" — Ch 30 is "Secure coding & OWASP for Java"; the logic-flaw-only-humans-catch class is **Chapter 37**'s catch (which the draft names throughout) | Rewrote to "the kind only human review or production finds — the catch of **Chapter 37**". |

> Non-blocking polish carried forward to the example-builder (CODE-REVIEW MINORs, not floor risks, do not block SHIP): (a) `release/release-gate.sh:31` use `grep -qF` for the literal changelog match; (b) add one `ReleaseDecision` defensive-copy/immutability test to enforce the "immutable, never null" comment. Optional readability nit: split the two 100+-word Deep-dive sentences ("Drawn as a loop…", "The sub-limits reinforce…").

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 45 / 50 (CLARITY 9 · ACCURACY 9† · UTILITY 9 · DEPTH 9 · READABILITY 9) | PASS | PASS | PASS (build GREEN; CR PASS-WITH-FIXES, no blockers) | SHIP (bar already cleared) | initial independent score. †ACCURACY 9 carried two wrong cross-ref numbers vs FINAL_INDEX. |
| 1 | 2026-06-28 | 45 / 50 (CLARITY 9 · ACCURACY 9 · UTILITY 9 · DEPTH 9 · READABILITY 9) | PASS | PASS | PASS | **SHIP** | Corrected both cross-ref numbers (semver Ch 26→7; logic-flaw Ch 30→Ch 37). No code touched → no rebuild; `check_snippets` 6/6 re-confirmed. Removed two reader-facing accuracy errors; ACCURACY now clean within its 9. |

> Pass count: 1 of the ≤3 budget used. Bar (44/50) cleared at Pass 0; the single pass was an in-bounds accuracy correction, not a bar-chasing lift.

---

## Learnings & pipeline suggestions

- **Cross-reference chapter NUMBERS are a recurring, low-visibility accuracy leak that gate reports miss.** Both EXAMPLE-BUILD and CODE-REVIEW (correctly) check code atoms and source-trace, but neither validates that an in-prose "Chapter NN"/"Part N" pointer resolves to the right title in `FINAL_INDEX`. Here a chapter that built green and passed code-review still shipped two wrong cross-ref numbers (semver→Ch 26, logic-flaw→Ch 30). **Recommend** a tiny scripted `lint_xrefs` pre-pass (greppable: every "Chapter NN"/"Part N" in a draft resolved against `01-index/FINAL_INDEX.md`, with the cited topic word fuzzily matched to that row's title) run at Step 4a alongside the heading/banned-phrase scan. Cheap; removes a whole class of reader-facing error before scoring.
- **A chapter that cites the SAME chapter number for two different topics is a strong drift signal.** This draft used "Chapter 26" correctly for fitness functions AND incorrectly for semver in the same file — a self-inconsistency a number-resolution lint would catch instantly. Worth flagging as a heuristic in `lint_xrefs`.
- **The "tested-policy-core + illustrative-config" companion shape (peers 62/67/75/83) keeps FLOOR C clean and reviews fast** — confirms the EXAMPLE-BUILD/CODE-REVIEW learning; worth naming canonical in `EXAMPLES-GUIDE` for any "a gate/policy made runnable" chapter.
- **DORA-by-names-only is the right pattern for a chapter that needs DORA but not its bands.** This draft asserts the stability metric *names* + the correlation *framing* (both pinned, §5) and explicitly parks the elite/high/medium/low bands as `⚠ still @pin` in back-matter — a clean template for citing a metrics framework without committing to unpinned figures. Recommend `SOURCE-PIN`/`EXAMPLES-GUIDE` reference it as the model for the other DORA-adjacent chapters (33, 38, 44).

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
