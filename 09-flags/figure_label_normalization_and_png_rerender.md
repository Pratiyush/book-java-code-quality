# FLAG — figure-label consistency (`Fig` → `Figure`) + deferred PNG re-render

**Found:** 2026-06-27 (anti-drift cross-ref sweep). **Status:** TEXT RESOLVED · PNG re-render DEFERRED.

## What was wrong
Figure labels were split book-wide: rendered PNGs show `Fig N.M`; markdown captions were 51 `![Fig` /
7 `![Figure`; prose references were 56 `Fig N.M` / 69 `Figure N.M`. `check_crossrefs.sh` recognizes only
the spelled-out `Figure N.M` as a figure definition/reference, so the `Fig` captions were not seen as
definitions and **41 prose references dangled** in the clean manuscript set.

## What was done (TEXT — resolved)
Normalized every figure mention in the 47 `03-drafts/*/*_v1.md` to the spelled-out **`Figure N.M`**
(`perl -i -pe 's/\bFig (\d+\.\d+)/Figure $1/g'`): 195 mentions now consistent; 0 `Fig N.M` / 0 `![Fig`
remain. `check_snippets` still PASS on all module chapters; `pre-final/` rebuilt. **Cross-ref dangling in
the clean manuscript: 41 → 0** (985 refs checked).

## Deferred (PNG re-render — cosmetic)
The 67 rendered figures (`05-figures/*/figNN_x.png`) and their HTML `fig-title` still display `Fig N.M`,
so a reader sees prose "Figure 1.1" pointing at an image titled "Fig 1.1" — a minor visible mismatch.
Fold the `fig-title` `Fig`→`Figure` change + re-render into the **final figure pass** (the same step that
would address any other figure tweaks from the external review), not a standalone 67-PNG re-render now.
The figure-number ordinals are unchanged, so this is label-text only.

## Residual (non-blocking)
`check_crossrefs.sh` still reports ~189 **warnings** (per-chapter figure-ordinal notes — e.g. a chapter
defining Figure N.1/N.2 with non-contiguous reference order). These are WARN, not FAIL; revisit during the
post-external-review CLARITY/PROOF pass.
