# GATE REPORT TEMPLATE — Java code quality Book

> One report per gate run, per chapter. Generic enough for any HARD gate: **VERIFY**, **AUDIT**,
> **CLARITY** (tech-clarity-reviewer), or **RECONCILE** (cross-chapter). Copy this shape, name the gate,
> fill every field. A report is the gate's verdict of record — it lives beside the draft in
> `03-drafts/NN_slug/` (e.g. `NN_slug_VERIFY.md`, `NN_slug_AUDIT.md`).
>
> A gate either **PASS**es, **FAIL**s, or **PASS**es **-WITH-FIXES**. There is no fourth verdict.
> Every finding names a location and a fix. No finding is left without one of those two.
>
> **Pin (HARD):** Any fact this gate checks — any rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, snippet, or example — traces to the
> pinned Java code quality the pins in SOURCE-PIN.md, clone/fetch at `the per-tool fetch dirs in SOURCE-PIN.md`. A claim that exists
> only ahead of the pin (a newer/unreleased state the project has not pinned) is a finding, not a fact.

---

## Header

- **Gate:** [VERIFY | AUDIT | CLARITY | READER-SIM | RECONCILE | EXAMPLE-BUILD | CODE-REVIEW | other]
  *(EXAMPLE-BUILD and CODE-REVIEW are technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off drop those rows.)*
- **Chapter key:** NN (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `NN_slug`
- **Draft under review:** `03-drafts/NN_slug/NN_slug_vN.md`
- **Run date:** YYYY-MM-DD
- **Reviewer:** [agent / script name, e.g. `source-verifier` + `verify_sources.sh`]
- **Scripts run:** [e.g. `verify_sources.sh`, `lint_citations.sh`, `reconcile_facts.sh` — or `none`]
- **Verdict:** **[PASS | FAIL | PASS-WITH-FIXES]**

---

## Verdict rationale

One or two sentences. Why this verdict. If **PASS-WITH-FIXES**, state that the listed fixes are
required before the next gate and are not optional. If **FAIL**, name the blocking floor or fact.

---

## Findings

Severity scale:

- **BLOCKER** — gate cannot pass; a HARD rule or pin violation, or an unverifiable load-bearing fact.
- **MAJOR** — must fix before approval; wrong, misleading, or a floor at risk.
- **MINOR** — should fix; clarity, precision, or polish.
- **NOTE** — observation, no action required.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | | | [section · line · snippet · cited reference/path] | |
| 2 | | | | |

> Use the exact location the next pass can jump to: section name, line range, the snippet, or the
> citation/path in question. A finding with no actionable location is itself a MINOR finding to record.

---

## Blockers

The subset of findings that are **BLOCKER** severity, restated so the gate boundary is unambiguous.
If there are none, write **None**. Any blocker present forces the verdict to **FAIL**.

- [ ] [Blocker 1 — short statement + finding #]
- [ ] [Blocker 2 — short statement + finding #]

---

## Gate-specific checks

Tick what this gate is responsible for. Drop the rows that do not apply to the gate named in the header.

- [ ] **VERIFY** — every rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, snippet, and example traces to an the pins in SOURCE-PIN.md source path; citations lint clean; no ahead-of-pin facts presented as fact.
- [ ] **AUDIT** — NEUTRALITY floor (neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X; no banned phrasings); voice matches `VOICE-GUIDE.md` (the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md); authenticity (a sharp reader cannot tell a machine wrote it).
- [ ] **CLARITY** — explanation is correct and followable; HONEST-LIMITATIONS floor present (every feature has a "when NOT to use"); structure follows `CHAPTER-TEMPLATE.md`.
- [ ] **READER-SIM** (Step 8a) — read in-persona as the target reader of `00-strategy/AUDIENCE.md` (`{{VOICE.AUDIENCE}}`, no prior knowledge of this chapter's topic): no STALL / GUESS / UNSTATED-PREREQUISITE / CANNOT-REPRODUCE blocker remains; the chapter's promise is delivered FOR THAT READER. Any unresolved reader-blocker is a BLOCKER (FAIL).
- [ ] **RECONCILE** — no contradiction with `LEDGER.md` continuity, the hand-off chain, or facts asserted in other chapters; every figure (per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured) is load-bearing and source-cited; designed diagrams are rendered from committed source text.
- [ ] **EXAMPLE** (Step 4b) *(technical profile — see BOOK-TYPE-PROFILES.md):* companion artifact builds green via `./mvnw -B verify` at the pins in SOURCE-PIN.md; every displayed snippet resolves to a real bounded tag region (per one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green) in the compiled file; FLOOR C source-trace clean.
- [ ] **CODE-REVIEW** *(technical profile — see BOOK-TYPE-PROFILES.md):* correctness / idiomatic / security / simplicity / prose-code-fidelity / neutrality-in-code PASS.

---

## Learnings & pipeline suggestions

[Close every gate report with what this run taught us and any rule/template change to propose.
Also append the learning to `PIPELINE-LEARNINGS.md`; `book-maintainer` logs it in `LEDGER.md`.]

---

## Self-log (final step — required)

Append one provenance line to the activity log so this gate run is recorded for the provenance-officer:

```
.claude/scripts/log_action.sh <actor> <step> <key> gate-run <verdict>
```

(e.g. `.claude/scripts/log_action.sh source-verifier 5 NN gate-run PASS`). One line per gate run, after the verdict is set.
