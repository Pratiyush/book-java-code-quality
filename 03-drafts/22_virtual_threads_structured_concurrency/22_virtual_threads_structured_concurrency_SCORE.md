# SCORECARD — Ch 14 "Virtual threads, structured concurrency & concurrency testing" (key 22 + 24 + 25)

> Part III CLOSER (Part III = Ch 13-14), three merged dossiers (virtual threads/structured concurrency +
> concurrency testing/JCStress + static concurrency detection). A-tier. Main-loop; gates = manual passes.
> GA-vs-preview-discipline + version-bound-advice + prove-a-bug-exists + approximation-of-a-spec-property
> shapes. Draft: `22_virtual_threads_structured_concurrency_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); platform-pools-vs-virtual-threads framed as different shapes not a ranking; the three static tools (Error Prone/SpotBugs/Checker FW) = different proxies, none crowned (→ Ch 17); JCStress-vs-Lincheck (sampling vs model-checking) neutral; reactive/async + ThreadLocal/ScopedValue as trade-offs. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (pinning version-specific; don't-pool/no-CPU-benefit; thread-local scale; VTs≠safe; structured concurrency preview+churning API; JCStress probabilistic/experimental/hardware-dependent; deterministic forces-one-interleaving; sleep anti-pattern; static checks proxies-not-property) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | VT GA@21 / structured-concurrency preview@21-25 / scoped-values GA@25 / JEP 491 @24 all by JEP Release field; pinning + scheduler verbatim JEP 444; JCStress Expect/Mode + experimental/probabilistic verbatim; static tool IDs each to own tool; JLS §§/flag names/tool versions/SpotBugs ranks/Sonar titles/Lincheck carried verify-at-pin; @GuardedBy 4-package trap stated. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (VT fan-out + pinning trap/fix; @GuardedBy build-fail; JCStress reordering; latch regression; preview branch excluded) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the readable-blocking-code-then-pinning-stall hook + the GA-vs-preview + version-bound CONCEPT callouts + the static-tool comparison table make a 3-dossier merge coherent; "cheap threads, same rules" thesis threads throughout. |
| ACCURACY | 9 | JEP Release/Status precise (VT GA@21, SC preview@21-25, scoped values GA@25, JEP 491 @24); pinning/scheduler/JCStress verbatim; −1 for JLS §§, flag/property names, tool versions/SpotBugs ranks/Sonar titles, JCStress-version, Lincheck carried verify-at-pin. |
| UTILITY | 8 | gives a senior reader the VT idiom (one-per-task, no-pool, version-bound lock advice), the verify playbook (JCStress vs deterministic vs anti-pattern), and the three static approaches with their proxies; per-shape When-to-use directly actionable. |
| DEPTH | 9 | merges the modern execution model + dynamic verification + static detection into one "cheap threads, same rules / so verify them" arc; the version-bound pinning, the prove-presence-not-absence framing, and the approximation-of-a-spec-property frame are senior-staff material. |
| READABILITY | 8 | strong concrete hook, three CONCEPT callouts + one AHEAD-OF-PIN, two comparison tables (Expect taxonomy, static approaches), the @GuardedBy 4-package trap; appropriately long (4123w) without a grey wall. |

**Aggregate 43/50**, none < 6 (ties Ch 13 as highest — fitting for the A-tier Part III closer). Floors A/B/C-source ✅;
FLOOR-C COMPILE = PENDING-RUNTIME. **CLOSES PART III (Ch 13-14); Parts I-III complete.** Reuses
version-bound-advice + approximation-of-a-spec-property + prove-a-bug-exists shapes; new GA-vs-preview-discipline.
Hands off to Part IV (Ch 15, how static analysis works) — the detectors used here become the subject.
