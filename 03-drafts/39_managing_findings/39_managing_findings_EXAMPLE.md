# GATE REPORT — EXAMPLE-BUILD (key 39)

## Header

- **Gate:** EXAMPLE (Step 4b — EXAMPLE-BUILD)
- **Chapter key:** 39 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `39_managing_findings`
- **Draft under review:** `03-drafts/39_managing_findings/39_managing_findings_v1.md`
- **Module path:** `08-companion-code/39_managing_findings/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×7), `check_snippets.sh` (draft) — all PASS
- **Verdict:** **PASS** — `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)

---

## Verdict rationale

The module builds green under the `quality` profile at the pinned toolchain; all seven displayed snippets
resolve to real tag regions ≤9 lines inside the compiled artifacts; every tool fact (annotation,
filter elements, bug pattern, config keys) traces to its pinned authority; the module is original-for-this-book.
Both silencing controls — the SpotBugs baseline `<Match>` and the site `@SuppressFBWarnings` — were verified
**load-bearing** (removing either turns the build red), so the chapter's discipline is executable, not decorative.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime/toolchain meets minimum (Java 21+):** ✅
  - `openjdk version "21.0.11" 2026-04-21` (Homebrew openjdk@21) — meets the SOURCE-PIN anchor (JDK 21 LTS).
  - `Apache Maven 3.9.16` — matches SOURCE-PIN §4 (Maven 3.9.16).
- **(b) `verify` finished GREEN:** ✅
  - Command (standalone module): `mvn -B -o -Pquality -f 08-companion-code/39_managing_findings/pom.xml clean verify`
  - Result line: `BUILD SUCCESS` — `Tests run: 15, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0` / `No errors/warnings found`.
  - (`-o` offline; all artifacts resolve from the local cache — checkstyle 10.26.1, spotbugs engine 4.9.3 / plugin 4.9.3.0, spotbugs-annotations 4.9.3, junit-bom 6.0.3, assertj 3.27.7.)

> Build state marked `[MANUAL — tooling pending]` until the key-01 pilot clears, per the EXAMPLE gate policy.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Module not yet registered in parent `<modules>` | NOTE | `08-companion-code/pom.xml` | Intentional — register only after green build **and** CODE-REVIEW pass (constraint honoured; parent pom not edited). |
| 2 | Chapter's `maxAllowedViolations` Checkstyle-ratchet idea not used in build | NOTE | `pom.xml` quality profile | By design: the ratchet is shown as runnable Java (`FindingRatchet`) + the SpotBugs-baseline TRY-IT; the Checkstyle file baseline is shown via `SuppressionFilter`. Foot-of-draft spec updated to match what was built (no over-claim). |
| 3 | Sonar "Won't Fix"→"Accepted" rename + tool-version/default atoms | NOTE | dossier flags | Not exercised by this module (no Sonar artifact); the two existing `09-flags/` entries stand, unchanged. |

---

## Blockers

None.

---

## Enterprise-grade checklist

- **Child of the ONE aggregator, no own version literal / BOM:** ✅ `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; the only version literal is the one pinned `spotbugs-annotations` GAV (compile-path `provided`), exactly as peer 27.
- **Pinned dependency set via inherited parent:** ✅ runtime (`maven.compiler.release=21`) and test libs (junit-bom, assertj) inherited from the aggregator; analyzer engines pinned in the `quality` profile (two-pin override: checkstyle 10.26.1 over the plugin's bundled 9.3).
- **Externalized config profiles:** ✅ all gate config lives under `config/` (`checkstyle/checkstyle.xml`, `checkstyle/checkstyle-suppressions.xml`, `spotbugs/spotbugs-exclude.xml`); the gates run only under the opt-in `-Pquality` profile, not hard-wired into the default build.
- **At least one integration test exercising the mechanism:** ✅ `FindingManagementTest` (15 tests, 6 `@Nested` groups): triage decision, the ratchet (grandfathers old / fails new), the load-bearing suppression's correctness, legacy-vs-clean contrast, the failure path, and the health surface.
- **Test-harness setup confirmed:** ✅ JUnit Jupiter via the parent's `junit-bom` import + surefire 3.5.6 (`JUnitPlatformProvider` auto-detected); AssertJ from the parent. No spurious logging — run is clean (15/15, 0 skipped). No extra system properties needed for this module.
- **Observability / health surface:** ✅ `GateHealth#report` (tag `gate-health`) — a readiness signal over the silenced-debt count (READY / DEGRADED past an agreed budget), the chapter's "keep debt about debt visible" made executable. A reporting surface, not a second gate (never changes the verdict).
- **Explicit failure path:** ✅ three halves — `PricingCatalog#priceFor` returns `Optional.empty()` for an unknown SKU (defined benign outcome); construction-time fast-fail (`PricingCatalog` negative-price, `PriceFormatter` negative-amount); and `Finding` **refuses to represent** an unjustified suppression (the HONEST-LIMITATIONS floor in code: a suppression with no evidence cannot be constructed). Plus the gate's own failure path: a new finding fails the build (proven below).

---

## Load-bearing verification (controls are real, not decorative)

| Control | Action | Result |
|---|---|---|
| SpotBugs baseline `<Match>` (legacy) | removed the `<Match>` block | `BugInstance size is 2` → `EI_EXPOSE_REP` @ `LegacyPriceTable.java:39` + `EI_EXPOSE_REP2` @ `:29` → **BUILD FAILURE**; restored → green |
| Site `@SuppressFBWarnings` (false positive) | removed the annotation | `BugInstance size is 1` → `EI_EXPOSE_REP` @ `PriceFormatter.java:40` (`denominationsCents()`) → **BUILD FAILURE**; restored → green |
| The ratchet (new finding on clean class) | `PricingCatalog#priceTiers` returns the internal array directly | **BUILD FAILURE** (a new `EI_EXPOSE_REP` on the clean class, while the legacy one stays frozen); restored `Arrays.copyOf` → green |

This is the chapter's thesis as an executable event: the past is tolerated (baselined), a judged false
positive is silenced narrowly with a reason, and a new finding fails the build.

---

## Snippet tags (displayed listings) — all resolve, all ≤9 lines

| Tag | File | Content lines | Marker site in draft |
|---|---|---|---|
| `triage-decision` | `src/main/java/org/acme/findings/FindingTriage.java` | 9 | after the triage-tree CONCEPT box |
| `reviewed-suppression` | `src/main/java/org/acme/findings/PriceFormatter.java` | 7 | Lever 1 (SpotBugs form) |
| `checkstyle-suppression-filter` | `config/checkstyle/checkstyle.xml` | 1 | Lever 1 (Checkstyle wiring) |
| `baseline-match` | `config/spotbugs/spotbugs-exclude.xml` | 1 | Lever 3 (SpotBugs baseline) |
| `checkstyle-baseline` | `config/checkstyle/checkstyle.xml` | 4 | Lever 3 (Checkstyle file baseline) |
| `ratchet` | `src/main/java/org/acme/findings/FindingRatchet.java` | 6 | Lever 4 (ratchet) |
| `gate-health` | `src/main/java/org/acme/findings/GateHealth.java` | 7 | "debt about debt" para (observability) |

`check_snippets.sh 03-drafts/39_managing_findings/39_managing_findings_v1.md` → **7 marker(s); 7 pass, 0 fail.**
7 tags total (within the 4–7 design window). Markers inserted as HTML comments with a one-line lead-in
each; no prose deleted; locked voice. Foot-of-draft spec updated to GREEN + a `Snippet tags:` line.

---

## Captured screenshots

**No captures planned.** This is a config-and-policy module with no subject-native UI surface (no dev
console, API explorer, or health view to photograph live); the chapter's figure is the designed
scope-ladder diagram `fig39_1` (authored as HTML→PNG separately, not an example-builder artifact). Step 4c
correctly yields zero captures here.

---

## Source-trace (every atom → pinned authority)

| Atom (in module) | Traces to | Pinned identifier |
|---|---|---|
| `@SuppressFBWarnings(value, justification)`, `edu.umd.cs.findbugs.annotations` | SpotBugs annotations | SOURCE-PIN §2 SpotBugs 4.10.2 line; built against cached 4.9.3 engine/annotations |
| `FindBugsFilter` / `<Match>` / `<Class>` / `<Bug pattern>` (+ `Confidence`/`Rank`/`Method`/`Field`/`Local`/`Source` leaves exist) | SpotBugs filter schema | verified in pinned engine jar (`spotbugs-4.9.3.jar`: `BugMatcher`/`ClassMatcher`/`MethodMatcher`/`ConfidenceMatcher`/`RankMatcher`/… present) |
| `EI_EXPOSE_REP`, `EI_EXPOSE_REP2` bug patterns | SpotBugs bug descriptions | verified verbatim in pinned engine `findbugs.xml` (`type="EI_EXPOSE_REP" category="MALICIOUS_CODE"`) — **resolves the dossier's one `⚠ verify at pin` example-pattern flag at FLOOR-C build time** |
| `excludeFilterFile`, `effort`, `threshold`, `includeTests`, `failOnViolation`, `violationSeverity`, `includeTestSourceDirectory`, `configLocation` | spotbugs-maven-plugin 4.9.3.0 / maven-checkstyle-plugin 3.6.0 | live plugin config accepted by the pinned plugins (build green) |
| `SuppressWarningsFilter` + `SuppressWarningsHolder`, `SuppressionFilter` (`file`, `optional`), the named Checks (`ConstantName`, `RecordComponentName`, `PatternVariableName`, `AvoidStarImport`, …) | Checkstyle | accepted by the pinned engine `com.puppycrawl.tools:checkstyle:10.26.1` (audit ran, 0 violations) |
| JUnit Jupiter `@Test`/`@Nested`/`@DisplayName`; AssertJ `assertThat`/`assertThatThrownBy` | JUnit 6 / AssertJ | SOURCE-PIN §3 (junit-bom 6.0.3 cached; assertj 3.27.7) |
| `maven.compiler.release=21`, records, `Map.copyOf`/`Arrays.copyOf`/`Optional` | JLS/JDK | SOURCE-PIN runtime anchor JDK 21 |

No atom was invented. No fact was needed that the dossier + pin lacked; **no new `09-flags/` entries
required.** The two pre-existing key-39 flags (`39_sonar_wontfix_accepted_rename_unverified.md`,
`39_tool_versions_and_suppression_defaults_unverified.md`) are untouched — neither is exercised by this
module (no Sonar artifact; tool/plugin versions used are the locally-cached pinned set).

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in `08-companion-code/39_managing_findings/` is original work written
for this chapter. No copied/renamed upstream sample, no quickstart/getting-started skeleton, no
`NOTICE`/license/header boilerplate, no `@author`, no `Generated by` (scan returned NONE). All code is in
the book-original `org.acme.findings` package. The tool identifiers used (annotation, filter elements,
config keys, bug patterns) are short API names from each tool's own documented surface, not copied source
files; none is taken substantially verbatim from a specific upstream file, so no per-file attribution is
required.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion artifact builds green via `-Pquality verify` at the pin; every displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file; FLOOR C source-trace clean.
- [x] Child of the single aggregator (`<parent>` set, no own version literal / BOM).
- [x] Pinned dependency set; externalized config; ≥1 integration test + harness configured; observability surface; explicit failure path.
- [x] Realizes the draft; adds no behaviour the prose does not claim.
- [x] Locked neutral voice in comments/README; no banned phrasings; no tool crowned.
- [x] Local only — no git remote / push / `gh`.
- [x] Both silencing controls verified load-bearing.
- [ ] Registered in parent `<modules>` — DEFERRED until green + CODE-REVIEW pass (correct per policy).

---

## Floor-C verdict: **PASS**

Both FLOOR C guard preconditions hold and are logged: (a) runtime Java 21.0.11 ≥ minimum; (b) `mvn -B -Pquality verify` GREEN. Zero invented atoms; every fact traces to a pinned authority. The module passes the EXAMPLE gate; the CODE-REVIEW gate (Step 4b, `code-reviewer`) is the next gate and is required before the module joins the reactor.

---

## Learnings & pipeline suggestions

- **A config-and-policy chapter still wants a runnable policy shell.** The four levers are tool config, but
  the *decision* (triage) and the *ratchet* are algorithms — modelling them as small total functions
  (`FindingTriage`, `FindingRatchet`) let the tests prove the chapter's rules directly, and gave the
  ≤9-line Java snippets a home alongside the config snippets. Good reuse pattern for keys 76/80/87.
- **Load-bearing proof belongs in the gate report, not just the README.** Removing each control and
  recording the exact `BugInstance size` + finding line is the cheapest way to show a suppression/baseline
  is real rather than decorative — and it is exactly the chapter's thesis. Suggest making "controls
  verified load-bearing (with the red-build evidence)" a standard EXAMPLE-gate row for any
  suppression/baseline module.
- **A FLOOR-C build can retire a dossier `⚠ verify at pin` flag.** The `EI_EXPOSE_REP` example-pattern
  atom was flagged pre-pin; the build verified it verbatim in the pinned engine jar. Worth noting that the
  example-builder is a second, independent confirmation path for tool-fact atoms, not only the
  source-verifier.
- **The "type refuses to represent the bad state" move doubles as the HONEST-LIMITATIONS failure path.**
  `Finding`'s rejection of an unjustified false positive makes the chapter's "suppression is a claim that
  needs evidence" unrepresentable in code — a clean way to satisfy the explicit-failure-path requirement
  while teaching the point.
