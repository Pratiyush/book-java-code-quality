# FLAG — key 40: JDK internal-API access + annotation-processing default/version boundaries unverified

**Raised:** 2026-06-15 (key 40 research)
**Severity:** version-sensitive (verified as framing/identity; exact wording + JDK versions need pin)

## Issue
Several key-40 facts depend on JDK-version-specific behavior that must be re-confirmed against the JDK's own
docs / the tool's own docs at the pin before being asserted as hard boundaries:

1. **`--add-opens` / `--add-exports` package set for `jdk.compiler`** — `com.sun.tools.javac.tree`,
   `com.sun.tools.javac.code`, `com.sun.tools.javac.processing`, etc. Verified as framing from Lombok issues
   #2681 / #3719 ("jdk.compiler does not export com.sun.tools.javac.processing to unnamed modules as of JDK
   16"). Re-confirm the exact package list + the JDK-16 export-change statement verbatim at the pinned
   Lombok/JDK versions.
2. **javac `-proc:*` default change** — `-proc:none` / `-proc:only` / `-proc:full`; the move away from
   implicit run-processors-on-the-classpath. Confirm the exact default and the JDK version it changed against
   the JDK javac docs at pin (do NOT assert a specific JDK number from memory).
3. **`annotationProcessorPaths` mandatory at JDK 23** — verified as framing from Lombok setup +
   corroboration. Re-confirm exact JDK-23 wording and whether it is a javac change or a build-plugin change.
4. **JEP 395 records** — Release field **16** taken from the PIPELINE-LEARNINGS verified JEP list; the JEP
   page 403s WebFetch. Fetch verbatim summary via `curl` + browser UA at draft.

## Action
Re-trace items 1–4 at `/pin-source` (or via curl for the JEP); quote, don't paraphrase, the version-boundary
statements. Reinforces the key-22 "behaviour-delta across the LTS window" discipline.
