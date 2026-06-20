# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Canon: Fowler strangler-fig (2004). Versions `⚠ verify at pin`.
> Neutral; honest-limitations met.

---

## Topic
- **Key:** 93 — `01-index/CANDIDATE_POOL.md` · **Title:** Strangler-fig & incremental modernization
- **Part:** XI · **Tier:** B · relates 92/95/96
- **Primary authorities:** Martin Fowler, "StranglerFigApplication" (bliki, 2004); modernization practice (AWS/Shopify case write-ups); feature flags (key 81).

## 1. Core definition & purpose
When a system is too large/risky to refactor in place (key 91) but too valuable to rewrite big-bang, the **strangler fig pattern** offers a middle path: grow a new implementation *around* the old one, incrementally route functionality to the new code, and retire the old once it's fully replaced. Named by Fowler (2004) after the strangler fig that grows over a host tree. This chapter covers incremental modernization as the low-risk alternative to the rewrite — the architecture-scale companion to refactoring (key 91) and version migration (key 95).

## 2. Mechanism (the spine)
- **The pattern (Fowler 2004):** put a façade/interception layer in front of the old system; build new functionality behind it; redirect calls from old → new piece by piece; when nothing routes to the old system, delete it. The system delivers value throughout (no big-bang cutover).
- **Mechanics in Java/services:** a routing layer (gateway/proxy/adapter) that decides old-vs-new per request; **feature flags** (key 81) to toggle/roll back each migrated slice; characterization tests (key 92) to verify the new path matches the old behavior; contract tests (key 50) at the seam.
- **Granularity:** strangle by feature/endpoint/module — small enough to ship + verify + roll back independently.
- **vs alternatives:** big-bang rewrite (high risk, often fails — Fowler's motivation); in-place refactoring (key 91, when the code *can* be evolved in place). Strangler is for "replace gradually while running."
- **Modernization scope:** can wrap language/framework upgrades (key 95), architecture changes (monolith→modular, keys 55/57), or whole-subsystem replacement.

## 3. Evidence FOR
- **De-risks large change** — incremental, always-shippable, each slice independently reversible (feature flags, key 81) — the opposite of the failure-prone big-bang.
- **Steady value delivery** — new functionality ships during the migration, not after a multi-year freeze.
- **Behavior-verified at the seam** — characterization (key 92) + contract tests (key 50) catch divergence between old and new.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Long-lived dual-running cost** — you maintain old + new + the routing layer simultaneously, sometimes for a long time; the migration can stall half-done (the worst state — two systems forever). Needs commitment to finish.
- **The façade/routing layer is itself complexity + risk** — and shared state/data between old and new is the hard part (DB migration, dual-writes, consistency).
- **Not always cheaper** — for a small system, a clean rewrite may genuinely be simpler; strangler shines at *large/high-risk* scale (when-NOT-to-use: a tiny app).
- **Requires good seams + tests** — without characterization (key 92) you can't verify the new path preserves behavior.
- **Organizational stamina** — incremental migrations lose funding/attention before completion; the half-strangled state accrues its own debt (key 59).

## 5. Current status
Strangler fig is the mainstream low-risk modernization pattern (Fowler 2004, widely applied); feature-flag-driven incremental cutover is current practice; pairs with OpenRewrite (key 94) for the code-level slices. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — figure-led; an illustrative routing-façade + feature-flag toggle for one migrated endpoint (reuses key 81 flag + key 92 characterization).
- **Figure:** Fig 93.1 — strangler progression: façade in front → new grows / old shrinks per slice → old removed; vs big-bang risk curve. Trace to Fowler bliki.

## 7. Gap-filling (verification queue)
- ⚠ Fowler StranglerFig bliki wording/date (2004) — confirm.
- ⚠ Any named case-study figures (Shopify/AWS) — cite precisely or keep general.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Fowler — StranglerFigApplication | martinfowler.com/bliki/StranglerFigApplication.html | ☑ pattern; ⚠ date |
| 2 | Feature flags (key 81); characterization (key 92) | (cross-ref) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | strangler fig 2026 | Fowler 2004; grow new over old; incremental; vs big-bang |

---
## Learnings & pipeline suggestions
- **Honest centerpiece:** the half-strangled stall risk + dual-running cost. **Cross-ref:** 91 (refactor in place), 92 (characterize the seam), 95 (version migration), 96 (playbook), 81 (flags), 50 (contract), 55/57 (architecture), 94 (code-level slices).
