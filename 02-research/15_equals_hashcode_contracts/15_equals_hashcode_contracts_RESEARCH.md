# RESEARCH DOSSIER — Java Code Quality Book

> Code-craft (Tier-B / Standard depth-band) dossier. Every rule ID, contract clause, API signature, and
> quoted claim traces to a pinned authority in `00-strategy/SOURCE-PIN.md` (the JLS/JDK API docs at the
> anchor, the named book canon, and each tool's own pinned docs/repo). Tool versions are `TO-PIN` in
> SOURCE-PIN.md, so rule IDs and behaviors are cited to the tool/doc and marked **⚠ verify at pin** for the
> exact version/threshold. Anything untraceable is `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.
>
> **This is NOT a `⚠` comparison key.** The *subject* is the JDK contracts (`Object.equals`/`hashCode`,
> `Comparable.compareTo`, `Object.toString`) — discuss them freely. The tools that *check* the contracts
> (Error Prone, SpotBugs, PMD, Checkstyle, Sonar) appear as first-class capabilities; each tool's rule is
> cited to that tool's own pinned docs, none is crowned, and the NEUTRALITY blocklist is observed.

---

## Topic
- **Key:** 15 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** `equals`/`hashCode`/`Comparable`/`toString` correctness — the contracts machines check
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B · **Depth band:** Standard · **Cmp:** (none — subject is the JDK contract; tools are checkers, not rivals)
- **Java code quality pin:** anchor **Java 21 LTS**; **Java 25 LTS** deltas called out (records, value-based caveats). Per SOURCE-PIN.md.
- **Primary dependency / source unit(s) (the never-invent atoms):**
  - **JDK API spec** — `java.lang.Object.equals(Object)` and `Object.hashCode()` general contracts; `java.lang.Object.toString()`; `java.lang.Comparable<T>.compareTo(T)` contract; `java.util.Objects.equals`, `Objects.hash`, `Objects.hashCode`, `Objects.requireNonNull`; `java.util.Comparator` combinators (`comparing`, `thenComparing`, `naturalOrder`). All at **Java SE 21**.
  - **JEP 395 (Records, final Java 16)** — implicitly-derived `equals`/`hashCode`/`toString` and the canonical-constructor invariant.
  - **Effective Java, 3rd ed. (Bloch, 2018)** — Item 10 (obey the `equals` general contract), Item 11 (always override `hashCode` when you override `equals`), Item 12 (always override `toString`), Item 14 (consider implementing `Comparable`).
  - **Tool rule IDs** (each cited to that tool's pinned docs; exact version/threshold ⚠ verify at pin):
    - Error Prone: `EqualsHashCode`, `EqualsGetClass`, `EqualsIncompatibleType`, `EqualsUsingHashCode`, `CompareToZero`, `EqualsBrokenForNull`, `ComparableType`, `NonOverridingEquals` *(presence ⚠ verify at pin)*.
    - SpotBugs: `HE_EQUALS_NO_HASHCODE`, `HE_HASHCODE_NO_EQUALS`, `HE_EQUALS_USE_HASHCODE`, `HE_HASHCODE_USE_OBJECT_EQUALS`, `EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC`, `EQ_COMPARING_CLASS_NAMES`, `EQ_ALWAYS_TRUE`, `EQ_ALWAYS_FALSE`, `EQ_SELF_NO_OBJECT`/`CO_SELF_NO_OBJECT` (covariant), `CO_COMPARETO_RESULTS_MIN_VALUE`, `CO_COMPARETO_INCORRECT_FLOATING`, `NP_EQUALS_SHOULD_HANDLE_NULL_ARGUMENT`.
    - PMD (Java, `errorprone` / `design` categories): `OverrideBothEqualsAndHashcode`, `OverrideBothEqualsAndHashCodeOnComparable`, `CompareObjectsWithEquals`, `SuspiciousEqualsMethodName`, `UselessOverridingMethod`.
    - Checkstyle (`coding` package): `EqualsHashCode`, `CovariantEquals`, `EqualsAvoidNull`.
    - SonarSource (`sonar-java`): `java:S1206` (equals & hashCode in pairs), `java:S1210` (override equals when implementing Comparable / vice-versa), `java:S2204` (`.equals()` on array), `java:S1244` (floating-point equality), `java:S2055` *(serialization — relates, not core)*.
- **Canonical doc page(s):**
  - `Object` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html
  - `Comparable` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Comparable.html
  - `Objects` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html
  - `Comparator` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Comparator.html
  - JEP 395 — https://openjdk.org/jeps/395
  - SpotBugs bug descriptions — https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html
  - Error Prone bug patterns — https://errorprone.info/bugpatterns
  - PMD Java rules — https://pmd.github.io/pmd/pmd_rules_java.html
  - Checkstyle checks — https://checkstyle.org/checks/coding/
  - Sonar Java rules — https://rules.sonarsource.com/java/
- **Canonical source path(s):** to be set when the tool repos are cloned at the pins in SOURCE-PIN.md (`the per-tool fetch dirs in SOURCE-PIN.md/...`); repo remote TBD when the book git repo is created.

---

## 1. Core definition & purpose

**Central claim.** Four `Object`/interface methods carry *machine-checked contracts*: `equals`, `hashCode`,
`Comparable.compareTo`, and (by convention) `toString`. These are not style preferences — they are
behavioral invariants the **JDK's own collections and algorithms rely on**. Break them and the breakage is
silent and data-dependent: a `HashMap` "loses" a key, a `TreeSet` drops or duplicates elements, `sort`
throws `IllegalArgumentException: Comparison method violates its general contract!`, or a `contains` returns
the wrong answer — all with code that compiles cleanly and may pass shallow tests. The chapter's job is to
(a) state the four contracts exactly as the JDK spec writes them, (b) show the recurring ways Java code
violates them, and (c) show that these violations are among the **most reliably caught by static analysis**,
because the contracts are formal enough for a tool to verify. This is the chapter where "code quality" stops
being advice and becomes a property a machine can check.

**Where it sits in the architecture (build-time vs runtime split).**
- **Runtime** owns the *consequence*: `java.util.HashMap`/`HashSet` bucket by `hashCode()` then confirm with
  `equals()`; `TreeMap`/`TreeSet`/`Collections.sort`/`Arrays.sort`/`Stream.sorted` order by
  `compareTo`/`Comparator`; `Objects.equals` and `List.contains` lean on `equals`. A contract violation is a
  *latent runtime defect*.
- **Build-time** owns the *prevention*: the analyzers (key cluster 27–30, 35) encode the contract as rules
  and flag violations before the code runs. The chapter's punchline — "the contracts machines check" — is
  exactly this: a formal contract is a gateable one.

**Which authority provides it.** The contracts are JDK-spec facts (`Object`, `Comparable` Javadoc at Java
SE 21). The idiom canon is *Effective Java* 3e (Items 10/11/12/14). The checks are each tool's own rules.

**When introduced / lifecycle.** `Object.equals`/`hashCode`/`toString` and `Comparable` predate Java 1.2 and
are stable at the anchor (21) and forward LTS (25). `java.util.Objects` (helper) arrived in Java 7;
`Comparator` combinators (`comparing`, `thenComparing`) in Java 8 (JEP/lambda era). **Records** (JEP 395,
final Java 16) change the picture by *deriving* `equals`/`hashCode`/`toString` automatically — relevant at
both 21 and 25. Behavior confirmed still current at the Java 21 API docs.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The `equals(Object)` general contract — verbatim (Java SE 21 `Object` Javadoc)

`public boolean equals(Object obj)` "implements an equivalence relation on non-null object references":

- **Reflexive:** "for any non-null reference value `x`, `x.equals(x)` should return `true`."
- **Symmetric:** "for any non-null reference values `x` and `y`, `x.equals(y)` should return `true` if and
  only if `y.equals(x)` returns `true`."
- **Transitive:** "if `x.equals(y)` returns `true` and `y.equals(z)` returns `true`, then `x.equals(z)`
  should return `true`."
- **Consistent:** "multiple invocations of `x.equals(y)` consistently return `true` or consistently return
  `false`, provided no information used in `equals` comparisons on the objects is modified."
- **Null:** "for any non-null reference value `x`, `x.equals(null)` should return `false`."

*(Source: Object Javadoc, Java SE 21 — verified verbatim.)*

### 2.2 The `hashCode()` general contract — verbatim (Java SE 21 `Object` Javadoc)

- "Whenever it is invoked on the same object more than once during an execution of a Java application, the
  `hashCode` method must consistently return the same integer, provided no information used in `equals`
  comparisons on the object is modified. This integer need not remain consistent from one execution … to
  another."
- "If two objects are equal according to the `equals` method, then calling the `hashCode` method on each of
  the two objects must produce the same integer result."
- "It is *not* required that if two objects are unequal … then calling the `hashCode` method on each …
  must produce distinct integer results. However, the programmer should be aware that producing distinct
  integer results for unequal objects may improve the performance of hash tables."

*(Source: Object Javadoc, Java SE 21 — verified verbatim.)* The load-bearing consequence: **override
`equals` ⇒ you MUST override `hashCode`** (Effective Java Item 11), or equal objects can land in different
buckets and a `HashMap` will fail to find a key it contains.

### 2.3 The `Comparable<T>.compareTo(T)` contract — verbatim (Java SE 21 `Comparable` Javadoc)

- **Signum / anti-symmetry:** "The implementor must ensure `signum(x.compareTo(y)) == -signum(y.compareTo(x))`
  for all `x` and `y`. (This implies that `x.compareTo(y)` must throw an exception if and only if
  `y.compareTo(x)` throws an exception.)"
- **Transitive:** "`(x.compareTo(y) > 0 && y.compareTo(z) > 0)` implies `x.compareTo(z) > 0`."
- **Substitutability of equal elements:** "`x.compareTo(y)==0` implies that
  `signum(x.compareTo(z)) == signum(y.compareTo(z))`, for all `z`."
- **Exceptions:** throws `NullPointerException` "if the specified object is null"; `ClassCastException` "if
  the specified object's type prevents it from being compared to this object."
- **Consistency with equals (recommended, not required):** "It is strongly recommended (though not required)
  that natural orderings be consistent with equals" — i.e. `e1.compareTo(e2) == 0` has the same boolean
  value as `e1.equals(e2)`. A class that violates this "should clearly indicate this fact. The recommended
  language is *'Note: this class has a natural ordering that is inconsistent with equals.'*"

*(Source: Comparable Javadoc, Java SE 21 — verified verbatim.)* The classic violation: `BigDecimal` —
`new BigDecimal("1.0").compareTo(new BigDecimal("1.00")) == 0` but `.equals()` is `false`; this is why a
`TreeSet<BigDecimal>` and a `HashSet<BigDecimal>` disagree on membership. *(Mechanism is JDK-documented;
treat the BigDecimal specifics as ⚠ verify at pin against the `BigDecimal` Javadoc before stating as a
worked figure — §7.)*

### 2.4 `toString()` — the convention (Java SE 21 `Object` Javadoc + Effective Java Item 12)

`toString()` has a *weaker* (recommended) contract: it "returns a string representation of the object" and
the spec recommends the result "be a concise but informative representation that is easy for a person to
read." Default `Object.toString()` returns `getClassName() + "@" + Integer.toHexString(hashCode())`.
Effective Java Item 12 ("always override `toString`") frames it as a quality and debuggability obligation —
log lines, assertion failures, and debugger views all surface it — while warning that **committing to a
specific `toString` format is a forward-compatibility liability** (callers parse it). *(Object Javadoc
verified; EJ Item 12 cited to the named edition.)*

### 2.5 The recurring violations the machines catch (the build-time spine)

| Violation | Why it breaks the contract | Concrete symptom |
|---|---|---|
| Override `equals`, not `hashCode` | Equal objects may differ in hash | `HashMap` "loses" the key |
| Override `hashCode`, not `equals` | Default identity `equals` ≠ value `hashCode` | objects never collide on value |
| Covariant `equals(MyType)` | Does not override `Object.equals(Object)` | collections still use identity `equals` |
| `instanceof` asymmetry across a subclass | symmetry/transitivity broken | `a.equals(b) != b.equals(a)` |
| `getClass()` vs `instanceof` | breaks Liskov substitutability of subclasses | subclass instances never equal superclass |
| `equals` doesn't handle `null` / wrong type | NPE or `ClassCastException` instead of `false` | crashes on mixed collections |
| `compareTo` returns a *specific* value (e.g. `-1`) and caller checks `== -1` | only the sign is contractual | logic fails for impls returning other magnitudes |
| `compareTo` uses `int` subtraction / `==` on floats | overflow / NaN / -0.0 mishandled | wrong order; `IllegalArgumentException` from `sort` |
| `compareTo`/`equals` inconsistent | sorted vs hashed collections disagree | element "in" a `TreeSet` is "not in" a `HashSet` |

### 2.6 The modern Java answer — records (JEP 395) and `Objects`/`Comparator` helpers

- **Records (JEP 395, final Java 16; current at 21/25).** A record "automatically acquires … `equals` and
  `hashCode` methods which ensure that two record values are equal if they are of the same type and contain
  equal component values; and a `toString` method that returns a string representation of all the record
  components, along with their names." The derived `equals`/`hashCode`/`toString` are **component-by-component
  and consistent by construction** — the single most reliable way to get the contracts right is to not write
  them. *(JEP 395, verified.)* Caveat: records do **not** auto-implement `Comparable`; ordering is still
  hand-written (or via `Comparator` combinators). A record with an array component gets array *identity*
  semantics in the derived `equals`/`hashCode` (arrays use identity) — a documented sharp edge.
- **`java.util.Objects` helpers (Java 7+).** `Objects.equals(a, b)` (null-safe), `Objects.hash(Object...)`
  (the EJ-recommended `hashCode` body: `return Objects.hash(areaCode, prefix, lineNum);`),
  `Objects.requireNonNull` (fail-fast in canonical constructors). *(Objects Javadoc, Java SE 21.)*
- **`java.util.Comparator` combinators (Java 8+).** `Comparator.comparing(...).thenComparing(...)`,
  `comparingInt`, `naturalOrder`/`reverseOrder`, `nullsFirst`/`nullsLast` — the modern, overflow-safe way to
  implement `compareTo` without manual subtraction. *(Comparator Javadoc, Java SE 21.)*

### 2.7 Reference units (rule IDs / API signatures — table form)

> Each tool rule is cited to that tool's own pinned docs; **exact version + default threshold ⚠ verify at
> pin** (tool rows are `TO-PIN` in SOURCE-PIN.md). API signatures are JDK SE 21 facts.

| Name | Type | Default / signature | Fixed early? (build-time) | Source |
|---|---|---|---|---|
| `Object.equals(Object)` | JDK API contract | equivalence relation (refl/sym/trans/consistent/null-false) | runtime contract | Object Javadoc SE 21 |
| `Object.hashCode()` | JDK API contract | equal ⇒ equal hash; consistent within run | runtime contract | Object Javadoc SE 21 |
| `Comparable.compareTo(T)` | JDK API contract | signum anti-symmetry, transitive, NPE/CCE | runtime contract | Comparable Javadoc SE 21 |
| `Object.toString()` | JDK API convention | "concise but informative"; default `class@hex` | runtime convention | Object Javadoc SE 21 |
| `Objects.hash(Object...)` | JDK API | `int` from components | helper | Objects Javadoc SE 21 |
| `Objects.equals(Object,Object)` | JDK API | null-safe `equals` | helper | Objects Javadoc SE 21 |
| Record derived `equals`/`hashCode`/`toString` | language feature | component-by-component | compile-time generated | JEP 395 |
| `EqualsHashCode` (Error Prone) | rule | override `equals` ⇒ override `hashCode` | build-time, ERROR-tier | errorprone.info/bugpatterns — ⚠ verify at pin |
| `EqualsGetClass` (Error Prone) | rule | discourage `getClass()` in `equals` for non-final | build-time | errorprone.info — ⚠ verify at pin |
| `CompareToZero` (Error Prone) | rule | check only sign of `compareTo`, not specific value | build-time | errorprone.info — ⚠ verify at pin |
| `EqualsIncompatibleType` (Error Prone) | rule | `equals` between incompatible types is always false | build-time | errorprone.info — ⚠ verify at pin |
| `HE_EQUALS_NO_HASHCODE` (SpotBugs) | rule | defines `equals()` but not `hashCode()` | build-time, Bad practice | spotbugs bugDescriptions — ⚠ verify at pin |
| `HE_HASHCODE_NO_EQUALS` (SpotBugs) | rule | defines `hashCode()` but not `equals()` | build-time | spotbugs — ⚠ verify at pin |
| `EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC` (SpotBugs) | rule | `instanceof` in parent+child risks asymmetry | build-time, Correctness | spotbugs — ⚠ verify at pin |
| `EQ_COMPARING_CLASS_NAMES` (SpotBugs) | rule | compares class *names* not class objects | build-time | spotbugs — ⚠ verify at pin |
| `CO_COMPARETO_INCORRECT_FLOATING` (SpotBugs) | rule | relational ops mishandle NaN/-0.0 | build-time | spotbugs — ⚠ verify at pin |
| `CO_COMPARETO_RESULTS_MIN_VALUE` (SpotBugs) | rule | returns `Integer.MIN_VALUE` (can't negate) | build-time | spotbugs — ⚠ verify at pin |
| `NP_EQUALS_SHOULD_HANDLE_NULL_ARGUMENT` (SpotBugs) | rule | `equals` must return false for null | build-time | spotbugs — ⚠ verify at pin |
| `OverrideBothEqualsAndHashcode` (PMD) | rule | override both `equals`+`hashCode` or neither | build-time, errorprone cat | pmd_rules_java — ⚠ verify at pin |
| `OverrideBothEqualsAndHashCodeOnComparable` (PMD) | rule | `Comparable` types need both | build-time | pmd_rules_java — ⚠ verify at pin |
| `CompareObjectsWithEquals` (PMD) | rule | use `equals()`, not `==`, for objects | build-time | pmd_rules_java — ⚠ verify at pin |
| `EqualsHashCode` (Checkstyle) | rule | override `hashCode` whenever `equals` overridden | build-time, coding pkg | checkstyle.org/checks/coding — ⚠ verify at pin |
| `CovariantEquals` (Checkstyle) | rule | flags `equals(SomeType)` covariant signature | build-time | checkstyle.org — ⚠ verify at pin |
| `EqualsAvoidNull` (Checkstyle) | rule | string-literal on left of `.equals()` | build-time | checkstyle.org — ⚠ verify at pin |
| `java:S1206` (Sonar) | rule | `equals(Object)` and `hashCode()` overridden in pairs | build-time | rules.sonarsource.com/java — ⚠ verify at pin |
| `java:S1210` (Sonar) | rule | override `equals` when implementing `Comparable` | build-time | rules.sonarsource.com/java — ⚠ verify at pin |
| `java:S1244` (Sonar) | rule | floating-point not tested for equality | build-time, Bug | rules.sonarsource.com/java — ⚠ verify at pin |

---

## 3. Evidence FOR

- **The contracts are normative, verbatim JDK spec** (not folklore): reflexive/symmetric/transitive/
  consistent/null for `equals`; equal-⇒-equal-hash for `hashCode`; signum anti-symmetry + transitivity for
  `compareTo`. Verified directly from the Java SE 21 `Object` and `Comparable` Javadoc.
- **First-class, multi-tool checking.** Every major analyzer in this book ships rules for these contracts —
  Error Prone (`EqualsHashCode`, `CompareToZero`, `EqualsGetClass`, `EqualsIncompatibleType`), SpotBugs
  (the `HE_*`, `EQ_*`, `CO_*` families), PMD (`OverrideBothEqualsAndHashcode`,
  `OverrideBothEqualsAndHashCodeOnComparable`, `CompareObjectsWithEquals`), Checkstyle (`EqualsHashCode`,
  `CovariantEquals`, `EqualsAvoidNull`), and Sonar (`java:S1206`, `java:S1210`, `java:S1244`). The contract's
  formality is *why* it is so checkable — this is the chapter's thesis made concrete.
- **The language now derives them correctly.** JEP 395 records generate component-by-component
  `equals`/`hashCode`/`toString` that satisfy the contract by construction — official, GA since Java 16,
  current at 21/25.
- **Canon corroboration.** Effective Java 3e devotes Items 10–12 and 14 to exactly these four methods; the
  recommended `hashCode` body is `Objects.hash(...)` and the EJ guidance aligns with the JDK spec (primary
  wins where they touch — they agree here).
- **Maturity.** All four methods and `Objects`/`Comparator` helpers are GA/stable; no preview, no deprecation
  at the anchor.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **`equals` + inheritance is a genuinely unsolved tension (EJ Item 10).** "There is no way to extend an
  instantiable class and add a value component while preserving the `equals` contract." `instanceof`-based
  `equals` keeps symmetry but can break Liskov substitution across subclasses; `getClass()`-based `equals`
  keeps subclass distinctness but breaks substitutability. The tools disagree by design: SpotBugs
  `EQ_GETCLASS_AND_CLASS_CONSTANT`/Error Prone `EqualsGetClass` lean against `getClass()`, while a
  `getClass()` check is sometimes the right call. **When NOT to "just fix the warning":** suppress with a
  documented rationale rather than mechanically satisfy a rule — favor *composition over inheritance* for
  value types instead (EJ Item 10). This is the chapter's honest limit: no rule resolves the design tension.
- **`compareTo` consistency with `equals` is *recommended, not required*** (Comparable Javadoc) — so a
  rule that flags every inconsistency would have false positives (e.g. `BigDecimal` is intentionally
  inconsistent). The contract itself sanctions the exception, with the "natural ordering inconsistent with
  equals" note as the documented escape hatch.
- **Records solve the common case, not all cases.** When NOT to use a record's derived methods: (a) you need
  array components compared by *content* (derived `equals` uses array identity); (b) you need ordering
  (records are not `Comparable`); (c) you want `equals` to ignore a derived/cache field — a record compares
  *all* components; (d) the type must be mutable or extend a class (records are implicitly final and cannot
  extend). In those cases hand-written methods (with the checkers on) are still required.
- **Static checks have false positives / negatives.** `OverrideBothEqualsAndHashcode` historically had false
  negatives with anonymous classes and ignored records (PMD issues #4457, #4546 — ⚠ verify status at pin);
  `EQ_DOESNT_OVERRIDE_EQUALS` has a documented case where adherence can itself violate the `equals` contract
  (SpotBugs #511). The chapter must present these as the normal cost of static analysis (forward-ref key 39,
  living with findings), not as tool failure.
- **Performance / cost trade-off.** A correct `hashCode` must be *fast and well-distributed*; `Objects.hash`
  boxes varargs and allocates an array on every call — fine for most code, measurable on a hot path. EJ Item
  11 notes the cached-`hashCode`-for-immutables pattern as the optimization, but warns lazy caching must stay
  thread-safe (cross-ref key 20/21). Over-engineering a hashCode for a cold-path value type is the opposite
  waste.
- **`toString` format as accidental API.** Committing to a parseable `toString` makes it a contract callers
  depend on (EJ Item 12) — a forward-compat liability; records bake in a fixed format, which is convenient
  but means the same caution applies.

---

## 5. Current status

- **Stable / GA across the board.** The four contracts, `Objects`, and `Comparator` combinators are
  unchanged at Java 21 and Java 25; no deprecation. Behavior re-confirmed against the Java SE 21 API docs.
- **Records (JEP 395) are GA** since Java 16; the derived-method story is the current best-practice default at
  21/25. **Java 25 delta to call out:** record *patterns* (JEP 440/441, finalized Java 21) and continued
  pattern-matching work make records even more central, raising the practical frequency of derived
  `equals`/`hashCode` — but the contract semantics are unchanged. *(JEP numbers ⚠ verify at pin against the
  JDK JEP index.)*
- **`@ValueBased` / Valhalla (AHEAD-OF-PIN watch).** Value classes / `==`-vs-`equals` guidance for
  value-based classes (e.g. `Optional`, boxed primitives) is evolving under Project Valhalla — **not** GA at
  the pin. Any value-class identity claim is `⚠ AHEAD-OF-PIN` (§7). At the pin, the rule is: do not synchronize
  on or `==`-compare value-based classes; use `equals`.
- **Tool rule sets are stable but version-pinned.** The rule *IDs* above are long-standing; exact default
  thresholds and rule *enablement* (e.g. which Error Prone checks are ON by default vs. opt-in) shift across
  versions — all marked ⚠ verify at pin.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** `DEMO-CATALOG.md` — shared domain `org.acme.storefront`. **No `15_*` row exists in the
  catalog yet → propose one (flag to example-builder, §7).** Proposed row:
  - **Demo (the module):** a `Money` value type and a `Product` identity type in the storefront domain that
    demonstrate the four contracts — one *correct* record-based value (`Money`), one *hand-written* value
    with a deliberately seeded contract bug, and the analyzers that catch it.
  - **Java code quality surface:** the `equals`/`hashCode`/`Comparable`/`toString` contracts + the static
    rules that check them (`java:S1206`, Error Prone `EqualsHashCode`/`CompareToZero`, SpotBugs `HE_*`/`CO_*`).
  - **Exercise (TRY IT):** add a `discountPercent` component to `Money`; observe that the record's derived
    `equals` now also compares it; then write a `Comparator<Money>` via `Comparator.comparing(...)` and assert
    that `compareTo` is sign-only consistent in a JUnit test.
- **Module key / path:** `08-companion-code/15_equals_hashcode_contracts/`
- **Intended dependencies (verified @the pins in SOURCE-PIN.md):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property) | Java 21 toolchain | SOURCE-PIN runtime baseline | ☐ |
  | `org.apache.maven.plugins:maven-compiler-plugin` (Error Prone wired) | hosts Error Prone `EqualsHashCode`/`CompareToZero` at compile | errorprone.info install docs — ⚠ verify GAV at pin | ☐ |
  | `com.github.spotbugs:spotbugs-maven-plugin` | runs `HE_*`/`EQ_*`/`CO_*` patterns | spotbugs docs — ⚠ verify GAV at pin | ☐ |
  | `org.junit.jupiter:junit-jupiter` | the test harness (canonical at the pin) | junit.org/junit5 — ⚠ verify GAV at pin | ☐ |
  | `org.assertj:assertj-core` | readable contract assertions | assertj.github.io — ⚠ verify GAV at pin | ☐ |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property; no loose versions.
  - **Externalized config / profiles** — a Maven profile `strict` that fails the build on contract-rule
    findings vs a default profile that only warns (demonstrates the gate; trace each rule to its pinned tool).
  - **At least one test** — a JUnit 5 test asserting the four contracts on the *correct* `Money` record
    (reflexive/symmetric/transitive `equals`; equal ⇒ equal `hashCode`; `compareTo` sign anti-symmetry).
  - **Observability / health surface** — `toString()` shown in a log line / assertion message (the
    debuggability payoff of Item 12); this is the topic's observability touch-point.
  - **Explicit failure path** — a `BrokenPrice` class that overrides `equals` but **not** `hashCode`,
    plus a test that puts it in a `HashMap` and shows the key is "lost" (`map.get(key) == null`), AND the
    analyzer report flagging `java:S1206` / `HE_EQUALS_NO_HASHCODE`. This proves the honest-limitations floor
    in code: the contract violation is a real, demonstrable runtime defect that the gate would have stopped.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `record-money` | the record deriving correct `equals`/`hashCode`/`toString` | `Money.java` |
  | `broken-hashcode` | the seeded violation (equals without hashCode) | `BrokenPrice.java` |
  | `comparator-impl` | `compareTo` via `Comparator.comparing` (overflow-safe) | `Money.java` |
  | `contract-test` | JUnit assertions of the four contracts | `MoneyContractTest.java` |
  | `hashmap-loses-key` | the failure-path test | `BrokenPriceTest.java` |

- **Run command:** `./mvnw -B -Pstrict verify` (strict profile fails on contract findings); default
  `./mvnw -B verify` builds green and emits the analyzer report.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** default build green; the contract test passes; `BrokenPriceTest` demonstrates
  `map.get(key) == null` for an "equal" key; under `-Pstrict` the build fails citing `java:S1206` /
  `HE_EQUALS_NO_HASHCODE` (or the Error Prone `EqualsHashCode` ERROR) on `BrokenPrice`.
- **Figure plan** (GUIDELINES §8):
  - **Chapter class:** *standard code-craft chapter* → budget **1–2 designed diagrams + 1 captured
    screenshot** (a Part-II craft chapter, not a short reference stub; ZERO is not appropriate here).
  - **Candidate designed diagram(s) + family:**
    - **Fig 15.1 — "Why a HashMap loses a key" (data-flow family).** Object → `hashCode()` → bucket index →
      `equals()` confirm; the broken case shows two value-equal objects with different hashes landing in
      different buckets so `get` misses. Trace each step to the `HashMap`/`Object.hashCode` Javadoc (SE 21).
    - **Fig 15.2 — "The four contracts at a glance" (concept/relationship family).** `equals` properties
      (refl/sym/trans/consistent/null) ↔ `hashCode` (equal ⇒ equal) ↔ `compareTo` (signum/transitive/
      consistent-with-equals) ↔ `toString` (recommended). Each label traced to the `Object`/`Comparable`
      Javadoc verbatim text in §2.1–2.4.
  - **Candidate captured surface(s):** an **analyzer report / IDE inspection screenshot** showing
    `java:S1206` (or Error Prone `EqualsHashCode`) flagging `BrokenPrice` — captured from the companion
    module's build output or IntelliJ inspection (technical profile permits a tool screenshot). Trace the
    rule text to the named tool's pinned rule page.
  - **Source trace per depicted claim:** Fig 15.1 → `HashMap`/`Object` Javadoc SE 21; Fig 15.2 →
    `Object`/`Comparable` Javadoc SE 21 (the verbatim clauses in §2); screenshot → the named tool's rule page
    at its pin.

---

## 7. Gap-filling (verification queue)

- ⚠ **Exact tool rule enablement & defaults** (all `TO-PIN` rows): which Error Prone checks are ON-by-default
  vs opt-in (`EqualsGetClass`, `EqualsIncompatibleType`, `CompareToZero` severities); SpotBugs rank/priority
  of `HE_*`/`CO_*`; Sonar `java:S1206`/`S1210`/`S1244` severity & "clean code attribute". Confirm against each
  tool's pinned docs/repo. → before draft. *(Material → flag.)*
- ⚠ **Error Prone rule IDs presence at pin** — confirm `EqualsBrokenForNull`, `ComparableType`,
  `NonOverridingEquals`, `EqualsUsingHashCode` still exist (not renamed/removed) at the pinned Error Prone
  version. *(Material → flag.)*
- ⚠ **PMD rule-name casing & category** — `OverrideBothEqualsAndHashcode` (lowercase "code"?) vs
  `OverrideBothEqualsAndHashCode`; confirm exact spelling + that it lives in `errorprone` category at the pin;
  confirm records-handling fix status (issue #4546).
- ⚠ **SpotBugs exact codes** — confirm `EQ_GETCLASS_AND_CLASS_CONSTANT`, `EQ_SELF_NO_OBJECT` vs
  `EQ_SELF_USE_OBJECT`, `EQ_ABSTRACT_SELF` spellings against the bugDescriptions page at the pinned version.
- ⚠ **`BigDecimal` inconsistency figures** — confirm `new BigDecimal("1.0").equals(new BigDecimal("1.00"))`
  is `false` while `compareTo == 0`, from the `BigDecimal` Javadoc SE 21, before stating as a worked example.
- ⚠ **JEP numbers/versions** — records final = JEP 395 / Java 16 (verified); record patterns JEP 440/441,
  Java 21 — confirm exact JEP numbers at the JDK JEP index.
- ⚠ **AHEAD-OF-PIN — Valhalla value classes.** Any value-class / `@ValueBased` identity-vs-equality claim is
  not GA at the pin → mark `⚠ AHEAD-OF-PIN`, do not state as fact. *(Material → flag.)*
- **DEMO-CATALOG gap** — no `15_*` row exists; §6 proposes one → flag to example-builder to add it.
- **Cross-ref to resolve at draft:** key 10 (immutability/records — don't re-teach records, link), key 13
  (modern Java features), key 29 (SpotBugs deep-dive — owns the `HE_*`/`CO_*` tutorial), key 30 (Error Prone),
  key 27/28/35 (Checkstyle/PMD/Sonar rule mechanics), key 39 (suppression/false positives), key 20/21
  (thread-safe lazy hashCode caching). This chapter keeps the *contract* + the *which-rule-checks-it* map;
  the per-tool mechanics live in their own chapters.

---

## 8. Sources & further reading

### Primary / Official (the pinned authority set: docs, source, official channels)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JDK API spec | `java.lang.Object` (equals/hashCode/toString contracts), Java SE 21 | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html | ☑ (verbatim) |
| 2 | JDK API spec | `java.lang.Comparable` (compareTo contract), Java SE 21 | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Comparable.html | ☑ (verbatim) |
| 3 | JDK API spec | `java.util.Objects` (`hash`, `equals`, `requireNonNull`), Java SE 21 | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html | ☐ (confirm signatures at pin) |
| 4 | JDK API spec | `java.util.Comparator` (combinators), Java SE 21 | https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Comparator.html | ☐ |
| 5 | JEP | JEP 395 — Records (derived equals/hashCode/toString) | https://openjdk.org/jeps/395 | ☑ (derivation text) |
| 6 | Book canon | Effective Java 3e — Items 10, 11, 12, 14 | print (Bloch, 2018) | ☑ (item scope; confirm verbatim at draft) |
| 7 | Tool docs | SpotBugs — Bug descriptions (`HE_*`/`EQ_*`/`CO_*`/`NP_*`) | https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html | ☑ codes; ⚠ version at pin |
| 8 | Tool docs | Error Prone — bug patterns (`EqualsHashCode`, `CompareToZero`, `EqualsGetClass`, `EqualsIncompatibleType`) | https://errorprone.info/bugpatterns | ☑ names; ⚠ defaults at pin |
| 9 | Tool docs | PMD — Java rules (`OverrideBothEqualsAndHashcode`, `…OnComparable`, `CompareObjectsWithEquals`) | https://pmd.github.io/pmd/pmd_rules_java.html | ☑ names; ⚠ version at pin |
| 10 | Tool docs | Checkstyle — coding checks (`EqualsHashCode`, `CovariantEquals`, `EqualsAvoidNull`) | https://checkstyle.org/checks/coding/ | ☑ names; ⚠ version at pin |
| 11 | Tool docs | SonarSource — Java rules (`S1206`, `S1210`, `S1244`) | https://rules.sonarsource.com/java/ | ☑ S1206/S1244; ⚠ S1210 title at pin |

### Accessible / Further reading (tutorials, talks, quality secondary sources)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Baeldung | Java equals() and hashCode() Contracts | https://www.baeldung.com/java-equals-hashcode-contracts | corroboration only |
| 2 | GitHub (PMD) | OverrideBothEqualsAndHashcode false-negative / records issues (#4457, #4546) | https://github.com/pmd/pmd/issues/4457 | ☑ (issue exists; status ⚠) |
| 3 | GitHub (SpotBugs) | EQ_DOESNT_OVERRIDE_EQUALS contract-conflict (#511) | https://github.com/spotbugs/spotbugs/issues/511 | ☑ (issue exists) |

> Source-quality order: JDK API spec / JEP → each tool's own pinned docs → the named book canon (dated) →
> quality secondary sources (corroboration only). No content farms or AI-generated text as a factual source.
> Medium/HowToDoInJava-style hits surfaced in search were demoted to non-citing color.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | equals/hashCode general contract | web search | refl/sym/trans/consistent/null + equal-⇒-equal-hash (paraphrase) |
| 2 | Comparable compareTo contract signum | web search | signum anti-symmetry, consistent-with-equals recommended-not-required |
| 3 | Object Javadoc SE 21 fetch | docs.oracle.com SE 21 | **verbatim** equals + hashCode contract bullets |
| 4 | Comparable Javadoc SE 21 fetch | docs.oracle.com SE 21 | **verbatim** signum/transitive/exception/consistent-with-equals + recommended note |
| 5 | Error Prone equals/compareTo patterns | web search | `EqualsHashCode`, `EqualsGetClass`, `EqualsIncompatibleType`, `CompareToZero`, `EqualsUsingHashCode` |
| 6 | SpotBugs HE/EQ/CO codes | web search + bugDescriptions fetch | `HE_*`, `EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC`, `EQ_COMPARING_CLASS_NAMES`, `CO_COMPARETO_INCORRECT_FLOATING`, `CO_COMPARETO_RESULTS_MIN_VALUE`, `NP_EQUALS_SHOULD_HANDLE_NULL_ARGUMENT` |
| 7 | PMD equals/hashCode rules | web search | `OverrideBothEqualsAndHashcode`, `…OnComparable`, `CompareObjectsWithEquals`; records/anon-class issues |
| 8 | Checkstyle coding checks | web search | `EqualsHashCode`, `CovariantEquals`, `EqualsAvoidNull` |
| 9 | Sonar S1206/S1210/S1244 | web search | S1206 (pairs), S1244 (float equality); S1210 title ⚠ |
| 10 | JEP 395 records derived methods | web search | derived equals/hashCode/toString component-by-component; record implicitly final |
| 11 | Effective Java 3e items | web search | Item 10/11/12/14 scope; `Objects.hash(...)` recommended hashCode body |
| 12 | DEMO-CATALOG read | local file | shared domain `org.acme.storefront`; no `15_*` row → propose one |

---
## Learnings & pipeline suggestions
- **"Contract card" shape (proposed reusable structure).** For any JDK behavioral-contract topic (this key,
  plus keys 09 method contracts, 14 generics/type-safety, 16 lifecycle/AutoCloseable), a fixed mini-structure
  works: *the verbatim spec clauses → the recurring violations → the rule-per-violation map (one row per
  tool) → the modern language feature that derives it correctly (records here) → the residual design tension
  no rule resolves.* Reuse so contract chapters stay consistent and the rule-map table is comparable
  cross-chapter. → append to PIPELINE-LEARNINGS; candidate template addition (low risk).
- **Rule-ID-per-violation matrix is the load-bearing artifact** for this family of chapters, and it is the
  thing most exposed to re-pin churn: rule enablement/severity move across tool versions even when IDs don't.
  Suggest the SOURCE-VERIFY step treat the §2.7 table as a single re-trace unit when any analyzer row in
  SOURCE-PIN.md is re-pinned.
- **DEMO-CATALOG is missing rows for Part-II craft keys** (15 here; likely 09–16 generally). Flag to the
  example-builder to add the proposed `15_*` row (the `Money` record + seeded `BrokenPrice` failure path) so
  the worked example is fixed before drafting, per the catalog's own "no example invented at draft time" rule.
- **AHEAD-OF-PIN discipline paid off:** Valhalla value-class equality guidance is tempting to state but is not
  GA at the pin — kept it out of fact and into §7 + a flag.
- **Cross-ref hygiene:** this chapter must NOT re-teach SpotBugs/Error Prone mechanics (keys 29/30) or records
  (keys 10/13); it owns the *contract* and the *which-rule-checks-it* map and links out. Recorded for the
  draft stage.
