# GATE-REPORT — CODE-REVIEW (FLOOR-C, second half)

- **Chapter / module:** 101 — Measure, Don't Guess · `08-companion-code/101_performance_profiling_memory_benchmarking/`
- **Draft reviewed:** `03-drafts/101_performance_profiling_memory_benchmarking/101_performance_profiling_memory_benchmarking_v1.md`
- **Reviewer:** code-reviewer agent (senior-PR pass on published deliverable code)
- **Date:** 2026-06-27
- **Build env:** JDK 21.0.11 (Homebrew openjdk@21) · Maven 3.9.16 · JMH 1.37 (SOURCE-PIN §3, ✅ pinned)

## Verdict: **PASS**

Re-ran `mvn -B -Pquality -f pom.xml clean verify` from a real JDK 21.0.11 toolchain: **BUILD SUCCESS**,
12 tests pass (0 fail/err/skip), **0 Checkstyle**, **0 SpotBugs** (BugInstance size 0), **warning-clean**
(no `[WARNING]` lines, no javac lint/deprecation/unchecked advisories). JMH harness genuinely generated
(7 `jmh_generated` sources; `BenchmarkList` registers all three `@Benchmark` methods with the in-source
Fork/Warmup/Measurement counts). No BLOCKER, no security finding, no neutrality finding, no invention,
no timeless asserted performance number. This is exemplary, idiomatic, teach-from code.

## Finding counts
- BLOCKER / FAIL: **0**
- MAJOR (FIX): **0**
- MINOR / NIT: **3** (all optional; none block FLOOR C)
- Banned NEUTRALITY words in deliverable text: **0**
- Timeless asserted perf numbers in code/comments: **0**
- Hardcoded secrets: **0**

## Six dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 + JMH/profiling quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity + originality/attribution (LEGAL-IP §5) | **PASS** |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `priceOrder` validates null / empty / over-`MAX_LINES` and fails fast with typed `IllegalArgumentException`.
  `tieredDiscountMinor` is a deliberate O(n²) (the *real* hotspot); `formatLineLabel` is the cheap *guessed*
  one — both correct, the asymmetry is the teaching point, and the Javadoc says only a profiler settles it.
- Records `LineItem`/`Quote`/`BenchmarkProfile` validate in compact constructors; no nonsensical state.
- No resource leaks: `BenchmarkProfile.load` reads the properties stream in try-with-resources; missing
  resource → explicit fail-fast (no silent default fallback); `IOException` wrapped, never swallowed.
- JMH idioms are correct: `@State(Scope.Thread)` with a per-thread mutable `sku` populated by
  `@Setup(Level.Trial)`; honest path returns the result, two-result path sinks each via `Blackhole`.
- Buffer sizing `items.size() * 16` cannot overflow (≤ 10 000 × 16 = 160 000).

### 2. Idiomatic Java 21 + benchmark/profiling quality — PASS
- Records, compact constructors, `List.of`, `IntStream`/`toList`, ternaries — modern and clean.
- JMH used as the docs show at the pin: explicit `@Warmup`/`@Measurement`/`@Fork` (Javadoc states *why*:
  defaults move between versions); `@BenchmarkMode(AverageTime)` + `@OutputTimeUnit(NANOSECONDS)`;
  annotation processor named explicitly in the POM (removes javac's auto-discovery advisory → warning-clean).
- `AtomicLong` for the per-trial sequence and the priced-order counter — correct concurrency primitive, no
  raw threads, no ad-hoc stdout (the `main` delegates output to the JMH `Runner`).

### 3. Security — PASS
- Secret grep over `src/`, `config/`, `*.properties` → **no** password/secret/token/apikey/key literals.
- No injection sink, no reflection-from-input, no resource path built from untrusted input (profile name is
  developer-supplied and the failure is a fail-fast IAE that names the resource, leaking nothing sensitive).
- `CONSTANT_SKU`, `"SKU-..."`, and the `1_125_899_906_842_597L`/`31L` literals are hash seed/multiplier and
  demo SKUs — not credentials.

### 4. Simplicity & readability — PASS
- Smallest code that teaches each beat; realistic storefront names (no Foo/Bar/tmp); every public type
  carries a one-line purpose Javadoc (PricingBenchmark's is a multi-line block above the annotations —
  present, the grep's "0" is a false negative from the blank/annotation lines between `*/` and `class`).
- No dead code (the "churning" form is intentional, exercised by a test and `summaryEquivalenceHolds`);
  no unused deps (JMH `provided`, JUnit/AssertJ `test`); reviewed SpotBugs/Checkstyle scoping is documented.

### 5. Prose↔code fidelity + originality — PASS
- **All five displayed tag-regions are brace-balanced, ≤9 lines, not broken mid-statement:**
  `allocation-reduced` 6 ln (1/1 braces) · `state-setup` 8 ln (1/1) · `lying-benchmark` 6 ln (1/1) ·
  `honest-benchmark` 6 ln (1/1) · `blackhole-sink` 7 ln (1/1). Each is a self-contained field+method or method.
- Prose claims trace to code: "non-final `@State` so JIT can't constant-fold" → `private String sku;` (line 55);
  "IDE `final` quick-fix breaks the benchmark" → field is genuinely non-final, `CONSTANT_SKU` is final and used
  only by `measureWrong`; "`@Setup(Level.Invocation)` almost always wrong" → code correctly uses `Level.Trial`;
  "SpotBugs flags the lying benchmark" → reviewed `RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT` suppression, narrowly
  scoped with reason. GAV `org.openjdk.jmh:*:1.37` matches SOURCE-PIN §3 (✅ pinned) in all three POM sites.
- **Originality (LEGAL-IP §5): PASS.** `PricingBenchmark` is an independent implementation — storefront SKU
  polynomial-hash over the flagship domain, `String` `@State`, a `CONSTANT_SKU` constant-folding demo, a
  per-trial `AtomicLong`, externalized `BenchmarkProfile`, and `Runner`/`main` wiring. It is NOT a lightly-edited
  copy of JMH's `JMHSample_08_DeadCode` (which uses `Math.log` on a double field and a `baseline()` method). It
  shares only the *descriptive* method names `measureWrong`/`measureRight` (see MINOR-1). No attribution required.

### 6. Neutrality in code — PASS
- Banned-phrasing grep over `src/`, `config/`, `README.md`, `pom.xml` → **none** (no "better than / unlike X /
  superior / beats / the problem with X" etc.). Tool niches (JFR / async-profiler / commercial) are described
  factually with no crowning anywhere in code, comments, or build files.

## Build / lint result
| Check | Result |
|---|---|
| `mvn -B -Pquality clean verify` | **BUILD SUCCESS** (re-run on JDK 21.0.11) |
| Tests | 12 run / 0 fail / 0 err / 0 skip |
| Checkstyle | 0 violations |
| SpotBugs | 0 (BugInstance size 0) |
| Warning-clean | yes — no `[WARNING]`, no javac advisories |
| JMH harness generated | yes — 3 benchmarks in `BenchmarkList` |
| Hardcoded secrets | none |
| Failure-path test | present & real (empty / over-limit / unknown profile / non-positive counts) |

## Findings table

| Sev | File:line | Issue | Suggested fix (for example-builder; do NOT block) |
|---|---|---|---|
| MINOR | `PricingBenchmark.java:66,75` | Method names `measureWrong`/`measureRight` coincide with JMH's canonical sample-08 names. Bodies/domain are original, so §5 is satisfied, but the names can read as a tell of the upstream sample. | Optional: rename to domain-specific `hashSkuDiscardingResult` / `hashSkuReturningResult` (or similar) to make originality unmistakable. Cosmetic only. |
| MINOR | `OrderPricing.java:109` | `summaryLineChurning` (the anti-pattern "before") is `public` API. It is intentional and Javadoc-explained, but shipping the deliberately-wasteful form as public surface is mildly unusual. | Optional: keep public (it is a teaching contrast and the test calls it) — or demote to package-private and have the test call it within-package, matching `summaryEquivalenceHolds`. |
| NIT | `OrderPricing.java:132` | `items.size() * 16` is a reasonable average-SKU heuristic but undocumented as a heuristic. | Optional: a 3-word comment ("~16 chars/line estimate") would pre-empt a reader wondering about the 16. |

## Perf-NUMBER audit (the critical perf gate)
No timeless asserted measurement anywhere in code or comments. Every numeric/temporal phrase is either
config, domain, or explicitly qualitative:
- `@OutputTimeUnit(NANOSECONDS)` — JMH reporting-unit config, not a result.
- "warms up and forks for **tens of seconds**" / README "would run for tens of seconds" — qualitative
  description of *harness runtime*, not a result of the code under test.
- README "report an **implausibly small time** because its work is eliminated" — explicitly labelled as the
  *lie* (DCE artifact), the correct way to describe `measureWrong`.
- "**10%** over the free-shipping threshold" (`OrderPricing.java:96`) — business discount rate (`worstCase / 10L`).
- "a bug, not a **speed-up**" — generic prose, no number.
- The hook's "10×/tenfold" lives only in the *prose draft*; the code never fabricates a figure.

## Learnings & pipeline suggestions
1. **Strong pattern to promote:** externalizing benchmark run-counts to `dev`/`prod` profiles (with a fail-fast
   loader + its own failure-path test) is a clean way to keep an honest-benchmark module green in `verify` while
   the real run stays offline. Worth recommending in `EXAMPLES-GUIDE` for any future JMH chapter.
2. **Originality near-collision:** reusing a well-known sample's method names (even with an original body) is the
   most likely place a JMH-based chapter could *look* derivative. Suggest a soft check / reviewer note: when a
   module teaches a documented harness idiom, prefer domain-specific identifiers over the upstream sample's names.
3. **Tooling gap:** this environment had no `mvnw`; the build only ran after pointing `JAVA_HOME` at
   `openjdk@21` and using the system `mvn`. The companion reactor relies on a Maven wrapper or a pre-provisioned
   toolchain — worth confirming `08-companion-code/mvnw` exists for reproducibility on a fresh checkout.
4. **Snippet hygiene held perfectly** — all five tag-regions brace-balanced and ≤9 lines on the first pass; the
   tag-region-is-a-compiled-method discipline is paying off. No action needed.
