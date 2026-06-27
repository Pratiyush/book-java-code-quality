# GATE REPORT — CODE-REVIEW — Chapter 67 (FINAL_INDEX Ch 29)

## Header

- **Gate:** CODE-REVIEW (FLOOR-C, second half)
- **Chapter key:** 67 (folds 68)
- **Slug:** `67_reproducible_builds_license_compliance`
- **Module under review:** `08-companion-code/67_reproducible_builds_license_compliance/`
- **Draft under review:** `03-drafts/67_reproducible_builds_license_compliance/67_reproducible_builds_license_compliance_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` (senior-PR-review pass on published-deliverable code)
- **Builds/checks run:** `mvn -B -o -Pquality -f pom.xml clean verify` (offline) · warning scan · two-build SHA-256 reproducibility proof · `cmp` · `unzip -l` entry-timestamp + MANIFEST inspection · `dependency:list` · hardcoded-secret grep · NEUTRALITY banned-phrase scan · per-tag line-count + brace/element-balance check
- **Toolchain:** JDK 21.0.11 (Homebrew openjdk@21; SOURCE-PIN anchor), Maven 3.9.16
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21, and it teaches exactly what the chapter claims. The build is
green and warning-clean, the license allow-list gate passes and emits a correct `THIRD-PARTY` attribution,
and the chapter's central claim is independently reproduced live: **two fresh `clean package` builds produced
a byte-identical jar with the same SHA-256 (`b5b3d7beae2ea03d0445c97f6e88fa9a7bbf425452745f51c8f8ac3cd30990d3`),
confirmed by `cmp` at the byte level**, all offline. No BLOCKER. The verdict is PASS-WITH-FIXES only on
small polish items (one prose↔code SPDX-id-form mismatch that lives in the prose, two optional
readability/test-explicitness nits in the code, and one tiny pom-comment over-statement). None block FLOOR C;
the prose SPDX-form item is owned by the drafter/source-verifier, the rest by the example-builder.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** (2 MINOR polish) |
| 5 | Prose↔code fidelity + originality/attribution | **PASS** (1 MINOR, prose-side) |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `LicensePolicy.evaluate` scans the full list, collects every disallowed entry, and throws naming all of
  them — the failure path is real, not cosmetic. `isDisallowed` is a pure allow-list membership test.
- `DisallowedLicenseException` is a correctly-formed serializable exception: `extends RuntimeException`,
  `@Serial private static final long serialVersionUID`, the `List<License>` field is `transient` (a
  `License`/`LicenseCategory` graph is not declared `Serializable`), and `disallowed()` null-guards with
  `List.of()` for the post-deserialization case. Constructor copies via `List.copyOf` (immutable, NPE on null
  element). This is textbook-correct, not over-engineered.
- `ReproducibleArtifact` validates in the constructor (`requireNonNull` + non-blank digest), `matches` is a
  null-guarded digest equality. No resource handles, no swallowed exceptions, no leaks anywhere in the module.
- Tests: 7 tests, 18 assertion/evaluate lines, none vacuous. Coverage spans the happy path, the **failure
  path** (`disallowedLicenseFailsTheGate`, asserting `containsExactly(banned)`), and both honest-limit branches
  the prose promises (`transitiveSurpriseIsCaughtByScanningTheFullGraph`, `policyIsTunedToDistributionMode`).
- Reproducibility verified empirically (see Build/Lint section). MANIFEST inspection confirms `Built-By` /
  `Build-Timestamp` are stripped and every jar entry is normalised to a single fixed instant (`01-01-2000
  00:00`) — the mechanism the prose and the `repro-plugin` comment describe actually executes.

### 2. Idiomatic Java 21 — PASS
- Records (`License`) with a compact constructor for validation; `enum` for the obligation band; `Set.copyOf`
  / `List.copyOf` defensive immutables; `Stream.toList()`; `String.formatted(...)`; `@Serial`. Accessor-style
  method names on the record-like types. All current, idiomatic Java 21.
- No anti-patterns: no `System.out`/`System.err`, no `printStackTrace`, no raw `new Thread(...)`/`.start()`,
  no blocking concerns (none apply to this domain). Confirmed by grep.
- The pom uses the framework idioms correctly: properties for versions, profile-scoped quality plugins,
  externalized config under `config/`, executions bound to the right phases (`package` for strip-jar,
  `verify` for the license/checkstyle/spotbugs gates).

### 3. Security — PASS
- Hardcoded-secret grep over `src`, `config`, `pom.xml` (password/secret/token/apikey/private-key patterns):
  **no matches**. The only string literals are SPDX ids, fake digests (`sha256:abc123`), and field names.
- No injection sink, no error-response leakage (this is a library/build module, not an endpoint). The
  exception message lists component + declared SPDX id only — appropriate, not internal-leaking.
- The allow-list is read from a `file://` URL under the module's own `config/` dir — no remote/untrusted input.

### 4. Simplicity & readability — PASS (2 MINOR polish)
- Smallest code that teaches the two facets. No dead code, no unused deps (commons-lang3 is the deliberate
  real dependency that gives the license gate a tree to read; junit/assertj are test-scoped), no gratuitous
  abstraction, realistic names (`org.acme.repro`, `commons-lang3`, `app-1.0.0.jar`). Every public type carries
  a purpose Javadoc. SpotBugs filter is empty-with-a-reason (good discipline).
- MINOR items #2 and #3 below (an always-true probe; an implicit no-throw assertion) are polish, not defects.

### 5. Prose↔code fidelity + originality/attribution — PASS (1 MINOR, prose-side)
- All six `// tag::` regions exist, are ≤9 lines, brace/element-balanced, and say what the prose says. The
  six draft `<!-- include: ... -->` directives map exactly 1:1 to the six tags (no orphan, no broken pull).
- Version traceability: JDK 21.0.11, Maven 3.9.16, AssertJ 3.27.7, SPDX=ISO/IEC 5962:2021 all trace to
  SOURCE-PIN. `commons-lang3` 3.18.0 and both plugin GAVs (`reproducible-build-maven-plugin` 0.17,
  `license-maven-plugin` 2.7.1) are **not** separately pinned in SOURCE-PIN and are correctly held as
  named properties + flagged in `09-flags/67_repro_license_plugin_versions_unpinned.md` (the flag even records
  the matching live SHA-256 and the plugin's exact-equality matching behaviour). Two-pin discipline followed.
- Originality (LEGAL-IP §5): the `org.acme.repro` package is original-for-this-book — a purpose-built in-code
  analogue of the build facets, not a copied upstream quickstart. No verbatim lift; no attribution required.
- MINOR #1: prose↔SPDX-form mismatch — the **prose** writes the bare `LGPL-2.1` (draft lines 84, 151; dossier
  comment line 8) while the **code** correctly uses the SPDX-canonical `LGPL-2.1-only`. `LGPL-2.1` (no suffix)
  is not a current SPDX identifier. This is a prose-only nit (no displayed code region is affected); it belongs
  to the drafter/source-verifier and the source-verifier addendum already lists "individual SPDX license-id
  specifics" as verify-at-pin. Recorded here for cross-gate visibility, not as a code-review code fix.

### 6. Neutrality in code — PASS
- Banned-phrase scan (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` / etc.) over
  `src`, `config`, `pom.xml`, `README.md`: **no matches**. Comparator tools (FOSSA, ScanCode, Gradle) are
  named neutrally in comments/README with no crowning language. License content reads **factual, not legal
  advice** consistently — every type, the enum, the config header, and the README repeat the caveat and defer
  obligation interpretation to counsel. No legal-advice phrasing anywhere.

---

## Findings

Severity scale: BLOCKER · MAJOR · MINOR · NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Prose uses bare `LGPL-2.1`; code uses SPDX-canonical `LGPL-2.1-only`. `LGPL-2.1` is not a current SPDX id. | MINOR | `03-drafts/.../67_..._v1.md:84` and `:151` (and dossier comment `:8`) | Drafter/source-verifier: change prose `LGPL-2.1` → `LGPL-2.1-only` (matches code + SPDX). Not a code change. |
| 2 | `ReproducibleArtifact.isVerifiable()` returns `!digest.isBlank()` but the constructor already rejects blank/null digest, so it can only return `true`. | MINOR | `src/main/java/org/acme/repro/ReproducibleArtifact.java:68-70` | Optional: it parallels the deliberately always-true `isIntegrityNotCorrectness()` as a readiness-probe analogue; the Javadoc explains it. Either keep as-is (documented teaching choice) or add one line to the Javadoc noting the invariant makes it always true post-construction. |
| 3 | Happy-path gate test asserts "does not throw" implicitly via a bare `policy.evaluate(tree)` call. | MINOR | `src/test/java/org/acme/repro/BuildIntegrityTest.java:62` | Optional: wrap as `assertThatCode(() -> policy.evaluate(tree)).doesNotThrowAnyException()` to make the assertion explicit. Not vacuous as-is (a throw fails the test). |
| 4 | pom comment (line 26) and README call `commons-lang3` "pinned (3.18.0, Apache-2.0)" — but it is a module-local literal, NOT a SOURCE-PIN row (correctly flagged). Slight over-statement of "pinned". | MINOR | `pom.xml:26`; `README.md:101` | Optional: soften to "version-locked (no range), flagged — not separately in SOURCE-PIN" to match the flag file's own framing. |
| 5 | `repro-plugin` tag comment says strip-jar normalises "Built-By, Build-Jdk"; the jar still carries value-stable `Build-Jdk-Spec: 21` / `Java-Version: 21`. Reproducibility is unaffected (SHA identical) since the value is constant on the pinned JDK. | NOTE | `pom.xml:118-119` (comment); jar MANIFEST | No action. `Built-By` is stripped; the remaining Jdk lines are value-stable and do not break byte-identity. Comment is illustrative, not wrong in effect. |
| 6 | Reproducibility independently proven: 2× `clean package` → same SHA-256 `b5b3d7be…`, `cmp` identical, all jar entries at fixed `01-01-2000 00:00`, MANIFEST de-stamped. Matches the SHA recorded in the flag file. | NOTE | module / `09-flags/67_...md` | No action — confirms the chapter's central claim and the EXAMPLE-BUILD record. |

---

## Blockers

**None.** No security, neutrality, invention, or snippet-integrity violation. No displayed tag region is
broken mid-statement; all six are brace/element-balanced and ≤9 lines. FLOOR C is not blocked.

---

## Build / lint result

- `mvn -B -o -Pquality -f pom.xml clean verify` — **BUILD SUCCESS**, fully offline.
- Tests: **7 run, 0 failures, 0 errors, 0 skipped** (`BuildIntegrityTest`), incl. the failure path and both
  honest-limit branches.
- Checkstyle: **0 violations**. SpotBugs (effort=Max, threshold=Medium): **0 bug instances, 0 errors**.
- **Warning-clean:** a fresh `-Pquality verify` produced no `[WARNING]`/deprecation/unchecked lines.
- License gate: allow-list whitelist applied; **passes** for the permissive Apache-2.0 `commons-lang3`;
  `THIRD-PARTY.txt` generated at `target/generated-sources/license/` listing
  `(Apache-2.0) Apache Commons Lang (org.apache.commons:commons-lang3:3.18.0)`.
- **Reproducibility proof (the chapter's headline claim):** two independent `clean package` builds →
  byte-identical jar, SHA-256 `b5b3d7beae2ea03d0445c97f6e88fa9a7bbf425452745f51c8f8ac3cd30990d3` both times;
  `cmp` reports identical bytes; every jar entry normalised to `01-01-2000 00:00`. PASS.
- Per-tag check: `repro-timestamp` 1 ln · `repro-plugin` 7 ln · `license-gate` 6 ln · `repro-verify` 4 ln ·
  `license-allow-list` 8 ln · `license-allow-list-file` 8 ln — all ≤9, all balanced.
- Flag file `09-flags/67_repro_license_plugin_versions_unpinned.md` present, accurate, and consistent with the
  live build (including the matching SHA-256).

---

## Required fixes before next gate

This verdict is PASS-WITH-FIXES; the listed fixes are required to be addressed (or consciously waived with a
one-line note), not optional:
- **#1 (MINOR, prose):** owned by drafter/source-verifier — `LGPL-2.1` → `LGPL-2.1-only` in the draft.
- **#2, #3, #4 (MINOR, code/README/pom):** owned by example-builder — small readability/explicitness/wording
  polish; re-review is trivial. None affect the green build or any displayed snippet.

---

## Learnings & pipeline suggestions

- **Reproducibility is a claim a gate can and should execute, not just read.** Re-running two `clean package`
  builds and diffing SHA-256 turned the chapter's headline assertion from "configured" into "proven on this
  machine," and it matched the SHA the flag file recorded — a strong cross-artifact consistency signal.
  Suggest a tiny reusable script (`.claude/scripts/check_reproducible.sh <module>`: two builds + `shasum` +
  `cmp`) so any repro-claiming module gets a one-command proof at EXAMPLE-BUILD and CODE-REVIEW.
- **SPDX identifier form is a recurring fidelity trap.** Code used the canonical `-only` suffix; prose dropped
  it. A lint rule flagging bare `LGPL-2.1` / `GPL-2.0` / `GPL-3.0` (no `-only`/`-or-later`) in drafts would
  catch this class cheaply at VERIFY.
- **The two-pin / flag discipline for unpinned plugin GAVs continues to work well.** Holding 0.17 / 2.7.1 /
  3.18.0 as properties + a thorough flag (which even decompiled the plugin's match semantics) is the right
  pattern; nothing here needed inventing a pinned fact.
- **MANIFEST inspection is a cheap, high-value reproducibility check** — confirming `Built-By` is stripped and
  entries share one fixed instant explains *why* the SHA is stable. Worth folding into the repro script above.

— code-reviewer, Chapter 29 (key 67) CODE-REVIEW, 2026-06-27.
