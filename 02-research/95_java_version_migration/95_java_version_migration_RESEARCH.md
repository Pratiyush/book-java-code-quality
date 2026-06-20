# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Anchors the book's 21/25 baseline. JEPs/versions
> `⚠ verify at pin` (standards-edition discipline). Neutral; honest-limitations met.

---

## Topic
- **Key:** 95 — `01-index/CANDIDATE_POOL.md` · **Title:** Migrating Java versions as quality work (8→17→21→25) — modernization recipes
- **Part:** XI · **Tier:** B · relates 13/64/94
- **Primary authorities:** OpenJDK release/migration notes + JEPs (key 13); OpenRewrite rewrite-migrate-java (key 94); JDK migration guides.

## 1. Core definition & purpose
Staying on an old Java version is silent debt: missed performance, missed language features that improve quality (records/sealed/pattern matching, key 13), missed security patches, and a widening upgrade cliff. **Version migration is quality work** — both the act (a large, test-backed change) and the payoff (modernizable code). This chapter covers migrating across the LTS line (8 → 11 → 17 → **21 → 25**, the book's anchors) as a disciplined, largely-automatable modernization — connecting modern Java (key 13), dependency currency (key 64), and automated change (key 94).

## 2. Mechanism (the spine)
- **The LTS path:** Java LTS releases 8, 11, 17, **21**, **25** (the book anchors 21, notes 25). Migrate LTS-to-LTS; each hop has known breaking changes (removed APIs, JPMS strong encapsulation since 17, removed `sun.misc.Unsafe`-style internals, deprecations-for-removal).
- **Automate the bulk (OpenRewrite, key 94):** `rewrite-migrate-java` recipes (UpgradeToJava17/21/25) apply the common mechanical changes; composite recipes chain (UpgradeToJava25 ⊇ 21). They "don't cover all changes" — manual follow-up for the rest.
- **The disciplined process:** (1) get tests green on current version (characterization where thin, key 92); (2) bump dependencies first (key 64 — many old libs don't run on new JDKs); (3) run the migration recipe; (4) fix residual breaks (removed APIs, illegal reflective access); (5) build + test on the target JDK (toolchain matrix, key 77); (6) adopt new features opportunistically (key 13) — but as separate steps from the version bump.
- **Common breakages:** strong encapsulation (JPMS) blocking deep reflection; removed/changed APIs; third-party libs/agents incompatible with the new JDK; build-tool/plugin versions needing bumps (keys 62/63).
- **Modernize after migrating:** once on 21/25, refactor toward records/sealed/pattern matching/virtual threads (keys 13/22) — a quality upgrade enabled by the version.

## 3. Evidence FOR
- **Largely automatable** — OpenRewrite migration recipes (key 94) do the mechanical bulk across the whole codebase, type-aware.
- **Incremental + test-backed** — LTS-to-LTS hops with green tests at each step (refactoring discipline, key 91) beat a multi-version big-bang.
- **Unlocks quality features** — modern Java (key 13) + performance + security come with the upgrade; staying behind is debt (key 02).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Recipes don't cover everything** — residual manual work for removed APIs, reflective-access, and library incompatibilities is real; budget it.
- **Dependency/agent incompatibility is the usual blocker** — an old framework/APM agent that won't run on the new JDK can stall the whole migration (key 64 must come first).
- **Big version jumps compound risk** — 8→25 in one leap is harder than stepping 8→11→17→21→25; the longer you wait, the worse the cliff (key 02).
- **Migration ≠ modernization** — bumping the version doesn't refactor old idioms; do the feature adoption (key 13) as deliberate follow-up, not conflated with the bump.
- **Preview features at 25 are not migration targets** (AHEAD-OF-PIN, key 13) — don't migrate onto preview APIs as if stable.
- **Test coverage gates safety** — migrating untested legacy is risky; characterize first (key 92).

## 5. Current status
21 and 25 are current LTS anchors; OpenRewrite migrate-java recipes current (through Java 25 composite); migration is mainstream quality work. *(JEPs/versions/recipe names verify-at-pin — standards-edition discipline.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** run `rewrite-migrate-java` UpgradeToJava21 on an older-style module, fix a residual break, build green on JDK 21 (matrix-note 25). **Toolchain-gated** (needs the JDKs). Built green; tag-region snippet of the recipe + a before/after using a modern feature (key 13).
- **Figure:** Fig 95.1 — the LTS migration path (8→11→17→21→25) with "automate (recipe) vs manual (residual)" per hop + "then modernize" step. Trace to OpenJDK/JEPs + OpenRewrite docs.

## 7. Gap-filling (verification queue)
- ⚠ LTS list + per-hop breaking changes (JPMS-since-17, removed APIs) — confirm against OpenJDK migration notes/JEPs (don't assert from memory).
- ⚠ OpenRewrite UpgradeToJavaNN recipe names + coverage caveats — verify at pin (key 94).

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | OpenJDK migration notes + JEPs | openjdk.org (key 13) | ⚠ confirm |
| 2 | OpenRewrite migrate-java | docs.openrewrite.org/.../migrate-to-java-21 (key 94) | ☑ recipes; ⚠ versions |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | OpenRewrite Java migration 2026 | UpgradeToJava17/21/25 composite recipes; LTS 8/11/17/21/25; recipes don't cover all changes |

---
## Learnings & pipeline suggestions
- Standards-edition discipline on JEPs/versions. Migration (act) + modernization (payoff) are separate steps. **Cross-ref:** 13 (modern features), 64 (deps first), 94 (automate), 91 (discipline), 92 (characterize), 02 (debt of staying behind), 77 (JDK matrix).
