# GATE REPORT — CODE-REVIEW — Chapter 35 (Composition, Not Accumulation)

## Header

- **Gate:** CODE-REVIEW (FLOOR C, second half — the judgment a green build cannot make)
- **Chapter key:** 35 (owner; folds 36, 37) · printed **Chapter 17**, Part IV
- **Slug:** `35_sonarqube_ide_layered_stack`
- **Module under review:** `08-companion-code/35_sonarqube_ide_layered_stack/`
- **Draft under review:** `03-drafts/35_sonarqube_ide_layered_stack/35_sonarqube_ide_layered_stack_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` (senior-PR review of reader-facing example code)
- **Builds/scripts run:** `mvn -B verify` (default) · `mvn -B -Pquality verify` (Checkstyle→SpotBugs) · `check_snippets.sh` · `extract_snippet.sh` (per-tag) · secret grep · neutrality grep — JDK 21.0.11 (openjdk@21), Maven (system), 2026-06-27
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is correct, idiomatic Java 21, secure, simple, well-tested, neutral, and builds green and
warning-clean on both the default and `-Pquality` profiles (7 tests / 0 Checkstyle / 0 SpotBugs —
matching the recorded green build). Every displayed `// tag::` region resolves, is brace/structure-sound
*for its language*, and is within the 9-line ceiling. **No BLOCKER.** Two MINOR fixes are required before
approval: (1) the displayed `layered-gate` XML region opens `<dependencies><dependency>` it does not close
inside the shown lines — a reader pasting the snippet alone gets non-well-formed XML; retag so the shown
region is self-balancing or add a one-line "(closing tags omitted)" cue. (2) a repo-wide pin-vs-build gap
the chapter inherits — the displayed Checkstyle engine `10.26.1` and maven-checkstyle-plugin `3.6.0` differ
from SOURCE-PIN (Checkstyle **13.6.0**), and the build's SpotBugs plugin `4.9.3.0` differs from the pinned
SpotBugs **4.10.2**. These are consistent across ~7 companion modules, so they are not a chapter-35
invention, but the displayed version atom is off-pin and should be reconciled (most cleanly at the
aggregator, repo-wide). Neither is a FLOOR-C invention or a security/neutrality breach, so the gate passes
with fixes rather than failing.

---

## Tag-region integrity (CRITICAL check — brace/structure balance, ≤9 lines, no fragment, no dup end-tag)

Exactly **6** include directives in the draft ↔ **6** tag pairs in the module. Each tag pair is opened
once and closed once: **no orphan tag, no duplicate end-tag, no imbalanced marker.** `check_snippets.sh`:
6/6 PASS. Per-region (body line counts exclude the marker lines, as the extractor strips them):

| Tag | File:lines (body) | Body lines (≤9) | Balanced for its language? | Verdict |
|---|---|---|---|---|
| `sonar-keys` | `sonar-project.properties:16–21` | 6 | yes — 6 complete `key=value` lines | PASS |
| `quality-gate-wait` | `sonar-project.properties:31–32` | 2 | yes — 2 complete properties | PASS |
| `connected-mode` | `.editorconfig:18–24` | 7 | yes — complete `[*.java]` stanza | PASS |
| `one-owner` | `LayeredStack.java:39–44` | 6 | **yes — braces balanced** (`if (...) {` L40 closes L43; `throw`/`return` complete) | PASS |
| `layered-gate` | `pom.xml:72–79` | 8 | **NO — opens `<dependencies>` (L75) + `<dependency>` (L76); closes are at L81–82, OUTSIDE the region** | **PASS (cap+resolve) / FIX (unbalanced XML shown)** |
| `ci-sonar-step` | `ci/sonar-analysis.yml:42–48` | 7 | yes — complete YAML step (`name`/`env`/`run`) | PASS |

**Not a BLOCKER:** the task's BLOCKER condition is a duplicate/imbalanced *end-tag* or a mid-statement
code fragment; there is none. `layered-gate` is a well-formed *Maven* fragment (it stops on a complete
`<version>…</version>` element, not mid-element) but is not a self-contained XML *tree* — the two
enclosing container tags are left open. It compiles in place (build is green) and resolves under the cap;
the issue is purely how the snippet reads when shown alone in the book. Recorded as **MINOR (F1)**.

---

## Six review dimensions

| # | Dimension | Result | Notes |
|---|---|---|---|
| 1 | Correctness | **PASS** | `assign` uses `putIfAbsent` then throws on a non-null prior value — no partial mutation on the reject path; null-guards precede the tag region; `ownerOf` returns `Optional` (no null leak); `requireOwnerOf` is the explicit failure path; `orderedCheapFirst` sorts by `Moment` with stable `EnumMap` iteration; no I/O, so no resource leak; no swallowed exception (no empty catch). 7 tests, all non-vacuous. |
| 2 | Idiomatic Java 21 | **PASS** | `record Analyzer` with a compact constructor + `Objects.requireNonNull` (Effective Java Item 17/Item 1-style validation); `EnumMap` for an enum key; `Comparator.comparing(Analyzer::moment)`; `Stream.toList()`; sealed-by-enum domain; no raw threads, no ad-hoc `System.out`/`printStackTrace`. Public types each carry a one-line purpose Javadoc. |
| 3 | Security | **PASS** | Secret grep clean. Every credential is an env/CI-secret reference (`${{ secrets.SONAR_TOKEN }}`, `SONAR_HOST_URL`), explicitly commented "never committed"; `sonar-project.properties` says the host URL + token are supplied at runtime. No hardcoded secret, no injection sink, no internal-leak in error strings. `permissions: contents: read` in the workflow is least-privilege. |
| 4 | Simplicity & readability | **PASS** | Smallest model that teaches the point: 4 small enums/record + one `LayeredStack`. No dead code, no unused deps (JDK-only at runtime; test libs inherited, no version literal). Realistic names (`org.acme.layered`, `Concern`, `Substrate`, `Moment`) — no `Foo`/`Bar`/`tmp`/`TODO`/placeholder. |
| 5 | Prose↔code fidelity & originality | **PASS-WITH-FIXES** | All 6 displayed regions match what the prose claims (`sonar.java.binaries` required, `qualitygate.wait` fails the build, Clean-as-You-Code new-code scope, one-owner refusal, source-then-bytecode ordering, the CI Sonar step by goal). Sonar version discipline is correct (no Sonar version literal; goal `sonar:sonar`; dated-at-use; flagged in `09-flags/35_*`). Code is original-for-this-book (composition model is the book's own synthesis, not an upstream quickstart). **FIX (F2):** displayed Checkstyle engine `10.26.1` / plugin `3.6.0` and build SpotBugs `4.9.3.0` are off SOURCE-PIN (Checkstyle 13.6.0 / SpotBugs 4.10.2). |
| 6 | Neutrality in code | **PASS** | No banned phrasing as a crowning/disparagement. The only grep hits are neutrality-*affirming*: Substrate.java "None of these is smarter than another; each simply stands somewhere different," and "no tool is crowned" / "none crowned." Both IDEs and every tool are named to their own docs; none crowned. |

---

## Findings

Severity: BLOCKER (gate cannot pass) · MAJOR (fix before approval) · MINOR (should fix) · NOTE (no action).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| F1 | Displayed `layered-gate` XML region is not a self-contained tree: it opens `<dependencies>` and `<dependency>` but their closes (`</dependency>`, `</dependencies>`) fall outside the shown lines. A reader copying the printed snippet gets non-well-formed XML. | MINOR | `08-companion-code/35_sonarqube_ide_layered_stack/pom.xml:72–79` (tag `layered-gate`; closes at L81–82) | Either move `end::layered-gate[]` after `</dependencies>` (L82) so the region closes both containers (raises body to ~10 lines → also tighten the region, e.g. start at `<artifactId>maven-checkstyle-plugin`), OR keep the region but add a single prose cue at draft line 128 such as "(plugin `<dependencies>`/`<dependency>` wrapper elided)". Goal: the shown XML parses, or the elision is explicit. |
| F2 | Off-pin version atoms. Displayed Checkstyle engine `<checkstyle>10.26.1` and `maven-checkstyle-plugin` `3.6.0`; build-only SpotBugs plugin `4.9.3.0`. SOURCE-PIN §"Checkstyle" = **13.6.0**, §"SpotBugs" = **4.10.2**. Only `10.26.1` and `3.6.0` are in a displayed region. | MINOR | `pom.xml:74,79` (displayed) and `pom.xml:111` (SpotBugs, not displayed) | Reconcile to the pin — Checkstyle engine → 13.6.0 (confirm Maven coordinate exists at pin), SpotBugs plugin → the plugin release matching SpotBugs 4.10.2. Because the same literals recur across ~7 modules (09, 26, 27, 62, 109, 110, 35), fix at the aggregator repo-wide and re-run `-Pquality verify`; do not patch chapter 35 in isolation and diverge from peers. Out-of-scope-to-this-module but recorded so FLOOR C is honest. |
| F3 | Parent aggregator pins `junit.version=6.0.3`; SOURCE-PIN §"JUnit" = **6.1.0**. Inherited by this module (no local literal). | NOTE | `08-companion-code/pom.xml:75` | Repo-wide: bump `junit.version` to 6.1.0 at the aggregator if the pin is to be honored. Not specific to ch 35; logged for the reconcile pass. |
| F4 | The `one-owner` snippet (and `layered-gate`) shows the body but not the preceding `Objects.requireNonNull` guards (which sit above the tag). The prose claims "refuses a second owner"; the null-contract is real in the file but invisible in the shown lines. | NOTE | `LayeredStack.java:36–37` (guards) vs tag at L38 | Acceptable — the snippet's teaching point is the one-owner refusal, and the Javadoc documents the null contract. No change required; noted for fidelity completeness. |
| F5 | Printed-chapter vs dossier-key naming: every config/comment says "Chapter 17" while the slug/dir is `35`. Verified CORRECT against the draft header ("printed Chapter 17, Part IV; owner key 35"). | NOTE | `pom.xml`, `package-info.java`, `README.md`, all config headers | None — this is the intended convention (printed number in prose/comments, frozen dossier key in paths). Flagged only so a later reviewer does not mistake it for an error. |

---

## Blockers

**None.** Exactly 6 tag pairs, each opened and closed once; no duplicate end-tag, no imbalanced marker,
no mid-statement code fragment. The `layered-gate` XML region is a complete-element Maven fragment (stops
on a closed `<version>` element), only its two enclosing container tags are left open — recorded as MINOR
F1, not a blocker. No security finding, no neutrality finding, no invented `rule ID / config key / tool
flag / API signature / GAV / version / benchmark / quoted claim`.

---

## Build / lint result

| Check | Command | Result |
|---|---|---|
| Default build + tests | `mvn -B -f pom.xml clean verify` | **BUILD SUCCESS** — Tests run: 7, Failures 0, Errors 0, Skipped 0; warning-clean (no deprecation/unchecked/preview/WARNING in output) |
| Local layered gate | `mvn -B -Pquality -f pom.xml clean verify` | **BUILD SUCCESS** — 7 tests; **0 Checkstyle violations**; SpotBugs "Error size is 0 / No errors/warnings found" |
| Snippet markers | `check_snippets.sh <draft>` | 6 markers, **6 pass / 0 fail** (all resolve, all ≤9 lines) |
| Secret scan | grep `password|secret|token|apikey|key` literal-assigned, all committed files | **clean** (only env/CI-secret references, never literals) |
| Neutrality scan | grep banned crowning/disparaging phrasings (code + comments + config + README) | **clean** (only neutrality-affirming "none smarter / none crowned") |
| Toolchain | openjdk **21.0.11** (SOURCE-PIN anchor LTS) / Maven (system) | matches recorded green-build env (JDK 21.0.11, 2026-06-26) |

Empty SpotBugs exclude filter verified honest: the model is immutable records + enums + an `EnumMap`
never handed out by reference, so representation-exposure detectors stay quiet **without** suppression —
the chapter's "no suppressions needed" claim is proven by the green run, not asserted.

---

## Test-quality assessment

7 tests, none vacuous. Both **failure-path** tests assert the exception type AND message content
(`hasMessageContaining("already owned by Checkstyle")` for the refused second owner;
`hasMessageContaining("coverage gap")` for the unowned concern), so they exercise the *real* failure
paths the prose and README promise, not merely "throws something." `runsCheapFirst` uses order-sensitive
`containsExactly`; `ownerCountReportsCoverage` asserts both the exact count and `isLessThan(values().length)`;
`readinessReflectsCoverage` checks the fail-closed empty-stack path; `analyzerRejectsBlankOrNull` covers
blank-name and null-component validation. The two intentionally-unowned concerns (SOURCE_SMELLS,
NULL_SAFETY) are the deliberate coverage-gap demonstration, well-documented in test comments.

---

## Learnings & pipeline suggestions

1. **Add an XML/brace balance check to `check_snippets.sh` (or a CODE-REVIEW sub-check).** The cap-and-resolve
   gate is green for `layered-gate` even though the shown XML opens two unclosed container tags. A cheap
   heuristic — for `.xml` regions, assert the shown lines have non-negative running tag depth ending at the
   start depth, or at least warn — would catch the "snippet reads as malformed when shown alone" class
   before human review. Worth a rule promotion in EXAMPLES-GUIDE: *a displayed region should be self-balancing
   for its language, or the elision must be explicit in prose.*
2. **Reconcile the companion-code build pins to SOURCE-PIN repo-wide.** Checkstyle engine `10.26.1` vs pinned
   `13.6.0`, SpotBugs plugin `4.9.3.0` vs pinned `4.10.2`, and JUnit `6.0.3` vs pinned `6.1.0` recur across
   ~7 modules. This is an aggregator-level drift, not a per-chapter defect; a single reconcile pass at
   `08-companion-code/pom.xml` (+ the per-module plugin literals) would clear F2/F3 everywhere at once and
   keep displayed version atoms on-pin. Recommend a dedicated reconcile task rather than per-chapter patches.
3. **The printed-number vs dossier-key duality is working but trips reviewers.** Consider a one-line banner
   comment in each module's `pom.xml` ("printed Chapter NN / dossier key MM") to pre-empt the "wrong chapter
   number" false alarm (F5) on every future code review.
4. **Sonar SaaS discipline is exemplary and reusable.** This module's pattern — assert no Sonar version
   literal, invoke the scanner by goal, mark every Sonar reference dated-at-use, push version/severity/edition
   atoms to a `09-flags/` entry, keep the live-server seeded-issue scan runtime-gated — is the right template
   for any hosted/rolling-tool chapter. Worth codifying in EXAMPLES-GUIDE as the canonical "config for a
   continuously-released platform" recipe.
