# GATE REPORT — EXAMPLE-BUILD — key 26 (How static analysis works)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 26 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`)
- **Slug:** `26_how_static_analysis_works`
- **Draft under review:** `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`
- **Module built:** `08-companion-code/26_how_static_analysis_works/` (artifactId `how-static-analysis-works`)
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×6), `check_snippets.sh`; build via Maven `verify` (`-Pquality`)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand with the pinned toolchain)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module builds green deterministically (8/8 clean `-Pquality verify` runs) at the pinned toolchain
(JDK 21.0.11), warning-clean, 0 Checkstyle, 0 unsuppressed SpotBugs. All six declared snippet tags
resolve to bounded (≤9-line) tag regions inside the compiled files and every prose include marker passes
`check_snippets.sh`. Every fact in the module traces to the dossier + SOURCE-PIN; no atom is invented.
Both FLOOR-C preconditions hold and are logged below.

---

## FLOOR C guard — preconditions (both required for PASS)

**(a) Runtime/toolchain version meets minimum (Java 21+):**
```
$ java -version
openjdk version "21.0.11" 2026-04-21
OpenJDK Runtime Environment Homebrew (build 21.0.11)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.11, mixed mode, sharing)
```
Maven: `Apache Maven 3.9.16`, runtime Java `21.0.11`. Anchor LTS per SOURCE-PIN.md = Java 21. **MET.**

**(b) Build GREEN.** Exact command (standalone, parent pom untouched):
```
mvn -B -Pquality -f 08-companion-code/26_how_static_analysis_works/pom.xml clean verify
```
Result lines:
```
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] You have 0 Checkstyle violations.
[INFO] BugInstance size is 0
[INFO] No errors/warnings found
[INFO] BUILD SUCCESS
```
Deterministic across 8 clean runs. No `[WARNING]` lines (compile inherits `-Xlint:all,-processing` from
the aggregator). **MET.**

> Toolchain export used before the build (human-only blocker pre-satisfied):
> `export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"; export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"`

---

## Enterprise-grade checklist

| Requirement | Status | Where |
|---|---|---|
| Child of the ONE aggregator (`<parent>`, no own `<groupId>`/`<version>`, no own BOM) | ✅ | `pom.xml` — `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT` |
| Self-contained like reference module 09 (own `config/` dir + own `quality` profile) | ✅ | `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml`, `<profile>quality</profile>` in `pom.xml` |
| Pinned dependency set via inherited parent properties; one extra pinned GAV only | ✅ | JUnit/AssertJ from aggregator BOM; `spotbugs-annotations:4.9.3` `provided` (single GAV, mirrors key-09 JSpecify) |
| Externalized config (not hard-coded) | ✅ | analyzer rules in `config/`; tunable thresholds (`effort=Max`, `threshold=Medium`) in the profile; sample data in `src/main/resources/catalog-line.txt` |
| At least one integration test exercising the chapter's mechanism | ✅ | `StaticAnalysisDemoTest` (8 tests) — proves each technique's target behaves as the prose claims |
| Test-harness setup correct (no spurious logs / fails) | ✅ | JUnit Jupiter via `junit-jupiter` + surefire 3.5.6 from the aggregator; no extra system properties needed (matches reference module 09); run reports `Tests run: 8` cleanly |
| Observability/health surface where the topic touches it | ✅ (light) | technique-demo module: the running-count/readiness seams belong to the service modules (09/19); here the observable surface is the analyzer findings themselves + the deterministic test assertions. NOTE below. |
| Explicit failure path | ✅ | taint sink degrades to empty result for unknown category (defined error response); `ResourceLeakDemo` is the named data-flow failure, fixed by `ResourceReader` try-with-resources; both exercised by the test |
| Register in parent `<modules>` only after green + CODE-REVIEW | ✅ (correctly NOT yet) | parent `08-companion-code/pom.xml` left unedited; module not in `<modules>` — awaits CODE-REVIEW (Step 4b code-reviewer) |

---

## Snippet tags (tag-include regions) — resolved line counts

All six declared tags resolve; each ≤9 lines (verified by `extract_snippet.sh`, which errors on >9):

| Tag | Backing file | Lines | Prose insertion point |
|---|---|---|---|
| `ast-smell` | `…/AstSmellDemo.java` | 8 | Move 1 — after the AST/pattern-matching paragraph ("an empty `catch`") |
| `type-misuse` | `…/TypeMisuseDemo.java` | 5 | Move 2 — after the symbols-&-types paragraph |
| `dataflow-leak` | `…/ResourceLeakDemo.java` | 8 | Move 3 — after the intraprocedural/interprocedural CONCEPT block |
| `taint-flow` | `…/TaintFlowDemo.java` | 6 | Move 4 — after the Semgrep taint-bounds paragraph |
| `taint-fixed` | `…/TaintFlowDemo.java` | 6 | Move 4 — directly after `taint-flow` (the sanitized counterpart) |
| `justified-suppression` | `…/SuppressionDemo.java` | 7 | "Living with imperfection — the controls" — after the controls bullet list |

`check_snippets.sh 03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`:
```
PASS  …/AstSmellDemo.java#ast-smell
PASS  …/TypeMisuseDemo.java#type-misuse
PASS  …/ResourceLeakDemo.java#dataflow-leak
PASS  …/TaintFlowDemo.java#taint-flow
PASS  …/TaintFlowDemo.java#taint-fixed
PASS  …/SuppressionDemo.java#justified-suppression
----
check_snippets: 6 marker(s); 6 pass, 0 fail.
```

---

## The module's gate behavior (the chapter's thesis, in code)

The module dogfoods the chapter: every analyzer-target is held to the same `quality` gate it describes
and the build is green. Two deliberate targets fire at the gate and carry **narrow, load-bearing,
reasoned suppressions** (mirroring the key-19 `OrderLeaky` discipline — suppress with a reason, never
disable a detector):

- `TypeMisuseDemo.catalogueHas` → `GC_UNRELATED_TYPES` (SpotBugs reaches by type resolution the same
  conclusion Error Prone draws as `CollectionIncompatibleType`) — Move 2.
- `ResourceLeakDemo.readFirstLine` → `OS_OPEN_STREAM` ("may fail to close stream") — Move 3.

The other two targets sit below the gate's chosen point and need no suppression — the chapter's
false-negative half shown in code: the empty `catch` carries a comment the house `EmptyBlock option=text`
tolerates; the taint flow runs into an in-module sink base SpotBugs does not model as a JDBC injection.
The `@SuppressFBWarnings(value, justification)` snippet (`SuppressionDemo.snapshot`) suppresses a
separate, **load-bearing** `EI_EXPOSE_REP` (confirmed to fire when the annotation is removed). Each
suppression was confirmed load-bearing before being kept.

---

## Captured screenshots (Step 4c)

**No captures planned.** The chapter's figure plan is a single designed conceptual diagram
(`05-figures/26_how_static_analysis_works/fig26_1.png`, the technique ladder) — authored as HTML and
rendered to PNG separately, with its `.sources.md` sidecar already present; it is NOT this agent's job
and NOT image-generated. This is a technique/concept chapter whose module is a JDK-only analyzer-target
demo with no subject-native UI surface (no dev console, health view, or API explorer) to capture. Per
the figure budget, zero captured screenshots is correct here.

---

## Source-trace (Floor C — every atom traces to dossier + SOURCE-PIN)

| Atom in module | Traces to |
|---|---|
| PMD `EmptyCatchBlock`, Checkstyle `EmptyBlock` (AST/pattern, Move 1) | dossier §2.1; SOURCE-PIN Checkstyle/PMD rows |
| Error Prone `CollectionIncompatibleType` (type-aware, Move 2) | dossier §2.2 (Error Prone "augment the compiler's type analysis") |
| SpotBugs `GC_UNRELATED_TYPES`, `OS_OPEN_STREAM` (bytecode data-flow, Move 3) | dossier §2.3 (SpotBugs bytecode data-flow); observed firing at pinned engine 4.9.3 |
| Taint source→sink→sanitizer→flow-step model (Move 4) | dossier §2.4 (CodeQL taint definition; Semgrep bounds) |
| `@SuppressFBWarnings(value, justification)`; filter-file `Match` | dossier §"controls" (SpotBugs `@SuppressFBWarnings` + filter file) |
| GAVs: `com.github.spotbugs:spotbugs-annotations:4.9.3`; plugin `4.9.3.0`; Checkstyle engine `10.26.1`; plugin `3.6.0` | reference module 09/19 (established pinned working set); cached in local `~/.m2` |
| JUnit/AssertJ | aggregator `pom.xml` (JUnit 6.0.3 BOM, AssertJ 3.27.7) per SOURCE-PIN §3 |

No invented rule IDs, config keys, tool flags, API signatures, GAVs, versions, benchmark figures, or
quoted claims. Nothing presented ahead of the pin.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book, following the
house pattern established by the (original) key-09 module. No file is a copied-or-renamed upstream tool
sample; no quickstart/getting-started skeleton, no `NOTICE`/license-header boilerplate, and no large
contiguous block was copied from any tool's docs or samples. Scan for upstream fingerprints
(`copyright`/`licensed under`/`@author`/`quickstart`/`all rights reserved`) returned none. No region is
taken substantially verbatim from a specific source file, so no per-region attribution is required.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | SpotBugs 4.9.3 reports a different finding *subset* across otherwise-identical runs (`GC_UNRELATED_TYPES` vs `OS_OPEN_STREAM`); both deliberate targets suppressed so the real configured `clean verify` is deterministically green (8/8). | NOTE | `config/spotbugs/spotbugs-exclude.xml` (two `Match` entries) | None — both suppressions verified load-bearing; logged to PIPELINE-LEARNINGS as a SpotBugs-determinism trap. |
| 2 | Observability surface is lighter than a service module (the demos are static analyzer-targets); the observable signal here is the analyzer findings + deterministic test assertions, not a metric/health endpoint. | NOTE | module shape | None — appropriate for a technique-demo chapter; the richer health/metric seams live in the service modules (09/19). |
| 3 | Module not yet in the parent `<modules>` list (by design). | NOTE | `08-companion-code/pom.xml` (unedited) | Register after the CODE-REVIEW gate (Step 4b) passes — per agent contract. |

---

## Blockers

**None.**

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `-Pquality verify` at the pinned toolchain; every
  displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file; FLOOR-C
  source-trace clean; both FLOOR-C preconditions logged.
- [ ] **CODE-REVIEW** — pending (Step 4b `code-reviewer` agent). The module must clear CODE-REVIEW before
  it is registered in the parent `<modules>` list.

---

## Learnings & pipeline suggestions

Appended to `00-strategy/PIPELINE-LEARNINGS.md` (2026-06-26 entry). Key items:
- New reusable module shape: **"analyzer-target per technique"** for how-analysis-works / technique-survey
  chapters — one tiny runnable shape per ladder rung beside its fix, keeping every snippet ≤9 lines.
- Three legitimate green-keeping mechanisms for deliberate smells, in priority order: write below the
  gate's chosen point → narrow load-bearing reasoned suppression (key-19 discipline) → per-site
  `@SuppressFBWarnings`.
- **SpotBugs run-to-run finding-subset non-determinism** is a real trap: verify each deliberate finding
  fires unsuppressed at least once, suppress all genuinely-observed ones, and trust only the no-override
  `clean verify` (run it several times). `-Dspotbugs.*` overrides do not cleanly replace the plugin
  `<configuration>` and must be treated as throwaway probes.
- `spotbugs-annotations` is the second pinned-GAV `provided` pattern (sibling of key-09 JSpecify).

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 26 gate-run PASS
```
