# Chapter 18 — Teaching the Build Your Rules (`custom-rules-codegen-lombok`)

One house invariant — *money is a dedicated value type, never a raw `double`/`float`* — encoded five
ways over five different artifacts, plus a codegen comparison for the boilerplate those conventions
create. It maps to dossier key `38_custom_rules_codegen_lombok` (folding key 40). It is a child module of
the companion-code reactor; it adds no version literals beyond the pinned analysis-library coordinates and
inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

Every custom rule shares one shape — *select* the elements of interest, *predicate* whether each breaches
the invariant, *report* the breach, *register/gate* it so a violation fails the build. What changes is the
*artifact* each reasons over, which fixes what the rule can see. The module realizes that shape five ways,
using only dependencies pinned in `SOURCE-PIN.md`:

| Realization | Artifact | Where in the code | Dependency |
|---|---|---|---|
| Hand-written runtime guard | a live call | `MoneyGuards.of` | JDK only |
| Record compact constructor | the type itself | `Money` | JDK only |
| Error Prone-style declarative fence | the caller, at compile time | `LegacyMoneyFactory.fromDouble` (`@RestrictedApi`) | `error_prone_annotations` 2.36.0 |
| Custom ArchUnit predicate + condition | the imported class graph | `MoneyArchRules`, `MoneyArchTest` | `archunit` 1.4.2 |
| Reflective API inspector | declared public members | `MoneyApiInspector` | JDK only |

The reflective inspector is the runnable stand-in for a Checkstyle / PMD / SpotBugs custom check: those
analyzers' plugin-authoring SDKs are not `SOURCE-PIN.md` rows, so the chapter teaches their authoring APIs
in prose and this module shows the same four steps over an artifact that needs no unpinned dependency (see
*Scoped out*, below).

The codegen half compares the boilerplate a value type generates: `HandWrittenMoney` writes the
constructor, accessors, `equals`, `hashCode`, and `toString` by hand (with the classic latent-bug risk),
while the `record` `Money` has the compiler derive all of it with none. A test asserts the two agree
value-for-value — the correctness claim a generator makes, checked rather than assumed. The new-file
processors (AutoValue, Immutables, MapStruct) and Lombok are other approaches to the same problem; none is
a `SOURCE-PIN.md` row, so they are described in the chapter prose and not depended on here.

## Build and run

Prerequisites: the **Java 21** baseline from `SOURCE-PIN.md` (anchor LTS; built and verified on JDK
21.0.11), driven by the committed Maven wrapper at the companion-code root.

```
# with the static-analysis gate (Checkstyle + SpotBugs) and the full test suite
mvn -B -Pquality -f pom.xml clean verify
```

A green run reports `Tests run: 14`, `You have 0 Checkstyle violations`, and `BugInstance size is 0`.

## The failure path

The seeded breach `legacy.LegacyOrderLine` exposes price as a raw `double`, exactly the representation the
invariant forbids, and every realization reacts to it: the reflective inspector and the source-style rule
report it, the ArchUnit condition throws `AssertionError`, the hand-written guard refuses to build it, and
an Error Prone build (described, not wired) flags the fenced caller. The honest edge is shown in code too —
the reflective inspector is silent on a `double` reached only through an erased generic, because each rule
sees only its own artifact. That is the chapter's reason layering (Chapter 17) matters: no single rule is a
complete fence.

## Observability surface

`MoneyPolicyHealth` exposes a readiness probe over the loaded policy and a running count of reported
breaches — a custom rule's natural observable signal is its finding stream, the same place a stock
analyzer's report shows up for a dashboard to consume (Chapter 88).

## Externalized config

`src/main/resources/money-policy.properties` carries a `dev` profile (a breach is a `WARNING`, the
adoption on-ramp) and a `prod` profile (a breach is an `ERROR` that fails the gate). The rules read their
severity and field-name pattern from it rather than embedding them, mirroring the separation the analyzers
keep between a rule's logic and its configuration.

## Scoped out (recorded, not invented)

- **Custom-rule authoring SDKs** for Checkstyle / PMD / SpotBugs, and the Error Prone `BugChecker` /
  Refaster build, are not `SOURCE-PIN.md` rows. Rather than introduce unpinned GAVs and a fragile off-pin
  toolchain, the module realizes the same select→predicate→report→gate shape with pinned-or-JDK forms (the
  reflective inspector and the `@RestrictedApi` fence). The prose teaches the analyzer authoring APIs by
  identity. Flag: `09-flags/38_codegen_tools_not_pinned.md`.
- **Lombok and the new-file processors** (AutoValue / Immutables / MapStruct) are likewise unpinned and
  stay prose-only. Flags: `09-flags/38_codegen_tools_not_pinned.md`, `09-flags/40_lombok_and_codegen_tools_not_pinned.md`.

No realization is crowned: each states its strongest case and its hardest limit, and which tool should own
a given rule is Chapter 17's routing question, not this chapter's.
