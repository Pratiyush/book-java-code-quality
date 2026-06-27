# Chapter 46 — So What Do I Actually Set Up? (`reference-quality-stack-gate`)

The book's one chapter that recommends — one coherent, worked, end-to-end quality stack and CI gate,
framed as **one defensible setup, not the setup** (the NEUTRALITY capstone carve-out: every layer names
its alternative, the banned crowning language still applies, nothing is crowned). It is a child module
of the companion-code reactor; it adds no group/version literal and inherits the runtime and
test-library pins from the aggregator. It has **zero runtime dependencies** — every surface is built on
the JDK alone.

This module realizes the recommendation along two axes, de-duplicated against its peers: Chapter 33
(`75_ci_pipeline_quality_gates`) makes **one** gate's policy runnable, and Chapter 17
(`35_sonarqube_ide_layered_stack`) shows the layered analyzer set; this capstone is the **synthesis** —
the build-side stack assembled into one module, plus a runnable helper that composes the four-stage gate
ladder into a single ship / no-ship verdict.

## The build-side stack (the `pom.xml`)

The `quality` profile assembles the stack the chapter recommends, each row a distinct concern (Chapter 3
layering), de-duplicated so the signal stays high (Chapter 19):

| Layer | Tool (this setup) | Where | Named alternative |
|---|---|---|---|
| Compiler floor | `-Xlint:all` | inherited from the aggregator | — |
| Format | Spotless + google-java-format | `config/spotless/spotless-reference.xml` (reference config) | palantir-java-format |
| Style | Checkstyle (plugin 3.6.0, engine 10.26.1) | `config/checkstyle/checkstyle.xml` | a second rule-based linter |
| Bug-finding | SpotBugs 4.9.3.0 (bytecode) | `quality` profile | either vantage point alone |
| Coverage | JaCoCo 0.8.15 — BRANCH gate | `quality` profile + default agent/report | — |

The **format layer is shown as a reference config, not wired live**: SOURCE-PIN §2 pins the Maven-plugin
coordinate `com.diffplug.spotless:spotless-maven-plugin:3.6.0` (re-pinned 2026-06-27; flag 34 RESOLVED —
the earlier "8.7.0" was the project/Gradle line). The reference config under `config/spotless/` pins that
coordinate via `${spotless.maven.plugin.version}`; it is shown rather than wired live so the green build
does not depend on the google-java-format JDK `--add-exports` matrix. `google-java-format` is pinned to
the resolvable `1.35.0`.

**Version notes (each a recorded deviation from a SOURCE-PIN top-line, forced by artifact availability,
not invented):** Checkstyle engine **10.26.1** and SpotBugs **4.9.3.0** match the values the whole
reactor builds green against; JaCoCo **0.8.15** (SOURCE-PIN pins 0.8.15 (0.8.16 was the unpublished snapshot) — see
`09-flags/48_jacoco_pin_0816_unpublished.md`). The full-stack tools the chapter also names (Error Prone,
NullAway, ArchUnit, PITest, OWASP Dependency-Check, gitleaks, CycloneDX, SonarQube) are surveyed in
their own chapters' modules and prose; this module assembles the build-side core that builds green
offline here, and the gate-composition that ties the stages together.

## The gate-side design (`org.acme.refstack`)

The runnable, unit-tested part: the gate design composed into one verdict.

- **`ReferenceGate.evaluate`** — composes every stage's outcome into one `ShipVerdict` (ship / no-ship),
  the decision a required status check reads at the merge line. The synthesis Chapter 33's single-stage
  gate builds toward.
- **`StackLayer`** / **`ReferenceStack`** — the nine layers of the stack, each carrying its concern and a
  named alternative (the carve-out in code: recommend, name the alternative, never crown).
- **`GateStage`** — the four-stage feedback-latency ladder (pre-commit, PR-fast, main/nightly, merge),
  ordered cheap-to-expensive (Chapter 35).
- **`GateLadder`** — the externalized `dev` / `prod` ladder (enforce-from stage, clean-as-you-code, block
  severity), so the gate is tailored, not compiled in.

## Build and run

```
# fast build (compile + tests + JaCoCo agent/report), standalone
mvn -B -f pom.xml verify

# the assembled quality stack (Checkstyle + SpotBugs + JaCoCo BRANCH gate) — the local equivalent of CI
mvn -B -Pquality -f pom.xml verify

# select a gate-ladder profile (default dev)
mvn -B -Drefstack.profile=prod -f pom.xml verify
```

A green `quality` run reports tests passing, zero Checkstyle violations, zero SpotBugs findings, and the
BRANCH coverage gate met. Java 21 anchor (`SOURCE-PIN.md`; forward-checked on 25); Maven 3.9.16.

## The failure path

`ReferenceGate.evaluate` is the explicit failure path: it returns a `ShipVerdict` — a sealed type with
`Ship` and `NoShip` variants — rather than a bare boolean. `NoShip` is the composed no-ship decision: the
gate blocks the change only on a finding at or above the ladder's block severity, on new code
(clean-as-you-code), at a stage the ladder enforces; a stage earlier than the enforced floor is advisory,
which is how a team adopts the stack incrementally. `NoShip` carries every blocking stage so the failure
is actionable, not a bare red mark.

## Observability surface

`ReferenceGate.noShipCount()` exposes a running count of no-ship verdicts — the headline metric a
dashboard trends the way it trends pipeline duration (a no-ship rate stuck at zero may mean the ladder is
too loose; one stuck high, too strict or too noisy). `isReady()` is a readiness probe over the wired
ladder: a gate with no ladder could only fail open, the silent way a gate stops gating, so it reports
not-ready rather than waving changes through. The JaCoCo HTML/XML coverage report under `target/site/`
is the build-side observability surface.

## Honest edges (the carve-out)

This is *a* stack, not *the* stack — every layer's alternative is legitimate; tailor it to the team's
ecosystem, scale, budget, and regulatory context. The full stack is a lot — the `enforce-from` knob
exists so a team adopts it incrementally (Chapters 38, 40) rather than turning every stage on at once.
The stack is code to own — build time, false-positive tuning, and config maintenance are real ongoing
costs (Chapters 33, 19, 27). A ship verdict means the mechanical floor is clear, **not** that the code is
good: design, review, and culture decide quality (Chapters 37, 1), and a green capstone build proves the
stack composes and runs, not that the codebase is well-designed. Versions move — this stack is a
snapshot; pin everything and re-verify at the team's own pin (Chapter 29).

## Original-for-this-book

Every file here is written for this book. The curated Checkstyle ruleset and the empty SpotBugs filter
follow the same small house shape the peer modules use (a shared convention, not a copied upstream
sample); the gate-composition domain (`org.acme.refstack`) is original to this chapter and distinct from
Chapter 33's single-stage gate (`org.acme.cigate`). No file is a lightly-edited copy of an upstream
quickstart or sample.
