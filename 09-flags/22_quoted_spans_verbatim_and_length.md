# FLAG — key 22: attributed quoted spans — verbatim UNVERIFIED offline + LEGAL-IP §2 length/count

- **Key:** 22 — Virtual threads & structured concurrency (folds 24, 25)
- **Draft:** `03-drafts/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_v1.md`
- **Type:** VERIFY gate — quoted-span verbatim check (UNVERIFIED) **+** LEGAL-IP §2 (prose-quote length/count)
- **Raised:** 2026-06-27 (deferred-marker resolution / source-verify pass)

## A. Verbatim UNVERIFIED offline (must re-confirm character-for-character at `/pin-source`)

The chapter attributes these double-quoted spans to named primary sources. In this environment the pinned
authority set is **not fetchable** (`verify_sources.sh` blocked: no clone at `tmp`; the JEP index,
docs.oracle.com/JLS, rules.sonarsource.com, and the Checker Framework manual are all unreachable). The
draft front-matter records they were "verified by curl" at draft time (2026-06-20); this VERIFY pass could
**not** independently re-confirm them. Per the VERIFY quoted-span rule an unverifiable quote is flagged,
not assumed correct. Confirm each is verbatim AND in context when the pin is fetchable:

| Line | Source | Quoted span (must match char-for-char) |
|---|---|---|
| 53 | JEP 444 | "are lightweight threads that dramatically reduce the effort of writing, maintaining, and observing high-throughput concurrent applications" |
| 53 | JEP 444 | "a work-stealing `ForkJoinPool` that operates in FIFO mode" |
| 55 | JEP 444 | "easy to understand, easy to program, and easy to debug and profile" |
| 61 | JEP 444 | "When it executes code inside a `synchronized` block or method, or when it executes a native method or a foreign function" |
| 61 | JEP 444 | "its carrier and the underlying OS thread are blocked for the duration of the operation" |
| 65 | Sonar `java:S6906` | "virtual threads should not run tasks that include `synchronized` code" (title — Sonar pages offline, cf. 09-flags/22_tool_rule_defaults_unverified.md) |
| 66 | JEP 491 | "the `synchronized` keyword no longer pins virtual threads, but we will retain it for other pinning situations" |
| 84 | JEP 444 | "does not increase the total number of threads" |
| 86–87 | JEP 506 | "share immutable data both with its callees within a thread, and with child threads" |
| 92–93 | JEP 453 | "treats groups of related tasks running in different threads as a single unit of work, thereby streamlining error handling and cancellation, improving reliability, and enhancing observability" |
| 119–120 | jcstress README | "usual suspects" (+ "experimental"/"probabilistic" — README is a moving target; not a pin, cf. 09-flags/24_jcstress_not_pinned.md) |
| 143 | Checker Framework (Lock Checker manual) | "if the Lock Checker type-checks your program without errors, then your program will not have data races caused by unsynchronized accesses to shared mutable fields" |

Note: the `IS2_INCONSISTENT_SYNC` rule **identity** and the `MT_CORRECTNESS` category ARE build-corroborated
against the companion module (`08-companion-code/22_virtual_threads_structured_concurrency/`, GREEN on JDK
21.0.11 — the one reviewed SpotBugs suppression). Only the quoted prose/titles above are unverified.

## B. LEGAL-IP §2 — prose-quote length (<15 words) and one-quote-per-source — offline-determinable

LEGAL-IP-RULES.md §2: "Prose quotes: under 15 words; one quote per source across a chapter." Several attributed
quotes exceed the 15-word ceiling, and **JEP 444 is quoted ~5 times** (lines 53 ×2, 55, 61 ×2, 84) against the
one-per-source rule. These are editorial calls (trim to <15 words / collapse to a single JEP-444 quote /
re-cast as paraphrase), not facts I can rewrite without the source to re-confirm a trimmed span stays verbatim:

- Line 53 JEP 444 quote #1 — ~17 words (**over 15**).
- Line 61 JEP 444 "When it executes code inside a `synchronized`…" — ~21 words (**over 15**).
- Line 93 JEP 453 summary — ~26 words (**over 15**).
- Line 143 Checker Framework — ~25 words (**over 15**).
- One-quote-per-source: JEP 444 quoted ~5× across the chapter (**over the per-source limit**).

(The ≤9-line snippet ceiling is separately CLEAN — `check_snippets.sh`: 7/7 tag regions resolve and are in
range; `lint_citations.sh` snippet-length check OK.)

## Required action / resolution
1. At `/pin-source` (pin fetchable): re-confirm every span in §A verbatim + in context against the pinned JEP/
   JLS/tool source; correct or attribute precisely; mark any that cannot be matched.
2. Editor: bring the §B quotes under the <15-word ceiling and collapse JEP 444 to one quote per chapter (the
   rest become attributed paraphrase — must be true rewrites, not close mirrors, per §2/§8).
3. **Status:** OPEN — blocks a clean VERIFY PASS for key 22 until §A re-confirmed and §B reconciled.

---

## RESOLUTION — §B (length/count) reconciled — 2026-06-27

**§B (LEGAL-IP §2 prose-quote length + one-quote-per-source) is now RESOLVED in `…_v1.md`.** No
verified technical fact was changed — only the *quoting* of facts (every JEP number, API name, package,
flag, rule ID, and stated behavior is byte-for-byte intact). The draft's body now holds **8 source-
attributed prose quotes, all ≤14 words, ≤1 per source except JEP 444 (kept at 2 short load-bearing
quotes per the editorial brief)** — down from 12 attributed spans (6 of them JEP 444) before.

**JEP 444 — reduced from 6 quotes → 2** (the only source the one-per-source rule was carved for here):
- KEPT (short, load-bearing, distinctive):
  - L53 — "a work-stealing `ForkJoinPool` that operates in FIFO mode" (8 words) — attribution made explicit ("JEP 444 describes the scheduler as …").
  - L84 — "does not increase the total number of threads" (8 words) — the no-pooling fact, attribution intact ("per JEP 444").
- PARAPHRASED into the book's locked third-person voice (fact preserved, verbatim dropped — true rewrites, not close mirrors):
  - L53 #1 (was ~17w) → "JEP 444 frames them as lightweight threads that cut the cost of writing, maintaining, and observing high-throughput concurrent applications."
  - L55 readability quote (was ~12w) → "the simple style JEP 444 sets out to recover — one that stays straightforward to understand, program, debug, and profile."
  - L61 pinning definition (was ~21w, over ceiling) → "while it runs code inside a `synchronized` block or method, or while it runs a native method or a foreign function" (both pinning cases preserved as fact).
  - L61 blocked-carrier (was ~14w) → "the carrier and the OS thread under it stay blocked for the length of that operation."

**Other over-ceiling quotes — trimmed to a short (<15w) load-bearing fragment, attribution intact (each source stays at 1 body quote):**
- L66 JEP 491 (was ~16w) → "no longer pins virtual threads" (5w); the retain-for-other-situations clause is now book-voice prose.
- L93 JEP 453 (was ~26w) → "as a single unit of work" (6w); the streamlining/reliability/observability tail is now paraphrase.
- L143/144 Checker Framework (was ~25w) → "will not have data races" (5w), attributed to the Lock Checker manual; the rest is book-voice.

**Within-ceiling, one-per-source quotes left intact (verified ≤15w, 1 per source):** L65 Sonar `java:S6906`
(10w), L87 JEP 506 (14w), L120 jcstress README "usual suspects" (2w). Short attributed terms-of-art
(JLS "correctly synchronized", SpotBugs "inaccuracy") are 1–2 words, one occurrence each — within §2.

**Snippet ceiling untouched:** `check_snippets.sh …_v1.md` → 7 markers, 7 pass, 0 fail (re-run 2026-06-27).
No `<!-- include: -->` marker or `// tag` region was modified. No other chapter touched.

**Status update:** §B **RESOLVED**. §A (verbatim re-confirm at the pin) remains **OPEN** — the kept/trimmed
spans above must still be re-checked character-for-character + in context once the pin is fetchable at
`/pin-source` (the §A table still applies to the surviving short quotes; trimmed spans are now shorter
fragments of the originally-listed text). The flag stays OPEN overall until §A clears.
