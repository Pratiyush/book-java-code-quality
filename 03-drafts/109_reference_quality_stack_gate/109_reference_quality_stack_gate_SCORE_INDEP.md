# INDEPENDENT SCORECARD — Ch 46 — model: Claude Opus 4.8 — 2026-06-28 (harsh-skeptic re-score + bounded lift)

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 109 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `109_reference_quality_stack_gate`
- **Title:** So What Do I Actually Set Up? (A reference quality stack & gate design) — Part XIV CAPSTONE
- **Part / arc position:** Part XIV — Capstone & Synthesis, Chapter 46 (OPENS Part XIV)
- **Artifact scored:** `03-drafts/109_reference_quality_stack_gate/109_reference_quality_stack_gate_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (EXAMPLE-BUILD = PASS, build green), `_CODEREVIEW.md` (CODE-REVIEW = PASS-WITH-FIXES, no blocker), prior `_SCORE_INDEP.md` (Sonnet 4.6, 2026-06-20 = 38/50 — now STALE; predates the figures-final / EXAMPLE-BUILD PASS / CODE-REVIEW / the self-narration+carve-out voice fixes already applied to the current draft).
- **Verified against:** SOURCE-PIN.md — pinned 2026-06-20; re-checked 2026-06-28.
- **Scorer:** chapter-scorer agent (Claude Opus 4.8 — INDEPENDENT, different model from drafter).
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (this session: XML-snippet repair on the capstone showcase + em-dash reduction)
- **Build re-run:** NOT possible in this environment — no JDK installed (`/usr/libexec/java_home`: "Unable to locate a Java Runtime"; `JAVA_HOME` empty). Maven is present (`/opt/homebrew/bin/mvn`) but cannot build without a runtime. Green build is taken from the recorded `_EXAMPLE.md` verdict-of-record, exactly as the CODE-REVIEW gate did. The XML fix below is comment-whitespace-only inside a tag region (no element structure, no version literal, no compiled code touched), so the recorded `BUILD SUCCESS` is unaffected; `check_snippets.sh` (which does run here) re-confirmed 7/7 PASS after the fix.

---

## What changed this session (the work order, executed in-bounds)

### 1. PRIMARY FIX — the mangled capstone showcase snippet (`checkstyle-two-pin`) — RESOLVED
The brief's KNOWN SUSPECT is confirmed and fixed. The `checkstyle-two-pin` tag region in
`08-companion-code/109_reference_quality_stack_gate/pom.xml` (lines 140–148) carried **inline `<!-- … -->`
comments pushed to the far right by long runs of padding spaces**, producing displayed lines of **86 and 97
columns**. In a printed-book fixed-width code box (≈70–80 char column) those over-wide lines soft-wrap, and
the far-right `<!-- … -->` drops onto the next line where it jams under the value — the exact reported
symptom ("tags jammed onto value lines / invalid-looking XML"). On the Part XIV capstone — THE chapter of
full-file listings and the marquee "two-pin lesson" snippet — a mangled showcase snippet is high-impact.

**Fix:** collapsed the far-right padding to the single-space trailing-comment house style the module's own
`config/spotless/spotless-reference.xml` already uses (`<version>…</version> <!-- comment -->`). Applied to
both `pom.xml` snippets (`checkstyle-two-pin` and `jacoco-gate`, which had the same 95-col defect on its
`<limit>` line).

**Verification:**
- `checkstyle-two-pin` max displayed line **97 → 78 cols**; `jacoco-gate` `<limit>` line **95 → 70 cols** —
  both now fit a standard print column, comments stay on their value lines, no wrap.
- `xmllint --noout pom.xml` → **WELL-FORMED** (element structure intact; no `<version>`/`<groupId>`/
  `<artifactId>` value changed).
- `check_snippets.sh` on the draft → **7 marker(s); 7 pass, 0 fail**; both fixed snippets still ≤9 displayed
  lines (9 and 5).
- Build impact: **none** (comment-whitespace only). Recorded green build (`BUILD SUCCESS`, 10 tests,
  0 Checkstyle, 0 SpotBugs, JaCoCo branch gate met) stands; not independently re-runnable (no JDK).

### 2. IN-BOUNDS LIFT — em-dash density (READABILITY)
Reader-facing prose (lines 14–153, excluding the back-matter dossier-shorthand dump) measured **9.29
em-dashes / 1000 words** — over the `< 8 / 1000` ceiling and a known AI-prose tell. Converted the heaviest
offenders to colons/commas with **no meaning change and no new facts** (the CONCEPT gate callout 4→0; the
Build/Style alternative parentheticals; the Overview list-wrapper and roadmap aside; the incremental-adoption
"Sequence it" line). Result: prose-only density **9.29 → 5.93 / 1000** (in-bounds). The 9 structural
`**Concern — Tool**` label dashes in the stack bullets were deliberately KEPT — they are a consistent visual-
rhythm device, not clutter; over-pruning would flatten the voice.

### 3. Cross-references — verified against `01-index/FINAL_INDEX.md`
Every prose chapter cross-ref resolves to a real chapter with the matching dossier key: Ch 3 (key 05/37),
Ch 6 (07/34), Ch 9 (11/31/32), Ch 16 (27/28/29/30), Ch 17 (35), Ch 18 (38/30), Ch 19 (39), Ch 25 (53/55),
Ch 27 (62/67), Ch 29 (67), Ch 33 (75/76/79), Ch 34 (80), Ch 35 (81/82), Ch 37 (84/86), Ch 38/40 (87),
Ch 47 (110). No dangling cross-ref. No lift needed.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Two load-bearing figures carry the synthesis before the prose unpacks it: Fig 46.1 is the *what* (the stack by concern, each row naming an alternative), Fig 46.2 the *when/where* (the four-stage feedback-latency ladder). The per-pick `*Catches:* · *Cost:* · *Alternative:* · when-to-swap` structure is uniform and scannable; the CONCEPT gate callout walks pre-commit→PR→nightly→merge cleanly. With the `checkstyle-two-pin` snippet now rendering as valid, copy-pasteable XML, the marquee "two-pin lesson" reads as intended. A reader can reconstruct both the stack and the gate from the chapter alone. Held off 10 only by the triple entry-point (pull-quote → Hook → Overview all re-state "45 chapters neutral, now one recommendation"). |
| 2 | **ACCURACY** | **9** | Fully traceable. The body deliberately asserts **no version/GAV literals** — tools are named by category — so there is no per-tool version in prose to drift. The only version literals live in the companion module, which builds green, and every skew from a SOURCE-PIN top-line is flagged: Checkstyle engine 10.26.1 / SpotBugs 4.9.3.0 (`09-flags/05`), Spotless coordinate split (`09-flags/34`, resolved to 3.6.0), JaCoCo 0.8.15 = pin (flag 48 resolved). All 7 snippets resolve to ≤9-line tag regions in compiling files with recorded paths. No invented rule ID, config key, flag, or benchmark figure anywhere. Not 10 only because the green build (the last accuracy anchor) is recorded, not independently re-runnable here (no JDK). |
| 3 | **UTILITY** | **9** | This is the page a team keeps open on adoption Monday. "When to use what" is directly actionable (starting point / gate stages / legacy / budget / small-team / how-to-swap); the four-stage ladder is copyable; the recommendation is grounded in 7 real snippets from an actually-built reference module a reader can clone, not a plausible-looking fragment. The build-state caveat that capped the stale prior score (UTILITY 8, "build PARTIAL, no snippet in prose") is now resolved: EXAMPLE-BUILD is PASS and the prose carries the module's real `pom.xml`, gate config, and gate-composition. |
| 4 | **DEPTH** | **8** | Correct synthesis depth: it composes the book's structural lessons (layering Ch 3, de-duplication Ch 19, feedback-latency ladder Ch 35, new-code focus Ch 34, incremental adoption Ch 38/40) rather than re-teaching each tool, which is the right scope for a capstone. The intellectual peak — "the stack is necessary scaffolding, not quality itself" — is earned with a concrete worked example (a 2,000-line god object that passes every gate green yet is unmaintainable). Not 9 because, by design, the per-tool mechanism depth lives in Parts IV–IX; this chapter is a-mile-wide-and-a-foot-deep on purpose. Not padded — DEPTH was NOT inflated by word count. |
| 5 | **READABILITY** | **9** | The two persistent AI tells the prior independent score flagged are **gone from the current draft**: zero self-narration ("This chapter is…", "deliberate exception" — none remain) and "carve-out" no longer repeats in running prose (one occurrence, in back-matter shorthand only). This session brought reader-facing em-dash density to 5.93/1000 (in-bounds). Figures break the grey; the Monday-morning hook lands; the hand-off to Ch 47 is clean; the locked voice holds throughout. Off 10 only for the triple entry-point pacing. |

**Cluster subtotal: 44 / 50** (no cluster below 6; lowest is DEPTH at 8).

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep of the body: **zero** hits ("better than", "unlike X", "the problem with X", "superior", "beats", "outperforms", "kills", "destroys", "blows away", "obvious choice over", "no reason to use"). All **3** occurrences of "best" are explicit meta-commentary on the book's OWN neutrality discipline ("'Best' never appeared as a verdict"; "no universally best linter"; "does not say 'this stack is best'"), never a verdict on a tool. Capstone carve-out (NEUTRALITY.md line 20, key 109) correctly invoked: framed "one defensible setup, not the setup"; each of the 9 stack layers names an equally-valid alternative + when-to-swap (verified in prose and mirrored in code via `StackLayer.alternative()`); nothing crowned. "Alternatives & adjacent approaches" is approach-based, not a leaderboard. No section title carries a comparative superlative. |
| **B — HONEST-LIMITATIONS** | **PASS** | Every stack layer carries an explicit `*Cost:*` beat. Dedicated "Limitations & when NOT to reach for it" section with 7 when-NOT items (a-stack-not-the-stack; adopt-incrementally; code-to-own; tools-don't-make-quality; versions-move; OSS-not-effort-free; green-build-necessary-not-sufficient). The deep-dive closes on the chapter's hardest self-limitation — a green gate over a bad design — with a concrete god-object example. Nothing is sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms; body asserts no version/GAV literals; every module skew flagged (09-flags/05, 34, 48). **Compile:** `_EXAMPLE.md` verdict-of-record = `BUILD SUCCESS` via `mvn -B -Pquality verify` on JDK 21.0.11 (Tests run: 10, 0 Checkstyle, 0 SpotBugs, "All coverage checks have been met"). This session's XML fix is comment-whitespace-only → build unaffected; `check_snippets.sh` re-PASS 7/7. Could NOT independently re-run `mvn verify` (no JDK in this env) — green taken from the recorded verdict, as the CODE-REVIEW gate did. **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES, **no blocker**, correctness/idiomatic/security/neutrality all PASS; the prior MAJOR (stale Spotless "does not exist" churn) was resolved 2026-06-27; residual MINORs (dead `evaluations` field F4; engine-skew under-disclosure F5) are non-fatal and out of this session's XML scope — flagged below for the next pass. No red build, no CODE-REVIEW FAIL, no invented detail → FLOOR C PASS. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar: **44/50**, no cluster below 6, all THREE floors PASS (A, B, C incl. recorded green build + CODE-REVIEW no-blocker). Auto-approves into `04-approved/` per the 88% bar; the only remaining human gate is the whole-book Step 16 MANUSCRIPT-GATE.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** With the mangled `checkstyle-two-pin` showcase snippet repaired to valid, copy-
pasteable XML (build unaffected, check_snippets 7/7) and reader-facing em-dash density brought in-bounds, the
capstone reads at 44/50 — CLARITY/ACCURACY/UTILITY/READABILITY 9, DEPTH 8 (correctly-scoped synthesis, not
padded) — with all three content floors PASS; it clears the ship bar and auto-approves.

**Delta from prior independent score:** 38/50 (Sonnet 4.6, 2026-06-20) → 44/50. The +6 is partly a real lift
(XML fix; em-dash reduction) and partly correction of a STALE score: the prior pass scored a draft that
still had self-narration, repeated "carve-out", a PARTIAL build, and no in-prose module snippet — all since
fixed by the drafter/example-builder before this session. Scoring the **current** draft cold, those caps no
longer apply.

---

## Flagged weakest cluster

- **Weakest cluster:** DEPTH — score 8.
- **Why it is the weakest:** By design, the per-tool mechanism depth lives in Parts IV–IX; this capstone is a
  synthesis (a-mile-wide-a-foot-deep). That is the correct scope, not a defect — which is exactly why it sits
  at 8 and not lower, and why padding it would be wrong.
- **Single highest-leverage move to lift it (NOT taken — would risk scope creep):** one more concrete worked
  contrast in the deep-dive (e.g., a second "green gate misses this" example at the architecture layer). Left
  alone: the chapter is already over the bar and the existing god-object example carries the point.

---

## Line-level fixes (carried for the next pass — none block SHIP)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | CLARITY/READABILITY (polish) | Hook + Overview (top of chapter) | Triple entry-point: pull-quote, Hook, and Overview each re-state "45 chapters neutral, now one recommendation" before any stack content. | Collapse to one entry point (fold the pull-quote into the Hook's first sentence; make Overview bullets-only). Worth one CLARITY/READABILITY point toward a 10; not required to ship. |
| 2 | CODE-REVIEW MINOR (F4) | `ReferenceGate.java:31,51` | Dead field: `evaluations` is incremented, never read. Published capstone code should model no dead state. | Remove the field + `incrementAndGet()`, OR add `evaluationCount()` + a test (the `noShipCount` Javadoc already references "evaluations"). One-line source change → must re-verify green. Out of this session's XML scope. |
| 3 | CODE-REVIEW MINOR (F5) | `README.md` version notes | README under-discloses the engine/SpotBugs skews vs the SOURCE-PIN top-lines (13.6.0 / 4.10.2) that `_EXAMPLE.md` already records. | Add the "(vs pinned 13.6.0 / 4.10.2, see 09-flags/05)" deltas so a cold reader sees the flagged skew. Doc-only. |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 35 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT | Initial independent score (Sonnet 4.6). |
| 1 (prior) | 2026-06-20 | 38 / 50 | PASS | PASS | PASS / PENDING / N/A | (approved at then-bar) | Voice pass + two figures. Self-narration / carve-out / build-PARTIAL caps still present at that time. |
| 1 (this) | 2026-06-28 | 44 / 50 | PASS | PASS | PASS / GREEN (recorded) / PASS-WITH-FIXES | **SHIP** | Repaired `checkstyle-two-pin` + `jacoco-gate` XML (97→78, 95→70 cols; xmllint well-formed; check_snippets 7/7; build unaffected). Em-dash density 9.29→5.93/1000 (in-bounds). Cross-refs verified vs FINAL_INDEX. Re-scored current draft cold: self-narration & carve-out-repetition already gone; build now PASS with in-prose module snippets → CLARITY 9, ACCURACY 9, UTILITY 9, DEPTH 8, READABILITY 9. |

---

## Learnings & pipeline suggestions

1. **Far-right padded inline comments are a print-render trap, invisible to `check_snippets`.** The
   `checkstyle-two-pin` snippet passed `check_snippets.sh` and the brace/element-balance audit yet still
   rendered mangled in print, because the failure mode is *column width on soft-wrap*, not malformed XML. A
   cheap lint over displayed tag regions — flag any displayed line > ~80 cols, and any trailing `<!-- … -->`
   preceded by > 1 space — would catch this class before a human spots it on the page. Especially load-bearing
   on the capstone (full-file listings = more wide lines). Suggest adding a max-display-width check to
   `check_snippets.sh`.

2. **Adopt one trailing-comment house style for displayed XML.** `spotless-reference.xml` already uses
   single-space `</…> <!-- … -->`; the `pom.xml` snippets used column-aligned far-right comments. Aligned
   comments look tidy in a wide editor and break in a narrow print column. Promote "displayed-snippet inline
   comments use a single leading space, never column alignment" to EXAMPLES-GUIDE.

3. **A stale independent score must be re-read cold, never trusted as a baseline.** The prior `_SCORE_INDEP`
   (38/50) capped READABILITY for self-narration and "carve-out" repetition that the drafter had **already
   removed**, and capped UTILITY for a build that is now PASS. Re-scoring the current artifact — not deltaing
   the old scorecard — recovered 6 legitimate points. Suggest the scorer always diff the draft's mtime
   against the prior scorecard date and re-verify every cited offending line still exists before carrying a
   cap forward.

4. **Body-prose vs back-matter must be measured separately for AI-tell metrics.** Whole-file em-dash density
   (9.32/1000) is dominated by the machine-shorthand back-matter dossier dump; reader-facing prose is
   5.93/1000. A metric that lumps the two would have demanded harmful cuts to clean prose. Scope AI-tell
   counters (em-dash, hedge words) to the reader-facing body.

*Append to `00-strategy/PIPELINE-LEARNINGS.md`.*
