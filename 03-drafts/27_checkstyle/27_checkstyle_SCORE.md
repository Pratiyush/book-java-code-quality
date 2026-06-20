# SCORECARD — Ch 16 "Style & bug-finding: Checkstyle, PMD, SpotBugs, Error Prone" (key 27 + 28 + 29 + 30)

> Part IV (Ch 15-19), four merged dossiers (the core bug-finders). A-tier. Main-loop; gates = manual passes.
> detection-time-position + encodes-a-written-standard + one-engine-N-detectors + compiler-as-gate shapes.
> Draft: `27_checkstyle_v1.md` (folder `27_checkstyle` = owner-key dossier slug per convention). Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); the four tools framed by vantage point not ranking ("never which one, but what each adds"); cross-tool verdict/layered stack explicitly routed to Ch 17; FindSecBugs/fb-contrib framed as SpotBugs capabilities not rivals; style values (LineLength 80 vs Google 100) = cited choices; each tool cited to its OWN docs. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (Checkstyle no-types/style≠correct; PMD syntactic-proxy/reflection FPs + CPD textual-only + minimum-tokens; SpotBugs bytecode-distance/heuristic/FindSecBugs-not-full-SAST; Error Prone every-compile/JDK-couple/Refaster-syntactic; all catch patterns-not-logic; build-time+triage cost; don't-add-redundant-tool) + §When to use + the two-pin-trap + suppress-with-reason discipline. |
| C — SOURCE-TRACE | ✅ PASS | each tool's mechanism + headline rules verbatim from its own docs (Checkstyle no-types limitation; PMD Karp-Rabin/minimum-tokens-required; SpotBugs rank 1-20/effort/144-patterns; Error Prone JDK-21-floor/ON_BY_DEFAULT-ERROR/javac flags); FindBugs→SpotBugs guard; all versions/defaults/thresholds/GAVs + two-pin coordinates carried verify-at-pin; no AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | four companion modules (one per tool, defective+fixed) spec'd, not yet authored/built; JDK 21.0.11+25.0.3 now installed so this is in-pipeline, not blocked. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the four-tools-four-bugs hook + the detection-time table (reads/when/types/reach/blind-spot) as the spine + one proportionate section per tool make a 4-dossier merge coherent; CONCEPT callouts (detection-time, one-tool-two-proxies, two-pin-trap) anchor it. |
| ACCURACY | 8 | mechanisms + headline rule IDs + the JDK-21 Error Prone floor verbatim from each tool's docs; −2 for all tool versions, per-rule defaults/severity/thresholds, two-pin coordinates, CPD minimumTokens=100, FindSecBugs count carried verify-at-pin (atoms, flagged). |
| UTILITY | 9 | gives a senior reader exactly what each tool sees (so they can read any finding knowing its vantage), the suppress-with-reason + legacy-on-ramp + two-pin discipline, and a per-tool When-to-use; directly actionable for composing a stack. |
| DEPTH | 8 | the detection-time-position axis unifies four tools into one "what each vantage lets it see" argument; CPD-as-second-proxy, SpotBugs one-engine-N-detectors, Error Prone compiler-as-gate + Refaster, and the two-pin trap are genuine senior material, not a tour. |
| READABILITY | 8 | strong hook, the spine table, three CONCEPT callouts, proportionate tool sections, the shared-discipline deep dive; appropriately the four-tool-chapter length (4017w) without a grey wall. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain now READY — modules authored/built next). Reuses detection-time-position (Ch 15) + approximation-of-spec-property + encodes-a-written-standard shapes; new one-engine-N-detectors + two-pin-trap. Hands off to Ch 17 (SonarQube/IDE/layered stack — the deferred verdict). Custom rules → Ch 18; FP policy → Ch 19; SAST depth → security part.
