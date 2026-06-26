# Chapter 26 — Wrong in Both Directions (`how-static-analysis-works`)

One buildable module of analyzer *targets*: small, runnable shapes that show what each rung of the
chapter's technique ladder reasons about. It is a child module of the companion-code reactor; it adds
no version literals beyond the one pinned SpotBugs-annotations GAV and inherits the runtime and
test-library pins from the aggregator.

## What it demonstrates

| Move (ladder rung) | Analyzer target | Technique that catches it | Where in the code |
|---|---|---|---|
| 1 — AST / pattern match | empty `catch` (swallowed exception) | tree-shape match (PMD `EmptyCatchBlock`, Checkstyle `EmptyBlock`) | `AstSmellDemo.parseQuantity` |
| 2 — symbols & types | `List<Long>.contains(int)` | type-resolved check (Error Prone `CollectionIncompatibleType`; SpotBugs `GC_UNRELATED_TYPES`) | `TypeMisuseDemo.catalogueHas` |
| 3 — control-/data-flow | unclosed stream | bytecode data-flow (SpotBugs `OS_OPEN_STREAM`) | `ResourceLeakDemo.readFirstLine` |
| 4 — taint tracking | request value → query sink | source→sink taint flow (SAST engines) | `TaintFlowDemo.lookupTainted` |
| control | per-site suppression with a reason | `@SuppressFBWarnings(value, justification)` | `SuppressionDemo.snapshot` |

Each target ships beside the form that resolves it: `parseQuantitySafely`, `containsQuantity`,
`ResourceReader.readFirstLine`, and `TaintFlowDemo.lookupSafe`. The pairing is the chapter's thesis —
a technique sees a shape, and the same technique goes quiet once the shape is gone.

## A module that dogfoods its own subject

The module is held to the `quality` gate it describes (Checkstyle + SpotBugs) and stays green while
still showing every smell. Two targets the gate genuinely raises are the Move-2 **type-misuse**
(`GC_UNRELATED_TYPES` — SpotBugs reaches by type resolution the same conclusion Error Prone draws) and
the Move-3 **data-flow leak** (`OS_OPEN_STREAM` — "may fail to close stream"). The detectors stay fully
enabled and each of those two counter-examples carries a single reviewed, load-bearing suppression in
`config/spotbugs/spotbugs-exclude.xml`, named narrowly by class, method, and pattern, with a reason and
a pointer to the test that proves it — the chapter's "suppress with a reason, never disable a detector"
discipline.

The remaining targets sit below the gate's chosen point and need no suppression: the empty catch carries
a comment the house `EmptyBlock` check tolerates, and the taint flow runs into an in-module sink base
SpotBugs does not model as JDBC injection. That silence is the chapter's **false-negative** half — a
clean scan is the absence of findings the tool's chosen point can see, not a proof of correctness.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 26_how_static_analysis_works -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 26_how_static_analysis_works -am verify

# or standalone, from this module directory
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings (the one
genuine finding is the reviewed, reasoned suppression).

## The failure path

The taint sink (`CatalogQuery`) degrades to an empty result for an unknown category rather than
throwing, so a request for a missing category is a defined, benign outcome rather than a crash. The
data-flow counter-example (`ResourceLeakDemo`) is the failure made visible the other way: it leaks a
file handle on every call, which is the defect the build's data-flow analysis reports — fixed by the
try-with-resources form in `ResourceReader`. Both halves are exercised by `StaticAnalysisDemoTest`.

## The two suppression controls

The chapter names two false-positive controls, and the module shows both: the **filter file**
(`config/spotbugs/spotbugs-exclude.xml`) suppresses the leak finding by location with a reason, and the
per-site **`@SuppressFBWarnings(value, justification)`** annotation on `SuppressionDemo.snapshot`
records the reason next to the code. Neither disables a detector — that is the discipline the chapter
argues for.
