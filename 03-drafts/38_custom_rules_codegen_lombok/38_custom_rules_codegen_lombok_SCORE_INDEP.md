# SCORECARD (INDEPENDENT) — Ch 18 "Writing custom rules; annotation processors & Lombok" (key 38 + folds 40)

> Independent (different-model) re-score for the 88% auto-approval bar, per `SCORING.md`. Harsh-skeptic
> pass. Scored against `templates/SCORE-TEMPLATE.md`. This is the gate-of-record score for Step 8
> auto-approval; it supersedes the main-loop self-score (`_SCORE.md`, 43/50, scored pre-build under the
> old 35-bar).

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 38 (frozen; folds 40) — `CANDIDATE_POOL.md` / `FINAL_INDEX.md` Ch 18
- **Slug:** `38_custom_rules_codegen_lombok`
- **Title (book of record):** Writing custom rules; annotation processors & Lombok — Part IV (Ch 15–19), Ch 18
- **Chapter title (draft):** "Teaching the Build Your Rules"
- **Artifact scored:** `03-drafts/38_custom_rules_codegen_lombok/38_custom_rules_codegen_lombok_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28 — pin current)
- **Gate reports read:** `_EXAMPLE.md` (Floor-C COMPILE), `_CODEREVIEW.md` (Floor-C CODE-REVIEW), `_SCORE.md` (prior self-score)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 2 (final)

---

## The five clusters (final, post-lift pass 2)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The `select → predicate → report → register/gate` shared shape + the one-shape-five-artifacts table collapse a 7-tool, 2-dossier chapter into one reconstructable mental model; the JSR 269 round model + the "relation to the standard contract" lens order the codegen half; two CONCEPT callouts anchor each half; the deep dive fuses both on one invariant. −1: the "Three realizations, briefly" heading undercounts — PMD + SpotBugs are folded into a parenthetical inside the ArchUnit bullet, so a reader can miss where their mechanism was taught. |
| 2 | **ACCURACY** | **8** | Dense, correct API identity across 5 analyzers + JSR 269 + 4 codegen tools; the two pinned-and-built atoms (`error_prone_annotations 2.36.0`, `archunit 1.4.2`) are `javap`-verified and built green; zero invented atoms. −2 (floored here, NOT in-bounds liftable): the analyzer custom-rule **authoring SDK** versions/GAVs/severities (Checkstyle/PMD/SpotBugs/Error Prone check_api) are not SOURCE-PIN rows, and the two double-quoted `RoundEnvironment`/`Filer` spans are attributed-verbatim SE 21 Javadoc whose character-match is unconfirmed. Both are correctly flagged `⚠ @pin` → `09-flags/`, so this is **not** a Floor-C fail — but unverified-verbatim-presented-as-quote + a large deferred verify surface cap accuracy at 8. Lifting needs `/pin-source` (out of in-bounds scope); fabricating verification is forbidden. |
| 3 | **UTILITY** | **9** | The page a senior keeps open to encode a house invariant: shared shape + per-artifact fit guidance + "does a stock rule already cover this?" gate + ship-WARNING-then-ERROR + the codegen decision frame (record vs new-file vs Lombok) with the JDK-23 wiring trap and the @Generated/coverage hook; runnable companion built green. −1: Checkstyle/PMD/SpotBugs custom-check authoring is taught by identity (the runnable form is a reflective stand-in), an honest, flagged consequence of the pinned-only build. |
| 4 | **DEPTH** | **9** | Senior material, not padded: substrate carried from Ch 17 into authoring; the relation-to-the-standard-contract lens; Lombok's edit-the-AST mechanism at the ShadowClassLoader / forced-round / patched-Filer level; the each-rule-sees-only-its-artifact edge proven in code (erased-generic blind spot). Full mechanism + for + against + alternatives + when-to-use, all sourced. Held at 9 deliberately — not inflated to 10. |
| 5 | **READABILITY** | **9** | (Lifted 7 → 8 → 9 over two passes.) The one named AI tell — over-target em-dash density + the over-used appositive cadence — is removed: narration em-dash density taken from **8.79 → 4.14 / 1000** (all-body 6.65/1000), and the two densest narration runs (Lombok internals) broken into varied-length sentences with deliberate short beats per the voice guide. Voice holds throughout; concrete hook; posed-question device; "one invariant, five ways" payoff; 0 banned filler, 0 banned-comparative. −1: still the longest Part IV chapter (~4,370 body words) over a 7-tool + codegen-spectrum surface, so "effortless at full precision" (the 10 anchor) is not quite met. |

**Cluster subtotal: 44 / 50** — no cluster below 6.

---

## The three content-floors (all THREE PASS)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep over the body = 0 (`better than` / `unlike X` / `the problem with X` / `superior` / `beats` / `outperforms`). Five authoring models framed as **one shape over five artifacts** — "the choice follows the artifact, not a ranking"; records vs generate-new-files vs Lombok = "different approaches to the same boilerplate problem … none is crowned" (stated twice); the which-analyzer verdict explicitly routed to Ch 17. Every cross-tool claim cited to that tool's own pinned doc (back-matter). No heading carries a winner-crowning superlative. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §"Limitations & when NOT to reach for it" + per-tool hardest-limit in How-it-works + §"When to use what" with an explicit when-NOT line. Every feature carries its cost: custom-rule maintenance-forever + version-bound API (PMD 7.x most upgrade-sensitive); FP-cost-trust (ship WARNING first); not-all-machine-checkable; each-rule-sees-only-its-artifact; codegen machinery + record-carrier-only + processor-ordering; Lombok internal-API trade + invisibility; generated≠reviewed + the `@Generated` package-name trap. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE:** zero invented atoms; every unpinned detail (authoring-SDK versions, PMD node renames, JDK-23 boundary, `--add-opens` set, JaCoCo ≥0.8.1, the verbatim JSR-269/JEP-395 spans) flagged `⚠ @pin` → `09-flags/`; nothing presented ahead of the pin (JDK 25 preview node shapes marked AHEAD-OF-PIN). **COMPILE** (`_EXAMPLE.md`): `mvn -B -Pquality clean verify` = BUILD SUCCESS, **14 tests** 0F/0E, **0 Checkstyle**, **BugInstance size 0**, deterministic ×3, at JDK 21.0.11 / Maven 3.9.16. **CODE-REVIEW** (`_CODEREVIEW.md`): **PASS-WITH-FIXES** — 0 BLOCKER, 0 FAIL, 0 security / 0 neutrality / 0 invention / 0 originality. The two FIX items are both resolved in the current artifact (F1 Chapter-88 → corrected to Ch 45 in the module + rebuilt green; F2 `@CheckReturnValue` → now motivated in draft prose L128). No BLOCKER/FAIL = passing gate. |

**Version-pin confirmation (task-specified):** ArchUnit **1.4.2** = SOURCE-PIN §2 row ✅ pinned. Error Prone = §2 "latest 2.x"; the asserted `error_prone_annotations 2.36.0` annotation coordinate is anchored by the NullAway 0.13.4 row ("Error Prone 2.36.0+") and `javap`-confirmed against the cached jar ✅. Lombok = **not** a SOURCE-PIN row and **no Lombok version is asserted** anywhere in the draft (all Lombok facts are class/feature identity, flagged `⚠ @pin`, build does not depend on Lombok) ✅ correct. No invented version.

**Cross-reference audit vs FINAL_INDEX (LOCKED):** all body cross-refs resolve — Ch 17 (layered stack), Ch 15–16 (how static analysis / the analyzers), Ch 19 (living with findings / baselines / ratcheting / FreezingArchRule), Ch 5 & 8 (records as language feature / immutability + records), Ch 7/8 (equals/hashCode contract → Ch 8 owns it). **No stale "Chapter 88" in the draft prose** (it existed only in the module comments; orchestrator corrected to Ch 45 + rebuilt). The "metrics/coverage chapter" @Generated routing is unnumbered/loose (coverage is Ch 23, metrics Ch 38) — a NIT, not an error.

---

## Verdict

- [x] **SHIP** — clears the 88% bar (**44/50**, no cluster below 6); all THREE floors PASS; ready for the Step 12 human approval gate.

**One-line rationale:** Floors A/B/C all PASS (build green, 14 tests, CODE-REVIEW no-BLOCKER, banned-sweep 0); after two in-bounds READABILITY lifts (em-dash narration 8.79→4.14/1000; two dense Lombok runs broken to varied cadence) the aggregate reaches 44/50; ACCURACY is honestly floored at 8 by a correctly-flagged deferred verify surface, not liftable without `/pin-source`.

---

## Flagged weakest cluster (pre-lift)

- **Weakest cluster (pass 0):** READABILITY — score **7**.
- **Why:** narration em-dash density **8.79/1000** (over the ~8 target and the `<8` trigger), with several over-used appositive "X — the thing — does Y" cadences the locked voice names as the chief AI tell; longest Part IV chapter with a few dense stacked sentences.
- **Single highest-leverage move:** convert the over-used appositive em-dashes to commas / parentheses / periods and break the two densest narration runs into varied-length sentences — fully in-bounds (no fact, no padding, no scope, no floor risk).

---

## Line-level fixes applied (the lift list)

| # | Cluster | Location | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | READABILITY | Deep dive ¶ (L124) | double appositive em-dash | "four steps — select, predicate, report — written" → "four steps (select, predicate, report) written" |
| 2 | READABILITY | Deep dive ¶ (L132) | double appositive em-dash | "predicate and condition — the filter and the constraint — combined" → "predicate and condition (the filter and the constraint), combined" |
| 3 | READABILITY | How-it-works ¶ (L85) | appositive em-dash | "code the team now owns — tested…" → "code the team now owns: tested…" |
| 4 | READABILITY | CONCEPT callout (L75) | appositive em-dash | "substrate and moment — and so" → "substrate and moment, and so" |
| 5 | READABILITY | Lombok debate (L110) | sentence-end restating-noun em-dash | "no dependency — the live question…" → "no dependency, which keeps live the question…" |
| 6 | READABILITY | Lombok debate (L108) | 50-word 4-clause run + em-dash | split into three finite sentences, falling length; em-dash → comma |
| 7 | READABILITY | Lombok debate (L109) | ~60-word stacked run + em-dash | split with a deliberate short beat ("IDEs need a Lombok plugin to resolve the members."); em-dash → comma |

> No code/snippet touched — all seven edits are in narration prose; 7 include markers intact, no rebuild / `check_snippets` required.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed | Narration em-dash /1000 |
|---|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | **42** (9/8/9/9/**7**) | PASS | PASS | PASS | LIFT | independent initial score (post-build, 88% bar) | 8.79 (all-body) |
| 1 | 2026-06-28 | **43** (9/8/9/9/**8**) | PASS | PASS | PASS | LIFT | 5 appositive em-dashes → commas/parens/period (READABILITY) | 7.12 all-body / 4.74 narration |
| 2 | 2026-06-28 | **44** (9/8/9/9/**9**) | PASS | PASS | PASS | **SHIP** | broke 2 densest Lombok runs into varied-length sentences (READABILITY) | 6.65 all-body / 4.14 narration |

Two passes used of the three permitted. ACCURACY (8) was the tied-weakest after pass 1 but is **not in-bounds liftable** (its cap is a correctly-flagged deferred verify surface — pinning the authoring SDKs is a `/pin-source` action, and fabricating verification is forbidden), so the loop correctly targeted READABILITY both passes.

---

## Learnings & pipeline suggestions

- **Re-score, do not trust, a pre-build self-score.** The self-`_SCORE.md` (43/50) was taken before EXAMPLE-BUILD + CODE-REVIEW and against the old 35-bar; under the live 88%/44 bar the independent harsh pass opened at 42, one cluster (READABILITY) below the prior reading. The independent gate is doing real work — keep it different-model and keep the self-score non-approving.
- **Em-dash density is a cheap, decisive READABILITY lever.** A 7-tool survey chapter naturally accretes appositive em-dashes; converting the double-appositive "X — Y — Z" cadence alone moved narration from 8.79 → 4.74/1000 with zero fact risk. Recommend the AUDIT gate report narration density **excluding the back-matter sources apparatus** (citation lists legitimately carry dashes) and excluding figure alt-text — measuring the whole file overstates the tell. A scripted `check_emdash.sh` with that scoping would front-run the manual count.
- **A correctly-flagged deferred verify surface should cap ACCURACY at 8, never trip Floor C.** This chapter is the canonical case: unpinned authoring SDKs + two unconfirmed-verbatim spec spans, all routed to `09-flags/` and never asserted as settled. The right scoring response is an honest 8 and SHIP-on-aggregate, not a floor fail and not an inflated 9 — and the lift loop must not try to "fix" it in-bounds (that would invite fabrication). Worth a one-line note in `SCORING.md`'s ACCURACY anchor that flagged-and-deferred ≠ floor fail but does cap the cluster.
- **Code-comment cross-refs drift from final chapter numbers** (the "Chapter 88" slip, caught at CODE-REVIEW, corrected to Ch 45). Endorse the code-reviewer's suggestion: a cheap lint that greps `08-companion-code/**` comments for `Chapter NN` and diffs against the `FINAL_INDEX.md` chapter set, run before the score gate.
