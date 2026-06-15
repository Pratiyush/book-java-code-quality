# BOOK-TYPE-PROFILES — the kernel's only knobs

> Every book built from `.foundation/` is the **same gated pipeline**. What changes is a small set of
> parameters, resolved once at instantiation from the profile that matches your book. Pick a profile,
> copy its placeholder values into the instantiated files, and delete the gates your type marks "off".
>
> A profile is just a column of values for the `{{PLACEHOLDER}}` tokens used across `templates/`,
> `agents/`, `commands/`, and `scripts/`. Add a new profile by adding a column — that is how the kernel
> grows "based on book type".

## The placeholder tokens

| Token | Meaning |
|---|---|
| `{{BOOK_SUBJECT}}` | the full subject (e.g. "Quarkus", "the origins of life", "pricing strategy") |
| `{{SUBJECT_SHORT}}` | short label used in headings/filenames |
| `{{AUTHORITY_SOURCE}}` | the single pinned source of truth every fact traces to |
| `{{AUTHORITY_PIN}}` | how that source is frozen (a tag+SHA, an edition+year, a corpus snapshot date) |
| `{{INVENT_UNITS}}` | the atoms that must never be invented (config keys / claims / stats / quotes …) |
| `{{NEUTRALITY_STANCE}}` | the fairness rule and its banned phrasings |
| `{{EXAMPLE_POLICY}}` | what a worked example is, and whether it is gated by a build |
| `{{FIGURE_POLICY}}` | the per-chapter image budget and production method |
| `{{VOICE}}` | the one locked voice (person, contractions, hedging, warmth rules) |
| `{{SCORING_CLUSTERS}}` | the five 1–10 quality clusters for this genre |
| `{{FLOORS}}` | the pass/fail content-floors that apply |
| `{{SHIP_BAR}}` | the numeric ship bar |
| `{{GATES_ON}}` / `{{GATES_OFF}}` | which pipeline gates are active for this type |
| `{{AUTHORITY_CLONE_PATH}}` | local path where the pinned source is fetched (code books — ephemeral; e.g. `/tmp/<subject>-<tag>`) |
| `{{REPO_REMOTE}}` | the book project's git remote URL |
| `{{BUILD_CMD}}` | the example build/verify command — *technical profile only* (e.g. `./mvnw -B verify`) |
| `{{LANG_RUNTIME}}` | the runtime/toolchain baseline — *technical profile only* (e.g. "Java 17+/21") |

### Additional tokens (paths, build & script CONFIG)

The core tokens above are the policy knobs. The kernel templates and scripts also use these finer-grained tokens; resolve them once at instantiation (most have an obvious default). `{{TOKEN}}` appearing in how-to prose is the literal word, not a real placeholder.

| Token | Meaning / default |
|---|---|
| `{{REPO_URL}}` `{{TAG}}` `{{SHA}}` `{{CLONE_PATH}}` `{{FETCH_CMD}}` `{{PIN_DATE}}` `{{DOCS_SUBDIR}}` | source-pin / script CONFIG values (code books). Set in `SOURCE-PIN.md` and each script's `# CONFIGURE PER BOOK` block. |
| `{{BUILD_TOOLCHAIN}}` `{{BUILD_VERSION_PROPERTY}}` | the build tool + the single inherited version property — *technical profile only* (e.g. Maven; `quarkus.platform.version`). |
| `{{SUBJECT_LICENSE}}` | the authority source's license (e.g. ASL-2.0), for `LEGAL-IP-RULES`. |
| `{{LEDGER_REL}}` `{{TRACKER_REL}}` `{{PIN_FILE_REL}}` `{{DRAFTS_DIR_REL}}` `{{APPROVED_DIR_REL}}` `{{CODE_ROOT_REL}}` `{{EXTRACT_REL}}` | repo-relative paths; defaults are the standard layout (`LEDGER.md`, `01-index/CHAPTER-TRACKER.md`, `00-strategy/SOURCE-PIN.md`, `03-drafts/`, `04-approved/`, `08-companion-code/`, `.claude/scripts/extract_snippet.sh`). |

## The editorial / production / QA roles — ON/OFF per book type

The publishing-house org-chart roles (PIPELINE Steps 3a, 3b, 4d, 6b, 8a, 11b, 11c, 14a, 15, 15a) are mostly **profile-independent** — a book is a book — with two knobs. The technical-only roles ride the same switch as `example-build`/`code-review`; the market role rides whether the book is commercially published.

| Role (step) | Owner artifact | A Technical | B Science | C Business | D Reference | E Narrative | Switch |
|---|---|---|---|---|---|---|---|
| `developmental-editor` (3b ARC-REVIEW) | LEARNING-ARC + CONCEPT-MAP + ARC-REVIEW | ON | ON | ON | ON | ON | all books |
| `copyeditor` (6b COPYEDIT) | …_COPYEDIT vs STYLE-SHEET | ON | ON | ON | ON | ON | all books |
| `reader-advocate` (8a READER-SIM) | …_READERSIM | ON | ON | ON | ON | ON | all books |
| `front-matter-author` (14a FRONT-MATTER) | 00_front-matter.md | ON | ON | ON | ON | ON | all books |
| `production-proofreader` (15 PRODUCTION-PROOF) | PROOF-REPORT + check_crossrefs.sh | ON | ON | ON | ON | ON | all books |
| `indexer` (15a INDEX-BUILD) | INDEX.md | ON | ON | ON | ON | ON | all books |
| `production-manager` (11b SCHEDULE, 11c PROCESS-CHECK) | SCHEDULE.md + check_process.sh | ON | ON | ON | ON | ON | all books |
| `market-analyst` (3a MARKET-FIT) | AUDIENCE + ACQUISITION-BRIEF + MARKET-ANALYSIS | ON | ON | ON | ON | ON | **any commercially-published book** (OFF for an internal/non-commercial volume) |
| `repro-proofer` (4d REPRO) | …_REPRO + repro-env-matrix.yml | ON | OFF | OFF | OFF¹ | OFF | **technical profile only** (rides the `{{GATES_ON}}`/`{{GATES_OFF}}` example-build switch; JDK-gated) |

¹ Reference/cookbook: ON only if the compile floor is on (runnable recipes) — same condition as `example-build`.

**Notes.** `AUDIENCE.md` (from 3a) is the persona source the 8a READER-SIM gate judges against; on a non-commercial volume where MARKET-FIT is OFF, supply a minimal audience definition in `LEARNING-ARC.md` so READER-SIM still has a target reader. The technical-only `repro-env-matrix.yml` and `check_crossrefs.sh`/`check_process.sh` follow the same `[BUILT]`-vs-`[MANUAL]` honesty as the rest of the kernel — see PIPELINE.

## The AI-authorship roles — because every book here is machine-written

Every book built from this kernel is AI-produced, so a second role family exists specifically to keep that defensible: two **independence gates** (run on a *different model/persona than the drafter*), the **accessibility** editor, the post-approval **errata** intake, and the **provenance** disclosure. These are **profile-independent — ON for every book type** (a book is AI-written regardless of subject), with only the usual `(technical profile)` shading of *what* gets checked.

| Role (step) | Owner artifact | A Technical | B Science | C Business | D Reference | E Narrative | Switch |
|---|---|---|---|---|---|---|---|
| `originality-checker` (5b ORIGINALITY-SCAN) | …_ORIGINALITY.md | ON | ON | ON | ON | ON | all books — **HARD; run on a different model/persona than the drafter** |
| `red-teamer` (8b RED-TEAM) | …_REDTEAM.md | ON | ON | ON | ON | ON | all books — **HARD; run on a different model/persona than the drafter** |
| `accessibility-editor` (9c A11Y) | alt-text/long-desc in figNN_x.sources.md + …_A11Y.md | ON² | ON² | ON² | ON² | ON² | all books — **soft (FIX); escalates to HARD at 15 PRODUCTION-PROOF** |
| `book-maintainer` (13a ERRATA-INTAKE) | 09-flags/ERRATA.md | ON | ON | ON | ON | ON | all books — soft, post-13 (living book); re-pin/update cadence on a new edition/tag |
| `provenance-officer` (14b PROVENANCE) | 06-assembly/AI-DISCLOSURE.md + 00-strategy/PROVENANCE-LOG.md + per-chapter stamp | ON | ON | ON | ON | ON | all books — soft, alongside 14a |

² A11Y runs wherever a book has figures (any type may); a book with no figures still checks code/listing readability. `(technical profile)`: A11Y also polices "no code-as-image" and ORIGINALITY/RED-TEAM treat verified ≤9-line code snippets as VERIFY's lane, not theirs.

**The two HARD AI-authorship gates push the kernel's HARD-gate count from 18 → 20** (full enumeration lives in `PIPELINE`). **Independence is the whole point of 5b + 8b:** a model is the worst judge of its own regurgitation (ORIGINALITY) and re-blesses its own blind spots (RED-TEAM), so both MUST be spawned on a different model/persona than the drafter — this is stated in their specs and in `PIPELINE`.

### `series-editor` — a PORTFOLIO role (OFF for a single title)

`series-editor` is **not** a per-book gate and **not** in either table above. It is a **portfolio / kernel role** that activates only when a **second** book is instantiated from this kernel — then it owns the cross-title shared canon/glossary, guards against contradiction and duplication across titles, and makes the "which book owns this topic" call. For a **single-title** project it is **dormant — OFF** (the instance ships a thin `.claude/agents/series-editor.md` stub; the generalized primary is `.foundation/agents/series-editor.md`). With one book, per-book continuity is already owned by the `reconciler` (RECONCILE) and `book-maintainer` (LEDGER); inventing a phantom sibling to give series-editor work would violate never-invent.

## Profiles

### A. Technical / framework / developer book  *(this Quarkus book — the reference instance)*
- `{{AUTHORITY_SOURCE}}` = the framework's source repository + docs; `{{AUTHORITY_PIN}}` = a release **tag + commit SHA**, cloned locally.
- `{{INVENT_UNITS}}` = config keys, version numbers, API signatures, CLI flags, GAV/dependency coordinates, benchmarks.
- `{{NEUTRALITY_STANCE}}` = framework-agnostic by total omission; banned: "better than / unlike X / superior / beats"; cross-framework claims need a cited source; one migration/comparison carve-out.
- `{{EXAMPLE_POLICY}}` = one runnable, enterprise-grade module per chapter; the displayed snippet is a tag-region inside the compiled file; **built green** (`mvn/gradle verify`).
- `{{FIGURE_POLICY}}` = per-chapter image budget; designed diagrams authored as **HTML → rendered PNG** (never image-generated); native-tool screenshots captured.
- `{{FLOORS}}` = A NEUTRALITY · B HONEST-LIMITATIONS · **C SOURCE-TRACE / COMPILE / CODE-REVIEW**.
- `{{GATES_ON}}` = research, source-verify, **example-build + code-review + repro**, verify, clarity, audit, score, reconcile, human-approve, assemble.

### B. Science / popular-science book
- `{{AUTHORITY_SOURCE}}` = the peer-reviewed literature + authoritative reviews; `{{AUTHORITY_PIN}}` = a frozen bibliography (DOIs, author-year, access date).
- `{{INVENT_UNITS}}` = empirical claims, quantities, dates, attributions, citations.
- `{{NEUTRALITY_STANCE}}` = present competing hypotheses fairly; no crowning of a contested theory; mark consensus vs. frontier.
- `{{EXAMPLE_POLICY}}` = thought experiments and analogies; no runnable code → **FLOOR C compile gate OFF** (source-trace only).
- `{{FIGURE_POLICY}}` = conceptual diagrams (HTML→PNG) and rights-cleared data figures; every number traced.
- `{{FLOORS}}` = A NEUTRALITY (balance) · B HONEST-LIMITATIONS (uncertainty stated) · **C SOURCE-TRACE** (no compile).
- `{{GATES_OFF}}` = example-build, code-review, repro. (Use citation-verify in their place.)

### C. Business / how-to / self-help book
- `{{AUTHORITY_SOURCE}}` = cited studies, named case studies, primary data; `{{AUTHORITY_PIN}}` = a fixed source list + access dates.
- `{{INVENT_UNITS}}` = statistics, case facts, named outcomes, quotations.
- `{{NEUTRALITY_STANCE}}` = vendor- and methodology-neutral; no silver bullets; trade-offs explicit.
- `{{EXAMPLE_POLICY}}` = worked scenarios, checklists, templates, decision tables — gated for internal consistency, not compiled.
- `{{FLOORS}}` = A NEUTRALITY · B HONEST-LIMITATIONS (when-NOT-to-use) · **C SOURCE-TRACE**.
- `{{GATES_OFF}}` = example-build, code-review, repro.

### D. Reference / cookbook
- `{{AUTHORITY_SOURCE}}` = the official documentation/spec; `{{AUTHORITY_PIN}}` = a versioned docs snapshot.
- `{{EXAMPLE_POLICY}}` = recipes/snippets; **compile floor optional** (on if the recipes are runnable).
- `{{FIGURE_POLICY}}` = tables and listings carry the load; few diagrams.
- `{{FLOORS}}` = B HONEST-LIMITATIONS · C SOURCE-TRACE (+ COMPILE if recipes run). Neutrality light.

### E. Narrative non-fiction / memoir / history
- `{{AUTHORITY_SOURCE}}` = primary sources, interviews, archives; `{{AUTHORITY_PIN}}` = a fixed source/interview log.
- `{{INVENT_UNITS}}` = quotes, dates, events, names — **nothing fabricated**; mark reconstruction.
- `{{NEUTRALITY_STANCE}}` = fairness to subjects; disclose perspective.
- `{{EXAMPLE_POLICY}}` = none (scenes, not examples); `{{FIGURE_POLICY}}` = rights-cleared photos/maps.
- `{{FLOORS}}` = B HONEST-LIMITATIONS (caveats/uncertainty) · C SOURCE-TRACE (no compile). Voice cluster weighted up.
- `{{GATES_OFF}}` = example-build, code-review, repro.

## How to add a profile

1. Add a column above (or a new `### F. …` block) with the token values for the new type.
2. If the type needs a gate the kernel doesn't have, add the agent/command to `agents/`+`commands/` and list it under `{{GATES_ON}}`.
3. Bump the kernel version in `CHANGELOG.md`.
