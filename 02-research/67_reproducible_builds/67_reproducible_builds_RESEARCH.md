# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Versions/flags `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 67 — `01-index/CANDIDATE_POOL.md` · **Title:** Reproducible & verifiable builds — build integrity, pinning, hermeticity
- **Part:** VII · **Tier:** B · relates 62/63/66
- **Primary authorities:** reproducible-builds.org; Maven "Reproducible Builds" guide (`project.build.outputTimestamp`, reproducible-build-maven-plugin); Gradle reproducibility docs; SLSA (key 66).

## 1. Core definition & purpose
A **reproducible build** produces a **bit-for-bit identical** artifact from the same source, independent of *when*, *where*, or *by whom* it's built. This is both a quality property (determinism — "works on my machine" dies) and a security property (you can verify a published artifact matches its source — anti-tampering, the heart of SLSA, key 66). This chapter covers what breaks reproducibility in Java and how to fix it.

## 2. Mechanism (the spine)
- **Sources of non-determinism (the Java specifics):** timestamps embedded in JAR entries + `MANIFEST`; file ordering in the archive; absolute paths; locale/timezone/encoding differences; non-pinned dependency/plugin versions or ranges (key 63); generated code with embedded dates; `Math.random`-style build-time variability.
- **The fixes:** Maven `<project.build.outputTimestamp>` (fixes JAR timestamps) + the **reproducible-build-maven-plugin** / Gradle's reproducible-archives settings (`preserveFileTimestamps=false`, `reproducibleFileOrder=true`); **pin everything** (exact dependency *and* plugin versions, the build-tool via the wrapper — key 62); fixed `LANG`/`TZ`/encoding; avoid embedding build-time data.
- **Verify:** build twice (different machine/time) and `diff` the artifacts; Maven's `artifact:compare`; publish checksums/signatures so others can verify (ties to SLSA provenance, key 66).
- **Hermeticity** (the strong form): builds depend only on declared, pinned inputs (no network fetch of moving deps at build time) — the basis for higher SLSA levels.

## 3. Evidence FOR
- **Determinism kills a class of "works on my machine" bugs** — the artifact is a pure function of source + pinned inputs.
- **Verifiability** — anyone can rebuild and confirm the published artifact wasn't tampered with (supply-chain integrity, key 66).
- **Tooling exists** — Maven/Gradle have first-class reproducibility support; it's mostly configuration, not rewriting.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Last-mile effort** — getting truly bit-identical can require chasing obscure non-determinism (a plugin that embeds a timestamp); diminishing returns for some teams (when-NOT-to-fully-invest: a purely internal app with no verification need).
- **Toolchain variance** — different JDK builds/vendors can still differ; full reproducibility may pin the JDK too.
- **Doesn't prove correctness** — a reproducible build of buggy code is still buggy; it proves *integrity/determinism*, not quality.
- **Ongoing discipline** — one unpinned plugin or a new timestamp-embedding tool silently breaks it; needs a verify step in CI to stay reproducible.

## 5. Current status
Maven and Gradle both support reproducible builds; `project.build.outputTimestamp` is the standard Maven knob; reproducibility is a prerequisite for higher SLSA levels (key 66) and a growing expectation. *(Flags/versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** set `project.build.outputTimestamp`, pin plugin versions, build twice and diff to show bit-identical output. Built green; tag-region snippet of the config.
- **Figure:** Fig 67.1 — non-determinism sources (timestamps/order/paths/locale/ranges) → the fix for each → verify-by-rebuild. Trace to reproducible-builds.org + Maven docs.

## 7. Gap-filling (verification queue)
- ⚠ `project.build.outputTimestamp`, reproducible-build plugin GAV, Gradle archive flags, `artifact:compare` — verify at pin.
- ⚠ SLSA-level dependence on reproducibility/hermeticity — confirm (key 66).

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | reproducible-builds.org | reproducible-builds.org | ☑ concept |
| 2 | Maven reproducible builds guide | maven.apache.org | ☑ outputTimestamp; ⚠ specifics |
| 3 | Gradle reproducibility | docs.gradle.org | ☑ archive flags |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | supply-chain 2026 | reproducible builds as SLSA prerequisite; bit-for-bit verifiability |

---
## Learnings & pipeline suggestions
- **Cross-ref:** build → 62; pin deps → 63; SLSA/provenance → 66; release → 83. Reproducibility = determinism (quality) + verifiability (security).
