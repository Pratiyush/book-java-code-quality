# LEDGER — Java code quality Book

> The living progress board, the continuity bible, and the rule-compliance log — one file. `RESUME.md` says where to start; `CLAUDE.md` is the operating contract; `00-strategy/GUIDELINES.md` is the law. This file says what is true and what has changed. Keep it skimmable; append, do not rewrite history.

> Three sections: **(1) PROGRESS BOARD** — phase, done, next. **(2) CONTINUITY BIBLE** — canonical facts every chapter must agree with; `reconcile_facts.sh` diffs drafts against this. **(3) RULE-COMPLIANCE LOG** — dated rule changes and promotions.

<!-- HOW TO USE THIS TEMPLATE: copy to repo root as LEDGER.md, drop ".template", resolve every {{TOKEN}} from your book-type profile (see .foundation/BOOK-TYPE-PROFILES.md), and START THE BOOK AT PHASE 0 — FOUNDATION with all three sections empty except the structure below. This is instance state; do not carry the reference instance's rows. -->

---

## 1. PROGRESS BOARD

> **LEDGER §1 is the SINGLE SOURCE OF LIVE STATE; RESUME/README/CLAUDE/AGENTS defer to it.** Never duplicate live state across those files — that is the #1 drift source.

<!-- HOW TO FILL: one paragraph each. Start a new book at Phase 0 — Foundation; update as the pipeline advances. The chapter count is canonical in FINAL_INDEX.md (point to it, never hardcode it here). -->

**Current phase:** Phase 3 — DRAFT + GATE; **Phase I (PRE-FINAL PREP) UNDERWAY (2026-06-27).** Phases 0–2 ✅ + `/pin-source` ✅; Phases G + H COMPLETE. FINAL_INDEX LOCKED = 47 chapters. **47/47 drafted + figured + source-verified + code-reviewed; all editorial gates except the external SCORE are done.** **FLOOR-C COMPLETE book-wide** (compile + CODE-REVIEW: 45 modules green + 2 N/A; 45 `_CODEREVIEW.md` on disk); floors A/B/C-source PASS. **Voice-lift: 29/47 chapters lifted** (18 remaining). **External-scoring packets built (47); front-matter draft-filled; @pin web-verify partial; engine-bump attempted+reverted (finding — see Phase I para).** **0 chapters approved (`04-approved/` empty)** — the per-chapter gate is the external-LLM independent SCORE ≥88% (8/47 at 68–80%; queue `09-flags/external-review/QUEUE.md`) → Claude lift → `status.py` auto-approve → Step-12. **Remaining this phase:** finish the 18 lifts → build the pre-final review folder → external review → prefinal-edit → prefinal-draft. See the dated paragraphs below + `09-flags/PENDING-TASKS.md`. RESUME: read this line + 01-index/FINAL_INDEX.md (locked TOC) + 01-index/CHAPTER-TRACKER.md (per-ch status) + 01-index/STATUS-MATRIX.md (rendered, drift-checked); dossiers in 02-research/; drafts in 03-drafts/.
<!-- prior-phase note: Phase 1 — RESEARCH COMPLETE: all 110/110 dossiers banked (keys 01–110, every Part I–XV). Built via multi-agent workflow (keys 07–40, each with a `_VERIFY.md`) then **cheaper main-loop solo research** (keys 01–06 pilot + 41–110, after the spend limit aborted the Part V workflow). Global neutrality sweep = 0 banned phrasings. Verify status: 07–40 have separate `_VERIFY.md`; 01–06 + 41–110 research-done, formal SOURCE-VERIFY pending → fold into `/pin-source`. SOURCE-PIN versions still `TO-PIN`. -->
**Authority pin:** multi-authority set in `00-strategy/SOURCE-PIN.md` (Java 21+25 LTS anchor; each tool pinned per row). ✅ PINNED 2026-06-20 (full /pin-source pass; versions web-verified). Rolling/SaaS rows (CodeQL, Renovate/Dependabot, Snyk, GitHub/GitLab) pinned at use; preview items marked AHEAD-OF-PIN.
**Repo state:** under git on `main`; remote `origin` = https://github.com/Pratiyush/book-java-code-quality (PUBLIC). Working dir `/Users/pratiyush/Desktop/AI/Book-Java-Code-Quality`. **Build toolchain ready (2026-06-20): openjdk@21 21.0.11 (anchor) + openjdk@25 25.0.3 (forward LTS) installed; Maven 3.9.16 drives both via JAVA_HOME; ahead-of-pin JDK 26 removed.** No human blocker.
**Last updated:** 2026-06-27 — **records/docs maintenance pass** (no git): `status.py` under-crediting fixed so FLOOR-C is now visible — the 45 `_CODEREVIEW.md` (FLOOR-C 2nd half) and the green-build evidence are read from disk and surfaced in the matrix/scoring/HTML (new C-build + C-rev floor columns; FLOOR-C rollup line); route = **0 auto-eligible / 42 lift / 5 need-indep / 0 approved**, **drift CLEAN**, figures **67/67** pass. `audit.jsonl` backfilled with 15 Phase C/D/E/G/H/I milestones at real git dates (`AUDIT_TS` override added to `audit_log.sh`). 11 dir-scoped `AGENTS.md` created. Phase status advanced to **Phase I underway** (see the Phase H/I paragraph below). — prior: **Phase G (post-WS-E) COMPLETE** (~13 PRs #42–#54 merged to `main`): **FLOOR-C complete book-wide** (compile + CODE-REVIEW on all 45 modules; 45 `_CODEREVIEW.md`); two FLOOR-A neutrality catches + several snippet fixes applied to deliverable content; process scaffolding landed (`09-flags/PENDING-TASKS.md`, the BOOK-STRATEGY charter, `10-logs/activity.jsonl`); WS-F machinery + 4 manuscript-gate dry-runs in `06-assembly/` (ORIGINALITY PASS · PROOF/REDTEAM PASS-WITH-FIXES · READERSIM FIX); book-wide cross-ref renumber (54 fixes, 0 in-prose refs >47). Still **0 chapters approved** — gated on external-LLM scores ≥88%. — Phase 0–2 complete + /pin-source done; 47/47 drafted (ALL chapters Ch 1-47; Parts I-XIV COMPLETE); build toolchain installed (JDK 21.0.11 + 25.0.3). **CAPSTONES BUILT (2026-06-20): three enterprise microservice apps under `08-companion-code/capstones/` — `shared-platform` lib + `01-commerce-checkout` (catalog/payment/order), `02-fintech-ledger` (account/ledger/transfer), `03-logistics-fulfil` (inventory/shipment/orchestrator). Full reactor green: tests + Checkstyle 0 + SpotBugs 0 under `-Pquality`. DEMO-CATALOG §4 + COMPANION-REPO updated to the 3-capstone layout (superseding the single `99_capstone_*`). Process-control tooling (status.py matrix/dashboard + anti-drift + run_gates/pre-commit enforcers) landed earlier 2026-06-20.**
**SPEC ADOPTED (2026-06-25) — the "Dev Branch Startover" target spec, applied to THIS GitHub repo (main + feature-branch→PR; no Jira/Confluence).** Workstream A done: auto-approval bar raised to **88% (44/50)** + floors A/B/C-source PASS, with auto-promote into `04-approved/` wired in `status.py` (a normal run applies; `--check-only`/`--no-apply` are read-only); `SCORING.md` reconciled (88% active bar; 35/50 noted as the locked Phase-2 cull threshold). Workstream B done: `review_figures.py` (20-parameter figure review) + `10-logs/figures.html` (linked in nav), `validate.sh` (pre-push suite), `.github/workflows/ci.yml` + `.gitlab-ci.yml` (parity), `Generate-Prompt.md`, `00-strategy/AUDIENCE.md` + `01-index/AUDIENCE.md`, `00-strategy/PROVENANCE-LOG.md`. Remaining: **C** 4th capstone (`04-quality-operations`), **D** per-chapter companion modules + EXAMPLE-BUILD, **E** `@pin`/`AHEAD-OF-PIN` cleanup, **F** assembly + manuscript-level gates. Approvals land incrementally as the external LLM re-scores lifted chapters to ≥88%. Gaps surfaced: 4 figures were missing `.sources.md` sidecars (being authored); the `Java Quality-BOOK-STRATEGY.md` charter referenced by the law files is absent on disk; `10-logs/activity.jsonl` not yet created.**
**WORKSTREAMS C + D COMPLETE (2026-06-27).** C: 4th capstone `04-quality-operations` (ingest/metrics/gate) built green; the capstones reactor is 4 apps + shared-platform. D: **all 47 chapters' EXAMPLE-BUILD gate resolved — 45 companion modules built green + 2 honest N/A (Ch01, Ch06, pure-concept)**. The `08-companion-code` reactor (47 module entries) builds `mvn -B -Pquality verify` → BUILD SUCCESS with **0 Checkstyle / 0 SpotBugs across the whole book**; every displayed snippet is bound to a compiled `// tag::`/`// end::` region (`check_snippets` all PASS); `status.py` "need example" = 0; drift clean. 22 PRs merged to `main` this run. Also fixed the recurring `target/` churn by untracking build output (PR #8). **WS-E started**: the per-chapter module builds surfaced concrete SOURCE-PIN corrections to apply — JaCoCo 0.8.16 is unpublished on Central (→ 0.8.15), Spotless 8.7.0 is the Gradle line not the Maven plugin, and the house analyzer engines (Checkstyle 10.26.1 / SpotBugs 4.9.3.0) trail the SOURCE-PIN top-lines (13.6.0 / 4.10.2) reactor-wide — all logged in `09-flags/`. Remaining: **E** `@pin`/`AHEAD-OF-PIN` prose cleanup + source-verify, **F** assembly + manuscript-level gates; CODE-REVIEW (FLOOR-C second half) is owed per module; chapter approvals still gated on the external LLM re-scoring lifted chapters to ≥88%.**
**PHASE G (post-WS-E) COMPLETE (2026-06-27) — ~13 feature-branch PRs (#42–#54) merged to `main`.** **FLOOR-C is now COMPLETE book-wide (compile + code-review).** G1 — durable process scaffolding: created `09-flags/PENDING-TASKS.md` (the standing remaining-work tracker), the long-missing `00-strategy/Java Quality-BOOK-STRATEGY.md` charter referenced by the law files, and seeded `10-logs/activity.jsonl`. G2 — trimmed Ch22 (virtual threads) over-quotes to the LEGAL-IP §2 ceiling (12→8 quotes; JEP 444 6→2); flag 22 §B resolved. G3 — **CODE-REVIEW gate (FLOOR-C second half) run on ALL 45 built companion modules** → 45 `_CODEREVIEW.md` reports; ~14 shipped-content fixes applied, including **two FLOOR-A NEUTRALITY catches in deliverable text** (Ch26 "beats" inside a displayed `@SuppressFBWarnings` justification string, Ch84 "better than") and several broken/inaccurate displayed snippets (Ch03 duplicate end-tag, Ch22 mid-statement snippet, Ch48 wrong boundary operator, Ch70 unverified OWASP ordinal, Ch75 off-pin JaCoCo 0.8.16 + a false binding). Verified on disk: the residual "beats"/"better than"/`Chapter >47` strings now live **only inside gate reports** (which quote dossier keys + document the catches), **never in the `_vN` manuscript prose** — drafts are clean. G4 — WS-F machinery scaffolded in `06-assembly/`: `00_front-matter.md`, `AI-DISCLOSURE.md`, `README.md` (assembler contract); `PROVENANCE-LOG` refreshed. `/assemble` dry-run = 0/47 approved → MANUSCRIPT pending, machinery ready. G5 — **4 manuscript-level gate DRY-RUNS** (independent Sonnet, reports in `06-assembly/`): ORIGINALITY = **PASS** (0 regurgitation), PROOF + REDTEAM = **PASS-WITH-FIXES**, READERSIM = **FIX**. Convergent #1 finding fixed: a book-wide cross-reference renumber (dossier-key → FINAL_INDEX printed number; 54 fixes; **0 in-prose refs >47 remain**). G6 — refreshed `09-flags/external-review/QUEUE.md` with per-chapter lift targets + a clean-first scoring order (the approval-loop handoff). **State after G:** all 47 drafted + voice-lifted + figured + source-verified + code-reviewed; FLOOR-C complete; **0 chapters approved (`04-approved/` empty)** — gated on external-LLM independent scores ≥88% (8/47 scored at 68–80%). `06-assembly/` has front-matter + AI-DISCLOSURE + 4 dry-run reports; MANUSCRIPT pending. **REMAINING (blocked, not Claude-solo): external scores → Claude lifts → auto-approve → assemble → human Step-16; networked `/pin-source` for ~182 flagged residuals; engine-version bump + env-gated REPRO (networked Maven); figure-caption-by-key normalization (deferred follow-up).**

**PHASE H COMPLETE + PHASE I (PRE-FINAL PREP) UNDERWAY (2026-06-27).** **H (approval-prep + cleanup):** H1 — generated **47 ready-to-paste external-scoring packets** (one-pager per chapter for the independent different-vendor LLM) + a reusable `gen_packets.sh`. H2 — **lifted the 8 chapters that carry independent scores** (68–80%) toward the 88% bar, packets regenerated. H3 — applied the convergent manuscript-dry-run **MAJOR fixes**: code fixes Ch9/24/26 (rebuilt green) + prose/claim fixes Ch1/13/30/40. H4 — **figure-caption renumber** (dossier-key → FINAL_INDEX printed number) across 18 chapters. H5 — **bounded web source-verify** of the public-spec residuals (JEP/ISO) — partial pass; SaaS/web-only rows stay flagged. **I (pre-final prep, in progress):** packets built (H1 / I-handoff); **voice-lift now 29/47** (printed-order batches, Ch1–21 this phase; **18 chapters remain**); **front-matter draft-filled** (I2); **I4 engine-bump FINDING** — bumping the house analyzer engines (Checkstyle/SpotBugs) to the SOURCE-PIN top-lines was attempted and **REVERTED**: the newer engine rules break the chapters' taught examples, so engines stay at the taught versions (logged as a finding, not a defect); **@pin web-verify partial** (carryover from H5). **REMAINING THIS PHASE:** finish the **18** outstanding voice-lifts → **build the pre-final review folder** → **external review** → **prefinal-edit** → **prefinal-draft**. Still **0 chapters approved** — the approval loop (external independent score ≥88% → Claude lift → `status.py` auto-approve) is the gate to clearing Phase 3.

### Phase map

> The five phases are LOCKED and invariant across every book type. Do not add, remove, or renumber.

| Phase | What | Status |
|---|---|---|
| 0 — Foundation | Strategy, authority pin, candidate pool, folder tree, tooling | **✅ COMPLETE** — kernel adapted; pool built; SOURCE-PIN fully pinned (2026-06-20). (Optional law-file fine-tuning remains, non-gating.) |
| 1 — Research | Bank a verified dossier per candidate | **COMPLETE** — 110/110 dossiers banked (07–40 with `_VERIFY.md`; rest verify-pending → `/pin-source`) |
| 2 — Select | Score the pool, cull to ONE book; human confirms the cut | **✅ COMPLETE / LOCKED 2026-06-20** — 110 keys → 47 chapters (14 Parts), human-confirmed. See `01-index/FINAL_INDEX.md`. |
| 3 — Draft + Gate | Draft confirmed chapters through the gated pipeline | **DRAFTING + GATES (non-score) COMPLETE — gated on external SCORE; Phase I pre-final prep underway.** 47/47 drafted + figured + source-verified + **code-reviewed**; **voice-lift 29/47 (18 remaining)**. **FLOOR-C COMPLETE book-wide** (compile + CODE-REVIEW: 45 modules green + 2 N/A; 45 `_CODEREVIEW.md`). Floors A/B/C-source PASS. 47 external-scoring packets built; front-matter draft-filled; engine-bump attempted+reverted (finding). **0 chapters approved (`04-approved/` empty)** — the sole remaining per-chapter gate is the **external-LLM independent SCORE ≥88%** (8/47 scored at 68–80%; queue `09-flags/external-review/QUEUE.md`) → Claude lift → `status.py` auto-approve → Step-12. Pre-final path: finish lifts → pre-final review folder → external review → prefinal-edit → prefinal-draft. |
| 4 — Assemble | Compile approved chapters into the manuscript | **machinery + dry-runs ready; full assembly blocked on approvals.** `06-assembly/` has `00_front-matter.md`, `AI-DISCLOSURE.md`, `README.md` + 4 manuscript-gate dry-run reports (ORIGINALITY PASS · PROOF/REDTEAM PASS-WITH-FIXES · READERSIM FIX). `/assemble` dry-run = 0/47 approved → MANUSCRIPT pending. |

### Done

<!-- HOW TO FILL: bullet the completed milestones, newest meaningful unit at the top of its phase. Empty at Phase 0 start. -->

- 2026-06-15 — Copied `.foundation/` kernel into the project (verified byte-identical to Book-Quarkus source).
- 2026-06-15 — Instantiated the kernel: all `templates/*.template.md` → real instance files; `agents/`, `commands/`, `scripts/` wired into `.claude/`; all `{{TOKEN}}` placeholders resolved to the Java-code-quality profile.
- 2026-06-15 — Authored `00-strategy/SOURCE-PIN.md` (multi-authority pin set) and `01-index/CANDIDATE_POOL.md` (110 frozen-key topics across 15 Parts).
- 2026-06-15 — Set scoring rubric (standard 5-cluster set fits) and this live-state board.

### Next (in order)

> The durable copy of this list lives in `09-flags/PENDING-TASKS.md`. Items 1–2 are Claude-solo (this phase); 3–6 are blocked / not-Claude-solo (external model, networked tooling, or the human gate).

1. **Finish the voice-lift (Claude-solo, this phase).** 18 of 47 chapters still to lift to the locked third-person voice (29/47 done), in printed order. Then re-run `status.py`.
2. **Build the pre-final review folder + complete front-matter (Claude-solo).** Assemble the pre-final review set for external review; finish the draft-filled front matter.
3. **The approval loop (THE gate to clearing Phase 3).** External LLM independently re-scores each lifted chapter (one-pager packet → `_SCORE_INDEP.md`) → Claude lifts the 8 at 68–80% (per `09-flags/external-review/QUEUE.md`, clean-first) → `status.py` auto-approves at ≥88% + floors A/B/C-source PASS → `04-approved/`. Then **prefinal-edit → prefinal-draft**.
4. **Networked `/pin-source` for the ~182 flagged residuals** (`@pin` / `AHEAD-OF-PIN` / `UNVERIFIED`), incl. the WS-D pin corrections to confirm (JaCoCo 0.8.16→0.8.15; Spotless Maven 3.x vs Gradle 8.7.0 line; Checkstyle/SpotBugs engine top-lines). Needs network; SaaS/web-only rows stay flagged.
5. **Engine-version bump + env-gated REPRO** (networked Maven) — note the I4 finding: a straight bump breaks the taught examples; any bump must reconcile the chapters' examples first. Then run the reproducibility check.
6. **Phase 4 — ASSEMBLE** once chapters reach `04-approved/` (`/assemble`), then the human **Step 16 MANUSCRIPT-GATE**. Deferred follow-up: figure-caption-by-key normalization.

---

## 2. CONTINUITY BIBLE

> Canonical facts that MUST agree across every dossier, draft, and figure. `reconcile_facts.sh` diffs drafts against this section; a mismatch is a HARD-gate failure. If a fact here is wrong, fix it HERE first, then reconcile. **Every entry traces to the pins in SOURCE-PIN.md.**

<!-- HOW TO FILL: one table of canonical facts (versions/editions/names/coordinates), one canonical-names table (exact spellings; do not drift), and a glossary stub that expands as chapters land. Every row must trace to the pin. Empty at Phase 0 except the pin row. -->

### Authority & source pin

| Fact | Canonical value |
|---|---|
| Subject | Java code quality |
| Authority source | the pinned authority set (00-strategy/SOURCE-PIN.md) |
| Pin | the pins in SOURCE-PIN.md |
| Clone / fetch path | per-tool ephemeral fetch dirs (see SOURCE-PIN.md) |
| Forbidden refs | any off-pin source: a tool's `main`/`-SNAPSHOT`/nightly, a version newer than the pinned row, a JDK feature ahead of 21/25, FindBugs (dead → SpotBugs), AI-generated text or content farms as a factual source. |

### Canonical names (use these EXACT spellings; do not drift)

> One row per name a reader could get wrong. Each says what to call it AND what NOT to call it.

| Canonical name | Use it for — and do NOT call it |
|---|---|
| _(name)_ | _(meaning; the wrong spellings/old names to avoid.)_ |

### Never-invent atoms register

> The atoms this book NEVER invents: rule IDs, config/ruleset keys, tool flags & option names, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Each that recurs across chapters is fixed here once and re-confirmed against the pin in the chapter's `_VERIFY.md` before it appears in a draft. Untraceable → mark `UNVERIFIED` and flag to `09-flags/`.

| Atom | Canonical value | Notes |
|---|---|---|
| _(unit)_ | _(value)_ | _(source path at the pin)_ |

### Glossary stub (expand as chapters land)

- _(term — one-line plain-language definition, traced to the pin.)_

---

## 3. RULE-COMPLIANCE LOG

> Dated entries: rule changes, promotions from `00-strategy/PIPELINE-LEARNINGS.md` into law, pin events, and gate-policy shifts. Newest first. Append; never delete.

<!-- HOW TO FILL: every meaningful unit of work appends an entry (Continuous-improvement HARD RULE). Empty at Phase 0 start. -->

### 2026-06-27 — Records/docs maintenance: FLOOR-C made visible in the reports; audit trail backfilled

- **Under-crediting bug fixed in `status.py` (FLOOR-C was invisible).** The 45 `_CODEREVIEW.md` reports (FLOOR-C 2nd half, a HARD gate) and the green-build evidence had **no surface** in the matrix/scoring/HTML — only `_EXAMPLE.md` was read, and the scoring page showed FLOOR-C COMPILE as the stale `PENDING-RUNTIME` copied from the (older) `_SCORE.md` floor tables. Added a verdict reader that reads `_EXAMPLE.md` + `_CODEREVIEW.md` from disk and **upgrades FLOOR-C from on-disk evidence** (mirroring the existing `_EXAMPLE.md` bubble upgrade): new **C-build + C-rev** floor columns (MD + HTML), a **FLOOR-C rollup** ("45/47 modules green; 45 CODE-REVIEW reports, 45 PASS / 0 FAIL"), and the example bubble now reflects both halves. Route unchanged (**0 auto-eligible / 42 lift / 5 need-indep / 0 approved**); **drift CLEAN**; figures **67/67**.
- **Verdict-parser lesson (logged to PIPELINE-LEARNINGS).** Reading a gate verdict off disk is non-trivial: the reports (a) state PASS while *counting* failures inline ("PASS-WITH-FIXES … 0 FAIL"), (b) open `## VERDICT: FAIL`, narrate the fix, then **upgrade** ("Verdict upgraded FAIL → PASS-WITH-FIXES"), and (c) carry narrative N/A mentions ("the dossier's N/A call was revised") on a module that was actually built. The reader must test PASS before FAIL, follow `→` upgrades to the target, take the LAST decisive declaration, and scope module-N/A to verdict lines. Confirms Ch03 + Ch26 FAILs were **fixed → PASS** (consistent with "FLOOR-C COMPLETE book-wide"); **no unresolved FLOOR-C blocker on disk**.
- **Audit trail backfilled + made honest.** `10-logs/audit.jsonl` was dominated by `status.py` regen lines; the Phase C/D/E/G/H/I semantic milestones were missing. Backfilled **15 milestones at real git-log dates** (code-review of the 45 modules, EXAMPLE-BUILD complete, source-verify cleanup, capstone-04, manuscript-gate dry-runs ×4-as-one, cross-ref renumber, scoring-packets, lifts, figure renumber, engine-bump-reverted). Added an `AUDIT_TS` env override to `audit_log.sh` so a backfill records the **real historical date** (never fabricated). **Going forward, AUTO per-tool capture still needs the `PostToolUse` hook wired in `settings.json` — human sign-off required.**
- **11 dir-scoped `AGENTS.md` created** (`00-strategy` … `.claude`) — each a ~30-line map (what it holds / naming / who writes-reads / pointer up to root AGENTS + LEDGER), no duplication of the root map.
- **No rule promoted this entry** (records/tooling milestone). Promotion candidate carried forward: the FLOOR-C-from-disk crediting + the `check_neutrality.sh`-over-`08-companion-code/**` pass (both in PIPELINE-LEARNINGS for `/retro`). Noted on disk: a stray nested `08-companion-code/08-companion-code/` path (earlier copy); canonical capstones are `08-companion-code/capstones/`.

### 2026-06-27 — Phase G: FLOOR-C COMPLETE book-wide + two FLOOR-A neutrality catches in deliverable content

- **FLOOR-C now PASS book-wide (compile + CODE-REVIEW).** The CODE-REVIEW half of FLOOR-C (HARD) ran on all **45 built companion modules** → 45 `03-drafts/*/*_CODEREVIEW.md` reports on disk. Compile half was already green (WS-D reactor `mvn -B -Pquality verify` = 0 Checkstyle / 0 SpotBugs). FLOOR-C is the first floor fully closed for every chapter that has a module (Ch01/Ch06 are honest N/A, pure-concept).
- **FLOOR-A (NEUTRALITY) — two banned-phrasing failures caught in DELIVERABLE text and fixed:** Ch26 "beats" shipped **inside a displayed `@SuppressFBWarnings` justification string** (the `justified-suppression` snippet readers paste) — not just prose; Ch84 "better than". Both fixed to non-verdict phrasing. Confirms the standing lesson that the NEUTRALITY blocklist must be scanned over **companion-code source/comments/justification strings**, not draft prose alone (see PIPELINE-LEARNINGS, promotion candidate: `check_neutrality.sh` over `08-companion-code/**`).
- **Other shipped-content corrections (~14 total, FLOOR-C / source-trace):** Ch03 duplicate `// end::` tag, Ch22 snippet starting mid-statement, Ch48 wrong boundary operator, Ch70 unverified OWASP ordinal, Ch75 off-pin JaCoCo 0.8.16 + a false binding. All are draft↔code fidelity / invention-floor fixes.
- **LEGAL-IP §2 enforced:** Ch22 over-quoting trimmed to the ceiling (12→8 quotes; JEP 444 6→2); `09-flags/` flag 22 §B resolved.
- **Manuscript-level gate DRY-RUNS (independent Sonnet, pre-assembly, reports in `06-assembly/`):** ORIGINALITY **PASS** (0 regurgitation), PROOF **PASS-WITH-FIXES**, REDTEAM **PASS-WITH-FIXES**, READERSIM **FIX**. Convergent #1 fix applied: book-wide cross-ref renumber (dossier-key → FINAL_INDEX printed number; 54 fixes). Verified: residual >47 / banned strings remain **only in gate reports**, 0 in `_vN` manuscript prose.
- **No rule promoted this entry** (records-only milestone); the `check_neutrality.sh`-over-code promotion is logged in PIPELINE-LEARNINGS for `/retro`.

### 2026-06-20 — Flagship companion app built GREEN (first real FLOOR-C exercise)

- **DEMO-CATALOG §2 realized:** `08-companion-code/storefront-checkout/` — a runnable checkout-generation API microservice + deterministic payment simulator in `org.acme.storefront`, **zero runtime dependencies** (JDK `HttpServer` + virtual threads). Built on a parent aggregator POM (`08-companion-code/pom.xml`) pinning JDK 21 + the SOURCE-PIN test libs (JUnit 6.0.3, AssertJ 3.27.7).
- **Green build (author request: "build a real app … add to rule"):** `mvn -B verify` = **28 tests pass**; `mvn -B -Pquality verify` adds the Chapter-16 rule gates — **Checkstyle 10.26.1** (curated ruleset, engine-override = the two-pin lesson) **0 violations** + **SpotBugs 4.9.3.0** (effort=Max) **0 bug instances**. First time FLOOR-C COMPILE has actually run green for a real module (the toolchain install paying off).
- **Dogfoods Parts II-III in code:** immutable records + defensive copy (Ch 8), sealed result types + exhaustive switch (Ch 10), Optional/no-null (Ch 9), atomic compound ops `computeIfPresent`/`computeIfAbsent` (Ch 13), virtual-thread-per-request + routes-in-start() to avoid this-escape (Ch 13/14), externalized config, Luhn + idempotent payment simulator. README with run/curl instructions shipped.
- **Pin correction surfaced:** Maven Central's real latest Checkstyle engine is **10.26.1**, not the key-27 dossier's misread "13.6.0" — verified at Central; pin/draft should use 10.26.1.
- **Next on the example:** remaining Ch-16 analyzers (PMD/CPD, Error Prone) can be added to the `quality` profile the same way; per-chapter companion modules + DEMO-CATALOG per-chapter rows still to backfill.

### 2026-06-20 — Build toolchain installed (FLOOR-C toolchain blocker RESOLVED)

- **JDK toolchain installed to the pin (human action, at author's request):** Homebrew `openjdk@21` = **21.0.11** (the SOURCE-PIN anchor) and `openjdk@25` = **25.0.3** (the SOURCE-PIN forward LTS); ahead-of-pin `openjdk` 26.0.1 **removed** (author decision: stay on the pinned 21/25 matrix). Maven 3.9.16 drives both via `JAVA_HOME` (anchor `/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home`; forward `/opt/homebrew/opt/openjdk@25/libexec/openjdk.jdk/Contents/Home`). Both keg-only (use `JAVA_HOME`, not a system symlink).
- **Effect:** the former HUMAN-ONLY "install a JDK" blocker is resolved. FLOOR-C COMPILE for each chapter is now PENDING only on authoring + building its companion module (Step 4b EXAMPLE-BUILD) green under `mvn -B verify` (`--release 21`; optional 25 forward-check) — an in-pipeline task, not a toolchain/human blocker. Companion modules under `08-companion-code/` are currently spec-only (in each draft's foot).

### 2026-06-20 — Phase 1 complete + Phase 2 cull LOCKED

- **Phase 1 RESEARCH complete:** all 110/110 dossiers banked (Parts II–IV via multi-agent workflow w/ `_VERIFY.md`; 01–06 + 41–110 via cheaper main-loop after the spend-limit aborted the Part V workflow). Global neutrality sweep = 0. ~302k words. Pushed to public GitHub (17 commits).
- **Phase 2 SELECT LOCKED (human gate):** `FINAL_INDEX.md` confirmed by Pratiyush — 110 keys → **47 chapters / 14 Parts**, 0 hard cuts (merge-driven). Drafting now unblocked.
- **Deferred (HARD, before drafting a chapter):** `/pin-source` resolves every `TO-PIN` + folds in the SOURCE-VERIFY for keys 01–06 + 41–110 + the 60 `09-flags/` items. Logged as the standing next action.
- **Process note:** "beats/better than" slipped into prose ~6× across batches (ordinary-English verbs); caught by the per-batch + global banned-phrasing sweep each time. Recommend promoting an automated `check_neutrality.sh` pre-pass (PIPELINE-LEARNINGS).

### 2026-06-15 — Part I research pilot /retro

- Created `00-strategy/PIPELINE-LEARNINGS.md` (changelog + Durable principles + Folklore list + OPEN ITEMS) and the de-port banned-list.
- **Promoted → `GUIDELINES-JAVA-QUALITY.md` §5:** (a) *Standards/spec edition discipline* — edition-specific facts require the edition's own text; secondaries corroboration-only. (b) *No folklore-as-fact* — don't repeat poorly-evidenced figures; cite the debunking. Also noted in `SOURCE-PIN.md` (ISO row).
- **Proposed, not yet promoted:** two-schools shape for ⚠ chapters, metric-card shape, concept-chapter example policy, companion reference-project seed → tracked in PIPELINE-LEARNINGS OPEN ITEMS (promote after a second batch confirms).
- Stage-report scripts not yet runnable (multi-authority source-pin scripts unwired) — logged as an OPEN ITEM, not a blocker.

### YYYY-MM-DD — _(template placeholder)_

- _(what rule changed / was promoted, and the rule file touched.)_

> Promotion procedure: confirmed lessons in `00-strategy/PIPELINE-LEARNINGS.md` are promoted into the relevant rule file (usually `GUIDELINES.md`), then logged here with the date and the rule touched. Use `/retro` at boundaries to formalize.
