# RESEARCH DOSSIER TEMPLATE — Java code quality Book

> One dossier per chapter, banked before drafting. This template is engineered to be **verifiable against
> the pinned authority set (00-strategy/SOURCE-PIN.md)**. Fill every section. If a fact cannot be traced to the pinned authority set (00-strategy/SOURCE-PIN.md), mark it
> `⚠ UNVERIFIED` and flag to `09-flags/` — never invent.
>
> **Pin (HARD):** Every rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims and every snippet/example MUST trace to the pinned
> Java code quality the pins in SOURCE-PIN.md, clone/fetch at `the per-tool fetch dirs in SOURCE-PIN.md`. If a feature, detail, or
> signature exists **only ahead of the pin** (a newer/unreleased state the project has not pinned), do NOT
> present it as fact: record it as `⚠ AHEAD-OF-PIN` with the source and flag it to `09-flags/`.

---

## Topic
- **Key:** [NN — dossier key from FINAL_INDEX.md / CANDIDATE_POOL.md]
- **Title:** [Topic name]
- **Part:** [Part number and name]
- **Tier:** [A / B / C]
- **Java code quality pin:** the pins in SOURCE-PIN.md
- **Primary dependency / source unit(s) (rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims):** [the exact coordinates the chapter draws from]
  *(technical profile — see BOOK-TYPE-PROFILES.md; for a non-code book this is the primary source unit(s) the chapter draws from)*
- **Canonical doc page(s):** [the canonical doc reference(s) under the per-tool fetch dirs in SOURCE-PIN.md + the public URL/citation]
- **Canonical source path(s):** [the module/location under the per-tool fetch dirs in SOURCE-PIN.md the chapter draws from]

## 1. Core definition & purpose
- Central claim / what this feature *is* and the problem it solves
- Which part of the pinned authority set (00-strategy/SOURCE-PIN.md) provides it (rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims coordinates)
- When introduced / which version; design notes if documented — confirm the behavior still holds at the pins in SOURCE-PIN.md
- Where it sits in the larger architecture / model
  *(technical profile: the build-time vs runtime split)*

## 2. Mechanism (the spine of the chapter)
- Step-by-step: how it works internally
- **Setup / build-time behavior** — what happens before the feature is live *(technical profile: name the processor / build step / build items)*
- **Active / runtime behavior** — what happens when it is exercised
- Key components/interfaces (with source paths, verified @the pins in SOURCE-PIN.md)
- **Reference units (rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims)** — table form

  | Name | Type | Default | Fixed early? | Source |
  |---|---|---|---|---|
  | | | | | |

## 3. Evidence FOR
- **Verified examples** (bounded per one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green, each traced to a doc or source file @the pins in SOURCE-PIN.md — record the path)
- Quantitative figures, if officially published (cite exactly)
- Real-world usage, official tutorials, first-class tooling support
- Maturity signals (GA / stable / endorsed)

## 4. Evidence AGAINST / limits
- Known limitations, sharp edges, open issues (link the record)
- **Environment/compatibility caveats** — what breaks where, required workarounds *(technical profile: native-image / reflection / registration needs)*
- Competing approach *inside* Java code quality — neutral framing
- "When NOT to reach for this"
- Performance / cost / trade-offs

## 5. Current status
- Last ~3–5 years of movement: gaining / stable / superseded / merged
- Version compatibility window; deprecations
- Stability: stable / preview / experimental / deprecated (use Java code quality's own label, as of the pins in SOURCE-PIN.md)

## 6. Runnable example spec (seeds the Step-4b companion module) *(technical profile — see BOOK-TYPE-PROFILES.md)*

> This stub lets the dossier hand the example-builder a buildable target. It is a SPEC, not code: name the
> intended artifact, the dependencies to pull, the enterprise-grade elements the artifact must demonstrate
> (including an explicit failure path), the run command, and the expected output. Every rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims
> named here MUST trace to the pin (the pins in SOURCE-PIN.md) or carry `⚠ UNVERIFIED` / `⚠ AHEAD-OF-PIN` and a
> flag to `09-flags/`. The artifact lands under `08-companion-code/NN_slug/`, inherits the pin property
> from the parent descriptor, and must build green under `./mvnw -B verify`. *Book types with the build/compile gate turned off
> for example-build replace this with a worked-scenario spec — verified for internal consistency and
> source-trace, not compiled.*

- **Catalog demo:** point to this `NN_slug`'s row in `DEMO-CATALOG.md` — name the demo, the Java code quality
  surface it exercises, and the TRY-IT exercise — so the drafter has the worked example fixed before
  drafting (no example invented at draft time).
- **Module key / path:** `08-companion-code/NN_slug/` (frozen `NN_slug` from FINAL_INDEX.md)
- **Intended dependencies (rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, verified @the pins in SOURCE-PIN.md):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @the pins in SOURCE-PIN.md |
  |---|---|---|---|
  | platform pin (inherited via the pin property) | establishes the pin | | ☐ |
  | | primary unit under study | | ☐ |
  | test harness | the test harness (canonical at the pins in SOURCE-PIN.md) | | ☐ |

- **Enterprise-grade elements the artifact MUST demonstrate** (the EXAMPLES-GUIDE floor):
  - **Pinned platform** — single inherited pin property, no loose versions.
  - **Externalized config / profiles** — environment-specific keys (name them; trace each to the pin).
  - **At least one test** — names the behavior it asserts.
  - **Observability / health surface** — where the topic touches it (name the surface/endpoint).
  - **Explicit failure path** — the HONEST-LIMITATIONS floor demonstrated in code: a fault-tolerance
    policy, graceful-shutdown hook, or deliberate error response. State which one and what it proves.
- **Displayed-snippet tie:** the book's bounded listing is one or more tag-region includes inside the
  full compilable file (one artifact, no drift). List the intended tag name(s) here.

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | | | |

- **Run command:** the run command (note any required profile/flag or dependency, e.g. a service the topic needs)
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** [what a developer should observe — e.g. startup line, response body/status, test pass count, the failure-path behavior when the error condition is triggered]
- **Figure plan** (per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured, GUIDELINES §8 — figures are load-bearing only; the per-chapter image
  budget and the zero-figure exception are set by per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured. The plan is fixed here at draft time,
  rendered at Step 9, reviewed with the chapter at Step 12.):
  - **Chapter class:** [the chapter's class per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured — drives the image budget].
  - **Candidate designed diagram(s) + family:** name each load-bearing spatial/flow/architecture diagram
    and its visual family. Authored as source text, rendered to a cropped image via
    `05-figures/_assets/render.mjs` — NEVER generated by an image model.
  - **Candidate captured surface(s):** name each captured surface (where the profile allows screenshots/captures).
  - **Source trace per depicted claim:** for each diagram/capture, the pinned source path
    (`the per-tool fetch dirs in SOURCE-PIN.md/...`) or doc reference each depicted claim traces to.

## 7. Gap-filling (verification queue)
- Every rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims still uncertain → its own check against the pins in SOURCE-PIN.md
- Anything that appears only ahead of the pin → mark `⚠ AHEAD-OF-PIN` and flag
- Open questions for the draft stage
- Anything sent to `09-flags/`

## 8. Sources & further reading

### Primary / Official (the pinned authority set (00-strategy/SOURCE-PIN.md): docs, source, official channels)
| # | Source | Title | URL / path | Verified @the pins in SOURCE-PIN.md |
|---|---|---|---|---|
| 1 | primary doc | | | ☐ |
| 2 | primary source | | the per-tool fetch dirs in SOURCE-PIN.md/... + {URL} | ☐ |

### Accessible / Further reading (tutorials, talks, quality secondary sources)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | | | | ☐ |

> Source-quality order: the pinned authority set (00-strategy/SOURCE-PIN.md) docs → the pinned authority set (00-strategy/SOURCE-PIN.md) primary source → specs/standards
> → release notes / official channels → quality secondary sources → casual forums (color only). Never
> content farms or AI-generated text as a factual source.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | | | |

---
## Learnings & pipeline suggestions
[Close every dossier with what was learned and any rule/template change to propose → also append to PIPELINE-LEARNINGS.md]
