# FLAG — key 36: IDE authorities not in SOURCE-PIN

**Severity:** material (the chapter's primary authorities are unpinned).

**Issue.** Key 36 ("IDE inspections as the first line — IntelliJ IDEA, Eclipse, save-actions") draws its
primary facts from **IntelliJ IDEA** (JetBrains help), **Eclipse / Eclipse JDT** (`help.eclipse.org` +
`github.com/eclipse-jdt`), and **JetBrains Qodana** (`jetbrains.com/help/qodana/` + `github.com/JetBrains/qodana-cli`).
None has a row in `SOURCE-PIN.md` §2, which currently pins only analyzers/linters/formatters — not the IDEs
themselves. This is the same gap shape as `09-flags/24_jcstress_not_pinned.md`.

**Impact.** Feature/option **identity** (severity set, Save-Action option names, CLI flags, profile locations)
is verified from each vendor's own current docs, but with no pin there is no frozen version to re-trace against;
all version/default facts are `⚠ verify at pin`.

**Proposed fix.** Add an "IDEs / IDE-platform analyzers" sub-group to `SOURCE-PIN.md` §2:
- IntelliJ IDEA — `jetbrains.com/help/idea/` — latest stable (TO-PIN).
- Eclipse + Eclipse JDT — `help.eclipse.org` + `github.com/eclipse-jdt` — latest stable (TO-PIN).
- JetBrains Qodana — `jetbrains.com/help/qodana/` + `github.com/JetBrains/qodana-cli` — latest stable (TO-PIN).

Qodana is also relevant to CI keys 75–80 and overlaps key 35.

**Status:** open — resolve at `/pin-source`.
