# INDEPENDENT SCORECARD — Ch 6 "Naming, structure & formatting" (key 07 + 17 + 34)

> **Independent (different-model) re-score — deliberately HARSH**, skeptical-senior-Java-engineer lens.
> Bar = **≥44/50 (88%), no cluster < 6, floors A/B/C-source PASS** (SCORING.md §"The ship bar", lines 91–98).
> ≥44 only if a senior engineer finds it excellent AND error-free. SCORE-ONLY — no draft edits, no lift loop applied.
>
> **This is a RE-SCORE after the EFFECTIVE-JAVA-QUOTE fix (Pass 2).** Chain of priors:
> - **Pass 0** (2026-06-28): CUT/RETURN on a FLOOR-C SOURCE-TRACE FAIL (the "JEP 456 = Java 21" error) + ACCURACY capped at 5.
> - **Pass 1** (2026-06-28): FLOOR-C fix verified (preview JEP 443 @21 / final JEP 456 @22, AHEAD-OF-PIN). All floors PASS, 40/50, LIFT — held off the bar by *named-book verbatim quotes (EJ Item 68/56 + Clean Code) carried UNVERIFIED behind `⚠` markers* and live marker debris in the prose.
> - **Pass 2 (this pass):** the two **Effective Java** verbatim spans (Item 68 "straightforward and largely unambiguous" / "more complex and looser") are now **faithful attributed paraphrases** (no quotation marks; Item#+titles web-confirmed against the published 3e TOC). **No presented-verbatim-but-unverifiable EJ atom remains.** This pass re-verifies that fix against the draft text and re-scores all five clusters cold. The one **out-of-scope Clean Code verbatim** ("a comment is an apology") is still flagged.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score, post-EJ-quote-paraphrase)
- **Dossier key:** 07 (owner; folds 17 + 34) — FINAL_INDEX Ch 6
- **Slug:** `07_naming_structure_formatting`
- **Title:** Naming, structure, formatting — and comments (the contested fourth)
- **Part / arc position:** Part II — Writing Quality Java, Chapter 6
- **Artifact scored:** `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build GREEN), `_CODEREVIEW.md` (PASS-WITH-FIXES). No `_VERIFY.md`,
  `_CLARITY.md`, or `_AUDIT.md` on disk — gates run as "manual passes" (draft header line 9). Independent
  VERIFY work was redone in this pass (below), focused on the EJ/Clean-Code verbatim atoms.
- **Flag re-read:** `09-flags/07_naming_defaults_unverified.md` — §5 RESOLVED (JEP), appendix atoms 1–2 (both
  EJ items) **cleared to N/A (PARAPHRASED) 2026-06-28**, atoms 3–4 (Clean Code verbatim + APoSD canon row)
  **STILL FLAGGED**. Status line: **PARTIALLY RESOLVED 2026-06-28**.
- **Verified against SOURCE-PIN** — pinned 2026-06-20; tool rows re-pinned 2026-06-27. Re-check date: 2026-06-28.
- **Scorer:** chapter-scorer agent (independent / harsh)
- **Date:** 2026-06-28
- **Lift-pass #:** 2 (re-score after the EJ-quote paraphrase; Pass 1 = FLOOR-C fix; Pass 0 = original harsh)

---

## Confirmation of the EJ-quote fix (the decisive finding for this pass)

The single thing this re-score must adjudicate is whether the two presented-verbatim-but-unverifiable
*Effective Java* quotes are gone. Confirmed on **three independent fronts**:

| Front | Prior state (Pass 1) | Current state (this pass) | Verdict |
|---|---|---|---|
| **Draft line 64 / 96 / 224 (Item 68)** | Carried verbatim spans "straightforward and largely unambiguous" / "more complex and looser" in quotation marks, `⚠ verbatim verify @pin`, `_ref/` empty → unconfirmable | Rewritten as a faithful attributed paraphrase in our own words: typographical conventions (case) are "tight and rarely in dispute" vs grammatical conventions (name shape) "softer and admit more judgement" — **no quotation marks on body prose**; the only quoted string is the **Item title** ("Adhere to generally accepted naming conventions"), which is web-confirmable. Back matter (224) states "paraphrased, not quoted … no verbatim asserted." | ✅ **FIXED — no verbatim EJ-68 atom** |
| **Draft line 134 / 224 (Item 56)** | Doc-comments-as-contract; only the Item title was ever quoted (already a paraphrase) | Unchanged in substance; only quoted string is the **Item title** ("Write doc comments for all exposed API elements"), web-confirmable; body is paraphrase. | ✅ **No verbatim EJ-56 atom** |
| **`09-flags/07_naming_defaults_unverified.md` appendix §§1–2** | Both EJ items open ("verbatim wording + page still needs the book text") | **✅ N/A — RESOLVED 2026-06-28 (PARAPHRASED)** for both; Item#+titles web-confirmed against the published Effective Java 3rd-ed TOC; "no verbatim presented anywhere, so there is nothing left to verbatim-verify." | ✅ **CLOSED for EJ** |

**The larger of the two pillars that capped ACCURACY/DEPTH at 8 in Pass 1 — the EJ verbatim quotes — is
removed.** The fix is prose-only (the companion module never carried EJ residue), so COMPILE/CODE-REVIEW are
untouched. This is the same paraphrase *pattern* the one remaining Clean Code quote still needs.

### The one remaining named-book verbatim — Clean Code (out of scope of this fix, STILL FLAGGED)

| Claim in draft | Line | State | Verdict |
|---|---|---|---|
| Clean Code (Martin, 2008): "a comment is an apology" — presented in quotation marks, `(⚠ verbatim verify @pin)` | 144, 234 | **STILL a presented verbatim quote behind a live `⚠` marker.** Flag appendix atom 3 warns it may be a *gloss, not a quotation*. FLOOR-C-legal (flagged to `09-flags/`), within the LEGAL-IP prose-quote ceiling — but it is exactly one unconfirmed named-book verbatim atom. | ⚠ **FLAGGED — single remaining verbatim** |

### Other load-bearing atoms — re-confirmed clean (unchanged from Pass 1)

| Claim in draft | Line | Pinned ground truth | Verdict |
|---|---|---|---|
| Unnamed `_`: preview JEP 443 @21 / final JEP 456 @22 (AHEAD-OF-PIN) | 194 | openjdk JEP index: JEP 443 = Release 21, JEP 456 = Release 22 | ✅ correct (Pass-1 fix holds) |
| `{@snippet}` = JEP 413, GA JDK 18 | 136 | JEP 413 = Release 18 | ✅ correct |
| `///` Markdown comments = JEP 467, JDK 23 (past anchor) | 138, 233 | JEP 467 = Release 23; flagged AHEAD-OF-PIN | ✅ correct + correctly framed |
| Checkstyle `ConstantName` `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, `MethodName` `^[a-z][a-zA-Z0-9]*$` | 83–84, 226 | "Verified" block in `09-flags/07_…` | ✅ correct |
| Version literals: Checkstyle 13.6.0, PMD 7.25.0, spotless-maven-plugin 3.6.0, g-j-f 1.35.0, SonarQube 2026.1 LTA | 6, 226–231 | Match SOURCE-PIN §2 | ✅ correct |
| All 5 `<!-- include: … -->` tags resolve to ≤9-line regions in buildable files | 92,120,124,165,183 | `_EXAMPLE.md` `check_snippets` = 5/5 pass | ✅ correct |

### Remaining `⚠ verify @pin` markers — FLOOR-COMPLIANT (not a SOURCE-TRACE breach), but still a quality cost

Live markers remain at lines **64** (JLS §6.1 wording), **86** (Sonar default regex, table cell), **144**
(Clean Code verbatim), **196** (formatter↔JDK matrix), and back-matter **225/226/228/232/234**, plus header
7–8. **Every one sits on prose framing, the one flagged named-book quote, or an unread default regex — never on
an atom asserted as anchor fact.** Each traces to the pin OR is flagged to
`09-flags/07_naming_defaults_unverified.md` (live atoms 3–4 + the JLS/Sonar/EditorConfig rows). LEGAL-IP
ceiling met. **Legitimate under FLOOR C → SOURCE-TRACE PASS.** But a flagged-unverified *verbatim quotation*
is not "fully traced, zero drift," and inline `⚠` debris in running prose scuffs READABILITY — so they still
cost cluster points (below). Neither is fatal.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | Unchanged — the EJ fix did not touch it. The single typography/meaning axis (Fig 6.1) is genuinely load-bearing; every layer sorts onto it. The three-layer table (52–56), the format/lint split CONCEPT (126), and the end-to-end worked readability pass (149–187) make the division of labour concrete — a reader can reconstruct *why* formatting is decidable and naming-meaning is not. Held off 9 only by the page↔module seam: the hand-written `naming-good` fence (171–178) shows `MAX_RETRIES` / `List<Invoice> invoices`; the real module constant is `MAX_QUANTITY_PER_LINE` (an `int`, no collection field) — a small divergence between page and compiled artifact (echoes `_CODEREVIEW.md` F2/F3 label drift). |
| 2 | **ACCURACY** | **8** | **Materially stronger than Pass 1's 8, but still 8 under a harsh lens.** The *larger* pillar that held this off 9 — the two presented-verbatim-but-unverifiable *Effective Java* quotes (Item 68/56) — is **gone**: both are now faithful attributed paraphrases (no quotation marks), Item#+titles web-confirmed against the published 3e TOC, flag appendix §§1–2 cleared to N/A. Every version-pinned atom is solid (JEP 443/456 fix holds, version literals, Checkstyle regexes, JEP 413/467, snippet tags); zero drift. Cannot reach 9 ("fully traced, zero drift") because the *smaller* pillar remains: **one** named-book verbatim still presented in quotation marks and flag-warned as possibly-a-gloss (Clean Code "a comment is an apology", 144), plus live `⚠ verify @pin` markers on unread default regexes (Sonar, 86/228), JLS §6.1 wording (64/225), and the formatter↔JDK matrix (196). All FLOOR-C-legal (flagged) — but a flagged-unverified verbatim + inline "verify @pin" debris is, by the rubric's own 9–10 anchor, not the top band. 9 returns when the Clean Code span is confirmed-or-paraphrased (the EJ pattern, now applied once) and the remaining regex/wording markers are resolved at `/pin-source`. |
| 3 | **UTILITY** | **8** | Unchanged. Highly actionable: the exact division of labour (formatter→typography, linter→case, human→meaning), the adoption recipe (`ratchetFrom` vs `.git-blame-ignore-revs`, 187/215), the doclint middle path `all,-missing` (136/195), the per-surface comment frame (147/214), and a "When to use what" section that reads like a lead's checklist. A reader does real work from this. |
| 4 | **DEPTH** | **8** | Unchanged band. The EJ paraphrase makes the Item-68 typographical-vs-grammatical depth claim *sounder* (it now rests on a web-confirmed framing, not an unverified quote), a genuine improvement. Merges three dossiers into one coherent axis without re-teaching tool internals (routed to Ch 16/17/18). Honest on the contested comments debate (two-schools table 142–147), the §5.2.4 deep-immutability constant trap (76), the camel-case algorithm (94), formatter↔JDK coupling (196). Full mechanism + for + against + alternatives + when-to-use, nearly all sourced. Held off 9 because a couple of sourced atoms still ride on the one `⚠`-flagged Clean Code quote and the unread regexes. |
| 5 | **READABILITY** | **8** | Unchanged. Strong two-reviewer hook (20–24) that earns the chapter; table-led, sparing CONCEPT/"Past the anchor" callouts, a clean worked before/after, a sharp forward hook ("A clear name on a method that swallows an exception is a clear name on a lie", 240). Voice holds — concrete, no hype, no banned phrasing, no grey-text wall. Held at 8 by the live `⚠`-marker debris mid-prose: "(⚠ confirm exact SE 21/25 wording @pin)" inside a sentence (64), "⚠ default @pin" in a Sonar table cell (86), "(⚠ verbatim verify @pin)" in the comments table (144), "(⚠ … verify @pin)" in a limitation bullet (196) — editorial scaffolding that should not survive into a scored draft. (The EJ fix removed two prior `⚠` markers at 64/96/224; the remaining ones are the next sweep.) |

**Cluster subtotal: 40 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | No winner crowned. Style values (2/4-space; 80/100/120 columns) stated as cited choices, none "correct" (193, 112). google-java-format vs palantir vs Eclipse JDT = "different points on the same axis, not a winner" (114, 203). Comments = two schools, neither crowned, each with its hardest objection (142–147). Banned-phrase sweep clean (greppable): the one "more reliably than" (line 22) is a tool-vs-human capability statement, not a tool-vs-rival crowning — in-bounds. `_CODEREVIEW.md` neutrality scan of code+config+README also clean. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Every feature carries a when-NOT-to-use. The "Limitations & when NOT to reach for it" section (189–198) is thorough: tools check typography not meaning; member-order is judgment not a rule; style values are choices; over-strict regexes false-positive (incl. the correct `_`/`ShortVariable` interaction, 194); vacuous forced Javadoc; formatter↔JDK coupling; `.editorconfig` is baseline-only; "when not to invest at all" (throwaway code). Plus a real adoption-cost paragraph (187, `git blame`/diff-noise). No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ **PASS** | **SOURCE-TRACE = PASS.** No invented/drifted atom. The Pass-1 JEP fix holds (preview JEP 443 @21 / final JEP 456 @22). This pass's EJ fix removes the two unverifiable verbatim EJ quotes (now paraphrases, Item#+titles web-confirmed). Every other never-invent atom traces to the pin OR is flagged to `09-flags/` per the floor — the residual `⚠` markers sit on the **one** flagged Clean Code verbatim, unread default regexes, and prose framing, **never on an asserted-as-fact atom**. **COMPILE = PASS** (`_EXAMPLE.md`: `mvn -B -Pquality verify` → BUILD SUCCESS, 6 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, JDK 21.0.11; 5/5 snippet tags extract ≤9 lines). **CODE-REVIEW = PASS** (`_CODEREVIEW.md`: PASS-WITH-FIXES; no correctness/security/neutrality/invention defect; the 4 open items are draft back-matter labels + one config comment, none blocking the floor). |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **LIFT-LOOP** — All THREE floors **PASS**, no single cluster below 6 (all at 8), so the chapter is not
  CUT. The aggregate is **40/50**, still **4 short of the 44/50 (88%) ship bar**. Every cluster sits at 8 and
  the remaining gap is a single in-bounds sweep, so it is a clean LIFT candidate, not a cut.

**One-line rationale:** The EJ-quote fix lands — both *Effective Java* verbatim spans are now faithful
attributed paraphrases (Item#+titles web-confirmed), so the **larger** of the two pillars capping ACCURACY/DEPTH
is gone; but the score holds at **40/50** because the **smaller** pillar survives untouched: the one out-of-scope
Clean Code verbatim ("a comment is an apology", flag-warned as possibly-a-gloss) plus live `⚠ verify @pin` marker
debris on unread regexes / JLS wording / the formatter↔JDK matrix keep ACCURACY, DEPTH, and READABILITY each at
8 rather than 9. No cluster crossed a band boundary, so the aggregate did not move. One more in-bounds sweep
clearing those reaches ~44–45.

---

## Flagged weakest cluster

- **Weakest cluster:** five-way tie at **8** (CLARITY, ACCURACY, UTILITY, DEPTH, READABILITY). No single
  laggard. The highest-leverage cluster to push is **ACCURACY** — it most directly governs a code-quality
  book's authority and is held off 9 by the smallest, most-mechanical residue (one verbatim quote + a handful
  of inline markers), which also drags DEPTH and READABILITY.
- **Why it is the (joint) weakest:** ACCURACY, DEPTH, and READABILITY share one root cause now that the EJ
  quotes are cleared — **(a)** the single remaining named-book verbatim (Clean Code "a comment is an apology",
  144/234), still in quotation marks and flag-warned as possibly-a-gloss; and **(b)** the live `⚠ verify @pin`
  markers on unread Sonar default regexes (86/228), JLS §6.1 wording (64/225), the formatter↔JDK matrix (196),
  and the EditorConfig non-row (232). They are FLOOR-C-legal (flagged) but keep the chapter out of the 9–10
  "fully traced, zero drift" band; the marker debris additionally scuffs READABILITY in running prose.
- **Single highest-leverage move to lift it:** run the same sweep that the EJ paraphrase just modelled —
  **(1)** confirm the Clean Code span verbatim+page against the pinned 2008 edition (SOURCE-PIN §7) **or**
  convert it to an attributed paraphrase exactly as Item 68/56 were just handled, and **(2)** resolve the
  remaining default regexes (Sonar RSPEC) + JLS §6.1 wording + formatter↔JDK matrix at `/pin-source`, then
  **delete every inline `⚠` marker from the scored draft**. That one in-bounds sweep (no new facts, no scope
  creep — it only confirms or downgrades what is already there) lifts ACCURACY and DEPTH 8 → 9 and
  READABILITY 8 → 9, reaching **~44–45/50** and the ship bar.

---

## The exact remaining gap (−4 to the bar)

> The EJ fix removed the larger cause but moved no cluster across a band boundary, because three
> FLOOR-C-legal-but-quality-costing residues remain. These are precisely the Pass-1 lift list **minus** the
> now-cleared EJ quotes.

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / DEPTH / READABILITY | lines 144, 234 | **The one out-of-scope Clean Code verbatim** — "a comment is an apology" still in quotation marks behind `⚠ verbatim verify @pin`; flag atom 3 warns it may be a gloss, not a quotation. The single remaining presented-verbatim-but-unverifiable named-book atom. | Confirm verbatim wording + page against the pinned Clean Code 2008 edition (SOURCE-PIN §7), **or** convert to an attributed paraphrase exactly as EJ Item 68/56 were just handled. Clear the `⚠` marker. Resolves flag appendix atom 3. |
| 2 | ACCURACY / READABILITY | lines 64, 86, 196, 225, 226, 228, 232 (+ header 7–8) | Live `⚠ verify @pin` scaffolding left inline in a scored draft: Sonar default regexes (86/228), JLS §6.1 wording (64/225), formatter↔JDK matrix (196), Checkstyle `LineLength` default (226), EditorConfig non-row (232). | Resolve each at `/pin-source` (Sonar RSPEC pages, JLS SE 21/25 §6.1 text, the formatter↔JDK matrix) and clear the markers, or downgrade each claim to exactly what the pin supports. Removes the `⚠` debris from the read. |
| 3 | CLARITY (minor) | Deep dive · lines 171–178 vs module | Hand-written `naming-good` fence shows `MAX_RETRIES` / `List<Invoice> invoices`; the real module constant is `MAX_QUANTITY_PER_LINE` (`int`), no collection field. | Align the illustrative fence with the actual module shape, or label it explicitly as a simplified sketch, so page and compiled artifact agree. (Echoes `_CODEREVIEW.md` F2/F3 label drift.) |
| — | (already fixed — record only) | line 64/96/224 (EJ Item 68); line 134/224 (EJ Item 56) | **DONE this pass.** Both *Effective Java* verbatim spans converted to faithful attributed paraphrases; Item#+titles web-confirmed; flag appendix §§1–2 cleared to N/A. No presented-verbatim-but-unverifiable EJ atom remains. | — (verified in this pass; no action) |
| — | (already fixed — record only) | Limitations · bullet 4 · line 194 | **DONE Pass 1.** JEP-456/Java-21 atom rewritten to preview JEP 443 @21 / final JEP 456 @22 (AHEAD-OF-PIN); inline marker removed; flag §5 RESOLVED. | — (verified; no action) |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 37 / 50 | PASS | PASS | **FAIL** (SOURCE-TRACE) / PASS / PASS | CUT/RETURN | independent harsh score; JEP-456/Java-21 error caught against the openjdk JEP index; ACCURACY capped at 5 |
| 1 | 2026-06-28 | 40 / 50 | PASS | PASS | **PASS** / PASS / PASS | LIFT-LOOP | FLOOR-C fix verified: line 194 → preview JEP 443 @21 / final JEP 456 @22 (AHEAD-OF-PIN), inline marker removed, flag §5 RESOLVED. ACCURACY 5→8, floor cleared. 4 short on flagged-unverified named-book quotes (EJ 68/56 + Clean Code) + `⚠` prose debris. |
| 2 | 2026-06-28 | **40 / 50** | PASS | PASS | **PASS** / PASS / PASS | **LIFT-LOOP** | EJ-quote fix verified: both *Effective Java* verbatim spans (Item 68/56) now faithful attributed paraphrases (no quotation marks), Item#+titles web-confirmed, flag appendix §§1–2 → N/A. No presented-verbatim-but-unverifiable EJ atom remains. The *larger* ACCURACY/DEPTH cap pillar is gone — but no cluster crossed a band boundary (still 8/8/8/8/8), so aggregate holds at 40. Remaining gap: the **one** out-of-scope Clean Code verbatim + live `⚠ verify @pin` marker debris (Sonar regexes, JLS §6.1, formatter↔JDK matrix). |

> Note on why the number is unchanged at 40 despite a real fix: the EJ quotes were one of *two* pillars holding
> ACCURACY/DEPTH/READABILITY at 8. Removing one pillar strengthens the 8 (it is now a high 8) but does not by
> itself cross the 8→9 boundary while the second pillar — one flagged Clean Code verbatim + inline `⚠` markers —
> is still standing. The honest read is "same aggregate, narrower remaining gap," not "no progress." One more
> in-bounds sweep clears the second pillar and the three affected clusters move to 9 together.

---

## Learnings & pipeline suggestions

1. **A targeted fix can be real and verified yet not move the aggregate, when two independent residues hold the
   same clusters at the same band.** This pass confirmed the EJ-quote fix on three fronts (draft text, web-confirmed
   TOC, the resolved flag appendix) and the clusters are genuinely sounder — but ACCURACY/DEPTH/READABILITY stay at
   8 because a *second* residue (the Clean Code verbatim + `⚠` markers) shares the same band ceiling. **Propose:** a
   re-score template note — "when a cluster is held off the next band by *multiple* residues, name each one and
   state that the score will not move until the *last* of them clears; do not over-credit a partial fix with a band
   bump it did not earn."
2. **A faithful attributed paraphrase is the sanctioned escape from an unverifiable verbatim book quote.** The EJ
   Item 68/56 fix — drop the quotation marks, restate the documented position in our own words, web-confirm the
   Item#+title, record it in the flag as N/A-paraphrased — is a clean, repeatable pattern that satisfies FLOOR-C and
   lifts ACCURACY without inventing anything. The one remaining Clean Code verbatim should take the same path.
   **Propose:** promote this to LEGAL-IP / the source-verify checklist as the default disposition for any named-book
   quote that cannot be verbatim-confirmed against a pinned edition (confirm-or-paraphrase, never leave a presented
   verbatim behind a live `⚠`).
3. **`⚠ verify @pin` markers must not survive into a scored draft even when FLOOR-C-legal.** Correct during VERIFY,
   but left inline at score time they (a) cost READABILITY directly and (b) keep ACCURACY/DEPTH out of the 9–10 band
   even when the underlying claim is sound-and-flagged. The flag record in `09-flags/` is where open verification
   lives, not the chapter body. **Propose:** add to the pre-score checklist — "a scored draft carries zero inline
   `⚠` markers; each is resolved against the pin or downgraded to attributed/paraphrased prose."
4. **The bounded lift loop is well-positioned and converging.** Pass 0 (fatal floor + ACCURACY 5) → Pass 1 (all
   floors PASS, 40) → Pass 2 (one of two ACCURACY pillars cleared). The *next* in-bounds sweep (Clean Code
   confirm-or-paraphrase + delete the remaining `⚠` markers + align the `naming-good` fence) is a single pass with
   no floor risk that should reach ~44–45 and the bar — comfortably inside the 3-pass ceiling.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
