# GATE REPORT — EXAMPLE-BUILD (Step 4 / Gate: EXAMPLE)

## Header

- **Gate:** EXAMPLE-BUILD
- **Chapter key:** 91 (owner; folds 92 legacy/seams, 93 strangler, 95 migration) — FINAL_INDEX Ch 39 (opens Part XI)
- **Slug:** `91_refactoring_legacy_modernization`
- **Draft under review:** `03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
- **Module path:** `08-companion-code/91_refactoring_legacy_modernization/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder` (manual run) + `extract_snippet.sh` / `check_snippets.sh`
- **Scripts run:** `check_snippets.sh` (PASS ×6), `extract_snippet.sh` (×6 PASS), `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module is a self-contained child of the one companion-code aggregator (mirrors the smell→refactor
peer `19_code_smells_antipatterns`), builds green under `mvn -B -Pquality verify` warning-clean on JDK
21.0.11, all six declared snippet tags resolve to ≤9-line regions and pass `check_snippets`, and every
rule key / version / API / canon name in the module traces to the pin. The module is NOT yet registered
in the parent `<modules>` list — deliberate and correct: registration is gated on the CODE-REVIEW pass
(Step 4b), which has not run, and the parent `pom.xml` was not edited.

The module realizes the chapter's one invariant (preserve behaviour, verify with tests, move in small
steps) at the legacy / under-test / refactor / system scales. The fourth scale (Java version migration
via OpenRewrite) is **scoped out of the build** — recipe resolution is network-gated (REPRO-pending) —
and the draft says so, so no migration fact is invented or asserted as built.

---

## FLOOR C guard — preconditions (both logged, both hold)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime ≥ minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` (Homebrew); Maven 3.9.16 on the same JDK | PASS — meets the SOURCE-PIN anchor (JDK 21.0.11) |
| (b) `mvn -B -Pquality verify` GREEN | `BUILD SUCCESS`; `Tests run: 16, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0`; `No errors/warnings found` | PASS |

- **Exact command:** `mvn -B -Pquality -f 08-companion-code/91_refactoring_legacy_modernization/pom.xml clean verify`
  (toolchain: `JAVA_HOME=/opt/homebrew/opt/openjdk@21/...`; standalone single-module — parent pom NOT edited).
- **Result line:** `BUILD SUCCESS` · 16 tests pass · 0 Checkstyle · 0 SpotBugs (BugInstance size is 0) · warning-clean.
- Default (no-profile) `mvn -B verify` also green (16 tests) — the module builds without the analysis gate too.

---

## Snippet tags (tag-include regions) — all resolve ≤9 lines

| Tag | Backing file | Resolved lines | Role (scale) |
|---|---|---|---|
| `legacy-no-seam` | `src/main/java/org/acme/refactoring/LegacyShippingCalculator.java` | 8 | legacy: inlined `new` (no seam) vs Parameterize Constructor seam |
| `seam-interface` | `src/main/java/org/acme/refactoring/RateTable.java` | 1 | seam: Extract Interface, the place a test injects a known table |
| `characterization-test` | `src/test/java/org/acme/refactoring/SafeChangeTest.java` | 3 | under-test: pins current behaviour (the rounding quirk) |
| `modern-refactor` | `src/main/java/org/acme/refactoring/ShippingCalculator.java` | 7 | refactor: `Optional` + typed result, one short recipe |
| `modern-immutable-snapshot` | `src/main/java/org/acme/refactoring/ShippingCalculator.java` | 3 | refactor: unmodifiable snapshot closes the leak |
| `sealed-switch` | `src/main/java/org/acme/refactoring/StranglerRouter.java` | 6 | modern Java: exhaustive pattern-matching `switch` over sealed `Quote` |

- `check_snippets.sh 03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
  → **6 marker(s); 6 pass, 0 fail.**
- Snippet ceiling (≤9 lines): all six under the cap. The displayed slice and the runnable file are one
  artifact (anti-drift): each tag is a tight region of a full compiling file; the surrounding Javadoc and
  method bodies stay in the file but outside the markers.

### Marker insertion points (draft prose)

All six inserted at the **end of the "Deep dive: one invariant, four scales, against the big-bang"**
section, immediately after the existing "The test net is the thread through all four scales…" paragraph.
No prose deleted; each marker carries a one-line neutral, third-person lead-in in the locked voice.
Order follows the scale ladder: `legacy-no-seam` → `seam-interface` → `characterization-test` →
`modern-refactor` → `modern-immutable-snapshot` → `sealed-switch`, then a closing sentence on the
behaviour-preservation test and the strangler router. The back-matter "Companion module" note was
updated from `EXAMPLE-BUILD = PENDING` to **built green** and carries the `Snippet tags:` line; the
migration scale is explicitly recorded there as REPRO-pending and not part of the built module.

---

## Enterprise-grade checklist

| Requirement | How met |
|---|---|
| Child of the ONE aggregator (no own version literal / BOM) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; only `<artifactId>` set; deps inherit from the aggregator's `dependencyManagement` (JUnit BOM 6.0.3, AssertJ 3.27.7) |
| Pinned dependency set via inherited parent property | `maven.compiler.release=21`, `junit.version=6.0.3`, `assertj.version=3.27.7` all inherited; analyzer engines pinned in the self-contained `quality` profile (Checkstyle 10.26.1, SpotBugs 4.9.3.0) — same shape as peer module 19 |
| Externalized config profiles | `src/main/resources/refactoring.properties` carries `%dev`/`%prod` keys incl. the strangler **cutover flag** (legacy in dev, modern in prod) + demo rate — no compiled constants for the cutover or the rate |
| ≥1 integration test exercising the mechanism | `SafeChangeTest` (12 methods → 16 executed): characterization pin of the quirk, behaviour-preservation across every `ServiceLevel` and the served/unserved boundary (`@ParameterizedTest` + `@EnumSource`), the leak-is-a-real-bug failure path, the snapshot fix, the typed `Unavailable` outcome, and both strangler routes incl. rollback |
| Test-harness setup | JUnit Jupiter via the surefire JUnitPlatform provider (auto-detected); AssertJ assertions; no logging system-properties needed (uses `System.Logger` at DEBUG, silent by default) — confirmed: no spurious logging in the green run |
| Observability / health surface | `ShippingCalculator.unavailableCount()` — a running counter of quotes that could not be priced, asserted by `modernPathReturnsATypedUnavailableForAnUnservedZone` |
| Explicit failure path | Modern path returns a typed `Quote.Unavailable` (not an in-band zero) for an unserved zone; the `LegacyShippingCalculator` leak proven a real mutation bug by test, closed by the modern unmodifiable snapshot — the HONEST-LIMITATIONS floor in code |

---

## Behaviour-preservation centrepiece (the load-bearing claim, verified)

- The legacy method applies the service surcharge per-kilo and **truncates before** the weight
  multiplication. For a 333 g `EXPEDITED` parcel at the `ZONE_A` base rate (500/kilo) it charges
  **191**; the naive "apply the surcharge to the final amount" ordering gives **190**. This quirk is
  reproducible and was confirmed by an independent arithmetic probe before being pinned.
- `characterizationTestPinsTheLegacyRoundingQuirk` asserts `191` as *current* behaviour (Feathers: pin
  what the code does, not what it should do).
- `refactoringPreservesBehaviourForEveryServiceLevel` and
  `refactoringPreservesTheQuirkOnTheQuirkExposingInput` assert the modernized `ShippingCalculator`
  returns the identical charge as the legacy method for every parcel — quirk and all — so the refactor
  changed structure (record, sealed types, `Optional`, streams) and not the result. The comments are
  explicit that changing the rounding would be a behaviour change wearing the wrong hat.

---

## Detection boundary — verified, and made honest in the prose

- **SpotBugs raises a real finding here.** With the exclude filter emptied, the build fails with exactly
  one `EI_EXPOSE_REP` (Medium) on `org.acme.refactoring.LegacyShippingCalculator.appliedSurcharges()`
  ("may expose internal representation by returning ... appliedSurcharges", `LegacyShippingCalculator.java`
  line 100). The filter was restored immediately. So the single reviewed suppression is **load-bearing**,
  not decorative.
- **The legacy leaker is a plain final class with a hand-written accessor** (not a record) — the
  reusable lesson from peer module 19: SpotBugs does not raise the `EI_EXPOSE_REP` family on a record's
  generated accessor, so the leak counter-example is a plain class so the finding genuinely fires.
- **No method-length / complexity check in the house ruleset.** Refactoring (Chapter 39) is governed by
  behaviour-preserving tests, not a source-level naming gate; both the legacy class and the modern
  refactor are held to the same naming/import/structure rules, so the refactor is judged on its idioms,
  not graded on a curve. Stated in the Checkstyle config comment and the README.

---

## Capture (Step 4c) — subject-native UI screenshots

**No captures planned.** Chapter 39's figure plan (draft-time) is a single designed conceptual diagram,
`fig91_1` (the safe-change loop + the same invariant at four scales), authored as HTML→PNG under
`05-figures/91_refactoring_legacy_modernization/` (not the example-builder's job). The chapter has no
live dev-console / health-endpoint / API-explorer figure in its plan — it is a craft/concept chapter on
a JDK-only module with no running web surface. No subject-native UI surface was captured; inventing one
would be an unplanned figure (an editorial signal to the drafter, not a capture decision).

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. No file is a
copied-or-renamed upstream sample, getting-started/quickstart skeleton, or `NOTICE`/header boilerplate.
A scan for `copyright` / `licensed under` / `apache license` / `@author` / `generated by` / `quickstart`
/ `getting started` across all `.java` / `.xml` / `.properties` / `.md` returned nothing. No region is
taken substantially verbatim from any source file (Fowler/Feathers are cited as named authority for the
*technique*, never copied as text), so no excerpt attribution is required. The module mirrors the
*shape* of the in-repo reference peer module 19 (parent wiring, `quality` profile, config layout,
reasoned-suppression pattern), which is itself book-original.

---

## Source trace (every fact → pin)

| Fact in the module | Traces to |
|---|---|
| Java release 21; `maven.compiler.release=21` | SOURCE-PIN §"Runtime baseline" — anchor JDK 21 (built on 21.0.11) |
| JUnit 6.0.3, AssertJ 3.27.7 | SOURCE-PIN §3 (inherited via aggregator `dependencyManagement`) |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, maven-checkstyle-plugin 3.6.0, spotbugs-maven-plugin 4.9.3.0 | SOURCE-PIN §2 (static analysis); same pins as peer module 19 `quality` profile |
| `EI_EXPOSE_REP` / `EI_EXPOSE_REP2` (SpotBugs bug patterns) | SOURCE-PIN §2 SpotBugs; dossier 92 `_RESEARCH.md`; **re-confirmed live** (EI_EXPOSE_REP fires with the filter emptied) |
| `record`, `sealed` interface + pattern-matching `switch` (record deconstruction patterns), `Optional`, `Stream` / `List.of`, `enum` (type-code retirement) | JDK 21 API + JLS SE 21 (SOURCE-PIN §1); record patterns / pattern matching for `switch` final in 21 |
| Refactoring = behaviour-preserving transformation; Extract Interface; Parameterize Constructor; "two hats"; preparatory refactoring | Fowler *Refactoring* 2e 2018 + Feathers *WELC* 2004 (SOURCE-PIN §7 canon); dossiers 91 + 92 |
| "legacy code is code without tests"; seam = "a place where you can alter behaviour without editing in that place"; object/interface/link seams; characterization test pins current behaviour incl. bugs | Feathers *WELC* 2004 (SOURCE-PIN §7); dossier 92 `_RESEARCH.md` |
| Strangler fig (façade routes old→new incrementally; feature flag = rollback lever) | Fowler StranglerFigApplication 2004 (SOURCE-PIN §7); dossier 93 |
| OpenRewrite `UpgradeToJava21` (named in the draft only; NOT built) | SOURCE-PIN §6 OpenRewrite 8.81.0; dossier 95 — scoped out of the module, REPRO-pending |

No fact in the module required a detail the dossier + pin lacked. **No `09-flags/` gap raised.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | First build raised `UUF_UNUSED_FIELD` on a gratuitous `verbosePricing` static demo flag (never read → dead, not the intended mutable-static lesson) | NOTE (resolved) | `LegacyShippingCalculator.java` | Removed the dead field and its filter entry rather than suppress dead code (suppressing a no-value field would be padding) — build green |
| 2 | `EI_EXPOSE_REP2` (store side) does not fire at threshold=Medium; only `EI_EXPOSE_REP` (return side) does | NOTE | filter / pom comment | Matched both in the filter (in case a stricter threshold raises the store side) and the comment states which actually fires — no over-claim |
| 3 | Module not in parent `<modules>` | NOTE (intended) | `08-companion-code/pom.xml` | Leave as-is; register only after CODE-REVIEW pass (Step 4b). Parent pom not edited, per task constraint |
| 4 | Migration scale (OpenRewrite `UpgradeToJava21`) not built | NOTE (scoped, intended) | draft back-matter | Network-gated recipe resolution → REPRO-pending; draft records it as not-built; no migration fact asserted as built |

## Blockers

**None.**

---

## Learnings & pipeline suggestions

- **A "bug-as-behaviour" quirk is the cleanest way to make characterization testing land.** A truncation
  that happens at a different point than a naive reading expects (here: surcharge-per-kilo truncated
  before the weight multiply → 191, not 190) gives the characterization test a concrete, surprising
  value to pin, and gives the behaviour-preservation test real teeth — the refactor must reproduce the
  quirk, not "fix" it. Compute the quirk with an independent probe before pinning it, so the asserted
  value is verified arithmetic, not a guess.
- **Don't add a counter-example field/flag the tests never drive.** The dead `verbosePricing` static
  pulled a `UUF_UNUSED_FIELD` finding that was noise, not a lesson; the right move is to delete it, not
  suppress it. A reasoned suppression must point at a finding a test actually exercises (verified
  load-bearing with the filter emptied) — otherwise it is padding the guide forbids.
- **Re-confirm the suppression is load-bearing by emptying the filter, then restore.** The `-D`
  property override of `spotbugs.excludeFilterFile` was not honoured by spotbugs-maven-plugin 4.9.3.0;
  the reliable check is to temporarily replace the configured filter file with an empty one, run
  `spotbugs:check` (it must fail with the expected pattern), and restore. Candidate to script.
- **Sealed result + pattern-matching `switch` is a strong, in-cap modern-Java snippet** (6 lines here):
  it shows record deconstruction and exhaustiveness-without-`default` in one tight region, and it
  doubles as the chapter's "modern Java supersedes a manual catalog step" point shown rather than
  asserted.
- **Scope the unbuildable scale explicitly in the draft, don't fake it.** The migration/OpenRewrite
  scale is network-gated; recording it as REPRO-pending and not-built (rather than stubbing a recipe)
  keeps Floor C honest and avoids asserting an unverified recipe outcome as fact.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
