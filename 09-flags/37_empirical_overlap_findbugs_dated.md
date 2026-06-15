# FLAG — key 37: empirical overlap study cites FindBugs (DATED) + per-tool numbers unverified

**Severity:** dating discipline (cite qualitatively + caveat; do not promote superseded-tool numbers).

**Source.** Lenarduzzi, Lujan, Saarimäki, Palomba — *"A Critical Comparison on Six Static Analysis Tools:
Detection, Agreement, and Precision"* — `arxiv.org/abs/2101.08832`. Tools studied: Better Code Hub,
Checkstyle, Coverity Scan, **FindBugs**, PMD, SonarQube. 47 Java projects.

**Verified (abstract, verbatim):** "little to no agreement among the tools and a low degree of precision."

**Why flagged.**
1. The study includes **FindBugs**, which `SOURCE-PIN.md` records as **dead** (→ **SpotBugs**). The study is
   valid for the *qualitative low-agreement* finding (the layering rationale of key 37), but its per-tool
   results must NOT be presented as current SpotBugs/Sonar benchmarks. Every cite must carry the dating caveat.
2. Exact per-tool **agreement/precision NUMBERS** live in the full paper; the PDF (`/pdf/2101.08832`) returned
   FlateDecode binary that WebFetch could not decode. Only the qualitative finding is verified.

**Action:** if the draft uses a specific number, fetch the full paper at draft (local `pdftotext` or a readable
mirror) and quote exactly, with the FindBugs-dating caveat. Otherwise use the qualitative finding only.
