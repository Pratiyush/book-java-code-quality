# GATE REPORT — CODE-REVIEW — Chapter 35 (key 81, folds 82)

## Header

- **Gate:** CODE-REVIEW (FLOOR C, second half) — technical profile
- **Chapter key:** 81 (owner; folds 82) — FINAL_INDEX Ch 35, Part IX
- **Slug:** `81_branch_protection_precommit_parity`
- **Draft under review:** `03-drafts/81_branch_protection_precommit_parity/81_branch_protection_precommit_parity_v1.md`
- **Module path:** `08-companion-code/81_branch_protection_precommit_parity/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Scripts run:** `check_snippets.sh` (5/5 PASS), `extract_snippet.sh` (per-tag balance + line-count), `mvn -B -Pquality verify` (fresh, JDK 21.0.11), `mvn -B -Dparity.profile=prod verify`, `ruby -ryaml` (both config files parse), banned-phrase grep over all module files, hardcoded-secret grep
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

This is exemplary, idiomatic Java-21 example code that a reader can paste with confidence: a fresh
`mvn -B -Pquality verify` is green and warning-clean (10 tests, 0 Checkstyle, 0 SpotBugs, no `[WARNING]`),
all five displayed `// tag::` regions are brace/element-balanced, ≤9 lines, and free of banned NEUTRALITY
words, and there is no security, neutrality, or invented-fact finding anywhere. **No BLOCKER.** One MINOR
prose-code-fidelity defect must be fixed before approval: a `pom.xml` comment points a reader at a config
file (`ruleset.json`) that does not exist (the real file is `ruleset.yml`). The two NOTE items are
observations, no action required. PASS-WITH-FIXES: the one MINOR fix is required before the next gate, not
optional.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness (logic, edge cases, no leaks, no swallowed exceptions, non-vacuous tests, real failure path) | **PASS** |
| 2 | Idiomatic Java 21 / code quality (records, sealed types, streams, DI/config idioms, lifecycle, no anti-patterns) | **PASS** |
| 3 | Security (no hardcoded secrets, input validated, no injection sink, no internal leak) | **PASS** |
| 4 | Simplicity & readability (smallest teaching size, no dead code/unused deps, realistic names, type purpose comments) | **PASS** |
| 5 | Prose↔code fidelity + originality/attribution (tags balanced/≤9; every atom traces; original-for-this-book) | **PASS-WITH-FIXES** (one MINOR: stale `ruleset.json` in pom comment) |
| 6 | Neutrality in code (no crowning/disparagement; zero banned phrasings in comments/identifiers/strings) | **PASS** |

### 1. Correctness — PASS
- The parity check is correct and one-directional exactly as the chapter argues: `check()` reports CI-required
  names with no local counterpart (`ci.checkNames().stream().filter(r -> !local.contains(r)).toList()`), so
  extra local checks never break parity. The three test cases (`inParityWhenLocalCoversCi`,
  `driftNamesTheMissingCheck`, `extraLocalChecksAreFine`) pin all three directions, and the assertions are
  substantive (`isInstanceOf`, `containsExactly`, `driftCount` deltas) — not vacuous.
- **The local↔CI parity assertion is genuinely tested**, which is the load-bearing requirement: `driftNamesTheMissingCheck`
  asserts the drift names the exact missing check (`containsExactly("static-and-security")`) and increments
  `driftCount()` to 1; `inParityWhenLocalCoversCi` asserts `driftCount()` is zero. The property is pinned by a
  test, not assumed.
- The **failure path is real and exercised**: `enforce()` under the strict (`prod`) policy throws
  `ParityBrokenException` naming the gap, and `prodFailsFastOnDrift` asserts the throw plus the message carries
  both missing names. `devProfileSurfacesDriftAsWarning` asserts the lenient profile returns `Drifted` instead
  of throwing — both branches of the policy fork are covered.
- No resource leaks: the only I/O is `ParityPolicy.load`, which reads the classpath properties in
  try-with-resources and rethrows `IOException` as `UncheckedIOException` (no swallow). A missing profile is a
  clear `IllegalArgumentException` (tested by `unknownProfileIsRejected`).
- Counters use `AtomicLong`; `check()` and `enforce()` are free of shared mutable state beyond those atomics, so
  the type is safe to share — appropriate for a metric surface.

### 2. Idiomatic Java 21 — PASS
- `ParityResult` is a **sealed interface** with `record` variants `InParity` / `Drifted` — the modern,
  exhaustive way to model a two-outcome result; the cast in `enforce()` is safe because the preceding
  `!result.inParity()` guard implies `Drifted` (the only non-in-parity permitted variant).
- `ParityPolicy` is a **record with a compact constructor** validating `localRunner`, plus a `%dev`/`%prod`
  externalized-config idiom selected by a system property — the framework-style config approach the chapter
  teaches, with nothing compiled in.
- `GateSet` is a correct immutable value (Effective Java Item 17, cited in its Javadoc): it defensively copies
  into a `LinkedHashSet`, null-checks each element, and exposes `Set.copyOf` — no representation exposure (which
  is why the SpotBugs filter is legitimately empty).
- `Objects.requireNonNull` guards every public entry point; `stream().toList()` (Java 16+) used idiomatically; no
  raw threads, no blocking, no `System.out` logging (the type is a library, so it returns/throws rather than
  logging — correct for example code a reader embeds).

### 3. Security — PASS
- Hardcoded-secret grep across `*.java/*.yml/*.yaml/*.xml/*.properties` returned **no credential literal**; the
  only hits for `secret` are the comment describing the secrets-scan hook (gitleaks) — not a secret.
- No injection sink, no user-facing endpoint, no reflection-from-input. `ParityBrokenException`'s message lists
  configured check names (not stack internals); error responses leak nothing.
- The `.pre-commit-config.yaml` adds a gitleaks secrets-scan hook and the prose/README correctly frame it as
  *feedback* with CI as enforcement — the security posture in the teaching artifact itself is sound.

### 4. Simplicity & readability — PASS
- Smallest code that teaches the point: 6 source types, each with a one-line purpose Javadoc, realistic
  `org.acme.parity` names (`GateParity`, `GateSet`, `ParityResult`, `ParityPolicy`, `ParityBrokenException`) —
  no `Foo`/`Bar`/`tmp`/placeholder packages. Zero runtime dependencies; test libs are the only GAVs and carry no
  version literal (inherited from the aggregator). No dead code, no gratuitous abstraction.
- The empty SpotBugs filter and curated Checkstyle ruleset are explicitly justified in-comment ("suppress with a
  reason, never disable a detector"; "curate a ruleset, don't enable everything") — the config dogfoods the
  book's own advice.

### 5. Prose↔code fidelity + originality — PASS-WITH-FIXES
- **All 5 displayed tag regions: balanced, ≤9 lines, clean** (see the table below). `check_snippets.sh` = 5/5 PASS.
- Every displayed atom traces: the four required-check names (`build-and-lint`, `test-and-coverage`,
  `static-and-security`, `quality-gate`) are this book's illustrative CI job names and are identical across
  `ruleset.yml`, the test fixture (`CI`), and the prose — the lock-step the chapter argues for is real, not
  claimed. SaaS surfaces (GitHub ruleset field names, the pre-commit `rev:`) are correctly dated-at-use (2026-06)
  in comments + README + draft, and flagged to `09-flags/81`, not asserted as pinned facts.
- **Originality (LEGAL-IP §5):** the `org.acme.parity` domain is original-for-this-book; the parity check is a
  bespoke abstraction, not an upstream quickstart. `.pre-commit-config.yaml` is documented config in the book's
  own shape, not a copied sample. No unattributed verbatim lift.
- **MINOR fidelity defect (Finding 1):** `pom.xml` line 14 tells the reader the ruleset lives at
  `config/branch-protection/ruleset.json`, but the file is `ruleset.yml` (no `.json` exists). The README,
  package-info, and draft all say `.yml`; only this one comment is stale and would send a copying reader to a
  non-existent file.

### 6. Neutrality in code — PASS
- Banned-phrase grep (`better than`, `unlike X`, `superior`, `beats`, `outperform`, `the problem with X`,
  `worse than`, `inferior`, `best-in-class`, `destroys`, …) over **all** module files (code, config, README):
  **zero matches.** Comments crown no model — `ruleset.yml` and package-info explicitly state "the book crowns
  no model" for branching strategy. No identifier, log string, or comment disparages a comparator.

---

## Tag-include regions (displayed snippets) — balance + bound

| Tag | File | Lines (≤9) | Balanced? | Banned words? |
|---|---|---|---|---|
| `#required-checks` | `config/branch-protection/ruleset.yml` | 7 | YES — complete YAML mapping (`required_status_checks:` + nested keys + 4 list items) | none |
| `#linear-history-and-review` | `config/branch-protection/ruleset.yml` | 6 | YES — complete YAML mappings | none |
| `#precommit-fast-hooks` | `.pre-commit-config.yaml` | 9 | YES — two complete hook list-items | none |
| `#parity-assertion` | `GateParity.java` | 8 | YES — stmt + complete `if {…}` + 2 stmts; braces 1:1 | none |
| `#feedback-not-enforcement` | `GateParity.java` | 5 | YES — one complete `if {…}` block; braces 1:1 | none |

No region is broken mid-statement; no banned NEUTRALITY word appears in any displayed region. **No BLOCKER on
the snippet axis.**

---

## Build / lint result (fresh run, this gate)

- **Command:** `mvn -B -Pquality -f 08-companion-code/81_branch_protection_precommit_parity/pom.xml clean verify`
  (JAVA_HOME = openjdk@21 → `openjdk version "21.0.11" 2026-04-21`, matches SOURCE-PIN §1).
- **Result:** `[INFO] BUILD SUCCESS` — `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`;
  `You have 0 Checkstyle violations.`; `BugInstance size is 0`; **no `[WARNING]`/`[ERROR]` lines** (warning-clean).
- **Default build** (no `-Pquality`) and **`-Dparity.profile=prod`** externalized-config selection both build green.
- **Both YAML config files parse** (`ruby -ryaml`): `ruleset.yml` valid; `.pre-commit-config.yaml` valid (2 repos).
- **Failure-path test present and asserting the real path:** `prodFailsFastOnDrift` (throws + names gap),
  `devProfileSurfacesDriftAsWarning` (returns Drifted). Integration coverage per public behavior is present
  (10 cases incl. null-guard and unknown-profile rejection).

---

## Findings

Severity: BLOCKER (gate cannot pass) · MAJOR (fix before approval) · MINOR (should fix) · NOTE (observation).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | pom.xml comment names a config file that does not exist (`ruleset.json`); the real file is `ruleset.yml`. A reader following the comment is sent to a missing path. | MINOR | `08-companion-code/81_branch_protection_precommit_parity/pom.xml:14` | Change `config/branch-protection/ruleset.json` to `config/branch-protection/ruleset.yml` in the comment (matches the README, package-info, and the EXAMPLE.md JSON→YAML decision). |
| 2 | Test helper `linked(String...)` wraps `Set.of(names)` in a `LinkedHashSet`, but `Set.of` has already discarded insertion order, so the fixture does not actually preserve the order the `GateSet` Javadoc promises. No test asserts multi-element drift order (`containsExactly` is on a single element), so this is latent only — `GateSet` itself does preserve order. | NOTE | `GateParityTest.java:29-31` | Optional: build the set directly from the varargs into a `LinkedHashSet` (skip the `Set.of` hop) if a future test asserts multi-element ordered drift. No action required for green/correctness today. |
| 3 | `checkstyle-fast` and `gitleaks` hooks omit `name:`; the snippet comment "name defaults to the id when omitted" is accurate for the pre-commit framework. Verified correct. | NOTE | `.pre-commit-config.yaml:30,38` | None — recorded as confirmation, not a defect. |

---

## Blockers

**None.** No security, neutrality, or invented-fact finding; no broken/over-length/banned-word snippet; build
green and warning-clean. FLOOR C (CODE-REVIEW half) is **not** blocked. The single MINOR fix (Finding 1) is
required before the next gate but does not gate FLOOR C.

- [x] No BLOCKER present.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic Java 21 / security / simplicity / prose-code-fidelity /
  neutrality-in-code all PASS (fidelity carries one MINOR fix).
- [x] `mvn -B -Pquality verify` re-run fresh: GREEN and **warning-clean** (0 `[WARNING]`).
- [x] ≥1 integration test per public behavior **including the failure path** (`enforce` throw under prod;
  `Drifted` return under dev).
- [x] Hardcoded-secret grep (password/secret/token/apikey literals): **no hit** (only the secrets-scan comment).
- [x] Every displayed `// tag::` region brace/element-balanced, ≤9 lines, free of banned NEUTRALITY words.
- [x] `ruleset.yml` + `.pre-commit-config.yaml` parse as valid YAML.
- [x] Originality (LEGAL-IP §5): all files original-for-this-book; no unattributed verbatim lift.

---

## Learnings & pipeline suggestions

- **A config-file comment in `pom.xml` is a prose-code-fidelity surface too.** The module's own doc comment
  pointed a copying reader at `ruleset.json` after the artifact was (correctly) authored as `ruleset.yml`. When
  the EXAMPLE-BUILD gate records a JSON→YAML decision (it did, finding #1 in the EXAMPLE report), a follow-up
  grep for the *old* extension across all build/doc comments would catch the stale pointer. Worth a one-line
  check in the example-builder's self-review: after renaming a config artifact, grep the module for the prior
  filename.
- **The parity check is a textbook fit for the failure-path requirement.** Turning "local must reproduce every
  required CI check" into a tested `ParityBrokenException` path is exactly the HONEST-LIMITATIONS-in-code the
  floor wants — a model other config-centric chapters (gate policy, release) can reuse rather than bolting a
  fault-tolerance annotation onto a topic that does not call for one.
- **`Set.of(...)` inside an order-preserving fixture is a quiet trap.** It compiles, stays green, and silently
  defeats an order guarantee the production type honors. A candidate note for EXAMPLES-GUIDE test-fixture
  guidance: build ordered fixtures straight into a `LinkedHashSet` from varargs, never via `Set.of`.
- Append to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log (final step — required)

```
.claude/scripts/log_action.sh code-reviewer 4b 81 gate-run PASS-WITH-FIXES
```

---
**ORCHESTRATOR FIX — 2026-06-27 — MINOR RESOLVED.** pom.xml comment referenced
`config/branch-protection/ruleset.json` but the file is `ruleset.yml`. Corrected. Rebuilt green.
