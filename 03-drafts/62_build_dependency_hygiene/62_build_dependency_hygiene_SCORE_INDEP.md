# INDEPENDENT SCORECARD — Ch 27 "The build & dependency hygiene" (key 62 + 63 + 64)

> **Independent (different-model) re-score** of the printed chapter, run as a harsh skeptic against
> `SCORING.md` (five clusters + floors A/B/C; ship bar ≥44/50, no cluster <6). This is the gate-of-record
> score for auto-approval — it does **not** inherit the main-loop self-score (`_SCORE.md` = 42/50). Floors
> A/B/C-source are checked first and gate the aggregate. Lift loop is bounded (≤3 passes), in-bounds only.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 62 (owner; folds 63 + 64) — `01-index/FINAL_INDEX.md` Ch 27, OPENS Part VII
- **Slug:** `62_build_dependency_hygiene`
- **Title:** The build & dependency hygiene ("The Foundation Under Every Gate")
- **Part / arc position:** Part VII — Build, Dependencies & Supply Chain (opener)
- **Artifact scored:** `03-drafts/62_build_dependency_hygiene/62_build_dependency_hygiene_v1.md`
- **Gate reports read whole:** `_EXAMPLE.md` (2026-06-26, build green), `_CODEREVIEW.md` (2026-06-27, PASS),
  `_SCORE.md` (self, 42/50). No `_VERIFY.md`/`_CLARITY.md`/`_AUDIT.md` present (main-loop manual gates per the
  draft header); floors checked here directly against the draft + SOURCE-PIN + NEUTRALITY.
- **Verified against SOURCE-PIN:** 2026-06-20 pin (re-check date 2026-06-28 — pin unchanged).
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (initial independent score)

---

## The three content-floors (checked FIRST — they gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | Empirical banned-phrase sweep of the whole file = **0** hits (`better than / unlike X / the problem with X / superior / beats / outperforms / kills / destroys / blows away / obvious choice over / no reason to use`). Maven/Gradle framed as "mirror-image costs," "the book crowns neither," "the practices port across both"; Renovate/Dependabot "choose by need, crown neither"; BOM vs version-catalog "two routes to the same goal"; the two failure modes (non-reproducible vs rotted) presented as a tension, neither crowned. No section title carries a superlative; the "Alternatives" section is approach-based, not a leaderboard. Every comparative claim cited to the named tool's own docs (back-matter). |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Every feature carries an explicit when-NOT / cost. Dedicated "Limitations & when NOT to reach for it" section: build-is-code over-engineering (2,000-line POM / Gradle sprawl), tool-migration rarely justified; slow build erodes quality; strict convergence noisy → gets disabled (with tune-don't-disable, Ch 19); hygiene needs buy-in + judges *agreement* not *quality*; pinning-without-currency rots; bots produce noise without a strategy; green build ≠ semantic break (auto-merging majors carries real risk). The pin-vs-rot deep-dive frames each of pin-and-forget / chase-latest as a trap. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ **PASS** | **COMPILE:** `_EXAMPLE.md` + `_CODEREVIEW.md` both record `BUILD SUCCESS` under the pinned toolchain (Maven 3.9.16, JDK 21.0.11) at default build AND `-Pquality`: 5 tests pass, 0 Checkstyle, 0 SpotBugs, all five Enforcer rules **execute and pass** on the resolved graph (Rule 0–4 log lines), and the `dependencyConvergence` failure path is proven transiently (seeded `commons-text`/`commons-lang3` conflict → BUILD FAILURE → POM restored byte-identical). **CODE-REVIEW:** PASS, zero BLOCKER/zero MAJOR, six dimensions all PASS, re-run live this gate. **SOURCE-TRACE:** zero invented atoms — all 5 displayed snippets resolve to ≤9-line `tag::` regions in the building POM (`check_snippets` 5/5 per `_EXAMPLE.md`); the one unpinned atom (enforcer `3.5.0` / versions `2.18.0` plugin version literals) is **flagged**, not invented (`09-flags/62_enforcer_versions_plugin_versions_unpinned.md` — confirmed present); the Gradle/SaaS/"nearest-wins" doc-only atoms are honestly marked `⚠ @pin` in-draft. **No code touched this gate → no rebuild required;** Floor-C state read from the two gate reports. |

**All three floors PASS.** Nothing fatal. The aggregate is scored on its merits below.

---

## The five clusters (1–10) — harsh-skeptic independent score

| # | Cluster | Score | Justification (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The hook stages two concrete failures (unknown deep-tree CVE + per-machine version drift from unmanaged resolution) and binds both to one root cause: an unmanaged dependency tree. The spine is explicit and each step earns the next — build hosts the gates → hygiene makes the graph deterministic → currency keeps it fresh — and the deep-dive closes the loop ("remove any one and the other two fail"). Three CONCEPT callouts carry the load-bearing definitions (tool-agnostic build quality; hygiene-makes-honest-not-good; currency-flows-through-the-gates). Figure 27.1 is load-bearing, named in prose before it appears, and crowns neither tool. A reader new to the topic could reconstruct the mechanism from the chapter alone. (−1: a few Overview terms — BOM, version-catalog, convergence — appear in the outline before their teaching gloss; outline convention, not a real defect.) |
| 2 | **ACCURACY** | **8** | Every load-bearing atom that the Maven build *can* exercise is verified green: Maven 3.9.16, lifecycle phase names (`validate`→`compile`→`test`→`verify`), the three Enforcer rule names confirmed as class names in the resolved `enforcer-rules-3.5.0.jar` AND observed executing, BOM-import syntax (`<type>pom</type>`+`<scope>import</scope>`), the `display-dependency-updates` goal. Nothing invented → FLOOR C clean. (−2, harsh-skeptic dock for unverified breadth: a real verify-at-pin surface remains — Gradle `check`/`libs.versions.toml`/`dependencyInsight` semantics, the Maven "nearest-wins" wording, the Renovate/Dependabot schema keys + NVD/OSV/GitHub-Advisory alert sources, and the two plugin version literals — all flagged `⚠ @pin`, all dossier-traced, none asserted as confirmed, but the chapter rests on a meaningful doc-only/SaaS set the build did not touch, and the SaaS-rolling nature is flagged only in the apparatus, not in the reader-facing prose.) |
| 3 | **UTILITY** | **9** | Directly actionable for a working engineer: cheap-first gate ordering, the tool-agnostic build-quality checklist, the single-source-of-version-truth recipe, the three Enforcer rules shown as runnable POM regions, ban-`LATEST`/`RELEASE`/ranges, minimal-surface via `dependency:analyze`, and a concrete bot strategy (auto-merge trusted patches with green CI / review majors+security / group / schedule / gate auto-merge on the tests that catch breaks). "When to use what" is a usable decision list; the companion module is a real artifact to copy. This is a page that stays open while working. |
| 4 | **DEPTH** | **8** | The pin-vs-rot tension + the "three facets are one system" synthesis (gates need hygiene; hygiene needs currency; currency needs gates) is genuine senior build material, and the "hygiene is the precondition for supply-chain security" framing earns the chapter's Part-VII-opener slot. Honest about the boundary (convergence proves agreement, not safety). (−2: all three source dossiers are concise Tier-B, and the heavy adjacent material — reproducible builds, CI mechanics, SCA/SBOM — is correctly routed elsewhere, so the depth is broad-and-coherent rather than deep-and-contested. No padding; not liftable without new verified substance.) |
| 5 | **READABILITY** | **8** | Voice holds throughout: third-person invisible narrator, **zero** narration contractions in the body (the single `we're` is inside quotation marks — the sanctioned quoted-idiom exception), **zero** banned filler/difficulty/hype words in the printed body (the apparent `grep` hits sit only in the editorial HTML header, not the printed prose). **Em-dash density on the printed NARRATIVE prose = 5.9 / 1000 words — under the 8/1000 target** (the whole-body 9.38 is inflated by the citation back-matter at ~20/1000, which is apparatus, not narration). Terms glossed plain-first at their teaching points; strong dual-failure hook; forward-pulling hand-off. (−2: a few dense comma/em-dash-stacked sentences in the deep-dive — notably "Remove any one and the other two fail: gates with no hygiene cannot tell what changed; hygiene with no currency rots; currency with no gates ships regressions" — and a mild content overlap between the CONCEPT callouts and the deep-dive give one stretch a repetitive cadence; clean, not effortless throughout.) |

**Cluster subtotal: 42 / 50** — no cluster below 6.

---

## Ship-bar verdict

- **Floors A/B/C:** all **PASS**.
- **Aggregate:** **42 / 50** — **2 short of the ≥44/50 ship bar.**
- **No cluster <6** (lowest is 8).

**Verdict: LIFT-LOOP** (cluster quality, not a floor). The chapter is close, floor-clean, and build-green; it
misses the bar by 2 on cluster quality. Per `SCORING.md`, run one in-bounds pass on the weakest cluster and
re-score. **The bar is not lowered to pass it.**

---

## Flagged weakest cluster + the bounded lift loop

**Weakest cluster (tie at 8 — ACCURACY / DEPTH / READABILITY); highest-leverage in-bounds target = ACCURACY.**

- **DEPTH (8)** is **not liftable in-bounds** — raising it needs new verified substance or padding (both
  forbidden); the concise-Tier-B scope and the correct routing-out of adjacent material are structural.
- **READABILITY (8)** is liftable only marginally by an in-bounds prose trim (one dense deep-dive run +
  de-duplicating the callout/deep-dive overlap); the scorer judges, the **drafter** owns the prose edit.
- **ACCURACY (8)** carries the single highest-leverage in-bounds move the task named, using **only
  already-verified / already-flagged** material (no new facts, zero floor risk):

  > **Pass-1 work order (hand to drafter — in-bounds, no new facts):** In the "Dependency currency" prose
  > (¶ at draft line 83, where `dependabot.yml`/`renovate.json` and their schema keys are introduced), add a
  > one-sentence **dated-at-use** beat stating plainly that Renovate and Dependabot are continuously-released
  > hosted tools whose config schema should be confirmed against current docs (SOURCE-PIN §4 marks both
  > `⚠ rolling`; the caveat currently lives only in the back-matter apparatus, not in the reader-facing
  > prose). This converts a flagged-in-apparatus-only honesty caveat into a reader-visible one — the exact
  > "voice a caveat by naming its condition" move VOICE-GUIDE prescribes for a rolling source. **Do not**
  > assert the "nearest-wins" wording or any Gradle/SaaS schema atom as pin-confirmed — they stay `⚠ @pin`.

  **Realistic re-score after Pass-1 (honest, not inflated):** the dated-at-use beat *improves honesty* but the
  unverified verify-at-pin **breadth** (Gradle semantics, nearest-wins, SaaS schema, plugin literals) is
  intrinsic to the dossier scope and cannot be closed by prose, so **ACCURACY stays 8**. CLARITY/UTILITY are
  already 9; DEPTH cannot rise without new substance; READABILITY 8→9 requires "effortless at full precision
  throughout," which a single trim does not fully deliver. **Projected post-Pass-1 aggregate ≈ 42–43/50 — still
  short of 44 by in-bounds means.**

**Honest disposition (harsh skeptic, bar held):** the in-bounds lift moves available do **not** credibly carry
two full cluster points without padding DEPTH or asserting unverified atoms — both out of bounds. The verdict
therefore stands at **LIFT-LOOP**: return for the Pass-1 in-bounds drafter pass above (and the optional
READABILITY trim), then re-score. If, after the bounded passes, the honest aggregate is still <44 without
lowering the bar or padding, the rubric routes this to the human gate (`09-flags/`) as a scope question
(concise-Tier-B dossier depth vs the 88% bar), **not** to a manufactured score. The bar is never lowered.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix (in-bounds) |
|---|---|---|---|---|
| 1 | ACCURACY (honesty) | "Dependency currency" ¶, draft line 83 | Renovate/Dependabot schema keys introduced in prose with no dated-at-use beat; the `⚠ rolling` caveat lives only in the back-matter apparatus | Add one sentence: name them as continuously-released hosted tools whose config schema is confirmed against current docs (no new fact — restates the flagged SOURCE-PIN §4 status in the prose). |
| 2 | READABILITY | Deep dive, draft line 101 | One dense run ("Remove any one and the other two fail: gates with no hygiene…; hygiene with no currency rots; currency with no gates ships regressions") + mild overlap with the three CONCEPT callouts gives a repetitive cadence | Break the run into shorter sentences / vary rhythm; trim the callout↔deep-dive duplication. No content change. |
| 3 | ACCURACY (precision) | "Convergence" bullet, draft line 66 + back-matter | "nearest-wins" is a doc-only atom not exercised by the Maven build | Keep flagged `⚠ @pin` (already done) OR verify against the pinned Maven docs at `/pin-source`; **do not** assert as confirmed until verified. No invention. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 42 / 50 | PASS | PASS | PASS (build green + CODE-REVIEW PASS; source-trace clean, one atom flagged) | **LIFT-LOOP** | initial independent score (harsh skeptic) |

---

## Learnings & pipeline suggestions

- **Em-dash density must be measured on printed NARRATIVE prose, not the whole file.** Scoring the whole
  `.md` body gave 9.38/1000 (over target) and would have produced a false READABILITY flag; the printed
  narrative prose (hook→"When to use what", excluding the citation back-matter and figure captions) is
  5.9/1000 — under target. The back-matter source-traceability apparatus legitimately runs em-dash-heavy
  (~20/1000) because it is dense citation, not narration. **Suggest the AUDIT/score em-dash scan exclude the
  `## Back matter`/citation region and figure-caption lines, or report prose-only and apparatus densities
  separately**, so a citation-heavy back-matter never trips a prose cadence flag.
- **A floor-clean, build-green chapter can sit honestly at 42/50.** The gap to the 88% bar here is *unverified
  breadth* (Gradle/SaaS/doc-only atoms correctly flagged) + *concise-Tier-B depth* — neither closable by an
  in-bounds prose pass. This is exactly the case the bounded lift loop is meant to surface to the human gate
  rather than paper over: when the only paths to +2 are padding DEPTH or asserting `⚠ @pin` atoms, the right
  move is LIFT→(bounded)→flag, not a manufactured 44. Worth recording in `PIPELINE-LEARNINGS.md` as the
  canonical "honest 42, bar held" pattern.
- **Rolling/SaaS tools need a dated-at-use beat in the PROSE, not only in the back-matter flag.** Renovate/
  Dependabot are `⚠ rolling` in SOURCE-PIN §4; the chapter flags this in the apparatus but the reader of the
  prose sees schema keys as settled. Suggest a drafter checklist item: **any `⚠ rolling` authority gets a
  one-sentence "confirm schema against current docs" beat at its teaching first-use**, per VOICE-GUIDE's
  "voice a caveat by naming its condition."
- **Independent score (42) diverges from the self-score (42) only in routing.** The self-score recorded 42 and
  treated FLOOR-C COMPILE as PENDING; the build has since gone green (`_EXAMPLE.md`/`_CODEREVIEW.md`), so the
  floors now all PASS — yet the aggregate is unchanged and still under the 44 bar. The divergence is not in the
  number but in the disposition: the independent gate holds the bar and routes to LIFT/flag rather than
  reading "floors now pass" as "ship."
