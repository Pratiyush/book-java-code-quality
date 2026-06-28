# FLAG (key 39) — SonarQube "Won't Fix" → "Accepted" rename + `//NOSONAR` scoping unverified

**Type:** `✅ RESOLVED` (was `⚠ UNVERIFIED` — version-sensitive terminology/behavior)
**Chapter key:** 39 — Living with findings (false positives, suppression, baselines, ratcheting)
**Filed:** 2026-06-15
**Resolved:** 2026-06-28 (web-verify against SonarSource's own versioned docs)

## RESOLUTION (2026-06-28 — web-verify, docs.sonarsource.com; SaaS/rolling → dated-at-use)
1. **Issue-status relabel — CONFIRMED.** The **Accepted** status replaced the older **Won't Fix**
   label in **SonarQube Server 10.4** (Feb 2024): "Developers can now mark an issue as 'accepted'
   instead of 'won't fix', including clear messaging explaining how accepting the issue contributes
   to technical debt" — `https://www.sonarsource.com/products/sonarqube/whats-new/sonarqube-10-4/`.
   As of the pin (**Server 2026.1 LTA**), the managing-issues doc uses only **Open / Accepted /
   False positive** — "Won't Fix" no longer appears — and states verbatim: "the issue status is
   then marked as **Accepted**" and "SonarQube Server ignores accepted issues in the quality reports
   and ratings of the code" (likewise false positives) —
   `https://docs.sonarsource.com/sonarqube-server/2026.1/user-guide/issues/managing`.
   Stated dated-at-use in the draft ("as of Server 2026.1 LTA; Accepted replaced Won't Fix in 10.4").
2. **`//NOSONAR` scope — CONFIRMED rule-blind, no scoped form.** `//NOSONAR` at end of line suppresses
   **all** issues on that line, "now and in the future" (regardless of rule); SonarSource documents no
   scoped `//NOSONAR <ruleKey>` form — suppression is line-level and rule-blind
   (`docs.sonarsource.com/sonarqube-server/10.6/user-guide/issues/managing`; pin 2026.1 phrases it as
   in-line line suppression). Draft now states "rule-blind ... no scoped form."
3. **FP/Accepted exclusion — CONFIRMED at pin.** Accepted and False-positive issues are ignored in the
   quality reports and ratings (verbatim above). The PR-analysis *persistence-after-merge / edition*
   nuance was NOT separately re-fetched this pass and is **not asserted** in the draft (the draft makes
   no edition-gated persistence claim), so nothing is left hanging — no UNVERIFIED claim remains in Ch19
   prose from this flag.

## Draft actions taken (03-drafts/39_managing_findings/39_managing_findings_v1.md)
- Body Lever-1 bullet + back-matter SonarQube source line: rewritten with dated-at-use + both doc URLs.
- Header dossier comment: rename/`//NOSONAR` atoms moved from `⚠ verify-at-pin` to a RESOLVED note.
- All `@pin` / "rename + version boundary @pin" markers tied to these two atoms removed.

## What is unverified
1. **Issue-status relabel.** SonarQube's manual issue resolution was historically **"Won't Fix"**; newer lines
   present it as **"Accepted"**. Both labels appear across versions in the official docs and Sonar community.
   The exact label *and the server version at which it applies* must be confirmed against the pinned SonarQube
   version (`SOURCE-PIN.md` §2, currently `TO-PIN`). Do NOT assert one name as current without the pin.
2. **`//NOSONAR` scope.** Verified that `//NOSONAR` suppresses issues on its line; whether a *scoped*
   `//NOSONAR <ruleKey>` form is supported is version-sensitive → `⚠ verify at pin`.
3. **FP/Accepted persistence after merge** (PR analysis retains the status) is documented as a Developer-Edition+
   behavior — confirm edition + version at pin.

## Verified (mechanism identity, from docs)
- **False Positive** and **Accepted** transitions exist; both are **excluded from ratings and quality reports**
  (verbatim: SonarQube "ignores both accepted issues and false positive issues"). `docs.sonarsource.com`.

## Action at `/pin-source` — DONE (2026-06-28)
Label + `//NOSONAR` scoping re-traced against the pinned server line (2026.1 LTA) and the rename source
(10.4 whats-new); draft updated; flag RESOLVED. SonarQube is SaaS/rolling, so the claim is stated
dated-at-use ("as of Server 2026.1 LTA") rather than as a timeless fact — re-confirm only if the pin
moves to a server line that re-labels the status again.

## Precedent
Same class as the ISO-25010:2023 edition trap (key 01) and the Jakarta Validation rename (key 18): assert
version/edition-specific labels only from the source's own text at the pin.
