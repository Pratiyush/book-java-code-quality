# GATE REPORT — EXAMPLE-BUILD — Chapter 40 (key 96, folds 94)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 96 (owner; folds 94 — automated change / OpenRewrite)
- **Slug:** `96_remediation_playbook_automated_change`
- **Draft under review:** `03-drafts/96_remediation_playbook_automated_change/96_remediation_playbook_automated_change_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×10), `check_snippets.sh` (draft); NEUTRALITY banned-phrase grep over the module
- **Build state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** (FLOOR C)

---

## Verdict rationale

The module is a self-contained child of the one companion-code aggregator, builds **green offline** under
the exact gate command on the pinned runtime, and every displayed snippet resolves to a real ≤9-line tag
region inside a compiling file. The OpenRewrite recipe **run** is honestly scoped as opt-in and REPRO
PENDING-RUNTIME (network-gated); the recipe **config**, the before/after pair, and the prioritization logic
all verify offline, so the build does not depend on it. No invented atom enters the build: the OpenRewrite
engine/plugin versions trace to `SOURCE-PIN.md §6`, and the recipe ID + recipe-module GAV are flagged
verify-at-pin to `09-flags/`.

---

## Module

- **Path:** `08-companion-code/96_remediation_playbook_automated_change/`
- **Artifact:** `remediation-playbook-automated-change` (child of `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`)
- **Parent wiring:** `<parent>` set to the aggregator; **no** `<groupId>`/`<version>` literal; **no** own BOM
  import. Self-contained: own `config/` dir (checkstyle + spotbugs + rewrite) and own `quality` + `rewrite`
  profiles (the peer-39 shape), so it builds standalone via `-f` without any prior chapter.
- **Registered in parent `<modules>`?** **NO** — deliberately left out until green build **and** CODE-REVIEW
  PASS (EXAMPLES-GUIDE §2). Parent `08-companion-code/pom.xml` was **not edited** (per task constraint).

---

## FLOOR C guard (both preconditions logged)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime ≥ Java 21 | `java -version` → `openjdk version "21.0.11" 2026-04-21` (SOURCE-PIN anchor JDK 21) | **PASS** |
| (b) Build GREEN | exact command below → `BUILD SUCCESS` | **PASS** |

**Exact gate command (run from repo root):**

```
mvn -B -Pquality -f 08-companion-code/96_remediation_playbook_automated_change/pom.xml clean verify
```

**Result line:** `BUILD SUCCESS` — `Tests run: 14, Failures: 0, Errors: 0, Skipped: 0`;
`You have 0 Checkstyle violations.`; SpotBugs `BugInstance size is 0`. **Warning-clean** — zero `[WARNING]`
and zero `[ERROR]` lines (compiler runs `-Xlint:all,-processing` inherited from the aggregator).

- **Toolchain:** Maven 3.9.16 (SOURCE-PIN §4); JDK 21.0.11 (SOURCE-PIN anchor).
- **Default build** (`mvn -B … verify`, no profile) is also green (10 sources, 14 tests) and needs **no
  network** — confirmed against an empty local OpenRewrite cache.

---

## Tag-include regions (10 tagged; 8 surfaced in the draft) — all ≤9 lines

| Tag | File | Resolved lines | In draft |
|---|---|---|---|
| `playbook-order` | `…/PlaybookStep.java` | 6 | ✅ |
| `churn-pain-rank` | `…/RemediationPrioritizer.java` | 6 | ✅ |
| `frozen-no-interest` | `…/RemediationPrioritizer.java` | 5 | ✅ |
| `churn-pain` | `…/DebtItem.java` | 3 | (module only) |
| `reject-big-bang` | `…/RemediationPlan.java` | 8 | ✅ |
| `program-health` | `…/ProgramHealth.java` | 4 | ✅ |
| `rewrite-recipe` | `config/rewrite/rewrite.yml` | 8 | ✅ |
| `before` | `…/legacy/LegacyReleaseNotes.java` | 7 | ✅ |
| `after` | `…/Modernized.java` | 5 | ✅ |
| `config-profiles` | `…/remediation.properties` | 9 | (module only) |

- `check_snippets.sh` on the draft: **8 marker(s); 8 pass, 0 fail.** Each region was also extracted directly
  (`extract_snippet.sh`) and is within the 9-line ceiling.
- The largest region (`config-profiles`) is exactly 9 lines — at the cap, not over it.

---

## Enterprise-grade checklist (all five hold)

| # | Requirement | How it is met |
|---|---|---|
| 1 | **Pinned platform** | Java pinned once in the aggregator (`maven.compiler.release` 21); module adds no runtime version literal. OpenRewrite pinned to `SOURCE-PIN.md §6` (8.81.0 / plugin 6.38.0) in the opt-in `rewrite` profile. |
| 2 | **Externalized config profiles** | `src/main/resources/remediation.properties` carries a `dev` default + a `prod` override; `RemediationConfig` resolves the active profile (prefixed-key-overrides-default). Test drives both. No threshold hard-coded. |
| 3 | **Integration test** | `RemediationPlaybookTest` (14 tests, JUnit Jupiter) exercises the mechanism: prioritization, ordered playbook, before/after equivalence, health, config profiles, and every failure branch. |
| 4 | **Observability surface** | `ProgramHealth#report` → `SUSTAINED` / `STALLING` so a stalled program is visible, not green-by-default (tag `program-health`). |
| 5 | **Explicit failure path** | `RemediationPlan` compact constructor **rejects** a baseline with no paydown plan (real error response, message names why) and `DebtItem` rejects negative proxies — both driven by tests (tag `reject-big-bang`). HONEST-LIMITATIONS floor as a code path. |

**Test-harness setup:** none beyond the aggregator — surefire 3.5.6 + the JUnit BOM (6.0.3) are inherited
from the parent `pluginManagement`/`dependencyManagement`; no per-module plugin config. Confirmed by a
clean `Tests run: 14` under both the default and `quality` builds.

---

## Step 4c — CAPTURE

**No captures planned.** The chapter's figure plan (draft §"How it works") is **two designed conceptual
diagrams** — `fig96_1` (playbook flow) and `fig96_2` (automation engine) — already authored as HTML→PNG
with their `.sources.md` sidecars under `05-figures/96_remediation_playbook_automated_change/` (not the
example-builder's job). The module is a plain JDK/Maven library with **no subject-native UI surface** (no
dev console, API explorer, or health view to capture live). ZERO captured screenshots is correct for this
chapter's class; none invented.

---

## Source trace (every load-bearing atom)

| Atom in module | Traces to |
|---|---|
| `maven.compiler.release` 21; min runtime | `SOURCE-PIN.md` Runtime baseline (anchor JDK 21) |
| JUnit Jupiter 6.0.3, AssertJ 3.27.7 | `SOURCE-PIN.md §3` (via aggregator `dependencyManagement`) |
| Maven 3.9.16 | `SOURCE-PIN.md §4` |
| Checkstyle engine 10.26.1, plugin 3.6.0; SpotBugs plugin 4.9.3.0 / annotations 4.9.3 | the companion-code locally-cached lines (same as peer Ch 27/39 modules) |
| OpenRewrite 8.81.0; `rewrite-maven-plugin` 6.38.0 | `SOURCE-PIN.md §6` ✅ pinned |
| Recipe ID `org.openrewrite.java.migrate.UpgradeToJava21`; `rewrite-migrate-java:3.34.0` (was `3.16.0` — corrected to the engine-8.81.0-aligned version) | ☑ **web-verified 2026-06-28** against docs.openrewrite.org (recipe ID, verbatim) + Maven Central (GAV; via rewrite-recipe-bom 3.30.0 → rewrite-bom 8.81.0); `09-flags/94_…` RESOLVED (identity). Recipe RUN stays REPRO PENDING-RUNTIME. |
| Playbook order, churn × pain, never-big-bang, baseline-without-paydown=amnesty, automation-proposes-tests-dispose | dossiers 96 + 94 (synthesis of Part XI) |

---

## LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK confirmation

Confirmed file-by-file: every source, config, README, and test file under the module is **original work
written for this book**. Nothing is a copied or lightly-edited upstream OpenRewrite/Checkstyle/SpotBugs
sample, quickstart, or `_ref/` listing. The `quality`-profile and config-dir **shape** mirrors this book's
own peer module (Ch 39 `managing-findings`), which is the book's reference implementation, not an upstream
file. The `rewrite.yml` uses the documented OpenRewrite recipe-descriptor schema
(`type: specs.openrewrite.org/v1beta/recipe`) with this book's **own** recipe name
(`org.acme.remediation.ModernizeForJava21`) and content — not copied from a sample. No substantially-verbatim
upstream block requiring attribution.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | OpenRewrite recipe ID + recipe-module GAV not resolvable offline (engine/plugin pinned; recipe atoms live-line) | NOTE | `config/rewrite/rewrite.yml`, `pom.xml` `rewrite` profile | Flagged to `09-flags/94_…`; recipe **run** is opt-in + REPRO PENDING-RUNTIME; re-trace at pin when artifacts are fetchable |
| 2 | SpotBugs/Checkstyle baselines are wired but **inert** (no `<Match>`/`<suppress>` row fires) | NOTE | `config/spotbugs/spotbugs-exclude.xml`, `config/checkstyle/checkstyle-suppressions.xml` | Intentional and documented in both files + README — the before/after pair carries the visible debt; same honest pattern as peer Ch 39 |

---

## Blockers

**None.** FLOOR C (a) + (b) both PASS; no invented atom; every marker resolves within the cap.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via the gate command at the pin; every displayed snippet
  resolves to a real bounded tag region in the compiled file; FLOOR C source-trace clean.
- [ ] **CODE-REVIEW** — pending (Step 4b `code-reviewer` agent). Module is **not** registered in the parent
  `<modules>` list until CODE-REVIEW PASSES.

---

## Learnings & pipeline suggestions

- **Network-gated tooling belongs in its own opt-in profile, kept off the default and `quality` paths.**
  The `rewrite` profile pattern lets the chapter ship a real, pinned OpenRewrite wiring AND a green offline
  build at once — the recipe RUN is REPRO PENDING-RUNTIME while the recipe CONFIG + before/after pair are
  verified offline. This is the cleanest way to satisfy FLOOR C COMPILE when a topic's tool needs the
  network. Worth promoting into EXAMPLES-GUIDE as the standard shape for network-gated engines.
- **A pinned engine version does not mean its recipe IDs / recipe-module GAVs are pinned.** OpenRewrite's
  engine (`§6`) and its recipe modules (`rewrite-recipe-bom`) version separately — the same "two-pin"
  lesson the Checkstyle plugin-vs-engine split taught. Recorded in the flag; suggest a one-line note in
  `SOURCE-PIN.md §6` that recipe-module GAVs are a separate pin from the engine.
- **The failure-path requirement carried this synthesis chapter well:** "reject a baseline-without-paydown"
  is the HONEST-LIMITATIONS floor (no big-bang, no amnesty) turned into a constructor that throws under
  test — exactly the EXAMPLES-GUIDE §1.1 intent for a non-HTTP topic (real error response over a domain
  invariant, not a bolted-on fault-tolerance annotation).

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 96 gate-run PASS
```
