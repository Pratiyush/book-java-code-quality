# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` multi-tool — NEUTRALITY balance. Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 71 — `01-index/CANDIDATE_POOL.md` · **Title:** Secrets detection — gitleaks, trufflehog; keeping secrets out of code & history
- **Part:** VIII · **Tier:** B · **Cmp:** ⚠ · relates 73/82
- **Primary authorities:** gitleaks (`github.com/gitleaks/gitleaks`), TruffleHog (`github.com/trufflesecurity/trufflehog`); pre-commit (key 82); platform secret-scanning (GitHub).

## 1. Core definition & purpose
A hardcoded API key, password, or token in source — or buried in git history — is one of the most common and damaging leaks. Secrets detection scans code, diffs, and history for credential patterns and high-entropy strings, blocking them before they reach a remote (or alerting when they already have). This chapter covers the tools, where to run them (pre-commit + CI + history), and the honest limits (false positives, the "already-leaked" problem).

## 2. Mechanism (the spine)
- **Detection:** regex/rule patterns for known credential formats (AWS keys, tokens, private keys) + **entropy analysis** for random-looking secrets; some tools **verify** a found secret against the provider (TruffleHog's verification) to cut false positives.
- **The tools (each its own case):** **gitleaks** (fast, config-driven rules, pre-commit + CI + history scan); **TruffleHog** (broad detectors + live verification); platform **secret scanning** (GitHub) with push protection. Cited to their own docs; crown none.
- **Where to run (defense in depth):** **pre-commit hook** (key 82) blocks the secret before it's committed (cheapest); **CI gate** (key 73) catches what slipped; **history scan** finds already-committed secrets; **push protection** at the platform.
- **Remediation:** a committed secret is **compromised** — rotate it (removing from history is necessary but not sufficient; assume exposed). History rewrite (`git filter-repo`/BFG) + rotation.
- **Prevention:** externalized config/secret managers (env vars, Vault, cloud secret stores), never in source (ties to config quality / key 06).

## 3. Evidence FOR
- **Cheap, high-value** — a pre-commit secret scan prevents a class of severe incidents for near-zero cost.
- **Defense in depth** — pre-commit + CI + history + push-protection catches secrets at multiple stages.
- **OSS tools** (gitleaks, TruffleHog) are mature and CI-friendly.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **False positives** — entropy detection flags non-secret random strings (UUIDs, hashes, test fixtures); needs allow-listing discipline (key 39) or the team disables it.
- **The "already leaked" problem** — detection ≠ remediation; a found secret must be **rotated**, not just deleted; scanning can't un-leak. State this plainly.
- **Coverage gaps** — novel/obfuscated secret formats, secrets in binaries or non-scanned files, can be missed.
- **Pre-commit is bypassable** (`--no-verify`) — CI/push-protection is the backstop (key 82 local↔CI parity caveat).
- **Not a secrets-management solution** — detection is the safety net; the real fix is never putting secrets in source (a secret manager).

## 5. Current status
gitleaks + TruffleHog are the mainstream OSS choices; platform push-protection (GitHub) is standard; secrets scanning is a baseline CI gate (key 73). *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** a gitleaks config + pre-commit hook + CI step catching a planted fake key; note the rotate-not-just-delete remediation. Config artifact (verified for consistency).
- **Figure:** Fig 71.1 — defense in depth: pre-commit → push protection → CI → history scan, with "found = rotate" at the end. Trace to gitleaks/TruffleHog docs.

## 7. Gap-filling (verification queue)
- ⚠ gitleaks/TruffleHog config + verification feature, versions — verify at pin.
- ⚠ GitHub push-protection specifics — confirm.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | gitleaks | github.com/gitleaks/gitleaks | ☑; ⚠ version |
| 2 | TruffleHog | github.com/trufflesecurity/trufflehog | ☑ verification; ⚠ version |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis) | regex+entropy+verification; pre-commit/CI/history/push-protection; rotate on leak |

---
## Learnings & pipeline suggestions
- The **"found = compromised = rotate"** point is the chapter's honest centerpiece. **Cross-ref:** pre-commit/parity → 82; security gate → 73; config/secrets → 06/55-production; suppression → 39.
