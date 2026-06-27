# GATE REPORT — CODE-REVIEW (FLOOR C, second half) — Chapter 32 (key 73)

## Header

- **Gate:** CODE-REVIEW (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW; this is the senior-PR-review half)
- **Chapter key:** 73 (single key, frozen from `01-index/CANDIDATE_POOL.md`) — FINAL_INDEX Ch 32 (CLOSES Part VIII)
- **Slug:** `73_security_in_ci`
- **Module path:** `08-companion-code/73_security_in_ci/`
- **Draft under review:** `03-drafts/73_security_in_ci/73_security_in_ci_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Verdict:** **PASS-WITH-FIXES** (no BLOCKER; 0 FAIL · 2 FIX · 5 NOTE)

---

## Verdict line

**PASS-WITH-FIXES — runnable, idiomatic, security-centred, prose-faithful; ships once the two minor FIX items are applied. No BLOCKER. No security / neutrality / invention finding. Floor-C CODE-REVIEW does not block.**

The module is the smallest code that still teaches the chapter's load-bearing decision: a `SecurityGate`
that pools every stage's `SecurityFinding`s and returns a three-way `SecurityGateDecision`
(`Pass`/`Review`/`Block`), scoped to new code, paired with an illustrative `ci/security-pipeline.yml`. It
is exemplary Java 21 (sealed verdict + records + enums + `Objects.requireNonNull` + a small stream
pipeline), holds zero hardcoded secrets, and the test class drives the real failure path. Every displayed
tag region is element-balanced (or a clean opening excerpt), at most nine lines, and free of banned
NEUTRALITY phrasing.

---

## The six review dimensions

| # | Dimension | Result | One-line basis |
|---|---|---|---|
| 1 | Correctness | **PASS** | `evaluate` filters-by-scope then picks the worst in-scope blocking finding via `max(comparing(severity))`; `Objects.requireNonNull` on every entry point; no leaks; the test asserts each branch (block / review / pass) with real logic, not vacuous checks. The failure path (`Block`) is exercised by 5 tests incl. multi-stage aggregation and prod fail-closed. |
| 2 | Idiomatic Java 21 | **PASS** | Sealed interface of records for the verdict, records for the value types, `EnumSet`/`Comparator`/`Stream.toList()`, an immutable-finding compact constructor, `AtomicLong` counters, externalized `%dev`/`%prod`-style config. No raw threads, no `System.out`, no anti-pattern. |
| 3 | Security | **PASS** | No hardcoded secret/password/token/key anywhere (literal-assignment + `PRIVATE KEY` scans clean; the only `secret`/`leaked-key` tokens are domain vocabulary — the secrets-scanning stage and a fixture ruleId). No injection sink, no internal/stack-trace leak in the verdict reasons (they name stage + ruleId only). The subject *is* a security gate and the security model is the centre of the design. |
| 4 | Simplicity & readability | **PASS** | Every public type carries a purpose Javadoc; realistic domain names (no `Foo`/`tmp`); no dead code (every enum constant is referenced — `EXISTING`, `IAST`, `CONTAINER_IAC` in tests; `INFO` documented in the properties); empty SpotBugs filter kept deliberately with a reason. |
| 5 | Prose↔code fidelity + originality | **PASS** | `check_snippets.sh` 7/7 PASS; all 7 displayed regions ≤9 lines; prose claims match the code (the exploitability "unreachable sink / non-security weak primitive" examples are identical in prose and comments; "fails above CVSS 7.0" is OWASP Dependency-Check's documented default, also used by peer module 65; Trivy 0.71.0 / Dependency-Check 12.2.2 trace to SOURCE-PIN §4; SaaS/unpinned tools dated-at-use + flagged). LEGAL-IP §5: original-for-this-book, confirmed file-by-file by the example-builder and re-confirmed here — not a reskinned upstream starter. |
| 6 | Neutrality in code | **PASS** | Banned-phrase scan over all `.java`/`.yml`/`.properties`/`.xml`: **NONE**. "gate fatigue is the killer" is self-referential (the failure mode kills the gate), not a comparator crowning; no stage or tool is crowned over another (the comments explicitly say "No stage is crowned"). |

---

## Displayed tag regions — balance + cap + neutrality (the BLOCKER-class check)

All 7 prose-displayed regions verified by extraction (cap = 9):

| Tag | File | Lines | Balance | Banned word | Verdict |
|---|---|---|---|---|---|
| `pre-commit-secrets` | `ci/security-pipeline.yml` | 2 | YAML steps, well-formed | none | OK |
| `pr-fast-trio` | `ci/security-pipeline.yml` | 4 | YAML steps, well-formed | none | OK |
| `container-iac-scan` | `ci/security-pipeline.yml` | 2 | YAML steps, well-formed | none | OK |
| `staging-dynamic` | `ci/security-pipeline.yml` | 2 | YAML steps, well-formed | none | OK |
| `gate-policy` | `SecurityGatePolicy.java` | 6 | `{`=1 `}`=0 — **clean opening excerpt** (record header + 2 constants; natural stop) | none | OK |
| `block-vs-warn` | `SecurityGateDecision.java` | 6 | `{`=4 `}`=4, `(`=3 `)`=3 — balanced | none | OK |
| `aggregate-and-gate` | `SecurityGate.java` | 8 | `{`=0 `}`=0, `(`=16 `)`=16 — balanced statement block | none | OK |

`gate-policy` ends mid-record-body, but the `end::` lands right after the second constant declaration, so
the displayed excerpt reads as a coherent opening (header + profile-selection constants) rather than a
statement broken mid-clause — the task's allowed "clean opening excerpt" case. **No BLOCKER.**

---

## Build / lint result

- **Build:** GREEN at the pin. Independently corroborated by the committed `target/spotbugsXml.xml`:
  `total_bugs='0'`, `errors='0'`, `missingClasses='0'`, 11 classes / `java_version='21.0.11'`,
  matching the EXAMPLE.md record (14 tests, 0 Checkstyle, 0 SpotBugs, `BUILD SUCCESS`). *(This sandbox has
  no JDK installed — `/usr/libexec/java_home` finds none — so `mvn` could not be re-run here; the green
  state is taken from the example-builder's `mvn -B -Pquality verify` evidence plus the on-disk SpotBugs
  report. The reviewer recommends a confirming re-run on a JDK-21 host, but no static evidence contradicts
  the green claim.)*
- **Strict warnings:** the aggregator compiles with `-Xlint:all,-processing` (warnings surfaced). It does
  **not** set `-Werror` / `failOnWarning`, so a warning would not fail the build — the EXAMPLE.md asserts
  the run was warning-clean; nothing in the source would plausibly emit one (no raw types, no deprecation,
  no unchecked use). No warning to report.
- **Snippets:** `check_snippets.sh` → 7 marker(s); 7 pass, 0 fail. The 8th `<!-- include:` literal in the
  draft is inside the "Snippet tags:" prose summary, not a marker.
- **Secret scan:** literal credential-assignment scan + `PRIVATE KEY` scan → NONE.
- **Module registration:** already registered as `<module>73_security_in_ci</module>` at line 51 of
  `08-companion-code/pom.xml` (the EXAMPLE.md's "not yet registered" note is now stale — it has been added).

---

## Findings

| # | Severity | File:line | Issue | Fix (for the example-builder; reviewer does not edit) |
|---|---|---|---|---|
| 1 | FIX | `SecurityGate.java:111-115` (`stagesReporting`) | `findings.forEach(f -> reporting.add(f.stage()))` dereferences each element and would NPE on a `null` element of the list, where the rest of the class guards inputs with `Objects.requireNonNull`. Low real risk (the gate consumes its own findings; `EnumSet.add(null)` also rejects null), but inconsistent with the class's own defensive contract that readers will copy. | Either document that elements are assumed non-null in the Javadoc, or filter/guard: `findings.stream().map(SecurityFinding::stage).forEach(reporting::add)` reads the same and keeps the NPE-on-null-element behavior explicit. Cosmetic; not a blocker. |
| 2 | FIX | `73_security_in_ci_v1.md:89-91` (`gate-policy` lead-in) | The lead-in says the policy "is externalized per profile rather than compiled in, so the feature-branch gate and the release gate can differ," then displays the record header + the `PROFILE_PROPERTY`/`DEFAULT_PROFILE` constants — which show *where* the profile is selected but not the `load()` externalization the sentence promises. The pairing is defensible (the constant is the externalization hook, and the Javadoc above it explains the loading) but a cold reader may expect the `load()` body. | Optional: tighten the lead-in to "the policy record and the system property that selects its profile" OR (preferred, if a tag budget allows) point the reader to `SecurityGatePolicy.load` for the loading itself. Fidelity is accurate as-is; this is a clarity nicety, not a mismatch. |
| 3 | NOTE | `SecurityGate.java:64-87` | `evaluate` mutates two `AtomicLong` counters (`evaluations`, `blocks`) as a side effect of a query. Intentional and documented (the dashboard metric the chapter argues for), thread-safe via atomics, and tested (`blockedCount`). Acceptable for the teaching point; flagged only so a future reader does not mistake it for an accidental side effect. | None required. |
| 4 | NOTE | `ci/security-pipeline.yml` (`on:` key) | Psych/YAML-1.1 parses a bare `on:` key as boolean `true`; GitHub Actions' own parser reads it correctly as the string key. Display-dialect artifact, not a defect (already explained in EXAMPLE.md). | None required; standard for Actions workflows. |
| 5 | NOTE | `ci/security-pipeline.yml:46,64,65,...` (`@v4` actions; gitleaks; `zap-baseline.py`) | SaaS/unpinned tool steps pinned only to mutable `@v4` major tags / no version (gitleaks, OWASP ZAP). Correctly presented as dated-at-use 2026-06 with inline comments and flagged to `09-flags/73_security_pipeline_saas_dated_at_use.md`. Not invention. | At public-push sign-off: pin actions to a commit digest and decide gitleaks/ZAP SOURCE-PIN rows. Already tracked. |
| 6 | NOTE | `ci/security-pipeline.yml` (`staging-dynamic` job) | DAST-against-staging is REPRO PENDING-RUNTIME (needs a live deployment); wired as illustrative config, not executed by the build. Honest and labelled. | None required; recorded. |
| 7 | NOTE | `SecurityGate.java:73` (displayed) | The filter `f.exploitable() || !policy.requireExploitableToBlock()` is the load-bearing fail-closed/route-to-review switch; the inline comment "exploitable, else review" is slightly terse for the prod (`requireExploitableToBlock=false`) path where a non-exploitable HIGH *blocks*. Correct (and tested by `prodProfileFailsClosedOnNewHigh`), just dense. | Optional: no change needed — the Javadoc and the prod properties file both explain the fail-closed path. |

---

## Blockers

**None.** No FAIL, no security finding, no neutrality finding, no invented atom, no broken-mid-statement or
banned-word displayed region. Floor-C CODE-REVIEW does not block FLOOR C.

---

## Learnings & pipeline suggestions

- **The "clean opening excerpt" rule earns its keep on record types.** `gate-policy` deliberately stops
  after the record header + its two constants (`{`=1 `}`=0). That is the *right* call for teaching — it
  shows the policy's shape and its profile hook without dragging the `load()` plumbing into a 9-line
  budget — but it does leave a brace formally unbalanced, which a naive balance check would flag. Suggest
  the snippet linter treat a region whose only imbalance is one trailing-open `{` on a type/method header
  as a sanctioned "opening excerpt," distinct from a mid-statement break (which must FAIL).
- **A query-with-a-side-effect counter is the cleanest way to make "gate fatigue is measurable" runnable.**
  `evaluate` bumping `blocks`/`evaluations` so `blockedCount()` can be trended is a small, thread-safe,
  tested deviation from CQS that carries a real chapter thesis (a block rate stuck at 0 or 100% means the
  policy needs tuning). Worth promoting as a sanctioned pattern for the observability-as-quality chapters
  rather than something a future reviewer re-litigates.
- **Re-confirm a stale EXAMPLE.md registration note against the live reactor.** The EXAMPLE.md still says
  the module is "NOT registered in `<modules>`," but it is now at line 51. Low-stakes here, but the
  reporting-discipline rule means a gate report should read the live pom, not trust a prior gate's snapshot.
- **Carry the no-JDK-in-sandbox caveat forward.** CODE-REVIEW could verify everything statically (balance,
  caps, secrets, neutrality, fidelity, and the on-disk SpotBugs report) but could not itself re-run `mvn`.
  Suggest the pipeline either provision a JDK-21 toolchain for the code-reviewer step or formally accept the
  example-builder's green build + committed `spotbugsXml.xml` as the build evidence for this gate.
