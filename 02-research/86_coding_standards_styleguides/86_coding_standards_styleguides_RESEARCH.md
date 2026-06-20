# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (style is partly subjective/contested). Versions
> `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 86 — `01-index/CANDIDATE_POOL.md` · **Title:** Coding standards & style guides — adopting, documenting, enforcing
- **Part:** X · **Tier:** B · **Cmp:** ⚠ · relates 07/27/34
- **Primary authorities:** Google Java Style Guide; Oracle Code Conventions (legacy); Checkstyle/Spotless (enforcement, keys 27/34); team-standard practice.

## 1. Core definition & purpose
A coding standard is the shared agreement on how the team's Java looks and is structured. Its quality value is **consistency** (key 03): a consistent codebase lowers the cognitive cost of reading anyone's code, and removes style from review (key 84) so reviewers focus on substance. This chapter covers choosing/writing a standard, documenting it, and — the part that makes it real — **enforcing it automatically** rather than via review nagging. The honest core: the *specific* style matters far less than *picking one and automating it*.

## 2. Mechanism (the spine)
- **Adopt vs author:** adopt an existing standard (e.g. **Google Java Style Guide** — a complete, readability/uniformity/maintainability-oriented definition) rather than bikeshedding your own; customize minimally. Oracle's old Code Conventions are dated (don't cite as current — key 08 canon-dating).
- **Document it** where developers look: a short `CONTRIBUTING`/standards doc that points at the formatter config + ruleset (the config *is* the canonical standard — DRY).
- **Enforce automatically (the key move):** a **formatter** (Spotless / google-java-format, key 34) makes style non-negotiable and non-manual — `spotless:apply` fixes it; CI `spotless:check` fails on drift. **Checkstyle** (key 27) enforces convention rules a formatter doesn't (naming, Javadoc presence, import order). Wire both into the build (key 62) + pre-commit (key 82) + CI gate (key 76).
- **Take style out of review:** once a formatter is canonical, reviewers never comment on formatting — a major review-quality win (key 84) and the end of style arguments.
- **Org-wide:** a shared parent POM / config module distributes the standard across repos (ties to keys 38/63).

## 3. Evidence FOR
- **Consistency lowers reading cost** (key 03) — the whole codebase reads as if one author wrote it.
- **Automation ends bikeshedding** — a deterministic formatter (key 34) removes subjective style debate and review nagging (key 84).
- **Adopting a vetted guide** (Google Java Style) saves the effort of authoring + maintaining one.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Style is partly subjective** (⚠) — tabs/spaces, brace placement, line length, `var` usage have no objective "best"; the win is *agreement + automation*, not the specific choice. Don't crown a style.
- **Standards-as-PDF that aren't automated are ignored** — a wiki standard nobody enforces drifts immediately; the config must be the source of truth, machine-enforced.
- **Over-strict conventions create friction** — too many opinionated Checkstyle rules generate noise + suppressions (key 39); enforce what matters, warn on the rest.
- **Migrating an existing codebase to a new standard** is a big reformat (noisy diffs, blame churn); use `ratchetFrom` / format-on-touch (keys 34/87) rather than a mega-commit.
- **Standard ≠ quality** — perfectly-formatted code can be badly designed; style is the floor, not the ceiling.

## 5. Current status
Google Java Style + Spotless/google-java-format + Checkstyle is the mainstream stack; config-as-standard, machine-enforced, is current best practice. Oracle Code Conventions are legacy. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** adopt Google Java Style via Spotless + a Checkstyle ruleset, wired into build + pre-commit + CI; show `spotless:check` failing on drift. Config artifact (verified for consistency).
- **Figure:** Fig 86.1 — the standard's lifecycle: choose → config-as-source-of-truth → auto-format (pre-commit/build) → CI check → review focuses on substance. Trace to Google Java Style + Spotless/Checkstyle docs.

## 7. Gap-filling (verification queue)
- ⚠ Google Java Style Guide specifics (e.g. 2-space indent, 100-col) + its IntelliJ config — verify at pin (shared key 34).
- ⚠ Checkstyle google_checks.xml ruleset — verify at pin (key 27).

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Google Java Style Guide | google.github.io/styleguide/javaguide.html | ☑; ⚠ specifics |
| 2 | Spotless / Checkstyle | (keys 34/27) | ☑ enforcement |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Google Java Style 2026 | complete style definition; readability/uniformity/maintainability; google_checks ruleset |

---
## Learnings & pipeline suggestions
- **Core message:** pick one + automate it; the choice matters less than the enforcement. Neutral on the specific style. **Cross-ref:** 03 (consistency), 07 (naming/format craft), 27 (Checkstyle), 34 (formatters), 84 (style out of review), 87 (migrating), 38/63 (org-wide config).
