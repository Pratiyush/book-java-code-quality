# RESEARCH DOSSIER — Java Code Quality Book

> Code-craft (Tier-B) dossier. Every specific fact is traced to a pinned authority in
> `00-strategy/SOURCE-PIN.md` (the JLS edition + JEP, a named book edition + item, or a tool's own
> pinned docs/rule catalog). Tool versions/thresholds are `TO-PIN` in SOURCE-PIN.md → each such atom is
> marked `⚠ verify at pin`. Anything untraceable is `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.
> Not a `⚠` comparison key, but it names several analyzers (Sonar, PMD, Error Prone, Checker Framework):
> each tool claim cites that tool's own source and crowns no winner (`NEUTRALITY.md`).

---

## Topic
- **Key:** 14 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Generics & type-safety — variance, bounded types, avoiding raw/unchecked
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B (code-craft) · **Depth band:** Standard
- **Java code quality pin:** Java **21 LTS** anchor (min companion runtime); Java **25 LTS** deltas called out. Language facts from **JLS SE 21** (+ SE 25 where it moves); JEPs at the stated feature level.
- **Primary dependency / source unit(s):**
  - **JLS SE 21** — §4.5 (parameterized types), **§4.6 Type Erasure**, **§4.7 Reifiable Types**, **§4.8 Raw Types**, §4.9 (intersection types), §4.10.2 (subtyping among class/interface types — variance via wildcards), §5.1.6/§5.1.9 (unchecked conversion), §8.4.1 (formal params / varargs), §9.6.4.7 (`@SafeVarargs`), §15.12.4.2 (heap pollution from varargs). *(§ numbers confirmed against JLS SE 21 ch.4 text — see §8.)*
  - **Effective Java, 3rd ed. (Bloch, 2018)** — Chapter 5 "Generics", **Items 26–33** (titles verified, §8).
  - **JEPs:** diamond operator + `@SafeVarargs` (Project Coin, **Java SE 7**); **JEP 213** "Milling Project Coin" (Java **9** — diamond on anonymous classes; `@SafeVarargs` on private instance methods).
  - **Tool rule catalogs (each at its own pin):** SonarSource `java:S3740` (raw types), `java:S1452` (wildcard return types), `java:S1149` (legacy synchronized collections — corroboration), `java:S6204` (Stream.toList) [adjacent]; PMD `UseDiamondOperator`, `LooseCoupling`; Error Prone `TypeParameterUnusedInFormals`; Checker Framework (pluggable type qualifiers).
- **Canonical doc page(s):**
  - JLS SE 21 ch.4 — https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html
  - The Java Tutorials / dev.java — Generics (type erasure, non-reifiable varargs) — https://dev.java/learn/generics/ and https://docs.oracle.com/javase/8/docs/technotes/guides/language/non-reifiable-varargs.html
  - Sonar rule — https://rules.sonarsource.com/java/RSPEC-3740/ ; PMD Java rules — https://pmd.github.io/pmd/pmd_rules_java.html ; Error Prone bugpatterns — https://errorprone.info/bugpatterns ; Checker Framework manual — https://checkerframework.org/manual/
- **Canonical source path(s):** `the per-tool fetch dirs in SOURCE-PIN.md/{jdk-specs,sonar-java,pmd,error-prone,checker-framework}` (fetch at the pinned identifier before any verification read; repos are ephemeral).

---

## 1. Core definition & purpose

**Central claim.** *Generics* (JLS §4.5; JSR-14, GA in **Java SE 5.0, 2004**) move a large class of type errors from **run time** (`ClassCastException`) to **compile time**, and let APIs document their element/parameter types in the type system instead of in prose. The chapter's job is to take the senior reader from "generics work" to "generics used so the compiler carries the safety burden": **never declaring raw types**, **eliminating every unchecked warning**, choosing **variance** (`extends`/`super` wildcards) correctly, and constraining type parameters with **bounds**. The unifying thesis is Bloch's: *use generics so the compiler is your collaborator, and treat an unchecked warning as an unproven obligation you still owe.*

**The problem solved.** Before generics, collections held `Object`; every read was a cast the programmer had to get right, unaided. Generics encode the element type so the compiler inserts and verifies those casts. The cost of that safety is **type erasure** (§2): generic type information is a compile-time-only artifact, which creates the exact sharp edges this chapter teaches around (raw types, unchecked warnings, heap pollution, `instanceof`/array limits).

**Where it sits.** This is a **compile-time** mechanism. At run time, `List<String>` and `List<Integer>` are the same `List` class (§4.6 erasure). So type-safety here is a *build-time* property the language and the static analyzers (Part IV) enforce; nothing at run time re-checks it (except the synthetic casts the compiler already inserted). That build-time/runtime split is the chapter's spine.

**ISO 25010 tie (key 01).** Type-safety primarily serves **Reliability** (faultlessness — designing `ClassCastException` out) and **Maintainability/Analysability** (a typed API states its contract). Cross-ref key 11 (null-safety, the sibling "design the failure out" discipline) and key 13 (modern Java — `var`/records/pattern matching interact with generics).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Type erasure — the build-time fact everything follows from (JLS §4.6)

JLS §4.6: erasure maps a parameterized type `G<T1,…,Tn>` to its raw form `|G|`; a type variable erases to **the erasure of its leftmost bound** (unbounded → `Object`); every other type erases to itself. *(Verified verbatim against JLS SE 21 §4.6.)* Consequences the chapter must make concrete:

- **No generic type info at run time.** `List<String>` and `List<Number>` are indistinguishable to the JVM → these are **non-reifiable** types (§2.2).
- **`new T[]` and `new List<String>[]` are illegal**; `T.class`, `instanceof List<String>` are illegal/limited (only unbounded-wildcard `instanceof List<?>` is allowed).
- **No runtime overhead** for generics (no per-instantiation classes) — the documented design trade for erasure (dev.java Type Erasure).

### 2.2 Reifiable vs non-reifiable types (JLS §4.7)

A type is **reifiable** iff (§4.7, verbatim list): a non-generic class/interface; a parameterized type whose **all** type args are **unbounded wildcards** (`List<?>`); a **raw** type; a primitive; an array whose element type is reifiable; or a reifiable nested type. Everything else (`List<String>`, `List<? extends Number>`) is **non-reifiable**. This is the line that governs what `instanceof`, casts, arrays, and varargs may do.

### 2.3 Raw types and the unchecked warning (JLS §4.8, §5.1.9; EJ Item 26/27)

A **raw type** (§4.8) is "the reference type formed by taking the name of a generic class or interface declaration **without an accompanying type argument list**" (e.g. `List`, not `List<E>`). On a raw type, instance members have the **erasure** of their generic type (static members keep their declared type). JLS §4.8-1 example: assigning into a raw `Cell` produces an **unchecked warning**; reads come back as `Object`.

- **Unchecked warning** (§5.1.9 unchecked conversion): emitted when the compiler cannot prove a parameterized operation type-safe because erasure removed the evidence. It is "a message that it has done what it can, and because of erasure there could be a run-time error it cannot prevent" (dev.java). **EJ Item 27: "Eliminate every unchecked warning."** If you cannot, suppress with `@SuppressWarnings("unchecked")` at the **narrowest** scope and add a comment proving why it is safe.
- **`List<Object>` ≠ `List` (raw).** `List<Object>` is type-checked (you opt into "any object"); raw `List` opts *out* of all checking. EJ Item 26: don't use raw types — except the two legal uses below.
- **Legal raw uses (Item 26):** in **class literals** (`List.class`, `String[].class` — `List<String>.class` is illegal) and with **`instanceof`** (`o instanceof Set` — generics are erased so `o instanceof Set<?>` is the parameterized form to prefer).

### 2.4 Variance — wildcards and PECS (JLS §4.5.1, §4.10.2; EJ Item 31)

Java generics are **invariant**: `List<String>` is **not** a subtype of `List<Object>` even though `String` is a subtype of `Object`. Variance is added at the **use site** via bounded wildcards:

- **`? extends T`** (upper-bounded, **covariant**) — a **producer** you read `T`s out of; you cannot add (except `null`).
- **`? super T`** (lower-bounded, **contravariant**) — a **consumer** you put `T`s into; reads come back as `Object`.
- **`?`** (unbounded) — when the type is irrelevant; the reifiable wildcard form.

**PECS — Producer-Extends, Consumer-Super** (Bloch's mnemonic, EJ Item 31): for a parameter that is a `T` producer use `<? extends T>`; for a `T` consumer use `<? super T>`. Item 31 caveat (verified): **do not** use bounded wildcards as **return types** — it forces wildcards on every caller (this is exactly what Sonar `java:S1452` flags).

### 2.5 Bounded type parameters and recursive bounds (JLS §4.4; EJ Item 30)

A **bounded type parameter** `<T extends Number>` restricts the argument and grants access to the bound's members. Multiple bounds: `<T extends A & B>` (class first, then interfaces). **Recursive type bound:** `<T extends Comparable<T>>` ("a type comparable to itself") — the idiom behind `max(Collection<? extends T>)` and the basis for self-typed builders. Distinguish **type parameter** (a declaration, `<E>`) from **wildcard** (a use-site `?`): generic *methods* (Item 30) parameterize the method (`static <E> Set<E> union(...)`) and infer `E` at the call.

### 2.6 Generics + arrays + varargs = heap pollution (JLS §4.12.2, §15.12.4.2; EJ Item 28, 32)

- **Item 28 "Prefer lists to arrays":** arrays are **covariant and reified** (`Object[] a = new Long[1]; a[0]="x";` throws `ArrayStoreException` at run time); generics are **invariant and erased**. The mismatch makes `new List<String>[]` illegal. Prefer `List<E>` to `E[]`.
- **Heap pollution** (§4.12.2): a variable of a parameterized type referring to an object that is not of that type — arises wherever an unchecked warning was ignored. A **varargs** method with a non-reifiable parameter creates a generic array internally and can leak it → warning at declaration and call site.
- **`@SafeVarargs`** (Java SE 7; JLS §9.6.4.7): the author **asserts** the varargs body performs no unsafe operation, suppressing the warnings. **EJ Item 32 "Combine generics and varargs judiciously":** apply `@SafeVarargs` only when the method genuinely does not store into or expose the varargs array. **JEP 213 (Java 9)** extended `@SafeVarargs` to **private instance methods** (previously only `final`/`static`).

### 2.7 Type inference helpers (Project Coin / JEP 213)

- **Diamond operator `<>`** (Java SE 7): `List<String> l = new ArrayList<>();` — compiler infers the constructor's type arguments. **JEP 213 (Java 9)** allows `<>` on **anonymous classes** when the inferred type is denotable. (PMD `UseDiamondOperator` flags redundant explicit type args; note its documented Java-21 inference false-positive, §4.)
- **`var`** (JEP 286, Java 10) interacts with generics: `var l = new ArrayList<String>();` keeps full type; `var l = new ArrayList<>();` infers `ArrayList<Object>` — a readability/safety trap (cross-ref key 13).

### Reference units (rule IDs / API / JLS / JEP coordinates)

| Name | Type | Default / value | Fixed early? (build-time) | Source |
|---|---|---|---|---|
| JLS §4.6 Type Erasure | spec section | parameterized→raw; tvar→leftmost bound erasure | yes — compile-time mapping | JLS SE 21 §4.6 ✅ |
| JLS §4.7 Reifiable Types | spec section | non-generic / unbounded-wildcard / raw / primitive / reifiable array | compile-time | JLS SE 21 §4.7 ✅ |
| JLS §4.8 Raw Types | spec section | name w/o type args; members = erasure | compile-time | JLS SE 21 §4.8 ✅ |
| Diamond `<>` | language | — | compile-time inference | Java SE 7; JEP 213 (Java 9, anon classes) ✅ |
| `@SafeVarargs` | annotation | suppresses varargs heap-pollution warning | author assertion, compile-time | Java SE 7; JEP 213 → private instance methods (Java 9); JLS §9.6.4.7 ✅ |
| EJ Item 26 | book idiom | "Don't use raw types" | n/a | Effective Java 3e ✅ |
| EJ Item 27 | book idiom | "Eliminate unchecked warnings" | n/a | Effective Java 3e ✅ |
| EJ Item 28 | book idiom | "Prefer lists to arrays" | n/a | Effective Java 3e ✅ |
| EJ Item 29/30 | book idiom | "Favor generic types" / "Favor generic methods" | n/a | Effective Java 3e ✅ |
| EJ Item 31 | book idiom | "Use bounded wildcards to increase API flexibility" (PECS) | n/a | Effective Java 3e ✅ |
| EJ Item 32 | book idiom | "Combine generics and varargs judiciously" | n/a | Effective Java 3e ✅ |
| EJ Item 33 | book idiom | "Consider typesafe heterogeneous containers" | n/a | Effective Java 3e ✅ |
| `java:S3740` | Sonar rule | "Raw types should not be used" (Code Smell) | static analysis | rules.sonarsource.com/java/RSPEC-3740 ⚠ verify type/sev at pin |
| `java:S1452` | Sonar rule | "Generic wildcard types should not be used in return parameters" | static analysis | sonar-java `WildcardReturnParameterTypeCheck` ⚠ verify at pin |
| `UseDiamondOperator` | PMD rule | replace explicit ctor type args with `<>` | static analysis | pmd.github.io java rules ⚠ verify at pin |
| `LooseCoupling` | PMD rule | flag coupling to concrete (e.g. `HashSet`) over interface | static analysis | pmd.github.io (category bestpractices) ⚠ verify at pin |
| `TypeParameterUnusedInFormals` | Error Prone | type param used only in return type → unsafe/unchecked | compile-time | errorprone.info/bugpatterns ⚠ verify at pin |

---

## 3. Evidence FOR

- **Verified spec facts (build green by construction):** §4.6/4.7/4.8 definitions above are quoted from JLS SE 21 ch.4; the raw-`Cell` unchecked-warning example is JLS §4.8-1 (so it compiles-with-warning exactly as documented). These seed the worked example (§6).
- **The canonical idiom set is named and stable.** Effective Java 3e Chapter 5 Items 26–33 are the field's reference for this topic (titles verified). PECS (Item 31) is the most-cited variance mnemonic in Java practice.
- **First-class tooling support.** Every major analyzer ships generics rules sourced to its own catalog: Sonar `java:S3740` (raw types), `java:S1452` (wildcard returns); PMD `UseDiamondOperator`, `LooseCoupling`; Error Prone `TypeParameterUnusedInFormals`; Checker Framework offers *pluggable* compile-time type qualifiers ("if the checker issues no warnings… those errors do not occur" — Checker manual). The compiler itself emits `unchecked`/`rawtypes` warnings (enable with `-Xlint:unchecked,rawtypes`).
- **Maturity.** Generics are GA since **Java SE 5.0 (2004)** — 20+ years stable; the spine (§4.6–4.8) is unchanged at SE 21 and SE 25.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **Erasure is a permanent limitation, not a bug to fix.** You cannot `new T[n]`, `T.class`, `instanceof List<String>`, overload on `List<String>` vs `List<Integer>`, or recover element type at run time. Workarounds (type tokens / `Class<T>` — EJ Item 33 typesafe heterogeneous container; `@SuppressWarnings` + comment) are the accepted craft, not defects.
- **Wildcards add real cognitive cost.** PECS makes APIs flexible but signatures harder to read; deeply nested wildcards (`Map<? extends K, ? super List<? extends V>>`) hurt analysability (key 03). Item 31's own rule: **do not** put wildcards in return types.
- **When NOT to reach for this:**
  - **Don't over-genericize.** A type parameter used **only in the return type** (`<T> T get()`) is an Error Prone bug (`TypeParameterUnusedInFormals`) — it hides an unchecked cast at every call. When-not: don't add `<T>` the compiler can't constrain from arguments.
  - **Don't use bounded wildcards as return types** (Item 31) — forces wildcards into all client code.
  - **Don't reach for `@SafeVarargs` to silence a real risk** — only assert it when the body truly never stores into or leaks the array (Item 32); otherwise the warning is correct.
  - **Raw types remain legal in two places** (class literals, `instanceof`) — a blanket "never write a bare type name" rule is wrong there.
- **Neutral tool framing (no crown).** The analyzers take different approaches to the same target and a team would reasonably pick by context: **Checkstyle/PMD** match source patterns (raw types, diamond, loose coupling) cheaply with some false positives (PMD's own tracker records a `UseDiamondOperator` Java-21 inference false positive, and a `LooseCoupling` generics false positive — both fixed/known per PMD issues). **Error Prone** runs *in the compiler* and can auto-fix. **Checker Framework** gives a *soundness guarantee* for its checked code at the cost of annotation effort and build time. **Sonar** `java:S1452` is community-contested (false positives on interface-mandated wildcard returns — Sonar community threads), illustrating that a generics rule's value is context-dependent. Each claim is cited to that tool's own source; none is declared best.
- **Compatibility caveat:** legacy code mixing raw and generic types (pre-5 libraries) forces unchecked conversions at the boundary; the discipline is to wrap/adapt at the seam, not to spread raw types inward.

---

## 5. Current status

- **Generics core: stable.** §4.6–4.8 unchanged Java 5 → 21 → 25. No JEP alters erasure at the anchor; the long-discussed *Project Valhalla* reified-generics / specialized-generics work is **not** in 21 or 25 → any "reified generics are coming" claim is `⚠ AHEAD-OF-PIN` and must be flagged, not stated as fact.
- **Inference & ergonomics gained over time:** diamond (SE 7) → JEP 213 diamond-on-anonymous + `@SafeVarargs`-on-private (Java 9) → `var` (Java 10) interacts with generics. These are stable at 21.
- **Java 21/25 deltas worth a sentence:** **pattern matching for `switch`/`instanceof`** (finalized Java 21) and **record patterns** (JEP 440, Java 21) deconstruct generic records and reduce unchecked casts — type-safety improves without touching erasure. Generic record patterns infer type arguments. (Cross-ref key 13.) Confirm exact JEP numbers at the pinned JDK docs (§7).
- **Tooling: actively maintained.** Sonar, PMD, Error Prone, Checker Framework all ship current Java-21-aware releases; exact versions/rule defaults are `TO-PIN` (SOURCE-PIN.md) → `⚠ verify at pin`.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** add/point to row `14_generics_type_safety` in `DEMO-CATALOG.md`. **Demo name:** "Type-safe stack & a PECS pushAll/popAll." **Surface exercised:** generic type (Item 29), generic method, bounded wildcards/PECS (Item 31), an eliminated-then-justified `@SuppressWarnings("unchecked")` (Item 27), and an `-Xlint:unchecked,rawtypes` clean build. **TRY-IT exercise:** introduce a raw-type assignment and a wildcard-return method, run `mvn -B verify`, watch the compiler `unchecked` warning + Sonar `java:S3740` / `java:S1452` flag it, then fix.
- **Module key / path:** `08-companion-code/14_generics_type_safety/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited) — Java 21 toolchain | establishes the pin | SOURCE-PIN runtime baseline | ☐ verify at pin |
  | (no runtime dep — pure JDK + generics) | primary unit under study | JLS SE 21 §4.5–4.8 | ☑ spec |
  | `org.junit.jupiter:junit-jupiter` | test harness | junit.org/junit5 | ☐ verify at pin |
  | `org.assertj:assertj-core` | fluent assertions (readable tests) | assertj.github.io | ☐ verify at pin |
  | `com.github.spotbugs`/`pmd`/Sonar (build plugin) | flags raw/wildcard for the TRY-IT | tool docs | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java 21 toolchain property; `maven-compiler-plugin` with `-Xlint:all` (specifically `unchecked,rawtypes`) so a raw type fails the build.
  - **Externalized config** — `<compilerArgs>` and the analyzer ruleset (Sonar/PMD) in the POM/profile, not inline.
  - **At least one test** — asserts `pushAll(Iterable<? extends E>)` accepts a `List<Integer>` into a `Stack<Number>` and `popAll(Collection<? super E>)` drains into a `List<Object>` (PECS proven by compilation + behavior).
  - **Observability / health surface** — the build's `-Xlint` output + analyzer report is the "health surface" for type-safety (no runtime endpoint; note this in the chapter).
  - **Explicit failure path** — a deliberately UNSAFE varargs method (stores the array / returns it) shown to trigger the heap-pollution warning, contrasted with a genuinely-safe one carrying `@SafeVarargs`; the failure path proves *why* `@SafeVarargs` is an assertion you must earn (Item 32).
- **Displayed-snippet tie (tag-region includes; each ≤ 9 lines):**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `pecs-pushall` | `pushAll(Iterable<? extends E>)` — producer-extends | `Stack.java` |
  | `pecs-popall` | `popAll(Collection<? super E>)` — consumer-super | `Stack.java` |
  | `suppress-justified` | narrowest-scope `@SuppressWarnings("unchecked")` + proof comment | `Stack.java` |
  | `unsafe-varargs` | the heap-pollution failure path (deliberately unsafe) | `Varargs.java` |

- **Run command:** `./mvnw -B verify` (no service needed)
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** clean compile with `-Xlint:unchecked,rawtypes` (zero warnings on the good code); the TRY-IT raw-type/wildcard-return edit produces a compiler `unchecked` warning and Sonar `java:S3740`/`java:S1452` findings; tests green; the unsafe-varargs demo produces the documented heap-pollution warning.
- **Figure plan** (GUIDELINES §8):
  - **Chapter class:** code-craft / standard chapter → modest budget: **1–2 designed diagrams + 0–1 captured screenshot**.
  - **Candidate designed diagram(s) + family:**
    - **Fig 14.1 — "The PECS variance ladder"** (concept/flow family): `? extends T` (read-only, covariant, producer) vs `? super T` (write-only-as-T, contravariant, consumer) vs invariant `T`, each with the allowed/forbidden operation. Trace each cell to JLS §4.5.1/§4.10.2 + EJ Item 31. HTML → PNG via `05-figures/_assets/render.mjs` (never image-generated).
    - **Fig 14.2 — "Compile-time vs run-time: erasure"** (architecture/timeline family): `List<String>` / `List<Integer>` at source collapsing to one `List` at run time; reifiable vs non-reifiable side panel. Trace to JLS §4.6/§4.7.
  - **Candidate captured surface:** an IDE/analyzer screenshot of the `unchecked` warning + Sonar `java:S3740` on the raw-type TRY-IT edit (subject-native capture; allowed under the technical profile). Trace to the tool's own rule page.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool versions & rule defaults/severities** — `java:S3740` (Code Smell vs other), `java:S1452`, PMD `UseDiamondOperator`/`LooseCoupling` ruleset names, Error Prone `TypeParameterUnusedInFormals` severity — all `TO-PIN` in SOURCE-PIN.md → confirm at the pinned tool versions before stating defaults. (`⚠ verify at pin`.)
- ⚠ **Sonar rule page text** — `rules.sonarsource.com/java/RSPEC-3740` returned ECONNREFUSED at research time; rule existence/title corroborated via Sonar community + `sonar-java` source + `next.sonarqube.com` coding_rules. Re-fetch the canonical RSPEC page at pin to confirm exact title/severity. **Material → flagged** (`09-flags/14_sonar_rule_pages_unverified.md`).
- ⚠ **Java 21/25 JEP numbers** — record patterns = JEP 440 (Java 21); pattern matching for switch = JEP 441 (Java 21); confirm against pinned JDK docs (detail belongs to key 13; here keep the concept).
- ⚠ **Project Valhalla / reified generics** — must NOT be presented as a pinned fact; it is not in 21/25 → `⚠ AHEAD-OF-PIN`, flagged (`09-flags/14_valhalla_ahead_of_pin.md`).
- **Open question for draft:** how much variance theory (covariance/contravariance) to formalize vs teach by PECS examples — code-craft chapter should lead with examples; defer to draft.

---

## 8. Sources & further reading

### Primary / Official
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JLS (spec) | JLS SE 21 §4.5–4.8 (parameterized/erasure/reifiable/raw) | docs.oracle.com/javase/specs/jls/se21/html/jls-4.html | ☑ (§4.6/4.7/4.8 quoted) |
| 2 | JLS (spec) | JLS SE 21 §4.12.2, §9.6.4.7, §15.12.4.2 (heap pollution, `@SafeVarargs`) | same domain | ☐ confirm § at pin |
| 3 | JEP | JEP 213 — Milling Project Coin (diamond-on-anon; `@SafeVarargs` private) | openjdk.org/jeps/213 | ☑ |
| 4 | JDK docs | Type inference for generic instance creation (diamond, SE 7) | docs.oracle.com/javase/7/docs/technotes/guides/language/type-inference-generic-instance-creation.html | ☑ |
| 5 | JDK docs | Non-reifiable varargs / improved warnings | docs.oracle.com/javase/8/docs/technotes/guides/language/non-reifiable-varargs.html | ☑ |
| 6 | dev.java (Oracle) | Generics — Type Erasure | dev.java/learn/generics/type-erasure/ | ☑ |
| 7 | Book canon | Effective Java 3e (Bloch, 2018) — Ch.5 Items 26–33 | print + InformIT TOC | ☑ titles |
| 8 | Tool catalog | Sonar `java:S3740` raw types; `java:S1452` wildcard returns | rules.sonarsource.com/java/RSPEC-3740 ; sonar-java `WildcardReturnParameterTypeCheck` | ⚠ S3740 page ECONNREFUSED — re-fetch at pin |
| 9 | Tool catalog | PMD Java rules — `UseDiamondOperator`, `LooseCoupling` | pmd.github.io/pmd/pmd_rules_java.html | ⚠ verify at pin |
| 10 | Tool catalog | Error Prone — `TypeParameterUnusedInFormals` | errorprone.info/bugpatterns ; github.com/google/error-prone | ☑ pattern exists |
| 11 | Tool docs | Checker Framework manual (pluggable type qualifiers, generics bounds) | checkerframework.org/manual/ | ☑ |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | NUS CS2030S | Type Erasure / Unchecked Warnings teaching notes | nus-cs2030s.github.io/2021-s2/21-erasure.html | ☑ (corroboration) |
| 2 | InformIT | EJ Item 31 — bounded wildcards (excerpt) | informit.com/articles/article.aspx?p=2861454 | ☑ |
| 3 | Wikipedia | Wildcard (Java) | en.wikipedia.org/wiki/Wildcard_(Java) | color only |

> Source-quality order: JLS/JEP → JDK docs/dev.java → the named book canon (EJ 3e) → each tool's own rule catalog (at its pin) → teaching notes/secondary (corroboration only). No content farms / AI text as factual sources.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | JLS SE 21 generics/erasure/unchecked | web search + dev.java | erasure, reifiable/non-reifiable, unchecked warning, heap pollution definitions |
| 2 | Effective Java 3e generics items | web search + gist fetch | Items 26–33 exact titles (PECS = Item 31) |
| 3 | Sonar `java:S3740`/`S1452` | web search (RSPEC page ECONNREFUSED) | rule existence/title via sonar-java source + community + next.sonarqube |
| 4 | Error Prone generics patterns | web search + github | `TypeParameterUnusedInFormals` (return-only type param) confirmed |
| 5 | PMD `UseDiamondOperator`/`LooseCoupling` | web search | both exist; documented FPs (diamond on Java 21; loose-coupling generics) |
| 6 | JLS SE 21 ch.4 fetch | docs.oracle.com | §4.6/4.7/4.8 verbatim definitions + §4.8-1 raw `Cell` example |
| 7 | JEP 213 / diamond / `@SafeVarargs` | web search | diamond SE7; JEP 213 (Java 9) anon-diamond + private `@SafeVarargs` |
| 8 | Checker Framework manual | web search | pluggable type qualifiers; nullness guarantee; generic bound annotation rule |

---
## Learnings & pipeline suggestions
- **Spec-section discipline paid off:** fetching JLS SE 21 ch.4 directly gave exact §4.6/4.7/4.8 numbers and the §4.8-1 example — secondaries blurred these. Reinforces the Durable principle #1 (edition's own text for edition-specific facts) — extend it explicitly to **JLS § numbers**, not just ISO/JEP editions.
- **New folklore candidate:** "reified generics are coming (Valhalla)" is frequently stated as imminent fact; it is not in Java 21/25. Propose adding to the Folklore list: *"Java will get reified generics soon" — Project Valhalla is exploratory; mark AHEAD-OF-PIN, never assert.*
- **Reusable shape:** §2.4 PECS ladder + §2.6 heap-pollution failure path is a clean "idiom + earned-assertion" pattern; reuse for key 16 (try-with-resources / `AutoCloseable`) and key 11 (null-safety) — idiom plus the one place you must justify a suppression.
- **Cross-ref:** language deltas → key 13 (records/patterns + generics); null-safety sibling → key 11; analyzer rule depth → keys 28 (PMD), 30 (Error Prone), 32 (Checker Framework), 35 (Sonar); EJ canon → key 08. Record in merge notes.
- Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
