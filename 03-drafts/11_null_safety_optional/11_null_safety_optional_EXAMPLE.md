# GATE REPORT — EXAMPLE-BUILD (key 11, Null-safety & Optional discipline)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 11 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `11_null_safety_optional`
- **Draft under review:** `03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `mvn -Pquality verify` + `extract_snippet.sh` + `check_snippets.sh`
- **Scripts run:** `check_snippets.sh`, `extract_snippet.sh` (per-tag), `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (the `./mvnw` wrapper / key-01 pilot is not yet latched; built with the repo toolchain `mvn` against the module pom, standalone)
- **Verdict:** **PASS**

---

## Verdict rationale

The Chapter 11 companion module builds green standalone with the `quality` gate (Checkstyle + SpotBugs)
on the pinned toolchain, all 16 tests pass, and every one of the seven declared snippet tags resolves
to a real, ≤9-line tag region in a compiled file (`check_snippets.sh`: 7/7 PASS). The module realizes
the draft's four-lever null-safety design and its "Companion module (spec)" back-matter without adding
behavior the prose does not claim. FLOOR C holds: both preconditions are logged below (Java 21.0.11 ≥
the Java 21 floor; `clean verify` = BUILD SUCCESS, warning-clean), and every fact traces to the dossier
+ SOURCE-PIN. No invented rule ID, GAV, version, flag, or API.

---

## FLOOR C guard — both preconditions (recorded)

- **(a) Runtime/toolchain meets the minimum.** `java -version` →
  `openjdk version "21.0.11" 2026-04-21` — meets the Java 21+ floor (21 LTS anchor). PASS.
- **(b) Build is GREEN.** Exact command (run from the module, standalone — parent pom NOT edited):
  ```
  mvn -B -Pquality -f 08-companion-code/11_null_safety_optional/pom.xml clean verify
  ```
  Result lines:
  ```
  Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
  You have 0 Checkstyle violations.
  BugInstance size is 0           (SpotBugs: No errors/warnings found)
  BUILD SUCCESS  (Total time ~3.5 s)
  ```
  Warning scan over the full run: **NO WARNINGS** (warning-clean). PASS.

Both preconditions hold → **FLOOR C verdict: PASS**.

---

## Module path & shape

- **Module path:** `08-companion-code/11_null_safety_optional/`
- **Self-contained, mirrors reference module 09:** own `config/checkstyle/checkstyle.xml` + own
  `config/spotbugs/spotbugs-exclude.xml` (copied from `09_api_method_contracts`, the book's own house
  ruleset), and its own `quality` profile block in the module pom (same two-pin Checkstyle-engine
  override + SpotBugs shape as 09).
- **Child of the ONE aggregator:** sets `<parent>` to `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`;
  carries no `<groupId>`/`<version>` literal and no BOM of its own; the only version literal is the
  single pinned `org.jspecify:jspecify:1.0.0` `provided` GAV (mirrors 09).
- **Registration deferred (correct):** the module is **NOT** in the parent `<modules>` list. Per the
  build-discipline rule, registration waits for green build **AND** the CODE-REVIEW gate. The parent
  `08-companion-code/pom.xml` was not modified.
- **Package:** `org.acme.storefront.pricing` (the draft spec's package; the book's shared storefront
  demo domain).

---

## Enterprise-grade checklist

| Requirement | How it is met | Where |
|---|---|---|
| Pinned dependency set via inherited parent | runtime + JUnit/AssertJ inherited from the aggregator; one pinned `jspecify:1.0.0` provided GAV; no loose versions | `pom.xml`, parent `pom.xml` |
| Externalized config profiles | `dev` / `prod` profiles in `pricing.properties`, selected by the `storefront.profile` system property; `%prod` leaves the default code empty → empty `Optional` (absence in the type, not null) | `PricingConfig.java`, `pricing.properties` |
| At least one integration test | 16 JUnit 5 + AssertJ tests over the full mechanism (happy path, empty-Optional contract, fail-fast guards, `@Nullable` opt-out, guarded vs unguarded contrast incl. JEP 358 message, both config profiles, type-use precision) | `DiscountServiceTest.java` |
| Test-harness setup | JUnit Jupiter via the inherited surefire/JUnit-BOM pins; no extra system properties needed; profile-dependent tests load the profile explicitly via `PricingConfig.load(profile)` so the run is deterministic | parent `pom.xml`, test |
| Observability / health surface | `discountsAppliedCount()` + `lookupsWithoutDiscountCount()` (present-vs-empty branch counters), `isReady()` readiness probe; DEBUG log on each branch | `DiscountService.java` |
| Explicit failure path (HONEST-LIMITATIONS in code) | `requireNonNull` boundary rejections (typed, named); the deliberate unguarded-`@Nullable` dereference in `BrokenCheckout` throws a JEP 358 helpful NPE naming the exact null expression — asserted in the test | `DiscountService`, `BrokenCheckout`, test |

---

## Tag-include regions (displayed snippets) — all ≤9 lines, all resolve

`check_snippets.sh 03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md` → **7 marker(s); 7 pass, 0 fail.**

| Tag | Resolved lines | Backing file |
|---|---|---|
| `nullmarked-package` | 4 | `.../pricing/package-info.java` |
| `optional-return` | 5 | `.../pricing/DiscountService.java` |
| `require-nonnull` | 5 | `.../pricing/DiscountService.java` |
| `nullable-return` | 3 | `.../pricing/DiscountService.java` |
| `unguarded-deref` | 3 | `.../pricing/BrokenCheckout.java` |
| `guarded-fix` | 4 | `.../pricing/Checkout.java` |
| `type-use-precision` | 5 | `.../pricing/TypeUsePrecision.java` |

All seven tags declared on the draft's "Snippet tags:" line are realized. (One extra interior region,
`optional-map`, exists in `DiscountService.java` for completeness but is not wired into the prose and is
not counted among the seven displayed snippets.)

### Marker insertion points in the draft (no prose deleted; locked third-person voice)

| Tag | Inserted after (prose anchor) | One-line lead-in added |
|---|---|---|
| `optional-return` | Lever 1, the `of`/`ofNullable` construction-paths paragraph | "The lookup done right returns the absence in its type…" |
| `require-nonnull` | Lever 2, the `requireNonNull` fail-fast paragraph | "The constructor guards every required collaborator the same way…" |
| `type-use-precision` | The "Declaration vs type-use" CONCEPT box | "In code the two placements are two different contracts…" |
| `nullmarked-package` | The JSpecify `@NullMarked` idiom paragraph | "Marking the package once is the whole gesture…" |
| `nullable-return` | Same JSpecify idiom paragraph (after `nullmarked-package`) | "Inside that scope, the few places null is the honest answer have to say so…" |
| `unguarded-deref` | Lever 3, the NullAway bullet ("dereferenced unguarded") | "The dereference such a checker rejects is the one below…" |
| `guarded-fix` | Immediately after `unguarded-deref` | "The fix gives the empty case a value instead of dereferencing it…" |

---

## Source trace (every fact → pinned authority / dossier)

| Atom in the module | Trace |
|---|---|
| `org.jspecify:jspecify:1.0.0`, pkg `org.jspecify.annotations`, `@NullMarked` / `@Nullable`, `TYPE_USE` | SOURCE-PIN.md §2; dossier §2.5 / §8 (GAV + semantics verified) |
| `@NullMarked` = non-null default within scope; `@Nullable` marks the exceptions | dossier §2.5; JSpecify spec |
| `Optional<T>` return-type design intent; `of`/`ofNullable`/`empty`/`map`/`flatMap`/`orElse`/`orElseGet` | dossier §2.1 / §2.7; Optional Javadoc SE 21 |
| never field/param/map-value; `map`/`orElse` not `get()` (Item 55) | dossier §2.2 (Effective Java 3e Item 55) |
| empty not null on the lookup miss (`Optional.ofNullable`) (Item 54) | dossier §2.2 (Effective Java 3e Item 54) |
| `Objects.requireNonNull(obj, msg)` fail-fast boundary guard | dossier §2.3; Objects Javadoc SE 21 |
| JEP 358 helpful NPE on by default at the anchor (no flag), names the exact null expression | dossier §2.4; JEP 358 (⚠ verbatim message NOT block-quoted — flagged in dossier; the module asserts only that the message *contains* the expression name, which is observed live, not quoted) |
| declaration vs type-use precision (`List<@Nullable String>` vs `@Nullable List<String>`) | dossier §2.5 + draft CONCEPT box; JSR 308 / JSpecify |
| toolchain pin Java 21 (build 21.0.11; forward-check 25) | SOURCE-PIN.md runtime baseline; parent pom |
| Checkstyle 10.26.1 / SpotBugs 4.9.3.0 / plugin versions | inherited shape from reference module 09 (Chapter 16 house ruleset) |

No fact in the module is absent from the dossier + pin. **No `UNVERIFIED` atom entered the module**, so
no new `09-flags/` entry was raised by this build. (The dossier's pre-existing flags — JEP 358 verbatim
text, EJ verbatim/pages, AHEAD-OF-PIN Spring/IntelliJ/Valhalla — are prose-side concerns; none is a
fact the module asserts, and the module deliberately avoids block-quoting any unverified text.)

---

## CAPTURE (Step 4c) — subject-native UI screenshots

**No captures planned at draft time for this build step.** The dossier §6 figure plan fixes two
*designed* diagrams (Fig 11.1 "four levers" layered defense; candidate Fig 11.2 "@NullMarked zone") —
authored as HTML and rendered to PNG separately (figure-designer's job, never image-generated, not this
agent's). The only candidate captured surface was a NullAway compile-error screenshot, marked
*optional* in the plan. NullAway is described by the chapter but is deliberately **not** wired into this
module's build (the `quality` profile is Checkstyle + SpotBugs, mirroring reference module 09), so there
is no live subject-native UI surface to capture from the running module. Wiring NullAway/Error Prone to
manufacture a screenshot would add a tool the module is not built around — an editorial signal to the
drafter/figure-designer if a captured NullAway-error figure is wanted, not a capture decision here.
Result: **no PNG/sidecar written under `05-figures/11_null_safety_optional/` by this step.**

---

## LEGAL-IP §5 — original-for-this-book confirmation (file-by-file)

Every source file in the module is original work written for this book's invented storefront-pricing /
promo-code domain. None is a copied-or-renamed upstream sample, getting-started skeleton, or
NOTICE/header boilerplate.

- `pom.xml`, `README.md` — original; pom structure follows the book's own reference module 09 (in-repo),
  not an upstream archetype.
- `package-info.java`, `Money.java`, `Discount.java`, `PromoCatalog.java`, `InMemoryPromoCatalog.java`,
  `DiscountService.java`, `Checkout.java`, `BrokenCheckout.java`, `TypeUsePrecision.java`,
  `PricingConfig.java`, `pricing.properties`, `DiscountServiceTest.java` — all original.
- `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml` — copied from the book's
  own reference module 09 (the shared house ruleset by design; in-repo, not third-party), as the task
  directed. Not an external sample.

No region is taken substantially verbatim from any specific upstream source file; no attribution row is
required. **LEGAL-IP §5: CONFIRMED.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | First three tag regions initially wrapped the full Javadoc and exceeded the 9-line ceiling (11/11/10 lines) | MINOR (resolved) | `DiscountService.java` `optional-return` / `require-nonnull` / `nullable-return` | Moved each `// tag::` marker below the Javadoc to wrap only the signature + body; re-verified at 5/5/3 lines |
| 2 | `List.copyOf` rejects null elements, breaking the `List<@Nullable String>` demonstration test | MINOR (resolved) | `TypeUsePrecision.java` ctor | Replaced with `Collections.unmodifiableList(new ArrayList<>(...))` for the nullable-element list; `List.copyOf` kept for the non-null-element list |
| 3 | NullAway not wired into the module build | NOTE | module `pom.xml` | Intentional — mirrors reference module 09; the chapter *describes* NullAway, the module shows the gap (SpotBugs stays green on the unguarded `@Nullable` deref) that a checker would close. Wiring NullAway is a possible future enhancement, flagged not done. |

---

## Blockers

**None.**

---

## Gate-specific checks

- [x] **EXAMPLE** — companion builds green via `mvn -Pquality verify` at the pin; all 7 displayed
  snippets resolve to real bounded (≤9-line) tag regions in compiled files; FLOOR C source-trace clean.
- [x] Module is a child of the ONE aggregator (`<parent>` set, no own version/BOM); registration to the
  parent `<modules>` list correctly deferred to post green-build + CODE-REVIEW.
- [x] Enterprise-grade: pinned deps, externalized profiles, integration test + harness, observability
  surface, explicit failure path — all present.
- [x] Realizes the draft; adds no behavior the prose does not claim.
- [x] Neutral voice in code/config comments and README (no banned phrasing; JSpecify/NullAway/Checker
  Framework each stated with case + limit, none crowned).
- [x] LEGAL-IP §5 original-for-this-book confirmed file-by-file.
- [ ] **CODE-REVIEW** — not run by this gate (Step 4b `code-reviewer` agent owns it; required before the
  module joins the parent `<modules>` list).

---

## Learnings & pipeline suggestions

- **Tag the code, not the Javadoc.** Wrapping the full Javadoc inside a `// tag::` region blows the
  9-line ceiling immediately. The reliable pattern (matching reference module 09) is to place the
  opening marker *below* the Javadoc so the displayed region is signature + body + at most one comment
  line. Recommend noting this in `EXAMPLES-GUIDE` as the default tag-placement rule.
- **`List.copyOf` vs nullable elements.** A `List<@Nullable String>` demonstration cannot defensively
  copy with `List.copyOf` (it NPEs on null elements) — `Collections.unmodifiableList(new ArrayList<>())`
  is the copy that tolerates the very nulls the type is meant to illustrate. A small but recurring trap
  for any null-safety / type-use example.
- **The "build stays green without a checker" point is demonstrable in code.** Structuring the broken
  path so the null arrives via an annotated parameter (not a provably-null local) lets the module
  compile and pass SpotBugs while still throwing at runtime — making the chapter's fourth-lever argument
  ("annotations alone do nothing; a checker closes the gap") concrete rather than asserted. Reusable
  shape for any "static analysis misses this without tool X" chapter.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 11 gate-run PASS
```
