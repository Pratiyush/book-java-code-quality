# FLAG (key 39) — SonarQube "Won't Fix" → "Accepted" rename + `//NOSONAR` scoping unverified

**Type:** `⚠ UNVERIFIED` (version-sensitive terminology/behavior — must trace to pinned server version)
**Chapter key:** 39 — Living with findings (false positives, suppression, baselines, ratcheting)
**Filed:** 2026-06-15

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

## Action at `/pin-source`
Re-trace the label, the `//NOSONAR` scoping form, and the persistence behavior against the pinned SonarQube
server version; update §2.2 / §2.7 / §5 of the dossier and remove this flag if resolved.

## Precedent
Same class as the ISO-25010:2023 edition trap (key 01) and the Jakarta Validation rename (key 18): assert
version/edition-specific labels only from the source's own text at the pin.
