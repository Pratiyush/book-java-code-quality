# Chapter 31 — Catching What You Forgot (`70_sast_secrets_detection`)

Companion module for the chapter on **SAST** (static application security testing) and **secrets
detection**. Maps to dossier key **70** (folds 71), FINAL_INDEX Chapter 31, Part VIII.

This chapter is **CONFIG-centric**, the same shape as the peer CI module (`75_ci_pipeline_quality_gates`).
The SAST and secrets tools run in the pipeline, not as runtime dependencies, so their configuration is
illustrative and is **not run by the Maven build**:

| File | What it shows | Tool |
|---|---|---|
| `config/semgrep/java-injection.yml` | a custom rule catching the string-concat SQL sink | Semgrep |
| `ci/sast-scan.yml` | SAST + secrets + SCA, three lenses layered in CI | Semgrep / CodeQL / gitleaks |
| `.gitleaks.toml` | a secrets-scanner config: an AWS-key rule plus an allowlist | gitleaks |
| `.pre-commit-config.yaml` | a pre-commit hook running the secrets scan before commit | pre-commit + gitleaks |

What **is** runnable and unit-tested here is the load-bearing pair of decisions the chapter argues for,
expressed in plain Java so they build green and a test can prove them:

- **SAST finds the injection class.** `VulnerableOrderLookup` concatenates untrusted input into a SQL
  string — the source-to-sink flow taint analysis traces, and the sink core SpotBugs raises here (SpotBugs
  is the engine behind FindSecBugs, Chapter 16). `OrderLookup` is the design-out fix: a bound parameter, so
  the value is data and the class is eliminated. Running `mvn -Pquality verify` exercises the same finding
  the chapter's Semgrep rule targets.
- **Secrets stay out of source.** `SecretsResolver` reads the credential from externalized configuration and
  **fails closed** when it is absent — the prevention secrets detection backstops. `SastSecretsGate` is the
  running path, wired to the fixes, carrying the failure path and the health surfaces.

## Build

```
mvn -B -Pquality -f 08-companion-code/70_sast_secrets_detection/pom.xml verify
```

`-Pquality` is the opt-in static-analysis profile (Checkstyle + SpotBugs), mirroring the peer modules and
acting as the local equivalent of the pipeline's static-and-security stage (Chapter 27, local/CI parity).
With the profile off the build is also green.

## Prerequisites

- **Java 21** (the `SOURCE-PIN.md` anchor LTS; minimum runtime for the companion code). Forward-checked on
  the `SOURCE-PIN.md` forward LTS, Java 25.
- Maven, matching the pinned version in `SOURCE-PIN.md`. The toolchain pin is inherited from the
  `08-companion-code` aggregator; this module carries no version literal of its own.

## The reasoned suppression (Chapter 16)

`VulnerableOrderLookup.findOrdersByEmail` is a deliberate counter-example whose defect is the lesson, so a
test proves the injection rather than merely asserting it. Core SpotBugs raises
`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` on it; that one finding is suppressed **narrowly, with a reason
and a test pointer** in `config/spotbugs/spotbugs-exclude.xml`. Every other class stays clean with the
detectors fully enabled. The fix is the bound parameter in `OrderLookup`, never the suppression. (Core
SpotBugs catches this SQL sink; the deeper taint-tracing and crypto/secrets detectors the chapter discusses
are Semgrep / CodeQL / FindSecBugs territory, demonstrated by the illustrative tool configuration and by
tests and prose here.)

## The planted fake key

The secrets-detection fixture (`SastSecretsTest.PlantedSecretFixture`) uses AWS's published, non-functional
**EXAMPLE** access key id (`AKIAIOSFODNN7EXAMPLE`) so it demonstrates the pattern a secrets scan flags
without being a real credential. It is allow-listed in `.gitleaks.toml` (and lives only under `src/test`).
The honest edge the chapter states: a **real** leaked key would be compromised the instant it pushed —
deleting it from `HEAD`, or even rewriting history, does not un-leak it. The only fix is to **rotate** it.
Detection is not remediation.

## Honest edges (carried in code, not only here)

- **SAST finds patterns, not design.** Taint analysis traces tainted input to a SQL sink, but it sails past
  a broken authorization check or a business-logic flaw, because the code looks structurally correct. Those
  need design review (key 84).
- **SAST produces false positives and false negatives.** A flagged sink may be unreachable; a model may miss
  a flow it cannot trace. Findings are triaged, not auto-blocked wholesale.
- **A committed secret is compromised; rotate it.** A pre-commit hook is the only stage that prevents the
  leak, and it is bypassable (`git commit --no-verify`), so the CI scan and platform push protection are the
  backstops.
- **Neither tool is a management solution.** SAST does not fix the code; secrets detection does not store the
  credential. The real fixes are secure coding (Chapter 30) and a secret manager.

## Neutrality

The SAST and secrets tools are a multi-tool survey, **crowning none** — each is cited to its own docs, with
its strongest case and its hardest limitation. The configuration shows one defensible setup, not a verdict.

## Source-trace and dated-at-use

Every fact traces to `SOURCE-PIN.md` or the chapter dossier. The SAST and secrets tools (Semgrep is pinned;
CodeQL is rolling; gitleaks and TruffleHog have no pinned version) and the GitHub Actions are SaaS/rolling
and are **dated at use (2026-06)**, not asserted as timeless versions — see
`09-flags/70_sast_secrets_tools_saas_dated_at_use.md`. The tool configs use the documented schemas; no
version, rule-id, or coordinate is invented.

> Public push is **gated** on the `COMPANION-REPO.md` §5 human/legal sign-off. Local build only.
