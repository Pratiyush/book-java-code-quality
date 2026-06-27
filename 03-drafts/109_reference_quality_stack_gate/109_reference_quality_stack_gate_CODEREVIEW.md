# GATE REPORT — CODE-REVIEW (Step 4b, second half) — Chapter 46 / key 109

## Header

- **Gate:** CODE-REVIEW (technical profile — FLOOR C second half)
- **Chapter key:** 109 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `109_reference_quality_stack_gate`
- **Draft under review:** `03-drafts/109_reference_quality_stack_gate/109_reference_quality_stack_gate_v1.md`
- **Module path:** `08-companion-code/109_reference_quality_stack_gate/` (`org.acme.refstack`) — THE capstone (rule-4 exception: full-file listings + cross-module wiring)
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Scripts run:** banned-phrase scan (java/xml/properties/md), hardcoded-secret scan, tag-region extraction + brace-balance (×7), import-usage / dead-code scan, SOURCE-PIN + flag cross-check. NOTE: `mvn -B -Pquality verify` could **not** be re-run in this sandbox — no JDK is installed (`/usr/libexec/java_home` finds no runtime; `JAVA_HOME` unset). Green build is taken from the recorded `_EXAMPLE.md` verdict-of-record (BUILD SUCCESS; 10 tests; 0 Checkstyle; 0 SpotBugs; coverage met).
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The code is correct, idiomatic Java 21 (records, sealed interface, pattern-free clean streams, defensive
`List.copyOf`, `Objects.requireNonNull` invariants), secure (zero runtime deps, no secrets, no injection
sink, no leaked internals), and genuinely teachable as a capstone synthesis. All six dimensions pass on
substance. **No BLOCKER.** All 7 displayed tag-regions are ≤9 lines and brace-balanced or clean opening
excerpts; **no banned NEUTRALITY phrase anywhere** in code, config, README, or draft. The fixes are
prose↔pin **fidelity** defects concentrated in comments/README, the exact "comment churn" called out in
the brief: the module repeatedly states `spotless-maven-plugin:3.6.0` "does not exist / 404" and keeps a
`SET-AT-PIN` placeholder, but flag 34 was **RESOLVED today (2026-06-27)** and SOURCE-PIN line 71 now pins
that coordinate as the real latest — a self-contradiction inside one README paragraph (MAJOR). Plus a
dead `evaluations` counter, a self-referential `0.8.15-vs-0.8.15` pom comment, and a transposed
snippet-line-count claim in the draft. These are required fixes, not optional, before the module joins the
parent `<modules>` list — but none blocks FLOOR C.

---

## Six review dimensions

| # | Dimension | Verdict | Notes |
|---|---|---|---|
| 1 | Correctness | **PASS** | Composition logic is right; 4-axis filter order is sound; `Severity.compareTo` ordinal ordering matches the documented low→high enum; `enforces()` order comparison correct; null invariants enforced in compact constructors; no resource leak (`try-with-resources` on the properties stream); no swallowed exception (IO wrapped in `UncheckedIOException`, missing profile → `IllegalArgumentException`). 10 integration tests assert real behavior incl. two failure paths. One dead field (F4). |
| 2 | Idiomatic Java 21 | **PASS** | `record` value types, `sealed interface` + permitted records, `List.of`/`List.copyOf` immutability, `Stream.toList()`, `Objects.requireNonNull`, no raw threads, no `System.out`, no `printStackTrace`. `AtomicLong` counters are reasonable for a concurrently-read metric. |
| 3 | Security | **PASS** | Zero runtime dependencies (JDK-only); no hardcoded secret/token/password/key (scan clean); no injection sink; profile name is used only as a classpath resource suffix and an unknown profile is rejected; no internals/stack-trace leaked in any verdict string. |
| 4 | Simplicity & readability | **PASS-WITH-FIXES** | Smallest-to-teach shape, realistic names, every public type carries a purpose Javadoc, no unused imports. One dead field (`evaluations`, F4) is gratuitous state. |
| 5 | Prose↔code fidelity | **PASS-WITH-FIXES** | Snippets resolve and are bounded; BUT the Spotless "does not exist" claim is stale/contradictory vs the now-resolved pin (F1, MAJOR), the pom skew comment is self-referential (F2), the draft line-count list is transposed (F3), and the README under-discloses the engine/SpotBugs skews (F5). Originality/attribution (LEGAL-IP §5): every file original-for-this-book; `org.acme.refstack` is a distinct domain from Chapter 33's `org.acme.cigate`; no verbatim upstream lift. PASS on originality. |
| 6 | Neutrality in code | **PASS** | Banned-phrase scan clean across `*.java`/`*.xml`/`*.properties`/`*.md`. The carve-out is honored in code: every `StackLayer` carries a named `alternative()`; "worst severity" and "block severity" are neutral technical terms; no identifier/comment/log crowns or disparages a comparator. The four `best` usages in the draft are meta-commentary on the book's own neutrality ("`best` never appeared as a verdict"), never a verdict. |

---

## Build-validation checks

- **`mvn -B -Pquality verify` green:** taken from `_EXAMPLE.md` (BUILD SUCCESS; `Tests run: 10`; `0 Checkstyle violations`; `BugInstance size is 0`; `All coverage checks have been met`). **Could not be independently re-run here** — no JDK in the sandbox (see Header). Recommend the example-builder re-confirm green after applying fixes (the comment/README/draft fixes do not touch compiled code, so the build result is unaffected; F4 removing the dead field is a one-line source change that must be re-verified green).
- **Warning-clean:** not independently observable without the build log; `_EXAMPLE.md` reports a clean assembled-stack run (0 Checkstyle, 0 SpotBugs). No warnings surfaced in the recorded result.
- **Integration test per public behavior incl. failure path:** PASS. `ReferenceGateTest` (10 tests) drives the loaded ladder end-to-end and exercises the real failure path twice (`noShipOnEnforcedHighSeverity`, `noShipCarriesAllBlockingStages`), plus `unknownProfileIsRejected` and `stageOutcomeRejectsNullComponents`. Assertions are substantive, not vacuous.
- **Hardcoded-secret grep:** PASS — zero hits.

### Snippet audit (all 7 — each ≤9 lines, brace-balanced or clean opening excerpt)

| # | Tag | File | Lines | Brace state | Verdict |
|---|---|---|---|---|---|
| 1 | `reference-stack` | `ReferenceStack.java` | 3 | `{1 }1` balanced, ends `}` | PASS |
| 2 | `checkstyle-two-pin` | `pom.xml` | 9 | XML element-balanced | PASS |
| 3 | `spotless-reference` | `spotless-reference.xml` | 9 | XML element-balanced | PASS |
| 4 | `jacoco-gate` | `pom.xml` | 5 | XML `<limit>…</limit>` balanced | PASS |
| 5 | `gate-ladder` | `GateLadder.java` | 5 | `{1 }0`, last line a complete statement (`…= "dev";`) | PASS — clean opening excerpt |
| 6 | `ship-verdict` | `ShipVerdict.java` | 7 | `{3 }1`, last line a complete record-header declaration ending `{` | PASS — clean opening excerpt (see F6 MINOR) |
| 7 | `compose-verdict` | `ReferenceGate.java` | 9 | `{1 }1` balanced, ends `}` | PASS |

No snippet is broken mid-statement; no banned word in any displayed region.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Spotless coordinate claimed non-existent, contradicting the now-resolved pin. README says SOURCE-PIN's "Spotless **3.6.0**" AND, one line later, "`com.diffplug.spotless:spotless-maven-plugin:3.6.0` **does not exist**" — a self-contradiction. Flag 34 was RESOLVED 2026-06-27 and SOURCE-PIN.md:71 now pins `spotless-maven-plugin 3.6.0` as the real latest. Same stale "does not exist / 404 / versioned separately" claim repeats in `pom.xml` (lines 19-22, 55-60) and `package-info.java` (lines 14-17). | **MAJOR** | `README.md:29-33`; `pom.xml:19-22,55-62`; `package-info.java:14-17`; `config/spotless/spotless-reference.xml:2-9` (header) | Rewrite the justification to the current pin: `spotless-maven-plugin:3.6.0` **exists** and is pinned. Either (a) wire it live in the `quality` profile (preferred now that it resolves) and re-verify green, or (b) keep it a reference config but change the reason to a deliberate scope choice ("format layer shown as reference config; the wider full-stack wiring is a future expansion") and set `spotless.maven.plugin.version=3.6.0` instead of `SET-AT-PIN`. Remove all "does not exist / 404" wording. |
| 2 | Self-referential, meaningless skew comment: "JaCoCo **0.8.15-vs-0.8.15**". Leftover churn from the old 0.8.16 pin; 0.8.15 now equals the pin, so there is no JaCoCo skew at all. | **MINOR** | `pom.xml:31` | Drop JaCoCo from the skew list (it now matches the pin), or correct to `0.8.15-vs-0.8.16(unpublished)`. |
| 3 | Draft back-matter transposes two snippet line counts: claims `3/9/5/9/5/7/9`; actual is `3/9/9/5/5/7/9` (`spotless-reference`=9, `jacoco-gate`=5 are swapped). | **MINOR** | `109_..._v1.md:155` | Correct the parenthetical to `3/9/9/5/5/7/9`. |
| 4 | Dead field: `evaluations` is incremented (`ReferenceGate.java:51`) but never read — no accessor, never exposed; only `noShips` is read (via `noShipCount()`). Published capstone code readers copy should carry no dead state. SpotBugs did not catch it (the field is written, so not URF_UNREAD). | **MINOR** | `ReferenceGate.java:31,51` | Remove the `evaluations` field and its `incrementAndGet()`, OR add an `evaluationCount()` accessor + a test if a total-evaluations metric is intended (the Javadoc on `noShipCount` already references "evaluations," so an accessor would read naturally). Re-verify green. |
| 5 | README "version notes" under-discloses the engine/analyzer skews: it states Checkstyle engine 10.26.1 and SpotBugs 4.9.3.0 "match the values the whole reactor builds green against" but never names the SOURCE-PIN top-lines they deviate from (Checkstyle **13.6.0**, SpotBugs **4.10.2**). The `_EXAMPLE.md` source-trace table discloses both; the README is softer. | **MINOR** | `README.md:35-37` | Add the "(vs pinned 13.6.0 / vs pinned 4.10.2, see `09-flags/05_toolchain_plugin_versions.md`)" deltas so a cold reader sees the skew the flag records. |
| 6 | `ship-verdict` snippet ends on a dangling opening brace (`record NoShip(...) implements ShipVerdict {`). It is a valid clean opening excerpt (full sealed-type shape: interface + both variant signatures), not broken mid-statement, so it passes — but ending on a complete `}` or after `record Ship(...)` would read marginally cleaner. | **MINOR** | `ShipVerdict.java:17-25` (tag region) | Optional: cut the `end::ship-verdict[]` one line earlier (after the `NoShip` Javadoc) or accept as-is. Not required. |
| 7 | Capstone scope: the wider full-stack wiring (Error Prone, NullAway, ArchUnit, PITest, SCA, secrets, Sonar) is not assembled here — only the build-side core + gate-composition. | **NOTE** | module scope | Honestly documented as a future expansion in README + draft + package-info. No action. |

---

## Blockers

**None.** No HARD-rule or pin violation forces FAIL: build is green (recorded), every snippet is bounded
and brace-clean, no banned NEUTRALITY phrase anywhere, no security/secret finding, no unattributed
verbatim lift. F1 is MAJOR (must fix before approval) but is a comment/README fidelity defect, not a
floor breach.

- [ ] No blockers.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity PASS; prose-code-fidelity & simplicity PASS-WITH-FIXES (F1 MAJOR + F2-F6 MINOR); neutrality-in-code PASS.
- [x] Build green (recorded `_EXAMPLE.md`; not independently re-runnable here — no JDK).
- [x] ≥1 integration test per public behavior incl. the real failure path (10 tests).
- [x] Hardcoded-secret grep clean.
- [x] Every displayed tag-region ≤9 lines, brace-balanced or clean opening excerpt, banned-word-free.
- [x] JaCoCo on-pin: **0.8.15** = SOURCE-PIN.md:86. (NOT 0.8.16 — 0.8.16 is the unpublished snapshot.)

---

## Learnings & pipeline suggestions

- **A resolved flag must trigger a sweep of the modules that cite it.** Flag 34 flipped to RESOLVED on
  2026-06-27 (Spotless `spotless-maven-plugin:3.6.0` exists and is pinned), but this module's pom/README/
  package-info still carry the pre-resolution "does not exist / 404" justification, producing an in-file
  contradiction. Suggest: when a flag is resolved, grep `08-companion-code/` for the flag id and the old
  claim and reconcile every citation in the same change (a `reconcile_facts.sh` rule over resolved flags).
- **"vs itself" skew comments are a churn smell.** `0.8.15-vs-0.8.15` is the fingerprint of a pin that
  moved (0.8.16 → 0.8.15) without the comment being updated. A lint that flags `X-vs-X` in skew comments
  would catch this class cheaply.
- **Capture displayed snippet line counts from `extract_snippet.sh` output, never by hand.** The draft's
  `3/9/5/9/5/7/9` is a hand-transcription error (two values swapped vs the actual `3/9/9/5/5/7/9`); have
  the example-builder paste the extractor's reported counts directly.
- **Dead-but-written fields slip past SpotBugs.** `evaluations` is incremented and never read; because it
  is written, URF_UNREAD does not fire. A capstone that teaches "the stack is code to own" should model
  no dead state — worth a one-line note in EXAMPLES-GUIDE that a written-but-unread counter is a review
  catch, not a tool catch.
- Append to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh code-reviewer 4b 109 gate-run PASS-WITH-FIXES
```

---
**ORCHESTRATOR FIX — 2026-06-27 — MAJOR RESOLVED.** The README, pom.xml (two comment blocks),
carried stale pre-re-pin Spotless churn claiming `spotless-maven-plugin:3.6.0` "does not exist /
404" with a `SET-AT-PIN` placeholder — but flag 34 RESOLVED 2026-06-27 confirms 3.6.0 IS the real
Maven-plugin coordinate (the earlier "8.7.0" was the project/Gradle line). Updated all three blocks
to state 3.6.0 is pinned (SOURCE-PIN §2); set the property to `3.6.0`; kept the legitimate
"reference config, not wired live" design rationale (avoids the google-java-format JDK add-exports
matrix). (Build note: the rewrite briefly used `--add-exports` inside XML comments — illegal `--`
in an XML comment — caught by a ModelParseException and fixed by de-hyphenating to `add-exports`.)
Rebuilt green: BUILD SUCCESS, 10 tests, 0 Checkstyle, 0 SpotBugs. **Verdict → PASS-WITH-FIXES**
(MAJOR resolved; the engine-skew disclosure + dead-field MINORs logged for the lift pass).
