# GATE REPORT — EXAMPLE-BUILD — Chapter 35 (key 81, folds 82)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 81 (owner; folds 82) — FINAL_INDEX Ch 35, Part IX
- **Slug:** `81_branch_protection_precommit_parity`
- **Draft under review:** `03-drafts/81_branch_protection_precommit_parity/81_branch_protection_precommit_parity_v1.md`
- **Module path:** `08-companion-code/81_branch_protection_precommit_parity/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `check_snippets.sh` (draft markers); `extract_snippet.sh` (per-tag line-count); `mvn -B -Pquality verify`; ruby/Psych YAML parse; `mvn -version` / `java -version`
- **Verdict:** **PASS** (FLOOR C) — `[MANUAL — tooling pending]` build-state (key-01 pilot not yet cleared)

---

## Verdict rationale

The module builds green and warning-clean via `mvn -B -Pquality verify` on JDK 21.0.11 (both FLOOR C
preconditions met and logged below); all five displayed snippets resolve to real `tag::` regions ≤9
lines inside compiling/parsing files; every fact traces to SOURCE-PIN or the dossier, with the two
SaaS surfaces (GitHub rulesets, pre-commit framework) flagged as dated-at-use rather than asserted as
pinned. No invented atom, no banned phrasing, no copied upstream file.

---

## FLOOR C guard — both preconditions (required for any PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) runtime ≥ Java 21 (SOURCE-PIN anchor) | `openjdk version "21.0.11" 2026-04-21` (matches SOURCE-PIN §1 JDK 21.0.11); Maven 3.9.16 (SOURCE-PIN §4) | MET |
| (b) `mvn -B -Pquality verify` GREEN | `[INFO] BUILD SUCCESS`; `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`; `0 Checkstyle violations`; `BugInstance size is 0`; **0 `[WARNING]` lines** (warning-clean) | MET |

- **Exact build command:** `mvn -B -Pquality -f 08-companion-code/81_branch_protection_precommit_parity/pom.xml clean verify`
- **Exact result line:** `[INFO] BUILD SUCCESS` — `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`
- Default (`-DskipTests`-free) build and the `-Dparity.profile=prod` externalized-config selection both build green.

---

## Tag-include regions (anti-drift) — `check_snippets.sh`: 5/5 PASS

| Tag (spec) | File | Displayed lines (≤9) |
|---|---|---|
| `#required-checks` | `config/branch-protection/ruleset.yml` | 7 |
| `#linear-history-and-review` | `config/branch-protection/ruleset.yml` | 6 |
| `#precommit-fast-hooks` | `.pre-commit-config.yaml` | 9 |
| `#parity-assertion` | `src/main/java/org/acme/parity/GateParity.java` | 8 |
| `#feedback-not-enforcement` | `src/main/java/org/acme/parity/GateParity.java` | 5 |

Markers inserted into the draft at 5 points (lines 50, 54, 66, 72, 78), each with a one-line locked-voice
lead-in; **no prose deleted**. `**Snippet tags:**` line added in the back matter; the companion-module
spec footer updated to `✅ EXAMPLE-BUILD = BUILT GREEN [MANUAL — tooling pending]`.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | How met |
|---|---|---|
| — | Child of the ONE aggregator | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`, `relativePath ../pom.xml`; no own groupId/version; no own BOM import. **NOT yet registered** in the parent `<modules>` (correct: register only after green + CODE-REVIEW; task forbade editing the parent pom). |
| 1 | Pinned platform (one inherited property) | Java release inherited from the aggregator (`maven.compiler.release=21`, SOURCE-PIN §1); JUnit/AssertJ versions inherited from the aggregator's `dependencyManagement`; module carries no version literal except the opt-in `-Pquality` analyzer engines (same as peers 75/27). |
| 2 | Externalized config profiles | `parity-dev.properties` (lenient: drift warns) and `parity-prod.properties` (strict: drift fails), selected by `-Dparity.profile` via `ParityPolicy.load` — no threshold compiled in. |
| 3 | ≥1 integration test of the mechanism | `GateParityTest` — 10 cases driving the parity check end-to-end through the externalized policy (in-parity, drift-naming-the-missing-check, one-directional parity, the strict failure path, dev-vs-prod profiles, readiness, null guards). |
| 4 | Observability/health surface | `GateParity.driftCount()` (headline metric) + `isReady()` (readiness over the wired policy). Scoped in-process, not HTTP — a build/workflow concern has no running service to expose a network probe on (§1.2 scoped-out reason recorded in README). |
| 5 | Explicit failure path (HONEST-LIMITATIONS in code) | `GateParity.enforce` throws `ParityBrokenException` (naming the missing CI-required checks) under a strict policy on broken parity; `ParityResult` is a sealed `InParity`/`Drifted`. Driven by the `prodFailsFastOnDrift` + `devProfileSurfacesDriftAsWarning` tests. |
| — | Test-harness setup | JUnit 6 platform via the aggregator's surefire 3.5.6 + junit-bom; no per-module plugin/log config needed (plain JDK module; no framework log manager). Confirmed against a green test run. |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Branch-protection ruleset authored as **YAML, not JSON** | NOTE | `config/branch-protection/ruleset.yml` | Intentional: JSON cannot carry the `# tag::` comment markers the anti-drift machinery requires (a `// tag::` line would break JSON validity). YAML documented-settings is the same protection set and is valid for tagging; recorded in the flag. No action. |
| 2 | SaaS surfaces are dated-at-use, not version-pinned | MAJOR (handled) | `ruleset.yml` (GitHub rulesets fields); `.pre-commit-config.yaml` (`rev: VERSION_PINNED_AT_ADOPTION`) | Flagged to `09-flags/81_branch_protection_precommit_saas_dated_at_use.md`; marked dated-at-use 2026-06 in file comments + README + draft footer; re-confirm/digest-pin at `/pin-source` and before public push. |
| 3 | Module not in parent `<modules>` | NOTE | `08-companion-code/pom.xml` | Correct per EXAMPLES-GUIDE §2 (register only after green + CODE-REVIEW); the task explicitly forbade editing the parent pom. Built standalone via `-f`. |
| 4 | `ParityBrokenException` initially raised a `-Xlint` serializable-field warning | MINOR (fixed) | `ParityBrokenException.java` | Removed the non-serializable `List` field; the missing checks live in the message + on `ParityResult.Drifted` (one home). Re-built warning-clean. |

---

## Blockers

**None.** FLOOR C: both preconditions MET; build green and warning-clean; snippets resolve; sources trace.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality verify` at SOURCE-PIN (JDK 21.0.11);
  every displayed snippet resolves to a real bounded `tag::` region (≤9 lines) in a compiling/parsing file;
  FLOOR C source-trace clean (every atom traces to SOURCE-PIN or the dossier; SaaS surfaces flagged).
- [x] Folder named with frozen `NN_slug`; child of the ONE aggregator (parent set, relativePath, no own version/BOM).
- [x] Module added to parent `<modules>` ONLY after green + CODE-REVIEW — **deferred** (not yet added; awaits CODE-REVIEW).
- [x] Externalized `dev`/`prod` profiles; ≥1 integration test; observability surface (scoped-out reason recorded); explicit failure path driven by a test.
- [x] NEUTRALITY + never-invent across comments, identifiers, log/error strings (banned-phrase scan clean on all module files + draft adds).
- [x] **LEGAL-IP §5 — every file ORIGINAL-FOR-THIS-BOOK:** all 15 module files written for this book. The
  `org.acme.parity` domain is original; `.pre-commit-config.yaml` differs from peer 70's (verified by diff);
  the curated Checkstyle ruleset and empty SpotBugs filter are the book's shared house pattern, not an
  upstream sample. No upstream quickstart/skeleton/NOTICE copied; no substantially-verbatim block requiring attribution.
- [x] No public push; local build only (COMPANION-REPO §5 sign-off not crossed).

---

## Source-trace (every atom → pin/dossier)

- Java 21 anchor / JDK 21.0.11 / Maven 3.9.16 → SOURCE-PIN §1, §4.
- JUnit 6 / AssertJ 3.27.7 → SOURCE-PIN §3 (inherited from aggregator).
- Checkstyle/SpotBugs `-Pquality` engines (Checkstyle 10.26.1 via plugin 3.6.0; spotbugs-maven-plugin 4.9.3.0) → same locally-cached engines the peer modules (75/27) pin; SOURCE-PIN §2 lines.
- pre-commit framework (`.pre-commit-config.yaml`) → SOURCE-PIN §5 (pre-commit, 2026-04); gitleaks `rev:` = dated-at-use placeholder (no pinned row) → flagged.
- GitHub branch-protection / rulesets settings → SOURCE-PIN §5 (GitHub, rolling/dated 2026-06) → flagged.
- Trunk-based development as a DORA capability for delivery performance → SOURCE-PIN §5 (2025 DORA report, `dora.dev`) — prose only; crowns no model.
- Required CI job names (`build-and-lint` … `quality-gate`) → this book's illustrative CI (consistent with peer 75 `ci/quality-gates.yml`), not asserted platform facts.

---

## Captured screenshots (Step 4c)

**No captures planned.** The chapter's figure plan is one designed conceptual diagram (`fig81_1` — the
feedback-latency ladder, already authored as HTML→PNG in `05-figures/81_branch_protection_precommit_parity/`).
There is no subject-native UI surface to capture: the module is a build/workflow concern (config files +
a plain-JDK parity check), with no running dev console, API explorer, or health view. Recorded here per
the Step-4c "ZERO captures" rule; the designed diagram is authored separately and is not the
example-builder's job.

---

## Flags raised

- `09-flags/81_branch_protection_precommit_saas_dated_at_use.md` (OPEN) — GitHub rulesets + pre-commit
  framework/hook `rev:` are SaaS/rolling → dated-at-use 2026-06; resolve at `/pin-source` / SOURCE-VERIFY
  (Step 5) and at public-push sign-off (digest-pin the gitleaks `rev:`, re-confirm ruleset field names).

---

## Learnings & pipeline suggestions

- **JSON config cannot carry tag-include markers.** A `// tag::` line breaks JSON validity and a `#` is not
  a JSON comment, so any config artifact whose regions the book displays must be a comment-bearing format
  (YAML/TOML/properties). When a chapter's natural artifact is JSON (a GitHub ruleset payload), author it as
  documented YAML instead — same settings, valid for the anti-drift machinery. Worth a one-line note in
  EXAMPLES-GUIDE §5 so future config-centric chapters don't hit the 9-line/validity collision.
- **`-Xlint:all` (inherited from the aggregator) catches the serializable-field smell on exceptions.** An
  exception carrying a non-serializable `List` field warns; the clean idiom is to keep auxiliary data in the
  message (and on the typed result value) rather than a field, so the exception stays a plain serializable
  signal. Mirrors the peers' "JDK-only, immutable model" discipline; candidate idiom note for the guide.
- **The parity check is a good fit for requirement 5 (failure path in code).** "Local must reproduce every
  required CI check" turns the chapter's central abstract claim into a tested code path with a real
  exception, exactly the HONEST-LIMITATIONS-in-code the failure-path requirement is designed to force —
  rather than bolting a fault-tolerance annotation onto a topic that doesn't call for it.
- Appended to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 81 gate-run PASS
```
