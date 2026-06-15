# SOURCE-PIN — {{SUBJECT_SHORT}} Book

> The single frozen {{BOOK_SUBJECT}} reference every verification reads. One pin. One identifier. One fetch path. Do not drift.

Every {{INVENT_UNITS}} in this book MUST trace to the exact authority pinned here — never to a moving target, never to a newer release, never to an unverified secondary. If a fact cannot be traced to this pin, it is cut or marked `UNVERIFIED` and flagged to `09-flags/`.

> The pin is one instance of the kernel's **single pinned authority source** invariant. The *form* of the pin varies by book type (see `BOOK-TYPE-PROFILES.md`): a release **tag + commit SHA** for a code book, an **edition + year** for a literature/science book, a **corpus snapshot date** for a reference/narrative book. The discipline below — freeze it, fetch it, self-heal an ephemeral copy, re-pin only as a deliberate logged runbook — is invariant across all of them.

## The pin

| Field | Value |
|---|---|
| Authority source | **{{AUTHORITY_SOURCE}}** |
| Pin identifier | **{{AUTHORITY_PIN}}** (tag+SHA / edition+year / corpus snapshot date) |
| Fetch / clone path | `{{AUTHORITY_CLONE_PATH}}` |
| Primary-content subpath | `{{AUTHORITY_CLONE_PATH}}/<docs-or-corpus-subpath>` |
| Version property *(technical profile — see BOOK-TYPE-PROFILES.md; book types without {{GATES_ON}} build gates drop this)* | **`{{BUILD_VERSION_PROPERTY}}` = `{{AUTHORITY_PIN}}`** (the inherited build/BOM version) |
| Runtime baseline *(technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop this)* | **{{LANG_RUNTIME}}** |
| Date pinned | **{{PIN_DATE}}** |

### Why this pin
- State the reason this exact identifier was chosen and why it is stable for the life of the book (e.g. a long-term-support train, a citable edition, a fixed corpus snapshot). Stability is the point: **verification against a moving target is forbidden**, precisely because a moving source can change a {{INVENT_UNITS}} under us between two reads.

### The version property (worked examples) *(technical profile — see BOOK-TYPE-PROFILES.md; book types with {{GATES_OFF}} for the build gate delete this whole subsection)*

Every companion module under `08-companion-code/NN_slug/` pins {{BOOK_SUBJECT}} through a single inherited parent-build property:

| Property | Value | Source path in the pin |
|---|---|---|
| `{{BUILD_VERSION_PROPERTY}}` | `{{AUTHORITY_PIN}}` | the build/BOM artifact version resolved by the pinned source |

- This is the one knob that binds the printed build manifest, the build plugin, and every imported dependency to the pinned identifier. A module sets it once in the parent build file; child modules inherit it.
- A re-pin changes this property in exactly one place, then the whole companion tree is rebuilt against the new version (see the runbook below).

### Runtime baseline (first-class pinned fact) *(technical profile — see BOOK-TYPE-PROFILES.md; non-code books delete this whole subsection)*

| Fact | Value | Source path in the pin |
|---|---|---|
| Minimum runtime | **{{LANG_RUNTIME}} (minimum)** | the compiler/runtime floor declared in the pinned tree + the getting-started prerequisite |
| Recommended runtime | **{{LANG_RUNTIME}} (recommended)** | the recommended runtime documented for the pinned source |
| Toolchain *(if applicable)* | **{{BUILD_TOOLCHAIN}}** | the build/packaging toolchain prerequisite documented for the pinned source |

- The minimum is a hard floor: nothing in the book or in any companion module may assume a runtime below it.
- State the recommended runtime and any toolchain as the **pinned baseline**, not as a moving recommendation.

## Moving-target policy (HARD)

1. **NEVER verify against a development/snapshot stream** (e.g. a `*-SNAPSHOT`, `main`, a pre-print, an un-versioned wiki). The development branch is unstable; a {{INVENT_UNITS}} that exists there may never ship, or may ship renamed.
2. **NEVER verify against a newer release than the pin** (any non-pinned edition/GA). The book is pinned to one line, not to the latest. Facts that are only true past the pin are out of scope.
3. **Only the pinned identifier counts.** When in doubt, read the file under `{{AUTHORITY_CLONE_PATH}}/` at the pin above. Record the exact source path in the chapter's `_VERIFY.md`.

## Re-evaluation trigger

- State the next plausible re-pin target and its expected date (e.g. the next LTS, the next edition, the next corpus refresh).
- **Do NOT re-pin on that schedule by default.** Re-pin to the next identifier **only if** production of this book extends past the stated cutoff.
- A re-pin is a deliberate, logged event. It is never a fast operation: the pinned copy is fetched shallow/minimal and carries no other refs, so a re-pin is a full re-fetch plus a re-trace of every recorded source path plus a rebuild of every worked example. Budget it as a multi-step runbook, not a one-line edit.

## Re-pin runbook (costed)

A re-pin replaces the identifier, the fetch path, and the version property, then re-validates everything that was traced to the old pin. Run the steps in order; each one gates the next.

| # | Step | Why it costs | Gate |
|---|---|---|---|
| 1 | **Full re-fetch at the new identifier.** The pinned copy is fetched shallow/minimal, so it contains only the pinned state and no other refs — an in-place checkout to a new identifier cannot work against it. Delete the old `{{AUTHORITY_CLONE_PATH}}` and fetch the new identifier fresh with the same fetch command pattern. | A shallow/minimal copy has no history to walk; there is no in-place upgrade path. | New copy present; the identifier check prints the new pin. |
| 2 | **Update this file.** Replace the authority source (if changed), pin identifier, fetch path, content subpath, date pinned, and `{{BUILD_VERSION_PROPERTY}}`. Re-confirm the runtime baseline against the new tree *(technical profile)*; it may move. | The pin is the source of truth; the fetched copy is ephemeral. | All pin-table fields match the new identifier. |
| 3 | **Re-run the source-pin check** (`scripts/ensure_source_pin.sh` / `check_source_pin.sh`). It MUST hard-fail until the copy is present and on the new pin. | Catches a stale or absent copy before any fact is read. | Script exits zero. |
| 4 | **Re-trace every recorded source path.** Each banked fact records a source path in its chapter `_VERIFY.md`. Re-confirm each path still exists at the new pin and still says what it said — files are renamed, pages are renumbered, and {{INVENT_UNITS}} are deprecated across releases/editions. | A renamed source or a moved location silently invalidates a citation. | Every recorded path resolves at the new pin, or is re-traced/flagged to `09-flags/`. |
| 5 | **Scan for stale references.** Grep the whole tree (`02-research/`, `03-drafts/`, `04-approved/`, `08-companion-code/`, and the index files) for the old pin identifier and the old fetch path, and replace them. `01-index/FINAL_INDEX.md` is the single canonical chapter source — re-confirm each promoted source location still exists at the new pin. | Hardcoded old identifiers drift the book away from the pin. | No occurrence of the old identifier/fetch path remains except in the dated change log. |
| 6 | **Rebuild the worked examples.** *(technical profile — see BOOK-TYPE-PROFILES.md; book types with {{GATES_OFF}} for the build gate skip this step.)* Bump `{{BUILD_VERSION_PROPERTY}}` to the new value in the parent build file under `08-companion-code/`, then run `{{BUILD_CMD}}` across every `NN_slug` module on the verified runtime baseline. A red build blocks the chapter from its human gate. | The new version can change a dependency coordinate, a config key, or an API signature that a module depends on. | Every companion module builds green at the new version. |
| 7 | **Human re-confirm `01-index/FINAL_INDEX.md`** and resume drafting. | Chapter selection is a human gate; a re-pin can change what is in scope. | Human sign-off recorded. |

Re-verify every previously banked fact against the new pin before drafting continues. Steps 4 through 6 are the expensive ones — plan the re-pin as a dedicated unit of work, committed per batch.

## WARNING — the fetched copy may live in ephemeral storage

The copy at `{{AUTHORITY_CLONE_PATH}}` may be **ephemeral** (e.g. under `/tmp`). A machine reboot, a storage sweep, or a fresh environment will delete it. This is expected. The pin in this file — not the copy on disk — is the source of truth.

- If the copy is **absent**, or present but **off-pin** (any identifier other than `{{AUTHORITY_PIN}}`), the source-pin check MUST **hard-fail** and block the pipeline.
- Re-fetch the copy with exactly this command before any verification proceeds:

```sh
# {{FETCH_CMD}} — e.g. for a code book:
# git clone --branch {{AUTHORITY_PIN}} --depth 1 \
#   {{REPO_REMOTE}} {{AUTHORITY_CLONE_PATH}}
{{FETCH_CMD}}
```

- After re-fetching, confirm the copy is on-pin (the identifier check must print `{{AUTHORITY_PIN}}`) and re-run the source-pin check before reading any fact.

## Sources and further reading

**Primary / official**
- {{AUTHORITY_SOURCE}} release/edition policy and cadence — `<url>`
- The pin announcement / changelog for `{{AUTHORITY_PIN}}` — `<url>`
- {{AUTHORITY_SOURCE}} at the pinned identifier — `<url>`

**Accessible / further reading**
- Release/edition history and access — `<url>`
- Support and versioning policy — `<url>`
