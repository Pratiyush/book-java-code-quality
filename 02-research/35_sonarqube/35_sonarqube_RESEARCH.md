# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) **single-platform** dossier (Part IV static-analysis cluster). The subject is the
> **Sonar quality platform** — `SonarQube for IDE` (author-time), `SonarQube Server` / `SonarQube Cloud`
> (CI + dashboard), and the **rule engine** underneath them (the `sonar-java` analyzer, the RSPEC rule
> catalogue, the Clean Code taxonomy, quality profiles, quality gates, and the SQALE-derived debt model).
> Row 35 carries a `⚠` in `CANDIDATE_POOL.md`: the chapter is **comparison-sensitive** because Sonar runs
> its *own* analyzer AND can ingest other tools' reports, so NEUTRALITY is load-bearing — Sonar gets its
> strongest case **and** its hardest limitation, every cross-tool fact is cited to the named tool's own
> pinned source, and the *cross-cutting overlap/redundancy verdict is routed to key 37* (which owns the
> layered-stack comparison). The platform-vs-analyzer distinction (key 05) is the spine.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. Sonar version is `TO-PIN` in `SOURCE-PIN.md` §2,
> so rule **key/identity, taxonomy, and mechanism** are verified from Sonar's own docs/RSPEC while exact
> **rule defaults / severities / profile membership / remediation minutes / edition gating / version
> numbers** carry `⚠ verify at pin`. The Sonar **product rename (Oct 2024)** is a hard naming fact, verified
> from Sonar's own press release. Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 35 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** The Sonar quality platform & its rule engine — SonarQube Server / SonarQube Cloud / SonarQube for IDE (the `sonar-java` analyzer, RSPEC rules, Clean Code taxonomy, quality profiles & quality gates)
- **Part:** Part IV — Static analysis, linting & formatting (cluster 26–40; **key 37 owns the cross-cutting comparison/overlap**, key 38 the custom-rules deep dive, key 39 living-with-findings/baselines)
- **Tier:** B (Part IV tool chapter) · **Depth band:** Standard (deep single-platform; platform + rule-engine mechanism, doc/RSPEC-anchored)
- **Cmp:** **⚠ comparison-sensitive** (per `CANDIDATE_POOL.md` row 35). Sonar is treated under the full
  NEUTRALITY discipline: it gets its strongest case + hardest limitation; every claim about Sonar is cited
  to **Sonar's own pinned docs/RSPEC**; every claim about another analyzer (Checkstyle/PMD/SpotBugs/Error
  Prone — keys 27/28/29/30) is cited to **that tool's** own pinned source; **no tool is crowned**; the
  *which-tools-to-layer / where-they-overlap* verdict is **routed to key 37**. The **subject** (the
  *concept* of a quality platform / dashboard / gate; the JLS/Java the rules check) is discussed freely;
  the **tools** are comparison targets covered in depth.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **Platform / analyzer (the subject of this chapter):**
    - **`sonar-java` analyzer** — `github.com/SonarSource/sonar-java`; rule-key prefix **`java:`** (e.g.
      `java:S2077`, `java:S1192`, `java:S106`); includes a **symbolic-execution / data-flow engine** for
      path-sensitive rules (verified, Java-analysis doc).
    - **RSPEC** (Sonar Rule Specification) — `github.com/SonarSource/rspec` / `sonarsource.github.io/rspec/`;
      one `rules/S<NNNN>` directory per rule with `metadata.json` (rule key, type, severity, Clean Code
      attribute, software-quality impacts, remediation effort) — the canonical rule definition.
    - **Products (post-Oct-2024 names):** **SonarQube Server** (self-managed; Community Build / Developer /
      Enterprise / Data Center), **SonarQube Cloud** (SaaS; Team / Enterprise), **SonarQube for IDE**
      (author-time plugin: VS Code, IntelliJ IDEA, Eclipse, Visual Studio). Verified from Sonar's own Oct-2024
      product-naming press release.
  - **Build / CI integration (GAV — `⚠ verify at pin`):**
    - SonarScanner for **Maven**: `org.sonarsource.scanner.maven:sonar-maven-plugin` (goal `sonar:sonar`).
    - SonarScanner for **Gradle**: the `org.sonarqube` Gradle plugin.
    - Generic **SonarScanner CLI** (`sonar-scanner`) for non-Maven/Gradle builds.
  - **Analysis-config properties (verified verbatim, Java-analysis doc):** `sonar.java.binaries`
    ("Comma-separated paths to directories containing the compiled bytecode files corresponding to your
    source files" — *required* for multi-file Java projects), `sonar.java.libraries`, `sonar.java.test.binaries`,
    `sonar.java.test.libraries`.
  - **Standards the rules rest on (Bucket i — shared foundations, discuss freely):** OWASP Top 10 / CWE
    (security rules), the JLS/JDK (the Java the rules check), ISO/IEC 25010 quality model (the maintainability/
    reliability/security framing). Cite, never treat as rivals.
- **Canonical doc page(s):** `docs.sonarsource.com/sonarqube-server/.../user-guide/rules/overview`
  (rule taxonomy / MQR vs Standard mode / severities); `.../analyzing-source-code/languages/java` (analyzer
  requirements, `sonar.java.*` properties, symbolic execution); `.../user-guide/quality-gates`
  (Sonar way gate, Clean as You Code conditions); `.../user-guide/metric-definitions` (technical debt, SQALE
  ratings); RSPEC site/repo (per-rule metadata); Sonar Oct-2024 product-naming press release.
- **Canonical source path(s):** rules + analyzer trace to `github.com/SonarSource/sonar-java` and
  `github.com/SonarSource/rspec` (SOURCE-PIN §2, Sonar row, `TO-PIN`). Companion artifact:
  `08-companion-code/35_sonarqube/`.

---

## 1. Core definition & purpose

**Central claim.** Sonar is not "another linter" — it is a **quality platform** that wraps a rule engine: a
single analyzer family (here `sonar-java`) runs at **author-time** (SonarQube for IDE), at **CI time**
(SonarQube Server / Cloud), and reports findings into a **persistent dashboard** that tracks them *over time*
against a **quality gate**. The platform's distinctive contribution (vs a bare analyzer such as Checkstyle or
SpotBugs, keys 27/29) is the layer *above* the rules: persistence, trend, per-pull-request decoration, and a
**pass/fail gate policy** — the "Clean as You Code" model that scopes the gate to *new* code. The chapter's
spine is therefore the **two halves**: (a) the **rule engine** (what `sonar-java` finds and how rules are
classified), and (b) the **platform** (profiles, gates, debt, trend) built on top of it.

**Which part of the pinned set provides it.**
- The *rule engine* is `sonar-java` + RSPEC: every Java rule has a key under the **`java:`** namespace
  (verified, Java-analysis doc gives `java:S2077` as an example); the RSPEC repo holds each rule's
  `metadata.json` (key/type/severity/Clean-Code-attribute/quality-impact/remediation). *(Exact rule defaults
  and the total Java rule count are version-sensitive → `⚠ verify at pin`; a Sonar community thread cites
  "600+" Java rules — corroboration only, not asserted.)*
- The *taxonomy* is Sonar's **Clean Code** model: each issue is tied to a **Clean Code attribute** and the
  **software qualities** it impacts (verified, rules-overview doc).
- The *platform* is SonarQube Server / Cloud / for IDE (verified product names from Sonar's Oct-2024 press
  release).

**When introduced / naming (HARD fact — verified from Sonar's own press release).** In **October 2024** Sonar
**renamed its products** to a single brand: **SonarQube Server** (formerly *SonarQube*), **SonarQube Cloud**
(formerly *SonarCloud*), **SonarQube for IDE** (formerly *SonarLint*), and **SonarQube Community Build**
(formerly the *Community Edition*). The book must use the post-2024 names and note the former names once
(standards/edition discipline — never assert a product name from memory). The **Clean Code taxonomy** (Clean
Code attributes + software qualities, replacing the older bug/vulnerability/code-smell *issue types* as the
primary framing) was introduced in the **10.x** line (corroborated by the SonarQube 10.2 announcement; exact
introduction version `⚠ verify at pin`).

**Where it sits in the architecture.** Sonar spans **three lifecycle moments** (key 05's "by moment" axis):
**author-time** (SonarQube for IDE, in-editor, optionally *Connected Mode* to pull the team's profile/gate),
**CI / build** (the scanner runs `sonar-java` and uploads to the server/cloud), and **server / dashboard**
(persistence, trend, quality gate, PR decoration — key 78). The `sonar-java` analyzer itself analyzes **both
source AND compiled bytecode**: for multi-file projects "Compiled `.class` files are required" via
`sonar.java.binaries`, or analysis fails (verified verbatim, Java-analysis doc) — so it is a **build-time /
post-compile** analyzer, not a pure source linter.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The rule engine — what `sonar-java` analyzes, and how

**Setup / build-time behavior.** The analyzer needs **source + bytecode + the classpath**:
- `sonar.java.binaries` — "Comma-separated paths to directories containing the compiled bytecode files
  corresponding to your source files" (verified verbatim). For a multi-file Java project this is **required**;
  without it "analysis will fail" (verified). The Maven/Gradle scanners supply it automatically; the generic
  scanner needs it set by hand (the docs call manual config "very error-prone" — verified).
- `sonar.java.libraries` — "Comma-separated paths to files with third-party libraries (JAR or Zip files) used
  by your project" (verified verbatim) — the classpath the analyzer resolves symbols against.
- `sonar.java.test.binaries` / `sonar.java.test.libraries` — the same for test code.
- The analyzer itself **requires Java 17 to run** (verified, "as of version 7.31" — the runtime that *hosts*
  the analyzer, distinct from the bytecode level it *analyzes*; exact version `⚠ verify at pin`).

This is the key mechanism teaching: `sonar-java` reads **source AST *and* bytecode + classpath**, which lets
it resolve types and run **cross-procedural data-flow** — a different analysis surface from a source-only
style checker (Checkstyle, key 27) or a bytecode-only detector (SpotBugs, key 29). *(Each compared tool's
analysis surface is cited to that tool's own docs; the overlap verdict → key 37.)*

**Active behavior — rule families.** `sonar-java` rules split into:
- **Syntactic / AST rules** — pattern checks on the parse tree (naming, complexity, dead code, idiom).
- **Semantic rules** — use resolved types/symbols (require the classpath).
- **Symbolic-execution / data-flow rules** — the analyzer "includes a symbolic-execution engine" for "rules
  that reason about the state of the program using data flow analysis"; these "usually work together to find
  path-sensitive bugs" and operate cross-procedurally in certain circumstances (verified, Java-analysis doc).
  This is what finds null-dereference / resource-leak / condition-always-true style bugs (e.g. the
  `java:S2259` null-dereference family — rule identity `⚠ verify at pin`).
- **Security rules** — including **taint analysis** (data-flow from source→sink for injection). Taint analysis
  for Java is a **commercial-edition / Cloud** capability (Developer Edition and above on Server, free for
  open-source on Cloud — verified from Sonar docs/blog); the **deeper SAST** feature is "available in the
  Developer Edition version 9.9 LTS or higher … at no additional cost" (verified; exact edition gating
  `⚠ verify at pin`). This is the §4 honest limit: the *flagship security analysis is not in the free Community
  Build*.

**Rule keys.** Every Java rule has a key in the **`java:`** namespace, **`java:S<NNNN>`** (verified example
`java:S2077`; the RSPEC `S`-number is shared across languages, the `java:` prefix scopes it to the Java
analyzer). The book MUST cite rule keys *with* the `java:` prefix and treat the exact key as a never-invent
atom (the key-18 "no rule-ID from memory" rule). Representative keys named for the worked example are
verified to **exist** but their **default severity / profile membership** is `⚠ verify at pin`:
`java:S106` (standard output / `System.out` use), `java:S1192` (string literals duplicated),
`java:S1118` (utility class hidden constructor), `java:S2259` (null dereference), `java:S2077`
(formatted SQL / injection). *(Identity from RSPEC; defaults at pin.)*

### 2.2 The taxonomy — Clean Code attributes & software qualities (the modern framing)

Sonar's **rule taxonomy** moved from the older *issue type* model to the **Clean Code** model (verified,
rules-overview doc). Two modes exist:

- **MQR (Multi-Quality Rule) mode** — rules are categorized by the **three software qualities**: **Security,
  Reliability, Maintainability** (verified). "A rule may impact more than one software quality" (verified).
  Severities in MQR mode: **Blocker, High, Medium, Low, Info** (verified). Each rule also carries a **Clean
  Code attribute** drawn from **14 attributes** (verified verbatim list): "FORMATTED, CONVENTIONAL,
  IDENTIFIABLE, CLEAR, LOGICAL, COMPLETE, EFFICIENT, FOCUSED, DISTINCT, MODULAR, TESTED, LAWFUL, TRUSTWORTHY,
  RESPECTFUL." These 14 group under four **attribute categories** — **Consistency, Intentionality,
  Adaptability, Responsibility** (categories named in the Clean Code user-guide doc; the exact
  attribute→category mapping is `⚠ verify at pin` — the dedicated `clean-code` doc page 404'd in this scan,
  see §7).
- **Standard Experience mode** — the legacy view: four **rule types** "Code smell (maintainability domain),
  Bug (reliability domain), Vulnerability (security domain), Security hotspot (security domain)" (verified),
  with severities **Blocker, Critical, Major, Minor, Info** (verified).

**Issue-type status (precision — avoid the trap).** The web summary that "issue types … are deprecated" is a
*framing* shift, **not** a removal: the rules-overview doc states both modes still use these concepts, "simply
organized differently depending on instance configuration" (verified). So the book must say: *the bug /
vulnerability / code-smell issue types are de-emphasized in favour of the Clean Code attributes + software
qualities in MQR mode; they remain available in Standard Experience mode.* Do **not** assert "issue types are
removed." (Flag filed — the exact deprecation wording/version is `⚠ verify at pin`.)

**Security hotspots.** A distinct category from issues: "Security hotspot rules draw attention to
security-sensitive code"; they are "not assigned severities as it is unknown whether there is truly an
underlying vulnerability until they are reviewed"; "After being reviewed by a developer, more than 80% of
issues are expected to be quickly resolved as 'reviewed'" (verified verbatim). Hotspots need *human review*,
not auto-fix — a teaching point for the §4 "tool surfaces, human decides" honesty.

### 2.3 Quality profiles — which rules are active

A **quality profile** is the named set of activated rules (with their parameters) applied to a project. The
built-in profile is **"Sonar way"** — "provided by SonarSource, activated by default, and read-only"
(verified). Teams **copy** "Sonar way" to a custom, editable profile to tune the active set (activate/
deactivate rules, change parameters/severities) — this is the key-39 ruleset-tuning surface and the key-38
custom-rules surface (Sonar can load custom-rule plugins). A **deprecated rule** "should no longer be used
because a similar, but more powerful and accurate rule exists" (verified) — Sonar's own deprecation discipline.

### 2.4 Quality gates — the pass/fail policy ("Clean as You Code")

A **quality gate** is the pass/fail condition set evaluated after analysis. The built-in
**"Sonar way" quality gate** is "Sonar's recommended quality gate for your new code … provided by SonarSource,
activated by default, and read-only" (verified). It implements **Clean as You Code**: focus the gate on **new
code** (code added/changed per the project's **new code definition**) rather than the whole codebase, so a
legacy codebase isn't blocked wholesale and "new features will be delivered cleanly" (verified). A gate is
"configured for Clean as You Code when it has … conditions on new code: No issues are introduced (the quality
gate fails when the Number of issues is higher than 0) … Security Hotspots Reviewed is not less than 100%"
(verified verbatim). This is the platform's load-bearing idea and the §3 strongest case: it makes the gate
**actionable on a legacy codebase** by scoping to the diff (cross-ref key 39 baselines/ratcheting, key 76/80
gate policy).

### 2.5 The debt / rating model (SQALE-derived) — trend over time

The platform quantifies maintainability as **technical debt** and grades it with letter ratings (verified,
metric-definitions doc; **exact grids `⚠ verify at pin`**, version-sensitive):
- **Technical debt** = "the sum of the maintainability issue remediation costs," each issue's cost being "the
  effort (in minutes) … taken over from the effort assigned to the rule that raised the issue" (verified).
  Each rule carries a **remediation effort** (its RSPEC `metadata.json`).
- **Technical debt ratio** (`sqale_debt_ratio`) = technical debt ÷ (cost-to-develop-one-line × LOC), with the
  per-line cost "predefined … (by default, 30 minutes)" (verified; default `⚠ verify at pin`).
- **Maintainability rating** (`sqale_rating`, "formerly the SQALE rating") graded A–E off the debt ratio;
  default grid "A=0-0.05, B=0.06-0.1, C=0.11-0.20, D=0.21-0.5, E=0.51-1" (verified; grid `⚠ verify at pin`).
- **Reliability** and **Security** ratings are A–E off the worst-severity issue present (verified outline).

**Folklore guard (key-04 cross-ref).** SQALE letter ratings and the Maintainability *Index* are **coarse
trend signals, not precise truth** — the per-line "30 minutes to develop" and per-rule remediation minutes are
*configurable conventions*, not measured facts. The book frames them as *trend/triage aids*, never as an exact
"this codebase is worth N hours of debt." (Consistent with the PIPELINE-LEARNINGS folklore list: "MI as a
precise 0–100 score" — opaque coefficients, coarse trend only.)

### 2.6 The platform tiers — Connected Mode & PR decoration

- **SonarQube for IDE** runs `sonar-java` locally as you type (author-time, free). In **Connected Mode** it
  binds to a Server/Cloud project to use the *same* quality profile and gate the team enforces in CI — so
  local findings match the gate (key 82 IDE-as-first-line; cross-ref key 36 IDE inspections). *(Connected
  Mode behavior verified from Sonar docs; exact IDE list verified: VS Code, IntelliJ IDEA, Eclipse, Visual
  Studio.)*
- **PR decoration** (inline annotations on the pull request, gate status as a check) is a Developer-Edition+
  / Cloud feature (key 78 PR-based quality). Edition gating `⚠ verify at pin`.

### 2.7 Reference units (rule keys / config / API / coordinates — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `sonar-java` analyzer | analyzer | rule-key prefix **`java:`**; source+bytecode | tool-version | `github.com/SonarSource/sonar-java`; Java-analysis doc ✅ |
| RSPEC `rules/S<NNNN>/metadata.json` | rule spec | key/type/severity/attribute/quality/remediation | tool-version | `github.com/SonarSource/rspec` ✅ (structure) |
| `java:S<NNNN>` | rule key | e.g. `java:S2077` (verified example) | tool-version | Java-analysis doc ✅ (prefix + example) |
| `sonar.java.binaries` | analysis property | **required** for multi-file projects (compiled `.class` dirs) | n/a | Java-analysis doc ✅ (verbatim) |
| `sonar.java.libraries` | analysis property | third-party JAR/Zip classpath | n/a | Java-analysis doc ✅ (verbatim) |
| `org.sonarsource.scanner.maven:sonar-maven-plugin` | Maven plugin (GAV) | goal `sonar:sonar` | tool-version | Java-analysis doc ✅ (name); GAV ⚠ verify at pin |
| `org.sonarqube` (Gradle plugin) | Gradle plugin | applies the scanner | tool-version | Java-analysis doc ✅ (name); version ⚠ verify at pin |
| SonarScanner CLI (`sonar-scanner`) | generic scanner | needs manual `sonar.java.binaries` | tool-version | Java-analysis doc ✅ |
| Clean Code attributes (14) | taxonomy | FORMATTED…RESPECTFUL; 4 categories | tool-version | rules-overview doc ✅ (14 list); category map ⚠ |
| Software qualities (3) | taxonomy | Security / Reliability / Maintainability (MQR) | tool-version | rules-overview doc ✅ |
| MQR severities | severity scale | Blocker / High / Medium / Low / Info | tool-version | rules-overview doc ✅ |
| Standard severities | severity scale | Blocker / Critical / Major / Minor / Info | tool-version | rules-overview doc ✅ |
| Standard rule types | taxonomy | Code smell / Bug / Vulnerability / Security hotspot | tool-version | rules-overview doc ✅ |
| Security hotspot | category | no severity until reviewed; review-driven | tool-version | rules-overview doc ✅ (verbatim) |
| "Sonar way" quality profile | profile | built-in, default, read-only | tool-version | quality-gates/rules docs ✅ |
| "Sonar way" quality gate | gate | built-in, default, read-only; new-code focus | tool-version | quality-gates doc ✅ (verbatim) |
| New code definition | gate scope | "Clean as You Code" — gate on added/changed code | tool-version | quality-gates / CaYC doc ✅ |
| Technical debt | metric | sum of remediation minutes | tool-version | metric-definitions doc ✅ (verbatim) |
| `sqale_rating` (Maintainability) | rating A–E | grid A=0-0.05…E=0.51-1 | tool-version | metric-definitions doc ✅; grid ⚠ verify at pin |
| `sqale_debt_ratio` | metric | debt ÷ (30 min/line × LOC) | tool-version | metric-definitions doc ✅; default ⚠ verify |
| Taint analysis (Java) | security engine | source→sink data-flow; Dev Edition+/Cloud | tool-version | Java-analysis / Sonar SAST docs ✅; edition ⚠ verify |
| SonarQube for IDE (Connected Mode) | product | author-time; binds profile/gate to Server/Cloud | product-line | Sonar docs ✅ (rename press release) |

---

## 3. Evidence FOR

- **One rule engine, three moments — author-time → CI → dashboard.** The same `sonar-java` analyzer runs in
  SonarQube for IDE (as you type), in CI (scanner), and persists to the Server/Cloud dashboard; Connected Mode
  binds the IDE to the team's profile + gate so local and gate findings agree (verified, Sonar docs +
  Oct-2024 rename press release). This is the platform's distinctive value over a bare analyzer (key 05).
- **"Clean as You Code" makes a gate workable on legacy code.** The built-in "Sonar way" gate scopes
  pass/fail to **new code** (added/changed per the new-code definition) so a legacy codebase isn't blocked
  wholesale: "new features will be delivered cleanly" (verified verbatim). A Clean-as-You-Code gate fails when
  "Number of issues is higher than 0" on new code and requires "Security Hotspots Reviewed … 100%" (verified).
- **Rules anchored on a published taxonomy + recognized standards.** Each rule ties to a **Clean Code
  attribute** and the **software qualities** it impacts (Security/Reliability/Maintainability), with severities
  and remediation effort in RSPEC `metadata.json` (verified). Security rules map to **OWASP / CWE** (Bucket-i
  shared standards). The book can cite the *exact* taxonomy and rule key, not folklore.
- **Path-sensitive analysis via a symbolic-execution engine.** The analyzer "includes a symbolic-execution
  engine" for "rules that reason about the state of the program using data flow analysis," finding
  "path-sensitive bugs" cross-procedurally (verified, Java-analysis doc) — beyond pattern matching.
- **Taint-based SAST for Java.** Sonar provides source→sink taint analysis for Java (injection vulnerabilities)
  in commercial editions / Cloud, with "deeper SAST … in the Developer Edition 9.9 LTS or higher … at no
  additional cost" (verified, Sonar docs/blog). (Security depth → key 70 SAST chapter.)
- **First-class build & PR integration.** Maven (`sonar-maven-plugin`, goal `sonar:sonar`), Gradle
  (`org.sonarqube`), and the generic SonarScanner wire analysis into any pipeline; PR decoration adds inline
  findings + gate status to the pull request (key 78). *(GAVs/editions `⚠ verify at pin`.)*
- **Maturity / scale.** Sonar is a long-standing, widely deployed platform; the free **SonarQube Community
  Build** (LGPL-3.0) covers 20+ languages with quality gates (verified product framing from the rename
  release; the "6,000+ rules" / "600+ Java rules" figures are corroboration only — `⚠ verify at pin`).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — Sonar's hardest objections + when-NOT-to-use)

**Sonar's hardest objections (cited to Sonar's own docs/behavior).**
- *Flagship security analysis is gated behind paid editions.* **Taint analysis / deeper SAST for Java** is a
  **Developer Edition (or higher) / Cloud** capability — the free **Community Build** does not include the
  injection-finding taint engine (verified, Sonar docs/blog). A team relying only on the free server gets the
  rule engine but not the deep SAST; cross-ref key 70 (which compares SAST options) and route the
  *tool-choice* verdict there.
- *Needs compiled bytecode + classpath — not a drop-in source linter.* For multi-file Java projects "Compiled
  `.class` files are required" via `sonar.java.binaries` or "analysis will fail" (verified verbatim); manual
  config is "very error-prone" (verified). So Sonar must run **after compile** with the build's classpath —
  heavier setup than a source-only style checker (key 27).
- *False positives + a triage/review burden.* Like any static analyzer, `sonar-java` raises findings that need
  human triage; **security hotspots** explicitly require human review ("not assigned severities … until
  reviewed"; "expected to be … resolved as 'reviewed'" — verified). Un-triaged findings erode gate
  credibility (key 39). Suppression is per-finding (`// NOSONAR`, issue "won't fix"/"false positive"
  resolutions) — overused, it hides real defects.
- *Metric/rating opacity (folklore guard).* The SQALE letter ratings and technical-debt minutes rest on
  **configurable conventions** ("30 minutes" per line by default; per-rule remediation minutes) — coarse trend
  signals, **not** precise measurements (verified the defaults exist; key-04 folklore list: MI/debt as a
  precise score is misleading). Don't present a debt figure as ground truth.
- *Server operational cost.* SonarQube Server is a stateful service (database, upgrades, the LTA/LTS upgrade
  path); a small team may find the platform overhead exceeds the value of the dashboard vs running the bare
  analyzers in CI. Cloud removes the ops cost but adds per-LOC pricing.

**When NOT to reach for Sonar (the honest when-NOT).**
- A team that only needs **fast local feedback on style/idiom** may get there with Checkstyle/PMD/Error Prone
  in the build (keys 27/28/30) without standing up a server. *(Each alternative's fit is cited to that tool's
  own docs; the layered-stack decision is key 37's.)*
- A team that needs **deep injection SAST for free** must weigh the edition gating against open-source SAST
  (FindSecBugs/Semgrep/CodeQL — key 70), each cited to its own source.
- A **prototype / throwaway** codebase rarely justifies a gate + dashboard.

**Competing approach *inside* the field — neutral framing (verdict → key 37).** Sonar runs its **own**
analyzer (`sonar-java`) **and** can ingest other tools' reports (e.g. via generic-issue / external-report
import for PMD/Checkstyle/SpotBugs output — config identity `⚠ verify at pin`). So Sonar and the standalone
analyzers (Checkstyle 27 / PMD 28 / SpotBugs 29 / Error Prone 30) **take different roles**: the standalone
tools are analyzers that fail the build directly; Sonar is a platform that runs an analyzer *and* aggregates/
tracks/gates results over time, and can also re-surface the others' findings. They **overlap** on many checks
(naming, complexity, common bug patterns) and **diverge** on others (Sonar's taint SAST; SpotBugs' bytecode
patterns; Error Prone's in-`javac` auto-fixes). **No tool is crowned** here — the *which-to-run / where-they-
overlap / how-to-layer* verdict is the explicit job of **key 37** (and the reference stack, key 109). Every
cross-tool fact is cited to the named tool's own pinned source.

**Shared limits of all static analysis (the honest centre).** Static analysis reasons about code without
running it: it cannot decide every property (undecidability), so it has false positives/negatives by
construction; it does not replace tests (Part V) or runtime security testing (DAST). Sonar's gate is a
*policy*, not proof of correctness.

---

## 5. Current status

- **Active and current at the anchor.** Sonar (Server / Cloud / for IDE) is actively developed; `sonar-java`
  ships regular releases. *(Exact latest-stable version is `TO-PIN` in `SOURCE-PIN.md` §2 — the Server line
  moved to a calendar-versioned **LTA** scheme, e.g. the 2025.x LTA; the analyzer carries its own version.
  Pin both at `/pin-source`.)*
- **Product rename — Oct 2024 (settled, verified).** SonarQube Server / Cloud / for IDE / Community Build are
  the current names (formerly SonarQube / SonarCloud / SonarLint / Community Edition). The book uses the new
  names and notes the old ones once.
- **Clean Code taxonomy + MQR mode (10.x onward).** The Clean Code attributes + software qualities framing and
  the MQR-vs-Standard-Experience mode toggle are the current rule model (verified, rules-overview doc). The
  older bug/vulnerability/code-smell *issue types* are **de-emphasized** (Standard Experience mode), **not
  removed** — assert it that way (exact deprecation wording/version `⚠ verify at pin`; flag filed).
- **Java-version coverage.** `sonar-java` tracks JDK releases and adds rules for new language features (records,
  sealed types, pattern matching, switch). The analyzer *runs on* Java 17+ (verified) and *analyzes* code at
  the anchor (21) and forward LTS (25); rules for 25-only/preview features are `⚠ verify at pin` (and any
  preview-feature rule is `⚠ AHEAD-OF-PIN` if it targets an unreleased state — cross-ref the key-13/22
  preview-API traps).
- **AI CodeFix / IDE evolution.** SonarQube for IDE added AI-assisted fix suggestions (2024+). This is product
  movement **ahead of / around the anchor** — mention as direction only, never as a settled anchor fact
  (`⚠ verify at pin`).
- **Deprecations.** Sonar deprecates individual rules (a deprecated rule "should no longer be used because a
  similar, but more powerful and accurate rule exists" — verified); cite the *replacement* rule key, never a
  deprecated key as current.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `35_sonarqube` *(row to be added — `DEMO-CATALOG.md` does
  not yet exist in the repo; see §7 flag, consistent with the keys 15/24/25 catalog-gap notes).*
  - **Demo name:** "From finding to gate — scanning a Java module with Sonar and failing the quality gate on new code."
  - **Java Quality surface exercised:** a small `org.acme.storefront` module deliberately seeded with issues
    each tied to a **known `java:S####` rule** — `java:S106` (`System.out.println` instead of a logger),
    `java:S1192` (a string literal duplicated ≥ threshold), `java:S1118` (a utility class with a public
    implicit constructor), and a **null-dereference** path the symbolic-execution engine flags
    (`java:S2259` family). Analysis runs via the **Maven** scanner (`sonar:sonar`) against a **local
    SonarQube Server (Community Build) in a Testcontainers / docker-compose** instance, using the built-in
    "Sonar way" profile + gate. A fixed variant (logger, extracted constant, private constructor, guarded
    deref) passes the gate. *(Rule keys verified to exist; default profile membership / severities
    `⚠ verify at pin`.)*
  - **TRY-IT exercise:** run `./mvnw -B verify sonar:sonar` against the local server; observe the new-code
    quality gate **fail** with the `java:S106`/`java:S1192`/`java:S1118`/`java:S2259` issues; then apply the
    fixes (SLF4J logger, a `private static final String` constant, a `private` constructor on the utility
    class, a null guard) and watch the gate go **green**. Then *deactivate* `java:S106` in a copied profile to
    show profiles are policy, not law (key 39). This makes "Clean as You Code on new code" tactile.
- **Module key / path:** `08-companion-code/35_sonarqube/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ (anchor) verify at pin |
  | `org.sonarsource.scanner.maven:sonar-maven-plugin` (goal `sonar:sonar`) | runs `sonar-java`, uploads to server (primary unit) | Java-analysis doc (GAV TO-PIN) | ☐ verify at pin |
  | SonarQube Server **Community Build** (Docker image, via Testcontainers/compose) | the local server + "Sonar way" gate | Sonar docs (version TO-PIN) | ☐ verify at pin |
  | `org.slf4j:slf4j-api` (+ a binding) | the logger that fixes `java:S106` | (SOURCE-PIN — logging, TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the fixed class behaves) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.testcontainers:testcontainers` (optional) | spin the local SonarQube Server for the demo | testcontainers.org (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose versions; `sonar-maven-plugin` version managed.
  - **Externalized config / profiles** — `sonar.*` properties in the POM / `sonar-project.properties`
    (`sonar.java.binaries` set by the Maven scanner; `sonar.qualitygate.wait=true` to fail the build on gate
    failure); a copied quality profile showing a *reviewed* rule deactivation (trace each rule key to RSPEC).
  - **At least one test** — asserts the **fixed** class behaves (e.g. the utility method returns correctly);
    names the behavior it asserts.
  - **Observability / health surface** — the SonarQube dashboard *is* the observability surface; the demo also
    logs via SLF4J (the `java:S106` fix), the surface key 106 touches.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **seeded-issues** variant is the failure path
    — the **quality gate fails the build** (`sonar.qualitygate.wait=true`) on the new-code issues. State in the
    chapter that the gate failing **is** the demonstrated failure path, and note its limit: the gate is a
    *policy* (deactivating `java:S106` makes it pass without fixing the smell — the §4 honesty).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `seeded-issues` | the class with `java:S106`/`S1192`/`S1118`/`S2259` smells (failure path) | `LegacyReport.java` |
  | `clean-fixed` | the fixed variant (logger, constant, private ctor, null guard) that passes the gate | `Report.java` |
  | `sonar-config` | the `sonar.*` properties + `sonar.qualitygate.wait=true` | `pom.xml` / `sonar-project.properties` |
  | `gate-test` | a test asserting the fixed class behaves correctly | `ReportTest.java` |

- **Run command:** `./mvnw -B verify sonar:sonar -Dsonar.host.url=<local-server> -Dsonar.qualitygate.wait=true`
  (requires the local SonarQube Server running; token via env).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the seeded issues — the scan reports the `java:S####` findings and the **quality
  gate fails** (build red via `sonar.qualitygate.wait=true`); fixed — green gate, green build, test pass count
  green, the dashboard shows 0 new-code issues + hotspots reviewed.
- **Figure plan** (GUIDELINES §8; **standard single-platform chapter** → image budget ~**1–2 designed
  diagrams + 1–2 captured screenshots**; not a zero-figure chapter — Sonar's dashboard/gate is inherently
  visual):
  - **Chapter class:** standard Part-IV tool chapter with a strong visual surface (the dashboard/gate earn
    captures; the platform/rule-engine split earns a diagram).
  - **Candidate designed diagram(s) + family:**
    - **Fig 35.1 — "The Sonar platform: one rule engine, three moments" (architecture/flow diagram):**
      `sonar-java` analyzer at the centre; arrows to **author-time** (SonarQube for IDE, Connected Mode),
      **CI** (scanner → Server/Cloud), and the **dashboard / quality gate** over time; with the source+bytecode
      inputs (`sonar.java.binaries`/`.libraries`). Family = *platform-architecture / lifecycle-flow diagram*.
      Trace: analyzer + properties → Java-analysis doc; products → Oct-2024 rename release; gate → quality-gates doc.
    - **Fig 35.2 — "How a rule is classified" (taxonomy map):** one example rule (`java:S####`) shown carrying
      a **Clean Code attribute** (one of the 14), the **software qualities** it impacts (Security/Reliability/
      Maintainability), a **severity** (MQR Blocker/High/Medium/Low/Info), and a **remediation effort**; with
      the Standard-Experience mapping (rule type) beside it. Family = *taxonomy / classification diagram*.
      Trace: every label → rules-overview doc; remediation → metric-definitions doc.
  - **Candidate captured surface(s):**
    - **Fig 35.3** — a capture of the **quality gate FAILED** panel (new-code issues > 0) from the companion
      module's local server, and a paired **PASSED** capture after the fix (Clean-as-You-Code made tactile).
    - **Fig 35.4 (optional)** — a SonarQube-for-IDE in-editor finding (the `java:S####` squiggle) showing the
      author-time surface. Capture only real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every analyzer/property label → Java-analysis doc; every product name
    → Oct-2024 rename release; every taxonomy label → rules-overview doc; gate/new-code labels → quality-gates
    + CaYC docs; debt/rating labels → metric-definitions doc; each `java:S####` key → RSPEC.

---

## 7. Gap-filling (verification queue)

- ⚠ **Sonar version(s) / GAV coordinates** — `SonarQube Server` (LTA line, e.g. 2025.x), `sonar-java`
  analyzer version, `org.sonarsource.scanner.maven:sonar-maven-plugin`, the `org.sonarqube` Gradle plugin
  version, SonarScanner CLI: all `TO-PIN` in `SOURCE-PIN.md` §2 → confirm exact latest-stable versions +
  coordinates at pin before stating any version number. Product names + analyzer identity verified.
- ⚠ **Clean Code attribute → category mapping** — the 14 attributes are verified verbatim
  (FORMATTED…RESPECTFUL); the four categories (Consistency / Intentionality / Adaptability / Responsibility)
  are named, but the exact attribute→category grouping needs the `clean-code` doc page (which 404'd to
  WebFetch in this scan at the 10.8 path). Re-fetch at the pinned doc version before printing the grouping.
- ⚠ **Issue-type "deprecated" wording/version** — verified that issue types are *de-emphasized/reorganized*
  (Standard Experience mode retains them), NOT removed; the exact "deprecated" phrasing and the version that
  introduced MQR mode are `⚠ verify at pin`. Do not assert "issue types removed." (Flag filed.)
- ⚠ **Default rule severities / profile membership** — `java:S106`, `java:S1192`, `java:S1118`, `java:S2259`,
  `java:S2077` verified to **exist**; their default severity (MQR + Standard) and "Sonar way" membership are
  version-sensitive → `⚠ verify at pin` (resolve on each rule's RSPEC `metadata.json` at the pinned version).
- ⚠ **SQALE/debt grids + defaults** — the Maintainability grid (A=0-0.05…E=0.51-1), the 30-min/line develop
  cost, and the remediation minutes are version-sensitive defaults → `⚠ verify at pin` from metric-definitions
  at the pin. Frame ratings as coarse trend (folklore guard), never exact truth.
- ⚠ **Edition gating** — taint analysis / deeper SAST / PR decoration as Developer-Edition+ / Cloud features:
  verified in current docs/blog ("Developer Edition 9.9 LTS or higher"); re-confirm the exact edition + version
  matrix at pin (it has shifted historically). Community Build feature set `⚠ verify at pin`.
- ⚠ **External-report / generic-issue import** — Sonar can ingest PMD/Checkstyle/SpotBugs output; the exact
  property names / supported importers are `⚠ verify at pin` (route the *should you* verdict to key 37).
- ⚠ **"600+ Java rules" / "6,000+ rules" / "20+ languages"** — corroboration only (community thread / rename
  release); never assert a count as fact without the pinned doc/RSPEC count.
- ⚠ **AI CodeFix / SonarQube-for-IDE 2024+ features** — product direction ahead of/around the anchor; mention
  as direction, `⚠ verify at pin`, never an anchor fact.
- **Open question (draft / Part IV routing):** boundary with **key 05** (the map — owns platform-vs-analyzer
  intro), **key 37** (owns the cross-cutting overlap/redundancy + layered-stack verdict — route ALL
  "Sonar vs Checkstyle/PMD/SpotBugs/Error Prone, which to run" content there), **key 38** (custom Sonar rule
  plugins), **key 39** (suppression/baselines/ratcheting — `// NOSONAR`, won't-fix), **key 70** (SAST — owns
  the taint/deeper-SAST comparison), **key 78** (PR decoration), **key 59** (technical debt / SQALE deep
  treatment), **key 80/88** (gate/dashboard, CI). Propose: **this** chapter owns the *platform + rule-engine
  mechanism + Clean-as-You-Code gate*; cite 37 for the comparison, 70 for SAST depth, 59 for debt depth.
- **DEMO-CATALOG.md** does not yet exist in the repo — add the `35_sonarqube` row when it is created (flag to
  catalog owner; same gap noted by keys 15/24/25).

### Filed to `09-flags/`
- `09-flags/35_sonar_versions_and_defaults_unverified.md` — Sonar Server (LTA), `sonar-java`, scanner Maven/
  Gradle GAVs are `TO-PIN`; rule keys/taxonomy/mechanism/product-names verified, but exact versions, rule
  default severities, "Sonar way" membership, SQALE grids/defaults, and **edition gating** (taint/deeper SAST/
  PR decoration) are `⚠ verify at pin`.
- `09-flags/35_clean_code_taxonomy_and_issuetype_status_unverified.md` — the 14 Clean Code attributes are
  verified verbatim but the **attribute→category** mapping needs the `clean-code` doc (404'd in scan); and the
  **issue-type "deprecated"** status is a *reorganization* (Standard Experience retains them), NOT a removal —
  must be stated precisely; exact deprecation wording + MQR-introduction version `⚠ verify at pin`.

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | Sonar doc | Java analysis — `sonar.java.binaries`/`.libraries` (required bytecode), `java:` rule prefix (`java:S2077`), symbolic-execution engine, scanners | docs.sonarsource.com/.../analyzing-source-code/languages/java | ☑ (verbatim properties + prefix + SE engine) |
| 2 | Sonar doc | Rules overview — Clean Code attributes (14: FORMATTED…RESPECTFUL), software qualities (Security/Reliability/Maintainability), MQR vs Standard mode, severities (MQR Blocker/High/Medium/Low/Info; Standard Blocker/Critical/Major/Minor/Info), rule types, security hotspot (verbatim) | docs.sonarsource.com/.../user-guide/rules/overview | ☑ (taxonomy + severities + hotspot verbatim) |
| 3 | Sonar doc | Quality gates — built-in "Sonar way" gate (default/read-only), Clean as You Code conditions ("Number of issues > 0" fails; "Security Hotspots Reviewed … 100%"), new-code focus | docs.sonarsource.com/.../user-guide/quality-gates | ☑ (verbatim conditions) |
| 4 | Sonar doc | Quality profiles — "Sonar way" profile (provided by SonarSource, activated by default, read-only); deprecated-rule definition | docs.sonarsource.com/.../managing-rules/rules | ☑ (verbatim) |
| 5 | Sonar doc | Metric definitions — technical debt (sum of remediation minutes, verbatim), `sqale_debt_ratio` (÷ 30 min/line × LOC), Maintainability rating grid (A=0-0.05…E=0.51-1, "formerly the SQALE rating") | docs.sonarsource.com/.../user-guide/metric-definitions | ☑ (verbatim; grids ⚠ verify at pin) |
| 6 | Sonar source | `sonar-java` analyzer (rule keys, JavaRulesDefinition, symbolic execution) | github.com/SonarSource/sonar-java | ☑ (repo + key format) |
| 7 | Sonar source | RSPEC — per-rule `rules/S<NNNN>/metadata.json` (key/type/severity/attribute/quality/remediation) | github.com/SonarSource/rspec ; sonarsource.github.io/rspec | ☑ (structure) |
| 8 | Sonar release | Product naming streamline (Oct 2024) — SonarQube Server / Cloud / for IDE / Community Build (formerly SonarQube / SonarCloud / SonarLint / Community Edition) | sonarsource.com/company/press-releases/sonar-streamlines-product-naming… | ☑ (names verbatim) |
| 9 | Sonar doc/blog | Taint analysis / deeper SAST for Java — Dev Edition 9.9 LTS+ / Cloud; data-flow source→sink | docs.sonarsource.com (SAST) + sonarsource.com/blog/what-is-taint-analysis | ☑ (edition gating — ⚠ verify exact matrix at pin) |
| 10 | Sonar doc | SonarQube for IDE — Connected Mode (binds profile/gate to Server/Cloud); IDEs VS Code/IntelliJ/Eclipse/Visual Studio | docs.sonarsource.com/.../sonarlint-connected-mode | ☑ (Connected Mode + IDE list) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Sonar blog | "Introducing SonarQube 10.2: setting new standards" (Clean Code taxonomy intro) | sonarsource.com/blog/sonarqube-10-2-new-standards… | ☐ corroboration (version ⚠ verify) |
| 2 | Sonar community | "Total number of Java rules" (≈600+ Java rules) | community.sonarsource.com/t/total-number-of-java-rules/46026 | ☐ corroboration only (count ⚠) |
| 3 | Standard | OWASP Top 10 / CWE (the security taxonomy Sonar security rules map to — Bucket-i foundation) | owasp.org | ☐ |

> Source-quality order applied: Sonar's own docs → Sonar source/RSPEC → Sonar release/press → recognized
> standards (OWASP/CWE, Bucket-i) → Sonar blog/community (corroboration/color only). Every cross-tool claim
> (Checkstyle/PMD/SpotBugs/Error Prone) is reserved for cite-to-that-tool's-own-source and routed to key 37.
> "Live-line" = verified against the current docs; re-verify byte-exact after `/pin-source` (pre-pin caveat).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebSearch rule taxonomy / Clean Code / issue types | docs.sonarsource.com | Clean Code attributes (4 categories), 3 software qualities (Sec/Rel/Maint), MQR mode, issue types reorganized (not removed); deprecated-rule definition |
| 2 | WebSearch quality gate / Sonar way / Clean as You Code | docs.sonarsource.com | "Sonar way" gate default/read-only; CaYC = new-code focus; conditions "issues > 0" fails + hotspots 100% reviewed; new-code definition |
| 3 | WebFetch rules/overview (10.8) | docs.sonarsource.com | 14 Clean Code attributes verbatim; 3 software qualities; MQR vs Standard mode; severities both modes; standard rule types; security hotspot verbatim; rule-key format not on this page |
| 4 | WebSearch sonar-java rule keys / RSPEC | github.com/SonarSource | rule-key `S`-number format; RSPEC `rules/Sxxxx/metadata.json`; "600+" Java rules (corroboration) |
| 5 | WebFetch analyzing/languages/java (10.8) | docs.sonarsource.com | `sonar.java.binaries` required + verbatim; `sonar.java.libraries`; `java:` prefix + `java:S2077`; symbolic-execution/data-flow engine verbatim; scanners (Maven `sonar-maven-plugin`/Gradle/CLI); analyzer runs on Java 17 |
| 6 | WebSearch SonarLint→SonarQube for IDE rename / Connected Mode | docs.sonarsource.com + Sonar blog | "SonarQube for IDE" (formerly SonarLint); Connected Mode; IDEs VS Code/IntelliJ/Eclipse/Visual Studio |
| 7 | WebSearch product naming / editions / Cloud rename | Sonar press release | Oct 2024 rename: Server/Cloud/for IDE/Community Build; editions Community/Developer/Enterprise/Data Center; Cloud Team/Enterprise |
| 8 | WebSearch taint analysis / deeper SAST / editions | Sonar docs/blog | taint analysis Java (Sec); deeper SAST "Developer Edition 9.9 LTS or higher … no additional cost"; Cloud free for OSS |
| 9 | WebFetch clean-code doc (10.8 path) | docs.sonarsource.com | 404 — attribute→category mapping not retrievable at this path (flagged §7) |
| 10 | WebSearch technical debt / SQALE / ratings | docs.sonarsource.com metric-definitions | technical debt = sum of remediation minutes (verbatim); `sqale_debt_ratio` (÷30 min/line × LOC); Maintainability grid A=0-0.05…E=0.51-1 ("formerly SQALE") |

---
## Learnings & pipeline suggestions
- **Reusable shape — "platform = rule engine + a layer above it" for any platform/aggregator chapter.** A
  quality *platform* (Sonar, and later Codacy key 88) is cleanest organized as **two halves**: (1) the **rule
  engine** (what its analyzer finds + how rules are classified — here the `java:` keys + Clean Code taxonomy),
  and (2) the **platform layer** (profiles, quality gate, debt/trend, PR decoration) that a bare analyzer
  (Checkstyle/SpotBugs) lacks. This keeps NEUTRALITY structural (Sonar's *distinct* contribution is the layer
  above, not "it's a better linter") and the comparison verdict routes cleanly to key 37. Reuse for key 88.
- **Product-rename atom trap (NEW, durable).** Sonar **renamed every product in Oct 2024**
  (SonarQube→SonarQube Server, SonarCloud→SonarQube Cloud, SonarLint→SonarQube for IDE, Community
  Edition→Community Build). A dossier or draft citing "SonarCloud" / "SonarLint" as *current* names is
  off-pin. Extend the "no name/ID from memory" rule (keys 18/15) to **product names** — verify against the
  vendor's own rename announcement. Filed in `09-flags/35_sonar_versions_and_defaults_unverified.md`.
- **"Deprecated" ≠ "removed" precision (NEW, sibling of the key-01 edition trap).** Web summaries said
  "issue types are deprecated"; the rules-overview doc actually shows issue types **reorganized** (Standard
  Experience mode retains bug/vuln/code-smell; MQR mode foregrounds Clean Code attributes + software
  qualities). State the *reorganization*, never "removed." A reusable trap for any "the new version dropped X"
  claim — confirm against the version's own doc.
- **Rule-key prefix discipline.** Sonar Java rule keys are `java:S<NNNN>` — always cite **with** the `java:`
  prefix (the bare `S####` is the cross-language RSPEC number). Identity is citeable now; **default severity /
  profile membership move per version** → cite "key + `verify at pin`" (reinforces keys 09/13/14/16/19 rule-
  ID-vs-severity split; `rules.sonarsource.com` has been flaky across the project — use the RSPEC repo).
- **Folklore guard reused (key 04).** SQALE letter ratings + technical-debt minutes rest on configurable
  conventions (30 min/line default; per-rule remediation minutes) — coarse trend, not precise truth. Frame as
  trend/triage, never an exact debt figure.
- **Tooling.** `docs.sonarsource.com/.../user-guide/rules/overview` and `.../languages/java` WebFetch cleanly
  (verbatim properties + taxonomy + severities); the `clean-code` doc page 404'd at the 10.8 path (attribute→
  category mapping deferred). `rules.sonarsource.com` per-rule pages have been unreliable across the project
  (keys 07/10/13/14/16/18) → resolve rule metadata from the **RSPEC repo** (`github.com/SonarSource/rspec`,
  `rules/S<NNNN>/metadata.json`) at pin. The Oct-2024 product-rename press release is the authoritative naming
  source (not Wikipedia/blogs).
- **Cross-ref:** keys 05 (toolchain map / platform-vs-analyzer intro), 27/28/29/30 (the analyzers Sonar
  overlaps/ingests — cite each tool's own source), **37 (owns the cross-cutting overlap + layered-stack
  verdict — route ALL "which-to-run" content there)**, 38 (custom Sonar rules), 39 (suppression/baselines/
  ratcheting — `// NOSONAR`), 70 (SAST / taint depth), 78 (PR decoration), 59 (SQALE/debt depth), 80/88
  (gate/dashboard/CI), 36 (IDE inspections — Connected Mode). Record in merge notes.
</content>
</invoke>
