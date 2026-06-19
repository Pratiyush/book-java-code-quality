# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` contested. NEUTRALITY balance + non-crowning; named
> positions attributed. Quotes `⚠ verify at pin`.

---

## Topic
- **Key:** 61 — `01-index/CANDIDATE_POOL.md` · **Title:** Design & anti-patterns for maintainability — when patterns help vs hurt
- **Part:** VI · **Tier:** B · **Cmp:** ⚠ · relates to key 19 (smells)
- **Primary authorities:** GoF (*Design Patterns*); Fowler (*Refactoring*, *PoEAA*); Brown et al. (*AntiPatterns*); modern-Java context (records/sealed/enums — key 13).

## 1. Core definition & purpose
Design patterns are named, reusable solutions to recurring design problems; anti-patterns are recurring solutions that look reasonable but degrade maintainability. For a code-quality book the point is *judgment*, not catalog-worship: a pattern applied to a problem it doesn't have is itself an anti-pattern (needless complexity, key 03/53). This chapter covers the maintainability-relevant patterns, the common Java anti-patterns, and — the ⚠ part — the honest debate about pattern overuse, especially now that modern Java (key 13) makes several classic patterns unnecessary.

## 2. Mechanism (the spine)
- **Patterns that aid maintainability (in Java):** Strategy/Template (OCP, key 53), Factory/Builder (construction clarity; records reduce the need, key 13), Adapter/Facade (decoupling, key 54), Observer (events), Dependency Injection (testability, keys 42/44). Cite GoF for definitions; show the *Java* form.
- **Modern Java changes the calculus (key 13):** `enum` singletons (Effective Java Item 3, key 08) replace the Singleton pattern; sealed interfaces + pattern matching replace some Visitor uses; records replace boilerplate value-object/DTO patterns. Note where a pattern is now "served by a language feature" (canon-dating shape).
- **Anti-patterns (maintainability killers):** God Object / God Service, Singleton-as-global-state, anemic-domain-vs-rich debate, primitive obsession, premature abstraction, "patternitis" (using patterns to look sophisticated). Overlaps the smell catalogue (key 19) — this chapter is design-level, key 19 is code-level.
- **Detection:** some smells/anti-patterns are tool-detectable (PMD/SonarGod-class rules, key 28/35); most need review (key 84).

## 3. Evidence FOR
- Patterns give a **shared vocabulary** ("use a Strategy here") that speeds design discussion and review.
- The right pattern reduces coupling and localizes change (the maintainability goal, key 01/54).
- Anti-pattern names make review feedback concrete and less personal ("this is a God Object").

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — the ⚠ core)
- **Overuse is a real anti-pattern** — "patternitis"/cargo-culting adds indirection that *hurts* readability (key 03; Ousterhout's over-decomposition critique). A pattern is justified by the problem, not by its name.
- **Many GoF patterns are dated for Java** — language features (records/sealed/enums/lambdas, key 13) make Singleton, some Factory/Visitor/Strategy forms unnecessary; teaching them uncritically pushes obsolete idioms (canon-dating, key 08).
- **Anti-pattern labels can be weaponized** in review — use them descriptively, not to dunk (key 06/84 culture).
- **Contested designs** (anemic vs rich domain model; inheritance vs composition) have no universal answer — present as trade-offs, crown neither.

## 5. Current status
GoF remains foundational vocabulary but is read through a modern-Java lens; the "patterns considered harmful when overused" view is mainstream. Live debate; book reflects it.

## 6. Worked example / figure spec
- **Illustrative:** one problem solved with a classic pattern vs the modern-Java way (e.g. Singleton → enum; visitor → sealed + switch pattern matching), plus a God-Object refactor. Concept chapter; figure-led (build optional).
- **Figure:** Fig 61.1 — "pattern → is it now served by a Java feature?" table (Singleton/Builder/Visitor/Strategy → enum/record/sealed-switch/lambda), with the when-still-useful note. Trace to GoF + JEPs (key 13).

## 7. Gap-filling (verification queue)
- ⚠ GoF pattern names/intents — confirm verbatim if quoted.
- ⚠ Which patterns are "served by" which Java feature — confirm the feature against key 13/JEPs (don't overstate).
- ⚠ AntiPatterns (Brown et al.) attribution — confirm.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | GoF — *Design Patterns* | print | ⚠ verbatim |
| 2 | Fowler — *Refactoring* / *PoEAA* | print (keys 91) | ⚠ |
| 3 | Modern-Java feature mapping | JEPs (key 13) | ☑ cross-ref |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis + canon) | GoF vocabulary; modern-Java supersession; patternitis debate |

---
## Learnings & pipeline suggestions
- Use **two-schools** (patterns-as-vocabulary vs simplicity-first) + **canon-dating** (which GoF patterns Java now serves). **Cross-ref:** 19 (code smells), 53 (SOLID), 03/17 (readability/over-abstraction), 13 (modern Java), 08 (EJ), 84 (review).
