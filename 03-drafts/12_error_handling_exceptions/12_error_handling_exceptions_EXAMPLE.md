# GATE REPORT — EXAMPLE-BUILD — Chapter 12

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 12 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 16 + 18)
- **Slug:** `12_error_handling_exceptions`
- **Draft under review:** `03-drafts/12_error_handling_exceptions/12_error_handling_exceptions_v1.md`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `extract_snippet.sh` / `check_snippets.sh` + Maven (`-Pquality`)
- **Scripts run:** `extract_snippet.sh` (×9), `check_snippets.sh`, `mvn -B -Pquality clean verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green warning-clean under `mvn -B -Pquality clean verify` on Java 21.0.11 (both FLOOR C
preconditions met), all nine declared snippet tags resolve to bounded (≤9-line) regions inside the compiled
files, every fact traces to `SOURCE-PIN.md`, and the module is self-contained exactly like the reference
module 09 (own `config/` dir, own `quality` profile, child of the one aggregator). No invented atom; the one
Floor-C boundary (a Jakarta Validation engine is not pinned) is documented and handled without inventing a
GAV. The module is NOT yet registered in the parent `<modules>` list (correct — that waits for CODE-REVIEW).

---

## Module

- **Path:** `08-companion-code/12_error_handling_exceptions/`
- **Artifact:** `org.acme.storefront:error-handling-exceptions` (inherits parent
  `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM).
- **Package:** `org.acme.orders`
- **Files (13 source + 2 config + pom + README):** `package-info`, `Money`, `OrderProblem`, `Result`,
  `OrderUnavailableException`, `StoreAccessException`, `OrderRepository`, `OrderService`, `OrderRequest`,
  `ReceiptWriter`, `NativeCounter`, `OrderBoundary`; test `OrderErrorModelTest`;
  `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml`.
- **Parent `08-companion-code/pom.xml`:** UNCHANGED (not edited; module not yet listed in `<modules>`).

---

## FLOOR C guard — both preconditions logged

| Precondition | Evidence | Result |
|---|---|---|
| (a) runtime ≥ Java 21 | `openjdk version "21.0.11" 2026-04-21` (`java -version`) | PASS |
| (b) build green | see exact command + result line below | PASS |

- **Exact build command:**
  `mvn -B -Pquality -f 08-companion-code/12_error_handling_exceptions/pom.xml clean verify`
  (run with `JAVA_HOME=/opt/homebrew/opt/openjdk@21/...`, Maven 3.9.16)
- **Result line:** `BUILD SUCCESS`
  - `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0`
  - `You have 0 Checkstyle violations.`
  - SpotBugs: `BugInstance size is 0` / `Error size is 0` / `No errors/warnings found`
  - compiler: zero warnings (`-Xlint:all,-processing` inherited from parent)

---

## Snippet tags — tag-include name(s) + resolved line count

All nine declared in the draft's "Snippet tags:" line; each resolves via `extract_snippet.sh` (≤9-line cap):

| Tag | File | Lines | Draft insertion point |
|---|---|---|---|
| `checked-vs-unchecked` | `OrderService.java` | 8 | "The exception model" — after Item 70 directive |
| `exception-translation` | `OrderRepository.java` | 6 | "The nine exception items" — after the Item 73 hook paragraph |
| `result-model` | `Result.java` | 7 | "Modern error models: the typed alternative" |
| `boundary-handler` | `OrderBoundary.java` | 7 | "When to use what" — after the "Catching" bullet |
| `twr-basic` | `ReceiptWriter.java` | 5 | "Resource management" — after the try-with-resources fence |
| `suppressed` | `ReceiptWriter.java` | 4 | "Resource management" — after the suppressed-exception paragraph |
| `cleaner-backstop` | `NativeCounter.java` | 6 | "Resource management" — after the `Cleaner` paragraph |
| `guard-clause` | `Money.java` | 6 | "Defensive coding" — after the `Money` example |
| `constraints` | `OrderRequest.java` | 6 | "Defensive coding" — after the Jakarta Validation paragraph |

- **`check_snippets.sh` over the draft:** `9 marker(s); 9 pass, 0 fail.`
- **Prose impact:** 36 insertions, 0 deletions (no prose removed; each marker has a one-line third-person
  lead-in; locked voice preserved).

---

## Enterprise-grade checklist

- [x] **Pinned dependency set via one inherited parent.** Runtime (Java 21), JUnit (6.0.3 BOM), AssertJ
  (3.27.7) inherited from the aggregator. The one literal GAV in the module —
  `jakarta.validation:jakarta.validation-api:3.1.1` (`provided`) — is the pinned row in `SOURCE-PIN.md §1`.
  No own BOM, no own version-set.
- [x] **Externalized config / profiles.** Static-analysis rules externalized to `config/checkstyle/`
  (LineLength 120, naming, import hygiene, `EmptyBlock`, etc.) and `config/spotbugs/spotbugs-exclude.xml`
  (empty — zero suppressions needed). The `quality` profile is opt-in (`-Pquality`) so the default build
  stays fast. (No runtime `%dev`/`%prod` profiles: this is a JDK-only library module, not a deployable
  app — the analyzer ruleset is the externalized config the chapter's subject calls for.)
- [x] **Integration test + harness.** `OrderErrorModelTest` (12 tests, JUnit Jupiter + AssertJ) exercises
  the chapter's mechanism: checked/unchecked split, cause-preserving translation, exhaustive `Result`
  fold, reverse-order close, suppressed-not-masked close, `Cleaner` release-once, fail-fast guards,
  constraint-metadata presence, boundary-handler mapping. Harness needs no extra parent config beyond the
  inherited surefire pin; the run is deterministic.
- [x] **Observability / health surface.** `OrderService.rejectedByGuardCount()` (running guard-rejection
  count), `OrderRepository.isAvailable()` (store health), and the boundary handler logging every
  recoverable/unexpected failure with its cause via `System.Logger`.
- [x] **Explicit failure path.** Both kinds, in code: fail-fast unchecked guard (programming error) and a
  declared checked `OrderUnavailableException` with the cause chained (recoverable); plus suppressed-not-
  masked close, and a boundary handler that maps every outcome to a defined `Response` (200/404/503/500).

---

## Captured screenshots (Step 4c)

**No captures planned.** This chapter's figure plan (dossier §6 / draft "How it works") is two designed
diagrams — `fig12_1` (the `Throwable` hierarchy + decision rule) and `fig12_2` (try-with-resources
suppressed vs masked) — both already authored as HTML→PNG under `05-figures/12_error_handling_exceptions/`
(not this gate's job). The plan's only capture candidate was an *optional* "0–1" analyzer report; it was
not committed at draft time, this is a JDK-only library module with no subject-native running UI (no dev
console / health endpoint / web surface) to capture live, and the peer code-craft modules (09, 10, 11) ship
zero captured-screenshot sidecars — the established precedent. Capturing an analyzer-firing screenshot would
require shipping deliberate-fault code purely for the image, which is out of scope here. Recorded as no
captures planned, consistent with peers.

---

## LEGAL-IP §5 — original-for-this-book confirmation

File-by-file, every `.java` file is original work written for this book — none is a copied/renamed upstream
sample, getting-started skeleton, or `NOTICE`/header boilerplate. Confirmed `Money.java` differs from the
Chapter 09 module's `Money.java` (different component order, focus, and Javadoc). No region is taken
substantially verbatim from any source file, so no per-region attribution is owed. The one deliberately
copied file is `config/checkstyle/checkstyle.xml` — the book's own shared *house ruleset* reused across
companion modules by design (as the Chapter 09 pom documents), not an upstream sample; `spotbugs-exclude.xml`
was rewritten for this chapter's context.

---

## Source trace per fact

Every never-invent atom traces to `SOURCE-PIN.md` or the language/canon it pins:

| Fact in the module | Source (pin) |
|---|---|
| Checked-vs-unchecked rule; "when in doubt, unchecked"; Items 8/9/49/70/72/73/77 | *Effective Java* 3e (SOURCE-PIN §7) |
| `Throwable` hierarchy; checked catch-or-specify; try-with-resources reverse-order close + suppressed (`addSuppressed`/`getSuppressed`) | JLS SE 21 §11, §14.20.3 (SOURCE-PIN §1) |
| `Objects.requireNonNull`; `java.lang.ref.Cleaner` / `Cleaner.Cleanable`; `AutoCloseable` | JDK 21 API (SOURCE-PIN §1, runtime baseline) |
| sealed (JEP 409) + records (JEP 395) + pattern `switch` (JEP 441), GA at 21 | JEPs at JDK 21 (SOURCE-PIN §1) |
| `@NotNull` / `@NotEmpty` / `@Valid` / `@Positive`; `jakarta.validation-api:3.1.1` | Jakarta Validation 3.1 (SOURCE-PIN §1) |
| JUnit Jupiter / AssertJ test APIs | JUnit 6.0.3 / AssertJ 3.27.7 (SOURCE-PIN §3) |
| Checkstyle engine 10.26.1; SpotBugs maven-plugin 4.9.3.0 | mirrors the pinned Chapter 09 `quality` profile (SOURCE-PIN §2) |

`⚠ Floor-C boundary (documented, not invented):` the draft prose names "Hibernate Validator 9.1.0.Final" as
the RI, but **no constraint-engine row exists in `SOURCE-PIN.md`** (the dossier itself marks the impl GAV
`⚠ @pin`). The module therefore declares the constraints against the pinned *API only* (`provided`) and the
test asserts the constraint metadata is present on the canonical constructor's parameters — where a
`Validator` reads it — rather than wiring an off-pin engine to evaluate them at runtime. This keeps the
`constraints` snippet true to the draft (the declared annotations) with zero invented GAV. No `09-flags/`
entry is newly required for the build (the existing `09-flags/12_jep358_default_level_and_rule_ids.md`
already tracks the validation-impl GAV as a pre-pin item); see "Flags" below.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Constraint engine (RI + Jakarta EL) not in `SOURCE-PIN.md`; constraints declared against API only, metadata asserted by reflection rather than evaluated by a live `Validator`. | NOTE | `OrderRequest.java` `constraints` tag; `OrderErrorModelTest#requestComponentsCarryTheDeclaredConstraintAnnotations` | If a validation-impl row is added to `SOURCE-PIN.md`, wire a `Validator` and assert `ConstraintViolation`s; until then, the API-only approach is the Floor-C-correct choice. |
| 2 | One intended log line (`WARNING: order store unavailable` + chained cause) prints during the boundary test — the chapter's "log the cause, never swallow" behavior, not harness noise. | NOTE | `OrderBoundary.handleReadTotal`; `OrderErrorModelTest#boundaryMapsEveryOutcomeToADefinedResponse` | None — intended, deterministic, single event; quieting it would need a parent-pom surefire system property (out of scope: do-not-edit parent). |

---

## Blockers

**None.**

---

## Gate-specific checks

- [x] **EXAMPLE** — companion artifact builds green via `mvn -B -Pquality verify` at the pin; all 9 displayed
  snippets resolve to real bounded tag regions in the compiled files; FLOOR C source-trace clean.
- [ ] **CODE-REVIEW** — not this gate (Step 4b `code-reviewer` runs next; module joins `<modules>` only
  after it PASSes).

---

## Learnings & pipeline suggestions

- **API-vs-engine pin boundary is a reusable pattern.** When a chapter's snippet needs a *declarative* API
  whose runtime engine is not pinned (Jakarta Validation here; the same shape will recur for JPA, CDI,
  logging facades), declare against the pinned API at `provided` scope and assert the metadata reflectively
  rather than inventing an engine GAV. Keeps Floor-C clean and the runtime JDK-only. Propose a one-line note
  in `EXAMPLES-GUIDE` §3.
- **Jakarta constraint annotations do not surface on `RecordComponent.isAnnotationPresent`.** Because
  `@NotNull` etc. lack `RECORD_COMPONENT` in their `@Target`, the reliable inspection point for a record is
  the canonical constructor's parameters (also where a `Validator` reads them). Worth capturing for any
  records + Bean Validation chapter (keys folding 18).
- **`-Xlint:all` + try-with-resources:** a TWR resource whose body throws before using it warns
  ("never referenced") and can create an unreachable trailing statement; reference the resource and let the
  catch be the sole exit. Reinforces the parent's `-Xlint:all` floor as a real gate, not decoration.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 12 gate-run PASS
```
