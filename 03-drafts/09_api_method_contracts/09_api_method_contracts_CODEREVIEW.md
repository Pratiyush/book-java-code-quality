# GATE REPORT — CODE-REVIEW — Chapter 09 (`api-method-contracts`)

## Header

- **Gate:** CODE-REVIEW (FLOOR C, second half)
- **Chapter key:** 09 (frozen key; folds 60)
- **Slug:** `09_api_method_contracts`
- **Module under review:** `08-companion-code/09_api_method_contracts/`
- **Draft for context:** `03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` agent
- **Build re-run:** `mvn -B -Pquality -pl 09_api_method_contracts -am clean verify` (JDK 21.0.11) — BUILD SUCCESS; 11/11 tests; 0 Checkstyle violations; 0 SpotBugs findings; warning-clean under `-Xlint:all`.
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

This is exemplary teaching code: a contract-tight module whose every displayed `// tag::` region is correct, idiomatic Java 21, within the snippet ceiling, and matched to what the prose claims. It builds green and warning-clean, carries no secrets, leaks no representation, and the SpotBugs `EI_EXPOSE_REP` family stays quiet by design with an empty (documented) suppression filter — the chapter's point made by the build. No BLOCKER, no security finding, no neutrality finding, no invented/unpinned fact.

Two MINOR fixes are required before approval, both about *documentation fidelity of a displayed exemplar*: (1) the `javadoc-contract` tag region — the chapter's showcase of "document the part types cannot carry (Item 56)" — omits `@throws NullPointerException` for its non-null parameter while the sibling constructor and `transfer` methods both document it, so the displayed best-practice exemplar is internally inconsistent with the module's own convention; (2) `InMemoryAccountRepository`'s class Javadoc advertises safe multi-threaded use, but `transfer` does a non-atomic read-check-write that loses updates under contention — the comment promises more than the code delivers. Neither blocks FLOOR C; both should be fixed so readers copy an unambiguously correct exemplar.

---

## Six-dimension scorecard

| # | Dimension | Result | Note |
|---|---|---|---|
| 1 | Correctness | **PASS** (1 MINOR) | Logic, edge cases, fail-fast ordering all correct; tests assert real behavior, not vacuous. One MINOR: comment overpromises thread-safety the `transfer` flow does not provide (F-2). |
| 2 | Idiomatic modern Java (21 LTS) | **PASS** | Records for value/result types; compact canonical constructors validate; `final` classes; `System.Logger` (no stdout/`println`); `@Serial` on `serialVersionUID`; `@Override` on adapter methods; `Objects.requireNonNull`/`checkIndex`; sealed-ness not needed. No raw threads, no anti-patterns. |
| 3 | Security | **PASS** | No hardcoded secrets/tokens/passwords (scanned). No injection sink (no SQL/reflection/exec). `TransferRejectedException` carries a stable code + a controlled message; no stack-trace/internal leak. Inputs validated fail-fast at every public entry. |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point; no dead code; one pinned dependency literal (JSpecify), justified inline; realistic finance domain names (no Foo/Bar/tmp); every public type carries a one-line purpose Javadoc. |
| 5 | Prose↔code fidelity | **PASS** (1 MINOR) | All 5 `<!-- include: -->` directives resolve to real tags; all 5 regions <=9 lines; Item numbers, `Objects` signatures, JSpecify GAV `1.0.0`, SpotBugs IDs all trace to SOURCE-PIN. One MINOR is a fidelity-of-exemplar nit inside a displayed region (F-1). |
| 6 | Neutrality in code | **PASS** | No comment/identifier/log/test name crowns or disparages any tool. Comments name tools (SpotBugs, PMD, Sonar, Checkstyle) descriptively and neutrally; zero banned phrasings. |

---

## Build / lint validation (re-run)

| Check | Result |
|---|---|
| `mvn -B -Pquality ... clean verify` | **BUILD SUCCESS** |
| Tests | **11 run, 0 failures, 0 errors, 0 skipped** (matches the draft's "11 tests" claim) |
| Checkstyle (engine 10.26.1 via plugin 3.6.0) | **0 violations** |
| SpotBugs (4.9.3.0, effort=Max, threshold=Medium) | **BugInstance size is 0** — empty exclude filter, no suppressions |
| Compiler warnings (`-Xlint:all,-processing`) | **none** (warning-clean) |
| Hardcoded-secret grep (password/secret/token/apikey/key/credential) | **0 hits** |
| Tag regions within <=9-line ceiling | **all 5 pass** (precondition-guards 7, javadoc-contract 8, defensive-copy 8, optional-return 7, nullness-marked 4) |
| `// include:` directives resolve to real tags | **5/5 resolve** |
| Failure-path test present | **yes** (null/range/negative/insufficient-funds/unknown-account; defensive-copy isolation) |

---

## Findings

Severity scale: BLOCKER (gate cannot pass) · MAJOR (must fix before approval) · MINOR (should fix) · NOTE (no action).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| F-1 | The `javadoc-contract` displayed exemplar omits `@throws NullPointerException`. `availableBalance` delegates to `require(id)` -> `repository.findById(id)` -> `Objects.requireNonNull(id, "id")`, so a null id throws NPE — but the tag region documents only `@throws TransferRejectedException`. The constructor (line 35) and `transfer` (line 49) DO document `@throws NullPointerException`, so the module is internally inconsistent, and this gap sits inside the *one region the chapter prints to teach "document everything the type can't carry" (Item 56)*. `@NullMarked` makes a null arg a type violation, which is why the prior EXAMPLE gate logged this as acceptable — but for the displayed exemplar, the JDK/Item-56 convention is to still document the NPE. | MINOR | `MoneyTransferService.java:79-91` (tag `javadoc-contract`, lines 80-88; method 89-91) | Add `@throws NullPointerException if {@code accountId} is {@code null}` to the `javadoc-contract` region so the printed exemplar matches the module's own constructor/`transfer` convention. Keep it inside the tag so the book shows it. |
| F-2 | Comment overpromises concurrency the code does not deliver. `InMemoryAccountRepository`'s class Javadoc states it is "backed by a `ConcurrentHashMap` so the service can be exercised from several threads at once," but `transfer` (lines 66-76) performs a non-atomic read (`require(fromId)`/`require(toId)`) -> balance check -> two independent `repository.save(...)` calls. Concurrent transfers touching the same account can lose updates or overdraw despite the concurrent map (classic check-then-act). The prose does NOT claim the transfer is atomic (so this is not a prose↔code FAIL), and concurrency is out of this chapter's scope (Chapters 20/22) — but a reader copying this may infer transfer is contention-safe because the adapter advertises multi-threaded use. | MINOR | `InMemoryAccountRepository.java:10-12` (claim) vs `MoneyTransferService.java:66-76` (non-atomic R-M-W) | Either (a) soften the adapter Javadoc to "the map's per-key operations are thread-safe; composing a transfer atomically is out of scope (Ch 20)," or (b) add a one-line `@implNote`/comment on `transfer` stating it is not safe under concurrent same-account mutation. (a) is the smaller, clearer fix. Do not add locking — that would pull concurrency into a method-contracts chapter. |
| F-3 | `availableBalance` (a public method exercised by a test) is not surfaced in the README's "What it demonstrates" table, and `backoffFor`/`isReady`/`rejectedByContractCount` are observability/illustrative surfaces only partially mapped there. Minor doc-completeness; not load-bearing. | NOTE | `README.md:10-19` | Optional: add an `availableBalance` row, or leave as-is — the table targets the contract surfaces, which is a reasonable scope. |
| F-4 | No `src/main/resources` config file with `%dev`/`%prod` profiles (EXAMPLES-GUIDE req. 2). The prior EXAMPLE-BUILD gate dispositioned this by treating the externalized Checkstyle/SpotBugs rulesets + the `-Pquality` profile as the externalized config for a deliberately JDK-only library module (`_EXAMPLE.md` line 80). Recorded here for completeness; this is the prior gate's scoping call, consistent with §1.2 "scoped-out reason recorded," and not a CODE-REVIEW finding to overturn. | NOTE | module root (no `src/main/resources`) | None for CODE-REVIEW. Carried as context for the human gate. |

---

## Blockers

**None.** No BLOCKER-severity finding. FLOOR C is not blocked by this review (build green, source-trace clean, zero security/neutrality/invention findings).

- [x] No security finding
- [x] No neutrality-in-code finding
- [x] No invented / unpinned-fact finding
- [x] Build green + warning-clean

---

## What is exemplary (worth keeping verbatim)

- **The two-place enforcement is real, not narrated.** Fail-fast guards reject at runtime AND the `-Pquality` gate (Checkstyle + SpotBugs) rejects the same mistakes at build time — and the SpotBugs exclude filter is *empty with a comment explaining why*, so the representation-exposure detectors stay quiet by defending the representation (Item 50), not by suppression. This is the chapter's thesis demonstrated by the build itself.
- **`TransferBatch` defensive copy-in/copy-out** (tag `defensive-copy`, 8 lines) is a textbook Item 50: copy in the constructor, copy in the getter, plus per-element null validation. The test `defensiveCopyIsolatesTheBatchFromCallerMutation` mutates *both* the caller's input list and the returned list and asserts the batch is unchanged — it exercises both directions, not a vacuous one.
- **`Optional<Account> findById`** (tag `optional-return`) puts absence in the signature exactly as the hook promises; `InMemoryAccountRepository` returns `Optional.ofNullable(...)` (empty, never null), and `require(...)` consumes it with `orElseThrow` into a typed error — the full Item 54/55 arc in three short methods.
- **Records as value/result types** (`Money`, `Account`, `TransferReceipt`, `TransferInstruction`) with compact canonical constructors that validate, so an invalid value can never exist; `Money` co-locates currency with amount so cross-currency addition is a type-carried contract, not a comment.
- **Test quality is genuinely high.** Each public behavior has a precondition/contract assertion: NPE-with-message (`withMessageContaining("fromId")`), `IndexOutOfBoundsException` from `checkIndex`, the negative-amount counter side-effect, both typed-error reason codes asserted on `ex.code()`, the empty-Optional contract, and the `@Nullable` opt-out (`backoffFor(99)` is null). The failure path is the real failure path, not a stub.
- **`System.Logger`** instead of `System.out`, `@Serial` on the exception's `serialVersionUID`, `final` on all concrete classes, JSpecify `@NullMarked` package with a single explicit `@Nullable` opt-out that the prose calls out — all idiomatic Java 21 a reader can paste with confidence.
- **Originality (LEGAL-IP §5):** the module is original-for-this-book — a finance/money-transfer domain authored for the chapter, not a reskinned upstream quickstart. No verbatim-lift concern; no in-file attribution required.

---

## FLOOR-C disposition

**CODE-REVIEW: PASS-WITH-FIXES** — FLOOR C is NOT blocked (green build, source-trace clean, zero security/neutrality/invention findings); apply the two MINOR documentation fixes (F-1 add `@throws NullPointerException` to the displayed `javadoc-contract` region; F-2 reconcile the adapter's thread-safety comment with `transfer`'s non-atomic flow), then this module ships.

---

## Learnings & pipeline suggestions

- **Add a "displayed-region exemplar" sub-check to CODE-REVIEW.** F-1 is a class of finding worth naming: a method whose Javadoc is *printed in the book to teach documentation* must itself be a flawless exemplar (here, document the NPE the `@NullMarked` contract implies). A region can be correct-for-the-build yet a weaker teaching exemplar than the module's own neighbours. Suggest a one-line rule in the CODE-REVIEW rubric: "tag regions that demonstrate a best practice are held to that best practice, not merely to compilation."
- **Comment-vs-behavior concurrency claims need a scan.** F-2 (a `ConcurrentHashMap`-advertising comment over a non-atomic read-modify-write) is invisible to Checkstyle/SpotBugs and to a prose audit. Consider a CODE-REVIEW checklist item: "any comment asserting thread-safety/atomicity is matched against the actual access pattern." Cheap to check, easy to miss.
- **Config-profile requirement (req. 2) is awkward for JDK-only library modules.** Two gates now (EXAMPLE-BUILD here, and the pattern will recur) have had to read "externalized rulesets + `-Pquality`" as the req-2 externalized config. Worth a note in EXAMPLES-GUIDE §1.2 that for a zero-runtime-dependency library module, externalized analyzer config legitimately satisfies req. 2 — so each gate doesn't re-litigate it.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement HARD RULE.
