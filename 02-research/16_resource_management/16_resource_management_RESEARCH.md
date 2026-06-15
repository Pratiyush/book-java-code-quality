# RESEARCH DOSSIER — Java Code Quality Book

> Code-craft (Tier-B / Part II) dossier, **Standard** depth band. Every specific fact (rule IDs, API
> signatures, JEP numbers, JLS section numbers, GAV coordinates) is traced to a pinned authority in
> `00-strategy/SOURCE-PIN.md` — the JLS/JDK API at the pinned feature level, named-book canon (*Effective
> Java* 3e), and each tool's own pinned docs (PMD, SpotBugs, Error Prone, SonarSource). Tool versions are
> `TO-PIN` in SOURCE-PIN.md, so tool/doc identity is cited and exact versions/thresholds are marked
> `⚠ verify at pin`. Unconfirmed items collected in §7; material unverified items flagged to `09-flags/`.
> **Not a `⚠` comparison key** — but it touches several tools whose claims are each cited to that tool's own source.

---

## Topic
- **Key:** 16 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Resource & lifecycle management — try-with-resources, `AutoCloseable`, cleanup
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B (code-craft) · **Depth band:** Standard
- **Cmp:** (none — not a comparison/agnostic-sensitive key; tool claims still each cited to their own source)
- **Java code quality pin:** SOURCE-PIN.md — Anchor **Java 21 (LTS)**; forward LTS **Java 25** called out where a delta matters.
- **Primary dependency / source units (API signatures, JLS sections, JEPs, rule IDs):**
  - **JLS** §**14.20.3** *try-with-resources* (basic §14.20.3.1 / extended §14.20.3.2), SE 21 edition.
  - **`java.lang.AutoCloseable`** — `void close() throws Exception` (java.base, SE 21 API).
  - **`java.io.Closeable`** — `void close() throws IOException` (extends `AutoCloseable`; idempotent contract).
  - **`java.lang.ref.Cleaner`** + **`Cleaner.Cleanable`** (java.base, since Java 9).
  - **`Throwable.addSuppressed(Throwable)`** / **`Throwable.getSuppressed()`** (suppressed-exception machinery).
  - **JEP 213** *Milling Project Coin* (Java 9 — effectively-final resources in try-with-resources).
  - **JEP 421** *Deprecate Finalization for Removal* (Java 18) — `Object.finalize()` deprecated-for-removal.
  - **Effective Java 3e** — **Item 9** "Prefer try-with-resources to try-finally"; **Item 8** "Avoid finalizers and cleaners".
  - Tool rules: SonarSource **`java:S2095`** ("Resources should be closed"); PMD **`CloseResource`** (errorprone category); SpotBugs **`OBL_UNSATISFIED_OBLIGATION`**, **`OS_OPEN_STREAM`**, **`ODR_OPEN_DATABASE_RESOURCE`**; Error Prone **`MustBeClosed`** (`@MustBeClosed`), **`StreamResourceLeak`**, **`Finalize`**.
- **Canonical doc page(s):**
  - `AutoCloseable` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AutoCloseable.html
  - `Closeable` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/Closeable.html
  - `Cleaner` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/ref/Cleaner.html
  - JLS §14.20.3 — https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.3
  - JEP 213 — https://openjdk.org/jeps/213 · JEP 421 — https://openjdk.org/jeps/421
- **Canonical source path(s):** JDK API at the pinned feature level (`java.base`: `java/lang/AutoCloseable`, `java/io/Closeable`, `java/lang/ref/Cleaner`, `java/lang/Throwable`); JLS SE 21 §14.20.3.

---

## 1. Core definition & purpose

**Central claim.** Many Java objects own a scarce, non-memory resource — a file handle, socket, JDBC
`Connection`/`Statement`/`ResultSet`, native memory, a lock, a thread pool — that the garbage collector
**does not** release. The GC reclaims *memory*; it does not promptly close *handles*. Quality code therefore
makes resource release **deterministic and exception-safe**: every resource is closed exactly when its
useful life ends, on every path including the exceptional one. The language mechanism for this is the
**try-with-resources** statement (JLS §14.20.3) over the **`AutoCloseable`** interface; the explicit
contract is the **`close()`** method; the last-resort safety net is the **`Cleaner`** API. The failure mode
this prevents is **resource leakage** — file-descriptor exhaustion, connection-pool starvation, locks held
forever — which surfaces as intermittent production failures, not compile errors.

**The problem with the older idiom (stated as mechanism, not folklore).** Before Java 7, correct cleanup
required `try`/`finally`, and *Effective Java* 3e Item 9 documents that nested `try`-`finally` blocks "don't
scale well with the increase of resources required to be closed, and it's tricky to get it right, even in
JDK" — and that a `close()` failing inside `finally` can **mask** the original exception from the `try`
block. *(Source: Effective Java 3e, Item 9.)* Try-with-resources fixes both: it generates the cleanup, and
it **suppresses** rather than masks secondary exceptions.

**Where it sits.** This is a **code-craft / language** topic (Part II): a build-of-the-code concern, not a
runtime-framework concern. It is enforced two ways — by the *language* (the compiler desugars
try-with-resources and wires suppressed exceptions) and by *static analysis* (SonarSource `java:S2095`, PMD
`CloseResource`, SpotBugs `OBL_*`/`OS_OPEN_STREAM`, Error Prone `MustBeClosed`/`StreamResourceLeak`) which
flags resources that escape a try-with-resources/`finally` close. The build-time vs runtime split: the
*compiler transformation* and the *linter check* are build-time; the *handle release* is runtime.

**When introduced.** try-with-resources + `AutoCloseable`: **Java 7** (Project Coin). Effectively-final
resources in the header: **Java 9** (JEP 213). `Cleaner`: **Java 9**. `finalize()` deprecated: Java 9;
deprecated-**for-removal**: Java 18 (JEP 421). All behaviors below confirmed present and unchanged at the
**Java 21** pin (and still present, unremoved, at Java 25 — see §5).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 `AutoCloseable` and `Closeable` — the two contracts

`java.lang.AutoCloseable` is the root interface; its single method is **`void close() throws Exception`**.
The class-level javadoc (SE 21): *"An object that may hold resources (such as file or socket handles) until
it is closed. The `close()` method of an `AutoCloseable` object is called automatically when exiting a
`try`-with-resources block… This construction ensures prompt release, avoiding resource exhaustion exceptions
and errors."* *(Source: `AutoCloseable` SE 21 javadoc — verified.)*

`java.io.Closeable` **extends** `AutoCloseable` and narrows the signature to **`void close() throws
IOException`**. The **idempotency** difference is a load-bearing, exam-grade Java fact, quoted verbatim from
the `AutoCloseable` javadoc:

> "Note that unlike the `close` method of `Closeable`, this `close` method is **not** required to be
> idempotent. In other words, calling this `close` method more than once may have some visible side effect,
> unlike `Closeable.close` which is required to have no effect if called more than once." *(Verified.)*

The same javadoc records four "strongly encouraged / advised" design rules for implementers — directly
relevant to "writing a quality `AutoCloseable`":
1. *"implementers of this interface are strongly encouraged to make their `close` methods idempotent."*
2. *"Implementers are strongly encouraged to declare concrete implementations of the `close` method to throw
   more specific exceptions, or to throw no exception at all if the close operation cannot fail."*
3. *"Implementers of this interface are also strongly advised to not have the `close` method throw
   `InterruptedException`"* — because a suppressed `InterruptedException` corrupts a thread's interrupt status.
4. *"It is strongly advised to relinquish the underlying resources and to internally mark the resource as
   closed, prior to throwing the exception."*

*(All four quoted from the SE 21 `AutoCloseable` javadoc — verified.)*

### 2.2 The try-with-resources statement (JLS §14.20.3) — semantics

Step-by-step, as specified by JLS §14.20.3 (SE 21):

1. **Resource scope & order.** Resources are declared (or, since Java 9, named) in the header; they are
   initialized left-to-right.
2. **Reverse-order close.** When the `try` block completes — normally or abruptly — each initialized resource
   has `close()` invoked in the **reverse order** of initialization (LIFO). *(Verified — JLS §14.20.3.)*
3. **Close always runs.** `close()` is called even if a *resource initialization* throws, or the `try` block
   throws. A resource whose initializer threw is not closed (it was never initialized); previously-initialized
   resources are. *(Verified — JLS §14.20.3.)*
4. **Suppressed exceptions.** If the `try` block throws exception E1, and a `close()` then throws E2, **E1 is
   propagated** and **E2 is suppressed** — attached via `Throwable.addSuppressed(E2)` and retrievable via
   `getSuppressed()`. This is the inversion of the old `try`/`finally` masking bug, where the `finally`
   exception would *replace* E1. *(Verified — JLS §14.20.3; corroborated by the Java Tutorials
   "try-with-resources" page and Effective Java Item 9.)*
5. **`AutoCloseable` requirement.** Every resource's static type must be a subtype of `AutoCloseable`;
   otherwise it is a compile-time error. *(Verified — JLS §14.20.3.)*

**Basic vs extended form (§14.20.3.1 vs §14.20.3.2):**
- **Basic (§14.20.3.1)** — the resource is *declared* in the header: `try (var in = open()) { … }`.
- **Extended (§14.20.3.2, Java 9 / JEP 213)** — the header may name a **final or effectively-final**
  variable already in scope: `try (existingResource) { … }`, with no new declaration. The variable must be
  final or effectively final; this restriction (vs allowing arbitrary expressions) is what avoids the
  semantic problems that earlier proposals hit. *(Verified — JEP 213; JLS §14.20.3.2.)*

### 2.3 What the compiler generates (build-time behavior)

The compiler **desugars** a try-with-resources into a `try`/`finally`-shaped form that (a) closes each
resource in reverse order and (b) routes any `close()` exception through `addSuppressed` when a primary
exception is in flight. The book draft can show the desugaring conceptually but should NOT quote a specific
generated-bytecode shape as spec — the *observable* contract (reverse order, suppressed exceptions, always-
close) is the spec; the exact synthetic code is an implementation detail. *(Mechanism per JLS §14.20.3; the
"desugar to try/finally + addSuppressed" framing is corroborated by the Java Tutorials and Item 9.)*

### 2.4 The `Cleaner` safety net (last resort, not primary)

`java.lang.ref.Cleaner` (since Java 9) registers a **cleaning action** (a `Runnable`) that runs *after* the
registered object becomes **phantom-reachable**, returning a `Cleaner.Cleanable`. Quoted/paraphrased from the
SE 21 `Cleaner` javadoc (verified): the most efficient use is to **explicitly invoke `clean()`** when the
object is closed; the action runs **at most once**; the cleaning action **must not refer to the object being
registered** (or it would keep it alive and never run); a **static nested `State` class** is the documented
pattern to hold cleanup state without retaining the outer object; cleaning actions should be quick and must
tolerate concurrent execution. The canonical pattern in the javadoc: a class `implements AutoCloseable`, holds
a `static final Cleaner`, a `static` nested `State implements Runnable`, registers it, and has `close()`
call `cleanable.clean()`. *(Source: `Cleaner` SE 21 javadoc — verified; this is the Effective Java Item 8
"safety net" pattern.)*

### 2.5 Reference units (API signatures, JLS sections, JEPs, tool rules)

| Name | Type | Default / value | Fixed early (introduced) | Source |
|---|---|---|---|---|
| `AutoCloseable.close()` | API — `void close() throws Exception` | not idempotent (by contract) | Java 7 | `AutoCloseable` SE 21 javadoc ✅ |
| `Closeable.close()` | API — `void close() throws IOException` | idempotent ("no effect if called more than once") | Java 5 (Closeable); subtypes AutoCloseable in 7 | `Closeable`/`AutoCloseable` SE 21 javadoc ✅ |
| `Throwable.addSuppressed(Throwable)` | API | wires suppressed list | Java 7 | JLS §14.20.3; `Throwable` SE 21 javadoc ✅ |
| `Throwable.getSuppressed()` | API — returns `Throwable[]` | empty array if none | Java 7 | `Throwable` SE 21 javadoc ✅ |
| try-with-resources (basic) | JLS §14.20.3.1 | reverse-order close, suppressed exceptions | Java 7 | JLS SE 21 §14.20.3.1 ✅ |
| try-with-resources (effectively-final) | JLS §14.20.3.2 | final/eff-final var as resource | Java 9 (JEP 213) | JLS §14.20.3.2; JEP 213 ✅ |
| `Cleaner` / `Cleaner.Cleanable` | API (`java.lang.ref`) | phantom-reachable, runs ≤ once | Java 9 | `Cleaner` SE 21 javadoc ✅ |
| `Object.finalize()` | API | deprecated **for removal** | dep. Java 9; for-removal Java 18 (JEP 421) | JEP 421; SE 21 deprecated-list ✅ |
| `java:S2095` "Resources should be closed" | Sonar rule (BUG) | enabled in default Java profile `⚠ verify at pin` | n/a | rules.sonarsource.com (S2095) ✅ identity; threshold `⚠` |
| PMD `CloseResource` | PMD rule (category: errorprone) | props: `types`, `closeTargets`, `closeAsDefaultTarget`, `allowedResourceTypes` | n/a | pmd.github.io errorprone rules ✅ |
| SpotBugs `OBL_UNSATISFIED_OBLIGATION` | SpotBugs pattern | "obligation to clean up not satisfied" | n/a | spotbugs.github.io findbugs descriptions ✅ identity |
| SpotBugs `OS_OPEN_STREAM` / `ODR_OPEN_DATABASE_RESOURCE` | SpotBugs patterns | stream / DB resource not closed on all paths | n/a | spotbugs descriptions ✅ identity |
| Error Prone `MustBeClosed` (`@MustBeClosed`) | Error Prone bug pattern (WARNING) | "returns a resource which must be managed carefully" | n/a | errorprone.info/bugpatterns ✅ |
| Error Prone `StreamResourceLeak` | Error Prone bug pattern (WARNING) | "Streams encapsulating a closeable resource should be closed using try-with-resources" | n/a | errorprone.info/bugpatterns ✅ |
| Error Prone `Finalize` | Error Prone bug pattern (WARNING) | "Do not override finalize" | n/a | errorprone.info/bugpatterns ✅ |

> Tool **versions/default thresholds** are `TO-PIN` in SOURCE-PIN.md → rule **identity** is verified against
> each tool's own docs; exact default-severity / threshold / enabled-by-default state is `⚠ verify at pin`.

---

## 3. Evidence FOR (the mechanism is real, canonical, and tool-backed)

- **Specified, not idiomatic.** try-with-resources is a *language* construct with normative semantics in JLS
  §14.20.3 (reverse-order close, suppressed exceptions, always-close). It is GA since Java 7 and unchanged at
  the Java 21 pin. *(JLS SE 21 — verified.)*
- **Named-canon endorsement.** *Effective Java* 3e devotes **Item 9** to "Prefer try-with-resources to
  try-finally," giving the masking-exception and scaling arguments; **Item 8** ("Avoid finalizers and
  cleaners") positions `AutoCloseable` + try-with-resources as the primary cleanup tool and `Cleaner` only as
  a safety net / native-resource terminator. *(Effective Java 3e — verified via item-list authorities;
  verbatim wording to confirm against print, §7.)*
- **First-class library support.** The entire `java.io`/`java.nio`/`java.sql`/`java.util.concurrent` surface
  implements `AutoCloseable`/`Closeable` (streams, channels, `Connection`/`Statement`/`ResultSet`,
  `ExecutorService` is `AutoCloseable` since Java 19 `⚠ verify at pin`), so the construct applies broadly.
- **Tool consensus (each cited to its own source).** Every major analyzer ships a dedicated unclosed-resource
  check: SonarSource `java:S2095`, PMD `CloseResource`, SpotBugs `OBL_*`/`OS_OPEN_STREAM`/`ODR_*`, Error
  Prone `MustBeClosed` + `StreamResourceLeak`. The industry treats unclosed resources as a gate-able defect
  class. *(Each rule verified on that tool's own docs page — see §8.)*
- **CERT mapping.** SEI CERT Oracle Java rule **ERR54-J** ("Use a try-with-resources statement to safely
  handle closeable resources") makes it a secure-coding requirement. *(wiki.sei.cmu.edu — corroboration.)*

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **`AutoCloseable.close()` is not idempotent by contract.** A double-close (e.g. close in the body *and*
  again via try-with-resources, or a resource shared across two try blocks) may have side effects. The
  javadoc only *encourages* idempotency; it does not require it. Quality code must not assume safe re-close.
  *(AutoCloseable SE 21 javadoc — verified.)*
- **Suppressed exceptions are easy to miss.** When `close()` throws while a primary exception is propagating,
  the close failure is **swallowed into `getSuppressed()`** and is invisible in default logging unless the
  logger prints suppressed throwables. Diagnostics can be lost. *(JLS §14.20.3 mechanism.)*
- **Resources created *outside* the header still leak.** try-with-resources only manages resources named in
  its header. A resource created in the body, passed to a helper, returned from a factory, or stored in a
  field is **not** auto-closed — which is precisely the false-negative class the linters (`java:S2095`, PMD
  `CloseResource`, Error Prone `MustBeClosed`) and their known gaps (Sonar FNs after helper-method calls)
  target. *(Sonar community FN reports — color; rule scope per S2095 docs.)*
- **`Cleaner` gives weak timing guarantees.** A cleaning action runs only **after** phantom-reachability is
  observed — *some time later*, possibly never before JVM exit, and on a cleaner thread. It is a **safety net,
  not a release mechanism**; relying on it for prompt release reintroduces the finalizer problems. *(Cleaner
  SE 21 javadoc; Effective Java Item 8.)*
- **`finalize()` is a trap (deprecated for removal).** JEP 421 documents finalization's "security,
  reliability, and performance risks": unpredictable timing, object resurrection, exceptions silently
  dropped, GC interference. It is deprecated-for-removal; Error Prone `Finalize` flags overrides. *(JEP 421;
  errorprone.info — verified.)*
- **Wrapping-stream double-close & "decorator closes delegate".** Closing an outer wrapper
  (`BufferedReader`) closes the underlying stream; constructing the wrapper inside the try header is the
  correct idiom, but a leak occurs if the wrapper constructor throws *after* the inner stream opens — a
  known sharp edge the JLS reverse-order close addresses only when each is its own header resource.
  *(JLS §14.20.3; Effective Java Item 9 multi-resource example.)*
- **When NOT to reach for it.** (a) An object whose `close()` is a no-op (`StringReader`,
  `ByteArrayOutputStream`) does not need closing — `java:S2095` itself exempts these. (b) A resource whose
  lifetime must **outlive** the method (a connection pool held in a field, a long-lived `ExecutorService`
  bound to the application) is closed by the *owner's* lifecycle, not a method-local try block. (c) Use
  `Cleaner`/`PhantomReference` **only** as a backstop for native resources or to guard against a forgotten
  `close()` — never as the primary path. *(S2095 exceptions; Cleaner javadoc; Effective Java Item 8.)*
- **Neutral note on competing-approach-inside-Java.** `Cleaner`, `PhantomReference`, manual `try`/`finally`,
  and try-with-resources are different points on a determinism/safety axis: try-with-resources gives
  deterministic, scoped release; `Cleaner` gives a best-effort GC-time backstop; manual `try`/`finally` gives
  full control at the cost of the masking-exception and scaling hazards Item 9 documents. The book maps each
  to where a team would reasonably choose it; it crowns none.

---

## 5. Current status

- **Stable / GA.** try-with-resources, `AutoCloseable`, `Closeable`, suppressed-exception machinery, and the
  effectively-final extension are all GA and unchanged at the **Java 21** pin. No deprecation.
- **`Cleaner`** is stable (since Java 9), the endorsed replacement for finalization.
- **Finalization** is **deprecated for removal** (JEP 421, Java 18) but **still present and not removed** in
  both Java 21 and Java 25; the SE 25 deprecated-list still lists `Object.finalize()` for removal and points
  to `Cleaner`/`AutoCloseable` as migration paths. A `--finalization=disabled` JVM flag exists (JEP 421) to
  test life without finalizers. *(JEP 421; SE 21 & SE 25 deprecated-lists — verified.)*
- **Java 25 delta:** no change to the try-with-resources/`AutoCloseable` contract. `ExecutorService`
  implementing `AutoCloseable` (Java 19) and **structured concurrency** `StructuredTaskScope` (preview track,
  key 22) make scoped, auto-closing lifecycles more common — note the cross-reference, do not assert preview
  API shape as settled (`⚠ AHEAD-OF-PIN` for any preview-only signature; owned by key 22).
- **No supersession.** The construct is the settled, idiomatic answer; movement is in *tooling precision*
  (analyzers reducing false negatives) not in the language contract.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `16_resource_management`'s row in `DEMO-CATALOG.md` — demo name
  **"deterministic resource cleanup & suppressed exceptions"**; the Java surface it exercises:
  try-with-resources (basic + effectively-final), a custom `AutoCloseable`, `Throwable.getSuppressed()`, and
  a `Cleaner` safety-net backstop; **TRY-IT exercise:** "remove the resource from the try-header (or make
  `close()` throw) and observe the leak warning from `java:S2095`/SpotBugs and the suppressed-exception
  output." *(If `DEMO-CATALOG.md` lacks a row 16, the catalog-maintainer adds it from this spec.)*
- **Module key / path:** `08-companion-code/16_resource_management/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property) | establishes the Java 21 pin | SOURCE-PIN runtime baseline | ☐ (inherit) |
  | JDK `java.base` (`java.lang.AutoCloseable`, `java.lang.ref.Cleaner`, `java.sql.*`) | primary unit under study — try-with-resources + cleanup | SE 21 API | ☑ (API verified) |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness — assert close order + suppressed exceptions | junit.org/junit5 | ☐ `⚠ verify at pin` |
  | `org.assertj:assertj-core` | readable assertions on `getSuppressed()` / close-order log | assertj.github.io | ☐ `⚠ verify at pin` |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property, no loose versions.
  - **Externalized config / profiles** — a `resources.dir` / DB URL key read from a profile-specific
    properties file (name it in the module), so the resource target is environment-specific, not hard-coded.
  - **At least one test** — `closesResourcesInReverseOrder()` asserts LIFO close order via an in-memory log;
    `primaryExceptionWinsCloseSuppressed()` asserts `getSuppressed()[0]` is the close failure.
  - **Observability / health surface** — a counter/log line on each `close()` (open/close balance), the
    "resource accounting" surface the topic touches.
  - **Explicit failure path** — a custom `AutoCloseable` whose `close()` **throws** while the body also
    throws; the test proves the body exception propagates and the close exception is *suppressed* (the
    HONEST-LIMITATIONS floor demonstrated in code). Plus a `Cleaner`-registered backstop proving the safety
    net runs when `close()` is forgotten.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `twr-basic` | basic try-with-resources over a custom `AutoCloseable` | `ResourceDemo.java` |
  | `twr-effectively-final` | Java 9 effectively-final resource in the header | `ResourceDemo.java` |
  | `suppressed` | reading `getSuppressed()` after a close-failure path | `ResourceDemoTest.java` |
  | `cleaner-backstop` | `Cleaner` + static nested `State` safety net | `NativeHandle.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/16_resource_management exec:java` (or the module's
  `Main`); no external service required (resources are in-memory / temp-file).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** startup line; a close-order log reading reverse-of-open; on the failure path, the
  primary exception printed with a "Suppressed:" close exception beneath it; the `Cleaner` backstop line
  emitted when an instance is dropped without `close()`; **all tests green** (close-order + suppressed +
  backstop assertions pass). Running the analyzers over a deliberately-leaky variant flags `java:S2095` /
  SpotBugs `OS_OPEN_STREAM`.
- **Figure plan** (GUIDELINES §8):
  - **Chapter class:** *code-craft / mechanism* chapter → image budget **1–2 designed diagrams**, **0
    captured screenshots** (no UI surface central to the topic; an optional IDE inspection capture is
    allowed but not load-bearing).
  - **Candidate designed diagram(s) + family:**
    - **Fig 16.1 — try-with-resources control flow & suppressed-exception routing** (flow family): the
      happy path vs the "body throws + close throws" path, showing reverse-order close and E2 attached to
      E1 via `addSuppressed`. Trace each arrow/label to **JLS §14.20.3**.
    - **Fig 16.2 — the cleanup-determinism spectrum** (concept/axis family): try-with-resources
      (deterministic, scoped) → manual `try`/`finally` (deterministic, manual) → `Cleaner`/`PhantomReference`
      (GC-time backstop) → `finalize()` (deprecated for removal). No crowning — each annotated with "when it
      fits." Trace labels to `AutoCloseable`/`Cleaner` javadocs, Effective Java Items 8–9, JEP 421.
  - **Candidate captured surface(s):** *(optional, non-load-bearing)* an IDE inspection / SpotBugs report
    showing an unclosed-resource warning — capture only if the draft needs it; not in the core budget.
  - **Source trace per depicted claim:** Fig 16.1 → JLS SE 21 §14.20.3 (reverse-order, suppressed); Fig 16.2
    → `AutoCloseable` SE 21 javadoc, `Cleaner` SE 21 javadoc, JEP 421, Effective Java 3e Items 8–9.

---

## 7. Gap-filling (verification queue)

- **`java:S2095` default-profile state & exact exception list** — confirm it is in the default "Sonar way"
  Java profile and the full exempt-types list (StringReader, ByteArrayOutputStream, …) at the pinned analyzer
  version. → `⚠ verify at pin`. (The rules.sonarsource.com page resolves under the per-language path; one
  fetch URL 404'd — re-fetch at the pinned rule URL.)
- **PMD `CloseResource` exact property defaults** — confirm default `types`/`closeTargets`/
  `allowedResourceTypes` values at the pinned PMD version (category confirmed = `errorprone`). → `⚠ verify`.
- **SpotBugs pattern set** — confirm `OBL_UNSATISFIED_OBLIGATION`, `OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE`,
  `OS_OPEN_STREAM`, `OS_OPEN_STREAM_EXCEPTION_PATH`, `ODR_OPEN_DATABASE_RESOURCE` exist and their default
  bug-rank/category at the pinned SpotBugs version. Note OBL requires the fb-contrib/JSR-305-ish
  `@CleanupObligation`/`@WillClose` annotations for full power. → `⚠ verify at pin`.
- **Error Prone severities** — `MustBeClosed`, `StreamResourceLeak`, `Finalize` confirmed as bug patterns;
  confirm default severity (shown WARNING) and whether `MustBeClosed` requires the `@MustBeClosed`
  annotation on the producing method at the pinned Error Prone version. → `⚠ verify at pin`.
- **`ExecutorService implements AutoCloseable`** — confirm since Java 19 and behavior at Java 21 (auto-close
  = orderly shutdown + await). → `⚠ verify at pin` against SE 21 `ExecutorService` javadoc.
- **Effective Java 3e verbatim** — confirm exact wording of Item 9's masking-exception passage and Item 8's
  "safety net / native resource" conclusion + page numbers before block-quoting. → `⚠ verify at print`.
- **Cleaner javadoc example code** — confirm the exact `CleaningExample` listing shape before reproducing
  (≤ 9-line snippet rule). → `⚠ verify at pin`.
- **JEP 213 / 421 numbers & target releases** — JEP 213 = Java 9 (effectively-final resources); JEP 421 =
  Java 18 (deprecate finalization for removal) — verified via openjdk.org; re-confirm at draft.
- **Flagged to `09-flags/`:** `16_s2095_tool_versions_unverified.md` (tool versions are TO-PIN; rule
  identities verified, exact thresholds/defaults/enabled-state unverified until `/pin-source`).

---

## 8. Sources & further reading

### Primary / Official (the pinned authority set: JDK API, JLS, JEPs, named canon, each tool's own docs)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JDK API | `java.lang.AutoCloseable` (SE 21) — close() contract, idempotency note | docs.oracle.com/en/java/javase/21/.../AutoCloseable.html | ☑ |
| 2 | JDK API | `java.io.Closeable` (SE 21) — idempotent close, IOException | docs.oracle.com/en/java/javase/21/.../io/Closeable.html | ☑ (via AutoCloseable cross-ref) |
| 3 | JLS | JLS SE 21 §14.20.3 / .3.1 / .3.2 — try-with-resources | docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.3 | ☑ |
| 4 | JDK API | `java.lang.ref.Cleaner` (SE 21) — Cleanable, State pattern | docs.oracle.com/en/java/javase/21/.../ref/Cleaner.html | ☑ |
| 5 | JEP | JEP 213 — Milling Project Coin (effectively-final resources, Java 9) | openjdk.org/jeps/213 | ☑ |
| 6 | JEP | JEP 421 — Deprecate Finalization for Removal (Java 18) | openjdk.org/jeps/421 | ☑ |
| 7 | JDK API | SE 21 & SE 25 Deprecated List — `Object.finalize()` for removal | docs.oracle.com/en/java/javase/{21,25}/docs/api/deprecated-list.html | ☑ |
| 8 | Book canon | Bloch, *Effective Java* 3e — Item 9 (try-with-resources), Item 8 (finalizers/cleaners) | print (2018) | ☑ items; ⚠ verbatim/page |
| 9 | Tool docs | SonarSource `java:S2095` "Resources should be closed" | rules.sonarsource.com/java/RSPEC-2095 | ☑ identity; ⚠ defaults |
| 10 | Tool docs | PMD `CloseResource` (errorprone category) + properties | pmd.github.io/.../pmd_rules_java_errorprone.html | ☑ |
| 11 | Tool docs | SpotBugs `OBL_*` / `OS_OPEN_STREAM` / `ODR_OPEN_DATABASE_RESOURCE` | spotbugs.github.io (bug descriptions) | ☑ identity; ⚠ version |
| 12 | Tool docs | Error Prone `MustBeClosed`, `StreamResourceLeak`, `Finalize` | errorprone.info/bugpatterns | ☑ |

### Accessible / Further reading (tutorials, talks, quality secondary sources)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Oracle Java Tutorials | "The try-with-resources Statement" | docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html | ☑ (corroboration) |
| 2 | SEI CERT | ERR54-J — use try-with-resources for closeable resources | wiki.sei.cmu.edu/confluence/display/java/ERR54-J | ☑ (corroboration) |
| 3 | Inside.java | Podcast #21 — JEP 421 & finalization deprecation | inside.java/2022/01/12/podcast-021 | color only |

> Source-quality order applied: JLS / JDK API at the pin → JEPs / release notes → named book canon (dated) →
> each tool's own pinned docs → CERT / official tutorials (corroboration) → podcasts (color). No content
> farms or AI text used as a factual source. Medium/Jenkov hits were demoted to non-cited corroboration.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | try-with-resources JLS / AutoCloseable / suppressed | web search | JLS §14.20.3; AutoCloseable vs Closeable idempotency; getSuppressed |
| 2 | AutoCloseable close() idempotency javadoc | web search + **fetch** SE 21 AutoCloseable | verbatim: not-idempotent note; 4 implementer rules; signature |
| 3 | JLS §14.20.3 | **fetch** jls-14.html | reverse-order close; always-close; addSuppressed; AutoCloseable requirement; §.3.2 eff-final |
| 4 | JEP 213 Milling Project Coin | web search | Java 9 effectively-final resources in try-with-resources |
| 5 | Effective Java 3e Items 8 & 9 | web search (item-list authorities) | Item 9 masking/scaling; Item 8 cleaners as safety net only |
| 6 | JEP 421 deprecate finalization | web search | Java 18 for-removal; Cleaner replacement; --finalization=disabled |
| 7 | Sonar S2095 / PMD CloseResource / SpotBugs OBL | web search | rule identities; S2095 exceptions (StringReader/ByteArrayOutputStream) |
| 8 | PMD errorprone rules page | **fetch** pmd.github.io | CloseResource: types/closeTargets/closeAsDefaultTarget/allowedResourceTypes |
| 9 | Error Prone bugpatterns | **fetch** errorprone.info | MustBeClosed (WARNING), StreamResourceLeak (WARNING), Finalize (WARNING) |
| 10 | Cleaner javadoc | web search | Cleanable; static State pattern; runs ≤ once; must not refer to object |
| 11 | finalize removal status Java 21/25 | web search | deprecated-for-removal, still present & unremoved at 21 and 25 |
| 12 | rules.sonarsource.com S2095 | **fetch** (404 at one URL) | re-fetch at pinned rule URL → §7 |

---
## Learnings & pipeline suggestions
- **Reusable "lifecycle-contract card" shape** for any chapter centered on a JDK interface contract
  (`AutoCloseable`, `equals`/`hashCode` key 15, `Comparable`, `Iterator`): *the signature / the documented
  contract clauses quoted verbatim / the idempotency-or-ordering rule / the tool that checks it / the sharp
  edge*. Mirrors the key-04 "metric card" and key-03 "two-schools" patterns. → propose to `templates/`.
- **Tool-rule identity vs threshold split (durable):** when SOURCE-PIN tool rows are `TO-PIN`, a Standard-band
  dossier can fully verify **rule identity + category + properties** from the tool's own docs while marking
  **exact default thresholds / enabled-by-default / severity** `⚠ verify at pin`. This is the right
  granularity for every Part IV tool chapter; record it so those dossiers don't over- or under-claim. →
  append below.
- **Cross-refs:** key 12 (exceptions — suppressed exceptions belong to *this* chapter's mechanism, link not
  re-derive); key 15 (`equals`/`hashCode` contracts — same "JDK contract" card); key 22 (virtual threads /
  structured concurrency — `StructuredTaskScope`/`ExecutorService` AutoCloseable, preview deltas owned there);
  keys 27–30/35 (the analyzers that own the deep tool tutorials — this chapter cites rule IDs, those chapters
  configure them); key 08 (Effective Java distilled — Items 8/9 land here). Record in merge notes.
- **Folklore watch:** none triggered; "GC closes your files" is a *misconception* worth stating-and-correcting
  (GC reclaims memory, not handles) — that is teaching, not folklore-as-fact.
