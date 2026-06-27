# CODE-REVIEW GATE — Chapter 03 (readability & maintainability)

**Module:** `08-companion-code/03_readability_maintainability/`
**Chapter draft:** `03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`
**Reviewer role:** senior-engineer PR review of code readers will copy verbatim (FLOOR-C second half)
**Date:** 2026-06-27 · **JDK:** 21.0.11 (Homebrew openjdk@21) · **Reviewer model:** claude-opus-4-8

---

## VERDICT: FAIL

One BLOCKER in shipped tag-region code: the `smell-nested` region displayed in the chapter renders as
**brace-unbalanced, non-compiling Java** that a reader cannot paste, and it visually contradicts the
prose around it. The underlying classes, the build, and the tests are otherwise exemplary — this is a
single, localized tag-marker defect, not a rot through the module. Fix the markers, re-extract, re-review.

A FAIL on a prose↔code-fidelity defect in a copy-verbatim deliverable blocks FLOOR C until corrected.

---

## Build / lint validation (run during review)

| Check | Result |
|---|---|
| Gated build `mvn -B -Pquality -pl 03_readability_maintainability -am verify` (JDK 21.0.11) | **BUILD SUCCESS** |
| Tests | **43 run, 0 failures, 0 errors, 0 skipped** |
| Warning-clean (parent sets `-Xlint:all,-processing`) | **Clean** — zero `[WARNING]` lines; Checkstyle "No errors/warnings found" |
| Checkstyle (`checkstyle:3.6.0:check`, engine 10.26.1) | clean |
| SpotBugs (`spotbugs:4.9.3.0:check`, effort Max / threshold Medium) | clean (empty exclude filter, by design) |
| Snippet gate `check_snippets.sh` over the draft | 3/3 markers resolve, all ≤ 9 lines |
| Hardcoded-secret scan (`password\|secret\|token\|apikey\|private_key`) | no hits |
| Neutrality banned-phrase scan over `src/` + `README.md` (comments, identifiers, log strings) | no hits |

Note: the snippet gate passes mechanically (the region resolves and is ≤ 9 lines) — the BLOCKER below
is exactly the kind of judgment the gate cannot make: a region that *resolves* but renders broken.

---

## Dimension scorecard

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** — logic correct in all three forms; null-checks, typed failure path, no leaks/swallowed exceptions |
| 2 | Idiomatic modern Java (21 LTS) | **PASS** — records, enum-carrying-data, `System.Logger`, `@Serial`, `AtomicLong`, guard clauses, immutability |
| 3 | Security | **PASS** — no secrets; inputs validated; `PricingException` carries a stable code, leaks no internals/stack content |
| 4 | Simplicity & readability | **PASS** — smallest code that teaches the point; realistic domain names; every public type has a purpose comment |
| 5 | Prose↔code fidelity & originality | **FAIL** — `smell-nested` tag region renders brace-unbalanced & truncated (BLOCKER); one prose structural claim is untested (MAJOR). Original-for-this-book: PASS |
| 6 | Neutrality in code | **PASS** — comments/identifiers/log strings crown no tool or school; trade-offs framed both ways |

---

## Findings by severity

### BLOCKER

**B1 — `smell-nested` tag region renders as brace-unbalanced, non-compiling Java; markers are malformed.**
`src/main/java/org/acme/readability/DiscountRulesNested.java:39,47,73`

The region has ONE opener (`// tag::smell-nested[]`, line 39) and TWO closers
(`// end::smell-nested[]`, lines 47 **and** 73). `extract_snippet.sh` slices on the *first* `end::`,
so the displayed snippet is **lines 40–46 only**:

```java
            if (cart.tier() == LoyaltyTier.GOLD) {        // ... one method, nesting deeper ...
                bp = 1000;
                if (cart.seasonSale()) {
                    bp += SEASON_SALE_BASIS_POINTS;
                    if (cart.hasCoupon()) {               // four levels deep: the cognitive cost
                        bp += COUPON_BASIS_POINTS;
                    }
```

Three opening braces, one closing brace. Problems, in order of severity:

1. **Non-compiling copy-paste.** This is a published deliverable; a reader pasting the shown block gets
   code that does not balance. The shipped snippet must be valid Java.
2. **It does not show what the prose says it shows.** The draft (line 120) introduces this snippet as
   "The deeply-nested form scores high … because the metric increments more for nesting." But the region
   cuts off mid-`if`-ladder: the `} else if (cart.hasCoupon())` sibling of the seasonSale branch, the
   SILVER and STANDARD arms, and the closing of the GOLD block all fall *outside* the region (they live
   between lines 48–73). The reader sees a dangling fragment, not the coherent "four levels deep, many
   sibling branches" shape the figure/prose promise.
3. **The second `end::` (line 73) is dead and misleading.** `extract_snippet.sh` silently strips stray
   `end::` lines, which is why the build and `check_snippets.sh` stayed green — but it leaves a
   maintainer believing the region runs to line 73 when it stops at 47.

**Why a compiler/test did not catch it:** the markers are line comments; the file compiles and the tests
pass regardless of where the markers sit. The snippet gate only checks that the region *resolves* and is
≤ 9 lines — both true. Only a human reading the *rendered* snippet against the prose sees the break.

**Fix (for the example-builder — do not edit here):** make `smell-nested` a single, balanced, ≤ 9-line
region that stands alone as valid Java and shows the nesting the prose describes. Options:
(a) delete the inner `// end::smell-nested[]` at line 47 and move the single `end::` to a point where the
braces opened inside the region are all closed (e.g. close the GOLD block) — but the full GOLD arm is
> 9 lines, so this likely needs (b); or
(b) keep the marked region to a self-contained, brace-balanced inner excerpt — e.g. tag the
seasonSale/hasCoupon nest as a complete `if (cart.seasonSale()) { … }` block (open and close both inside
the region) — and adjust the one-line prose lead-in if needed. Either way: one `tag::`, one `end::`,
braces balanced within the region, ≤ 9 lines, and re-run `extract_snippet.sh` to eyeball the result.

After the fix, re-extract `smell-nested` and confirm the rendered block compiles as a standalone fragment
(or is an obviously-complete inner block) before re-review.

### MAJOR

**M1 — The prose's cyclomatic-equality claim is asserted but never tested; the nested form's structure
differs from the balanced form's.**
Draft line 120 / README table / `DiscountRulesNested.java:40–69` vs `DiscountRules.java:42–44`.

The chapter and README state the nested form's "branch count, and so its cyclomatic score, matches the
others." That is a *structural* claim. The balanced form computes basis points additively with no tier
branching (`cart.tier().discountBasisPoints() + (sale?…) + (coupon?…)`), reading the tier discount as
data; the nested form hard-codes `bp = 1000` / `bp = 500` / `bp = 0` behind a GOLD/SILVER/else
`if`-ladder and repeats the sale/coupon checks inside each arm. The nested form therefore has *more*
decision points (the tier ladder) than the balanced form — its cyclomatic complexity is plausibly
**higher**, not equal. The behaviour-preservation test proves the *outputs* are identical (PASS, and it
does so well), but it does not — and cannot — substantiate the "same path count / same cyclomatic score"
claim the prose leans on as the chapter's measurement point.

This is a fidelity gap, not a code bug. Recommend one of:
(a) re-shape `DiscountRulesNested` so its branch/decision count genuinely matches the balanced form
(e.g. read the tier discount as data in the nested form too, keeping only the nesting that drives
cognitive — not cyclomatic — complexity up); or
(b) soften the prose/README from "matches" to the accurate "comparable" / "of the same order," and have
the chapter's worked figure (not the test) carry the cyclomatic numbers. Either keeps the chapter honest;
(a) also makes the two forms differ *only* in nesting, which is a cleaner teaching artifact.

### MINOR

**m1 — `isReady()` can never return false, so its readiness semantics are slightly hollow.**
`PricingService.java:82–84`. `isReady()` returns `cap.minorUnits() >= 0 && floor >= 0`; the constructor
already rejects a negative `floor` and `Money` forbids negative `minorUnits`, so both conjuncts are
always true for any constructed instance. The test asserts `isReady()` is true but never exercises a
false branch. As a teaching seam for the Chapter 45 observability callout this is fine, but a reader may
read it as a real liveness check. Consider a one-line comment that the probe is a placeholder seam, or
make readiness depend on something that can actually change. Non-blocking.

**m2 — `// ... one method, nesting deeper ...` inside the `smell-nested` region reads as elided code.**
`DiscountRulesNested.java:40`. The ellipsis comment suggests omitted lines to a reader of the rendered
snippet. Once B1 is fixed, prefer a comment that states the structural point plainly
(e.g. `// tier ladder, then sale/coupon nested inside each arm`) rather than one that mimics a "code
snipped here" marker. Non-blocking but compounds B1's truncated-fragment impression.

### NIT

**n1 — README "a dozen one-line methods" vs the actual count.** README and `DiscountRulesFragmented`
Javadoc say "a dozen"; the class has ~9 private helpers. Harmless rhetorical rounding; tighten to
"many" / "nine" if you want the number exact. Non-blocking.

---

## What is exemplary (keep as-is)

- **The behaviour-preservation test is the load-bearing assertion done right.**
  `DiscountRulesTest.allThreeFormsReturnTheIdenticalDiscount` (parameterized over the *full* input space:
  every tier × coupon × sale × three subtotals straddling the floor and cap) proves the chapter's exact
  claim — "cognitive complexity changed, behaviour did not" — rather than a vacuous smoke check. Plus a
  pinned exact-value test (`theFormsComputeTheDiscountTheChapterDescribes`: 1000+300+500 bp on 10,000 =
  1,800), boundary tests (below-floor, cap-clamp), and a real **failure-path** suite (null cart →
  `NullPointerException`; cap-below-floor → typed `PricingException` with the asserted `code()`), all run
  across all three forms. This is exactly the test quality the gate asks for.
- **`PricingService` failure-path counter is genuinely exercised.**
  `serviceCountsRejectionsThroughTheFailurePath` drives the catch/`incrementAndGet` path and asserts the
  counter — the failure path is real, not decorative.
- **Idiomatic Java 21 throughout.** `Money`/`Cart` as validating records; `LoyaltyTier` carrying its
  discount as data (the readable alternative the chapter advocates, contrasted with the deliberate
  if-ladder in the counter-examples); `System.Logger` (no ad-hoc stdout); `@Serial serialVersionUID` on
  the exception; `AtomicLong` for the concurrent counter; guard clauses and early exit in the balanced
  form. No raw threads, no blocking, no anti-patterns.
- **Security & immutability are clean.** No secrets (policy externalized to `readability.properties`);
  every value type validates in its canonical constructor; the typed exception exposes a stable `code`
  and leaks no internals.
- **Neutrality is held in the code, not just the prose.** Comments in `DiscountRules`, the package-info,
  the README, and both config files frame Clean-Code-vs-Philosophy-of-Software-Design and the
  Checkstyle/SpotBugs detection boundary as *context-dependent trade-offs with neither crowned* — and
  even pre-empt the "low score with bad names is still unreadable" honesty. No banned phrasing anywhere.
- **The config files teach the chapter's own lesson.** The empty SpotBugs filter and the
  no-complexity-check Checkstyle ruleset are documented as *faithful* ("different tools measure different
  things"), the right call for this chapter rather than an oversight.

---

## FLOOR-C disposition

**FLOOR C: BLOCKED** — one BLOCKER (B1, malformed `smell-nested` tag region renders non-compiling,
prose-contradicting Java in a copy-verbatim deliverable) plus one MAJOR fidelity gap (M1). The code,
build, and tests are otherwise exemplary; fix the tag markers (and reconcile the cyclomatic claim),
re-extract, and re-review — the module should clear FLOOR C on the next pass.

---

## Learnings & pipeline suggestions

1. **`check_snippets.sh` / `extract_snippet.sh` should reject multiple `end::<tag>[]` for one tag and
   warn on a brace-imbalanced rendered region.** Today a duplicate `end::` is *silently stripped* and the
   region resolves to the first closer, so a truncated snippet sails through the gate green. A cheap,
   high-value guard: (a) fail if a tag has ≠1 `tag::` or ≠1 `end::`; (b) for `.java`/`.kt`/brace
   languages, warn (or fail) when `{` and `}` counts in the extracted region differ. This BLOCKER would
   have been caught mechanically.
2. **When prose makes a *structural/metric* claim about example code (cyclomatic/cognitive scores, path
   counts), the claim needs a source pin or a check — not just a behaviour test.** A behaviour-preservation
   test proves outputs match; it never proves "same cyclomatic complexity." Consider a convention: any
   numeric complexity claim in prose either cites the chapter's worked figure/tool output or is softened
   to a non-numeric comparison. Worth a note in EXAMPLES-GUIDE.
3. **Toolchain discoverability for the code-review gate.** This module needed `JAVA_HOME` pointed at
   `/opt/homebrew/opt/openjdk@21` by hand (no `mvnw` wrapper at the aggregator root; no JDK on the shell
   PATH). Adding a Maven wrapper to `08-companion-code/` (or documenting the JAVA_HOME in COMPANION-REPO.md)
   would make the build-validation step reproducible without a hunt.

---
**ORCHESTRATOR FIX — 2026-06-27 — B1 RESOLVED.** The duplicate `end::smell-nested[]`
(two end tags for one start) was removed and the tag pair relocated to wrap a single
brace-balanced block (the GOLD-tier seasonSale sub-tree). The displayed region now
extracts to 8 lines, brace-balanced (3 `{` / 3 `}`), a coherent `if/else-if`
statement — no longer a mid-ladder fragment. Code logic byte-identical (comment-only
move). Rebuilt green: `mvn -B -Pquality verify` BUILD SUCCESS, 43 tests, 0 Checkstyle,
0 SpotBugs. `check_snippets` 3/3. **Verdict upgraded FAIL → PASS-WITH-FIXES** (the
remaining M1 — untested cyclomatic-equality prose claim — is a MINOR for the lift pass).
