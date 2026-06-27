# FLAG — key 85: DORA performance bands + SPACE five dimensions + SonarQube dashboard/portfolio specifics verify-at-pin

**Chapter:** 85 `metrics_rollout_dashboards` (owner key 85, folds 87 + 88) — FINAL_INDEX Ch 38 (CLOSES Part X).
**Companion module:** `08-companion-code/85_metrics_rollout_dashboards/` — ✅ BUILT GREEN (JDK 21.0.11,
`mvn -B -Pquality verify`: 11 tests, 0 Checkstyle, 0 SpotBugs; JDK-only).

## What is VERIFIED (resolved out of the deferred-marker set — recorded here so it is not re-flagged)

- **DORA four-key definitions** (deployment frequency + lead time for changes = throughput;
  change-failure rate + failed-deployment recovery time = stability) and the **throughput/stability-correlate**
  finding ("elite teams are both fast and stable, not a trade-off"). Pinned: SOURCE-PIN **§5** (2025 DORA
  report, `dora.dev`) + **§7** (*Accelerate*, Forsgren/Humble/Kim, 2018) — both pinned, **corrected
  2026-06-27**. Also runnable + tested in the module (`DoraMetrics`: the change-failure-rate formula is the
  fraction of deployments that caused a failure, `correlatedReport()` returns throughput beside stability).
  The chapter attributes these by name. **No longer a deferred marker.**

## Atoms that STAY `⚠ verify-at-pin` (never asserted as fact in the body)

1. **DORA performance bands** — the elite / high / medium / low cohort **thresholds** (e.g. a change-failure-rate
   percentage, a deployment-frequency cadence, a lead-time or recovery-time band). These are **edition-specific
   and the year matters**; SOURCE-PIN §5 pins the *2025 DORA report* but the book does not fetch the report into
   a pinned clone and these numeric bands shift edition to edition. **The chapter asserts NO numeric band**, by
   design and by instruction — `DoraMetrics` deliberately reports values and not a band (javadoc lines 18–23);
   `MetricsConfig` / `metrics-{dev,prod}.properties` carry a *chosen alert level* (0.30 dev / 0.15 prod)
   explicitly labelled "not a DORA band". The cohort word "elite" appears only in the attributed correlation
   finding (line 40), never as a threshold a reader is told to hit. This flag is the standing guard: any later
   edit that introduces a band **must** take it from the pinned edition, dated + attributed, never timeless.
2. **DORA key wording — "failed-deployment recovery time"** vs DORA's standard **"time to restore service"**
   (sometimes MTTR). The chapter uses "failed-deployment recovery time" as a **paraphrase** of the stability
   key; the exact current key label must be confirmed character-for-character against the pinned DORA edition.
   Paraphrase, not a verbatim quote — flagged for precision, not a fabrication.
3. **SPACE five dimensions** (Satisfaction & well-being / Performance / Activity / Communication & collaboration
   / Efficiency & flow) and the "never Activity alone" rule. Attributed to **Forsgren et al., ACM Queue, 2021**.
   SPACE is **NOT a pinned SOURCE-PIN row** (§7 canon pins *Accelerate* 2018, not the SPACE paper) — a genuine
   §7 canon gap. The five dimensions are stated as an attributed framework, **no figure or verbatim quote** is
   claimed. Confirm the dimension wording at the cited paper; escalate to the SOURCE-PIN re-pin runbook to add
   SPACE as a pinned row.
4. **Tool-specific dashboard / baseline specifics** — SonarQube dashboard + portfolio/aggregate **feature names
   and editions**; the **clean-as-you-code / new-code** wording; SpotBugs baseline/exclude-filter and Checkstyle
   suppression specifics. The chapter **paraphrases** these as mechanisms and routes the detail to the chapters
   that own it (Ch 16/17/19/34); it asserts **no** rule ID, config key, or feature name as a verbatim/pinned
   fact. Covered at the owning chapters' flags — see related flags below. Dated-at-use for the SaaS/feature
   surface.

**Why a flag, not a silent fix.** HARD rule 3 (never invent) + the task's explicit instruction forbid asserting
a DORA performance band. The bands, the exact DORA key label, the SPACE dimension wording, and the SonarQube
feature names cannot be confirmed against the multi-authority pin from inside this gate (no fetched clone for
DORA/SPACE; SonarQube features are a rolling SaaS surface). None is invoked by the green build; none is asserted
as a pinned fact in the body. They are recorded here rather than guessed.

**Status:** `⚠ verify-at-pin` (atoms 1–3) / `⚠ dated-at-use` (atom 4). At `/pin-source`: add **DORA/State of
DevOps** and the **SPACE paper (ACM Queue 2021)** as explicit SOURCE-PIN §7 canon rows, then re-confirm the
DORA bands + key labels + SPACE dimensions against those pinned editions; date-and-attribute any band introduced.

**Related flags:** `83_release_versioning_plugin_versions_unpinned.md` (same DORA-bands precautionary treatment);
`35_sonar_versions_and_defaults_unverified.md`, `27_checkstyle_versions_and_defaults_unverified.md`,
`29_spotbugs_versions_and_defaults_unverified.md` (the tool-specific baseline/dashboard feature surface);
`80_coverage_pr_platforms_saas_dated_at_use.md` (new-code/clean-as-you-code lens, SaaS-dated).

**Filed by:** source-verifier, Chapter 38 (key 85) VERIFY / deferred-marker resolution (2026-06-27).

---

## Cross-reference — Chapter 47 / key 110 (`maturity_model_roadmap`, THE FINAL CHAPTER) also depends on atom 1's framing

**Added 2026-06-27 (source-verifier, key 110 deferred-marker resolution).** The book's closer (FINAL_INDEX
Ch 47, key 110 `110_maturity_model_roadmap`) is built on the **same unconfirmable DORA framing guarded above**:
its single new primary atom is **"DORA deliberately moved from rigid maturity *levels* to *capabilities* and
*continuous improvement*"** (the chapter's stated "single most important framing"). This is the same atom as
**atom 1 / atom's-companion** here — it cannot be diffed character-for-character against the multi-authority pin
(DORA is web-hosted, SOURCE-PIN §5, pinned by date 2026-06-27, no local clone), so it **STAYS `⚠ verify-at-pin`**
in the key-110 draft (front-matter + back-matter `⚠ @pin` markers, both updated 2026-06-27) and is **not promoted
to fact** by the key-110 VERIFY pass.

- **What is confirmed for key 110 (markers removed):** the companion module
  `08-companion-code/110_maturity_model_roadmap/` (`org.acme.maturity`) is **BUILT GREEN** (`mvn -B -Pquality
  verify` BUILD SUCCESS, 12 tests / 0 Checkstyle / 0 SpotBugs, Java 21.0.11 — see the chapter `_EXAMPLE.md`);
  all five displayed snippet markers resolve to real ≤9-line tag regions; the chapter's structural claims
  (overall level = the LOWEST dimension never an average; a stalled-outcome dimension is discounted →
  `RestoreOutcomes`; `Sustain` past the policy threshold; `CULTURE_KNOWLEDGE` first-class) are verified in the
  source. The stale "EXAMPLE-BUILD = N/A / gates manual / SOURCE-PIN 2026-06-20" strings were corrected to the
  built-green / corrected-pin reality.
- **What STAYS flagged for key 110:** the DORA capabilities-over-levels **wording** (atom 1 above). Per HARD
  rule 3 and instruction, **no DORA performance band or statistic is asserted** anywhere in the key-110 body —
  the chapter states the capabilities-over-levels *framing* only, self-flagged, never a numeric cohort threshold.
- **Named maturity-model canon (CMMI / SaaS maturity ladders):** key 110 critiques "maturity levels" generically
  (the Goodhart vanity-ladder argument) and names **no** specific external maturity-model source (no CMMI level
  definitions, no vendor SaaS maturity model) as a pinned fact — so there is no separate named-source atom to
  verify; had one been asserted it would stay `⚠ verify-at-pin` here too. Recorded so a later edit that *does*
  name CMMI/SaaS levels must take it from a pinned source, dated + attributed.

**At `/pin-source`:** the same DORA/SPACE re-pin action above closes the key-110 atom; re-confirm the
capabilities-over-levels wording against the pinned DORA edition and append a VERIFIED/UNVERIFIED line to **both**
this flag and the key-110 `_VERIFY.md`.
