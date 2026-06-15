# FLAG — key 34: Spotless `check`-goal default Maven phase unverified

**Atom:** the default Maven lifecycle phase the `spotless-maven-plugin` `check` goal binds to.

**Issue:** two reads of the Spotless docs in the key-34 research scan returned **different** answers — one
(repo README level) read as the **`check`** phase, another (plugin-maven level) read as the **`verify`**
phase. Only one can be correct at a given pinned version; both may differ across plugin versions.

**Status:** `⚠ verify at pin`. Do not assert a default phase in the draft until re-confirmed against the
pinned `com.diffplug.spotless:spotless-maven-plugin` docs/source after `/pin-source`. Until then, describe
it as "bound into the build lifecycle by default (exact phase verify at pin)."

**Source:** `github.com/diffplug/spotless` (+ `plugin-maven/` docs). SOURCE-PIN §2 row = `TO-PIN`.

**Filed by:** key 34 research (2026-06-15).
