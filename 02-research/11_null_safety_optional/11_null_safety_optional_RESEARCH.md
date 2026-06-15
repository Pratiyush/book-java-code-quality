# RESEARCH DOSSIER — Java Code Quality Book

> Code-craft (Tier-B / Part II) dossier. Every specific fact (rule ID, GAV, JEP number, method
> signature, threshold) is traced to a pinned authority in `00-strategy/SOURCE-PIN.md` — the JDK
> Javadoc/JLS/JEP, a tool's own docs/repo, or the named book canon (edition). Tool versions are
> `TO-PIN` in SOURCE-PIN.md, so versions/thresholds are cited to the tool/doc and marked
> `⚠ verify at pin` where the exact number must be confirmed against the pinned release. Untraceable
> items are marked `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.
>
> **Cluster note:** key 11 owns the *language / design* angle (designing NPEs out: the JLS reality of
> `null`, JDK tooling, `Optional` discipline, the annotation idea at a conceptual level). The deep
> tool tutorials live in siblings: **key 31** (NullAway — build-time enforcement) and **key 32**
> (the annotation landscape — JSpecify / Checker Framework / JSR-305 legacy, the `⚠` neutrality key).
> This dossier introduces those tools only enough to frame the design problem and cross-references the
> siblings rather than duplicating their depth.

---

## Topic
- **Key:** 11 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Null-safety & Optional discipline — designing NPEs out
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B · **Depth band:** Standard (with conceptual reach into the annotation ecosystem)
- **Cmp flag:** none on 11 itself; the `⚠` (contested) flag sits on sibling key 32. Where this chapter
  names the annotation systems, it still obeys NEUTRALITY (each its strongest case + hardest limit).
- **Merge cluster:** 11 / 31 / 32 (likely one-or-two chapters, not three). Related: 10 (immutability —
  fewer mutable fields means fewer "not-yet-initialized null" states), 12 (error handling — null vs
  exception as the absence signal), 13 (modern Java — records and their non-null component discipline),
  18 (defensive coding — guard clauses / `Objects.requireNonNull`).
- **Java code quality pin (runtime):** Java 21 (LTS) anchor; Java 25 (LTS) deltas noted. Per SOURCE-PIN.md.
- **Primary dependency / source units:**
  - `java.util.Optional<T>` (and `OptionalInt`/`OptionalLong`/`OptionalDouble`) — `java.base`.
  - `java.util.Objects` — `requireNonNull` family, `requireNonNullElse`, `requireNonNullElseGet`.
  - **JEP 358** — Helpful NullPointerExceptions; JVM flag `-XX:+ShowCodeDetailsInExceptionMessages`.
  - **JSpecify 1.0.0** — GAV `org.jspecify:jspecify:1.0.0`; annotations `@Nullable`, `@NonNull`,
    `@NullMarked`, `@NullUnmarked` (package `org.jspecify.annotations`).
  - *Effective Java* 3e — Item 54 (return empty, not null) and Item 55 (return Optionals judiciously).
  - Tool rules that police null/Optional usage (named, cited to each tool): Sonar `java:S2789`,
    `java:S3655`, `java:S2259`; SpotBugs `NP_NULL_ON_SOME_PATH`, `RCN_*`; Error Prone
    `NullableOptional`, `OptionalNotPresent`, `ReturnMissingNullable`; NullAway / Checker Nullness.
- **Canonical doc page(s):**
  - `Optional` Javadoc (SE 21): https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html
  - `Objects` Javadoc (SE 21): https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html
  - JEP 358: https://openjdk.org/jeps/358
  - JSpecify spec / using: https://jspecify.dev/docs/spec/ · https://jspecify.dev/docs/using/
- **Canonical source path(s) (under the per-tool fetch dirs in SOURCE-PIN.md):** JDK 21 `java.base`
  module (`java/util/Optional.java`, `java/util/Objects.java`); `github.com/jspecify/jspecify` @ v1.0.0;
  `github.com/uber/NullAway`; `checkerframework.org` Nullness Checker manual.

---

## 1. Core definition & purpose

**Central claim.** `null` is the most common single source of runtime failure in Java, and the
`NullPointerException` is the canonical symptom. "Designing NPEs out" is the discipline of pushing the
*possibility* of null out of a code path — at design time (don't model absence with null where you can
avoid it), at API-boundary time (be explicit about what may be null), and at build time (let a tool
prove the remaining cases). The chapter's thesis: an NPE is almost always a **design defect made
visible at runtime**, and Java now offers four complementary levers to move that detection earlier:

1. **Avoid null as a return value** — return empty collections/arrays, not null (*Effective Java*
   Item 54); use `Optional<T>` as a deliberate "may be absent" return type (Item 55).
2. **Fail fast at the boundary** — `Objects.requireNonNull(...)` to convert a silent-later NPE into a
   loud-now one at the method entry (key 18 cross-ref).
3. **Get a better message when it still happens** — JEP 358 Helpful NullPointerExceptions name the
   exact expression that was null.
4. **Prove it at compile time** — nullness annotations (JSpecify) plus a checker (NullAway / Checker
   Framework) that turns "might NPE" into a build error (deep dive in keys 31 / 32).

**Where it sits in the architecture.** Levers 1–2 are pure design/API decisions (no tooling needed);
lever 3 is a JVM diagnostic (build-time-free, on by default since Java 15); lever 4 is a static-analysis
layer added at compile time. The **build-time vs runtime split**: annotations + checker (lever 4) and
`Optional`-discipline (lever 1) are *build-time / design-time*; `requireNonNull` (lever 2) and helpful
NPE messages (lever 3) are *runtime*. A mature null-safety program uses all four, layered.

**Why null is special in Java.** The JLS gives `null` as a member of every reference type (a
reference can always be null), and the language has no compile-time non-null obligation — so the type
system alone does not stop NPEs. That gap is exactly what the four levers fill. *(Source: JLS SE 21
§4.1 reference types / `null` type; confirm section number at pin — §7.)*

---

## 2. Mechanism (the spine of the chapter)

### 2.1 `Optional<T>` — the design-time absence container

`Optional<T>` (Javadoc, SE 21) is "a container object which may or may not contain a non-null value."
Its **stated design intent**, verbatim from the class API Note:

> "`Optional` is primarily intended for use as a method return type where there is a clear need to
> represent 'no result,' and where using `null` is likely to cause errors. A variable whose type is
> `Optional` should never itself be `null`; it should always point to an `Optional` instance."
> *(Source: `Optional` Javadoc, SE 21 — verified @pin.)*

`Optional` is a **value-based class**: "programmers should treat instances that are equal as
interchangeable and should not use instances for synchronization, or unpredictable behavior may occur."
*(Source: same Javadoc — verified @pin.)* The Java-concrete consequences (each is a chapter teaching
beat): never synchronize on an `Optional`; never compare with `==`; never use it as a map key; an
`Optional` field/parameter is an anti-pattern the design intent argues against.

**The two-construction split (a load-bearing API distinction):**
- `Optional.of(value)` throws NPE if `value` is null — use when null is a programming error.
- `Optional.ofNullable(value)` yields empty for null — use to lift a possibly-null legacy value.

### 2.2 The *Effective Java* discipline (Items 54 & 55)

- **Item 54 — "Return empty collections or arrays, not nulls."** Never return null in place of an
  empty array or collection; it forces null-handling on every caller and adds no performance benefit.
  *(Source: Effective Java 3e, Item 54 — confirm verbatim/page at draft, §7.)*
- **Item 55 — "Return optionals judiciously."** `Optional<T>` is "essentially an immutable collection
  that can hold at most one element." Key prescriptions, each cited to the Item:
  - **Never wrap a container in an Optional.** "Container types, including collections, maps, streams,
    arrays, and optionals should not be wrapped in optionals" — return the empty container instead
    (ties back to Item 54).
  - **Never use `Optional` as a map value** — it creates two ways to express absence.
  - Returning `Optional` is for methods that "might not be able to return a result and clients will
    have to perform special processing if no result is returned."
  - Performance caveat: `Optional` allocates an object and the value is unwrapped — not for
    performance-critical, hot-path methods (use `OptionalInt`/`OptionalLong`/`OptionalDouble` for
    boxed primitives; never `Optional<Integer>`).
  *(Source: Effective Java 3e, Item 55 — verified via Item summary; confirm verbatim quotes/page at draft, §7.)*

### 2.3 `Objects` — fail-fast at the boundary (runtime lever)

`java.util.Objects` supplies the canonical guard-clause helpers (all in `java.base`):

- `requireNonNull(T obj)` / `requireNonNull(T obj, String message)` — throws `NullPointerException`
  immediately if null; returns the value otherwise (composes inline in constructors/setters).
  *(Java 7.)*
- `requireNonNull(T obj, Supplier<String> messageSupplier)` — lazy message. *(Java 8.)*
- `requireNonNullElse(T obj, T defaultObj)` and `requireNonNullElseGet(T obj, Supplier<? extends T>)`
  — return the value or a (non-null) default. *(Java 9.)*

The mechanism is **fail-fast**: convert a silent null that would NPE several frames later into a loud
NPE at the exact entry point, with a message naming the offending parameter. Canonical idiom is
`this.x = Objects.requireNonNull(x, "x")` in a constructor — pairs with immutability (key 10) and
defensive coding (key 18). *(Source: `Objects` Javadoc, SE 21 — verified @pin.)*

### 2.4 JEP 358 — Helpful NullPointerExceptions (runtime diagnostic lever)

JEP 358 makes the JVM analyse the bytecode at the throw site to name the exact null expression. Verified
facts:
- Targeted **JDK 14**; flag `-XX:+ShowCodeDetailsInExceptionMessages`.
- **Off by default in JDK 14; on by default since JDK 15** (so it is on at the Java 21 / 25 anchor with
  no flag). *(Source: JEP 358; corroborated by Baeldung "Java 14 NPE" — verify the on-by-default-15
  line against the JDK 15 release notes at pin, §7.)*
- Message shape: two parts — the failing operation and the reason. Example form:
  `Cannot invoke "String.toLowerCase()" because the return value of "getEmailAddress()" is null`.
  *(Source: JEP 358 examples — confirm exact wording against the JEP text; the openjdk.org page returns
  403 to the fetch tool, so corroborated via secondary — §7 flag.)*

Teaching beat: this does not *prevent* NPEs; it makes the remaining ones diagnosable in one read,
which is the honest counterpoint to "you can't design every null out."

### 2.5 Nullness annotations + a checker — the build-time proof lever (intro only; depth in 31/32)

The JLS does not let the type system express "this reference is never null." Annotation systems add that
information and a pluggable checker enforces it at compile time.

**JSpecify 1.0.0** (`org.jspecify:jspecify:1.0.0`, package `org.jspecify.annotations`) — four
annotations, declared official and frozen (no backwards-incompatible change ever):
- `@Nullable` — the annotated **type usage** may be null.
- `@NonNull` — the annotated type usage is never null.
- `@NullMarked` — applied to a module/package/class/method: within the scope, **unannotated type
  usages default to non-null** (you then mark only the genuine exceptions with `@Nullable`).
- `@NullUnmarked` — cancels `@NullMarked` for a scope (exceptions to a marked zone).
*(Source: JSpecify spec + release 1.0.0 notes — verified @pin for the four names + the marked-zone
default semantics; `@Nullable`/`@NonNull` are **type-use** annotations, a deliberate design point.)*

The annotations carry **no behaviour on their own** — a checker reads them: **NullAway** (an Error
Prone plugin, build-time, low overhead) or the **Checker Framework Nullness Checker** (a pluggable
type system aiming at a soundness guarantee). The trade-offs between these are key 31 / 32 content;
this chapter states only that the *annotation + checker* layer is what converts "might NPE" into a
build failure.

### 2.6 Tooling that polices null/Optional usage (named, neutral, cited per tool)

Even without a full annotation regime, the standard analyzers ship null/Optional rules. Each is cited to
its own tool (NEUTRALITY): these are reported as what each tool does, not ranked.

| Tool | Rule ID / pattern | What it flags | Source |
|---|---|---|---|
| SonarQube/SonarLint | `java:S3655` | `Optional.get()` / value access without `isPresent()`/`isEmpty()` guard | rules.sonarsource.com (S3655) |
| SonarQube/SonarLint | `java:S2789` | an `Optional` reference itself being null / null-checked | rules.sonarsource.com (S2789) |
| SonarQube/SonarLint | `java:S2259` | null pointer dereference (data-flow) | rules.sonarsource.com (S2259) |
| SpotBugs | `NP_NULL_ON_SOME_PATH` | possible null dereference on some path | spotbugs.readthedocs.io detectors |
| SpotBugs | `RCN_REDUNDANT_NULLCHECK_*` | redundant null check (value known non-null / would have been NPE) | spotbugs.readthedocs.io detectors |
| Error Prone | `NullableOptional` | an `Optional` typed/used as possibly-null (use empty instead) | errorprone.info/bugpatterns |
| Error Prone | `OptionalNotPresent` | accessing an `Optional` known to be empty | errorprone.info/bugpatterns |
| Error Prone | `ReturnMissingNullable` | a method that can return null but is not `@Nullable` | errorprone.info/bugpattern/ReturnMissingNullable |
| NullAway | (Error Prone check `NullAway`) | dereference of a value not proven non-null in `@NullMarked` code | github.com/uber/NullAway |
| Checker Framework | Nullness Checker | sound: no NPE possible if no warnings (cost: false positives / annotation effort) | checkerframework.org |

*(Exact rule defaults / severities are version-pinned — `⚠ verify at pin` per the relevant tool row in §7;
rule-ID existence and meaning verified via each tool's own docs.)*

### 2.7 Reference units (atoms) table

| Name | Type | Default / value | Fixed early? | Source |
|---|---|---|---|---|
| `Optional.of(T)` | static factory | NPE on null arg | API, SE 21 | Optional Javadoc @pin |
| `Optional.ofNullable(T)` | static factory | empty on null arg | API, SE 21 | Optional Javadoc @pin |
| `Optional.empty()` | static factory | shared empty instance | API | Optional Javadoc @pin |
| `Optional.get()` | accessor | `NoSuchElementException` if empty | API | Optional Javadoc @pin |
| `Optional.orElse(T)` / `orElseGet(Supplier)` / `orElseThrow()` | accessors | — | API | Optional Javadoc @pin |
| `Optional.map/flatMap/filter/ifPresent/ifPresentOrElse/or/stream` | combinators | — | Java 8 (8/9 for ifPresentOrElse/or/stream) | Optional Javadoc @pin |
| `Objects.requireNonNull(T[,msg])` | guard | throws NPE | Java 7 | Objects Javadoc @pin |
| `Objects.requireNonNullElse(T,T)` | guard | returns default | Java 9 | Objects Javadoc @pin |
| `-XX:+ShowCodeDetailsInExceptionMessages` | JVM flag | **on by default ≥ JDK 15** | JEP 358 | JEP 358 (verify @pin) |
| `@Nullable`/`@NonNull`/`@NullMarked`/`@NullUnmarked` | annotations | non-null default inside `@NullMarked` | JSpecify 1.0.0 | jspecify.dev/spec @pin |
| `org.jspecify:jspecify` | GAV | `1.0.0` | released Jul 2024 | Maven Central / jspecify @pin |

---

## 3. Evidence FOR

- **First-class JDK support, GA and stable.** `Optional` (Java 8), `Objects.requireNonNull` (Java 7),
  the `*Else`/`*ElseGet` forms (Java 9), and helpful NPE messages (JEP 358, on by default since Java 15)
  are all shipped, documented, and stable at the Java 21/25 anchor — no preview/incubator status.
- **Documented design intent, not folklore.** The `Optional` Javadoc itself states the
  return-type-only intent and the value-based-class constraints; *Effective Java* 3e Items 54/55 give
  the idiom canon. Claims here trace to primary text, not blog summary.
- **Published quantitative figure (build cost of the checker lever).** The NullAway paper (Banerjee,
  Clapp, Sridharan et al., 2019, arXiv:1907.02127) reports average build-time overhead **1.15×** for
  NullAway vs **2.8×** for Infer Eradicate and **5.1×** for the Checker Framework Nullness Checker —
  the trade-off being that the Checker Framework does fuller, sound generic/key checking. *(Cite as a
  2019 published figure; corroborated by the NullAway README's "usually less than 10%" overhead claim.)*
- **Broad, current adoption signal.** JSpecify 1.0.0 (Jul 2024) is the first consensus, tool-independent
  nullness annotation set; Spring Framework 7 / Spring Boot 4 migrated to JSpecify (Nov 2025), and
  IntelliJ IDEA added first-class JSpecify support (2025.3). *(Sources: jspecify.dev release blog;
  spring.io null-safety blog; cited as adoption corroboration — note Spring 7 / Boot 4 are
  `⚠ AHEAD-OF-PIN` relative to the Java-21 anchor's typical stack; see §7.)*
- **Tool consensus.** Every major analyzer (Sonar, SpotBugs, Error Prone, plus the dedicated checkers)
  ships null/Optional rules — the industry treats null-safety as measurable and gate-able.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each lever its objection + when-NOT-to-use)

- **`Optional` is not free, and is easy to misuse.**
  - *Hardest objection:* it allocates and adds a wrapping layer; Item 55 explicitly warns it is "not
    free" and unsuited to performance-critical hot paths. Overuse (Optional fields, Optional
    parameters, `Optional<List<T>>`, `optional.get()` without a guard) recreates the very NPE risk it
    was meant to remove (now as `NoSuchElementException`).
  - *When NOT to use:* fields, method parameters, collection element types, map values, container
    return types, and hot loops. There, prefer empty collections (Item 54), overloads, or a plain
    non-null value.
- **`Objects.requireNonNull` shifts, it doesn't eliminate.**
  - *Objection:* it still throws at runtime — it makes the failure *earlier and clearer*, not absent;
    it is a runtime check, invisible to the compiler.
  - *When NOT to use:* on every parameter reflexively (noise on private/internal methods where the
    invariant is already guaranteed); for control flow (it is for programming-error invariants, not
    expected absence — use `Optional`/validation for that, key 18).
- **JEP 358 helpful messages are a diagnostic, not a defense.**
  - *Objection:* they improve the *message* after the NPE has already occurred; they prevent nothing.
  - *When NOT to use as a strategy:* relying on them in place of design (levers 1/2/4). Also a minor
    consideration: the message can expose variable/method names in logs (information-disclosure note).
- **Annotation + checker layer: real costs (intro; full neutral treatment in keys 31/32).**
  - *NullAway:* low build overhead (~1.15×) but **not sound** — it makes pragmatic local assumptions
    (e.g. about un-annotated third-party code), so it can miss some NPEs. *When NOT to use:* if you
    require a soundness guarantee.
  - *Checker Framework Nullness Checker:* **sound** ("if no warnings, no NPE at runtime") but at the
    cost of higher build time (~5.1× in the 2019 study), a steeper annotation/migration effort, and
    more false positives. *When NOT to use:* when build speed or migration budget can't absorb it.
  - *JSpecify:* annotations only — they do nothing without a checker that implements their semantics;
    tool support for the full 1.0 spec is still maturing. Each system cited to its own source; no
    crowning (NEUTRALITY) — the choice is context-dependent and owned by key 32.
- **Annotations cannot retrofit the whole world.** Un-annotated dependencies and reflection/serialization
  frameworks inject nulls the checker can't see; null-safety is strongest at your own `@NullMarked` code
  and weakest at its boundaries.
- **`null` is structural in the JLS.** None of these levers changes the language: a reference can always
  be null, and arrays/`==`/JNI/reflection can still produce one. The discipline reduces the surface; it
  does not make NPE impossible (only the sound Checker Framework path claims to, within its rules).

## 5. Current status

- **Stable core, growing ecosystem.** `Optional`, `Objects`, and JEP 358 are stable JDK features at the
  21/25 anchor; nothing here is preview or deprecated. JSR-305 (`javax.annotation.Nullable`) is
  **dormant** — do not present as current (per SOURCE-PIN stale-name note); JSpecify is the consensus
  successor.
- **The live 2024–2026 movement is consolidation of nullness annotations.** JSpecify 1.0.0 (Jul 2024)
  unified the long-fragmented annotation landscape; Spring Framework 7 / Spring Boot 4 adopted it
  (Nov 2025) and IntelliJ added first-class support (2025.3). This is the "designing NPEs out" story's
  current chapter. *(Spring 7 / Boot 4 / IntelliJ 2025.3 are ahead of the Java-21 anchor's baseline —
  cite as adoption signal, flag `⚠ AHEAD-OF-PIN`, §7.)*
- **No language-level non-null type yet.** A null-restricted/null-safe type system (associated with
  Project Valhalla's value types and null-restricted types) is exploratory and **not** at the pin —
  any mention is `⚠ AHEAD-OF-PIN` and must not be stated as a shipping feature (§7).
- **Java 25 deltas:** no change to `Optional`/`Objects`/JEP 358 semantics relevant here; the annotation
  ecosystem (a library/tool layer) is where movement happens, not the JDK API. (Confirm no 22–25 API
  additions to `Optional` at pin — §7.)

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo (pointer to `DEMO-CATALOG.md`, key 11 row):** demo name **"the nullable promo code"** —
  a storefront checkout where an `Order` may carry an **optional** discount/promo code that may be
  absent, and a customer email that must **never** be null. Java surface exercised: `Optional<T>` as a
  return type for the lookup, `Objects.requireNonNull` at the constructor boundary, JEP 358 message on
  the deliberate failure path, and `@NullMarked` + `@Nullable` (JSpecify) on the package so a checker
  proves the rest. **TRY-IT exercise:** add a new field that genuinely may be absent, annotate it, run
  the checker, and watch the build fail until the caller handles the empty case. *(If the key-11 row is
  not yet present in `DEMO-CATALOG.md`, this spec is the proposal to add it — flag to catalog owner.)*
- **Module key / path:** `08-companion-code/11_null_safety_optional/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property) | establishes the Java 21 pin | SOURCE-PIN.md runtime baseline | ☐ |
  | JDK `java.base` (`java.util.Optional`, `java.util.Objects`) | primary unit under study | Optional/Objects Javadoc SE 21 | ☑ (API verified) |
  | `org.jspecify:jspecify:1.0.0` | nullness annotations for `@NullMarked`/`@Nullable` | Maven Central / jspecify.dev | ☑ (GAV verified) |
  | NullAway + Error Prone (build-time plugin) | the checker that proves nullness | github.com/uber/NullAway | ☐ (version TO-PIN) |
  | JUnit 5 (Jupiter) | the test harness (canonical at pin) | junit.org/junit5 | ☐ (version TO-PIN) |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property; JSpecify/NullAway versions via the pin, no loose versions.
  - **Externalized config / profiles** — a config key whose value is genuinely optional (e.g.
    `storefront.promo.default-code` absent → empty `Optional`) demonstrating absence-without-null.
  - **At least one test** — asserts: empty promo yields the no-discount path; a null required-field
    argument is rejected at construction by `requireNonNull`.
  - **Observability / health surface** — a log/metric line on the empty-vs-present branch (where the
    absence decision is made) so the optional path is observable.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** a deliberate `requireNonNull` rejection of a
    null required field, plus a deliberately-triggered NPE elsewhere whose **helpful NPE message** is
    shown — proving JEP 358 names the exact null expression. This is the fault/guard policy.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `optional-return` | a lookup returning `Optional<PromoCode>` and a caller using `map/orElse` (no `get()`) | `PromoCodeService.java` |
  | `require-nonnull` | constructor boundary `this.email = Objects.requireNonNull(email, "email")` | `Order.java` |
  | `nullmarked-pkg` | `@NullMarked` on `package-info.java` + one `@Nullable` exception | `package-info.java` |

- **Run command:** `./mvnw -B test` (no service needed) — or `java -XX:+ShowCodeDetailsInExceptionMessages`
  is the default at the anchor, so the helpful message appears with no flag.
- **Build/verify command:** `./mvnw -B verify` (NullAway runs as part of compilation; a null-safety
  violation FAILS the build — the proof that lever 4 is active).
- **Expected output:** tests pass (empty-promo path + requireNonNull rejection); the deliberate-NPE demo
  prints a JEP 358 message naming the null expression; if the TRY-IT exercise is done wrong, `verify`
  fails at compile with a NullAway error pointing at the unhandled `@Nullable`.
- **Figure plan** (GUIDELINES §8; chapter class drives budget):
  - **Chapter class:** standard code-craft chapter → budget ~1–2 designed diagrams + 0–1 captured
    screenshot.
  - **Candidate designed diagram(s) + family:**
    - **Fig 11.1 — "the four levers" layered defense** (flow/layers family): a path from a value to a
      dereference, with the four levers placed on the timeline — design-time (`Optional`/Item 54),
      boundary (`requireNonNull`), build-time (annotation+checker), runtime (helpful NPE). Authored in
      HTML → rendered PNG via `05-figures/_assets/render.mjs` (never image-generated).
    - **Fig 11.2 — the `@NullMarked` zone** (region/scope family): a package marked non-null-by-default
      with one `@Nullable` exception punched through, showing where the checker does/doesn't see nulls
      (own code vs un-annotated dependency boundary).
  - **Candidate captured surface(s):** an IDE/build screenshot of a NullAway compile error (the build
    failing on an unhandled `@Nullable`) — captured at Step 4c, optional.
  - **Source trace per depicted claim:** Fig 11.1 levers → Optional Javadoc (lever 1), Objects Javadoc
    (lever 2), JSpecify spec + NullAway README (lever 4), JEP 358 (lever 3). Fig 11.2 `@NullMarked`
    default-non-null semantics → JSpecify spec @pin.

## 7. Gap-filling (verification queue)

- ⚠ **JEP 358 exact text** — the openjdk.org/jeps/358 page returns HTTP 403 to the fetch tool; the flag
  name, default-off-14/on-15, and the example message are corroborated via secondary (Baeldung) and the
  JEP bug record. Confirm verbatim against the JEP text / JDK 15 release notes at pin. → flag filed.
- ⚠ **JLS section for `null` type / reference types** — cited as JLS SE 21 §4.1; confirm exact section
  number/wording against the SE 21 spec before asserting in draft.
- ⚠ **Effective Java 3e verbatim quotes + page numbers** (Items 54, 55) — confirm wording/pages before
  block-quoting; current text is from Item summaries.
- ⚠ **Tool rule defaults/severities** — `java:S3655`/`S2789`/`S2259` existence + meaning verified via
  Sonar docs; SpotBugs `NP_NULL_ON_SOME_PATH`/`RCN_*` via detectors doc; Error Prone `NullableOptional`/
  `OptionalNotPresent`/`ReturnMissingNullable` via errorprone.info. Exact default severity/threshold per
  tool is version-pinned → `⚠ verify at pin` against each pinned tool (detail owned by keys 27/28/29/30/35).
- ⚠ **NullAway 1.15× / Checker 5.1× / Eradicate 2.8× figures** — published in arXiv:1907.02127 (2019);
  cite as a dated study figure, not a current benchmark. Confirm framing at draft.
- ⚠ **AHEAD-OF-PIN:** Spring Framework 7 / Spring Boot 4 JSpecify adoption (Nov 2025), IntelliJ 2025.3
  JSpecify support, and any Valhalla null-restricted-type work — all past/around the Java-21 anchor;
  present only as adoption signal / future direction, never as anchor-baseline fact. → flag filed.
- **Cross-ref hand-offs:** annotation-system comparison + JSR-305 legacy → key 32 (the `⚠` key); NullAway
  configuration/`AnnotatedPackages`/`@Initializer` mechanics → key 31; analyzer rule config/suppression →
  keys 27–30/35; guard-clause/validation depth → key 18.

## 8. Sources & further reading

### Primary / Official (pinned authority set)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JDK Javadoc | `java.util.Optional` (SE 21) | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html | ☑ (design intent, value-based, signatures) |
| 2 | JDK Javadoc | `java.util.Objects` (SE 21) | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html | ☑ (requireNonNull family) |
| 3 | JEP | JEP 358 — Helpful NullPointerExceptions | https://openjdk.org/jeps/358 | partial (403 to fetch; flag+default corroborated) |
| 4 | JLS | Java Language Specification SE 21 — reference types / `null` type | https://docs.oracle.com/javase/specs | ☐ (confirm §4.1) |
| 5 | Spec / repo | JSpecify nullness spec + 1.0.0 release | https://jspecify.dev/docs/spec/ · github.com/jspecify/jspecify v1.0.0 | ☑ (4 annotations, NullMarked default) |
| 6 | GAV | Maven Central `org.jspecify:jspecify:1.0.0` | https://central.sonatype.com/artifact/org.jspecify/jspecify | ☑ |
| 7 | Tool repo | NullAway (README; arXiv:1907.02127 overhead figures) | github.com/uber/NullAway · arxiv.org/pdf/1907.02127 | ☑ (overhead figures, AnnotatedPackages) |
| 8 | Tool docs | Checker Framework — Nullness Checker (sound guarantee) | https://checkerframework.org/manual/ | ☑ (soundness statement, @MonotonicNonNull/@PolyNull) |
| 9 | Tool docs | Sonar rules S2789 / S3655 / S2259 | https://rules.sonarsource.com | ☑ (rule meaning) |
| 10 | Tool docs | SpotBugs detectors (NP_NULL_ON_SOME_PATH, RCN_*) | https://spotbugs.readthedocs.io/en/stable/detectors.html | ☑ (pattern existence) |
| 11 | Tool docs | Error Prone bug patterns (NullableOptional, OptionalNotPresent, ReturnMissingNullable) | https://errorprone.info/bugpatterns | ☑ (pattern meaning) |
| 12 | Book canon | *Effective Java* 3e — Items 54 & 55 | print | ☑ guidance; ⚠ verbatim/page at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Spring blog | Null Safety in Spring apps with JSpecify and NullAway | https://spring.io/blog/2025/03/10/null-safety-in-spring-apps-with-jspecify-and-null-away/ | ☑ (adoption; AHEAD-OF-PIN) |
| 2 | Baeldung | Helpful NullPointerExceptions in Java (JEP 358) | https://www.baeldung.com/java-14-nullpointerexception | corroboration (JEP 358 default/flag) |
| 3 | Baeldung | A Practical Guide to Null-Safety in Java With JSpecify | https://www.baeldung.com/java-jspecify-null-safety | corroboration |
| 4 | Scott Logic | Using JSpecify 1.0 to Tame Nulls in Java | https://blog.scottlogic.com/2024/12/18/taming-nullness-in-java-with-jspecify.html | corroboration |

> Source-quality order: JDK Javadoc/JLS/JEP → each tool's own docs/repo at its pin → the published
> NullAway paper → the named book canon (dated) → quality secondary blogs (corroboration only). JSR-305
> demoted as dormant; Spring 7 / Boot 4 cited as adoption signal only (AHEAD-OF-PIN).

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | Effective Java Items 54/55 | web search | Item 54 (empty not null), Item 55 (Optional return discipline; no container-in-Optional; no Optional map value; perf caveat) |
| 2 | Optional Javadoc design intent / value-based | web search + fetch SE 21 Javadoc | verbatim API Note (return-type intent), value-based-class warning, full method signatures |
| 3 | JSpecify 1.0 annotations/semantics | web search | 4 annotations; @NullMarked = non-null default; type-use; type-use vs Spring deprecated |
| 4 | JSpecify GAV | web search | org.jspecify:jspecify:1.0.0; package org.jspecify.annotations; released Jul 2024 |
| 5 | JEP 358 | fetch (403) + web search | flag -XX:+ShowCodeDetailsInExceptionMessages; off in 14, on by default 15; message shape |
| 6 | Objects.requireNonNull family | web search | requireNonNull (J7), Supplier msg (J8), requireNonNullElse/ElseGet (J9); fail-fast |
| 7 | NullAway / Checker overhead | web search | 1.15× NullAway vs 2.8× Eradicate vs 5.1× Checker (arXiv:1907.02127); Checker sound; README <10% |
| 8 | Sonar S2789/S3655/S2259 | web search | S3655 (access after isPresent), S2789 (Optional not null), S2259 (null deref) |
| 9 | SpotBugs / Error Prone null patterns | web search | NP_NULL_ON_SOME_PATH, RCN_*; NullableOptional, OptionalNotPresent, ReturnMissingNullable |
| 10 | JSpecify/Spring adoption | web search | Spring 7 / Boot 4 (Nov 2025), IntelliJ 2025.3 — AHEAD-OF-PIN adoption signal |

---
## Learnings & pipeline suggestions
- **Layered-defense shape for "designing X out" chapters.** Null-safety naturally decomposes into
  *design-time → boundary → build-time → runtime* levers; this "layered defense, each lever with its own
  when-NOT-to-use" shape is reusable for similar topics (key 12 error handling, key 18 defensive coding,
  the security keys). Propose as a template note. → PIPELINE-LEARNINGS.md.
- **403 on openjdk.org/jeps to the fetch tool.** JEP pages return 403 to WebFetch (hit on JEP 358);
  JEP facts had to be corroborated via the JEP bug record + secondary. Standing note: verify JEP verbatim
  text against the JDK release notes / a non-403 mirror at pin; flag any JEP fact not read from primary.
- **AHEAD-OF-PIN adoption trap.** The most current null-safety story (JSpecify in Spring 7 / Boot 4,
  IntelliJ 2025.3) is past the Java-21 anchor; the right move is to cite it as *adoption signal / future
  direction*, never as anchor-baseline fact. Reinforces the SOURCE-PIN AHEAD-OF-PIN discipline.
- **Cross-ref discipline for cluster 11/31/32.** Keep tool-comparison + JSR-305 legacy in key 32 (the
  `⚠` key), NullAway config mechanics in key 31; key 11 stays the design/language chapter. Recorded for
  the merge-cluster decision.
