# A11Y gate report — Taming the Inherited Disaster

- **Gate:** A11Y (Step 9c — accessibility / perceivability of the visual layer)
- **Chapter key:** 96 (folds 94) · **Slug:** `96_remediation_playbook_automated_change` · Ch 40 (closes Part XI)
- **Draft under review:** `03-drafts/96_remediation_playbook_automated_change/96_remediation_playbook_automated_change_v1.md`
- **Figures:** `05-figures/96_remediation_playbook_automated_change/fig96_1.{html,png,sources.md}`, `fig96_2.{html,png,sources.md}`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft, Phase 3)

> Soft now, HARD at Step 15 PRODUCTION-PROOF. Any unresolved finding here escalates to a whole-book proof FAIL at assembly.

## Verdict rationale

Both figures carry conformant `## Accessibility` blocks (alt-texts tightened to ≤125 chars; existing full long-descriptions retained; grayscale/contrast/legibility notes added). fig96_1 (seven-step playbook) reads in grayscale (steps by number 1–7 + headings + position; phase bands by labeled span; the two rejection boxes set apart by an "✕" glyph + red dashed border + the word "Never"). fig96_2 (automation engine) reads in grayscale (three bands by header + position; LST-vs-regex by accent left-bar + labeled pills; three niches as named columns; the flow ordered by arrows; `@BeforeTemplate`/`@AfterTemplate` and the GAV coordinates are real selectable text). Both referenced in prose (draft line 41) before they appear (lines 43, 47); both captions match the figure title bars (Figure 40.1 / 40.2). No invented label — every atom traces to the existing source-trace tables.

## Per-figure coverage

| Figure | Alt-text (Y/N, chars) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig96_1 | Y (102) | Y | PASS | PASS (no code in figure) | PASS |
| fig96_2 | Y (122) | Y | PASS | PASS | PASS |

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Pre-existing alt-texts over length (fig96_1 ~310, fig96_2 ~480 chars) | NOTE (resolved) | both sidecars `## Accessibility` | Tightened to ≤125 chars; long-descriptions retained; grayscale/contrast/legibility lines added. No designer action. |

## Blockers

None.

## Gate-specific checks

- [x] Alt-text authored for both figures (≤125 chars; every named label matches the `figNN_x.html` source and traces via the sidecar).
- [x] Long-description authored for both (playbook: seven steps + phase bands + churn×pain note + two rejections; engine: LST-vs-regex band + three-niche map + propose/dispose flow).
- [x] Grayscale-safe confirmed for both (number/header/position + "✕"-glyph rejection boxes + labeled pills; accents are markers only).
- [x] Contrast + code-legibility confirmed (dark ink on light fills; fig96_2 annotation tags + GAVs are real text, not a code screenshot; fig96_1 has no code).
- [x] Caption referenced before each figure; alt-text + caption + prose consistent.

## Learnings & pipeline suggestions

fig96_2 cross-references fig96_1 ("automate the bulk — see Figure 40.2") from inside the playbook; the long-descriptions keep that pointer so a non-visual reader follows the same "the engine sits under step 5" link a sighted reader follows. Confirms that within-chapter figure cross-references belong in the long-description, not only in prose. Appended to `PIPELINE-LEARNINGS.md`.
