# Chapter 35 — Teeth and Speed (`branch-protection-precommit-parity`)

Branch protection, merge queues, trunk-based development, and pre-commit hooks with local&#8596;CI
parity, at the smallest size that still teaches them. This is a CONFIG-centric chapter (the peer shape
of Chapter 33 and Chapter 27): the artifacts a team adopts are configuration, and the runnable,
unit-tested part is the one load-bearing decision the chapter argues for. It is a child module of the
companion-code reactor; it adds no version literals and inherits the runtime and test-library pins from
the aggregator. It has **zero runtime dependencies** — every surface is built on the JDK alone.

This module maps to dossier key `81` (folding `82`); `NN_slug` = `81_branch_protection_precommit_parity`.
The `Java 21+` baseline and every pinned tool version trace to `00-strategy/SOURCE-PIN.md`.

## What this module is, and is not

Three artifacts, one module, kept in lock-step:

- **`config/branch-protection/ruleset.yml`** — the protections that give the gate **teeth**: the quality
  gate (Chapter 33) wired as a **required status check**, **required review** (Chapter 84), **linear
  history**, and **restricted force-push / deletion**. This file is **configuration** — it is **not** run
  by this module's Maven build. The required check names in it match the CI job names the parity check
  asserts.

- **`.pre-commit-config.yaml`** — the fast hooks that give the gate **speed** at the keyboard: a format
  check (Chapter 6), a fast Checkstyle subset against **this module's own `config/checkstyle`**, and a
  secrets scan (Chapter 31). Fast checks only; the slow checks stay in CI. Also **configuration**, not
  run by this build, and managed by the pre-commit framework.

- **The parity check in `org.acme.parity`** — the load-bearing decision made runnable and unit-tested:
  `GateParity.check` asserts the local gate set (the pre-commit hooks plus the local build) reproduces
  every check CI requires, so **"green locally" predicts "green in CI"** is a property a test pins rather
  than a hope. `mvn -Pquality verify` runs the same Checkstyle the hook runs, so the two cannot silently
  drift (Chapter 27, local/CI parity).

### Dated-at-use (SaaS) surfaces

GitHub branch protection / rulesets and the pre-commit framework (and its hook repos) are hosted/SaaS
surfaces that `SOURCE-PIN.md` §5 records as rolling ("docs as of 2026-06"). The setting names in
`ruleset.yml` and the `rev:` tags in `.pre-commit-config.yaml` are therefore **dated at use (2026-06)**,
not timeless facts, and are flagged for confirmation/digest-pinning at adoption (see `09-flags/`).

## What it demonstrates

| Teaching | Where |
|---|---|
| Branch protection: the gate as a required status check (teeth) | `config/branch-protection/ruleset.yml` `required_status_checks` |
| Linear history + required review + restricted force-push | `config/branch-protection/ruleset.yml` `linear-history-and-review` region |
| Pre-commit fast hooks at the keyboard (speed) | `.pre-commit-config.yaml` `precommit-fast-hooks` region |
| Local&#8596;CI parity, as an executable assertion | `GateParity.check`, `ParityResult` |
| The parity failure path ("works locally, fails in CI") | `GateParity.enforce`, `ParityBrokenException` |
| Feedback (hooks) vs enforcement (CI + branch protection) | `GateParity.enforce` `feedback-not-enforcement` region |
| Externalized config profiles (`dev` / `prod`) | `ParityPolicy`, `parity-*.properties` |

## Build and run

```
# fast build (compile + tests), standalone single-module build against the parent
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs) — the same Checkstyle the pre-commit hook runs
mvn -B -Pquality -f pom.xml verify

# select a parity-policy profile (default dev)
mvn -B -Dparity.profile=prod -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The CI/git
configuration files are validated separately as parseable YAML, not by this build.

## The failure path

`GateParity.enforce` is the explicit failure path. When the local gate set is missing a check CI
requires, a strict (`prod`) policy throws `ParityBrokenException` carrying the **exact missing check
names** — the "works locally, fails in CI" gap made loud and actionable, rather than discovered as a red
pipeline after a push. A lenient (`dev`) policy returns the `Drifted` result instead, surfacing the gap
as feedback for the developer to close. `ParityResult` is a sealed type (`InParity` / `Drifted`), so the
drift is a named outcome, not a bare boolean.

## Observability surface

`GateParity.driftCount()` exposes a running count of parity checks that found drift — the headline metric
a dashboard trends the way it trends pipeline duration: drift creeping above zero means the local and CI
gate sets are diverging. `isReady()` is a readiness probe over the wired policy: a check with no policy
could only fail open (declare parity unconditionally), the silent way a check stops checking, so it
reports not-ready rather than waving drift through.

## Honest edges

Pre-commit hooks are **feedback, not enforcement** — `git commit --no-verify` skips them and they run
only where installed, so CI and branch protection remain the gate; treating a hook as the gate is a
category error (Chapter 35). Trunk-based development is **not "just commit to `main`"** — it demands a
trustworthy gate (Parts IV–VIII), reliable tests (Chapter 20), and feature flags for incomplete work.
Merge queues add latency and cost and are overkill for a low-merge-rate repo. Over-strict branch
protection slows delivery and breeds bypass requests. Parity is **hard to maintain** — local and CI tool
versions drift, so the wrapper and pinned versions are required, and even then OS/Docker differences
(Chapter 22) can diverge. And branching strategy is **context-dependent**: release-train, regulated, and
OSS contexts may legitimately use more branching, and the book crowns no model.

## Scoped requirement note

Requirement 4 (observability/health) is satisfied in-process (`driftCount`, `isReady`) rather than as a
network endpoint: this module is a build/workflow concern with no running service to expose an HTTP probe
on, so the health surface is the readiness/metric the parity check itself reports (`EXAMPLES-GUIDE` §1.2).
