# RESEARCH DOSSIER — Java Code Quality Book

> **Part-IV synthesis (⚠ comparison-sensitive) dossier.** Key 37 is the **cross-cutting comparison + layered-stack**
> chapter the cluster routes its verdicts to: keys 27 (Checkstyle), 28 (PMD/CPD), 29 (SpotBugs), 30 (Error Prone),
> 31/32 (null-safety), 33 (ArchUnit), 34 (formatters), 35 (Sonar), 36 (IDE) go DEEP on each tool; **this** chapter
> owns *how they overlap, where they are redundant, and how to compose one coherent stack* — and hands the
> end-to-end gate design to key 109. NEUTRALITY is maximally load-bearing here (the whole chapter is comparison):
> each tool gets its strongest case AND its hardest limitation, every cross-tool fact cites that tool's OWN pinned
> source, NO tool is crowned, banned phrasings barred. Tool versions are `TO-PIN` in `SOURCE-PIN.md` §2 → rule/plugin
> **identity & mechanism** are verified from each tool's docs; exact **versions / GAV / default-ruleset membership /
> severities / thresholds** carry `⚠ verify at pin`. Empirical overlap figures are dated and attributed (not folklore).
> Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 37 — dossier key from `01-index/CANDIDATE_POOL.md` (row 79: "Comparing & layering the analyzers — overlap, redundancy, a coherent stack" · `⚠` · "synthesizes 27–36; relates 109")
- **Title:** Comparing & layering the analyzers — overlap, redundancy, and a coherent stack
- **Part:** Part IV — Static analysis, linting & formatting (cluster 27–36; synthesis node)
- **Tier:** B (Part-IV synthesis) · **Depth band:** Standard (cross-tool comparison + composition; tool-doc + empirical-study anchored)
- **Cmp:** **comparison-sensitive (`⚠`)** — the chapter *is* the comparison. Every analyzer is a comparison target; the
  full NEUTRALITY discipline applies (strongest case + hardest limitation per tool; every cross-tool claim cited to
  that tool's own pinned source; no crowning; banned phrasings barred). The **subject** — the *concept* of layering
  defenses, the analysis-substrate distinction (source vs bytecode vs compile-integrated), the overlap problem — is
  discussed freely; the **tools** are comparison targets covered in depth by 27–36 and synthesized here.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md` (analyzers run on
  both; substrate/mechanism facts are language-version-independent).
- **Primary dependency / source unit(s) (the comparison targets — each cited to its OWN pinned source, `SOURCE-PIN.md` §2):**
  - **Checkstyle** — source-AST style/convention checker; Maven `org.apache.maven.plugins:maven-checkstyle-plugin`
    (deep: key 27). `checkstyle.org` + `github.com/checkstyle/checkstyle`.
  - **PMD (+ CPD)** — source-AST rule engine + token-based copy-paste detector; Maven
    `org.apache.maven.plugins:maven-pmd-plugin` (deep: key 28). `pmd.github.io` + `github.com/pmd/pmd`.
  - **SpotBugs (+ FindSecBugs, fb-contrib)** — **bytecode** bug-pattern detector; Maven
    `com.github.spotbugs:spotbugs-maven-plugin` (deep: key 29). `spotbugs.github.io` + `spotbugs.readthedocs.io`.
  - **Error Prone (+ Refaster)** — **`javac` compiler plugin**, compile-time bug patterns + in-place fixes; GAV
    `com.google.errorprone:error_prone_core`, activated via `maven-compiler-plugin` `annotationProcessorPaths` +
    `-Xplugin:ErrorProne` (deep: key 30). `errorprone.info` + `github.com/google/error-prone`.
  - **SonarQube / SonarLint** — quality **platform** + rule engine that aggregates and tracks over time with quality
    gates (deep: key 35). `docs.sonarsource.com` + `rules.sonarsource.com` (`java:S###` rule IDs).
  - **NullAway / JSpecify / Checker Framework** — null-safety layer (deep: keys 31/32; referenced as a distinct concern).
  - **ArchUnit** — architecture rules as JUnit tests (deep: key 33; referenced as a distinct concern).
  - **Spotless / google-java-format / palantir-java-format / EditorConfig** — formatting layer (deep: key 34).
  - **IDE inspections** — IntelliJ IDEA / Eclipse, the author-time first line (deep: key 36).
  - **Build wiring:** Apache Maven + Gradle (`SOURCE-PIN.md` §4) — the lifecycle the stack composes into (deep: key 62/75–82).
- **Named empirical authority (dated, attributed — corroboration for the overlap claim):** Lenarduzzi, Lujan,
  Saarimäki, Palomba, *"A Critical Comparison on Six Static Analysis Tools: Detection, Agreement, and Precision"*
  (Better Code Hub, Checkstyle, Coverity Scan, **FindBugs**, PMD, SonarQube; 47 Java projects) —
  `arxiv.org/abs/2101.08832`. **Dating caveat:** the study includes **FindBugs** (now superseded by **SpotBugs**),
  so it is cited for the *low-agreement* finding (which supports layering), not as a current per-tool benchmark.
- **Canonical doc page(s):** each tool's own docs (above); Error Prone `errorprone.info/docs/installation` (javac-plugin
  mechanism); Maven plugin reference pages (`maven.apache.org/plugins/...`); the empirical study's abstract.
- **Canonical source path(s):** rule/mechanism facts trace to each tool's pinned source (`SOURCE-PIN.md` §2/§4).
  Companion artifact: `08-companion-code/37_comparing_layering_analyzers/`.

---

## 1. Core definition & purpose

**Central claim.** No single Java analyzer covers the field; each reasons over a **different substrate** (source text,
source AST, compiled bytecode, or the `javac` AST during compilation) at a **different moment** (author-time, compile,
post-compile, CI, platform), and so each *sees a different class of defect*. Independent comparison finds **little to no
agreement** between tools' findings (Lenarduzzi et al., 2021, dated below) — which means running several is *additive*
in coverage, not merely redundant. The same diversity creates the chapter's central tension: **overlap** (more than one
tool flags the same thing), **redundancy** (paying build time for duplicate findings), and **noise** (un-tuned tools
drowning the signal). This chapter's job is to make the trade-offs legible and to compose a **coherent, de-duplicated,
layered stack** — the menu (key 05's map) turned into a deliberate order. It does NOT re-teach each tool's rules (keys
27–36 own that) and it does NOT design the full end-to-end gate (key 109 owns that).

**The organizing axis (reused from the key-25 "approximation" shape, generalized).** Each analyzer is positioned by
**(a) substrate** — what it reads — and **(b) moment** — when it runs. Substrate determines *what is even visible*
(SpotBugs sees compiler-generated bytecode Checkstyle never reads; Checkstyle sees source layout SpotBugs has discarded);
moment determines *feedback latency* (Error Prone fails the compile; SpotBugs needs a post-compile pass; Sonar reports at
the platform). The "coherent stack" is the choice of *which substrate × moment cells to cover, once each*, so coverage is
maximized and duplication minimized.

**Which part of the pinned set provides it.**
- The *tools* are the `SOURCE-PIN.md` §2 comparison targets; each substrate/mechanism claim is cited to that tool's own docs.
- The *build lifecycle* the stack composes into is Maven/Gradle (`SOURCE-PIN.md` §4) — the substrate-vs-moment table maps
  onto concrete lifecycle phases (validate → compile → verify → CI gate).
- The *overlap evidence* is the dated empirical study (above), used as corroboration of the layering rationale.

**When this concern arose.** As soon as teams ran more than one analyzer (the FindBugs+PMD+Checkstyle trio of the late
2000s) the overlap/noise problem appeared; the modern form adds the compile-integrated layer (Error Prone) and the
platform layer (Sonar). The substrate distinctions are stable; the brands move (FindBugs → SpotBugs — `SOURCE-PIN.md`
records FindBugs as dead).

**Where it sits in the architecture.** This is a **build-time composition** topic: it spans author-time (IDE, key 36) →
compile (Error Prone, key 30) → post-compile (SpotBugs, key 29) → source-pass (Checkstyle/PMD, keys 27/28) → platform
(Sonar, key 35), wired through Maven/Gradle into a CI gate (keys 75–82). It changes no runtime behavior.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The substrate × moment matrix (the chapter's load-bearing artifact)

Every analyzer reads ONE of four substrates; that choice fixes both what it can see and when it can run. Verified per
tool from each tool's own docs (deep treatment in the cited key):

| Tool | Substrate (what it reads) | Moment (when it runs) | Maven wiring (GAV — `⚠ version verify at pin`) | Deep chapter |
|---|---|---|---|---|
| IDE inspections (IntelliJ/Eclipse) | source AST, live in editor | **author-time** (keystroke) | n/a (IDE) | 36 |
| Error Prone | **`javac` AST during compilation** | **compile-time** (fails the compile) | `com.google.errorprone:error_prone_core` via `maven-compiler-plugin` `annotationProcessorPaths` + `-Xplugin:ErrorProne` | 30 |
| Checkstyle | **source text + source AST** | source pass (pre/post compile) | `org.apache.maven.plugins:maven-checkstyle-plugin` | 27 |
| PMD | **source AST** | source pass | `org.apache.maven.plugins:maven-pmd-plugin` | 28 |
| PMD-CPD | **source tokens** (duplication) | source pass | (CPD goal of `maven-pmd-plugin`) | 28 |
| SpotBugs | **compiled bytecode (`.class`)** | **post-compile** pass | `com.github.spotbugs:spotbugs-maven-plugin` | 29 |
| Sonar (SonarQube/SonarLint) | source + (its own analyzers, can ingest others) | IDE + CI + **platform** (tracks over time) | Sonar Scanner / `sonar-maven-plugin` (key 35) | 35 |
| NullAway | `javac` AST (Error Prone sub-checker) | compile-time | rides Error Prone (key 31) | 31 |
| ArchUnit | **bytecode graph, asserted in JUnit tests** | **test phase** | a test dependency (key 33) | 33 |
| Spotless / google-java-format | **source text** | author-time / pre-commit / CI | `com.diffplug.spotless:spotless-maven-plugin` (key 34) | 34 |

**The three substrate facts that justify layering (each cited to the tool's own source):**
1. **Source vs bytecode.** Per the SpotBugs/PMD docs as compared in the cluster: PMD and Checkstyle read **source**;
   SpotBugs reads **bytecode**, so anything the compiler discards (e.g. unused imports, source-only constructs) is
   invisible to SpotBugs, while bytecode-level facts (some null-deref, redundant comparisons the compiler exposes) are
   invisible to a source reader. *(Substrate per each tool's docs; key 29 owns SpotBugs depth, key 28 owns PMD.)*
2. **Compile-integrated.** Error Prone runs **inside `javac`** as a compiler plugin (`-Xplugin:ErrorProne`), so its
   findings arrive as compile errors/warnings with the AST + type information the compiler already built, and it can
   emit **in-place fixes** (Refaster/`@BugPattern` suggested fixes) — verified, `errorprone.info/docs/installation`.
   This is the fastest-feedback substrate but is scoped to what `javac` can be made to check (key 30).
3. **Platform aggregation.** Sonar is a *platform*, not just an analyzer: it runs its own rule engine (`java:S###`),
   can ingest other tools' reports, and **tracks findings over time with quality gates** — a different role from a
   single-pass analyzer (key 35). This is the "tool ≠ platform" distinction from key 05.

### 2.2 Overlap, redundancy, noise — the three failure modes of a naive stack

- **Overlap (same finding from ≥2 tools).** Style/convention rules in particular overlap (Checkstyle, PMD, and Sonar
  each ship line-length / naming / empty-block rules). Empirically, *cross-tool* agreement is **low** (Lenarduzzi et
  al., 2021: "little to no agreement among the tools" across 47 projects — dated, see §3), so overlap is concentrated
  in a minority of rule families (mostly style) while the bulk of findings are tool-unique. The teaching: overlap is
  real but bounded; the fix is **assigning each concern to one owner tool**, not dropping tools.
- **Redundancy (paying twice).** Running two source-AST style checkers (Checkstyle *and* PMD's style rules *and* Sonar's
  style rules) costs build time and produces near-duplicate findings with no coverage gain. The composition decision is
  *which tool owns style, which owns bug patterns, which owns duplication, which owns architecture* — one owner per
  concern (the §2.3 concern-ownership map).
- **Noise (un-tuned, un-triaged).** Every static tool has false positives; an un-tuned full stack as a build-breaker
  trains developers to ignore the gate — the worst outcome. Managing this (suppression, baselines, ratcheting) is key
  39's job; this chapter only states that *the stack must be tuned before it is gated* (cross-ref 39, 87).

### 2.3 Concern-ownership map (the "coherent stack" mechanism — one owner per concern)

The composition principle: **assign each quality concern to exactly one primary tool**, layered by substrate × moment so
each runs at its cheapest moment. This is a *neutral routing*, not a ranking — each cell names the tool whose substrate
fits that concern, and any equally-valid alternative is named (per NEUTRALITY's "name the alternatives"):

| Concern | Primary owner (by substrate fit) | Equally-valid alternative(s) | Moment |
|---|---|---|---|
| Formatting | a formatter (Spotless + google-java-format / palantir) | EditorConfig + IDE save-actions | author-time / pre-commit |
| Style & convention | Checkstyle | PMD style rules; Sonar | source pass |
| Source-level bug patterns / smells | PMD | Sonar; Error Prone (compile subset) | source pass / compile |
| Bytecode bug patterns | SpotBugs (+ fb-contrib) | Sonar (own analyzer) | post-compile |
| Compile-time correctness + auto-fix | Error Prone | — (unique substrate) | compile |
| Null-safety | NullAway / JSpecify / Checker FW | Sonar null rules; SpotBugs `NP_*` | compile / build |
| Duplication | PMD-CPD | Sonar duplication | source pass |
| Architecture / dependency rules | ArchUnit | jQAssistant; Sonar architecture | test phase |
| Security (SAST) | FindSecBugs / Semgrep / CodeQL / Sonar | — (key 70 owns) | CI |
| Trend / debt / gate | Sonar (platform) | aggregators (Codacy) | platform |

The chapter's deliverable is this map turned into one Maven/Gradle build with **one owner per row**, ordered cheap-first
(format-check → compile+Error Prone → Checkstyle/PMD → SpotBugs → Sonar/coverage/mutation), with the gate policy and
local↔CI parity delegated to keys 75–82 and the false-positive management to key 39.

### 2.4 Ordering & fail-fast (build-time composition)

- **Order by feedback latency and cost (cheap, fast, common-failure first).** Format-check and Error Prone (rides the
  compile) fail earliest and cheapest; source-AST passes (Checkstyle/PMD) next; the SpotBugs **post-compile** bytecode
  pass after `compile`; heavier platform/coverage/mutation last (keys 47/48/79). Substrate dictates the *minimum* phase
  (SpotBugs cannot run before `compile`; Checkstyle/PMD can run at `validate`).
- **Local ↔ CI parity.** The same ordered checks run at pre-commit (key 82) and in CI (key 77) so the developer is not
  surprised at the gate. (Mechanics: keys 75–82.)
- **One gate policy.** Which findings break the build vs warn is a policy decision (keys 76/80) layered on top of this
  composition; false-positive/baseline management keeps it credible (key 39).

### 2.5 Reference units (atoms — table)

| Name | Type | Substrate / form | Fixed early? | Source |
|---|---|---|---|---|
| `org.apache.maven.plugins:maven-checkstyle-plugin` | Maven plugin GAV | runs Checkstyle (source) | `⚠ version verify at pin` | maven.apache.org plugin ref; key 27 ✅ identity |
| `org.apache.maven.plugins:maven-pmd-plugin` | Maven plugin GAV | runs PMD + CPD (source) | `⚠ version verify at pin` | maven.apache.org plugin ref; key 28 ✅ identity |
| `com.github.spotbugs:spotbugs-maven-plugin` | Maven plugin GAV | runs SpotBugs (bytecode) | `⚠ version verify at pin` | spotbugs.readthedocs.io; key 29 ✅ identity |
| `com.google.errorprone:error_prone_core` | GAV (javac plugin) | Error Prone via `annotationProcessorPaths` | `⚠ version verify at pin` | errorprone.info/docs/installation ✅ |
| `-Xplugin:ErrorProne` | javac compiler arg | activates Error Prone in `javac` | tool-version | errorprone.info/docs/installation ✅ |
| `-XDcompilePolicy=simple` | javac flag | required by Error Prone | tool-version | errorprone.info/docs/installation ✅ |
| `com.diffplug.spotless:spotless-maven-plugin` | Maven plugin GAV | runs Spotless (source format) | `⚠ version verify at pin` | github.com/diffplug/spotless; key 34 ✅ identity |
| `java:S###` | Sonar rule ID family | Sonar rule engine | `⚠ verify at pin` | rules.sonarsource.com (offline Feb 2026 — RSPEC); key 35 |
| Lenarduzzi et al. 2021 "little to no agreement … low precision" | empirical finding | 6 tools, 47 Java projects (incl. FindBugs — dated) | dated 2021 | arxiv.org/abs/2101.08832 ✅ (abstract) |
| substrate: source (Checkstyle/PMD) vs bytecode (SpotBugs) vs `javac` (Error Prone) | distinction | what each tool reads | per tool docs | each tool's docs ✅ (cluster) |

---

## 3. Evidence FOR (layering is the real-world practice — neutral, each tool its strongest case)

- **Empirical low agreement supports additive layering (dated, attributed).** Lenarduzzi, Lujan, Saarimäki & Palomba,
  *"A Critical Comparison on Six Static Analysis Tools"* (2021, `arxiv.org/abs/2101.08832`), studied six tools (Better
  Code Hub, Checkstyle, Coverity Scan, FindBugs, PMD, SonarQube) across **47 Java projects** and report **"little to no
  agreement among the tools and a low degree of precision"** (abstract, verbatim). The low-agreement half is the
  load-bearing FOR-evidence: tools mostly find *different* things, so running several is additive. **Dating caveat:**
  the study used **FindBugs** (now **SpotBugs**); cite it for the agreement finding, not as a current benchmark.
- **Each tool's strongest case is a distinct substrate (each cited to its own docs):**
  - *Checkstyle* — strongest at **source layout / convention** the compiler discards (formatting-adjacent style,
    Javadoc presence, naming patterns) that a bytecode reader cannot see (key 27).
  - *PMD (+ CPD)* — strongest at **source-AST smells and copy-paste duplication** (CPD is token-based across the source
    tree), a concern no bytecode tool addresses well (key 28).
  - *SpotBugs* — strongest at **bytecode bug patterns** (e.g. the `NP_*`/`MT_*` families) visible only after compilation,
    with **zero annotation cost** — drop in the plugin, get findings on existing code (key 29).
  - *Error Prone* — strongest at **compile-time feedback + in-place fixes**: findings arrive as compile errors with full
    `javac` type info, and many checks ship suggested fixes (Refaster) — the fastest-feedback substrate (key 30).
  - *Sonar* — strongest as a **platform**: aggregates findings, tracks them over time, and applies a **quality gate**,
    a role single-pass analyzers do not fill (key 35).
- **First-class build integration for the whole staple set.** Each staple ships an official Maven plugin / javac
  integration (GAVs in §2.5), so the composition is *supported*, not improvised (Maven/Gradle, `SOURCE-PIN.md` §4).
- **The substrate distinctions are stable across the LTS window.** Source/bytecode/`javac` substrate facts are
  language-version-independent; the stack composes identically on Java 21 and Java 25 (anchor verified; no JEP changes
  the analyzer substrate model).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each tool its hardest objection + when-NOT-to-use)

**The shared limits of a multi-analyzer stack (the chapter's honest centre):**
- *Overlap → redundant cost.* Style/convention rules overlap across Checkstyle, PMD, and Sonar; running all three for
  style pays build time for duplicate findings with no coverage gain. *When NOT to:* do not run multiple owners for the
  same concern — assign one owner per row (§2.3).
- *Noise erodes the gate.* Every static tool has false positives (the same Lenarduzzi study reports **low precision**
  across the set). An un-tuned full stack as a build-breaker gets ignored or disabled. *When NOT to:* never gate an
  un-tuned stack; tune + baseline first (key 39).
- *Build-time cost compounds.* Each layer adds time; a SpotBugs full-effort pass + Sonar + coverage + mutation can make
  CI slow (key 79). *When NOT to:* don't run the heaviest layers on every push if feedback latency suffers — split fast
  (pre-commit) vs full (CI) (keys 79/82).
- *More tools ≠ more quality.* Passing every analyzer is necessary, not sufficient — lint-clean ≠ bug-free (keys 04/47).
  Static analysis reasons without running the code; it does not replace tests.

**Each tool's hardest objection (each cited to its own substrate/docs — neutral, no crowning):**
- **Checkstyle** — *hardest objection:* it checks **typography/convention**, not behavior; it finds no bugs and its
  value depends entirely on the configured ruleset (key 27). *When NOT to rely on it alone:* for bug or security
  defects.
- **PMD** — *hardest objection:* source-AST smell rules are **heuristic** and rule-set-dependent; many smell rules
  produce style-of-judgment findings that need tuning, and CPD's duplication threshold is a tuned parameter (key 28).
  *When NOT:* as a bug oracle or without ruleset curation.
- **SpotBugs** — *hardest objection:* bytecode analysis puts findings **a step from the source**, and some detectors
  are heuristic (key 29 owns the per-pattern detail, e.g. the `IS2_INCONSISTENT_SYNC` ratio noted in key 25). *When
  NOT:* as proof of correctness or as a build-breaker without triage.
- **Error Prone** — *hardest objection:* scoped to what can run **inside `javac`** and to its enabled check set; it
  needs the `maven-compiler-plugin` `annotationProcessorPaths` + `-Xplugin:ErrorProne` wiring (and `--release`/module
  flags can complicate setup) (key 30). *When NOT:* as a substitute for the post-compile bytecode view (SpotBugs) or
  for whole-program dataflow (CodeQL).
- **Sonar** — *hardest objection:* it is a **platform** (server/scan infrastructure, licensing for some editions) and
  its findings overlap the standalone analyzers, so running Sonar *and* the standalone set duplicates effort unless
  ownership is assigned (key 35). *When NOT:* if a lightweight build-only stack suffices and the trend/gate platform is
  not needed.

**Competing approaches *inside* Java code quality — neutral framing.** The analyzers take **different approaches to the
same goal**: Checkstyle/PMD read source, SpotBugs reads bytecode, Error Prone runs in `javac`, Sonar aggregates as a
platform. A team may run several — they overlap on a minority of (mostly style) rules but cover different blind spots.
Each choice states its trade-off; **none is crowned**. The cross-cutting verdict on overlap/redundancy is owned **here**
(per the cluster note); the per-tool depth lives in 27–36; the full end-to-end gate is key 109.

---

## 5. Current status

- **All staples active and integrated (2026 — date in draft).** Checkstyle, PMD/CPD, SpotBugs, Error Prone, Sonar,
  Spotless, ArchUnit are maintained with current Maven/Gradle integration (`SOURCE-PIN.md` §2/§4). *(Exact latest-stable
  versions + plugin GAVs are `TO-PIN` → `⚠ verify at pin`; per-tool currency is owned by 27–36.)*
- **Dead — do not cite as current:** **FindBugs** (→ **SpotBugs**); `findbugs-maven-plugin` (→ `spotbugs-maven-plugin`).
  The Lenarduzzi 2021 study's FindBugs row is the live instance of this dating discipline.
- **Substrate model stable.** The source/bytecode/`javac`/platform substrate distinctions have not changed across the
  21→25 window; no anchor or forward-LTS JEP alters the analyzer composition story.
- **Moving frontier (context, not asserted here):** Sonar rule-engine evolution, Error Prone check additions, and
  SpotBugs detector updates move per tool release — re-trace at `/pin-source`; the *composition principle* (one owner
  per concern, ordered by substrate × moment) is stabler than any tool's rule list.
- **Deprecations:** none in the composition model. `rules.sonarsource.com` reported offline (Feb 2026, keys 07/12/13/14)
  — Sonar rule IDs cited from RSPEC / in-product at pin.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `37_comparing_layering_analyzers` *(row not yet present — see §7 flag)*.
  - **Demo name:** "One module, four substrates — the layered analyzer stack on `org.acme.storefront`."
  - **Java Quality surface exercised:** a single small Maven module in the shared `org.acme.storefront` domain (e.g. an
    `OrderPricing` class) with the **layered staple stack** wired into one build — Spotless (format), Error Prone
    (compile), Checkstyle (style), PMD+CPD (smell+duplication), SpotBugs (bytecode) — each assigned **one concern**
    (the §2.3 ownership map), each emitting exactly one representative finding on a deliberately seeded defect, ordered
    cheap-first. A second, fixed variant passes all layers. This is the *composition* the chapter teaches; it doubles as
    a slice of the key-109 reference stack and the key-05 staple-stack seed.
  - **TRY-IT exercise:** seed one defect per substrate — a mis-format (Spotless), an Error-Prone-caught compile bug, a
    Checkstyle style violation, a PMD smell + a copy-pasted block (CPD), and a SpotBugs bytecode pattern — then run
    `./mvnw -B verify` and observe **each layer catch its own concern, in order**; then deliberately add the *same*
    style rule to PMD that Checkstyle already owns and observe the **duplicate finding** (the overlap/redundancy lesson);
    remove the duplicate to restore one-owner-per-concern; finally fix all defects and watch the ordered stack go green.
- **Module key / path:** `08-companion-code/37_comparing_layering_analyzers/`
- **Intended dependencies (verified @pin where identity-confirmed; versions `⚠ verify at pin`):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.diffplug.spotless:spotless-maven-plugin` | format layer (owns formatting) | github.com/diffplug/spotless (TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_core` (+ `-Xplugin:ErrorProne`, `-XDcompilePolicy=simple`) | compile layer (owns compile-time bugs) | errorprone.info/docs/installation | ☑ mechanism; ☐ version |
  | `org.apache.maven.plugins:maven-checkstyle-plugin` | style layer (owns convention) | maven.apache.org (TO-PIN) | ☐ verify at pin |
  | `org.apache.maven.plugins:maven-pmd-plugin` | smell + CPD layer (owns smells/duplication) | maven.apache.org (TO-PIN) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs-maven-plugin` | bytecode layer (owns bytecode patterns) | spotbugs.readthedocs.io (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts fixed variant correct) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose plugin versions (all from the parent/property).
  - **Externalized config / profiles** — each analyzer's ruleset is the "config": `checkstyle.xml`, a PMD ruleset XML, a
    `spotbugs-exclude.xml`, Spotless format config — each owning exactly one concern; a build profile that runs the fast
    layers (format + Error Prone + Checkstyle) vs a `full` profile that adds SpotBugs (the fast-vs-full split, key 79).
  - **At least one test** — asserts the **fixed** `OrderPricing` behaves correctly (names the pricing behavior asserted).
  - **Observability / health surface** — the build/analysis reports (target/site or the aggregated finding output) are
    the surface; name where the layered findings are collected (cross-ref key 35 platform, key 78 PR annotations).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the seeded-defect variant is the failure path — the ordered
    stack **fails at the cheapest layer first** (Spotless/Error Prone before SpotBugs), demonstrating fail-fast; AND the
    deliberately-duplicated style rule demonstrates the **redundancy** limit (two tools flag one thing). State in the
    chapter that the duplicate finding **is** the demonstrated honest-limitation (overlap costs build time without
    coverage gain), resolved by one-owner-per-concern.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `layered-build` | the ordered plugin block (format → compile/EP → checkstyle → pmd → spotbugs) | `pom.xml` |
  | `errorprone-wiring` | `maven-compiler-plugin` `annotationProcessorPaths` + `-Xplugin:ErrorProne` | `pom.xml` |
  | `one-owner-concern` | the concern→tool ownership comments mapping §2.3 to config | `pom.xml` |
  | `seeded-defects` | the one-defect-per-substrate variant (the failure path) | `BrokenOrderPricing.java` |
  | `clean-variant` | the fixed class that passes every layer | `OrderPricing.java` |
  | `pricing-test` | test asserting the fixed pricing behavior | `OrderPricingTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/37_comparing_layering_analyzers verify` (and `-Pfull` to add SpotBugs).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with defects present — the ordered stack fails at the **earliest** offended layer (format/Error
  Prone), and after fixing those, Checkstyle/PMD/SpotBugs each report their seeded finding in turn; the duplicate-style
  experiment shows the same finding from two tools. Fixed + one-owner-per-concern — green build, single finding per
  concern, test pass count green.
- **Figure plan** (GUIDELINES §8; **standard comparison/synthesis chapter** → image budget ~**2 designed diagrams + 1
  captured screenshot**; not a zero-figure chapter — this chapter's whole value is the spatial composition):
  - **Chapter class:** standard comparison/synthesis chapter (the comparison matrix + the composed pipeline each earn a diagram).
  - **Candidate designed diagram(s) + family:**
    - **Fig 37.1 — the substrate × moment matrix:** a grid with **substrate** (source text / source AST / bytecode /
      `javac` AST / platform) on one axis and **moment** (author-time → compile → post-compile → CI → platform) on the
      other, each analyzer placed in its cell; family = *coverage-matrix diagram*. This is the §2.1 table as a figure —
      the reader's "what reads what, when" lookup. Trace each placement to that tool's own docs (Error Prone =
      `javac`/compile per errorprone.info; SpotBugs = bytecode/post-compile per spotbugs.readthedocs.io;
      Checkstyle/PMD = source per their docs; Sonar = platform per docs.sonarsource.com).
    - **Fig 37.2 — the coherent layered pipeline (one owner per concern):** a left-to-right build pipeline (format-check
      → compile+Error Prone → Checkstyle/PMD → SpotBugs → Sonar/coverage/mutation gate) with each stage labeled by its
      *single owned concern* and its Maven phase, arrows = fail-fast order; family = *pipeline / build-lifecycle
      diagram* (sibling of Fig 05.1, scoped to the analyzer layer). Trace each stage to its plugin GAV (§2.5) and Maven
      phase; cross-ref key 109 for the full gate.
  - **Candidate captured surface(s):** **Fig 37.3** — a build-log capture of the layered `./mvnw verify` run showing
    each layer's finding in order (and the duplicate-finding experiment), from the companion module. Capture only real
    tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every substrate/moment cell → that tool's own pinned doc; every pipeline stage →
    its plugin GAV (§2.5) + the Maven lifecycle reference; the "low agreement" callout (if depicted) → arxiv.org/abs/2101.08832
    (dated, FindBugs-caveated).

---

## 7. Gap-filling (verification queue)

- ⚠ **Plugin GAV versions** — `maven-checkstyle-plugin`, `maven-pmd-plugin`, `spotbugs-maven-plugin`,
  `error_prone_core`, `spotless-maven-plugin`, Sonar scanner: all `TO-PIN` in `SOURCE-PIN.md` §2/§4 → confirm exact
  latest-stable version + coordinates at `/pin-source` before stating any number. GAV **identity** verified; **versions**
  not. (Per-tool depth + defaults owned by 27–36; this chapter cites identity + mechanism only.)
- ⚠ **Error Prone javac wiring atoms** — `-Xplugin:ErrorProne`, `-XDcompilePolicy=simple`,
  `--should-stop=ifError=FLOW`, `annotationProcessorPaths`: verified from `errorprone.info/docs/installation` (live
  line); re-confirm byte-identical at the pinned Error Prone version.
- ⚠ **Empirical overlap figures** — Lenarduzzi et al. 2021: the **"little to no agreement … low precision"** wording is
  verbatim from the abstract; exact per-tool agreement/precision *numbers* are in the full paper (the PDF did not decode
  via WebFetch) → if a specific number is used in the draft, fetch the full paper at draft and quote exactly; otherwise
  use the qualitative finding only. **FindBugs-dating caveat must accompany every cite.** → flag filed.
- ⚠ **Sonar rule IDs (`java:S###`)** — never cite a specific Sonar rule ID from memory; `rules.sonarsource.com` offline
  (Feb 2026) → resolve from RSPEC (`sonarsource.github.io/rspec/`) or in-product at pin (keys 07/12/13/14). Key 35 owns.
- ⚠ **Default-ruleset membership / severities / thresholds** for every analyzer — version-sensitive; owned by 27–36;
  cite "rule/tool identity + `verify at pin`" here, never a default value.
- **Open question (draft / cluster boundary):** the boundary with key 109 (the full end-to-end reference stack & gate
  design) and key 39 (false-positive/baseline management). Propose: **this** chapter owns *the comparison + the
  one-owner-per-concern composition principle*; **109** owns *the worked end-to-end gate*; **39** owns *living with the
  findings*; **05** owns *the landscape map*; **27–36** own *each tool's depth*. Cross-ref 27/28/29/30/31/32/33/34/35/36
  (the tools), 38 (custom rules over them), 39 (findings), 75–82 (CI/gate), 109 (reference stack), 05 (map), 70 (SAST).
- **DEMO-CATALOG.md row** for `37_comparing_layering_analyzers` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/37_tool_gavs_and_defaults_unverified.md` — all analyzer plugin GAVs/versions are `TO-PIN`; GAV identity +
  Error Prone javac-wiring + substrate facts verified from each tool's docs, but exact versions, default-ruleset
  membership, severities, thresholds, and Sonar `java:S###` rule IDs are `⚠ verify at pin` (re-trace after `/pin-source`).
- `09-flags/37_empirical_overlap_findbugs_dated.md` — the Lenarduzzi et al. (2021) overlap/precision study includes
  **FindBugs** (superseded by **SpotBugs**); cite for the *qualitative low-agreement* finding only, always with the
  dating caveat; per-tool numbers require fetching the full paper at draft (PDF did not decode via WebFetch).

---

## 8. Sources & further reading

### Primary / Official (live-line; verify at `/pin-source`)
| # | Source | Title | URL / path | Verified |
|---|---|---|---|---|
| 1 | Tool doc | Error Prone — installation (javac plugin; `error_prone_core`; `-Xplugin:ErrorProne`; `annotationProcessorPaths`; `-XDcompilePolicy=simple`) | errorprone.info/docs/installation | ☑ mechanism (live-line) |
| 2 | Tool doc | SpotBugs — bug descriptions / Maven plugin (bytecode substrate; `com.github.spotbugs:spotbugs-maven-plugin`) | spotbugs.readthedocs.io ; spotbugs.github.io | ☑ identity (key 29 depth) |
| 3 | Build doc | Apache Maven Checkstyle / PMD plugin references (`org.apache.maven.plugins:maven-checkstyle-plugin` / `maven-pmd-plugin`) | maven.apache.org/plugins | ☑ GAV identity |
| 4 | Tool doc | PMD / CPD — source-AST rules + token duplication | pmd.github.io | ☑ identity (key 28 depth) |
| 5 | Tool doc | Checkstyle — source style/convention | checkstyle.org | ☑ identity (key 27 depth) |
| 6 | Tool doc | Sonar — rule engine (`java:S###`) + quality-gate platform | docs.sonarsource.com ; rules.sonarsource.com (offline Feb 2026 → RSPEC) | ☐ verify at pin (key 35) |
| 7 | Tool doc | Spotless — `com.diffplug.spotless:spotless-maven-plugin` (source format) | github.com/diffplug/spotless | ☑ identity (key 34 depth) |
| 8 | Build doc | Maven / Gradle lifecycle (phase mapping for substrate-vs-moment) | maven.apache.org ; docs.gradle.org | ☑ (integration) |

### Accessible / Further reading (dated/secondary — corroboration only)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Study (peer-reviewed) | Lenarduzzi, Lujan, Saarimäki, Palomba — "A Critical Comparison on Six Static Analysis Tools: Detection, Agreement, and Precision" (6 tools incl. FindBugs — DATED; 47 Java projects; "little to no agreement … low precision") | arxiv.org/abs/2101.08832 | ☑ abstract (FindBugs-dated; numbers in full paper) |
| 2 | Curated list | Checkstyle wiki — Java static code analysis tools (landscape) | github.com/checkstyle/checkstyle/wiki | ☑ (landscape; via key 05) |
| 3 | Real-world stack | GeoServer / GeoTools QA guide — a layered Checkstyle+PMD+SpotBugs+Error Prone+format Maven stack | docs.geoserver.org / docs.geotools.org developer QA | ☐ (color; URLs shift across versions) |

> Source-quality order applied: each tool's own doc (substrate/mechanism/GAV identity) → Maven/Gradle build docs
> (lifecycle phases) → peer-reviewed comparison (overlap finding, dated) → curated lists / real-world stacks (color).
> Every cross-tool claim cites the named tool's own pinned source (NEUTRALITY "cited-source requirement"); the FindBugs
> study is dated and caveated; no content farms.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | WebSearch — six-tool comparison / overlap / precision | arxiv listings | identified Lenarduzzi et al. 2101.08832 (the ScienceDirect/key-05 study); CWE-coverage & detection-count figures from secondary surveys noted but NOT asserted (need primary) |
| 2 | WebSearch — substrate distinction (source/bytecode/compile) | tool roundups | confirmed: Checkstyle=style/source, PMD=source, SpotBugs=bytecode ("checks bytecode, whereas PMD scans source"), Error Prone=compile-time javac plugin |
| 3 | WebFetch arxiv.org/abs/2101.08832 (abstract) | arxiv | tools = Better Code Hub/Checkstyle/Coverity/FindBugs/PMD/SonarQube; 47 Java projects; "little to no agreement among the tools and a low degree of precision" (verbatim abstract) |
| 4 | WebFetch arxiv PDF | arxiv | PDF did not decode via WebFetch (FlateDecode) — per-tool numbers deferred to full-paper fetch at draft; flagged |
| 5 | WebSearch + WebFetch — Maven plugin GAVs | maven.apache.org / spotbugs / errorprone | GAVs: `org.apache.maven.plugins:maven-checkstyle-plugin` / `:maven-pmd-plugin`; `com.github.spotbugs:spotbugs-maven-plugin`; `com.google.errorprone:error_prone_core` |
| 6 | WebFetch errorprone.info/docs/installation | errorprone.info | Error Prone = javac plugin via `maven-compiler-plugin` `annotationProcessorPaths` + `-Xplugin:ErrorProne` + `-XDcompilePolicy=simple` + `--should-stop=ifError=FLOW` (verbatim mechanism) |
| 7 | Read keys 05, 25 dossiers + CANDIDATE_POOL row 79 + cluster notes | repo | confirmed 37 owns cross-cutting comparison + layered stack; 109 owns end-to-end gate; 27–36 own per-tool depth; 39 owns findings; reused key-25 substrate-proxy shape + key-05 map |

---
## Learnings & pipeline suggestions
- **Reusable shape — "substrate × moment matrix" for any tool-comparison/synthesis chapter.** Generalizing the key-25
  "approximation-of-a-spec-property" shape to a *multi-tool synthesis*: position each tool by (a) the **substrate** it
  reads (source text / source AST / bytecode / `javac` AST / platform) and (b) the **moment** it runs — the two axes
  *jointly* explain coverage diversity (substrate = what is visible) and feedback latency (moment), make NEUTRALITY
  structural (each tool = a different cell, no winner), and turn "the coherent stack" into "cover each cell once =
  one-owner-per-concern." Reuse for keys 70 (SAST tools), 65/73 (security stack), 47/48 (test-adequacy stack), 109.
- **Synthesis-chapter scope discipline (extends the key-05 "map stays thin" rule).** A `⚠` synthesis node (37, 109, 05)
  must NOT re-teach per-tool configuration — that is the deep chapters' job (27–36). 37 owns *comparison + composition
  principle*; 109 owns *the worked end-to-end gate*; 39 owns *findings*. Drift symptom: the chapter starts listing a
  tool's rule IDs. Record the routing in merge notes so the cluster does not duplicate "what is a rule / how to suppress"
  across 05/27–36/37/39/109.
- **Dated-empirical discipline (live instance).** The standard Java six-tool comparison (Lenarduzzi et al. 2021) still
  uses **FindBugs**, which `SOURCE-PIN.md` records as dead. Cite it for the *qualitative* low-agreement finding (which is
  the layering rationale) with an explicit dating caveat; do NOT promote its per-tool numbers as current SpotBugs/Sonar
  benchmarks. Pairs with the folklore guard — a peer-reviewed paper can still carry a superseded tool. Filed
  `09-flags/37_empirical_overlap_findbugs_dated.md`.
- **Tooling:** the arxiv abstract page (`arxiv.org/abs/NN`) decodes cleanly via WebFetch; the **PDF** (`/pdf/NN`) returns
  FlateDecode binary that WebFetch cannot read — fetch the abstract for the headline finding and defer exact numbers to a
  draft-time full-paper read (or a local `pdftotext`). Reconfirms `rules.sonarsource.com` offline (cite RSPEC). →
  append to PIPELINE-LEARNINGS.
- **Cross-ref:** keys 05 (the map), 27 (Checkstyle), 28 (PMD/CPD), 29 (SpotBugs), 30 (Error Prone), 31/32 (null-safety),
  33 (ArchUnit), 34 (formatters), 35 (Sonar), 36 (IDE), 38 (custom rules), 39 (findings/baselines), 70 (SAST), 75–82
  (CI/gate), 109 (reference stack). Record in merge notes.
</content>
</invoke>
