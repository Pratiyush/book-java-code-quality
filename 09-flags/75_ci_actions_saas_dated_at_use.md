# FLAG — key 75 — CI actions/tools in the pipeline config are SaaS/rolling, dated-at-use (not version-pinned)

- **Severity:** minor-to-material (the YAML is illustrative configuration, not run by the build; but the
  versions it names must not read as timeless facts).
- **Issue:** the companion CI configuration `08-companion-code/75_ci_pipeline_quality_gates/ci/quality-gates.yml`
  references hosted/SaaS GitHub Actions that `SOURCE-PIN.md` §5 records as **rolling** ("GitHub Actions —
  docs as of 2026-06 (rolling SaaS)"). These are **dated at use (2026-06)**, not pinned to an immutable
  identifier, and a CI action's behaviour can change under a moving major tag.
- **Atoms affected (in `ci/quality-gates.yml`):**
  - `actions/checkout@v4`, `actions/setup-java@v4`, `actions/cache@v4` — major-version tags only. These are
    the same tags the repo's own `.github/workflows/ci.yml` already uses (consistency, not a new assertion).
    A `@v4` tag is mutable; a fully reproducible pin is `@<full-40-char-commit-sha>` plus a comment recording
    the release. **Not done here** because the file is illustrative config and SOURCE-PIN treats GitHub
    Actions as dated/rolling, not pinned.
  - The Maven-invoked tools named in stage comments — OWASP Dependency-Check `12.2.2`, PITest `1.25.3`,
    JaCoCo `0.8.16` — **are** pinned SOURCE-PIN rows (§4/§3). They appear only in comments/`run:` goal
    coordinates as illustrative pipeline steps; the module's actual `-Pquality` gate pins Checkstyle/SpotBugs
    via the pom. No tool version in the YAML is asserted beyond its SOURCE-PIN row or the dossier.
  - The `cron` schedule, `ubuntu-latest` runner label, and `mvn -T 1C` flag are GitHub-Actions / Maven syntax,
    dated 2026-06, not version-bearing claims.
- **What was NOT done (never-invent / SOURCE-PIN floor):** no action was pinned to a sha the agent could not
  verify against the pin; no action/tool version was invented beyond the dossier + SOURCE-PIN. SaaS actions
  are presented as dated-at-use, not timeless. The chapter prose names CI platforms (GitHub Actions / GitLab /
  Jenkins) as direction and routes platform specifics to Ch 34 (keys 77/78), so no platform-syntax fact is
  asserted as settled in the prose itself.
- **EXAMPLE-BUILD handling (2026-06-26):** the module builds green via `mvn -B -Pquality verify` (11 tests, 0
  Checkstyle, 0 SpotBugs). The 4 YAML snippet tags (`pr-vs-main-split`, `cache`, `test-coverage-gate`,
  `static-security-gate`) display only the illustrative stages; the 3 Java tags realize the runnable gate
  policy. The displayed CI snippets carry inline comments marking the SaaS actions as dated-at-use.
- **Required action:** at `/pin-source` / SOURCE-VERIFY (Step 5) and before any public push (COMPANION-REPO §5),
  decide whether to pin the three GitHub Actions to commit-sha digests (CI-reproducibility best practice) or to
  keep the dated `@v4` tags and record in the README/`EXAMPLES-GUIDE` that CI-platform actions in this book are
  dated-at-use per SOURCE-PIN §5. Re-confirm the Dependency-Check / PITest / JaCoCo coordinates against their
  pins if the pipeline is ever actually executed.
- **Status:** OPEN — resolve at `/pin-source` / SOURCE-VERIFY (Step 5); revisit at public-push sign-off.
