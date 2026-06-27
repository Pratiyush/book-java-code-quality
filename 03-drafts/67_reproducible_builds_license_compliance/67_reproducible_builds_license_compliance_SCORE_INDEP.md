# SCORECARD (INDEPENDENT) — Ch 29 "Reproducible builds & license compliance" (key 67 + folds 68)

> Independent (different-model) re-score at the 88% auto-approval bar (≥44/50, no cluster below 6, floors
> A/B/C-source PASS), per `00-strategy/SCORING.md`. Harsh-skeptic pass. A bounded in-bounds lift loop was
> applied during this scoring event (2 passes, no floor risk, no new unverified facts, no padding); the
> final scores below are post-lift. The prior main-loop SELF-score (`_SCORE.md`) was 42/50 and does not
> approve a chapter — only this independent re-score does.

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 67 (frozen; folds 68) — `01-index/FINAL_INDEX.md` Ch 29 (CLOSES Part VII)
- **Slug:** `67_reproducible_builds_license_compliance`
- **Title:** Reproducible builds & license compliance ("A Build You Can Stand Behind")
- **Part / arc position:** Part VII — Build, Dependencies & Supply Chain (Ch 27–29; this is the closer)
- **Artifact scored:** `03-drafts/67_reproducible_builds_license_compliance/67_reproducible_builds_license_compliance_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build green + reproducibility proven live), `_CODEREVIEW.md` (PASS-WITH-FIXES, no BLOCKER), prior `_SCORE.md` (self, 42/50)
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 2 (two in-bounds passes applied this event)

---

## The five clusters (post-lift)

| # | Cluster | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The two-facets framing (technical integrity = reproducible bytes; legal integrity = licensed tree) unifies two unrelated-looking topics cleanly; the provenance-over-a-moving-target hook ties directly to Ch 28 and frames *why* reproducibility matters; the non-determinism→fix table (§How it works) and the obligation spectrum land in one read; two figures (29.1 chain, 29.2 spectrum) each have an intro sentence + caption; two CONCEPT callouts (verify-by-rebuild, obligation-depends-on-distribution) anchor the mechanisms. A reader can reconstruct both mechanisms from the chapter alone. |
| 2 | **ACCURACY** | **9** | Post-lift: the one known-wrong atom — bare `LGPL-2.1` (not a current SPDX id) at draft §Identify (¶ "Normalize…") and the back-matter ledger — was corrected to the SPDX-canonical `LGPL-2.1-only`, now matching the companion code (`LicensePolicy`/`LicenseCategory`/`BuildIntegrityTest` already used `-only`). This closes CODE-REVIEW finding #1 (prose↔code SPDX-form mismatch). Every other atom (`project.build.outputTimestamp`, the obligation categories, SPDX = ISO/IEC 5962:2021, `commons-lang3` 3.18.0) traces to SOURCE-PIN or is honestly flagged; the verify-at-pin surface (the two plugin GAVs `reproducible-build-maven-plugin` 0.17 / `license-maven-plugin` 2.7.1, Gradle archive flags, the SLSA-level mapping, obligation summaries) is disclosed in-text and carried in `09-flags/67_repro_license_plugin_versions_unpinned.md`. The central reproducibility claim is *empirically* verified (two builds → byte-identical jar, same SHA-256). |
| 3 | **UTILITY** | **9** | Directly actionable: `<project.build.outputTimestamp>` + pin plugins + fixed `LANG`/`TZ`/encoding; the build-twice-and-diff / `artifact:compare` verify; hermeticity for higher SLSA; read SPDX off the SBOM; the `license-maven-plugin` allow-list policy gate scanning the full transitive graph; auto-generated `THIRD-PARTY`/`NOTICE`. Backed by a green companion module whose reproducibility is *demonstrated* (not just configured). The reproducibility-makes-provenance-meaningful link is a concrete "why now." This is a page a build/release engineer keeps open. |
| 4 | **DEPTH** | **8** | Real synthesis: two-facets-of-one-integrity; reproducibility as the *culmination* of pinning (pinned inputs → deterministic resolution → bit-identical output → attestable); license-almost-free-on-the-SBOM (same artifact answers the CVE question and the GPL-obligation question); the necessary-not-sufficient/neither-proves-correctness center shared with every gate. Solid senior build-integrity material. Held at 8 (not 9) honestly: both source dossiers are concise Tier-B, and no depth was invented to lift it (in-bounds rule). |
| 5 | **READABILITY** | **9** | Post-lift: reader-facing em-dash density pulled from 8.28/1000 to **7.59/1000** (under the house <8/1000 lever) by converting flowing-prose dashes to colons at §License compliance (the `license-maven-plugin` lead-in and the allow-list lead-in) — voice held, meaning unchanged, list-item definitional dashes left intact. Strong moving-target hook, the source→fix table and obligation spectrum break up the prose, two callouts, a clean Part VII→VIII inward-to-your-own-code hand-off. The locked invisible-narrator voice is consistent throughout. |

**Cluster subtotal: 44 / 50** (no cluster below 6).

---

## The three content-floors (PASS / FAIL)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep over the draft: 0 hits (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` / etc.). Reproducible-build tooling and license tooling are presented neutrally — no tool crowned; `license-maven-plugin` / FOSSA / ScanCode named side-by-side as "OSS plugin versus hosted scanner; choose by depth of detection and reporting needs" (approach-based, not a leaderboard). The obligation spectrum is a *factual categorization* (permissive / weak / strong copyleft) framed by distribution context, not a ranking of "good/bad licenses." No comparative superlative in any heading. Cross-tool/standard claims cited to their own sources (SPDX, reproducible-builds.org, SLSA v1.0, Ch 28). CODE-REVIEW dimension 6 independently confirms neutrality-in-code (no banned phrasing in `src`/`config`/`pom.xml`/`README`). |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries its hardest objection + a when-NOT-to-use. Reproducibility: last-mile-effort/diminishing-returns (internal-only apps), toolchain variance (different JDK builds/vendors → may pin the JDK), proves-integrity-not-correctness, decays-silently (one unpinned plugin → needs a CI verify step). License: **NOT legal advice** (stated prominently — in the Hook, a dedicated CONCEPT callout, §Limitations, and the back matter), detection-imperfect (missing/ambiguous/multi/relicensed/dual-licensed; POM-declared ≠ actual), obligation-depends-on-distribution (AGPL reaches SaaS; blanket deny-list blocks harmless deps — tune), transitive-surprise (scan the full graph), license≠security. The §Limitations section, §When to use what, and the deep-dive necessary-not-sufficient center all reinforce it. License content is factual, not legal advice — confirmed throughout. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms; the one off-canonical SPDX id (`LGPL-2.1`) was the only accuracy defect and is now fixed to `LGPL-2.1-only`; the two unpinned plugin GAVs and the obligation summaries are flagged to `09-flags/67_repro_license_plugin_versions_unpinned.md`, not asserted as pinned. **Compile:** `_EXAMPLE.md` and `_CODEREVIEW.md` both independently ran `mvn -B -Pquality verify` → **BUILD SUCCESS**, warning-clean (0 Checkstyle, 0 SpotBugs, 7/7 tests), on the pinned toolchain (JDK 21.0.11, Maven 3.9.16); reproducibility **proven live** (two `clean package` builds → byte-identical jar, SHA-256 `b5b3d7be…`, `cmp` identical). **Code-review:** `_CODEREVIEW.md` verdict = PASS-WITH-FIXES, **no BLOCKER** across all six dimensions; the only prose-owned fix (finding #1, the SPDX form) is the one applied in this lift. No code was touched in this scoring event (the SPDX edits are prose-only; the module already used `-only`), so the green build is unaffected and the 6 include markers / tag regions are intact. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar (44/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate (Step 12).

**One-line rationale:** After a 2-pass in-bounds lift (canonical SPDX id + em-dash de-clustering), the chapter reaches 44/50 with all floors PASS and the companion module green + reproducibility proven — it ships.

---

## Flagged weakest cluster (post-lift)

- **Weakest cluster:** DEPTH — score 8 (tied-lowest; the four others sit at 9).
- **Why it is the weakest:** Both source dossiers are concise Tier-B, so the chapter is rich on synthesis but does not reach the contested/foundational density of a 9. This is an honest ceiling, not a fixable gap — lifting it further would require *new* verified material the dossiers do not contain, which the in-bounds rule forbids (no padding). It is correctly left at 8; the aggregate clears the bar without it.
- **Single highest-leverage move (if ever re-opened, out of bounds for this loop):** add a sourced worked transitive-surprise example (a named permissive direct dep pulling a named copyleft transitive) once such a pairing is pinned — would deepen the license facet to a 9. Not done here (would be a new unverified fact).

---

## Line-level fixes applied this event (the lift list)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | ACCURACY / FLOOR-C source-trace | §License compliance · "Identify" bullet; §Back matter ledger; front-matter dossier comment | Bare `LGPL-2.1` is not a current SPDX identifier (CODE-REVIEW finding #1); code already used `-only` → prose↔code mismatch | Changed all three `LGPL-2.1` → `LGPL-2.1-only` (SPDX-canonical, matches companion code + SPDX = ISO/IEC 5962:2021). Whole-file grep confirms no bare `LGPL-2.1` remains. |
| 2 | READABILITY | §License compliance · `license-maven-plugin` lead-in | Flowing-prose em-dash; reader-prose density 8.28/1000 (> house <8 lever) | Converted the dash to a colon ("…in one execution: fail on a license…"). Meaning unchanged. |
| 3 | READABILITY | §License compliance · allow-list lead-in | Flowing-prose em-dash (same density pressure) | Converted the dash to a colon ("…externalized, reviewable config: a list of permitted…"). Reader-prose density now 7.59/1000 (PASS). |

> Minor polish noted but **not** applied (out of scope for a prose lift; owned by the example-builder, none block ship): CODE-REVIEW findings #2 (`isVerifiable()` always-true probe — documented teaching choice), #3 (explicit `assertThatCode(...).doesNotThrowAnyException()` in the happy-path test), #4 ("pinned" wording for the module-local `commons-lang3` literal vs the flag's own framing). All are code/README/pom nits with no effect on the green build or any displayed snippet.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 42 / 50 | PASS | PASS | PASS / PENDING-then-GREEN / — | (self, no-approve) | initial main-loop self-score (COMPILE recorded PENDING; later proven green at EXAMPLE-BUILD 2026-06-26) |
| 0 (indep, pre-lift) | 2026-06-28 | 41 / 50 | PASS | PASS | PASS / GREEN / PASS-no-BLOCKER | LIFT-LOOP | independent re-score; ACCURACY marked down to 7 for the bare-`LGPL-2.1` SPDX defect sitting in reader prose while code uses `-only`; READABILITY 8 (em-dash 8.28/1k > lever) |
| 1 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT-LOOP | PASS 1 (ACCURACY): applied CODE-REVIEW finding #1 — `LGPL-2.1` → `LGPL-2.1-only` ×3 (prose-only; no code touched). ACCURACY 7→9. Now-weakest: READABILITY 8. |
| 2 | 2026-06-28 | **44 / 50** | PASS | PASS | PASS / GREEN / PASS | **SHIP** | PASS 2 (READABILITY): converted 2 flowing-prose em-dashes to colons; reader-prose em-dash 8.28→7.59/1k (under <8 lever). READABILITY 8→9. Bar cleared. |

---

## Learnings & pipeline suggestions

1. **A CODE-REVIEW "prose-owned MINOR" left unapplied is a live ACCURACY drag at scoring time.** Finding #1 (the SPDX `-only` form) was correctly routed to the drafter but had not yet been applied to the draft when this independent score ran, so the chapter still carried a factually-wrong SPDX id in reader prose while its own companion code used the canonical form. The scorer is the right place to close such a prose-only fidelity fix inside the bounded lift loop — but the pipeline would lose less time if a prose-side CODE-REVIEW MINOR were folded back into the draft *before* the scoring gate, not after. Suggest: the reconcile/source-verify step picks up prose-owned CODE-REVIEW findings as a checklist item before SCORE runs.
2. **An SPDX-identifier-form lint would catch this class cheaply at VERIFY** (echoing the CODE-REVIEW learning): flag bare `LGPL-2.1` / `GPL-2.0` / `GPL-3.0` (missing `-only`/`-or-later`) in any draft. It is a recurring fidelity trap because the short form reads natural but is not a valid SPDX id.
3. **Measure em-dash density on reader-facing prose, not the whole file.** The whole-file figure (10.72/1000) is dominated by the back-matter traceability ledger (24/1000), which is a dense internal record, not flowing prose. Scoring READABILITY on the Hook→Hand-off body (8.28→7.59/1000 here) is the honest measure of what a reader experiences; the back-matter ledger should be excluded from the lever. Worth a one-line note in the scoring procedure so future READABILITY passes do not chase dashes in the traceability block.
4. **The reproducibility-double-build is a strong, honest scoring signal.** Both the EXAMPLE-BUILD and CODE-REVIEW gates independently re-ran two `clean package` builds and matched the same SHA-256 (and the flag file records the same hash) — a cross-artifact consistency check that turns the chapter's headline claim from "configured" into "proven." Recommend the proposed `check_reproducible.sh` (two builds + `shasum` + `cmp`) so any future repro-claiming chapter gets a one-command proof recorded with its SHA.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
