<!--
Dossier key: 73 (single key) — per 01-index/FINAL_INDEX.md Ch 32 (CLOSES Part VIII; Ch 33 opens Part IX — CI/CD & Quality Gates)
Slug: 73_security_in_ci
Part / arc position: Part VIII — Security & SAST, Chapter 32 of 30-32 (CLOSER)
Companion module: 08-companion-code/ (CI job: secrets + SAST + SCA with severity-threshold block on NEW findings) — ⚠ EXAMPLE-BUILD = PENDING (toolchain READY: JDK 21.0.11+25.0.3; tools/CI network-gated → REPRO caveat). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (single concise synthesis dossier; assembles keys 65/70/71 into the gate; DevSecOps frame):
- Security gate (73): security controls only protect you if they run AUTOMATICALLY, EVERY CHANGE. Assemble Part-VIII tools into a coherent security GATE in CI — what runs, in what order, what blocks-a-merge vs warns. Security instance of the general quality gate (Part IX keys 75/76); a fitness function (Ch 26 key 56) for ISO Security (Ch 1 key 01). The testing types: SAST (static, YOUR code, Ch 31 key 70 — PR/build; injection/crypto early); SCA (deps, Ch 28 key 65 — known-CVE + license Ch 29; build + continuous monitoring); secrets scanning (Ch 31 key 71 — pre-commit + CI + push protection); DAST (dynamic, running app — black-box vs deployed instance e.g. OWASP ZAP; catches runtime/config SAST can't; slower, later, staging); IAST (interactive — instruments running app during tests for vulns with runtime context; hybrid SAST/DAST). Gate ordering (fast→slow, shift-left): secrets + SAST + SCA at PR (fast, block on high severity); DAST/IAST later stage vs staging (key 83); container/IaC scanning (Trivy, Ch 28) if applicable. BLOCK vs WARN policy (senior content): block on high-severity NEW findings (clean-as-you-code Ch 19/Part IX key 80); warn/triage the rest to avoid gate fatigue (Ch 19 key 39); security findings often route to a security reviewer, not pure auto-block. DevSecOps: security is everyone's, shifted left (Ch 1 key 06), automated as gates, with SBOM/provenance (Ch 28 key 66) for the supply side. LIMITS: GATE FATIGUE is the killer (noisy blocking-everything → bypassed/disabled; tune to high-severity-new + triage; a gate routed around is a net negative); tools don't find logic/authz flaws (broken access control / business-logic → threat modeling + review + tests Ch 30/84); DAST/IAST cost+setup (deployed app + scenarios; slow; not every team — when-NOT small internal); FPs across the stack compound (need ownership + triage Ch 19); GREEN GATE ≠ SECURE (means "no DETECTED known issues"; pen-testing/threat-modeling still matter; not a compliance guarantee).
⚠ verify-at-pin: DAST/IAST tool specifics (OWASP ZAP) + CI integration; severity-threshold/block-policy config per tool; CI platform specifics. ⚠ AHEAD-OF-PIN: none material. SOURCE-PIN: OWASP DevSecOps/ASVS, ZAP, tool rows TO-PIN. REPRO: security tools + CI network-gated → REPRO PENDING-RUNTIME offline.
Routes: SAST → Ch 31 (70); SCA → Ch 28 (65); secrets → Ch 31 (71); SBOM/supply-chain → Ch 28 (66); secure-coding classes / threat modeling → Ch 30 (69) + design review key 84; the GENERAL quality gate (pipeline/policy/performance) → Part IX Ch 33 (75/76/79); clean-as-you-code / new-code gating → Part IX (80); suppression/triage → Ch 19 (39); fitness functions → Ch 26 (56); release/staging → key 83; shift-left → Ch 1 (06).
DRAFT v1 — gates manual; security-gate-as-fitness-function + five-testing-types(SAST/SCA/secrets/DAST/IAST) + fast→slow-gate-ordering + block-vs-warn-policy + gate-fatigue-is-the-killer + green-gate≠secure shapes; PART VIII CLOSER (hand-off opens Part IX — CI/CD & Quality Gates, Ch 33 keys 75+76+79). EXAMPLE-BUILD pending.
-->

# Making the Security Gate Stick

*Assembling SAST, SCA, secrets, and DAST into one automated gate — and the block-versus-warn policy that keeps it from being disabled · 73 · Part VIII (closer)*

> Turn every security tool on as a hard blocker and within two weeks the build is permanently red and someone has added `continue-on-error: true`. A security gate the team routes around protects nothing.

## Hook

A team finishes Part VIII's toolbox: SAST on the code, SCA on the dependencies, secrets scanning on every commit, and a DAST scan the team is proud of. Eager, the team wires all of it into CI set to **block on any finding**. Two weeks later the gate reports eight hundred findings (most false positives, pre-existing debt, or low-severity noise), the build is permanently red, nobody can merge, and a senior engineer quietly adds `continue-on-error: true` to the security job to unblock the team. The gate is now decorative: it runs, it reports, and nothing depends on it. This is precisely how the static-analysis gate died back in Chapter 19, now wearing a security badge: death by **gate fatigue**.

That failure, and how to avoid it, is this closing chapter of Part VIII. The tools are not the hard part; the toolbox from Chapters 28, 30, and 31 is assembled. The hard part is wiring those tools into a **security gate** that *sticks*, one that runs automatically on every change, blocks what genuinely must be blocked, and is trusted enough that no one disables it. This chapter frames the five security testing types (SAST, SCA, secrets, and the two dynamic kinds, DAST and IAST), orders them across the pipeline fast-to-slow, and sets the **block-versus-warn policy** that separates a gate that protects the codebase from one the team learns to bypass. This is the security instance of the general quality gate that Part IX will generalize, and a security fitness function (Chapter 26) for the ISO 25010 Security characteristic. The governing principle, carried from Chapter 19 and now applied to security: *a gate the team routes around is a net negative, manufacturing the belief that something is being checked when nothing is.*

## Overview

**What this chapter covers**

- The **five testing types** — SAST, SCA, secrets, DAST, IAST — what each covers and where it runs.
- **Gate ordering**: fast checks at the pull request, slow dynamic scans later against staging.
- The **block-versus-warn policy**: blocking high-severity new findings while triaging the rest, the discipline that prevents gate fatigue.
- The **DevSecOps** frame, and the honest limits: a green gate is not "secure."

**What this chapter does NOT cover.** The individual tools in depth — SAST and secrets (Chapter 31), SCA and the supply chain (Part VII). The secure-coding classes the tools detect (Chapter 30). The *general* quality-gate machinery — pipeline design, gate policy, and gate performance — which Part IX owns (this is its security instance). New-code/clean-as-you-code gating mechanics (Part IX). DAST/IAST tool specifics are named, with details verified at the pin; the tools are presented neutrally.

**One idea to hold:** *assembling the security tools is the straightforward part; making the gate stick is the discipline. Order them fast-to-slow, block only high-severity new findings and triage the rest, route real findings to a reviewer, and keep in mind that a green gate means "no detected known issues," not "secure."*

## How it works

![Fig 73.1 — The security gate: five testing types ordered fast-to-slow — Static checks (secrets, SAST, SCA) run at pre-commit and PR; dynamic checks (DAST, IAST) gate the release against staging. Block only high-severity new findings; warn and triage the rest.](../../05-figures/73_security_in_ci/fig73_1.png)

*Fig 73.1 — The security gate: five testing types ordered fast-to-slow — Static checks (secrets, SAST, SCA) run at pre-commit and PR; dynamic checks (DAST, IAST) gate the release against staging. Block only high-severity new findings; warn and triage the rest.*


### The five testing types

A security gate layers complementary lenses, because each is blind to what the others see:

| Type | What it analyzes | When it runs | Catches |
|---|---|---|---|
| **SAST** (static) | the application's own code (Chapter 31) | PR / build | injection, crypto misuse, unsafe deserialization — early |
| **SCA** (composition) | the project's dependencies (Part VII) | build + continuous | known CVEs in libraries; license issues |
| **Secrets scanning** | code, diffs, history (Chapter 31) | pre-commit + CI + push protection | leaked credentials |
| **DAST** (dynamic) | the *running* app (e.g. OWASP ZAP) | later stage, vs staging | runtime and configuration flaws static tools cannot see |
| **IAST** (interactive) | the running app, *instrumented during tests* | test stage | vulnerabilities with runtime context — a SAST/DAST hybrid |

The static trio (SAST, SCA, secrets) analyzes artifacts without running them and is fast enough for the pull request. The dynamic pair (DAST, IAST) needs a deployed, running application: DAST probes it black-box from outside, catching the runtime misconfiguration and the environment-specific flaw that no static tool can reach; IAST instruments the app while its tests run, adding runtime context to reduce the false positives static analysis suffers. Static analysis misses what only happens at runtime; dynamic analysis misses code paths the tests never exercise. A thorough program layers them rather than picking one.

### Ordering the gate: fast-to-slow, shift-left

The ordering principle is the same cheap-first, fail-fast logic from the analyzer and build chapters, applied to security:

> **CONCEPT** *Run the cheap, common-failure checks earliest.* **Secrets scanning** goes at pre-commit (the only stage that *prevents* a leak) and again in CI. **SAST and SCA** run at the **pull request**, fast enough for every change, blocking on high severity. **DAST and IAST** run in a **later pipeline stage against staging**, because they need a deployed app and are slow; they gate the release, not the PR. Container and infrastructure-as-code scanning slot in where applicable. The cheap security feedback arrives in minutes on the PR; the expensive dynamic feedback gates the release. The slow scan's cost never falls on every commit.

Wired into a pipeline, secrets scanning is the first stage, running before any other so a leak is caught before it spreads:

<!-- include: 73_security_in_ci/ci/security-pipeline.yml#pre-commit-secrets -->

The fast static trio then runs on the pull request, where blocking only on high severity keeps the feedback in minutes:

<!-- include: 73_security_in_ci/ci/security-pipeline.yml#pr-fast-trio -->

Container and infrastructure-as-code scanning slot in for the teams that ship them, and skip cleanly for the teams that do not:

<!-- include: 73_security_in_ci/ci/security-pipeline.yml#container-iac-scan -->

The dynamic pair runs last, against staging, gating the release rather than the pull request because it needs a deployed app:

<!-- include: 73_security_in_ci/ci/security-pipeline.yml#staging-dynamic -->

This is the **DevSecOps** frame: security is not a separate team's late-stage audit but everyone's responsibility, shifted left (Chapter 1) and automated as gates in the same pipeline as every other quality check, with the SBOM and provenance layer (Part VII) covering the supply side. The security gate is, in the language of Chapter 26, a *portfolio of security fitness functions* — each testing type an automated, continuous assessment of one security characteristic.

### The policy that makes it stick: block vs warn

Whether the gate survives is a *policy* decision, not a tool decision. Turn every finding into a hard block and the result is the hook's eight-hundred-finding dead gate. The discipline is to **block narrowly and warn broadly**:

> **CONCEPT** *Block high-severity new findings; triage the rest.* The gate should **fail the build only on high-severity findings in new or changed code**, applying the clean-as-you-code scoping from Chapter 19 to security: a pull request does not get blocked on a thousand pre-existing low-severity findings it did not introduce. Everything else **warns** and is **triaged**, routed to a backlog or to a security reviewer who decides. Security findings in particular often go to a human reviewer rather than pure auto-block, because exploitability is a judgment (an unreachable sink, a non-security `MD5`) that a severity number alone does not capture. The trusted gate fails *only* when something genuinely new and serious is wrong; that precision is what keeps it from being disabled.

That policy is externalized per profile rather than compiled in, so the feature-branch gate and the release gate can differ:

<!-- include: 73_security_in_ci/src/main/java/org/acme/secgate/SecurityGatePolicy.java#gate-policy -->

The verdict it returns is three-way, not two, and the middle path is what routes a finding to a reviewer instead of auto-blocking:

<!-- include: 73_security_in_ci/src/main/java/org/acme/secgate/SecurityGateDecision.java#block-vs-warn -->

The gate aggregates the findings every stage produced, scopes them to new code, then blocks only on the new, high-severity, exploitable ones:

<!-- include: 73_security_in_ci/src/main/java/org/acme/secgate/SecurityGate.java#aggregate-and-gate -->

The false-positive discipline from Chapter 19 multiplies here: false positives compound *across* the stack (SAST noise plus SCA noise plus secrets-entropy noise), so without ownership, triage, and justified suppression, the combined security gate becomes pure noise faster than any single tool would. A gate with a credible, narrow blocking policy and a triaged warning stream is one the team keeps green honestly; a gate that blocks on everything is one the team blinds.

## Deep dive: the gate is the assembly, not the tools

This chapter closes Part VIII not by introducing anything new, but because *the gate is where the security program becomes real*. Chapters 28, 30, and 31 supplied the pieces: design out the classes, scan dependencies, run SAST, catch secrets. A security control that runs when someone remembers to run it, or that runs and reports into a void, protects nothing. The shift-left thesis of the whole book (automate, do not trust memory) reaches its security conclusion here: the value of every Part VIII tool is realized only when it runs *automatically, on every change, with consequences*. The gate is that automation, and assembling it well is a distinct skill from operating any individual tool.

Three decisions determine whether the assembly works. *Which lenses:* the five types, layered because each is blind to the others' findings, with the static trio fast and the dynamic pair slow. *In what order:* fast-to-slow across the pipeline, so cheap feedback is immediate and expensive feedback gates the release. *With what policy:* block high-severity new, warn-and-triage the rest, route to a reviewer. The first two are engineering; the third is what decides survival, carrying the same lesson Chapter 19 taught for the static-analysis gate, now non-negotiable because the cost of a disabled *security* gate is a breach. A security gate is not a technical artifact to install; it is a policy to tune and a credibility to maintain.

The honest center: **a green security gate does not mean the application is secure.** It means *no detected known issue,* and every word of that qualifier is a limit. *Detected:* the tools find patterns, not the broken-access-control and business-logic flaws that need threat modeling and design review (Chapters 30, 84) and that cause the highest-severity breaches. *Known:* SCA and the CVE databases cover disclosed vulnerabilities, not zero-days. *Issue:* a clean scan is not a penetration test, a threat model, or a compliance certification. The gate is necessary and high-value, catching automatically and cheaply the large volume of pattern-matchable known vulnerabilities that cause most *incidents.* It is one layer in a posture that also includes secure design (Chapter 30), human review (Chapter 84), and periodic expert assessment. Treating "the security gate is green" as "we are secure" is the same category error as treating "100% coverage" as "well-tested" (Chapter 23): a strong signal mistaken for a proof. The mature stance is to run the gate, keep it credible by blocking narrowly, and never let its green light substitute for the judgment and design it cannot replace. Automated, layered, narrowly blocking, and honestly bounded: that is what the security half of the book delivers, and it is, as the next part will show, exactly the shape of every quality gate.

## Limitations & when NOT to reach for it

- **Gate fatigue is the killer.** A noisy, blocks-on-everything security gate gets bypassed or disabled. A gate the team routes around is a net negative, manufacturing false assurance. Tune to high-severity-new-code blocking plus triage; this is the single most important design decision.
- **A green gate is not "secure."** It means no *detected known* issue: tools miss logic and authorization flaws, CVE databases miss zero-days, and a scan is not a pen-test or a compliance certificate. Do not read the green light as a guarantee.
- **Static tools cannot find design and authorization flaws.** Broken access control and business-logic abuse need threat modeling, design review, and tests (Chapters 30, 84), not scanners; these are often the highest-severity breaches.
- **DAST/IAST cost and setup are real.** They need a deployed app and test scenarios and are slow; a small internal app may rationally skip them. They gate the release stage, not the PR.
- **False positives compound across the stack.** SAST + SCA + secrets noise multiplies; without ownership, triage, and justified suppression (Chapter 19), the combined gate becomes noise fast.
- **Security findings need human judgment.** Exploitability (reachability, context) is often a reviewer's call, not a severity number's — pure auto-block on every finding misjudges as often as it protects.
- **Automated gates complement, not replace, periodic assessment.** Pen-testing and threat modeling remain necessary; the gate catches the cheap, known, high-volume classes, not the bespoke attack.

## Alternatives & adjacent approaches

- **Penetration testing and threat modeling** — periodic, human, design-level assessment that catches the logic and authorization flaws the automated gate structurally cannot; complementary, not a contest.
- **Security reviewer in the loop** — routing findings to a human for exploitability judgment rather than pure auto-block, the middle path between blocking everything and blocking nothing.
- **DAST vs IAST** — black-box dynamic scanning versus instrumented runtime analysis; IAST reduces false positives with runtime context but needs instrumentation, DAST needs only a running endpoint.
- **The general quality gate** (Part IX) — the machinery this security gate is an instance of; the pipeline, policy, and performance patterns generalize across security and non-security checks alike.
- **Compliance frameworks** (driven by the mandates in Part VII) — where a security gate is a *required* control, not a discretionary one.

These compose into a security posture: the automated gate for the high-volume known classes, human review and threat modeling for the design flaws, and periodic pen-testing for assurance — layered, because no single control is sufficient.

## When to use what

- **For fast per-change security feedback:** secrets + SAST + SCA at the pull request, blocking on high-severity new findings.
- **For runtime and configuration flaws:** DAST (and IAST where instrumentation is feasible) in a later stage against staging — release gate, not PR gate.
- **To keep the gate credible:** block narrowly (high-severity, new code), warn and triage the rest, and own the false-positive suppression.
- **For exploitability judgment:** route findings to a security reviewer rather than auto-blocking every severity number.
- **For the supply side:** the SBOM, SCA continuous monitoring, and provenance from Part VII, wired into the same pipeline.
- **For the flaws the gate cannot see:** threat modeling, design review, and periodic pen-testing — the gate is a layer, not the whole.
- **When the gate is failing constantly on old findings:** fix the *policy* (scope to new code), not the tools — gate fatigue is a tuning problem.

## Hand-off to the next part

This chapter built the *security* gate and in doing so kept describing something more general: checks wired into the pipeline, ordered fast-to-slow, with a block-versus-warn policy scoped to new code, tuned to avoid the fatigue that gets gates disabled. That is not a security-specific pattern; it is *the* pattern for every quality gate in this book. The formatter, the analyzers, the architecture rules, the tests, coverage, mutation, and now security all run in CI, all face the same ordering, policy, and fatigue questions, and all live or die by the same discipline. **Part IX: CI/CD & Quality Gates** generalizes it, designing the CI pipeline, the quality-gate policy (what blocks, what warns, clean-as-you-code), and gate *performance* (caching, parallelism) so the whole gate stays fast enough that developers do not route around it. The next chapter opens with pipeline and gate design, of which the security gate assembled here is one instance.

## Back matter — sources & traceability

- **Security gate** (key 73; OWASP DevSecOps/ASVS; tool docs keys 65/70/71; CI platform docs): security controls protect only if automated every-change; assemble Part-VIII tools into a CI gate (what runs / order / block-vs-warn). Security instance of the general gate (Part IX 75/76); a fitness function (Ch 26 56) for ISO Security (Ch 1 01). **Five types**: SAST (static, your code, Ch 31 70), SCA (deps, Ch 28 65 + license Ch 29 68), secrets (Ch 31 71), DAST (dynamic, running app, OWASP ZAP), IAST (interactive, instrumented). **Ordering** fast→slow: secrets+SAST+SCA at PR (block high severity), DAST/IAST later vs staging (key 83), container/IaC (Trivy) if applicable. **Block-vs-warn policy**: block high-severity NEW (clean-as-you-code Ch 19/Part IX 80), warn/triage rest (avoid gate fatigue Ch 19 39), route to security reviewer. **DevSecOps** (shift-left Ch 1 06; SBOM/provenance supply side Ch 28 66). *(frame verified; DAST/IAST/ZAP specifics + severity-threshold config + CI specifics ⚠ @pin; tools+CI network-gated → REPRO PENDING-RUNTIME.)*
- **Limits**: gate-fatigue-is-the-killer (routed-around is a net negative); green-gate≠secure ("no detected known issue" — tools-miss-logic/authz → threat modeling + review Ch 30/84; CVE-DBs-miss-zero-days; not-a-pen-test/compliance); DAST/IAST cost+setup (when-NOT small internal); FPs-compound-across-stack (ownership+triage Ch 19); security-findings-need-human-judgment.
- **Routing** — SAST → Ch 31 (70); SCA → Ch 28 (65); secrets → Ch 31 (71); SBOM/supply chain → Ch 28 (66); secure-coding/threat-modeling → Ch 30 (69) + review key 84; **general quality gate (pipeline/policy/performance) → Part IX Ch 33 (75/76/79)**; clean-as-you-code/new-code → Part IX (80); suppression/triage → Ch 19 (39); fitness functions → Ch 26 (56); release/staging → key 83; shift-left → Ch 1 (06). SOURCE-PIN: OWASP DevSecOps/ASVS, ZAP, tool rows TO-PIN.

**Companion module (spec — ⚠ EXAMPLE-BUILD = PENDING; toolchain READY; tools + CI network-gated → REPRO PENDING-RUNTIME):** a CI workflow (GitHub Actions or generic) running **secrets** (gitleaks, Ch 31) + **SAST** (FindSecBugs/Semgrep, Ch 31) + **SCA** (OWASP Dependency-Check, Ch 28) over the flagship, with a **severity-threshold block on NEW findings only** and a warn/triage stream for the rest. **Failure path:** a seeded high-severity new finding (an injection sink or a planted key) fails the gate; pre-existing low-severity findings only warn. **Honest edges (comments):** the gate is scoped to high-severity-new to avoid fatigue (blocking-all would be disabled — Ch 19); a deliberately-included broken-access-control flaw is NOT caught by any tool (needs review, Ch 84); the green gate means "no detected known issue", not "secure". DAST is described (needs a deployed app) but the runnable example is the static trio; this is the security instance of the Part IX general gate.

**Companion module (BUILT — `08-companion-code/73_security_in_ci/`, `mvn -B -Pquality verify` green: 14 tests, 0 Checkstyle, 0 SpotBugs).** Two artifacts in lock-step: an illustrative CI pipeline (`ci/security-pipeline.yml`) wiring the five security stages fast-to-slow with their fail thresholds, and a runnable, unit-tested gate **policy** (`org.acme.secgate.SecurityGate`) — the local equivalent — that aggregates the stages' findings into a three-way decision (block / route-to-review / pass), scoped to new code. The failure path is the sealed `SecurityGateDecision`: a new exploitable HIGH finding blocks; a severe-but-unproven or sub-blocking finding routes to a security reviewer; pre-existing debt passes. The green-gate-is-not-secure edge is a test: a broken-access-control flaw that no stage produces lets the gate pass. The SaaS/unpinned tools in the YAML (GitHub Actions, gitleaks, CodeQL, OWASP ZAP) are dated-at-use 2026-06 and flagged (`09-flags/73_security_pipeline_saas_dated_at_use.md`); DAST against staging is REPRO PENDING-RUNTIME (needs a live deployment).

**Snippet tags:** `pre-commit-secrets`, `pr-fast-trio`, `container-iac-scan`, `staging-dynamic` (security stages, `ci/security-pipeline.yml`); `gate-policy` (`SecurityGatePolicy.java`), `block-vs-warn` (`SecurityGateDecision.java`), `aggregate-and-gate` (`SecurityGate.java`) — the gate policy. Seven tag-regions, each ≤9 lines, displayed via `<!-- include: -->` markers; the printed listing and the buildable file are one artifact.

## Next chapter teaser

Building the security gate required three decisions: which checks to run, in what order, and what blocks versus warns with what scope. Those turn out to be the decisions behind *every* gate in the book. Part IX generalizes them: designing the CI pipeline, the quality-gate policy (clean-as-you-code, block-versus-warn), and gate performance (caching, parallelism) so the whole quality program runs on every change without the slowness or noise that gets gates disabled. The security gate assembled here is one instance of the pattern the next part makes general.
