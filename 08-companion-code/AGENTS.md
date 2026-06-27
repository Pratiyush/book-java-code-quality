# AGENTS.md — `08-companion-code/` (the runnable companion modules)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.
> Conventions: `00-strategy/COMPANION-REPO.md`; the shared example domain: `00-strategy/DEMO-CATALOG.md`.

## What this holds

**One build aggregator** (`08-companion-code/pom.xml`) pinning the runtime once, with each chapter's
runnable example as a `<module>` child. The displayed snippet in a chapter is a `// tag::`/`// end::`
region inside the compiled file here — code and prose stay in lockstep.

```
08-companion-code/
  pom.xml                       the parent reactor (pins JDK + the SOURCE-PIN test libs)
  NN_slug/                      one module per chapter (sets <parent>, no version literal)
  storefront-checkout/          the shared flagship microservice
  capstones/                    the enterprise capstone apps + CAPSTONE-STATUS.json
```

## File-naming / convention

`NN_slug` matches the chapter's frozen key. A module joins the reactor's `<modules>` list **only after**
a green build **and** a passing CODE-REVIEW. Build output (`target/`) is gitignored.

## Who writes / reads it

- Written by the `example-builder` (Step 4b EXAMPLE-BUILD) and `code-reviewer` (FLOOR-C second half).
- The build is the FLOOR-C compile floor: `mvn -B -Pquality verify` → BUILD SUCCESS with 0 Checkstyle /
  0 SpotBugs. The `_EXAMPLE.md` / `_CODEREVIEW.md` reports land in `03-drafts/NN_slug/`, where
  `status.py` reads them as FLOOR-C evidence.
- Pure-concept chapters (no module) are an honest **N/A** — recorded, not faked.

## Hard rules

- Never quote or close-paraphrase the `_ref/` works into companion code.
- The NEUTRALITY blocklist applies to **source, comments, and justification strings**, not just prose
  (a banned word inside a displayed `@SuppressFBWarnings` reason is still a FLOOR-A failure).
- Public push is gated (legal / public-repo sign-off) — see `COMPANION-REPO.md`.

> Note: a stray nested `08-companion-code/08-companion-code/` path exists from an earlier copy; the
> canonical capstones live at `08-companion-code/capstones/` (the one with `CAPSTONE-STATUS.json`).
