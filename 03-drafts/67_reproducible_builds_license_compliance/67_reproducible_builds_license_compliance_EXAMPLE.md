# GATE REPORT — EXAMPLE-BUILD — Chapter 29 (key 67)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 67 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 68)
- **Slug:** `67_reproducible_builds_license_compliance`
- **Draft under review:** `03-drafts/67_reproducible_builds_license_compliance/67_reproducible_builds_license_compliance_v1.md`
- **Module built:** `08-companion-code/67_reproducible_builds_license_compliance/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `mvn`, `check_snippets.sh`, `extract_snippet.sh`
- **Scripts run:** `mvn -B -Pquality verify`, `mvn -B clean package` (×2, reproducibility), `check_snippets.sh`, `extract_snippet.sh`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run manually with the pinned toolchain)
- **Verdict:** **PASS**

---

## Verdict rationale

The companion module builds **warning-clean green** via `mvn -B -Pquality verify` (exit 0, 0 WARNINGs) on
the pinned toolchain (JDK 21.0.11, Maven 3.9.16), all six displayed snippets resolve to real bounded tag
regions (≤9 lines each) in the built files, and the chapter's central claim — same source ⇒ bit-identical
artifact — is **demonstrated live and offline** (two fresh builds, identical SHA-256). Every fact traces to
SOURCE-PIN or is flagged; the two unpinned plugin versions are flagged to `09-flags/`. FLOOR C source-trace
+ compile + (in-code) honest-limitations all hold.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime/toolchain meets the minimum (Java 21+).**
  `openjdk version "21.0.11" 2026-04-21` (Homebrew openjdk@21) — meets the SOURCE-PIN anchor (Java 21 LTS).
  `Apache Maven 3.9.16` — matches SOURCE-PIN §4.
- **(b) Build finished GREEN.**
  Exact command (standalone path, build of record):
  `mvn -B -Pquality -f 08-companion-code/67_reproducible_builds_license_compliance/pom.xml clean verify`
  Result line: **`BUILD SUCCESS`** — Maven exit 0, **0 WARNINGs**.
  - `Tests run: 7, Failures: 0, Errors: 0, Skipped: 0`
  - `You have 0 Checkstyle violations.`
  - SpotBugs: `No errors/warnings found`
  - license gate passed; wrote `target/generated-sources/license/THIRD-PARTY.txt`

Both preconditions hold → Floor-C verdict is a real **PASS** (not conditional/assumed).

---

## Reproducibility demonstration (the chapter's central claim, verified)

Built twice, fresh `clean package` each time, fully offline:

```
mvn -B -f .../67_reproducible_builds_license_compliance/pom.xml clean package
shasum -a 256 target/reproducible-builds-license-compliance-1.0.0-SNAPSHOT.jar
```

- build 1 SHA-256: `b5b3d7beae2ea03d0445c97f6e88fa9a7bbf425452745f51c8f8ac3cd30990d3`
- build 2 SHA-256: `b5b3d7beae2ea03d0445c97f6e88fa9a7bbf425452745f51c8f8ac3cd30990d3`
- `cmp` of the two jars: **identical**

Mechanism: `<project.build.outputTimestamp>2026-06-20T00:00:00Z</project.build.outputTimestamp>` +
`reproducible-build-maven-plugin:0.17 strip-jar`. The jar's entries are all normalised to `01-01-2000
00:00`. This is **NOT** REPRO PENDING-RUNTIME — everything runs offline; the deterministic artifact is
proven, not merely configured. (The dossier left REPRO as a possible pending-runtime caveat; it cleared.)

---

## Tag-include regions (displayed snippets) — all resolve, all ≤9 lines

| Tag | Backing file | Resolved content lines |
|---|---|---|
| `repro-timestamp` | `pom.xml` | 1 |
| `repro-plugin` | `pom.xml` | 7 |
| `repro-verify` | `src/main/java/org/acme/repro/ReproducibleArtifact.java` | 4 |
| `license-gate` | `pom.xml` | 6 |
| `license-allow-list-file` | `config/license/allowed-licenses.txt` | 8 |
| `license-allow-list` | `src/main/java/org/acme/repro/LicensePolicy.java` | 8 |

`check_snippets.sh` over the draft: **6 marker(s); 6 pass, 0 fail** (exit 0). The displayed listing and the
runnable file are one artifact (anti-drift, COMPANION-REPO §2.5).

---

## Enterprise-grade checklist

- **Child of the ONE aggregator; no own version literal / BOM.** ✅ `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; module carries no `<groupId>`/`<version>`, imports no BOM of its own. Reference shape = peers 27/62/65.
- **Registered last.** ✅ NOT added to the parent `<modules>` list (parent `pom.xml` untouched, git-clean). Registration is deferred to after CODE-REVIEW per the FLOOR-C "register last" rule.
- **Pinned dependency set via inherited parent properties.** ✅ runtime (Java 21), `junit-bom`, `assertj-core` inherited; `commons-lang3` pinned (3.18.0, no range). The two plugin version literals not pinned in SOURCE-PIN are held as named properties and flagged (see Flags).
- **Externalized config.** ✅ `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml`, and the allow-list policy `config/license/allowed-licenses.txt` (referenced via `<includedLicenses>` file URL) — not hard-coded inline. Profiles: default build (reproducibility, always-on, offline) + `-Pquality` (license gate + static analysis, offline).
- **At least one integration test, harness configured.** ✅ `BuildIntegrityTest` (7 tests) exercises both facets end-to-end: reproducible-build digest comparison (pass + drift) and the license allow-list gate (pass, failure path, transitive surprise, distribution-mode tuning). Test harness inherited from the aggregator (`junit-bom` import + surefire JUnit-Platform provider); confirmed a clean run (`Tests run: 7 … Skipped: 0`, no spurious logging).
- **Observability surface.** ✅ `ReproducibleArtifact.digest()` (the value a provenance attestation signs / a CI verify watches) and `isVerifiable()` (readiness probe).
- **Explicit failure path.** ✅ `LicensePolicy.evaluate` throws `DisallowedLicenseException` naming each offending component+license — the in-code analogue of `license-maven-plugin` `failOnBlacklist` failing the build. Honest-limitations floor in code: `ReproducibleArtifact.isIntegrityNotCorrectness()` (reproducible ≠ correct).

---

## Realize-the-draft check

The module demonstrates exactly what the chapter describes (the non-determinism→fix table, verify-by-
rebuilding, the SPDX allow-list gate, the generated `THIRD-PARTY`, the obligation spectrum, not-legal-
advice) and adds no behaviour the prose does not claim. The draft's companion-module spec paragraph was
updated from the PENDING spec to the built reality (locked invisible-narrator voice), and six one-line
lead-ins were added before the include markers; **no prose was deleted**.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed **file-by-file**: every file in the module is original work written for this book. The
`org.acme.repro` package, the test, the `pom.xml` (with chapter-specific structure and original comments),
the house checkstyle/spotbugs configs, and the authored SPDX allow-list are NOT copied or lightly-edited
upstream samples — no quickstart/getting-started skeleton, no `NOTICE`/header boilerplate copied from the
plugins or their docs. No region is taken substantially verbatim from a specific source file, so no
attribution block is required. The `THIRD-PARTY.txt` is GENERATED by the build (the chapter's mechanism),
not authored.

---

## Source-trace (Floor C) — every atom

| Atom | Value | Traces to |
|---|---|---|
| Runtime anchor | Java 21 (built 21.0.11) | SOURCE-PIN §Runtime baseline + §1 |
| Build tool | Maven 3.9.16 | SOURCE-PIN §4 |
| Reproducible-build mechanism | `project.build.outputTimestamp` | Dossier key 67 (Maven reproducible-builds guide) — SPDX/repro rows TO-PIN |
| `reproducible-build-maven-plugin` | `0.17` | ⚠ NOT pinned in SOURCE-PIN → `09-flags/67_repro_license_plugin_versions_unpinned.md` (resolves on pinned toolchain) |
| `license-maven-plugin` | `2.7.1` | ⚠ NOT pinned in SOURCE-PIN → same flag (resolves on pinned toolchain) |
| SPDX identifiers (`Apache-2.0`, `MIT`, `BSD-*`, `GPL-3.0-only`, `LGPL-2.1-only`, `AGPL-3.0-only`, `EPL-2.0`, `MPL-2.0`, `ISC`) | as used | SPDX = ISO/IEC 5962:2021 (SOURCE-PIN §4); `spdx.org/licenses` |
| Obligation spectrum (permissive / weak / strong copyleft; AGPL reaches network/SaaS) | factual, not legal advice | Dossier key 68 + chapter; LEGAL-IP-RULES (factual only) |
| `commons-lang3` | `3.18.0` (Apache-2.0) | SOURCE-PIN §4 (pinned; reused from peer 65) |
| Checkstyle plugin/engine | `3.6.0` / `10.26.1` | house shape (peers 27/62/65); engine = SOURCE-PIN §2 line |
| SpotBugs plugin | `4.9.3.0` | house shape (peers) |
| JUnit / AssertJ | via inherited `junit-bom` 6.0.3 / `assertj-core` 3.27.7 | SOURCE-PIN §3 (aggregator) |

No invented rule IDs, config keys, flags, API signatures, GAV coordinates, versions, benchmark figures, or
quoted claims. The two unpinned plugin versions are flagged, not asserted as pinned facts.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan (fixed at draft time) is two **designed conceptual
diagrams** (`fig67_1` the reproducibility chain, `fig67_2` the license-obligation spectrum) — both already
authored as HTML and rendered to PNG with `.sources.md` sidecars under
`05-figures/67_reproducible_builds_license_compliance/`. Designed diagrams are the figure-designer's job,
not this gate's. This config-centric reproducibility/license chapter has **no subject-native UI surface**
to capture (no dev console, no health/API endpoint) — the demonstration is a build + a digest comparison on
the command line, already evidenced above. So Step 4c is correctly empty; no screenshots were invented.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `reproducible-build-maven-plugin` (0.17) and `license-maven-plugin` (2.7.1) version literals not separately pinned in SOURCE-PIN (repro/SPDX/license-tool rows = TO-PIN) | NOTE | `pom.xml` properties | Held as named properties + flagged to `09-flags/67_repro_license_plugin_versions_unpinned.md`; add explicit version lines at next `/pin-source`, then re-confirm green + bit-identical |
| 2 | `license-maven-plugin` `includedLicenses` matches license names by **exact equality**, not regex (verified by decompiling `isDependencyWhitelisted` → `List.contains`) | NOTE | `config/license/allowed-licenses.txt` | Allow-list uses exact SPDX ids + common long-form names; recorded in the flag so a plugin major re-pin re-verifies the matching behaviour |
| 3 | License gate scoped to `runtime+compile` via `<excludedScopes>test</excludedScopes>` (test-only EPL/byte-buddy deps are not shipped) | NOTE | `pom.xml` `license-gate` | Intentional — the gate evaluates the SHIPPED graph (the chapter's distribution-mode point); documented in the pom comment |

---

## Blockers

**None.** Build is green and warning-clean; all snippets resolve; reproducibility demonstrated; Floor-C
preconditions both hold.

---

## Gate-specific checks

- [x] **EXAMPLE** — module builds green via `mvn -B -Pquality verify` at the pin (warning-clean); all six displayed snippets resolve to real ≤9-line tag regions in the compiled files; FLOOR C source-trace clean.
- [x] Pinned dependency set via inherited parent; no own version literal / BOM.
- [x] Externalized config (checkstyle, spotbugs, license allow-list) + profiles (default + `-Pquality`).
- [x] Integration test present (7 tests) + harness configured (clean run, no spurious logging).
- [x] Observability surface (`digest()`, `isVerifiable()`).
- [x] Explicit failure path (`DisallowedLicenseException`) + honest-limitations in code.
- [x] LEGAL-IP §5 original-for-this-book confirmed file-by-file.
- [x] Reproducibility demonstrated live + offline (bit-identical, verified by SHA-256 + `cmp`).
- [x] Module NOT yet registered in parent `<modules>` (deferred to post-CODE-REVIEW; parent pom untouched).

---

## Learnings & pipeline suggestions

1. **`license-maven-plugin` `includedLicenses` is exact-match, not regex.** A regex allow-list silently
   fails (every license reads as "forbidden") with no error — only a `BUILD FAILURE` on a license you meant
   to allow. The fix is exact SPDX ids + the common long-form POM names. Worth a one-line note in
   `EXAMPLES-GUIDE` for any future license-gate module so the next builder does not lose time to it.
2. **Scope the license gate to the shipped graph (`<excludedScopes>test</excludedScopes>`).** Default
   `add-third-party` includes test-only deps (JUnit = EPL-2.0, byte-buddy), which would fail a permissive
   allow-list for non-shipped tooling and muddy the chapter's distribution-mode point. Gating
   runtime+compile is both correct and pedagogically cleaner.
3. **Reproducibility is cheaply demonstrable offline.** `outputTimestamp` + `reproducible-build-maven-plugin`
   `strip-jar` made the jar bit-identical with no network; a build-twice-and-diff is a strong, honest
   artifact for a reproducibility chapter (better than only tagging config). Recommend the
   reproducibility-double-build become the standard evidence for any future "reproducible build" topic,
   recorded with the SHA in the gate report.
4. **The "in-code analogue + tagged real config" pattern (peers 62/65) generalises well** to a two-facet
   chapter: one config-tagged mechanism per facet (`repro-*`, `license-gate`/`license-allow-list-file`) plus
   one in-code analogue per facet (`repro-verify`, `license-allow-list`) gave 6 clean tags across pom +
   config + Java without strain.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 67 gate-run PASS
```
(One line, after the verdict is set. Not executed here per task constraint — recorded for the orchestrator.)
