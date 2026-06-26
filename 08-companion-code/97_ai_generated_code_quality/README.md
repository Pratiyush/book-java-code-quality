# Chapter 41 — The Draft That Looks Like a Deliverable (`ai-generated-code-quality`)

The Part XII umbrella chapter's stance in code: **AI-generated code is a draft, not a deliverable** — it
goes through the same quality gate as human code, plus a few AI-specific checks. It is a child module of
the companion-code reactor; it adds no version literals beyond the one pinned SpotBugs-annotations GAV
and inherits the runtime and test-library pins from the aggregator. Maps to `NN_slug`
`97_ai_generated_code_quality`.

The content here is **factual engineering guidance, not legal or security sign-off**, and the AI claims
in the chapter prose are **dated, attributed snapshots** — model capability moves too fast for any figure
to stay true. No statistic is embedded in this code: the code demonstrates the structural mechanisms
(which do not date), not the numbers (which do).

## What it demonstrates

Two demonstrations, both on the storefront domain.

### (a) An AI draft goes through the security gate

| Shape | Role | OWASP / CWE |
|---|---|---|
| `AiDraftedLookup` | AI draft: string-concatenated SQL (vulnerability inheritance) | A03 / CWE-89 |
| `ReviewedLookup` | the reviewed fix: bound `PreparedStatement` parameter | — |

The AI draft reproduces an injection pattern well-represented in public training data. It is a deliberate
teaching counter-example, exercised only for behaviour by a test and never wired into a running path. The
reviewed fix binds the value as data. The chapter's thesis shows up in the build: the same SpotBugs rule
fires on the AI-drafted query (`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`) — **the gate does not care who
wrote the code.**

### (b) The tests-from-code trap, beside a spec-derived test

`OrderTotals.payableTotal` has a free-shipping boundary and a fee addition — exactly the kinds of thing a
mutation operator changes. Two tests show the contrast the chapter draws:

- `AiTestGeneratedFromTheCode` runs every line of `payableTotal` (full line coverage) but asserts only
  that the result is non-null. Run a mutation tool (Chapter 23 / PITest) over the module with that test
  alone and the `CONDITIONALS_BOUNDARY` and `MATH` mutants **survive** — covered, executed, untested.
  That is the tests-from-code trap: a test that pins current behaviour, bugs included, instead of
  independently encoding intended behaviour.
- `SpecDerivedTest` encodes the intended behaviour from the requirement (both sides of the boundary, the
  exact paid total), which **kills** those mutants.

Mutation testing is the check that exposes the hollow test. As in the Chapter 23 module, the contrast is
demonstrated here with tests and prose rather than by wiring a mutation-testing plugin into this build.

## The analyzer finding (the chapter's thesis, in code)

The module dogfoods the chapter: every shape is held to the same `quality` gate the chapter describes, and
the build is green. One deliberate counter-example fires at the gate and carries a **narrow, load-bearing,
reasoned SpotBugs suppression** (the Chapter 16 discipline — suppress with a reason, never disable a
detector), naming the class, the verified pattern, and the proving test:

- `AiDraftedLookup.findIdsByEmail` → `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`.

The reviewed fix (`ReviewedLookup`) carries no finding — that is the point made by the build.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 97_ai_generated_code_quality -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 97_ai_generated_code_quality -am verify
```

A green run reports the test suite passing, zero Checkstyle violations, and zero unsuppressed SpotBugs
findings. The `Java 21` baseline and the tool versions trace to `00-strategy/SOURCE-PIN.md` (anchor LTS
Java 21; OWASP Top 10:2025).

## Externalized config

`src/main/resources/application.properties` externalizes the gate's tunables — the request-body cap and
whether provenance is required — under a `%dev` and a `%prod` profile, read by `AiReviewProfile` and
selected with `-Dai.profile=prod` (default `dev`). The dev profile keeps the lower-friction posture so the
local loop stays fast; the prod profile requires an AI contribution's provenance to be attested, matching
the chapter's stance that an AI draft is untrusted until verified.

## The failure path

`AiReviewGate.acceptLookup` is the explicit failure path: a `null`, oversized, malformed, or (in the prod
posture) provenance-less contribution is turned away with a `RejectedContributionException` carrying a
stable reason code (`body-too-large`, `malformed-body`, `provenance-missing`) rather than being accepted.
Rejecting an AI contribution the gate cannot make safe is the chapter's stance made concrete, and
`AiGeneratedCodeQualityTest` drives every branch.

## Observability surface

`AiReviewGate.isReady(Connection)` is a readiness probe that reports ready only when the wired reviewed
lookup runs end to end, and `rejectedContributionCount()` exposes a running count of contributions turned
away by the gate's checks. `OrderTotals.freeShippingGranted()` is a second small counter — illustrative
seams the later observability chapter builds on.
