# FLAG — key 29 (SpotBugs): tool versions, GAV coordinates & defaults `⚠ verify at pin`

**Filed:** 2026-06-15 · **Dossier:** `02-research/29_spotbugs/29_spotbugs_RESEARCH.md`

## What is flagged
SpotBugs and its plugins are all `TO-PIN` rows in `SOURCE-PIN.md §2`. The dossier verified rule/pattern,
category, filter-grammar, effort, and bug-rank **identity** from each tool's own docs at the **live line**,
but the following are version-sensitive and must be re-traced after `/pin-source`:

1. **Exact versions / GAV coordinates** (live-line observed, NOT pinned):
   - `com.github.spotbugs:spotbugs` — **4.10.2** (intro/maven doc)
   - `com.github.spotbugs:spotbugs-maven-plugin` — **4.9.8.4** (maven doc)
   - SpotBugs Gradle Plugin — `toolVersion='4.10.2'`; needs Gradle 7.0+
   - `com.github.spotbugs:spotbugs-annotations` — **4.10.2**
   - `com.h3xstream.findsecbugs:findsecbugs-plugin` — **1.14.0** (home/Gradle) / **1.12.0** (Maven example)
   - `com.mebigfatguy.fb-contrib:fb-contrib` — **7.6.11** (Maven Central) / **7.0.3** (README)

2. **Version drift within one tool's own docs (trap):** FindSecBugs home + Gradle doc say **1.14.0** and
   "144 bug patterns / 826 API signatures", while the SpotBugs **Maven** doc example still says **1.12.0**
   and "138 different vulnerability types." fb-contrib README pins **7.0.3** but Maven Central latest is
   **7.6.11** (Jun 2025). Always take a plugin GAV from **Maven Central** at pin, never from an example.

3. **FindSecBugs pattern count** — "144 bug patterns / 826 unique API signatures" verbatim from
   `find-sec-bugs.github.io` (live line); count moves per release → re-confirm at pinned version.

4. **Default rank / effort membership per pattern** — which patterns fire at default effort (`more`) and
   each pattern's rank/priority are version-sensitive. Pattern codes + short descriptions are verified
   verbatim; defaults are not.

5. **Java 21 / 25 bytecode-support status** — the SpotBugs doc labels Java 11+ support **"experimental"**
   and warns about newer JDK bytecode. Confirm no new false positives on the **Java 21 anchor** and
   **Java 25** output at pin; carry the project's own "experimental" label — do NOT upgrade it to "fully
   supports Java 21/25."

## Resolution
Re-trace all of the above against the pinned SpotBugs / FindSecBugs / fb-contrib identifiers after
`/pin-source`; reserve `☑`/"@the pin" for post-pin verification (pre-pin = "live-line, verify at pin").
