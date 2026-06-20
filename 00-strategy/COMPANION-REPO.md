# COMPANION CODE REPOSITORY — SPEC — Java code quality

> **(technical/code profile — see BOOK-TYPE-PROFILES.md; book types without runnable examples drop this spec along with the EXAMPLES-GUIDE and the DEMO-CATALOG. Active only when `research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble` includes example-build + code-review.)**

> ## STATUS: APPROVED IN PRINCIPLE — local module build active under `08-companion-code/`; PUBLIC push remains gated on the §5 human/legal sign-off.
>
> The strategy is **approved in principle**: companion modules are built **locally now** inside the
> in-repo `08-companion-code/NN_slug/` tree, exercised by `./mvnw -B verify`, and kept in lock-step
> with the prose via the `NN_slug` key and the tag-includes (§2.5). What remains gated is the
> **public-facing action**: creating and pushing a public repository at `{URL}` touches the project's IP,
> legal, and trademark posture, and must **not** be executed until:
>
> 1. The human gives **explicit sign-off** to publish the repo at all.
> 2. The license choice is **reconciled with `LEGAL-IP-RULES.md`** (code vs. book text).
> 3. The arrangement is **reconciled with the eventual publisher contract**, which may relicense or
>    constrain the sample code.
>
> Until that sign-off, no `git`-remote, no `gh`, no public repo, no remote, no push to `{URL}` is implied or
> authorized by this file. Local building under `08-companion-code/` is active and is **not** an
> outward-facing action. The open decisions still blocking publication are listed in §5.

This file turns the verified companion-code proposal from the book-craft research into a clean, actionable
specification for **this** book. The strategy is approved in principle and the modules are built
locally now under `08-companion-code/NN_slug/`; only the public push remains gated (§5). It inherits
the book's law: `neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X` (`NEUTRALITY.md`), never invent a source
(`LEGAL-IP-RULES.md` §3, `SOURCE-PIN.md`), and the locked `the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md` (`VOICE-GUIDE.md`). Where
this spec would collide with that law, the law wins.

---

## 1. Why a companion repo (the convention, briefly)

A technical book needs a place where its code actually compiles and runs. Readers expect
it, and the strongest published books ship one. The repo is also the structural defense against the
one failure that most reliably betrays a machine author: a listing in the printed book that does not
compile. That makes the repo part of the authenticity gate, not a courtesy.

The convention is well established. Survey exemplar repositories during instantiation and cite them as
**evidence rather than as targets to copy** — common, durable patterns to look for:

- **Per-chapter folders, zero-padded** — per-chapter directories with section subfolders.
- **Snippet and compilable file as ONE artifact via anchor comments** — the canonical anti-drift
  technique: store full files and pull only the anchored region into the prose at build time.
- **initial/ and complete/ variants for hands-on guides** — some guides ship both, plus a
  separate writing license.
- **CI that builds/tests every chapter on push/PR** — CI wired to the per-chapter folders.
- **Errata via Issues, PRs declined, code matches the printed edition** — keeps the repo in lock-step
  with the printed book.
- **Editions via a protected published branch + revision tracking** — protect the published branch and
  track fixes per edition.

These are cited as prior art. This book's repo follows its own `NN_slug` law (§3), not any one of
their folder schemes verbatim, and must not read as derivative of an existing title (§3, repo name).

---

## 2. The spec for this book

### 2.1 Repo name (decision deferred to the human, §5)

Candidate names, all chosen to avoid echoing an existing title so the repo does not read as derivative:

- `<book-slug>-code`
- `<book-slug>-examples`

The final book slug is not yet locked; the name is an **open decision** (§5).

### 2.2 Layout — keyed to the book's own `NN_slug` law

Top-level folders are named with the **same dossier key** already used across `01-index/`,
`03-drafts/`, and `04-approved/`: lowercase, underscores, two-digit zero-padded — for example
`07_database/`. The chapter number from `01-index/CANDIDATE_POOL.md` (and, after the cull,
`FINAL_INDEX.md`) is the **single source of truth** across prose, drafts, and code.

```
<repo-root>/
├── <parent build file>          # parent/aggregator: pins the platform once (§2.4)
├── <committed build wrapper>    # committed build wrapper / lockfile (§2.4)
├── README                       # exceeds the publisher floor (§2.5)
├── LICENSE / LICENSE.writing    # code vs. prose, pending legal sign-off (§4)
├── .github/workflows/           # CI: ./mvnw -B verify across the NN_slug matrix (§2.6)
├── 01_<slug>/                   # one self-contained module per chapter
│   ├── <child build file>       # inherits the parent; no version literal here
│   └── src/...                  # full compilable files with tag-include markers (§2.3)
├── 07_database/
│   └── ...
└── ...                          # one folder per selected chapter, NN_slug from 01-index
```

### 2.3 Running-example strategy — modules of the ONE parent aggregator

**The public repo IS the parent aggregator plus its `NN_slug` modules — one reactor, not a bag of standalone projects.** The repo-root build file is a single aggregator/parent that pins Java code quality once (§2.4); every chapter's `NN_slug/` directory is a child `<module>` of it whose build file declares the parent (with a relative path back to the aggregator), carries no version literal, and imports no BOM of its own. A module joins the parent module list only after it builds green and passes CODE-REVIEW. The reference chapter's module is the reference shape.

Because the book selects the chapters listed in `01-index/FINAL_INDEX.md` across many independent Java code quality features, each
`NN_slug/` module is **topic-independent and self-contained to run**: a reader can build and run a single
chapter on its own (the single-module build of `NN_slug` against the parent) without building any prior chapter.
"Self-contained" means topic-independent, **not** a separate project — it is still a child of the one
aggregator. This is an inference from the book's shape, recorded as such.

A through-line application is **not** part of the per-chapter proposal: the per-chapter `NN_slug/`
modules stay independent and self-contained, one per `NN_slug`. The "compose several areas into a
runnable app" need is met instead by the **capstones** — three microservice applications under
`08-companion-code/capstones/` (`DEMO-CATALOG.md` §4): a shared `shared-platform` library plus
`01-commerce-checkout`, `02-fintech-ledger`, and `03-logistics-fulfil`, each an aggregator of
independently-runnable services that talk over HTTP. The capstones are the **only** place cross-module
wiring is allowed; they are gated on their own track (`mvn -B -Pquality verify` + CODE-REVIEW) and
assembled in Phase 4. So the old "through-line vs. independent" decision resolves as: per-chapter
modules are independent; cross-module composition lives in the three capstones, each on its own domain.

### 2.4 Toolchain pin — one inherited property, committed wrapper

- **Java code quality version is pinned in exactly one place.** The parent/aggregator build file sets
  the platform pin to `the pins in SOURCE-PIN.md`; every `NN_slug/` module inherits it and carries **no** version literal
  of its own. A future re-pin is then a one-line edit,
  matching `SOURCE-PIN.md`'s single-source-of-truth discipline.
- **The exact property value and its placement must be confirmed against a real generated build file in the
  pinned clone** (the per-tool fetch dirs in SOURCE-PIN.md) before anything is committed — never assumed. Same
  never-invent law as the prose.
- **The build wrapper is committed** (wrapper script(s) + bootstrap dir / lockfile) so the build is reproducible
  without a preinstalled toolchain. The documented entry points are the dev/run command and
  `./mvnw -B verify` (verify both against the pin's tooling guide before stating either in the
  README).

### 2.5 Listing sync — the tag-include mechanism (the anti-drift core)

This is the structural reason book and repo cannot silently diverge.

- The repo stores **full, compilable** source, config, and build files.
- The region the book displays is wrapped in tag markers in that same file:

  ```
  // tag::name[]
  ... the bounded region the book shows ...
  // end::name[]
  ```

- The manuscript **references** the tagged region rather than pasting a copy. The displayed snippet and
  the compilable file are therefore **one artifact**: edit the file, the book updates; the file is
  what CI compiles. Drift between the printed listing and the runnable code becomes structurally
  impossible.
- The displayed region stays within the book's **snippet ceiling** (`LEGAL-IP-RULES.md` §2);
  the surrounding full file may be longer because it is never reproduced in the prose.
- For inline, non-numbered code, adopt a `no-listing-*` convention so **every** snippet
  in the book has a home in the repo.
- **How the reference resolves (honest mechanics).** The markers use the upstream documentation's `tag::`/`end::` syntax for
  consistency and forward-compatibility. But this book's manuscript is **Markdown**, which has no native include —
  so the regions are **not** resolved by a native include. Instead, the prose carries a stable marker
  `<!-- include: NN_slug/path/File.ext#name -->` (invisible in rendered Markdown), and `.claude/scripts/extract_snippet.sh`
  slices the tagged region (asserting it is within cap) into a fenced block at assembly (Step 14). `.claude/scripts/check_snippets.sh`
  is the gate that every marker in `04-approved/` resolves to an existing within-cap region in a compiling file.

### 2.6 CI — a CI matrix that is effectively a HARD gate

- A workflow runs `./mvnw -B verify` across a **matrix of the `NN_slug` chapter modules**, on the
  `Java 21+ (21 LTS anchor, 25 LTS forward)` baseline recorded in `SOURCE-PIN.md`, pinned to Java code quality the pins in SOURCE-PIN.md.
- If any chapter module stops compiling, **the build fails** and a green badge is surfaced in the
  README.
- A non-compiling listing is the fastest way to fail the book's authenticity gate ("a sharp
  developer could not tell it's AI-produced", `LEGAL-IP-RULES.md` §8). So CI is treated as an
  **effective HARD gate** for the companion code, not a nicety.

### 2.7 README — exceed the publisher floor

The publisher minimum is a one-line ISBN attribution + clone instruction + catalog link + errata
link. This README exceeds it with:

- Exact **title and edition**.
- A **chapter → folder map** (`NN_slug`), so prose and code resolve to each other.
- **Prerequisites**: the **`Java 21+ (21 LTS anchor, 25 LTS forward)` baseline from `SOURCE-PIN.md`**, **Java code quality the pins in SOURCE-PIN.md**,
  and the toolchain via the committed wrapper. `SOURCE-PIN.md` is the single source of truth for this value.
- **One copy-paste build/run command** (confirmed against the pin).
- The **CI badge**.
- **Errata via Issues, PRs declined** instructions, so the repo always matches the printed edition.
- **No version number, config key, or dependency coordinate** that the book has not itself verified against the pin.

### 2.8 Errata and editions

- **Errata via Issues; pull requests declined** — so the published
  code keeps matching the printed edition; accepted reports feed the next edition.
- **Edition tagging at manuscript freeze**: cut a release tag (e.g. the the pins in SOURCE-PIN.md pin or
  `v1.0-manuscript`) so a reader's edition always has a matching checkout even after the default branch advances
  to a newer pin. Do future re-pins on an **edition branch**, not by overwriting the published
  branch.

### 2.9 How the book text references listings

In running prose, refer to a listing by **what it does** (the `the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md` rule). Where a
number is unavoidable, use "Listing N" mapped to a named repo path, and ensure every numbered
listing or figure has a specific in-text reference **before** it appears.

---

## 3. Licensing (FLAG FOR LEGAL SIGN-OFF — not decided here)

This is a legal decision. It is **proposed**, not committed. Reconcile with
`LEGAL-IP-RULES.md` and the publisher contract before any license file lands.

- **CODE → propose a permissive OSS license that matches Java code quality's own license** (`LEGAL-IP-RULES.md` §5),
  so readers can reuse the samples inside their own apps without a license mismatch.
- **Committed PROSE / FIGURES → a separate license/notice**, distinct from the code `LICENSE`
  (e.g. a `LICENSE.writing` file alongside).
- **The book TEXT stays under its own copyright** and is **not** relicensed by the code repo. The
  manuscript prose in `04-approved/` is the book's copyrighted work (§5).
- **Do not commit any license file unilaterally.** See `LEGAL-IP-RULES.md` and the open decisions
  in §5.

---

## 4. Relationship to the in-repo pipeline

There are three distinct artifacts with three distinct purposes:

| In-repo `04-approved/` | In-repo `08-companion-code/NN_slug/` | Public companion repo |
|---|---|---|
| Holds the **approved PROSE** (the manuscript) | Holds the **runnable CODE, built locally now** | The eventual **published** code, after sign-off |
| Governed by the **book's copyright** | Internal, tracked, built by `./mvnw -B verify` | Code under the proposed OSS license (§3) |
| Per-chapter **approval commit** discipline | Per-chapter module keyed by frozen `NN_slug` | Separate, **outward-facing** public artifact |
| Internal pipeline output | Internal pipeline output | Reader-facing deliverable, **gated on §5** |

**The companion modules are built under the in-repo `08-companion-code/NN_slug/` tree now**, exercised
by `./mvnw -B verify`, and are **only PUBLISHED to a public repo after the §5 human/legal sign-off**.
Local building is internal and is **not** an outward-facing action; the public push is.

**The bridge across all three is the `NN_slug` key + the tag-includes.** An approved chapter's listing
is a `// tag::name[]` / `// end::name[]` region inside the full compilable file in
`08-companion-code/NN_slug/`, and the manuscript **includes** that region rather than copying it
(§2.5). So the prose in `04-approved/`, the local module, and — after sign-off — the published repo
can **never silently diverge**: a code edit either flows into the included snippet or fails the build.

Step 4b (**EXAMPLE-BUILD**) in `PIPELINE.md` is the gate that **populates this tree** — it produces,
per chapter, the one self-contained enterprise-grade module under `08-companion-code/NN_slug/`,
pinned to Java code quality the pins in SOURCE-PIN.md, that Step 5 (VERIFY) then checks the prose against. The public repo
described in §2 is the eventual publication target for exactly these modules, once §5 clears.

The public repo carries the **same law as the prose**:

- **neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X** — no crowning of Java code quality over an alternative anywhere in the README, comments, or commit messages
  (`NEUTRALITY.md`).
- **Never invent a source** — no `rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims` atom that has not been verified against the
  pin (`LEGAL-IP-RULES.md` §3, `SOURCE-PIN.md`). The repo is held to the pinned Java code quality the pins in SOURCE-PIN.md the
  same way the book is.

---

## 5. Open decisions for the human (checklist)

Local building under `08-companion-code/` is approved in principle and active. The items below gate
the **public push**, not the local build. Each must clear before the modules are published.

- [ ] **Approve or deny PUBLIC repo creation/push at all.** This is the outward-facing gate; no
      public repo, remote, or push to `{URL}` proceeds without it.
- [ ] **Repo name** — a `<book-slug>-code` / `<book-slug>-examples` candidate (§2.1). Depends on the
      locked book slug.
- [ ] **`Java 21+ (21 LTS anchor, 25 LTS forward)` baseline** — recorded in `SOURCE-PIN.md`, verified against the per-tool fetch dirs in SOURCE-PIN.md.
      README and CI both read that single source of truth (§2.6, §2.7).
- [ ] **License confirmation** — a permissive OSS license for code + a separate prose/figure notice, reconciled
      with `LEGAL-IP-RULES.md` and the publisher contract (§3).
- [ ] **Through-line app vs. independent modules** — default is independent, self-contained
      per-chapter modules; a through-line app, if chosen, needs its own folder and a non-overlapping
      domain (§2.3).

---

## Learnings & pipeline suggestions

- This spec exists because the dossier flagged repo creation as outward-facing. Splitting the work
  into a local build (approved in principle, active under `08-companion-code/`) and a gated public
  push keeps the IP/legal posture intact while still letting Step 4b populate runnable modules now.
  If more outward-facing artifacts arise (a website, a package publish), give each the same
  STATUS-banner-plus-open-decisions treatment.
- The tag-include mechanism (§2.5) is the load-bearing idea: it converts "keep the book and code in
  sync" from a manual discipline into a structural guarantee enforced by CI. Consider promoting a
  one-line note into `LEGAL-IP-RULES.md` §2 that the snippet ceiling applies to the **displayed**
  tagged region, while the full backing file may be longer because it is never reproduced in prose.
- The `Java 21+ (21 LTS anchor, 25 LTS forward)` baseline is a recurring "do not guess" trap — record the verified value in `SOURCE-PIN.md`
  once confirmed against the pin, so the README and CI both read one source of truth.
- Append confirmed lessons here to `00-strategy/PIPELINE-LEARNINGS.md` and promote durable ones into
  the relevant rule file per the continuous-improvement HARD RULE.
