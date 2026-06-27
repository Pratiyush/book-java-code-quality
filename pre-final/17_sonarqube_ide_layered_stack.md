# Composition, Not Accumulation

*SonarQube as the platform, the IDE as the first line, and how to layer analyzers into one coherent stack · 35 (folds 36, 37) · Part IV*

> Bolting on every analyzer produces a slow build and a flood of duplicate findings. The skill is composing a few that each watch a different blind spot.

## Hook

Two facts sit in tension. First: independent studies find that Java static analyzers **barely agree**. Point several at the same 47 projects and they flag mostly *different* things (Lenarduzzi et al., 2021). That argues for running several: their coverage is additive, not redundant. Second: a team that acts on that by bolting on Checkstyle *and* PMD *and* SpotBugs *and* Sonar with default rulesets gets a twenty-minute CI build, the same style nit reported by three tools, and developers who have learned to ignore the gate. That argues for running fewer.

Both are right, and the resolution is neither "more tools" nor "fewer tools" but **composition**: each analyzer reads a *different substrate* (source text, source AST, compiled bytecode, the `javac` tree during compilation, or an aggregating platform) at a *different moment* (while editing, at compile, after compile, in CI), so each sees a class of defect the others cannot — and a coherent stack assigns *one owner per concern*, runs each at its cheapest moment, and rolls the whole thing up into one signal a team can act on. This chapter builds that stack. It leads with **SonarQube** (the platform that aggregates and gates), adds the **IDE** (the author-time first line), and closes with the **layering verdict** the previous chapter deferred: which tool owns what, and how to stop them drowning each other.

## Overview

**What this chapter covers**

- The **substrate × moment matrix**: the one picture that explains why layering is additive, not redundant.
- **SonarQube**: the platform *above* a rule engine (quality profiles, the **quality gate**, "Clean as You Code," the Clean Code taxonomy, and the debt/trend model), and where it is gated behind paid editions.
- **IDE inspections** as the author-time **first line** (IntelliJ and Eclipse), why they are not a gate, and how `Connected Mode`/Qodana/EditorConfig make them a *shared* first line.
- The **coherent stack**: one owner per concern, ordered cheap-first, fail-fast; the cross-tool verdict Chapter 16 routed here.

**What this chapter does NOT cover.** Each analyzer's rule catalogue and config in depth (Chapters 15–16). Writing custom rules (Chapter 18). False-positive *policy*, including baselines, ratcheting, and what breaks the build (Chapter 19). Deep SAST / taint analysis (the security part). The SQALE/technical-debt model in depth (the metrics chapter). The full end-to-end CI gate (the CI part). Each tool here is cited to its own docs; **no tool is crowned**.

**One idea to hold**: *a tool's substrate and moment decide what it can see and how fast. Compose a stack by covering each substrate×moment cell once (one owner per concern), not by stacking tools that re-check the same one.*

## How it works

One picture carries the chapter's whole argument: which tool reads which substrate, at which moment, and the single cell each one covers that no other tool reaches. Figure 17.1 lays that grid out before the prose walks each cell.

![Fig 17.1 &mdash; The substrate &times; moment matrix: one owner per concern — Each tool reads one substrate at one moment &mdash; covering a cell no other tool reaches. Compose by assigning each concern exactly once.](figures/fig35_1.png)

*Fig 17.1 &mdash; The substrate &times; moment matrix: one owner per concern — Each tool reads one substrate at one moment &mdash; covering a cell no other tool reaches. Compose by assigning each concern exactly once.*

### The substrate × moment matrix

Every analyzer reads exactly one substrate, and that choice fixes both *what it can possibly see* and *when it can run*:

| Tool | Substrate (what it reads) | Moment (when it runs) | Sees what the others cannot |
|---|---|---|---|
| IDE inspections | source AST, live in the editor | **author-time** (keystroke) | the fastest feedback; nothing the others structurally cannot, but *first* |
| Error Prone | **`javac` AST during compilation** | **compile** (fails the build) | type-aware bugs, with the compiler's own type info + auto-fixes |
| Checkstyle / PMD | **source text / source AST** | source pass | layout, naming, Javadoc, smells, duplication — things erased by compilation |
| SpotBugs | **compiled bytecode** | **post-compile** | impossible casts, `volatile++`, null-on-exception-path — only visible after `javac` |
| SonarQube | source + bytecode, its own engine (+ can ingest others) | IDE + CI + **platform** | trend over time, a server-side gate, PR decoration — a role above a single check |

> **CONCEPT** *Substrate determines reach; moment determines latency.* SpotBugs sees the bytecode `javac` emits that Checkstyle never reads; Checkstyle sees source layout SpotBugs has discarded; Error Prone has the compiler's resolved types the source linters lack. None is "smarter" — each stands somewhere different. The "coherent stack" is the deliberate choice of which cells to cover, *once each*. (This is Chapter 15's technique ladder turned into a composition rule.)

This is *why* the analyzers barely agree, and why low agreement is good news: it means additive coverage. The composition problem is to capture that additivity without paying for the overlap.

### SonarQube: the platform above the rule engine

SonarQube is not "another linter" — it is a **quality platform** wrapping a rule engine. That distinction drives every choice in this section. It has two halves.

**The rule engine** (`sonar-java`) analyzes *both source and bytecode*: for a multi-file project, "Compiled `.class` files are required" via `sonar.java.binaries` or "analysis will fail," so it runs after compile with the build's classpath, not as a source-only linter. Every Java rule has a key in the **`java:`** namespace (e.g. `java:S2077`), and the engine includes a **symbolic-execution / data-flow engine** for path-sensitive bugs (null dereference, resource leaks) beyond pattern matching. Rules are classified by the **Clean Code taxonomy**: in **MQR mode**, each rule impacts one or more of three **software qualities** (Security, Reliability, Maintainability) and carries one of 14 **Clean Code attributes** (FORMATTED, CONVENTIONAL, … RESPECTFUL), with severities Blocker/High/Medium/Low/Info. The older **Standard Experience mode** keeps the familiar rule *types* (Bug, Vulnerability, Code Smell, Security Hotspot). A precision point: those issue types are **de-emphasized in favour of the Clean Code framing, not removed**; both modes coexist.

The scanner config records exactly that — where the engine reads its compiled inputs and classpath:

```properties
sonar.projectKey=org.acme.storefront:sonarqube-ide-layered-stack
sonar.java.source=21                  # analyze at the SOURCE-PIN anchor LTS (Java 21)
sonar.java.binaries=target/classes    # REQUIRED for multi-file Java: compiled .class dirs, or analysis fails
sonar.java.libraries=target/dependency/*.jar   # the classpath sonar-java resolves symbols against
sonar.sources=src/main/java
sonar.tests=src/test/java
```

**The platform** is the layer a bare analyzer lacks: a **quality profile** (the named set of active rules; the built-in "Sonar way" is default and read-only and teams copy it to tune), a **quality gate** (the pass/fail policy), persistence and **trend over time**, and PR decoration. Its load-bearing idea is **Clean as You Code**: scope the gate to **new code** (added/changed per the project's new-code definition) rather than the whole codebase; the built-in gate fails when new-code "issues is higher than 0" and requires "Security Hotspots Reviewed … 100%." That is what makes a gate *workable on a legacy codebase*: fixing a decade of debt is not required to turn it on. Keeping new code clean is enough.

Two scanner properties make that gate scope the diff and fail the build when it is breached:

```properties
sonar.qualitygate.wait=true                       # fail the build when the quality gate fails
sonar.newCode.referenceBranch=main                # Clean as You Code: gate only NEW code vs this reference
```

> **CONCEPT** *Platform = rule engine + a layer above it.* SonarQube's distinct contribution over Checkstyle/SpotBugs is not "a better linter" — it is the layer above the rules: profiles, a server-side gate, trend, and PR decoration. It runs at three moments (author-time via SonarQube for IDE, CI via the scanner, and the dashboard over time), and in **Connected Mode** the IDE pulls the team's profile and gate so local findings match CI.

Two honest limits to state plainly. The **flagship security analysis** — taint-based SAST that follows untrusted input to a sink — is a **Developer Edition (or higher) / Cloud** capability; the free **Community Build** has the rule engine but not the deep taint SAST. And the debt/rating model (SQALE: technical debt as a sum of per-rule "remediation minutes," a Maintainability A–E rating off a debt ratio) rests on **configurable conventions** (a default "30 minutes to develop a line"), so it is a *coarse trend signal, not a precise measurement*. Never report a debt figure as ground truth (the metrics chapter goes deeper). (Naming note: as of October 2024 the products are **SonarQube Server / Cloud / for IDE / Community Build** — formerly SonarQube / SonarCloud / SonarLint / Community Edition; use the current names.)

### IDE inspections: the author-time first line

The cheapest place to catch a defect is the **keystroke**. Both mainstream Java IDEs run a continuous analysis engine that highlights problems at the keystroke and offers a one-keystroke fix, plus a **save-action** layer that auto-formats and mechanically repairs on save. This is the leftmost moment in the toolchain — but it is a **first line, not a gate**, because it runs on the *author's* machine with the *author's* settings.

The two IDEs deliver it differently (neither crowned):

| | IntelliJ IDEA | Eclipse (JDT) |
|---|---|---|
| Where analysis lives | a dedicated **inspection engine** (separate from `javac`) | folded into the **JDT incremental compiler** (diagnostics are compiler warnings) |
| Severities | Error / Warning / Weak Warning / … / No-highlighting, per-profile, scopable | per-diagnostic Ignore / Warning / Error |
| Shared config | **inspection profile** XML under `.idea/inspectionProfiles/` (committed) | Errors/Warnings + Clean Up as project settings under `.settings/` (committed) |
| Save-time | **Actions on Save** (Reformat / Optimize imports / Rearrange / Run cleanup) | **Save Actions** (Format / Organize imports / Additional via Clean Up) |

The organizing distinction is **on-the-fly vs batch**: the squiggle appears at the keystroke (local, open files), while a batch run (`Code | Inspect Code…`, or the headless `inspect.sh` / **Qodana**, which runs the *same* inspections in CI with SARIF output and quality gates) covers the whole project and can feed a gate. The fix that makes the first line *trustworthy* is sharing it: commit the inspection profile, and pin formatting via **EditorConfig** so every developer's IDE and the build's formatter produce the same output.

A committed `.editorconfig` is that shared style, with a note on binding the IDE to the team's gate:

```
[*.java]
charset = utf-8                 # one shared style so every IDE and the build's formatter agree
indent_style = space
indent_size = 4
max_line_length = 120          # this team's cited choice (Chapter 6), not a universal truth
insert_final_newline = true
trim_trailing_whitespace = true
```

> **CONCEPT** *First line, not the gate.* The IDE gives the fastest feedback but guarantees nothing about what reaches the repo: a teammate who lowers the highlighting level or never commits the profile sees something different. So the author-time layer must be (a) *shared* (committed profile + `.editorconfig`) and (b) *backed by a build-time/CI equivalent*. "It's clean in my IDE" is not a contract; the gate lives in CI.

One trap specific to the save-action half: aggressive reformat-on-save with an *unshared* style rewrites lines a developer never touched, producing noisy diffs and reformat fights. Pin one style via EditorConfig aligned with the build's canonical formatter (Chapter 6) so the IDE and CI agree.

### The coherent stack: one owner per concern

Now the verdict Chapter 16 deferred. With analyzers that each read a different substrate, the composition principle is **assign each quality concern to exactly one primary tool**, layered so each runs at its cheapest moment:

Encoded in the companion module, that one-owner rule refuses a second owner for a concern outright:

```java
        Analyzer existing = owners.putIfAbsent(concern, analyzer);
        if (existing != null) {                                   // one owner per concern — refuse a second
            throw new IllegalStateException(
                concern + " is already owned by " + existing.name() + "; assign each concern exactly once");
        }
        return this;
```

| Concern | Primary owner (by substrate fit) | Equally-valid alternative |
|---|---|---|
| Formatting | a formatter (Spotless + google-java-format) | EditorConfig + IDE save-actions |
| Style & convention | Checkstyle | PMD style rules; Sonar |
| Source smells / duplication | PMD (+ CPD) | Sonar |
| Compile-time correctness + auto-fix | Error Prone | — (unique substrate) |
| Bytecode bug patterns | SpotBugs (+ fb-contrib) | Sonar's own engine |
| Null-safety | NullAway / JSpecify / Checker FW | Sonar null rules |
| Trend / debt / gate | SonarQube (platform) | aggregators |

Turn that into *one* build with one owner per row, ordered **cheap-first / fail-fast**: format-check and Error Prone (which rides the compile) fail earliest; the source passes (Checkstyle/PMD) next; the SpotBugs bytecode pass after `compile`; the heavier platform/coverage/mutation last. The same ordered checks run at the IDE/pre-commit *and* in CI (local↔CI parity) so the developer is not surprised at the gate. That ordering is the menu from Chapter 3's map turned into a deliberate sequence — the answer is composition, not accumulation.

In the build, the source-pass owner pins its analyzer engine separately from the build plugin:

```xml
            <groupId>org.apache.maven.plugins</groupId>                 <artifactId>maven-checkstyle-plugin</artifactId>
            <version>3.6.0</version>
            <dependencies>
              <dependency>                                                  <groupId>com.puppycrawl.tools</groupId>
                <artifactId>checkstyle</artifactId>
                <version>10.26.1</version>
```

In CI, the platform layer runs above those bare analyzers and gates the new code:

```yaml
      - name: Sonar platform layer (scan + quality gate on new code)
        env:                                            # secrets, never committed
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
          SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
        run: >                                          # SonarScanner for Maven, goal sonar:sonar (version @pin)
          mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
          -Dsonar.qualitygate.wait=true                 # block the pipeline when the gate fails on new code
```

## Deep dive: composing the stack without redundancy

The naive stack (every analyzer, default rulesets, all as build-breakers) fails in three specific ways, and the composition above is the fix for each.

**Overlap.** Style and convention rules overlap heavily: Checkstyle, PMD, and Sonar each ship line-length, naming, and empty-block checks. But the *empirical* picture is that cross-tool agreement is **low**: Lenarduzzi et al. (2021), studying six tools across 47 Java projects, report "little to no agreement among the tools and a low degree of precision." (Dating caveat: that study includes FindBugs, now superseded by SpotBugs; cite it for the *qualitative* low-agreement finding, which is the layering rationale, not as a current per-tool benchmark.) The lesson: overlap is real but concentrated in a minority of (mostly style) rule families; the bulk of findings are tool-unique. The fix is not dropping tools but **assigning each concern one owner**: let Checkstyle own style, and turn *off* Sonar's and PMD's overlapping style rules, so each finding comes from exactly one place.

**Redundancy.** Running two source-AST style checkers for the same concern costs build time and produces near-duplicate findings with no coverage gain. The one-owner map eliminates it by construction.

**Noise.** Every static tool has false positives (the same study reports low precision across the set). An un-tuned full stack as a build-breaker trains developers to ignore or disable the gate; the worst outcome is that the *real* findings drown too. The discipline: **tune and baseline the stack before gating it** (Chapter 19 owns the how — suppression with reason, baselines, ratcheting). A noisy gate is a net negative: it teaches the team that the gate lies, so the real findings get ignored alongside the false ones.

The composed result is a stack where the IDE is the shared first line (fast, local, backed by CI), Error Prone fails the compile on type-aware bugs, Checkstyle and PMD own style and smells on the source, SpotBugs catches what only bytecode reveals, and SonarQube sits above it all — aggregating findings, tracking the trend, and applying one Clean-as-You-Code gate on new code that a team can actually keep green. Each tool earns its place by owning a substrate×moment cell no other tool covers; none is there to re-check a cell already owned.

The unifying thread of Part IV's tool chapters: static analysis is a set of approximations reading different views of a program, and quality comes from *composing* those views deliberately — covering each blind spot once, at its cheapest moment, with a gate scoped so the team keeps it green rather than learns to route around it.

## Limitations & when NOT to reach for it

- **SonarQube is a platform, with platform cost.** SonarQube Server is a stateful service (database, upgrades, the LTA upgrade path); a small team may find the dashboard's value does not justify the ops overhead versus running the bare analyzers in CI. Cloud removes the ops cost but adds per-LOC pricing. When NOT: a build-only stack suffices and the team does not need trend/gate-over-time.
- **Sonar's deep security is paid.** Taint-based SAST for Java is Developer Edition+/Cloud, not the free Community Build. A team needing free injection analysis must weigh open-source SAST (the security part), each cited to its own source.
- **Sonar needs bytecode + classpath** (`sonar.java.binaries`): heavier setup than a source-only checker, and it must run after compile.
- **IDE inspections are not a gate.** Local, settings-dependent, scoped to open files on-the-fly; without a committed profile/EditorConfig *and* a CI equivalent, "clean in my IDE" guarantees nothing. The headless path (`inspect.sh`/Qodana) *can* gate but spins up a full IDE, making it heavier than a purpose-built CI linter.
- **Save-actions can wreck diffs.** Reformat-on-save with an unshared style rewrites untouched lines and causes reformat fights; pin EditorConfig + the build's canonical formatter, and consider scoping reformat to changed lines.
- **The debt/rating model is coarse, not exact.** SQALE letter ratings and technical-debt minutes rest on configurable conventions: trend/triage signals, never a precise "this codebase owes N hours."
- **More tools is not more quality.** Overlap costs build time without coverage gain; an un-tuned, un-baselined stack as a build-breaker erodes the gate's credibility. Compose one owner per concern, tune before gating, split fast (pre-commit) from full (CI) so feedback latency stays low.
- **The whole stack is necessary, not sufficient.** Static analysis reasons without running the code; a green gate is a *policy* met, not proof of correctness. It does not replace tests (Part V) or runtime security testing.

> **AHEAD-OF-PIN** Sonar's AI-assisted fix suggestions (AI CodeFix, 2024+) are product movement around the anchor; mention as direction, never as a settled anchor fact.

## Alternatives & adjacent approaches

- **The bare analyzers in CI without a platform** (Chapter 16): Checkstyle/PMD/SpotBugs/Error Prone wired into Maven/Gradle and failing the build directly. Sufficient for many teams; the trade-off is loss of trend, the server-side gate, and PR decoration.
- **Other aggregators** (Codacy, Code Climate): hosted platforms that ingest multiple analyzers' output, filling the same "platform above the engines" role as Sonar with a different commercial shape.
- **Qodana** (JetBrains): runs the IDE's *own* inspection engine headless in CI with SARIF + quality gates; it reuses the IDE-first-line profile as a gate and offers an alternative platform path for IntelliJ shops.
- **CodeQL / Semgrep** (the security part): whole-program taint SAST for the injection class Sonar's paid taint engine targets.
- **Pre-commit hooks** (the CI part): the line between the IDE and CI. Run the fast layers locally before the commit lands.

These layer rather than compete: the IDE first line, the bare analyzers (or Qodana) in the build, and a platform (Sonar/aggregator) above for trend and the gate, composed with one owner per concern.

## When to use what

- **For the author-time first line:** the IDE's inspections + save-actions, with a *committed* inspection profile and `.editorconfig` so the configuration is shared, and (optionally) `Connected Mode` to match the team's gate.
- **For the build-time checks:** the bare analyzers (Chapter 16) with one owner per concern, ordered cheap-first, run identically at pre-commit and in CI.
- **For trend, a server-side gate, and PR decoration:** SonarQube, with "Clean as You Code" scoping the gate to new code so a legacy codebase is not blocked wholesale.
- **For free injection SAST:** open-source SAST (security part), not the free Community Build.
- **For a small team / build-only setup:** skip the platform; run the bare analyzers in CI and gate on those.
- **Before gating any stack:** tune and baseline it (Chapter 19); a noisy gate undermines itself.

## Hand-off to the next chapter

The composed stack now stands: the IDE first line, the bare analyzers each owning a substrate, and SonarQube above as the platform and gate. Everything so far has used the rules these tools *ship*. But every team eventually hits a convention or anti-pattern no off-the-shelf rule catches — a house API that must be called a certain way, a banned method, a project-specific contract. The next chapter is **writing custom rules**: custom Checkstyle checks, PMD/Sonar rules, and Error Prone/Refaster patterns, plus the related compile-time machinery of **annotation processors** and the contested **Lombok** debate. That is where the stack stops being something to configure and becomes something to extend.

## Back matter — sources & traceability

- **SonarQube** (`docs.sonarsource.com`; Server/Cloud/for IDE/Community Build — Oct-2024 rename, verbatim from Sonar's press release): `sonar-java` reads source+bytecode (`sonar.java.binaries` required, verbatim); `java:S####` keys (`java:S2077` verified); symbolic-execution engine; Clean Code taxonomy (MQR: 3 software qualities + 14 attributes FORMATTED…RESPECTFUL, severities Blocker/High/Medium/Low/Info; Standard: bug/vuln/code-smell/hotspot); issue types de-emphasized not removed; security hotspots review-driven; "Sonar way" profile + gate (default/read-only); Clean as You Code (new-code gate: issues>0 fails, hotspots 100%); SQALE debt (sum of remediation minutes, 30min/line default, A=0-0.05…E=0.51-1 — coarse trend); taint/deeper-SAST = Developer Edition (or higher)/Cloud; Connected Mode; scanners `sonar-maven-plugin`(`sonar:sonar`)/`org.sonarqube` Gradle/SonarScanner CLI. *(identity/taxonomy verified; Server version PINNED at **2026.1 LTA / patch 2026.1.3** per SOURCE-PIN §2 corrected 2026-06-27 — dated-at-use 2026-06, Sonar SaaS/rolling; scanner GAV/rule defaults/SQALE grids/exact edition-version matrix still ⚠ verify-at-pin; `rules.sonarsource.com` offline → RSPEC repo; AI CodeFix AHEAD-OF-PIN.)*
- **IDE inspections** — IntelliJ IDEA (`jetbrains.com/help/idea`): inspections "detect and correct abnormal code … before you compile it" (verbatim); severities Error/Warning/Weak Warning/Server Problem/Grammar Error/Typo/Consideration/No-highlighting; highlighting None/Syntax/Essential/All-Problems; profile XML `.idea/inspectionProfiles/`; `Code|Inspect Code` batch; `inspect.sh` CLI; Actions on Save. Eclipse JDT (`help.eclipse.org`): Compiler Errors/Warnings (Ignore/Warning/Error); Save Actions (Format/Organize imports/Additional via Clean Up). Qodana (same engine in CI, SARIF + gates). EditorConfig (shared truth). *(identity verified; IDE versions/default-profile membership ⚠ @pin; IDEs+Qodana not yet SOURCE-PIN §2 rows.)*
- **Layered stack** (the synthesis, owned here): substrate × moment matrix; one-owner-per-concern map; overlap/redundancy/noise; cheap-first/fail-fast ordering; local↔CI parity. **Lenarduzzi, Lujan, Saarimäki, Palomba, "A Critical Comparison on Six Static Analysis Tools" (2021, arXiv:2101.08832)** — "little to no agreement among the tools and a low degree of precision" (verbatim abstract; DATED — includes FindBugs→SpotBugs; cite for low-agreement=additive only). FindBugs is dead → SpotBugs.
- **Routing** — per-tool depth → Ch 15/16; custom rules → Ch 18 (key 38); false-positive policy/baselines/ratcheting → Ch 19 (key 39); SAST/taint depth → the security part; SQALE/debt depth → the metrics chapter; end-to-end CI gate → the CI part.

## Next chapter teaser

The stack so far runs rules other people wrote. The next chapter covers writing them: a custom Checkstyle check or PMD/Sonar rule for a house convention, an Error Prone/Refaster pattern that bans-and-rewrites a deprecated API across the codebase — and the compile-time machinery next door: annotation processors, and the long-running argument over Lombok, the library that rewrites a class during compilation to delete boilerplate.
