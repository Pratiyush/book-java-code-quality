# GATE REPORT — EXAMPLE-BUILD (key 08, Effective Java — The Canon, Dated)

## Header

- **Gate:** EXAMPLE-BUILD
- **Chapter key:** 08 (folds 13) — `01-index/FINAL_INDEX.md` Ch 5, opens Part II
- **Slug:** `08_effective_java`
- **Draft under review:** `03-drafts/08_effective_java/08_effective_java_v1.md`
- **Module path:** `08-companion-code/08_effective_java/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×6), `check_snippets.sh`
- **Build-state marker:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** — Floor C: **PASS**

---

## Verdict rationale

The module is self-contained (own `config/` + own `quality` profile, mirroring the reference module
`09_api_method_contracts/`), builds GREEN at the pin with 0 Checkstyle violations and 0 SpotBugs
findings, and every one of the six designed snippet tags resolves to a ≤9-line region via
`check_snippets.sh`. Every fact traces to SOURCE-PIN (JEP/JLS primaries + the named book dated as
secondary). Both FLOOR-C preconditions hold and are logged below. No invented atom; no banned phrasing.

---

## FLOOR C guard — preconditions (both required)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime ≥ Java 21 | `java -version` → `openjdk version "21.0.11" 2026-04-21` (Homebrew) | PASS |
| (b) `verify` GREEN | `mvn -B -Pquality -f 08-companion-code/08_effective_java/pom.xml clean verify` → `BUILD SUCCESS` | PASS |

Maven: `Apache Maven 3.9.16` (matches SOURCE-PIN §4). Build line (exact):

```
mvn -B -Pquality -f 08-companion-code/08_effective_java/pom.xml clean verify
→ Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0
→ BUILD SUCCESS
```

> Build run standalone, as instructed. `08-companion-code/pom.xml` was NOT edited; the module is not
> yet in the aggregator `<modules>` list and joins the reactor only after the CODE-REVIEW gate.

---

## Snippet tags (designed for this chapter — the chapter pre-declared none)

Six load-bearing idioms the prose actually shows were chosen from the draft's hook, "features briefly"
list, deep-dive folklore paragraph, and "When to use" standing-principles bullet, and seeded by the
dossier §6 tag plan (`ej-record-immutable` / `ej-sealed-switch` / `ej-handrolled-contrast`).

| Tag | File | Idiom | Verdict shown | Lines | Resolves |
|---|---|---|---|---|---|
| `handrolled-contract` | `LegacyPoint.java` | hand-written `equals` (Items 10/11) | the boilerplate records retired | 9 | PASS |
| `record-value` | `Point.java` | the one-line record (JEP 395) | Served by a feature | 2 | PASS |
| `record-invariant` | `Temperature.java` | compact-constructor invariant (Items 49/17) | Served, not retired | 5 | PASS |
| `enum-singleton` | `PricingPolicy.java` | single-element enum singleton (Item 3) | Stands | 7 | PASS |
| `sealed-types` | `Shape.java` | sealed interface of permitted types (JEP 409) | Reinforced | 7 | PASS |
| `pattern-switch` | `Areas.java` | exhaustive pattern-matching switch (JEP 441) | Reinforced | 7 | PASS |

`check_snippets.sh 03-drafts/08_effective_java/08_effective_java_v1.md` → **6 marker(s); 6 pass, 0 fail.**

---

## Marker insertion points (in the draft prose — no prose deleted; locked third-person voice)

| Marker | Insertion point | Lead-in added |
|---|---|---|
| `handrolled-contract`, `record-value` | Hook, just after the `record Point` one-liner fence | one line tying the companion's two forms to the PR story |
| `record-invariant` | Deep dive — "records make immutability obsolete" paragraph, after "do not retire the principle" | one clause naming the temperature carrier |
| `sealed-types` | "The features, briefly" — after the **Sealed types** bullet | none (marker only) |
| `pattern-switch` | "The features, briefly" — after the **Pattern matching for `switch`** bullet | none (marker only) |
| `enum-singleton` | "When to use" — after the standing-principles bullet | one clause naming the Item-3 enum singleton as a standing idiom |

A "Snippet tags: …" line and an updated File list / BUILD STATUS were written into the draft's
`RUNNABLE EXAMPLE SPEC` block at the foot so the record matches the module.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Child of the ONE aggregator, no own version literal / BOM | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no `<groupId>`/`<version>`; no BOM import (inherits JUnit-BOM + AssertJ from parent `dependencyManagement`) |
| Pinned dependency set via inherited property | runtime `maven.compiler.release=21`, JUnit/AssertJ versions all set once in the aggregator; module adds zero version literals; JDK-only runtime |
| Self-contained like the reference module | own `config/checkstyle/checkstyle.xml` + `config/spotbugs/spotbugs-exclude.xml` (copied from `09`, comment tailored) + own `quality` profile in `pom.xml` |
| Externalized config / profiles | the `quality` static-analysis gate is an externalized `-Pquality` profile reading config under the module's `config/` dir (the chapter is a concept/code-craft chapter; the "config" surface here is the analysis profile, not a `%dev`/`%prod` runtime app) |
| At least one integration test + harness setup | `CanonIdiomsTest` — 7 tests (JUnit Jupiter via the inherited JUnit-BOM; AssertJ); harness needs no extra system properties; surefire auto-detects the JUnit Platform provider; confirmed green |
| Observability / health surface | `CanonDemo` logs each idiom's canonical `toString` (Item 12) via `java.lang.System.Logger` — the self-describing-value seam the later observability chapter builds on |
| Explicit failure path | `Temperature`'s compact constructor throws `IllegalArgumentException` on a sub-zero value (Items 49/72); `LegacyPoint` rejects a null label fail-fast — both demonstrated by a test, not asserted |
| Compiler-checked canon (chapter's TRY-IT) | `Areas.of` is an exhaustive `switch` with no `default` over the sealed `Shape`; adding a permitted type without a case is a compile error |

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file. Every `src/` file is original work written for this book in the storefront
companion domain (`org.acme.canon`): the idioms are generic Java language idioms (records, sealed
types, enum singleton, defensive validation), not copied from *Effective Java*'s code or any upstream
sample. No upstream header/`@author`/copyright/`NOTICE` boilerplate is present (scan clean). No
verbatim Bloch text is reproduced — the book is cited by Item number+title only, as a dated secondary
authority (SOURCE-PIN §7). The only copied artifacts are the sibling module's `config/` files and the
`quality` profile shape, which is the intended SELF-CONTAINED companion convention (reference module
`09_api_method_contracts/`), not third-party content. No short region is taken substantially verbatim
from a source file, so no per-region attribution is owed.

---

## Source trace (every atom → pin)

| Atom in the module | Traces to |
|---|---|
| Records are a final feature at JDK 16 | JEP 395 (SOURCE-PIN §1, JEP index) |
| Sealed classes final at JDK 17 | JEP 409 |
| Pattern matching for `switch` final at JDK 21 | JEP 441 |
| Record patterns final at JDK 21 (used in `Areas`) | JEP 440 |
| Enum singleton idiom (Item 3); equals/hashCode/toString contracts (Items 10/11/12); minimize mutability (Item 17); minimize accessibility (Items 15/16); check parameters (Item 49); favor standard exceptions (Item 72) | *Effective Java* 3e (2018), SOURCE-PIN §7 — secondary, dated per the Canon rule |
| JUnit Jupiter / AssertJ coordinates | SOURCE-PIN §3 (JUnit 6 line via aggregator BOM; AssertJ 3.27.7) |
| Checkstyle 10.26.1 engine, plugin 3.6.0; SpotBugs plugin 4.9.3.0; compiler 3.13.0; surefire 3.5.6; Maven 3.9.16 | aggregator `pom.xml` + module `quality` profile (consistent with the pinned tool rows, SOURCE-PIN §2/§4); exact build patches re-confirmed at this FLOOR-C build |

No fact required by the module was absent from the dossier + pin; **no `09-flags/` gap raised** by this
build. (The pre-existing `09-flags/08_structured_concurrency_ahead_of_pin.md` is untouched and out of
scope — structured concurrency is deliberately NOT demonstrated in this module, consistent with the
AHEAD-OF-PIN policy.)

---

## CAPTURE — subject-native UI screenshots

**No captures planned.** The chapter's figure plan (dossier §6, GUIDELINES §8) is ~1–2 designed
conceptual diagrams (fig08_1, fig08_2) and explicitly **no UI screenshots** — "this chapter exercises
no tool UI." Designed diagrams are authored separately (HTML → render.mjs) and are not this gate's job.
Nothing written to `05-figures/08_effective_java/` by this gate.

---

## Findings

| # | Severity | Location | Finding | Fix |
|---|---|---|---|---|
| 1 | INFO | `config/spotbugs/spotbugs-exclude.xml` | Empty filter by design (every idiom is the canon read correctly; no suppression needed) — mirrors module 09's "the empty filter is the point" stance | none — intentional |
| 2 | INFO | module `pom.xml` | "Externalized config" is realized as the `-Pquality` analysis profile, not a `%dev`/`%prod` runtime profile, because this is a concept/code-craft chapter with no runtime app surface | none — appropriate to the chapter class |

No HIGH/MED findings. No blocking issues.

---

## Verdict

**PASS.** Floor C (SOURCE-TRACE + COMPILE) **PASS**: runtime ≥ 21 (21.0.11) and `verify` GREEN
(warning-clean, 0 Checkstyle, 0 SpotBugs, 7 tests), all six tags resolve ≤9 lines, every atom traces
to the pin, LEGAL-IP §5 confirmed original-for-this-book. The module awaits the CODE-REVIEW gate
(Step 4b) before being registered in the aggregator `<modules>` list.

---

## Learnings & pipeline suggestions

- **Designing tags from a spec works cleanly when the dossier seeds the names.** The dossier §6 tag
  plan (`ej-record-immutable` etc.) gave the right idioms even though the chapter pre-declared no tags;
  mapping each dossier-named idiom to a ≤9-line region was mechanical. Suggest the drafter, for
  concept/canon chapters, always carries a dossier "displayed-snippet tie" table even when the chapter
  body shows no fenced code — it makes Step-4b tag design deterministic.
- **The 9-line ceiling bites `equals`/`hashCode` bodies.** A correct hand-written `equals` with
  `@Override` is 10 lines; moving the `// tag::` marker below the annotation (keeping `@Override` on
  the method, off the displayed region) is the clean fix and reads correctly as a snippet. Worth a note
  in EXAMPLES-GUIDE: annotations may sit outside the tagged region to keep contract-method snippets
  under the ceiling.
- **A "Stands" idiom needs a deliberate prose home for its marker.** Idioms the canon-dating *table*
  asserts (e.g. Item 3 enum singleton) may have no body-prose discussion; a one-line lead-in bullet in
  "When to use" is the lowest-risk place to anchor the include without inventing behavior. Confirms the
  guidance that a marker may carry a single-line lead-in.
