# SCORECARD (INDEPENDENT) — Chapter 17 (key 35)

> Independent (different-model, harsh-skeptic) score at Step 8. Rubric: `00-strategy/SCORING.md`
> (five clusters 1–10 + floors A/B/C; ship bar ≥44/50 with no cluster <6, all floors PASS).
> A self-score never approves; this independent re-score is the approval-gating score.

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard
- **Dossier key:** 35 (frozen; owner — folds 36 + 37)
- **Slug:** `35_sonarqube_ide_layered_stack`
- **Title:** Composition, Not Accumulation — SonarQube as platform, the IDE as first line, layering analyzers into one stack
- **Part / arc position:** Part IV — Static Analysis, Linting & Formatting · printed **Chapter 17** (FINAL_INDEX, LOCKED)
- **Artifact scored:** `03-drafts/35_sonarqube_ide_layered_stack/35_sonarqube_ide_layered_stack_v1.md`
- **Verified against** SOURCE-PIN 2026-06-20 (SonarQube Server **2026.1 LTA / patch 2026.1.3**; Checkstyle **13.6.0**; SpotBugs **4.10.2**; JUnit **6.1.0**; Java 21.0.11 anchor) — re-check date: **2026-06-28**
- **Scorer:** chapter-scorer agent (independent / harsh-skeptic)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds pass applied; see log)

---

## The three content-floors (checked FIRST — gate, not averaged)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan (`better than / unlike X / the problem with / superior / beats / outperforms / blows away / destroys / no reason to use`) = **0 hits**. Survey stance held: Hook resolves "neither more tools nor fewer tools"; CONCEPT "None is 'smarter' — each stands somewhere different"; "no tool is crowned" stated twice; IDE table captioned "(neither crowned)"; one-owner table carries an "Equally-valid alternative" column. Every cross-tool claim (Lenarduzzi low-agreement) carries the citation. The two `is not "a better linter"` instances are misconception-*refutations* (deny a crowning), neutrality-safe; one was edited to a period in the lift pass. No section title crowns. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" with explicit when-NOT per feature: platform ops cost (build-only suffices); paid taint SAST (free Community Build lacks it); bytecode+classpath setup cost; IDE-not-a-gate; save-action diff wreckage; SQALE coarseness; "more tools is not more quality"; static analysis ≠ proof of correctness. Each body feature also carries its cost inline. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** *(one reconcile condition recorded)* | **COMPILE:** `_EXAMPLE.md` — `mvn -B -Pquality verify` GREEN: 7 tests, 0 Checkstyle, 0 SpotBugs (JDK 21.0.11 / Maven 3.9.16, 2026-06-26); offline re-run also green. **CODE-REVIEW:** `_CODEREVIEW.md` verdict **PASS-WITH-FIXES** — no BLOCKER, no security/neutrality breach, no invented atom. **SOURCE-TRACE:** Sonar SaaS atoms dated-at-use and flagged to `09-flags/35_sonar_versions_and_defaults_unverified.md` + `09-flags/35_clean_code_taxonomy_and_issuetype_status_unverified.md`; `java:S2077` verified; `sonar.java.binaries` + Lenarduzzi quote verbatim; no rule severity / "Sonar way" membership asserted from memory. **Recorded condition (not a FAIL):** the displayed `layered-gate` snippet shows `<version>10.26.1</version>` (Checkstyle engine) and `3.6.0` (plugin), both off the SOURCE-PIN value of **Checkstyle 13.6.0** (and build-only SpotBugs `4.9.3.0` vs pinned 4.10.2). These are **real, green-building, off-pin** literals, not invented — a known aggregator-wide drift recurring across ~7 modules (CODE-REVIEW F2). The CODE-REVIEW gate of record grades it MINOR and explicitly forbids patching ch 35 in isolation ("fix at the aggregator repo-wide"). Floor passes because the atom is real/traceable-to-the-build and the drift is flagged in the gate of record; it **must** be reconciled repo-wide before the Step-16 MANUSCRIPT-GATE. It costs ACCURACY one point below. |

**All three floors PASS.** No floor failure — the lift loop is a cluster-quality lift, not a floor fix.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | One load-bearing spine carries the chapter: substrate × moment → one-owner-per-concern. Figure 17.1 leads, the matrix table makes it concrete, three CONCEPT callouts name the load-bearing ideas (substrate=reach/moment=latency; platform=engine+layer-above; first-line-not-the-gate), and the deep dive maps each of three failure modes (overlap/redundancy/noise) to its fix. A reader new to the topic can reconstruct *why* layering is additive and *how* to compose without overlap. Held off 10 only by the density of the SonarQube section (MQR-vs-Standard taxonomy + engine + platform + Clean-as-You-Code in one block). |
| 2 | **ACCURACY** | **8** | Sonar identity/taxonomy facts trace to the dossier with verify-at-pin discipline; SaaS atoms flagged to two `09-flags/` entries; `java:S2077` verified; no severity asserted from memory; Lenarduzzi quote verbatim + dated (DATED caveat present). **Ding:** the displayed `layered-gate` snippet prints `<version>10.26.1</version>` / `3.6.0` (Checkstyle engine/plugin) against the SOURCE-PIN value **13.6.0** — a displayed, version-sensitive atom that contradicts the pin and is not itself flagged (only in CODE-REVIEW F2). Real and green-building, not fabricated, but off-pin in print. That is exactly what holds this at 8. |
| 3 | **UTILITY** | **9** | A page a working developer keeps open: one-owner table with "Equally-valid alternative" is directly actionable; cheap-first/fail-fast ordering is a concrete build sequence; "When to use what" gives crisp routing (author-time / build / trend-gate / free-SAST / small-team / before-gating); the green companion module's displayed config (`sonar.java.binaries`, `sonar.qualitygate.wait=true`, Clean-as-You-Code reference, `.editorconfig`) is copy-usable. Decisions are concrete, not a tour. |
| 4 | **DEPTH** | **9** | Full mechanism + evidence-for (additivity, Lenarduzzi) + honest limits + alternatives (bare analyzers, Codacy/Code Climate, Qodana, CodeQL/Semgrep, pre-commit) + when-to-use, all sourced. Three layers each carry real substance; the three-failure-mode deep dive is genuine analysis. Routing to Ch 15/16/18/19/security/metrics/CI keeps it from sprawling. Verified substance, **no word-count padding** (the lift pass was subtractive on punctuation only — DEPTH unchanged). |
| 5 | **READABILITY** | **9** | Voice holds: invisible narrator, no first person, no narration contractions, **zero** banned filler/hype (greps clean), terms glossed plain-first, callouts used sparingly from the fixed taxonomy, strong short-sentence rhythm ("Both are right."). **Lifted 8→9:** em-dash density brought from **8.04/1000 → 6.04/1000** (24 in ~3,972 prose words) by converting eight appositive em-dashes in the SonarQube section, deep dive, and back-matter to periods/commas/parentheses. Comfortably under the ~8/1000 ceiling. |

**Cluster subtotal: 44 / 50.**

---

## Verdict

- [x] **SHIP** — clears the bar (44/50, no cluster <6; all three floors PASS) after one in-bounds lift pass. Ready for the human approval gate, with one recorded MANUSCRIPT-GATE reconcile condition (displayed Checkstyle/SpotBugs version atoms → pin, repo-wide).
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** A genuinely strong neutral survey of the layered analyzer stack — clear spine, copy-usable config, full depth — that cleared the 88% bar after a single READABILITY lift (em-dash density), carrying one out-of-scope displayed off-pin version atom recorded for the repo-wide reconcile.

---

## Flagged weakest cluster (now resolved to the bar)

- **Weakest at Pass 0:** READABILITY (and ACCURACY) tied at 8.
- **Why READABILITY was the liftable one:** ACCURACY's only ding (displayed off-pin Checkstyle `10.26.1`) is **out of in-bounds reach** — fixing it needs a pom `<version>` edit + rebuild, and the CODE-REVIEW gate of record explicitly forbids patching ch 35 in isolation ("fix at the aggregator repo-wide"). Lifting it in-bounds would mean editing displayed code against the gate or fabricating a flag — not permitted. READABILITY had a clean, no-new-facts, no-floor-risk lever (em-dash density at 8.04/1000, marginally over the ceiling).
- **Highest-leverage move (applied):** convert ~8 appositive em-dashes to periods/commas/parentheses → density 6.04/1000, READABILITY 8→9, aggregate 43→44.

---

## Line-level fixes (lift list — Pass 1, all applied in-bounds)

| # | Cluster | Location (section · ¶) | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | READABILITY | SonarQube §, opening ¶ | appositive em-dash | "is not 'another linter' — it is" → "is not 'another linter.' It is … wrapping a rule engine, and that distinction…" |
| 2 | READABILITY | SonarQube §, scanner-config lead-in | em-dash appositive | "records exactly that — where…" → "records exactly that: where…" |
| 3 | READABILITY | CONCEPT (platform=engine+layer) | em-dash appositive | "is not 'a better linter' — it is the layer" → "is not 'a better linter.' It is the layer" |
| 4 | READABILITY | two-honest-limits ¶ (densest) | two stacked em-dashes | flagship-security em-dash pair → parenthetical; naming-note em-dash → comma (removed 2) |
| 5 | READABILITY | deep dive · Noise | em-dash appositive | "Chapter 19 owns the how — suppression…" → "Chapter 19 owns the how: suppression…" |
| 6 | READABILITY | deep dive · composed-result ¶ | long em-dash appositive | "sits above it all — aggregating…" → "sits above it all, aggregating…" |

> No fact, citation, version atom, snippet, or code file was touched. No rebuild / no `check_snippets` required (constraint: rebuild only if code touched; check_snippets only if a displayed snippet touched — neither occurred). Banned-phrase/filler re-scan after edits: CLEAN.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A NEUTRALITY | B HONEST-LIMITATIONS | C SRC/COMPILE/CR | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | **43** | PASS | PASS | PASS (1 cond.) | LIFT-LOOP | initial independent score — C/A/U/D/R = 9/8/9/9/8; em-dash 8.04/1000 |
| 1 | 2026-06-28 | **44** | PASS | PASS | PASS (1 cond.) | **SHIP** | READABILITY 8→9 (em-dash 8.04→6.04/1000, 8 appositives recast); all other clusters unchanged; floors unchanged |

---

## Conditions carried to the human / MANUSCRIPT-GATE (not ship-blockers at Step 8)

1. **Displayed off-pin version atoms (CODE-REVIEW F2 / F3).** `layered-gate` snippet shows Checkstyle engine `10.26.1` + plugin `3.6.0` vs SOURCE-PIN **13.6.0**; build-only SpotBugs `4.9.3.0` vs **4.10.2**; inherited JUnit `6.0.3` vs **6.1.0**. Reconcile **at the aggregator, repo-wide** (recurs across ~7 modules) and re-run `-Pquality verify` before Step 16; do not patch ch 35 in isolation. If the displayed `10.26.1`/`3.6.0` cannot move to the pin before release, add a `09-flags/` displayed-atom entry so the printed literal is explicitly flagged rather than silently off-pin.
2. **`layered-gate` XML self-balance (CODE-REVIEW F1, MINOR).** The shown region opens `<dependencies>`/`<dependency>` whose closes fall outside the snippet; either retag to self-balance or add a "(wrapper elided)" prose cue at the include site. Editorial, not a floor breach.
3. **Sonar SaaS atoms stay dated-at-use.** Scanner GAV, rule default severities, edition gating, SQALE grid remain `⚠ verify at pin` (flagged); re-confirm at `/pin-source` / public-push. Correctly handled as-is.

---

## Learnings & pipeline suggestions

- **Off-pin *displayed* version literals are an ACCURACY/FLOOR-C edge the harsh score must name even when CODE-REVIEW grades them MINOR.** A literal that builds green is not automatically pin-traceable: `10.26.1` is a real Checkstyle release but contradicts the pin (13.6.0) and is printed in the book. The honest resolution at Step 8 is *floor passes (real, not invented) but the cluster takes the hit and the reconcile is carried as a release condition* — never score it away as clean. Worth a one-line note in SCORING.md FLOOR C: "a displayed version atom must match the pin **or** carry a `09-flags/` displayed-atom entry; a green build at an off-pin literal does not satisfy source-trace by itself."
- **Em-dash density should be measured on the prose body only.** A raw file-wide count over-reports: 9 of the 43 raw em-dashes were inside the front-matter provenance comment and 2 in the figure caption (`&mdash;` entities aside). Computing on lines 15–199 minus include directives and the caption gave the true 8.04→6.04/1000. Recommend the AUDIT/score em-dash check strip the front-matter HTML comment and figure captions before dividing — otherwise drafts get flagged for cadence that readers never see.
- **The in-bounds lift had a real ceiling this time, and naming it mattered.** The tied-weakest cluster (ACCURACY) was *not* liftable in-bounds because the only fix collides with the CODE-REVIEW gate's repo-wide-reconcile instruction. Choosing the genuinely liftable cluster (READABILITY) rather than forcing an out-of-bounds ACCURACY edit is the correct discipline; the loop should always check "is the weakest cluster's fix actually in-bounds?" before spending a pass on it.
