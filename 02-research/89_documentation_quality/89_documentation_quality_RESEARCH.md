# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (docs-vs-self-documenting overlaps the comments debate,
> key 17). Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 89 — `01-index/CANDIDATE_POOL.md` · **Title:** Documentation quality — ADRs, READMEs, runbooks, Javadoc as contract
- **Part:** X · **Tier:** B · **Cmp:** ⚠ · relates 17/09/60
- **Primary authorities:** ADR (adr.github.io, Nygard's original ADR post); Javadoc tool + conventions (JDK); Diátaxis (docs taxonomy); README/runbook practice.

## 1. Core definition & purpose
Code says *what* and *how*; documentation captures the *why* and the *how-to-operate* that code cannot. Documentation quality is a real maintainability lever (key 01): the right docs (an ADR explaining a decision, a Javadoc contract on a public API, a runbook for an incident) save hours; the wrong docs (stale, redundant, "what" comments) actively mislead. This chapter covers the doc types that earn their keep and how to keep them from rotting — the doc-level complement to the in-code comments debate (key 17).

## 2. Mechanism (the spine)
- **Match doc type to purpose (Diátaxis lens):** tutorials, how-to guides, reference, explanation — each serves a different reader need; conflating them produces bad docs.
- **The high-value Java doc types:**
  - **Javadoc as contract** — on *public* API: what it does, params/returns, `@throws`, pre/postconditions, nullability (ties to keys 09/11/32); the contract callers rely on (key 60). Not "what" narration of obvious code.
  - **ADRs** (Architecture Decision Records, Nygard) — short, immutable, point-in-time records of a decision + context + consequences; an ADR **log** (ADL) preserves *why* for future teams, avoiding re-litigating settled decisions. Live in-repo (`docs/adr/`), reviewed like code.
  - **README** — what the project is, how to build/run/test (the wrapper command, key 62), where to get help; the front door for onboarding (key 90).
  - **Runbooks** — operational how-to for incidents (ties to release/observability, keys 83/108).
- **Keep docs alive (the hard part):** docs-as-code (in-repo, reviewed, versioned with the code); generate what you can (Javadoc from source; API docs from OpenAPI); link don't duplicate; delete stale docs (stale docs are worse than none). Some checks are automatable (Javadoc presence on public API via Checkstyle, key 27; doc-link checking).
- **The contested overlap (⚠, key 17):** "self-documenting code vs comments/docs" — present neutrally; docs capture *why/decisions/operation* that even perfect code can't, but redundant "what" docs rot. Crown neither extreme.

## 3. Evidence FOR
- **ADRs preserve rationale** — the single highest-leverage doc for long-lived systems; they stop teams re-deciding and re-breaking settled choices.
- **Javadoc-as-contract** makes public APIs usable + enforceable (key 60); nullability/precondition docs prevent misuse (keys 11/18).
- **Docs-as-code** (in-repo, reviewed) keeps docs closer to truth than a separate wiki that rots.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Stale docs are worse than no docs** — they actively mislead; un-maintained docs are a liability, so write only docs you'll maintain (echoes key 17's comment-rot point).
- **Redundant "what" documentation** duplicates the code and drifts — Javadoc that restates the method name adds noise (key 17 contested zone).
- **Doc effort has diminishing returns** — exhaustively documenting internal/private code rarely pays; focus on public contracts, decisions, and operations.
- **Automatable only partially** — Checkstyle can enforce Javadoc *presence*, not *quality*; a present-but-useless Javadoc passes the check (key 04 vanity parallel).
- **⚠ contested** — how much to document is team/context-dependent; crown no single doctrine.

## 5. Current status
ADRs (Nygard/adr.github.io) are mainstream; Javadoc + docs-as-code + Diátaxis are current practice; in-repo reviewed docs favored over standalone wikis. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — artifacts: a sample ADR (Nygard template), a Javadoc-as-contract example on a public method (with `@throws`/nullability), a README skeleton. Figure-led.
- **Figure:** Fig 89.1 — doc types × purpose (Diátaxis) × where they live × how they're kept alive; "why/decision/operation" (document) vs "what" (let the code say it). Trace to adr.github.io / Diátaxis / Javadoc docs.

## 7. Gap-filling (verification queue)
- ⚠ Nygard ADR template + adr.github.io specifics — confirm.
- ⚠ Javadoc tag conventions + Checkstyle Javadoc rules — verify at pin (key 27).
- ⚠ Diátaxis four-type framing — confirm attribution.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | ADR (Nygard / adr.github.io) | adr.github.io | ☑ concept |
| 2 | Javadoc tool + conventions | docs.oracle.com | ⚠ specifics |
| 3 | Diátaxis | diataxis.fr | ☑ taxonomy |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | ADR / docs 2026 | ADR = point-in-time decision + reasoning; ADL = collection; docs-as-code |

---
## Learnings & pipeline suggestions
- Coordinate with key 17 (comments) — this chapter owns *docs* (ADR/README/Javadoc-contract/runbook), 17 owns *in-code comments*; cross-link, don't re-litigate. **Cross-ref:** 17, 09, 11, 60, 90, 83/108, 27.
