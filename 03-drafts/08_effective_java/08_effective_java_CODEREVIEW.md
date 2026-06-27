# GATE REPORT — CODE-REVIEW (key 08, Effective Java — The Canon, Dated)

## Header

- **Gate:** CODE-REVIEW (FLOOR-C second half)
- **Chapter key:** 08 (folds 13) — `01-index/FINAL_INDEX.md` Ch 5, opens Part II
- **Slug:** `08_effective_java`
- **Draft under review:** `03-drafts/08_effective_java/08_effective_java_v1.md`
- **Module path:** `08-companion-code/08_effective_java/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Scripts run:** manual tag-region count; include↔tag cross-map; secret/neutrality/placeholder greps; JEP-version cross-check. (Build NOT re-run — see Build/lint note.)
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21 and teaches its point cleanly: the six displayed tag
regions are all real, balanced, and within the 9-line cap; every JEP number / "final in JDK NN"
claim in code traces to SOURCE-PIN; there are no secrets, no banned phrasings, no swallowed
exceptions, no placeholders, and the failure path is real and test-driven. It does **not** FAIL —
no security, neutrality, or invented-fact finding exists. Verdict is PASS-WITH-FIXES purely on
polish in one illustrative method (`PricingPolicy.roundUpToMajorUnit`): a published-deliverable
reader will copy it, and its contract (non-negative input, divide-by-zero, long-overflow) is
under-stated. The listed MINOR fixes are required before approval; none blocks FLOOR C.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **FIX** — logic correct for the demonstrated cases; edge-case contract of `roundUpToMajorUnit` under-specified (findings 1–3) |
| 2 | Idiomatic Java / Java 21 | **PASS** — records, sealed interface, exhaustive record-pattern switch, compact constructor, single-element enum, `System.Logger`, `instanceof` pattern in `equals` — all canonical |
| 3 | Security | **PASS** — zero secrets/credentials; no injection sink; no internals leaked; not a public-endpoint surface |
| 4 | Simplicity & readability | **PASS** — smallest code that teaches each idiom; every public type carries a one-line purpose Javadoc; realistic names; no dead code/unused deps |
| 5 | Prose↔code fidelity + originality | **PASS** — 6/6 includes map 1:1 to tags; all JEP/version atoms trace to pin; original-for-this-book (one NIT, finding 4) |
| 6 | Neutrality in code | **PASS** — no crowning/disparagement in any comment, identifier, log string, or test name |

---

## Findings

Severity: **BLOCKER** (gate cannot pass) · **MAJOR** (must fix before approval) · **MINOR** (should fix; polish) · **NOTE** (no action).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `roundUpToMajorUnit` throws `ArithmeticException: / by zero` when `minorUnitsPerMajor == 0`, with no documented precondition. For copy-paste production code the contract should be explicit. | MINOR | `PricingPolicy.java:19-22` (`roundUpToMajorUnit`) | Document the precondition in Javadoc (`minorUnitsPerMajor` must be > 0) and/or guard with `if (minorUnitsPerMajor <= 0) throw new IllegalArgumentException(...)` — matching the Item-49 "check parameters for validity" discipline the module itself teaches. |
| 2 | Negative `minorUnits` does not produce a true round-up-to-multiple: `(-1, 100)` returns `100`, not the ceiling-to-multiple `0`. Javadoc says "Rounds a price ... up" without stating non-negative scope. | MINOR | `PricingPolicy.java:19-22` | State in Javadoc that `minorUnits` is a non-negative price (the realistic domain), or compute `Math.ceilDiv`/`Math.floorMod`-based rounding if negatives must be supported. Add one assertion for the boundary if scope is restated. |
| 3 | `minorUnits + (minorUnitsPerMajor - remainder)` can silently overflow `long` near `Long.MAX_VALUE`. | MINOR | `PricingPolicy.java:21` | Either note the bounded domain in Javadoc (minor-unit prices are far from `Long.MAX_VALUE`) or use `Math.addExact(...)` to fail loudly. Lowest-effort acceptable fix: the Javadoc note. |
| 4 | Test name `handWrittenAndRecordValuesAreObservablyEquivalent` asserts only the **record** side's `toString` (`x=3, y=4, label=p`); it never asserts `LegacyPoint`'s accessors/`toString` carry the same observable values, so "equivalent" is shown one-directionally. Not vacuous (component accessors are cross-compared), but the name promises slightly more than the body checks. | MINOR (NIT) | `CanonIdiomsTest.java:24-32` | Add `assertThat(legacy.toString()).contains("x=3", "y=4", "label=p")` (and/or assert `legacy.x()/y()/label()` directly) so both forms are shown observably equal, matching the hook's "both forms side by side" claim. |
| 5 | Module declares **no** `%dev`/`%prod` config profile (EXAMPLES-GUIDE req. 2) and no health/metrics surface (req. 4). For this JDK-only canon module both are genuinely N/A — there is nothing to externalize and no service to instrument — but req. 2 is listed "unconditional" in the guide. | NOTE | `08-companion-code/08_effective_java/` (no `src/main/resources`) | No code change. Confirm with the example-builder that the README's scoped-out rationale (it does record "Zero runtime dependencies — JDK only") satisfies §1.2; this is a guide-interpretation item, not a code defect. EXAMPLE-BUILD already PASSed it. |
| 6 | Tool versions in this module (Checkstyle 10.26.1, SpotBugs 4.9.3.0, JUnit 6.0.3) trail SOURCE-PIN (Checkstyle 13.6.0, SpotBugs 4.10.2, JUnit 6.1.0). This is set at the parent aggregator and is identical across **all** sibling modules — a repo-wide pin-reconciliation matter owned by the aggregator/SOURCE-PIN, not introduced or controllable by this chapter's deliverable. | NOTE | `08-companion-code/pom.xml:63,75` (parent); this module inherits | No change in this module. Flag to the SOURCE-PIN reconciliation / book-maintainer to align the aggregator pins repo-wide; out of scope for this chapter's CODE-REVIEW. |

---

## Blockers

**None.** No BLOCKER-severity finding. No security, neutrality, or invented-fact finding. FLOOR C is
not blocked by this gate.

---

## What is exemplary (worth keeping verbatim)

- **`Areas.of` (`Areas.java:31-37`)** — a flat, exhaustive record-pattern switch over the sealed
  `Shape` with **no `default`**, and the README + class Javadoc both explain *why* the missing
  `default` is deliberate (it preserves the compile-time exhaustiveness check). This is the single
  best teaching artifact in the module.
- **`Temperature` compact constructor (`Temperature.java:21-25`)** — the chapter's "records serve,
  not retire, the immutability principle" thesis made real: the invariant is enforced at
  construction and the rejection is a genuine, test-driven failure path (Item 49 + Item 72).
- **`LegacyPoint` ↔ `Point` pairing** — the hand-rolled value class and its one-line record twin
  side by side, with `equals` using the `instanceof` pattern and `Objects.hash`/`requireNonNull`;
  the contrast *is* the chapter's hook and it is honest (the hand-written form is shown correct, not
  strawmanned).
- **`System.Logger` over `System.out` (`CanonDemo.java`)** — the module dogfoods the book's own
  observability advice with the JDK-native logger and parameterized messages; the caught
  `IllegalArgumentException` is logged, not swallowed.
- **Empty SpotBugs filter with a written rationale (`spotbugs-exclude.xml`)** — zero suppressions,
  and a comment explaining the file is kept empty so a future *reviewed* suppression has a home.
  Mirrors the book's Chapter-16 "suppress with a reason" discipline.
- **Tag hygiene** — 6/6 `// tag::` regions are balanced, ≤9 lines (largest = `handrolled-contract`
  at exactly 9), and every draft `<!-- include: ... -->` resolves 1:1 to a real region. Drift is
  structurally impossible.

---

## Build / lint result

- **Green + warning-clean: relied on EXAMPLE-BUILD evidence; NOT independently re-run.** No JDK is
  available in this review shell (`/usr/bin/java` and `/usr/libexec/java_home` find no runtime; no
  `mvnw` wrapper exists in the tree). The EXAMPLE-BUILD gate report
  (`03-drafts/08_effective_java/08_effective_java_EXAMPLE.md`) records `mvn -B -Pquality -f
  08-companion-code/08_effective_java/pom.xml clean verify` → **BUILD SUCCESS**, 7 tests / 0 failures,
  **0 Checkstyle violations, 0 SpotBugs findings** at JDK 21.0.11 / Maven 3.9.16 (2026-06-26).
- **Strict warnings.** Parent aggregator sets `-Xlint:all,-processing` (`08-companion-code/pom.xml:108-110`);
  the EXAMPLE run surfaced no warning. No warning-as-FIX finding on the recorded run.
- **Static analysis.** `-Pquality` runs Checkstyle (curated house ruleset, `violationSeverity=error`,
  test sources included) + SpotBugs (`effort=Max`, `threshold=Medium`); both clean with an empty,
  reasoned exclude filter.
- **Test coverage of behavior.** 7 tests cover every public behavior incl. the failure path:
  contract equality (legacy + record), null-component fail-fast (`LegacyPoint`), the sub-zero
  `Temperature` rejection (the explicit failure path), the enum singleton, and each sealed variant
  through the switch. The failure-path tests exercise the real throw, not a stub.

> Disposition note: re-running `verify` once a JDK is on PATH is advisable for full independence, but
> the prior gate's recorded green + this review's static analysis are sufficient for the verdict.

---

## FLOOR-C disposition (one line)

**FLOOR C — CODE-REVIEW half: PASS (PASS-WITH-FIXES).** No BLOCKER and no security/neutrality/invented-fact finding; four MINOR polish fixes (findings 1–4) to apply before approval, then re-confirm — FLOOR C is not blocked.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness (FIX, non-blocking) / idiomatic PASS / security PASS / simplicity PASS / prose-code-fidelity PASS / neutrality-in-code PASS.
- [x] Every displayed snippet resolves to a real ≤9-line `// tag::` region in a compiling file (6/6).
- [x] No hardcoded secret/credential anywhere in the tree.
- [x] At least one test per public behavior **including the failure path**.
- [x] No banned NEUTRALITY phrasing in any comment, identifier, log string, or test name.
- [x] Originality — every file original-for-this-book; no unattributed verbatim lift.

---

## Learnings & pipeline suggestions

- **Illustrative "policy" methods deserve the same Item-49 discipline the module preaches.**
  `roundUpToMajorUnit` is the one spot where the module teaches a rule (check parameters for
  validity) it does not fully follow on its own demo method (no zero-divisor guard, undocumented
  non-negative scope). Worth a one-line note in EXAMPLES-GUIDE: a demo method that exists to show an
  idiom should still state its preconditions, because readers copy it verbatim.
- **The CODE-REVIEW shell has no JDK and the tree ships no `mvnw` wrapper**, so the reviewer cannot
  independently re-run `verify` and must lean on the EXAMPLE-BUILD record. EXAMPLES-GUIDE §4 mandates
  a committed wrapper; it is currently absent (system `mvn` is used). Recommend committing the
  `mvnw`/wrapper at the companion-tree root (per §4) so any gate can reproduce the build, and/or
  ensuring the JDK is on PATH for the code-reviewer step.
- **Repo-wide tool-pin drift (finding 6) recurs on every module** because it is set once at the
  aggregator. This is correctly a SOURCE-PIN reconciliation item, not a per-chapter CODE-REVIEW
  finding — but the per-chapter gate keeps re-discovering it. Suggest the reconciler/book-maintainer
  align `08-companion-code/pom.xml` to SOURCE-PIN once, repo-wide.
- Append confirmed lessons to `00-strategy/PIPELINE-LEARNINGS.md` and promote durable ones per the
  continuous-improvement HARD RULE.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh code-reviewer 4b 08 gate-run PASS-WITH-FIXES
```
