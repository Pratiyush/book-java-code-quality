# GATE REPORT — EXAMPLE-BUILD (Step 4b)

## Header

- **Gate:** EXAMPLE-BUILD
- **Chapter key:** 20 (owner; folds 21 + 23) — Part III Ch 13
- **Slug:** `20_thread_safety_jmm`
- **Draft under review:** `03-drafts/20_thread_safety_jmm/20_thread_safety_jmm_v1.md`
- **Module path:** `08-companion-code/20_thread_safety_jmm/` (artifactId `thread-safety-jmm`)
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh`, `check_snippets.sh`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand)
- **Verdict:** **PASS** (Floor-C: **PASS**)

---

## Verdict rationale

The module is a self-contained child of the one `08-companion-code` aggregator, mirrors the reference
module `09_api_method_contracts/` (own `config/` dir, own `quality` profile), and builds **green** under
`mvn -B -Pquality verify` on Java 21.0.11 — warning-clean, 0 Checkstyle violations, 0 SpotBugs findings,
9 tests passing. All five declared snippet tags resolve to real tag regions ≤9 lines and are bound into
the draft prose. Every fact traces to the dossier + SOURCE-PIN; the one detail that needed verification
(the exact SpotBugs pattern for `volatile++`) was resolved against the build, not invented.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime meets minimum:** `openjdk version "21.0.11" 2026-04-21` (≥ Java 21 LTS anchor). PASS.
- **(b) Build green:** `mvn -B -Pquality -f 08-companion-code/20_thread_safety_jmm/pom.xml clean verify`
  → `BUILD SUCCESS`. PASS.

Exact result lines (from the clean run):

```
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
You have 0 Checkstyle violations.
BugInstance size is 0
BUILD SUCCESS
```

Toolchain: `JAVA_HOME=/opt/homebrew/opt/openjdk@21/...`, Maven 3.9.16 (matches SOURCE-PIN §4 pin).

---

## Module shape (mirrors reference module 09)

- **Child of the ONE aggregator:** `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`;
  no `<groupId>`/`<version>` literal of its own; no BOM/version-set of its own.
- **NOT registered yet:** `08-companion-code/pom.xml` `<modules>` does **not** list `20_thread_safety_jmm`
  (confirmed: parent pom untouched, `git status` clean for it). Registration is deferred until after
  green build **and** the CODE-REVIEW gate, per the build-discipline rule.
- **Self-contained config:** `config/checkstyle/checkstyle.xml` (copied verbatim from module 09 house
  ruleset) + `config/spotbugs/spotbugs-exclude.xml` (one reviewed suppression); own `quality` profile in
  the pom (checkstyle-plugin 3.6.0 + checkstyle engine 10.26.1; spotbugs-maven-plugin 4.9.3.0), same
  shape as module 09.

### Files created

```
08-companion-code/20_thread_safety_jmm/
  pom.xml
  README.md
  config/checkstyle/checkstyle.xml              (copied from module 09)
  config/spotbugs/spotbugs-exclude.xml          (1 reviewed suppression)
  src/main/resources/thread-safety.properties   (dev/prod profiles)
  src/main/java/org/acme/concurrency/
    package-info.java
    RacyCounter.java            (deliberate counter-example — tag: racy-counter)
    AtomicCounter.java          (lock-free fix)
    SynchronizedCounter.java    (monitor edge + @GuardedBy — tag: guarded-by)
    ServiceConfiguration.java   (final-field publication — tag: final-publication)
    LazyResource.java           (init-on-demand holder — tag: lazy-holder)
    WorkCoordinator.java        (executor + observability + failure path)
    WorkCoordinatorConfig.java  (externalized-config loader)
  src/test/java/org/acme/concurrency/
    ThreadSafetyContractTest.java  (9 tests; tag: jcstress-test)
```

---

## Pinned dependency set (one inherited parent property + one pinned annotation GAV)

- Runtime/test pins inherited from the aggregator: `maven.compiler.release=21`, JUnit BOM `6.0.3`,
  AssertJ `3.27.7` (SOURCE-PIN §3). No version literals added for these.
- **One pinned annotation GAV** (mirrors module 09's single-annotation precedent):
  `com.google.errorprone:error_prone_annotations:2.36.0`, scope `provided`. This is the Error Prone 2.x
  line pinned in SOURCE-PIN §2 ("latest 2.x … confirm exact build at companion-build"); patch `2.36.0`
  is the build-confirmed patch SOURCE-PIN already cites in the NullAway row, present in the local m2.
  It carries `com.google.errorprone.annotations.concurrent.GuardedBy` — the dossier's **recommended**
  `@GuardedBy` annotation (RESEARCH §2.5 l.157). CLASS-retained → runtime stays JDK-only.

---

## Enterprise-grade checklist

| # | Requirement | Status | Evidence |
|---|---|---|---|
| 1 | Module of the ONE parent project | PASS | `<parent>` set; no own version/BOM; child of `08-companion-code` |
| 2 | Externalized config profiles (dev/prod) | PASS | `thread-safety.properties` (`coordinator.dev.*` / `coordinator.prod.*`); loaded by `WorkCoordinatorConfig.load(Profile)`; asserted by `devAndProdProfilesReadDifferentExternalizedValues` |
| 3 | ≥1 integration test exercising the mechanism | PASS | `ThreadSafetyContractTest` (9 tests): concurrent stress over racy/atomic/synchronized counters, publication idioms, coordinator lifecycle. Test harness = JUnit Jupiter (junit-jupiter) + AssertJ from the inherited BOM; surefire 3.5.6 auto-detects the JUnit Platform provider (confirmed in the green run) |
| 4 | Observability / health surface | PASS | `WorkCoordinator.isReady()` (readiness probe) + `processedCount()` (metric); asserted by `readinessProbeFlipsOnGracefulShutdown` |
| 5 | Explicit failure path (driven by a test) | PASS | `WorkCoordinator.shutdown()` = graceful `shutdown()` → `awaitTermination(grace)` → `shutdownNow()` fallback; a shut-down coordinator rejects new work. Driven by `readinessProbeFlipsOnGracefulShutdown` + `shutDownCoordinatorRejectsNewWork` |

Deliberate counter-example: `RacyCounter` (`volatile++`) is the module's one reviewed SpotBugs
suppression — narrow, reasoned, pointed at `racyCounterCanLoseUpdatesUnderContention` — exactly the
module-10/19 pattern. Detectors stay fully enabled for every other type.

---

## Snippet tags (tag-include / anti-drift)

`check_snippets.sh 03-drafts/20_thread_safety_jmm/20_thread_safety_jmm_v1.md` → **5 markers; 5 pass, 0 fail.**

| Tag | Backing file | Lines | Prose insertion point |
|---|---|---|---|
| `racy-counter` | `RacyCounter.java` | 7 | "Three levers" — after the *Visibility is not atomicity* CONCEPT box |
| `guarded-by` | `SynchronizedCounter.java` | 9 | Deep dive — after the `@GuardedBy` / Error Prone paragraph |
| `final-publication` | `ServiceConfiguration.java` | 7 | "Safe publication" — after the §17.5 `final`-field-idiom paragraph |
| `lazy-holder` | `LazyResource.java` | 7 | Limitations — after the double-checked-locking bullet (holder idiom) |
| `jcstress-test` | `ThreadSafetyContractTest.java` | 8 | Deep dive — after the JCStress paragraph |

Each marker carries a one-line lead-in; no prose was deleted; locked third-person voice preserved.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | SpotBugs reports `volatile++` under **two** MT patterns — `VO_VOLATILE_INCREMENT` (the prose's named pattern) **and** `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`. SpotBugs surfaces one at a time by priority, so suppressing only one left the other to fail the build. | NOTE | `config/spotbugs/spotbugs-exclude.xml` | Resolved: both patterns matched in the one reviewed suppression; verified by suppressing each in turn. Prose `VO_VOLATILE_INCREMENT` is **build-accurate** (no prose change needed). |
| 2 | JCStress has **no pinned GAV** in SOURCE-PIN (§3 pins JMH, not JCStress). | NOTE | SOURCE-PIN §3; `09-flags/24_jcstress_not_pinned.md` | The `jcstress-test` region is a compiling JUnit concurrency probe of the same shape — no faked harness output, build green. Handling appended to the existing flag `09-flags/24_jcstress_not_pinned.md`. |
| 3 | `@GuardedBy` is framed in the draft as the Error Prone check, but this module's `quality` profile runs SpotBugs, not Error Prone. | NOTE | `pom.xml`, `SynchronizedCounter.java` | Used Error Prone's `@GuardedBy` annotation (dossier's recommended one) as documentation, consistent with the draft; SpotBugs also reads it for consistent-sync analysis. Error Prone as a build-time enforcer is not added (its `quality`-profile wiring is a later/own-chapter concern). |

---

## Blockers

**None.**

---

## Source trace (Floor C — every atom traces to the pin or the dossier)

| Atom in the module | Traces to |
|---|---|
| `VO_VOLATILE_INCREMENT`, `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`, `MT_CORRECTNESS` | RESEARCH §2.7 (SpotBugs `bugDescriptions.html`, verified ✅) + **confirmed by the build output** |
| `@GuardedBy` = `com.google.errorprone.annotations.concurrent.GuardedBy` | RESEARCH §2.5 l.157 (recommended annotation, verified) ; GAV from SOURCE-PIN §2 Error Prone 2.x line |
| `final`-field freeze / safe publication | RESEARCH §2.3 (JLS SE 21 §17.5, verbatim ✅) |
| init-on-demand holder idiom | RESEARCH §4 l.249 + draft Limitations bullet |
| `AtomicLong.incrementAndGet`, `ExecutorService`/`Future`, `shutdown`/`awaitTermination`/`shutdownNow` | RESEARCH §2.6 / §4 + j.u.c @JDK 21 (SOURCE-PIN runtime baseline) |
| JCStress "experimental harness" framing | RESEARCH §2.6 (verbatim ✅); GAV deliberately omitted (unpinned — flag) |
| Checkstyle 10.26.1 / checkstyle-plugin 3.6.0 / spotbugs-maven-plugin 4.9.3.0 | reference module 09 (build-confirmed), SOURCE-PIN §2 |

No invented rule IDs, GAVs, API signatures, or version numbers. No atom is `UNVERIFIED`.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Every file in the module is original work written for this book. File-by-file: `RacyCounter`,
`AtomicCounter`, `SynchronizedCounter`, `ServiceConfiguration`, `LazyResource`, `WorkCoordinator`,
`WorkCoordinatorConfig`, `package-info`, the test, the README, the properties, and the pom are all
authored here for the `org.acme.concurrency` domain — none is a copied or lightly-edited upstream
sample, getting-started skeleton, or `NOTICE`/header boilerplate. The counter / publication idioms are
textbook JMM patterns expressed in original code, not copied from JCIP, the JLS, or any tool's sample
repo. `config/checkstyle/checkstyle.xml` and the pom `quality`-profile shape are reused from the book's
own reference module 09 (the house ruleset), not from an external project. No region is taken
substantially verbatim from a specific upstream source file, so no per-file attribution is required.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan is one **designed** diagram (`fig20_1`, JMM
happens-before edges + safe publication), already authored as HTML→PNG (`05-figures/20_thread_safety_jmm/`)
and not this gate's job. The topic has **no subject-native UI surface** (pure JDK concurrency code — no
dev console, health UI, API explorer, or services view), so zero screenshots are in the plan and none
were captured. No sidecars written.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality verify` at the pin; every
  displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file; Floor-C
  source-trace clean.
- [x] Module is a child of the single aggregator (no own version/BOM); **not** registered in `<modules>`
  until green + CODE-REVIEW.
- [x] Pinned dependency set via inherited parent property + one pinned annotation GAV.
- [x] Externalized config (dev/prod) present and consumed.
- [x] ≥1 integration test exercises the mechanism; test harness configured (JUnit Platform provider
  auto-detected; no spurious logging).
- [x] Observability surface present (readiness probe + metric).
- [x] Explicit failure path present and test-driven (graceful shutdown + reject-after-shutdown).
- [x] LEGAL-IP §5 original-for-this-book confirmed file-by-file.
- [x] Neutrality in code/comments/README: no banned phrasings; no crowning (scanned clean).
- [ ] **CODE-REVIEW** — not run by this gate (next: `code-reviewer` agent). Required before the module
  joins `<modules>`.

---

## Learnings & pipeline suggestions

- **Tool pattern names can be version-true under more than one ID.** SpotBugs 4.9.x raises **both**
  `VO_VOLATILE_INCREMENT` and `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` for `volatile++`, surfacing
  one at a time by priority. A suppression for a deliberate counter-example must match **every** pattern
  the build actually fires, not just the one the prose names — verify by suppressing each in turn and
  re-running. Propose: when a counter-example targets a named detector finding, the builder confirms the
  *exact* pattern set from `target/spotbugsXml.xml`, not from the dossier name alone.
- **Unpinned-tool snippet tags need a documented stand-in.** JCStress (and any authority lacking a
  SOURCE-PIN row) cannot be added as a GAV. The clean pattern: make the tagged region a compiling
  same-shape test with an honest one-line lead-in in the prose, and append the handling to the existing
  not-pinned flag rather than faking output or breaking the build. Worth promoting to EXAMPLES-GUIDE as
  the standard move for an unpinnable runtime harness.
- **Annotation-GAV precedent generalizes.** Module 09's "one pinned annotation GAV, scope `provided`,
  runtime stays JDK-only" is a reusable shape for any chapter whose subject is an annotation contract
  (here `@GuardedBy`). The choice of *which* `@GuardedBy` is dossier-driven (Error Prone's recommended
  one) and the GAV is the one the pin already names — no invention.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 20 gate-run PASS
```
