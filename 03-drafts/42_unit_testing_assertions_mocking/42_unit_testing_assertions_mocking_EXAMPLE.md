# GATE REPORT — EXAMPLE-BUILD — Chapter 42

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module + snippet binding
- **Chapter key:** 42 (owner; folds 43 + 44) — `01-index/FINAL_INDEX.md` Ch 21
- **Slug:** `42_unit_testing_assertions_mocking`
- **Draft under review:** `03-drafts/42_unit_testing_assertions_mocking/42_unit_testing_assertions_mocking_v1.md`
- **Module built:** `08-companion-code/42_unit_testing_assertions_mocking/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `extract_snippet.sh` + `check_snippets.sh`
- **Scripts run:** `check_snippets.sh` (PASS, 7/7); `mvn -B -Pquality … clean verify` (BUILD SUCCESS)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; built and verified by hand)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module builds green at the pin under both preconditions (JDK 21.0.11 ≥ Java 21 floor; `mvn -B
-Pquality clean verify` = BUILD SUCCESS), all 7 displayed snippets resolve to real tag regions ≤9
lines and `check_snippets.sh` is green 7/7, and every Mockito/JUnit/AssertJ/Hamcrest atom in the
module traces to `SOURCE-PIN.md` §3. Two non-blocking decisions are flagged for the human (Truth shown
in prose not compiled; JUnit resolves to the parent-pinned 6.0.3, not 6.1.0) — neither affects the
Floor-C verdict.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime ≥ minimum (Java 21+).**
  `openjdk version "21.0.11" 2026-04-21` — Homebrew `openjdk@21`. Meets the SOURCE-PIN anchor (Java 21
  LTS). Maven: `Apache Maven 3.9.16`.
- **(b) Build GREEN.** Exact command (from the module dir, absolute pom path):
  `mvn -B -Pquality -f 08-companion-code/42_unit_testing_assertions_mocking/pom.xml clean verify`
  Result lines:
  - `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0`
  - `You have 0 Checkstyle violations.`
  - `BugInstance size is 0` / `No errors/warnings found` (SpotBugs)
  - `BUILD SUCCESS`

Both preconditions hold → Floor-C verdict **PASS** (not conditional/assumed).

> Benign advisory (not a build warning): under JDK 21 Mockito's inline mock maker self-attaches its
> Byte Buddy agent and prints a future-JDK deprecation notice. The build is green and Surefire is
> clean; a production build would add the agent explicitly per Mockito's docs. Recorded as NOTE.

---

## Module path & shape (self-contained, like key-09)

`08-companion-code/42_unit_testing_assertions_mocking/`

```
pom.xml                                   <- child of companion-code; <parent> set, no version literal
config/checkstyle/checkstyle.xml          <- copied from key-09 (in-house house ruleset)
config/spotbugs/spotbugs-exclude.xml      <- copied from key-09 (empty; zero reviewed suppressions)
README.md
src/main/java/org/acme/orders/            Money, LineItem, Receipt (value objects, used real)
                                          PriceCatalog (query port), PaymentGateway (command port)
                                          OrderService (unit under test), package-info
                                          OrderRejectedException, PaymentDeclinedException (failure path)
src/test/java/org/acme/orders/            OrderServiceTest (8 tests), AssertionStylesTest (3),
                                          OverMockedOrderServiceTest (2)
```

Own `quality` profile (Checkstyle 10.26.1 engine over plugin 3.6.0; SpotBugs 4.9.3.0), self-contained
under the module's own `config/` — the same two-pin shape as key-09. Did NOT edit
`08-companion-code/pom.xml`. The module is NOT yet registered in the aggregator's `<modules>` list
(per the floor: register only after green build AND a CODE-REVIEW pass — CODE-REVIEW is the next gate).

---

## Snippet tags (tag-include regions; all ≤9 lines)

| # | Tag | File | Content lines | Bound in prose at |
|---|---|---|---|---|
| 1 | `mockito-setup` | `OrderServiceTest.java` | 7 | "Mockito is the Java library that creates these…" |
| 2 | `aaa-structure` | `OrderServiceTest.java` | 6 | "What a quality JUnit test looks like…" |
| 3 | `stub-a-query` | `OrderServiceTest.java` | 6 | CONCEPT "The choice follows the collaborator" (query→stub) |
| 4 | `verify-a-command` | `OrderServiceTest.java` | 2 | same CONCEPT (command→verify) |
| 5 | `value-not-mocked` | `OrderServiceTest.java` | 7 | same CONCEPT (value object→real) |
| 6 | `four-assertion-styles` | `AssertionStylesTest.java` | 6 | after the assertion-axes paragraph |
| 7 | `over-mock-smell` | `OverMockedOrderServiceTest.java` | 5 | "Behaviour verification couples tests to implementation" |

`check_snippets.sh 03-drafts/42_unit_testing_assertions_mocking/42_unit_testing_assertions_mocking_v1.md`
→ **7 marker(s); 7 pass, 0 fail.** Each region verified ≤9 lines by `extract_snippet.sh` (max = 7).
"Snippet tags:" line added to the draft's companion-module spec.

---

## Enterprise-grade checklist

- **Pinned dependency set (one inherited parent + pinned literals traced to SOURCE-PIN §3).**
  - JUnit Jupiter: inherited from parent `junit-bom` → resolves **6.0.3** (no literal in module). *(See
    flag: parent pins 6.0.3; brief said 6.1.0 — both the pinned JUnit 6 line.)*
  - AssertJ: inherited (managed) → **3.27.7** ✓ (SOURCE-PIN §3).
  - Mockito `mockito-core` + `mockito-junit-jupiter`: explicit test-scope literals **5.23.0** ✓
    (SOURCE-PIN §3; aggregator does not manage them — added explicitly per brief).
  - Hamcrest: explicit test-scope literal **3.0** ✓ (SOURCE-PIN §3; aggregator does not manage it).
  - No own `<groupId>`/`<version>`; no own BOM. `<parent>` = `org.acme.storefront:companion-code`.
- **Externalized config / profiles.** Static-analysis gate behind the `-Pquality` profile (default
  build stays fast); Checkstyle/SpotBugs config externalized under `config/`. Mockito strictness is the
  externalized `MockitoExtension` default (`STRICT_STUBS`), documented in a class comment and in the
  prose, not hard-coded per test.
- **At least one integration/mechanism test + harness setup.** 13 JUnit Jupiter tests exercise the
  chapter's mechanism (harness wiring, three assertion styles, stub-a-query, verify-a-command,
  value-real, the failure path). Test harness: `maven-surefire-plugin` 3.5.6 (inherited)
  auto-detects the JUnit Platform provider; `includeTestSourceDirectory=true` holds the tests to the
  house Checkstyle ruleset. Confirmed a clean run with no spurious logging (only the benign Mockito
  self-attach advisory).
- **Observability / health surface.** `OrderService.placedCount()`, `rejectedCount()` (running
  counters) and `isReady()` (readiness probe over the two wired ports); the Surefire report is the
  test-side observability surface.
- **Explicit failure path.** Typed `OrderRejectedException` with stable codes (`empty-order`,
  `unknown-sku`) + fail-fast guards so a rejected order never reaches the gateway
  (`verifyNoInteractions`); a declined charge surfaces a typed `PaymentDeclinedException`
  (`doThrow(...).when(...)`). The over-mock anti-pattern is shown as a passing-but-brittle `InOrder`
  test the prose critiques, with the dead-stub `UnnecessaryStubbingException` case documented (kept out
  of the running test so the build stays green — per the brief's "keep it green" constraint).

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan as fixed in the draft is two designed diagrams
(`fig42_1`, `fig42_2`, already authored as HTML→PNG with source sidecars under
`05-figures/42_unit_testing_assertions_mocking/`) and **zero captured screenshots**. The dossier §6
listed an *optional* `UnnecessaryStubbingException` console capture (Fig 44.3), but the draft (the
authority for the fixed figure plan) does not include it, and Step 4c forbids inventing an unplanned
figure here. Nothing captured; no sidecars written.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file. Every `.java` file is original work written for this book: an order/pricing
domain authored to the dossier's spec, structurally mirroring the in-house key-09 reference module —
not a copied or lightly-edited upstream Mockito/JUnit/AssertJ sample, quickstart, or getting-started
skeleton. No upstream `NOTICE`/header boilerplate copied. The two `config/` files are copied from the
book's own key-09 module (the in-house house ruleset), as the brief directs ("COPY config/ from 09");
they are the book's own artifacts, not upstream samples. No region is taken substantially verbatim from
a source file, so no per-file attribution is required.

---

## Source trace (each load-bearing atom → pinned authority)

| Atom in module | Traces to |
|---|---|
| `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks` | Mockito Javadoc (SOURCE-PIN §3, Mockito 5.23.0); dossier 44 §2.2 |
| `when(...).thenReturn(...)`, `doThrow(...).when(...)` | Mockito Javadoc; dossier 44 §2.3 |
| `verify(...)`, `verify(..., never())`, `verifyNoInteractions(...)`, `InOrder`/`inOrder` | Mockito Javadoc; dossier 44 §2.3 |
| `ArgumentMatchers.eq(...)`, `any(...)` (all-or-none rule) | Mockito Javadoc; dossier 44 §2.3 |
| `STRICT_STUBS` default + `UnnecessaryStubbingException` | Mockito `MockitoExtension`/`Strictness` Javadoc; dossier 44 §2.4 |
| Mockito GAV + version 5.23.0; inline maker default (no `mockito-inline`) | SOURCE-PIN §3; dossier 44 §5 |
| `assertEquals`/`assertThrows`/`assertAll` (JUnit built-in) | JUnit Jupiter `Assertions` (SOURCE-PIN §3); dossier 43 §2.1 |
| AssertJ `assertThat(...).isEqualTo`, `assertThatThrownBy` | AssertJ docs (SOURCE-PIN §3, 3.27.7); dossier 43 §2.1 |
| Hamcrest `assertThat(x, is(equalTo(...)))` | Hamcrest docs (SOURCE-PIN §3, 3.0); dossier 43 §2.1 |
| `@Test`/`@DisplayName`/`@Nested`; AAA structure; FIRST | JUnit User Guide (SOURCE-PIN §3); dossier 42 §2.2/§2.4 |
| query→stub / command→mock / value→real decision | Fowler *Mocks Aren't Stubs* (dossier 44 §2.6); draft "choice follows the collaborator" |

No invented atoms. No ahead-of-pin facts presented as fact.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | JUnit resolves to **6.0.3** (parent `junit-bom`), not the brief's 6.1.0. Module correctly inherits the parent and adds no literal; both are the pinned JUnit 6 line. | NOTE | `08-companion-code/pom.xml` `junit.version` | Human decision: bump the aggregator `junit.version` to 6.1.0 (SOURCE-PIN row) in a separate aggregator-level change, then all child modules move together. Out of scope here ("do NOT edit 08-companion-code/pom.xml"). Flagged. |
| 2 | **Truth** (4th assertion style in the chapter's table) shown in README/prose, not compiled. | NOTE | `AssertionStylesTest.java`; README "The four assertion styles" | Truth (1.4.5, SOURCE-PIN §3) is outside the brief's authorized dep list, absent from the local `.m2`, and pulls a heavy transitive tree (Guava+). If a compiled 4th style is wanted, add `com.google.truth:truth:1.4.5` test-scope (own literal) and a `truth-style` tag. Human decision; flagged. |
| 3 | Transitive `net.bytebuddy:byte-buddy` resolves to **1.18.3** (core) while `byte-buddy-agent` is 1.17.7 (Mockito 5.23.0's pin). | NOTE | dependency tree | Tolerated by Mockito at runtime; build green, 13 tests pass. No action unless a future Mockito run misbehaves; recorded for provenance. |
| 4 | Mockito self-attach advisory under JDK 21. | NOTE | Surefire console | Informational, not a build warning. A production build would add the Byte Buddy agent explicitly per Mockito docs. No action. |

---

## Blockers

**None.** Build is green under both FLOOR C preconditions; all snippets resolve; all atoms trace to the
pin. The 4 findings are NOTE-severity decisions/observations for the human, none blocking.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality clean verify` at the pin;
  every displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file;
  FLOOR C source-trace clean.
- [ ] **CODE-REVIEW** — not run by this gate; the module is held back from the aggregator `<modules>`
  list until CODE-REVIEW passes (next gate, the `code-reviewer` agent).

---

## Learnings & pipeline suggestions

- **Authorized-dep list narrower than SOURCE-PIN is a recurring example-builder decision.** When a
  chapter contrasts N libraries but the build brief authorizes a subset, compile the subset and present
  the rest in prose/README with a flag, rather than pulling an unauthorized (even if pinned) dep with a
  heavy transitive tree. Faithful to the chapter (the table still shows all four), green, and
  in-bounds. Candidate note for `EXAMPLES-GUIDE` / the example brief: state explicitly which of a
  comparison chapter's libraries must be compiled vs may be prose-only.
- **Aggregator pin vs brief pin drift (JUnit 6.0.3 vs 6.1.0).** A child module that correctly inherits
  the parent will silently resolve the parent's version, not the version a task brief names. The
  aggregator is the single source for managed versions; bumping a managed version is an aggregator-level
  change that re-tests every child. Worth a one-line check in the example brief: "confirm the
  aggregator's managed version matches the SOURCE-PIN row before building."
- **Failure-path-kept-green pattern works cleanly for the over-mock smell.** Showing the brittle
  `InOrder` test as passing-but-critiqued (with the dead-stub `UnnecessaryStubbingException` documented
  rather than triggered) satisfies both the chapter's teaching need and the "build must stay green"
  floor. Reusable shape for any "anti-pattern as failure path" chapter where a live failing test would
  redden the build.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 42 gate-run PASS
```
