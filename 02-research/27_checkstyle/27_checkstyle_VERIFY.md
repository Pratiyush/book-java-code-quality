# SOURCE-VERIFY — key 27 (Checkstyle), step-2 gate

**Gate:** SOURCE-VERIFY · **Date:** 2026-06-15 · **Artifact:** `02-research/27_checkstyle/27_checkstyle_RESEARCH.md`
**Verdict:** **PASS_WITH_FLAGS** · **Blockers:** 0

## Pin / script status (pre-pin, same construction as keys 07/10/11/12/13/15/19/20/22/23/25)
- `check_source_pin.sh` → **FAIL by construction**: SOURCE-PIN is multi-authority, every tool/JDK/spec row `TO-PIN`,
  clone path absent, fetch URL is `{URL}` (`/pin-source` never run). Unhealable. No pinned text fetchable.
- `verify_sources.sh` → cannot trace (usage error / no clone).
- `lint_citations.sh` → 16 violations, **all known false positives**: bare-domain URLs (no `http://`) + the two
  `☐ verify at pin` / `☐ corroboration` further-reading rows. Not content defects.
- `check_snippets.sh` → N/A at research stage (no draft, no `<!-- include -->` markers).
- **Consequence:** this is a flag-discipline audit, NOT atom byte-verification. A PASS_WITH_FLAGS here means
  "flagged the right things"; atom re-trace MUST happen after `/pin-source` (per key-12 lesson).

## Checked claims
| # | Claim / atom | Check | Result |
|---|---|---|---|
| 1 | Checkstyle version 13.6.0 (2026-06-15) | version atom | OK — `⚠ verify at pin`, never asserted; flagged |
| 2 | maven-checkstyle-plugin 3.6.0 bundles Checkstyle **9.3** by default | two-pin gotcha | OK — `⚠ verify at pin`; flagged (see F2) |
| 3 | `LineLength max=80`, `ignorePattern=^(package\|import) .*` | default property | OK — live-line, `⚠ verify at pin`, flagged for byte re-trace |
| 4 | `ConstantName format=^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` | default property | OK — live-line, `⚠ verify at pin` |
| 5 | 14 check categories (Annotations…Whitespace) | identity | OK — identity-only, citeable now |
| 6 | `Checker`/`TreeWalker`/`Check`, severity error/warning/info/ignore | structure | OK — structural, citeable |
| 7 | Single-file LIMITATIONS quote ("one file only … cannot determine the type … inheritance hierarchy") | verbatim quote, ×2 uses | OK — consistent wording both uses; re-verify byte-identical at pin |
| 8 | Purpose quote ("spare humans of this boring (but important) task") | verbatim quote | OK — single consistent spelling, no drift |
| 9 | JEP 395/440/441 records & pattern-matching GA@21 | JEP Release fields | OK — matches key-13 verified list; no preview asserted |
| 10 | FindBugs→SpotBugs | folklore guard | OK — explicitly stated NOT to apply (Checkstyle own lineage); avoids FindBugs-as-current |
| 11 | Cross-tool mentions (PMD/SpotBugs/Error Prone/Sonar/formatters) | NEUTRALITY | OK — each routed to its own key; verdict routed to key 37; no crowning |
| 12 | Banned-phrase scan | NEUTRALITY blocklist | CLEAN — "crown/crowned" hits are anti-crowning; "unlike openjdk JEPs" = fetch-tool contrast, not a rival |
| 13 | HONEST-LIMITATIONS floor | §4 | MET — hardest objection (single-file/no-type) + style≠correctness + legacy noise + config burden + explicit when-NOT-to-use list |
| 14 | Thresholds (CyclomaticComplexity/NPath/MagicNumber) | key-19 threshold guard | OK — never printed as "the" limit; `⚠ verify at pin` |
| 15 | Flag file `09-flags/27_..._unverified.md` | exists + matches dossier | OK — present, accurate |

## Findings (non-blocking, draft-fix)
- **F1 (overclaim trap, recurs keys 19/22/25):** §5 / §2.6 / §7 annotate "bundles Checkstyle 9.3" and the
  `3.6.0`/`9.3` pairs as **"(verbatim, plugin docs)"** though the source is the LIVE plugin page, not a pin.
  Use "live-line, verify at pin" — reserve "verbatim/confirmed" for post-`/pin-source`. (Already `⚠ verify at
  pin`-marked elsewhere, so non-blocking.)
- **F2 (precision):** scan-log row 11 says plugin 3.6.0 "requires Java 8" while §1/§5 say the modern Checkstyle
  line "requires modern Java to run." These are plugin-runtime vs engine-runtime — distinguish explicitly at draft.
- **F3 (naming-regex completeness):** §2.3/§2.6 name `MemberName`/`MethodName`/`LocalVariableName`/`ParameterName`/
  `TypeName`/`PackageName` but their `format` regexes are NOT captured (correctly not printed). §7 already queues
  this — confirm none leaks as an asserted regex at draft.
- **F4 (catalog gap, carried):** `DEMO-CATALOG.md` has no `27_checkstyle` row — §7 flags it; route to catalog owner.
- **F5 (lint false positives):** the 16 lint_citations violations are bare-domain + `☐`-status rows; a future
  lint refinement should whitelist these (recurs every key).

## Verdict
**PASS_WITH_FLAGS, 0 blockers.** No invented or unflagged fact; no folklore-as-fact (FindBugs handled correctly,
thresholds guarded); no off-pin/SNAPSHOT citation (every version/default/GAV is `⚠ verify at pin` and flagged);
NEUTRALITY blocklist clean with cross-tool verdict routed to key 37 and each cross-tool fact attributed to its
own source; synthesized claims (single-file boundary ⇒ style-not-bugs reach) trace to the docs' own LIMITATIONS
text; HONEST-LIMITATIONS floor met. Same pre-pin caveat as prior keys: atoms are *flagged*, not *verified* —
re-trace every Checkstyle version/default/GAV after `/pin-source`. F1–F5 carried to draft, none blocking.
