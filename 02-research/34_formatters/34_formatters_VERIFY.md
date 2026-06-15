# GATE REPORT — SOURCE-VERIFY (key 34: Formatters — Spotless / google-java-format / palantir-java-format / EditorConfig)

- **Gate:** SOURCE-VERIFY (pipeline step 2)
- **Artifact:** `02-research/34_formatters/34_formatters_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** (0 blockers)
- **Pin status:** ABSENT + unhealable by construction — `SOURCE-PIN.md` is multi-authority, every §2 tool
  row is `TO-PIN`, repo URL is `{URL}`, `/pin-source` never run. Same pre-pin posture as keys 11/12/20/22/23/25.

> **Pre-pin caveat (PIPELINE-LEARNINGS keys 12/22/25):** a pre-pin PASS_WITH_FLAGS means "the right things
> are flagged," NOT "atoms byte-verified." Rule/step/flag/property **identity** is verified from each tool's
> own live docs; **version numbers, default phase bindings, default style values, and JDK matrices** are
> `⚠ verify at pin`. Atom byte re-trace MUST run after `/pin-source`.

## Scripts run (vs manual)

| Script | Result |
|---|---|
| `check_source_pin.sh` | FAIL by construction (clone absent, `{URL}`, multi-authority tag). Expected pre-pin. |
| `verify_sources.sh` | FAIL by construction (no clone to trace against). Cannot confirm atoms — deferred to `/pin-source`. |
| `lint_citations.sh` | 14 structural "violations" — ALL known false positives: bare-domain URLs (no `http://`) + `☐ live-line` status markers in the §8 source tables (same class as keys 19/20/22/23/25). No DOI/author-year residue. |
| `check_neutrality.sh` | **PASS — blocklist clean.** Advisory FLAGs only: 4 filler words, em-dash density 17/1000 (AUDIT-gate cosmetic, not a VERIFY concern). |
| `check_snippets.sh` | N/A — dossier (step 2), no `<!-- include -->` markers yet. |

## Checked claims / atoms

| # | Claim / atom | Location | Finding |
|---|---|---|---|
| 1 | Spotless GAVs/goals/tasks/steps (`spotless:check`/`apply`, `spotlessCheck`/`spotlessApply`, `<googleJavaFormat>`/`<palantirJavaFormat>`/`<eclipse>`/`<importOrder>`/`<removeUnusedImports>`/`<formatAnnotations>`/`<licenseHeader>`/`<ratchetFrom>`) | §0, §2.4, §2.7 | Identity verified from live repo docs; versions `⚠`. OK (cited to diffplug/spotless). |
| 2 | g-j-f GAV, CLI flags (`--replace`/`-r`/`-i`, `--dry-run`/`-n`, `--set-exit-if-changed`, `--aosp`, `--skip-*`, `--fix-imports-only`), `Formatter` API | §2.2, §2.7 | Identity verified from README; short-flag spellings flagged for re-confirm (§7). OK. |
| 3 | "no configurability … deliberate design decision to unify our code formatting on a single format" (verbatim, g-j-f) | §1, §2.2, §3 | Quoted as verbatim, marked verify-byte-identical-at-pin. Consistent spelling across §1/§2.2/§3 (no quote-drift — key-19 trap clear). OK. |
| 4 | palantir "modern, lambda-friendly, 120 character Java formatter" / "based on the excellent google-java-format" / chaining "limit of 80 chars for chained method calls …" (verbatim) | §1, §2.3, §3 | Quoted verbatim, consistent spelling across occurrences, re-confirm-at-pin marked (§7). OK. |
| 5 | Google Java Style numbers — 100-col, +2 indent, +4 continuation, no tabs, no wildcard imports (verbatim) | §1, §2.2, §2.7 | Cited verbatim to the style guide; flagged living/versionless → "cite as retrieved at pin." OK. |
| 6 | EditorConfig properties + precedence ("closer files take precedence"; `root=true` stops search) verbatim | §2.5, §2.7 | Cited verbatim to spec.editorconfig.org. OK. |
| 7 | `max_line_length` "not among core spec listed properties" | §2.5, §2.7, §4, §7 | Correctly NOT asserted as a spec guarantee; flagged + filed. OK. |
| 8 | Spotless `check` default Maven phase (`check` vs `verify`) | §2.4, §2.7, §7 | Two conflicting reads — asserted NEITHER, both recorded, filed. Correct flag discipline. OK. |
| 9 | g-j-f / palantir ↔ JDK version matrix + `--add-exports` | §4, §5, §7 | `⚠ verify at pin`; the "g-j-f 1.8 min for Java 11" note explicitly marked "illustrative, re-verify." No version asserted as fact. OK. |
| 10 | Comparative claims (100 vs 120 col; palantir derived from g-j-f; configurability spectrum) | §2.6, §4 | Each side cited to the named tool's own source; framed as approaches; no crowning. NEUTRALITY OK. |
| 11 | "FindBugs→SpotBugs-style death does not apply here" | §5 | FindBugs named only as historically-superseded analogy, NOT asserted as current. Folklore guard clear. OK. |
| 12 | Cross-tool verdict routing | §0, §2.6, §4, §7, Learnings | Consistently routed to key 37; choosing-verdict kept out of 34. OK. |

## Floors

- **NEUTRALITY (FLOOR A):** PASS. Blocklist clean (script + manual grep for `unlike/better than/superior/
  beats/the problem with/obvious choice`). "crown(ed)" hits are the dossier's own NON-crowning statements;
  "correct/right width" hits are the dossier explicitly forbidding crowning. No section title carries a
  superlative; per-tool subsections are survey-organized, not a leaderboard. Every cross-tool fact cited to
  the named tool. Style-value neutrality (100 vs 120) handled as cited choices, never "the right width."
- **HONEST-LIMITATIONS (FLOOR, §4):** PASS. Each of the four tools has its hardest objection + a
  when-NOT-to-use; plus a shared-limits centre ("format ≠ quality," one-time churn, tool-fighting).
- **Folklore:** clean — no defect-cost-curve / MI-as-score / coverage-as-quality; FindBugs correctly framed.

## Flag files (all present + accurate)

- `09-flags/34_spotless_default_phase_unverified.md` — matches §2.4/§7 (conflicting phase, asserted neither).
- `09-flags/34_editorconfig_not_pinned_and_maxlinelength.md` — matches §7 (SOURCE-PIN gap + max_line_length).
- `09-flags/34_formatter_jdk_version_matrix_unverified.md` — matches §4/§5/§7 (version↔JDK + --add-exports).

## Blockers

None.

## Required fixes (carry to draft; non-blocking)

1. **F1 (draft, atom re-trace):** after `/pin-source`, byte-verify every "verbatim" quote (g-j-f
   non-configurability, palantir 120-col + 80-char chaining, Google-style numbers, EditorConfig precedence)
   character-for-character at the pinned version, and replace `☐ live-line` with `☑` only then.
2. **F2 (SOURCE-PIN, material):** add the missing rows — EditorConfig spec (`spec.editorconfig.org`) to §2,
   and confirm Spotless / g-j-f / palantir version rows — before any version number is printed.
3. **F3 (catalog):** add the `34_formatters` row to `DEMO-CATALOG.md` (flagged in §6/§7).
4. **F4 (cosmetic, AUDIT-gate):** em-dash density (17/1000) and 4 filler words flagged by check_neutrality —
   trim at clarity/audit, not a VERIFY concern.

## Learnings & pipeline suggestions

- **"Living/versionless style guide" atom class.** The Google Java Style Guide is versionless (living doc),
  so its numbers (100/+2/+4) cannot be pinned to an edition the way a JLS/JEP can. The dossier's
  "cite as retrieved at pin + re-confirm the column number at draft" handling is the correct pattern —
  propose noting it in SOURCE-PIN for any living-doc authority (sibling of the JLS-§/JEP-Release atom rules).
- **Version-delta trap extends cleanly to tooling launch (reconfirms key 22).** The same `spotless:check`
  config can pass on one JDK and fail to launch on another (formatter↔JDK match + `--add-exports`). Good
  reuse of the key-22 behaviour-delta shape; carried + flagged.
- **lint_citations bare-domain/`☐` false-positive class recurs (keys 19/20/22/23/25).** 14 violations here
  are all `github.com/...` / `spec.editorconfig.org` bare domains (no `http://`) + `☐ live-line` status.
  Reinforces the standing proposal to teach `lint_citations.sh` to accept bare-domain source paths and
  `☐/live-line` status markers in pre-pin dossiers.
- **Pre-pin SOURCE-VERIFY = flag-discipline audit, not atom verification (keys 12/22/25 caveat holds).**
  PASS_WITH_FLAGS here = "flagged the right things." Re-run atom byte-trace after `/pin-source`.

**Return:** verdict = PASS_WITH_FLAGS, blockers = 0.
