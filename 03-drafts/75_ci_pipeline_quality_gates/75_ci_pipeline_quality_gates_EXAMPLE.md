# GATE REPORT — EXAMPLE-BUILD — Chapter 33 (key 75)

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 75 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 76 + 79) — FINAL_INDEX Ch 33 (OPENS Part IX)
- **Slug:** `75_ci_pipeline_quality_gates`
- **Draft under review:** `03-drafts/75_ci_pipeline_quality_gates/75_ci_pipeline_quality_gates_v1.md`
- **Module path:** `08-companion-code/75_ci_pipeline_quality_gates/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×7), `check_snippets.sh`; Ruby/Psych YAML parse; build via Maven `verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned Java 21 toolchain with the static-analysis gate on
(`-Pquality`): 11 tests pass, 0 Checkstyle violations, 0 SpotBugs findings. This is a CONFIG-centric
chapter, so the module ships two artifacts kept in lock-step: an illustrative CI pipeline
(`ci/quality-gates.yml`) carrying the pipeline-design and performance snippets, and a runnable,
unit-tested gate **policy** (`org.acme.cigate`) that is the local equivalent of the CI gate, carrying
the policy snippets. All 7 displayed snippets resolve to tag regions of at most nine lines inside
compiling/parsing files, and all 7 prose markers bind (`check_snippets.sh`: 7/7 PASS). The YAML parses.
Every fact traces to the pin or to the dossier; the only unpinned atoms are SaaS GitHub Actions, which
are presented as dated-at-use and flagged to `09-flags/`, not invented. Both Floor-C preconditions hold.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; `Apache Maven 3.9.16` matches the pin | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (clean verify, quality profile) — see exact line below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/75_ci_pipeline_quality_gates/pom.xml clean verify
→ Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0 / No errors/warnings found
→ BUILD SUCCESS   (Total time: 2.969 s)
```

(`./mvnw -B verify` is the canonical floor command; this reactor uses a system `mvn 3.9.16` that
matches the pinned Maven version — there is no committed wrapper at the companion-code root. The quality
profile is opt-in `-Pquality`, mirroring the peer modules; with the profile off the build is also green.
Per the task constraint the module is built standalone via `-f <module>/pom.xml`; the root
`08-companion-code/pom.xml` `<modules>` list was NOT edited — see Findings #2 / the register-last note.)

**YAML parse (the CI config is illustrative, validated separately from the Maven build):**

```
ruby -e "require 'yaml'; YAML.load_file('.../ci/quality-gates.yml')"
→ YAML OK — jobs: build-and-lint, test-and-coverage, static-and-security, quality-gate, deep-checks-main-nightly
```

(Ruby/Psych is the available YAML 1.1 parser; Python `yaml` is not installed in this environment. Psych
surfaces the well-known YAML-1.1 quirk that a bare `on:` key parses as boolean `true` — GitHub Actions'
own parser reads it as the string key, which is correct and standard for workflows. The file is valid;
this is a parser-dialect display artifact, not an error.)

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `pr-vs-main-split` | `ci/quality-gates.yml` | 7 | "Pipeline design", after the *Not everything runs on every PR* CONCEPT block |
| `test-coverage-gate` | `ci/quality-gates.yml` | 2 | "Pipeline design", after the same-build-locally paragraph |
| `static-security-gate` | `ci/quality-gates.yml` | 4 | "Pipeline design", after the same-build-locally paragraph |
| `cache` | `ci/quality-gates.yml` | 6 | "Gate performance", after the levers bullet list |
| `gate-policy` | `GatePolicy.java` | 5 | "Quality-gate policy", after the gate-can-check paragraph |
| `clean-as-you-code` | `QualityGate.java` | 7 | "Quality-gate policy", after the *Gate new code* CONCEPT block |
| `block-vs-warn` | `GateDecision.java` | 4 | "Quality-gate policy", after the block-versus-warn paragraph |

`check_snippets.sh 03-drafts/75_ci_pipeline_quality_gates/75_ci_pipeline_quality_gates_v1.md` → **7
marker(s); 7 pass, 0 fail.** Each prose marker carries a one-line lead-in in the locked voice; **no
prose was deleted**. A "Snippet tags:" line was added to the foot-of-chapter companion spec. The
displayed listing and the runnable/parsing file are one artifact (the prose shows a tag-region inside
the full file; the file holds the enterprise context around it).

> Snippet-fit note: this is the book's **first CONFIG-centric (YAML) module**. YAML tag regions use
> `# tag::name[]` / `# end::name[]` (hash comments), which `extract_snippet.sh` resolves identically to
> the `//` Java markers and renders as a ```yaml block. The 4-7-tag budget landed at 7 (4 CI-config +
> 3 Java); an 8th candidate (`quality-gate`, the required-status-check job) was dropped as conceptually
> redundant with `clean-as-you-code` + `block-vs-warn`, keeping the set within range with no teaching lost.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit/AssertJ inherited from the aggregator `dependencyManagement`; **zero** version literals in this module — zero runtime dependencies (JDK-only) |
| Externalized config profiles | Gate policy externalized to `cigate-dev.properties` / `cigate-prod.properties`, selected by `-Dcigate.profile` (the `%dev`/`%prod` idea); static-analysis rulesets externalized to `config/`; quality gate is the opt-in `-Pquality` profile |
| At least one integration test | `QualityGateTest` — 11 tests driving the whole stack (externalized `GatePolicy.load` + decision) across pass / warn / block, new-vs-existing scope, whole-repo policy, both profiles, and fail-fast construction |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`; tests build a fresh gate per case. No spurious logging observed in the run |
| Observability / health surface | `QualityGate.blockedCount()` (running counter of blocking decisions — the metric a dashboard trends) and `isReady()` (readiness probe over the wired policy) |
| Explicit failure path | `QualityGate.evaluate` returns a sealed `GateDecision` (`Pass`/`Warn`/`Block`); a new high-severity finding returns `Block` and fails the build, a lesser new finding `Warn`s, pre-existing debt is out of scope (`Pass`). Proven by `blocksNewHighSeverity`, `warnsNewSubBlockingSeverity`, `doesNotBlockPreExistingDebt`, `wholeRepoPolicyBlocksOnExistingDebt` |

The CI YAML additionally demonstrates the chapter's pipeline-level surfaces: ordered fail-fast stages,
the PR/main/nightly split, `~/.m2` caching, and the parallel reactor — illustrative config, not run by
the build, with each gate stage mapped to the same `mvn` command a developer runs locally.

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** The fixed figure plan for this chapter is **1 designed diagram + 0 captured
screenshots**: the draft references only `Fig 75.1`, the fail-fast-portfolio pipeline diagram, already
authored separately at `05-figures/75_ci_pipeline_quality_gates/fig75_1.{html,png,sources.md}`
(HTML→PNG; not this agent's job). There is **no live subject-native UI surface** to capture from this
module: the runnable artifact is a JDK-only library (gate-policy logic — no dev console, API explorer,
or health view), and the pipeline's only native UI (a GitHub Actions run view) requires a live remote
CI run, which the dossier records as CI/network-gated → **REPRO PENDING-RUNTIME**. A CI-run screenshot
is therefore an editorial/figure-stage item (it needs a live pipeline), not a live capture from this
green build, and is not invented here.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. The domain
(`QualityGate` / `Finding` / `GatePolicy` / `GateDecision` over the book's `org.acme` namespace) is the
book's own, modelled on the peer gate module (`105_performance_regression_gates`), not an upstream
sample. The pom, Checkstyle, and SpotBugs configs follow the book's own established house shape (the
peer modules), not an upstream template. `ci/quality-gates.yml` is **authored for this chapter** — it is
**not** a copied GitHub Actions starter/quickstart workflow: the stage decomposition, the job
dependency graph, the comments, and the `mvn` commands are written to teach this chapter's three
decisions, and no `actions/*-starter` skeleton or upstream sample `.yml` was copied and renamed. The
GitHub Actions step syntax (`uses: actions/checkout@v4` etc.) is platform vocabulary, not copied prose,
and matches the shape the repo's own `.github/workflows/ci.yml` already uses. Nothing is taken
substantially verbatim from a specific source file, so no in-file attribution is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| Java 21 runtime anchor; `maven.compiler.release=21` | SOURCE-PIN runtime baseline (JDK 21.0.11), inherited from aggregator |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, checkstyle-plugin 3.6.0, spotbugs-plugin 4.9.3.0, surefire 3.5.6, AssertJ 3.27.7, JUnit 6.x | SOURCE-PIN §2/§3; the house module shape (peer modules) |
| Sealed interface + records (`GateDecision`), records (`Finding`/`GatePolicy`), `Objects.requireNonNull`, `Stream`/`Comparator`/`AtomicLong` API | JDK 21.0.11 API + language (JEP 409 sealed GA 17, JEP 395 records GA 16 — both available at 21); standard library usage |
| Clean-as-you-code = gate new/changed code, Sonar default new-code-focused; block-vs-warn; required status check; PR/main/nightly split; fail-fast cheap→expensive ordering; caching/parallelism/`-T`; pipeline duration as a metric | Dossier (keys 75/76/79) — frame verified; the chapter's own verified body |
| `actions/checkout@v4`, `actions/setup-java@v4`, `actions/cache@v4`, `temurin`, `ubuntu-latest`, `cron`, `mvn -T 1C` (in `ci/quality-gates.yml`) | GitHub Actions, SOURCE-PIN §5 (rolling SaaS, docs as of 2026-06) — **dated-at-use**, flagged `09-flags/75_ci_actions_saas_dated_at_use.md`; same tags as the repo's own `ci.yml` |
| OWASP Dependency-Check 12.2.2, PITest 1.25.3, JaCoCo 0.8.16 (named in YAML stage comments/goals) | SOURCE-PIN §4/§3 (pinned rows); illustrative pipeline steps, not run by the module build |

No fact in the module is invented. The unpinned SaaS CI actions are presented as dated-at-use (2026-06),
not as timeless facts, and are flagged. Nothing was sourced from memory or an ahead-of-pin state; the
chapter's `⚠ verify-at-pin` atoms (CI platform syntax key 77, DORA wording key 85, Sonar default-gate
wording) are routed onward (Ch 34/Part X) and are NOT asserted as settled in the module or its comments.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | First-draft test asserted a pre-existing HIGH finding under clean-as-you-code would `Warn`; the build correctly returned `Pass` (the finding is filtered out of scope before the decision). The CODE was right and more accurate; the test expectation and the `GateDecision.Warn` semantics were tightened to: pre-existing debt is out of scope → `Pass`; only a NEW sub-blocking finding `Warn`s. Javadoc + README aligned. | NOTE (resolved during build) | `QualityGateTest.doesNotBlockPreExistingDebt`, `QualityGate` Javadoc | Fixed; the more honest behaviour is now documented and tested |
| 2 | The module was built standalone (`-f <module>/pom.xml`) and is **not** registered in `08-companion-code/pom.xml` `<modules>`. The task constraint forbids editing the root pom; the register-last rule also requires green build + CODE-REVIEW before registration. | NOTE | `08-companion-code/pom.xml` (unmodified) | Add `<module>75_ci_pipeline_quality_gates</module>` after CODE-REVIEW passes (separate step; not done here) |
| 3 | `ci/quality-gates.yml` references SaaS GitHub Actions pinned only to `@v4` major tags (mutable), per SOURCE-PIN §5 rolling policy. | MINOR (dated-at-use, not a build defect) | `ci/quality-gates.yml` | Flagged to `09-flags/75_ci_actions_saas_dated_at_use.md`; pin to commit-sha digests at public-push sign-off or record the dated-at-use convention |
| 4 | The chapter's only captured-figure candidate (a CI-run UI view) needs a live remote pipeline (REPRO PENDING-RUNTIME); not capturable from this green local build. | MINOR (editorial/figure signal, not a build defect) | `05-figures/75_ci_pipeline_quality_gates/` | Figure stage: capture from a live pipeline run if/when one exists, or rely on the designed `Fig 75.1` |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green warning-clean, all 7 snippets resolve,
the YAML parses, and no detail is invented (unpinned SaaS actions are dated-at-use and flagged).
Floor-C verdict: **PASS**.

---

## Module contents

```
08-companion-code/75_ci_pipeline_quality_gates/
├── pom.xml                         (child of companion-code; quality profile; zero runtime deps)
├── README.md                       (neutral-voice module overview; CONFIG-centric framing)
├── ci/
│   └── quality-gates.yml           (illustrative GitHub Actions pipeline — NOT run by the build;
│                                     tags: pr-vs-main-split, cache, test-coverage-gate, static-security-gate)
├── config/checkstyle/checkstyle.xml
├── config/spotbugs/spotbugs-exclude.xml      (empty — model is immutable records/sealed; no suppression)
└── src/
    ├── main/java/org/acme/cigate/
    │   ├── package-info.java        (chapter overview + honest edges)
    │   ├── Severity.java            (INFO/LOW/MEDIUM/HIGH — the block-vs-warn axis)
    │   ├── FindingScope.java        (NEW/EXISTING — the clean-as-you-code axis)
    │   ├── Finding.java             (immutable finding: ruleId + severity + scope)
    │   ├── GatePolicy.java          (externalized dev/prod policy — tag: gate-policy)
    │   ├── GateDecision.java        (sealed Pass/Warn/Block — tag: block-vs-warn)
    │   └── QualityGate.java         (the decision — tag: clean-as-you-code; metric + readiness)
    ├── main/resources/
    │   ├── cigate-dev.properties    (profile: feature-branch policy)
    │   └── cigate-prod.properties   (profile: trunk/release policy)
    └── test/java/org/acme/cigate/
        └── QualityGateTest.java     (11 tests — the integration test over the whole stack)
```

NOT registered in the reactor `<modules>` (root pom unedited per task constraint; register after
CODE-REVIEW). Built standalone, green.

---

## Learnings & pipeline suggestions

- **CONFIG-centric chapters bind cleanly to YAML tag regions.** `# tag::name[]` / `# end::name[]` in a
  `.yml` file resolve through the same `extract_snippet.sh` as Java `//` markers and render as ```yaml.
  This is the first such module; the pattern works with no script change and should be the template for
  every future CI/config chapter (34, 35, 36). Worth a one-line note in `EXAMPLES-GUIDE` that hash-comment
  tag regions in YAML/properties are first-class.
- **Make the config-centric chapter BUILDABLE by modelling the decision the config enforces.** The
  pipeline YAML cannot be exercised by `mvn`, so the module's runnable core is the *gate policy* the
  pipeline wires (clean-as-you-code + block-vs-warn), as plain unit-tested Java — the "local equivalent of
  the CI gate" (Ch 27 parity). This keeps Floor-C COMPILE real for a chapter whose headline artifact is
  config, and mirrors how `105_performance_regression_gates` made a gate-centric chapter buildable.
- **A failing test can be the code teaching you the more honest behaviour.** The build caught that under
  clean-as-you-code a pre-existing HIGH finding is *out of scope* (→ Pass), not merely *warned*. The fix
  was to the test and the prose, not the code — and the chapter is sharper for it (clean-as-you-code
  filters before it decides). Let the build adjudicate semantics; do not weaken a test to match a first guess.
- **SaaS CI actions are dated-at-use, not pinned — flag, do not invent a digest.** SOURCE-PIN §5 treats
  GitHub Actions as rolling. The honest move is `@v4` + an inline "dated-at-use 2026-06" comment + a
  `09-flags/` entry proposing digest-pinning at public-push, rather than inventing a commit sha the agent
  cannot verify. The pinned Maven-invoked tools (Dependency-Check/PITest/JaCoCo) appear only as illustrative
  steps and trace to their SOURCE-PIN rows.
- **Honest edges live in the comments AND the prose, not just the README.** The package-info, the YAML
  comments, and the README each carry the four limits (new-code-only, green≠good, gameable proxy,
  cache-false-green / culture) so the HONEST-LIMITATIONS floor shows up in code, matching the dossier's
  "honest edges (comments)" requirement.
