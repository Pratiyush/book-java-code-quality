# GATE REPORT — EXAMPLE-BUILD — Chapter 50

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module + snippet binding
- **Chapter key:** 50 (owner; folds 52) — `01-index/FINAL_INDEX.md` Ch 24 (CLOSES Part V)
- **Slug:** `50_contract_approval_testing`
- **Draft under review:** `03-drafts/50_contract_approval_testing/50_contract_approval_testing_v1.md`
- **Module built:** `08-companion-code/50_contract_approval_testing/`
- **Run date:** 2026-06-26 · **Updated 2026-06-28** (REPRO: real ApprovalTests.Java wired — see Update note)
- **Reviewer:** `example-builder` + `extract_snippet.sh` + `check_snippets.sh`
- **Scripts run:** `check_snippets.sh` (PASS, 8/8); `mvn -B -Pquality … clean verify` (BUILD SUCCESS)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; built and verified by hand)
- **Verdict:** **PASS** (Floor C)

> **UPDATE 2026-06-28 — REPRO (lift the UTILITY cap): real ApprovalTests.Java now built, not substituted.**
> ApprovalTests.Java is now pinned (`com.approvaltests:approvaltests:31.0.0`, Apache-2.0 — SOURCE-PIN §3,
> verified against Central `maven-metadata.xml` `<release>31.0.0</release>`; bytecode target Java 8, runs on
> the JDK-21 anchor; only mandatory transitive is `approvaltests-util:31.0.0`, all its other deps `<optional>`).
> The dependency is wired into the module in **test scope** and the headline approval test
> (`OrderReportApprovalTest.reportMatchesApprovedBaseline`) is now a **genuine `Approvals.verify(report,
> Options)`** — `Options.withScrubber(new RegExScrubber("generated-at: .*", "generated-at: <timestamp>"))`
> plus `withReporter(QuietReporter.INSTANCE)` so the build stays non-interactive (a mismatch fails the test
> and writes `*.received.txt` instead of launching a diff GUI). The committed baseline
> `OrderReportApprovalTest.reportMatchesApprovedBaseline.approved.txt` sits next to the test, where the
> default `StackTraceNamer` resolves it. The displayed tags `scrubber` (2 lines) and `approval-verify`
> (6 lines) now show the **real library** — closer to the prose's `Approvals.verify(result)` than the
> earlier substitute. The hand-rolled `SnapshotVerifier` is kept alongside it (its `snapshot-verify` tag
> unchanged) to walk the received/approved loop step by step and carry the honest-edge tests (missing
> baseline, changed output, rubber-stamp). Rebuilt green: `mvn -B -Pquality -pl 50_contract_approval_testing
> verify` → **BUILD SUCCESS**, 12 tests 0 failures, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11; `check_snippets.sh`
> 8/8. **The chapter can now re-score for UTILITY** (ApprovalTests moves from cited-not-built → built-green;
> Pact + REST-assured remain runtime-gated/in-JVM, still flagged). LEGAL-IP §5 unaffected — the test is
> original work calling the library's public API; no upstream sample copied.

---

## Verdict rationale

The module builds green at the pin under both preconditions (JDK 21.0.11 ≥ Java 21 floor; `mvn -B
-Pquality clean verify` = BUILD SUCCESS), all 8 displayed snippets resolve to real tag regions ≤9 lines
and `check_snippets.sh` is green 8/8, and every JUnit/AssertJ atom in the module traces to
`SOURCE-PIN.md` §3. The chapter's three named tools (Pact, REST-assured, ApprovalTests.Java) are
realized as their in-JVM mechanisms with zero runtime dependencies, because Pact provider verification
and REST-assured both need a running provider (REPRO PENDING-RUNTIME per the draft) and ApprovalTests.Java
has no SOURCE-PIN row — recorded in `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md`.
This prose-only status of the three tools is the one material decision, and it follows the established
key-20 precedent (an unpinned tool → realize the mechanism in plain JUnit, no faked output); it does not
affect the Floor-C verdict.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime ≥ minimum (Java 21+).**
  `openjdk version "21.0.11" 2026-04-21` — Homebrew `openjdk@21`. Meets the SOURCE-PIN anchor (Java 21
  LTS). Maven: `Apache Maven 3.9.16`.
- **(b) Build GREEN.** Exact command (absolute pom path, standalone — module is not in the aggregator
  `<modules>` list):
  `mvn -B -Pquality -f 08-companion-code/50_contract_approval_testing/pom.xml clean verify`
  Result lines:
  - `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0`
  - `You have 0 Checkstyle violations.`
  - `BugInstance size is 0` / `No errors/warnings found` (SpotBugs)
  - `BUILD SUCCESS`

Both preconditions hold → Floor-C verdict **PASS** (not conditional/assumed).

---

## Module path & shape (self-contained, like key-09)

`08-companion-code/50_contract_approval_testing/`

```
pom.xml                                   <- child of companion-code; <parent> set, no version literal
config/checkstyle/checkstyle.xml          <- copied from key-09 (in-house house ruleset)
config/spotbugs/spotbugs-exclude.xml      <- own (empty; zero reviewed suppressions, with reason)
README.md
src/main/java/org/acme/contracttesting/   Order (value record), OrderReport (report renderer)
                                          OrderProvider (provider; renders id field, observability+ready)
                                          OrderClient (consumer; reads the id field)
                                          OrderContract (consumer-driven contract), ContractViolationException
                                          SnapshotVerifier (approval mechanism), SnapshotMismatchException
                                          OrderNotFoundException (failure path), package-info
src/test/java/org/acme/contracttesting/    OrderContractTest (5: both halves + failure path),
                                          OrderEndpointTest (2), OrderReportApprovalTest (5)
src/test/resources/approvals/             order-report.approved.txt  <- committed reviewed baseline
```

Own `quality` profile (Checkstyle 10.26.1 engine over plugin 3.6.0; SpotBugs 4.9.3.0), self-contained
under the module's own `config/` — the same two-pin shape as key-09. The checkstyle config is copied from
key-09 (the in-house house ruleset, per brief); the spotbugs-exclude is the module's own empty filter with
a written reason. Did NOT edit `08-companion-code/pom.xml`. The module is NOT yet registered in the
aggregator's `<modules>` list (per the floor: register only after green build AND a CODE-REVIEW pass —
CODE-REVIEW is the next gate).

---

## Snippet tags (tag-include regions; all ≤9 lines)

| # | Tag | File | Content lines | Bound in prose at |
|---|---|---|---|---|
| 1 | `consumer-contract` | `OrderContractTest.java` | 2 | after the four-stage pipeline list (consumer drives the contract) |
| 2 | `provider-render` | `OrderProvider.java` | 6 | same section (the provider renders the id field — one edit = the rename) |
| 3 | `provider-verify` | `OrderContractTest.java` | 3 | same section (provider verification replays the contract) |
| 4 | `contract-verify` | `OrderContract.java` | 8 | same section (the contract's presence check over named fields) |
| 5 | `endpoint-behaviour` | `OrderEndpointTest.java` | 5 | after the REST-assured DSL fence (in-JVM request/response/then) |
| 6 | `snapshot-verify` | `SnapshotVerifier.java` | 8 | after the `Approvals.verify` mechanism paragraph |
| 7 | `scrubber` | `OrderReportApprovalTest.java` | 2 | same section (a real `RegExScrubber` normalizes the timestamp) |
| 8 | `approval-verify` | `OrderReportApprovalTest.java` | 6 | same section (the test is one real `Approvals.verify(report, Options)` against the baseline) |

`check_snippets.sh 03-drafts/50_contract_approval_testing/50_contract_approval_testing_v1.md`
→ **8 marker(s); 8 pass, 0 fail.** Each region verified ≤9 lines by `extract_snippet.sh` (max = 8).
"Snippet tags:" line added to the draft after the "When to use what" section. No prose deleted; each
marker carries a one-line lead-in in the locked voice.

---

## Enterprise-grade checklist

- **Pinned dependency set (one inherited parent; test-scope tools pinned).**
  - JUnit Jupiter: inherited from parent `junit-bom` → resolves **6.0.3** (no literal in module). *(Same
    aggregator-pin note as key-42: parent pins 6.0.3; SOURCE-PIN §3 row names the JUnit 6 line — both are
    that line.)*
  - AssertJ: inherited (managed) → **3.27.7** ✓ (SOURCE-PIN §3).
  - **ApprovalTests.Java: `com.approvaltests:approvaltests:31.0.0` (test scope) ✓ (SOURCE-PIN §3).** The
    aggregator's `dependencyManagement` pins only the universal test stack (JUnit + AssertJ), so this
    chapter-specific tool carries its pinned version literal in the module (the brief forbids editing the
    aggregator pom). Footprint is minimal: the only non-`<optional>` transitive is
    `com.approvaltests:approvaltests-util:31.0.0`; every other ApprovalTests dependency is `<optional>` and
    is not pulled in. No `<groupId>`/`<version>` of its own and no own BOM. `<parent>` =
    `org.acme.storefront:companion-code`. No production runtime dependency (the library is test-scope only;
    Pact + REST-assured remain prose-only — see flag).
- **Externalized config / profiles.** Static-analysis gate behind the `-Pquality` profile (default build
  stays fast); Checkstyle/SpotBugs config externalized under `config/`. The provider's identifier field
  name is an externalized constructor parameter (`idFieldName`), not hard-coded, so the failure-path
  rename is config-driven; the approval baseline is an externalized committed file under
  `src/test/resources/approvals/`, and the scrubber is an injected `UnaryOperator<String>`.
- **At least one integration/mechanism test + harness setup.** 12 JUnit Jupiter tests exercise the
  chapter's mechanisms: the consumer-driven contract (both halves), the field-rename failure path, the
  endpoint behaviour, and the approval/snapshot loop (match, missing-baseline-fails, changed-output-fails,
  rubber-stamp risk, scrubber). Test harness: `maven-surefire-plugin` 3.5.6 (inherited) auto-detects the
  JUnit Platform provider; `includeTestSourceDirectory=true` holds the tests to the house Checkstyle
  ruleset; `@TempDir` keeps the snapshot read/write hermetic. Confirmed a clean run with no spurious
  logging.
- **Observability / health surface.** `OrderProvider.notFoundCount()` (a running counter of missed
  lookups) and `OrderProvider.isReady()` (readiness probe over the orders held); the Surefire report is
  the test-side observability surface.
- **Explicit failure path.** Two typed, branchable failures, each the point of its technique:
  `ContractViolationException` (the contract names the missing field after the `id`→`orderId` rename — the
  consumer-breaking change a one-sided test misses) and `OrderNotFoundException` with a stable `code`
  (the endpoint's defined not-found response). The approval test demonstrates the honest edge — a
  rubber-stamped wrong baseline confirming wrong output — as a passing test the prose critiques, kept
  green per the brief.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan as fixed in the draft is two designed diagrams
(`fig50_1` — the Pact four-stage pipeline; `fig50_2` — three techniques/three questions), already authored
as HTML→PNG with source sidecars under `05-figures/50_contract_approval_testing/`, and **zero captured
screenshots**. The module is a library/JUnit module with no subject-native UI surface (no dev console,
health view, or API explorer) to capture, and Step 4c forbids inventing an unplanned figure. Nothing
captured; no sidecars written.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file. Every `.java` file is original work written for this book: an orders
provider/consumer boundary authored to the dossier's spec, structurally mirroring the in-house key-09 and
key-42 reference modules — not a copied or lightly-edited upstream Pact, REST-assured, or ApprovalTests
sample, quickstart, or getting-started skeleton. The `OrderContract`/`SnapshotVerifier` are original
hand-rolled stand-ins for the tools' mechanisms, not upstream code. No upstream `NOTICE`/header
boilerplate copied. The `order-report.approved.txt` baseline is content this module's own `OrderReport`
renders. The `config/checkstyle/checkstyle.xml` is copied from the book's own key-09 module (the in-house
house ruleset), as the brief directs ("config/ copied from 09"); it is the book's own artifact. No region
is taken substantially verbatim from a source file, so no per-file attribution is required.

---

## Source trace (each load-bearing atom → pinned authority / flag)

| Atom in module | Traces to |
|---|---|
| `@Test`/`@DisplayName`/`@Nested`; `@TempDir` | JUnit Jupiter User Guide (SOURCE-PIN §3, JUnit 6 line); dossier 50 §0 |
| AssertJ `assertThat(...)`, `assertThatExceptionOfType`, `assertThatNoException` | AssertJ docs (SOURCE-PIN §3, 3.27.7) |
| Consumer-driven contract model (consumer declares what it reads; provider replays) | Pact docs (dossier 50 §1, verbatim "code-first consumer-driven … only communication paths actually used by consumers get tested") — realized in-JVM; tool prose-only (flag) |
| Provider-verification-replays-against-real-provider; `@State` precondition idea | Pact provider/junit5 docs (dossier 50 §0/§1) — realized in-JVM; tool prose-only (flag) |
| `given()/when()/then()`, `body(path, matcher)` endpoint-assertion shape | REST-assured wiki (dossier 50 §1, verified DSL) — realized in-JVM request/response/then; tool prose-only (flag) |
| `Approvals.verify(String, Options)`, `Options#withScrubber/#withReporter`, `RegExScrubber`, `QuietReporter` | ApprovalTests.Java **31.0.0** (SOURCE-PIN §3; API decompiled from the pinned jar) — **real library, built green in test scope** |
| Approval loop: `*.received.*` vs `*.approved.*`, diff on mismatch, scrubbers, "verifies unchanged not correct" | ApprovalTests.Java repo/site (dossier 52 §2/§4) — headline test uses the real library; `SnapshotVerifier` retained to walk the loop + carry the honest-edge tests |
| Golden-master / characterization framing | Feathers *WELC* (SOURCE-PIN §7 canon; dossier 52 §2.2) — ⚠ §7 canon row, cited not asserted |
| `Map.copyOf` / `Set.copyOf` defensive copy; record validation; `System.Logger`; `AtomicLong` | JDK 21 API (SOURCE-PIN §1) — same idioms as key-09/key-42 |
| ApprovalTests.Java GAV/version | **pinned + built**: `com.approvaltests:approvaltests:31.0.0` (SOURCE-PIN §3; 2026-06-28) |
| Pact GAV (4.7.0), REST-assured 6.0.0 | `⚠ verify at pin` / runtime-gated — see `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md` |

No invented atoms. No tool API/version/GAV presented as fact beyond the dossier + SOURCE-PIN; the three
runtime/unpinned tools are named in prose only and flagged.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | **Pact + REST-assured remain runtime-gated** (provider verification / running endpoint; REPRO PENDING-RUNTIME per draft). Their mechanisms stay realized in plain JDK+JUnit; tools named in prose/README only. **(ApprovalTests.Java is RESOLVED — pinned + built green, 2026-06-28; see Update note.)** | MATERIAL (flagged, narrowed) | `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md` | Human: provision a Docker/runtime env for a real Pact provider verification + REST-assured test; re-confirm Pact (4.7.0) / REST-assured (6.0.0) annotation+GAV at `/pin-source`, then optionally upgrade those tagged regions to real tool forms (prose claims unchanged). |
| 1b | **ApprovalTests.Java REPRO done** — pinned `com.approvaltests:approvaltests:31.0.0` (Apache-2.0) and wired in test scope; headline approval test now a real `Approvals.verify(...)`; built green. | RESOLVED | module pom + `OrderReportApprovalTest.java` + SOURCE-PIN §3 | Chapter re-scores for UTILITY (cited-not-built → built-green). |
| 2 | JUnit resolves to **6.0.3** (parent `junit-bom`), not a 6.1.0 literal. Module correctly inherits the parent and adds no literal; both are the pinned JUnit 6 line. | NOTE | `08-companion-code/pom.xml` `junit.version` | Aggregator-level decision (out of scope: "do NOT edit 08-companion-code/pom.xml"). Same note recorded for key-42. |

---

## Blockers

**None for the EXAMPLE gate.** Build is green under both FLOOR C preconditions; all 8 snippets resolve;
all compiled atoms trace to the pin. Finding 1 is a material flag (the three named tools' prose-only
status) routed to the human for `/pin-source` + runtime provisioning, not a build blocker — the module
builds green and faithfully demonstrates each mechanism without inventing any tool API/version/GAV.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality clean verify` at the pin; every
  displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file; FLOOR C
  source-trace clean.
- [ ] **CODE-REVIEW** — not run by this gate; the module is held back from the aggregator `<modules>`
  list until CODE-REVIEW passes (next gate, the `code-reviewer` agent).

---

## Learnings & pipeline suggestions

- **"Unpinned/runtime-gated tool → realize the mechanism in plain JUnit, no faked output" is now a
  repeatable pattern** (key 20 JCStress, key 42 Truth, key 50 Pact/REST-assured/ApprovalTests). When a
  chapter's named tool cannot enter a green standalone build — it is unpinned, or it needs Docker/a running
  service — the example-builder realizes the tool's mechanism with the JDK + the pinned test stack, names
  the tool neutrally in the README/prose, and flags the prose-only status. Candidate promotion to
  `EXAMPLES-GUIDE`: state that a chapter naming a runtime-gated or unpinned tool gets an in-JVM mechanism
  stand-in plus a flag, never an invented coordinate and never faked tool output.
- **A consumer-driven contract has a clean in-JVM shape.** A shared contract object both the consumer test
  and the provider verification check against reproduces Pact's central guarantee (and its failure path —
  the field rename) with zero infrastructure. Reusable for any "two sides agree on a message" chapter where
  a broker is overkill for the teaching point.
- **The approval honest-edge reads best as a passing-but-critiqued test.** Showing a rubber-stamped wrong
  baseline as a green assertion the prose names as the failure mode (rather than a disabled or red test)
  satisfies both the chapter's centerpiece-risk teaching and the build-must-stay-green floor — the same
  shape as the key-42 over-mock smell.
- **REPRO 2026-06-28 — a "cited-not-built unpinned tool" can be promoted to built once the GAV is verified
  on Central and its bytecode target clears the anchor.** ApprovalTests.Java was capped as unpinned; the lift
  was: (1) read the latest `<release>` from Central `maven-metadata.xml` (31.0.0 — never guessed), (2) inspect
  the jar's bytecode major version (52 = Java 8 → safe on JDK 21) and the POM's mandatory-vs-`<optional>`
  transitives (only `approvaltests-util` is mandatory — a tiny footprint), (3) decompile the public API from
  the pinned jar to write the test against verified signatures, (4) prove it in a throwaway POC before
  touching the book module. Two ApprovalTests CI gotchas worth promoting to EXAMPLES-GUIDE: use
  `QuietReporter.INSTANCE` (the default reporter launches a diff GUI → hangs headless CI), and place the
  `<TestClass>.<method>.approved.txt` baseline next to the test source (the default `StackTraceNamer`
  convention) so the first run is green, not "fails-by-design" red. Keeping the hand-rolled `SnapshotVerifier`
  alongside the real call let the honest-edge tests (missing/changed/rubber-stamp) stay without faking library
  internals.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 50 gate-run PASS
```
