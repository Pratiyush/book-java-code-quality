# SCORECARD — Ch 17 "SonarQube, IDE inspections & the layered stack" (key 35 + 36 + 37)

> Part IV (Ch 15-19), three merged dossiers (Sonar platform + IDE first line + layering synthesis). A-tier;
> carries the cross-tool verdict Ch 16 deferred. Main-loop; gates = manual passes. substrate×moment-matrix +
> platform=engine+layer-above + first-line-not-the-gate + one-owner-per-concern shapes. Draft:
> `35_sonarqube_ide_layered_stack_v1.md` (folder = owner-key dossier slug). Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (2 "worse than" reworded); the whole chapter IS the comparison and crowns nothing — substrate×moment matrix frames each tool by vantage; one-owner-per-concern is neutral routing with named alternatives; IntelliJ-vs-Eclipse each its case+limit; "composition not accumulation" thesis; each tool cited to its own docs. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (Sonar platform/ops cost + paid taint SAST + bytecode-classpath need; IDE not-a-gate + save-action diff-noise; SQALE coarse-not-exact; more-tools≠more-quality + noisy-gate-net-negative; necessary-not-sufficient) + §When to use + the overlap/redundancy/noise deep dive. |
| C — SOURCE-TRACE | ✅ PASS | Sonar taxonomy/CaYC/sonar.java.binaries/product-rename verbatim from Sonar docs; IDE severities/save-actions/inspect.sh verbatim from vendor docs; Lenarduzzi "little to no agreement" verbatim (DATED/FindBugs caveat); all versions/defaults/edition-gating/SQALE grids/IDE-membership carried verify-at-pin; AI CodeFix flagged AHEAD-OF-PIN; rules.sonarsource.com offline noted (RSPEC). |
| C — COMPILE | ⚠ PENDING (toolchain READY) | extends the flagship quality profile into the layered stack + a Sonar Testcontainers demo; spec'd, not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the two-facts-in-tension hook + the substrate×moment matrix + the one-owner-per-concern table make a 3-dossier synthesis cohere; three CONCEPT callouts (substrate/latency, platform=engine+layer, first-line-not-gate) anchor it. |
| ACCURACY | 8 | Sonar taxonomy/CaYC/rename + IDE atoms + Lenarduzzi verbatim; −2 for all Sonar/IDE versions, rule defaults, edition gating, SQALE grids, default-profile membership carried verify-at-pin (atoms, flagged); product-rename + dated-study handled precisely. |
| UTILITY | 9 | delivers the actionable layering verdict Ch 16 deferred: one-owner-per-concern map, cheap-first/fail-fast order, IDE-as-shared-first-line, Clean-as-You-Code gate scoping, tune-before-gate; directly composable into a real pipeline. |
| DEPTH | 9 | substrate×moment as the unifying lens + the overlap/redundancy/noise failure-mode analysis + platform-vs-analyzer distinction + the dated-empirical-agreement evidence is senior-architect material, not a tool tour. |
| READABILITY | 8 | strong tension hook, matrix + comparison tables, three CONCEPT callouts + one AHEAD-OF-PIN, the deep dive carries the composition argument; appropriately the synthesis-chapter length (4077w). |

**Aggregate 43/50**, none < 6 (ties Ch 13/14 as highest — fitting for the Part IV synthesis node). Floors A/B/C-source ✅;
FLOOR-C COMPILE = PENDING (toolchain READY). Reuses substrate×moment-matrix + platform=engine+layer + first-line
-not-the-gate + one-owner-per-concern shapes (new); + product-rename + dated-empirical disciplines. Hands off to
Ch 18 (custom rules + annotation processors + Lombok). Closes the analyzer-composition arc (Ch 15-17).
