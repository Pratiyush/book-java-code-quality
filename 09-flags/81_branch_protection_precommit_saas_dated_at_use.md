# FLAG — key 81 (folds 82) — branch-protection + pre-commit config surfaces are SaaS/rolling, dated-at-use

- **Severity:** minor-to-material (the YAML files are illustrative configuration, not run by the build;
  but the setting names and hook `rev:` tags they carry must not read as timeless, version-pinned facts).
- **Issue:** the companion config for Chapter 35 names two hosted/SaaS surfaces that `SOURCE-PIN.md` §5
  records as rolling, so their identifiers are **dated at use (2026-06)**, not pinned to an immutable
  version, and their behaviour/field names can change under a moving release:
  1. **GitHub branch protection / rulesets** — `SOURCE-PIN.md` §5: "GitHub Actions — docs as of 2026-06
     (rolling SaaS)"; the branch-protection / rulesets surface is the same rolling GitHub platform.
  2. **The pre-commit framework + its hook repos** — `SOURCE-PIN.md` §5 pins "pre-commit latest (2026-04
     release)", but the framework's hook `rev:` tags point at external repos pinned by a moving tag.
- **Atoms affected:**
  - In `08-companion-code/81_branch_protection_precommit_parity/config/branch-protection/ruleset.yml`:
    the setting names `required_status_checks` (`strict`, `checks`), `required_pull_request_reviews`
    (`required_approving_review_count`, `dismiss_stale_reviews`), `required_linear_history`,
    `allow_force_pushes`, `allow_deletions`. These are documented GitHub branch-protection settings as of
    2026-06; the exact JSON/YAML payload a given API/rulesets version expects can differ. The file is
    illustrative, version-controlled config — **not** an asserted API schema. The required check NAMES
    (`build-and-lint`, `test-and-coverage`, `static-and-security`, `quality-gate`) are this book's
    illustrative CI job names (consistent with the peer Chapter 33 `ci/quality-gates.yml`), not GitHub
    facts.
  - In the chapter **prose** (`03-drafts/81_branch_protection_precommit_parity/.../v1.md`): the
    statement that **GitHub offers a merge queue** ("How it works" §, dated in-prose "as of mid-2026") is a
    rolling hosted-platform feature, not a timeless fact — re-confirm availability/behaviour at adoption.
    The **trunk-based development** capability is attributed to the pinned *Accelerate* (2018, SOURCE-PIN §7)
    + the **2025 DORA report** (`dora.dev`, §5); the prose **paraphrases** the capability and crowns no
    branching model — the exact `dora.dev` wording is a rolling surface, so it is paraphrased, never quoted.
  - In `08-companion-code/81_branch_protection_precommit_parity/.pre-commit-config.yaml`: the gitleaks
    hook `rev:` is a **`VERSION_PINNED_AT_ADOPTION` placeholder** (dated-at-use 2026-06), not an asserted
    gitleaks version — gitleaks itself has no pinned SOURCE-PIN row (the §2 SAST rows mark scanner
    versions for re-confirmation). The `local` hooks (`spotless-format-check`, `checkstyle-fast`) invoke
    the build wrapper, so they inherit this module's pinned Spotless/Checkstyle (no new version asserted).
- **What was NOT done (never-invent / SOURCE-PIN floor):** no GitHub ruleset API version, no gitleaks
  release, and no pre-commit hook `rev:` was invented or asserted as a pinned fact; every SaaS surface is
  presented as dated-at-use, not timeless. No setting/field was invented beyond the dossier + the
  documented GitHub branch-protection surface as of 2026-06. The chapter prose names trunk-based
  development as DORA-evidenced (`dora.dev`, SOURCE-PIN §5: 2025 DORA report) for *delivery performance*
  and crowns no branching model, and routes merge-queue/platform specifics as direction (Chapter 34
  keys 77/78), so no platform-syntax fact is asserted as settled in the prose itself.
- **EXAMPLE-BUILD handling (2026-06-27):** the module builds green via `mvn -B -Pquality verify`
  (JDK 21.0.11; 10 tests, 0 Checkstyle, 0 SpotBugs; warning-clean). Both YAML files parse as valid YAML.
  The 5 snippet tags display the illustrative config (`required-checks`, `linear-history-and-review`,
  `precommit-fast-hooks`) and the runnable parity core (`parity-assertion`, `feedback-not-enforcement`).
  The displayed config snippets carry inline comments marking the SaaS surfaces as dated-at-use; the
  pure-JSON option was set aside because JSON cannot carry the `# tag::` comment markers the anti-drift
  machinery needs — the ruleset is therefore documented YAML (the same settings, valid for tagging).
- **Required action:** at `/pin-source` / SOURCE-VERIFY (Step 5) and before any public push
  (`COMPANION-REPO.md` §5), (1) re-confirm the GitHub branch-protection / rulesets field names against the
  platform docs at adoption, or record in the README/`EXAMPLES-GUIDE` that git-platform config in this
  book is dated-at-use per SOURCE-PIN §5; (2) replace the gitleaks `rev:` placeholder with the confirmed
  release (or a commit-sha digest, the reproducible-CI best practice) and re-confirm the pre-commit
  framework version.
- **Status:** OPEN — resolve at `/pin-source` / SOURCE-VERIFY (Step 5); revisit at public-push sign-off.
