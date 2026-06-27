# CODE-REVIEW Gate Report — Chapter 12 (When Things Go Wrong)

> FLOOR-C, second half (CODE-REVIEW). Senior-PR review of the companion module readers paste into
> their own apps. The module already builds green; this gate is the judgment a compiler and a passing
> test cannot make. Code was NOT edited — fixes (none blocking) are reported for the example-builder.

## Header

- **Gate:** CODE-REVIEW (technical profile)
- **Chapter key:** 12 (owner; folds 16, 18) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `12_error_handling_exceptions`
- **Module under review:** `08-companion-code/12_error_handling_exceptions/`
- **Draft for context:** `03-drafts/12_error_handling_exceptions/12_error_handling_exceptions_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Toolchain:** openjdk@21 21.0.11 (matches SOURCE-PIN anchor JDK 21 / parent-pom pin exactly); system `mvn` (no `mvnw` wrapper present in this repo — the standalone `mvn -B -Pquality -f pom.xml clean verify` per the module README was used)
- **Build result:** GREEN — `BUILD SUCCESS`; 12 tests pass (0 fail/err/skip), 0 Checkstyle violations, 0 SpotBugs findings, 0 compiler/`-Xlint:all` warnings, 0 test-compile deprecation warnings.
- **Verdict:** **PASS**

---

## Verdict rationale

Exemplary, idiomatic, modern Java-21 error-handling code with no blocking, security, neutrality, or
invention finding. Every one of the six review dimensions PASSes. The subtle traps a compiler cannot
catch — the `Cleaner` action that must not capture `this`, suppressed-not-masked semantics, cause-chained
exception translation, idempotent `close()`, defensive copies, `@Serial`/`serialVersionUID` on checked
exceptions — are all handled correctly. All nine displayed tag regions are balanced, ≤9 lines, and
exemplary. The single recorded item is a MINOR prose-fidelity nit that lives in the **draft prose's
hand-written sketch**, not in the deliverable code, and is referred to the drafter/clarity gate.

---

## Six review dimensions

| # | Dimension | Result | Note |
|---|---|---|---|
| 1 | Correctness | **PASS** | Cause-chained translation; reverse-order close asserted; suppressed-not-masked asserted; `Cleaner` action holds no back-reference; idempotent `close()`; `released` guard prevents double-decrement; exhaustive sealed `switch`; failure-path tests are non-vacuous. |
| 2 | Idiomatic modern Java 21 | **PASS** | `sealed`+`record`+pattern `switch` (JEP 409/395/441); compact-constructor guards; `record` value types; `Map.copyOf`/`List.copyOf`; `System.Logger` facade (no ad-hoc stdout); `@Serial`; `Objects.requireNonNull`/`checkIndex`-family. No raw threads, no blocking misuse. |
| 3 | Security | **PASS** | No hardcoded secret/password/token/key (grep clean). `null`/range validated at every public entry and compact constructor. No injection sink (no SQL/EL/reflection on untrusted input; `provided`-scope annotations only). Boundary handler returns generic bodies (`"internal error"`) and logs the cause server-side — no stack trace leaked to the response. |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point; no dead code; no unused deps; `@Positive` imported AND used; realistic domain names (`OrderService`, `Money`, `OrderRepository`), no `Foo`/`tmp`/placeholder packages; every public type carries a one-line purpose Javadoc. |
| 5 | Prose-code fidelity | **PASS** | All 9 draft `include:` anchors resolve to balanced tag regions; method names, status codes (200/404/503/500), GAV (`jakarta.validation-api:3.1.1`), JEP/Item IDs, and the "12 tests" claim all trace and match. Originality: original-for-this-book, no upstream-sample lift; no attribution comment owed. |
| 6 | Neutrality in code | **PASS** | No banned phrasing (`better than`/`unlike X`/`superior`/`beats`/`the problem with X`/…) anywhere in src, pom, README, or config. |

---

## Build-validation checks (run)

- [x] `mvn -B -Pquality clean verify` on JDK 21.0.11 → **BUILD SUCCESS**, 12/12 tests, 0 Checkstyle, 0 SpotBugs. (The `StoreAccessException` stack trace in the log is the test's deliberately-triggered, logged failure path — not a test failure.)
- [x] **Warning-clean** — parent pom enables `-Xlint:all,-processing`; full-log scan shows zero compiler/lint/deprecation warnings. (The one `WARNING` line in the log is the boundary handler's own `LOG.log(Level.WARNING, …)` console output — the code doing exactly what the prose promises.)
- [x] **Failure-path integration test present** — `recoverableFailureIsACheckedExceptionWithCausePreserved` (store unavailable → translated checked exception, `getCause()` asserted to be the original `StoreAccessException`), `closeExceptionIsSuppressedNotMasked`, and the 503 branch of `boundaryMapsEveryOutcomeToADefinedResponse` all exercise real failure paths and assert behaviour, not just "did not throw."
- [x] **Hardcoded-secret grep** (`password|secret|token|apikey|credential|private key|bearer|aws_secret`) over `src/` → **NO MATCHES**.

---

## Displayed tag-region audit (BLOCKER class: duplicate/imbalanced end-tag)

Every region opens exactly once and ends exactly once (`open=1 end=1`), and every draft `include:`
directive maps to a real defined tag. **No duplicate or imbalanced end-tag. No BLOCKER.**

| Anchor | File | Lines (≤9) | Balanced | Exemplary |
|---|---|---|---|---|
| `checked-vs-unchecked` | `OrderService.java` | 8 | yes | Both kinds in one method: unchecked fail-fast guard + declared checked recoverable path, each commented. |
| `exception-translation` | `OrderRepository.java` | 6 | yes | try/catch translates `StoreAccessException` → `OrderUnavailableException`, cause chained (Item 73). |
| `result-model` | `Result.java` | 7 | yes | `sealed interface` + two `record` cases — the typed alternative, minimal. |
| `boundary-handler` | `OrderBoundary.java` | 7 | yes | Narrow `catch` first, then the one justified `catch (Exception)` backstop that logs + maps. |
| `twr-basic` | `ReceiptWriter.java` | 5 | yes | Two resources, reverse-order close comment on the closing brace. |
| `suppressed` | `ReceiptWriter.java` | 4 | yes | Body throws E1, `close()` throws E2 — comments mark E1/E2; tight. |
| `cleaner-backstop` | `NativeCounter.java` | 6 | yes | `CLEANER.register(this, state)` with the "captures State, not `this`" comment — the load-bearing point. |
| `guard-clause` | `Money.java` | 6 | yes | Compact constructor: `requireNonNull` + range check before assignment. |
| `constraints` | `OrderRequest.java` | 6 | yes | `@NotNull`/`@NotEmpty`/`@Valid` on a record + nested `Line` with `@Positive`. |

---

## Findings

Severity scale: BLOCKER · MAJOR · MINOR · NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Draft prose's inline (non-tagged) `Money` sketch shows the guard message as `"minorUnits < 0: " + minorUnits`, but the deliverable code emits `"minorUnits must not be negative: " + minorUnits`. The displayed **tag region** (`guard-clause`, included separately) is the real code and is correct — this divergence is only in the hand-written illustrative block in the prose, not in the companion module. | MINOR | `03-drafts/12_error_handling_exceptions/12_error_handling_exceptions_v1.md:152` (prose sketch) vs `08-companion-code/.../Money.java:30` (code) | Drafter/CLARITY: align the inline sketch's literal with the real message (or replace the sketch with the included tag). No code change. Out of CODE-REVIEW scope; recorded for cross-gate. |
| 2 | No `mvnw` wrapper in `08-companion-code/`; the template/spec assumes `./mvnw -B verify`. Build was run with system `mvn` + openjdk@21 (pin-exact), per the module README's documented invocation. | NOTE | `08-companion-code/` (no `mvnw`) | None for this chapter. Pipeline-level: either add a committed wrapper or amend the spec to the repo's system-`mvn` reality (see Learnings). |
| 3 | Empty SpotBugs exclude filter is intentional and documented (zero reviewed suppressions — the chapter's point made by the build). | NOTE | `config/spotbugs/spotbugs-exclude.xml` | None — correct and well-justified. |
| 4 | Jakarta Validation: module declares constraints against the pinned API and the test asserts the annotation **metadata is present** (via the canonical constructor's parameters) rather than wiring an off-pin `Validator`. Honest, documented Floor-C boundary (no engine pinned in SOURCE-PIN). | NOTE | `OrderRequest.java`, `OrderErrorModelTest.java:162`, `pom.xml:40` | None — the right call given the pin; the assertion is real (reflects `@NotNull`/`@NotEmpty`/`@Valid` on the canonical ctor). |

---

## Blockers

**None.** No duplicate/imbalanced end-tag, no security finding, no neutrality finding, no invented fact.

---

## Exemplary notes (what this module gets right that is worth keeping)

- **The `Cleaner` trap, handled correctly.** `State` is a `static` nested class capturing only an
  `AtomicInteger`; `CLEANER.register(this, state)` passes `this` as the referent and the separate
  `state` as the action, so the action has **no** back-reference to the `NativeCounter`. A non-static
  state or a `this`-capturing lambda would silently pin the object and defeat cleaning. This is the
  exact thing a compiler and a green test cannot verify, and the code (and its comment) get it right.
- **Suppressed-not-masked, observably.** `writeWithFailingBodyAndClose` returns `getSuppressed()`, and
  the test asserts the body's `IllegalStateException` propagates with the close failure attached —
  demonstrating Item 9's masking fix rather than asserting it.
- **Cause-chained translation** with `getCause()` asserted in the test; `@Serial serialVersionUID` on
  both checked exceptions (serialization hygiene most examples omit).
- **One justified broad catch, and only one** — narrow `OrderUnavailableException` first, then the
  logged `catch (Exception)` backstop; `REC_CATCH_EXCEPTION` stays quiet because a checked exception
  genuinely reaches it. Boundary leaks no internals to the response body.
- **`System.Logger`** (the JDK facade) instead of stdout — zero-dependency, idiomatic, dogfoods the book.
- **Defensive copies** (`Map.copyOf`/`List.copyOf`) and **compact-constructor guards** throughout.

---

## FLOOR-C disposition

**FLOOR C (second half — CODE-REVIEW): PASS.** Combined with the GREEN EXAMPLE-BUILD (source-trace +
compile, first half), FLOOR C is satisfied for Chapter 12. Nothing in this gate blocks the floor: no
FAIL, no security/neutrality/invention finding, no tag BLOCKER. Finding #1 is a MINOR prose-sketch nit
for the drafter/CLARITY gate and does not gate the code module or FLOOR C.

---

## Learnings & pipeline suggestions

- **Wrapper vs spec drift.** The CODE-REVIEW spec and GATE-REPORT template hardcode `./mvnw -B verify`,
  but `08-companion-code/` ships no Maven wrapper and relies on system `mvn` + a pin-exact JDK. Either
  commit a `mvnw` to the aggregator or update the technical-profile spec/template to the repo's actual
  `mvn -B -Pquality -f pom.xml verify` invocation, so the gate instruction matches reality.
- **Inline prose sketches drift from the deliverable.** Finding #1 is the second time a hand-written
  illustrative block in the prose can diverge from the included tag's real literal. Suggest a CLARITY/
  RECONCILE lint: any inline ```java fence that paraphrases a tagged region should either be replaced by
  the `include:` or be string-checked against the region it mirrors.
- **`Cleaner` back-reference is a reusable CODE-REVIEW checklist item.** "Does the cleaning action (or any
  lambda/inner class registered for GC-time cleanup) capture `this`?" is a high-value, compiler-invisible
  check worth promoting into the code-reviewer's standing checklist for any chapter touching `Cleaner`/
  `PhantomReference`/finalization.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 12 gate-run PASS
```
