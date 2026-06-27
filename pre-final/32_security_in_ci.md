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

The whole gate fits one picture: the five testing types laid out left-to-right in the order they run, the fast static checks at pre-commit and the pull request, the slow dynamic checks deferred to a stage against staging, and one block-versus-warn line drawn through all of them. Figure 32.1 is that map; the sections below walk it lens by lens.

![Fig 32.1 — The security gate: five testing types ordered fast-to-slow — Static checks (secrets, SAST, SCA) run at pre-commit and PR; dynamic checks (DAST, IAST) gate the release against staging. Block only high-severity new findings; warn and triage the rest.](figures/fig73_1.png)

*Fig 32.1 — The security gate: five testing types ordered fast-to-slow — Static checks (secrets, SAST, SCA) run at pre-commit and PR; dynamic checks (DAST, IAST) gate the release against staging. Block only high-severity new findings; warn and triage the rest.*

### The five testing types

A security gate layers complementary lenses, because each is blind to what the others see:

| Type | What it analyzes | When it runs | Catches |
|---|---|---|---|
| **SAST** — static application security testing | the application's own code (Chapter 31) | PR / build | injection, crypto misuse, unsafe deserialization — early |
| **SCA** — software composition analysis | the project's dependencies (Part VII) | build + continuous | known CVEs in libraries; license issues |
| **Secrets scanning** | code, diffs, history (Chapter 31) | pre-commit + CI + push protection | leaked credentials |
| **DAST** — dynamic application security testing | the *running* app (e.g. OWASP ZAP) | later stage, vs staging | runtime and configuration flaws static tools cannot see |
| **IAST** — interactive application security testing | the running app, *instrumented during tests* | test stage | vulnerabilities with runtime context — a SAST/DAST hybrid |

The static trio (SAST, SCA, secrets) analyzes artifacts without running them and is fast enough for the pull request. The dynamic pair (DAST, IAST) needs a deployed, running application: DAST probes it black-box from outside, catching the runtime misconfiguration and the environment-specific flaw that no static tool can reach; IAST instruments the app while its tests run, adding runtime context to reduce the false positives static analysis suffers. Static analysis misses what only happens at runtime; dynamic analysis misses code paths the tests never exercise. A thorough program layers them rather than picking one.

### Ordering the gate: fast-to-slow, shift-left

The ordering principle is the same cheap-first, fail-fast logic from the analyzer and build chapters, applied to security:

> **CONCEPT** *Run the cheap, common-failure checks earliest.* **Secrets scanning** goes at pre-commit (the only stage that *prevents* a leak) and again in CI. **SAST and SCA** run at the **pull request**, fast enough for every change, blocking on high severity. **DAST and IAST** run in a **later pipeline stage against staging**, because they need a deployed app and are slow; they gate the release, not the PR. Container and infrastructure-as-code scanning slot in where applicable. The cheap security feedback arrives in minutes on the PR; the expensive dynamic feedback gates the release. The slow scan's cost never falls on every commit.

Wired into a pipeline, secrets scanning is the first stage, running before any other so a leak is caught before it spreads:

```yaml
      - name: Secrets scan (the leak-prevention stage, runs first and fast)
        run: gitleaks detect --redact      # gitleaks: no pinned version (dated-at-use 2026-06); also a pre-commit hook
```

The fast static trio then runs on the pull request, where blocking only on high severity keeps the feedback in minutes:

```yaml
      - name: SAST (the codebase lens, Chapter 31)
        run: mvn -B -Pquality verify       # static analysis; the local equivalent of this stage
      - name: SCA — known-CVE dependency scan (Part VII)
        run: mvn -B org.owasp:dependency-check-maven:check   # OWASP Dependency-Check 12.2.2; fails above CVSS 7.0
```

Container and infrastructure-as-code scanning slot in for the teams that ship them, and skip cleanly for the teams that do not:

```yaml
      - name: Container + IaC scan (Trivy), where the team ships them
        run: trivy fs --severity HIGH,CRITICAL .   # Trivy 0.71.0 (SOURCE-PIN §4); skip if no containers/IaC
```

The dynamic pair runs last, against staging, gating the release rather than the pull request because it needs a deployed app:

```yaml
      - name: DAST against staging (black-box, needs a deployed app)
        run: zap-baseline.py -t "$STAGING_URL"   # OWASP ZAP; dated-at-use 2026-06; gates the release, not the PR
```

This is the **DevSecOps** frame: security is not a separate team's late-stage audit but everyone's responsibility, shifted left (Chapter 1) and automated as gates in the same pipeline as every other quality check, with the SBOM and provenance layer (Part VII) covering the supply side. The security gate is, in the language of Chapter 26, a *portfolio of security fitness functions* — each testing type an automated, continuous assessment of one security characteristic.

### The policy that makes it stick: block vs warn

Whether the gate survives is a *policy* decision, not a tool decision. Turn every finding into a hard block and the result is the hook's eight-hundred-finding dead gate. The discipline is to **block narrowly and warn broadly**:

> **CONCEPT** *Block high-severity new findings; triage the rest.* The gate should **fail the build only on high-severity findings in new or changed code**, applying the clean-as-you-code scoping from Chapter 19 to security: a pull request does not get blocked on a thousand pre-existing low-severity findings it did not introduce. Everything else **warns** and is **triaged**, routed to a backlog or to a security reviewer who decides. Security findings in particular often go to a human reviewer rather than pure auto-block, because exploitability is a judgment (an unreachable sink, a non-security `MD5`) that a severity number alone does not capture. The trusted gate fails *only* when something genuinely new and serious is wrong; that precision is what keeps it from being disabled.

That policy is externalized per profile rather than compiled in, so the feature-branch gate and the release gate can differ:

```java
public record SecurityGatePolicy(
        boolean cleanAsYouCode, Severity blockSeverity, boolean requireExploitableToBlock) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "secgate.profile";
    private static final String DEFAULT_PROFILE = "dev";
```

The verdict it returns is three-way, not two, and the middle path is what routes a finding to a reviewer instead of auto-blocking:

```java
public sealed interface SecurityGateDecision
        permits SecurityGateDecision.Pass, SecurityGateDecision.Review, SecurityGateDecision.Block {
    record Pass(String reason) implements SecurityGateDecision { }   // nothing to act on — merge may proceed
    record Review(String reason) implements SecurityGateDecision { } // routed to a security reviewer, not auto-blocked
    record Block(String reason) implements SecurityGateDecision { }  // new + high-severity + exploitable: fail build
}
```

The gate aggregates the findings every stage produced, scopes them to new code, then blocks only on the new, high-severity, exploitable ones:

```java
        List<SecurityFinding> inScope = findings.stream()
            .filter(f -> !policy.cleanAsYouCode() || f.scope() == FindingScope.NEW)   // new code only
            .toList();
        SecurityFinding worstBlocking = inScope.stream()
            .filter(f -> f.severity().meetsOrExceeds(policy.blockSeverity()))         // block narrowly
            .filter(f -> f.exploitable() || !policy.requireExploitableToBlock())      // exploitable, else review
            .max(Comparator.comparing(SecurityFinding::severity))
            .orElse(null);
```

The false-positive discipline from Chapter 19 multiplies here: false positives compound *across* the stack (SAST noise plus SCA noise plus secrets-entropy noise), so without ownership, triage, and justified suppression, the combined security gate becomes pure noise faster than any single tool would. A gate with a credible, narrow blocking policy and a triaged warning stream is one the team keeps green honestly; a gate that blocks on everything is one the team blinds.

## Deep dive: the gate is the assembly, not the tools

This chapter closes Part VIII not by introducing anything new, but because *the gate is where the security program becomes real*. Chapters 28, 30, and 31 supplied the pieces: design out the classes, scan dependencies, run SAST, catch secrets. A security control that runs when someone remembers to run it, or that runs and reports into a void, protects nothing. The shift-left thesis of the whole book (automate, do not trust memory) reaches its security conclusion here: the value of every Part VIII tool is realized only when it runs *automatically, on every change, with consequences*. The gate is that automation, and assembling it well is a distinct skill from operating any individual tool.

Three decisions determine whether the assembly works. *Which lenses:* the five types, layered because each is blind to the others' findings, with the static trio fast and the dynamic pair slow. *In what order:* fast-to-slow across the pipeline, so cheap feedback is immediate and expensive feedback gates the release. *With what policy:* block high-severity new, warn-and-triage the rest, route to a reviewer. The first two are engineering; the third is what decides survival, carrying the same lesson Chapter 19 taught for the static-analysis gate, now non-negotiable because the cost of a disabled *security* gate is a breach. A security gate is not a technical artifact to install; it is a policy to tune and a credibility to maintain.

The honest center: **a green security gate does not mean the application is secure.** It means *no detected known issue,* and every word of that qualifier is a limit. *Detected:* the tools find patterns, not the broken-access-control and business-logic flaws that need threat modeling and design review (Chapters 30, 37) and that cause the highest-severity breaches. *Known:* SCA and the CVE databases cover disclosed vulnerabilities, not zero-days. *Issue:* a clean scan is not a penetration test, a threat model, or a compliance certification. The gate is necessary and high-value, catching automatically and cheaply the large volume of pattern-matchable known vulnerabilities that cause most *incidents.* It is one layer in a posture that also includes secure design (Chapter 30), human review (Chapter 37), and periodic expert assessment. Treating "the security gate is green" as "we are secure" is the same category error as treating "100% coverage" as "well-tested" (Chapter 23): a strong signal mistaken for a proof. The mature stance is to run the gate, keep it credible by blocking narrowly, and never let its green light substitute for the judgment and design it cannot replace. Automated, layered, narrowly blocking, and honestly bounded: that is what the security half of the book delivers, and it is, as the next part will show, exactly the shape of every quality gate.

## Limitations & when NOT to reach for it

- **Gate fatigue is the killer.** A noisy, blocks-on-everything security gate gets bypassed or disabled. A gate the team routes around is a net negative, manufacturing false assurance. Tune to high-severity-new-code blocking plus triage; this is the single most important design decision.
- **A green gate is not "secure."** It means no *detected known* issue: tools miss logic and authorization flaws, CVE databases miss zero-days, and a scan is not a pen-test or a compliance certificate. Do not read the green light as a guarantee.
- **Static tools cannot find design and authorization flaws.** Broken access control and business-logic abuse need threat modeling, design review, and tests (Chapters 30, 37), not scanners; these are often the highest-severity breaches.
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

- **Security gate** (key 73; OWASP DevSecOps/ASVS; tool docs keys 65/70/71; CI platform docs): security controls protect only if automated every-change; assemble Part-VIII tools into a CI gate (what runs / order / block-vs-warn). Security instance of the general gate (Part IX 75/76); a fitness function (Ch 26 56) for ISO Security (Ch 1 01). **Five types**: SAST (static, your code, Ch 31 70), SCA (deps, Ch 28 65 + license Ch 29 68), secrets (Ch 31 71), DAST (dynamic, running app, OWASP ZAP), IAST (interactive, instrumented). **Ordering** fast→slow: secrets+SAST+SCA at PR (block high severity), DAST/IAST later vs staging (key 83), container/IaC (Trivy) if applicable. **Block-vs-warn policy**: block high-severity NEW (clean-as-you-code Ch 19/Part IX 80), warn/triage rest (avoid gate fatigue Ch 19 39), route to security reviewer. **DevSecOps** (shift-left Ch 1 06; SBOM/provenance supply side Ch 28 66). *(frame verified; block-policy / severity-threshold MECHANICS realized + unit-tested in the BUILT module `org.acme.secgate.SecurityGate`; Trivy 0.71.0 + OWASP Dependency-Check 12.2.2 PINNED (§4); DAST/IAST/ZAP tool specifics + GitHub Actions @v4 + gitleaks ⚠ dated-at-use 2026-06, flagged; DAST against staging ⚠ REPRO PENDING-RUNTIME — needs a live deployment.)*
- **Limits**: gate-fatigue-is-the-killer (routed-around is a net negative); green-gate≠secure ("no detected known issue" — tools-miss-logic/authz → threat modeling + review Ch 30/84; CVE-DBs-miss-zero-days; not-a-pen-test/compliance); DAST/IAST cost+setup (when-NOT small internal); FPs-compound-across-stack (ownership+triage Ch 19); security-findings-need-human-judgment.
- **Routing** — SAST → Ch 31 (70); SCA → Ch 28 (65); secrets → Ch 31 (71); SBOM/supply chain → Ch 28 (66); secure-coding/threat-modeling → Ch 30 (69) + review key 84; **general quality gate (pipeline/policy/performance) → Part IX Ch 33 (75/76/79)**; clean-as-you-code/new-code → Part IX (80); suppression/triage → Ch 19 (39); fitness functions → Ch 26 (56); release/staging → key 83; shift-left → Ch 1 (06). SOURCE-PIN: OWASP DevSecOps/ASVS; Trivy 0.71.0 + OWASP Dependency-Check 12.2.2 PINNED (§4); GitHub Actions rolling (§5); OWASP ZAP + gitleaks have no SOURCE-PIN row → dated-at-use 2026-06, flagged.

**Snippet tags:** `pre-commit-secrets`, `pr-fast-trio`, `container-iac-scan`, `staging-dynamic` (security stages, `ci/security-pipeline.yml`); `gate-policy` (`SecurityGatePolicy.java`), `block-vs-warn` (`SecurityGateDecision.java`), `aggregate-and-gate` (`SecurityGate.java`) — the gate policy. Seven tag-regions, each ≤9 lines, displayed via `` markers; the printed listing and the buildable file are one artifact.

## Next chapter teaser

Building the security gate required three decisions: which checks to run, in what order, and what blocks versus warns with what scope. Those turn out to be the decisions behind *every* gate in the book. Part IX generalizes them: designing the CI pipeline, the quality-gate policy (clean-as-you-code, block-versus-warn), and gate performance (caching, parallelism) so the whole quality program runs on every change without the slowness or noise that gets gates disabled. The security gate assembled here is one instance of the pattern the next part makes general.
