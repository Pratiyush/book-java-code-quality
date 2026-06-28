# Chapter 40 — Taming the Inherited Disaster (`remediation-playbook-automated-change`)

One buildable model of the chapter's two synthesis claims. The chapter is a synthesis, so the module makes
the load-bearing parts runnable rather than asserted:

- **The ordered playbook** — `PlaybookStep` is the canonical sequence (assess/baseline → gate new code →
  safety net → hotspot paydown → strangle → sustain), and `RemediationPlan` validates a plan against it.
- **Prioritize by churn × pain** — `RemediationPrioritizer` ranks debt by the interest it accrues and
  explicitly declines to fix frozen code, because debt no one pays to read or change costs nothing to carry.
- **The automation engine** — an OpenRewrite recipe (`config/rewrite/rewrite.yml`) plus the before/after
  Java pair it would transform (`legacy.LegacyReleaseNotes` → `Modernized`), both compiling green now.

The printed config and the running config are one artifact. It is a child module of the companion-code
reactor; it adds no version literals beyond the one pinned SpotBugs-annotations GAV and inherits the runtime
and test-library pins from the aggregator.

## Maps to

| | |
|---|---|
| Chapter | 40 — Taming the Inherited Disaster (`96_remediation_playbook_automated_change`, owner key 96, folds 94) |
| Java baseline | **Java 21+** — anchor LTS per `00-strategy/SOURCE-PIN.md` (built on 21.0.11) |
| OpenRewrite | **8.81.0**, `rewrite-maven-plugin` **6.38.0** — `SOURCE-PIN.md` §6 |
| Build | `mvn -B -Pquality -f 08-companion-code/96_remediation_playbook_automated_change/pom.xml verify` |

## What it demonstrates

| Concept (Chapter 40) | Where in the module | Tag |
|---|---|---|
| The ordered playbook is the strategy | `PlaybookStep` (the declared sequence) | `playbook-order` |
| Prioritize by churn × pain | `RemediationPrioritizer#rankByInterest` | `churn-pain-rank` |
| Debt in frozen code accrues no interest | `RemediationPrioritizer#selectHotspots`, `DebtItem#interest` | `frozen-no-interest`, `churn-pain` |
| Failure path — reject big-bang / baseline-without-paydown | `RemediationPlan` compact constructor | `reject-big-bang` |
| The engine — a type-aware recipe | `config/rewrite/rewrite.yml` | `rewrite-recipe` |
| Before/after the recipe transforms | `legacy.LegacyReleaseNotes`, `Modernized` | `before`, `after` |
| Observability — sustained or stalling | `ProgramHealth#report` | `program-health` |
| Externalized config profiles (dev/prod) | `src/main/resources/remediation.properties` | `config-profiles` |

## The five enterprise-grade requirements

1. **Pinned platform** — Java pinned once in the aggregator (`maven.compiler.release` 21); this module adds
   no runtime version literal. OpenRewrite is pinned to `SOURCE-PIN.md` §6 in the opt-in `rewrite` profile.
2. **Externalized config profiles** — `remediation.properties` carries a `dev` default and a `prod`
   override (`RemediationConfig` resolves the active profile); no threshold is hard-coded.
3. **An integration test** — `RemediationPlaybookTest` exercises every public behaviour, including each
   failure branch, under `verify`.
4. **An observability surface** — `ProgramHealth` reports whether the program is `SUSTAINED` or `STALLING`,
   so a stalled program shows up instead of being mistaken for a healthy one.
5. **An explicit failure path** — `RemediationPlan` rejects a baseline with no paydown plan (a real error
   response, driven by a test), making the HONEST-LIMITATIONS floor a code path, not a comment.

## The automation engine is opt-in and REPRO-pending (honest scope)

The OpenRewrite recipe **run** resolves the plugin and recipe modules from the network, so it is **not**
part of the default or `quality` build. It lives in the opt-in `rewrite` profile and is **REPRO
PENDING-RUNTIME**:

```
mvn -Prewrite rewrite:dryRun   # preview the diff, write nothing
mvn -Prewrite rewrite:run      # apply — produces a diff to review as a normal PR
```

The recipe **config**, the **before/after pair**, and the **prioritization logic** are all verified
offline by `mvn -Pquality verify`. This is the chapter's point made structural: automation proposes, and
the tests and the review dispose — the recipe's output is a pull request to confirm, never a blind apply.

The composed recipe ID (`org.openrewrite.java.migrate.UpgradeToJava21`) and the `rewrite-migrate-java`
module GAV (`3.34.0`) are **web-verified** (2026-06-28) against `docs.openrewrite.org` (the recipe ID,
verbatim) and Maven Central (the GAV): `rewrite-recipe-bom 3.30.0` imports `rewrite-bom 8.81.0` — the
pinned engine — and pins `rewrite-migrate-java 3.34.0`, whose own POM imports `rewrite-bom 8.81.0`, so
`3.34.0` is the recipe module aligned to this engine line (SOURCE-PIN §6). Only the recipe IDENTITY is
verified; the recipe RUN stays REPRO PENDING-RUNTIME (network-gated) — see `_EXAMPLE.md`.

## A module green *because* the past is staged, not because it is empty

The `quality` profile runs the same two gates the chapter discusses — Checkstyle (the source view) and
SpotBugs (the bytecode view). The before/after pair is written so the modernization is the *visible debt*
(the legacy form is fully correct, just older-idiom), so the baselines here are wired and inert — the exact
place a real legacy package would be frozen with a reviewed `<Match>` or `<suppress>` row. Both suppression
surfaces are present and documented so the chapter's "freeze the past, gate the new" mechanism is real in
the build, not decorative.
