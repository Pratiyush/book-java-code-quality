# SCORECARD — Ch 3 "The Java quality toolchain — a map" (key 05)

> Map chapter, main-loop; gates = manual passes. Draft: `05_java_quality_toolchain_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0; no tool crowned; "menu not order"; Alternatives (platform vs best-of-breed) framed as trade-off. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (overlap/noise, build-time, false positives, necessary-not-sufficient) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | every tool → pinned `SOURCE-PIN.md` row; source/bytecode/compile-time → tool docs; FindBugs→SpotBugs guarded. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion = the reusable reference-stack seed; spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | two-axis model + routing table + the source/bytecode/compile ASCII; legible territory. |
| ACCURACY | 8 | tool placements traced; versions pinned; −2 for plugin GAVs deferred to Ch 16/27. |
| UTILITY | 9 | the routing table is a direct "which tool for which problem" lookup a lead will reuse. |
| DEPTH | 7 | deliberately thin (map chapter); routes depth to later chapters by design. |
| READABILITY | 8 | concrete hook (the firehose), tables + ASCII carry rhythm. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; **FLOOR-C COMPILE = PENDING-RUNTIME**. The §6 companion is flagged as the reusable reference-project seed (Ch 46).
