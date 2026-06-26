# GATE REPORT — EXAMPLE-BUILD — Chapter 25 (key 53)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 53 (folds 54, 57) — `01-index/FINAL_INDEX.md` Ch 25
- **Slug:** `53_solid_coupling_cohesion_packages`
- **Draft under review:** `03-drafts/53_solid_coupling_cohesion_packages/53_solid_coupling_cohesion_packages_v1.md`
- **Module path:** `08-companion-code/53_solid_coupling_cohesion_packages/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×6), `check_snippets.sh`; `mvn -B -Pquality … verify`
- **Verdict:** **PASS** (FLOOR C PASS)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)

---

## Verdict rationale

The module builds green standalone at the pinned runtime with the house static-analysis gate on, all
six displayed snippets resolve to real ≤9-line tag regions inside compiling files, and every fact
traces to the chapter draft/dossier and `SOURCE-PIN.md`. Both FLOOR-C preconditions are met and logged
below, so the FLOOR-C verdict is PASS. CODE-REVIEW (the `code-reviewer` agent) is the separate Step-4b
gate and is not part of this report; the module is **not** registered in the parent `<modules>` list
yet (it joins only after green build AND CODE-REVIEW PASS, per EXAMPLES-GUIDE §2).

---

## FLOOR C guard — both preconditions (required for any PASS)

- **(a) Runtime meets the minimum.** `java -version` → `openjdk version "21.0.11" 2026-04-21`. Meets
  the Java 21 LTS anchor (`SOURCE-PIN.md` §1: "Anchor LTS — Java 21 … Minimum assumed runtime for
  companion code"). Maven 3.9.16, `release 21`. **PASS.**
- **(b) Build is green.** Exact command (run standalone — the parent `08-companion-code/pom.xml` was
  NOT edited):
  ```
  mvn -B -Pquality -f 08-companion-code/53_solid_coupling_cohesion_packages/pom.xml verify
  ```
  Result: **`BUILD SUCCESS`** — `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0`;
  `You have 0 Checkstyle violations.`; `BugInstance size is 0` (SpotBugs). **PASS.**

Both preconditions hold → **FLOOR C = PASS** (not conditional, not assumed).

---

## Designed tags (6) — what the prose displays

The chapter did not pre-declare tags; these were designed from the v1 "Companion module" spec (the
three contrasts: over-abstracted vs balanced; a cycle then the DIP inversion; by-layer vs by-feature).
All six resolve via `extract_snippet.sh` and are within the 9-line ceiling.

| Tag | Backing file | Lines | Shows |
|---|---|---|---|
| `over-abstracted` | `overengineered/OrderPricingService.java` | 7 | SOLID at its over-application trap: factory + interface for one implementation, wiring threaded through several types |
| `balanced` | `balanced/OrderPricer.java` | 6 | The same pricing outcome with a record + one interface kept only where a real variation exists |
| `cycle` | `cycle/notify/OrderNotifier.java` | 7 | The back-edge that closes a two-package `orders`↔`notify` cycle |
| `dip-inversion` | `inverted/orders/OrderEvents.java` | 6 | The owned abstraction on the stable side that breaks the cycle |
| `by-layer` | `bylayer/controller/OrderController.java` | 7 | The orders feature spread across controller/service/repository packages |
| `by-feature` | `byfeature/orders/OrderService.java` | 8 | The orders feature kept whole in one package |

`check_snippets.sh 03-drafts/53_solid_coupling_cohesion_packages/53_solid_coupling_cohesion_packages_v1.md`
→ **`6 marker(s); 6 pass, 0 fail.`**

---

## Marker insertion points (in the draft, *How it works*)

- **SOLID section** (after the "deeper honesty: SOLID is principles, not metrics" paragraph): one
  lead-in + `over-abstracted`, then one lead-in + `balanced`. No prose deleted.
- **Coupling/cohesion section** (after the cycles paragraph): one lead-in + `cycle`, then one lead-in
  + `dip-inversion`. No prose deleted.
- **Package-structure section** (immediately after the by-layer/by-feature table): one lead-in +
  `by-layer`, then one lead-in + `by-feature`. No prose deleted.
- Foot-of-chapter spec updated: `EXAMPLE-BUILD = PENDING` → `BUILT` (with the green result line), the
  package root corrected to `org.acme.design` (was `org.acme.orders`), the JDepend/ArchUnit "reports
  it" claim re-routed to Ch 26 (this module *shows* the cycle structurally), and a **"Snippet tags:"**
  line added listing all six regions.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | Status |
|---|---|---|
| Module-of-the-ONE-parent | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `groupId`/`version`; no own BOM. Self-contained (no cross-module imports — grep confirms only `org.acme.design`). | PASS |
| Registered last | NOT added to the parent `<modules>` list (awaits green + CODE-REVIEW). Parent pom untouched. | PASS |
| 1 — Pinned platform | Runtime + test libs (JUnit 6 line, AssertJ 3.27.7) inherited from the aggregator's one property/`dependencyManagement`; the module carries no runtime version literal (production code is plain JDK). | PASS |
| 2 — Externalized config profiles | `src/main/resources/design.properties` with `%dev` (lenient) and `%prod` (strict) profiles, selected by the `design.profile` system property; read by `DirectionConfig`. | PASS |
| 3 — Integration test(s) | 4 test classes, 13 tests, run under `verify`. Exercise both pricing shapes, the cycle vs inverted wiring, both package layouts, the instability formula, and both failure-path branches. | PASS |
| 4 — Observability/health surface | `DependencyDirection.rejectedDependencyCount()` (running counter) and `isReady()` (readiness probe over loaded config). | PASS |
| 5 — Explicit failure path | `DependencyDirection.checkDependency` rejects a wrong-direction dependency (Stable Dependencies Principle violation) with a typed `UnstableDependencyException` under `%prod`, reports it under `%dev`. Both branches driven by tests. | PASS |
| Snippets are tag regions ≤9 lines | 6/6, confirmed by `extract_snippet.sh`. | PASS |
| Test-harness setup | JUnit Platform auto-detected by the inherited surefire 3.5.6; no extra log-manager/runner property needed for plain JUnit 6 (confirmed: tests run clean, no spurious logging — the one WARNING line is the failure-path test deliberately exercising the `%dev` report branch). | PASS |
| Baseline cites SOURCE-PIN | README states "Requires Java 21 (the companion-code baseline; see `00-strategy/SOURCE-PIN.md`)". | PASS |

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** This is a largely illustrative/concept chapter; its figure plan (v1 *How it
works*) is two **designed** conceptual diagrams — fig53_1 (SOLID intent/trap) and fig53_2 (the
module-strength ladder) — both authored as HTML and rendered to PNG by the figure pipeline, NOT
captured by this agent and NOT image-generated. The module is a plain-JDK library with no subject-
native UI surface (no dev console / health endpoint UI / services view) to capture live. Per
EXAMPLES-GUIDE §1.2 and the CAPTURE step, "no captures planned" is recorded here rather than inventing
a figure.

---

## NEUTRALITY in code (FLOOR A) — comments, identifiers, log/error strings

- Banned-phrase scan over `src/`, `README.md`, `config/`, `pom.xml`: **clean** after one fix —
  `inverted/orders/OrderService.java` Javadoc reworded `"Unlike the cycle variant"` → `"Where the
  cycle variant imports notify, this class imports nothing from it"` (removed the banned `unlike`
  trigger, even though it contrasted two of the book's own variants, not a rival).
- No comment, identifier, package name, log, or error string crowns the subject or disparages an
  alternative. Each contrast pair is framed as a trade-off ("neither is crowned"); package-info
  comments state the strongest case AND the cost of each shape.

---

## LEGAL-IP §5 — every companion file ORIGINAL-FOR-THIS-BOOK

- All 35 `src/` files are original work written for this chapter — none is a copied/renamed upstream
  sample, quickstart skeleton, or `_ref/` listing. The order domain, the cycle/inversion pair, the
  by-layer/by-feature split, and the `DependencyDirection` surface were authored for this chapter.
- The two `config/` files (`checkstyle/checkstyle.xml`, `spotbugs/spotbugs-exclude.xml`) are the
  shared **house ruleset** carried by every companion module (the same small curated set the flagship
  modules use, per Chapter 16), copied from the reference module `09_api_method_contracts/` as shared
  build infrastructure — not chapter content; the SpotBugs filter comment was rewritten for this
  chapter's context. The `pom.xml` follows the reference module's parent/profile *shape* (required by
  EXAMPLES-GUIDE §2/§3) with this chapter's own coordinates, dependencies, and comments.
- No substantially-verbatim block from a pinned source listing is present, so no per-file attribution
  is required.

---

## Source traceability (FLOOR C — every fact → a source)

| Fact in the module | Traces to |
|---|---|
| SOLID = SRP/OCP/LSP/ISP/DIP; "useful vocabulary, not a law to maximize"; over-application trap | v1 draft §"SOLID…" + research header (key 53; ⚠ Martin verbatim defs are a §7 canon gap — used here as *concepts*, no verbatim definition quoted in code) |
| DIP inverts a wrong-direction dependency by an owned abstraction on the stable side | v1 draft CONCEPT box (line on DIP) + research header (key 54) |
| Cycle = cardinal sin; couples a cluster into one indivisible blob | v1 draft §"Coupling and cohesion" (cycles paragraph) + research header (key 54) |
| Instability `I = Ce/(Ca+Ce)` (0 = depended-on, 1 = depends-on-everything) | v1 draft CONCEPT box (verbatim formula in the chapter's own prose) + research header (key 54; metric *definition* set routed to Ch 04, but this formula appears in this chapter) |
| Stable Dependencies Principle (depend toward stability) | v1 draft CONCEPT box + research header (key 54) |
| By-layer (`controller`/`service`/`repository`) vs by-feature (`orders`/`billing`); crown neither; cross-feature sharing needs a deliberate API | v1 draft §"Package structure" table + prose + research header (key 57) |
| Records / one interface where a real variation exists (modern Java reaches a SOLID goal more directly) | v1 draft CONCEPT box (Java-specific subtlety) + research header (key 53) |
| Toolchain: Java 21 anchor; JUnit 6 line; AssertJ 3.27.7 | `SOURCE-PIN.md` §1, §3 |
| Checkstyle/SpotBugs plugin+engine versions (`maven-checkstyle-plugin:3.6.0` + `checkstyle:10.26.1`; `spotbugs-maven-plugin:4.9.3.0`) | the house reference module `09_api_method_contracts/pom.xml` (the cached, green house values) — see flag F1 below |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| F1 | House static-analysis engine versions (`checkstyle 10.26.1`, `spotbugs-maven-plugin 4.9.3.0`) trail the `SOURCE-PIN.md` rows (Checkstyle **13.6.0**, SpotBugs **4.10.2**). | NOTE | `pom.xml` `quality` profile | Matched the house reference module 09 exactly (the cached, green house values), per the task's "match house Checkstyle/SpotBugs / copy config from 09" instruction. A book-wide bump of the analyzer engines to the pinned rows should be done once at the aggregator/module level (out of scope for one chapter; flagged so the gap is visible). Not a FLOOR-C blocker: no invented value — both versions trace to the in-repo house module. |
| F2 | The cycle's `OrderNotifier` triggered SpotBugs `EI_EXPOSE_REP2` when it stored a concrete `OrderService`. | NOTE (resolved) | `cycle/notify/OrderNotifier.java` | Fixed (not suppressed): introduced an `orders`-owned `OrderSummaries` functional interface for the read-back surface. The two-package cycle is preserved (both packages still import each other); SpotBugs is clean. Empty SpotBugs filter retained (zero suppressions). |

---

## Blockers

**None.** Build green; both FLOOR-C preconditions met; all markers resolve; NEUTRALITY clean;
originality confirmed.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality … verify` at the pinned
  runtime; every displayed snippet resolves to a real bounded `// tag::name[]` region in the compiled
  file (6/6, ≤9 lines); FLOOR C source-trace clean.

---

## Learnings & pipeline suggestions

- **Tag names must not be mentioned verbatim in surrounding Javadoc.** `extract_snippet.sh` matches
  `tag::<name>[]` anywhere in the file, so a Javadoc sentence like `{@code // tag::balanced[]}` is
  matched as the opening marker and the region over-runs the cap. Three regions failed for exactly
  this reason until the prose mentions were reworded to "the displayed region below". Suggest a
  one-line note in EXAMPLES-GUIDE §5 (and/or a `check_snippets` warning) that a tag literal must
  appear only on its own marker line, never inside a comment.
- **A "largely illustrative" chapter can still meet all five enterprise requirements honestly.** The
  failure path (req. 5) is the load-bearing one: rather than bolt on an unrelated fault-tolerance
  annotation, the wrong-direction-dependency rejection (Stable Dependencies Principle) is the
  chapter's own mechanism turned into a real, tested, config-driven error path — and it carried the
  observability surface (req. 4) and the `%dev`/`%prod` externalized config (req. 2) with it.
- **Preserving a "bad" pattern (a cycle) past SpotBugs without a suppression is possible.** Narrowing
  the back-edge to an owned interface kept the package cycle intact (the teaching point) while
  clearing `EI_EXPOSE_REP2` — the house "prefer fixing over reasoned-suppression" rule held even for
  deliberately-imperfect demo code.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 53 gate-run PASS
```
