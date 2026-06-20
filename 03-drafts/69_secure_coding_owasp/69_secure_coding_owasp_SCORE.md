# SCORECARD — Ch 30 "Secure coding & OWASP for Java" (key 69 + 72 + 74)

> Part VIII OPENER. Three merged dossiers (secure-coding/OWASP umbrella leads + injection/deser section +
> crypto-misuse section). Tier-A. Main-loop; gates = manual passes. security-is-a-quality-attribute +
> design-out-the-class-not-mitigate + OWASP-as-map-not-checklist + untrusted-data-as-trusted +
> parameterize-dont-concatenate + validation≠safety + misuse-not-broken-algorithms + tools-catch-patterns
> -not-design + edition-discipline + not-legal-advice shapes. Draft: `69_secure_coding_owasp_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (1 "beats" reworded); subject-not-comparison chapter (secure-coding concepts, no rival products crowned); tools named neutrally + routed; the design-out vs mitigate framing is a quality principle not a tool ranking; OWASP/ASVS/CWE cited as standards. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (Top-10-awareness-not-checklist; tools-catch-patterns-not-design/authz; validation≠parameterization; allow-list-mitigation-not-elimination; crypto-static-misuse-not-protocol + needs-expert; crypto-guidance-evolves + FPs; edition-drift; security≠all-of-quality + not-legal-advice) + the deep-dive design-flaws-tools-miss center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | OWASP Top 10:2025 anchored (2021 prior; exact categories ⚠ @pin per edition discipline); ASVS/CWE/Cheat Sheets; PreparedStatement/ObjectInputFilter(JEP 290)/SecureRandom/AES-GCM; crypto misuse checklist; all category-lists/JEP/EJ-item/rule-IDs/algorithm-recommendations carried ⚠ @pin + DATED; "not legal/pentest advice" stated repeatedly; post-quantum AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | vulnerable endpoint (concat-SQL + native-deser + AES/ECB+Random+MD5) → fixes (PreparedStatement + JSON/ObjectInputFilter + AES/GCM+SecureRandom+password-hash), FindSecBugs-flagged module spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the pristine-supply-chain-but-SQL-injection hook makes the inward turn vivid; the design-out-the-class method unifies 3 dossiers; the injection/deser + crypto-misuse tables land cleanly; an EDITION callout + four CONCEPT callouts (Top-10-not-checklist, eliminate-then-filter, tools-catch-patterns-not-design, +method) anchor it. |
| ACCURACY | 9 | OWASP/injection/deser/crypto atoms all sourced + edition-disciplined; design-out principle correct; −1 only for the broad verify-at-pin surface (2025 categories, JEP/EJ-item, rule-IDs, algorithm specifics) — all flagged + dated; validation≠parameterization + misuse-not-broken-algorithms stated precisely; not-legal-advice throughout. |
| UTILITY | 9 | directly actionable: design-out-the-class method, parameterize-don't-concatenate, avoid-native-deser/JSON-no-polymorphic, the crypto safe-defaults checklist, don't-roll-your-own, suppress-non-security-MD5-with-reason; the eliminate>mitigate hierarchy is a reusable security stance. |
| DEPTH | 9 | the design-out-the-class method + untrusted-data-as-trusted root-cause unification + eliminate-vs-mitigate (impossible-by-construction vs maintainable-control) + tools-catch-patterns-not-design is senior security material, tied back to make-bad-states-unrepresentable (Ch 5/8/9). |
| READABILITY | 8 | strong inward-turn hook, two checklists (injection/deser + crypto), five callouts, the design-out-then-detect synthesis; 4319w — right for a Tier-A 3-dossier Part-opener; clean secure-coding→SAST hand-off. |

**Aggregate 44/50**, none < 6 (Part-opener high, ties Ch 20/23). Floors A/B/C-source ✅; FLOOR-C COMPILE =
PENDING (toolchain READY). New shapes: design-out-the-class-not-mitigate + untrusted-data-as-trusted +
parameterize-dont-concatenate + validation≠safety + misuse-not-broken-algorithms + tools-catch-patterns
-not-design + not-legal-advice. **OPENS Part VIII (Security & SAST).** The inward turn from Part VII (your
deps) to your own code; design-out method echoes make-bad-states-unrepresentable (Ch 5/8/9). Hands off to
Ch 31 (SAST & secrets detection, keys 70+71). Reuses edition-discipline + folklore-guard + shift-left.
