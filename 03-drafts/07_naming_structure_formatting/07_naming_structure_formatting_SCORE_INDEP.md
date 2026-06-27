# INDEPENDENT SCORECARD — Ch 6 "Naming, structure & formatting" (key 07 + 17 + 34)

> **Independent (different-model) re-score** — deliberately harsh, skeptical-senior-Java-engineer lens.
> Bar = **≥44/50 (88%), no cluster < 6, floors A/B/C-source PASS**. ≥44 only if a senior engineer finds
> it excellent AND error-free. SCORE-ONLY — no draft edits, no lift loop applied.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 07 (owner; folds 17 + 34) — FINAL_INDEX Ch 6
- **Slug:** `07_naming_structure_formatting`
- **Title:** Naming, structure, formatting — and comments (the contested fourth)
- **Part / arc position:** Part II — Writing Quality Java, Chapter 6
- **Artifact scored:** `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build GREEN), `_CODEREVIEW.md` (PASS-WITH-FIXES). **No `_VERIFY.md`,
  `_CLARITY.md`, or `_AUDIT.md` exist** for this chapter — gates were run as "manual passes" (draft header
  line 9; prior self-score line 2). The independent VERIFY work below was therefore done in this pass.
- **Verified against SOURCE-PIN** — pinned 2026-06-20; tool rows re-pinned 2026-06-27. Re-check date: 2026-06-28.
- **Scorer:** chapter-scorer agent (independent / harsh)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (independent score; no lift applied)

---

## Independent fact-verification done in this pass (load-bearing atoms)

Checked against the openjdk JEP index (SOURCE-PIN §1: "JEP index — openjdk.org/jeps — per-feature, confirm
each JEP # at use") and against the SOURCE-PIN version rows.

| Claim in draft | Line | Pinned ground truth | Verdict |
|---|---|---|---|
| **"Java 21's unnamed variable `_`, JEP 456"** | 194 | openjdk JEP 456 "Unnamed Variables & Patterns" = **Closed/Delivered, Release 22**. The Java **21** form was **preview JEP 443** ("Unnamed Patterns and Variables (Preview)", Release 21). | **WRONG — drifted/AHEAD-OF-PIN** |
| `{@snippet}` = JEP 413, GA JDK 18 | 136 | JEP 413 "Code Snippets in Java API Documentation" = Release **18** | ✅ correct |
| `///` Markdown comments = JEP 467, JDK 23 (past anchor) | 138 | JEP 467 = Release **23**, Closed/Delivered; flagged AHEAD-OF-PIN (`09-flags/09_…`) | ✅ correct + correctly framed |
| Checkstyle `ConstantName` `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, `MethodName` `^[a-z][a-zA-Z0-9]*$` | 83–84, 226 | Confirmed verified in `09-flags/07_naming_defaults_unverified.md` "Verified" block | ✅ correct |
| Version literals: Checkstyle 13.6.0, PMD 7.25.0, spotless-maven-plugin 3.6.0, g-j-f 1.35.0, SonarQube 2026.1 LTA | 6, 226–230 | Match SOURCE-PIN §2 exactly (Spotless re-pinned 3.6.0 on 2026-06-27) | ✅ correct |
| All 5 snippet `<!-- include: … -->` tags resolve to ≤9-line regions in buildable module files | 92,120,124,165,183 | All 5 `tag::` regions exist in `08-companion-code/07_naming_structure_formatting/`; `_EXAMPLE.md` `check_snippets` = 5/5 pass | ✅ correct |

**The JEP-456 atom is the decisive finding.** It is a version-pinned never-invent atom (a JEP number bound to
a JDK level) asserted in running prose as **anchor fact** ("Java 21's unnamed variable"). It is wrong twice:
(a) JEP **456** is a **Java 22** feature, not Java 21; (b) at the Java-21 anchor the feature existed only as
**preview JEP 443**, which the SOURCE-PIN moving-target policy says must be marked as preview, not stated as a
language fact. This is precisely the JEP-467-class error the project flags elsewhere — and the project's own
flag `09-flags/07_naming_defaults_unverified.md` §5 raised this exact doubt ("JEP 456? … 21? / 22? UNVERIFIED")
and it was **never resolved** before this draft revision (draft still carries the inline `⚠ verify number/JDK
@pin` marker at line 194). The companion module is clean of any JEP residue — the error is confined to draft
prose, so it does not threaten COMPILE/CODE-REVIEW, but it is a direct **SOURCE-TRACE** breach.

**Carried-but-unresolved `⚠ verify @pin` markers (still live in the draft):** lines 64, 86, 144, 194, 196,
224–234. Of these, the named-book **verbatim quotes** are explicitly UNVERIFIED per the flag and cannot be
machine-confirmed here: Effective Java Item 68 ("straightforward and largely unambiguous" / "more complex and
looser", lines 64/224), Item 56 contract framing (132/224), Clean Code "a comment is an apology" (142) — the
flag warns this last may be a gloss, not a quotation (paraphrase-vs-quote risk). JLS §6.1 wording (64/225) and
the SonarSource default regexes (86/228) also remain unverified. These are *honestly* flagged, which is to the
draft's credit — but a senior reader cannot treat the chapter as "fully traced, zero drift" while load-bearing
quotations and one JEP fact are unconfirmed or wrong.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The single typography/meaning axis (Fig 6.1) is genuinely load-bearing and every layer sorts onto it cleanly. The three-layer table (lines 52–56), the format/lint split (CONCEPT, line 126), and the end-to-end worked readability pass (lines 149–187) make the division of labour concrete — a reader can reconstruct *why* formatting is decidable and naming-meaning is not. Clean, ordered, the "why" is explicit. Not a 9 only because the worked-pass displays two hand-written `java` fences (lines 155–160, 171–178) that are *illustrations*, not the tag-include regions beside them (165, 183), and they diverge from the real module (shows `MAX_RETRIES`; module constant is `MAX_QUANTITY_PER_LINE`) — a small seam between the page and the artifact. |
| 2 | **ACCURACY** | **5** | **The cap.** One outright factual error in a never-invent atom: "Java 21's unnamed variable `_`, JEP 456" (line 194) — JEP 456 is **Java 22**; the Java-21 form is preview JEP 443. Plus several load-bearing claims carried UNVERIFIED behind live `⚠ verify @pin` markers: three named-book verbatim quotes (EJ 68/56, Clean Code) the flag itself cannot confirm, JLS §6.1 wording, Sonar default regexes. The verified atoms (version literals, Checkstyle regexes, JEP 413/467, snippet tags) are solid and well-cited — but SCORING's ACCURACY band 1–3 names "facts from an unpinned version" as floor-tripping, and a wrong JEP→JDK mapping in running prose is exactly that. A skeptical senior engineer who knows the JEP history would catch line 194 immediately, which is fatal to an "error-free" claim. |
| 3 | **UTILITY** | **8** | Highly actionable: the exact division of labour (formatter→typography, linter→case, human→meaning), the adoption recipe (`ratchetFrom` vs a `.git-blame-ignore-revs` reformat commit, lines 187/215), the doclint middle path `all,-missing` (136/195), the per-surface comment decision frame (147/214). The "When to use what" section reads like a checklist a lead keeps open. A reader does real work from this. |
| 4 | **DEPTH** | **8** | Merges three dossiers (naming/structure/formatting + comments + formatters) into one coherent axis without re-teaching tool internals (correctly routed to Ch 16/17/18). Honest on the genuinely contested comments debate (two-schools table, lines 142–147), the constant-definition trap (§5.2.4 deep-immutability, line 76), the camel-case algorithm (94), formatter↔JDK coupling (196). Full mechanism + for + against + alternatives + when-to-use, nearly all sourced. Held back half a notch because one depth claim (the unnamed-variable interaction with short-name rules, 194) rests on the wrong fact. |
| 5 | **READABILITY** | **8** | Strong two-reviewer hook (lines 20–24) that earns the whole chapter; table-led, sparing CONCEPT/"Past the anchor" callouts, a clean worked before/after, a forward hook out ("A clear name on a method that swallows an exception is a clear name on a lie", 240). Voice holds — concrete, no hype, no banned phrasing. No grey-text wall. The live `⚠`-marker debris (e.g. "(Bloch, 2018 — ⚠ verbatim verify @pin)" mid-sentence, line 64) is editorial scaffolding that should not survive into a scored draft and slightly scuffs the read. |

**Cluster subtotal: 37 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | No winner crowned. Style values (2/4-space; 80/100/120 columns) stated as cited choices, none "correct" (lines 193, 112). google-java-format vs palantir vs Eclipse JDT = "three points on the same axis, not a winner" (114, 203). Comments presented as two schools, neither crowned, each with its hardest objection (142–147). Banned-phrase sweep clean: the one "more reliably than" (line 22) is a tool-vs-human capability statement, not a tool-vs-rival crowning — in-bounds. `_CODEREVIEW.md` neutrality scan of code/config/README also clean. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Every feature carries a when-NOT-to-use. The "Limitations & when NOT to reach for it" section (189–198) is thorough: tools check typography not meaning; member-order is judgment not a rule; style values are choices; over-strict regexes false-positive; vacuous forced Javadoc; formatter↔JDK coupling; `.editorconfig` is baseline-only; "when not to invest at all" (throwaway code). Plus a real adoption-cost paragraph (187, `git blame`/diff-noise). No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ❌ **FAIL** (on SOURCE-TRACE) | **COMPILE = PASS** (`_EXAMPLE.md`: `mvn -B -Pquality verify` → BUILD SUCCESS, 6 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, JDK 21.0.11). **CODE-REVIEW = PASS** (`_CODEREVIEW.md`: PASS-WITH-FIXES; no correctness/security/neutrality/invention defect in the module; open items are draft-label fixes). **SOURCE-TRACE = FAIL:** the version-pinned atom at draft line 194 — *"(Java 21's unnamed variable `_`, JEP 456 …)"* — is drifted/invented: JEP 456 is a **Java 22** final feature (openjdk JEP index, Closed/Delivered, Release 22), and at the Java-21 anchor it existed only as **preview JEP 443**. Asserted in running prose as anchor fact, guarded only by an unresolved `⚠ verify @pin` marker. **Fix at source:** either drop the parenthetical, or rewrite to "Java 21 previewed unnamed variables (`_`) under **JEP 443**; the feature was finalized in **Java 22** (JEP 456) — AHEAD-OF-PIN," and clear the inline marker. Per SCORING FLOOR C, "any invented or untraceable detail … is fatal regardless of the aggregate." |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **CUT / RETURN (LIFT after floor fix)** — A **FLOOR C (SOURCE-TRACE) FAIL** caps the verdict regardless of
  cluster scores. The aggregate (37/50) is also below the 44/50 ship bar, and ACCURACY at **5** is below the
  no-cluster-under-6 rule — so the chapter would not auto-approve even if the floor passed.

**One-line rationale:** A genuinely strong, well-shaped chapter undone by one wrong version-pinned fact
("Java 21's unnamed variable `_`, JEP 456" — it is Java 22 / JEP 456, preview JEP 443 at 21), which fails
FLOOR C SOURCE-TRACE *and* caps ACCURACY at 5; honestly-flagged-but-unresolved verbatim quotes keep ACCURACY
from recovering. Fix at source, clear the live `⚠` markers, then re-score.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score **5**
- **Why it is the weakest:** It carries the single fatal error (the JEP-456/Java-21 mistake) and a cluster of
  load-bearing claims still behind unresolved `⚠ verify @pin` markers (three named-book quotes, JLS §6.1, Sonar
  regexes). A code-quality book's whole authority rests on never drifting a version-pinned atom; this draft
  drifts one and defers several.
- **Single highest-leverage move to lift it:** Resolve `09-flags/07_naming_defaults_unverified.md` end to end —
  correct line 194 to JEP 443 (preview, JDK 21) / JEP 456 (final, JDK 22, AHEAD-OF-PIN), confirm or cut the
  three named-book verbatim quotes against the pinned editions, and clear every inline `⚠` marker the draft
  still carries. That single sweep fixes the floor and lifts ACCURACY from 5 toward 8.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | **FLOOR C / ACCURACY** | Limitations · bullet 4 · line 194 | "Java 21's unnamed variable `_`, JEP 456 — ⚠ verify number/JDK @pin" — JEP 456 is **Java 22**; Java-21 form is **preview JEP 443**. | Rewrite: "Java 21 *previewed* unnamed variables (`_`) under **JEP 443**; they were finalized in **Java 22** (JEP 456) — AHEAD-OF-PIN, so treat `_` as a Java-22 delta." Remove the inline `⚠` marker. Add a one-line AHEAD-OF-PIN flag for JEP 456 (mirror the JEP 467 flag). |
| 2 | ACCURACY | lines 64, 132, 142, 222, 224, 234 | Named-book verbatim quotes (EJ Item 68 / Item 56, Clean Code "a comment is an apology") carried UNVERIFIED; the flag warns the Clean Code span may be a gloss, not a quote. | Confirm each verbatim wording + page against the pinned editions (SOURCE-PIN §7), or convert to attributed paraphrase. Clear the `⚠ verbatim verify @pin` markers. |
| 3 | ACCURACY / READABILITY | lines 64, 86, 196, 224–234 | Live `⚠ verify @pin` scaffolding (JLS §6.1 wording, Sonar default regexes, formatter↔JDK matrix) left inline in a scored draft. | Resolve each at `/pin-source` (Sonar RSPEC pages, JLS SE 21/25 §6.1 text, formatter↔JDK matrix) and clear the markers, or downgrade the claim to what the pin supports. |
| 4 | CLARITY (minor) | Deep dive · lines 171–178 vs module | Hand-written `naming-good` fence shows `MAX_RETRIES` / `List<Invoice> invoices`; the real module constant is `MAX_QUANTITY_PER_LINE` (`int`), no collection field. | Align the illustrative fence with the actual module shape, or label it explicitly as a simplified sketch, so the page and the compiled artifact agree. (Echoes `_CODEREVIEW.md` F2/F3 back-matter label drift: "repo-root" → "module-root", "JUnit 5" → "JUnit 6".) |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 37 / 50 | PASS | PASS | **FAIL** (SOURCE-TRACE) / PASS (COMPILE) / PASS (CODE-REVIEW) | CUT/RETURN | independent harsh re-score; JEP-456/Java-21 error caught against the openjdk JEP index |

> For contrast, the main-loop self-score (`_SCORE.md`) recorded 40/50, ACCURACY 8, all floors A/B/C-source PASS.
> The divergence is entirely the JEP-456 atom (the self-score even *listed* "JEP numbers (456/467) … carried
> verify-at-pin" as a known −2 but still passed SOURCE-TRACE and scored ACCURACY 8). This is exactly why the
> bar requires an independent re-score: a self-score treated a deferred-and-wrong atom as acceptable.

---

## Learnings & pipeline suggestions

1. **A deferred `⚠ verify @pin` marker is not a trace — and SOURCE-TRACE must be checked against the actual
   authority, not against the presence of a marker.** The self-score saw the JEP-456 marker, noted it, and
   passed the floor anyway. An inline marker on a version-pinned atom asserted as fact should *block*
   SOURCE-TRACE until resolved, not soften it. **Propose:** add to the VERIFY/score checklist — "any
   `⚠ verify @pin` left on a never-invent atom (rule ID, JEP#, GAV, version, flag, API sig) is an automatic
   SOURCE-TRACE FAIL until cleared; markers are allowed only on prose framing, never on the atom itself."
2. **JEP number ↔ JDK level is a high-frequency drift atom; preview-vs-final is the trap.** JEP 443 (21,
   preview) vs JEP 456 (22, final) for unnamed variables is the same shape as JEP 467 (23) the project already
   flags. **Propose:** SOURCE-PIN §1 add a small "JEP→JDK + preview/final" table for every JEP the book cites
   (413→18 GA, 443→21 preview, 456→22 final, 467→23 final), so any draft asserting a JEP can be greped against
   it. The flag `07_naming_defaults_unverified.md` §5 already predicted this exact miss — a flag that names a
   risk should gate the draft that inherits it.
3. **This chapter never received independent VERIFY/CLARITY/AUDIT gate reports** (only EXAMPLE + CODEREVIEW
   exist; the rest were "manual passes"). The one fatal error is precisely the kind a VERIFY gate exists to
   catch. **Propose:** a chapter should not reach the scorecard without a `_VERIFY.md` on disk; "gates manual"
   is how a wrong atom reaches scoring with an ACCURACY-8 self-score.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
