# GATE REPORT — CODE-REVIEW (FLOOR C, second half)

## Header

- **Gate:** CODE-REVIEW
- **Chapter key:** 70 (folds 71) — FINAL_INDEX Chapter 31, Part VIII (Security & SAST)
- **Slug:** `70_sast_secrets_detection`
- **Module:** `08-companion-code/70_sast_secrets_detection/`
- **Draft under review:** `03-drafts/70_sast_secrets_detection/70_sast_secrets_detection_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer (senior-PR pass) + Bash validation
- **Scripts run:** none scripted; manual `grep`/`awk` region extraction, secret scan, brace/line-count audit, banned-phrase scan, on-disk build-evidence inspection
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is correct, idiomatic Java 21, security-clean, simple, and faithful to the prose. The
security core the chapter rests on is sound: the SQL sink/fix pair is real, the SpotBugs suppression is
**load-bearing** (post-filter `BugInstance` count = 0, with the one named counter-example finding
`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` suppressed narrowly with a reason and a test pointer), the
secrets resolver fails closed with no baked-in fallback, and the planted AWS key is the **published
non-functional EXAMPLE key `AKIAIOSFODNN7EXAMPLE`** in all three places it appears (test fixture,
gitleaks allowlist, README) — no real-looking secret anywhere. All six displayed `// tag::` regions are
brace/paren-balanced, ≤9 lines, clean excerpts, and free of banned NEUTRALITY phrasings.

The single non-trivial finding is a **source-trace** one the project has already flagged itself: the
companion-code comments assert the OWASP Top 10:**2025** ordinal as `A03` flat (including inside the
DISPLAYED `semgrep-rule` snippet), while `09-flags/70_sast_secrets_tools_saas_dated_at_use.md` (lines
52–60) records that ordinal as ⚠ verify-at-pin and explicitly asks for the code comments to be corrected.
CWE-89 itself is stable/verified. This is a MAJOR fix, not a blocker, and it is the only required fix
before approval. No security, neutrality, broken-snippet, or invented-API finding is present.

**No BLOCKER.** (Build re-run could not be performed — no JDK on this host; verdict relies on the
on-disk build evidence, which corroborates the green claim: see Build/lint section.)

---

## Six review dimensions

| Dimension | Result | Note |
|---|---|---|
| 1. Correctness | **PASS** | Sink/fix logic correct; try-with-resources on every JDBC resource (no leaks); the readiness `catch` is a deliberate fail-closed→boolean, not swallowed; tests assert real behavior incl. the failure path (missing + blank secret, refused lookup, `lastSql()==null` before touching data). |
| 2. Idiomatic Java 21 | **PASS** | `switch` expressions with arrow/`yield` in the `Proxy` test double; `UnaryOperator<String>` for env injection (clean seam); `Objects.requireNonNull` guards; `AtomicLong` for the counter; named catch variable. No raw threads, no blocking-where-forbidden. |
| 3. Security | **PASS** | No hardcoded secret literals (full-tree scan clean). AWS key = published non-functional EXAMPLE key only. Fail-closed resolver refuses default/empty credential. Parameterized fix removes the injection class; exception message leaks only the *variable name*, not a value/stack trace. Load-bearing suppression is narrow (class+method+pattern), never a detector disable. |
| 4. Simplicity & readability | **PASS** | Smallest code that teaches the point; JDK-only, driver-free, DB-free; every public type carries a one-line purpose Javadoc; realistic storefront names (no Foo/Bar/tmp). |
| 5. Prose↔code fidelity | **FIX** | 6/6 displayed regions resolve, are ≤9 lines, brace-balanced, clean excerpts. CWE-89, the SpotBugs pattern name, GAV inheritance, and the fail-closed/health surfaces all match the prose. **Exception:** the OWASP `2025 A03` ordinal is asserted in code (incl. a displayed snippet) but is verify-at-pin per 09-flags (Finding 1). Minor: code says "key 84" where prose mixes "key 84"/"Chapter 84" (Finding 2). |
| 6. Neutrality in code | **PASS** | No banned phrasing in any file (code, comments, configs, README). No SAST/secrets tool crowned — each cited to its own docs; checkstyle config even states "this team's cited choice, not a universal truth." |

---

## Findings

Severity: **BLOCKER** / **MAJOR** / **MINOR** / **NOTE**

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | OWASP Top 10:**2025** ordinal asserted flat as `A03`, but it is ⚠ verify-at-pin (2025 reshuffled categories from 2021; SOURCE-PIN OWASP row names only A01=Broken Access Control for 2025). Appears in a **displayed** snippet, so it reaches printed book text. CWE-89 itself is verified. The project's own flag note already calls for the code comments to be corrected. | **MAJOR** (source-trace) | `config/semgrep/java-injection.yml:19` (DISPLAYED `semgrep-rule` region: "OWASP A03 / CWE-89"); `VulnerableOrderLookup.java:15`; `OrderLookup.java:14`; `config/spotbugs/spotbugs-exclude.xml:24`; cf. `09-flags/70_sast_secrets_tools_saas_dated_at_use.md:52-60` | Replace `A03` with the verify-at-pin-safe form the prose already uses — e.g. "OWASP Top 10:2025 Injection category (CWE-89)" — in all four code/config sites, OR confirm the 2025 Injection ordinal against `owasp.org/Top10/2025` at SOURCE-VERIFY and only then keep a number. CWE-89 stays as-is. |
| 2 | Code comments cite "key 84" for design review; draft prose mixes "key 84" (metadata) and "Chapter 84" (prose body). Internal label inconsistency, not a wrong fact. | **MINOR** (fidelity) | `package-info.java:29`, `README.md:70` ("key 84") vs draft `v1.md` lines 105/112/126/138 ("Chapter 84") | Pick one form across code + prose for the design-review chapter reference (reconcile at VERIFY/RECONCILE). Code-side change optional once prose settles. |
| 3 | `config/checkstyle/checkstyle.xml` cites "Chapter 6 (style-value neutrality)"; the draft never references Chapter 6. File is **not** a displayed region, so no printed-text impact. | **NOTE** | `config/checkstyle/checkstyle.xml:11,25` | Confirm Chapter 6 is the conventions/style chapter in FINAL_INDEX; the reference is plausible and harmless. No action required for this gate. |
| 4 | `target/surefire-reports/*.txt` per-class summary line reads "Tests run: 0" (nested-container aggregate artifact); the authoritative JUnit XML reads `tests="9"` with 9 `<testcase>` elements, matching 9 `@Test` methods in source. No defect — recorded so a future reader of the `.txt` is not misled. | **NOTE** | `target/surefire-reports/org.acme.sast.SastSecretsTest.txt` vs `TEST-...xml` | None. The "9 tests" claim is corroborated by the XML and the source. |

---

## Blockers

**None.**

No security finding, no NEUTRALITY finding, no broken/over-length displayed snippet, no invented API
or GAV coordinate. The AWS key is the published non-functional EXAMPLE key in every occurrence (the
"real-looking secret" blocker condition is NOT met). Finding 1 is a source-trace MAJOR (already
self-flagged as verify-at-pin), required before approval but not gate-fatal.

---

## Build / lint result (re-run not possible here — on-disk evidence inspected)

No JDK is installed on this review host (`java -version` fails; `/usr/libexec/java_home` finds none), so
`./mvnw -B verify` could not be re-executed. There is no `mvnw` wrapper under `08-companion-code/`
(system `mvn` only). The verdict therefore relies on the committed build evidence, which is internally
consistent and **newer than the sources** (build artifacts 2026-06-27 10:15; sources 2026-06-26 23:31),
so it reflects the current code:

- **Tests:** `TEST-org.acme.sast.SastSecretsTest.xml` → `tests="9"`, 9 `<testcase>`, 0 failures/errors;
  source has 9 `@Test` across 4 `@Nested` groups (Injection / Secrets / Gate / PlantedSecretFixture),
  including the failure paths (missing secret, blank secret, refused lookup). Integration-per-behavior
  incl. failure path: satisfied.
- **Checkstyle:** `target/checkstyle-result.xml` present, `<error>` count = 0.
- **SpotBugs:** `target/spotbugsXml.xml` post-filter `BugInstance` count = 0 — i.e. the lone
  `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` finding on `VulnerableOrderLookup.findOrdersByEmail` is
  fully accounted for by the narrow suppression; detectors otherwise enabled (`effort=Max`,
  `threshold=Medium`, `includeTests=false`). The suppression is therefore load-bearing as documented.
- **Warning-clean:** no warning evidence on disk; `-Pquality` (Checkstyle@3.6.0 + checkstyle engine
  10.26.1, SpotBugs@4.9.3.0) clean per the result files. The draft header records the green run
  (JDK 21.0.11; `mvn -B -Pquality verify`: 9 tests, 0 Checkstyle, 0 SpotBugs).
- **Reactor wiring:** module is `<module>70_sast_secrets_detection</module>` in the aggregator pom;
  carries no version literal for test deps (junit-jupiter/assertj inherited from aggregator
  `dependencyManagement`); only build-plugin + parent versions are local.
- **Secret scan:** full-tree literal scan (password/secret/token/apikey/private-key patterns) = no hits;
  AWS-key-shaped scan = only `AKIAIOSFODNN7EXAMPLE` in README, `.gitleaks.toml` allowlist, and the test
  fixture; no high-entropy literals in `src/main`.

> ACTION for example-builder: re-run `mvn -B -Pquality verify` once on a JDK-21 host after applying
> Finding 1 to confirm green + warning-clean (expected: unchanged, since the OWASP-ordinal fix is
> comment-only). The gate's green verdict on the build is currently evidence-based, not freshly re-run.

---

## Displayed tag-region audit (the CRITICAL check)

All six regions the draft includes — measured by extracting between `tag::`/`end::`:

| Region | File | Displayed lines (≤9) | Braces {} | Parens () | Excerpt clean? |
|---|---|---|---|---|---|
| `sql-sink` | `VulnerableOrderLookup.java` | 8 | 2/2 | 5/5 | complete method |
| `semgrep-rule` | `config/semgrep/java-injection.yml` | 8 | 0/0 | 3/3 | complete rule block |
| `sast-ci-step` | `ci/sast-scan.yml` | 6 | 0/0 | 3/3 | complete step pair |
| `gitleaks-config` | `.gitleaks.toml` | 9 | 1/1 | 1/1 | complete rule+allowlist |
| `precommit-hook` | `.pre-commit-config.yaml` | 4 | 0/0 | 0/0 | complete repo block |
| `fail-closed` | `SecretsResolver.java` | 7 | 1/1 | 5/5 | clean mid-method excerpt (opens `String value=…`, closes `return value;`, inner if-block balanced) |

No region is broken mid-statement; none exceeds 9 lines; none contains a banned NEUTRALITY word. The
only printed-text concern is the `semgrep-rule` region's "OWASP A03" string (Finding 1).

---

## Learnings & pipeline suggestions

- **An unverified ordinal can hide inside a displayed config snippet.** The prose was correctly softened
  to "Injection category (exact A0x ordinal verify-at-pin)," but the same atom lived on, asserted flat,
  inside a `// tag::` region that the book prints — so the hedge in prose was silently undone by the
  snippet. Suggest the VERIFY/snippet checks scan *displayed regions* for ahead-of-pin atoms (CWE/OWASP
  ordinals, version numbers), not just prose body, since a tag region is published verbatim.
- **`surefire .txt` summary "Tests run: 0" for an all-`@Nested` class is a false alarm.** The JUnit XML
  (`tests="N"`) is the authoritative count for nested-container suites; a gate that reads the `.txt`
  line would misreport. Worth noting in the example-builder runbook.
- **Load-bearing-suppression verification is exemplary here** (filter-empty=1, filter-applied=0,
  deterministic, with a test pointer): a good template for any future teaching counter-example module.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 70 gate-run PASS-WITH-FIXES
```

---
**ORCHESTRATOR FIX — 2026-06-27 — MAJOR (source-trace) RESOLVED.** The unverified
"OWASP Top 10:2025 A03" ordinal was asserted flat in 4 source files including the
displayed `semgrep-rule` snippet (java-injection.yml:19), VulnerableOrderLookup.java,
OrderLookup.java, and spotbugs-exclude.xml. Per 09-flags/70 the 2025 ordinal is verify-
at-pin (2025 reshuffled categories; only CWE-89 + the "Injection" category name are
verified). Replaced "A03" with the verified category name ("OWASP Top 10:2025 Injection,
CWE-89") in all 4 source files. Rebuilt green (0 Checkstyle, 9 tests); 0 "A03" left in
source; check_snippets 6/6. **Verdict → PASS** (the source-trace major resolved; the
remaining items are NOTEs).
