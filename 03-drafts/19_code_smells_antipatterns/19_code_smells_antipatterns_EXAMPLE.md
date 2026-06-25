# GATE REPORT — EXAMPLE-BUILD (Step 4 / Gate: EXAMPLE)

## Header

- **Gate:** EXAMPLE-BUILD
- **Chapter key:** 19 (folds 61)
- **Slug:** `19_code_smells_antipatterns`
- **Draft under review:** `03-drafts/19_code_smells_antipatterns/19_code_smells_antipatterns_v1.md`
- **Module path:** `08-companion-code/19_code_smells_antipatterns/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` (manual run) + `extract_snippet.sh` / `check_snippets.sh`
- **Scripts run:** `check_snippets.sh` (PASS), `extract_snippet.sh` (×4 PASS), `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module is a self-contained child of the one companion-code aggregator (mirrors
`09_api_method_contracts` / `10_immutability_value_design`), builds green under
`mvn -B -Pquality verify` warning-clean on JDK 21.0.11, all four declared snippet tags resolve to
≤9-line regions and pass `check_snippets`, and every rule key / version / API in the module traces to
the pin. The module is NOT yet registered in the parent `<modules>` list — that is deliberate and
correct: registration is gated on the CODE-REVIEW pass (Step 4b), which has not run.

---

## FLOOR C guard — preconditions (both logged, both hold)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime ≥ minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` (Homebrew); Maven 3.9.16 on the same JDK | PASS — meets the SOURCE-PIN anchor (JDK 21.0.11) |
| (b) `mvn -B -Pquality verify` GREEN | `BUILD SUCCESS`; `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0`; `No errors/warnings found` | PASS |

- **Exact command:** `mvn -B -Pquality -f 08-companion-code/19_code_smells_antipatterns/pom.xml clean verify`
  (toolchain: `JAVA_HOME=/opt/homebrew/opt/openjdk@21/...`; standalone — parent pom NOT edited).
- **Result line:** `BUILD SUCCESS` · 13 tests pass · 0 Checkstyle · 0 SpotBugs (BugInstance size is 0) · warning-clean.

---

## Snippet tags (tag-include regions) — all resolve ≤9 lines

| Tag | Backing file | Resolved lines | Pair role |
|---|---|---|---|
| `smell-long-method` | `src/main/java/org/acme/smells/OrderServiceSmelly.java` | 8 | smell (Long Method / type-code branch) |
| `refactor-extract` | `src/main/java/org/acme/smells/OrderService.java` | 8 | refactor (Extract Function + guard clauses) |
| `smell-expose-rep` | `src/main/java/org/acme/smells/OrderLeaky.java` | 8 | smell (exposing internal representation) |
| `refactor-defensive-copy` | `src/main/java/org/acme/smells/Order.java` | 5 | refactor (defensive copy / `List.copyOf`) |

- `check_snippets.sh 03-drafts/19_code_smells_antipatterns/19_code_smells_antipatterns_v1.md`
  → **4 marker(s); 4 pass, 0 fail.**
- Snippet ceiling (≤9 lines): all four under the cap. The displayed slice and the runnable file are
  one artifact (anti-drift): the smelly `placeOrder` body stays long in the file; the displayed region
  is a tight ≤9-line slice of its nested type-code branch.

### Marker insertion points (draft prose)

All inserted in the **Deep dive: when the pattern is the disease** section, immediately after the
existing "One worked smell…" paragraph (the paragraph that already contrasts the leaking getter with
the sixty-line method). No prose deleted; each marker carries a one-line neutral, third-person lead-in.
Order: `smell-expose-rep` → `refactor-defensive-copy` → `smell-long-method` → `refactor-extract`, then
a closing sentence on the behaviour-preservation test. The back-matter "Companion module" note and the
header comment were updated from `PENDING-RUNTIME` to **built green**.

---

## Enterprise-grade checklist

| Requirement | How met |
|---|---|
| Child of the ONE aggregator (no own version literal / BOM) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; only `<artifactId>` set; deps inherit from the aggregator's `dependencyManagement` (JUnit BOM 6.0.3, AssertJ 3.27.7) |
| Pinned dependency set via inherited parent property | `maven.compiler.release=21`, `junit.version=6.0.3`, `assertj.version=3.27.7` all inherited; analyzer engines pinned in the `quality` profile (Checkstyle 10.26.1, SpotBugs 4.9.3.0) — same shape as modules 09/10 |
| Externalized config profiles | `src/main/resources/code-smells.properties` carries `%dev`/`%prod` analysis-profile keys + pricing policy (currency, free-shipping threshold) — no magic constants in a long method |
| ≥1 integration test exercising the mechanism | `CodeSmellsTest` (13 tests): behaviour-preservation across every `LoyaltyTier` and the free-shipping boundary (`@ParameterizedTest` + `@EnumSource`), the leak-is-a-real-bug failure path, the defensive-copy fix, typed rejections, observability probes |
| Test-harness setup | JUnit Jupiter via the surefire JUnitPlatform provider (auto-detected); AssertJ assertions; no logging system-properties needed (uses `System.Logger` at DEBUG, silent by default) — confirmed: no spurious logging in the green run |
| Observability / health surface | `OrderService.rejectedCount()` + `isReady()` (and the same on `OrderServiceSmelly`) — a running rejected-counter metric and a readiness probe |
| Explicit failure path | `OrderRejectedException` (typed, stable reason code) for empty / mixed-currency orders; the `OrderLeaky` mutation-corruption path proven by test — the HONEST-LIMITATIONS floor in code |

---

## Detection boundary — verified, and made honest in the prose

- **SpotBugs raises a real finding here.** With the exclude filter removed, the raw report
  (`target/spotbugsXml.xml`) contains exactly one `BugInstance type='EI_EXPOSE_REP' priority='2'`
  (Medium) on `org.acme.smells.OrderLeaky.lines()` — "may expose internal representation by returning
  reference to mutable object." So the single reviewed suppression is **load-bearing**, not decorative.
- **Verified tool quirk (recorded for the pipeline):** SpotBugs 4.9.3.0 does **not** raise the
  `EI_EXPOSE_REP` family on a **record's** generated accessor (confirmed by an empty-filter run on the
  record form, and against module 10's record `OrderLeaky`). `OrderLeaky` here is therefore a **plain
  final class** with a hand-written getter so the finding genuinely fires — faithful to the chapter's
  hook ("a getter hands back its internal `List<LineItem>`"). Only `EI_EXPOSE_REP` (the return side)
  fires at Medium; `EI_EXPOSE_REP2` (the store side) does not at this threshold — the javadoc, filter
  comment, README, and back-matter were all corrected to state exactly this, no over-claim.
- **The Long Method is deliberately NOT flagged by this gate.** The house Checkstyle ruleset carries no
  method-length/complexity check, and SpotBugs is a bytecode bug detector, not a metric tool — so the
  long `placeOrder` is the shape Sonar `java:S3776` / PMD `NcssCount` measure but is not caught here.
  This is the chapter's own point ("different tools see different smells") shown by the build rather
  than asserted; the prose and README say so explicitly.

---

## Capture (Step 4c) — subject-native UI screenshots

**No captures planned.** Chapter 19's figure plan (draft-time) is a single designed conceptual diagram,
`fig19_1` (the smell-triple), already authored as HTML→PNG under `05-figures/19_code_smells_antipatterns/`
(not the example-builder's job). The chapter has no live dev-console / health-endpoint / API-explorer
figure in its plan — it is a concept/catalogue chapter. No subject-native UI surface was captured;
inventing one would be an unplanned figure (an editorial signal to the drafter, not a capture decision).

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. No file is a
copied-or-renamed upstream sample, getting-started/quickstart skeleton, or `NOTICE`/header boilerplate.
A scan for `copyright` / `licensed under` / `apache license` / `@author` / `generated by` / `quickstart`
across all `.java` / `.xml` / `.properties` returned nothing. No region is taken substantially verbatim
from any source file, so no excerpt attribution is required. The module mirrors the *shape* of the
in-repo reference modules 09/10 (parent wiring, `quality` profile, config layout, reasoned-suppression
pattern), which are themselves book-original.

---

## Source trace (every fact → pin)

| Fact in the module | Traces to |
|---|---|
| Java release 21; `maven.compiler.release=21` | SOURCE-PIN §"Runtime baseline" — anchor JDK 21 (built on 21.0.11) |
| JUnit 6.0.3, AssertJ 3.27.7 | SOURCE-PIN §3 (inherited via aggregator `dependencyManagement`) |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, maven-checkstyle-plugin 3.6.0, spotbugs-maven-plugin 4.9.3.0 | SOURCE-PIN §2 (static analysis); same pins as modules 09/10 `quality` profile |
| `EI_EXPOSE_REP` / `EI_EXPOSE_REP2` (SpotBugs bug patterns) | SOURCE-PIN §2 SpotBugs; dossier `_RESEARCH.md` lines 145/193; **re-confirmed live** in `target/spotbugsXml.xml` (EI_EXPOSE_REP fires) |
| `java:S3776` (cognitive complexity, 15), PMD `NcssCount` (60) — cited, not run | dossier `_RESEARCH.md` lines 131/174/191; cited as the rule the smell's *shape* matches (the module does not wire Sonar/PMD) |
| Refactoring names: Extract Function, guard clauses (Replace Nested Conditional with Guard Clauses), Encapsulate Collection / defensive copy | Fowler *Refactoring* 2e (dossier back-matter); EJ Item 50 (defensive copy) |
| `record`, `List.copyOf`, `enum` (type-code retirement) | JDK 21 API (SOURCE-PIN §1); EJ Items 17 / 62 |

No fact in the module required a detail the dossier + pin lacked. **No `09-flags/` gap raised.**

> Note (pre-existing, not introduced here): the dossier `_VERIFY.md` records a repo-wide standing item
> B1 (the multi-authority pin existed as `TO-PIN` at research time). SOURCE-PIN.md is now pinned
> (✅ 2026-06-20) and the module's facts trace to it; the dossier's `⚠ verify at pin` markers on cited
> Sonar/PMD thresholds remain the drafter's concern, not the module's (the module runs Checkstyle +
> SpotBugs only and asserts nothing about those thresholds in code).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | SpotBugs does not flag `EI_EXPOSE_REP` on a record accessor at this version; first-built record form left the suppression dead | NOTE (resolved) | `OrderLeaky.java` | Rendered `OrderLeaky` as a plain final class with a hand-written getter; finding now fires and is genuinely suppressed (verified in raw XML) |
| 2 | `EI_EXPOSE_REP2` (store side) does not fire at threshold=Medium; initial comments implied both fire | NOTE (resolved) | javadoc / filter / README / back-matter | Reworded to state `EI_EXPOSE_REP` fires and `EI_EXPOSE_REP2` is the related store-side pattern |
| 3 | Module not in parent `<modules>` | NOTE (intended) | `08-companion-code/pom.xml` | Leave as-is; register only after CODE-REVIEW pass (Step 4b) |

## Blockers

**None.**

---

## Learnings & pipeline suggestions

- **SpotBugs + records blind spot (high-value, reusable):** SpotBugs 4.9.3.0 does **not** raise the
  `EI_EXPOSE_REP` / `EI_EXPOSE_REP2` family on a **record's** compiler-generated accessor or canonical
  constructor. Any chapter whose "leaking getter" counter-example must *actually trip SpotBugs* should
  render the leaky type as a **plain class with a hand-written getter**, not a record. (Module 10's
  record `OrderLeaky` carries a suppression that is effectively dead for the EI patterns — worth a
  follow-up note there.) Propose adding this to EXAMPLES-GUIDE / a SpotBugs gotchas list.
- **Verify the suppression is load-bearing, not decorative:** a green SpotBugs run with a suppression
  present does not prove the suppressed finding exists. Always re-run once with the filter emptied and
  confirm the `BugInstance` appears in `target/spotbugsXml.xml` before claiming "the tool reports it."
  Candidate check to script: warn on any `<Match>` in an exclude filter that suppresses zero findings.
- **Grep the report with the right quote style:** SpotBugs XML uses single-quoted attributes
  (`type='...'`); a `type="..."` grep silently finds nothing and can masquerade as "no findings."
- **Bounded display of an intentionally-long method:** to honour both the ≤9-line snippet ceiling and
  the "displayed slice is a region of the real file" rule, tag a tight representative slice of the long
  method (its nested branch) while leaving the method long in the file — the smell stays measurable,
  the snippet stays in-bounds.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
