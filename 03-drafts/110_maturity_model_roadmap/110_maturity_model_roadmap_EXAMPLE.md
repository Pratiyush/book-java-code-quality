# GATE REPORT — EXAMPLE-BUILD (Step 4b) — Chapter 47 / key 110

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 110 (frozen key from `01-index/CANDIDATE_POOL.md`) — **THE FINAL CHAPTER** (closes Part XIV + the whole book)
- **Slug:** `110_maturity_model_roadmap`
- **Draft under review:** `03-drafts/110_maturity_model_roadmap/110_maturity_model_roadmap_v1.md`
- **Module path:** `08-companion-code/110_maturity_model_roadmap/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×5), `check_snippets.sh`, `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (`/example` pilot not yet cleared)
- **Verdict:** **PASS** (FLOOR C PASS) — module built green; CODE-REVIEW (Step 4b second half) still to run before the module joins the parent `<modules>` list.

---

## Verdict rationale — BUILT (the dossier's N/A call was revised)

The dossier and v1 front-matter marked **EXAMPLE-BUILD = N/A** ("a roadmap artifact, not a buildable
module"). On assessment that call was **revised to BUILT**, for the same reason the immediately-prior
Part XIV capstone peer (Chapter 46 / key 109) was built rather than left a static diagram: the staged
roadmap has a **concrete, runnable composition**. Where key 109 composes a CI run's stage outcomes into
one ship / no-ship verdict (`org.acme.refstack`), this chapter composes a team's per-dimension ratings
into one maturity **level** + one **next step** (`org.acme.maturity`) — exactly the "score a team/codebase
across the maturity dimensions → a level + next-step recommendation" the build brief calls for, and
exactly the kind of buildable model the EXAMPLES-GUIDE expects.

Crucially, the chapter's **closing honesty is encoded in code, not only prose** (the EXAMPLES-GUIDE §1.1
failure-path requirement and FLOOR B in code): the overall level is the **lowest** dimension's stage and
never an average (so a wall of green cannot hide a fire); a high stage whose outcomes have stalled is
**discounted** (the Goodhart / vanity-ladder guard) and triggers `RestoreOutcomes` rather than more
climbing; past the policy threshold the model recommends `Sustain` (more maturity ≠ more value); and
`CULTURE_KNOWLEDGE` is a first-class dimension (tools without culture fail). No new primary atom is
introduced — the chapter is pure synthesis, and the one framing fact (DORA's move from rigid maturity
levels to capabilities + continuous improvement) traces to `SOURCE-PIN.md` §5 (DORA, dora.dev).

`mvn -B -Pquality verify` is green at the Java 21 anchor; all 5 displayed snippets resolve to real
≤9-line tag regions in compiling files. No invented or unflagged source atom.

---

## FLOOR C guard — the two preconditions (both logged, both hold)

| Precondition | Evidence | Status |
|---|---|---|
| (a) Runtime meets the minimum (Java 21 anchor) | `openjdk version "21.0.11" 2026-04-21` (Maven 3.9.16, runtime 21.0.11) | **MET** |
| (b) `mvn -B -Pquality verify` finished GREEN | `BUILD SUCCESS` (see verdict-of-record below) | **MET** |

**Verdict-of-record command (run from the repo, pointing at the module pom; module not yet in the
reactor `<modules>` list, by design):**

```
mvn -B -Pquality -f 08-companion-code/110_maturity_model_roadmap/pom.xml clean verify
```

**Result line:**

```
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO] You have 0 Checkstyle violations.
[INFO] BugInstance size is 0
[INFO] BUILD SUCCESS
[INFO] Total time:  3.065 s
```

The default fast build (`mvn -B -f pom.xml verify`, no profile) is also green (12 tests). The `quality`
profile adds Checkstyle (engine 10.26.1 behind plugin 3.6.0) + SpotBugs 4.9.3.0. Full `-Xlint:all`
warning sweep over `clean compile test-compile`: **NO WARNINGS** (warning-clean per FLOOR C). The
`-Dmaturity.profile=prod` selection was confirmed to resolve and stay green.

---

## Tag-includes (the displayed snippets — one artifact with the runnable file)

5 tags, each verified ≤9 displayed lines by `extract_snippet.sh`; all 5 markers in the draft resolve PASS
under `check_snippets.sh`.

| # | Tag | File | Lines | What the book shows |
|---|---|---|---|---|
| 1 | `roadmap-stages` | `src/main/java/org/acme/maturity/Roadmap.java` | 3 | the five stages as a cheapest-first ordered view |
| 2 | `overall-level` | `src/main/java/org/acme/maturity/MaturityAssessment.java` | 4 | the overall level = the LOWEST dimension's counted stage, never an average |
| 3 | `recommend` | `src/main/java/org/acme/maturity/MaturityAssessment.java` | 7 | the next step: refuse to climb on a stalled outcome, else advance/sustain |
| 4 | `roadmap-policy` | `src/main/java/org/acme/maturity/RoadmapPolicy.java` | 5 | the externalized require-outcomes + sustain-at-stage policy + profile property |
| 5 | `next-step` | `src/main/java/org/acme/maturity/NextStep.java` | 5 | the sealed advance / restore-outcomes / sustain recommendation |

`check_snippets.sh` output: **5 marker(s); 5 pass, 0 fail.**

**Marker insertion points in the draft (one-line lead-ins added, no prose deleted, locked voice):**
- "The staged roadmap" section, after the Stage-0..4 CONCEPT block: tag 1 (`roadmap-stages`).
- "Start where your pain is" CONCEPT (context-driven, not linear): tags 2 (`overall-level`), 3 (`recommend`).
- "Capabilities … not maturity levels" CONCEPT: tags 4 (`roadmap-policy`), 5 (`next-step`).
- Back-matter `**Snippet tags:**` line added after the companion-artifact spec paragraph.
- Both N/A notes (front-matter line + back-matter spec) revised to record the built module + the reason.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | Status | Where |
|---|---|---|---|
| — | Child of the ONE aggregator; no group/version literal; no own BOM | **PASS** | `<parent>` set; inherits junit-bom + AssertJ from the aggregator; JDK-only runtime |
| — | NOT yet in parent `<modules>` (joins after green + CODE-REVIEW) | **PASS** | confirmed absent from `08-companion-code/pom.xml` (grep count 0) |
| 1 | Pinned platform via one inherited property | **PASS** | `maven.compiler.release` 21 + test libs inherited; analyzer versions match the reactor-wide proven set |
| 2 | Externalized config profiles (`dev` / `prod`) | **PASS** | `maturity-dev.properties` / `maturity-prod.properties`, selected by `-Dmaturity.profile` (`RoadmapPolicy`) |
| 3 | ≥1 integration test exercising the mechanism | **PASS** | `MaturityAssessmentTest` — 12 tests driving the loaded policy + composed level + next step end to end |
| 4 | Observability / health surface | **PASS** | `MaturityAssessment.assessmentsRun()` + `lowestDimensionStageOrder()` (metrics) + `isReady()` (readiness) |
| 5 | Explicit failure path, driven by a test | **PASS** | `NextStep.RestoreOutcomes` (the vanity-ladder refusal) — driven by `recommendsRestoringOutcomesBeforeClimbing`, `stalledDimensionIsDiscountedInTheLevel`; plus empty/unknown-profile/null rejections |

**Test-harness setup:** JUnit Jupiter via the aggregator's `junit-bom` import; Surefire 3.5.6 (inherited
`pluginManagement`) auto-detects the JUnit Platform provider — no extra runner property needed (no JaCoCo
agent in this module, so no `argLine` interaction). Confirmed: 12 tests run, 0 skipped, under the default,
the `quality`, and the `prod`-profile builds.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's fixed figure plan is one *designed conceptual diagram* (fig110_1,
the staged adoption roadmap — five stage cards with practices + chapter keys), authored as HTML and
rendered to PNG separately (the figure-designer track; already present under
`05-figures/110_maturity_model_roadmap/` as `fig110_1.{html,png,sources.md}`) — NOT my job and not
image-generated. This is a JDK-only model with no running dev console, API explorer, or health endpoint to
photograph, so ZERO subject-native screenshots are planned. Nothing captured by this gate.

---

## FLOOR C — source-trace (zero invented atoms; every version traced or flagged)

| Atom in the module | Traces to | Status |
|---|---|---|
| Java 21 anchor (compiler release 21) | `SOURCE-PIN.md` Runtime baseline; aggregator `maven.compiler.release` | traced |
| JUnit (junit-bom) / AssertJ 3.27.7 | `SOURCE-PIN.md` §3; aggregator `dependencyManagement` | traced |
| `maven-checkstyle-plugin` 3.6.0 + engine `com.puppycrawl.tools:checkstyle` 10.26.1 | proven reactor-wide set; `09-flags/05_toolchain_plugin_versions.md` (two-pin split; engine vs SOURCE-PIN 13.6.0) | traced + flagged |
| `spotbugs-maven-plugin` 4.9.3.0 | proven reactor-wide set; `09-flags/05_toolchain_plugin_versions.md` (vs SOURCE-PIN 4.10.2) | traced + flagged |
| Checkstyle module names (LineLength, NeedBraces, RecordComponentName, …) | Checkstyle 10.26.1 (matches the shared house ruleset) | traced |
| DORA "capabilities + continuous improvement over rigid maturity levels" (the one framing fact) | `SOURCE-PIN.md` §5 (2025 DORA report + *Accelerate*, dora.dev) | traced |
| The five stages' practices + per-dimension chapter cross-refs | the v1 draft + each cited chapter's own dossier (pure synthesis; no new primary atom) | traced |

**No new flag filed:** the only version deviations from a SOURCE-PIN top-line (Checkstyle engine, SpotBugs)
are the already-documented, reactor-wide, artifact-availability-forced skews shared by every module — no
atom is invented, and none is unflagged. The model's stages, dimensions, and recommendation logic are
original synthesis of the book's own (already-verified) practices, introducing no new pinnable fact.

---

## NEUTRALITY-in-code (FLOOR A) + LEGAL-IP §5

- **NEUTRALITY:** banned-phrase scan over all module text (`*.java`, `*.xml`, `*.properties`, `*.md`) —
  **clean** (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` and the usual
  near-variants: none). The model crowns no tool and no stage: a higher stage is explicitly "a default not
  a rung", `Roadmap.describe` says so on every stage, and the whole point of the discount/sustain logic is
  that more is not automatically better. `CULTURE_KNOWLEDGE` is named neutrally as the dimension no
  plug-in installs. No comment, identifier, log/error string, or test name disparages an alternative.
- **LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK (file-by-file confirmed):** every file is written for this book.
  The `org.acme.maturity` domain (7 classes + package-info) is distinct from the Chapter 46 capstone's
  `org.acme.refstack` — a different concern (a team's staged self-assessment → a level + a next step, vs a
  CI run → a ship/no-ship verdict), not a rename. The curated Checkstyle ruleset and empty SpotBugs filter
  follow the shared small house convention (a convention reused across peer modules, not a copied upstream
  sample). No file is a lightly-edited upstream quickstart/sample; no `NOTICE`/header boilerplate copied.
  No substantially-verbatim upstream block, so no per-file attribution is required.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Dossier/v1 had marked EXAMPLE-BUILD = N/A | NOTE (resolved) | draft front-matter + back-matter | revised to BUILT with an explicit reason recorded in both notes; mirrors the Ch 46 capstone peer's buildable-synthesis precedent |
| 2 | Build clean on first `quality` run | NOTE | — | no Checkstyle/SpotBugs finding to fix; the immutable-record + stateless-view design keeps the representation-exposure detectors quiet without any suppression |

No code defects raised by the assembled gate; the model's value types are immutable records, the view is
stateless, and the assessment exposes no mutable collection.

---

## Blockers

**None.** Both FLOOR C preconditions hold (Java 21.0.11; `BUILD SUCCESS`, warning-clean). CODE-REVIEW (the
Step-4b second half, the `code-reviewer` agent) is still to run; the module stays OUT of the parent
`<modules>` list until it passes, per EXAMPLES-GUIDE §6.1.

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b) — companion module builds green via `mvn -B -Pquality verify` at the Java 21
  anchor (warning-clean); every displayed snippet resolves to a real ≤9-line tag region in a compiling
  file; FLOOR C source-trace clean (no invented atom; every skew flagged).
- [ ] **CODE-REVIEW** — to be run by the `code-reviewer` agent before the module joins the build and before
  Step 5 VERIFY treats FLOOR C as fully closed.

---

## Learnings & pipeline suggestions

- **A "concept/roadmap" dossier call of EXAMPLE-BUILD = N/A is worth re-testing at build time.** Both
  Part XIV closers (keys 109, 110) were dossier-marked as figure/artifact chapters, yet both have a real,
  buildable *composition* (a CI run → a verdict; a self-assessment → a level + next step). The signal: a
  synthesis chapter is buildable whenever its synthesis reduces inputs to a decision — and building it is
  the strongest way to get the chapter's honest-limitations into a tested code path. Recommend a one-line
  note in EXAMPLES-GUIDE: "before accepting a dossier's N/A, check whether the chapter's mechanism composes
  inputs into a decision; if so, it is buildable."
- **The failure-path requirement carried the chapter's whole thesis.** The vanity-ladder warning is the
  point of this final chapter, and encoding it as `NextStep.RestoreOutcomes` + the discount-to-FOUNDATIONS
  rule (driven by tests) turns "maturity is for outcomes, not a badge" from a sentence into a code path a
  reader can run — exactly the §1.1 intent. The "lowest, not average" choice for the overall level is the
  same idea and is similarly the most teachable line in the module.
- **De-duplication against the key-109 peer worked as a design constraint.** Keeping the model to a
  team's staged self-assessment (`org.acme.maturity`), distinct from the CI-run gate composition
  (`org.acme.refstack`), kept the final chapter genuinely additive and import-free while reusing the same
  proven module shape (externalized dev/prod policy, sealed result, metric + readiness, JDK-only).
- Appended to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 110 gate-run PASS
```
