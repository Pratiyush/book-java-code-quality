# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 63 — `01-index/CANDIDATE_POOL.md` · **Title:** Dependency management & hygiene — BOMs, version catalogs, convergence, enforcer
- **Part:** VII · **Tier:** B · relates 64/65
- **Primary authorities:** Maven (dependencyManagement, BOM import, `maven-enforcer-plugin`); Gradle (platforms, version catalogs `libs.versions.toml`); Maven Central / repository docs.

## 1. Core definition & purpose
Dependencies are most of a modern Java app, and unmanaged dependencies are a quality and security liability: version conflicts, transitive surprises, and "works on my machine" non-determinism. **Dependency hygiene** = one source of version truth, no conflicting transitive versions, no unpinned/range versions, and a minimal, justified dependency set. This chapter covers the mechanisms (BOMs, catalogs, convergence enforcement) that make the dependency graph deterministic and reviewable — the foundation the currency (key 64) and vulnerability (key 65) chapters build on.

## 2. Mechanism (the spine)
- **Single source of version truth:** Maven `<dependencyManagement>` + imported **BOMs** (e.g. a platform BOM) so versions are declared once; Gradle **version catalogs** (`gradle/libs.versions.toml`) + platforms. Child modules omit versions.
- **Convergence:** transitive deps can pull conflicting versions; Maven's "nearest wins" can silently downgrade. Enforce with `maven-enforcer-plugin` rules `dependencyConvergence` and `requireUpperBoundDeps`; inspect with `mvn dependency:tree`/`analyze`. Gradle has resolution strategies + `dependencyInsight`.
- **No moving versions:** ban `LATEST`/`RELEASE`/open ranges (non-reproducible, key 67) — pin exact versions; let an update bot move them deliberately (key 64).
- **Minimal surface:** remove unused deps (`dependency:analyze` flags used-undeclared / declared-unused); fewer deps = smaller attack surface (key 65) + faster builds.
- **Scope discipline:** correct `test`/`provided`/`runtime` scopes; don't leak test deps to runtime.
- **Repository hygiene:** trusted repos only, checksum/signature verification (ties to supply chain, key 66).

## 3. Evidence FOR
- BOMs/catalogs eliminate version drift across modules — one bump, everywhere consistent.
- Enforcer convergence catches silent transitive downgrades that cause subtle runtime bugs.
- Minimal, pinned dependencies are directly reproducible (key 67) and reduce vulnerability exposure (key 65).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Strict convergence can be noisy** — large graphs have many minor conflicts; over-strict rules block builds and get disabled (key 39). Tune scope.
- **BOM/catalog discipline needs buy-in** — a team that adds ad-hoc versions defeats the single-source-of-truth.
- **Pinning vs currency tension** — pinned exact versions are reproducible but go stale; you need an update mechanism (key 64) or pinning becomes rot.
- **Doesn't judge dependency *quality*** — convergence says versions agree, not that the dependency is well-maintained or safe (keys 64/65 cover that).

## 5. Current status
Maven dependencyManagement/BOM and Gradle version catalogs are current best practice; Enforcer widely used. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** a parent POM with BOM import + Enforcer `dependencyConvergence`; show a deliberate convergence failure + fix. Built green; tag-region snippet.
- **Figure:** Fig 63.1 — dependency tree with a transitive conflict → BOM/convergence resolving it. Trace to Maven/Gradle docs.

## 7. Gap-filling (verification queue)
- ⚠ Enforcer rule names (`dependencyConvergence`, `requireUpperBoundDeps`), BOM-import syntax, catalog format — verify at pin.
- ⚠ Maven conflict-resolution ("nearest wins") wording — confirm against Maven docs.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Maven — dependency mgmt + Enforcer | maven.apache.org | ☑ mechanism; ⚠ version |
| 2 | Gradle — platforms + version catalogs | docs.gradle.org | ☑; ⚠ version |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis) | BOM/catalog single-source; enforcer convergence; pin no-ranges; minimal surface |

---
## Learnings & pipeline suggestions
- **Cross-ref:** build → 62; currency/bots → 64; vuln scanning → 65; SBOM → 66; reproducibility → 67; suppression discipline → 39.
