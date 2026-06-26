# FLAG — key 70 (folds 71) — SAST/secrets tools + CI actions in the companion config are SaaS/rolling or unpinned, dated-at-use

- **Severity:** minor-to-material (the tool configuration is illustrative, not run by the Maven build; but the
  tools, schemas, and versions it names must not read as timeless or pinned facts).
- **Issue:** the chapter's SAST and secrets tools are a mix of pinned, rolling/SaaS, and unpinned authorities.
  `SOURCE-PIN.md` records Semgrep as pinned (§2, **Semgrep 1.163.0**), CodeQL as **rolling** ("current bundle;
  pin the bundle tag at use"), GitHub Actions as **rolling** ("docs as of 2026-06"), and pre-commit as pinned
  (§5, latest 2026-04). **gitleaks and TruffleHog have no SOURCE-PIN row at all** — the dossier (key 70/71)
  marks "tool versions/rule-IDs/pack-names; gitleaks/TruffleHog config + verification feature; GitHub
  push-protection specifics" as `⚠ verify-at-pin`. The companion config therefore presents every such atom as
  **dated at use (2026-06)** and uses the documented config schema, rather than inventing a version, rule-id,
  or unverifiable config key.
- **Atoms affected (in the companion module `08-companion-code/70_sast_secrets_detection/`):**
  - `ci/sast-scan.yml` — GitHub Actions `actions/checkout@v4`, `actions/setup-java@v4`, and
    `github/codeql-action/analyze@v3` are SaaS/rolling, **dated-at-use 2026-06**. A `@v3`/`@v4` major tag is
    mutable; a fully reproducible pin is `@<full-40-char-commit-sha>` plus a release comment. **Not done here**
    because the file is illustrative config and SOURCE-PIN treats GitHub Actions / CodeQL as rolling. The
    `semgrep` and `gitleaks` CLI invocations name no version. OWASP Dependency-Check `12.2.2` (named in the SCA
    stage) **is** a pinned SOURCE-PIN row (§4) and appears only as an illustrative pipeline step.
  - `config/semgrep/java-injection.yml` — Semgrep is pinned (§2, 1.163.0), but the rule pack/engine are
    continuously released; the rule uses the documented `rules:`/`id`/`languages`/`severity`/`message`/
    `patterns` schema and is **dated-at-use 2026-06**. The rule id `java-sql-string-concat` is the book's own
    custom-rule id (original-for-this-book), not an upstream registry pack id; no upstream pack name is asserted.
  - `.gitleaks.toml` — **gitleaks has no pinned version** in SOURCE-PIN. The config uses the documented
    `[[rules]]` (`id`/`description`/`regex`) and `[allowlist]` (`paths`/`regexes`) schema and is
    **dated-at-use 2026-06**. No version is asserted. The AWS access-key-id regex shape
    `(A3T[A-Z0-9]|AKIA|ASIA)[A-Z0-9]{16}` is the publicly-documented AWS key-id prefix/length shape, used to
    teach the pattern a scan flags; it is verified at the pin if/when the tool config is re-confirmed.
  - `.pre-commit-config.yaml` — pre-commit is pinned (§5), but the gitleaks hook's `rev:` pins the (unpinned)
    gitleaks release, so the `rev` is the literal placeholder `VERSION_PINNED_AT_ADOPTION` with a
    **dated-at-use 2026-06** comment, NOT an invented version tag.
- **Planted fake key (never-invent / safety):** the secrets-detection fixture uses AWS's published,
  **non-functional EXAMPLE** access key id `AKIAIOSFODNN7EXAMPLE` (the placeholder that appears throughout AWS
  documentation), placed only under `src/test` and allow-listed in `.gitleaks.toml`. No real, live, or
  realistic-random credential was planted; the fixture demonstrates the detected pattern shape without shipping
  a secret. The honest edge is stated in code and prose: a REAL leaked key would be compromised the instant it
  pushed — detection is not remediation, and the only fix is to rotate it.
- **What was NOT done (never-invent / SOURCE-PIN floor):** no tool version was invented beyond the dossier +
  SOURCE-PIN; no CodeQL bundle tag, gitleaks/TruffleHog version, or GitHub push-protection specific was
  asserted; no upstream Semgrep registry pack id or upstream sample config was copied. SaaS/rolling/unpinned
  atoms are presented as dated-at-use, not timeless. The chapter prose names the tools as a multi-tool survey
  (crowning none) and routes the security-gate fail/warn policy to Ch 32 and platform specifics onward, so no
  unpinned platform fact is asserted as settled in the prose.
- **EXAMPLE-BUILD handling (2026-06-26):** the module builds green via `mvn -B -Pquality verify` (9 tests, 0
  Checkstyle, 0 SpotBugs). The one SpotBugs finding on the injection counter-example
  (`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`, High) is verified load-bearing (BugInstance size 1 with the
  exclude filter empty, 0 with the narrow per-class/per-method/per-bug suppression) and is the true SAST
  finding the chapter teaches. The 4 config snippet tags (`semgrep-rule`, `sast-ci-step`, `gitleaks-config`,
  `precommit-hook`) display only the illustrative configuration; the 2 Java tags (`sql-sink`, `fail-closed`)
  realize the runnable core. The displayed config snippets carry inline comments marking the SaaS/unpinned
  atoms as dated-at-use.
- **Required action:** at `/pin-source` / SOURCE-VERIFY (Step 5) and before any public push (COMPANION-REPO §5),
  re-confirm the Semgrep rule schema/version, decide whether to add gitleaks/TruffleHog SOURCE-PIN rows and pin
  the gitleaks `rev` + the GitHub Actions / CodeQL bundle to commit-sha digests, and re-confirm the AWS key-id
  regex shape and the gitleaks/Semgrep config keys against the tools' own pinned docs. Re-confirm
  Dependency-Check if the pipeline is ever actually executed.
- **Status:** OPEN — resolve at `/pin-source` / SOURCE-VERIFY (Step 5); revisit at public-push sign-off.
