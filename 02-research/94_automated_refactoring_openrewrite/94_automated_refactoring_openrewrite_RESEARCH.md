# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (OpenRewrite vs Refaster vs IDE — different niches).
> Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 94 — `01-index/CANDIDATE_POOL.md` · **Title:** Automated large-scale change — OpenRewrite, Refaster, IDE structural refactors
- **Part:** XI · **Tier:** B · **Cmp:** ⚠ · relates 30/91/95
- **Primary authorities:** OpenRewrite (`docs.openrewrite.org`, `github.com/openrewrite`); Refaster (Wasserman, Google research; Error Prone, key 30); IDE structural search/replace.

## 1. Core definition & purpose
Manual refactoring (key 91) doesn't scale to "change this pattern in 5,000 files across 200 repos." Automated, **type-aware** code transformation does. This chapter covers the tools that apply refactorings + migrations mechanically at scale — OpenRewrite (recipes), Refaster (example-based templates), and IDE structural refactors — and the honest limits (review burden, semantic edge cases). It's the at-scale arm of refactoring (key 91) and the engine behind version migration (key 95) and debt paydown (key 59/87).

## 2. Mechanism (the spine)
- **OpenRewrite:** parses code into a **Lossless Semantic Tree (LST)** — every node carries full type info, so transformations find *every* reference to a type/method without false positives (even implicit/aliased imports), and preserve formatting. **Recipes** (composable, declarative + imperative) apply changes; runs via Maven/Gradle plugin; huge recipe catalog (framework migrations, Java upgrades, static-analysis-fix recipes). Composite recipes build on each other (e.g. UpgradeToJava25 includes UpgradeToJava21).
- **Refaster** (Wasserman/Google; part of Error Prone, key 30): **example-based** — write compilable `@BeforeTemplate`/`@AfterTemplate` methods; the tool matches the before-pattern and rewrites to the after. OpenRewrite also supports Refaster-style recipe authoring. Great for "replace this API-usage pattern everywhere."
- **IDE structural refactors:** IntelliJ structural search/replace + automated refactorings (key 91) — interactive, smaller scale, immediate.
- **Where each fits (⚠, niches not ranks):** OpenRewrite = large cross-cutting migrations/recipes; Refaster/Error Prone = pattern-level fixes integrated with the compiler (key 30); IDE = interactive single-codebase changes. Crown none.
- **Workflow:** run recipe → review the diff (it's a normal PR) → CI gates verify (tests/build, keys 75/42) → merge. Automation proposes; tests + review dispose.

## 3. Evidence FOR
- **Type-aware (LST) = correct at scale** — finds every real reference without the false positives of text/regex search; preserves formatting.
- **Recipe catalog** turns big migrations (Java versions, framework upgrades) into a command (key 95) — huge debt-paydown leverage (keys 59/87).
- **Example-based Refaster** lowers the cost of authoring custom transforms (compilable before/after vs a DSL); proven at Google scale.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Automation still needs review + tests** — a recipe can be subtly wrong for your code; the output is a PR to verify (keys 84/75), not a blind apply. Large auto-generated diffs are hard to review (tension with key 84 size limits) — apply per-recipe, verify incrementally.
- **Semantic edge cases** — recipes handle the common cases; unusual code may need manual follow-up (the migration recipes "don't cover all changes").
- **Recipe authoring has a learning curve** — custom OpenRewrite recipes (LST visitors) are non-trivial; Refaster eases simple cases.
- **⚠ niches differ** — OpenRewrite/Refaster/IDE aren't competitors; using the wrong one (e.g. regex for a type-aware change) causes errors. Crown none.
- **Over-automation risk** — mass-applying a stylistic recipe creates churn/noise (key 86 migration caution).

## 5. Current status
OpenRewrite is the mainstream Java large-scale-migration tool (active recipe catalog, Maven/Gradle); Refaster (via Error Prone, key 30) for example-based transforms; IDE refactors for interactive. *(Versions/recipe names verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** run an OpenRewrite recipe (e.g. a common static-analysis-fix or a small API migration) on the module; show the diff + tests staying green. **Toolchain note:** recipe run needs the plugin/network. Built green; tag-region snippet of the recipe config.
- **Figure:** Fig 94.1 — automated-change workflow: recipe (LST/Refaster) → diff/PR → CI verify → merge; + tool-niche map (OpenRewrite/Refaster/IDE). Trace to OpenRewrite/Refaster docs.

## 7. Gap-filling (verification queue)
- ⚠ OpenRewrite recipe names + plugin GAVs; LST claim; composite-recipe behavior — verify at pin.
- ⚠ Refaster @BeforeTemplate/@AfterTemplate + Error Prone integration (key 30) — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | OpenRewrite docs + repo | docs.openrewrite.org ; github.com/openrewrite | ☑ LST/recipes; ⚠ versions |
| 2 | Refaster (Wasserman; Error Prone) | research.google + (key 30) | ☑ example-based |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | OpenRewrite/Refaster 2026 | LST full-type-info; composite migration recipes (Java 17/21/25); Refaster @Before/@AfterTemplate |

---
## Learnings & pipeline suggestions
- Niche-map (OpenRewrite/Refaster/IDE), crown none. Engine behind keys 59/87/95. **Honest centerpiece:** automation proposes, tests+review dispose. **Cross-ref:** 30, 91, 95, 59, 87, 84, 75.
