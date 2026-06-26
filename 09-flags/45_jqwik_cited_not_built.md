# FLAG — key 45 — jqwik cited in prose but realized prose-only in the companion build

- **Severity:** minor (companion-build scope / tool-vitality; not a fact error — jqwik IS pinned).
- **Context:** key 45 ("The Database That Does Not Exist in Production") presents **jqwik** as the chapter's
  named Java property-based-testing library: `@Property` + `@ForAll`, `Arbitraries`/`@Provide`, shrinking
  to a minimal counterexample, its own `TestEngine` on the JUnit Platform. jqwik **is** a `SOURCE-PIN.md`
  §3 row (**1.10.1**, recorded there as `⚠ maintenance mode`).
- **Decision taken at EXAMPLE-BUILD (Step 4b):** the companion module `08-companion-code/45_integration_property_based_testing/`
  does **NOT** add `net.jqwik:jqwik` to its build. Two reasons, both recorded in the module pom and README:
  1. jqwik is **not managed by the aggregator BOM**, and SOURCE-PIN records it as **maintenance mode**
     (the chapter's own tool-vitality caveat) — a heavy commitment to weigh, not a default add.
  2. The chapter's two teaching ideas — **generated inputs** and **shrinking to a minimal counterexample** —
     are realized runnably with the already-pinned stack only: a seeded `java.util.random.RandomGenerator`
     feeding a JUnit `@ParameterizedTest` body (`SkuPropertyTest`), plus a small JDK `Shrinker` that reduces a
     found failure to its minimal counterexample. The build is green with no extra dependency.
- **What is asserted vs. what is built:** every *factual* claim about jqwik in the PROSE (its annotations,
  shrinking behaviour, TestEngine integration, maintenance-mode status) traces to jqwik's own docs at the
  pin and stays in the prose. The CODE realizes the technique, not the library. The prose lead-ins around the
  property snippets state this honestly ("the module builds without a dedicated property library on its
  classpath, so it realizes the technique with a seeded JDK generator … jqwik is the prose's named Java
  realization").
- **Required action (human/editorial):** confirm this is the intended scope — present jqwik as the named
  library (cited, crown-none, maintenance-mode caveat intact) while the runnable artifact uses the pinned
  JDK-only realization. If a future decision wants jqwik compiled, add it as an explicit test-scope
  dependency at the SOURCE-PIN version (1.10.1) and re-run FLOOR C.
- **Status:** OPEN — scope decision for the human gate. No fact error; no NEUTRALITY breach; build is green.
