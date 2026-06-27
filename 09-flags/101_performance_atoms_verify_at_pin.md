# FLAG — key 101: performance/profiling atoms still ⚠ verify-at-pin

- **Chapter / key:** 101 — "Measure, Don't Guess" (Part XIII opener; folds 102 + 103 + 104/51)
- **Type:** ⚠ verify-at-pin (SOURCE-VERIFY track — atoms NOT confirmable from SOURCE-PIN.md + the companion module alone)
- **Raised:** 2026-06-27 (deferred-marker resolution pass, draft v1)

## Context
A deferred-marker pass on `03-drafts/101_.../101_..._v1.md` resolved the atoms confirmable
against the two in-scope authorities — (a) the corrected `SOURCE-PIN.md` (2026-06-27) and (b)
the BUILT-green companion module `08-companion-code/101_.../` (`mvn -B -Pquality verify`, JMH
1.37 harness compiled). Those are listed under "Resolved" below. The atoms here are the residue:
each needs its own pinned/dated primary source, which the module deliberately asserts none of and
which `SOURCE-PIN.md` does not pin to a fact-level row. They stay marked in the draft.

## ✅ Resolved in this pass (no longer marked)
- **JMH version 1.37** — `SOURCE-PIN.md` §3 pinned row (✅ 2026-06-20) AND the companion `pom.xml`
  pins `org.openjdk.jmh:jmh-core:1.37` + `jmh-generator-annprocess:1.37`; the harness compiles green
  (generated `*_jmhTest` drivers + `META-INF/BenchmarkList`).
- **JMH annotations / Mode / Scope / Level / Blackhole / @OutputTimeUnit** — used verbatim in the
  compiling `PricingBenchmark.java` at the pin (`@Benchmark`, `@BenchmarkMode(Mode.AverageTime)`,
  `@State(Scope.Thread)`, `@Setup(Level.Trial)`, `Blackhole.consume`, `@Fork/@Warmup/@Measurement`).
- **Stale build-status string** — front-matter line corrected from "⚠ EXAMPLE-BUILD = PENDING" to
  BUILT GREEN (matching `_EXAMPLE.md` PASS, 2026-06-26). REPRO PENDING-RUNTIME retained (runs are
  genuinely env-gated; the benchmark is built-not-run).

## ⚠ Still unverified — needs a pinned/dated primary source (SOURCE-VERIFY, Step 5)
1. **Knuth "premature optimization" quote + attribution** — named-canon (SOURCE-PIN §7), not a
   pinned row; verify the verbatim wording/source. The draft paraphrases the idea, asserts no quote.
2. **ISO/IEC 25010 Performance Efficiency sub-characteristics** (time-behavior / resource-utilization /
   capacity) — standard text not redistributed; see also `09-flags/01_iso25010_2023_subtree_unverified.md`.
   Draft states the characteristic at top level only.
3. **JFR overhead "~1%"** — a tool/vendor perf claim; not pinned, module asserts no number. Prose keeps
   "roughly 1% overhead (verify at the pin)". Vendor perf claim → stays a dated/attributed figure or flagged.
4. **JEP 509 status** (experimental CPU-time profiling) — per-feature JEP confirm at use (SOURCE-PIN
   §1 JEP index policy); not confirmable from the two authorities.
5. **async-profiler modes/versions + JDK Mission Control** — async-profiler is NOT a pinned row;
   profiler-tool versions are explicitly out-of-reach for this pass.
6. **Default GC = G1; ZGC/Parallel trade-offs; escape-analysis specifics** — JDK GC defaults vary by
   JDK/platform; not pinned to a fact row. Prose keeps "verify defaults at the pin".
7. **JMH default warmup/fork/measurement counts** — defaults move between JMH versions; the module sets
   explicit counts precisely so no default is asserted. Never print a JMH default count as fact.
8. **Costa et al. (IEEE TSE) "widespread bad benchmarking practice"** — academic citation; verify
   author/venue/year before asserting. Appears in the dossier ledger + back-matter, not rendered prose.

## Rule
Per SOURCE-PIN moving-target policy + LEGAL-IP §1/§3: an atom that cannot be traced to a pinned/dated
primary source is kept marked, not asserted. No performance NUMBER is printed timeless: any figure that
survives must be dated + attributed. (Value types / Valhalla AHEAD-OF-PIN is tracked separately in
`09-flags/14_valhalla_ahead_of_pin.md` — re-confirmed AHEAD-OF-PIN 2026-06-27.)

## Resolution path
SOURCE-VERIFY (Step 5) confirms each item against its primary source (JDK/JFR docs at the pinned JDK,
the JEP page, the async-profiler release, the ISO text, the Knuth source, the TSE paper) or cuts/
qualifies it. The companion module remains the authority that none of these is hard-coded as a fact.
