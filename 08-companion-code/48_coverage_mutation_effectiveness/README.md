# Chapter 48 — The Number That Feels Like Quality (`coverage-mutation-effectiveness`)

One behaviour-rich method put under **both** measures the chapter contrasts, so the gap between them
is something the build demonstrates rather than asserts. `Discount.apply` has a quantity boundary, an
arithmetic expression, and an early return. Two test classes act on it: a **weak** test that executes
every line yet asserts only that the result is non-null, and a **strong** test that pins the boundary
and the computed value. Coverage records that the lines *ran*; mutation testing records whether a
test would *notice* if they were wrong. They are a complementary pair — neither is crowned the test-
quality measure, because coverage alone flatters an assertion-free suite and mutation score alone can
be gamed and is unachievable at 100%.

This is a child module of the companion-code reactor: it sets `<parent>`, carries no version literal
of its own, and inherits the runtime and the JUnit/AssertJ pins from the aggregator. JaCoCo, PITest,
and the PITest JUnit 5 plugin are declared here because the aggregator does not manage them.

## What it demonstrates

| Idea | Where in the code | Tag |
|---|---|---|
| The method under both tools: boundary + arithmetic + early return | `Discount.apply` | `under-test` |
| The coverage-padding test: full line coverage, no behaviour checked | `DiscountWeakTest` | `weak-test` |
| The strong test: same lines, real assertions, mutants killed | `DiscountTest` | `strong-test` |
| The JaCoCo gate reads **BRANCH**, not only LINE | `pom.xml` | `jacoco-check` |

## Two measures, two places in the build

**Coverage — JaCoCo, in the default `verify`.** `prepare-agent` binds the agent into the test JVM
(via the `argLine` property — this module sets no `<argLine>` of its own, which would clobber it and
silently report 0%); `report` writes the HTML/XML report; `check` gates on the **BRANCH** counter. A
method can reach 100% line coverage with a whole branch untested, because a line counts as covered
when one instruction on it ran, so the gate reads BRANCH. Two limits sit on the rule: a
`COVEREDRATIO` minimum (the familiar "at least X%") and a `MISSEDCOUNT` maximum of zero (the ratchet-
friendly "no uncovered branch"). *Which* policy a real project adopts — whole-codebase percentage,
new-code ratchet, or a trend rather than a gate — is a deliberate choice the CI part owns; this
module shows only the mechanism both policies use.

**Mutation — PITest, behind the `pitest` profile.** Running the covering tests once per mutant is
orders of magnitude slower than a unit-test run, so the stage is opt-in and belongs in a separate CI
run, never the inner loop. The `pitest-junit5-plugin` is required for PITest to run JUnit Jupiter
tests; without it the tests silently report no coverage.

> **Version note.** `SOURCE-PIN.md` §3 pins JaCoCo **0.8.15**; 0.8.16 is not published on
> Maven Central (the real latest is **0.8.15**). This module builds against **0.8.15**, which covers
> the anchor JDK 21 (JaCoCo support since 0.8.11) and the forward JDK 25 (since 0.8.14). The deviation
> is recorded in `09-flags/48_jacoco_pin_0816_unpublished.md` for a deliberate re-pin. PITest is on
> the pinned **1.25.3**; the `pitest-junit5-plugin` version is the matrix atom flagged in
> `09-flags/48_pitest_junit5_plugin_matrix_verify_at_pin.md`.

## Build and run

```
# default build: compile + tests + JaCoCo agent/report/check (the BRANCH gate)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify

# the slow mutation stage (opt-in): writes target/pit-reports/
mvn -B -Ppitest test org.pitest:pitest-maven:mutationCoverage
```

A green `-Pquality` run reports tests passing, all coverage checks met, zero Checkstyle violations,
and zero SpotBugs findings. The mutation run over the full suite kills most mutants and meets the
`mutationThreshold`; scoped to the weak test alone it leaves mutants surviving and fails the gate —
the chapter's thesis made tactile (see below).

## The failure path

The explicit failure path is the chapter's whole point demonstrated in code: a test can cover a line
without testing it. Scope the mutation run to the weak test only —

```
mvn -B -Ppitest test org.pitest:pitest-maven:mutationCoverage \
    -DtargetTests=org.acme.effectiveness.DiscountWeakTest \
    -DtargetClasses=org.acme.effectiveness.Discount
```

— and the boundary, arithmetic, and returns mutants on `apply` **survive** on lines the weak test
executes, dropping the mutation score below the `mutationThreshold` and **failing the build**. Add the
strong test back (the default `targetTests`) and the same mutants are **killed**, while line coverage
does not move. The honest edge: gating coverage on INSTRUCTION or LINE alone would let a buggy variant
pass, and chasing a 100% mutation score is futile because equivalent mutants cannot be killed by any
test. `Discount` and `Money` each also guard their inputs (a negative quantity, an out-of-range rate,
a null or negative amount, a blank currency), and those guards are the value-level failure path the
strong tests exercise.

## Observability surface

`Discount.discountsApplied()` exposes a running count of discounted prices computed since startup — a
small, illustrative seam the later observability chapter builds on. The two reports are the module's
other observability surfaces: the JaCoCo HTML report (`target/site/jacoco/index.html`, line green but
a branch diamond where a decision path is untested) pairs with the PITest report
(`target/pit-reports/`, a surviving-mutant red line under the weak test, then killed/green under the
strong one) as the two-axis contrast — execution on one axis, detection on the other.
