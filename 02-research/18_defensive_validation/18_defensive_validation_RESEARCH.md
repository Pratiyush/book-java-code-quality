# RESEARCH DOSSIER — Java Code Quality Book

> Tier-A code-craft dossier (Part II). Every rule ID, annotation name, API signature, GAV coordinate, spec
> edition and version number is traced to a pinned authority in `00-strategy/SOURCE-PIN.md` (a named spec
> edition, a named book edition, a tool's own docs, or a primary URL). Tool/spec versions on TO-PIN rows are
> stated with the exact value found and marked **⚠ verify at pin**. Anything untraceable is `⚠ UNVERIFIED`
> in §7 and flagged to `09-flags/`.
>
> **Subject discipline:** Java code quality is the subject. Jakarta Validation, Hibernate Validator, the
> JDK guard-clause primitives, and the analyzers are all *components of the Java quality toolchain*, not
> rivals to be crowned. Where two approaches solve the same problem (declarative constraints vs imperative
> guard clauses; spec constraints vs implementation-extra constraints) each gets its strongest case and its
> hardest limitation, cited to its own source. No banned phrasings.

---

## Topic
- **Key:** 18 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Defensive coding & input validation — Jakarta Bean Validation, guard clauses
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** A · **Depth band:** Standard (code-craft) · **Cmp:** not `⚠` (no contested-practice flag; survey two complementary mechanisms neutrally)
- **Java code quality pin:** per `00-strategy/SOURCE-PIN.md` — anchor **Java 21 LTS**, **Java 25 LTS** delta called out.
- **Primary dependency / source unit(s):**
  - **Jakarta Validation 3.1** spec (renamed from "Jakarta Bean Validation"; **released 2024-03-28, Final**) — package `jakarta.validation` / `jakarta.validation.constraints`. *(SOURCE-PIN §1, "Jakarta Validation (Bean Validation) 3.x" — TO-PIN; exact value 3.1 confirmed from the spec page.)*
  - **Hibernate Validator 9.1.0.Final** — reference implementation of Jakarta Validation **3.1.1**; GAV `org.hibernate.validator:hibernate-validator:9.1.0.Final`; requires **Java 17+**. *(SOURCE-PIN §1 RI; ⚠ verify exact patch at pin.)*
  - **JDK** — `java.util.Objects.requireNonNull` (Java 7+), `requireNonNullElse` / `checkIndex` / `checkFromIndexSize` (`Objects` & `java.util.Objects`), `assert` statement (JLS §14.10). Java 21 records (JEP 395, final 16) for validation in compact constructors; Java 21 pattern matching for the validation/guard split.
  - **Effective Java, 3rd ed. (Bloch, 2018)** — Item 49 "Check parameters for validity"; Item 50 (defensive copies) cross-ref to key 10.
  - **OWASP Input Validation Cheat Sheet** + **OWASP Top 10 / ASVS** — syntactic vs semantic validation, allowlist vs denylist, server-side/trusted-system rule. *(SOURCE-PIN §1.)*
  - **Static-analysis rules:** SonarSource `java:S5128` (Bean Validation configuration — ⚠ exact title verify at pin); Error Prone bug patterns in the nullness family; PMD/Checkstyle guard-clause-adjacent rules.
- **Canonical doc page(s):**
  - Jakarta Validation 3.1 spec — https://jakarta.ee/specifications/bean-validation/3.1/jakarta-validation-spec-3.1.html
  - Hibernate Validator 9.1 reference — https://docs.hibernate.org/stable/validator/reference/en-US/html_single/
  - OWASP Input Validation Cheat Sheet — https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html
  - `java.util.Objects` — JDK 21 API docs (docs.oracle.com/en/java/javase/21/docs/api)
- **Canonical source path(s):** to be set under the per-tool fetch dirs in SOURCE-PIN.md once `/pin-source` runs (Jakarta spec PDF/HTML; `org.hibernate.validator` jar/javadoc; JDK 21 `java.base` `Objects`).

---

## 1. Core definition & purpose

**Central claim.** "Defensive coding" is the discipline of **not trusting inputs** — to a method, a constructor, an API endpoint, or a deserializer — and **failing fast and clearly** when an input violates a contract, rather than letting bad data propagate to a distant, confusing failure. The chapter teaches two complementary mechanisms the Java quality toolchain provides for this:

1. **Guard clauses (imperative, in-method).** Explicit checks at the top of a method/constructor that reject invalid arguments before any work begins — the codification of *Effective Java* Item 49, "Check parameters for validity." The JDK ships first-class primitives for this: `Objects.requireNonNull`, `Objects.checkIndex`, the `assert` statement.
2. **Jakarta Validation (declarative, annotation-driven).** A standardized constraint model where constraints (`@NotNull`, `@Size`, `@Email`, …) are declared as annotations on fields, getters, records, method parameters and return values, then enforced by a `Validator` (programmatically) or by a container (Jakarta REST, Persistence, CDI) at well-defined integration points.

**The problem each solves.** Bad input is the single largest source of both *bugs* (NPEs, `IndexOutOfBoundsException`, corrupt state) and *vulnerabilities* (injection, deserialization attacks — key 72). Defensive coding moves the failure to the boundary, where it is cheap to diagnose. *Effective Java* Item 49: most methods restrict their parameters (non-null, non-negative index, in-range), and "you should check these restrictions at the beginning of the method body … fail as soon as possible after the failure, cleanly and with an appropriate exception." *(Bloch, 2018, Item 49.)*

**Quality model placement (key 01).** This chapter moves ISO/IEC 25010 **Reliability** (fault tolerance, maturity) and **Security** (integrity), and supports **Maintainability/Analysability** (a guard clause documents a precondition in executable form). It is the code-level companion to key 72 (injection/deserialization/validation *safety*) — key 18 owns the *technique and tools*; key 72 owns the *attack/defense framing*.

**Build-time vs runtime split (technical profile).** Both mechanisms are predominantly **runtime** (checks execute when the method/endpoint is exercised). The **build-time** layer is the analyzers (Sonar/PMD/SpotBugs/Error Prone) that flag *missing* guards/constraints, and the annotation metadata Jakarta Validation reads via reflection. Null-safety analyzers (NullAway/JSpecify, key 11) shift some of this to compile time.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Guard clauses and the JDK primitives (imperative path)

A guard clause is an early-return / early-throw at the top of a method that rejects invalid state. The fail-fast principle (Wikipedia *Fail-fast system*; *Effective Java* Item 49): surface the error immediately, at the boundary, with a precise exception, rather than letting `null` or an out-of-range value travel.

**JDK primitives (verified against `java.util.Objects`, JDK 21 API):**

- `Objects.requireNonNull(T obj)` / `requireNonNull(T obj, String message)` — throws `NullPointerException` if `obj` is null; returns `obj` so it can **check-and-assign in one expression** (`this.x = requireNonNull(x)`). *(Item 49 explicitly recommends this over hand-written null checks.)*
- `Objects.requireNonNullElse(obj, default)` — null-coalescing default.
- `Objects.checkIndex(index, length)` / `checkFromIndexSize(...)` / `checkFromToIndex(...)` — range guards throwing `IndexOutOfBoundsException`.
- The `assert` statement (JLS §14.10) — for **non-public** methods, `assert cond : msg;` throws `AssertionError`; disabled by default, enabled with `-ea`. *(Item 49: assertions suit private methods because the caller is under your control and the check has "essentially no cost unless you enable assertions.")*

**Exception-type convention (Item 49):** a null parameter → `NullPointerException`; an out-of-range value → `IndexOutOfBoundsException`; any other invalid argument → `IllegalArgumentException`; an invalid object *state* (not a parameter) → `IllegalStateException`. Public/protected methods document each with a Javadoc `@throws` tag.

**Records (Java 21, JEP 395).** The **compact constructor** is the canonical home for record invariant guards — validate before the implicit field assignment:

```java
public record Money(String currency, long cents) {
    public Money {                                  // compact constructor
        requireNonNull(currency, "currency");
        if (cents < 0) throw new IllegalArgumentException("cents < 0: " + cents);
    }
}
```
*(Verified: JEP 395 compact-constructor semantics; ≤9 lines.)* This ties defensive coding to the immutability chapter (key 10) and modern-Java chapter (key 13).

### 2.2 Jakarta Validation (declarative path)

**Setup / metadata.** Constraints are annotations on a constrained element — a field, JavaBean getter, **record component**, type-use, method/constructor parameter, or return value. The constraint model is *standardized* in `jakarta.validation` and `jakarta.validation.constraints`. An implementation (RI: Hibernate Validator) reads this metadata reflectively. *(Spec 3.1; record support "clarified" in 3.1.)*

**Active / runtime.** Two trigger modes (spec):
- **Programmatic.** Bootstrap a factory then validate:
  ```java
  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  Validator validator = factory.getValidator();
  Set<ConstraintViolation<Order>> violations = validator.validate(order);
  ```
  Each `ConstraintViolation` carries the message, property path, invalid value, and root bean. *(Verified: Hibernate Validator 9.1 reference.)*
- **Integrated / container.** Jakarta REST, Persistence, CDI, Faces invoke validation at defined lifecycle points (e.g. a `@Valid`-annotated request body before a controller method runs; pre-persist on an entity).

**Cascaded validation.** `@Valid` on a field/parameter cascades into the referenced object graph (validate an `Order`'s `List<@Valid LineItem>`).

**Method (executable) validation** (spec since 1.1; `ExecutableValidator`):
- `validator.forExecutables()` → `ExecutableValidator` with `validateParameters`, `validateReturnValue`, `validateConstructorParameters`, `validateConstructorReturnValue`. Parameter constraints = preconditions; return-value constraints = postconditions (design-by-contract). *(Verified: Hibernate Validator 9.1 reference.)*
- **Cross-parameter constraints** validate consistency across several parameters via `@SupportedValidationTarget(ValidationTarget.PARAMETERS)`; `ConstraintTarget` (`IMPLICIT`, `PARAMETERS`, `RETURN_VALUE`) disambiguates. *(Spec 3.1.)*

**Groups & sequencing.** Validation **groups** (marker interfaces) partition constraints into subsets (e.g. `Create` vs `Update`); `@GroupSequence` orders group evaluation (stop at first failing group). *(Spec.)*

**Custom constraints.** A new annotation + a `ConstraintValidator<A extends Annotation, T>` implementing `boolean isValid(T value, ConstraintValidatorContext ctx)`. *(Spec; verified type signature.)*

**Message interpolation.** Violation messages support an **Expression Language** (Jakarta EL); in Java SE the EL RI must be added as a dependency. Hibernate Validator's docs flag that interpolating **untrusted constraint expressions** is a safety concern (treat constraint expressions as code). *(Hibernate Validator 9.1 reference — feed key 72.)*

### 2.3 Reference units (atoms)

| Name | Type | Default / note | Fixed early? | Source |
|---|---|---|---|---|
| `jakarta.validation.constraints.@NotNull` | spec constraint | element must not be null | — | Jakarta Validation 3.1 spec |
| `@NotEmpty` / `@NotBlank` | spec constraint | non-empty / non-blank (trimmed) string/collection | — | spec 3.1 |
| `@Size(min,max)` | spec constraint | size bounds | min 0 / max MAX | spec 3.1 |
| `@Min/@Max/@DecimalMin/@DecimalMax/@Digits` | spec constraint | numeric bounds | — | spec 3.1 |
| `@Positive/@PositiveOrZero/@Negative/@NegativeOrZero` | spec constraint | sign | — | spec 3.1 |
| `@Past/@PastOrPresent/@Future/@FutureOrPresent` | spec constraint | temporal | — | spec 3.1 |
| `@Pattern(regexp)` / `@Email` | spec constraint | regex / email format | — | spec 3.1 |
| `@AssertTrue/@AssertFalse/@Null` | spec constraint | boolean / null | — | spec 3.1 |
| `@Valid` | cascade marker | cascades into object graph | — | spec 3.1 |
| `Validator`, `ValidatorFactory`, `Validation`, `ConstraintViolation`, `ConstraintValidator`, `ExecutableValidator`, `ConstraintValidatorContext` | core API types | `jakarta.validation` | — | spec 3.1 |
| `@GroupSequence`, `ConstraintTarget`, `ValidationTarget`, `@SupportedValidationTarget` | grouping / target | — | — | spec 3.1 |
| `org.hibernate.validator:hibernate-validator` | GAV (RI) | `9.1.0.Final` (impl of 3.1.1; Java 17+) | ⚠ verify at pin | Hibernate Validator 9.1 ref |
| HV extra constraints `@Length`,`@Range`,`@URL`,`@CreditCardNumber`,`@UniqueElements`,`@ISBN`,`@UUID`,`@LuhnCheck` | impl-specific constraints | non-spec; HV-only | — | Hibernate Validator 9.1 ref |
| `java.util.Objects.requireNonNull(T)` / `(T,String)` | JDK guard primitive | throws NPE; returns arg | Java 7+ | JDK 21 `java.util.Objects` |
| `Objects.checkIndex(i,len)` / `checkFromIndexSize` | JDK range guard | throws IOOBE | Java 9+ | JDK 21 `java.util.Objects` |
| `assert cond : msg;` | JLS statement | disabled unless `-ea` | JLS §14.10 | JLS SE 21 |
| `java:S5128` | Sonar rule | Bean Validation configuration (⚠ exact title verify at pin) | — | rules.sonarsource.com |
| Error Prone `ParameterMissingNullable` / `FieldMissingNullable` / `ReturnMissingNullable` | bug patterns | nullness annotation hygiene | — | errorprone.info/bugpatterns |

---

## 3. Evidence FOR

- **Verified examples (≤9 lines each, traced):**
  - Guard-clause one-liner: `this.currency = Objects.requireNonNull(currency, "currency");` — check-and-assign. *(JDK 21 `Objects` javadoc; Item 49.)*
  - Record compact-constructor guard (§2.1) — *(JEP 395 + Item 49.)*
  - Declarative constraint on a record/bean: `record CreateOrder(@NotEmpty List<@Valid LineItem> items, @Email String contact) {}` — *(Jakarta Validation 3.1 spec; record support clarified 3.1.)*
  - Programmatic bootstrap + `validate` returning `Set<ConstraintViolation<T>>` (§2.2). *(Hibernate Validator 9.1 reference.)*
- **Standardization / maturity.** Jakarta Validation is an **active Jakarta EE specification** (3.1 Final, 2024-03-28), descended from JSR 303/349/380; Hibernate Validator is the named RI with a current 9.1.0.Final release — i.e. the model is governed and live, not folklore.
- **First-class tooling.** Static analyzers ship rules to enforce *both* paths: SonarSource has rules in the Bean-Validation family (`java:S5128`) and a large null-related rule set; Error Prone ships nullness bug patterns; PMD/Checkstyle complexity rules indirectly reward guard-clause early-return over deep nesting (key 03 / key 27 cross-ref).
- **Authoritative endorsement.** *Effective Java* Item 49 is the canonical Java statement of fail-fast parameter checking; OWASP treats server-side input validation as a required (supporting) control.
- **Records integration.** The 3.1 spec explicitly clarified record support, and the JDK compact constructor gives a language-level home for invariant guards — defensive coding aligns with modern Java (keys 10, 13).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

**Guard clauses (imperative).**
- *Hardest objection:* hand-written guards scatter the same precondition logic across many methods (duplication), and over-guarding internal/private methods adds noise the type system or assertions could carry. Item 49 itself scopes the cost: for private methods where the caller is trusted, prefer `assert` (zero runtime cost when disabled).
- *When NOT to use:* do not guard parameters the JVM will validate anyway with an equally clear exception (e.g. an immediate array access already throws `IndexOutOfBoundsException`) — Item 49 names this exception; redundant guards add code without diagnostic gain. Do not use `assert` for **public-API** argument checks (assertions are disabled in production).

**Jakarta Validation (declarative).**
- *Hardest objection:* it is **reflection-based and runtime**, so a missing `@Valid` (a forgotten cascade trigger) silently disables validation — the failure is *absence of an error*, which static analysis only partially catches (the motivation for Sonar's Bean-Validation rule). It needs a wired implementation + Jakarta EL on the classpath; in plain Java SE that is non-trivial setup.
- *Environment/compatibility caveats:* native-image / reflection — Hibernate Validator relies on reflection and EL, which require explicit reflection/resource registration under GraalVM native image (cross-ref key 72 / native chapters). EL message interpolation of **untrusted** constraint expressions is itself a safety concern (Hibernate Validator docs).
- *When NOT to use:* for a single deep-internal precondition, a one-line guard clause is more local and visible than an annotation + validator wiring; for **security-critical** sanitization, Jakarta Validation is **not** the primary defense (see below).

**The cross-cutting limit (OWASP — load-bearing for neutrality with key 72).** OWASP states input validation "should **not** be used as the *primary* method of preventing XSS, SQL Injection and other attacks"; parameterized queries and output encoding are the frontline defenses, with validation a supporting control. Prefer **allowlist** over denylist (denylists are circumventable and reject legitimate input like `O'Brien`). Validation **must** run server-side on a trusted system; client-side validation is UX only. *(OWASP Input Validation Cheat Sheet.)*

**Two complementary mechanisms — neutral framing.** Guard clauses give **local, visible, zero-dependency** checks ideal for invariants and private methods; Jakarta Validation gives **declarative, reusable, container-integrated** checks ideal for request/DTO/entity boundaries with structured violation reporting. They are routinely combined (constraints at the edge, guards on internal invariants); neither subsumes the other.

## 5. Current status

- **Jakarta Validation 3.x** is current: **3.0** (2020, the `javax`→`jakarta` namespace move) → **3.1** (2024-03-28 Final; renamed "Jakarta Bean Validation" → "Jakarta Validation"; record support clarified). Lineage JSR 303 (1.0) → 349 (1.1, executable validation) → 380 (2.0). *(Spec pages.)*
- **Hibernate Validator** line: **9.1.0.Final** implements Jakarta Validation **3.1.1**, requires Java 17+; the 8.0.x line implements 3.0. *(Hibernate Validator references.)* ⚠ verify exact pinned patch at `/pin-source`.
- **JDK guard primitives** are stable: `requireNonNull` (Java 7), `checkIndex` family (Java 9); unchanged through 21/25. Records final since Java 16 (JEP 395); no guard-relevant change at 25 beyond general record/pattern maturity (⚠ AHEAD-OF-PIN check any 25 preview that touches this — none expected).
- **Static-analysis rules** evolve per analyzer release; rule IDs (`java:S5128`, Error Prone nullness patterns) are version-pinned — treat exact titles/defaults as **⚠ verify at pin**.
- **Trend:** stable and widely adopted; the active movement is at the *compile-time null-safety* edge (JSpecify 1.0, NullAway — key 11) shifting null guards left, complementing runtime validation.

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** `DEMO-CATALOG.md` shared domain = storefront (`org.acme.storefront`). **This chapter's demo:** the order-creation boundary — a `POST /orders` endpoint (the flagship checkout-link surface) that accepts an order DTO/record and **rejects an invalid order at the boundary**. Java code quality surface: declarative constraints (`@NotEmpty`, `@Valid`, `@Positive`) on the request record + a guard clause / compact-constructor invariant on the domain `Money`/`Order` type. **TRY-IT:** add a `@Size(max=50)` constraint to a new `note` field, post an over-long note, and assert the `400` + `ConstraintViolation` message in a test. *(Add/confirm the row in `DEMO-CATALOG.md` §3.)*
- **Module key / path:** `08-companion-code/18_defensive_validation/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property) — Java 21 | establishes the pin | SOURCE-PIN runtime baseline | ☐ |
  | `jakarta.validation:jakarta.validation-api` (3.1) | the constraint model under study | Jakarta Validation 3.1 spec | ☐ (⚠ verify GAV+version at pin) |
  | `org.hibernate.validator:hibernate-validator:9.1.0.Final` | the RI that enforces constraints | Hibernate Validator 9.1 ref | ☐ |
  | `org.glassfish.expressly:expressly` (Jakarta EL impl) | EL for message interpolation in Java SE | Hibernate Validator 9.1 ref | ☐ (⚠ verify EL GAV at pin) |
  | `org.junit.jupiter:junit-jupiter` + AssertJ | test harness | SOURCE-PIN §3 | ☐ |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property; no loose versions.
  - **Externalized config / profiles** — the order `note` max length and a strict/lenient validation-groups profile as config keys (name + trace).
  - **At least one test** — asserts that an empty `items` list produces a `@NotEmpty` `ConstraintViolation` (and the `400` at the endpoint).
  - **Observability / health surface** — the validation-failure response body (RFC-7807-style problem detail) is the observable surface; note where it maps from `ConstraintViolation`.
  - **Explicit failure path** — the deliberate error response: invalid order → `400 Bad Request` with the violation set (this is the honest-limitations beat in code). Plus a guard-clause `IllegalArgumentException` on the `Money` invariant proving fail-fast at the domain layer.
- **Displayed-snippet tie (tag regions inside the one compiled file):**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `guard-clause` | `requireNonNull` + compact-constructor invariant | `Money.java` |
  | `constraints` | `@NotEmpty`/`@Valid`/`@Positive` on the request record | `CreateOrderRequest.java` |
  | `programmatic-validate` | `Validation.buildDefaultValidatorFactory()` → `validate` | `OrderValidationTest.java` |

- **Run command:** `./mvnw -B spring-boot:run` (or the module's REST runner) then `POST /orders`; note the EL dependency must be present.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** valid order → `201`; empty `items` → `400` with a `@NotEmpty` violation message; `Money(cents<0)` → `IllegalArgumentException`; test suite green asserting all three.
- **Figure plan** (per-chapter image budget; GUIDELINES §8):
  - **Chapter class:** standard code-craft chapter → budget ~1–2 designed diagrams + 0–1 captured screenshot.
  - **Candidate designed diagram(s) + family:**
    - **Fig 18.1 — Two validation paths on one request:** flow diagram showing an incoming request hitting (a) the declarative constraint layer (`@Valid` → `Validator` → `ConstraintViolation` set → `400`) and (b) the imperative guard-clause layer at the domain boundary (`requireNonNull`/compact ctor → `IllegalArgumentException`). Visual family: data-flow/boundary diagram. Authored in HTML → rendered PNG via `05-figures/_assets/render.mjs`.
    - **Fig 18.2 (optional) — allowlist vs denylist + syntactic/semantic quadrant:** trace to OWASP Input Validation Cheat Sheet.
  - **Candidate captured surface(s):** a Sonar/IDE finding for a missing `@Valid` or a missing null guard (`java:S5128` / nullness rule) — capture only if the analyzer is wired at draft time.
  - **Source trace per depicted claim:** Fig 18.1 — Jakarta Validation 3.1 spec (trigger flow) + JDK 21 `Objects`/JLS §14.10 (guard path); Fig 18.2 — OWASP Input Validation Cheat Sheet.

## 7. Gap-filling (verification queue)

- ⚠ **Jakarta Validation pinned version** — `/pin-source` to confirm 3.1 (vs 3.0) as the book pin and the exact `jakarta.validation:jakarta.validation-api` GAV/version. *(Spec page states 3.1 Final 2024-03-28.)*
- ⚠ **Hibernate Validator pinned patch** — confirm `9.1.0.Final` (impl of 3.1.1) is the pin, or whether the book holds 8.0.x (impl of 3.0). Requires Java 17+ — compatible with the Java 21 anchor.
- ⚠ **Jakarta EL implementation GAV** — confirm the EL RI artifact (`org.glassfish.expressly:expressly` vs `org.glassfish:jakarta.el`) at the pin; the example needs it.
- ⚠ **`java:S5128` exact title & type** — `rules.sonarsource.com` was unreachable during research (ECONNREFUSED / typo-port). Confirm the exact rule title ("Bean Validation … should be properly configured", per community thread) and rule type at pin. Material → flagged `09-flags/18_sonar_s5128_title_unverified.md`.
- ⚠ **Spring "missing @Valid" rule** — if the companion uses Spring, confirm whether a distinct rule flags a missing `@Valid` on a controller parameter (community discussion references it; exact ID unconfirmed). Detail belongs to key 35 (Sonar) / key 72.
- ⚠ **Error Prone nullness patterns** — confirm exact pattern names (`ParameterMissingNullable`, `FieldMissingNullable`, `ReturnMissingNullable`) and severities at the pinned Error Prone version (detail shared with key 11).
- ⚠ **Effective Java Item 49 verbatim quotes** — confirm exact wording + page before block-quoting ("check these restrictions at the beginning of the method body…").
- **Cross-ref to confirm not duplicated:** key 72 owns injection/deserialization/EL-injection attack framing; key 11 owns null-safety annotations/`Optional`; key 10 owns defensive copies (Item 50); key 12 owns exception-type design.

## 8. Sources & further reading

### Primary / Official (pinned authority set)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Jakarta spec | Jakarta Validation 3.1 specification (Final 2024-03-28) | https://jakarta.ee/specifications/bean-validation/3.1/jakarta-validation-spec-3.1.html | ☑ (constraints, API, executable/cross-param, groups; ⚠ pin the edition) |
| 2 | Jakarta spec | Jakarta Bean Validation 3.0 specification (Gunnar Morling) | https://jakarta.ee/specifications/bean-validation/3.0/jakarta-bean-validation-spec-3.0.html | ☑ (namespace move; lineage) |
| 3 | Tool docs (RI) | Hibernate Validator 9.1 reference guide | https://docs.hibernate.org/stable/validator/reference/en-US/html_single/ | ☑ (GAV, extra constraints, bootstrap, ExecutableValidator, EL note, Java 17+) |
| 4 | JDK API | `java.util.Objects` (JDK 21) — requireNonNull/checkIndex | docs.oracle.com/en/java/javase/21/docs/api | ☑ (signatures; ⚠ confirm at pinned patch) |
| 5 | Spec | JLS SE 21 §14.10 — the `assert` statement | docs.oracle.com/javase/specs | ☑ (assert semantics) |
| 6 | JEP | JEP 395 — Records (final, Java 16) | openjdk.org/jeps/395 | ☑ (compact constructor) |
| 7 | Book canon | Bloch, *Effective Java* 3e (2018), Item 49 "Check parameters for validity" (+ Item 50) | print | ☑ (fail-fast, requireNonNull, exception types, assert for private; ⚠ verbatim at draft) |
| 8 | OWASP | Input Validation Cheat Sheet | https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html | ☑ (syntactic/semantic, allowlist>denylist, server-side, not primary anti-injection defense) |
| 9 | Tool rules | SonarSource `java:S5128` (Bean Validation configuration) | rules.sonarsource.com/java/RSPEC-5128 | ⚠ (site unreachable in scan — verify title/type at pin) |
| 10 | Tool docs | Error Prone bug patterns (nullness family) | https://errorprone.info/bugpatterns | ☑ (pattern names; ⚠ pin severities) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Baeldung | Guide to `Objects.requireNonNull()` | https://www.baeldung.com/java-objects-requirenonnull | ☑ (corroboration only) |
| 2 | Wikipedia | Fail-fast system / Guard (computer science) | en.wikipedia.org/wiki/Fail-fast_system | ☑ (concept color) |
| 3 | Community | Sonar community — "Bean Validation should be enabled" / S5128 thread | community.sonarsource.com | ☑ (corroboration for S5128 scope) |

> Source-quality order (house style): Jakarta spec / JDK API / JLS / JEP → Hibernate Validator (RI) docs → OWASP → *Effective Java* (canon) → analyzer rule docs → quality secondary (Baeldung, corroboration only). No content farms / AI text as a factual source.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | Jakarta Bean Validation 3.1 constraints/API | web search | RI = Hibernate Validator 9.1.0.Final → Jakarta Validation 3.1; built-in constraint list |
| 2 | guard clauses / requireNonNull / Item 49 | web search | fail-fast; requireNonNull (Java 7); Item 49 scope (public vs private, exception types) |
| 3 | fetch Jakarta Validation 3.1 spec | jakarta.ee | full constraint list; core API types; executable+cross-param validation; groups/@GroupSequence; 3.1 Final 2024-03-28; renamed; record support clarified |
| 4 | fetch Hibernate Validator reference | docs.hibernate.org | 9.1.0.Final → 3.1.1; Java 17+; GAV; extra constraints (@Length/@Range/@URL/@CreditCardNumber/@UniqueElements…); bootstrap; ExecutableValidator; EL note |
| 5 | Sonar S4790 / S5876 / S5527 checks | web search | S4790=hashing, S5876=session, S5527=TLS — NOT validation; discarded to avoid mis-cite |
| 6 | Sonar S5128 / Bean Validation rule | web search | java:S5128 = "Bean Validation … should be properly configured" (community); exact title to verify at pin |
| 7 | Error Prone nullness bug patterns | web search | ParameterMissingNullable / FieldMissingNullable / ReturnMissingNullable / CheckReturnValue |
| 8 | fetch OWASP Input Validation Cheat Sheet | owasp.org | syntactic vs semantic; allowlist>denylist; server-side/trusted; validation not the primary anti-injection defense |
| 9 | fetch rules.sonarsource.com S5128 | rules.sonarsource.com | UNREACHABLE (ECONNREFUSED / bad port) → flagged |

---
## Learnings & pipeline suggestions
- **Rule-ID mis-cite trap (new, generalizable):** several plausible-sounding Sonar rule IDs guessed from memory mapped to *unrelated* checks — `java:S4790` is weak-hashing, `S5876` is session fixation, `S5527` is TLS hostname verification, none is input validation. **Lesson:** never assert a Sonar/Error Prone/PMD rule ID from memory; resolve it on `rules.sonarsource.com` (or the analyzer's own rule doc) at the pin, and if the rules site is unreachable, mark the title `⚠ verify at pin` rather than guess. Extends the existing "no folklore-as-fact" principle to **rule IDs**. → propose adding "tool rule IDs" to the never-invent emphasis in SOURCE-PIN's atom list (already covered generally; worth an explicit example).
- **Spec-rename trap:** "Jakarta **Bean** Validation" became "Jakarta Validation" at 3.1 (2024-03-28); cite the post-3.1 name as "Jakarta Validation" and note the historical name. Consistent with the standards/edition-discipline durable principle.
- **Cross-ref hygiene:** key 18 (technique/tools) vs key 72 (attack/defense framing) vs key 11 (null-safety) vs key 10 (defensive copies, Item 50) vs key 12 (exception types) — record explicit boundaries in merge notes so the OWASP "not the primary defense" point and EL-injection caveat land in 72, not duplicated here.
- **Flag filed:** `09-flags/18_sonar_s5128_title_unverified.md` (rules.sonarsource.com unreachable during scan).
