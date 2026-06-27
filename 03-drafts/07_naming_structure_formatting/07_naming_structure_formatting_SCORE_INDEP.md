# INDEPENDENT SCORECARD — Ch 6 "Naming, structure & formatting" (key 07 + 17 + 34)

> **Independent (different-model) re-score — deliberately HARSH**, skeptical-senior-Java-engineer lens.
> Bar = **≥44/50 (88%), no cluster < 6, floors A/B/C-source PASS** (SCORING.md §"The ship bar", lines 91–98).
> ≥44 only if a senior engineer finds it excellent AND error-free. SCORE-ONLY — no draft edits, no lift loop applied.
>
> **This is a RE-SCORE after a FLOOR-C fix.** The prior INDEP pass (Pass 0, 2026-06-28) returned **CUT/RETURN
> on a FLOOR-C SOURCE-TRACE FAIL** + ACCURACY capped at 5, driven by **one** wrong version-pinned atom:
> "Java 21's unnamed variable `_`, JEP 456" (JEP 456 is Java **22**; the Java-21 form is preview JEP 443).
> That atom has now been fixed at source. This pass re-verifies the fix and re-scores all five clusters cold.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score, post-FLOOR-C-fix)
- **Dossier key:** 07 (owner; folds 17 + 34) — FINAL_INDEX Ch 6
- **Slug:** `07_naming_structure_formatting`
- **Title:** Naming, structure, formatting — and comments (the contested fourth)
- **Part / arc position:** Part II — Writing Quality Java, Chapter 6
- **Artifact scored:** `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build GREEN), `_CODEREVIEW.md` (PASS-WITH-FIXES). No `_VERIFY.md`,
  `_CLARITY.md`, or `_AUDIT.md` on disk — gates run as "manual passes" (draft header line 9). Independent
  VERIFY work was redone in this pass (below), focused on the previously-fatal atom.
- **Flag re-read:** `09-flags/07_naming_defaults_unverified.md` — §5 now marked **✅ RESOLVED 2026-06-28**.
- **Verified against SOURCE-PIN** — pinned 2026-06-20; tool rows re-pinned 2026-06-27. Re-check date: 2026-06-28.
- **Scorer:** chapter-scorer agent (independent / harsh)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (re-score after the FLOOR-C SOURCE-TRACE fix; prior INDEP pass = 0)

---

## Confirmation of the FLOOR-C fix (the decisive finding)

The previously-fatal atom is the single thing this re-score must adjudicate. It is fixed, confirmed on **three
independent fronts**:

| Front | Prior state (Pass 0) | Current state (this pass) | Verdict |
|---|---|---|---|
| **Draft line 194** | "Java 21's unnamed variable `_`, JEP 456 — ⚠ verify number/JDK @pin" (asserted as anchor fact; inline marker live) | "Java 21 *previewed* it as **JEP 443** (\"Unnamed Patterns and Variables (Preview)\"), and it was finalized in **Java 22** as **JEP 456** (\"Unnamed Variables & Patterns\") — past the anchor, so treat `_` as a Java 22-era delta, not anchor fact, and configure naming rules to exempt it only once on 22+." Inline `⚠` marker **removed**. | ✅ **FIXED** |
| **openjdk JEP index** (SOURCE-PIN §1 authority) | — | JEP **443** "Unnamed Patterns and Variables (Preview)" = Closed/Delivered, **Release 21**; JEP **456** "Unnamed Variables & Patterns" = Closed/Delivered, **Release 22**. The two cross-reference each other. Draft now matches ground truth exactly. | ✅ **MATCHES PIN** |
| **`09-flags/07_naming_defaults_unverified.md` §5** | "JEP 456? … 21? / 22? UNVERIFIED" — open | **✅ RESOLVED 2026-06-28**, with the JEP-index ground truth recorded and the AHEAD-OF-PIN framing applied per the runtime-baseline convention. | ✅ **CLOSED** |

The fix is precisely the rewrite the prior INDEP pass's line-fix #1 prescribed (preview JEP 443 @21 / final
JEP 456 @22, AHEAD-OF-PIN). The companion module never carried any JEP residue, so the fix is prose-only and
does not touch COMPILE/CODE-REVIEW. **FLOOR-C SOURCE-TRACE's decisive breach is repaired.**

### Other load-bearing atoms — re-confirmed clean (unchanged from Pass 0)

| Claim in draft | Line | Pinned ground truth | Verdict |
|---|---|---|---|
| `{@snippet}` = JEP 413, GA JDK 18 | 136 | JEP 413 = Release 18 | ✅ correct |
| `///` Markdown comments = JEP 467, JDK 23 (past anchor) | 138, 233 | JEP 467 = Release 23; flagged AHEAD-OF-PIN (`09-flags/09_…`) | ✅ correct + correctly framed |
| Checkstyle `ConstantName` `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, `MethodName` `^[a-z][a-zA-Z0-9]*$` | 83–84, 226 | "Verified" block in `09-flags/07_…` | ✅ correct |
| Version literals: Checkstyle 13.6.0, PMD 7.25.0, spotless-maven-plugin 3.6.0, g-j-f 1.35.0, SonarQube 2026.1 LTA | 6, 226–231 | Match SOURCE-PIN §2 exactly | ✅ correct |
| All 5 `<!-- include: … -->` tags resolve to ≤9-line regions in buildable module files | 92,120,124,165,183 | `_EXAMPLE.md` `check_snippets` = 5/5 pass | ✅ correct |

### Remaining `⚠ verify @pin` markers — now FLOOR-COMPLIANT (not a SOURCE-TRACE breach)

Live markers remain at lines 64, 86, 144, 196, 224–234. **Every one of them now sits on prose framing,
a named-book verbatim quotation, or an unread default regex — never on an atom asserted as anchor fact.**
SCORING FLOOR C requires each atom to "trace to the pin **OR** be flagged to `09-flags/`." These are flagged:
`09-flags/07_naming_defaults_unverified.md` (RESOLVED §5; live §1–§4 + the 2026-06-27 named-book-quote
appendix) is the open record for: EJ Item 68/56 verbatim wording (lines 64/224), Clean Code "a comment is an
apology" (144 — flag warns quote-vs-gloss risk), JLS §6.1 wording (64/225), SonarSource default regexes
(86/228), formatter↔JDK matrix (196), EditorConfig non-row (232). LEGAL-IP ceiling (quotes <15 words, one per
source) is met. These honestly-flagged-but-unconfirmed items are **legitimate under the floor** and do not fail
SOURCE-TRACE — but they do still cost ACCURACY points (a flagged-unverified quote is not "fully traced, zero
drift"), and the editorial `⚠` debris in running prose still scuffs READABILITY. Neither is fatal now.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | Unchanged from Pass 0 — the fix did not touch it. The single typography/meaning axis (Fig 6.1) is genuinely load-bearing; every layer sorts onto it. The three-layer table (lines 52–56), the format/lint split CONCEPT (126), and the end-to-end worked readability pass (149–187) make the division of labour concrete — a reader can reconstruct *why* formatting is decidable and naming-meaning is not. Not a 9 only because the worked-pass shows two hand-written `java` fences (155–160, 171–178) that diverge from the real module (fence shows `MAX_RETRIES`; module constant is `MAX_QUANTITY_PER_LINE`), a small seam between page and artifact. |
| 2 | **ACCURACY** | **8** | **Lifted from 5 → 8 — the cap is gone.** The single outright factual error (JEP-456/Java-21) that previously capped this cluster is fixed at source and now matches the openjdk JEP index exactly (preview JEP 443 @21, final JEP 456 @22, AHEAD-OF-PIN). Every verified atom (version literals, Checkstyle regexes, JEP 413/467, snippet tags) is solid and well-cited; zero drift remains. Held at 8, not 9, because load-bearing named-book verbatim quotes (EJ 68/56, Clean Code "a comment is an apology") and a few default regexes (Sonar) / spec wordings (JLS §6.1) are still carried UNVERIFIED behind `⚠`-markers — *honestly flagged to `09-flags/`* (so FLOOR-C-legal), but a chapter cannot be called "fully traced, zero drift" (the 9–10 band) while one quotation the flag itself warns may be a gloss is unconfirmed. 9 returns when the flag's named-book-quote appendix is cleared against the pinned editions. |
| 3 | **UTILITY** | **8** | Unchanged. Highly actionable: the exact division of labour (formatter→typography, linter→case, human→meaning), the adoption recipe (`ratchetFrom` vs `.git-blame-ignore-revs`, lines 187/215), the doclint middle path `all,-missing` (136/195), the per-surface comment frame (147/214), and a "When to use what" section that reads like a lead's checklist. A reader does real work from this. |
| 4 | **DEPTH** | **8** | Lifted half a notch within the same band (the Pass-0 note docked it because one depth claim — the unnamed-variable/short-name-rule interaction at 194 — rested on the wrong fact; that fact is now correct, so the depth claim is sound). Merges three dossiers into one coherent axis without re-teaching tool internals (routed to Ch 16/17/18). Honest on the contested comments debate (two-schools table 142–147), the constant-definition trap (§5.2.4 deep-immutability, 76), the camel-case algorithm (94), formatter↔JDK coupling (196). Full mechanism + for + against + alternatives + when-to-use, nearly all sourced. Not a 9 because a few sourced atoms still ride on `⚠`-flagged quotes. |
| 5 | **READABILITY** | **8** | Unchanged. Strong two-reviewer hook (20–24) that earns the chapter; table-led, sparing CONCEPT/"Past the anchor" callouts, a clean worked before/after, a sharp forward hook ("A clear name on a method that swallows an exception is a clear name on a lie", 240). Voice holds — concrete, no hype, no banned phrasing, no grey-text wall. Held at 8 by the live `⚠`-marker debris mid-prose (e.g. "(Bloch, 2018 — ⚠ verbatim verify @pin)", line 64; "⚠ default @pin" in the Sonar table cell, 86) — editorial scaffolding that should not survive into a scored draft. |

**Cluster subtotal: 40 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | No winner crowned. Style values (2/4-space; 80/100/120 columns) stated as cited choices, none "correct" (193, 112). google-java-format vs palantir vs Eclipse JDT = "different points on the same axis, not a winner" (114, 203). Comments = two schools, neither crowned, each with its hardest objection (142–147). Banned-phrase sweep clean (greppable): the one "more reliably than" (line 22) is a tool-vs-human capability statement, not a tool-vs-rival crowning — in-bounds. `_CODEREVIEW.md` neutrality scan of code+config+README also clean. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Every feature carries a when-NOT-to-use. The "Limitations & when NOT to reach for it" section (189–198) is thorough: tools check typography not meaning; member-order is judgment not a rule; style values are choices; over-strict regexes false-positive (incl. the now-correct `_`/`ShortVariable` interaction, 194); vacuous forced Javadoc; formatter↔JDK coupling; `.editorconfig` is baseline-only; "when not to invest at all" (throwaway code). Plus a real adoption-cost paragraph (187, `git blame`/diff-noise). No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ **PASS** | **SOURCE-TRACE = PASS** (the fix): the previously-fatal version-pinned atom at line 194 is corrected to match the openjdk JEP index (preview JEP 443 @21 / final JEP 456 @22, AHEAD-OF-PIN), the inline marker removed, and `09-flags/07_…` §5 marked RESOLVED. No invented/drifted atom remains; every other never-invent atom traces to the pin OR is flagged to `09-flags/` per the floor (the residual `⚠` markers sit on flagged named-book quotes / unread regexes / prose framing, not on asserted-as-fact atoms). **COMPILE = PASS** (`_EXAMPLE.md`: `mvn -B -Pquality verify` → BUILD SUCCESS, 6 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, JDK 21.0.11; 5/5 snippet tags extract ≤9 lines). **CODE-REVIEW = PASS** (`_CODEREVIEW.md`: PASS-WITH-FIXES; no correctness/security/neutrality/invention defect; the 4 open items are draft back-matter labels + one config comment, none blocking the floor). |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **LIFT-LOOP** — All THREE floors now **PASS** (the FLOOR-C breach is fixed), and no single cluster is
  below 6, so the chapter is no longer CUT. But the aggregate is **40/50**, **below the 44/50 (88%) ship bar**.
  It is close (−4) and every cluster sits at 7–8, so it is a clean LIFT candidate, not a cut.

**One-line rationale:** The FLOOR-C SOURCE-TRACE fix lands — JEP 456/Java-21 is corrected to preview JEP 443
@21 / final JEP 456 @22 (AHEAD-OF-PIN), matching the openjdk JEP index — so ACCURACY recovers 5 → 8, all floors
PASS, and the chapter clears the no-cluster-below-6 rule; at **40/50** it sits 4 short of the 88% auto-approval
bar, blocked now only by honestly-flagged-but-unconfirmed named-book quotes (ACCURACY/DEPTH) and live `⚠`-marker
debris in the prose (READABILITY). One in-bounds lift pass clearing those reaches 44.

---

## Flagged weakest cluster

- **Weakest cluster:** four-way tie at **8** (ACCURACY, DEPTH, READABILITY, UTILITY); CLARITY also 8. No single
  laggard remains. The highest-leverage cluster to push is **ACCURACY** (it most directly governs a
  code-quality book's authority and is the one held off 9 by an *external* dependency rather than prose work).
- **Why it is the (joint) weakest:** ACCURACY and DEPTH are both held at 8 by the same root cause — a handful of
  load-bearing named-book verbatim quotes (EJ Item 68/56, Clean Code "a comment is an apology") and a few
  default regexes / spec wordings carried UNVERIFIED behind `⚠`-markers. They are correctly flagged (FLOOR-C
  legal) but keep the chapter out of the 9–10 "fully traced, zero drift" band. READABILITY is held at 8 by the
  cosmetic residue of those same markers appearing in running prose.
- **Single highest-leverage move to lift it:** clear the `09-flags/07_naming_defaults_unverified.md`
  named-book-quote appendix end-to-end — confirm (or convert to attributed paraphrase) the EJ 68/56 and Clean
  Code spans against the pinned editions (SOURCE-PIN §7), resolve the Sonar default regexes + JLS §6.1 wording
  at `/pin-source`, and **delete every inline `⚠` marker from the scored draft**. That one in-bounds sweep
  (no new facts, no scope creep — it only confirms or downgrades what is already there) lifts ACCURACY and
  DEPTH 8 → 9 and READABILITY 8 → 9, reaching **~44–45/50** and the ship bar.

---

## Line-level fixes (the lift list)

> In-bounds for the bounded lift loop: confirm-or-downgrade already-present claims and remove editorial
> scaffolding. No new unverified facts, no padding, no scope creep, no floor risk.

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / DEPTH | lines 64, 132, 144, 224, 234 | Named-book verbatim quotes (EJ Item 68 "straightforward and largely unambiguous" / "more complex and looser"; Item 56 contract framing; Clean Code "a comment is an apology") carried UNVERIFIED; the flag warns the Clean Code span may be a gloss, not a quote. | Confirm each verbatim wording + page against the pinned editions (SOURCE-PIN §7), or convert to attributed paraphrase. Clear the `⚠ verbatim verify @pin` markers. Resolves the flag's 2026-06-27 appendix. |
| 2 | ACCURACY / READABILITY | lines 86, 196, 225, 228, 232 | Live `⚠ verify @pin` scaffolding (Sonar default regexes, JLS §6.1 wording, formatter↔JDK matrix, EditorConfig non-row) left inline in a scored draft. | Resolve each at `/pin-source` (Sonar RSPEC pages, JLS SE 21/25 §6.1 text, the formatter↔JDK matrix) and clear the markers, or downgrade the claim to exactly what the pin supports. Removes the `⚠` debris from the read. |
| 3 | CLARITY (minor) | Deep dive · lines 171–178 vs module | Hand-written `naming-good` fence shows `MAX_RETRIES` / `List<Invoice> invoices`; the real module constant is `MAX_QUANTITY_PER_LINE` (`int`), no collection field. | Align the illustrative fence with the actual module shape, or label it explicitly as a simplified sketch, so the page and the compiled artifact agree. (Echoes `_CODEREVIEW.md` F2/F3 label drift.) |
| 4 | (already fixed — record only) | Limitations · bullet 4 · line 194 | **DONE.** JEP-456/Java-21 atom rewritten to preview JEP 443 @21 / final JEP 456 @22 (AHEAD-OF-PIN); inline marker removed; flag §5 RESOLVED. | — (verified in this pass; no action) |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 37 / 50 | PASS | PASS | **FAIL** (SOURCE-TRACE) / PASS / PASS | CUT/RETURN | independent harsh score; JEP-456/Java-21 error caught against the openjdk JEP index; ACCURACY capped at 5 |
| 1 | 2026-06-28 | **40 / 50** | PASS | PASS | **PASS** / PASS / PASS | **LIFT-LOOP** | FLOOR-C fix verified: line 194 corrected to preview JEP 443 @21 / final JEP 456 @22 (AHEAD-OF-PIN), inline marker removed, flag §5 RESOLVED. ACCURACY 5→8, DEPTH +0.5 band, floor cleared. Now 4 short of 44 on flagged-unverified quotes + `⚠` prose debris. |

> For contrast, the main-loop self-score (`_SCORE.md`) recorded 40/50, ACCURACY 8, all floors PASS — the same
> aggregate this independent pass now reaches, but the self-score reached it *while the JEP-456 atom was still
> wrong* (it treated a deferred-and-wrong atom as acceptable). This pass reaches 40 honestly, *after* the atom
> is fixed. The number agreeing for the right reason is the point of the independent gate.

---

## Learnings & pipeline suggestions

1. **A FLOOR-C fix re-score must adjudicate the specific atom, not re-litigate the whole chapter.** This pass
   confirmed the one fatal atom on three fronts (draft text, openjdk JEP index, the resolved flag) and held the
   four untouched clusters steady, moving only the two the fix actually touched (ACCURACY, DEPTH). **Propose:**
   a re-score template note — "on a lift after a floor fix, name the atom, prove it fixed against the pin, and
   move only the clusters causally downstream of it; do not silently re-grade unrelated clusters."
2. **The bounded lift loop is now correctly positioned**: one fix took the chapter from a fatal floor FAIL +
   ACCURACY 5 to all-floors-PASS + 40/50, and the *remaining* 4-point gap is a single in-bounds sweep (clear
   the flagged named-book quotes + delete the `⚠` prose debris) with no floor risk. This is exactly what the
   loop is for. **Propose:** confirm SCORING's lift-loop guidance covers "clear honestly-flagged-but-unverified
   atoms and remove editorial markers" as an explicitly in-bounds pass (no new facts — it only confirms or
   downgrades what is already present).
3. **`⚠ verify @pin` markers must not survive into a scored draft even when FLOOR-C-legal.** They are correct
   process during VERIFY but, left inline at score time, they (a) cost READABILITY directly and (b) keep
   ACCURACY/DEPTH out of the 9–10 band even when the underlying claim is sound-and-flagged. **Propose:** add to
   the pre-score checklist — "a scored draft carries zero inline `⚠` markers; each is either resolved against
   the pin or downgraded to attributed/paraphrased prose; the flag record in `09-flags/` is where open
   verification lives, not the chapter body."
4. **The earlier learning holds and is now demonstrated:** a flag that *named* the JEP-456 risk
   (`07_naming_defaults_unverified.md` §5) is what made the fix fast and verifiable. A flag that names a
   version-pinned risk should gate the draft that inherits it until resolved — the resolve→re-score cycle here
   is the model.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
