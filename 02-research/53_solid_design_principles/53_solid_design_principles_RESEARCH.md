# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` contested — NEUTRALITY balance + non-crowning; named
> positions attributed, never asserted as the field's verdict. Versions/quotes `⚠ verify at pin`.

---

## Topic
- **Key:** 53 — `01-index/CANDIDATE_POOL.md` · **Title:** SOLID & design principles in real Java — useful, not dogmatic
- **Part:** VI — Architecture & design governance · **Tier:** B · **Cmp:** ⚠
- **Primary authorities:** Robert C. Martin (SOLID; *Agile Software Development*, *Clean Architecture*); critiques (Dan North "CUPID", practitioner pushback); GoF for pattern context. Canon-dating shape applies (PIPELINE-LEARNINGS).

## 1. Core definition & purpose
SOLID is five object-oriented design principles — **S**ingle Responsibility, **O**pen/Closed, **L**iskov Substitution, **I**nterface Segregation, **D**ependency Inversion — popularized by Robert C. Martin. They aim at the same target as key 01's *maintainability* (low coupling, high cohesion, safe change). The chapter's stance: SOLID is a useful *vocabulary and set of heuristics*, **not** a law to maximize — applied dogmatically it produces over-abstraction. Present each principle's intent in concrete Java, its honest counter-case, and the named alternatives.

## 2. Mechanism (each principle, in Java)
- **SRP** — a class has one reason to change. Java smell it fights: the 2,000-line "God service" (key 19). Trap: taken too far → a cloud of one-method classes (the over-decomposition critique, key 03/Ousterhout).
- **OCP** — open for extension, closed for modification; in Java via interfaces/polymorphism/strategy. Trap: speculative abstraction for change that never comes (YAGNI tension).
- **LSP** — subtypes must be substitutable; ties to the `equals`/contract correctness of key 15 and sealed-type modeling (key 13). The one principle that is closest to a hard correctness rule.
- **ISP** — many focused interfaces over one fat one; Java: role interfaces. Records/sealed interfaces (key 13) make this cheaper.
- **DIP** — depend on abstractions; Java: dependency injection, program to interfaces. Enables testability (key 42/44) and module boundaries (keys 55, 57).

## 3. Evidence FOR
- Widely taught shared vocabulary; LSP and DIP in particular underpin testability and modularity the rest of the book relies on.
- Maps cleanly to measurable structure (coupling/cohesion, key 54; ArchUnit-enforceable boundaries, key 55).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — the ⚠ core)
- **Dogmatic SOLID over-engineers.** Critics (incl. Dan North's "CUPID" — Composable, Unix-philosophy, Predictable, Idiomatic, Domain-based — as an alternative lens) argue SOLID drives premature abstraction and indirection that *hurts* readability. Present CUPID and the "simple over SOLID" view as legitimate alternatives; crown neither.
- **SRP's "one reason to change" is subjective** — reasonable engineers disagree on responsibility boundaries.
- **Principles, not metrics** — you can't gate "SOLID-ness"; chasing it is the vanity trap (key 04). The enforceable parts are coupling/cycles (keys 54, 55).
- **Not Java-version-aware** — records/sealed types/pattern matching (key 13) sometimes achieve the goal more directly than a classic SOLID refactor.

## 5. Current status
SOLID remains standard vocabulary; the "dogma vs pragmatism" debate (CUPID, Ousterhout's deep modules, key 03) is live and unresolved. Book presents it as current debate.

## 6. Worked example / figure spec
- **Illustrative example:** one Java service refactored two ways — a balanced SOLID application vs an over-abstracted one — showing the cost of dogma. Concept chapter; figure may carry it (build optional).
- **Figure:** Fig 53.1 — the five principles each with a one-line Java smell-it-fights + its over-application failure (no crown).

## 7. Gap-filling (verification queue)
- ⚠ SOLID definitions verbatim (Martin) + CUPID (North) — confirm wording/attribution before quoting.
- ⚠ LSP formal statement (Liskov/Wing) — cite primary if stated formally.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Martin — *Agile Software Development* / *Clean Architecture* | print | ⚠ verbatim |
| 2 | Dan North — "CUPID" (alternative to SOLID) | dannorth.net | ☑ named alt |
| 3 | Liskov & Wing — subtyping (LSP origin) | paper | ⚠ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis + canon) | SOLID well-known; CUPID/critiques as live counter-positions |

---
## Learnings & pipeline suggestions
- Use the **two-schools** shape: SOLID-as-heuristic vs CUPID/simple-design; crown neither.
- **Cross-ref:** maintainability → 01; readability/over-decomposition → 03/17; coupling/cohesion → 54; enforce via ArchUnit → 55; anti-patterns → 61; modern-Java alternatives → 13.
