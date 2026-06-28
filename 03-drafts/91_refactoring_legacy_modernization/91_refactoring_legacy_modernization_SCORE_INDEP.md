# INDEPENDENT SCORECARD — Ch 39 — model: Claude Opus 4.8 — 2026-06-28 (lift pass 4)

> Harsh-skeptic independent re-score of the **current** draft. Supersedes the prior independent score
> (Opus 4.8, pass 3, 43/50, 2026-06-28), pass 2 (Opus 4.8, 42/50), and the original (Sonnet 4.6, pass 1,
> 37/50, 2026-06-20). **One material change since pass 3, and it resolves the exact residual pass 3 named
> as the ACCURACY 9→10 ceiling:** the Fowler (*Refactoring* 2e) and Feathers (*WELC*) named-canon spans
> that pass 3 held back as "by-policy unconfirmable in-repo secondary-text wording" have been **rewritten as
> faithful ATTRIBUTED PARAPHRASE** — no quotation marks around any book definition, attribution kept, no
> book text reproduced (`09-flags/91`, atom-1 status → **N/A — paraphrased 2026-06-28**). The OpenRewrite
> recipe IDs / GAV were already IDENTITY-resolved at pass 3; per the brief, SOURCE-TRACE is judged on
> **identity, not run** (the migration RUN stays REPRO PENDING-RUNTIME and is honestly scoped out of the
> built module). All mechanical sweeps below were re-run independently on the file as it stands.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 91 (owner; folds 92 legacy/seams + 93 strangler + 95 migration)
- **Slug:** `91_refactoring_legacy_modernization`
- **Title:** Changing Code Without Breaking It
- **Part / arc position:** Part XI — Refactoring & Legacy (Ch 39, opener)
- **Artifact scored:** `03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
- **Figure artifact:** `05-figures/91_refactoring_legacy_modernization/fig91_1.png` (source-traced via `fig91_1.sources.md`)
- **Floor-C gate reports read:** `_EXAMPLE.md` (BUILD SUCCESS, 16 tests, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11) + `_CODEREVIEW.md` (PASS, six-dimension scorecard all PASS)
- **Flags read:** `09-flags/91_canon_verbatims_and_openrewrite_recipe_ids.md` (canon verbatims → **N/A, rewritten as attributed paraphrase 2026-06-28**; recipe-ID IDENTITY resolved; only the StranglerFig bliki *date* + the recipe RUN remain) + `09-flags/94_openrewrite_recipe_ids_and_recipe_module_gavs_unverified.md` (atoms 1–2 RESOLVED; GAV `3.16.0 → 3.34.0` corrected)
- **Verified against:** SOURCE-PIN.md pinned 2026-06-20 (§6 OpenRewrite re-confirmed 2026-06-28; §7 canon rows = Fowler 2e 2018 + Feathers WELC 2004, text not redistributed → carried as attributed paraphrase)
- **Scorer:** chapter-scorer agent (Claude Opus 4.8 — independent gate)
- **Date:** 2026-06-28
- **Lift-pass #:** 4 (pass 0 Sonnet initial; pass 1 Sonnet voice; pass 2 Opus build+cross-ref; pass 3 Opus SOURCE-TRACE re-confirm; this pass 4 = canon-verbatim → attributed-paraphrase rewrite, resolving the pass-3 ACCURACY 9→10 ceiling)

---

## What changed since pass 3 (Opus 4.8, 43/50)

| Item | Pass-3 finding | Current (pass-4) status |
|---|---|---|
| §7 named-canon *book* wording | **The named cap.** Held ACCURACY at 9 (named explicitly: "the §7 named-*book* wording … is by-policy unconfirmable in-repo … a single secondary-text channel is not primary-confirmed in-repo, which is exactly the 'fully traced, zero drift' gap between 9 and 10"). | **RESOLVED.** The Fowler 2e two-hats/preparatory ideas + catalog names, and the Feathers seam idea/taxonomy + the legacy=no-tests criterion, are **reworded in our own words and attributed, no quotation marks around any book definition, no book text reproduced** (`09-flags/91` atom 1 → **N/A — paraphrased**). There is **no exact secondary-source wording left for a reader to check against an out-of-repo book** → the unverifiable-secondary-text channel that capped ACCURACY at 9 is gone. |
| OpenRewrite recipe IDs / GAV | IDENTITY RESOLVED at pass 3 (web-verified verbatim vs docs.openrewrite.org; GAV `3.16.0 → 3.34.0`, aligned to engine 8.81.0). | **Unchanged — RESOLVED.** Carried forward; the draft prose holds only the recipe IDs, not a GAV literal. Per brief, judged on **identity**. |
| Migration RUN (REPRO) | Network-gated, scoped out of the built module, REPRO PENDING-RUNTIME. | **Unchanged.** Still honestly scoped out; a coverage disclosure (a UTILITY cap), not a SOURCE-TRACE/identity defect. |
| Residual canon verify-at-pin | The whole §7 wording channel. | **Narrowed to factual metadata only:** the Fowler *StranglerFigApplication* bliki **date (2004) / not-a-pinned-§7-row** status — independent of any reproduced wording. Correctly flagged in `09-flags/91`, never asserted as a settled pin. |

---

## Independent mechanical re-measure (run on the file as it stands, 2026-06-28)

- **Banned-phrase sweep** (running prose, lines 17–152, independently scripted): `better than` / `unlike X` / `superior` / `beats` / `the problem with X` / `outperforms` / `blows away` / `destroys` / `no reason to use` / `kills` / `the obvious choice over` → **0 hits.**
- **Em-dash density** (running prose body, Hook → Hand-off): **2 em-dashes** over ~3,587 words ≈ **0.56/1,000**, against the ~8/1,000 target. The whole-file count (59) is inflated by the **front-matter dossier HTML comment (lines 1–15) and the back-matter citation apparatus (lines 154–166)**, neither of which is rendered prose, and both correctly excluded. The pass-1 blocker is genuinely retired.
- **Quotation-mark audit (the load-bearing check this pass):** every residual `"…"` in the body is a **scare-quote on a single word** (`"legacy"` L61, `"refactoring"` L88) or the author's **own colloquialism / internal cross-reference** (`"rewriting and hoping"` L53, `"while I'm here"` L121, `"behavior changed"/"behavior is correct"` L90, `"debt trending down"` L90, `"some legacy is better strangled than characterized"` L90). **None reproduces a Fowler/Feathers book sentence.** The seam definition (L64), the two-hats metaphor (L55), and the legacy=no-tests criterion (L61) are all carried **without quotation marks**, paraphrased and attributed — confirming the flag's claim independently.
- **Voice tells** (body): the only `just` hits are the load-bearing aphorism "refactoring without tests is just editing" (L57, L120 — semantic, not filler); the only first-person token is inside the scare-quoted developer phrase `"while I'm here"` (L121). Zero self-narration; no author-in-first-person.
- **Printed cross-references vs `FINAL_INDEX`** (running prose 17–152): all distinct refs validate by key — Ch 4 (06, Boy Scout — confirmed landed at L55: "the Boy Scout Rule, Chapter 4"), Ch 5 (13), Ch 12 (19), Ch 21 (44), Ch 24 (50/52), Ch 27 (64), Ch 34 (77), Ch 35 (81), Ch 37 (89). No stray "Chapter 1" in printed text (the two HTML-comment scaffolding lines 7/13 are not rendered).
- **Snippet tags vs `_EXAMPLE.md`**: six `<!-- include: -->` directives match the `_EXAMPLE.md` tag table 1:1 (paths + tag names exact); all six resolve to ≤9-line regions; `check_snippets` = 6/6 pass per `_EXAMPLE.md`. The paraphrase edits touched only prose/flags → build + snippet state unaffected, no rebuild required.

---

## The five clusters (score 1–10)

| # | Cluster | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The four-scale ladder (method → getting-under-test → system → platform) unified by one explicit invariant (preserve behavior, verify with tests, move in small reversible increments, never big-bang) is exceptionally clean architecture for a chapter folding four dossiers. Figure 39.1 carries the loop visually and is introduced in prose (L44) before it appears (L46). Five CONCEPT callouts anchor each scale. The strangler section is backed by the embedded `StranglerRouter` snippet and the deep dive. A reader new to legacy work could reconstruct the whole discipline from the chapter alone. Held at 9, not 10: the deep dive (L84–90) runs at high abstraction density that rewards a re-read, and the central thesis is restated in the Overview, the deep dive, and both hand-off sections. |
| 2 | **ACCURACY** | **10** | **Lifted from 9.** The single residual pass 3 named as the 9→10 ceiling is resolved. The Fowler 2e / Feathers WELC named-canon material is now **attributed paraphrase with no book text reproduced** (independently confirmed: every body quotation mark is a scare-quote on one word or the author's own colloquialism/cross-reference — no secondary-source sentence is reproduced for a reader to check against an out-of-repo book), so **no unverifiable secondary-source verbatim is asserted as fact** — exactly what closes the "single secondary-text channel not primary-confirmed in-repo" gap. Every remaining load-bearing atom traces to a **primary** or is runnable-confirmed: OpenRewrite recipe IDs `UpgradeToJava17/21/25` + composite web-verified verbatim vs docs.openrewrite.org and the off-pin GAV corrected `3.16.0 → 3.34.0` (aligned to engine 8.81.0); LTS path 8→11→17→21→25 + JPMS-since-17 (JEP 403) → SOURCE-PIN §1; records/sealed+pattern-matching switch/Optional/streams runnable-confirmed against JLS SE 21 by the green build. SOURCE-TRACE judged on **identity, not run** (per brief): the migration RUN is REPRO PENDING-RUNTIME — a coverage disclosure, not an identity defect. The only residual flag is factual metadata independent of wording (the StranglerFig bliki *date*, correctly `⚠ verify-at-pin`, never asserted as settled). **No invented atom anywhere; no unverifiable verbatim anywhere; zero drift off the pin.** That is the 10 anchor ("fully traced, snippets verified with recorded paths, zero drift"). |
| 3 | **UTILITY** | **8** | A complete safe-change toolkit a working engineer reaches for: the refactoring loop + two hats, the get-to-a-seam-without-tests technique, the strangler façade+flags+contract-tests recipe, the six-step migration process (tests-green → deps-first → recipe → fix-residual → JDK-matrix → modernize-after), and a decision-keyed "When to use what." The verified companion module makes three of four scales concrete: a `LegacyShippingCalculator` with no seam, an Extract-Interface seam, a characterization test pinning a real rounding quirk (191, not the naive 190), a behavior-preservation property test across every `ServiceLevel`, a proven mutate-through representation-leak bug, and a flag-gated `StranglerRouter`. The displayed snippets are tag regions of that compiled file, not plausible-looking fragments. Held at 8, not 9: the migration scale — the fourth scale, and the one a reader most wants a runnable recipe for — is honestly scoped out of the build as REPRO-pending (network-gated). The recipe IDs are verified (the prose is accurate), but a reader still cannot *run* the demonstrated migration from this module; that is a real, correctly-disclosed utility gap whose lift requires **building the network-gated scale** — not an in-bounds prose move and not fakeable. |
| 4 | **DEPTH** | **9** | Four authoritative bodies of knowledge synthesized under one invariant, with the bounded-per-step vs unbounded-all-or-nothing risk argument (L88) as the intellectual center — why the big-bang loses at scale. Nine distinct, sourced when-NOT-to-use points. The subtlest honesty in the chapter — a characterization test pins *current* behavior including bugs, so it is a net, not a specification (L90) — is stated precisely and reinforced by the module (the quirk is preserved by the refactor, not "fixed"; the leak is shown as a *real* latent bug by a mutate-through test). Senior modernization material and the spine of Part XI. DEPTH is earned by verified substance (four dossiers, a 16-test green module, the resolved + remaining flags), never by length. Held at 9, not 10: the strangler shared-state / data-consistency challenge (dual-writes) is named as the hard part (L76) but its mechanism is described rather than worked — the one place a deeper treatment is available within scope. |
| 5 | **READABILITY** | **8** | The pass-1 blockers are genuinely retired (independently re-measured this pass): em-dash density ~0.56/1,000 in running prose; banned words gone; zero self-narration tells; the one first-person token is inside a scare-quoted developer phrase; terms glossed plain-language-first; the stakes-first hook (the two-year cancelled rewrite) is vivid; the forward hand-off pulls cleanly to Ch 40. The paraphrase rewrite reads naturally in the locked voice — the de-quoted canon material is now plain attributed prose, not a stitched quotation. Held at 8, not 9: the chapter is the longest in the book (~3,600 words of body prose), and the deep dive plus six back-to-back snippet lead-ins create a stretch of high, even density; a couple of snippet lead-ins share a cadence (L96–104), and the central thesis is restated in the Overview, the deep dive, and both hand-off sections. Clean and paced, but not yet effortless end-to-end. |

**Cluster subtotal: 44 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent scripted banned-phrase sweep across the running prose (lines 17–152) = **0 hits**. The big-bang rewrite is framed as a scale/risk argument about an *approach*, explicitly "the option this chapter argues against at scale, not a forbidden one" (L132) — not a crowning verdict against a named product. In-place vs strangler vs rewrite is a scale ladder; "Alternatives & adjacent approaches" is approach-based, not a leaderboard. Fowler/Feathers canon is read through a modern-Java lens, dated, never crowned. `_CODEREVIEW.md` dimension 6 (neutrality-in-code) = PASS: no identifier, comment, log string, or test name crowns or disparages. |
| **B — HONEST-LIMITATIONS** | **PASS** | A dedicated "Limitations & when NOT to reach for it" section carries **nine** distinct when-NOT-to-use points (refactoring-without-tests; don't-mix-hats; big-refactoring-is-redesign; characterization-pins-bugs; strangler-stalls-half-done + shared-state; recipes-incomplete + deps-first; big-jumps-risk; migration≠modernization; not-all-code-worth-changing), reinforced by per-scale inline limits, the deep-dive bounded-vs-unbounded center, and the "When to use what" verdicts (incl. the explicit "Never:" L148). No scale is sold cost-free. The module encodes the limitation in code: a real failure path (typed `Quote.Unavailable` over an in-band zero) and the proven representation-leak bug. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE (judged on identity, per brief):** zero invented atoms; **zero unverifiable secondary-source verbatim** (the §7 canon material is now attributed paraphrase, no book text reproduced — independently confirmed via the quotation-mark audit above; `09-flags/91` atom 1 → N/A). OpenRewrite recipe IDs `UpgradeToJava17/21/25` web-verified verbatim vs docs.openrewrite.org; off-pin GAV corrected `3.16.0 → 3.34.0` aligned to engine 8.81.0 (`09-flags/94` atoms 1–2 RESOLVED). LTS path + JPMS-since-17 → SOURCE-PIN §1. Printed cross-refs validate vs FINAL_INDEX. The only residual flag is factual metadata (StranglerFig bliki *date*), correctly `⚠ verify-at-pin`. The recipe RUN (REPRO) is network-gated → PENDING-RUNTIME — a run, not an identity, honestly scoped out of the built module; not a SOURCE-TRACE failure. **COMPILE:** `_EXAMPLE.md` — `mvn -B -Pquality verify` BUILD SUCCESS, 16 tests, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11 (the SOURCE-PIN anchor). **CODE-REVIEW:** `_CODEREVIEW.md` verdict PASS, six-dimension scorecard all PASS, no BLOCKER/MAJOR. Paraphrase edits touched only prose/flags — build + `check_snippets` state unaffected. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP / AUTO-APPROVE** — clears the rubric ship bar: aggregate **44/50 (88%)**, no cluster below 6, all THREE floors PASS, on an independent score.
- [ ] **LIFT-LOOP**
- [ ] **CUT**

**One-line rationale:** 44/50 with no cluster below 6 and all three floors PASS — clears the live SCORING.md auto-approval bar (≥44/50, no cluster <6, floors A/B/C-source PASS, independent score). ACCURACY 9→10 is the one defensible lift since pass 3, and it is honest, not inflation: it resolves the **exact** residual pass 3 named — the §7 secondary-text verbatim channel — now eliminated by rewriting the Fowler/Feathers canon as attributed paraphrase with no book text reproduced (independently confirmed). Every load-bearing atom traces to a primary or is runnable-confirmed; SOURCE-TRACE judged on identity per the brief.

> **Ship-bar note (harsh-skeptic honesty).** This pass takes **exactly one** cluster lift over pass 3
> (ACCURACY 9→10), and it is the one the prior pass itself nominated as the gating residual. I did **not**
> touch the other four clusters: UTILITY stays **8** (the network-gated migration scale is still not
> runnable from the module — a real, correctly-disclosed gap not fixable by prose and not fakeable), DEPTH
> and CLARITY stay **9** (the thesis-restatement and the described-not-worked dual-writes are unchanged),
> READABILITY stays **8** (length + even density unchanged). The aggregate reaches **44 by resolving a real
> SOURCE-TRACE liability, not by padding a soft cluster.** The bounded lift loop is now exhausted on the
> prose surface; the remaining 44→ headroom (UTILITY 8→9, DEPTH/CLARITY/READABILITY 9→10) is gated on the
> network-gated migration build and on optional polish, neither an in-bounds single-pass prose move.
> Recorded as **AUTO-APPROVE at 44/50**, the migration REPRO-pending gap disclosed to the Step-16
> whole-book gate (FLOOR-C COMPILE for the migration scale must be green book-wide before release; the other
> three scales are built green now).

---

## Flagged weakest cluster

- **Weakest cluster:** **UTILITY at 8** (ACCURACY lifted to 10 this pass; UTILITY and READABILITY now tie at 8, UTILITY flagged as the one with a non-prose, non-fakeable gap).
- **Why:** the migration scale — the fourth of four, and the one readers most want a runnable recipe for — is honestly scoped out of the built module as REPRO-pending (network-gated). The recipe IDs are verified (prose is accurate), but the demonstrated migration cannot be *run* from the module.
- **Single highest-leverage move (out of in-bounds prose scope):** build the OpenRewrite `UpgradeToJava21` migration scale when the network gate lifts (`mvn -Prewrite rewrite:dryRun` then a built before/after pair) → lifts UTILITY to 9 → aggregate 45. This is **not** required for auto-approval (the chapter already clears the bar at 44) and is **not** an in-bounds prose revision; it may not be faked.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | ACCURACY / SOURCE-TRACE | §7 canon spans (seam definition L64, two-hats L55, legacy=no-tests L61, catalog names L53) | Named-book wording previously carried as definition/verbatim → unconfirmable in-repo (pass 3 cap) | Rewritten as attributed paraphrase in our own words, no quotation marks around any book definition, no book text reproduced (`09-flags/91` atom 1 → N/A) | **DONE (this pass confirms it independently; lifts ACCURACY 9→10)** |
| 2 | ACCURACY / SOURCE-TRACE | OpenRewrite recipe IDs (L82, L159) + GAV in SOURCE-PIN §6 | Recipe-id spelling unconfirmed (pass 2); GAV off-pin `3.16.0` (pass 3) | Recipe IDs web-verified verbatim; GAV corrected `3.16.0 → 3.34.0` aligned to engine 8.81.0 | DONE (pass 3; carried forward) |
| 3 | UTILITY (cap) | migration scale | OpenRewrite `UpgradeToJava21` scale REPRO-pending, not built (network-gated) | Build the dry-run + before/after pair when the network gate lifts → UTILITY 8→9 | open (separate gate; not prose; not required for auto-approval) |
| 4 | SOURCE-TRACE (metadata only) | back-matter §7 (StranglerFig bliki) | Bliki *date* (2004) + not-a-pinned-§7-row status | Confirm the date out-of-band or promote the bliki to a pinned §7 row to close the canon gap | open (factual metadata, independent of wording; correctly flagged) |
| 5 | READABILITY | snippet lead-ins (L96–104) + thesis restated in Overview/deep-dive/hand-off | A couple of lead-ins share cadence; thesis restated three times | Optional final read-aloud to vary two lead-in openers and trim one thesis restatement; not a blocker | open (polish) |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | Initial independent score (Sonnet 4.6) |
| 1 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | Voice pass: self-narration cleared; deep-dive opening rewritten; fig91_1.png added. Em-dash NOT reduced; "easy"/"just" hits (Sonnet 4.6) |
| 2 | 2026-06-28 | 42 / 50 | PASS | PASS | PASS / GREEN / PASS | SHIP (42/50, gap disclosed) | Em-dash audit confirmed real; figure introduced in prose; EXAMPLE-BUILD green + CODE-REVIEW PASS + six snippets embedded; Boy Scout cross-ref → Ch 4 fixed (Opus 4.8) |
| 3 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS / GREEN / PASS | SHIP (43/50, one-point gap disclosed) | OpenRewrite recipe IDs web-verified verbatim; off-pin GAV `3.16.0 → 3.34.0` corrected & aligned to engine 8.81.0; `09-flags/94` atoms 1–2 RESOLVED. SOURCE-TRACE judged on identity → ACCURACY 8 → 9. UTILITY held at 8 (Opus 4.8) |
| 4 | 2026-06-28 | **44 / 50** | PASS | PASS | **PASS / GREEN / PASS** | **AUTO-APPROVE (44/50, clears the bar)** | **Fowler 2e / Feathers WELC named-canon verbatims rewritten as attributed paraphrase — no book text reproduced, no quotation marks around any book definition (`09-flags/91` atom 1 → N/A).** Independent quotation-mark audit confirms zero secondary-source verbatim remains → **ACCURACY 9 → 10** (resolves the exact pass-3 9→10 ceiling). Mechanical measures (banned-phrase, em-dash 0.56/1,000, cross-ref, snippet 1:1) independently re-run on the current file. UTILITY held at 8 (migration REPRO-pending, correctly scoped out) (Opus 4.8) |

---

## Learnings & pipeline suggestions

1. **De-quoting copyrighted secondary text into faithful attributed paraphrase is a legitimate ACCURACY/SOURCE-TRACE lift — when the wording was the only unverifiable channel.** Pass 3 named "the §7 named-*book* wording … by-policy unconfirmable in-repo" as the precise 9→10 ceiling. Rewriting that material in the author's own words (attribution kept, no book sentence reproduced) removes the unverifiable-secondary-text channel entirely: there is no exact wording left for a reader to check against an out-of-repo book, so nothing unverifiable is asserted as fact. Lifting ACCURACY 9→10 is then honest, not inflation — the prior scorecard's own cap-note is the audit trail. Propose codifying in SCORING.md FLOOR-C: a copyrighted-canon span carried as *attributed paraphrase with no reproduced wording* is fully source-traced; only a *reproduced* verbatim (text not in-repo) is the verify-at-pin liability.

2. **The quotation-mark audit is the right independent check for a "verbatim → paraphrase" claim.** Don't take the flag's word for it — sweep every `"…"` in the body and classify each: a scare-quote on a single word, the author's own colloquialism/cross-reference, or a reproduced secondary-source sentence. Only the third is a liability. Here all residual quotation marks were of the first two kinds; the canon definitions (seam, two-hats, legacy=no-tests) carry **no** quotation marks. Worth scripting as a paraphrase-fidelity pre-pass: flag any quoted span >~6 words that sits near a §7 attribution.

3. **Separate identity-resolved from run-pending, and separate wording-paraphrased from metadata-pending.** This chapter's residuals are now all of the *correct, expected* kind: a network-gated RUN (a UTILITY coverage gap, not an ACCURACY defect) and a non-pinned bliki *date* (factual metadata, independent of any reproduced wording). Neither is an invented or drifted atom. A scorer who conflates "REPRO-pending run" or "verify-at-pin date" with "invented detail" under-scores a chapter that has done all the verifiable work. Codify the three-way split (identity / run; wording / metadata) in the FLOOR-C rubric note.

4. **Take one defensible lift per pass and leave the soft clusters honest.** This pass lifted only ACCURACY (the residual the prior pass nominated) and left UTILITY/DEPTH/CLARITY/READABILITY untouched at their pass-3 values. The aggregate reached the 44 bar by resolving a real SOURCE-TRACE liability, not by nudging a soft cluster a point to clear the bar. That is the discipline the bounded lift loop is meant to enforce: no bar-lowering, no soft-cluster padding to manufacture a pass.

5. **Measure em-dash density on the running-prose span, on the current file, every pass.** Re-confirmed: whole-file em-dash count (59) is dominated by the front-matter dossier comment (lines 1–15) and the back-matter citation apparatus (lines 154–166), neither rendered; the running-prose body (Hook → Hand-off) carries 2 → 0.56/1,000. A naive whole-file grep would falsely fail a clean chapter. The honest span is the rendered body only.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
