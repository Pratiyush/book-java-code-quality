# FLAG — key 40: Lombok & codegen tools have no SOURCE-PIN rows

**Raised:** 2026-06-15 (key 40 research — compile-time codegen & the Lombok debate)
**Severity:** material (the chapter's primary comparison targets are unpinned)

## Issue
Key 40's primary comparison targets — **Lombok**, **AutoValue**, **Immutables**, **MapStruct**
(+`lombok-mapstruct-binding`) — have **no rows in `SOURCE-PIN.md` §2 (static analysis / linters /
formatters)**. Lombok in particular is the chapter's central subject and is absent. Until pinned, every
version number, GAV coordinate, supported-JDK matrix, and `--add-opens` flag set drawn from these tools is
`⚠ UNVERIFIED`.

## What IS verified (from live docs, identity only)
- Lombok SPI/internals: `lombok.launch.AnnotationProcessorHider$AnnotationProcessor`,
  `lombok.core.AnnotationProcessor`, `lombok.javac.apt.LombokProcessor`, `ShadowClassLoader` (`.SCL.lombok`),
  `HandlerLibrary`/`HandleX`/`@HandlerPriority` (`projectlombok.org/contributing/lombok-execution-path`).
- Lombok annotation roster (stable + experimental) from `/features/all` + `/features/experimental/all`.
- `delombok` (`projectlombok.org/features/delombok`); `@lombok.Generated` + `lombok.addLombokGeneratedAnnotation`.
- AutoValue (`@AutoValue`), Immutables (`@Value.Immutable`), MapStruct (`@Mapper`) generate **new files**.

## What is NOT verified (needs pin)
- Exact Lombok/AutoValue/Immutables/MapStruct versions + GAV coordinates.
- Lombok supported-JDK matrix; experimental-vs-stable annotation boundary (moves per version).
- `lombok-mapstruct-binding` version; JaCoCo ≥ 0.8.1 / Lombok ≥ 1.16.20 thresholds.

## Action
Add SOURCE-PIN §2 rows for `org.projectlombok:lombok` (`projectlombok.org` +
`github.com/projectlombok/lombok`), `com.google.auto.value:auto-value`, `org.immutables:value`,
`org.mapstruct:mapstruct`(+`-processor`, `-binding`) at `/pin-source`; re-trace key 40 atoms after.
