<!--
Dossier key: 12 (owner) + folds 16 + 18 — per 01-index/FINAL_INDEX.md Ch 10
Slug: 12_error_handling_exceptions
Part / arc position: Part II — Writing Quality Java, Chapter 10 (Part II = Ch 5-12; Ch 12 closes it)
Companion module: 08-companion-code/12_error_handling_exceptions/ — ✅ EXAMPLE-BUILD = GREEN (mvn -B -Pquality clean verify on JDK 21.0.11; 12 tests pass, 0 Checkstyle, 0 SpotBugs — see 12_error_handling_exceptions_EXAMPLE.md, 2026-06-26). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20 (re-traced 2026-06-27 against the corrected pin + the green companion build). Sources: JLS SE 21 §11 (Throwable hierarchy, checked catch-or-specify §11.2), §14.20/§14.20.3 (try/catch/finally, try-with-resources basic §14.20.3.1/extended §14.20.3.2), §14.10 (assert); JEP 358 (Helpful NullPointerExceptions, Release 14, default-on since JDK 15 — openjdk.org JEP 358, confirmed), JEP 213 (effectively-final resources, Java 9), JEP 421 (deprecate finalization, Java 18), JEP 395/409/441 (records/sealed/pattern-switch GA at 21), JEP 453 (StructuredTaskScope, PREVIEW — ⚠ AHEAD-OF-PIN); JDK 21 API (AutoCloseable not-idempotent vs Closeable idempotent verbatim, Throwable.addSuppressed/getSuppressed, Cleaner, Objects.requireNonNull/checkIndex); Effective Java 3e Items 8/9/49/69–77; Jakarta Validation 3.1 (Final 2024-03-28; @NotNull/@Size/@Valid; Validator/ExecutableValidator; groups; API jakarta.validation-api:3.1.1 — built into the companion module); a constraint-engine implementation (e.g. Hibernate Validator + Jakarta EL) is NOT pinned — ⚠ impl GAV @pin; OWASP Input Validation Cheat Sheet; tool rules Sonar java:S112/S1166/S2095, PMD EmptyCatchBlock/AvoidCatchingGenericException/PreserveStackTrace/CloseResource, SpotBugs REC_CATCH_EXCEPTION/OS_OPEN_STREAM/OBL_*, Checkstyle IllegalCatch/IllegalThrows, Error Prone DeadException/MustBeClosed/StreamResourceLeak/Finalize, Sonar java:S5128.
⚠ verify-at-pin (genuinely unconfirmable here, tracked in 09-flags/): analyzer rule IDs/defaults at each tool's pinned version (09-flags/12_jep358_default_level_and_rule_ids.md — note: the JEP 358 delivered-14/default-on-15 fact in that flag is now web-verified against openjdk.org JEP 358 + JDK-8233014 and is no longer pending); Effective Java verbatim quotes/page numbers (named-book text); the unpinned constraint-engine impl/EL GAVs; Sonar java:S5128 exact title/type (rules.sonarsource.com unreachable at scan — 09-flags/18_sonar_s5128_title_unverified.md). ⚠ AHEAD-OF-PIN: StructuredTaskScope (preview, JEP 453).
DRAFT v1 — gates manual; throwable-decision + item-to-rule crosswalk + two-paths-validation + lifecycle-card shapes; EXAMPLE-BUILD GREEN (built — see _EXAMPLE.md).
-->

# When Things Go Wrong

*Exceptions, resource cleanup, and not trusting inputs: the failure paths done right · Part II*

> The happy path is where the program does its job. The failure paths are where it earns trust.

## Hook

```java
try {
    return repository.load(id);
} catch (Exception e) {
    // TODO
}
```

Three lines, three separate failures. The `catch (Exception e)` swallows everything: a recoverable "not found," an unrecoverable programming bug, even the `InterruptedException` that should restore a thread's interrupt status. All of it flattens into one undifferentiated nothing. The empty body discards the stack trace, so when the method silently returns `null` and something NPEs three frames away, there is no thread to pull. And the comment promises a fix that will never come. This is not error handling; it is error *hiding*, and it is the single most common quality defect in production Java.

This chapter takes on the failure paths: how to signal an error (exceptions, and the choice between checked and unchecked), how to release the resources a failing path still has to clean up (try-with-resources), and how to stop bad input at the door before it can fail at all (defensive coding and validation). The three are one subject (*what the code does when the happy path does not happen*), and the discipline across all three is the one Part II keeps repeating: make the failure explicit, fail fast, and never let it disappear silently.

## Overview

**What this chapter covers**

- The exception model: the `Throwable` hierarchy, the compile-time checked rule, and the *Effective Java* decision: checked for recoverable conditions, unchecked for programming errors.
- The nine *Effective Java* exception items (69–77) as a crosswalk to the analyzer rules that enforce each.
- **Resource management:** try-with-resources and `AutoCloseable`, reverse-order close, *suppressed* (not masked) exceptions, and the `Cleaner` safety net.
- **Defensive coding:** guard clauses and the JDK primitives (`Objects.requireNonNull`, `checkIndex`, `assert`), plus declarative **Jakarta Validation** at the boundary: two complementary paths.

**What this chapter does NOT cover.** The analyzer internals (Part IV), concurrency error handling and structured concurrency (Part III, still preview), the security framing of injection and deserialization (the security part covers the *attack/defense*; this chapter covers the *technique*), and exception translation across architectural layers in depth (the architecture part).

*A visible error is a fixable error.* Every move in this chapter (the right throwable kind, the documented `@throws`, the preserved cause, the suppressed-not-masked close exception, the fail-fast guard) exists to keep failure visible instead of letting it go quiet.

## How it works

Two figures anchor the chapter. Figure 10.1 lays out the `Throwable` hierarchy that the exception-model section walks: where the checked/unchecked split falls, and which branch the compile-time catch-or-specify rule governs.

![Figure 10.1 — The Throwable — JLS SE 21 §11 hierarchy · §11.2 catch-or-specify rule · Effective Java](../../05-figures/12_error_handling_exceptions/fig12_1.png)

*Figure 10.1 — The Throwable — JLS SE 21 §11 hierarchy · §11.2 catch-or-specify rule · Effective Java*

Figure 10.2 sets up the resource-management section: it contrasts the suppressed exception that try-with-resources records against the masked exception the older `finally`-with-close idiom loses.

![Figure 10.2 — try-with-resources: suppressed vs masked exceptions — JLS SE 21 §14.20.3 · Effective Java](../../05-figures/12_error_handling_exceptions/fig12_2.png)

*Figure 10.2 — try-with-resources: suppressed vs masked exceptions — JLS SE 21 §14.20.3 · Effective Java*


### The exception model: the hierarchy and the decision

Java's error channel is the `Throwable` hierarchy (JLS §11): `Throwable` splits into `Error` and `Exception`, and `Exception` splits into checked subclasses and `RuntimeException`. The dividing line that matters is *checked vs unchecked*: a method that can throw a *checked* exception must either catch it or declare it in a `throws` clause (the compile-time "catch or specify" rule, JLS §11.2). `RuntimeException` and `Error` are exempt. That compiler rule is what makes "checked" load-bearing: it is the first gate on the error model.

*Effective Java* Item 70 gives the decision rule, and it is the spine of this section:

| Kind | Use it for | Example |
|---|---|---|
| **Checked** (`Exception` − `RuntimeException`) | conditions the caller **can reasonably recover from** | a file that may not exist, a remote call that may time out |
| **Unchecked** (`RuntimeException`) | **programming errors** — broken preconditions | `IllegalArgumentException`, `IllegalStateException` |
| **Error** | the JVM's, by convention | `OutOfMemoryError` — do not throw or subclass (bar `AssertionError`) |

Item 70's summary directive: "throw checked exceptions for recoverable conditions and unchecked exceptions for programming errors. When in doubt, throw unchecked exceptions."

The companion module puts both kinds in one method: a broken precondition fails fast with an unchecked exception, while a recoverable store failure is a declared checked one:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/OrderService.java#checked-vs-unchecked -->

> **CONCEPT** *The failure model is a contract.* The set and shape of exceptions a module exposes is part of its API, exactly like its return types (Chapter 7). A caller plans recovery around the checked exceptions a module declares and debugs around its unchecked ones. Choosing the wrong kind, or swallowing one, breaks the contract as surely as returning the wrong value.

### The nine exception items, and the rule that catches each

*Effective Java* Chapter 10 (Items 69–77) is the idiom canon, and almost every item maps to a static-analysis rule. These properties are mechanically checkable, not matters of taste.

| Item | The rule in one line | Caught by (cited to its own tool) |
|---|---|---|
| 69 | exceptions only for exceptional conditions, not control flow | — (design) |
| 70 | checked = recoverable, unchecked = programming error | Checkstyle `IllegalThrows`; Sonar `java:S112` (no raw `Throwable`/`Exception` throws) |
| 71 | avoid *unnecessary* checked exceptions | — (design; consider `Optional`, Chapter 9) |
| 72 | favor standard exceptions (`IllegalArgumentException`, …) | PMD `AvoidThrowingRawExceptionTypes` |
| 73 | throw exceptions appropriate to the abstraction (translate, chain the cause) | PMD `PreserveStackTrace`; Sonar `java:S1166` |
| 74 | document every exception with `@throws` | — (Javadoc; doclint, Chapter 7) |
| 75 | include failure-capture info in the message | — (review) |
| 76 | strive for failure atomicity (failed call leaves state unchanged) | — (design) |
| 77 | do not ignore exceptions | PMD `EmptyCatchBlock`; SpotBugs `DE_MIGHT_IGNORE`; Checkstyle/Sonar empty-catch |

The hook violates three at once: it catches too broadly (Item 70 / Checkstyle `IllegalCatch`), it ignores the exception (Item 77 / `EmptyCatchBlock`), and by discarding the cause it fails Item 73 (`PreserveStackTrace`). Item 71 also ties back to the previous chapter: if recovery is possible, first consider returning an `Optional`; only throw a checked exception if `Optional` gives the caller too little information.

Item 73 done right is exception translation that chains the cause; the companion module's repository translates a low-level store failure into a domain exception and keeps the original as the cause:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/OrderRepository.java#exception-translation -->

### Fail fast: detect the violation at its cause

Fail-fast means detecting a broken contract as close to its cause as possible and throwing immediately, rather than letting a corrupted value travel to a distant, confusing failure. In Java that is guard clauses at method entry: `Objects.requireNonNull(x, "x")` for null, `Objects.checkIndex` for range, and an explicit `throw new IllegalArgumentException(...)` for everything else, following Item 72's exception conventions. And when a null still slips through, **JEP 358** helpful NullPointerExceptions name the exact null expression, so even the failure that was not caught early is diagnosable in one read. (JEP 358 was delivered in JDK 14 behind `-XX:+ShowCodeDetailsInExceptionMessages` and turned on by default since JDK 15, so it is active at the Java 21 anchor with no flag, per openjdk.org JEP 358. The full defensive-coding treatment is its own section below.)

### Modern error models: the typed alternative

Exceptions are not the only way to model failure. A closed set of outcomes can be a *value*: a `sealed` interface (JEP 409, Java 17) with record (JEP 395) cases, deconstructed exhaustively by pattern matching for `switch` (JEP 441, GA in Java 21). The compiler rejects a `switch` that misses a permitted case. This is a `Result`/`Either`-style model, presented here as an *approach alongside* exceptions, not a winner. It puts every failure in the type, so every call site must destructure it. That is precise but verbose, and it interoperates awkwardly with the exception-throwing libraries (JDBC, Spring) most code crosses. These features are post-2018, so they are not *Effective Java* recommendations; they are the modern modelling option, and the Limitations section weighs their trade-offs. (Structured concurrency's `StructuredTaskScope`, which streamlines concurrent error handling, is **preview** across 21→25; it belongs to Part III, not the anchor.)

Because the interface is `sealed`, the compiler knows the full set of cases, and a `switch` that omits one fails to compile. The trade-off this buys is concrete: handling every outcome becomes a compile-time obligation rather than a run-time one, at the cost of threading the type through every call site. The companion module models that closed set as a sealed `Result` with record cases:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/Result.java#result-model -->

## Deep dive: resources and inputs, the two failure paths code forgets

Two failure paths are systematically under-handled: the resource a failing block still has to close, and the input a method should never have trusted. Both fold into this chapter because both are about *what happens when the happy path does not*.

### Resource management: deterministic cleanup

The garbage collector reclaims *memory*; it does not promptly close *handles*: files, sockets, JDBC connections, locks. Leaving those to the GC means file-descriptor exhaustion and connection-pool starvation, which surface as intermittent production failures, never compile errors. The mechanism for deterministic release is **try-with-resources** (JLS §14.20.3) over `AutoCloseable`:

```java
try (var in = Files.newInputStream(path);
     var out = Files.newOutputStream(dest)) {
    in.transferTo(out);
}   // close() called on out, then in — reverse order — on every path
```

The companion module makes the close order observable, with two channels that record when they close:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/ReceiptWriter.java#twr-basic -->

Three semantics make this more than syntactic sugar. Resources close in **reverse order** of initialization (LIFO). `close()` runs on **every** path: normal completion, a `try`-block throw, or a later resource's failed init. The critical fix over the old `try`/`finally` idiom: if the body throws E1 and a `close()` then throws E2, **E1 propagates and E2 is suppressed** (attached via `Throwable.addSuppressed`, readable via `getSuppressed`). The old `finally`-with-close pattern did the opposite: the close exception *replaced* the real one, masking the failure. *Effective Java* Item 9 ("prefer try-with-resources to try-finally") documents exactly that masking-and-scaling problem.

In the module, a failing body and a failing `close()` together leave the body's exception in charge and the close exception in `getSuppressed()`:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/ReceiptWriter.java#suppressed -->

> **CONCEPT** *The `AutoCloseable` contract (a lifecycle card).* `AutoCloseable.close()` is `void close() throws Exception` and is **not** required to be idempotent. `Closeable` (its subtype) narrows to `throws IOException` and **is** required to have no effect when called more than once (verbatim, JDK 21 Javadoc). Writing a quality `AutoCloseable`: make `close()` idempotent anyway, throw a *specific* exception or none, never throw `InterruptedException` (a suppressed one corrupts the interrupt status), and release the resource before throwing.

For objects whose `close()` might be forgotten (typically native handles), `Cleaner` (Java 9) registers a cleaning action that runs after the object becomes phantom-reachable. It is a *safety net, not a release mechanism*: it runs some time later, possibly only at JVM exit, so relying on it for prompt release reintroduces the finalizer problem. (Finalization itself is deprecated for removal, JEP 421.) The analyzers enforce the primary path: Sonar `java:S2095`, PMD `CloseResource`, SpotBugs `OS_OPEN_STREAM`, Error Prone `MustBeClosed`/`StreamResourceLeak` all flag a resource that escapes a try-with-resources.

The companion module registers such a backstop, with the cleaning action holding no reference back to the owning object:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/NativeCounter.java#cleaner-backstop -->

### Defensive coding: do not trust the input

The other forgotten path is the input that should have been rejected at the door. Defensive coding is the discipline of not trusting inputs (to a method, a constructor, an endpoint, a deserializer) and failing fast and clearly when one violates its contract. Two complementary mechanisms carry that discipline.

**Guard clauses (imperative, in-method)** are the codification of *Effective Java* Item 49: "check parameters for validity… at the beginning of the method body." The JDK ships the primitives: `requireNonNull` (check-and-assign in one expression), `checkIndex`, and the `assert` statement (for *private* methods only; assertions are disabled in production, so never use them for public-API argument checks). The record compact constructor is the canonical home for invariant guards:

```java
public record Money(long minorUnits, String currency) {
    public Money {
        Objects.requireNonNull(currency, "currency");
        if (minorUnits < 0) throw new IllegalArgumentException("minorUnits must not be negative: " + minorUnits);
    }
}
```

The companion module's `Money` puts that guard in a compact constructor, so an invalid amount can never be constructed:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/Money.java#guard-clause -->

**Jakarta Validation (declarative, annotation-driven)** is the boundary mechanism: constraints (`@NotNull`, `@Size`, `@Email`, `@Valid` to cascade) declared on fields, record components, or method parameters, then enforced by a `Validator` programmatically or by the container (Jakarta REST, Persistence) at a request boundary. A `ConstraintViolation` carries the message, the property path, and the invalid value: a structured, reusable rejection. (Jakarta Validation 3.1, Final 2024-03-28; API `jakarta.validation-api:3.1.1`. The spec is implemented by a separate constraint engine plus a Jakarta EL provider; no specific implementation version is pinned for this book; ⚠ impl GAV @pin.)

The companion module declares those constraints on a request record, cascading into each line with `@Valid`:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/OrderRequest.java#constraints -->

These are complementary, not rivals: guard clauses give local, visible, zero-dependency checks ideal for invariants and private methods; Jakarta Validation gives declarative, reusable, container-integrated checks ideal for request and DTO boundaries with structured reporting. Teams combine them: constraints at the edge, guards on internal invariants.

> **CONCEPT** *Validation is a supporting control, not the frontline defense.* OWASP is explicit: input validation should **not** be the *primary* defense against XSS or SQL injection. Parameterized queries and output encoding are. Prefer an allowlist over a denylist (denylists are circumventable and reject legitimate input like `O'Brien`), and validation must run server-side on a trusted system. The security framing is the security part's; the discipline is this one's.

Every technique here (the right throwable kind, the suppressed-not-masked close, the fail-fast guard, the boundary constraint) takes a failure that could have been silent or distant and makes it loud and local.

## Limitations & when NOT to reach for it

- **Checked exceptions are contested.** Item 71 warns against *overusing* them; they burden every caller and compose poorly with streams and lambdas (functional interfaces do not declare checked exceptions, forcing wrap-in-unchecked). When NOT to use checked: lambda/stream-facing APIs, or where the caller realistically cannot recover. Apply Item 70's trade-off, not a verdict.
- **Broad-catch rules fire on legitimate boundary handlers.** A framework boundary that *must* catch `Exception` to map it to an HTTP response is correct; suppress the rule there with a justification rather than deleting it. `PreserveStackTrace` itself has documented false positives (builder pattern, separate `initCause`).
- **`sealed`/`Result`-type modelling has costs.** It threads failure through every call site and interoperates awkwardly with exception-based frameworks; when NOT to use: across a framework/IO boundary that already speaks exceptions, or in deep chains where threading a `Result` is more noise than signal.
- **`finally` can swallow the in-flight exception** if it contains a `return` or `throw`. That is the exact bug try-with-resources' suppression was designed to avoid. Suppressed exceptions are silent by default: a `close()` failure goes into `getSuppressed()` and is invisible unless the logger prints suppressed throwables.
- **`AutoCloseable.close()` is not idempotent by contract.** A double-close is not guaranteed safe. Try-with-resources only manages resources named in its *header*; one created in the body, returned from a factory, or stored in a field still leaks (the false-negative class the linters target, imperfectly).
- **`Cleaner` gives weak timing guarantees.** Backstop only, never the primary release path.
- **Guard clauses scatter and over-guard.** Hand-written guards duplicate precondition logic; for private methods, prefer `assert` (zero cost when disabled). Avoid redundant guards on what the JVM already validates with an equally clear exception (an immediate array access already throws `IndexOutOfBoundsException`).
- **Jakarta Validation is reflection-based and runtime.** A forgotten `@Valid` silently disables a cascade. The failure is the *absence* of an error, which static analysis catches only partially (Sonar `java:S5128`; ⚠ title @pin). It needs a wired implementation plus Jakarta EL on the classpath, and reflection/EL require explicit registration under GraalVM native image.
- **JEP 358 is a diagnostic, not a defense.** It names the null only when bytecode analysis can, and can expose variable names in logs. It improves the message after the failure; it prevents nothing.

> **AHEAD-OF-PIN** `StructuredTaskScope` (structured concurrency, JEP 453) is **preview** across Java 21→25. Its API is not stable. Its home is Part III, where it is marked preview.

## Alternatives & adjacent approaches

- **`Optional` / empty returns** (Chapter 9): for "no result," often clearer than a checked exception (Item 71's ladder).
- **`Result`/`Either` libraries** (Vavr, or a hand-rolled sealed type): typed error channels for code that wants failures in the type rather than the stack (at the interop cost above).
- **Bean Validation groups and `@GroupSequence`**: partition constraints (Create vs Update) and order their evaluation, richer than a single guard, when a boundary has stateful validation needs.
- **`Cleaner` / `PhantomReference`**: GC-time backstops for native resources, never the primary path.
- **The older `try`/`finally`**: still correct, but Item 9's masking and scaling hazards make try-with-resources the default for anything `AutoCloseable`.

These layer rather than compete: guard clauses and constraints stop bad input, exceptions signal the failures that get through, try-with-resources cleans up regardless, and the typed/Optional models handle the failures that do not warrant an exception.

## When to use what

- **Choosing a throwable:** recoverable → checked (or `Optional`); programming error → unchecked; never `Error`. When in doubt, unchecked (Item 70).
- **Catching:** catch the narrowest type the handler can act on; never empty-catch; preserve the cause on translation (`new DomainException(e)`); reserve broad `catch (Exception)` for a justified boundary handler that logs and maps.

The companion module's boundary handler is the one place that justified broad catch lives. It takes a narrow recoverable case first, then a backstop that logs the cause and maps it, so nothing escapes and nothing is swallowed:

<!-- include: 12_error_handling_exceptions/src/main/java/org/acme/orders/OrderBoundary.java#boundary-handler -->
- **Resources:** anything `AutoCloseable` goes in a try-with-resources header; never a hand-rolled `finally`. Use `Cleaner` only as a native-resource backstop; let owners (pools, long-lived executors) manage field-held resources by their own lifecycle.
- **Validating input:** guard clauses (and record compact constructors) for internal invariants and private methods; Jakarta Validation at request/DTO/entity boundaries; `assert` only for private preconditions.
- **Security-critical input:** validate as a *supporting* control, allowlist over denylist, server-side. Rely on parameterized queries and output encoding as the frontline (security part).

## Hand-off to the next chapter

The failure paths are now handled: the right exception, the cleaned-up resource, the rejected bad input. The next chapter stays inside Part II and pushes the same idea one layer deeper into the type system. Generics move an entire class of failure (the `ClassCastException`) from run time to compile time, so the compiler catches it before the program ever runs. That is the purest form of this part's recurring move: make the failure visible as early as possible. Two more chapters then close Part II: generics, and the code-smell and design-pattern catalogue that reads the whole part back as a set of recognizable shapes.

## Back matter — sources & traceability

- **JLS SE 21** — §11 (`Throwable` hierarchy; §11.2 checked catch-or-specify), §14.20/§14.20.3 (try/catch/finally; try-with-resources basic §14.20.3.1 / extended §14.20.3.2, reverse-order close, suppressed exceptions) — the try-with-resources sections cross-checked against the figure source-trace sidecar (`05-figures/12_error_handling_exceptions/fig12_2.sources.md`); §14.10 (`assert`) *(section number not independently re-confirmed against the JLS text here — ⚠ @pin).*
- **JEPs** — 358 (Helpful NullPointerExceptions, `-XX:+ShowCodeDetailsInExceptionMessages`; Status Closed/Delivered, Release 14; default-on since JDK 15 via JDK-8233014, so active at the Java 21 anchor — openjdk.org JEP 358); 213 (effectively-final resources, Java 9); 421 (deprecate finalization, Java 18); 395/409/441 (records/sealed/pattern-`switch`, GA at 21); 453 (`StructuredTaskScope`, preview — AHEAD-OF-PIN).
- **JDK 21 API** — `AutoCloseable` (not-idempotent note + implementer rules, verbatim), `Closeable` (idempotent), `Throwable.addSuppressed`/`getSuppressed`, `Cleaner`/`Cleaner.Cleanable`, `Objects.requireNonNull`/`checkIndex`/`requireNonNullElse`.
- **Effective Java 3e** (Bloch, 2018) — Items 69–77 (exceptions), Item 9 (try-with-resources), Item 8 (avoid finalizers/cleaners), Item 49 (check parameters for validity). *(item titles verified; ⚠ verbatim/pages @pin.)*
- **Jakarta Validation 3.1** (Final 2024-03-28; renamed from Jakarta Bean Validation) — `@NotNull`/`@NotEmpty`/`@Size`/`@Email`/`@Valid`/…; `Validator`/`ValidatorFactory`/`ConstraintViolation`/`ExecutableValidator`; groups + `@GroupSequence`; custom `ConstraintValidator`. API `jakarta.validation:jakarta.validation-api:3.1.1` (SOURCE-PIN §1; built into the companion module at `provided` scope). A **constraint-engine implementation** (a reference implementation such as Hibernate Validator, plus a Jakarta EL provider) is **NOT pinned** in `SOURCE-PIN.md`, so no impl GAV/version is asserted as fact and the module evaluates no live `Validator`. *(⚠ impl/EL GAV @pin — 09-flags/12_jep358_default_level_and_rule_ids.md.)*
- **OWASP Input Validation Cheat Sheet** — syntactic vs semantic; allowlist over denylist; server-side/trusted; validation is a supporting, not primary, anti-injection control.
- **Tool rules** — Sonar `java:S112`/`java:S1166`/`java:S2221`/`java:S2095`/`java:S5128`; PMD `EmptyCatchBlock`/`AvoidCatchingGenericException`/`AvoidThrowingRawExceptionTypes`/`PreserveStackTrace`/`CloseResource`; SpotBugs `REC_CATCH_EXCEPTION`/`DE_MIGHT_IGNORE`/`OS_OPEN_STREAM`/`OBL_UNSATISFIED_OBLIGATION`; Checkstyle `IllegalCatch`/`IllegalThrows`; Error Prone `DeadException`/`MustBeClosed`/`StreamResourceLeak`/`Finalize`. *(IDs cited to each tool; ⚠ defaults/exact IDs @pin.)*

**Companion module (✅ EXAMPLE-BUILD = GREEN — `mvn -B -Pquality clean verify` on JDK 21.0.11; 12 tests pass, 0 Checkstyle, 0 SpotBugs; see `12_error_handling_exceptions_EXAMPLE.md`, 2026-06-26):** `08-companion-code/12_error_handling_exceptions/` — an order-service error model: a guard-clause/compact-constructor `Money` invariant (`Money(long minorUnits, String currency)`, fail-fast `IllegalArgumentException`/`NullPointerException`), a recoverable path (`OrderService.readTotal`) throwing a checked `OrderUnavailableException` with the cause chained (Item 73), a sealed `Result<T, E>` typed alternative deconstructed by an exhaustive `switch`, a try-with-resources demo asserting reverse-order close and `getSuppressed()`, a `Cleaner` backstop, and a `@Valid`/`@NotNull`/`@NotEmpty` request record (`OrderRequest`, with a `lines` component) carrying its boundary constraints as declared metadata. **Demonstrated handling:** the constraint-engine implementation is NOT pinned in `SOURCE-PIN.md`, so the module declares constraints against the pinned API (`jakarta.validation-api:3.1.1`, `provided`) and the test asserts the constraint annotations are present on the canonical constructor's parameters rather than wiring an off-pin `Validator`; the one justified broad-catch boundary handler (`OrderBoundary.handleReadTotal`) maps every outcome to a defined `Response` (`200`/`404`/`503`/`500`), logging the cause, swallowing nothing. The module ships **no deliberate-fault code** — it demonstrates the correct handling and so passes the `-Pquality` analyzer gate clean (the empty SpotBugs exclude filter is intentional: zero reviewed suppressions). Snippet tags: `checked-vs-unchecked`, `exception-translation`, `result-model`, `boundary-handler`, `twr-basic`, `suppressed`, `cleaner-backstop`, `guard-clause`, `constraints`.

## Next chapter teaser

An exception handles a failure at run time; a generic type prevents one at compile time. The next chapter covers generics and type-safety: type erasure and the sharp edges it leaves behind, the unchecked warning read as an unpaid debt, and PECS variance. The discipline: write code so the compiler, not a runtime cast, carries the type-safety burden.
