# GATE REPORT — EXAMPLE-BUILD (Step 4b) — Chapter 46 / key 109

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 109 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `109_reference_quality_stack_gate`
- **Draft under review:** `03-drafts/109_reference_quality_stack_gate/109_reference_quality_stack_gate_v1.md`
- **Module path:** `08-companion-code/109_reference_quality_stack_gate/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×7), `check_snippets.sh`, `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (`/example` pilot not yet cleared)
- **Verdict:** **PASS** (FLOOR C PASS) — module built green; CODE-REVIEW (Step 4b second half) still to run before the module joins the parent `<modules>` list.

---

## Verdict rationale

The chapter is the Part XIV capstone — the one chapter that recommends a concrete stack (the NEUTRALITY
carve-out). It shows concrete config and a load-bearing decision, so a buildable module is warranted (not
N/A). The module assembles the build-side core of the reference stack (compiler floor + Checkstyle +
SpotBugs + JaCoCo branch gate, with the Spotless format layer as a reference config) and makes the
chapter's distinct synthesis runnable: composing the four-stage gate ladder and the nine-layer stack into
one ship / no-ship verdict (`org.acme.refstack`), separate from Chapter 33's single-stage gate
(`org.acme.cigate`). `mvn -B -Pquality verify` is green at the Java 21 anchor; all 7 displayed snippets
resolve to real ≤9-line tag regions in compiling files. No invented or unflagged source atom.

---

## FLOOR C guard — the two preconditions (both logged, both hold)

| Precondition | Evidence | Status |
|---|---|---|
| (a) Runtime meets the minimum (Java 21 anchor) | `openjdk version "21.0.11" 2026-04-21` (Maven 3.9.16, runtime 21.0.11) | **MET** |
| (b) `mvn -B -Pquality verify` finished GREEN | `BUILD SUCCESS` (see verdict-of-record below) | **MET** |

**Verdict-of-record command (run from the module dir; `-o` offline against the proven reactor cache):**

```
mvn -B -Pquality -f 08-companion-code/109_reference_quality_stack_gate/pom.xml verify
```

**Result line:**

```
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
[INFO] You have 0 Checkstyle violations.
[INFO] BugInstance size is 0   /   Error size is 0   /   No errors/warnings found
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
[INFO] Total time:  3.368 s
```

(The default fast build — `mvn -B -f pom.xml verify`, no profile — is also green: 10 tests + JaCoCo
agent/report, no analyzer gate. The `quality` profile adds Checkstyle + SpotBugs + the JaCoCo branch
gate, the assembled stack.)

---

## Tag-includes (the displayed snippets — one artifact with the runnable file)

7 tags, each verified ≤9 displayed lines by `extract_snippet.sh`; all 7 markers in the draft resolve PASS
under `check_snippets.sh`.

| # | Tag | File | Lines | What the book shows |
|---|---|---|---|---|
| 1 | `reference-stack` | `src/main/java/org/acme/refstack/ReferenceStack.java` | 3 | the layered stack as an ordered list of distinct concerns |
| 2 | `checkstyle-two-pin` | `pom.xml` | 9 | the build-plugin + pinned-engine "two-pin" assembly (style layer) |
| 3 | `spotless-reference` | `config/spotless/spotless-reference.xml` | 9 | the format-layer reference config (placeholder version + g-j-f 1.35.0 + ratchetFrom) |
| 4 | `jacoco-gate` | `pom.xml` | 5 | the BRANCH coverage gate threshold |
| 5 | `gate-ladder` | `src/main/java/org/acme/refstack/GateLadder.java` | 5 | the externalized dev/prod ladder + profile property |
| 6 | `ship-verdict` | `src/main/java/org/acme/refstack/ShipVerdict.java` | 7 | the composed ship / no-ship sealed verdict |
| 7 | `compose-verdict` | `src/main/java/org/acme/refstack/ReferenceGate.java` | 9 | the four-axis composition into one verdict |

`check_snippets.sh` output: **7 marker(s); 7 pass, 0 fail.**

**Marker insertion points in the draft (one-line lead-ins added, no prose deleted, locked voice):**
- "The reference stack" section (after the all-OSS paragraph): tags 1, 2, 3, 4.
- "The gate design" section (after the adoptable-choices paragraph): tag 5.
- "The capstone module" section (after the CONCEPT block): tags 6, 7.
- Back-matter `**Snippet tags:**` line added after the companion-module note.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | Status | Where |
|---|---|---|---|
| — | Child of the ONE aggregator; no group/version literal; no own BOM | **PASS** | `<parent>` set; inherits junit-bom + AssertJ from the aggregator |
| — | NOT yet in parent `<modules>` (joins after green + CODE-REVIEW) | **PASS** | confirmed absent from `08-companion-code/pom.xml` |
| 1 | Pinned platform via one inherited property | **PASS** | runtime + test libs inherited; analyzer/coverage versions match the reactor-wide proven set |
| 2 | Externalized config profiles (`dev` / `prod`) | **PASS** | `refstack-dev.properties` / `refstack-prod.properties`, selected by `-Drefstack.profile` |
| 3 | ≥1 integration test exercising the mechanism | **PASS** | `ReferenceGateTest` — 10 tests driving the loaded ladder + composed verdict end to end |
| 4 | Observability / health surface | **PASS** | `ReferenceGate.noShipCount()` (metric) + `isReady()` (readiness); JaCoCo HTML/XML report |
| 5 | Explicit failure path, driven by a test | **PASS** | `ShipVerdict.NoShip` (composed no-ship) — driven by `noShipOnEnforcedHighSeverity`, `noShipCarriesAllBlockingStages` |

**Test-harness setup:** JUnit Jupiter via the aggregator's `junit-bom` import; Surefire 3.5.6 (inherited
`pluginManagement`) auto-detects the JUnit Platform provider — no extra runner property needed; JaCoCo
injects its agent via `argLine` (the module sets no `<argLine>` of its own, avoiding the agent-clobber
trap). Confirmed: 10 tests run, 0 skipped, under both the default and `quality` builds.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's fixed figure plan is two *designed conceptual diagrams* (fig109_1
the layered stack by concern; fig109_2 the four-stage gate ladder), authored as HTML and rendered to PNG
separately (the figure-designer track) — NOT my job and not image-generated. This is a CONFIG-centric,
JDK-only module with no running dev console, API explorer, or health endpoint to photograph, so ZERO
subject-native screenshots are planned. Nothing captured under `05-figures/109_reference_quality_stack_gate/`
by this gate.

---

## FLOOR C — source-trace (zero invented atoms; every version traced or flagged)

| Atom in the module | Traces to | Status |
|---|---|---|
| Java 21 anchor (compiler release 21) | `SOURCE-PIN.md` Runtime baseline; aggregator `maven.compiler.release` | traced |
| JUnit (junit-bom) / AssertJ 3.27.7 | `SOURCE-PIN.md` §3; aggregator `dependencyManagement` | traced |
| `maven-checkstyle-plugin` 3.6.0 + engine `com.puppycrawl.tools:checkstyle` 10.26.1 | proven reactor-wide set; `09-flags/05_toolchain_plugin_versions.md` (two-pin split; engine vs SOURCE-PIN 13.6.0) | traced + flagged |
| `spotbugs-maven-plugin` 4.9.3.0 | proven reactor-wide set; `09-flags/05_toolchain_plugin_versions.md` (vs SOURCE-PIN 4.10.2) | traced + flagged |
| `jacoco-maven-plugin` 0.8.15 | `09-flags/48_jacoco_pin_0816_unpublished.md` (SOURCE-PIN 0.8.16 is 404 on Central) | traced + flagged |
| JaCoCo check fields (BUNDLE/BRANCH/COVEREDRATIO/minimum) | JaCoCo check-mojo (matches key-48 usage) | traced |
| Checkstyle module names (LineLength, NeedBraces, …) | Checkstyle 10.26.1 (matches the peer house ruleset) | traced |
| Spotless coordinate + `${spotless.maven.plugin.version}` placeholder; google-java-format 1.35.0 | `09-flags/34_spotless_maven_plugin_version_unresolved.md`; SOURCE-PIN §2 (g-j-f 1.35.0 resolves) | traced + flagged |

**No new flag filed:** every deviation from a SOURCE-PIN top-line is an already-documented,
artifact-availability-forced skew shared by the whole reactor — no atom is invented, and none is
unflagged. The Spotless format layer is shown as a *reference config* (placeholder version), never wired
live, so the green build asserts no unpinned coordinate.

---

## NEUTRALITY-in-code (FLOOR A) + LEGAL-IP §5

- **NEUTRALITY:** banned-phrase scan over all module text (`*.java`, `*.xml`, `*.properties`, `*.md`) —
  **clean**. The carve-out is honored in code: every `StackLayer` carries a *named alternative*
  (`alternative()`), so the recommendation states its trade-off rather than crowning a tool; comments
  describe each layer's cost and the human ceiling (Chapters 37, 1). "worst severity" usages are neutral,
  technical descriptions of a finding, not crowning. No comment/identifier/log string disparages an
  alternative.
- **LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK (file-by-file confirmed):** every file is written for this book.
  The `org.acme.refstack` domain (9 classes) is distinct from Chapter 33's `org.acme.cigate` (7 classes) —
  a different concern (composing the four-stage ladder + nine-layer stack into one verdict, vs deciding
  one gate), not a rename. The curated Checkstyle ruleset and empty SpotBugs filter follow the shared
  small house convention (a convention, not a copied upstream sample; the ruleset body even differs from
  peer 75's). The Spotless reference config follows the house pattern established in key-07. No file is a
  lightly-edited upstream quickstart/sample; no `NOTICE`/header boilerplate copied. No substantially-
  verbatim upstream block, so no per-file attribution is required.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Checkstyle `LineLength` caught a 123-char Javadoc line during assembly | MINOR (fixed) | `StackLayer.java:21` | shortened the comment to ≤120 — the assembled gate doing its job |
| 2 | SpotBugs `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` on `NoShip`'s mutable `List` accessor/ctor | MAJOR (fixed) | `ShipVerdict.java` | added a compact constructor defensively copying to `List.copyOf` (Effective Java Item 50) — idiomatic immutable record |
| 3 | Full multi-module wiring (Error Prone, NullAway, ArchUnit, PITest, SCA, secrets, Sonar) not assembled in this module | NOTE | module scope | recorded as a future capstone expansion in the draft + README; the build-side *core* + gate-composition is real and green |
| 4 | Spotless format layer is a reference config, not a live plugin | NOTE | `config/spotless/` | gated on the Spotless Maven-plugin re-pin (`09-flags/34_...`); same honest treatment as key-07 |

---

## Blockers

**None.** Both FLOOR C preconditions hold (Java 21.0.11; `BUILD SUCCESS`). The two real findings raised by
the assembled stack (1, 2) were fixed and re-verified green. CODE-REVIEW (the Step-4b second half, the
`code-reviewer` agent) is still to run; the module stays OUT of the parent `<modules>` list until it
passes, per EXAMPLES-GUIDE §6.1.

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b) — companion module builds green via `mvn -B -Pquality verify` at the Java 21
  anchor; every displayed snippet resolves to a real ≤9-line tag region in a compiling file; FLOOR C
  source-trace clean (no invented atom; every skew flagged).
- [ ] **CODE-REVIEW** — to be run by the `code-reviewer` agent before the module joins the build and before
  Step 5 VERIFY treats FLOOR C as fully closed.

---

## Learnings & pipeline suggestions

- **The capstone's value is composition, and the gate proved it.** Assembling Checkstyle + SpotBugs + the
  JaCoCo branch gate over fresh code immediately surfaced two real defects (a long line, a mutable-list
  record exposure) that the single-tool peer modules never hit — exactly the "layered, see different
  things" thesis (Chapter 3) demonstrated by the build, not asserted. The `EI_EXPOSE_REP` fix
  (`List.copyOf` in a record's compact constructor) is a clean, teachable idiom worth keeping visible in
  the capstone.
- **De-duplication against peers worked as a design constraint.** Keeping the runnable helper to the
  *synthesis* (composing the four-stage ladder + nine-layer stack into one verdict, `org.acme.refstack`)
  rather than re-implementing Chapter 33's single-stage gate (`org.acme.cigate`) avoided code coupling and
  kept the capstone genuinely additive. Recommend future capstone-expansion work add the heavier analyzers
  (Error Prone, NullAway, ArchUnit, PITest) as opt-in profiles on this same module rather than a new one.
- **Version-skew honesty paid off.** Every analyzer/coverage version that differs from a SOURCE-PIN
  top-line was already flagged reactor-wide (`05_toolchain_plugin_versions.md`, `48_...`, `34_...`), so the
  capstone needed no new flag — the discipline of matching the proven-green reactor set, plus referencing
  the existing flags, kept FLOOR C clean without inventing a coordinate. Promote a one-line note to
  EXAMPLES-GUIDE: "an assembled-stack module inherits the reactor's already-flagged plugin/engine skews;
  reference them, do not re-file."
- Appended to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 109 gate-run PASS
```
