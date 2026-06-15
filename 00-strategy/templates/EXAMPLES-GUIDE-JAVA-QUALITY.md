# EXAMPLES GUIDE — Java code quality

> **(technical/code profile — see BOOK-TYPE-PROFILES.md; book types without runnable examples drop this entire guide, the COMPANION-REPO spec, and the DEMO-CATALOG. Active only when `research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble` includes example-build + code-review and FLOOR C carries the COMPILE clause.)**

> The standard for the **Step-4b companion module** (the 9th HARD gate). Every selected chapter ships **one** self-contained, runnable, enterprise-grade `Java 21+ (21 LTS anchor, 25 LTS forward)` module keyed by its frozen `NN_slug`, pinned to Java code quality **the pins in SOURCE-PIN.md**, built and tested by `./mvnw -B verify`. The book's displayed bounded snippet is a tag-include region *inside* that compiled file, so the printed listing and the runnable code are **one artifact**.
>
> This guide inherits the book's law and is subordinate to it. Where it would collide, the law wins: `neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X` (`NEUTRALITY.md`), never invent a source (`LEGAL-IP-RULES.md` §3, `SOURCE-PIN.md`), the locked `the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md` (`VOICE-GUIDE.md`). It is referenced by `COMPANION-REPO.md`, `SOURCE-PIN.md`, and `SCORING.md` (FLOOR C), and by the `/example` pipeline step.

## Where Step-4b sits in the pipeline

Step 4 drafts the chapter. **Step 4b builds the companion module.** Step 5 (VERIFY) then checks the prose against the compiled code, not against an unverified fragment. A module that does not build green at the pins in SOURCE-PIN.md fails FLOOR C, and FLOOR C is fatal regardless of the aggregate score (`SCORING.md`).

```
Step 4   DRAFT          prose, with displayed snippets marked for tag-include
   |
Step 4b  EXAMPLE-BUILD  one runnable enterprise-grade NN_slug module; ./mvnw -B verify green
   |
Step 5   VERIFY         prose-against-compiled-code; recorded source paths
```

- Modules are built **locally now** under the tracked tree `08-companion-code/NN_slug/`. Local building is not an outward-facing action.
- The **public** repository push stays gated on the human/legal sign-off in `COMPANION-REPO.md` §5. Nothing in this guide authorizes a `git`-remote, a `gh` action, or a push to `{URL}`.
- New tooling is the `/example` command plus the `example-builder` agent, mirroring `/draft` and the drafter. Until the one-chapter pilot lands, a chapter's build-state is marked `[MANUAL — tooling pending]`.

---

## 1. What "enterprise-grade" means (the five concrete requirements)

A module is **enterprise-grade** only when all five hold. These are not aspirations; each is a checklist item the `example-builder` ticks and the Step-5 VERIFY gate confirms.

| # | Requirement | Concretely |
|---|---|---|
| 1 | **Pinned platform** | Java code quality is pinned through **one inherited property/manifest entry**, set once in the parent build file; the child module carries no version literal of its own. |
| 2 | **Externalized config profiles** | Configuration lives in an externalized config file, with at least a `%dev` and `%prod` (dev/prod) profile externalized — no behavior hard-coded that a deployment would need to change. |
| 3 | **At least one integration test** | The module carries `>=1` test (the subject's in-process test-harness annotation) that exercises the chapter's mechanism and runs under `./mvnw -B verify`. |
| 4 | **An observability / health surface** | Where the chapter's topic touches it, the module exposes a health or metrics surface (e.g. a liveness/readiness probe or a metrics endpoint) using the verified extension for the pinned tag. |
| 5 | **An explicit failure path** | The module demonstrates the **HONEST-LIMITATIONS floor in code** via a real failure path: a fault-tolerance policy, a graceful-shutdown hook, **or** a genuine error response. A module that only shows the happy path does not qualify. |

> The failure path (5) is the load-bearing one. The book's HONEST-LIMITATIONS floor (`SCORING.md` FLOOR B) says every feature gets its hardest objections and a when-NOT-to-use. The companion module must show that floor **in code** — not in a comment, but in a code path that actually runs under test.

> NOTE — The test runner may need build-file configuration to work (the subject's test framework may require a specific log manager, test plugin, or runner property). Configure it **once in the parent build file** alongside the pin (§3); the child `NN_slug/` module inherits it and carries no plugin config of its own. Confirm the exact requirement against the pinned source (the per-tool fetch dirs in SOURCE-PIN.md) — never assume it.

### 1.1 The failure path — pick exactly one that fits the topic

| Failure-path kind | When it fits | What the module shows |
|---|---|---|
| **Fault-tolerance policy** | The chapter touches calls that can fail or time out (clients, integrations, downstream services). | A retry / timeout / fallback / circuit-breaker policy with a test that drives the failing branch. |
| **Graceful-shutdown hook** | The chapter touches lifecycle, long-running work, or in-flight requests at stop. | A shutdown observer (or the configured shutdown timeout) that drains in-flight work, with a test that asserts the orderly path. |
| **Real error response** | The chapter touches an HTTP/messaging surface that must reject bad input or upstream failure. | An exception mapper or explicit non-2xx response, with a test that asserts the error contract. |

Choose the one that the chapter's mechanism actually exercises. Do not bolt on a fault-tolerance annotation that the topic never calls — that is padding, and it fails the same authenticity bar the prose does. Confirm the chosen extension and annotation against the pinned clone before using them (`source-verify` skill/protocol).

### 1.2 When NOT to over-build (honest limitation of this guide)

Not every requirement applies to every topic. A pure internals chapter may have no surface to expose a health probe on. The rule is: requirement 4 (observability/health) is scoped to **"where the topic touches it"** — if the topic genuinely has no such surface, record that in the module README rather than inventing one. Requirements 1, 2, 3, and 5 are unconditional.

---

## 2. The `NN_slug` module layout

The module folder is named with the **same frozen dossier key** used across `01-index/`, `03-drafts/`, and `04-approved/`: lowercase, underscores, two-digit zero-padded — for example `07_database/`. The chapter number is the single source of truth across prose, drafts, and code.

```
08-companion-code/
├── <parent build file>            # parent/aggregator: sets the platform pin once (§3)
├── <committed build wrapper>      # committed build wrapper / lockfile (§4)
├── NN_slug/                       # one self-contained module per chapter
│   ├── <child build file>         # inherits the parent; NO version literal here
│   └── src/
│       ├── main/...               # full compilable sources with // tag::name[] regions (§5)
│       ├── main/resources/
│       │   └── <config file>      # %dev / %prod profiles externalized (req. 2)
│       └── test/...               # >=1 integration test, incl. the failure-path test (req. 3, 5)
└── ...                            # one folder per selected chapter, NN_slug from FINAL_INDEX.md
```

- **HARD — every example is a module of the ONE parent project; there are no standalone example projects.** The companion-tree root build file is a single aggregator/parent. Each chapter's `NN_slug/` directory is a child `<module>` of it: its build file declares the parent (with a relative path back to the aggregator), carries **no** group/version literal of its own, and inherits the pin and the platform BOM/manifest from the parent (§3). The whole companion tree is **one reactor**. The reference chapter's module is the reference implementation — copy its build-file shape. A module is added to the parent module list **only after** it builds green (`./mvnw -B verify`) **and** passes the CODE-REVIEW gate (§6.1); until then it stays out of the module list so a red or unreviewed module never breaks the reactor.
- Each module is **self-contained to run**: a reader can build and run a single chapter on its own (the single-module build of `NN_slug` against the parent) without building any prior chapter. Self-contained means topic-independent, **not** a separate project — it is still a child of the one aggregator (the default recorded in `COMPANION-REPO.md` §2.3).
- The module README states the chapter title and the `NN_slug` it maps to, the `Java 21+ (21 LTS anchor, 25 LTS forward)` baseline, the pinned Java code quality version, and the one copy-paste build command — and notes any scoped-out requirement per §1.2. It carries **no** version, config key, or dependency coordinate the book has not itself verified against the pin.
- The number of selected chapters is governed by `01-index/FINAL_INDEX.md`. Do not hardcode a chapter count here; point to `FINAL_INDEX.md`.
- **Every `NN_slug` module realizes its chapter's demo from `DEMO-CATALOG.md`** in the shared domain — a **shared narrative motif, NOT code coupling**: no module imports or depends on another module's code (§2, modules-of-the-ONE-parent; `DEMO-CATALOG.md` §5).

### 2.1 The capstone — the single named bounded exception (GUIDELINES §8)

There is **exactly one** named exception to the bounded-snippet cap and the standalone-module rule: the **capstone** module (`DEMO-CATALOG.md` §4). The capstone wires several areas into one runnable app so a reader sees the pieces compose, and **it is the only place** full-file listings (over the bounded snippet cap) and **cross-module wiring** appear. **No other module carries this exception** — everywhere else, modules stay standalone and snippets stay within the cap as `// tag::` regions. The capstone is **gated like any module**: green `./mvnw -B verify` **and** the CODE-REVIEW gate (§6.1). The exception is to the snippet cap and the no-imports rule only, never to FLOOR C.

---

## 3. The pin — one inherited property

Java code quality is pinned in **exactly one place**. The parent/aggregator build file sets the platform pin; every `NN_slug/` module inherits it and carries no version literal.

> **HARD — child wiring.** Each `NN_slug/` child build file MUST declare the parent with a relative path back to the aggregator, MUST omit its own group and version (both inherited), and MUST NOT redeclare the platform BOM/manifest or any version literal. A module that defines its own version or imports its own BOM is a standalone project and is rejected — see §2, modules-of-the-ONE-parent.

| Property | Value | Why |
|---|---|---|
| platform pin | `the pins in SOURCE-PIN.md` | The single knob that binds the platform BOM, the build plugin, and every imported extension to the pinned tag (`SOURCE-PIN.md`). |

- A future re-pin is then a **one-line edit** in the parent build file, after which the whole companion tree is rebuilt against the new version — matching `SOURCE-PIN.md`'s single-source-of-truth discipline.
- The exact property value and its placement are **confirmed against a real generated build file in the pinned clone** (the per-tool fetch dirs in SOURCE-PIN.md) before anything is committed — never assumed. The same never-invent law that governs the prose governs the build file.

> NOTE — The pin property name and the BOM reference are attested in the pinned tree (the subject's own tooling emits them). Re-confirm against the clone at each re-pin; do not carry this forward on memory.

---

## 4. The committed build wrapper

- The build wrapper / lockfile is **committed**: the wrapper script(s) and bootstrap directory live at the companion-tree root so the build is reproducible without a preinstalled toolchain.
- The documented entry points are the dev/run command and `./mvnw -B verify` (the build contract, §6). Confirm both phrasings against the pin's tooling guide before stating them in any README.

---

## 5. The tag-include convention (the anti-drift core)

This is the structural reason the book and the runnable code cannot silently diverge.

- The module stores **full, compilable** source, config, and build files.
- The region the book displays is wrapped in tag markers **inside that same file**:

```
// tag::name[]
... the bounded region the book shows ...
// end::name[]
```

- The manuscript **references** the tagged region rather than pasting a copy. The displayed snippet and the compilable file are therefore **one artifact**: edit the file, the book updates; the file is what CI compiles. Drift between the printed listing and the runnable code becomes structurally impossible.
- The displayed region stays within the book's **snippet ceiling** (`LEGAL-IP-RULES.md` §2). The surrounding full file may be longer because it is never reproduced in the prose.
- For inline, non-numbered code, use a `no-listing-*` tag so **every** snippet shown in the book has a home in the module.
- **How the reference resolves (honest mechanics).** The `tag::`/`end::` markers use the upstream documentation's tag syntax for consistency and forward-compatibility. The book's manuscript is **Markdown**, which has no native include — so the regions are resolved by tooling, not a native directive: prose carries `<!-- include: NN_slug/path/File.ext#name -->`, and `.claude/scripts/extract_snippet.sh` slices the region (cap enforced) into a fenced block at assembly; `.claude/scripts/check_snippets.sh` gates that every marker resolves to a real, compiling, within-cap region.

> WARNING — A displayed snippet that is not a real `// tag::name[]` region inside a file that compiles is a FLOOR C failure. A fragment that "looks plausible" is exactly the failure mode the companion module exists to prevent.

---

## 6. The `./mvnw -B verify` contract

`./mvnw -B verify` is the build contract for every companion module. It is the gate's verdict of record at Step 4b.

- **Batch/non-interactive mode** so output is deterministic and CI-friendly — no interactive prompts, no progress spinners.
- **The full verify lifecycle** runs through integration: compile, unit tests, package, and the integration-test suite.
- **Green is the only passing state.** A red build blocks the chapter from the human gate.
- **CI:** a CI matrix runs `./mvnw -B verify` across the `NN_slug` modules on the verified `Java 21+ (21 LTS anchor, 25 LTS forward)` baseline. A red build blocks the chapter. CI is treated as an effective HARD gate for the companion code (`COMPANION-REPO.md` §2.6).

```
edit module / snippet  ->  ./mvnw -B verify  ->  green  ->  CODE-REVIEW  ->  Step 5 VERIFY (prose vs. compiled code)
                                           \-> red    ->  fix; chapter blocked from human gate
```

---

## 6.1 Validation & code-review — beyond a green build

A green `./mvnw -B verify` proves the module compiles and its tests pass. For a technical book whose
code readers copy into production, that is necessary but not sufficient. Two extra validation layers
raise the bar before FLOOR C is recorded:

**Build-level validation (mechanical, enforced by the parent build file + tests).**
- Every module compiles with the strictest lint/warning level the toolchain offers, warnings surfaced. A new
  compiler warning is a FIX, not ignored.
- At least one test **per public behavior, including the failure path** (the blank-input
  rejection, the fallback, the error response). A behavior with no test is incomplete.
- **No hardcoded secrets/credentials** (config or env only); public endpoints validate their input.

**CODE-REVIEW gate (judgment) — the `code-reviewer` agent, part of Step 4b.**
After the green build, the `code-reviewer` agent reviews the module the way a senior engineer reviews a
PR, across six dimensions — **correctness, idiomatic Java code quality & Java 21+ (21 LTS anchor, 25 LTS forward), security, simplicity/readability,
prose↔code fidelity, and neutrality-in-code** — and writes `03-drafts/NN_slug/NN_slug_CODEREVIEW.md`.
A **FAIL — or any security, neutrality, or invented-fact finding — blocks FLOOR C**, exactly as a red
build does. The reviewer reports fixes; the `example-builder` applies them; re-review until PASS. The
full rubric is in `.claude/agents/code-reviewer.md`. The principle: example code is a published
deliverable and must be **exemplary and idiomatic, not merely working**.

---

## 7. `Java 21+ (21 LTS anchor, 25 LTS forward)` baseline

The module targets the pinned `Java 21+ (21 LTS anchor, 25 LTS forward)` baseline (`SOURCE-PIN.md`), not a guessed value:

| Fact | Value |
|---|---|
| Minimum runtime | **Java 21+ (21 LTS anchor, 25 LTS forward)** (read from the pinned tree; cite the prerequisite line in the source) |
| Recommended runtime | the recommended value recorded in `SOURCE-PIN.md` |
| Native/specialized toolchain | the toolchain the pin records (where the subject ships one) |

- The minimum is a hard floor: no module may assume a runtime below the recorded baseline.
- The README's prerequisites and the CI matrix both read this one source of truth. Do not guess the baseline in any module file; cite `SOURCE-PIN.md`.

---

## 8. NEUTRALITY and never-invent apply to code, not just prose

The companion module carries the **same law as the prose** — and the law reaches every byte the module ships, including comments, identifiers, and log/error strings.

### 8.1 NEUTRALITY (`neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X`) — `NEUTRALITY.md`, `SCORING.md` FLOOR A

- No comment, identifier, package name, log message, error string, test name, or commit message crowns the subject or disparages an alternative. The banned phrasings recorded in `NEUTRALITY.md` are an automatic FAIL wherever they appear, code included.
- Java code quality is the subject. An alternative appears in code **only** for a necessary direct feature comparison or a migration topic, and even then neutrally and with a cited the pinned authority set (00-strategy/SOURCE-PIN.md) reference — never to position any alternative as the winner.
- Name things for what they do. A class called `OrderService` is fine; a class, comment, or log line that editorializes about a competing tool is not.

### 8.2 Never invent a source — `LEGAL-IP-RULES.md` §3, `SOURCE-PIN.md`

- Every `rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims` atom in the module (config key, dependency coordinate, version number, API signature, CLI flag, annotation) traces to the pinned **Java code quality the pins in SOURCE-PIN.md** clone. None is invented, and none drifts to an unpinned newer release.
- This includes **log and error strings that quote a config key or property name** — a logged property name is a fact and must trace to the pin like any other.
- Use the subject's canonical names, not informal or legacy aliases.
- Anything that cannot be traced to the pin is removed or flagged to `09-flags/` for the human gate, exactly as in the prose. Use the `source-verify` skill/protocol to confirm any fact before it lands in the module.

### 8.3 Attribution — every companion file ORIGINAL-FOR-THIS-BOOK — `LEGAL-IP-RULES.md` §5

- **Attribution gate (companion code).** At Step 4b the code-reviewer / example-builder **must confirm every companion-code file under `08-companion-code/NN_slug/` is ORIGINAL-FOR-THIS-BOOK** — written for this book, not a lightly-edited copy of an upstream sample, guide quickstart, or `_ref/` listing.
- Any file or block taken substantially verbatim from a specific the pinned authority set (00-strategy/SOURCE-PIN.md) listing must **name its guide/file and the pinned the pins in SOURCE-PIN.md** in the chapter sources; an unattributed verbatim copy is a gate **FAIL** (`LEGAL-IP-RULES.md` §5).

---

## 9. FLOOR C — SOURCE-TRACE / COMPILE (PASS / FAIL)

FLOOR C is the scoring floor this guide serves (`SCORING.md`). It is fatal regardless of the aggregate score.

- **PASS** — Zero invented `rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims` atoms **AND** the chapter's companion module compiles green via `./mvnw -B verify` at Java code quality the pins in SOURCE-PIN.md.
- **FAIL** — Any invented or unpinned source detail, **or** a module that does not build green. A FAIL blocks the chapter; fix the module and re-run, do not score around it.

A module can satisfy every cluster score and still fail to ship if FLOOR C fails. Floors are checked first, and FLOOR C is checked against the compiled artifact, never against a fragment that merely looks right.

---

## 10. The Step-4b checklist (what the `example-builder` confirms)

- [ ] Folder named with the frozen `NN_slug` under `08-companion-code/` (key from `FINAL_INDEX.md`).
- [ ] Module is a child of the ONE aggregator: parent set with a relative path back to the aggregator, no own group/version, no own BOM import — never a standalone project (§2, §3).
- [ ] Parent build file sets the platform pin to `the pins in SOURCE-PIN.md`; child module has no version literal (§3).
- [ ] Module added to the parent module list ONLY after green build AND CODE-REVIEW PASS (§6.1).
- [ ] Config file externalizes at least `%dev` and `%prod` (req. 2).
- [ ] `>=1` integration test exercises the chapter's mechanism (req. 3).
- [ ] Observability/health surface present where the topic touches it, or scoped-out reason recorded (req. 4, §1.2).
- [ ] Explicit failure path present and driven by a test — fault-tolerance policy, graceful-shutdown hook, or real error response (req. 5, §1.1).
- [ ] Every displayed book snippet is a `// tag::name[]` region inside a compiling file, within the snippet ceiling (§5).
- [ ] Build wrapper committed; `./mvnw -B verify` green locally (§4, §6).
- [ ] `Java 21+ (21 LTS anchor, 25 LTS forward)` baseline cites `SOURCE-PIN.md`, not a guess (§7).
- [ ] NEUTRALITY and never-invent hold across comments, identifiers, and log/error strings (§8).
- [ ] Every companion-code file confirmed ORIGINAL-FOR-THIS-BOOK; any substantially-verbatim block names its guide/file + pinned the pins in SOURCE-PIN.md (§8.3, `LEGAL-IP-RULES.md` §5).
- [ ] FLOOR C PASS recorded for Step 5 (§9).
- [ ] No public push; local build only until `COMPANION-REPO.md` §5 sign-off.

---

## Learnings & pipeline suggestions

- The failure-path requirement (§1.1) is the idea that earns its place: it forces the HONEST-LIMITATIONS floor out of prose and into a code path that a test actually drives, so a "sells-only" chapter cannot hide behind a happy-path demo. If a chapter genuinely has no failure path, that is itself a signal the topic may be thin (a DEPTH concern at scoring).
- Extending NEUTRALITY and never-invent explicitly to comments, identifiers, and log/error strings (§8) closes a real gap: a banned phrasing or an invented config key can slip into a log line that the prose audit never reads. Consider promoting a one-line note into `NEUTRALITY.md` that the banned-phrase pre-pass should also scan `08-companion-code/`.
- Requirement 4 (observability/health) is the one that varies by topic; the §1.2 "scoped-out reason recorded" rule keeps it from becoming padding on chapters with no surface to instrument. Watch the pilot for whether this exception is invoked honestly or as an escape hatch.
- Append confirmed lessons here to `00-strategy/PIPELINE-LEARNINGS.md` and promote durable ones into the relevant rule file per the continuous-improvement HARD RULE.
