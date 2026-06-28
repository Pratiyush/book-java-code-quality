# INDEPENDENT SCORECARD — Ch 27 "The build & dependency hygiene" (key 62 + 63 + 64)

> **Independent (different-model) re-score** of the printed chapter, run as a harsh skeptic against
> `SCORING.md` (five clusters + floors A/B/C; ship bar ≥44/50, no cluster <6). This is the gate-of-record
> score for auto-approval — it does **not** inherit the main-loop self-score (`_SCORE.md` = 42/50). Floors
> A/B/C-source are checked first and gate the aggregate. Lift loop is bounded (≤3 passes), in-bounds only.
>
> **This pass (2026-06-28, re-score #3 — POST-LIFT-PASS-1):** re-score **after the one bounded in-bounds
> READABILITY pass** the prior re-score (#2, 43/50) named as the single deciding path to 44. The pass made
> three prose-only moves, all in-bounds (no new facts, no padding, zero floor risk): **(a)** broke the dense
> deep-dive run-on at the synthesis paragraph ("Remove any one and the other two fail…") into four short
> sentences and split the long "two foundations" sentence; **(b)** trimmed the content overlap between the
> "Currency flows through the same gates" CONCEPT callout and the deep-dive so the synthesis reads once, not
> as an echo of the callout; **(c)** added a reader-facing **dated-at-use beat** in the currency prose naming
> Renovate/Dependabot as continuously-released hosted services whose schema is confirmed against current docs
> (restates the flagged SOURCE-PIN §4 `⚠ rolling` status in the prose — no new fact). The `⚠ @pin` residual
> set (plugin version literals; the third-party `dependencyUpdates` task; SaaS schema + NVD/OSV feeds) stays
> flagged, not asserted. No code was touched → FLOOR-C compile/code-review state is unchanged from the gate
> reports. Re-scored harshly and honestly below.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score #3 — post-lift-pass-1)
- **Dossier key:** 62 (owner; folds 63 + 64) — `01-index/FINAL_INDEX.md` Ch 27, OPENS Part VII
- **Slug:** `62_build_dependency_hygiene`
- **Title:** The build & dependency hygiene ("The Foundation Under Every Gate")
- **Part / arc position:** Part VII — Build, Dependencies & Supply Chain (opener)
- **Artifact scored:** `03-drafts/62_build_dependency_hygiene/62_build_dependency_hygiene_v1.md` (post lift-pass-1)
- **Gate reports read whole:** `_EXAMPLE.md` (2026-06-26, build green), `_CODEREVIEW.md` (2026-06-27, PASS),
  `_SCORE.md` (self, 42/50), prior `_SCORE_INDEP.md` (re-score #1 42/50; re-score #2 43/50, LIFT). No
  `_VERIFY.md`/`_CLARITY.md`/`_AUDIT.md` present (main-loop manual gates per the draft header); floors checked
  here directly against the lifted draft + SOURCE-PIN + NEUTRALITY + the flag file's resolution addenda.
- **Verified against SOURCE-PIN:** 2026-06-20 pin; doc-atom WEB-VERIFY pass 2026-06-28 (`09-flags/62_...`
  addendum) re-read whole and spot-checked against the lifted prose.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 of 3 applied this re-score (READABILITY-targeted; in-bounds; re-scored all five below)

---

## The three content-floors (checked FIRST — they gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Empirical banned-phrase sweep of the whole post-lift file = **0** hits (`better than / unlike X / the problem with X / superior / beats / outperforms / blows away / obvious choice over / no reason to use / destroys / kills`). The lift introduced no comparative phrasing: the new currency sentence is a tool-status caveat ("continuously-released hosted services … confirmed against each tool's current documentation"), crowning neither; the trimmed synthesis keeps "crowns neither"/mirror-image-costs framing. Maven/Gradle still "mirror-image costs," "the book crowns neither"; Renovate/Dependabot "choose by need, crown neither"; BOM vs version-catalog "two routes to the same goal." No section title carries a superlative; "Alternatives" is approach-based. Every comparative claim cited to the named tool's own pinned docs (back-matter). |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries an explicit when-NOT / cost — unchanged by the lift and, on the rolling-tools point, **strengthened**: the new dated-at-use beat makes the Renovate/Dependabot rolling-schema caveat reader-visible, not apparatus-only. Dedicated "Limitations & when NOT to reach for it" section intact: build-is-code over-engineering, tool-migration rarely justified; slow build erodes quality; strict convergence noisy → gets disabled (tune-don't-disable, Ch 19); hygiene needs buy-in + judges *agreement* not *quality*; pinning-without-currency rots; bots produce noise without a strategy; green build ≠ semantic break (auto-merging majors carries real risk). The pin-vs-rot deep-dive frames each of pin-and-forget / chase-latest as a trap. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **No code touched this lift → no rebuild required; Floor-C compile state read unchanged from the two gate reports. COMPILE:** `_EXAMPLE.md` (2026-06-26) + `_CODEREVIEW.md` (2026-06-27, re-run live) both record `BUILD SUCCESS` under the pinned toolchain (Maven 3.9.16, JDK 21.0.11) at default build AND `-Pquality`: 5 tests pass, 0 Checkstyle, 0 SpotBugs, all five Enforcer rules **execute and pass** on the resolved graph (Rule 0–4 log lines), the `dependencyConvergence` failure path proven transiently (seeded conflict → BUILD FAILURE → POM restored byte-identical). **CODE-REVIEW:** PASS, zero BLOCKER/zero MAJOR, six dimensions all PASS. **SOURCE-TRACE:** zero invented atoms — all 5 displayed snippets resolve to ≤9-line `tag::` regions in the building POM (`check_snippets` 5/5), and the lift did **not** alter, add, or move any `<!-- include: -->` directive (all 5 confirmed present + unchanged: `dep-management-bom`/`enforcer-convergence`/`enforcer-upper-bound`/`enforcer-banned`/`versions-plugin`). The doc atoms web-verified + cited in re-score #2 remain (Maven "nearest definition" + tie-breaker → `maven.apache.org`; Gradle `check`/`libs.versions.toml`/`dependencyInsight` → `docs.gradle.org` 9.6). The new prose sentence asserts **no** new fact — it restates the SOURCE-PIN §4 `⚠ rolling` status; **zero plugin-version literals (`3.5.0`/`2.18.0`) appear in prose** (grep clean). The narrow residual set stays **flagged**, not invented (`09-flags/62_enforcer_versions_plugin_versions_unpinned.md`). |

**All three floors PASS.** Nothing fatal. The aggregate is scored on its merits below.

---

## The five clusters (1–10) — harsh-skeptic independent re-score (post lift-pass-1)

| # | Cluster | Score | Justification (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Unchanged at 9, and marginally helped by the lift. The hook stages two concrete failures (unknown deep-tree CVE + per-machine version drift from unmanaged resolution) bound to one root cause: an unmanaged dependency tree. The spine is explicit and each step earns the next — build hosts the gates → hygiene makes the graph deterministic → currency keeps it fresh — and the deep-dive closes the loop. The synthesis paragraph, previously one long sentence asking the reader to hold all three facets at once, now lands the same logic in four short sentences ("Remove any one and the other two fail. Gates with no hygiene cannot tell what changed. Hygiene with no currency rots. Currency with no gates ships regressions."), which reads more cleanly without changing the content. Three CONCEPT callouts carry the load-bearing definitions; the deep-dive no longer re-teaches the "currency rides on the gates" mechanism, so the callout owns it once. Figure 27.1 load-bearing, named before it appears, crowns neither tool. (−1: a few Overview terms — BOM, version-catalog, convergence — appear in the outline before their teaching gloss; outline convention, not a real defect. Held at 9: clean throughout, not a perfect-10 reconstruction surface.) |
| 2 | **ACCURACY** | **9** | Unchanged at 9 — the lift added **no new fact**. The doc-only atoms resolved at `/pin-source` in re-score #2 remain cited verbatim against the pinned authorities: Maven dependency-mediation term **"nearest definition"** + the equal-depth "first declaration wins" tie-breaker (`maven.apache.org`, "Introduction to the Dependency Mechanism"); `dependency:tree`/`analyze` goals; Gradle `check` "aggregate task that performs verification tasks", version catalog `gradle/libs.versions.toml`/TOML/`libs` accessor, `dependencyInsight` "reason and origin for its version selection" (`docs.gradle.org` 9.6). The build-verified atoms hold (Maven 3.9.16, lifecycle phase names, the three Enforcer rule names confirmed as classes AND observed executing, BOM-import syntax, `display-dependency-updates`). The new currency sentence restates the **already-flagged** SOURCE-PIN §4 `⚠ rolling` status — it makes an existing, traced caveat reader-visible rather than asserting anything new, which is accuracy-neutral-to-positive (the precision caveat is now in the prose, not only the apparatus). (−1, harsh-skeptic, held off 10: the narrow `⚠ @pin` set remains — the two plugin version literals (one SOURCE-PIN row, not separately pinned), the third-party `dependencyUpdates` ben-manes task (outside the Gradle authority), and the SaaS Renovate/Dependabot schema + NVD/OSV feeds — all correctly flagged and dated-at-use, none asserted as settled. The chapter still rests on a small honestly-flagged set the pin cannot close; the rubric scores that as honest, not as a 10.) |
| 3 | **UTILITY** | **9** | Unchanged at 9. Directly actionable: cheap-first gate ordering, the tool-agnostic build-quality checklist, the single-source-of-version-truth recipe, the three Enforcer rules shown as runnable POM regions, ban-`LATEST`/`RELEASE`/ranges, minimal-surface via `dependency:analyze`, and a concrete bot strategy (auto-merge trusted patches with green CI / review majors+security / group / schedule / gate auto-merge on the tests that catch breaks). "When to use what" is a usable decision list; the companion module is a real, green-building artifact to copy. The new rolling-tools beat marginally raises utility: a reader now knows to confirm the bot schema against current docs rather than trusting a pinned snippet — an operationally true instruction for a SaaS tool. This stays a page kept open while working. |
| 4 | **DEPTH** | **8** | Unchanged at 8 — DEPTH is **not liftable in-bounds** and was correctly left alone (raising it needs new verified substance or padding, both forbidden). The pin-vs-rot tension + the "three facets are one system" synthesis is genuine senior build material; the "hygiene is the precondition for supply-chain security" framing earns the Part-VII-opener slot; honest about the boundary (convergence proves agreement, not safety). (−2: all three source dossiers are concise Tier-B, and the heavy adjacent material — reproducible builds, CI mechanics, SCA/SBOM — is correctly routed elsewhere, so depth is broad-and-coherent rather than deep-and-contested. The chapter's job as an opener is breadth-with-a-spine, which it delivers. Not the deciding cluster.) |
| 5 | **READABILITY** | **9** | **Lifted 8→9 by lift-pass-1 — the two halves of the prior −2 are both removed.** (i) The dense deep-dive run-on ("Remove any one and the other two fail: gates with no hygiene cannot tell what changed; hygiene with no currency rots; currency with no gates ships regressions") is now four short, varied sentences, and the long "two foundations" sentence was split — the one stretch the prior score named as a repetitive, comma/semicolon-stacked cadence now reads with deliberate short-sentence rhythm (the "Databases." cadence VOICE-GUIDE prescribes). The callout↔deep-dive overlap is trimmed: the synthesis leans on the established CONCEPT ("as the previous section established") instead of re-explaining the patch-bump mechanism, so the point reads once. (ii) The Renovate/Dependabot `⚠ rolling` caveat is now a reader-facing dated-at-use beat in the currency prose ("continuously-released hosted services … schema is confirmed against each tool's current documentation … not pinned to a release number") — the exact "voice a caveat by naming its condition" move, converting an apparatus-only honesty caveat into a prose one, so the reader no longer meets schema keys as settled. Voice holds: third-person invisible narrator, **zero** narration contractions in the body (the single `we're` is inside quotation marks — the sanctioned quoted-idiom exception; the lift added none), **zero** banned filler/difficulty/hype in the printed body. **Em-dash density on printed NARRATIVE prose = 6.21/1000 words — under the ~8/1000 target** (the lift lowered it from 6.30 by converting the synthesis run-on's stacked dashes to periods; the whole-file figure is inflated by citation back-matter, which is not narration). The chapter now reads effortlessly at full precision throughout. (Held at 9, not 10: a Part-opener carrying three folded topics is dense by design; clean and effortless, but not a frictionless-prose 10.) |

**Cluster subtotal: 44 / 50** — no cluster below 6 (lowest is 8).

---

## Ship-bar verdict

- **Floors A/B/C:** all **PASS**.
- **Aggregate:** **44 / 50** — **meets the ≥44/50 (88%) ship bar.**
- **No cluster <6** (lowest is 8; four clusters at 9, DEPTH at 8).

**Verdict: SHIP (auto-approve).** The one bounded in-bounds READABILITY pass the prior re-score (#2) named
delivered exactly the projected lift: the dense-run cadence and the apparatus-only rolling caveat — the two
clauses of the READABILITY −2 — are both removed, lifting READABILITY 8→9 and the aggregate 43→**44**, with
every cluster ≥8 and all floors PASS. The bar was **not** lowered and **no** score was inflated to reach it:
ACCURACY stayed at 9 (the residual flagged atoms are intrinsic, correctly flagged scope), DEPTH stayed at 8
(not liftable in-bounds), and the +1 came solely from the READABILITY pass operating on already-present,
already-verified material with zero floor risk. Both ship-bar conditions hold: floors A/B/C-source PASS and
the independent aggregate clears 88%.

---

## Lift-loop disposition

**Lift loop CLOSED at pass 1 of 3 — bar cleared.**

- **Pass-1 target (READABILITY, weakest liftable cluster at the tie 8/8 with DEPTH):** the highest-leverage
  in-bounds move, exactly as the prior re-score's work order specified. Applied (a) run-on split + (b)
  callout↔deep-dive overlap trim + (c) reader-facing dated-at-use beat — all prose-only, no new facts, no
  padding, zero floor risk.
- **Re-scored all five after the pass** (per the lift-loop rule that a lift in one cluster can move another):
  CLARITY held at 9 (marginally helped), ACCURACY held at 9 (no new fact), UTILITY held at 9 (marginally
  helped), DEPTH held at 8 (correctly untouched — not liftable in-bounds), READABILITY 8→9. Net +1 → 44/50.
- **No further pass needed** — the bar is met at pass 1. Passes 2 and 3 are not used.

---

## Line-level record (what the lift changed)

| # | Cluster / floor | Location (section · ¶) | Before | After (in-bounds) |
|---|---|---|---|---|
| 1 | READABILITY (deciding) | Deep dive, synthesis ¶ | One dense run ("Remove any one and the other two fail: …; …; …") + a long "two foundations" sentence | Run split into four short sentences; "two foundations" sentence split into two; varied short-sentence rhythm. No content change. |
| 2 | READABILITY (deciding) | Deep dive, synthesis ¶ | The deep-dive re-explained the "currency rides on the gates" mechanism already taught in the CONCEPT callout (an echo) | Trimmed to lean on the established CONCEPT ("as the previous section established"); the mechanism is stated once, in the callout. No content lost. |
| 3 | READABILITY (honesty) / B (strengthened) | "Dependency currency" ¶ | Renovate/Dependabot schema keys introduced in prose with no dated-at-use beat; `⚠ rolling` caveat lived only in back-matter apparatus | Added one sentence naming them as continuously-released hosted services whose config schema is confirmed against current docs (restates flagged SOURCE-PIN §4 status — no new fact). Apparatus-only caveat is now reader-facing. |
| — | ACCURACY (for the record) | "Convergence" bullet + back-matter | (resolved at `/pin-source` re-score #2) | Unchanged — "nearest definition" + tie-breaker cited to `maven.apache.org`; no longer a defect. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (re-score #1) | 2026-06-28 | 42 / 50 | PASS | PASS | PASS (build green + CODE-REVIEW PASS; one atom flagged; doc atoms `⚠ @pin`) | LIFT-LOOP | initial independent score (harsh skeptic); ACCURACY 8 capped by doc-only `⚠ @pin` atoms |
| 0 (re-score #2) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS (build green + CODE-REVIEW PASS; doc atoms now web-verified + cited; narrow residual flagged) | LIFT-LOOP | `/pin-source` resolved + cited the "nearest definition"/Gradle doc atoms (corrected the "nearest-wins" error) → ACCURACY 8→9 at source. One in-bounds READABILITY point remained. |
| **1 (re-score #3)** | **2026-06-28** | **44 / 50** | **PASS** | **PASS** | **PASS** (no code touched → compile/code-review state read unchanged from the gate reports; snippet includes + tag-regions verified intact; no plugin-version literals in prose) | **SHIP** | one bounded in-bounds READABILITY pass: (a) split the dense synthesis run-on, (b) trimmed the callout↔deep-dive overlap, (c) added a reader-facing dated-at-use beat for the `⚠ rolling` Renovate/Dependabot schema → READABILITY 8→9. Aggregate 43→44, clears the bar. |

---

## Learnings & pipeline suggestions

- **The bounded lift loop did exactly what re-score #2 projected — a +1 from one cluster, earned on
  already-verified material.** Re-score #2 named READABILITY as the single deciding in-bounds point and
  predicted a clean 44 if the dense-run cadence and the apparatus-only caveat were both removed. Lift-pass-1
  removed both and READABILITY moved 8→9 with no other cluster disturbed and no floor risk. Worth recording in
  `PIPELINE-LEARNINGS.md`: **when a prior re-score names a specific, located, in-bounds lift and projects the
  resulting score honestly, the lift loop is the right route over a flag-to-human — the projection held to the
  point.**
- **A reader-facing dated-at-use beat for a `⚠ rolling`/SaaS authority both lifts READABILITY and strengthens
  HONEST-LIMITATIONS at once.** Moving the Renovate/Dependabot rolling-schema caveat from the back-matter
  apparatus into the prose removed the READABILITY −2's honesty clause AND made FLOOR B's when-NOT/condition
  reader-visible — a single in-bounds sentence served two rubric dimensions. This confirms the prior
  suggestion: **any `⚠ rolling` authority should get a one-sentence "confirm schema against current docs" beat
  at its teaching first-use**; promote to the drafter checklist so the caveat lands in v1, not at lift time.
- **Trimming a callout↔deep-dive echo is a legitimate in-bounds READABILITY lever, distinct from cutting
  content.** The deep-dive re-explained a mechanism the CONCEPT callout already owned; leaning the synthesis on
  the established callout removed the echo without losing the point. This is "state the point once" rather than
  padding-removal. Suggest the clarity/score checklist flag **callout-then-deep-dive duplication** as a named
  readability smell for chapters that carry both a CONCEPT callout and a synthesis deep-dive.
- **A prose-only lift correctly requires no rebuild — read FLOOR-C compile/code-review state from the gate
  reports.** No code was touched, so the build is not re-run; the floor-C verdict is read from `_EXAMPLE.md`
  (green) and `_CODEREVIEW.md` (PASS). The scorer's job on a prose lift is to **verify the prose edits did not
  disturb the snippet includes, the tag-regions, or leak a flagged literal into the prose** (all three checked
  clean here), not to re-run `verify`. Worth a checklist note: **after a prose lift, grep the include
  directives + the flagged literals before re-scoring FLOOR C as unchanged.**
- **Em-dash density can drop as a side effect of splitting a run-on.** Converting the synthesis run-on's
  stacked dashes to periods lowered printed-narrative density from 6.30 to 6.21/1000 — the readability lift and
  the cadence metric moved together. Reaffirms re-score #2's note: measure em-dash density on **printed
  narrative**, not the whole file; the citation back-matter is dense by nature and inflates the whole-file
  figure.
