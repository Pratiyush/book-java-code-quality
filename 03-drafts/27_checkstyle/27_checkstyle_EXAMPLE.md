# GATE REPORT — EXAMPLE (key 27, Checkstyle)

## Header

- **Gate:** EXAMPLE (Step 4b — EXAMPLE-BUILD)
- **Chapter key:** 27 (owner; folds 28+29+30 per FINAL_INDEX Ch 16) — this build covers the **Checkstyle** module only
- **Slug:** `27_checkstyle`
- **Draft under review:** `03-drafts/27_checkstyle/27_checkstyle_v1.md`
- **Run date:** 2026-06-26
- **Reviewer:** example-builder (Maven build + `extract_snippet.sh` + `check_snippets.sh`)
- **Scripts run:** `check_snippets.sh`, `extract_snippet.sh`, `xmllint`, `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; built and verified by hand)
- **Verdict:** **PASS**

---

## Verdict rationale

The Chapter 27 Checkstyle companion module builds green at the pin via `mvn -B -Pquality verify` (BUILD
SUCCESS: 6/6 tests, **0 Checkstyle violations**, **0 SpotBugs findings**) on JDK 21.0.11, and all six
displayed snippets resolve to real tag regions (1–7 lines each) inside the compiled/gated files. Every
Checkstyle fact in the module traces to the key-27 dossier and SOURCE-PIN; the `ConstantName` default
regex is additionally confirmed live by the engine. Floor C (SOURCE-TRACE + COMPILE) is satisfied.

---

## Module

- **Path:** `08-companion-code/27_checkstyle/`
- **Artifact:** `checkstyle-house-ruleset` (child of the `org.acme.storefront:companion-code` aggregator)
- **Parent / no own version:** sets `<parent>` to the aggregator; carries no `<groupId>`/`<version>`
  literal and imports no BOM of its own. The one version literal is the pinned `spotbugs-annotations`
  GAV (`provided` scope), matching the peer-module shape (09, 26).
- **Subject framing:** config-centric. The chapter's displayed snippets are tag regions **inside the
  Checkstyle ruleset that actually gates the module** (`config/checkstyle/checkstyle.xml`), so the printed
  config and the running config are one artifact.

### FLOOR C guard — both preconditions logged

- **(a) Runtime meets minimum:** `openjdk version "21.0.11" 2026-04-21` — ≥ Java 21 anchor. PASS.
- **(b) Build green:** `mvn -B -Pquality -f 08-companion-code/27_checkstyle/pom.xml verify` →
  `BUILD SUCCESS` (`Tests run: 6, Failures: 0, Errors: 0`; `You have 0 Checkstyle violations.`;
  `BugInstance size is 0`). PASS.

Exact command:
```
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
mvn -B -Pquality -f 08-companion-code/27_checkstyle/pom.xml verify
```

---

## Snippet tag-includes (resolved line counts, ceiling = 9)

`check_snippets.sh 03-drafts/27_checkstyle/27_checkstyle_v1.md` → **6 marker(s); 6 pass, 0 fail.**

| Tag | Backing file | Lines | Kind |
|---|---|---|---|
| `naming-rules` | `config/checkstyle/checkstyle.xml` | 7 | config (XML-comment markers) |
| `import-hygiene` | `config/checkstyle/checkstyle.xml` | 3 | config |
| `line-length` | `config/checkstyle/checkstyle.xml` | 4 | config |
| `suppression-filter` | `config/checkstyle/checkstyle.xml` | 1 | config |
| `constant-naming` | `src/main/java/org/acme/checkstyle/PricingRules.java` | 5 | Java (rule-governed) |
| `reviewed-suppression` | `src/main/java/org/acme/checkstyle/PriceFormatter.java` | 5 | Java (per-site suppression) |

Marker points inserted into the draft (one-line lead-in each, no prose deleted, locked voice):
- after the config-hierarchy paragraph (§ "Checkstyle — encoding a written standard"): `naming-rules`,
  `import-hygiene`, `line-length`, `suppression-filter`;
- after the single-file-limit / `ConstantName` paragraph: `constant-naming`, `reviewed-suppression`;
- foot-of-draft companion spec updated with a built-state note + the `Snippet tags:` line.

XML-comment tag technique: the rule blocks carry `<!-- tag::NAME[] -->` … `<!-- end::NAME[] -->`;
`extract_snippet.sh` matches `tag::NAME[]`/`end::NAME[]` regardless of comment syntax. (Caveat learned:
the literal `-->` cannot appear inside a surrounding XML comment — XML forbids `--` within comments — so
header prose describes the markers in words, not by quoting `<!-- … -->`.)

---

## Enterprise-grade checklist

- **Pinned dependency set via the inherited parent.** Runtime (`maven.compiler.release=21`), JUnit
  (`junit-bom`), and AssertJ all inherited from the aggregator; no version literal added except the one
  pinned `spotbugs-annotations:4.9.3` (`provided`).
- **Externalized config profile.** The static-analysis gate is the opt-in `-Pquality` profile (same shape
  as modules 09/26); the default build stays fast. Rules live in `config/checkstyle/checkstyle.xml`, not
  hard-coded in the POM.
- **Two-pin engine override (chapter's own lesson, in the build).** `maven-checkstyle-plugin:3.6.0`
  bundles Checkstyle 9.3; the profile overrides it to the pinned house engine
  `com.puppycrawl.tools:checkstyle:10.26.1` via a plugin-level dependency.
- **Test that exercises the mechanism.** `CheckstyleHouseRulesetTest` (6 tests) drives the slice the
  ruleset governs (record validation, the `UPPER_SNAKE` pricing constants, the suppressed formatter,
  SKU lookup + the pattern-variable branch, failure path). The `-Pquality` run asserts the complementary
  half — that the slice passes Checkstyle and SpotBugs. Surefire JUnit-Platform provider auto-detected;
  no extra logging/system-property harness needed for this module.
- **Observability/health surface.** N/A by topic — Checkstyle is a build-time source linter with no
  runtime/health surface; recorded as not-applicable rather than bolted on (HONEST-LIMITATIONS in the
  build: the module is held to a *bytecode* gate too, to show the style gate's reach is necessary, not
  sufficient).
- **Explicit failure path.** `Catalog.findBySku` returns `Optional.empty()` for a missing SKU (defined,
  benign) rather than throwing; construction-time guards (`CatalogItem`, duplicate-SKU, negative-amount)
  fail fast and loud. Both halves exercised by the test.
- **Load-bearing suppression (integrity-checked).** Removing the `@SuppressWarnings("checkstyle:ConstantName")`
  on `PriceFormatter.centsFormat` makes the gate fail with
  `Name 'centsFormat' must match pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'. [ConstantName]` — verified by a
  strip-and-rebuild, then restored to green. The suppression is a recorded judgment, not decoration.

---

## Captured screenshots (Step 4c)

**No captures planned.** The chapter's figure plan (GUIDELINES §8) is a single *designed* conceptual
diagram (`Fig 16.1` / `fig27_1.png` — the detection-time axis), authored as HTML→PNG separately, not a
captured UI surface. Checkstyle has no subject-native dev console / live UI to capture from a running app;
this module produces no web/health endpoint. Zero captured screenshots for this module; no sidecars
required.

---

## Floor C — SOURCE-TRACE confirmation (every fact traces to the pin)

| Atom in module | Value | Traces to |
|---|---|---|
| Config hierarchy `Checker` / `TreeWalker` / `Check` | structural | dossier §2.2; SOURCE-PIN §2 Checkstyle (`checkstyle.org/config.html`) |
| `severity` = `error` (default) / `warning` / `info` / `ignore`; gate on `error` | tool default | dossier §2.2 / VERIFY row 6 |
| `ConstantName` default regex `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` | UPPER_SNAKE | dossier §2.3 + flag file; **confirmed live by engine 10.26.1** in this build |
| `LineLength` default `max=80`, `ignorePattern=^(package\|import) .*`; Google config 100; house 120 cited | size limit | dossier §2.3 / §2.5 (style-value neutrality) |
| Naming family incl. `RecordComponentName` / `PatternVariableName` (modern Java) | identity | dossier §2.3 (`checkstyle.org/checks/naming/index.html`) |
| Import checks `AvoidStarImport` / `RedundantImport` / `UnusedImports` | identity | dossier §2.6 (Imports category) |
| `SuppressWarningsFilter` (+ `SuppressWarningsHolder`); `@SuppressWarnings("checkstyle:…")` | suppression layer | dossier §2.4 (`checkstyle.org/filters/index.html`) |
| `maven-checkstyle-plugin` 3.6.0 bundles CS 9.3; override coord `com.puppycrawl.tools:checkstyle` | two-pin | dossier §2.7 / flag file item 3 |
| Engine pin `checkstyle:10.26.1`; `spotbugs-annotations:4.9.3`; `spotbugs-maven-plugin:4.9.3.0` | house pin | peer modules 09/26 (the consistent companion-code engine); cached in local `.m2` |

**No invented atoms.** Every rule ID, property, regex, and GAV above is dossier- or pin-traceable.
**Pin-currency note (NOT a blocker):** SOURCE-PIN §2 now pins Checkstyle at **13.6.0** (the dossier was
written pre-pin). All 22 built peer modules use the locally-cached house engine **10.26.1** via the
two-pin override, and this module follows that same consistent choice (per the build instruction). When
the companion-code aggregator re-pins its engine to the 13.x line (re-pin runbook step 4), this module
rebuilds against it unchanged — its rule blocks use only checks present across the 10.x/13.x lines. Logged
to `09-flags/` as a future re-pin item (see below), not a content defect.

---

## LEGAL-IP §5 — original-for-this-book confirmation

File-by-file confirmation that nothing is a copied or lightly-edited upstream sample:

- `pom.xml`, `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml` — authored for this
  book, modelled on the book's own peer modules (09/26), not on any Checkstyle quickstart/sample POM or the
  bundled `google_checks.xml`/`sun_checks.xml`. The ruleset is a curated original list; no upstream config
  file is copied. Short factual tokens (rule IDs, the `LineLength` default, the `ConstantName` regex) are
  not copyrightable expression.
- `CatalogItem.java`, `PricingRules.java`, `Catalog.java`, `PriceFormatter.java`, `package-info.java`,
  `CheckstyleHouseRulesetTest.java` — original storefront-domain code written for this chapter.
- No `NOTICE`/license-header boilerplate, getting-started skeleton, or whole-file block was copied from any
  source or its samples. No region is taken substantially verbatim from a specific upstream file, so no
  per-region attribution is required.

---

## Build-list registration (deferred, per contract)

The module is **NOT yet added** to `08-companion-code/pom.xml` `<modules>`. Per the EXAMPLE-BUILD contract,
registration waits until the build is green (done) **AND** the CODE-REVIEW gate (Step 4b, `code-reviewer`)
passes. Build is green; CODE-REVIEW is the next gate. `08-companion-code/pom.xml` was not edited (per task
constraint). Registering it is a one-line add after CODE-REVIEW PASS.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | XML comments forbid `--`; an embedded literal `<!-- … -->` in the ruleset header broke the parse on first build | NOTE (fixed) | `config/checkstyle/checkstyle.xml` header | Describe markers in words, not by quoting the comment syntax — done |
| 2 | SOURCE-PIN §2 pins Checkstyle 13.6.0 but the house/companion engine is 10.26.1 (consistent across all peers) | NOTE | `pom.xml` two-pin override | Re-pin item flagged to `09-flags/`; rebuild on aggregator re-pin |

### Blockers

**None.**

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality verify` at the pin; every displayed
  snippet resolves to a real, ≤9-line tag region in the gated files; FLOOR C source-trace clean.

---

## Learnings & pipeline suggestions

- **Config-centric chapters bind snippets directly to the gating config.** Wrapping the *live* ruleset's
  rule blocks in XML-comment tag regions makes the "prose ↔ code one artifact" guarantee true for config,
  not just Java — the displayed rules are the rules that ran. Reusable pattern for the PMD ruleset (key 28),
  the SpotBugs filter (key 29), and any future YAML/properties-driven chapter.
- **XML-comment tag caveat (worth promoting to EXAMPLES-GUIDE).** `tag::NAME[]`/`end::NAME[]` work inside
  `<!-- … -->`, but the surrounding *prose* comments must never contain a literal `--` (so do not quote
  `<!-- … -->` inside a comment). One sentence in the guide would have saved a build cycle.
- **Always integrity-check a "reviewed suppression" by removing it.** The strip-and-rebuild both proves the
  suppression is load-bearing and incidentally re-verifies the rule's default (here the `ConstantName`
  regex straight from engine 10.26.1) — a cheap live source-verification. Recommend making this a standard
  EXAMPLE-BUILD step for any module that ships a suppression.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 27 gate-run PASS
```
