# RESEARCH DOSSIER — Java Code Quality Book

> Tier-B code-craft dossier. Every specific fact (Effective Java item number/title, JEP number, JLS
> construct, tool rule ID, default, flag) is traced to a pinned authority in `00-strategy/SOURCE-PIN.md`
> (a named book edition, the JEP/JLS, or a tool's own docs/repo). Tool **versions and exact default
> thresholds are TO-PIN** in SOURCE-PIN.md, so rule IDs and behaviour are cited to the tool's own docs
> and exact version-sensitive numbers are marked `⚠ verify at pin`. Anything untraceable → `⚠ UNVERIFIED`
> in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 12 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Error handling & exceptions done right — checked vs unchecked, fail-fast, error models
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B · **Depth band:** Standard (code-craft) · **Cmp:** none (no contested-practice flag), but
  the tool layer is a *neutral survey* of overlapping rules across Sonar / PMD / SpotBugs / Error Prone /
  Checkstyle — apply NEUTRALITY (no crowning; each tool's own docs cited).
- **Java code quality pin:** SOURCE-PIN.md (Java **21 LTS** anchor; **25 LTS** forward deltas; tools TO-PIN).
- **Primary dependency / source unit(s):**
  - **JLS SE 21** — the language definitions: §11 *Exceptions* (`Throwable` / `Error` / `Exception` /
    `RuntimeException` hierarchy; checked-exception "catch or specify" rule), §14.20 `try`/`catch`/`finally`,
    §14.20.3 try-with-resources, §11.2 compile-time checking of exceptions, multi-catch (§14.20).
  - **JEP 358 — Helpful NullPointerExceptions** (delivered JDK 14; on by default since JDK 15 — confirm §7).
  - ***Effective Java*, 3rd ed. (Bloch, 2018) — Chapter 10 "Exceptions," Items 69–77** (canon).
  - Tool rules: SonarSource `java:S112`/`java:S1166`/`java:S2221`/`java:S1181`/`java:S00108`/`java:S00112`,
    PMD `EmptyCatchBlock`/`AvoidCatchingGenericException`/`AvoidThrowingRawExceptionTypes`/`PreserveStackTrace`,
    SpotBugs `REC_CATCH_EXCEPTION`/`DE_MIGHT_IGNORE`/`RCN_*`, Error Prone `DeadException`/`InterruptedException`,
    Checkstyle `IllegalCatch`/`IllegalThrows`.
- **Canonical doc page(s):**
  - JLS SE 21 §11 — https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html
  - JEP 358 — https://openjdk.org/jeps/358
  - SonarSource Java rules — https://rules.sonarsource.com/java/
  - PMD Error-Prone / Best-Practices — https://pmd.github.io/pmd/pmd_rules_java_errorprone.html
  - SpotBugs bug descriptions — https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html
  - Checkstyle `IllegalCatch` — https://checkstyle.sourceforge.io/checks/coding/illegalcatch.html
  - Error Prone bug patterns — https://errorprone.info/bugpatterns
- **Canonical source path(s):** to be set under the per-tool fetch dirs in SOURCE-PIN.md once tools are
  pinned (e.g. `<pmd>/pmd-java/src/main/resources/category/java/errorprone.xml`,
  `<checkstyle>/.../checks/coding/IllegalCatchCheck.java`, `<sonar-java>/.../rules/.../S112.html`).

---

## 1. Core definition & purpose

**Central claim.** Exceptions are Java's *built-in error-signalling channel*; "doing them right" means
choosing the correct throwable kind for each failure, **failing fast** at the point a contract is violated,
never silently swallowing information, and presenting callers an **error model** (the set and shape of the
exceptions a module exposes) that is honest about what they can and cannot recover from. The chapter
replaces folk habits (`catch (Exception e) {}`, `throw new RuntimeException("oops")`, swallowed stack
traces) with a defensible, tool-enforceable discipline.

**Where it comes from.** The *language* gives the mechanism (JLS SE 21 §11 — the `Throwable` hierarchy and
the compile-time checked-exception rule). The *idiom canon* gives the discipline (*Effective Java* 3e,
Chapter 10, Items 69–77). The *tools* give enforcement (Sonar / PMD / SpotBugs / Error Prone / Checkstyle
rules that flag the anti-patterns). All three are in scope and pinned.

**The three throwable kinds (JLS + Effective Java Item 70).**
- **Checked exceptions** (`Exception` minus `RuntimeException`): subject to the compile-time
  *catch-or-specify* requirement (JLS §11.2). *Effective Java* Item 70: "use checked exceptions for
  conditions from which the caller **can reasonably be expected to recover**."
- **Unchecked / runtime exceptions** (`RuntimeException` and subclasses): not checked at compile time.
  Item 70: "use runtime exceptions to indicate **programming errors**" — typically precondition
  violations (e.g. `ArrayIndexOutOfBoundsException`).
- **Errors** (`Error` and subclasses): by convention reserved for the JVM (resource exhaustion,
  unrecoverable state). Item 70: "with the exception of `AssertionError`, you shouldn't throw them either,"
  and you should not subclass `Error`.
- Item 70 summary directive: "Throw checked exceptions for recoverable conditions and unchecked exceptions
  for programming errors. **When in doubt, throw unchecked exceptions.**"

**Where it sits.** Build-time vs runtime split: the **checked-exception rule is a compile-time gate** (JLS
§11.2 — code that can throw a checked exception must declare or handle it); the **runtime behaviour** is the
stack-unwinding, `finally`/try-with-resources cleanup, and (with JEP 358) the detailed NPE message produced
when an exception actually propagates. The static-analysis tools add a *third* gate (CI/PR time).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The language mechanism (JLS SE 21 §11)

- **The hierarchy:** `Throwable` → {`Error`, `Exception`}; `Exception` → {checked subclasses,
  `RuntimeException`}; `RuntimeException` and `Error` are the *unchecked* throwables (JLS §11.1.1).
- **Compile-time checking (JLS §11.2):** a method body that can throw a *checked* exception class must
  either catch it or list it in a `throws` clause; unchecked throwables are exempt. This is the property
  that makes "checked" load-bearing — the compiler is the first error-model gate.
- **`try`/`catch`/`finally` (JLS §14.20):** `finally` runs whether or not an exception is thrown.
- **Multi-catch (`catch (A | B e)`)** and **more-precise rethrow** (JLS §14.20) let one handler cover
  several types without widening to `Exception`.
- **try-with-resources (JLS §14.20.3):** an `AutoCloseable` declared in the resource spec is closed
  automatically; a close exception thrown while another is propagating is **suppressed** and attached via
  `Throwable.addSuppressed` / retrievable with `getSuppressed`. (Resource-management depth → key 16.)
- **JEP 358 — Helpful NullPointerExceptions:** the JVM analyses bytecode to name *which* expression was
  null, e.g. `Cannot invoke "...getAddressLine1()" because the return value of "...getAddress()" is null`.
  Delivered in JDK 14 behind `-XX:+ShowCodeDetailsInExceptionMessages`; **on by default from JDK 15**
  (⚠ confirm the "default-on since 15" detail against the JDK 15 release notes at pin — §7). This is the
  language's own "fail-fast with diagnosis" feature and anchors the fail-fast section on Java 21/25.

### 2.2 The idiom mechanism — Effective Java 3e, Chapter 10 (Items 69–77)

The chapter's spine is the nine Items, each a verifiable named rule (titles verbatim from the 3e TOC):

| Item | Title (verbatim, EJ 3e Ch.10) | The rule in one line |
|---|---|---|
| 69 | Use exceptions only for exceptional conditions | No exceptions for ordinary control flow. |
| 70 | Use checked exceptions for recoverable conditions and runtime exceptions for programming errors | The checked-vs-unchecked decision rule. |
| 71 | Avoid unnecessary use of checked exceptions | Overused checked exceptions burden callers; consider `Optional`/an unchecked exception. |
| 72 | Favor the use of standard exceptions | Reuse `IllegalArgumentException`, `IllegalStateException`, `NullPointerException`, `IndexOutOfBoundsException`, etc. |
| 73 | Throw exceptions appropriate to the abstraction | Translate low-level exceptions; don't leak implementation detail (exception translation / chaining). |
| 74 | Document all exceptions thrown by each method | `@throws` Javadoc for checked AND unchecked. |
| 75 | Include failure-capture information in detail messages | The message should carry the values that caused the failure. |
| 76 | Strive for failure atomicity | A failed call should leave the object in its pre-call state. |
| 77 | Don't ignore exceptions | An empty catch block defeats the purpose of exceptions. |

Item 71's recovery-preference ladder (verbatim sense): *if recovery may be possible, first consider
returning an `Optional`; only if that gives insufficient failure information should you throw a checked
exception.* This ties this chapter to key 11 (Null-safety & `Optional`).

### 2.3 Fail-fast (the design discipline)

Fail-fast = detect a contract violation as close to its cause as possible and throw immediately, rather
than letting a corrupted value propagate to a distant, confusing failure. In Java this means:
guard clauses validating preconditions at method entry (`Objects.requireNonNull`, range checks →
`IllegalArgumentException`/`IllegalStateException`, EJ Item 72), and letting JEP 358's detailed messages
pinpoint the null when one slips through. Defensive-validation depth (Jakarta Bean Validation, guard
clauses) is key 18; this chapter owns the *exception-shape* side of fail-fast.

### 2.4 Error models (the module-level view)

The "error model" is the contract a module presents: which exceptions it throws, checked vs unchecked,
and whether it exposes a *closed* set. Modern Java offers a typed alternative to throw-based modelling:

- **`sealed` interfaces + records** (JEP 409 sealed, JDK 17; records JEP 395, JDK 16) can model a closed
  set of outcomes as a value (`Result`/`Either`-style) that **pattern matching for `switch`** (JEP 441,
  finalized JDK 21) deconstructs exhaustively — the compiler rejects a `switch` missing a permitted case.
  (⚠ confirm JEP numbers/levels at pin — §7. Records/sealed are post-3e, so EJ 3e does not cover them;
  present as the modern modelling option, not as a Bloch recommendation.)
- **Structured concurrency** (`StructuredTaskScope`) "streamlines error handling and cancellation" for
  concurrent subtasks; it is **preview** at Java 21 (JEP 453) and still preview through later releases
  (JEP 462/480/499/505) — record as `⚠ AHEAD-OF-PIN`/preview, never as settled API (§7; depth → Part III).
- These are presented neutrally as *approaches* to error modelling alongside exceptions, with trade-offs
  (see §4), not as a winner over exceptions.

### 2.5 Reference units (rule IDs / flags / API) — table form

Tool versions and exact defaults are TO-PIN; rule IDs/keys are cited to each tool's own docs.

| Name | Type | Default / behaviour | Fixed early (build/PR-time)? | Source |
|---|---|---|---|---|
| `java.lang.Throwable.addSuppressed` / `getSuppressed` | JDK API | suppressed-exception list for try-w-resources | runtime | JLS §14.20.3.2 / Javadoc |
| `java.util.Objects.requireNonNull` | JDK API | throws NPE (fail-fast on null arg) | runtime | JDK Javadoc |
| `-XX:+ShowCodeDetailsInExceptionMessages` | JVM flag | enables JEP 358 detail; default-on ⚠ since JDK 15 | runtime | JEP 358 |
| `java:S112` / `squid:S00112` | Sonar rule | "Generic exceptions should never be thrown" (Throwable/Error/Exception/RuntimeException) | PR/CI | rules.sonarsource.com/java |
| `java:S1166` | Sonar rule | "Exception handlers should preserve the original exceptions" (log or rethrow with cause) | PR/CI | rules.sonarsource.com/java |
| `java:S2221` | Sonar rule | `"Exception"` should not be caught when not required by called methods | PR/CI | rules.sonarsource.com/java |
| `java:S1181` | Sonar rule | `Throwable` and `Error` should not be caught | PR/CI | rules.sonarsource.com/java (⚠ confirm ID at pin) |
| `java:S108` / `squid:S00108` | Sonar rule | empty nested blocks (incl. catch) should be removed | PR/CI | rules.sonarsource.com/java (⚠ confirm ID/scope at pin) |
| `EmptyCatchBlock` | PMD rule (errorprone) | flags catch that does nothing; props `allowCommentedBlocks`, `allowExceptionNameRegex` | PR/CI | pmd_rules_java_errorprone.html |
| `AvoidCatchingGenericException` | PMD rule (errorprone) | flags catching `NPE`/`Throwable`/`Exception`/`RuntimeException`/`Error`; configurable `typesThatShouldNotBeCaught` (PMD 7) | PR/CI | pmd_rules_java_errorprone.html |
| `AvoidThrowingRawExceptionTypes` | PMD rule (errorprone) | throw a subclass, not raw `RuntimeException`/`Throwable`/`Exception`/`Error` | PR/CI | pmd_rules_java_errorprone.html |
| `PreserveStackTrace` | PMD rule (bestpractices) | wrapping must preserve cause (`new X(e)`/`initCause`) | PR/CI | pmd_rules_java_bestpractices.html |
| `REC_CATCH_EXCEPTION` (detector REC) | SpotBugs pattern | catches `Exception` when `Exception` not thrown in block | PR/CI | spotbugs bugDescriptions |
| `DE_MIGHT_IGNORE` (detector DroppedException) | SpotBugs pattern | dropped/ignored exception | PR/CI | spotbugs bugDescriptions |
| `IllegalCatch` | Checkstyle check (coding) | rejects catching `Exception`/`Error`/`RuntimeException` (configurable `illegalClassNames`) | PR/CI | checkstyle illegalcatch.html |
| `IllegalThrows` | Checkstyle check (coding) | rejects declaring `throws Error`/`RuntimeException`/`Throwable` (`illegalClassNames`) | PR/CI | checkstyle illegalthrows.html |
| `DeadException` | Error Prone pattern | exception created but never thrown (e.g. forgot `throw`) | compile | errorprone.info/bugpatterns |
| `InterruptedException` | Error Prone pattern | `InterruptedException` caught via a supertype and not handled specially | compile | errorprone.info/bugpatterns |

> Neutral framing: these rule sets **overlap** (every analyzer flags broad-catch and swallowed exceptions)
> but differ in mechanism — Error Prone runs as a `javac` plugin at *compile* time; Checkstyle/PMD parse
> source; SpotBugs analyses bytecode; Sonar aggregates. Each fits a different point in the pipeline. Tool
> comparison depth is keys 27–30/35/37; here they appear only as enforcement of this chapter's idioms.

---

## 3. Evidence FOR

- **Compiler-enforced contract.** The checked-exception rule (JLS §11.2) is a *language-level* guarantee:
  certain recoverable failures cannot be silently ignored at compile time. Verified against JLS SE 21 §11.
- **Canon coverage.** Nine dedicated *Effective Java* 3e items (69–77) give a complete, named idiom set —
  every one maps to a verifiable rule, several to a static-analysis rule (Item 73→`PreserveStackTrace`/
  `java:S1166`; Item 77→`EmptyCatchBlock`/`DE_MIGHT_IGNORE`; Item 72→`AvoidThrowingRawExceptionTypes`/
  `java:S112`).
- **First-class tooling.** Every major analyzer ships exception rules (Sonar `java:S112/S1166/S2221`,
  PMD error-prone category, SpotBugs REC/DE detectors, Checkstyle `IllegalCatch`/`IllegalThrows`, Error
  Prone `DeadException`), i.e. the industry treats these idioms as mechanically checkable. Each verified
  to its tool's own docs.
- **Language investment continues.** JEP 358 (helpful NPEs) is a JDK-level improvement specifically aimed
  at making failures diagnosable — evidence the platform itself treats fail-fast diagnosis as a goal.
- **Modern modelling support.** Sealed types + records + pattern-matching `switch` (JDK 17/16/21) give a
  type-checked, exhaustiveness-verified way to model closed outcome sets — GA, not preview.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **Checked exceptions are contested.** *Effective Java* itself (Item 71) warns against *overusing* them:
  they burden every caller and compose poorly with streams/lambdas (functional interfaces in `java.util.function`
  don't declare checked exceptions, forcing wrap-in-unchecked). **When NOT to use checked:** APIs intended
  for lambda/stream use, or where the caller realistically cannot recover. Present the checked-vs-unchecked
  choice as a trade-off (Item 70's "when in doubt, unchecked"), not a verdict.
- **Tool-rule overlap and false positives.** Broad-catch rules fire on legitimate top-level handlers
  (a framework boundary that *must* catch `Exception`); `PreserveStackTrace` has documented false positives
  (builder pattern, `initCause` in a separate statement, pattern-matching `instanceof` — PMD issues
  #422/#844/#5318). Swallowing-detection cannot tell intentional ignores from bugs without a comment/annotation.
  **When NOT to enforce strictly:** at controlled boundary handlers — suppress with justification, don't
  delete the rule.
- **`sealed`/`Result`-type modelling has costs.** It moves error handling into the type system (every
  call site must destructure the result), adds verbosity, and interoperates awkwardly with exception-based
  libraries and frameworks (Spring, JDBC) that throw. **When NOT to use:** crossing a framework/IO boundary
  that already speaks exceptions; deep call chains where threading a `Result` is more noise than signal.
- **Structured concurrency is preview at the pin.** `StructuredTaskScope` is **not GA** at Java 21 (JEP 453,
  preview) — do not present its API as stable; mark `⚠ AHEAD-OF-PIN`/preview (§7).
- **JEP 358 has limits.** It names the null expression only when bytecode analysis can; reflective/generated
  frames and some optimized code give less detail. It is a diagnostic aid, not a substitute for fail-fast
  precondition checks.
- **`finally` pitfalls.** A `return`/`throw` inside `finally` swallows the in-flight exception (a classic
  bug try-with-resources' suppression mechanism was designed to avoid); SpotBugs/PMD flag related patterns.

## 5. Current status

- **Language mechanism (JLS §11, try-with-resources, multi-catch):** stable, unchanged in substance through
  Java 21 → 25.
- **JEP 358:** delivered JDK 14, **on by default since JDK 15** (⚠ confirm at pin) — stable at 21/25.
- **Effective Java 3e (2018):** the current edition; Items 69–77 stand. Records/sealed/pattern-matching as
  *error-model* tools are **post-3e** language features (note as such; don't attribute to Bloch).
- **Pattern matching for `switch`:** finalized **JDK 21** (JEP 441) — GA at the anchor. Record patterns
  finalized JDK 21 (JEP 440). (⚠ confirm JEP numbers at pin.)
- **Structured concurrency:** still **preview** across the 21→25 line (JEP 453/462/480/499/505) — not GA.
- **Tools:** all named rules are present in current stable lines; exact IDs/defaults are version-sensitive →
  pin and re-trace per SOURCE-PIN re-pin runbook.

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** add/point to key `12_error_handling_exceptions` row in `DEMO-CATALOG.md` —
  **demo name:** "Order-service error model"; **surface exercised:** checked-vs-unchecked decision, exception
  translation (Item 73), fail-fast precondition guards, try-with-resources suppression, and a sealed
  `Result` alternative; **TRY-IT:** "introduce a swallowed catch and a raw `throw new RuntimeException`,
  run the analyzers, watch `EmptyCatchBlock` / `java:S112` / `IllegalCatch` flag them; then refactor to a
  domain exception + preserved cause and watch the build go green."
- **Module key / path:** `08-companion-code/12_error_handling_exceptions/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited pin property, Java 21) | establishes the pin | SOURCE-PIN runtime baseline | ☐ |
  | JDK `java.lang`/`java.util.Objects` (no GAV) | primary unit under study (throwables, requireNonNull) | JLS §11 / Javadoc | ☐ |
  | `org.junit.jupiter:junit-jupiter` (TO-PIN) | test harness | junit.org/junit5 | ☐ |
  | `org.assertj:assertj-core` (TO-PIN) | `assertThatThrownBy` assertions on exceptions | assertj.github.io | ☐ |
  | analyzer plugins (PMD/SpotBugs/Error Prone/Checkstyle, TO-PIN) | demonstrate the rules firing | each tool's docs | ☐ |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property, Java 21, no loose versions.
  - **Externalized config / profiles** — analyzer rule config (e.g. Checkstyle `IllegalCatch`
    `illegalClassNames`, PMD ruleset) externalized; name each.
  - **At least one test** — asserts a domain checked exception is thrown for a recoverable condition AND
    that the original cause is preserved (`getCause()` non-null) — covers Item 73/`PreserveStackTrace`.
  - **Observability / health surface** — exception → structured-log mapping at the boundary handler (the
    one place a broad catch is justified, with a suppression comment).
  - **Explicit failure path** — a deliberate domain failure: invalid input → fail-fast
    `IllegalArgumentException` (programming-error path) AND a recoverable IO failure → checked domain
    exception with cause chained (translation path). Proves the checked/unchecked split in code.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `checked-vs-unchecked` | a guard clause throwing unchecked + a recoverable path throwing a checked domain exception | `OrderService.java` |
  | `exception-translation` | catching a low-level exception and rethrowing a domain one with the cause (Item 73) | `OrderRepository.java` |
  | `result-model` | sealed `Result` + records + exhaustive `switch` as the typed alternative | `Result.java` |
  | `boundary-handler` | the one justified broad catch, with suppression + structured log | `OrderController.java` |

- **Run command:** `./mvnw -B spring-boot:run` (or `java`-main equivalent; confirm at build).
- **Build/verify command:** `./mvnw -B verify` (runs analyzers + tests).
- **Expected output:** tests pass; analyzer run is clean on the refactored code; the deliberate-fault
  variant (TRY-IT) trips `EmptyCatchBlock`/`java:S112`/`IllegalCatch` with the exact rule IDs in the report.

- **Figure plan** (GUIDELINES §8):
  - **Chapter class:** code-craft chapter → **1–2 designed diagrams** + 0–1 captured tool screenshot.
  - **Candidate designed diagram(s) + family:**
    - **Fig 12.1 — The `Throwable` hierarchy + the decision rule** (family: hierarchy/decision tree):
      `Throwable`→`Error`/`Exception`→`RuntimeException`, annotated "checked = recoverable / unchecked =
      programming error / Error = JVM-only (don't throw, except AssertionError)." Trace to JLS §11.1.1 +
      EJ Item 70. HTML → cropped PNG via `05-figures/_assets/render.mjs` (never image-generated).
    - **Fig 12.2 — The error-handling pipeline gates** (family: flow): compile-time checked rule (JLS
      §11.2) → fail-fast guard → exception translation → boundary handler/log, with the analyzer rule that
      guards each step (`java:S112`, `PreserveStackTrace`, `EmptyCatchBlock`). Trace each label to its
      pinned source.
  - **Candidate captured surface(s):** an analyzer report (SonarLint/PMD/SpotBugs in IDE or CI) showing
    `java:S112` / `EmptyCatchBlock` firing on the deliberate-fault code (Java profile allows tool
    screenshots). Trace to the tool's own rule page.
  - **Source trace per depicted claim:** every box/label cites JLS SE 21 §11, JEP 358, EJ 3e Item N, or the
    named tool's rule page (paths under the per-tool fetch dirs once pinned).

## 7. Gap-filling (verification queue)

- ⚠ **JEP 358 default-on level** — confirm "default-on since JDK 15" against the JDK 15 release notes / JEP
  text at pin (some sources say 14-behind-flag, 15-default). Material to the fail-fast section → flag filed.
- ⚠ **Sonar rule IDs/defaults** — confirm `java:S112` vs legacy `squid:S00112`, and the IDs for "catch
  `Throwable`/`Error`" (`java:S1181`?) and "empty block" (`java:S108`/`S00108`?) against rules.sonarsource.com
  at the pinned analyzer version (IDs renamed across versions). → before stating IDs as fact.
- ⚠ **PMD 7 vs 6 rule moves** — `AvoidCatchingNPE`/`AvoidCatchingThrowable` deprecated/merged into
  `AvoidCatchingGenericException` (configurable `typesThatShouldNotBeCaught`); confirm exact state at the
  pinned PMD version.
- ⚠ **Checkstyle defaults** — `IllegalCatch`/`IllegalThrows` default `illegalClassNames` lists — confirm
  exact defaults at the pinned Checkstyle version.
- ⚠ **SpotBugs pattern IDs** — confirm `REC_CATCH_EXCEPTION`, `DE_MIGHT_IGNORE` exact spellings/category at
  the pinned SpotBugs version.
- ⚠ **JEP numbers** — sealed=409, records=395, pattern-matching-`switch`=441, record patterns=440; confirm
  against pinned JDK docs (carried from key 03/13).
- ⚠ **AHEAD-OF-PIN:** `StructuredTaskScope` is **preview** at 21 — do not present API as stable; depth → Part III.
- **Effective Java verbatim quotes** (Item 70 directives) — confirm wording + page before block-quoting
  beyond fair-use sense.

## 8. Sources & further reading

### Primary / Official (pinned authority set: specs, docs, source, canon)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JLS spec | JLS SE 21 §11 Exceptions; §14.20 try/catch/finally; §14.20.3 try-with-resources | https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html | ☑ (constructs; cross-checked) |
| 2 | JEP | JEP 358 — Helpful NullPointerExceptions | https://openjdk.org/jeps/358 | ☑ (mechanism, flag; ⚠ default level §7) |
| 3 | Book canon | *Effective Java* 3e (Bloch, 2018) Ch.10 Items 69–77 | print + TOC (informit/oreilly) | ☑ (item titles + Item 70 directives) |
| 4 | Sonar rules | `java:S112`, `java:S1166`, `java:S2221` (et al.) | https://rules.sonarsource.com/java/ | ☑ rule intent; ⚠ exact IDs/defaults at pin |
| 5 | PMD docs | Error-Prone rules (`EmptyCatchBlock`, `AvoidCatchingGenericException`, `AvoidThrowingRawExceptionTypes`); `PreserveStackTrace` (bestpractices) | https://pmd.github.io/pmd/pmd_rules_java_errorprone.html | ☑ rule intent; ⚠ version moves §7 |
| 6 | SpotBugs docs | Bug descriptions (REC / DroppedException detectors) | https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html | ☑ pattern intent; ⚠ exact IDs §7 |
| 7 | Checkstyle docs | `IllegalCatch`, `IllegalThrows` | https://checkstyle.sourceforge.io/checks/coding/illegalcatch.html | ☑ intent; ⚠ defaults §7 |
| 8 | Error Prone docs | Bug patterns (`DeadException`, `InterruptedException`) | https://errorprone.info/bugpatterns | ☑ pattern intent |
| 9 | JEP | JEP 441 pattern matching for switch; 409 sealed; 395 records | https://openjdk.org/jeps/441 | ⚠ confirm numbers/levels at pin |
| 10 | JEP | JEP 453 Structured Concurrency (preview) | https://openjdk.org/jeps/453 | ☑ (preview status) — AHEAD-OF-PIN |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Baeldung | Helpful NullPointerExceptions in Java | https://www.baeldung.com/java-14-nullpointerexception | corroboration |
| 2 | Baeldung | Catch Common Mistakes with Error Prone | https://www.baeldung.com/java-error-prone-library | corroboration |
| 3 | O'Reilly | *Effective Java* 3e Ch.10 (Exceptions) reader | https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/ch10.xhtml | corroboration |

> Source-quality order: JLS/JEP → *Effective Java* 3e (canon) → each tool's own pinned docs → quality
> secondary (corroboration only). No content-farm or AI text as a factual source.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | EJ 3e Ch.10 checked vs unchecked recover | web + fetch (clxering Item 70) | Item 70 three-kinds rule, AssertionError caveat, "when in doubt unchecked" (verbatim sense) |
| 2 | Sonar S00112/S1166/S2221 | web + sonar-java source | rule intents: generic-throw, preserve-original, catch-not-required |
| 3 | Error Prone exception patterns | web + errorprone.info | DeadException, InterruptedException, ThrowsUncheckedException |
| 4 | SpotBugs REC/DE/RCN | web + spotbugs docs | REC_CATCH_EXCEPTION, DE_MIGHT_IGNORE intents |
| 5 | JEP 358 helpful NPE | web + openjdk | mechanism, flag, message structure, JDK 14/15 |
| 6 | PMD errorprone rules | web + pmd docs/repo | EmptyCatchBlock, AvoidCatchingGenericException (PMD7 config), AvoidThrowingRawExceptionTypes, PreserveStackTrace + FP issues |
| 7 | Checkstyle IllegalCatch/IllegalThrows | web + checkstyle docs | rationale: catching Exception/Error/RuntimeException; illegalClassNames |
| 8 | EJ Ch.10 item list 69–77 | fetch (jkmcl gist) | verbatim item titles |
| 9 | sealed/records/structured concurrency error models | web + openjdk | pattern-match exhaustiveness; StructuredTaskScope preview (JEP 453+) |

---
## Learnings & pipeline suggestions
- **Item-to-rule crosswalk shape.** A reusable mini-structure for any "idiom canon + tool enforcement"
  chapter: a table mapping each named canon item (EJ Item N) → the static-analysis rule(s) that enforce it
  (`java:Sxxx` / PMD / SpotBugs / Checkstyle / Error Prone), each cited to its own pinned source. Reuse for
  keys 08, 10, 14, 15, 16, 18. Propose adding to `templates/`.
- **Preview-feature discipline confirmed.** `StructuredTaskScope` is preview across 21→25 — a recurring
  trap for error-handling/concurrency chapters. Keep marking preview APIs `⚠ AHEAD-OF-PIN`; do not let a
  drafter present them as stable. (Reinforces SOURCE-PIN moving-target policy.)
- **Cross-ref:** checked-vs-`Optional` ladder → key 11; defensive validation/guard clauses → key 18;
  try-with-resources/`AutoCloseable` → key 16; the analyzer rules in depth → keys 27–30/35/37; modern
  language constructs (sealed/records/pattern matching) → key 13; concurrency error handling → Part III.
- **Flag filed:** `09-flags/12_jep358_default_level_and_rule_ids.md` (JEP 358 default-on level + several
  version-sensitive Sonar/PMD/Checkstyle/SpotBugs rule IDs unconfirmed until tools are pinned).
