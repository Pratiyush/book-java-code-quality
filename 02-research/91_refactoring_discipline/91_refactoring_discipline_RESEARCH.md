# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Canon-dating shape (Fowler 2e + modern Java). Versions
> `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 91 — `01-index/CANDIDATE_POOL.md` · **Title:** Refactoring discipline — safe, incremental, test-backed (the Fowler catalog)
- **Part:** XI — Refactoring & legacy · **Tier:** B · relates 19/42/94
- **Primary authorities:** Martin Fowler, *Refactoring* (2nd ed., 2018); IDE refactorings (IntelliJ/Eclipse); the test substrate (Part V).

## 1. Core definition & purpose
Refactoring is **changing the structure of code without changing its behavior** — the disciplined mechanism by which quality is *recovered* and debt (key 02) paid down. The discipline is what separates refactoring from "rewriting and hoping": small behavior-preserving steps, each verified by tests, so the code is always working. This chapter covers refactoring as a rigorous practice (Fowler's definition + catalog), distinct from the code-smells catalogue (key 19, which says *what* to fix) — here is *how* to fix it safely.

## 2. Mechanism (the spine)
- **Definition (Fowler):** a refactoring is a small, behavior-preserving transformation (Extract Method, Rename, Move, Replace Conditional with Polymorphism, Introduce Parameter Object…); refactoring *is* applying these in series under a green test suite.
- **The discipline:** (1) ensure tests cover the area (or write characterization tests first, key 92); (2) make one small refactoring; (3) run tests (green); (4) commit; repeat. Never refactor and change behavior in the same step.
- **Smells → refactorings:** key 19 catalogues the smells; Fowler pairs each with the refactoring that removes it. This chapter owns the *mechanics* of applying them.
- **Tooling:** IDE automated refactorings (IntelliJ/Eclipse) are behavior-preserving by construction (safer than manual edits); Error Prone/Refaster (keys 30/94) and OpenRewrite (key 94) automate refactorings at scale.
- **Modern Java (canon-dating, key 13):** some 2018-era manual refactorings are now language features — Replace Constructor with Factory → records; some Visitor → sealed + pattern matching. Note where the JEP supersedes the catalog entry.
- **When to refactor:** the "two hats" (Fowler) — refactor *or* add feature, never both at once; refactor before a change to make it easy (preparatory refactoring), and opportunistically (Boy Scout Rule, key 06).

## 3. Evidence FOR
- **Behavior-preserving + test-backed = safe** — the codebase is always working; risk is bounded per step (vs big-bang rewrite, key 93/96).
- **Fowler's catalog is the canonical, named vocabulary** — shared language for "what transformation."
- **IDE/automated refactorings** make many transformations mechanical + reliable (keys 30/94).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Refactoring without tests is just editing** — the #1 risk; on untested legacy you must characterize first (key 92) or you can't know behavior is preserved.
- **Scope creep** — "while I'm here" refactoring inside a feature PR bloats review (key 84) and mixes behavior+structure changes; separate the hats.
- **Big refactorings can overrun** — a "refactoring" that's really a redesign loses the behavior-preserving safety; that's modernization (keys 93/96), planned differently.
- **Catalog is partly dated** (canon-dating) — apply through a modern-Java lens (key 13), don't teach obsolete manual patterns as current.
- **Refactoring has opportunity cost** — not all code is worth refactoring (frozen/throwaway, key 02 when-NOT-to).

## 5. Current status
Fowler *Refactoring* 2e is current canon; IDE + OpenRewrite/Refaster automate at scale; the discipline is stable, read through modern-Java. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module:** a smelly method (key 19) refactored in small green steps (Extract Method → Introduce Parameter Object) with the test suite staying green; show the IDE refactoring + the commit-per-step. Built green; tag-region snippet.
- **Figure:** Fig 91.1 — the refactoring loop (cover with tests → small transform → green → commit → repeat) + "two hats." Trace to Fowler.

## 7. Gap-filling (verification queue)
- ⚠ Fowler refactoring names + "two hats"/preparatory-refactoring wording — confirm against *Refactoring* 2e.
- ⚠ Which catalog entries modern Java supersedes (key 13) — confirm against JEPs, don't overstate.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Fowler — *Refactoring* (2e, 2018) | refactoring.com ; print | ☑ definition/catalog; ⚠ verbatim |
| 2 | IDE refactorings; OpenRewrite/Refaster | (key 94) | ☑ automation |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | refactoring / OpenRewrite 2026 | behavior-preserving catalog; IDE + automated; modern-Java supersession |

---
## Learnings & pipeline suggestions
- Canon-dating (Fowler 2e + modern Java). Owns *how*; key 19 owns *what* (smells); key 94 owns *at scale*. **Cross-ref:** 19, 42 (tests), 92 (characterize first), 94 (automate), 13 (modern Java), 06 (Boy Scout).
