# SCORECARD (INDEPENDENT) — Chapter 26 · key 55 (enforcing architecture / fitness functions)

> Independent (different-model) re-score for the 88% auto-approval bar (`SCORING.md`). Harsh-skeptic pass.
> One in-bounds FLOOR-C code-rigor fix (CODE-REVIEW FIX-5b) was applied to the companion module and the
> module was rebuilt green; this is a floor-rigor fix, not a cluster lift-loop pass (no lift pass was
> needed — the draft cleared the bar on first independent score).

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 55 (owner; folds 33 + 56)
- **Slug:** `55_enforcing_architecture_fitness_functions`
- **Title:** Giving the Diagram Teeth — enforcing architecture with ArchUnit & JPMS + the fitness-function frame
- **Part / arc position:** Part VI — Architecture & Design Governance, Chapter 26 of 47 (Part VI CLOSER; FINAL_INDEX confirmed)
- **Artifact scored:** `03-drafts/55_enforcing_architecture_fitness_functions/55_enforcing_architecture_fitness_functions_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (ArchUnit row 1.4.2; re-check date 2026-06-28 — confirmed against the resolved `archunit-1.4.2.jar` + green build)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (no cluster lift-loop pass used; one FLOOR-C code-rigor fix applied — see log)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Spine is unmistakable: enforcement-spectrum (convention→ArchUnit→JPMS→build module) → import-then-assert mechanism → fitness-function unifying frame, each step earning the next. The "why" is explicit throughout (why docs erode; why bytecode-only is a blind spot; why freezing enables adoption). Fig 26.1 is introduced (¶ before it) and load-bearing. Deep-dive traces one decision (domain↛web) through all three enforcement levels. Not 10: the fitness-function taxonomy (atomic/holistic/triggered/continuous/static/dynamic) lands in one fast sentence (line 111) and could earn one more beat. |
| 2 | **ACCURACY** | **9** | Every load-bearing atom traces and was re-verified live against the resolved `archunit-1.4.2.jar`: `layeredArchitecture()`, `slices().beFreeOfCycles()`, `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`, `FreezingArchRule.freeze/.as`, `ClassFileImporter`+`DoNotIncludeTests`, `CanOverrideDescription.as` — all real API at the pin. `archunit.properties` 100/20 match the built module. Snippets are tag-regions in a green module (check_snippets 5/5). Unverifiable items (BEA definition/taxonomy/Ford-Parsons-Kua-Sadalage attribution; full `GeneralCodingRules` set; JPMS=JEP 261) are correctly marked ⚠ verify-at-pin → `09-flags/55_*`, not asserted. Zero drift, zero invention. Not 10: the BEA canon source is still an unpinned SOURCE-PIN §7 row, so the central fitness-function quotation is flagged-not-pinned (honest, but a gap). |
| 3 | **UTILITY** | **9** | A page a practitioner keeps open: concrete "When to use what" frame, the spectrum trade-off table, copy-paste rules (layering/cycles/coding/freeze), and a runnable enterprise-grade companion module (green; 9 tests; real layered storefront; two seeded breaches each rule-proven; ratchet exercised both halves). Answers the real question ("how do I stop architecture eroding?"). Not 10: JPMS is covered at decision-level only (no companion `module-info`), a defensible scope choice that caps applied JPMS guidance. |
| 4 | **DEPTH** | **9** | Full mechanism + evidence-for + honest-limitations + alternatives + when-to-use, all sourced. Three folded keys give genuine breadth: enforcement spectrum, import-then-assert internals, rule families, the ratchet, the bytecode blind spot, the JPMS trade-off, and the fitness-function portfolio frame that unifies the whole book. Deep-dive adds real substance (wiki-line→rule→module→JPMS progression). Verified substance, not padding. Not 10: jQAssistant/Deptective/Spring Modulith are named-and-routed-onward rather than developed (correct routing, but caps depth here). |
| 5 | **READABILITY** | **9** | Locked senior-engineer voice holds; terms glossed plain-first (JPMS, fitness function, ratchet, evolutionary architecture). Em-dash density **5.80/1000** (under the 8 ceiling; 5.34 literal-only). CONCEPT callouts used sparingly (3, each earning its place). Strong erosion hook; clean forward hand-off to Part VII. Not 10: a few sentences run long/dense (e.g. the deep-dive "The honest boundary, and the reason Chapter 25 spent so long on judgment…" sentence). |

**Cluster subtotal: 45 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase grep clean (better than / unlike X / superior / beats / outperforms / the problem with / kills / destroys / blows away / in the dust / obvious choice over / no reason to use — none). ArchUnit, JPMS, jQAssistant, JDepend, Spring Modulith, Deptective explicitly "different approaches… none crowned," each cited to its own source (lines 36, 62, 145–151). JPMS-vs-ArchUnit framed as trade-off: "stronger guarantee… but it comes at a real cost," "Neither is crowned" (line 107). "Alternatives" section is approach-based, not a leaderboard. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries hardest objections + explicit when-NOT-to-use: ArchUnit "honest limits" ¶ (103) + bytecode-only blind spot stated 3× + dedicated "Limitations & when NOT to reach for it" (8 bullets) + "When to use what". JPMS "adoption is heavy… many teams rationally stay on the classpath." Freezing "can mask debt if the baseline is never driven down" (3×). Fitness functions: only-measurable, over-governance-ossifies, code-that-rots, verify-not-make-decisions. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** no invented atom; all ArchUnit API/GAV/version/properties pin-confirmed (SOURCE-PIN §2 + live jar + green build); unverifiable items flagged to `09-flags/55_*`, not asserted. **Compile:** `mvn -B -Pquality -pl 55_enforcing_architecture_fitness_functions clean verify` → **BUILD SUCCESS**, 9 tests, JDK 21.0.11, ArchUnit 1.4.2, 0 Checkstyle, 0 SpotBugs (re-run by this scorer 2026-06-28). **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES, no BLOCKER, floor not blocked. FIX-5a (layered breach test-proven) already applied in the live test (`seededLayeringBreachIsRejectedByTheLayeredRule`, 6 arch tests). **FIX-5b (freezing-ratchet asserted only `isNotNull`) applied by this scorer** — added the discriminating "new violation still fails" assertion via a separately-described frozen rule over an empty `CLEAN_LAYERS` baseline, then `assertThatThrownBy(... check(LAYERS))`; rebuilt green. `freezing-ratchet` tag region untouched (still 2 lines); check_snippets 5/5. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — 45/50 (≥44, no cluster below 6); all THREE floors PASS; companion module green + CODE-REVIEW fixes resolved.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** Clears the 88% bar at 45/50 on an independent harsh-skeptic score with all floors PASS; the one open code-rigor item (FIX-5b) was fixed in-bounds and the module rebuilt green, so FLOOR C is fully clean.

---

## Flagged weakest cluster

- **Weakest cluster:** five-way tie at 9 — nominally **ACCURACY (9)** carries the only structural gap.
- **Why it is the weakest:** the central fitness-function definition/taxonomy and the *Building Evolutionary Architectures* attribution rest on a source that is **not yet a pinned SOURCE-PIN §7 canon row** — correctly flagged ⚠ verify-at-pin to `09-flags/55_*`, but a flag is not a pin.
- **Single highest-leverage move to lift it:** add *Building Evolutionary Architectures* (1e/2e) as a §7 canon row in `SOURCE-PIN.md`, then re-trace the definition + atomic/holistic/triggered/continuous/static/dynamic taxonomy + author attribution against it and clear the `09-flags/55_*` atoms. This is a pin/scope action, not a prose lift, and would move ACCURACY toward 10. **Not required for SHIP.**

---

## Line-level fixes (optional polish — none gate SHIP)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY (pin scope) | back-matter + line 111 | BEA definition/taxonomy/attribution flagged-not-pinned | Pin BEA as SOURCE-PIN §7 canon row; clear `09-flags/55_*`. |
| 2 | READABILITY | Deep dive ¶ (line 127) | One very long dense sentence ("The honest boundary, and the reason Chapter 25…") | Split into two; no content change. |
| 3 | ACCURACY (cross-ref nit) | line 115 | "Chapter 1's culture" — culture is Chapter 4 (Ch 1 = cost/shift-left) | Either "Chapter 4's culture" or "the culture work of Part I"; shift-left→Ch 1 is correct. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | A | B | C | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 45 / 50 | PASS | PASS | PASS | SHIP | Initial independent score. FLOOR-C code-rigor fix FIX-5b applied to the companion module (discriminating ratchet assertion); `mvn -B -Pquality -pl 55 clean verify` → BUILD SUCCESS, 9 tests; check_snippets 5/5; tag regions untouched. No cluster lift-loop pass was needed. |

---

## Learnings & pipeline suggestions

- **Gate-report freshness lag is a real scoring hazard.** `_EXAMPLE.md` (8 tests, FIX-5a "applied 2026-06-27") and `_CODEREVIEW.md` (5 arch tests, FIX-5a listed OPEN, same date) disagreed with the live module (6 arch tests, FIX-5a already in code). The draft front-matter (9 tests) matched truth; the gate reports lagged. Lesson: the scorer must read the **live source + surefire reports**, not trust the gate-report counts — and FIX application should bump the gate report in the same commit. Worth a reporting-discipline note: when a CODE-REVIEW FIX is applied, re-stamp `_CODEREVIEW.md` (or append an "applied" row) so the report never claims an open fix that is closed.
- **Ratchet/freeze tests need both halves, and the clean recipe is description-keyed stores.** A `FreezingArchRule` test that only re-checks the *baseline* import proves "old suppressed" but not "new caught." The in-bounds way to add the discriminating half without disturbing the demonstrated baseline is a **separately-`.as(...)`-described** frozen rule (its own store slot) frozen over a clean import, then checked over the breaching import. Promote to `EXAMPLES-GUIDE` as the standard two-phase freezing-test shape (this confirms the suggestion already raised in `_CODEREVIEW.md` learning #3).
- **"Demonstrate a gate failing" stays green via assert-the-throw.** The module's pattern — breach is real compiled code, kept outside the passing rules' scope, and each breach is the subject of an `assertThatThrownBy(rule.check(fullImport))` — is the right recipe and is now proven for both the coding-rule edge (FIX-5a) and the freeze discrimination (FIX-5b). Reinforces the existing EXAMPLES-GUIDE candidate.
- **Live-jar `javap` is cheap, high-value verification for ACCURACY.** Confirming `CanOverrideDescription.as`, `FreezingArchRule.freeze/.as`, and `DoNotIncludeTests` directly against the resolved pinned jar closed any doubt about the displayed API at near-zero cost. Suggest the scorer default to a `javap` spot-check on any load-bearing API atom when the pinned jar is already in `~/.m2`.
