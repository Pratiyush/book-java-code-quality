# GATE REPORT — EXAMPLE-BUILD — Chapter 31 (key 70)

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 70 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 71) — FINAL_INDEX Ch 31 (Part VIII)
- **Slug:** `70_sast_secrets_detection`
- **Draft under review:** `03-drafts/70_sast_secrets_detection/70_sast_secrets_detection_v1.md`
- **Module path:** `08-companion-code/70_sast_secrets_detection/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×6), `check_snippets.sh`; Ruby/Psych YAML parse (×3); Python `tomllib` TOML parse (×1); build via Maven `verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned Java 21 toolchain with the static-analysis gate on (`-Pquality`):
9 tests pass, 0 Checkstyle violations, 0 SpotBugs findings. This is a CONFIG-centric chapter (the peer
shape of `75_ci_pipeline_quality_gates`), so the module ships the SAST/secrets configuration the chapter
teaches — a Semgrep rule, a SAST CI workflow, a gitleaks config, and a pre-commit hook — kept in lock-step
with the prose, plus a runnable, unit-tested core for the two load-bearing decisions: parameterize the
injection sink and keep credentials out of source (fail-closed). All 6 displayed snippets resolve to tag
regions of at most nine lines inside compiling/parsing files, and all 6 prose markers bind
(`check_snippets.sh`: 6/6 PASS). The SpotBugs finding on the injection counter-example is verified
load-bearing (a true positive, not a decorative suppression). Every fact traces to the pin or the dossier;
the unpinned atoms are SaaS/rolling/unpinned SAST and secrets tools, presented as dated-at-use and flagged
to `09-flags/`, not invented. Both Floor-C preconditions hold.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; `Apache Maven 3.9.16` matches the pin | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (clean verify, quality profile) — see exact line below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/70_sast_secrets_detection/pom.xml clean verify
→ Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0 / No errors/warnings found
→ BUILD SUCCESS   (Total time: 3.106 s)
```

(`./mvnw -B verify` is the canonical floor command; this reactor uses a system `mvn 3.9.16` that matches
the pinned Maven version — there is no committed wrapper at the companion-code root. The quality profile is
opt-in `-Pquality`, mirroring the peer modules; with the profile off the build is also green. Per the task
constraint the module is built standalone via `-f <module>/pom.xml`; the root `08-companion-code/pom.xml`
`<modules>` list was NOT edited — see Findings #2 / the register-last note.)

**Load-bearing-suppression check (the SAST finding is a true positive, not decorative):**

```
# spotbugs-exclude.xml emptied → SpotBugs raises the finding, build fails:
→ BugInstance size is 1
→ High: org.acme.sast.VulnerableOrderLookup.findOrdersByEmail(String) passes a nonconstant String to an
        execute or addBatch method on an SQL statement … SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE  (line 46)
→ BUILD FAILURE
# narrow per-class/per-method/per-bug filter restored → 0, build green:
→ BugInstance size is 0 / BUILD SUCCESS
```

The injection counter-example raises exactly the sink finding the chapter's SAST rule targets; the
suppression is narrow (class + method + bug pattern), carries a reason and a test pointer, and the
detectors stay fully enabled for every other class (Chapter 16/19).

**Config parses (the tool config is illustrative, validated separately from the Maven build):**

```
ruby -e "require 'yaml'; YAML.load_file(...)"   → OK ×3:
  ci/sast-scan.yml                  (keys: name, on, permissions, jobs)
  config/semgrep/java-injection.yml (key:  rules)
  .pre-commit-config.yaml           (key:  repos)
python3 -c "import tomllib; tomllib.load(...)"  → OK ×1:
  .gitleaks.toml                    (keys: title, rules[aws-access-key-id], allowlist)
```

(Ruby/Psych is the available YAML 1.1 parser; Python `yaml` is not installed. Psych surfaces the
well-known YAML-1.1 quirk that a bare `on:` key parses as boolean `true` — GitHub Actions' own parser reads
it as the string key, which is correct and standard for workflows. The file is valid; this is a
parser-dialect display artifact, not an error. The same artifact is documented in the peer 75 report.)

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `sql-sink` | `src/main/java/org/acme/sast/VulnerableOrderLookup.java` | 8 | "SAST", after the tools paragraph — the concatenated sink a scan flags |
| `semgrep-rule` | `config/semgrep/java-injection.yml` | 8 | "SAST", after the sink — the custom Semgrep rule |
| `sast-ci-step` | `ci/sast-scan.yml` | 6 | "SAST", before the three-lenses CONCEPT — the scan in CI |
| `gitleaks-config` | `.gitleaks.toml` | 9 | "Secrets detection", after the ladder CONCEPT — the scanner config |
| `precommit-hook` | `.pre-commit-config.yaml` | 4 | "Secrets detection", after the gitleaks config — the pre-commit hook |
| `fail-closed` | `src/main/java/org/acme/sast/SecretsResolver.java` | 7 | "Secrets detection", after the prevention paragraph — the externalized-config failure path |

`check_snippets.sh 03-drafts/70_sast_secrets_detection/70_sast_secrets_detection_v1.md` → **6 marker(s);
6 pass, 0 fail.** Each prose marker carries a one-line lead-in in the locked voice; **no prose was
deleted**. A "Snippet tags:" line was added to the foot-of-chapter companion spec, and the spec's
EXAMPLE-BUILD status line was updated from PENDING to BUILT GREEN. The displayed listing and the
runnable/parsing file are one artifact (the prose shows a tag-region inside the full file; the file holds
the enterprise context around it).

> Snippet-fit note: the 4–7-tag budget landed at 6 (4 config + 2 Java), the same CONFIG-centric balance as
> peer 75. YAML tag regions use `# tag::name[]` / `# end::name[]` (hash comments) and the `.gitleaks.toml`
> region uses `# tag::name[]` TOML comments; `extract_snippet.sh` resolves all of them identically to the
> `//` Java markers. The `.toml` extension has no language mapping in `extract_snippet.sh`, so the gitleaks
> region renders as a plain fenced block (acceptable; the region resolves and is within cap at exactly 9).

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit/AssertJ inherited from the aggregator `dependencyManagement`; **zero** version literals in this module — zero runtime dependencies (JDK-only). Tool engines (Checkstyle 10.26.1, SpotBugs 4.9.3.0) pinned in the module's own `quality` profile, the peer-module shape |
| Externalized config profiles | `application-dev.properties` / `application-prod.properties` externalize the credential variable name (`%dev`/`%prod`), selected by `-Dsecrets.profile`; the static-analysis rulesets are externalized to `config/`; the quality gate is the opt-in `-Pquality` profile |
| At least one integration test | `SastSecretsTest` — 9 tests across 4 nested groups driving the whole stack: the injection sink vs the fix, externalized resolution + fail-closed, the gate's running/failure/health paths, and the planted-fixture pattern match |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`; `@Nested` groups; a fresh gate/resolver per case. No spurious logging observed in the run |
| Observability / health surface | `SecretsResolver.isReady()` / `missingSecretCount()` and `SastSecretsGate.isReady()` (readiness probe over the resolved credential) / `refusedLookupCount()` (running count of refused lookups — the metric a dashboard trends) |
| Explicit failure path | Fail-closed: `SecretsResolver.resolve()` throws `MissingSecretException` when the credential is absent or blank (no baked-in fallback), and `SastSecretsGate.findOrders` refuses before touching data. Proven by `missingSecretFailsClosed`, `blankSecretFailsClosed`, `gateRefusesLookupWhenSecretMissing` |

The SAST/secrets config additionally demonstrates the chapter's pipeline-level surfaces: the three layered
lenses (SAST/SCA/secrets), the defense-in-depth ladder (pre-commit → CI), and the custom-rule pattern —
illustrative config, not run by the build, each tool cited to its own docs and crowned none.

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** The fixed figure plan for this chapter is **2 designed diagrams + 0 captured
screenshots**: the draft references `Fig 70.1` (SAST/SCA/DAST — three lenses) and `Fig 71.1` (the secrets
ladder), both already authored separately as HTML→PNG with source sidecars at
`05-figures/70_sast_secrets_detection/fig70_1.{html,png,sources.md}` and `fig71_1.{html,png,sources.md}`
(not this agent's job). There is **no live subject-native UI surface** to capture from this module: the
runnable artifact is a JDK-only library (lookup + secrets-resolver logic — no dev console, API explorer, or
health view), and the SAST/secrets tools' only native UIs (a code-scanning findings view, a CI run view)
require live remote scans/CI runs, which the dossier records as scanner/network-gated → **REPRO
PENDING-RUNTIME**. A tool-UI screenshot is therefore an editorial/figure-stage item (it needs a live scan),
not a live capture from this green build, and is not invented here.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book.

- The Java domain (`OrderLookup` / `VulnerableOrderLookup` / `SecretsResolver` / `SastSecretsGate` /
  `MissingSecretException` over the book's `org.acme.sast` namespace) is the book's own storefront demo,
  modelled on the peer security module (`69_secure_coding_owasp`) but written for the *order* domain
  (distinct from 69's *customer* domain), not an upstream sample.
- `FakeConnection` is the book's own `java.sql` test double, the established peer-module scaffolding shape
  (modelled on `69_secure_coding_owasp/.../FakeConnection.java`), adapted for the order lookup — the book's
  own pattern reused, not an upstream sample.
- The pom, Checkstyle, and SpotBugs configs follow the book's own established house shape (the peer
  modules), not an upstream template.
- `config/semgrep/java-injection.yml`, `.gitleaks.toml`, `.pre-commit-config.yaml`, and `ci/sast-scan.yml`
  are **authored for this chapter**: the Semgrep rule id (`java-sql-string-concat`) is the book's own
  custom-rule id (NOT an upstream registry pack id), the gitleaks rule/allowlist are written for this
  module's fixtures, the pre-commit `rev` is a dated-at-use placeholder, and the CI workflow's stage
  decomposition and comments teach this chapter's lenses. **No upstream sample `.yml`/`.toml`, getting-started
  skeleton, registry pack, or `actions/*-starter` workflow was copied and renamed.**
- The tool config schemas (Semgrep `rules:`/`patterns:`, gitleaks `[[rules]]`/`[allowlist]`, pre-commit
  `repos:`/`rev:`/`hooks:`, GitHub Actions step syntax) are platform vocabulary, not copied prose. The AWS
  example key id `AKIAIOSFODNN7EXAMPLE` is AWS's published documentation placeholder, used as a fixture and
  allow-listed; it is not a real credential and is not copyrightable prose.

Nothing is taken substantially verbatim from a specific source file, so no in-file attribution is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| Java 21 runtime anchor; `maven.compiler.release=21` | SOURCE-PIN runtime baseline (JDK 21.0.11), inherited from aggregator |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, checkstyle-plugin 3.6.0, spotbugs-plugin 4.9.3.0, surefire 3.5.6, AssertJ 3.27.7, JUnit 6.x | SOURCE-PIN §2/§3; the house module shape (peer modules) |
| `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (the SpotBugs bug pattern raised + suppressed) | core SpotBugs 4.9.3.0 detector, verified by a real run of this module (BugInstance size 1 → 0); the same shape suppressed in peer `69_secure_coding_owasp` |
| `PreparedStatement` bound-parameter fix vs `Statement.executeQuery(String)` concat sink; `java.sql` API; `Objects.requireNonNull`, `AtomicLong`, `UnaryOperator`, `Pattern` | JDK 21.0.11 API + language (records/sealed not needed here); standard library usage |
| Injection = OWASP Top 10:2025 A03 / CWE-89; SAST source→sink / pattern-vs-taint; SAST/SCA/DAST three lenses; secrets = pattern + entropy + verify; defense-in-depth ladder (pre-commit→CI→history→push protection); committed secret = compromised → ROTATE; detection ≠ remediation; SAST misses design/authz | Dossier (keys 70/71, verified 2026-06-20) — mechanism verified; the chapter's own verified body. OWASP Top 10:2025 is a pinned SOURCE-PIN row (§1) |
| Semgrep (custom rules; `rules:`/`patterns:` schema); CodeQL (deep taint, GitHub, bundle); gitleaks (`[[rules]]`/`[allowlist]`); TruffleHog (verification); pre-commit hook schema; GitHub Actions (`actions/checkout@v4`, `setup-java@v4`, `codeql-action/analyze@v3`); `cron`, `ubuntu-latest` | Semgrep pinned (SOURCE-PIN §2, 1.163.0) but rolling pack — **dated-at-use 2026-06**; CodeQL rolling (§2), GitHub Actions rolling (§5), pre-commit pinned (§5); **gitleaks/TruffleHog have NO SOURCE-PIN row** → dated-at-use, no version asserted. All flagged `09-flags/70_sast_secrets_tools_saas_dated_at_use.md` |
| OWASP Dependency-Check 12.2.2 (named in the SCA stage comment/goal) | SOURCE-PIN §4 (pinned row); illustrative pipeline step, not run by the module build |
| AWS access-key-id regex shape `(A3T[A-Z0-9]|AKIA|ASIA)[A-Z0-9]{16}`; example key `AKIAIOSFODNN7EXAMPLE` | publicly-documented AWS key-id prefix/length shape + AWS's published non-functional example key; marked verify-at-pin in the flag, used to teach the pattern a scan flags |

No fact in the module is invented. The unpinned SaaS/rolling SAST and secrets tools are presented as
dated-at-use (2026-06), not as timeless facts, and are flagged. Nothing was sourced from memory or an
ahead-of-pin state; the dossier's `⚠ verify-at-pin` atoms (tool versions/rule-IDs/config; CodeQL/Snyk
licensing; gitleaks/TruffleHog config + verification; GitHub push-protection specifics) are NOT asserted as
settled in the module or its comments — they are routed to the flag and to Ch 32 (the security gate).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | The pinned-authority clone is multi-authority and not a single git repo; `ensure_source_pin.sh` is configured with a placeholder repo (`AUTHORITY_REPO="{URL}"`, `SHA="n/a-multi-authority"`) and cannot heal a single clone. Verification therefore reads the banked, verified dossier (verified against SOURCE-PIN 2026-06-20) and the pinned SOURCE-PIN rows; the SaaS/rolling/unpinned tool config schemas are the dossier's own `⚠ verify-at-pin` atoms. | NOTE | `.claude/scripts/ensure_source_pin.sh`; `SOURCE-PIN.md` (multi-authority) | No fact asserted beyond dossier + SOURCE-PIN; unpinned tool atoms dated-at-use + flagged. Re-confirm tool config schemas at `/pin-source` |
| 2 | The module was built standalone (`-f <module>/pom.xml`) and is **not** registered in `08-companion-code/pom.xml` `<modules>`. The task constraint forbids editing the root pom; the register-last rule also requires green build + CODE-REVIEW before registration. | NOTE | `08-companion-code/pom.xml` (unmodified) | Add `<module>70_sast_secrets_detection</module>` after CODE-REVIEW passes (separate step; not done here) |
| 3 | The SAST/secrets tools (CodeQL, GitHub Actions) are SaaS/rolling and gitleaks/TruffleHog have no SOURCE-PIN row; the pre-commit `rev` is a placeholder; the Semgrep pack is rolling. | MINOR (dated-at-use, not a build defect) | `ci/sast-scan.yml`, `config/semgrep/java-injection.yml`, `.gitleaks.toml`, `.pre-commit-config.yaml` | Flagged to `09-flags/70_sast_secrets_tools_saas_dated_at_use.md`; add gitleaks/TruffleHog pin rows + commit-sha digests at public-push sign-off, or record the dated-at-use convention |
| 4 | The dossier's companion spec mentioned a "deliberately-included broken-access-control check" as a code-level honest edge; the built module demonstrates *SAST-misses-design* in the package-info + README prose/comments rather than as a separate vulnerable class, to keep the module focused on the chapter's headline pair (injection sink + secrets). | NOTE (realizes the draft's intent; does not extend it) | `src/main/java/org/acme/sast/package-info.java`, `README.md` | None required; the honest edge is present in code-adjacent prose. A future module could add an authz counter-example if the chapter ever shows one |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green warning-clean, all 6 snippets resolve, the
config files parse, the SpotBugs suppression is verified load-bearing (a true SAST finding), and no detail
is invented (unpinned SaaS/rolling tools are dated-at-use and flagged). Floor-C verdict: **PASS**.

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b): companion artifact builds green via `mvn -B -Pquality verify` at the pin
  (Java 21.0.11); every displayed snippet resolves to a real bounded tag region (≤9 lines) in a
  compiling/parsing file; FLOOR C source-trace clean (unpinned tool atoms dated-at-use + flagged).

---

## Module contents

```
08-companion-code/70_sast_secrets_detection/
├── pom.xml                          (child of companion-code; own quality profile; zero runtime deps)
├── README.md                        (neutral-voice module overview; CONFIG-centric framing; honest edges)
├── .gitleaks.toml                   (secrets-scanner config — NOT run by the build; tag: gitleaks-config)
├── .pre-commit-config.yaml          (pre-commit secrets hook — NOT run by the build; tag: precommit-hook)
├── ci/
│   └── sast-scan.yml                (illustrative SAST + secrets + SCA workflow — NOT run; tag: sast-ci-step)
├── config/
│   ├── checkstyle/checkstyle.xml    (the house ruleset, peer shape)
│   ├── spotbugs/spotbugs-exclude.xml (one reasoned suppression: the injection sink, with a test pointer)
│   └── semgrep/java-injection.yml   (custom Semgrep injection rule — NOT run; tag: semgrep-rule)
└── src/
    ├── main/java/org/acme/sast/
    │   ├── package-info.java         (chapter overview + the four honest edges, in code)
    │   ├── OrderLookup.java          (the design-out fix: bound parameter)
    │   ├── VulnerableOrderLookup.java (the SAST counter-example: string-concat sink; tag: sql-sink)
    │   ├── SecretsResolver.java      (externalized-config, fail-closed; tag: fail-closed; health surfaces)
    │   ├── MissingSecretException.java (the fail-closed signal)
    │   └── SastSecretsGate.java      (the running path: parameterized lookup + fail-closed secret)
    ├── main/resources/
    │   ├── application-dev.properties  (profile: dev credential-variable binding)
    │   └── application-prod.properties (profile: prod credential-variable binding)
    └── test/java/org/acme/sast/
        ├── FakeConnection.java       (the java.sql test double — peer scaffolding shape)
        └── SastSecretsTest.java      (9 tests — the integration test over the whole stack)
```

NOT registered in the reactor `<modules>` (root pom unedited per task constraint; register after
CODE-REVIEW). Built standalone, green.

---

## Learnings & pipeline suggestions

- **The CONFIG-centric template (from peer 75) extends cleanly to a second config domain (security
  scanning).** A chapter whose headline artifacts are tool configs (Semgrep/gitleaks/CI/pre-commit) becomes
  Floor-C-buildable by modelling the *decision* the config enforces as plain unit-tested Java: the injection
  sink vs the parameterized fix, and the externalized fail-closed secrets resolver. The config files carry
  the displayed tags; the Java carries the runnable proof. This is the same move 75 made for CI gates and 69
  made for secure-coding counter-examples — worth promoting as the standard shape for every tooling chapter.
- **A deliberate-bad counter-example earns its keep only if the finding is proven load-bearing.** Emptying
  the SpotBugs exclude filter and confirming the build fails on exactly
  `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (then 0 with the narrow filter) proves the suppression hides a
  *true* SAST finding, not a phantom — the difference between teaching SAST and faking it. Core SpotBugs (the
  engine behind FindSecBugs) raises this SQL sink; deeper taint and crypto/secrets detectors are
  Semgrep/CodeQL/FindSecBugs territory and are shown by config + tests, the honest division the comments state.
- **A planted "fake key" must be a *documented non-functional* key, allow-listed, test-only.** Using AWS's
  published EXAMPLE key id (`AKIAIOSFODNN7EXAMPLE`), placed under `src/test` and allow-listed in the gitleaks
  config, teaches the pattern a scan flags without shipping anything that looks like a live credential — and
  it makes the chapter's sharpest point in code: a *real* leaked key is compromised the instant it pushes, so
  the fixture is safe precisely because the key is not. Detection is not remediation; rotate, do not delete.
- **SaaS/rolling/unpinned tools are dated-at-use, not invented — and "unpinned" is stricter than "rolling".**
  Semgrep is pinned but its pack rolls; CodeQL/GitHub Actions are rolling; gitleaks/TruffleHog have *no*
  SOURCE-PIN row at all. The honest move is the peer-75 pattern (dated-at-use comment + `09-flags/` entry)
  plus, for the truly-unpinned tools, a literal `rev` placeholder rather than an invented tag, and a flag note
  proposing SOURCE-PIN rows for gitleaks/TruffleHog. Consider adding those two rows at the next `/pin-source`.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 70 gate-run PASS
```
