# FLAG — key 31 (NullAway): overhead figures + JSpecify-mode status

**Status:** ◐ PARTIALLY RESOLVED — FSE'19 atoms VERIFIED & RESOLVED (2026-06-28); Checker Framework soundness-sentence quote VERIFIED VERBATIM & RESOLVED (2026-06-28, CF Manual v4.2.0 §3.1). Remaining: NullAway README "<10%" overhead re-quote + JSpecify-mode wiki status — both ⚠ verify-at-pin (pin-deferred, not verbatim-quote ACCURACY caps). No remaining quoted-verbatim atom is unverified.

## ✅ RESOLVED — FSE'19 overhead figures + breakdown + quote (web-verified 2026-06-28)

Source: Banerjee, Clapp, Sridharan, "NullAway: Practical Type-Based Null Safety for Java,"
ESEC/FSE 2019 — **arXiv:1907.02127**, https://arxiv.org/abs/1907.02127. Verified two ways:
(a) WebFetch of the arXiv abstract; (b) direct text extraction of the PDF body (§8.2 + Figure 4).

- **Build-time overhead** — CONFIRMED VERBATIM, §8.2: *"On average, NullAway's build times are only
  1.15× those of standard builds, compared to 2.8× for Eradicate and 5.1× for CFNullness."* Figure 4
  mean normalized compile times: **NullAway 1.15 · Eradicate 2.83 · CFNullness 5.08**. So the per-tool
  split is correct: **Eradicate ≈ 2.8×**, **CFNullness (Checker Framework Nullness Checker) ≈ 5.1×**.
  Tool versions used: CFNullness v.2.8.1, Infer v.0.15.0 (Eradicate), Java JDK 8.
- **Production NPE breakdown + the quote** — CONFIRMED VERBATIM (abstract / §8.3): *"…remaining NPEs
  were due to unchecked third-party libraries (64%), deliberate error suppressions (17%), or reflection
  and other forms of post-checking code modification (17%), **never due to NullAway's unsound
  assumptions for checked code.**"* The 64% / 17% / 17% split and the quoted span are exact.

Draft actions (03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md): figures stated with a
proper footnote citation to arXiv:1907.02127 §8.2/Fig.4; per-tool attribution (Eradicate 2.8×,
CFNullness 5.1×) added; breakdown reworded to the paper's exact category phrasing; all
@pin/UNVERIFIED markers for these atoms removed from the prose (§"Deep dive"), the reference list, and
the header source/residual lines.

## ⚠ STILL OPEN — re-quote byte-exact at the pinned source

- **NullAway README overhead claim:** *"the build-time overhead of running NullAway is usually less than
  10%"* — re-quote from the pinned NullAway README tag (separate from the FSE'19 1.15× figure above).
- **JSpecify mode** (`-XepOpt:NullAway:JSpecifyMode=true`): wiki status *"work-in-progress and
  experimental … may report false positive warnings"* + the generics gap list (jspecify/jdk
  annotations, generic-method inference, wildcards, generic-class validation) — re-confirm at the pinned
  wiki tag (fastest-moving surface).
- ~~**Checker Framework soundness sentence**~~ — ✅ **RESOLVED (web-verified verbatim 2026-06-28).** It is a
  *different* source from the FSE'19 paper (the CF manual, NOT arXiv:1907.02127). The guarantee sentence
  quoted in the draft — *"If the Nullness Checker type-checks your program without errors, then your program
  will not crash with a NullPointerException that is caused by misuse of null in checked code."* — was
  confirmed **byte-exact** against **The Checker Framework Manual, version 4.2.0 (2026-06-01), §3.1 "What the
  Nullness Checker guarantees"** (Nullness Checker chapter), https://checkerframework.org/manual/#nullness-checker
  (multiple independent fetches agree). The companion §3.1 opener — *"If the Nullness Checker issues no
  warnings for a given program, then running that program will never throw a null pointer exception."* — was
  also confirmed present; the draft keeps the more precise "checked code" sentence as it matches the chapter's
  honest-limits framing. Draft now carries a footnote citation (manual §3.1 + URL + v4.2.0); the reference-list
  entry and both header lines have the ⚠ verify-at-pin marker removed. No quote-mark change needed (the verbatim
  quote stands, confirmed).

## NEUTRALITY note (unchanged)
The per-tool comparative figures (Eradicate 2.8×, CFNullness 5.1×) are cited to NullAway's **own**
FSE'19 paper, never to a rival's docs/marketing. The rival's own strongest case + cost belong to key 32
(annotation ecosystem) / key 37 (cross-tool verdict). Fig 31.2 plots the trade-off axis, no winner crowned.
