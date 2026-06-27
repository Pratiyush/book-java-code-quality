# GATE-REPORT — CODE-REVIEW (FLOOR-C, second half)

- **Chapter:** 48 — The Number That Feels Like Quality (`coverage-mutation-effectiveness`)
- **Module:** `08-companion-code/48_coverage_mutation_effectiveness/`
- **Draft reviewed:** `03-drafts/48_coverage_mutation_effectiveness/48_coverage_mutation_effectiveness_v1.md`
- **Reviewer:** code-reviewer agent (senior-PR pass on code readers copy)
- **Date:** 2026-06-27
- **Toolchain:** JDK 21.0.11 (Homebrew openjdk@21 — matches aggregator pin `maven.compiler.release=21`); Maven 3.x; JaCoCo 0.8.15; PITest 1.25.3 + pitest-junit5-plugin 1.2.3. Build executed by the reviewer (parent pom installed, then module verified).

## Verdict: **PASS-WITH-FIXES**

No BLOCKER. One MAJOR fidelity FIX (a wrong operator claim inside a displayed `// tag::` comment). Everything load-bearing for FLOOR C — green warning-clean build, real failure path, mutants genuinely killed not just covered, no secrets, no banned phrasing in code — passes and was reproduced from source.

---

## Six dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose <-> code fidelity + originality/attribution | **FIX** (one MAJOR; otherwise faithful) |
| 6 | Neutrality in code | **PASS** |

---

## 1. Correctness — PASS
- `Discount.apply` validates first (NPE on null price, IAE on negative qty / rate outside [0,1)), then branches on the threshold, computes in exact minor-unit integer math via `Math.round`, increments the counter only on the discount path, and returns a fresh immutable `Money`. No resource leaks, no swallowed exceptions, no mutable shared state beyond the deliberate `AtomicLong` counter (correctly atomic).
- `Money` is an immutable record with fail-fast canonical-constructor guards (null/blank currency, negative amount).
- Tests are non-vacuous and assert real behaviour. `DiscountTest` pins both sides of the boundary (qty 9 vs 10), the exact discounted value (900, 750, and the rate=0 identity), the counter advancing only on the discount path, and three distinct failure paths. `MoneyTest` covers both sides of every guard branch.
- **Failure path is real and is the chapter's whole point — reproduced:** scoping the mutation run to the weak test alone (`-DtargetTests=...DiscountWeakTest`) gives 15 mutations / 5 killed = **33%** and `BUILD FAILURE: Mutation score of 33 is below threshold of 80`. Exactly the spec's claim.

## 2. Idiomatic Java 21 quality — PASS
- `record Money` with compact canonical constructor; `final` class `Discount`; `java.util.Objects.requireNonNull`; `java.lang.System.Logger` (no ad-hoc `System.out`, no third-party log dep) — modern, framework-free idioms.
- JUnit 5 + `@Nested`/`@DisplayName` group the cases readably; AssertJ fluent assertions; no raw threads, no blocking misuse.
- Build is **warning-clean**: aggregator enables `-Xlint:all,-processing`; `[WARNING]` count in the quality build = **0**. (The two `WARNING: rejecting out-of-range discount rate` lines are `System.Logger` output from the failure-path tests, not build warnings.)

## 3. Security — PASS
- Hardcoded-secret grep (password/secret/token/apikey/credential/private-key/PEM) across `src`, `pom.xml`, `config`: **no hits**.
- Inputs validated on the public surface (`apply`, `Money` constructor). No injection sink, no reflection, no I/O, no network. Exceptions carry safe messages (the offending value only) — no internals/stack-trace leakage by design.

## 4. Simplicity & readability — PASS
- Smallest code that teaches the point: one behaviour-rich method + one value type + three focused test classes. No dead code, no unused deps (JUnit + AssertJ both used), no gratuitous abstraction.
- Realistic names (`Discount`, `Money`, `THRESHOLD`, `discountsApplied`) — no Foo/Bar/tmp; package `org.acme.effectiveness` is the house convention, not a placeholder lift.
- Every public type carries a one-line purpose Javadoc; the SpotBugs exclude file is empty *with a reason* (good hygiene model).

## 5. Prose <-> code fidelity + originality — FIX (one MAJOR)
- **Originality/attribution: PASS.** Bespoke domain (`Discount`/`Money`) with chapter-specific Javadoc; not a copied/reskinned JaCoCo or PITest quickstart. No verbatim upstream sample, so no attribution comment is owed.
- **Tool facts trace to pin: PASS.** JaCoCo 0.8.15 and PITest 1.25.3 match SOURCE-PIN §3 (0.8.16 deviation flagged in `09-flags/48_jacoco_pin_0816_unpublished.md`); GAVs, goals, the `check` rule model (element/counter/value/minimum/maximum), and the mutator names (CONDITIONALS_BOUNDARY, MATH, returns family, VOID_METHOD_CALL, NEGATE_CONDITIONALS) all match what PITest actually generated (15 mutations across exactly those operators).
- **Headline figures reproduced and accurate:** full suite = 15 mutations / 13 killed = **87%**, line coverage **17/17 = 100%**; strong test alone reproduces the same 87% + 100% (confirms the prose's invariance claim that the strong test does the killing while line coverage is identical to the weak test). JaCoCo CSV: Discount 0 branches missed / 8 covered, 0 lines missed / 17 covered — so the BRANCH `COVEREDRATIO >= 0.90` + `MISSEDCOUNT max=0` gate passes legitimately.
- **MAJOR FIX — wrong operator inside the displayed `under-test` comment.** `Discount.java:49` (displayed region):
  `if (quantity < THRESHOLD) {       // CONDITIONALS_BOUNDARY mutates >= to > here`
  The operator on that line is `<`, not `>=`. CONDITIONALS_BOUNDARY on `<` mutates `<` -> `<=` (it does not touch a `>=` that is not present). The comment contradicts the line it annotates and the prose two lines above the include (line 87: "boundary (`qty >= 10`)" and "`>=` -> `>`"), and the `strong-test` comment (`DiscountTest.java:33`, also displayed) repeats "the `>=`/`>` CONDITIONALS_BOUNDARY mutant." A reader copying this code is told the line contains `>=` when it contains `<`. Behaviour is correct and a CONDITIONALS_BOUNDARY mutant on the line *is* killed by the strong test, so this is a teaching-accuracy defect, not a logic bug — but it is in a *displayed* region, so it must be fixed. **Two clean fixes (builder picks one):** (a) make the comment match the code — `// CONDITIONALS_BOUNDARY mutates < to <= here` and align both `strong-test` comments + prose line 87 to describe the `<` form; or (b) make the code match the prose — rewrite as `if (quantity >= THRESHOLD) { ...discount...; return ...; } return unitPrice;` so the displayed `>=` matches every comment and the prose. Option (b) keeps the prose's `qty >= 10` framing intact and is the smaller cross-surface change.
- **Snippet mechanics: PASS.** All four displayed regions (`under-test`, `weak-test`, `strong-test`, `jacoco-check`) are brace- and paren-balanced, <=9 content lines (6/6/4/5), not mid-statement fragments, and extract cleanly via `extract_snippet.sh`. The include markers in the draft match the four tags.

## 6. Neutrality in code — PASS
- Banned-phrase scan (better than / superior / unlike X / the problem with / beats / gold standard / state of the art / outperforms / dominates) across code, config, and README: **no hits**, and specifically **none inside any displayed region**. The "gold standard"/"state of the art" self-description appears only in the prose, correctly attributed to PITest as quoted self-description, not crowning.

---

## Findings table

| Severity | File:line | Issue | Fix |
|---|---|---|---|
| MAJOR (FIX) | `src/main/java/org/acme/effectiveness/Discount.java:49` (displayed `under-test`) | Inline comment says "CONDITIONALS_BOUNDARY mutates `>=` to `>` here" but the line's operator is `<`. Contradicts the line, the prose (draft:87), and the `strong-test` comment (DiscountTest.java:33). Misleads a reader copying the code. | Align comment to code (`<` -> `<=`) **or** flip the condition to `quantity >= THRESHOLD` so the displayed `>=` matches all comments + prose. |
| NIT | draft:87 | "JaCoCo reports ~100% line coverage" — the tilde is honest; whole-class Discount line coverage is exactly 100%. No change required; noted only because the isolated-weak PITest line-coverage pass reads 13/17 (it excludes guard lines), which a careful reader might cross-check. | Optional: keep the tilde; no action. |

## Build / lint result (reproduced by reviewer)
- `mvn -Pquality verify`: **BUILD SUCCESS** — 12 tests, 0 failures; "All coverage checks have been met"; "0 Checkstyle violations"; SpotBugs clean; **0 `[WARNING]`**.
- `mvn -Ppitest ... mutationCoverage` (full): **BUILD SUCCESS** — 15 mutations, 13 killed (**87%**), line coverage 17/17 (100%), threshold 80 met.
- `mvn -Ppitest ... mutationCoverage` (weak test only): **BUILD FAILURE** — 15 mutations, 5 killed (**33%**), "below threshold of 80". The failure path is real and the boundary/MATH/returns mutants survive on covered code exactly as taught.

## Verdict rationale
The tests kill mutants, not merely cover lines (strong test alone = 87% mutation score, weak test alone = 33% with the seeded faults surviving on a covered method) — the central bar for a coverage+mutation chapter, met and reproduced. Build is green and warning-clean; no security or neutrality finding; all displayed regions are well-formed and word-clean, so **no BLOCKER and FLOOR C is not blocked**. The single MAJOR is a wrong-operator claim inside a displayed comment — a real teaching defect readers will hit, so the verdict is PASS-WITH-FIXES: route the one fix to the example-builder, then re-review the touched snippet/prose.

## Learnings & pipeline suggestions
- **Add a snippet-comment/operator lint.** When a displayed `// tag::` region's *comment* names a source-level operator (`>=`, `*`, `<`), a check should assert that token actually appears on the annotated line. This catches "comment describes a different operator than the code" — exactly the MAJOR here — which `check_snippets.sh` (brace/line-count) cannot.
- **Cross-surface operator consistency.** When prose, an `under-test` comment, and a `strong-test` comment all describe the same mutated operator, drift between them is easy after an edit. Worth a reconcile-pass grep tying the three together for boundary-style examples.
- **Toolchain note for the gate runner.** This module needs a JDK on `JAVA_HOME`; the repo has Homebrew openjdk@21 (21.0.11, the pin) at `/opt/homebrew/opt/openjdk@21/...` but it is not the system default and there is no `mvnw` wrapper under `08-companion-code/`. A one-line `JAVA_HOME` export (or a committed wrapper) would let the CODE-REVIEW build run without the reviewer hunting for a JDK.

---
**ORCHESTRATOR FIX — 2026-06-27 — MAJOR RESOLVED.** The displayed `under-test` region used
`if (quantity < THRESHOLD)` while the in-line comment, the Javadoc (`qty >= THRESHOLD`), the
prose (`qty >= 10`), and the strong-test comment all describe the boundary as `>=`→`>`. The
code was the outlier — restructured to the guard form `if (quantity >= THRESHOLD) { ...discount... }
return unitPrice;` (logic-equivalent: discount at/above threshold, unchanged below). Now the
operator, comment, Javadoc, prose, and CONDITIONALS_BOUNDARY mutant all agree. Rebuilt green:
0 Checkstyle, BUILD SUCCESS, tests pass (qty==9 / qty==10 boundary unchanged); region 6 lines,
balanced. **Verdict → PASS.**
