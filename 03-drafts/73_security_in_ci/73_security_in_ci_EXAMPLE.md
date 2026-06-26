# GATE REPORT — EXAMPLE-BUILD — Chapter 32 (key 73)

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 73 (single key, frozen from `01-index/CANDIDATE_POOL.md`) — FINAL_INDEX Ch 32 (CLOSES Part VIII)
- **Slug:** `73_security_in_ci`
- **Draft under review:** `03-drafts/73_security_in_ci/73_security_in_ci_v1.md`
- **Module path:** `08-companion-code/73_security_in_ci/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×7), `check_snippets.sh`; Ruby/Psych YAML parse; build via Maven `verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned Java 21 toolchain with the static-analysis gate on (`-Pquality`):
14 tests pass, 0 Checkstyle violations, 0 SpotBugs findings, warning-clean. This is the CI-integration
view of Part VIII (the security STAGES and their gating policy — distinct from the SAST/secrets tool
internals in key 70 and the general quality gate in key 75), so the module ships two artifacts in
lock-step: an illustrative security pipeline (`ci/security-pipeline.yml`) wiring the five testing types
fast-to-slow with their fail thresholds, carrying the security-stage snippets; and a runnable,
unit-tested gate **policy** (`org.acme.secgate.SecurityGate`) that aggregates the stages' findings into a
three-way decision, carrying the gate-policy snippets. All 7 displayed snippets resolve to tag regions of
at most nine lines inside compiling/parsing files, and all 7 prose markers bind (`check_snippets.sh`: 7/7
PASS). The YAML parses. Every fact traces to the pin or the dossier; the only unpinned atoms are SaaS CI
actions and dynamic-scan tools (gitleaks, OWASP ZAP, GitHub Actions), presented as dated-at-use and
flagged to `09-flags/`, not invented. Both Floor-C preconditions hold.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; `Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)` matches the pin | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (clean verify, quality profile) — see exact line below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/73_security_in_ci/pom.xml clean verify
→ Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0 / No errors/warnings found
→ BUILD SUCCESS   (Total time: 3.030 s)
```

(`./mvnw -B verify` is the canonical floor command; this reactor uses a system `mvn 3.9.16` that matches
the pinned Maven version — there is no committed wrapper at the companion-code root. The quality profile
is opt-in `-Pquality`, mirroring the peer modules; with the profile off the build is also green — `Tests
run: 14 … BUILD SUCCESS`. Per the task constraint the module is built standalone via `-f <module>/pom.xml`;
the root `08-companion-code/pom.xml` `<modules>` list was NOT edited — see Findings #2 / the register-last
note.)

**YAML parse (the CI config is illustrative, validated separately from the Maven build):**

```
ruby -e "require 'yaml'; YAML.load_file('.../ci/security-pipeline.yml')"
→ YAML OK — jobs: secrets, pr-static-trio, container-iac, staging-dynamic, security-gate
```

(Ruby/Psych is the available YAML 1.1 parser; Python `yaml` is not installed in this environment — same
as the peer key-75 build. Psych surfaces the well-known YAML-1.1 quirk that a bare `on:` key parses as
boolean `true`; GitHub Actions' own parser reads it as the string key, which is correct and standard for
workflows. The file is valid; this is a parser-dialect display artifact, not an error.)

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `pre-commit-secrets` | `ci/security-pipeline.yml` | 2 | "Ordering the gate", after the *Run the cheap checks earliest* CONCEPT block |
| `pr-fast-trio` | `ci/security-pipeline.yml` | 4 | "Ordering the gate", after the same CONCEPT block |
| `container-iac-scan` | `ci/security-pipeline.yml` | 2 | "Ordering the gate", after the same CONCEPT block |
| `staging-dynamic` | `ci/security-pipeline.yml` | 2 | "Ordering the gate", after the same CONCEPT block |
| `gate-policy` | `SecurityGatePolicy.java` | 5 | "The policy that makes it stick", after the *Block high-severity new findings* CONCEPT block |
| `block-vs-warn` | `SecurityGateDecision.java` | 6 | "The policy that makes it stick", after the same CONCEPT block |
| `aggregate-and-gate` | `SecurityGate.java` | 8 | "The policy that makes it stick", after the same CONCEPT block |

`check_snippets.sh 03-drafts/73_security_in_ci/73_security_in_ci_v1.md` → **7 marker(s); 7 pass, 0 fail.**
Each prose marker carries a one-line lead-in in the locked voice; **no prose was deleted**. A "Snippet
tags:" line was added to the foot-of-chapter companion spec, alongside a BUILT companion-module note. The
displayed listing and the runnable/parsing file are one artifact (the prose shows a tag-region inside the
full file; the file holds the enterprise context around it).

> Tag-budget note: the task asked for **4–7 tags (security stages + the gate policy)**; the set landed at
> **7** — 4 security-stage tags (the five testing types, with the SAST+SCA trio shown as one PR-stage
> region) + 3 gate-policy tags. An earlier 8th candidate (`security-gate-policy`, a YAML echo of the gate
> job) was dropped: the gate policy is displayed as the runnable Java (`block-vs-warn` + `aggregate-and-gate`),
> which is the more honest display than a shell stub, keeping the set within range with no teaching lost.
> The job itself stays in the YAML as context (untagged).

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit/AssertJ inherited from the aggregator `dependencyManagement`; **zero** version literals in this module — zero runtime dependencies (JDK-only) |
| Externalized config profiles | Gate policy externalized to `secgate-dev.properties` / `secgate-prod.properties`, selected by `-Dsecgate.profile` (the `%dev`/`%prod` idea); the profiles genuinely differ (dev routes a severe-but-unproven finding to review; prod fails closed on it). Static-analysis rulesets externalized to `config/`; the SAST-class gate is the opt-in `-Pquality` profile |
| At least one integration test | `SecurityGateTest` — 14 tests driving the whole stack (externalized `SecurityGatePolicy.load` + the aggregation decision) across pass / route-to-review / block, new-vs-existing scope, multi-stage aggregation, exploitability routing, whole-repo policy, both profiles, the stage static/dynamic split, and fail-fast construction |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`; tests build a fresh gate per case. No spurious logging observed in the run |
| Observability / health surface | `SecurityGate.blockedCount()` (running counter of blocking decisions — the metric a dashboard trends to spot gate fatigue), `stagesReporting(...)` (which stages contributed findings — surfaces a stage that silently stopped running), and `isReady()` (readiness probe over the wired policy) |
| Explicit failure path | `SecurityGate.evaluate` returns a sealed `SecurityGateDecision` (`Pass`/`Review`/`Block`); a new exploitable high-severity finding returns `Block` and fails the build, a severe-but-unproven or sub-blocking finding `Review`s (the security-reviewer route), pre-existing debt is out of scope (`Pass`). Proven by `blocksNewExploitableHighSeverity`, `routesSevereButUnprovenToReview`, `doesNotBlockPreExistingDebt`, `prodProfileFailsClosedOnNewHigh`, `wholeRepoPolicyBlocksOnExistingDebt` |

The CI YAML additionally demonstrates the chapter's pipeline-level surfaces: the five security stages
ordered fast-to-slow (secrets → SAST+SCA → container/IaC → DAST/IAST), the PR-vs-staging split, and the
required-status-check gate job — illustrative config, not run by the build, with each stage mapped to the
same `mvn`/CLI command a developer runs locally.

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** The fixed figure plan for this chapter is **1 designed diagram + 0 captured
screenshots**: the draft references only `Fig 73.1`, the security-gate-across-the-pipeline diagram, to be
authored separately at `05-figures/73_security_in_ci/fig73_1.{html,png,sources.md}` (HTML→PNG; not this
agent's job). There is **no live subject-native UI surface** to capture from this module: the runnable
artifact is a JDK-only library (gate-policy logic — no dev console, API explorer, or health view), and the
pipeline's only native UI (a GitHub Actions run view, or a ZAP/Trivy report) requires a live remote CI run
and a deployed staging app, which the dossier records as **REPRO PENDING-RUNTIME** (tools + CI
network-gated; DAST needs a deployed app). A CI/scan-run screenshot is therefore an editorial/figure-stage
item (it needs a live pipeline), not a live capture from this green build, and is not invented here.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. The domain
(`SecurityGate` / `SecurityFinding` / `SecurityGatePolicy` / `SecurityGateDecision` / `SecurityStage` over
the book's `org.acme` namespace) is the book's own, modelled on the peer gate modules
(`75_ci_pipeline_quality_gates`, `65_dependency_scanning_sbom_supply_chain`) but written fresh for the
security-orchestration angle — not an upstream sample. The pom, Checkstyle, and SpotBugs configs follow the
book's own established house shape (the peer modules), not an upstream template. `ci/security-pipeline.yml`
is **authored for this chapter** — it is **not** a copied GitHub Actions starter/quickstart workflow: the
stage decomposition (the five testing types fast-to-slow), the job dependency graph, the comments, and the
commands are written to teach this chapter's assembly, and no `actions/*-starter` skeleton or upstream
sample `.yml` was copied and renamed. The GitHub Actions step syntax (`uses: actions/checkout@v4` etc.) is
platform vocabulary, not copied prose, and matches the shape the repo's own `.github/workflows/ci.yml` and
the peer CI modules already use. Nothing is taken substantially verbatim from a specific source file, so no
in-file attribution is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| Java 21 runtime anchor; `maven.compiler.release=21` | SOURCE-PIN runtime baseline (JDK 21.0.11), inherited from aggregator |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, checkstyle-plugin 3.6.0, spotbugs-plugin 4.9.3.0, surefire 3.5.6, AssertJ 3.27.7, JUnit 6.x | SOURCE-PIN §2/§3; the house module shape (peer modules) |
| Sealed interface + records (`SecurityGateDecision`), records (`SecurityFinding`/`SecurityGatePolicy`), enums, `Objects.requireNonNull`, `Stream`/`Comparator`/`AtomicLong`/`EnumSet` API | JDK 21.0.11 API + language (JEP 409 sealed GA 17, JEP 395 records GA 16 — both available at 21); standard library usage |
| Five testing types (SAST/SCA/secrets/DAST/IAST); fast-to-slow shift-left ordering (secrets pre-commit, SAST+SCA at PR, DAST/IAST at staging); container/IaC slots in; block-new-high-severity + route-to-reviewer; clean-as-you-code scoping; "vulnerable ≠ exploitable"; gate-fatigue; green-gate≠secure (BAC/logic flaws no tool finds) | Dossier (key 73, synthesizing 65/70/71 + OWASP DevSecOps/ASVS) — frame verified; the chapter's own verified body |
| `actions/checkout@v4`, `actions/setup-java@v4`, `temurin`, `ubuntu-latest`, `cron` (in `ci/security-pipeline.yml`) | GitHub Actions, SOURCE-PIN §5 (rolling SaaS, docs as of 2026-06) — **dated-at-use**, flagged `09-flags/73_security_pipeline_saas_dated_at_use.md`; same tags as the repo's own `ci.yml` and the peer CI modules |
| `gitleaks detect` (secrets), `zap-baseline.py` (DAST) | gitleaks + OWASP ZAP — **no SOURCE-PIN row** — **dated-at-use 2026-06**, flagged; named as stages, no version asserted |
| Trivy 0.71.0 (container/IaC stage), OWASP Dependency-Check 12.2.2 (SCA stage), in `ci/security-pipeline.yml` | SOURCE-PIN §4 (pinned rows); illustrative pipeline steps, not run by the module build |
| `CWE-89` (SAST injection class), `CVE-2021-44228` (the Log4Shell id used as an SCA-finding fixture) | CWE/CVE are public registry identifiers used as fixture ids; not asserted as tool output, recorded verbatim as the gate would receive them |

No fact in the module is invented. The unpinned SaaS CI actions and the unpinned scan tools (gitleaks,
OWASP ZAP) are presented as dated-at-use (2026-06), not as timeless facts, and are flagged. Nothing was
sourced from memory or an ahead-of-pin state. The chapter's `⚠ verify-at-pin` atoms (OWASP ZAP / DAST/IAST
specifics, per-tool severity-threshold config, CI platform specifics) are NOT asserted as settled in the
module: ZAP appears only as a dated-at-use illustrative stage command, the gating policy is shown as the
book's own runnable Java, and DAST/IAST are described as staging-gated and REPRO PENDING-RUNTIME.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Three Javadoc comment lines in `SecurityStage.java` exceeded the 120-char house limit; the quality build correctly failed (the gate doing its job, exactly the chapter's thesis). | NOTE (resolved during build) | `SecurityStage.java` constant Javadoc | Fixed by reflowing the comments; rebuild green |
| 2 | The module was built standalone (`-f <module>/pom.xml`) and is **not** registered in `08-companion-code/pom.xml` `<modules>`. The task constraint forbids editing the root pom; the register-last rule also requires green build + CODE-REVIEW before registration. | NOTE | `08-companion-code/pom.xml` (unmodified — 0 occurrences of `73_security_in_ci`) | Add `<module>73_security_in_ci</module>` after CODE-REVIEW passes (separate step; not done here) |
| 3 | `ci/security-pipeline.yml` references SaaS GitHub Actions pinned only to `@v4` major tags (mutable) and names gitleaks / OWASP ZAP, which have no SOURCE-PIN row, per SOURCE-PIN §5 rolling policy. | MINOR (dated-at-use, not a build defect) | `ci/security-pipeline.yml` | Flagged to `09-flags/73_security_pipeline_saas_dated_at_use.md`; pin actions to commit-sha digests + decide gitleaks/ZAP SOURCE-PIN rows at public-push sign-off |
| 4 | The DAST stage (`staging-dynamic`) needs a live staging deployment to run (REPRO PENDING-RUNTIME) and is wired as illustrative config only; not executed by the build. The chapter's only captured-figure candidate (a CI-run / scan-report UI view) likewise needs a live pipeline. | MINOR (editorial/figure + repro signal, not a build defect) | `ci/security-pipeline.yml`; `05-figures/73_security_in_ci/` | Record DAST as described-not-run (done in YAML comments + README); figure stage captures from a live pipeline if one exists, else rely on the designed `Fig 73.1` |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green warning-clean, all 7 snippets resolve, the
YAML parses, and no detail is invented (unpinned SaaS actions and scan tools are dated-at-use and flagged).
Floor-C verdict: **PASS**.

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b): companion artifact builds green via `mvn -B -Pquality verify` at the pin
  (JDK 21.0.11 / Maven 3.9.16); every displayed snippet resolves to a real bounded tag region (≤9 lines)
  in the compiled/parsing file; FLOOR C source-trace clean.
- [ ] **CODE-REVIEW**: NOT run by this agent (separate Step-4b `code-reviewer` pass). Register the module in
  the reactor `<modules>` only after CODE-REVIEW PASS.

---

## Module contents

```
08-companion-code/73_security_in_ci/
├── pom.xml                         (child of companion-code; quality profile; zero runtime deps)
├── README.md                       (neutral-voice module overview; CI-integration framing)
├── ci/
│   └── security-pipeline.yml       (illustrative security pipeline — NOT run by the build;
│                                     tags: pre-commit-secrets, pr-fast-trio, container-iac-scan, staging-dynamic)
├── config/checkstyle/checkstyle.xml
├── config/spotbugs/spotbugs-exclude.xml      (empty — model is immutable records/sealed; no suppression)
└── src/
    ├── main/java/org/acme/secgate/
    │   ├── package-info.java        (chapter overview + honest edges)
    │   ├── SecurityStage.java       (the five testing types; static/dynamic split — fast-to-slow ordering)
    │   ├── Severity.java            (INFO/LOW/MEDIUM/HIGH — the block-vs-review axis)
    │   ├── FindingScope.java        (NEW/EXISTING — the clean-as-you-code axis)
    │   ├── SecurityFinding.java     (immutable finding: stage + ruleId + severity + scope + exploitable)
    │   ├── SecurityGatePolicy.java  (externalized dev/prod policy — tag: gate-policy)
    │   ├── SecurityGateDecision.java(sealed Pass/Review/Block — tag: block-vs-warn)
    │   └── SecurityGate.java        (the aggregating decision — tag: aggregate-and-gate; metrics + readiness)
    ├── main/resources/
    │   ├── secgate-dev.properties   (profile: feature-branch — routes severe-but-unproven to review)
    │   └── secgate-prod.properties  (profile: release — fails closed on new HIGH)
    └── test/java/org/acme/secgate/
        └── SecurityGateTest.java    (14 tests — the integration test over the whole stack)
```

NOT registered in the reactor `<modules>` (root pom unedited per task constraint; register after
CODE-REVIEW). Built standalone, green.

---

## Learnings & pipeline suggestions

- **The CONFIG-centric security chapter binds by modelling the DECISION the pipeline enforces, with a
  domain distinct from its peers.** Key 73 is the CI-integration view of the security gate, so it must not
  duplicate key 70 (SAST/secrets tool internals) or key 75 (the general gate). The split that worked: the
  YAML carries the *five security stages* (the orchestration the chapter owns), and the runnable Java carries
  the *aggregating gate policy* — a `SecurityGate` over multi-stage `SecurityFinding`s that returns a
  three-way `SecurityGateDecision`. This is the same "local equivalent of the CI gate" pattern as key 75, but
  the model is security-specific (a `SecurityStage` dimension and a `Review` verdict), so the two modules
  teach different things with no overlap. A future synthesis chapter should likewise carve a distinct model
  rather than re-skin a peer's.
- **The security-reviewer route is the honest middle path, and it earns a third verdict.** Where the general
  gate (key 75) is `Pass`/`Warn`/`Block`, the security gate is `Pass`/`Review`/`Block`: exploitability is a
  judgment, so a severe-but-unproven finding routes to a human rather than auto-blocking. Modelling this as a
  policy knob (`requireExploitableToBlock`) that differs between the `dev` and `prod` profiles made the
  externalized-config requirement *load-bearing* — the profiles genuinely change behaviour (dev reviews,
  prod fails closed), which a test asserts, rather than being two near-identical files.
- **"Green gate ≠ secure" is testable, not just a comment.** The strongest way to put the chapter's honest
  center in code is a test where a broken-access-control flaw that **no stage produces** lets the gate
  `Pass` — the test name (`greenGateIsNotSecure`) and its comment state that a Pass means only "no detected
  known exploitable issue", not secure. This forces the HONEST-LIMITATIONS floor into a code path that runs,
  matching the dossier's "honest edges (comments)" requirement and the EXAMPLES-GUIDE failure-path intent.
- **SaaS/unpinned security tools are dated-at-use, not pinned — flag, do not invent.** gitleaks and OWASP ZAP
  have no SOURCE-PIN row; the honest move is to name them as stages with a dated-at-use 2026-06 comment and a
  `09-flags/` entry, never an invented version. The pinned tools that appear (Trivy 0.71.0, Dependency-Check
  12.2.2) trace to their SOURCE-PIN §4 rows and appear only as illustrative steps. DAST against staging is
  recorded as REPRO PENDING-RUNTIME (needs a deployed app), described not executed.
- Append confirmed lessons here to `00-strategy/PIPELINE-LEARNINGS.md` and promote durable ones into the
  relevant rule file per the continuous-improvement HARD RULE.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 73 gate-run PASS
```

(One line per gate run; the build-state is `[MANUAL — tooling pending]`, so this is recorded for the
provenance trail rather than emitted by a scripted runner.)
