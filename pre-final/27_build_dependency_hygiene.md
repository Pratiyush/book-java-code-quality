# The Foundation Under Every Gate

*The build as a quality surface, dependency hygiene that makes the graph deterministic, and the bots that keep it from rotting · 62 (folds 63, 64) · Part VII (opener)*

> Most of an application is other people's code. The build is where the team keeps that tree honest: pinned so it is reproducible, and current so it does not rot into a wall of vulnerabilities.

## Hook

A security alert fires: a three-year-old transitive library, four levels deep in the dependency tree, has a critical CVE — and nobody on the team knew it was there. While investigating, an engineer notices something else: their local build pulls a *different* version of that library than CI does, because nothing pins it and Maven's conflict resolution picked differently on the two machines. Two failures, one root cause: the dependency tree (which is most of the application's actual code) was never managed. It was not pinned, so it is not reproducible; it was not maintained, so it rotted.

Both failures live in the **build**, and the build is the subject of this opening chapter of Part VII. Every quality gate in this book — the formatters, the analyzers, the tests, the coverage and mutation thresholds, the architecture rules, the fitness functions of the last chapter — *runs in the build*. That makes the build the foundation the entire quality program stands on, and a disciplined build is itself a quality artifact: reproducible, fast, fail-fast, and identical on every machine. The build's hardest sub-problem is the dependency tree, because that tree has a tension at its heart: versions must be **pinned** for reproducibility, but pins **rot** into stale, vulnerable dependencies, so they must be kept **current**, without drowning in churn. This chapter covers all three: the build as the gate host, dependency *hygiene* (pin, converge, minimize) that makes the graph deterministic, and dependency *currency* (the update bots) that keeps it from rotting, with the currency flowing back through the same gates.

## Overview

**What this chapter covers**

- **The build as a quality surface**: the lifecycle as gate host, the tool-agnostic properties of a good build, and Maven versus Gradle (neutrally).
- **Dependency hygiene**: a single source of version truth (BOMs, version catalogs), convergence enforcement, and a minimal pinned surface.
- **Dependency currency**: Renovate and Dependabot, the update *strategy* (group, schedule, auto-merge the safe), and how updates flow through the gates.
- The pin-versus-rot tension and how pinning plus automated updates resolves it.

**What this chapter does NOT cover.** Reproducible builds in depth (a later chapter). Build *speed* and local↔CI parity, and the CI gate mechanics (the CI part). Vulnerability scanning, SBOMs, and supply-chain provenance (the security half of Part VII, next chapter). Large-scale version migration (a later chapter). Maven/Gradle and Renovate/Dependabot are **two-tool pairs, crowned neither**; each is cited to its own docs.

**One idea to take from this chapter:** *the build hosts every gate, so its discipline is the program's foundation. The dependency tree it builds must be pinned to be reproducible and continuously updated to avoid rot — a tension resolved by exact pins plus small automated bumps that flow through the same gates.*

## How it works

Every quality gate runs inside the build, so the shape of the build decides the shape of the gating. Figure 27.1 lays out that shape: the build lifecycle as the host the gates hang off, with Maven's phases on one side and Gradle's `check` task on the other, and the checks bound cheapest-first so the build fails on the smallest offense before it spends time on the expensive ones. The diagram crowns neither tool; it shows the same gate-host role expressed two ways.

![Figure 27.1 — The build lifecycle as quality-gate host — Maven phases and Gradle check task · bind checks cheapest-first · no winner between the two tools](figures/fig62_1.png)

*Figure 27.1 — The build lifecycle as quality-gate host — Maven phases and Gradle check task · bind checks cheapest-first · no winner between the two tools*

### The build as the gate host

Everything the book has gated runs through the build tool: the formatter, the analyzers, the tests, coverage, mutation, the architecture rules. The build is where the quality program physically lives (Chapter 3's toolchain map), which makes a disciplined build itself a quality artifact. The lifecycle is the gate host: in Maven, checks bind to phases (`validate` → `compile` → `test` → `verify`); in Gradle, the `check` task aggregates verification. The ordering principle from the analyzer chapters applies: cheap, fast, common-failure checks first (format, lint), heavier ones later (SpotBugs, Sonar), coverage and mutation last, so the build fails fast on the cheapest offense.

> **CONCEPT** *The quality of a build is tool-agnostic.* Regardless of whether Maven or Gradle is the choice, a high-quality build is **reproducible** (exact pinned versions, no `LATEST`/`RELEASE`/open ranges, so it produces the same result on every machine and every day), **fast** (incremental, cached, parallel — because a 30-minute `verify` is one developers skip locally, and a skipped gate is no gate), **fail-fast** with clear errors, identical **local and CI** (the same `./mvnw verify` everywhere), built on a **single source of dependency truth**, and run through a committed **wrapper** (`mvnw`/`gradlew`) that pins the build-tool version itself so the build does not drift with whatever Maven a developer happens to have installed. These properties matter more than the tool choice.

The two tools embody different philosophies, and the book crowns neither. **Maven** is declarative (a POM, convention over configuration, a stable lifecycle), with quality plugins (Surefire/Failsafe for tests, the Enforcer for ban-rules, the analyzer plugins) and org-wide rules that standardize well across teams. **Gradle** is programmable (a Groovy or Kotlin DSL), with incremental builds and a build cache that make large builds fast, version catalogs for dependency management, and the same analyzer plugins. Their costs are mirror images: Maven's rigidity can fight a non-standard need, and Gradle's programmability can sprawl into unreadable build logic. Which is the deeper point — *the build is code, and needs the same discipline as code*: a 2,000-line POM or a maze of custom Gradle tasks is a maintainability liability in its own right. Tool choice is rarely worth a migration on its own; the practices port across both.

### Dependency hygiene: making the graph deterministic

Dependencies are most of a modern Java application, and an unmanaged dependency tree is both a quality and a security liability: version conflicts, transitive surprises, and the "works on my machine" non-determinism from the hook. **Hygiene** is the discipline that makes the graph deterministic and reviewable, and it has four parts.

- **A single source of version truth.** Declare each version once: in Maven, `<dependencyManagement>` plus imported **BOMs** (bill-of-materials POMs, files that fix a coherent set of versions for a whole library family); in Gradle, **version catalogs** (a central list of dependencies and their versions, kept in `gradle/libs.versions.toml`) plus platforms. Child modules then omit versions entirely, so one bump updates everywhere consistently and no module can drift to an ad-hoc version. Importing a BOM looks like this:

```xml
      <dependency>
        <groupId>org.junit</groupId>                  <artifactId>junit-bom</artifactId>
        <version>${junit.version}</version>            <type>pom</type>
        <scope>import</scope>                        </dependency>
```

- **Convergence.** Transitive dependencies can pull conflicting versions of the same library, and Maven's "nearest-wins" resolution can silently *downgrade* one, a subtle source of runtime bugs. The `maven-enforcer-plugin` rules `dependencyConvergence` and `requireUpperBoundDeps` make a conflict fail the build; `mvn dependency:tree` and `dependency:analyze` expose the conflict for inspection (Gradle has resolution strategies and `dependencyInsight`). The two rules are one line each in the Enforcer:

```xml
                <dependencyConvergence/>   ```

```xml
                <requireUpperBoundDeps/>   ```

- **No moving versions.** Ban `LATEST`, `RELEASE`, and open ranges: they make the build non-reproducible, since a rebuild can silently pick up a different version. Pin exact versions, and let an update bot move them *deliberately* (the next section). The Enforcer's `bannedDependencies` rule turns the ban into a build failure:

```xml
                <bannedDependencies>
                  <excludes>
                    <exclude>*:*:*:*:*:LATEST</exclude>                       <exclude>*:*:*:*:*:RELEASE</exclude>
                    <exclude>commons-logging:commons-logging</exclude>                    </excludes>
                </bannedDependencies>
```
- **A minimal surface.** `dependency:analyze` flags used-but-undeclared and declared-but-unused dependencies; removing the unused ones shrinks the attack surface (next chapter) and speeds the build. Correct scopes (`test`/`provided`/`runtime`) keep test dependencies out of the runtime, and trusted repositories with checksum/signature verification guard the supply chain.

> **CONCEPT** *Hygiene makes the graph honest, not good.* Convergence proves the versions *agree*; it says nothing about whether a dependency is well-maintained, secure, or even necessary. A pinned, converged, minimal tree is deterministic and reviewable (the foundation), but judging dependency *quality* (is it current? does it have a CVE?) is the job of the currency and security chapters. Hygiene is necessary, not sufficient.

### Dependency currency: keeping pins from rotting

Pinning buys reproducibility and creates a new problem: pinned dependencies *rot*. A version pinned and forgotten becomes the leading source of known vulnerabilities, and the longer it sits, the more terrifying the eventual upgrade: the "we're four years behind" cliff where every bump is a big-bang migration. **Automated dependency updates** turn that annual nightmare into a steady stream of small, reviewable pull requests.

**Dependabot** (GitHub-native, configured in `dependabot.yml`) and **Renovate** (multi-platform, highly configurable via `renovate.json`) open a PR when a newer version exists, attach the changelog, and run the full build and tests on each one, so breakage surfaces *before* merging. But the bot is not the strategy; turning one on without a policy produces noise, not hygiene. The senior content is the strategy:

- **Auto-merge the low-risk:** patch versions of trusted dependencies with green CI, to cut noise.
- **Require review for the rest:** majors, minors, and especially security updates, which need a human reading the changelog.
- **Group related updates** (e.g. all test dependencies) to reduce PR volume, and **schedule** (e.g. weekly) to batch them.

Alongside the always-on bot, the build tool offers an on-demand view of what is available; the Maven versions plugin is configured to report updates without ever editing the POM itself:

```xml
        <configuration>
                    <generateBackupPoms>false</generateBackupPoms>
          <rulesUri>file://${project.basedir}/config/versions/version-rules.xml</rulesUri>
        </configuration>
```

> **CONCEPT** *Currency flows through the same gates.* The power of automated updates is that each update PR runs the *entire* quality program — tests, analyzers, contract tests, and the vulnerability scan of the next chapter. An update that breaks a contract (Chapter 24) or introduces a CVE fails the PR automatically, so currency does not cost stability. This is why the build-as-gate-host of the first section matters here: the gates make it safe to move fast on dependencies.

The honest costs: **PR fatigue** is real — without grouping, scheduling, and tuned auto-merge, a busy repo drowns in update PRs and the team starts ignoring them, defeating the purpose; configuration discipline is mandatory. A **green build does not catch a semantic break**: a major version can compile and pass tests while changing behaviour, so majors need human judgment, not auto-merge. And **auto-merge without strong tests** can ship a regression, so gate it on the coverage and contract tests that actually catch breaks. Renovate and Dependabot differ in configurability and platform reach; choose by need, crown neither.

## Deep dive: resolving the pin-versus-rot tension

The chapter's three parts meet in one tension, and resolving it is the whole point. Hygiene says **pin exactly** (no ranges, one version of truth), because that is what makes a build reproducible and a "works on my machine" bug impossible. Currency says pins **rot** — a frozen dependency tree is a growing pile of unpatched vulnerabilities and a deferred big-bang upgrade. Taken alone, each is a trap: pin-and-forget rots into the CVE wall from the hook; chase-the-latest (ranges, `LATEST`) gives up reproducibility and ships different code on every build. The resolution is to do *both at once*: pin every version exactly, and run an update bot that proposes small, individual version bumps as reviewable PRs that flow through the full gate stack. At any instant the build is perfectly reproducible (everything is pinned); over time the pins move forward deliberately, one validated step at a time, never as a panicked migration.

That resolution only works because of the foundation the first section laid. The bot can move fast *because* the gates are in the build: a patch bump auto-merges only when the tests, analyzers, and vulnerability scan all pass, so currency rides on the same machinery that enforces every other quality property. And it only works because of the hygiene the second section laid: a *single source of version truth* means the bot changes one line in a BOM or catalog and the whole graph moves consistently, while *convergence enforcement* catches the case where an update introduces a transitive conflict. The three facets are not independent topics that happen to share a chapter. They are one system. The build hosts the gates; hygiene makes the dependency graph deterministic so a change to it is predictable; currency moves the graph forward continuously through those gates. Remove any one and the other two fail: gates with no hygiene cannot tell what changed; hygiene with no currency rots; currency with no gates ships regressions.

This is also the foundation Part VII's second half stands on. Scanning a dependency tree for vulnerabilities (next chapter) requires a *deterministic* tree first: a scan of an unpinned graph reports findings that may not match what actually ships. A trustworthy software bill of materials cannot come from a build that resolves differently each time. And responding to a newly-disclosed CVE quickly demands the currency machinery to land the patched version through the gates. Hygiene and a disciplined build are the precondition for supply-chain security, which is why this chapter comes first: a quality program is only as solid as the build it runs in, and a build is only as trustworthy as the dependency tree it assembles.

## Limitations & when NOT to reach for it

- **The build is code, and over-engineering it is a quality risk.** A 2,000-line POM or a sprawl of custom Gradle logic is its own maintainability liability; keep the build as disciplined as the code. Maven's rigidity and Gradle's programmability are mirror-image costs; neither tool is a default win, and a tool migration is rarely justified on its own.
- **A slow build erodes quality.** If `verify` is slow, developers skip it locally and the gates only run in CI, late; build speed is a quality concern, not a luxury (the CI part covers it).
- **Strict convergence can be noisy.** Large graphs have many minor conflicts; an over-strict enforcer rule blocks builds and gets disabled (Chapter 19). Tune scope rather than turning it off.
- **Hygiene needs buy-in.** A single source of version truth only works if the team uses it; one developer adding ad-hoc versions defeats the BOM/catalog. And hygiene judges version *agreement*, not dependency *quality* — convergence says nothing about whether a dependency is maintained or safe.
- **Pinning without currency rots.** Exact pins are reproducible and go stale; without an update mechanism, pinning becomes the CVE wall it was meant to avoid.
- **Bots produce noise without a strategy.** Without grouping, scheduling, and tuned auto-merge, update PRs overwhelm the team and get ignored. The bot is a mechanism, not a policy.
- **A green build does not catch a semantic break.** A major version can compile and pass tests while changing behaviour; auto-merging majors carries real risk, because they need a human reading the changelog. Gate any auto-merge on the contract and coverage tests that actually catch regressions.

## Alternatives & adjacent approaches

- **Maven or Gradle** — the two dominant builds; pick by team fit and ecosystem, not a quality ranking; the practices in this chapter port across both.
- **Version catalogs vs BOM import** — Gradle's `libs.versions.toml` and Maven's `dependencyManagement`/BOM are two routes to the same single-source-of-truth goal.
- **Renovate vs Dependabot** — multi-platform configurability versus GitHub-native simplicity; choose by platform and tuning needs.
- **Manual periodic upgrades** — viable for a tiny project, but does not scale and tends to defer into the big-bang cliff; the bots exist because manual currency rots.
- **`dependency:analyze` / `dependencyUpdates`** — on-demand local views of unused dependencies and available updates, complementing the always-on bot.

These compose into one dependency program: a disciplined build hosting the gates, a single pinned source of version truth, convergence enforced, the surface kept minimal, and a tuned bot moving the pins forward through the gates.

## When to use what

- **To host the quality gates:** the build (Maven or Gradle), with checks bound to phases cheap-first and a committed wrapper.
- **For a single source of version truth:** Maven `dependencyManagement`/BOM or Gradle version catalogs; child modules omit versions.
- **To catch silent transitive downgrades:** the Enforcer's `dependencyConvergence` (tuned, not maximal).
- **For reproducibility:** exact pins — never `LATEST`/`RELEASE`/ranges — plus the wrapper pinning the build tool.
- **To keep pins from rotting:** an update bot (Renovate or Dependabot), with grouping, scheduling, and auto-merge tuned to the team's risk tolerance.
- **To move fast on dependencies safely:** route every update PR through the full gate stack; auto-merge only what strong tests cover.
- **For a newly-disclosed CVE:** the currency machinery to land the patched version through the gates quickly (the security half, next).

## Hand-off to the next chapter

This chapter made the dependency tree *deterministic*: pinned, converged, minimal, and kept current. That determinism is the precondition for the question this chapter deliberately deferred: not "do the versions agree?" but "is anything in this tree *dangerous*?" A pinned, converged dependency graph can still be full of known vulnerabilities. The next chapter scans for exactly that: **dependency vulnerability scanning** (software composition analysis: OWASP Dependency-Check, Dependency-Track) against vulnerability databases, and the broader **supply-chain security** layer: software bills of materials (CycloneDX, SPDX) that inventory what ships, and provenance frameworks (SLSA) that attest how it was built. That is where the honest, reproducible dependency tree of this chapter becomes a *securable* one.

## Back matter — sources & traceability

- **Build** (key 62, ⚠ Maven/Gradle) — Apache Maven **3.9.16** (`maven.apache.org`, SOURCE-PIN §4): declarative POM, lifecycle `validate`→`compile`→`test`→`verify`, `maven-surefire`/`failsafe`, `maven-enforcer-plugin`. Gradle (`docs.gradle.org`): programmable DSL, incremental + build cache, `check` lifecycle, version catalogs. Tool-agnostic build-quality: reproducible (pinned, no `LATEST`/`RELEASE`/ranges), fast (incremental/cache/parallel), fail-fast, local↔CI parity, single source of dep truth, committed wrapper (`mvnw`/`gradlew`). Crown neither; build-is-code discipline. *(Maven version + lifecycle phase-names + Enforcer rule-names VERIFIED green at EXAMPLE-BUILD; Gradle `check` semantics + the exact enforcer/versions plugin version literals ⚠ @pin — Gradle docs unfetched / plugin versions not separately pinned, see 09-flags/62_enforcer_versions_plugin_versions_unpinned.md.)*
- **Dependency hygiene** (key 63) — Maven `<dependencyManagement>` + imported BOM; Gradle version catalogs (`gradle/libs.versions.toml`) + platforms; child modules omit versions. Convergence: Maven "nearest wins" silent downgrade; `maven-enforcer-plugin` `dependencyConvergence`/`requireUpperBoundDeps`; `mvn dependency:tree`/`analyze`; Gradle resolution strategies/`dependencyInsight`. Ban `LATEST`/`RELEASE`/ranges. Minimal surface (`dependency:analyze` used-undeclared/declared-unused); scope discipline; trusted repos + checksum/signature. *(Enforcer rule names `dependencyConvergence`/`requireUpperBoundDeps`/`bannedDependencies` + Maven `<dependencyManagement>`/BOM-import syntax VERIFIED green at EXAMPLE-BUILD; Gradle version-catalog/`dependencyInsight` syntax + Maven "nearest wins" wording + `dependency:tree`/`analyze` goal names ⚠ @pin — Gradle/doc-only, not exercised by the Maven build.)*
- **Dependency currency** (key 64, ⚠ Renovate/Dependabot) — pinned deps rot → #1 vuln source + big-bang cliff. Dependabot (GitHub-native, `dependabot.yml`) / Renovate (multi-platform, `renovate.json`, grouping/schedules/automerge); both run build/tests per PR. Strategy: auto-merge low-risk patches w/ green CI; review majors/minors + security; group + schedule. Local: `versions:display-dependency-updates` (Maven), `dependencyUpdates` (Gradle). Updates flow through the same gates; security updates prioritized (vs GitHub Advisory/OSV). *(Maven `versions:display-dependency-updates` goal VERIFIED green at EXAMPLE-BUILD; Renovate/Dependabot config keys + security-alert sources NVD/OSV/GitHub-Advisory + Gradle `dependencyUpdates` goal ⚠ @pin — SOURCE-PIN §4 marks Renovate/Dependabot ⚠ rolling/SaaS, schema dated-at-use; Gradle docs unfetched. See 09-flags/75_ci_actions_saas_dated_at_use.md pattern.)*
- **Routing** — toolchain map → Ch 3 (05); reproducible builds → later (67); build speed + CI gates + local↔CI parity → CI part (79/82/75/76); vuln scanning/SCA → Ch 28 (65); SBOM/SLSA → Ch 28 (66); version migration → later (95); contract tests catch update breaks → Ch 24 (50); suppression discipline → Ch 19 (39). SOURCE-PIN §4 PINNED (2026-06-20): Maven **3.9.16**, Gradle **9.6.0**; Renovate/Dependabot ⚠ rolling (SaaS, pinned at point of use); the exact `maven-enforcer-plugin`/`versions-maven-plugin` version literals are not separately pinned → `09-flags/62_enforcer_versions_plugin_versions_unpinned.md`.

## Next chapter teaser

A dependency tree can be perfectly pinned, converged, and current — and still ship a critical vulnerability, because "the versions agree" is a different question from "is anything in here dangerous?" The next chapter scans for danger: software composition analysis (OWASP Dependency-Check, Dependency-Track) matching the project's dependencies against vulnerability databases, and the supply-chain layer above it: software bills of materials (CycloneDX, SPDX) that inventory what ships, and provenance (SLSA) that attests how it was built. The deterministic tree this chapter built is what makes that scan trustworthy.
