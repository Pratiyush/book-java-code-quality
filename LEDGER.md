# LEDGER — Java code quality Book

> The living progress board, the continuity bible, and the rule-compliance log — one file. `RESUME.md` says where to start; `CLAUDE.md` is the operating contract; `00-strategy/GUIDELINES.md` is the law. This file says what is true and what has changed. Keep it skimmable; append, do not rewrite history.

> Three sections: **(1) PROGRESS BOARD** — phase, done, next. **(2) CONTINUITY BIBLE** — canonical facts every chapter must agree with; `reconcile_facts.sh` diffs drafts against this. **(3) RULE-COMPLIANCE LOG** — dated rule changes and promotions.

<!-- HOW TO USE THIS TEMPLATE: copy to repo root as LEDGER.md, drop ".template", resolve every {{TOKEN}} from your book-type profile (see .foundation/BOOK-TYPE-PROFILES.md), and START THE BOOK AT PHASE 0 — FOUNDATION with all three sections empty except the structure below. This is instance state; do not carry the reference instance's rows. -->

---

## 1. PROGRESS BOARD

> **LEDGER §1 is the SINGLE SOURCE OF LIVE STATE; RESUME/README/CLAUDE/AGENTS defer to it.** Never duplicate live state across those files — that is the #1 drift source.

<!-- HOW TO FILL: one paragraph each. Start a new book at Phase 0 — Foundation; update as the pipeline advances. The chapter count is canonical in FINAL_INDEX.md (point to it, never hardcode it here). -->

**Current phase:** Phase 3 — DRAFT (IN PROGRESS, main-loop / cheaper mode). Phases 0–2 ✅ + `/pin-source` ✅ done. FINAL_INDEX LOCKED = 47 chapters. **17/47 drafted (Ch 1-17): Parts I-III complete + Part IV Ch 15-17 of 15-19.** Build toolchain READY (JDK 21.0.11 anchor + 25.0.3 forward installed 2026-06-20). FLOOR-C compile = PENDING on companion-module authoring+build (Step 4b EXAMPLE-BUILD), not a toolchain blocker. RESUME: read this line + 01-index/FINAL_INDEX.md (locked TOC) + 01-index/CHAPTER-TRACKER.md (per-ch status); dossiers in 02-research/; drafts in 03-drafts/.
<!-- prior-phase note: Phase 1 — RESEARCH COMPLETE: all 110/110 dossiers banked (keys 01–110, every Part I–XV). Built via multi-agent workflow (keys 07–40, each with a `_VERIFY.md`) then **cheaper main-loop solo research** (keys 01–06 pilot + 41–110, after the spend limit aborted the Part V workflow). Global neutrality sweep = 0 banned phrasings. Verify status: 07–40 have separate `_VERIFY.md`; 01–06 + 41–110 research-done, formal SOURCE-VERIFY pending → fold into `/pin-source`. SOURCE-PIN versions still `TO-PIN`. -->
**Authority pin:** multi-authority set in `00-strategy/SOURCE-PIN.md` (Java 21+25 LTS anchor; each tool pinned per row). ✅ PINNED 2026-06-20 (full /pin-source pass; versions web-verified). Rolling/SaaS rows (CodeQL, Renovate/Dependabot, Snyk, GitHub/GitLab) pinned at use; preview items marked AHEAD-OF-PIN.
**Repo state:** under git on `main`; remote `origin` = https://github.com/Pratiyush/book-java-code-quality (PUBLIC). Working dir `/Users/pratiyush/Desktop/AI/Book-Java-Code-Quality`. **Build toolchain ready (2026-06-20): openjdk@21 21.0.11 (anchor) + openjdk@25 25.0.3 (forward LTS) installed; Maven 3.9.16 drives both via JAVA_HOME; ahead-of-pin JDK 26 removed.** No human blocker.
**Last updated:** 2026-06-20 — Phase 0–2 complete + /pin-source done; 17/47 drafted (Parts I-III complete + Part IV Ch 15-17); build toolchain installed (JDK 21.0.11 + 25.0.3); FLOOR-C now PENDING on companion-module authoring+build, no longer on a missing JDK.

### Phase map

> The five phases are LOCKED and invariant across every book type. Do not add, remove, or renumber.

| Phase | What | Status |
|---|---|---|
| 0 — Foundation | Strategy, authority pin, candidate pool, folder tree, tooling | **✅ COMPLETE** — kernel adapted; pool built; SOURCE-PIN fully pinned (2026-06-20). (Optional law-file fine-tuning remains, non-gating.) |
| 1 — Research | Bank a verified dossier per candidate | **COMPLETE** — 110/110 dossiers banked (07–40 with `_VERIFY.md`; rest verify-pending → `/pin-source`) |
| 2 — Select | Score the pool, cull to ONE book; human confirms the cut | **✅ COMPLETE / LOCKED 2026-06-20** — 110 keys → 47 chapters (14 Parts), human-confirmed. See `01-index/FINAL_INDEX.md`. |
| 3 — Draft + Gate | Draft confirmed chapters through the gated pipeline | **IN PROGRESS** — 17/47 drafted (Parts I-III complete + Part IV Ch 15-17 of 15-19); per-chapter SCORE 40-43/50, floors A/B/C-source PASS; FLOOR-C compile = PENDING on companion-module authoring+build (toolchain READY: JDK 21.0.11 anchor + 25.0.3 forward installed 2026-06-20). Next: Ch 18 (keys 38+40, writing custom rules; annotation processors & Lombok). |
| 4 — Assemble | Compile approved chapters into the manuscript | not started |

### Done

<!-- HOW TO FILL: bullet the completed milestones, newest meaningful unit at the top of its phase. Empty at Phase 0 start. -->

- 2026-06-15 — Copied `.foundation/` kernel into the project (verified byte-identical to Book-Quarkus source).
- 2026-06-15 — Instantiated the kernel: all `templates/*.template.md` → real instance files; `agents/`, `commands/`, `scripts/` wired into `.claude/`; all `{{TOKEN}}` placeholders resolved to the Java-code-quality profile.
- 2026-06-15 — Authored `00-strategy/SOURCE-PIN.md` (multi-authority pin set) and `01-index/CANDIDATE_POOL.md` (110 frozen-key topics across 15 Parts).
- 2026-06-15 — Set scoring rubric (standard 5-cluster set fits) and this live-state board.

### Next (in order)

1. **Phase 3 — DRAFT.** Draft the 47 locked chapters through the gated pipeline (`/draft` → example-build + CODE-REVIEW → VERIFY → clarity → audit → score → reconcile → human-approve). Start with a pilot chapter (e.g. Ch 1) to calibrate the draft pipeline, then fan out. Each chapter's draft re-confirms its dossier atoms against the now-pinned SOURCE-PIN (Step-5 SOURCE-VERIFY) and resolves its `09-flags/` "verify-at-pin" items.
2. **Per-chapter (HARD, at draft):** Step-5 SOURCE-VERIFY (the deferred verify for keys 01–06 + 41–110 folds in here, per chapter) + FLOOR-C example-build (companion module green under `./mvnw -B verify`).
3. **Optional, non-gating:** fine-tune `GUIDELINES-JAVA-QUALITY.md` §0–§1 + `VOICE-GUIDE-JAVA-QUALITY.md` prose; wire the multi-authority source-pin scripts.
4. **Phase 4 — ASSEMBLE** approved chapters into the manuscript (`/assemble`).

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
