# RESEARCH DOSSIER — Java Code Quality Book

> Tier-B, Part-II code-craft dossier. Every specific fact is traced to a pinned authority in
> `00-strategy/SOURCE-PIN.md` (the JDK API docs at the anchor LTS, the JLS/JEPs, a named book edition, or a
> tool's own pinned docs/repo). Tool versions/thresholds are **TO-PIN** in SOURCE-PIN.md, so rule IDs are
> cited by their stable ID with the tool named, and exact default thresholds / version numbers are marked
> `⚠ verify at pin`. Anything that could not be confirmed against a primary source is marked `⚠ UNVERIFIED`
> in §7 and (where material) flagged to `09-flags/`.

---

## Topic
- **Key:** 09 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Designing clear APIs & method contracts (at the code level)
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B · **Depth band:** Standard (code-craft + multi-tool trace)
- **Cmp:** (not a `⚠` key) — but it names several tools (Error Prone, SpotBugs, PMD, SonarSource, JSpecify);
  every cross-tool fact is cited to that tool's own pinned source, and the NEUTRALITY non-crowning rule
  still applies (no tool is declared "best" for contract enforcement).
- **Java code quality pin (runtime):** Java **21 LTS** anchor; Java **25 LTS** deltas called out. (SOURCE-PIN runtime baseline.)
- **Primary dependency / source unit(s):**
  - **JDK 21 API — `java.util.Objects`** (`requireNonNull`, `requireNonNullElse{,Get}`, `checkIndex`, `checkFromToIndex`, `checkFromIndexSize`) — the in-JDK precondition vocabulary. Confirmed signatures + `since` in §2.5.
  - **`java.util.Optional`** (return-type contract for "value may be absent") — Effective Java Item 55.
  - **JLS SE 21** — varargs (§8.4.1), overload resolution (§15.12.2), `final` parameters, access modifiers (§6.6).
  - **JEPs:** records JEP 395 (final Java 16), sealed types JEP 409 (Java 17), pattern matching for `switch` JEP 441 (Java 21) — used as contract-clarifying constructs. JEP 467 (Markdown doc comments) is **Java 23 → AHEAD-OF-PIN** (see §7).
  - **Effective Java, 3rd ed. (Bloch, 2018)** — the API-design canon: Ch 4 (classes/interfaces, Items 15–17), Ch 8 (methods, **Items 49–56**). This is the spine.
  - **Tool rule IDs (each cited to its own tool):** Error Prone `CheckReturnValue` / `CanIgnoreReturnValue` / `MissingOverride` / `InconsistentOverloads` / `ParameterName` / `NullArgumentForNonNullParameter`; SpotBugs `NP_NONNULL_PARAM_VIOLATION` / `RV_RETURN_VALUE_IGNORED` / `EI_EXPOSE_REP` / `EI_EXPOSE_REP2`; PMD `MethodReturnsInternalArray` / `ArrayIsStoredDirectly` / `AvoidReassigningParameters` / `UseVarargs`; SonarSource `java:S2201` (return value ignored) / `java:S1226` (parameters reassigned); JSpecify `@Nullable`/`@NonNull`/`@NullMarked`/`@NullUnmarked`.
- **Canonical doc page(s):**
  - `java.util.Objects` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html
  - Effective Java 3e, Ch 8 — https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/ch8.xhtml
  - Error Prone bug patterns — https://errorprone.info/bugpatterns
  - SpotBugs bug descriptions — https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html
  - PMD Java rules — https://pmd.github.io/pmd/pmd_rules_java.html
  - JSpecify spec/user guide — https://jspecify.dev/docs/spec/ , https://jspecify.dev/docs/user-guide/
- **Canonical source path(s):** to be set under the per-tool fetch dirs in SOURCE-PIN.md once `/pin-source`
  fixes each `TO-PIN` row (sonar-java `IgnoredReturnValueCheck.java`; error-prone `CheckReturnValue.java`).

---

## 1. Core definition & purpose

**Central claim.** A method (or class) is not just an implementation — it is a **contract**: a promise about
what callers must supply (preconditions), what the method guarantees in return (postconditions), and what it
will do when the promise is broken (the failure model). "Designing a clear API" at the code level means making
that contract **explicit, hard to misuse, and machine-checkable** — through signatures, types, naming,
preconditions enforced early, and documentation that states the parts the type system cannot.

This sits squarely in ISO/IEC 25010 **Maintainability → Reusability** and **Analysability** (key 01's spine):
a clear contract is what lets one part of a system be used elsewhere without reading its implementation. It is
the *code-level* counterpart to key 60 (library/shared-module API quality, semantic versioning, binary
compatibility) — key 09 is the in-the-small craft; key 60 is the in-the-large evolution discipline.

**The two halves of "contract."**
1. **What the type system can carry** — visibility, immutability, parameter/return types, `Optional`,
   nullness annotations, generics. These are enforced by the compiler or a static checker; a misuse is a
   *compile error*, the cheapest possible feedback.
2. **What only documentation/runtime can carry** — preconditions beyond the type (`index >= 0`, "list must
   be non-empty"), thrown-exception semantics, thread-safety, and side effects. These are stated in Javadoc
   (`@param`/`@return`/`@throws`/`@implSpec`) and enforced at runtime by fail-fast precondition checks.

**The guiding maxim (Bloch).** *Effective Java* Item 49 frames the precondition half directly: "You should
clearly document all such restrictions and enforce them with checks at the beginning of the method body."
*(Source: Effective Java 3e, Item 49.)* And the API-design north star, Item 51 / the book's API chapters:
APIs should be **easy to use correctly and hard to use incorrectly**.

**Where it sits in the architecture.** This is a *build-time / design-time* concern with a *runtime* tail: the
signature and types are fixed at compile time (and checked by analyzers in CI); the precondition checks fire at
runtime when a caller breaks the contract. The chapter lives between key 07 (naming/structure), key 11
(null-safety), key 12 (error handling), and key 15 (`equals`/`hashCode` contracts) — all contract-shaped.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Minimize accessibility — the contract surface (Effective Java Items 15–16)

The smallest API is the easiest to keep correct. *Effective Java* Item 15 ("Minimize the accessibility of
classes and members"): each class or member should be **as inaccessible as possible**; a top-level class or
interface that can be package-private should be. Item 16: in public classes, use accessor methods, not public
fields. *(Source: Effective Java 3e, Items 15–16.)* Mechanism: every `public`/`protected` member is a promise
you must keep forever (key 60's binary-compatibility burden); shrinking the surface shrinks the contract you
are bound to. JLS SE 21 §6.6 defines the access-control levels this rests on.

### 2.2 Design method signatures carefully (Item 51), overloading (52), varargs (53)

- **Signatures (Item 51):** choose method names carefully; avoid long parameter lists (Bloch's guidance: aim
  for **four parameters or fewer**); for long lists prefer a builder (Item 2), a helper "parameter object," or
  breaking the method up; favor interfaces over classes for parameter types; prefer two-element enums over
  `boolean` parameters when the meaning isn't obvious at the call site. *(Source: Effective Java 3e, Item 51.)*
- **Overloading (Item 52):** "Use overloading judiciously." Overload resolution is **static** (compile-time,
  by declared type — JLS SE 21 §15.12.2), whereas overriding is dynamic; this mismatch surprises callers, so
  avoid two overloads with the same parameter count where a single argument could match either.
- **Varargs (Item 53):** "Use varargs judiciously." Varargs (JLS SE 21 §8.4.1) allocate an array per call and
  the "require at least one argument" case is awkward; Bloch's pattern is an explicit first parameter plus
  varargs for the rest. Tool echo: PMD **`UseVarargs`** suggests varargs where an array parameter is used;
  SpotBugs/Error Prone flag confusing varargs/array-vs-vararg calls. *(Tool facts: PMD docs; Error Prone docs.)*

### 2.3 Fail fast on bad input — preconditions enforced early (Item 49)

The contract's precondition half is enforced by **checking parameters at the top of the method body** and
throwing immediately on violation. This converts "wrong result far away" into "clear exception at the call
site." The JDK ships the vocabulary (§2.5): `Objects.requireNonNull` for null checks; `Objects.checkIndex` /
`checkFromToIndex` / `checkFromIndexSize` for range checks; explicit `if (…) throw new
IllegalArgumentException(…)` for everything else. The documented exception conventions: `NullPointerException`
for null, `IndexOutOfBoundsException` for bad index, `IllegalArgumentException` for other bad values,
`IllegalStateException` for bad object state. *(Source: Effective Java 3e, Item 49; JDK `Objects` Javadoc.)*

Cross-ref: heavier declarative precondition checking (Jakarta Bean Validation `@NotNull`/`@Size`, guard
clauses) is **key 18**'s territory; key 09 owns the in-method fail-fast idiom and `Objects` checks.

### 2.4 Return-type contracts: empty over null (Item 54), Optional judiciously (Item 55)

- **Item 54 — "Return empty collections or arrays, not nulls."** A method that can return "no elements" should
  return an empty collection/array, never `null`, so callers need no null-guard. Tool echo: SpotBugs flags
  null returns from methods documented non-null. *(Source: Effective Java 3e, Item 54.)*
- **Item 55 — "Return optionals judiciously."** `Optional<T>` (return type only) signals "a result may be
  absent" in the *type*, making the contract self-documenting. Caveats Bloch states: never return
  `Optional` of a boxed primitive (use `OptionalInt`/`OptionalLong`/`OptionalDouble`); never use `Optional`
  for collection/array element types (return empty); avoid `Optional` fields and parameters. *(Source:
  Effective Java 3e, Item 55.)* SonarSource `java:S2201` enforces that the returned `Optional` is actually
  used. Cross-ref key 11 (null-safety/Optional discipline) for the design rationale.

### 2.5 The JDK precondition API — `java.util.Objects` (verified signatures @ JDK 21)

Confirmed against the Java SE 21 `Objects` Javadoc (canonical page above). `requireNonNull` is documented as
"designed primarily for parameter validation in methods and constructors."

| Method | Signature | `since` | Throws on violation |
|---|---|---|---|
| `requireNonNull(obj)` | `static <T> T requireNonNull(T obj)` | 1.7 | `NullPointerException` |
| `requireNonNull(obj, message)` | `static <T> T requireNonNull(T obj, String message)` | 1.7 | `NPE` (custom message) |
| `requireNonNull(obj, supplier)` | `static <T> T requireNonNull(T obj, Supplier<String> msgSupplier)` | 1.8 | `NPE` (lazy message) |
| `requireNonNullElse(obj, default)` | `static <T> T requireNonNullElse(T obj, T defaultObj)` | 9 | `NPE` if both null |
| `requireNonNullElseGet(obj, supplier)` | `static <T> T requireNonNullElseGet(T obj, Supplier<? extends T> supplier)` | 9 | `NPE` if both null |
| `checkIndex(index, length)` | `static int checkIndex(int index, int length)` | 9 | `IndexOutOfBoundsException` |
| `checkIndex(long, long)` | `static long checkIndex(long index, long length)` | 16 | `IOOBE` |
| `checkFromToIndex(from, to, length)` | `static int checkFromToIndex(int fromIndex, int toIndex, int length)` | 9 | `IOOBE` |
| `checkFromIndexSize(from, size, length)` | `static int checkFromIndexSize(int fromIndex, int size, int length)` | 9 | `IOOBE` |
| `checkFromToIndex/checkFromIndexSize (long)` | `…(long, long, long)` | 16 | `IOOBE` |

*(All rows verified against the JDK 21 `Objects` API doc. The `long` index-check overloads landed in JDK 16,
so they are available at the 21 anchor.)*

### 2.6 Make immutable & defensive — don't leak internal representation (Items 17, 50)

- **Item 17 — "Minimize mutability."** Immutable value types have the simplest possible contract (state never
  changes), are inherently thread-safe (key 21), and need no defensive copying on the way out. Records
  (JEP 395, final Java 16) are the modern shorthand for transparent immutable carriers; cross-ref key 10.
- **Item 50 — "Make defensive copies when needed."** When a class stores or returns a mutable object
  (array, `Date`, mutable collection), copy on the way **in** (constructor/setter) and on the way **out**
  (getter) so callers cannot mutate internal state. *(Source: Effective Java 3e, Items 17, 50.)* This is the
  single most tool-corroborated contract rule:

  - SpotBugs **`EI_EXPOSE_REP`** — a getter returns a reference to a mutable internal field (representation
    exposure); **`EI_EXPOSE_REP2`** — a constructor/setter stores an externally-given mutable object directly.
    *(Source: SpotBugs bug descriptions; FindReturnRef detector.)*
  - PMD **`MethodReturnsInternalArray`** ("exposing internal arrays… violates encapsulation") and
    **`ArrayIsStoredDirectly`** ("constructors and methods receiving arrays should clone… and store the copy").
    *(Source: PMD Java design rules.)*

### 2.7 Encode the contract in the type system: nullness (JSpecify) and `@Override`

- **Nullness as contract (JSpecify).** JSpecify standardizes `@Nullable` (may be null), `@NonNull` (never
  null), `@NullMarked` (everything in this scope is non-null unless marked `@Nullable`), `@NullUnmarked`.
  JSpecify is a **specification, not a checker** — it gives a tool-agnostic vocabulary so a method's null
  contract lives in its signature and any conforming checker (NullAway, Checker Framework, the IDE) can verify
  it. *(Source: JSpecify spec + user guide.)* Cross-ref keys 31 (NullAway enforcement) and 32 (the annotation
  landscape) — key 09 owns the *design* statement ("nullness is part of the signature"), not the tooling.
- **`@Override` as a re-declaration of contract.** Error Prone **`MissingOverride`** (WARNING) flags a method
  that overrides a supertype method without `@Override`; the annotation makes the compiler verify the override
  is real, catching signature drift. *(Source: Error Prone bug patterns.)*

### 2.8 Return values are part of the contract — make them un-ignorable

A method whose return value carries the result of the call (a check, a new immutable value) is misused when
the caller drops it. The contract can be **annotated and enforced**:

- Error Prone **`CheckReturnValue`** (ERROR): the result of an annotated call must be used.
  **`CanIgnoreReturnValue`** (SUGGESTION) exempts methods that return `this` (builders) or an input param.
  Error Prone validates the pair: the two must not both be on one method, and neither belongs on a
  `void` method (`CheckReturnValue` bug pattern). **`NoCanIgnoreReturnValueOnClasses`** warns that
  `@CanIgnoreReturnValue` on a class over-matches. *(Source: Error Prone docs + `CheckReturnValue.md`.)*
- SpotBugs **`RV_RETURN_VALUE_IGNORED`** (and `…_NO_SIDE_EFFECT`) flags ignored return values.
- SonarSource **`java:S2201`** — "Return values should not be ignored when function calls don't have any
  side effects" (currently scoped, per Sonar's own docs, to a fixed list of immutable types: `String`,
  `Boolean`, `Integer`, `Double`, `Float`, `Byte`, `Character`, `Short`, `StackTraceElement` — a documented
  *limitation*, see §4). *(Source: sonar-java `IgnoredReturnValueCheck`; Sonar community/docs.)*

### 2.9 Don't silently reassign parameters

A parameter is part of the inbound contract; overwriting it before reading hides the caller's value and
confuses readers. PMD **`AvoidReassigningParameters`** and SonarSource **`java:S1226`** ("method parameters,
caught exceptions and foreach variables' initial values should not be ignored" — raises only when reassigned
without being read first, per the updated RSPEC-1226). `final` parameters (JLS SE 21) make this a compile
constraint. *(Source: PMD docs; SonarSource S1226 RSPEC + GitHub.)*

### 2.10 Document the part types can't carry (Item 56) — Javadoc AS contract

*Effective Java* Item 56 — "Write doc comments for all exposed API elements." The doc comment **is** the
contract for everything the signature cannot express. The Javadoc conventions (Oracle Javadoc guide):
- `@param <name> <desc>` — one per parameter, in declaration order; states preconditions on that argument.
- `@return <desc>` — required for every non-`void`, non-constructor method (omit for `void`/constructors),
  even if redundant, so the return contract is findable.
- `@throws <Exception> <when>` — for every checked exception and for unchecked exceptions a caller might
  reasonably catch; the "when" clause is the documented precondition-violation behavior.
- The **first sentence** becomes the **summary fragment** in generated docs — keep it a self-contained
  description of the method's contract.
- `@implSpec` (Java 8+, non-standard tag) — documents the contract between a method and its subclasses (what
  an overrider may rely on); `@apiNote`/`@implNote` separate API-audience from implementor notes.
  *(Source: Oracle Javadoc guide; Effective Java 3e Item 56; Parlog "Kinds of Comments".)*
- ⚠ **AHEAD-OF-PIN:** **JEP 467 (Markdown documentation comments)** changes how doc comments are *authored*
  (`///` Markdown) — it targets **JDK 23**, which is past the Java 21 anchor. Do not present Markdown doc
  comments as available at the pin; note as a 23+ delta only. See §7.

> Cross-ref key 17 (comments/Javadoc as a contested practice) and key 89 (Javadoc as contract at the
> docs-quality level). Key 09 states the *contract* role; key 17 owns the comment-style debate.

### 2.11 Reference units (rule IDs / API signatures / JEPs) — table

| Name | Type | Default / status | Fixed early? | Source |
|---|---|---|---|---|
| `Objects.requireNonNull(T)` | JDK API | since 1.7; throws NPE | compile-resolved, runtime check | JDK 21 `Objects` Javadoc |
| `Objects.checkIndex(int,int)` | JDK API | since 9; throws IOOBE | runtime check | JDK 21 `Objects` Javadoc |
| `Optional<T>` (return type) | JDK API | EJ Item 55: return-only | compile-time type | JDK 21 `Optional`; EJ 3e |
| EJ Item 49 "check params" | book principle | — | design-time | Effective Java 3e |
| EJ Item 50 "defensive copies" | book principle | — | design-time | Effective Java 3e |
| EJ Item 51 "≤4 params, no boolean" | book guidance | — | design-time | Effective Java 3e |
| EJ Item 54/55 empty-not-null / Optional | book principle | — | design-time | Effective Java 3e |
| EJ Item 56 "doc all exposed API" | book principle | — | design-time | Effective Java 3e |
| Error Prone `CheckReturnValue` | rule ID | ERROR | build-time | errorprone.info/bugpatterns |
| Error Prone `CanIgnoreReturnValue` | rule ID | SUGGESTION | build-time | errorprone.info/bugpatterns |
| Error Prone `MissingOverride` | rule ID | WARNING | build-time | errorprone.info/bugpatterns |
| Error Prone `InconsistentOverloads` | rule ID | WARNING | build-time | errorprone.info/bugpatterns |
| Error Prone `ParameterName` | rule ID | WARNING | build-time | errorprone.info/bugpatterns |
| Error Prone `NullArgumentForNonNullParameter` | rule ID | ERROR | build-time | errorprone.info/bugpatterns |
| SpotBugs `NP_NONNULL_PARAM_VIOLATION` | bug pattern | — | build-time | SpotBugs bug descriptions |
| SpotBugs `RV_RETURN_VALUE_IGNORED` | bug pattern | — | build-time | SpotBugs bug descriptions |
| SpotBugs `EI_EXPOSE_REP` / `EI_EXPOSE_REP2` | bug pattern | — | build-time | SpotBugs bug descriptions |
| PMD `MethodReturnsInternalArray` | rule | design ruleset | build-time | PMD Java rules |
| PMD `ArrayIsStoredDirectly` | rule | design ruleset | build-time | PMD Java rules |
| PMD `AvoidReassigningParameters` | rule | design ruleset | build-time | PMD Java rules |
| PMD `UseVarargs` | rule | design ruleset | build-time | PMD Java rules |
| SonarSource `java:S2201` | rule | scoped (see §4) | build-time | rules.sonarsource.com; sonar-java |
| SonarSource `java:S1226` | rule | reassign-before-read | build-time | rules.sonarsource.com (RSPEC-1226) |
| JSpecify `@Nullable`/`@NonNull`/`@NullMarked` | annotations | spec, tool-agnostic | compile/check-time | jspecify.dev spec |
| records JEP 395 | language | final Java 16 | compile-time | openjdk.org/jeps/395 |
| sealed types JEP 409 | language | Java 17 | compile-time | openjdk.org/jeps/409 |
| pattern matching `switch` JEP 441 | language | Java 21 | compile-time | openjdk.org/jeps/441 |
| Markdown doc comments JEP 467 | language | **JDK 23 — AHEAD-OF-PIN** | — | openjdk.org/jeps/467 |

> Exact tool default severities and version-specific thresholds are `⚠ verify at pin` until `/pin-source`
> fixes each TO-PIN row. Rule IDs are stable identifiers and are cited as such; the severities above are taken
> from each tool's own current docs and must be re-confirmed at the pinned tool version.

---

## 3. Evidence FOR

- **Verified JDK API surface.** The entire `Objects` precondition family (§2.5) is confirmed against the
  Java SE 21 Javadoc with exact signatures and `since` versions — first-class, GA, in `java.base`. No invention.
- **The canon is explicit and structured.** *Effective Java* 3e devotes a whole chapter (Ch 8, Items 49–56)
  to method/API contracts, plus Items 15–17, 50–51 — i.e. contract design is a named, organized discipline,
  not folklore. The "easy to use correctly, hard to use incorrectly" maxim is the chapter's spine.
- **Multi-tool corroboration (each cited to its own source).** Every contract rule of thumb has a primary
  enforcer: defensive copying → SpotBugs `EI_EXPOSE_REP(2)` + PMD `MethodReturnsInternalArray`/
  `ArrayIsStoredDirectly`; un-ignorable returns → Error Prone `CheckReturnValue` + SpotBugs
  `RV_RETURN_VALUE_IGNORED` + Sonar `java:S2201`; no param reassignment → PMD `AvoidReassigningParameters` +
  Sonar `java:S1226`; real overrides → Error Prone `MissingOverride`. This shows the design rules are
  *machine-checkable*, the chapter's load-bearing point.
- **Industry adoption of the annotation contract.** JSpecify 1.0 standardizes nullness annotations and IntelliJ
  IDEA (2025.3) makes JSpecify its preferred nullability source — evidence the "nullness in the signature"
  idea is consolidating across the ecosystem. *(Source: JSpecify; JetBrains blog — AHEAD-OF-PIN re: 2025.3 IDE.)*
- **Maturity.** `Objects`, `Optional`, `@Override` are stable JDK GA features; Error Prone, SpotBugs, PMD,
  Sonar are mature, widely-deployed analyzers with these rules in their default/standard rulesets.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **Runtime precondition checks are not free, and not complete.** `Objects.requireNonNull` + explicit guards
  add a check on every call and shift the failure to runtime, not compile time. For a hot inner-loop private
  method whose callers are all in-module and already validated, the checks can be redundant cost.
  **When NOT to use:** Bloch's own caveat — for private/package-private methods you control all callers of,
  prefer `assert` over a thrown exception, and you may skip checks where validation happens implicitly during
  the computation (Item 49). Over-checking every internal method is ceremony.
- **`Optional` has real costs (Item 55's own caveats).** It is a heap object with a wrapping cost; it is **not**
  `Serializable`; using it for fields, parameters, or collection elements is an anti-pattern Bloch warns
  against. **When NOT to use:** performance-critical paths, fields, parameters, and any boxed-primitive case
  (use `OptionalInt`/etc.). Returning `Optional` where an empty collection or a documented sentinel is clearer
  over-engineers the contract.
- **Defensive copying can be expensive or wrong.** Copying large collections/arrays on every getter is a real
  cost; and SpotBugs' own issue tracker notes `.clone()` on a multi-dimensional or element-mutable array is a
  *shallow* copy that does not actually protect the representation (SpotBugs issue #4003 / #2898). **When NOT
  to use:** when the stored type is already immutable (a record of immutables, an unmodifiable view), defensive
  copies are pure overhead; reach for immutability (Item 17) instead.
- **Tool rules have documented scope limits — not full coverage.** SonarSource's own docs state `java:S2201`
  only checks a **fixed list** of immutable return types (`String`, `Boolean`, `Integer`, `Double`, `Float`,
  `Byte`, `Character`, `Short`, `StackTraceElement`) — it will miss most ignored returns. Error Prone's
  `CheckReturnValue` only fires where the annotation is present. So "the analyzer enforces my contract" is true
  only for the annotated/scoped subset; the rest still rests on review and documentation.
- **`@CheckReturnValue`/`@CanIgnoreReturnValue` and `@Nullable` are not one standard.** There are multiple
  competing annotation packages (Error Prone's `com.google.errorprone.annotations`, JSR-305
  `javax.annotation` — dormant, JSpecify `org.jspecify.annotations`). Mixing them, or annotating a library and
  hoping every consumer's checker honors it, is fragile. JSpecify is the consolidation effort but adoption is
  partial. (Neutral framing: each annotation set targets a different checker; JSpecify aims to be the shared
  vocabulary — cross-ref key 32, the landscape chapter, for the full picture; no set is crowned here.)
- **Documentation contracts drift.** Javadoc `@param`/`@throws` can silently disconnect from the code
  (Error Prone `ParameterName` and `OverridingMethodInconsistentArgumentNames` catch *some* of this, not all).
  A precise-looking doc comment that no longer matches the method is worse than none — the comments-rot
  objection (cross-ref key 17, contested).
- **Too small an API can over-fragment.** Minimizing accessibility and parameter counts can push logic into
  many tiny methods/types (the over-decomposition objection from *A Philosophy of Software Design*, raised in
  key 03). Contract clarity is a balance, not "always smaller."

> Neutral-survey note: this chapter names several analyzers as *enforcers of the same design rules*, not as
> rivals. Where two tools cover the same contract (e.g. ignored-return), each is cited to its own docs and the
> overlap/layering question is deferred to key 37 (comparing & layering analyzers). No tool is crowned.

---

## 5. Current status

- **JDK precondition + return-type APIs:** stable/GA at the Java 21 anchor and unchanged through Java 25 for the
  members used here; `long` index-check overloads (since 16) are available at 21. No deprecations affecting this
  chapter.
- **Language constructs that sharpen contracts are still expanding:** records (final 16), sealed types (17),
  pattern matching for `switch` (final 21) are GA at the anchor; record patterns (Java 21, JEP 440) and further
  pattern-matching work continue toward 25 — relevant where a sealed+pattern contract replaces an
  `instanceof` ladder (cross-ref key 13). **JEP 467 Markdown doc comments (JDK 23)** is AHEAD-OF-PIN.
- **Nullness contracts are consolidating around JSpecify 1.0** (released; tool-agnostic spec), with growing IDE
  and checker support — the direction of travel for "nullness in the signature." Adoption is partial, not
  universal (key 32).
- **The named analyzer rules are stable** in their tools' current lines; exact rule severities/defaults are
  `⚠ verify at pin` pending `/pin-source`. SpotBugs `EI_EXPOSE_REP` family is under active refinement (open
  issues on shallow-copy detection), but the rules themselves are long-standing.
- **Effective Java 3e (2018)** remains the canonical edition; its Item numbering (49–56 for methods) is current.
  Records/sealed types post-date 3e and are noted as post-3e where they change an item's advice (e.g. Item 17
  immutability now has records).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

> SPEC, not code. Every API/rule named below traces to §2/§3 or carries a flag. Lands under
> `08-companion-code/09_api_method_contracts/`, inherits the pin property, builds green under `./mvnw -B verify`.

- **Catalog demo:** add/point to this `09_api_method_contracts` row in `DEMO-CATALOG.md`.
  - **Demo name:** *"The contract-tight `MoneyTransfer` service API."* A small domain service whose public
    method `transfer(...)` demonstrates every contract surface: minimized accessibility, fail-fast
    preconditions, an immutable value type for the result, an `Optional` lookup return, a defensive copy, full
    Javadoc, and a deliberate contract-violation failure path.
  - **Java surface it exercises:** `Objects.requireNonNull` / `Objects.checkIndex`; a `record` (immutable
    value); `Optional<Account>` return; `@Override` with `@Nullable`/`@NullMarked` (JSpecify); Javadoc
    `@param`/`@return`/`@throws`/`@implSpec`. Static-analysis gate runs Error Prone (`CheckReturnValue`,
    `MissingOverride`), SpotBugs (`EI_EXPOSE_REP2`), and PMD (`AvoidReassigningParameters`,
    `MethodReturnsInternalArray`) so the module's own build proves the contracts are machine-checked.
  - **TRY-IT exercise:** "Remove the `Objects.requireNonNull` guard and the `@CheckReturnValue` annotation,
    pass `null`, and ignore the returned `Optional` — observe (a) the late `NullPointerException` deep in the
    call vs the early fail-fast, and (b) the Error Prone build failure. Then restore them."
- **Module key / path:** `08-companion-code/09_api_method_contracts/`
- **Intended dependencies (verified @ pin where the JDK; tool GAVs are `⚠ verify at pin`):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @ pin |
  |---|---|---|---|
  | Java 21 toolchain (inherited pin property) | establishes the pin | SOURCE-PIN runtime baseline | ☑ (anchor) |
  | `java.util.Objects` / `Optional` (JDK, no GAV) | primary unit under study | JDK 21 Javadoc | ☑ |
  | `org.jspecify:jspecify` (nullness annotations) | nullness contract in signatures | jspecify.dev | ☐ ⚠ verify at pin |
  | `com.google.errorprone:error_prone_annotations` + Error Prone compiler | un-ignorable returns / `@Override` | errorprone.info | ☐ ⚠ verify at pin |
  | `com.github.spotbugs:spotbugs-maven-plugin` | rep-exposure detection | spotbugs.github.io | ☐ ⚠ verify at pin |
  | `net.sourceforge.pmd` (pmd) | param-reassign / internal-array rules | pmd.github.io | ☐ ⚠ verify at pin |
  | JUnit 5 (Jupiter) + AssertJ | the test harness | junit.org/junit5 | ☐ ⚠ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java 21 pin property; no loose tool versions.
  - **Externalized config / profiles** — analyzer rulesets externalized (Checkstyle/PMD/SpotBugs config files,
    Error Prone severity flags), not inline; name each ruleset file.
  - **At least one test** — `transferRejectsNullAccount()` asserts the fail-fast `NullPointerException`/`IAE`
    message; `lookupReturnsEmptyOptionalWhenAbsent()` asserts the empty-`Optional` contract.
  - **Observability / health surface** — log the precondition-violation at the boundary (cross-ref key 106);
    a metric counter for rejected-by-contract calls (illustrative, key 107).
  - **Explicit failure path (HONEST-LIMITATIONS floor):** the deliberate contract violation — passing `null`
    and ignoring the return — triggering both the runtime fail-fast and the build-time analyzer failure. Proves
    "the contract is enforced twice: at runtime by the guard, at build time by the analyzer."
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `precondition-guards` | `Objects.requireNonNull` + `checkIndex` at method top (Item 49) | `MoneyTransferService.java` |
  | `optional-return` | `Optional<Account>` lookup return (Item 55) | `AccountRepository.java` |
  | `defensive-copy` | copy-in/copy-out of a mutable field (Item 50) | `TransferBatch.java` |
  | `javadoc-contract` | `@param`/`@return`/`@throws`/`@implSpec` doc comment (Item 56) | `MoneyTransferService.java` |
  | `nullness-marked` | `@NullMarked` package + `@Nullable` on one param (JSpecify) | `package-info.java` |

- **Run command:** `./mvnw -B exec:java` (or the JUnit run) — no external service required.
- **Build/verify command:** `./mvnw -B verify` (must also run Error Prone + SpotBugs + PMD as part of verify).
- **Expected output:** tests pass (≥2 assertions green); `verify` is green with contracts intact; when the
  TRY-IT edit removes the guard/annotation, `verify` fails at the Error Prone/SpotBugs step **and** the runtime
  path throws an early `NullPointerException` with the documented message.
- **Figure plan** (GUIDELINES §8 — figures load-bearing; per-chapter budget):
  - **Chapter class:** code-craft chapter (Part II) → small image budget: **1–2 designed diagrams + 1 captured
    surface**. Not a zero-figure chapter (the contract layering is genuinely spatial).
  - **Candidate designed diagram(s) + family:**
    - **Fig 09.1 — "The two halves of a method contract"** (concept/flow family): inbound preconditions
      (`@param`, `requireNonNull`, `checkIndex`) → method body → outbound postconditions (`@return`,
      empty-not-null, `Optional`, defensive copy) → failure model (`@throws`: NPE/IAE/IOOBE/ISE). Each label
      traced to JDK `Objects` Javadoc / EJ Items 49–56. HTML → cropped PNG via `05-figures/_assets/render.mjs`.
    - **Fig 09.2 — "Where each contract rule is enforced"** (matrix/layer family): rows = contract concerns
      (null, range, return-value-used, no-rep-exposure, no-param-reassign, real-override); columns = enforcement
      layer (type system / runtime guard / Error Prone / SpotBugs / PMD / Sonar / Javadoc+review). Each cell
      traced to that tool's pinned doc. Shows "machine-checked vs review-only" without crowning any tool.
  - **Candidate captured surface(s):** an IDE/CI screenshot of the Error Prone or SpotBugs build failure on the
    contract-violation TRY-IT (the `CheckReturnValue` / `EI_EXPOSE_REP2` finding) — captured from the companion
    module's own build, not invented.
  - **Source trace per depicted claim:** Fig 09.1 labels → JDK 21 `Objects` Javadoc + EJ 3e Items 49–56;
    Fig 09.2 cells → each tool's pinned bug-pattern/rule page; capture → the companion module's real build log.

---

## 7. Gap-filling (verification queue)

- ⚠ **verify at pin — tool rule severities/defaults & GAV coordinates.** Error Prone (`CheckReturnValue`=ERROR,
  `MissingOverride`=WARNING, etc.), SpotBugs (`EI_EXPOSE_REP`, `RV_RETURN_VALUE_IGNORED`), PMD design rules,
  Sonar `java:S2201`/`java:S1226` — confirm exact severity, default-ruleset membership, and version at each
  tool's pinned release once `/pin-source` runs. Rule IDs themselves are stable and confirmed against current
  docs; the *defaults* are the uncertain part.
- ⚠ **`java:S2201` exact scoped type list** — confirmed against sonar-java source/community as
  `String, Boolean, Integer, Double, Float, Byte, Character, Short, StackTraceElement`; re-confirm at the
  pinned analyzer version (the list may widen between versions). Material limitation → flagged.
- ⚠ **AHEAD-OF-PIN: JEP 467 (Markdown documentation comments, JDK 23).** Do not present `///` Markdown doc
  comments as available at the Java 21 anchor. Flagged to `09-flags/`.
- ⚠ **AHEAD-OF-PIN: IntelliJ IDEA 2025.3 "JSpecify preferred"** — IDE behavior past current GA; treat as
  direction-of-travel corroboration, not a pinned fact.
- ⚠ **JSpecify version** — JSpecify 1.0 is released; confirm the exact pinned `org.jspecify:jspecify` version
  and that `@NullMarked` package-level semantics hold at that version before block-quoting the spec.
- **Effective Java verbatim quotes** — Item 49 "enforce them with checks at the beginning of the method body"
  and the "easy to use correctly, hard to use incorrectly" maxim: confirm exact wording + page before
  block-quoting (≤ fair-use). Item numbers (15–17, 49–56) confirmed against the O'Reilly ToC.
- **JLS section numbers** — varargs §8.4.1, overload resolution §15.12.2, access §6.6: confirm against the
  JLS SE 21 edition text before citing section numbers as fact.
- **Defensive-copy shallow-`.clone()` caveat** — corroborated from SpotBugs issue tracker (#4003/#2898);
  present as a documented sharp edge, attributed, not as a general law.

---

## 8. Sources & further reading

### Primary / Official (pinned authority set: docs, source, specs)
| # | Source | Title | URL / path | Verified @ pin |
|---|---|---|---|---|
| 1 | JDK 21 API | `java.util.Objects` (requireNonNull, checkIndex, …) | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html | ☑ (signatures + `since`) |
| 2 | JDK 21 API | `java.util.Optional` | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html | ☐ (confirm at draft) |
| 3 | Book canon | Bloch — *Effective Java* 3e (2018), Ch 8 Items 49–56; Items 15–17, 50–51 | https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/ch8.xhtml | ☑ (item titles via ToC); ⚠ verbatim at draft |
| 4 | Tool docs | Error Prone bug patterns (`CheckReturnValue`, `CanIgnoreReturnValue`, `MissingOverride`, `InconsistentOverloads`, `ParameterName`, `NullArgumentForNonNullParameter`) | https://errorprone.info/bugpatterns | ☑ (IDs + severities); ⚠ verify version at pin |
| 5 | Tool docs | SpotBugs bug descriptions (`EI_EXPOSE_REP(2)`, `RV_RETURN_VALUE_IGNORED`, `NP_NONNULL_PARAM_VIOLATION`) | https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html | ☑ (IDs); ⚠ verify version at pin |
| 6 | Tool docs | PMD Java rules (`MethodReturnsInternalArray`, `ArrayIsStoredDirectly`, `AvoidReassigningParameters`, `UseVarargs`) | https://pmd.github.io/pmd/pmd_rules_java.html | ☑ (IDs); ⚠ verify version at pin |
| 7 | Tool docs/source | SonarSource `java:S2201`, `java:S1226` | https://rules.sonarsource.com/java ; sonar-java `IgnoredReturnValueCheck.java` | ☑ (IDs + S2201 scope); ⚠ verify at pin |
| 8 | Spec | JSpecify spec + user guide (`@Nullable`/`@NonNull`/`@NullMarked`/`@NullUnmarked`) | https://jspecify.dev/docs/spec/ ; https://jspecify.dev/docs/user-guide/ | ☑ (annotation semantics) |
| 9 | Spec/JEP | JEP 395 records, JEP 409 sealed, JEP 441 pattern-`switch`, **JEP 467 Markdown docs (23, AHEAD-OF-PIN)** | https://openjdk.org/jeps/{395,409,441,467} | ☐ confirm numbers at pin |
| 10 | Doc guide | Oracle Javadoc guide — `@param`/`@return`/`@throws`/`@implSpec`/summary fragment | https://docs.oracle.com/en/java/javase/21/javadoc/javadoc-guide.pdf | ☐ confirm @ 21 |

### Accessible / Further reading (corroboration only)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | inside.java | "Objects Utility Class — Sip of Java" | https://inside.java/2023/05/28/sip078/ | ☑ corroboration |
| 2 | Baeldung | A Practical Guide to Null-Safety in Java With JSpecify | https://www.baeldung.com/java-jspecify-null-safety | corroboration |
| 3 | N. Parlog | "Kinds of Comments" (`@apiNote`/`@implSpec`/`@implNote`) | https://medium.com/97-things/kinds-of-comments-667a5b505ca8 | corroboration |
| 4 | error-prone repo | `CheckReturnValue.md` (annotation-pair validation) | https://github.com/google/error-prone/blob/master/docs/bugpattern/CheckReturnValue.md | corroboration |

> Source-quality order applied: JDK API docs/JLS/JEP → each tool's own docs/repo at its pin → the JSpecify spec
> → the named book canon (EJ 3e, dated) → quality secondary blogs (corroboration only). No content farms / AI
> text used as a factual source. Tool *severities/defaults* demoted to `⚠ verify at pin` pending `/pin-source`.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | Effective Java 3e API design items | web search + fetch O'Reilly ch8 ToC | Ch 8 = Items 49–56 (check params, defensive copies, signatures, overloading, varargs, empty-not-null, Optional, doc comments); Items 15–17 accessibility/mutability |
| 2 | Error Prone CheckReturnValue/CanIgnoreReturnValue/MissingOverride | web search + fetch errorprone.info/bugpatterns | rule IDs + severities (CheckReturnValue=ERROR, MissingOverride=WARNING, CanIgnoreReturnValue=SUGGESTION) + annotation-pair validation |
| 3 | `Objects` requireNonNull/checkIndex Java 21 | web search + fetch JDK 21 Javadoc | exact signatures + `since` (1.7/1.8/9/16); "designed for parameter validation" |
| 4 | SpotBugs EI_EXPOSE_REP / RV_RETURN_VALUE_IGNORED / NP_NONNULL | web search | rep-exposure + ignored-return + non-null-param patterns; shallow-clone caveat (issues #4003/#2898) |
| 5 | JSpecify nullness contract 1.0 | web search | `@Nullable`/`@NonNull`/`@NullMarked`/`@NullUnmarked`; spec-not-checker; tool-agnostic; IDE 2025.3 (AHEAD) |
| 6 | SonarSource S2201 / S1226 | web search | S2201 scoped to fixed immutable-type list; S1226 reassign-before-read (RSPEC-1226) |
| 7 | EJ Item 56 Javadoc tags + JEP 467 | web search | @param/@return/@throws/@implSpec/@apiNote/@implNote; summary fragment; JEP 467 Markdown = JDK 23 (AHEAD-OF-PIN) |
| 8 | PMD design rules | web search | MethodReturnsInternalArray, ArrayIsStoredDirectly, AvoidReassigningParameters, UseVarargs |

---

## Learnings & pipeline suggestions
- **Reusable shape — "contract-card" for code-craft chapters (07–18).** Each contract rule fits a fixed
  mini-structure: *the contract (what's promised) / the type-system or runtime mechanism that carries it / the
  Effective Java item / the analyzer rule(s) that machine-check it (one per named tool, cited) / its
  when-NOT-to-use.* This keeps Part II consistent and forces the "is it machine-checkable?" question every
  time. Propose adding to `templates/` alongside the metric-card and two-schools shapes. → PIPELINE-LEARNINGS.
- **Standing AHEAD-OF-PIN trap: doc-comment authoring.** JEP 467 (Markdown `///` doc comments) is JDK 23 and
  will tempt any Javadoc-touching chapter (09, 17, 89) to show it as current at the Java 21 anchor. Recorded in
  the folklore-adjacent "ahead-of-pin watch" so keys 17/89 inherit the flag rather than re-discovering it.
- **Cross-ref map (record in merge notes):** key 09 owns the *code-level* contract craft; defer enforcement
  tooling depth to keys 30 (Error Prone), 29 (SpotBugs), 28 (PMD), 35 (Sonar), 31/32 (nullness); defer
  *library-evolution* contract (semver, binary compat, revapi/japicmp) to key 60; defer Bean Validation guard
  clauses to key 18; defer the comments/Javadoc-style debate to key 17 and docs-as-contract to key 89; defer
  the immutability/records design to key 10 and equals/hashCode contracts to key 15. State the "contract has a
  type-carried half and a doc/runtime-carried half" framing once, here, and reuse.
- **Severity-vs-rule-ID discipline confirmed useful.** Rule IDs are stable and safe to cite now; severities and
  GAV versions are the moving parts → cite ID + tool + "verify at pin." Recommend this become the standing
  pattern for every tool-naming dossier (not just `⚠` keys). → append to PIPELINE-LEARNINGS.
