# Chapter 32 — Making the Security Gate Stick (`security-in-ci`)

The security gate, at the smallest size that still teaches it. This is the CI-integration view of Part
VIII: not the individual tools (SAST and secrets detection are Chapter 31; SCA and the supply chain are
Part VII), but how the security **stages** are ordered across the pipeline and the **policy** that
aggregates their findings into one merge decision. It is a child module of the companion-code reactor; it
adds no version literals and inherits the runtime and test-library pins from the aggregator. It has
**zero runtime dependencies** — every surface is built on the JDK alone.

## What this module is, and is not

Two artifacts, one module, kept in lock-step:

- **`ci/security-pipeline.yml`** — an illustrative GitHub Actions pipeline that wires the five security
  testing types fast-to-slow (shift-left): **secrets** scanning at pre-commit and CI (the only stage that
  prevents a leak), the fast **SAST + SCA + secrets** trio at the **pull request** blocking on high
  severity, a **container/IaC** scan where applicable, and the slow **DAST/IAST** dynamic pair against
  **staging** that gates the release rather than the pull request. This file is **configuration** — it is
  **not** run by this module's Maven build. Its stages run the same `mvn` commands a developer runs
  locally.

- **The gate policy in `org.acme.secgate`** — the load-bearing decision the chapter argues for, made
  runnable and unit-tested: `SecurityGate.evaluate` pools the findings every stage produced, scopes them
  to new code (clean-as-you-code), blocks on new high-severity exploitable findings, and routes the rest
  to a security reviewer. This is the **local equivalent** of the CI security gate, so `mvn -Pquality
  verify` exercises both the same static-analysis stage the YAML names and the aggregation logic the YAML
  wires — the two cannot silently drift (Chapter 27, local/CI parity).

This module deliberately does **not** re-teach the SAST/secrets tool internals (Chapter 31's module) or
the general quality-gate machinery (Chapter 33's module). Its subject is the *assembly*: the security
stages and their gating policy.

Action and tool versions are pinned where `00-strategy/SOURCE-PIN.md` pins them (the Java 21 anchor; OWASP
Dependency-Check 12.2.2 and Trivy 0.71.0; the analyzers in the pom). The GitHub Actions in the YAML
(`actions/checkout`, `actions/setup-java`) and the hosted scanners (Semgrep, CodeQL, gitleaks, OWASP ZAP)
are hosted/SaaS or unpinned — SOURCE-PIN §5 records GitHub Actions as "docs as of 2026-06 (rolling)" — so
they are **dated at use (2026-06)**, not timeless, and are flagged for digest pinning at adoption (see
`09-flags/`).

## What it demonstrates

| Teaching | Where |
|---|---|
| The five testing types (secrets, SAST, SCA, container/IaC, DAST/IAST) | `SecurityStage`; `ci/security-pipeline.yml` jobs |
| Fast-to-slow stage ordering (static at PR, dynamic at staging) | `SecurityStage.isStatic`; `ci/security-pipeline.yml` `secrets` → `pr-static-trio` → `staging-dynamic` |
| Aggregating many stages into one gate decision | `SecurityGate.evaluate` |
| Block-versus-review policy (the security-reviewer route) | `SecurityGateDecision` (sealed: `Pass` / `Review` / `Block`) |
| Clean-as-you-code (new vs whole-repo) | `SecurityGate.evaluate`, `SecurityGatePolicy.cleanAsYouCode` |
| Exploitability is a judgment, not a severity number | `SecurityFinding.exploitable`, `SecurityGatePolicy.requireExploitableToBlock` |
| Externalized config profiles (`dev` / `prod`) | `SecurityGatePolicy`, `secgate-*.properties` |

## Build and run

```
# fast build (compile + tests), standalone
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs) — the local equivalent of the SAST stage
mvn -B -Pquality -f pom.xml verify

# select a gate-policy profile (default dev)
mvn -B -Dsecgate.profile=prod -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The CI YAML is
configuration and is validated separately as parseable YAML, not by this build.

## The failure path

`SecurityGate.evaluate` is the explicit failure path: it returns a `SecurityGateDecision` — a sealed type
with `Pass`, `Review`, and `Block` variants — rather than a bare boolean. The three variants are the
chapter's policy point: the gate **blocks** the build only on a new finding at or above the policy's block
severity that is confirmed exploitable; a severe-but-unproven or sub-blocking new finding is **routed to a
security reviewer** (surfaced, not auto-blocked, because exploitability is a judgment); and a pre-existing
finding is out of scope under clean-as-you-code, so a change that adds nothing new **passes** even atop a
mountain of legacy security debt — gating whole-repo absolutes would stop every pull request (Chapter 19).
`Block` carries the worst new finding's stage and rule id so the failure is actionable, not a bare red
mark.

## Observability surface

`SecurityGate.blockedCount()` exposes a running count of evaluations that blocked a merge — the headline
metric a dashboard trends the way it trends pipeline duration (a block rate stuck at zero may mean the
gate is too loose; one stuck high, too noisy and about to be routed around). `stagesReporting(...)` is the
coverage signal — which stages contributed a finding — so a stage that silently stopped running is
noticed rather than read as a clean result. `isReady()` is a readiness probe over the wired policy: a gate
with no policy could only fail open, the silent way a gate stops gating.

## Honest edges

Gate fatigue is the killer: a noisy, blocks-on-everything security gate gets bypassed or disabled, so the
gate blocks narrowly (new, high-severity, exploitable) and routes the rest to a reviewer (Chapter 19), the
single most important design decision. A green gate means "no detected, known, exploitable issue on new
code," **not** secure: the stages cannot find broken-access-control or business-logic flaws — often the
highest-severity breaches — which need threat modeling and design review (Chapter 84), and the CVE
databases the SCA stage reads cannot find zero-days. Exploitability is a reviewer's judgment, not a
severity number's, so the gate routes rather than auto-blocks the unproven. The dynamic stages (DAST/IAST)
need a deployed app and scenarios and are slow; a small internal app may rationally skip them, and they
gate the release stage, not the PR (their live-staging requirement is REPRO PENDING-RUNTIME in the YAML).
False positives compound across stages, so without ownership and triage the combined gate becomes noise
faster than any single tool would. The gate is one layer in a security posture, not the whole.
