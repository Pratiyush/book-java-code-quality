# FLAG — key 73 — security-pipeline CI actions + dynamic-scan tools are SaaS/rolling or unpinned, dated-at-use

- **Severity:** minor-to-material (the CI configuration is illustrative, not run by the Maven build; but the
  actions, tools, and versions it names must not read as timeless or pinned facts).
- **Issue:** Chapter 32 is the CI-INTEGRATION view of the security gate, so its companion config orchestrates
  the security STAGES across the pipeline. Several of the stage tools are SaaS/rolling or have no SOURCE-PIN
  row. `SOURCE-PIN.md` records GitHub Actions as **rolling** ("docs as of 2026-06"), CodeQL as **rolling**
  ("current bundle; pin the bundle tag at use"), Trivy as **pinned** (§4, **0.71.0**), and OWASP
  Dependency-Check as **pinned** (§4, **12.2.2**). **gitleaks and OWASP ZAP have no SOURCE-PIN row at all.**
  The companion config therefore presents every such atom as **dated at use (2026-06)** and uses the
  documented invocation shape, rather than inventing a version or an unverifiable flag.
- **Atoms affected (in `08-companion-code/73_security_in_ci/ci/security-pipeline.yml`):**
  - GitHub Actions `actions/checkout@v4` and `actions/setup-java@v4` are SaaS/rolling, **dated-at-use 2026-06**.
    A `@v4` major tag is mutable; a fully reproducible pin is `@<full-40-char-commit-sha>` plus a release
    comment. **Not done here** because the file is illustrative config and SOURCE-PIN treats GitHub Actions as
    rolling. These are the same tags the repo's own `.github/workflows/ci.yml` and the peer CI modules
    (Chapters 31/33) use.
  - `gitleaks detect --redact` (the secrets stage) names **no version — gitleaks has no SOURCE-PIN row**;
    **dated-at-use 2026-06**. The tool internals (config, rule ids) are Chapter 31's module, not re-taught here.
  - `zap-baseline.py -t "$STAGING_URL"` (the DAST stage) — **OWASP ZAP has no SOURCE-PIN row**;
    **dated-at-use 2026-06**. The DAST stage also needs a live staging deployment, recorded as
    **REPRO PENDING-RUNTIME** (network/environment-gated) — it is described and wired as config, not executed.
  - `trivy fs --severity HIGH,CRITICAL .` (the container/IaC stage) — Trivy **is** a pinned SOURCE-PIN row
    (§4, 0.71.0) and appears only as an illustrative pipeline step.
  - `mvn -B org.owasp:dependency-check-maven:check` (the SCA stage) — OWASP Dependency-Check **is** a pinned
    SOURCE-PIN row (§4, 12.2.2) and appears only as an illustrative pipeline step.
- **What was NOT done (never-invent / SOURCE-PIN floor):** no tool version was invented beyond the dossier +
  SOURCE-PIN; no CodeQL bundle tag, gitleaks version, or OWASP ZAP version was asserted; no GitHub Actions
  starter/quickstart workflow was copied. SaaS/rolling/unpinned atoms are presented as dated-at-use, not
  timeless. The chapter prose names the tools as a multi-tool survey (crowning none) and routes the
  block-vs-warn policy mechanics to the runnable Java gate; no unpinned platform fact is asserted as settled.
- **EXAMPLE-BUILD handling (2026-06-26):** the module builds green via `mvn -B -Pquality verify` (14 tests, 0
  Checkstyle, 0 SpotBugs). The runnable core is the gate POLICY (`org.acme.secgate.SecurityGate`) that
  aggregates the stages' findings into a three-way decision (block / route-to-review / pass); the CI YAML is
  illustrative config carrying the four security-stage snippet tags (`pre-commit-secrets`, `pr-fast-trio`,
  `container-iac-scan`, `staging-dynamic`). The three Java tags (`gate-policy`, `block-vs-warn`,
  `aggregate-and-gate`) realize the policy. The displayed config snippets carry inline comments marking the
  SaaS/unpinned atoms as dated-at-use.
- **Required action:** at `/pin-source` / SOURCE-VERIFY (Step 5) and before any public push (COMPANION-REPO §5),
  decide whether to add gitleaks and OWASP ZAP SOURCE-PIN rows, pin the GitHub Actions to commit-sha digests,
  and re-confirm the Trivy / Dependency-Check / ZAP invocation flags against the tools' own pinned docs.
  Re-confirm Trivy and Dependency-Check if the pipeline is ever actually executed.
- **Status:** OPEN — resolve at `/pin-source` / SOURCE-VERIFY (Step 5); revisit at public-push sign-off.
