# FLAG — key 34: formatter ↔ JDK version matrix and `--add-exports` unverified

**Atom:** the exact `google-java-format` / `palantir-java-format` version required per JDK (21 / 25), and
the precise `--add-exports` / `--add-opens` JVM args needed to run a given formatter version on newer JDKs
to reach `jdk.compiler` internals.

**Issue:** because google-java-format and palantir-java-format parse Java source via `jdk.compiler`
internals, (a) each new JDK with new language constructs needs a formatter version that understands it, and
(b) newer formatter versions on newer JDKs have historically required `--add-exports`/`--add-opens` JVM
args. This is a **version-specific behaviour delta** (PIPELINE-LEARNINGS key 22 applied to tooling): the
*same* `spotless:check` config can pass on one JDK and fail to even launch on another. One scan note
("google-java-format 1.8 is the minimum supported version for Java 11") is illustrative only.

**Status:** `⚠ verify at pin`. Do not state a formatter-version↔JDK pair or specific `--add-exports` args
until re-confirmed from the pinned formatter README/release notes after `/pin-source`. Any "format Java
21/25 code" recommendation in the draft MUST carry the verified formatter-version↔JDK pair.

**Source:** `github.com/google/google-java-format`, `github.com/palantir/palantir-java-format`
(SOURCE-PIN §2 rows = `TO-PIN`).

**Filed by:** key 34 research (2026-06-15).
