# GATE REPORT — CODE-REVIEW (key 27, Checkstyle)

## Header

- **Gate:** CODE-REVIEW (Step 4b — FLOOR-C second half; technical profile)
- **Chapter key:** 27 (owner; folds 28+29+30 per FINAL_INDEX Ch 16) — this review covers the **Checkstyle** module only
- **Slug:** `27_checkstyle`
- **Module under review:** `08-companion-code/27_checkstyle/`
- **Draft cross-checked:** `03-drafts/27_checkstyle/27_checkstyle_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer (senior-PR judgment + Bash build/lint/extract verification)
- **Scripts / tools run:** `mvn -B -Pquality verify` (clean), strip-and-rebuild integrity check, `check_snippets.sh`, `extract_snippet.sh`, secret/neutrality/placeholder greps, engine-jar inspection (`google_checks.xml`, plugin pom `checkstyleVersion`)
- **Verdict:** **PASS**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21 a reader can copy with confidence. It builds green and
warning-clean (6/6 tests, 0 Checkstyle violations, 0 SpotBugs findings, BUILD SUCCESS on JDK 21.0.11),
every displayed `tag::` region is brace/element-balanced, ≤9 lines, and a complete (non-fragment)
construct, and every code-borne atom traces to the pin — three of the load-bearing facts (the
`ConstantName` default regex, Google's `LineLength` max=100, and the plugin-3.6.0→engine-9.3 two-pin
trap) were re-confirmed live against the engine this run. No security, neutrality, or invention finding
exists. **No BLOCKER.** A handful of NOTE/MINOR items below are either out-of-gate (prose-only source
formatting belongs to VERIFY) or polish; none blocks FLOOR C. The taught Checkstyle house ruleset and
the (intentionally empty, well-justified) SpotBugs suppression filter are coherent and exemplary.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 + code quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity + originality/attribution | **PASS** (one prose-only NOTE routed to VERIFY) |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `CatalogItem` compact constructor validates fully: null `sku`/`name` → NPE, blank `sku` and negative
  `priceCents` → IAE. Prices held as integer cents (exact; no float). 
- `Catalog` ctor rejects duplicate SKUs via `putIfAbsent` and defensively wraps with `Map.copyOf` after a
  `LinkedHashMap` (preserves insertion order, then immutable) — no leak of internal mutable state.
- `findBySku` is the declared failure path: missing SKU → `Optional.empty()`, not a throw; exercised by
  `missingSkuIsAnEmptyOptionalRatherThanAThrow`.
- `PriceFormatter.format` is arithmetically correct across boundaries (`0`→`0.00`, `5`→`0.05`,
  `599`→`5.99`, `1000`→`10.00`) and rejects negatives; `Locale.ROOT` prevents locale-dependent grouping.
- Tests are non-vacuous: every test asserts a concrete value or a specific exception type, both branches
  of `describe(Object)` are covered (match + non-match), and the failure path plus three construction-time
  guards each have an explicit assertion. No resource handles, no swallowed exceptions, no empty catch.

### 2. Idiomatic Java 21 + code quality — PASS
- Record with compact constructor (`CatalogItem`); `instanceof` **pattern variable** in `describe`
  (`value instanceof CatalogItem found`) — modern, and it doubles as the live subject of the
  `PatternVariableName` rule.
- `Objects.requireNonNull(..., "msg")`, `Optional.ofNullable`, `Map.copyOf`, underscore numeric literals
  (`5_000L`), correct `long` cents typing throughout. Utility classes (`PricingRules`, `PriceFormatter`)
  are `final` with private constructors. No raw types, no ad-hoc threads, no `System.out`/`printStackTrace`.
- Build is warning-clean with the parent's `-Xlint:all,-processing` active (compile emitted no warnings).

### 3. Security — PASS
- Secret grep (`password|secret|token|api[_-]?key|credential|private[_-]?key|access[_-]?key`) across
  `src/ config/ pom.xml README.md` → **no matches**. No hardcoded credentials.
- No injection sink (no SQL/JDBC/reflection/file/exec). No network or runtime endpoint — build-time linter
  domain. No error path leaks internals (exception messages are domain values like the offending SKU /
  amount, not stack traces or system internals). `spotbugs-annotations` is `provided` scope (compile-only).

### 4. Simplicity & readability — PASS
- Smallest code that teaches the point: five classes, one cohesive storefront slice. No dead code, no
  unused deps (the lone extra GAV, `spotbugs-annotations`, is used to demonstrate the per-site suppression
  vocabulary and the shared bytecode gate), no gratuitous abstraction.
- Realistic names from the book's `org.acme.storefront` domain (`CatalogItem`, `PricingRules`,
  `findBySku`) — no `Foo`/`Bar`/`tmp`/placeholder. Every public type and method carries a purpose Javadoc;
  the `package-info.java` states the chapter's thesis. README and config headers are clear and accurate.

### 5. Prose↔code fidelity + originality — PASS (one prose-only NOTE → VERIFY)
- All 6 `// include:` directives in the draft resolve to real tag regions (`check_snippets.sh`: 6 pass,
  0 fail). Each region was extracted and inspected (see "Tag-region audit"): all ≤9 lines, all
  brace/element-balanced, none a mid-statement fragment, no duplicate/imbalanced end-tag.
- Live re-verification this run: (a) **ConstantName** default regex `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`
  emitted verbatim by engine 10.26.1 on strip-and-rebuild; (b) **Google `LineLength` max=100** confirmed in
  `google_checks.xml` inside the engine jar; (c) **two-pin trap** confirmed — `maven-checkstyle-plugin`
  3.6.0 pom declares `<checkstyleVersion>9.3</checkstyleVersion>`, and the module overrides it to
  `checkstyle:10.26.1`. House `max=120` is correctly framed as a cited choice.
- **Originality (LEGAL-IP §5):** all Java + config is original storefront-domain code authored for this
  book, modelled on the book's own peer modules (09/26), not on any Checkstyle quickstart, sample POM, or
  the bundled `google_checks.xml`/`sun_checks.xml`. Short factual tokens (rule IDs, the regex, GAVs) are
  not copyrightable. No unattributed verbatim lift; no per-region attribution required. PASS.

### 6. Neutrality in code — PASS
- Banned-phrasing scan (`better than | unlike X | superior | beats | the problem with | outperform | …`)
  across `src/ config/ pom.xml README.md` → **no matches**. Comments and `package-info.java` explicitly
  state "No tool is crowned … Checkstyle owns the style/convention layer," and the SpotBugs gate is framed
  as complementary (different vantage point), never as a contest.

---

## The taught artifacts (config quality judgment)

- **House ruleset (`config/checkstyle/checkstyle.xml`) — coherent and exemplary.** Correct module
  hierarchy (`Checker` → project props + `LineLength` + `SuppressWarningsFilter`; `TreeWalker` → AST
  checks + `SuppressWarningsHolder`). The holder/filter pairing is wired correctly (holder inside
  `TreeWalker`, filter at `Checker` level) — this is the subtle bit teams get wrong, and it is right here.
  Deliberately small/high-signal (naming, imports, braces, modifiers, a size limit), with a header comment
  honestly stating what it omits (Indentation, MagicNumber, mandatory Javadoc) and why. `EmptyBlock`
  `option=text` is a sensible, defensible choice. DTD declared. This is a ruleset a reader can adopt.
- **SpotBugs filter (`config/spotbugs/spotbugs-exclude.xml`) — exemplary by being empty, with the right
  rationale.** The comment makes the teaching point: defend the representation rather than suppress, and a
  real reviewed FP would be named narrowly by class/method/pattern, never by disabling a detector. Correct
  filter namespace. Effort `Max` / threshold `Medium` are reasonable for a teaching module.

---

## Build / lint result

- `mvn -B -Pquality -f pom.xml clean verify` (JDK 21.0.11) → **BUILD SUCCESS**:
  `Tests run: 6, Failures: 0, Errors: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0`.
- **Warning-clean:** compile under parent `-Xlint:all,-processing` produced no warnings;
  SpotBugs `No errors/warnings found`. (Parent surfaces warnings via `-Xlint:all` but does **not** set
  `failOnWarning`/`-Werror` — observed-clean rather than enforced-clean; see NOTE 4.)
- **Integrity (load-bearing suppression):** removing `@SuppressWarnings("checkstyle:ConstantName")` →
  `BUILD FAILURE` with `Name 'centsFormat' must match pattern '^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'.
  [ConstantName]`. Source restored byte-identical.

---

## Tag-region audit (BLOCKER-class check — all clear)

| Tag | File | Lines | Balanced? | Complete construct? |
|---|---|---|---|---|
| `naming-rules` | `config/checkstyle/checkstyle.xml` | 7 | yes (self-closing `<module/>`) | yes |
| `import-hygiene` | `config/checkstyle/checkstyle.xml` | 3 | yes (self-closing) | yes |
| `line-length` | `config/checkstyle/checkstyle.xml` | 4 | yes (`<module>…</module>`) | yes |
| `suppression-filter` | `config/checkstyle/checkstyle.xml` | 1 | yes (self-closing) | yes |
| `constant-naming` | `src/main/java/org/acme/checkstyle/PricingRules.java` | 5 | yes (no braces) | yes (two complete `;`-terminated fields) |
| `reviewed-suppression` | `src/main/java/org/acme/checkstyle/PriceFormatter.java` | 5 | yes | yes (annotation + complete `;`-terminated field) |

No duplicate or imbalanced `end::` markers anywhere in the tree. No mid-statement fragment.

---

## Findings

Severity scale: BLOCKER / MAJOR / MINOR / NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Prose names bundled configs `openjdk_checks.xml` + `doc_comments_checks.xml`; only `google_checks.xml` + `sun_checks.xml` are present in the 10.26.1 engine jar this module runs. Prose-only — no code/config/tag-region/identifier references these names, so module fidelity is intact. Already marked `⚠ verify-at-pin` in the draft. | NOTE (route to VERIFY) | draft `27_checkstyle_v1.md` lines 66, 169 | Out of CODE-REVIEW scope (no code atom). VERIFY should confirm these two names against the **pinned 13.6.0** docs/jar or soften the prose; the module needs no change. |
| 2 | Module already listed in aggregator `<modules>` (`08-companion-code/pom.xml` line 35) while the EXAMPLE report (its own §"Build-list registration") said registration is deferred until CODE-REVIEW PASS. Harmless now (this gate PASSes), but the ordering ran ahead of contract. | NOTE | `08-companion-code/pom.xml:35` | None required given this PASS; note the sequencing so future modules register only after CODE-REVIEW. |
| 3 | Pin-currency: SOURCE-PIN pins Checkstyle **13.6.0**; module (consistent with all peer modules) runs the locally-cached house engine **10.26.1** via the two-pin override. Already flagged to `09-flags/` by the EXAMPLE gate; rule blocks used exist across the 10.x/13.x lines (green build proves it on 10.26.1). | NOTE | `pom.xml:74-77` | None for this gate. Rebuild unchanged when the aggregator re-pins its engine to the 13.x line. |
| 4 | Build is observed warning-clean but not warning-*enforced*: parent uses `-Xlint:all,-processing` without `failOnWarning`/`-Werror`. Not specific to this module. | MINOR | `08-companion-code/pom.xml` compiler plugin (parent) | Optional book-wide: consider `<failOnWarning>true</failOnWarning>` so "warning-clean" is a gate, not a habit. Out of this module's scope. |
| 5 | `lint_citations.sh` reports the draft's Back-matter Sources lack the two-tier structure / per-row plain-URL / date-verified markers. This is prose-sourcing format, owned by VERIFY/AUDIT, not CODE-REVIEW. | NOTE (route to VERIFY) | draft Back-matter (lines 167-173) | Hand to source-verifier; no module impact. |

### Blockers

**None.**

- [x] No security finding.
- [x] No neutrality finding.
- [x] No invention / unattributed-verbatim finding.
- [x] No broken / imbalanced / duplicate / oversized tag region.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic Java 21 / security / simplicity / prose-code-fidelity /
  neutrality-in-code all **PASS**. Build green + warning-clean; ≥1 integration test per public behavior
  **including the failure path** (`findBySku` empty-Optional + construction-time guards); no hardcoded
  secrets; taught Checkstyle ruleset + SpotBugs filter judged coherent and exemplary.

---

## Learnings & pipeline suggestions

- **Config-centric modules are reviewable as code.** Re-confirming the three load-bearing config facts
  live (engine-emitted ConstantName regex via strip-rebuild, Google `LineLength=100` from the engine jar,
  plugin `checkstyleVersion=9.3` from the plugin pom) is cheap and turns "trust the dossier" into "trust
  the engine." Recommend this engine-jar/plugin-pom spot-check become a standard CODE-REVIEW step for the
  PMD (28), SpotBugs (29), and Error Prone (30) peer modules.
- **Separate prose-fact-trace from code-fact-trace at the gate boundary.** Two NOTES here
  (`openjdk_checks.xml`/`doc_comments_checks.xml`; Sources two-tier format) are real but belong to
  VERIFY/AUDIT, not CODE-REVIEW, because no code atom carries them. Worth a one-line note in the
  code-reviewer brief that prose-only claims route out rather than block FLOOR C.
- **Registration-before-review drift.** The module was already in the aggregator `<modules>` before this
  gate ran, contrary to the EXAMPLE report's stated deferral. A tiny pipeline guard (register only on
  CODE-REVIEW PASS) would keep the reactor honest about what has cleared the gate.

Append to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh code-reviewer 4b 27 gate-run PASS
```
