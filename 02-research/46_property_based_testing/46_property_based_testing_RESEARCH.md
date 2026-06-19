# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B) dossier, authored main-loop (cheaper mode). Facts traced to pinned authorities;
> versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 46 — `01-index/CANDIDATE_POOL.md`
- **Title:** Parameterized & property-based testing — jqwik, fuzzing
- **Part:** Part V — Testing · **Tier:** B · **Depth:** Standard · **Cmp:** ⚠ (approaches)
- **Primary authorities:** jqwik (`jqwik.net`, `github.com/jqwik-team/jqwik`); JUnit Jupiter params (`junit-jupiter-params`, docs.junit.org); fuzzing references (Jazzer/JQF) cited as adjacent.

## 1. Core definition & purpose
Example-based tests (key 42) check the cases *you thought of*. Two techniques widen that: **parameterized tests** run one test body over many supplied inputs (kills duplication), and **property-based testing (PBT)** asserts a *property that should hold for all inputs* and lets the framework **generate** hundreds of inputs and **shrink** any failure to a minimal counterexample. The quality payoff: PBT finds edge cases (empty, boundary, unicode, huge, negative) humans skip. The chapter covers both, where each fits, and the honest costs.

## 2. Mechanism (the spine)
### 2.1 Parameterized tests (JUnit Jupiter)
`@ParameterizedTest` + a source: `@ValueSource`, `@CsvSource`, `@EnumSource`, `@MethodSource` (computed args), `@ArgumentsSource`. Same body, many inputs, each a reported case. The cheapest step up from `@Test`; you still pick the inputs.

### 2.2 Property-based testing (jqwik)
- Runs **on the JUnit Platform** as its own `TestEngine` (composes with JUnit 6/5).
- `@Property` (instead of `@Test`) + `@ForAll` parameters → jqwik **generates** values; default a configurable number of tries.
- **Arbitraries** (`Arbitraries`, `@ForAll` providers, `@Provide`) describe the input domain; combinators build complex generators.
- **Shrinking** — on failure, jqwik reduces the failing input to a minimal counterexample (the feature that makes PBT debuggable).
- Lifecycle: `@BeforeProperty`/`@AfterProperty`, `@BeforeContainer`/`@AfterContainer`.
- Integrates with Testcontainers (`jqwik-testcontainers`) for model-based tests (key 45).

### 2.3 Fuzzing (adjacent)
Coverage-guided fuzzing (e.g. Jazzer, JQF) generates inputs to maximize code coverage / trigger crashes — overlaps PBT's "generate inputs" idea but is security/robustness-oriented (ties to key 70). Cited as adjacent; deep security treatment is Part VIII.

### 2.4 Where each fits
- Parameterized: known finite input sets, table-driven cases.
- PBT: invariants/round-trips (`parse(format(x)) == x`), commutativity, idempotence, "never throws on valid input."
- Fuzzing: untrusted-input robustness/security.

### 2.5 Wiring
`net.jqwik:jqwik` (test scope) on the JUnit Platform; `junit-jupiter-params` ships with JUnit. *(GAVs/versions `⚠ verify at pin`.)*

## 3. Evidence FOR
- **Finds edge cases example tests miss** — generation + shrinking surfaces minimal counterexamples automatically.
- **Parameterized tests cut duplication** — one of the cheapest test-quality wins (less copy-paste = fewer divergent near-tests, key 49).
- **Composes on the Platform** — jqwik runs alongside Jupiter tests; no separate runner needed.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **jqwik is in maintenance mode (2026)** — per its own project status, development is maintenance-only. A senior team must weigh that when adopting (when-NOT-to-adopt-heavily, or plan for the possibility of a quieter future). **State this plainly** (do not crown jqwik; present it with this limitation). *(Source: jqwik project status; reconfirm at pin.)*
- **Writing good properties is hard** — the discipline is finding the *invariant*; a weak property ("doesn't throw") tests little. PBT has a real learning curve.
- **Non-determinism & repro** — generated inputs vary; you need seed-pinning to reproduce a failure in CI (flakiness risk, key 49).
- **Slower** — hundreds of generated cases cost time vs a handful of examples (key 79).
- **Not a replacement** for example tests — the two complement; PBT for invariants, examples for specific known behaviours.

## 5. Current status
JUnit parameterized tests: current, first-class. jqwik: functional and Platform-integrated but **maintenance mode** — the headline honest caveat. Fuzzing (Jazzer/JQF) active in the security space. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion add-on** to the key-42 harness: a `@ParameterizedTest` table + a jqwik `@Property` with `@ForAll` demonstrating a round-trip invariant and a shrunk counterexample. Built green; tag-region snippet.
- **Figure:** Fig 46.1 — example vs parameterized vs property (inputs you pick → inputs generated → shrunk counterexample). Trace to jqwik docs.

## 7. Gap-filling (verification queue)
- ⚠ jqwik current version + **maintenance-mode status** — confirm against jqwik's own release notes/README at pin.
- ⚠ Default `@Property` tries count, `Arbitraries`/`@Provide` API — verify against jqwik User Guide (pin the version).
- ⚠ JUnit param source annotations unchanged in JUnit 6 — confirm at pin.
- ⚠ Fuzzing tools (Jazzer/JQF) Java status — confirm if used beyond a mention.

## 8. Sources & further reading
### Primary / official
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | jqwik (site + repo + release notes) | jqwik.net ; github.com/jqwik-team/jqwik | ☑ model + maintenance-mode; ⚠ version |
| 2 | JUnit Jupiter params | docs.junit.org/current/user-guide | ☑ @ParameterizedTest sources |
### Accessible
| # | Source | URL |
|---|---|---|
| 1 | Baeldung — PBT with jqwik | baeldung.com/java-jqwik-property-based-testing |
| 2 | Docker — model-based testing (Testcontainers + jqwik) | docker.com/blog |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | jqwik / parameterized 2026 | @Property/@ForAll; lifecycle annotations; Testcontainers integration; **jqwik in maintenance mode** |

---
## Learnings & pipeline suggestions
- **New folklore-adjacent honesty:** record jqwik's maintenance-mode status so no draft oversells it; this is a "tool vitality" caveat worth a standing note (adoption-risk lens).
- **Cross-ref:** substrate → key 42; duplication/test smells → key 49; fuzzing/security → key 70; model-based + containers → key 45.
