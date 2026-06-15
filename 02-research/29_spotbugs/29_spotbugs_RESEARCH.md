# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) **single-tool, comparison-aware** dossier (cluster 27/28/29/30 + 36 analyzers). The
> subject is **SpotBugs** — a static analyzer that reasons over **compiled Java bytecode** to find
> instances of "bug patterns" — together with its two staple plugins, **FindSecBugs** (security
> detectors) and **fb-contrib** (extra correctness/quality detectors). Row 29 carries the `⚠`
> comparison glyph in `CANDIDATE_POOL.md`, so NEUTRALITY is load-bearing: SpotBugs gets its strongest
> case AND its hardest limitation; where it overlaps Checkstyle (27), PMD (28), Error Prone (30) or
> Sonar/CodeQL (35), the **cross-cutting verdict routes to key 37**; the SAST overlap routes to key 70.
> No tool is crowned; every cross-tool fact cites the named tool's own pinned source.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. Tool rows are `TO-PIN` in `SOURCE-PIN.md §2`;
> SpotBugs docs read at the live line announce **SpotBugs 4.10.2**, **spotbugs-maven-plugin 4.9.8.4**,
> **findsecbugs-plugin 1.14.0**, **fb-contrib 7.6.11** — these exact version numbers are
> `⚠ verify at pin`. Rule/pattern **identity** is verified from each tool's own docs; exact
> **default rank/effort membership / GAV version** carry `⚠ verify at pin`. Untraceable atoms →
> `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 29 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** SpotBugs (+ FindSecBugs, fb-contrib) — bytecode bug patterns
- **Part:** Part IV — Static analysis, linting & formatting (cluster 27/28/29/30 + 36)
- **Tier:** B · **Depth band:** Standard (deep single-tool + plugin ecosystem, with comparison awareness)
- **Cmp:** **⚠ comparison-sensitive** (CANDIDATE_POOL row 29). SpotBugs is a comparison **target** covered
  in depth; the cross-cutting "which analyzer / how they layer" verdict belongs to **key 37**, the SAST
  comparison to **key 70**, custom-detector authoring to **key 38**, and the concurrency-pattern slice to
  **key 25**. This chapter discusses the *subject* (the JVM/bytecode it reads; the JLS contracts the
  patterns approximate) freely, and treats Checkstyle/PMD/Error Prone/Sonar as named comparison targets
  with each claim cited to that tool's own pinned source and no crowning.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (rule IDs / config keys / flags / GAV — verified at the live line, versions `⚠ verify at pin`):**
  - **SpotBugs core** — `spotbugs.readthedocs.io/en/latest/` (doc states version **4.10.2**), repo
    `github.com/spotbugs/spotbugs`. Requires a **Java 11+** runtime; scans bytecode produced by JDK 11+.
  - **Maven** — `com.github.spotbugs:spotbugs-maven-plugin` (doc shows **4.9.8.4**); goals `spotbugs`,
    `check`, `gui`, `help`. Override the analyzer engine via a `com.github.spotbugs:spotbugs` dependency.
  - **Gradle** — the SpotBugs Gradle Plugin (`com.github.spotbugs.snom` / `spotbugs` id); generates
    `spotbugsMain` / `spotbugsTest` tasks per `sourceSet`; `spotbugs { toolVersion = '4.10.2' }`; wired
    into `check`.
  - **FindSecBugs** — `com.h3xstream.findsecbugs:findsecbugs-plugin` (doc shows **1.14.0** on the home
    page / Gradle doc; **1.12.0** in the Maven example — version drift noted, `⚠ verify at pin`).
    `find-sec-bugs.github.io` states **144 bug patterns** over **826 unique API signatures**, OWASP
    Top 10 + CWE references.
  - **fb-contrib** — `com.mebigfatguy.fb-contrib:fb-contrib` (Maven Central latest **7.6.11**, Jun 2025;
    README pins an older 7.0.3 — `⚠ verify at pin`).
  - **Bug-pattern catalogue** — `spotbugs.readthedocs.io/en/latest/bugDescriptions.html` (single page,
    ~595 KB); nine bug **categories** (Bad practice, Correctness, Experimental, Internationalization,
    Malicious code vulnerability, Multithreaded correctness, Performance, Security, Dodgy code).
  - **Filter / exclude grammar** — `spotbugs.readthedocs.io/en/latest/filter.html`
    (`<Match>`, `<Bug>`, `<Class>`, `<Method>`, `<Field>`, `<Rank>`, `<Confidence>`, `<Priority>`,
    `<Or>`/`<And>`/`<Not>`); **bug rank 1–20** (1–4 scariest, 5–9 scary, 10–14 troubling, 15–20 of concern).
  - **Annotations** — `com.github.spotbugs:spotbugs-annotations` (doc **4.10.2**); incl.
    `@SuppressFBWarnings`, `@CheckForNull`, `@CheckReturnValue`, `@NonNull`, `@Nullable`, `@DefaultAnnotation`.
  - **Effort** — `spotbugs.readthedocs.io/en/latest/effort.html` (levels `min` / `less` / `more` / `max`;
    "The default effort configuration is same with `more`").
  - **JLS / library contracts the patterns approximate** — JLS SE 21 (`equals`/`hashCode`, serialization,
    null), cited where a pattern enforces a documented contract (cross-ref keys 15/16).
- **Canonical doc page(s):** `spotbugs.readthedocs.io/en/latest/{introduction,maven,gradle,filter,effort,annotations,bugDescriptions}.html`;
  `find-sec-bugs.github.io/{,bugs.htm}`; fb-contrib README `github.com/mebigfatguy/fb-contrib`.
- **Canonical source path(s):** language facts live in the JLS (not a repo). Tool atoms trace to each
  tool's pinned source (`SOURCE-PIN.md §2`). Companion artifact: `08-companion-code/29_spotbugs/`.

---

## 1. Core definition & purpose

**Central claim.** SpotBugs is "a program to find bugs in Java programs … it looks for instances of
*bug patterns* — code instances that are likely to be errors" (verbatim, `introduction.html`, SpotBugs
4.10.2 doc). What sets it apart **as an approach** is *what it reads*: SpotBugs analyzes **compiled
`.class` bytecode**, not source text. Because it works on the artifact the compiler actually emits, it can
catch defects that are invisible or awkward to see at the source level — e.g. comparing objects of
unrelated types, a `volatile++` that compiles to a non-atomic read-modify-write, a null dereference on an
exception path the source obscures — while paying the price that it cannot see formatting/style and its
messages sit one step removed from the source. It is the **bytecode** corner of the analyzer map (key 05):
Checkstyle/PMD read **source/AST**, Error Prone runs **inside `javac`**, SpotBugs reads **bytecode after
compilation** (each cited to its own docs; the layering verdict is key 37's).

**The lineage fact (HARD — folklore guard).** SpotBugs is the **successor to FindBugs**, which is no longer
maintained. The docs ship a "Guide for migration from FindBugs 3.0 to SpotBugs 3.1" and SpotBugs retains
the historic `edu.umd.cs.findbugs.*` package names (e.g. `edu.umd.cs.findbugs.annotations.SuppressFBWarnings`)
for compatibility. The book **never cites FindBugs or `findbugs-maven-plugin` as current** — the live tool
is SpotBugs / `spotbugs-maven-plugin` (CANDIDATE_POOL stale-name note; SOURCE-PIN §2).

**Which part of the pinned set provides it.**
- The *tool* is the comparison target: SpotBugs core + the FindSecBugs and fb-contrib **detector plugins**
  (each cited to its own doc/repo at its pin).
- The *correctness criteria* many patterns approximate are JDK/JLS contracts — e.g. the `equals`/`hashCode`
  contract (key 15), serialization (`Serializable`/`serialVersionUID`), the JMM data-race property
  (JLS §17.4.5, key 25). SpotBugs checks **tractable bytecode proxies** for these; that is the source of
  both its value and its false positives/negatives (the "approximation-of-a-spec-property" shape, key 25).

**When introduced / version.** SpotBugs 1.0 forked from FindBugs in 2016 (SpotBugs is "© 2016-2022,
spotbugs community" in the doc footer). The live doc describes **version 4.10.2**; the analyzer requires a
**Java 11+** runtime and scans bytecode from **JDK 11 and newer** (Java 11+ support is labelled
"experimental" in the doc — `⚠`, see §4). Confirm exact versions at pin.

**Where it sits in the architecture.** **Build-time / post-compilation bytecode analysis.** It runs as a
*separate analysis step* after `javac` (Maven `check`/`spotbugs` goal; Gradle `spotbugsMain` task, which
"uses compiled `.class` files as input, so it will run after java compilation"). It does not change runtime
behavior. FindSecBugs and fb-contrib are **detector plugins loaded into the same SpotBugs engine** — they
are part of the SpotBugs analysis, not separate tools (so they are SpotBugs capabilities, not rivals).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 What "a bug pattern over bytecode" means

A SpotBugs **detector** walks the bytecode of each analyzed class and reports **bug instances** that match
a known **bug pattern** (an abbreviation + code, e.g. `EC_UNRELATED_TYPES`). Each instance carries:
- a **bug pattern type** (the `_`-delimited code) and an **abbreviation** (the prefix, e.g. `NP`, `EC`, `DLS`);
- a **category** — one of nine: *Bad practice, Correctness, Experimental, Internationalization,
  Malicious code vulnerability, Multithreaded correctness, Performance, Security, Dodgy code*
  (verified, `bugDescriptions.html`);
- a **bug rank 1–20** — "1 to 4 are scariest, 5 to 9 scary, 10 to 14 troubling, and 15 to 20 of concern"
  (verbatim, `filter.html`);
- a **confidence / priority** (formerly "priority": High/Medium/Low; `<Confidence>`/`<Priority>` filter
  elements).

Analysis depth is governed by **effort** (`effort.html`): `min` / `less` / `more` / `max`, and "The default
effort configuration is same with `more`." Higher effort enables interprocedural analysis, value-number
tracking in null-pointer analysis, etc.; `max` adds interprocedural analysis of referenced (non-application)
classes. `min` adds "Conserve Space" (precision/memory trade) and "Skip Huge Methods" (skip a method whose
bytecode length exceeds 6,000).

### 2.2 Setup / build-time behavior — how it plugs into the build

**Maven** (`maven.html`, verbatim coordinates):
```xml
<plugin>
  <groupId>com.github.spotbugs</groupId>
  <artifactId>spotbugs-maven-plugin</artifactId>
  <version>4.9.8.4</version>
</plugin>
```
Goals: **`spotbugs`** (run + report), **`check`** (run and **fail the build** on findings — the gate),
`gui`, `help`. The analyzer engine version is overridable by adding a `com.github.spotbugs:spotbugs`
dependency inside the plugin (the Maven doc example overrides to `4.10.2`).

**Gradle** (`gradle.html`, verbatim): the plugin "generates task for each sourceSet … `spotbugsMain` and
`spotbugsTest`", adds a `check` → `spotbugs*` task dependency (so `./gradlew check` runs it), and is
configured via `spotbugs { toolVersion = '4.10.2' }`. Gradle v7.0+ is required (v6 unsupported). A
"Base Plugin" variant generates no tasks (configure from scratch).

**Loading the plugins:**
- FindSecBugs — Maven: a nested `<plugin>` (`com.h3xstream.findsecbugs:findsecbugs-plugin`) inside the
  spotbugs-maven-plugin `<configuration>`, typically with `spotbugs-security-include.xml` /
  `-exclude.xml` filters (verbatim, `maven.html`). Gradle: `spotbugsPlugins
  'com.h3xstream.findsecbugs:findsecbugs-plugin:1.14.0'` in `dependencies` (verbatim, `gradle.html`).
- fb-contrib — added the same way (`spotbugsPlugins 'com.mebigfatguy.fb-contrib:fb-contrib:<ver>'` /
  nested Maven `<plugin>`). It is "a FindBugs/SpotBugs plugin for doing static code analysis on java byte
  code" (verbatim, fb-contrib README).

### 2.3 Active behavior — the catalogue (codes + verbatim short descriptions, verified from `bugDescriptions.html`)

**Correctness (the family SpotBugs is known for):**
- `NP_NULL_ON_SOME_PATH` — "Possible null pointer dereference in method on exception path"
  (and the family `NP_ALWAYS_NULL`, `NP_NULL_ON_SOME_PATH_EXCEPTION`, `NP_CLOSING_NULL`).
- `EC_UNRELATED_TYPES` — "Call to equals() comparing different types"
  (and `EC_UNRELATED_TYPES_USING_POINTER_EQUALITY`).
- `RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE` — redundant null comparison; and
  `RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE` — "Nullcheck of value previously dereferenced."
- `BC_IMPOSSIBLE_CAST` / `BC_IMPOSSIBLE_DOWNCAST` — "Impossible cast / downcast" (only visible at bytecode).
- `GC_UNRELATED_TYPES` — generic-collection call with an argument of an incompatible type.
- `SA_FIELD_SELF_ASSIGNMENT` / `SA_LOCAL_SELF_COMPARISON` — self-assignment / self-comparison.

**Bad practice / contract-enforcing (cross-ref key 15, key 16):**
- `HE_EQUALS_USE_HASHCODE` — "Class defines equals() and uses Object.hashCode()"; and
  `HE_HASHCODE_NO_EQUALS`, `HE_INHERITS_EQUALS_USE_HASHCODE` (the equals/hashCode contract, JLS / `Object`
  Javadoc — key 15).
- `SE_NO_SERIALVERSIONID` — "Class is Serializable, but doesn't define serialVersionUID";
  `SE_READ_RESOLVE_MUST_RETURN_OBJECT`.
- `RV_RETURN_VALUE_IGNORED_BAD_PRACTICE` / `RV_RETURN_VALUE_IGNORED` — ignored return value
  (ties to `@CheckReturnValue`); `RV_DONT_JUST_NULL_CHECK_READLINE`.
- `NP_TOSTRING_COULD_RETURN_NULL` — "toString method may return null."

**Dodgy code / Performance / i18n:**
- `DLS_DEAD_LOCAL_STORE` — "Dead store to local variable" (and `DLS_DEAD_LOCAL_STORE_IN_RETURN`).
- `URF_UNREAD_FIELD` — unread field; `SIC_INNER_SHOULD_BE_STATIC` — "Should be a static inner class."
- `DMI_INVOKING_TOSTRING_ON_ARRAY` — toString on an array; `DM_*` performance patterns
  (e.g. boxing/`String` construction); `Dm_DEFAULT_ENCODING` — reliance on the default charset (i18n).

**Multithreaded correctness (the slice key 25 owns — cite, do not re-teach):**
- `IS2_INCONSISTENT_SYNC`, `IS_FIELD_NOT_GUARDED`, `DC_DOUBLECHECK`, `VO_VOLATILE_INCREMENT`,
  `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`,
  `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION`, etc. (verified verbatim in key 25's dossier; this
  chapter references them as SpotBugs' concurrency category, with the deep treatment in key 25).

**Security (Malicious code vulnerability + Security categories; deepened by FindSecBugs):**
- Core SpotBugs: `DMI_CONSTANT_DB_PASSWORD`, `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`,
  `HRS_REQUEST_PARAMETER_TO_HTTP_HEADER` (HTTP response splitting), `EI_EXPOSE_REP` /
  `EI_EXPOSE_REP2` (returning/storing a mutable reference — ties to defensive copying, key 10).
- **FindSecBugs adds 144 patterns** (verbatim, `find-sec-bugs.github.io`), e.g. `SQL_INJECTION_JDBC`,
  `SQL_INJECTION_HIBERNATE`, `SQL_INJECTION_JPA`, `SQL_INJECTION_SPRING_JDBC`, `COMMAND_INJECTION`,
  `PATH_TRAVERSAL_IN` / `PATH_TRAVERSAL_OUT`, `XXE_SAXPARSER` / `XXE_DOCUMENT` / `XXE_XPATH`,
  `LDAP_INJECTION`, `XPATH_INJECTION`, `SPEL_INJECTION` / `EL_INJECTION`, `WEAK_TRUST_MANAGER`,
  `WEAK_HOSTNAME_VERIFIER`, `WEAK_MESSAGE_DIGEST_MD5` / `_SHA1`, `PREDICTABLE_RANDOM`, `DES_USAGE` /
  `TDES_USAGE`, `RSA_NO_PADDING`, `HARD_CODE_PASSWORD` / `HARD_CODE_KEY`, `UNVALIDATED_REDIRECT`
  (verified verbatim from `bugs.htm`; each pattern carries OWASP Top 10 / CWE references). The deeper
  SAST treatment / comparison with Semgrep / CodeQL is **key 70**.

### 2.4 Filtering, suppression, and the gate (the credibility levers — cross-ref key 39)

- **Filter files** (`filter.html`): XML `<FindBugsFilter>` with `<Match>` blocks. Match by
  `<Bug pattern="…"/>` / `<Bug category="…"/>` / `<Bug code="…"/>`, narrowed by `<Class name="…"/>`,
  `<Method name="…"/>`, `<Field>`, `<Package>`, `<Source>`, and combined with `<Or>`/`<And>`/`<Not>`.
  Wired via `includeFilterFile` / `excludeFilterFile`.
- **Rank threshold** (`<Rank value="…"/>`): an integer 1–20; gate on "scariest"/"scary" only to keep the
  signal high (key 39). Confidence/priority filterable via `<Confidence>`/`<Priority>`.
- **Inline suppression**: `@SuppressFBWarnings("PATTERN")` (`edu.umd.cs.findbugs.annotations.SuppressFBWarnings`,
  from `spotbugs-annotations`) for a *reviewed* false positive — narrower than disabling the pattern.
- **Effort/threshold** are the cost/recall knobs (§2.1); the **gate decision** (which findings break the
  build) is policy (keys 76/80), tuning is key 39.

### 2.5 Reference units (rule IDs / config keys / flags / GAV — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `com.github.spotbugs:spotbugs` | analyzer engine GAV | doc **4.10.2** | tool-version `⚠ verify at pin` | `maven.html` / `introduction.html` ✅ identity |
| `com.github.spotbugs:spotbugs-maven-plugin` | Maven plugin GAV | doc **4.9.8.4**; goals `spotbugs`/`check`/`gui`/`help` | tool-version `⚠ verify at pin` | `maven.html` ✅ |
| SpotBugs Gradle Plugin (`spotbugs { toolVersion }`) | Gradle plugin | tasks `spotbugsMain`/`spotbugsTest`; needs Gradle 7.0+ | tool-version `⚠ verify at pin` | `gradle.html` ✅ |
| `com.h3xstream.findsecbugs:findsecbugs-plugin` | detector plugin GAV | **1.14.0** (home/Gradle) / **1.12.0** (Maven ex.) — drift | tool-version `⚠ verify at pin` | `find-sec-bugs.github.io`, `maven.html`, `gradle.html` ✅ |
| `com.mebigfatguy.fb-contrib:fb-contrib` | detector plugin GAV | Maven Central latest **7.6.11** (README 7.0.3) | tool-version `⚠ verify at pin` | fb-contrib README + Maven Central ✅ |
| `com.github.spotbugs:spotbugs-annotations` | annotations GAV | doc **4.10.2**; `<optional>true` | tool-version `⚠ verify at pin` | `annotations.html` ✅ |
| effort levels | analysis flag | `min`/`less`/`more`/`max`; **default = `more`** | doc-stable | `effort.html` ✅ verbatim |
| bug rank | finding attribute | **1–20** (1–4 scariest, 5–9 scary, 10–14 troubling, 15–20 of concern) | doc-stable | `filter.html` ✅ verbatim |
| bug categories (×9) | finding attribute | Bad practice / Correctness / Experimental / i18n / Malicious code / Multithreaded / Performance / Security / Dodgy | doc-stable | `bugDescriptions.html` ✅ |
| filter elements | config grammar | `<Match>`,`<Bug>`,`<Class>`,`<Method>`,`<Field>`,`<Rank>`,`<Confidence>`,`<Or>`/`<And>`/`<Not>` | doc-stable | `filter.html` ✅ |
| `@SuppressFBWarnings` | annotation | `edu.umd.cs.findbugs.annotations.SuppressFBWarnings` | doc-stable | `annotations.html` ✅ |
| `EC_UNRELATED_TYPES` | Correctness pattern | "Call to equals() comparing different types" | doc-stable | `bugDescriptions.html` ✅ verbatim |
| `NP_NULL_ON_SOME_PATH` | Correctness pattern | "Possible null pointer dereference … on exception path" | doc-stable | `bugDescriptions.html` ✅ verbatim |
| `HE_EQUALS_USE_HASHCODE` | Bad-practice pattern | "Class defines equals() and uses Object.hashCode()" | doc-stable | `bugDescriptions.html` ✅ verbatim |
| `SE_NO_SERIALVERSIONID` | Bad-practice pattern | "Serializable, but doesn't define serialVersionUID" | doc-stable | `bugDescriptions.html` ✅ verbatim |
| `DLS_DEAD_LOCAL_STORE` | Dodgy pattern | "Dead store to local variable" | doc-stable | `bugDescriptions.html` ✅ verbatim |
| `EI_EXPOSE_REP` / `EI_EXPOSE_REP2` | Malicious-code pattern | exposes/stores a mutable reference (ties key 10) | doc-stable | `bugDescriptions.html` ✅ |
| `SQL_INJECTION_JDBC` (FindSecBugs) | Security pattern | SQL injection via JDBC; OWASP/CWE refs | plugin-version | `find-sec-bugs.github.io/bugs.htm` ✅ verbatim |
| `WEAK_TRUST_MANAGER` (FindSecBugs) | Security pattern | "TrustManager that accept any certificates" | plugin-version | `bugs.htm` ✅ verbatim |
| `PREDICTABLE_RANDOM` (FindSecBugs) | Security pattern | "Predictable pseudorandom number generator" | plugin-version | `bugs.htm` ✅ verbatim |

---

## 3. Evidence FOR

- **Bytecode analysis sees what source-level tools cannot.** Because SpotBugs reads the emitted `.class`
  files, it catches defects that surface only after compilation — impossible casts (`BC_IMPOSSIBLE_CAST`),
  `volatile++` non-atomicity (`VO_VOLATILE_INCREMENT`), null dereferences on exception paths
  (`NP_NULL_ON_SOME_PATH`), comparing unrelated types (`EC_UNRELATED_TYPES`) — verified from
  `bugDescriptions.html`. This is its distinctive contribution to a layered stack (the layering verdict is
  key 37's).
- **A large, categorized, ranked catalogue.** Patterns are organized into **nine categories** and a
  **1–20 rank**, so a team can gate on "scariest/scary" findings and triage the rest (verified,
  `bugDescriptions.html` / `filter.html`). This makes a credible, tunable gate possible (key 39).
- **Plugin extensibility ships real depth for free.** **FindSecBugs** adds **144 security patterns over
  826 API signatures** with OWASP Top 10 / CWE references (verbatim, `find-sec-bugs.github.io`) — turning
  SpotBugs into a SAST engine (key 70). **fb-contrib** adds a broad set of additional correctness/quality
  detectors (fb-contrib README). Both load into the same engine, so one analysis pass covers core + security
  + contrib.
- **First-class build & IDE integration.** Official **Maven** plugin (goals incl. `check` to fail the
  build) and **Gradle** plugin (per-sourceSet tasks wired into `check`); Eclipse and (via FindSecBugs)
  IntelliJ/NetBeans plugins; usable on **SonarQube** (the SpotBugs doc has a dedicated page) and CI
  (Jenkins). Fits the CI gate (keys 41/75/77). *(Exact plugin GAVs `⚠ verify at pin`.)*
- **Reviewed-suppression and filter mechanisms are first-class.** `@SuppressFBWarnings` for an inspected
  false positive, plus a rich XML filter grammar (`<Match>`/`<Bug>`/`<Rank>`/`<Class>`…), keep the gate
  credible without disabling whole patterns (verified, `annotations.html` / `filter.html`).
- **Active maintenance under the SpotBugs name.** The live doc describes **4.10.2** and supports bytecode
  from JDK 11+ — the project is current (the dead FindBugs is explicitly migrated *away from*, not cited as
  current).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — hardest objection + when-NOT-to-use)

**SpotBugs — hardest objection: heuristic patterns over bytecode → false positives + distance from source.**
- Several detectors are **heuristic** and the docs say so (e.g. the concurrency `IS2_INCONSISTENT_SYNC`
  ratio detector "there are various sources of inaccuracy in this detector" — key 25). Findings are reported
  against **bytecode**, so messages and line mappings sit one step from the source, and synthetic/compiler-
  generated code can produce confusing reports. Un-triaged findings train developers to ignore the gate
  (key 39 / key 06).
- **Limited dataflow / no source-only constructs.** SpotBugs does intra- and (at higher effort) some
  interprocedural analysis, but it is not a full taint/dataflow engine for arbitrary security flows the way
  CodeQL is (key 70); and it cannot see anything that does not survive to bytecode (formatting, comments,
  some generics — type erasure). It also will not enforce style or naming (that is Checkstyle/PMD's area —
  key 37 owns the split).

**Environment/compatibility caveats.**
- Requires a **Java 11+ runtime** to *run*; scans bytecode from **JDK 11+**, but the doc labels Java 11+
  support **"experimental"** and points to the issue tracker for known problems (`⚠`, verbatim,
  `introduction.html`) — verify behavior on the **Java 21 anchor** at pin (e.g. records, sealed classes,
  pattern-matching bytecode may surface new patterns or FPs). Newer-than-pin JDK bytecode is `⚠ verify at pin`.
- Needs **≥512 MB** memory; large projects need more (`-Xmx`). `min` effort "Skip Huge Methods" (>6,000
  bytecode length) means very large methods may be silently skipped (recall gap).
- The Gradle plugin needs **Gradle 7.0+** (v6 unsupported).

**FindSecBugs — when NOT to rely on it alone.** It is a **pattern/signature** detector (144 patterns, 826
signatures) — strong on *known* sink/source API shapes, but it is not a substitute for a full taint-tracking
SAST or for manual review of novel flows. Use it as one SAST layer; the SAST comparison (Semgrep/CodeQL/
Snyk/Sonar security) and the layered SAST strategy are **key 70**. Version drift (home page **1.14.0** vs
the Maven doc example **1.12.0**) means the coordinate must be confirmed at pin.

**fb-contrib — when NOT to reach for it.** It adds many *additional* detectors, which raises the finding
volume and can increase noise on a codebase not yet baseline-clean; introduce it after the core SpotBugs
gate is stable (key 39). Its versioning is community-driven (README pins an old 7.0.3 while Maven Central is
at 7.6.11) — always take the coordinate from Maven Central at pin.

**Shared limits of static bytecode analysis (the honest centre).**
- It catches **patterns**, not arbitrary logic bugs; passing SpotBugs is **necessary, not sufficient**
  (keys 04/47). Many famous defects (wrong business logic, missing requirements) are out of scope.
- It adds **build time** as a separate post-compile pass; an un-tuned full stack (core + FindSecBugs +
  fb-contrib at `max` effort) can make CI slow (key 79). Tune effort + rank threshold for the gate.

**Competing approach *inside* Java code quality — neutral framing.** SpotBugs (bytecode, after compile),
Checkstyle (source, style — key 27), PMD (source AST — key 28), and Error Prone (inside `javac` — key 30)
take **different approaches** to static analysis and see different things; teams commonly run more than one.
SonarQube (key 35) can host/aggregate analyzers including SpotBugs output. Each choice states its trade-off;
**no tool is crowned here — the cross-cutting "which/how to layer" verdict is key 37, the SAST comparison
key 70.** Every cross-tool claim cites the named tool's own pinned source.

---

## 5. Current status

- **Active and current at the anchor (Java 21).** The live doc describes **SpotBugs 4.10.2**; Maven plugin
  **4.9.8.4**; annotations **4.10.2**; bytecode from JDK 11+ supported (Java 11+ "experimental"). FindBugs
  is dead and migrated away from — cite SpotBugs only. *(Exact latest-stable versions are `TO-PIN` in
  `SOURCE-PIN.md §2`; the numbers above are the live line, `⚠ verify at pin`.)*
- **Plugin ecosystem moving steadily.** FindSecBugs **1.14.0** (release notes dated April 20, 2025, per the
  home page); fb-contrib **7.6.11** (Maven Central, June 2025). Both track the SpotBugs engine.
- **Java-25 delta.** SpotBugs reads bytecode; new language features at 22–25 (e.g. record patterns,
  unnamed variables/patterns, the evolving switch) compile to bytecode SpotBugs must handle — confirm no
  new false positives on **Java 25** output at pin (the doc's "experimental" caveat for newer JDKs is the
  watch item; `⚠ verify at pin`, not asserted). No language *preview* feature is required by this chapter.
- **Stability label:** SpotBugs core is **stable/GA** (the analyzer); per-JDK bytecode support beyond 11 is
  the project's own "experimental" flag — carry that label verbatim, do not upgrade it to "fully supported."
- **Deprecations:** within `spotbugs-annotations`, several legacy annotations are marked **(Deprecated)** in
  the doc (e.g. `DesireWarning`, `ExpectWarning`, `NoWarning`, `PossiblyNull`, the old `SuppressWarnings`,
  and a deprecated `CleanupObligation`/`CreatesObligation`/`DischargesObligation` pair) — prefer the current
  set (`@SuppressFBWarnings`, `@CheckForNull`, `@CheckReturnValue`, `@NonNull`, `@Nullable`).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** add row `29_spotbugs` to `DEMO-CATALOG.md` *(row not yet present — see §7 flag)*.
  - **Demo name:** "Bugs the bytecode sees — SpotBugs + FindSecBugs catching what compiles cleanly."
  - **Java Quality surface exercised:** in the shared `org.acme.storefront` domain, a deliberately-buggy
    service class that **compiles without warnings** yet trips SpotBugs: (1) an `equals()` comparing
    unrelated types (`EC_UNRELATED_TYPES`); (2) a `Serializable` value class missing `serialVersionUID`
    (`SE_NO_SERIALVERSIONID`); (3) an `EI_EXPOSE_REP2` (storing a caller's mutable `Date`/array — ties
    key 10 defensive copying); plus a **FindSecBugs** finding — a JDBC query built by string concatenation
    (`SQL_INJECTION_JDBC`) and a `WEAK_MESSAGE_DIGEST_MD5` password hash. A second, fixed class passes all
    three plugins (core + FindSecBugs + fb-contrib).
  - **TRY-IT exercise:** run `./mvnw -B verify`; observe `spotbugs:check` fail with the pattern codes.
    Then raise the `<Rank>` threshold in the filter file and watch a low-rank finding drop out (showing the
    triage knob, key 39); add a *reviewed* `@SuppressFBWarnings("EI_EXPOSE_REP2")` to one site and confirm
    only that instance is suppressed. Finally fix the `SQL_INJECTION_JDBC` with a `PreparedStatement` and
    the MD5 with a strong hash, and watch the gate go green. This makes "the bug that compiled cleanly,
    caught at build time" tactile, and shows the §4 limits (suppression scope; rank tuning).
- **Module key / path:** `08-companion-code/29_spotbugs/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; check under 25) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.github.spotbugs:spotbugs-maven-plugin` (goal `check`) | the gate (primary unit under study) | `spotbugs.readthedocs.io/maven.html` (4.9.8.4 `⚠ verify at pin`) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs` (engine override) | analyzer engine version pin | `maven.html` (4.10.2 `⚠`) | ☐ verify at pin |
  | `com.h3xstream.findsecbugs:findsecbugs-plugin` | security detectors (SQL_INJECTION_JDBC, WEAK_MESSAGE_DIGEST_MD5) | `find-sec-bugs.github.io` (1.14.0 `⚠`) | ☐ verify at pin |
  | `com.mebigfatguy.fb-contrib:fb-contrib` | extra correctness/quality detectors | Maven Central (7.6.11 `⚠`) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs-annotations` | `@SuppressFBWarnings` / `@CheckForNull` | `annotations.html` (4.10.2 `⚠`) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the fixed class behaves) | `SOURCE-PIN.md §3` (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md §3` (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags.
  - **Externalized config / profiles** — `spotbugs-maven-plugin` config in the POM (effort, threshold);
    an `spotbugs-exclude.xml` filter (a reviewed `<Match><Bug pattern="EI_EXPOSE_REP2"/></Match>` and a
    `<Rank value="…"/>` threshold) and a `spotbugs-security-include.xml` for FindSecBugs (trace each pattern
    to its tool doc).
  - **At least one test** — asserts the **fixed** service is correct (e.g. the defensively-copied field is
    independent of the caller's mutable input); names the behavior it asserts.
  - **Observability / health surface** — the SpotBugs SARIF/XML report as the build's quality artifact (the
    surface a dashboard/CI consumes — key 88/106); name the report path.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **broken** class is the failure path — the
    `spotbugs:check` goal **fails the build** on `EC_UNRELATED_TYPES` / `SQL_INJECTION_JDBC` /
    `WEAK_MESSAGE_DIGEST_MD5`, turning a defect that compiled cleanly into a deterministic build failure.
    State the limit in the chapter: a `@SuppressFBWarnings` removes the check at that site (the §4 honest
    edge), and bytecode-distance can mislabel a synthetic-code finding.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `pom-spotbugs` | the `spotbugs-maven-plugin` + FindSecBugs + fb-contrib config (effort + rank threshold) | `pom.xml` |
  | `exclude-filter` | the `<FindBugsFilter>` with a reviewed `<Match>` + `<Rank>` | `spotbugs-exclude.xml` |
  | `bad-equals` | the `EC_UNRELATED_TYPES` / `SE_NO_SERIALVERSIONID` / `EI_EXPOSE_REP2` traps (failure path) | `BrokenOrderService.java` |
  | `sql-injection` | the `SQL_INJECTION_JDBC` + `WEAK_MESSAGE_DIGEST_MD5` FindSecBugs traps | `BrokenOrderService.java` |
  | `fixed-service` | the corrected service (PreparedStatement, defensive copy, serialVersionUID) | `OrderService.java` |
  | `suppress-fb` | a narrow, justified `@SuppressFBWarnings` on one reviewed site | `OrderService.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/29_spotbugs spotbugs:check` (report: `spotbugs:spotbugs`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with bugs present — `spotbugs:check` reports `EC_UNRELATED_TYPES`,
  `SE_NO_SERIALVERSIONID`, `EI_EXPOSE_REP2`, `SQL_INJECTION_JDBC`, `WEAK_MESSAGE_DIGEST_MD5` and the build
  **fails**; after fixes + one reviewed suppression — green build, test pass count green, a clean SpotBugs
  report artifact.
- **Figure plan** (GUIDELINES §8; **standard deep single-tool chapter** → image budget ~**1–2 designed
  diagrams + 1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard deep single-tool (analyzer) chapter with a comparison-aware framing.
  - **Candidate designed diagram(s) + family:**
    - **Fig 29.1 — "From source to finding: the SpotBugs bytecode pipeline":** `.java` → `javac` →
      `.class` bytecode → SpotBugs engine (+ FindSecBugs / fb-contrib detector plugins) → bug instances
      tagged *category × rank × confidence* → filter/threshold → gate (`check`). Family = *build-pipeline /
      data-flow diagram*. Trace each stage to `introduction.html` (bytecode), `maven.html`/`gradle.html`
      (post-compile step + plugin loading), `bugDescriptions.html` (categories), `filter.html` (rank/filter).
    - **Fig 29.2 — "One engine, three detector sets":** SpotBugs core + FindSecBugs (144 security patterns)
      + fb-contrib (extra correctness/quality) drawn as plugins into a single analysis pass, each labelled
      with example pattern codes. Family = *plugin/composition diagram*. Trace to `find-sec-bugs.github.io`
      (144 patterns / 826 signatures), fb-contrib README, `bugDescriptions.html`.
  - **Candidate captured surface(s):** **Fig 29.3** — a build-log / IDE capture of `spotbugs:check` failing
    `./mvnw verify` with a real pattern code (e.g. `SQL_INJECTION_JDBC`), from the companion module (capture
    only real tool output; technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every pattern code → `bugDescriptions.html` (core) or
    `bugs.htm` (FindSecBugs) or fb-contrib README; pipeline stages → SpotBugs Maven/Gradle docs; rank/filter
    labels → `filter.html`; contract patterns (`HE_*`, `SE_*`, `EI_*`) cross-ref keys 15/10.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool versions / GAV coordinates** — `spotbugs`, `spotbugs-maven-plugin`, SpotBugs Gradle Plugin,
  `findsecbugs-plugin`, `fb-contrib`, `spotbugs-annotations`: all `TO-PIN` in `SOURCE-PIN.md §2`. Live-line
  observed: SpotBugs **4.10.2**, maven-plugin **4.9.8.4**, FindSecBugs **1.14.0** (Maven example **1.12.0** —
  drift), fb-contrib **7.6.11**. Confirm exact latest-stable + coordinates at pin before stating a version.
  Pattern/category/filter **identity** is verified; **versions** are not.
- ⚠ **FindSecBugs pattern count** — "144 bug patterns over 826 unique API signatures" verified verbatim from
  `find-sec-bugs.github.io` at the live line; re-confirm byte-identical at the pinned plugin version (count
  moves between releases — the Maven doc example still says "138 different vulnerability types").
- ⚠ **Default rank / effort membership per pattern** — which patterns fire at default effort (`more`) and
  each pattern's *rank/priority* are version-sensitive → `⚠ verify at pin`. Pattern codes + short
  descriptions verified verbatim from `bugDescriptions.html`.
- ⚠ **Java 21 / 25 bytecode support** — the doc labels Java 11+ support "experimental" and warns about
  newer JDK bytecode; confirm no new false positives on **Java 21** (anchor) and **Java 25** output at pin.
  Do not assert "fully supports Java 21" — carry the project's own "experimental" label.
- ⚠ **Verbatim short descriptions** — `EC_UNRELATED_TYPES`, `NP_NULL_ON_SOME_PATH`, `HE_EQUALS_USE_HASHCODE`,
  `SE_NO_SERIALVERSIONID`, `DLS_DEAD_LOCAL_STORE`, FindSecBugs `WEAK_TRUST_MANAGER` / `PREDICTABLE_RANDOM` /
  `SQL_INJECTION_*` — verified verbatim at the live line; quote, don't paraphrase, and re-confirm at the
  pinned version.
- ⚠ **`@SuppressFBWarnings` package** — `edu.umd.cs.findbugs.annotations.SuppressFBWarnings` (FindBugs
  lineage package retained) verified from `annotations.html`; never write a generic `@SuppressWarnings` for
  SpotBugs suppression (the doc's `SuppressWarnings` is deprecated).
- **Open question (draft / merge cluster 27/28/29/30+36):** boundary with key 27 (Checkstyle/style),
  key 28 (PMD/CPD/source AST), key 30 (Error Prone/`javac`), key 37 (the cross-cutting comparison + layered
  stack — owns "which/how to combine"), key 70 (SAST comparison — owns the FindSecBugs-vs-Semgrep/CodeQL
  verdict), key 38 (custom detectors), key 25 (the concurrency MT_CORRECTNESS slice), keys 15/10 (the
  contract/defensive-copy patterns `HE_*`/`SE_*`/`EI_*`), key 39 (suppression/baseline tuning). Propose:
  **this** chapter owns *SpotBugs the bytecode analyzer + its plugin ecosystem*; route every "vs another
  analyzer" verdict to 37, every "vs another SAST" verdict to 70.
- **DEMO-CATALOG.md row** for `29_spotbugs` not yet present — add it (flag to catalog owner; Part-IV
  analyzer keys 27–30 likely all need rows in the `org.acme.storefront` domain).

### Filed to `09-flags/`
- `09-flags/29_spotbugs_versions_and_defaults_unverified.md` — SpotBugs / spotbugs-maven-plugin / SpotBugs
  Gradle Plugin / FindSecBugs / fb-contrib / spotbugs-annotations rows are all `TO-PIN`; pattern/category/
  filter identity verified from docs, but exact versions, GAV coordinates, FindSecBugs pattern count,
  default rank/effort membership, and Java-21/25 bytecode-support status are `⚠ verify at pin`. Records the
  FindSecBugs version drift (1.14.0 home vs 1.12.0 Maven example) and fb-contrib README/Maven-Central drift.

---

## 8. Sources & further reading

### Primary / Official (verified by direct fetch @ the live line; versions `⚠ verify at pin`)
| # | Source | Title | URL / path | Verified |
|---|---|---|---|---|
| 1 | Tool doc | SpotBugs — Introduction (v4.10.2; "find bugs … instances of bug patterns"; Java 11+ runtime/bytecode, experimental; FindBugs lineage) | spotbugs.readthedocs.io/en/latest/introduction.html | ☑ verbatim |
| 2 | Tool doc | SpotBugs — Using the Maven Plugin (`spotbugs-maven-plugin` 4.9.8.4; goals spotbugs/check/gui/help; engine override; FindSecBugs integration) | spotbugs.readthedocs.io/en/latest/maven.html | ☑ verbatim coords |
| 3 | Tool doc | SpotBugs — Using the Gradle Plugin (spotbugsMain/spotbugsTest; toolVersion; Gradle 7.0+; spotbugsPlugins) | spotbugs.readthedocs.io/en/latest/gradle.html | ☑ verbatim |
| 4 | Tool doc | SpotBugs — Filter file (`<Match>`/`<Bug>`/`<Rank>`/`<Confidence>`; rank 1–20 scariest→of concern) | spotbugs.readthedocs.io/en/latest/filter.html | ☑ verbatim |
| 5 | Tool doc | SpotBugs — Effort (min/less/more/max; default = more; skip huge methods >6000) | spotbugs.readthedocs.io/en/latest/effort.html | ☑ verbatim |
| 6 | Tool doc | SpotBugs — Annotations (`spotbugs-annotations`; `@SuppressFBWarnings`/`@CheckForNull`/`@CheckReturnValue`; deprecations) | spotbugs.readthedocs.io/en/latest/annotations.html | ☑ verbatim |
| 7 | Tool doc | SpotBugs — Bug descriptions (9 categories; EC_/NP_/HE_/SE_/DLS_/RCN_/EI_ codes + verbatim short text) | spotbugs.readthedocs.io/en/latest/bugDescriptions.html | ☑ codes + text verbatim |
| 8 | Plugin doc | Find Security Bugs — home (144 patterns / 826 API signatures; OWASP/CWE; v1.14.0, Apr 20 2025) | find-sec-bugs.github.io | ☑ verbatim |
| 9 | Plugin doc | Find Security Bugs — Bug Patterns (SQL_INJECTION_*, COMMAND_INJECTION, PATH_TRAVERSAL_*, XXE_*, WEAK_TRUST_MANAGER, PREDICTABLE_RANDOM, …) | find-sec-bugs.github.io/bugs.htm | ☑ codes verbatim |
| 10 | Plugin repo | fb-contrib — README (`com.mebigfatguy.fb-contrib:fb-contrib`; SpotBugs bytecode plugin) + Maven Central latest 7.6.11 | github.com/mebigfatguy/fb-contrib + search.maven.org | ☑ GAV + version |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Tool doc | SpotBugs — Guide for migration from FindBugs 3.0 to SpotBugs 3.1 (FindBugs is dead) | spotbugs.readthedocs.io/en/latest | ☐ verify at pin |
| 2 | Tool doc | SpotBugs — Use SpotBugs Plugin on SonarQube (cross-ref key 35) | spotbugs.readthedocs.io/en/latest | ☐ verify at pin |
| 3 | Cross-ref | key 25 dossier — SpotBugs MT_CORRECTNESS concurrency patterns (verbatim) | 02-research/25_static_concurrency_detection | ☑ |

> Source order applied: each tool's own pinned doc/repo → official release notes → cross-ref dossiers. No
> content farms. Every cross-tool claim cites the named tool's own pinned source (NEUTRALITY §"cited-source
> requirement"); the analyzer comparison verdict routes to key 37, the SAST verdict to key 70.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl + parse SpotBugs introduction/running | spotbugs.readthedocs.io | v4.10.2; "instances of bug patterns"; Java 11+ runtime + bytecode (experimental); `-textui`/`-gui` CLI; FindBugs lineage |
| 2 | curl + parse SpotBugs maven.html | spotbugs.readthedocs.io | `com.github.spotbugs:spotbugs-maven-plugin:4.9.8.4`; goals spotbugs/check/gui/help; engine override 4.10.2; FindSecBugs `1.12.0` nested-plugin example |
| 3 | curl + parse SpotBugs gradle.html | spotbugs.readthedocs.io | spotbugsMain/spotbugsTest per sourceSet; `toolVersion='4.10.2'`; Gradle 7.0+; `spotbugsPlugins 'com.h3xstream.findsecbugs:findsecbugs-plugin:1.14.0'` |
| 4 | curl + parse SpotBugs effort.html | spotbugs.readthedocs.io | min/less/more/max; "default effort … same with more"; interprocedural at more/max; skip huge methods >6000 |
| 5 | curl + parse SpotBugs filter.html | spotbugs.readthedocs.io | `<Match>`/`<Bug pattern/code/category>`/`<Class>`/`<Method>`/`<Rank>`/`<Confidence>`/`<Or>`/`<And>`/`<Not>`; rank 1–20 (1–4 scariest … 15–20 of concern) verbatim |
| 6 | curl + parse SpotBugs annotations.html | spotbugs.readthedocs.io | `spotbugs-annotations:4.10.2`; `@SuppressFBWarnings`/`@CheckForNull`/`@CheckReturnValue`/`@NonNull`/`@Nullable`/`@DefaultAnnotation`; several legacy annotations Deprecated |
| 7 | curl + parse SpotBugs bugDescriptions.html (~595 KB) | spotbugs.readthedocs.io | 9 categories; verbatim codes+text: EC_UNRELATED_TYPES, NP_NULL_ON_SOME_PATH, HE_EQUALS_USE_HASHCODE, SE_NO_SERIALVERSIONID, DLS_DEAD_LOCAL_STORE, RCN_*, BC_IMPOSSIBLE_CAST, EI_EXPOSE_REP |
| 8 | curl + parse FindSecBugs home + bugs.htm | find-sec-bugs.github.io | v1.14.0 (Apr 20 2025); 144 patterns / 826 API signatures; OWASP Top 10 + CWE; 149 distinct pattern codes incl. SQL_INJECTION_*, COMMAND_INJECTION, PATH_TRAVERSAL_*, XXE_*, WEAK_TRUST_MANAGER, PREDICTABLE_RANDOM |
| 9 | fb-contrib README + Maven Central GAV search | github.com/mebigfatguy/fb-contrib + search.maven.org | GAV `com.mebigfatguy.fb-contrib:fb-contrib`; "SpotBugs plugin … on java byte code"; README pins 7.0.3, Maven Central latest 7.6.11 (Jun 2025) — drift noted |
| 10 | cross-ref read | 02-research/25 dossier | MT_CORRECTNESS pattern codes verbatim (concurrency slice owned by key 25) |

---
## Learnings & pipeline suggestions
- **Reusable shape reconfirmed — "approximation-of-a-spec-property" (key 25) generalizes to a whole-tool
  chapter.** SpotBugs organizes cleanly as: (1) what it *reads* (bytecode, the distinctive axis); (2) the
  spec/contract each pattern approximates (equals/hashCode JLS, serialization, JMM §17.4.5, defensive
  copying); (3) the proxy = a bytecode pattern → its strongest case AND its FP/distance-from-source limit.
  Makes NEUTRALITY structural and the HONEST-LIMITATIONS floor fall out. Reuse for keys 27/28/30.
- **"One engine, N detector sets" framing keeps NEUTRALITY clean.** FindSecBugs and fb-contrib are
  *plugins into SpotBugs*, so they are SpotBugs **capabilities**, not rivals — treat them as the subject
  (Bucket i), and route any "FindSecBugs vs Semgrep/CodeQL" verdict to key 70 and any "SpotBugs vs
  Checkstyle/PMD/Error Prone" verdict to key 37. This is the right split for every Part-IV analyzer chapter.
- **Version-drift trap (new instance).** The *same* tool advertises different versions on different pages:
  FindSecBugs home/Gradle doc say **1.14.0** but the SpotBugs Maven doc example still says **1.12.0** /
  "138 vulnerability types"; fb-contrib README says **7.0.3** but Maven Central is **7.6.11**. Always take a
  plugin GAV from **Maven Central** at pin, never from a tutorial/example snippet. Extends the key-19
  quote-drift / key-09 "cite ID, defer version" rules to *cross-page version drift within one tool's docs*.
- **Tooling:** `spotbugs.readthedocs.io/bugDescriptions.html` is one ~595 KB page — WebFetch truncates;
  `curl` + a tiny Python tag-strip + targeted `find()` per code parses it reliably (reconfirms the key-25
  finding). Maven Central's `search.maven.org/solrsearch` JSON API gives the authoritative latest plugin
  version in one call — propose adding to the fetch helpers.
- **Folklore guard reinforced:** FindBugs is **dead** → SpotBugs; `findbugs-maven-plugin` →
  `spotbugs-maven-plugin`; the SpotBugs `edu.umd.cs.findbugs.*` package names are *retained lineage*, not
  evidence FindBugs is current. Never cite FindBugs as a live tool (CANDIDATE_POOL stale-name note).
- **Cross-ref:** keys 05 (toolchain map), 25 (concurrency MT patterns), 27 (Checkstyle), 28 (PMD/CPD),
  30 (Error Prone), 37 (analyzer comparison + layered stack — owns the verdict), 38 (custom detectors),
  39 (suppression/baseline), 70 (SAST comparison — owns FindSecBugs verdict), 15 (equals/hashCode contracts
  `HE_*`/`SE_*`), 10 (defensive copying `EI_EXPOSE_REP*`), 35 (Sonar hosting SpotBugs), 75/77 (CI gate).
  Record in merge notes.
