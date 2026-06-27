# AGENTS.md — `05-figures/` (the designed figures)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.

## What this holds

One folder per chapter holding its load-bearing figures. Designed diagrams are **authored as HTML and
rendered to a cropped PNG — never image-generated** (GUIDELINES §8).

```
05-figures/NN_slug/
  figNN_x.html          the authored diagram (HTML/CSS)
  figNN_x.png           the rendered, cropped image that ships
  figNN_x.sources.md    the source paths at the pin for every depicted claim (sidecar)
05-figures/_assets/
  render.mjs            the HTML→PNG renderer
  figures.css           shared figure styling
  package.json …        the render toolchain (node_modules, lockfile)
```

## File-naming / convention

`figNN_x` — `NN` is the frozen chapter key, `x` is the figure index within the chapter. Each figure
follows the per-chapter image budget (load-bearing, not decorative; zero is the exception). Every
`.png` must have its `.html` source and its `.sources.md` sidecar.

## Who writes / reads it

- Written by the `figure-designer` agent (Step 9): design the HTML, render via `_assets/render.mjs`,
  cite sources in the sidecar.
- Read by `status.py` (a rendered `figNN_*.png` upgrades the chapter's `figure` gate to 🟢) and by
  `review_figures.py` (the 20-parameter figure review → `10-logs/figures.html`).

## After any figure change

Re-render the PNG, refresh the sidecar, then run `python3 .claude/scripts/review_figures.py` and
`python3 .claude/scripts/status.py`. A depicted claim with no cited source is a gate failure.
