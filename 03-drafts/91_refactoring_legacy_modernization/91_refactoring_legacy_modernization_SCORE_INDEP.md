# INDEPENDENT SCORECARD — Ch 39 — model: Claude Opus 4.8 — 2026-06-28 (lift pass 5)

> Harsh-skeptic independent re-score of the **current** draft. Supersedes pass 4 (Opus 4.8, 44/50,
> 2026-06-28), pass 3 (Opus 4.8, 43/50), pass 2 (Opus 4.8, 42/50), and the original (Sonnet 4.6, pass 1,
> 37/50, 2026-06-20). **One material change since pass 4, and it resolves the exact residual pass 4 named
> as the UTILITY 8→9 cap:** the OpenRewrite migration scale — pass 4's only non-prose, non-fakeable gap,
> nominated as "lift requires building the network-gated scale" — has now **actually RUN**. With Maven
> Central reachable, `mvn -B -Prewrite … rewrite:dryRun` resolved the pinned engine + recipe module
> (rewrite-core **8.81.0** / rewrite-migrate-java **3.34.0** / rewrite-maven-plugin **6.38.0** — exactly
> SOURCE-PIN §6), built the Lossless Semantic Tree over the module sources, ran the composite recipe
> `org.acme.remediation.ModernizeForJava21` (which chains `UpgradeToJava21`), emitted a real proposed
> patch, and finished `BUILD SUCCESS` — `dryRun` mutated nothing and the default build stays green
> (evidence: `03-drafts/96_remediation_playbook_automated_change/96_…_EXAMPLE.md`, "REPRO — UPGRADED
> 2026-06-28", which states verbatim "This lifts the UTILITY cap on Ch 39/Ch 40"). The canon verbatims
> were already rewritten as attributed paraphrase at pass 4 (ACCURACY 10, unchanged). All mechanical
> sweeps below were re-run independently on the file as it stands.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 91 (owner; folds 92 legacy/seams + 93 strangler + 95 migration)
- **Slug:** `91_refactoring_legacy_modernization`
- **Title:** Changing Code Without Breaking It
- **Part / arc position:** Part XI — Refactoring & Legacy (Ch 39, opener)
- **Artifact scored:** `03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
- **Figure artifact:** `05-figures/91_refactoring_legacy_modernization/fig91_1.png` (source-traced via `fig91_1.sources.md`)
- **Floor-C gate reports read:** Ch 39 own module — `_EXAMPLE.md` (BUILD SUCCESS, 16 tests, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11) + `_CODEREVIEW.md` (PASS, six-dimension scorecard all PASS); migration-scale run evidence — `03-drafts/96_remediation_playbook_automated_change/96_…_EXAMPLE.md` (the OpenRewrite engine Ch 39 hands forward; `rewrite:dryRun` resolved + validated + RAN at the pin, BUILD SUCCESS, nothing mutated)
- **Flags read:** `09-flags/91_canon_verbatims_and_openrewrite_recipe_ids.md` (canon verbatims → **N/A, attributed paraphrase 2026-06-28**; recipe-ID IDENTITY resolved; recipe RUN now corroborated by execution) + `09-flags/94_openrewrite_recipe_ids_and_recipe_module_gavs_unverified.md` (atoms 1–2 RESOLVED; GAV `3.16.0 → 3.34.0`)
- **Verified against:** SOURCE-PIN.md pinned 2026-06-20 (§6 OpenRewrite re-confirmed 2026-06-28 — engine 8.81.0 / plugin 6.38.0 / recipe module rewrite-migrate-java 3.34.0, all matched at the executed run; §7 canon rows = Fowler 2e 2018 + Feathers WELC 2004, text not redistributed → carried as attributed paraphrase)
- **Scorer:** chapter-scorer agent (Claude Opus 4.8 — independent gate)
- **Date:** 2026-06-28
- **Lift-pass #:** 5 (pass 0 Sonnet initial; 1 Sonnet voice; 2 Opus build+cross-ref; 3 Opus SOURCE-TRACE re-confirm; 4 Opus canon-verbatim → attributed-paraphrase, ACCURACY 9→10; this pass 5 = OpenRewrite migration scale RAN green at the pin, resolving the pass-4 UTILITY 8→9 cap)

---

## What changed since pass 4 (Opus 4.8, 44/50)

| Item | Pass-4 finding | Current (pass-5) status |
|---|---|---|
| Migration RUN (REPRO) | **The named cap.** Held UTILITY at 8: "a reader still cannot *run* the demonstrated migration from this module; that is a real, correctly-disclosed utility gap whose lift requires **building the network-gated scale** — not an in-bounds prose move and not fakeable." | **RESOLVED (the run now exists).** `rewrite:dryRun` actually RAN at the pin in the companion repo (the Ch 40 / key-96 module — the engine Ch 39 explicitly hands the migration forward to): it resolved rewrite-core 8.81.0 / rewrite-migrate-java 3.34.0 / rewrite-maven-plugin 6.38.0, built the LST, ran the composite recipe (chaining `UpgradeToJava21`), emitted a real patch, `BUILD SUCCESS`, mutated nothing, default build still green (`96_…_EXAMPLE.md`). The demonstrated migration is now reproducible from the repo by a reader following the book → the exact pass-4 gating condition ("build the network-gated scale") is satisfied. **UTILITY 8 → 9.** |
| OpenRewrite recipe IDs / GAV | IDENTITY web-verified at pass 3; ACCURACY-10 channel resolved at pass 4. | **Strengthened, unchanged score.** The recipe IDs the Ch 39 prose names (`UpgradeToJava17/21/25`, composite) are now **corroborated by execution**, not just web-lookup — a stronger ACCURACY proof at the same anchor (already 10). Versions match SOURCE-PIN §6 exactly; no drift, no AHEAD-OF-PIN. |
| §7 named-canon wording | RESOLVED at pass 4 (attributed paraphrase, no book text reproduced). | **Unchanged — RESOLVED.** ACCURACY stays 10. |
| Residual verify-at-pin | StranglerFig bliki *date* (2004) / canon-gap; recipe RUN. | **Narrowed to metadata only.** The recipe RUN is now done; the sole residual is the Fowler *StranglerFigApplication* bliki **date (2004) / not-a-pinned-§7-row** — factual metadata, independent of wording, correctly flagged, never asserted as settled. |

---

## Independent mechanical re-measure (run on the file as it stands, 2026-06-28)

- **Banned-phrase sweep** (running prose, lines 17–152): `better than` / `unlike X` / `superior` / `beats` / `the problem with X` / `outperforms` / `blows away` / `destroys` / `no reason to use` / `kills` / `the obvious choice over` → **0 hits.**
- **Em-dash density** (running prose body, Hook → Hand-off): **2 em-dashes** over ~3,587 words ≈ **0.56/1,000**, against the ~8/1,000 target. The whole-file count (59) is dominated by the front-matter dossier HTML comment (lines 1–15) and the back-matter citation apparatus (lines 154–166), neither rendered prose, both correctly excluded.
- **Quotation-mark audit (carried from pass 4, re-confirmed):** every residual `"…"` in the body is a scare-quote on a single word (`"legacy"` L61, `"refactoring"` L88) or the author's own colloquialism / internal cross-reference (`"rewriting and hoping"` L53, `"while I'm here"` L121, `"behavior changed"/"behavior is correct"` L90, `"debt trending down"` L90, `"some legacy is better strangled than characterized"` L90). The seam definition (L64), two-hats metaphor (L55), and legacy=no-tests criterion (L61) carry **no** quotation marks — paraphrased and attributed. No Fowler/Feathers book sentence is reproduced.
- **Voice tells** (body): the only `just` hits are the load-bearing aphorism "refactoring without tests is just editing" (L57, L120 — semantic, not filler); the only first-person token is inside the scare-quoted developer phrase `"while I'm here"` (L121). Zero self-narration.
- **Printed cross-references vs `FINAL_INDEX`** (running prose 17–152): all distinct refs validate by key — Ch 4 (06, Boy Scout, L55), Ch 5 (13), Ch 12 (19), Ch 21 (44), Ch 24 (50/52), Ch 27 (64), Ch 34 (77), Ch 35 (81), Ch 37 (89). The forward hand-off to Ch 40 (automated change / OpenRewrite) is the chapter that now carries the run migration. No stray printed "Chapter 1".
- **Snippet tags vs `_EXAMPLE.md`**: six `<!-- include: -->` directives match the Ch 39 `_EXAMPLE.md` tag table 1:1 (paths + tag names exact); all six resolve to ≤9-line regions; `check_snippets` = 6/6 pass. The migration run touched only the Ch 40 module + REPRO state → Ch 39 build + snippet state unaffected, no rebuild required.

---

## The five clusters (score 1–10)

| # | Cluster | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The four-scale ladder (method → getting-under-test → system → platform) unified by one explicit invariant (preserve behavior, verify with tests, move in small reversible increments, never big-bang) is exceptionally clean architecture for a chapter folding four dossiers. Figure 39.1 carries the loop visually and is introduced in prose (L44) before it appears (L46). Five CONCEPT callouts anchor each scale; the strangler section is backed by the embedded `StranglerRouter` snippet and the deep dive. A reader new to legacy work could reconstruct the whole discipline from the chapter alone. Held at 9, not 10: the deep dive (L84–90) runs at high abstraction density that rewards a re-read, and the central thesis is restated in the Overview, the deep dive, and both hand-off sections. Unchanged this pass (the migration run is not a clarity move). |
| 2 | **ACCURACY** | **10** | Every load-bearing atom traces to a **primary** or is runnable-confirmed, and the one channel that capped it at 9 (the §7 secondary-text verbatim) was eliminated at pass 4 by rewriting the Fowler 2e / Feathers WELC canon as attributed paraphrase with no book text reproduced (independently re-confirmed by the quotation-mark audit above). **This pass strengthens the 10 without inflating it:** the OpenRewrite recipe IDs the prose names (`UpgradeToJava17/21/25`, composite 25 ⊇ 21) are now **corroborated by execution** — `rewrite:dryRun` resolved + validated + ran them at the pinned engine line (rewrite-core 8.81.0 / rewrite-migrate-java 3.34.0 / rewrite-maven-plugin 6.38.0, matching SOURCE-PIN §6 exactly), not merely a web-lookup. The "recipes don't cover everything / budget residual manual" framing (L82) is corroborated, not contradicted, by the near-empty real diff (only a stale plugin bump). LTS path 8→11→17→21→25 + JPMS-since-17 (JEP 403) → SOURCE-PIN §1; records/sealed+pattern-matching switch/Optional/streams runnable-confirmed against JLS SE 21 by the green build. No drift, no AHEAD-OF-PIN. The sole residual flag is factual metadata independent of wording (the StranglerFig bliki *date*, correctly `⚠ verify-at-pin`). **No invented atom; no unverifiable verbatim; zero drift.** The 10 anchor ("fully traced, snippets verified with recorded paths, zero drift"). |
| 3 | **UTILITY** | **9** | **Lifted from 8 — the one defensible lift this pass, and the exact gap pass 4 nominated.** A complete safe-change toolkit a working engineer reaches for: the refactoring loop + two hats, the get-to-a-seam-without-tests technique, the strangler façade+flags+contract-tests recipe, the six-step migration process (tests-green → deps-first → recipe → fix-residual → JDK-matrix → modernize-after), and a decision-keyed "When to use what." Three of four scales were already concrete in Ch 39's own green module (legacy/under-test/refactor/system — a no-seam `LegacyShippingCalculator`, an Extract-Interface seam, a characterization test pinning a real rounding quirk 191-not-190, a behavior-preservation property test across every `ServiceLevel`, a proven mutate-through leak bug, a flag-gated `StranglerRouter`); the displayed snippets are tag regions of that compiled file. **The fourth scale — migration, the one a reader most wants a runnable recipe for — is now actually runnable from the companion repo:** the engine Ch 39 hands forward (Ch 40 / key-96 module) RAN the migration green at the pin (`rewrite:dryRun` resolved the pinned engine+recipe module, ran the composite recipe, emitted a real patch, default build still green; `96_…_EXAMPLE.md` states verbatim "This lifts the UTILITY cap on Ch 39/Ch 40"). So the demonstrated migration is reproducible by a reader following the book — the precise pass-4 gating condition ("build the network-gated scale") is met. Held at 9, not 10: the run lives in the Ch 40 module Ch 39 hands off to, not in Ch 39's *own* `91_` module (which still scopes migration out, correctly — migration ≠ modernization). For *this* chapter the migration is runnable-in-the-companion-repo-and-handed-forward rather than runnable-in-this-chapter's-own-module; that honest residual is the 9-not-10. |
| 4 | **DEPTH** | **9** | Four authoritative bodies of knowledge synthesized under one invariant, with the bounded-per-step vs unbounded-all-or-nothing risk argument (L88) as the intellectual center — why the big-bang loses at scale. Nine distinct, sourced when-NOT-to-use points. The subtlest honesty in the chapter — a characterization test pins *current* behavior including bugs, so it is a net, not a specification (L90) — is stated precisely and reinforced by the module (the quirk is preserved by the refactor, not "fixed"; the leak is shown as a *real* latent bug by a mutate-through test). DEPTH is earned by verified substance (four dossiers, a 16-test green module, the now-executed migration recipe, the resolved + remaining flags), never by length. Held at 9, not 10: the strangler shared-state / data-consistency challenge (dual-writes) is named as the hard part (L76) but its mechanism is described rather than worked. Unchanged this pass. |
| 5 | **READABILITY** | **8** | The pass-1 blockers are genuinely retired (independently re-measured): em-dash density ~0.56/1,000 in running prose; banned words gone; zero self-narration tells; the one first-person token is inside a scare-quoted developer phrase; terms glossed plain-language-first; the stakes-first hook (the two-year cancelled rewrite) is vivid; the forward hand-off pulls cleanly to Ch 40. The de-quoted canon material reads as plain attributed prose in the locked voice. Held at 8, not 9: the chapter is the longest in the book (~3,600 words of body prose), and the deep dive plus six back-to-back snippet lead-ins create a stretch of high, even density; a couple of snippet lead-ins share a cadence (L96–104), and the central thesis is restated in the Overview, the deep dive, and both hand-off sections. Clean and paced, but not yet effortless end-to-end. Unchanged this pass. |

**Cluster subtotal: 45 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent banned-phrase sweep across running prose (lines 17–152) = **0 hits**. The big-bang rewrite is framed as a scale/risk argument about an *approach*, explicitly "the option this chapter argues against at scale, not a forbidden one" (L132) — not a crowning verdict against a named product. In-place vs strangler vs rewrite is a scale ladder; "Alternatives & adjacent approaches" is approach-based, not a leaderboard. Fowler/Feathers canon is read through a modern-Java lens, dated, never crowned. `_CODEREVIEW.md` dimension 6 (neutrality-in-code) = PASS: no identifier, comment, log string, or test name crowns or disparages. The migration run introduced no comparative claim. |
| **B — HONEST-LIMITATIONS** | **PASS** | A dedicated "Limitations & when NOT to reach for it" section carries **nine** distinct when-NOT-to-use points (refactoring-without-tests; don't-mix-hats; big-refactoring-is-redesign; characterization-pins-bugs; strangler-stalls-half-done + shared-state; recipes-incomplete + deps-first; big-jumps-risk; migration≠modernization; not-all-code-worth-changing), reinforced by per-scale inline limits, the deep-dive bounded-vs-unbounded center, and the "When to use what" verdicts (incl. the explicit "Never:" L148). No scale is sold cost-free; the executed migration's near-empty real diff is itself the "recipes don't cover everything → budget residual manual" limitation shown true. The module encodes a real failure path (typed `Quote.Unavailable` over an in-band zero) and a proven representation-leak bug. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE (identity):** zero invented atoms; zero unverifiable secondary-source verbatim (the §7 canon is attributed paraphrase, no book text reproduced; `09-flags/91` atom 1 → N/A). OpenRewrite recipe IDs `UpgradeToJava17/21/25` now **corroborated by execution** (resolved + validated + RAN at the pin) on top of the prior verbatim web-verification; GAV `3.16.0 → 3.34.0` aligned to engine 8.81.0 (`09-flags/94` atoms 1–2 RESOLVED). LTS path + JPMS-since-17 → SOURCE-PIN §1. The sole residual flag is factual metadata (StranglerFig bliki *date*), correctly `⚠ verify-at-pin`. **COMPILE:** Ch 39 own module — `mvn -B -Pquality verify` BUILD SUCCESS, 16 tests, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11 (`_EXAMPLE.md`); the migration scale, scoped out of Ch 39's own module, is now **built/run green** in the Ch 40 / key-96 companion module to which Ch 39 hands the engine (`96_…_EXAMPLE.md`, `rewrite:dryRun` BUILD SUCCESS at the pin, nothing mutated, default build green) — book-wide FLOOR-C COMPILE for the migration scale is green, no longer pending. **CODE-REVIEW:** `_CODEREVIEW.md` verdict PASS, six-dimension scorecard all PASS, no BLOCKER/MAJOR. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP / AUTO-APPROVE** — clears the rubric ship bar: aggregate **45/50 (90%)**, no cluster below 6, all THREE floors PASS, on an independent score.
- [ ] **LIFT-LOOP**
- [ ] **CUT**

**One-line rationale:** 45/50 with no cluster below 6 and all three floors PASS — clears the live SCORING.md auto-approval bar (≥44/50, no cluster <6, floors A/B/C-source PASS, independent score) with one point of headroom. UTILITY 8→9 is the single defensible lift since pass 4, and it is honest, not inflation: it resolves the **exact** residual pass 4 named — the network-gated migration scale, explicitly "not fakeable" and "requires building the network-gated scale" — which has now RUN green at the pin (`rewrite:dryRun` resolved + validated + ran the composite recipe, real patch emitted, default build still green) in the Ch 40 module Ch 39 hands the engine forward to. The other four clusters are held at their pass-4 values; the aggregate moved by resolving a real, named, non-fakeable coverage gap, not by padding a soft cluster.

> **Ship-bar note (harsh-skeptic honesty).** This pass takes **exactly one** cluster lift over pass 4
> (UTILITY 8→9), and it is the one pass 4 itself nominated as the gating residual and explicitly tagged
> "not an in-bounds prose move and not fakeable." It is now landed by an actual run, not by prose. I did
> **not** touch the other four clusters: ACCURACY stays **10** (the recipe run *strengthens* the trace but
> the anchor was already 10), CLARITY and DEPTH stay **9** (the thesis-restatement and the
> described-not-worked dual-writes are unchanged), READABILITY stays **8** (length + even density
> unchanged). One honest residual keeps UTILITY at 9 rather than 10: the run lives in the Ch 40 companion
> module Ch 39 hands the engine to, not in Ch 39's *own* `91_` module (which correctly scopes migration out
> — migration ≠ modernization). The aggregate reaches **45 by landing a real, named, non-fakeable coverage
> gap, not by nudging a soft cluster.** Recorded as **AUTO-APPROVE at 45/50**; book-wide FLOOR-C COMPILE
> for the migration scale is now green (no longer a Step-16 pending item), the remaining residual being only
> the factual StranglerFig bliki *date* metadata.

---

## Flagged weakest cluster

- **Weakest cluster:** **READABILITY at 8** (UTILITY lifted to 9 this pass; READABILITY is now the sole cluster at 8).
- **Why:** the chapter is the longest in the book (~3,600 words of body prose); the deep dive plus six back-to-back snippet lead-ins create a stretch of high, even density; a couple of snippet lead-ins share a cadence (L96–104); and the central thesis is restated in the Overview, the deep dive, and both hand-off sections.
- **Single highest-leverage move (in-bounds prose):** one final read-aloud pass to vary two snippet lead-in openers (L96–104) and trim one of the three thesis restatements (Overview / deep dive / hand-off) → would lift READABILITY toward 9 → aggregate 46. This is **not** required for auto-approval (the chapter already clears the bar at 45) and is a genuine in-bounds prose move (no new facts, no scope creep, no floor risk).

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | UTILITY | migration scale (the engine Ch 39 hands to Ch 40 / key 96) | Migration `UpgradeToJava21` REPRO-pending at pass 4 (network-gated), demonstrated migration not runnable from the repo | `rewrite:dryRun` RAN at the pin: resolved rewrite-core 8.81.0 / rewrite-migrate-java 3.34.0 / rewrite-maven-plugin 6.38.0, ran the composite recipe, emitted a real patch, BUILD SUCCESS, default build green, nothing mutated (`96_…_EXAMPLE.md`) | **DONE (this pass; lifts UTILITY 8→9)** |
| 2 | ACCURACY / SOURCE-TRACE | §7 canon spans (seam L64, two-hats L55, legacy=no-tests L61, catalog names L53) | Named-book wording carried as definition/verbatim → unconfirmable in-repo (pass 3 cap) | Rewritten as attributed paraphrase, no quotation marks around any book definition, no book text reproduced (`09-flags/91` atom 1 → N/A) | DONE (pass 4; carried forward — ACCURACY 10) |
| 3 | ACCURACY / SOURCE-TRACE | OpenRewrite recipe IDs (L82, L159) + GAV in SOURCE-PIN §6 | Recipe-id spelling unconfirmed (pass 2); GAV off-pin `3.16.0` (pass 3) | Recipe IDs web-verified verbatim then **execution-corroborated** this pass; GAV corrected `3.16.0 → 3.34.0` aligned to engine 8.81.0 | DONE (strengthened this pass) |
| 4 | SOURCE-TRACE (metadata only) | back-matter §7 (StranglerFig bliki) | Bliki *date* (2004) + not-a-pinned-§7-row status | Confirm the date out-of-band or promote the bliki to a pinned §7 row to close the canon gap | open (factual metadata, independent of wording; correctly flagged; the sole residual) |
| 5 | READABILITY | snippet lead-ins (L96–104) + thesis restated in Overview/deep-dive/hand-off | A couple of lead-ins share cadence; thesis restated three times | Optional final read-aloud to vary two lead-in openers and trim one thesis restatement; not a blocker | open (polish; the now-weakest cluster) |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | Initial independent score (Sonnet 4.6) |
| 1 | 2026-06-20 | 37 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | Voice pass: self-narration cleared; deep-dive opening rewritten; fig91_1.png added. Em-dash NOT reduced; "easy"/"just" hits (Sonnet 4.6) |
| 2 | 2026-06-28 | 42 / 50 | PASS | PASS | PASS / GREEN / PASS | SHIP (42/50, gap disclosed) | Em-dash audit confirmed real; figure introduced in prose; EXAMPLE-BUILD green + CODE-REVIEW PASS + six snippets embedded; Boy Scout cross-ref → Ch 4 fixed (Opus 4.8) |
| 3 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS / GREEN / PASS | SHIP (43/50, one-point gap disclosed) | OpenRewrite recipe IDs web-verified verbatim; off-pin GAV `3.16.0 → 3.34.0` corrected & aligned to engine 8.81.0; `09-flags/94` atoms 1–2 RESOLVED. SOURCE-TRACE judged on identity → ACCURACY 8 → 9. UTILITY held at 8 (Opus 4.8) |
| 4 | 2026-06-28 | 44 / 50 | PASS | PASS | PASS / GREEN / PASS | AUTO-APPROVE (44/50, clears the bar) | Fowler 2e / Feathers WELC named-canon verbatims rewritten as attributed paraphrase — no book text reproduced (`09-flags/91` atom 1 → N/A). Quotation-mark audit confirms zero secondary-source verbatim → ACCURACY 9 → 10. UTILITY held at 8 (migration REPRO-pending) (Opus 4.8) |
| 5 | 2026-06-28 | **45 / 50** | PASS | PASS | **PASS / GREEN / PASS** | **AUTO-APPROVE (45/50, clears the bar +1)** | **OpenRewrite migration scale RAN green at the pin** — `rewrite:dryRun` resolved rewrite-core 8.81.0 / rewrite-migrate-java 3.34.0 / rewrite-maven-plugin 6.38.0, ran the composite recipe (chaining `UpgradeToJava21`), emitted a real patch, BUILD SUCCESS, default build still green, nothing mutated (`96_…_EXAMPLE.md`: "This lifts the UTILITY cap on Ch 39/Ch 40"). Resolves the exact pass-4 non-fakeable cap → **UTILITY 8 → 9.** Recipe IDs now execution-corroborated (ACCURACY held at 10). Other three clusters unchanged. Book-wide FLOOR-C migration COMPILE now green (Opus 4.8) |

---

## Learnings & pipeline suggestions

1. **A REPRO-pending run that later executes green is a legitimate UTILITY lift — and only UTILITY.** Pass 4 nominated the network-gated migration as the precise 8→9 cap and tagged it "not an in-bounds prose move and not fakeable." When that run actually executes at the pin (resolve + validate + run, real patch, default build green), the named gap is closed by *evidence*, not prose, and lifting UTILITY 8→9 is honest. It does not retroactively change ACCURACY beyond strengthening it (the recipe-ID identity was already at the 10 anchor via web-verification; execution corroborates it more strongly but cannot exceed 10). Codify: a successful `rewrite:dryRun` (resolve+validate+execute, nothing mutated) is the canonical proof that lifts a migration chapter's UTILITY off the REPRO-pending cap.

2. **Cross-module evidence is valid when the chapter explicitly hands the engine forward.** Ch 39 scopes the migration *run* out of its own `91_` module by design (migration ≠ modernization) and hands the OpenRewrite engine to Ch 40 (key 96), whose module now runs it. The UTILITY lift is therefore "runnable from the companion repo / handed-forward," not "runnable from this chapter's own module" — which is why the honest ceiling is 9, not 10. A scorer should credit cross-module runnability that the chapter's own hand-off points to, while keeping the one-step residual visible in the score.

3. **Execution corroborates identity more strongly than a web-lookup, but does not double-count.** The recipe IDs were already verbatim-verified against docs.openrewrite.org at the pin; the run confirms the *same* atoms resolve and execute. That strengthens the SOURCE-TRACE/ACCURACY confidence but stays at the 10 anchor — corroboration is not a second point. Keep "identity verified" and "identity execution-corroborated" as confidence states under the same score, not as separate score increments.

4. **A near-empty migration diff is the LST working, not a no-op.** The executed `UpgradeToJava21` over an already-modernized module yielded only a stale plugin bump. That corroborates — does not contradict — the chapter's "recipes don't cover everything / budget residual manual" honest-limitation. Read a small real diff as evidence the type-aware engine ran correctly, and as the limitation shown true, not as a failed demonstration.

5. **Take one defensible lift per pass and leave the soft clusters honest.** This pass lifted only UTILITY (the residual pass 4 nominated) and left ACCURACY/DEPTH/CLARITY/READABILITY untouched at their pass-4 values. The aggregate reached 45 by landing a real, named, non-fakeable coverage gap, not by nudging a soft cluster to clear a threshold — the discipline the bounded lift loop exists to enforce.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
