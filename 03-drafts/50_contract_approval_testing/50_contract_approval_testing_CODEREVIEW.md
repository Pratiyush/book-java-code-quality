# GATE REPORT — CODE-REVIEW — Chapter 50

> Senior-PR review of the published companion module readers copy into their own apps. The bar is
> "exemplary and idiomatic," not merely "compiles + tests pass." Code is not edited here; fixes are
> reported for the example-builder to apply, then re-review.

## Header

- **Gate:** CODE-REVIEW (FLOOR-C second half) — technical profile
- **Chapter key:** 50 (owner; folds 52) — `01-index/FINAL_INDEX.md` Ch 24 (CLOSES Part V)
- **Slug:** `50_contract_approval_testing`
- **Draft under review:** `03-drafts/50_contract_approval_testing/50_contract_approval_testing_v1.md`
- **Module reviewed:** `08-companion-code/50_contract_approval_testing/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` (+ `check_snippets.sh`; `mvn -B -Pquality clean verify`; tag brace/line/banned-word scans)
- **Scripts run:** `check_snippets.sh` (8/8 PASS); `mvn -B -Pquality -f pom.xml clean verify` (BUILD SUCCESS; JAVA_HOME=openjdk@21 21.0.11)
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is genuinely exemplary on structure, idiom, security, and snippet hygiene: it builds green and
warning-clean (12 tests, 0 failures; 0 Checkstyle, 0 SpotBugs; no `-Xlint` warnings), all eight displayed
`// tag::` regions are brace-balanced, <=8 lines, statement-complete, and free of banned NEUTRALITY words,
and the cited-not-built framing (Pact / REST-assured / ApprovalTests.Java realized in plain JDK+JUnit) is
stated honestly in javadoc, the README, and the package-info. **No BLOCKER, no security finding, no
neutrality finding, no invention.** Two MAJOR correctness/prose-fidelity defects keep it from a clean PASS:
(1) the `rubberStampingAWrongBaselineHidesABug` test asserts the *opposite* of the chapter's central
risk — it proves a mismatch is *caught*, not that a rubber-stamped bug is confirmed; and (2) the displayed
`approval-verify` snippet's comment claims "Render at two different instants" while the code renders once.
Both are published, copy-me artifacts illustrating the chapter's load-bearing points, so both are required
fixes before approval. They do not block FLOOR C (no BLOCKER/security/neutrality/invention), but the gate
is PASS-WITH-FIXES, not PASS.

---

## Review dimensions

| # | Dimension | Result | Note |
|---|---|---|---|
| 1 | Correctness | **FIX** | Logic, edge cases, resource handling all sound; but the rubber-stamp test (F1) does not exercise the behaviour its name/prose claim, and the `approval-verify` comment (F2) misdescribes the code. |
| 2 | Idiomatic Java 21 | **PASS** | Records with compact-canonical validation; `Map.copyOf`/`Set.copyOf` defensive copies; `System.Logger`; `AtomicLong`; `UnaryOperator<String>` scrubber injected; `@Serial`; `Optional` return; `@TempDir`; `@Nested`/`@DisplayName`. No raw threads, no blocking misuse, no stdout. |
| 3 | Security | **PASS** | No hardcoded secrets/tokens/keys (grep clean); inputs null-validated on every public method; no injection sink; exceptions carry domain messages, no stack-trace/internals leak. (See F4 NOTE: response body is hand-built string concatenation — fine for a teaching stand-in, not a JSON-injection sink here.) |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point; no dead code, no unused deps (zero runtime deps), no gratuitous abstraction; realistic names (`OrderProvider`/`OrderClient`/`OrderContract`), no `Foo`/`Bar`/`tmp`; every public type carries a one-line purpose javadoc. |
| 5 | Prose<->code fidelity & originality | **FIX** | GAV/version atoms trace to the pin or the flag; LEGAL-IP §5 original-for-this-book confirmed (hand-rolled stand-ins, not upstream samples). But F1 and F2 are prose<->code mismatches; F3 is a minor over-declaration. |
| 6 | Neutrality in code | **PASS** | No banned phrasing (better than / unlike X / superior / beats / the problem with X) and no crowning in any comment, identifier, log string, or README line. "complementary, not rivals" framing is the allowed neutral carve-out and appears in prose only. |

---

## Build / lint result

- **Command:** `mvn -B -Pquality -f 08-companion-code/50_contract_approval_testing/pom.xml clean verify`
  with `JAVA_HOME=/opt/homebrew/opt/openjdk@21` (`openjdk version "21.0.11"`).
- **Result:** `BUILD SUCCESS`; `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0`.
- **Warning-clean:** compiler `-Xlint:all,-processing` (inherited from aggregator) emits **no** warnings;
  `You have 0 Checkstyle violations.`; SpotBugs `BugInstance size is 0` (`checkstyle-result.xml` errors=0,
  `spotbugsXml.xml` BugInstance count=0). No warning to report.
- **Failure-path coverage:** present and real — `OrderContractTest.FieldRenameBreaksTheConsumer` exercises
  the `id`->`orderId` rename (contract throws `ContractViolationException` naming `id`; one-sided shape test
  still passes); `OrderEndpointTest.getUnknownOrderReportsNotFound` exercises the 404/`OrderNotFoundException`
  path; `OrderReportApprovalTest` covers missing-baseline-fails and changed-output-fails. (The rubber-stamp
  failure-mode test exists but mis-asserts — see F1.)

---

## Snippet hygiene (every displayed `// tag::` region — BLOCKER-class checks)

`check_snippets.sh` = **8 markers; 8 pass, 0 fail.** Each region independently re-extracted and checked for
brace/paren balance, line count (<=9), statement-completeness, and banned words:

| # | Tag | File | Non-blank lines | Braces {/} | Parens (/) | Fragment? | Banned word? |
|---|---|---|---|---|---|---|---|
| 1 | `consumer-contract` | `OrderContractTest.java` | 2 | 0/0 | 2/2 | no (complete decl) | none |
| 2 | `provider-render` | `OrderProvider.java` | 6 | 2/2 | 8/8 | no (complete method) | none |
| 3 | `provider-verify` | `OrderContractTest.java` | 3 | 0/0 | 5/5 | no (complete stmts) | none |
| 4 | `contract-verify` | `OrderContract.java` | 8 | 3/3 | 6/6 | no (complete method) | none |
| 5 | `endpoint-behaviour` | `OrderEndpointTest.java` | 4 | 0/0 | 7/7 | no (complete stmts) | none |
| 6 | `snapshot-verify` | `SnapshotVerifier.java` | 8 | 2/2 | 9/9 | no (complete method) | none |
| 7 | `scrubber` | `OrderReportApprovalTest.java` | 2 | 0/0 | 1/1 | no (complete decl) | none |
| 8 | `approval-verify` | `OrderReportApprovalTest.java` | 4 | 0/0 | 6/6 | no (complete stmt) | none |

**No broken snippet, no mid-statement fragment, no over-length region, no banned word in any displayed
region.** No BLOCKER. (Content defect in #8 is the *comment*, recorded as MAJOR F2 — the region is
structurally clean.)

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | **The rubber-stamp test asserts the opposite of its claim.** `rubberStampingAWrongBaselineHidesABug` writes a wrong baseline (`total: 9999`), then asserts `verify(...)` **throws** `SnapshotMismatchException` because the correct freshly-rendered report (`total: 6200`) does NOT match it. So it demonstrates a mismatch being *caught*, not a rubber-stamped bug being *confirmed*. The `@DisplayName` ("rubber-stamping a wrong baseline makes the test confirm wrong output"), the method name, and the draft (line 178: "approving a wrong `received` report bakes the bug into the baseline") all describe the opposite of what runs. The test is vacuous w.r.t. its stated purpose. | MAJOR | `src/test/java/org/acme/contracttesting/OrderReportApprovalTest.java:84-104`; draft line 178 | Rewrite to model the real failure mode: render the WRONG/old output, approve THAT as the baseline (promote received->approved), then verify the SAME wrong output again and assert it **passes** (`assertThatNoException`) — the bug is now baked in and confirmed green. Then keep one assertion that the real correct value (6200) differs from the now-approved 9999. Re-align method name + `@DisplayName` + draft line 178 to the corrected behaviour. |
| 2 | **Displayed `approval-verify` snippet comment contradicts the code.** Comment says "Render at two different instants; the scrubber makes both match the one approved baseline," but the region renders the report exactly once (`Instant.now()`). The "two instants" idea belongs to `scrubberRemovesNonDeterminism` (lines 109-110). Misleading comment in a copy-me, displayed snippet; the draft lead-in (line 114, "a single `verify` call") is correct, so the in-code comment contradicts both code and prose. | MAJOR | `src/test/java/org/acme/contracttesting/OrderReportApprovalTest.java:50` (tag `approval-verify`) | Replace the comment with one that matches the single render, e.g. `// Render the report, scrub the timestamp, and verify it against the one committed baseline.` Region stays <=9 lines and brace-balanced. |
| 3 | **Contract over-declares relative to what the consumer reads.** `OrderContract` requires `{"id","status"}` and the javadoc says the consumer "declares what it actually reads (here, the `id` and `status` fields)," but `OrderClient` only reads `id` (`REQUIRED_FIELD="id"`, `ID_PATTERN` over `id`). Defensible as teaching (a contract may name several fields), but the "what it actually reads" wording is slightly inconsistent with `OrderClient`. | MINOR | `src/main/java/org/acme/contracttesting/OrderContract.java:10-11`; `OrderClient.java:18` | Either make `OrderClient` also read `status`, or soften the javadoc to "the fields a consumer relies on (here `id` and `status`)" so it does not claim this specific client reads both. |
| 4 | **SnapshotVerifier compares raw-written received against normalized-read approved.** `verify` writes `scrubbed` verbatim but compares `read(approved)` (lines re-joined with `\n` + one trailing `\n`) against the raw `scrubbed`. For this module's single-`\n` report it matches, but as published approval-verifier code the one-sided newline normalization is a latent gotcha (CRLF input, or differing trailing-newline counts, would mismatch content-equal text; a received file would not round-trip equal to itself). | NOTE | `src/main/java/org/acme/contracttesting/SnapshotVerifier.java:50-78` | Optional: normalize both sides the same way (read the received-equivalent through the same path, or compare normalized-to-normalized), or add a one-line javadoc noting the comparison is line-normalized. |
| 5 | **Hand-built JSON string in `renderOrder` / `OrderReport`.** Response bodies are string concatenation, not a serializer. Correct and injection-safe for the fixed test data here, and a deliberate teaching simplification, but worth a one-line note so a reader does not copy string-built JSON into production where a value could contain a quote. | NOTE | `src/main/java/org/acme/contracttesting/OrderProvider.java:70-75` | Optional: a one-line comment that production code would use a JSON serializer; values here are controlled tokens. |
| 6 | **Module already registered in the aggregator `<modules>` list.** `08-companion-code/pom.xml` lists `50_contract_approval_testing`. Per the floor, registration follows a CODE-REVIEW pass; it is present ahead of this report. Informational — not a code defect; flagged so the orchestrator confirms the ordering is intended. | NOTE | `08-companion-code/pom.xml` `<modules>` | None (informational). Confirm registration was intended post-EXAMPLE; this gate's PASS-WITH-FIXES does not retroactively block it, but F1/F2 fixes should land before reliance on the displayed artifacts. |

---

## Blockers

**None.** No HARD-rule violation: no broken/over-length/fragment snippet, no banned NEUTRALITY word in any
displayed region or anywhere in the module, no hardcoded secret, no invented rule/flag/GAV/version, no
unattributed verbatim lift (LEGAL-IP §5 original-for-this-book confirmed). FLOOR C is not blocked.

- [x] Snippet brace-balance / <=9 lines / no fragment / no banned word — all 8 regions clean.
- [x] No security finding (secret scan clean; inputs validated; no internals leak).
- [x] No neutrality finding in code.
- [x] No invention (every atom traces to the pin or the recorded flag).

> F1 and F2 are MAJOR (required before approval) but not BLOCKER: the build is green, the snippets are
> structurally sound, and the defects are wrong-behaviour/wrong-comment in copy-me artifacts, fixable
> without touching the law floors.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness **FIX** (F1, F2) / idiomatic **PASS** / security **PASS** /
  simplicity **PASS** / prose<->code fidelity **FIX** (F1, F2, F3) / neutrality-in-code **PASS**.
- [x] Build green + warning-clean re-confirmed (`mvn -B -Pquality clean verify` = BUILD SUCCESS; 0/0/0).
- [x] At least one failure-path integration test per public behaviour — present (contract rename; 404 /
  not-found; missing baseline; changed output). One failure-mode test (rubber-stamp) mis-asserts — F1.
- [x] Hardcoded-secret grep — no hits.

---

## Learnings & pipeline suggestions

- **"Passing-but-critiqued" failure-mode tests need an assertion that proves the failure mode, not its
  inverse.** The rubber-stamp test passes (green build) yet asserts a *mismatch*, the opposite of the
  "bug baked into the baseline" point it exists to teach. A green test whose `@DisplayName` names a risk it
  does not actually exercise is vacuous-by-intent. Suggest an EXAMPLES-GUIDE note: when a test demonstrates
  a *failure mode kept green*, the assertion must show the wrong outcome being *accepted* (e.g. approve the
  wrong output, then assert a re-verify of that wrong output passes), never the wrong outcome being caught.
- **Comments inside displayed `// tag::` regions are part of the published deliverable and must be
  fidelity-checked against the code in the region, not just the prose.** The `approval-verify` comment
  ("two different instants") survived check_snippets (which checks resolution/length, not comment truth) and
  the EXAMPLE gate. Suggest the snippet checker (or code-review checklist) flag in-region comments that
  reference constructs (here, a second `Instant`) absent from the region.
- **The in-JVM-mechanism-for-runtime-gated-tool pattern reads honestly here** (Pact/REST-assured/Approvals
  realized in plain JDK+JUnit, named neutrally in README + javadoc + package-info, flagged in `09-flags/`):
  the framing is stated in code comments as the brief required, with no faked tool output and no invented
  GAV. Confirms the key-20/key-42 precedent; good to keep as the template for cited-not-built chapters.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh code-reviewer 4d 50 gate-run PASS-WITH-FIXES
```

---
**ORCHESTRATOR FIX — 2026-06-27 — F2 RESOLVED.** The displayed `approval-verify` region's
comment "Render at two different instants" contradicted the code (a single `render(..., Instant.now())`).
Reworded to "Render with a live timestamp; the scrubber normalizes it so it matches the approved
baseline." Comment-only; rebuilt green (12 tests). **F1 (the `rubberStampingAWrongBaselineHidesABug`
test asserts the opposite of its name — it should approve a wrong baseline then verify it passes)
is logged as a PRIORITY LIFT-PASS item** — a correct rewrite of the test's logic, done with the
chapter's approval-testing narrative, before this chapter is approved.
