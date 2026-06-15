# FLAG — key 31 (NullAway): overhead figures + JSpecify-mode status need byte-exact re-quote at pin

**Status:** ⚠ verify at pin

**Atoms to re-quote byte-exact from the pinned source/paper before asserting:**
- Repo claim: **"the build-time overhead of running NullAway is usually less than 10%"** — re-quote from the
  pinned NullAway README tag.
- FSE'19 paper (`arxiv.org/abs/1907.02127`, Banerjee/Clapp/Sridharan, ESEC/FSE 2019): **"NullAway has
  significantly lower build-time overhead (1.15X) than comparable tools (2.8-5.1X)"**; the per-tool split
  **Eradicate ≈ 2.8×**, **CFNullness (Checker Framework Nullness) ≈ 5.1×**; the production-NPE breakdown
  **64% unchecked third-party libs / 17% suppressions / 17% reflection**, "never due to NullAway's unsound
  assumptions for checked code." Re-quote from the pinned paper PDF.
- JSpecify mode (`-XepOpt:NullAway:JSpecifyMode=true`): wiki status **"work-in-progress and experimental …
  may report false positive warnings"** + the generics gap list (jspecify/jdk annotations, generic-method
  inference, wildcards, generic-class validation) — re-confirm at the pinned wiki tag (fastest-moving surface).

**NEUTRALITY note.** The per-tool comparative figures (Eradicate 2.8×, CFNullness 5.1×) MUST be cited to
NullAway's **own** FSE'19 paper, never to a rival's docs/marketing. The rival's own strongest case + cost
belong to key 32 (annotation ecosystem) / key 37 (cross-tool verdict). Fig 31.2 plots the trade-off axis
with no winner crowned.
